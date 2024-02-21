package p2.DataStructures.SortedList;

import java.util.Comparator;
import java.util.NoSuchElementException;

import p2.DataStructures.Tree.BTNode;

/**
 * Implementation of a Sorted List using a Singly Linked List structure
 * 
 * @author Fernando J. Bermudez - bermed28
 * @author Anthony Manzano - 802-19-4083
 * @version 3.0
 * @since 03/28/2023
 * @param <E> 
 */
public class SortedLinkedList<E extends Comparable<? super E>> extends AbstractSortedList<E> {

	@SuppressWarnings("unused")
	private static class Node<E> {

		private E value;
		private Node<E> next;

		public Node(E value, Node<E> next) {
			this.value = value;
			this.next = next;
		}

		public Node(E value) {
			this(value, null); // Delegate to other constructor
		}

		public Node() {
			this(null, null); // Delegate to other constructor
		}

		public E getValue() {
			return value;
		}

		public void setValue(E value) {
			this.value = value;
		}

		public Node<E> getNext() {
			return next;
		}

		public void setNext(Node<E> next) {
			this.next = next;
		}

		public void clear() {
			value = null;
			next = null;
		}				
	} // End of Node class

	
	private Node<E> head; // First DATA node (This is NOT a dummy header node)
	
	public SortedLinkedList() {
		head = null;
		currentSize = 0;
	}

	/*
	 * Add method that puts the elements in ascending order inside of the linked list.
	 * @Param - element to insert
	 */
	@Override
	public void add(E e) {
		/* TODO ADD CODE HERE */
		//Initializing new node and temp node
		Node<E> newnode = new Node<>();
		Node<E> temp = this.head;
		newnode.setValue(e);
		
		//checks if the list if empty and adds the new element
		if(isEmpty()) {
			this.head = newnode;
			currentSize++;
			return;
		}
		
		//checks if the new element is smaller than the fisrt element
		if(e.compareTo(this.head.getValue()) < 0) {
			newnode.setNext(temp);
			this.head = newnode;
			currentSize++;
			return;
		}
		
		//iterates through the list until it finds an element that its larger and inserts itself in that position to keep the order.
		//if the element already exist in the list it does nothing.
		while (temp.getNext() != null) {
			if(e.compareTo(temp.getNext().getValue()) == 0) {
				return;
			}
			else if (e.compareTo(temp.getNext().getValue()) < 0) {
				newnode.setNext(temp.getNext());
				temp.setNext(newnode);
				currentSize++;
				return;
			}
			temp = temp.getNext();
		}
		
		
		//gets the last node in the list
		temp = this.head;
		while( temp.getNext()!=null) {
			temp = temp.getNext();
		}
		//compares itself with the last node and if its larger, inserts itself at the end
		if(e.compareTo(temp.getValue()) > 0) {
			temp.setNext(newnode);
			currentSize++;
			return;
		}
		
		//list has only one element and new element is equal to it
		if (e.compareTo(head.getValue()) == 0) {
			return;
		}

		//list has only one element and new element is greater than it
		if (e.compareTo(head.getValue()) > 0) {
			head.setNext(newnode);
			currentSize++;
			return;
		}

	}
		

		
		
	/*
	 * Removes elements given the specific element we want to remove.
	 * @Param - element to remove.
	 * @return - true if element was removed.
	 */
	@Override
	public boolean remove(E e) {
		/* TODO ADD CODE HERE */
		/* Special case: Be careful when the value is found at the head node */
		Node<E> rmNode, curNode;
		curNode = this.head;
		
		//if the list is empty throw an exception
		if(isEmpty()) {
			throw new NoSuchElementException();
		}
		
		//if the element is found at the head, remove it and return it
		if(e.compareTo(curNode.getValue()) == 0) {
			rmNode = curNode;
			this.head = curNode.getNext();
			rmNode.clear();
			currentSize--;
			return true;
		}
		
		//iterate though the list until we find the element that we are looking for and remove it.
		while( curNode != null) {
			if(e.compareTo(curNode.getNext().getValue()) == 0) {
				rmNode = curNode.getNext();
				curNode.setNext(rmNode.getNext());
				rmNode.clear();
				currentSize--;
				return true;
			}
			curNode = curNode.getNext();
		}
		return false; //Dummy Return
	}

	/*
	 * Removes and element given its index
	 * @Param - index to remove
	 * @return - element at that position
	 */
	@Override
	public E removeIndex(int index) {
		/* TODO ADD CODE HERE */
		/* Special case: Be careful when index = 0 */
		//list is empty
		if (head == null) {
			throw new NoSuchElementException();
		}

		//index is negative
		if (index < 0) {
			throw new IndexOutOfBoundsException();
		}

		//index is out of bounds
		if (index >= size()) {
			throw new IndexOutOfBoundsException();
		}

		//index is at the head
		if (index == 0) {
			E value = head.getValue();
			head = head.getNext();
			currentSize--;
			return value;
		}

		//index is at the tail
		if (index == size() - 1) {
			Node<E> curNode = head;
			while (curNode.getNext().getNext() != null) {
				curNode = curNode.getNext();
			}
			E value = curNode.getNext().getValue();
			curNode.setNext(null);
			currentSize--;
			return value;
		}

		//index is in the middle
		Node<E> curNode = head;
		for (int i = 0; i < index - 1; i++) {
			curNode = curNode.getNext();
		}
		E value = curNode.getNext().getValue();
		curNode.setNext(curNode.getNext().getNext());
		currentSize--;
		return value;
	}

	/*
	 * Finds the first instance of an element in the list 
	 * @Param - element to find
	 * @Return - Index 
	 */
	@Override
	public int firstIndex(E e) {
		/* TODO ADD CODE HERE */
		
		Node<E> temp = this.head;
		
		//we iteriate through the list with a for loop where i represents the position
		for(int i=0; temp != null; i++) {
			if(e.compareTo(temp.getValue()) == 0) {
				return i;// element is found so return i
			}
			temp = temp.getNext();
		}
		
		return 0;
		 //Dummy Return
	}

	/* Gets a certain elements given its index
	 * @param - index
	 * @return - element at that position
	 * 
	 */
	@Override
	public E get(int index) {

		//check if index is valid.
		if( index < 0 || index > this.currentSize) {
			throw new IndexOutOfBoundsException();
		}
		
		Node<E> temp = this.head;
		Node<E> target = null;
		int i = 0;
		
		while(temp != null) {
			if( i == index) {
				target = temp;
				break;
			}else {
				temp = temp.getNext();
				i++;
			}
		}
	
		 return target.getValue();//Dummy Return
	}


	@Override
	public Comparable<E>[] toArray() {
		int index = 0;
		Comparable[] theArray = new Comparable[size()]; // Cannot use Object here
		for(Node<E> curNode = this.head; index < size() && curNode  != null; curNode = curNode.getNext(), index++) {
			theArray[index] = curNode.getValue();
		}
		return theArray;
	}

	public static void main(String[] args) {
		SortedLinkedList<Integer> test = new SortedLinkedList<>();
		
		test.add(3);
		test.add(1);
		test.add(1);
		System.out.println(test.toArray());
	}
}