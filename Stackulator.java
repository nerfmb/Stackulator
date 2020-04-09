import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

public class Stackulator extends Application
{
    private Text display;
    private LStack<Double> theStack;  //added this
    private Text stackDisplay;
    private boolean showingResult;
    private boolean radians;
    public Stackulator()
    {
        display = new Text();
        theStack = new LStack<Double>();
        stackDisplay = new Text();
        stackDisplay.setText("[]");
        showingResult = false;
        radians = true;
    }
    @Override
    public void start(Stage primary)
    {
        HBox top = new HBox();
        top.getChildren().add(display);
        BorderPane bp = new BorderPane();
        GridPane left = new GridPane();
        GridPane right = new GridPane();
        GridPane container = new GridPane();
        bp.setCenter(container);
        bp.setBottom(stackDisplay);
        container.add(left, 0,0);   //add left
        container.add(right, 1, 0);   //add right
        //make the number buttons
        NumberButton[] numbers = new NumberButton[10];
        for(int k = 0; k < 10; k++)
        {
            numbers[k] = new NumberButton("" + k);
        }
        NumberButton point = new NumberButton(".");
        NumberButton exponent = new NumberButton("E");
        //install them on the left
        //7 8 9
        left.add(numbers[7], 0, 0);
        left.add(numbers[8], 1, 0);
        left.add(numbers[9], 2, 0);
        left.add(numbers[4], 0, 1);
        left.add(numbers[5], 1, 1);
        left.add(numbers[6], 2, 1);
        left.add(numbers[1], 0, 2);
        left.add(numbers[2], 1, 2);
        left.add(numbers[3], 2, 2);
        left.add(point, 0, 3);
        left.add(numbers[0], 1, 3);
        left.add(exponent, 2, 3);
        /*  This is better; try it
		int i = 0;
		char[] b = "789456123.0E".toCharArray();
;â€¨      for (char c : b ) 
		{ 
			left.add(new NumberButton("" + c), i % 3, i / 3);
			i++;
		}

        */
        Button enter = new Button("ENTER");
        enter.setOnAction( e ->
        {
            double d = getDisplayValue();
            if(!Double.isNaN(d))
            {    
                theStack.push(d);
                stackDisplay.setText(theStack.toString());
                System.out.println(theStack);
                display.setText("");
            }    
            else
            {
                display.setText("ERROR");
            }
        });      
        right.add(enter, 0, 0);
        Button clear = new Button("C");
        clear.setOnAction( e ->
        { 
            //reset the calculator
            theStack.clear();
            display.setText("");
            stackDisplay.setText(theStack.toString());
        });
        Button clearEntry = new Button("CE");
        clearEntry.setOnAction( e ->
        {
            display.setText("");
        });
        Button backspace = new Button("<-");
        backspace.setOnAction( e ->
        {
            if(display.getText().length() > 0)
            {
                int len = display.getText().length();
                display.setText(display.getText().substring(0, len - 1));
                showingResult = false;
            }
        });
        //add     in the op buttons.
        OpButton plus = new OpButton("+", (x, y) -> x + y);
        OpButton minus = new OpButton("-", (x, y) -> x - y);
        OpButton times = new OpButton("*", (x, y) -> x*y);
        OpButton divide = new OpButton("/", (x, y) -> x/y);
        OpButton pow = new OpButton("**", (x,y) -> Math.pow(x,y));
        right.add(plus, 0, 1);
        right.add(minus, 0, 2);
        right.add(times, 0, 3);
        right.add(divide, 0, 4);
        right.add(pow, 0, 5);
        right.add(clear, 1,1);
        right.add(clearEntry, 1,2);
        right.add(backspace, 1,3);
        FunctionButton log = new FunctionButton("log", x -> Math.log(x));         
        FunctionButton log10 = new FunctionButton("log10", x -> Math.log10(x));
        FunctionButton sqrt = new FunctionButton("sqrt", x -> Math.sqrt(x));         
        FunctionButton chs = new FunctionButton("(-)", x -> -x );         
        //oh bring me some triggie pudding
        Button degreeRad = new Button("-> deg");
        degreeRad.setOnAction( e ->
        {
            radians = !radians;
            String text = radians? "-> deg": "-> rad";
            degreeRad.setText(text);
            
         });
        FunctionButton sin = new FunctionButton("sin", x ->
        {
             return radians? Math.sin(x): Math.sin(Math.toRadians(x));
        });
        FunctionButton asin = new FunctionButton("sin<sup>-1</sup>", x ->
        {
             return radians? Math.asin(x): Math.toDegrees(Math.asin(x));
        });

        FunctionButton cos = new FunctionButton("cosin", x ->
        {
             return radians? Math.cos(x): Math.cos(Math.toRadians(x));
        });
        FunctionButton acos = new FunctionButton("cosin<sup>-1</sup>", x ->
        {
             return radians? Math.acos(x): Math.toDegrees(Math.acos(x));
        });
        FunctionButton tan = new FunctionButton("tan", x ->
        {
             return radians? Math.tan(x): Math.tan(Math.toRadians(x));
        });
        FunctionButton atan = new FunctionButton("tan<sup>-1</sup>", x ->
        {
             return radians? Math.atan(x): Math.toDegrees(Math.atan(x));
        });
        right.add(log, 2,0);
        right.add(log10, 2,1);
        right.add(sqrt, 2,2);
        right.add(chs, 2,3);
        right.add(degreeRad, 2,4);
        right.add(sin, 2,5);
        right.add(asin, 3,0);
        right.add(cos, 2,6);
        right.add(acos, 3,1);
        right.add(tan, 2,7);
        right.add(atan, 3,2);

        bp.setTop(top);
        primary.setScene(new Scene(bp, 800, 500));
        primary.show();
    }
    private double getDisplayValue()
    {
        String s = display.getText();
        double d = 0;
        try
        {
            d = Double.parseDouble(s);
            display.setText("");
        }
        catch(NumberFormatException ex)
        {
            //quack  TODO: handle this properly
            d = Double.NaN;
            //ex.printStackTrace();
        }
        return d;
           
    }
    class NumberButton extends Button
    {
        final String symbol;
        public NumberButton(String symbol)
        {
            super(symbol);
            this.symbol = symbol;
            setOnAction( e ->
            {
                //append number to display
                if(showingResult)
                {
                    double d = getDisplayValue();
                    if(!Double.isNaN(d))
                    {
                        theStack.push(d);
                    }
                    display.setText("");
                }
                display.setText(display.getText() + symbol);
                showingResult = false;
            });
        }
    }
	class OpButton extends Button
	{
        private final DoubleBinaryOperator op;
		public OpButton(String symbol, DoubleBinaryOperator op)
		{
            super(symbol);
            this.op = op;
            setOnAction( e ->
            {
                String s = display.getText();
                double first = theStack.pop();
                double second = Double.parseDouble(s);
                double toDisplay = op.applyAsDouble(first, second);
                display.setText("" + toDisplay);
                stackDisplay.setText(theStack.toString());
                showingResult = true;
                //do yer thang
            });
		}
	}
    class FunctionButton extends Button
    {
        DoubleUnaryOperator function;
        public FunctionButton(String symbol, DoubleUnaryOperator function)
        {
            super(symbol);
            this.function = function;
            setOnAction( e ->
            {
                double d = getDisplayValue();
                if(! Double.isNaN(d))
                {
                    display.setText("" + function.applyAsDouble(d) );
                    showingResult = true;
                }
                else if(display.getText().length() > 0)
                {
                    display.setText("ERROR");
                    showingResult = true;
                }
            });
        }
    }
    public static void main(String[] args)
    {
        Application.launch(args);
    }

}
/* TODO
 * featurezilla:
 *    what should we do in the event someone just pushes
 *    an op key and the display is empty and the stack
 *    is empty
 */
