package all;


class Item {
	String name;
	int next;
	public Item(String name, int next) {
		this.name = name;
		this.next = next;
	}
	public String toString() {
		return "(" + name + "," + next + ")";
	}
}

public class ArrayLL {

	private Item[] all;			//master array
	private int numItems;		//n
	private int front;			//starting point
	private int[] avail;		//deleted indexes in master array
	private int numAvail;		//number of index entries in avail

	// Constructor, initializes all data fields, to represent 
	// an empty Item array linked list of length maxItems
	public ArrayLL(int maxItems) {

		all = new Item[maxItems];
		avail = new int[maxItems];
		// COMPLETE THIS CONSTRUCTOR

	}

	// Adds a name to the front of this array linked list, in worst case O(1) time,
	// and returns true.
	// Returns false if the array is full, in O(1) time
	public boolean addFront(String name) {
		
		Item temp = new Item(name, front);
		front = front + 1;
		if(all[front]==null){
			all[front]=temp;
			return true;
		}
		return false;
		/**
		public static Node addToFront(Node head, int x){
		
	
		Node temp = new Node(x);
		temp.next = head;
		
		return temp;
		
	}
		 */
		// COMPLETE THIS METHOD
		
		// THE NEXT LINE IS ONLY A PLACEHOLDER TO MAKE THE PROGRAM COMPILE 
		// YOU WILL NEED TO CHANGE IT APPROPRIATELY IN YOUR IMPLEMENTATION

	}

	// Deletes the name that is at the front this array linked list, in worst case O(1) time,
	// and returns the deleted name
	// Returns null if the list is empty, in O(1) time
	public String deleteFront() {
		
		if(all[front] == null){
			return null;	
		}else{
		
			Item temp = all[(all[front].next)];
			Item delItem = all[front];
			String delName = delItem.name;
			avail[numAvail] = front;
			numAvail = numAvail+1;
			front = front-1;
			all[front] = temp;
			numItems = numItems-1;
			return delName;
		}
		
		/**	if(head != null){
		Node temp = head.next;
		return temp;
		
		}else{
			return null;
		}
		**/
		
		// COMPLETE THIS METHOD

		// THE NEXT LINE IS ONLY A PLACEHOLDER TO MAKE THE PROGRAM COMPILE 
		// YOU WILL NEED TO CHANGE IT APPROPRIATELY IN YOUR IMPLEMENTATION
		
	}

	// Deletes the given name from this array linked list, and returns true.
	// Returns false if the name is not in the list.
	// Note: If there are n active items in the list, then this method must run in
	// worst case O(n) time, i.e. time must not depend on the length of the all array
	// (since the array might include available space not filled by active items)
	// Also, avail array should be accessed/updated in O(1) time
	public boolean delete(String name) {
		Item temp = all[front];
		Item prev = null;
		
		if(all[front] != null && all[front].name.equals(name)){	
			temp = all[(all[front].next)];
			avail[numAvail] = front;
			numAvail = numAvail+1;
			front = front-1;
			all[front] = temp;
			numItems = numItems-1;						//head is target
			return true;
		}
		
		while(temp != null){
			
			if(temp.name.equals(name)){
				prev.next = temp.next;
				all[temp.next+2].next = temp.next;
				avail[numAvail] = temp.next + 1;
				numAvail = numAvail+1;
				numItems = numItems-1;
				
				return true;
			}
			prev = temp;
			temp = all[temp.next];
			}		
		
		return false;
	}
		
		/**
		Node temp = head;
		Node previous = null;
		
		if(head != null && head.data.equals(x)){
			return head.next;
		}
		
		while(temp != null){
		
			if(temp.data.equals(x)){
				previous.next = temp.next;
				return head;
			}
			previous = temp;
			temp = temp.next;
		}
		
		return head;
		**/
	
	/*
	 int index = -10;
		Item delItem;
		
		if(all[0] != null){
			for(int i = 0; i<all.length; i++){			//locates item which name is to be deleted
				if(name.equals(all[i].name)){
					delItem = all[i];
					index = i;
				}
				break;
			}
			for(int j=0; j<all.length; j++){		//changes next of Item after target
				if(index == all[j].next){
					all[j].next = index - 1;
					numItems = numItems-1;
				}
			}
			
			for(int k = 0; k<avail.length; k++){		//add deleted index to avail	
				if(avail[k] == 0){
					index = k;
					break;
				}
			}
			if(index != -10){
				avail[index] = front;
				numAvail = numAvail + 1;
			}
			return true;
	 */
		
		// COMPLETE THIS METHOD

		// THE NEXT LINE IS ONLY A PLACEHOLDER TO MAKE THE PROGRAM COMPILE
		// YOU WILL NEED TO CHANGE IT APPROPRIATELY IN YOUR IMPLEMENTATION
	

	// Checks if the given name is in this array linked list
	// Note: If there are n items in the list, then this method must run in
	// worst case O(n) time, i.e. time does not depend on the length of the all array.
	public boolean contains(String name) {
		
		Item temp = all[front];
		
		while(temp != null){
			if(temp.name.equals(name)){
				return true;
			}
			temp = all[temp.next];
		}
		return false;
	/**
		 public boolean search(T s){
			
			Node temp = head;
			
			while(temp != null){
				
				if(temp.data.equals(s)){ //if(temp.data.equals(x);
					return true;
				}	
					temp = temp.next;
				
			}
			return false;
			
		}
		 */
		// COMPLETE THIS METHOD

		// THE NEXT LINE IS ONLY A PLACEHOLDER TO MAKE THE PROGRAM COMPILE
		// YOU WILL NEED TO CHANGE IT APPROPRIATELY IN YOUR IMPLEMENTATION
	}

	// Prints the items in this array linked list in sequence from first to last,
	// in worst case O(n) time where n is the number of items in the linked list
	// The list should be printed in a single line, separated by commas
	// Example: earth,mercury,venus
	// Make sure there aren't any extra commas in your output.
	// If the list is empty, you may print either nothing, or an empty string
	public void printList() {

		Item temp = all[front];
		while(temp != null){
			System.out.print(temp.name + ',');
			temp = all[temp.next];
		}
		/**
		Node temp = head;
		while(temp != null){
			
			System.out.println(temp.data +" ---> ");
			temp = temp.next;
		}
		**/
		// COMPLETE THIS METHOD
	}

	// Prints all the entries in the main array (including unused spaces)
	// You may fill in this method and use it for debugging
	// This method WILL NOT be graded
	public void printArray() {
		for(int i = 0; i<all.length; i++){
			System.out.println(all[i].name);
		}
	}

	// Prints all the entries in the avail array that correspond to
	// available spaces in the main array
	// You may fill in this method and use it for debugging
	// This method WILL NOT be graded
	public void printAvailableSpots() {
		for(int i = 0; i<avail.length; i++){
			System.out.println(avail[i]);
		}
	}
}
