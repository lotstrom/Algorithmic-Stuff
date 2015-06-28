package M6;

public interface PrioQueue<E> {
	
	public void add(E e);
	public E removeMin();
	public int size();
}
