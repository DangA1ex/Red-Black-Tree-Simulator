package application;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import hw4.LinkedStack;

/**
 * handles creation and methods for a red black tree
 * 
 * @author Alex
 *
 * @param <E> generic data type
 */
public class RedBlackTree<E extends Comparable<E>> {
	protected RBNode<E> root;
	protected RBNode<E> nil = new RBNode<E>();

	/**
	 * default constructor
	 */
	public RedBlackTree() {
		this.root = nil;
	}

	/**
	 * constructs red black tree using array
	 * 
	 * @param rb
	 *            array of values
	 */
	public RedBlackTree(E[] rb) {
		for (int i = 0; i < rb.length; i++) {
			insert(rb[i]);
		}
	}

	/**
	 * adds data to the red black tree
	 * 
	 * @param data
	 *            value to add
	 */
	public void insert(E data) {
		RBNode<E> newNode = new RBNode<E>(data, "R");

		if (root == null || root.equals(nil)) {
			root = newNode;
			root.left = nil;
			root.right = nil;
		} else {
			RBNode<E> point = insertionPoint(data);

			if (data.compareTo(point.getData()) < 0) {
				point.left = newNode;
				point.left.left = nil;
				point.left.right = nil;
				point.left.parent = point;
			} else if (data.compareTo(point.getData()) > 0) {
				point.right = newNode;
				point.right.right = nil;
				point.right.left = nil;
				point.right.parent = point;
			}
		}

		insertionCleanup(newNode);
	}

	/**
	 * finds position to add node
	 * 
	 * @param data
	 *            value to be added
	 * @return position
	 */
	private RBNode<E> insertionPoint(E data) {
		RBNode<E> current = root;
		RBNode<E> point = null;

		while (!current.equals(nil)) {
			if (data.equals(current.getData())) {
				break;
			} else if (data.compareTo(current.getData()) < 0) {
				point = current;
				current = current.left;
			} else if (data.compareTo(current.getData()) > 0) {
				point = current;
				current = current.right;
			}
		}

		return point;
	}

	/**
	 * fixes violations when adding to tree
	 * 
	 * @param node
	 *            node to be checked
	 */
	private void insertionCleanup(RBNode<E> node) {
		while (node != root && node.parent.color == "R") {
			if (isLeftChild(node.parent)) {
				RBNode<E> uncle = node.parent.parent.right;
				if (uncle.color == "R") {
					node.parent.color = "B";
					uncle.color = "B";
					grandparent(node).color = "R";
					node = grandparent(node);
				} else {
					if (isRightChild(node)) {
						node = node.parent;
						leftRotate(node);
					}
					node.parent.color = "B";
					grandparent(node).color = "R";
					rightRotate(grandparent(node));
				}
			} else {
				RBNode<E> uncle = node.parent.parent.left;
				if (uncle.color == "R") {
					node.parent.color = "B";
					uncle.color = "B";
					grandparent(node).color = "R";
					node = grandparent(node);
				} else {
					if (isLeftChild(node)) {
						node = node.parent;
						rightRotate(node);
					}
					node.parent.color = "B";
					grandparent(node).color = "R";
					leftRotate(grandparent(node));
				}
			}

			// if (uncle(node).color == "R") {
			// node.parent.color = "B";
			// uncle(node).color = "B";
			// grandparent(node).color = "R";
			// } else if (uncle(node).color == "B") {
			// if (isRightChild(node) && isLeftChild(node.parent)) {
			// node = node.parent;
			// leftRotate(node);
			// node.parent.color = "B";
			// grandparent(node).color = "R";
			// rightRotate(grandparent(node));
			// } else if (isLeftChild(node) && isRightChild(node.parent)) {
			// node = node.parent;
			// rightRotate(node);
			// node.parent.color = "B";
			// grandparent(node).color = "R";
			// leftRotate(grandparent(node));
			// }
			// }
		}
		root.color = "B";
	}

	/**
	 * rotates section of the tree left
	 * 
	 * @param node
	 *            pivot
	 */
	private void leftRotate(RBNode<E> node) {
		RBNode<E> temp = node.right;

		node.right = temp.left;

		if (!node.left.equals(nil)) {
			node.left.parent = node;
		}

		temp.parent = node.parent;

		if (node.parent == null) {
			root = temp;
		} else if (node.equals(node.parent.left)) {
			node.parent.left = temp;
		} else {
			node.parent.right = temp;
		}

		temp.left = node;
		node.parent = temp;
	}

	/**
	 * rotates section of tree right
	 * 
	 * @param node
	 *            pivot
	 */
	private void rightRotate(RBNode<E> node) {
		RBNode<E> temp = node.left;

		node.left = temp.right;

		if (!node.left.equals(nil)) {
			node.right.parent = node;
		}

		temp.parent = node.parent;

		if (node.parent == null) {
			root = temp;
		} else if (node.equals(node.parent.right)) {
			node.parent.right = temp;
		} else {
			node.parent.left = temp;
		}

		temp.right = node;
		node.parent = temp;
	}

	/**
	 * deletes value from tree "helper"
	 * 
	 * @param data
	 *            value to delete
	 */
	public void delete(E data) {
		delete(nodeToDelete(data));
	}

	/**
	 * deletes value from tree
	 * 
	 * @param node
	 *            node to be deleted
	 */
	private void delete(RBNode<E> node) {
		if (isLeaf(node)) {
			if (isLeftChild(node)) {
				node.parent.left = nil;
			} else if (isRightChild(node)) {
				node.parent.right = nil;
			}
			nil.parent = node.parent;

			if (node.color == "B") {
				nil.color = "DB";
				fixDoubleBlack(nil);
			}
		} else if (numChildren(node) == 1) {
			if (isLeftChild(node)) {
				if (!node.left.equals(nil)) {
					node.parent.left = node.left;
					node.left.parent = node.parent;
				} else if (!node.right.equals(nil)) {
					node.parent.left = node.right;
					node.right.parent = node.parent;
				}

				if (node.left.color == "R" || node.color == "R") {
					node.left.color = "B";
				} else if (node.left.color == "B" && node.color == "B") {
					node.left.color = "DB";
					fixDoubleBlack(node.left);
				}
			} else if (isRightChild(node)) {
				if (!node.left.equals(nil)) {
					node.parent.right = node.left;
					node.left.parent = node.parent;
				} else if (!node.right.equals(nil)) {
					node.parent.right = node.right;
					node.right.parent = node.parent;
				}

				if (node.right.color == "R" || node.color == "R") {
					node.right.color = "B";
				} else if (node.right.color == "B" && node.color == "B") {
					node.right.color = "DB";
					fixDoubleBlack(node.right);
				}
			}
		} else if (numChildren(node) == 2) {
			RBNode<E> max = maxLeftSub(node);
			node.setData(max.getData());
			delete(max);
		}
	}

	/**
	 * fixes violations when deleting
	 * 
	 * @param node
	 *            node to check
	 */
	private void fixDoubleBlack(RBNode<E> node) {
		if (sibling(node).color == "R") {
			if (isRightChild(node)) {
				sibling(node).color = "B";
				node.parent.color = "R";
				rightRotate(node.parent);
				fixDoubleBlack(node);
			} else {
				sibling(node).color = "B";
				node.parent.color = "R";
				leftRotate(node.parent);
				fixDoubleBlack(node);
			}
		}
		if (!sibling(node).equals(nil)) {
			if (sibling(node).left.color == "R" || sibling(node).right.color == "R") {
				if (isLeftChild(sibling(node))) {
					if (sibling(node).right.color == "R") {
						leftRotate(sibling(node));
						rightRotate(node.parent);
						sibling(node).right.color = node.parent.color;
						sibling(node).color = "B";
						node.parent.color = "B";
						node.color = "B";
					} else if (sibling(node).left.color == "R") {
						rightRotate(node.parent);
						sibling(node).color = node.parent.color;
						sibling(node).left.color = "B";
						node.parent.color = "B";
						node.color = "B";
					}
				} else if (isRightChild(sibling(node))) {
					if (sibling(node).left.color == "R") {
						rightRotate(sibling(node));
						leftRotate(node.parent);
						sibling(node).left.color = node.parent.color;
						sibling(node).color = "B";
						node.parent.color = "B";
						node.color = "B";
					} else if (sibling(node).right.color == "R") {
						leftRotate(node.parent);
						sibling(node).color = node.parent.color;
						sibling(node).right.color = "B";
						node.parent.color = "B";
						node.color = "B";
					}
				}
			}
		}

		if (sibling(node).color == "B" && sibling(node).left.color == "B" && sibling(node).right.color == "B") {
			if (node.parent.color == "R") {
				sibling(node).color = "R";
				node.parent.color = "B";
				node.color = "B";
			} else {
				sibling(node).color = "R";
				node.parent.color = "DB";
				node.color = "B";
				fixDoubleBlack(node.parent);
			}
		}
		root.color = "B";

	}

	/**
	 * finds the maximum value of the left sub tree
	 * 
	 * @param node
	 *            node to traverse from
	 * @return max value in left sub tree
	 */
	private RBNode<E> maxLeftSub(RBNode<E> node) {
		RBNode<E> max = node.left;
		RBNode<E> temp = node;

		while (!max.right.equals(nil)) {
			temp = max;
			max = max.right;
		}

		return max;
	}

	/**
	 * finds place on tree to delete node
	 * 
	 * @param data
	 *            value to be deleted
	 * @return position
	 */
	private RBNode<E> nodeToDelete(E data) {
		RBNode<E> current = root;

		while (current != null) {
			if (data.equals(current.getData())) {
				return current;
			} else if (data.compareTo(current.getData()) < 0) {
				current = current.left;
			} else if (data.compareTo(current.getData()) > 0) {
				current = current.right;
			}
		}
		return null;
	}

	/**
	 * checks number of children a node has
	 * 
	 * @param node
	 *            node to be checked
	 * @return number of children
	 */
	private int numChildren(RBNode<E> node) {
		int count = 0;

		if (!node.left.equals(nil)) {
			count++;
		}
		if (!node.right.equals(nil)) {
			count++;
		}
		return count;
	}

	/**
	 * checks if value is in tree
	 * 
	 * @param data
	 *            value to be checked
	 * @return boolean if value is present
	 */
	public boolean find(E data) {
		RBNode<E> current = root;

		while (!current.equals(nil)) {
			if (data.equals(current.getData())) {
				return true;
			} else if (data.compareTo(current.getData()) < 0) {
				current = current.left;
			} else if (data.compareTo(current.getData()) > 0) {
				current = current.right;
			}
		}

		return false;
	}

	/**
	 * checks if tree is empty
	 * 
	 * @return boolean
	 */
	public boolean isEmpty() {
		return root.equals(nil);
	}

	/**
	 * checks if node is a leaf
	 * 
	 * @param node
	 *            node to be checked
	 * @return boolean
	 */
	public boolean isLeaf(RBNode<E> node) {
		return node.left.equals(nil) && node.right.equals(nil);
	}

	/**
	 * checks if node is left child
	 * 
	 * @param node
	 *            node to be checked
	 * @return boolean
	 */
	public boolean isLeftChild(RBNode<E> node) {
		if (node.equals(root)) {
			return false;
		}
		return node.parent.left == node;
	}

	/**
	 * checks if node is right child
	 * 
	 * @param node
	 *            node to be checked
	 * @return boolean
	 */
	public boolean isRightChild(RBNode<E> node) {
		if (node.equals(root)) {
			return false;
		}
		return node.parent.right == node;
	}

	/**
	 * looks for node's sibling
	 * 
	 * @param node
	 *            node to check
	 * @return sibling of node
	 */
	public RBNode<E> sibling(RBNode<E> node) {
		if (node.equals(root)) {
			return null;
		}
		if (isLeftChild(node)) {
			return node.parent.right;
		} else if (isRightChild(node)) {
			return node.parent.left;
		} else {
			return null;
		}
	}

	/**
	 * looks for uncle of node
	 * 
	 * @param node
	 *            node to be checked
	 * @return uncle of node
	 */
	public RBNode<E> uncle(RBNode<E> node) {
		if (node.equals(root)) {
			return null;
		}
		if (isRightChild(node.parent)) {
			if (node.parent.parent.left != null) {
				return node.parent.parent.left;
			} else {
				return nil;
			}
		} else if (isLeftChild(node.parent)) {
			if (node.parent.parent.right != null) {
				return node.parent.parent.right;
			} else {
				return nil;
			}
		} else {
			return nil;
		}
	}

	/**
	 * looks for grandparent of node
	 * 
	 * @param node
	 *            node to be checked
	 * @return grandparent of node
	 */
	public RBNode<E> grandparent(RBNode<E> node) {
		if (node.equals(root)) {
			return null;
		}
		if (node.parent != root) {
			return node.parent.parent;
		} else {
			return null;
		}
	}

	/**
	 * creates a list of node using preorder traversal
	 * 
	 * @return list of nodes
	 */
	public ArrayList<RBNode<E>> preorder() {
		ArrayList<RBNode<E>> sorted = new ArrayList<RBNode<E>>();

		if (root == null) {

		} else {
			RBNode<E> current;
			LinkedStack<RBNode<E>> stack = new LinkedStack<RBNode<E>>();

			stack.push(root);

			while (!stack.isEmpty()) {
				current = stack.pop();
				sorted.add(current);

				if (current.right != null) {
					stack.push(current.right);
				}
				if (current.left != null) {
					stack.push(current.left);
				}
			}
		}
		return sorted;
	}

	/**
	 * creates a list of node using inorder traversal
	 * 
	 * @return list of nodes
	 */
	public ArrayList<RBNode<E>> inorder() {
		ArrayList<RBNode<E>> sorted = new ArrayList<RBNode<E>>();

		if (root == null) {
			System.out.println("Tree is empty");
		} else {
			RBNode<E> current = root;
			LinkedStack<RBNode<E>> stack = new LinkedStack<RBNode<E>>();

			while (!stack.isEmpty() || current != null) {
				if (current != null) {
					stack.push(current);
					current = current.left;
				} else {
					current = stack.pop();
					sorted.add(current);
					current = current.right;
				}
			}
		}
		return sorted;
	}

	/**
	 * creates a list of node using postorder traversal
	 * 
	 * @return list of nodes
	 */
	public ArrayList<RBNode<E>> postorder() {
		ArrayList<RBNode<E>> sorted = new ArrayList<RBNode<E>>();

		if (root == null) {
			System.out.println("Tree is empty");
		} else {
			RBNode<E> current = root;
			LinkedStack<RBNode<E>> stack1 = new LinkedStack<RBNode<E>>();
			LinkedStack<RBNode<E>> stack2 = new LinkedStack<RBNode<E>>();

			stack1.push(root);

			while (!stack1.isEmpty()) {
				current = stack1.pop();
				stack2.push(current);

				if (current.left != null) {
					stack1.push(current.left);
				}
				if (current.right != null) {
					stack1.push((current.right));
				}
			}

			while (!stack2.isEmpty()) {
				current = stack2.pop();
				sorted.add(current);
			}
		}
		return sorted;
	}

	/**
	 * creates a list of node using breadthfirst traversal
	 * 
	 * @return list of nodes
	 */
	public ArrayList<RBNode<E>> breadthfirst() {
		ArrayList<RBNode<E>> sorted = new ArrayList<RBNode<E>>();

		if (root == null) {
			System.out.println("Tree is empty");
		} else {
			RBNode<E> current = root;
			Queue<RBNode<E>> que = new LinkedList<RBNode<E>>();

			que.add(root);

			while (!que.isEmpty()) {
				current = que.remove();
				sorted.add(current);
				if (current.left != null) {
					que.add(current.left);
				}
				if (current.right != null) {
					que.add(current.right);
				}
			}
		}
		return sorted;
	}

	public void printTree() {

		if (this.root.right != null) {
			this.printTree(this.root.right, true, "");
		}

		printNodeValue(this.root);

		if (this.root.left != null) {
			this.printTree(this.root.left, false, "");
		}
	}

	private void printTree(RBNode<E> node, boolean isRight, String indent) {
		if (node.right != null) {
			printTree(node.right, true, indent + (isRight ? "        " : " |      "));
		}

		System.out.print(indent);

		if (isRight) {
			System.out.print(" /");
		} else {
			System.out.print(" \\");
		}
		System.out.print("----- ");
		printNodeValue(node);
		if (node.left != null) {
			printTree(node.left, false, indent + (isRight ? " |      " : "        "));
		}
	}

	private void printNodeValue(RBNode<E> node) {
		if (node == null) {
			System.out.print("<null>");
		} else {
			System.out.print(node.getData() + node.color);
		}
		System.out.println();
	}
}
