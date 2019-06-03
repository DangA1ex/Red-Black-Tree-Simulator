package application;

/**
 * handles creating red black nodes
 * 
 * @author Alex
 *
 * @param <E>
 *            generic data type
 */
public class RBNode<E extends Comparable<E>> implements Comparable<E> {
	private E data;
	protected String color;
	protected RBNode<E> parent;
	protected RBNode<E> left;
	protected RBNode<E> right;

	/**
	 * default constructor
	 */
	public RBNode() {
		this.data = null;
		this.color = "B";
	}

	/**
	 * constructs node using a value and a color
	 * 
	 * @param data
	 *            value
	 * @param color
	 *            color of node
	 */
	public RBNode(E data, String color) {
		this.data = data;
		this.color = color;
	}

	/**
	 * getter for data
	 * 
	 * @return value of node
	 */
	public E getData() {
		return data;
	}

	/**
	 * sets value of node
	 * 
	 * @param data
	 *            value of node
	 */
	public void setData(E data) {
		this.data = data;
	}

	@Override
	public int compareTo(E e) {
		return data.compareTo(e);
	}

	public int compareTo(RBNode<E> node) {
		return data.compareTo(node.getData());
	}
}
