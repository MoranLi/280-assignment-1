package lib280.list;


import lib280.base.BilinearIterator280;
import lib280.base.CursorPosition280;
import lib280.base.Pair280;
import lib280.exception.*;

/**	This list class incorporates the functions of an iterated 
	dictionary such as has, obtain, search, goFirst, goForth, 
	deleteItem, etc.  It also has the capabilities to iterate backwards 
	in the list, goLast and goBack. */
public class BilinkedList280<I> extends LinkedList280<I> implements BilinearIterator280<I>
{
	/* 	Note that because firstRemainder() and remainder() should not cut links of the original list,
		the previous node reference of firstNode is not always correct.
		Also, the instance variable prev is generally kept up to date, but may not always be correct.  
		Use previousNode() instead! */

	/**	Construct an empty list.
		Analysis: Time = O(1) */
	public BilinkedList280()
	{
		super();
	}

	/**
	 * Create a BilinkedNode280 this Bilinked list.  This routine should be
	 * overridden for classes that extend this class that need a specialized node.
	 * @param item - element to store in the new node
	 * @return a new node containing item
	 */
	protected BilinkedNode280<I> createNewNode(I item)
	{
		// TODO



		return new BilinkedNode280<I>(item);  // This line is present only to prevent a compile error.  You should remove it before
		              // completing this method.
	}

	/**
	 * Insert element at the beginning of the list
	 * @param x item to be inserted at the beginning of the list 
	 */
	public void insertFirst(I x) 
	{
		// TODO
		BilinkedNode280<I> newItem = createNewNode(x);
		newItem.setNextNode(this.head);
		newItem.setPreviousNode(null);

		// If the cursor is at the first node, cursor predecessor becomes the new node.
		if( this.position == this.head ) this.prevPosition = newItem;

		// Special case: if the list is empty, the new item also becomes the tail.
		if( this.isEmpty() ) this.tail = newItem;
		else  ((BilinkedNode280)head).setPreviousNode(newItem);
		this.head = newItem;
	}

	/**
	 * Insert element at the beginning of the list
	 * @param x item to be inserted at the beginning of the list 
	 */
	public void insert(I x) 
	{
		this.insertFirst(x);
	}

	/**
	 * Insert an item before the current position.
	 * @param x - The item to be inserted.
	 */
	public void insertBefore(I x) throws InvalidState280Exception {
		if( this.before() ) throw new InvalidState280Exception("Cannot insertBefore() when the cursor is already before the first element.");
		
		// If the item goes at the beginning or the end, handle those special cases.
		if( this.head == position ) {
			insertFirst(x);  // special case - inserting before first element
		}
		else if( this.after() ) {
			insertLast(x);   // special case - inserting at the end
		}
		else {
			// Otherwise, insert the node between the current position and the previous position.
			BilinkedNode280<I> newNode = createNewNode(x);
			newNode.setNextNode(position);
			newNode.setPreviousNode((BilinkedNode280<I>)this.prevPosition);
			prevPosition.setNextNode(newNode);
			((BilinkedNode280<I>)this.position).setPreviousNode(newNode);
			
			// since position didn't change, but we changed it's predecessor, prevPosition needs to be updated to be the new previous node.
			prevPosition = newNode;			
		}
	}
	
	
	/**	Insert x before the current position and make it current item. <br>
		Analysis: Time = O(1)
		@param x item to be inserted before the current position */
	public void insertPriorGo(I x) 
	{
		this.insertBefore(x);
		this.goBack();
	}

	/**	Insert x after the current item. <br>
		Analysis: Time = O(1) 
		@param x item to be inserted after the current position */
	public void insertNext(I x) 
	{
		if (isEmpty() || before())
			insertFirst(x); 
		else if (this.position==lastNode())
			insertLast(x); 
		else if (after()) // if after then have to deal with previous node  
		{
			insertLast(x); 
			this.position = this.prevPosition.nextNode();
		}
		else // in the list, so create a node and set the pointers to the new node 
		{
			BilinkedNode280<I> temp = createNewNode(x);
			temp.setNextNode(this.position.nextNode());
			temp.setPreviousNode((BilinkedNode280<I>)this.position);
			((BilinkedNode280<I>) this.position.nextNode()).setPreviousNode(temp);
			this.position.setNextNode(temp);
		}
	}

	/**
	 * Insert a new element at the end of the list
	 * @param x item to be inserted at the end of the list 
	 */
	public void insertLast(I x) 
	{
		// TODO
		BilinkedNode280<I> lastNode = createNewNode(x);
		lastNode.setNextNode(null);

		if(after() && head != null && tail != null){
			prevPosition = lastNode;
			lastNode.setPreviousNode((BilinkedNode280<I>)tail);
			tail.setNextNode(lastNode);
		}
		if(isEmpty()){
			head = lastNode;
			lastNode.setPreviousNode(null);
		}
		else {
			tail.setNextNode(lastNode);
			lastNode.setPreviousNode((BilinkedNode280<I>)tail);
		}
		tail = lastNode;
	}

	/**
	 * Delete the item at which the cursor is positioned
	 * @precond itemExists() must be true (the cursor must be positioned at some element)
	 */
	public void deleteItem() throws NoCurrentItem280Exception
	{
		// TODO
		if(!this.itemExists()) throw new NoCurrentItem280Exception("There is no item at the cursor to delete.");
		this.delete(this.position.item());
		/*
		// If we are deleting the first item...
		if( this.position == this.head ) {
			this.deleteFirst();
			this.head = this.head.nextNode;
			this.position = this.head;
		}
		else {
			// Set the previous node to point to the successor node.
			this.prevPosition.setNextNode(this.position.nextNode());

			// Reset the tail reference if we deleted the last node.
			if( this.position == this.tail ) {
				this.tail = this.prevPosition;
			}
			this.position = this.position.nextNode();
		}
		*/
	}

	
	@Override
	public void delete(I x) throws ItemNotFound280Exception {
		if( this.isEmpty() ) throw new ContainerEmpty280Exception("Cannot delete from an empty list.");

		// Save cursor position
		LinkedIterator280<I> savePos = this.currentPosition();
		
		// Find the item to be deleted.
		search(x);
		if( !this.itemExists() ) throw new ItemNotFound280Exception("Item to be deleted wasn't in the list.");

		// If we are about to delete the item that the cursor was pointing at,
		// advance the cursor in the saved position, but leave the predecessor where
		// it is because it will remain the predecessor.
		if( this.position == savePos.cur ) savePos.cur = savePos.cur.nextNode();
		
		// If we are about to delete the predecessor to the cursor, the predecessor 
		// must be moved back one item.
		if( this.position == savePos.prev ) {
			
			// If savePos.prev is the first node, then the first node is being deleted
			// and savePos.prev has to be null.
			if( savePos.prev == this.head ) savePos.prev = null;
			else {
				// Otherwise, Find the node preceding savePos.prev
				LinkedNode280<I> tmp = this.head;
				while(tmp.nextNode() != savePos.prev) tmp = tmp.nextNode();
				
				// Update the cursor position to be restored.
				savePos.prev = tmp;
			}
		}
				
		// Unlink the node to be deleted.
		if( this.prevPosition != null)
			// Set previous node to point to next node.
			// Only do this if the node we are deleting is not the first one.
			this.prevPosition.setNextNode(this.position.nextNode());
		
		if( this.position.nextNode() != null )
			// Set next node to point to previous node 
			// But only do this if we are not deleting the last node.
			((BilinkedNode280<I>)this.position.nextNode()).setPreviousNode(((BilinkedNode280<I>)this.position).previousNode());
		
		// If we deleted the first or last node (or both, in the case
		// that the list only contained one element), update head/tail.
		if( this.position == this.head ) this.head = this.head.nextNode();
		if( this.position == this.tail ) this.tail = this.prevPosition;
		
		// Clean up references in the node being deleted.
		this.position.setNextNode(null);
		((BilinkedNode280<I>)this.position).setPreviousNode(null);
		
		// Restore the old, possibly modified cursor.
		this.goPosition(savePos);
		
	}
	/**
	 * Remove the first item from the list.
	 * @precond !isEmpty() - the list cannot be empty
	 */
	public void deleteFirst() throws ContainerEmpty280Exception
	{
		// TODO
		if(isEmpty()){throw new Container280Exception("Can not delete element for a empty list");}
		this.delete(this.head.item());

	}

	/**
	 * Remove the last item from the list.
	 * @precond !isEmpty() - the list cannot be empty
	 */
	public void deleteLast() throws ContainerEmpty280Exception
	{
		// TODO
		if(isEmpty()){throw new Container280Exception("Can not delete element for a empty list");}
		this.delete(this.tail.item());
	}

	
	/**
	 * Move the cursor to the last item in the list.
	 * @precond The list is not empty.
	 */
	public void goLast() throws ContainerEmpty280Exception
	{
		// TODO
		if(isEmpty()){throw new Container280Exception("Can not move cursor in a empty list");}
		position = this.tail;
		prevPosition = ((BilinkedNode280)tail).previousNode;
//		BilinkedNode280 tempNode = (BilinkedNode280)position;
//		goFirst();
//		while(position.nextNode() != tempNode && position.nextNode() != null){
//			goForth();
//		}
//		prevPosition = position;
//		position = this.tail;
	}
  
	/**	Move back one item in the list. 
		Analysis: Time = O(1)
		@precond !before() 
	 */
	public void goBack() throws BeforeTheStart280Exception
	{
		// TODO
		if(before()){throw new BeforeTheStart280Exception("Can not go back at the start of LinkedList");}
		position = prevPosition;
		prevPosition = ((BilinkedNode280)position).previousNode;
//		BilinkedNode280 tempNode = (BilinkedNode280)position;
//		goFirst();
//		while(position.nextNode() != tempNode){
//			goForth();
//		}
//		prevPosition = position;
//		position = tempNode;
	}

	/**	Iterator for list initialized to first item. 
		Analysis: Time = O(1) 
	*/
	public BilinkedIterator280<I> iterator()
	{
		return new BilinkedIterator280<I>(this);
	}

	/**	Go to the position in the list specified by c. <br>
		Analysis: Time = O(1) 
		@param c position to which to go */
	@SuppressWarnings("unchecked")
	public void goPosition(CursorPosition280 c)
	{
		if (!(c instanceof BilinkedIterator280))
			throw new InvalidArgument280Exception("The cursor position parameter" 
					    + " must be a BilinkedIterator280<I>");
		BilinkedIterator280<I> lc = (BilinkedIterator280<I>) c;
		this.position = lc.cur;
		this.prevPosition = lc.prev;
	}

	/**	The current position in this list. 
		Analysis: Time = O(1) */
	public BilinkedIterator280<I> currentPosition()
	{
		return  new BilinkedIterator280<I>(this, this.prevPosition, this.position);
	}

	
  
	/**	A shallow clone of this object. 
		Analysis: Time = O(1) */
	public BilinkedList280<I> clone() throws CloneNotSupportedException
	{
		return (BilinkedList280<I>) super.clone();
	}


	/* Regression test. */
	public static void main(String[] args) {
		// TODO
		BilinkedList280<Integer> list1 = new BilinkedList280<Integer>();
		BilinkedNode280<Integer> node1 = list1.createNewNode(1);
		System.out.print("Testing for createNewNode:\n");
		System.out.print("The Node should contain 1,\n");
		System.out.print(node1.item());
		System.out.print("\n");
		// test for insertFirst
		// test insertFirst when null
		System.out.print("Testing for InsertFisrt when list length is 0:\n");
		list1.insertFirst(2);
		System.out.print("The result should be 2,\n");
		System.out.print(list1.toString());
		System.out.print("\n");
		// test insertFirst when 1
		System.out.print("Testing for InsertFisrt when list length is 1:\n");
		list1.insertFirst(3);
		System.out.print("The result should be 3,2,\n");
		System.out.print(list1.toString());
		System.out.print("\n");
		// test insertFirst when > 1
		System.out.print("Testing for InsertFisrt when list length > 1:\n");
		list1.insertFirst(1);
		System.out.print("The result should be 1,3,2,\n");
		System.out.print(list1.toString());
		System.out.print("\n");
		// test deleteLast when > 1
		System.out.print("Testing for deleteLast when list length > 1:\n");
		list1.deleteLast();
		System.out.print("The result should be 1,3,\n");
		System.out.print(list1.toString());
		System.out.print("\n");
		// test deleteFirst when > 1
		System.out.print("Testing for deleteFirst when list length > 1:\n");
		list1.deleteFirst();
		System.out.print("The result should be 3,\n");
		System.out.print(list1.toString());
		System.out.print("\n");
		// test deleteLast when = 1
		System.out.print("Testing for deleteLast when list length = 1:\n");
		list1.deleteLast();
		System.out.print("The result should be <Empty>\n");
		System.out.print(list1.toString());
		System.out.print("\n");
		// test deleteFirst when = 1
		System.out.print("Testing for deleteFirst when list length = 1:\n");
		list1.insertFirst(3);
		list1.deleteFirst();
		System.out.print("The result should be <Empty>\n");
		System.out.print(list1.toString());
		System.out.print("\n");
		// test deleteFirst when null
		System.out.print("Testing for deleteLast when list is null:\n");
		try{
			list1.deleteFirst();
		}
		catch (Container280Exception e){
			System.out.print("Receive Exception means deleteFirst is fine\n");
		}
		// test deleteFirst when null
		System.out.print("Testing for deleteFirst when list is null:\n");
		try{
			list1.deleteLast();
		}
		catch (Container280Exception e){
			System.out.print("Receive Exception means deleteLast is fine\n");
		}
		// test insertLast when null
		System.out.print("Testing for insertLast when list is null:\n");
		list1.insertLast(1);
		System.out.print("The result should be 1,\n");
		System.out.print(list1.toString());
		System.out.print("\n");
		// test insertLast when = 1
		System.out.print("Testing for insertLast when list length = 1:\n");
		list1.insertLast(2);
		System.out.print("The result should be 1,2,\n");
		System.out.print(list1.toString());
		System.out.print("\n");
		// test insertLast when > 1
		System.out.print("Testing for insertLast when list length > 1:\n");
		list1.insertLast(3);
		System.out.print("The result should be 1,2,3,\n");
		System.out.print(list1.toString());
		System.out.print("\n");
		// test goLast when > 1
		System.out.print("Testing for goLast when list length > 1:\n");
		list1.goLast();
		System.out.print("The result should be 3\n");
		System.out.print(list1.position.item());
		System.out.print("\n");
		// test deleteItem when > 1
		System.out.print("Testing for deleteItem when list length > 1:\n");
		list1.deleteItem();
		System.out.print("The result should be 1,2,\n");
		System.out.print(list1.toString());
		System.out.print("\n");
		//test goBack when > 1
		System.out.print("Testing for goBack when list length > 1:\n");
		list1.goBack();
		System.out.print("The result should be 2\n");
		System.out.print(list1.position.item());
		System.out.print("\n");
		// test goBack when = 1
		System.out.print("Testing for goBack when list length = 1:\n");
		list1.deleteItem();
		System.out.print("Right now the list is " + list1.toString() + "\n");
		list1.goBack();
		System.out.print("The result should be 1\n");
		System.out.print(list1.position.item());
		System.out.print("\n");
		// test goLast when = 1
		System.out.print("Testing for goLast when list length = 1:\n");
		System.out.print("Right now the list is " + list1.toString() + "\n");
		list1.goLast();
		System.out.print("The result should be 1\n");
		System.out.print(list1.position.item());
		System.out.print("\n");
		// test deleteItem when = 1
		System.out.print("Testing for deleteItem when list length = 1:\n");
		list1.deleteItem();
		System.out.print("The result should be <Empty>\n");
		System.out.print(list1.toString());
		System.out.print("\n");
		// test deleteItem when null
		System.out.print("Testing for deleteItem when list is null:\n");
		try{
			list1.deleteItem();
		}
		catch (Container280Exception e){
			System.out.print("Receive Exception means deleteItem is fine for null list\n");
		}
		// test goLast when null
		System.out.print("Testing for goLast when list is null:\n");
		try{
			list1.goLast();
		}
		catch (Container280Exception e){
			System.out.print("Receive Exception means goLast is fine for null list\n");
		}
		// test goBack when null
		System.out.print("Testing for goBack when list is null:\n");
		try{
			list1.goBack();
		}
		catch (BeforeTheStart280Exception e){
			System.out.print("Receive Exception means goBack is fine for null list\n");
		}
		list1.insert(1);
		list1.goBefore();
		// test for goBack at before of list
		System.out.print("Testing for goBack when position is before list:\n");
		try{
			list1.goBack();
		}
		catch(BeforeTheStart280Exception e){
			System.out.print("Receive Exception means goBack is fine for case position is at before of list\n");
		}
		// test for goBack at after of list
		System.out.print("Testing for goBack when position is after list:\n");
		list1.goAfter();
		list1.goBack();
		if (list1.position == list1.tail){
			System.out.print("goBack is fine for case position is at after of list.\n");
		}
		// Test insertFirst when position at before
		System.out.print("Testing for insertFirst when position at before:\n");
		list1.goBefore();
		list1.insertFirst(2);
		System.out.print("The result should be 2,1,\n");
		System.out.print(list1.toString());
		System.out.print("\n");
		// Test insertFirst when position at First
		System.out.print("Testing for insertFirst when position at first:\n");
		list1.goFirst();
		list1.insertFirst(3);
		System.out.print("The result should be 3,2,1,\n");
		System.out.print(list1.toString());
		System.out.print("\n");
		// Test deleteFirst when position at before
		System.out.print("Testing for deleteFirst when position at before:\n");
		list1.goBefore();
		list1.deleteFirst();
		System.out.print("The result should be 2,1,\n");
		System.out.print(list1.toString());
		System.out.print("\n");
		// Test insertFirst when position at before
		System.out.print("Testing for delteFirst when position at First:\n");
		list1.goFirst();
		list1.deleteFirst();
		System.out.print("The result should be 1,\n");
		System.out.print(list1.toString());
		System.out.print("\n");
		// Test insertFirst when position at before
		System.out.print("Testing for insertLast when position at after:\n");
		list1.goAfter();
		list1.insertLast(2);
		System.out.print("The result should be 1,2,\n");
		System.out.print(list1.toString());
		System.out.print("\n");
		// Test insertFirst when position at before
		System.out.print("Testing for insertLast when position at last:\n");
		list1.goLast();
		list1.insertLast(3);
		System.out.print("The result should be 1,2,3,\n");
		System.out.print(list1.toString());
		System.out.print("\n");
		// Test insertFirst when position at before
		System.out.print("Testing for deleteLast when position at after:\n");
		list1.goAfter();
		list1.deleteLast();
		System.out.print("The result should be 1,2,\n");
		System.out.print(list1.toString());
		System.out.print("\n");
		// Test insertFirst when position at before
		System.out.print("Testing for deleteLast when position at last:\n");
		list1.goLast();
		list1.deleteLast();
		System.out.print("The result should be 1,\n");
		System.out.print(list1.toString());
		System.out.print("\n");

	}
} 
