import java.util.Iterator;
public class LStack<T> implements IStack<T>
{
    private Link top;
    private int size;
    public LStack()
    {
        top = null;
        size = 0;
    }
    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        for(Link nav = top; nav != null; nav=nav.getNext())
        {
            sb.append(nav.getDatum());
            if(nav.getNext() != null)
            {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
    public void push(T newItem)
    {
        top = new Link(newItem, top);
        size++;
    }
    public T pop()
    {
        if(isEmpty())
        {
            throw new EmptyStackException();
        }
        size--;
        T out = top.getDatum();
        top.exsanguinate();  //kill top's datum so gc will get it
        top = top.getNext();
        return out;
    }
    public T peek()
    {
        if(isEmpty())
        {
            throw new EmptyStackException();
        }
        return top.getDatum();
    }
    public void clear()
    {
        top = null;
        size = 0;
    }
    public int size()
    {
        return size;
    }
    public boolean isEmpty()
    {
        return top == null;
    }
    public Iterator<T> iterator()
    {
        return new LStackIterator();
    }
    /********************aliens*******************/
    class LStackIterator implements Iterator<T>
    {
        private Link current;
        public LStackIterator()
        {
            current = top;
        }
        public T next()
        {
            T out = current.getDatum();
            current = current.getNext();
            return out;
        }
        public boolean hasNext()
        {
            return current != null;
        }

    }
    public static void main(String[] args)
    {
        LStack<String> elStack = new LStack<>();
        elStack.push("A");
        elStack.push("B");
        elStack.push("C");
        elStack.push("D");
        for(String quack: elStack)
        {
            System.out.println(quack);
        }
        System.out.printf("The size is %d\n", elStack.size());
        System.out.println(elStack);
        System.out.println(elStack.pop());
        System.out.println(elStack.pop());
        System.out.println(elStack.pop());
        System.out.println(elStack.pop());
        System.out.println(elStack);
    }
    /*********************Aliens*******************/
    class Link
    {
        private T datum;
        private Link next;
        public Link(T datum, Link next)
        {
            this.datum = datum;
            this.next = next;
        }
        public Link(T datum)
        {
            this(datum, null);
        }
        public T getDatum()
        {
            return datum;
        }
        public Link getNext()
        {
            return next;
        }
        public void exsanguinate()
        {
            top.datum = null;
        }
    }
}
