 Array Linked List Assignment
 
 Motivation and Design

You know how to insert and delete items in an array if you are allowed to move things around.

Suppose you have an array of length 10, and you add a:

    -----------------------------------------
    | a |   |   |   |   |   |   |   |   |   |
    -----------------------------------------
      0   1   2   3   4   5   6   7   8   9

Assume that you are carrying along a number, say n, that tells you how many things are actually in the list. After adding a, n would be 1.

Now we want to add b to the front of the list. By list, we mean a sequence of items. If you choose to do it by moving things around, you can move a over by one spot to make room for b:

    -----------------------------------------
    | b | a |   |   |   |   |   |   |   |   |
    -----------------------------------------
      0   1   2   3   4   5   6   7   8   9

But what if you want to make this run faster? It will, if you don't move things around. But if you don't move things around, you will be forced to place b right after a. However, remember that you want to preserve the order of the sequence, which means that when you traverse the list you should see b first, then a. To get this effect, you need to tack on an additional next index with each item you add:

    -----------------------------------------------------
    |  a  |  b  |    |    |    |    |    |    |    |    |
    | --- | --- |    |    |    |    |    |    |    |    |  
    | -1  |  0  |    |    |    |    |    |    |    |    |
    -----------------------------------------------------
      0      1     2    3    4    5    6   7    8    9

In the picture above, the next index for b is 0, which means the item that comes after b in the list sequence is at position 0 of the array. That item is a. The next index for a is -1, which is a special index to mean there is nothing after a. (You can use any number you want to mean "nothing after" as long as it is not one of the legal array indexes. In this example, any number except 0..9 can be used instead of -1.) The next indices are the links that tie together all the elements in sequence.

There is only one more thing left to do, and that is to set up an index which tells us where the sequence starts. Remember that we already have a variable n which tells us how many things there are in the list. You may be tempted to use this variable - after adding b, n is 2, so the first item in the sequence is at index n-1, which is 1. But what if you remove a from the list? Since you don't want to move items over, the resulting array will look like this:

    -----------------------------------------------------
    |  a  |  b  |    |    |    |    |    |    |    |    |
    | --- | --- |    |    |    |    |    |    |    |    |  
    | -1  |  -1 |    |    |    |    |    |    |    |    |
    -----------------------------------------------------
      0      1     2    3    4    5    6   7    8    9

Notice that the next index of b is now set to -1, to mean there is nothing after it. (So nothing needs to be done in the space occupied previously by a.) The number of list entries, n, is now 1, but the first entry in the list is still at 1, which is not equal to n-1 (which is 0). So we will need to keep a separate variable, say front, which is set to the index of the first entry in the list. front will be 0 after adding a, 1 after adding b, and still 1 after deleting a.

    -----------------------------------------------------
    |  a  |  b  |    |    |    |    |    |    |    |    |
    | --- | --- |    |    |    |    |    |    |    |    |    front = 1
    | -1  |  -1 |    |    |    |    |    |    |    |    |    n = 1
    -----------------------------------------------------
      0      1     2    3    4    5    6   7    8    9

The last detail in this implementation has to do with managing the space opened up by deleted entires. Why? Well, imagine that 10 entries are inserted (which fills up the array), and 9 of them are deleted. So n=1, and there 9 open spots. But where are they, and how can they be located efficiently?

Since the deleted entries could be strewn throughout the array, we need to keep track of all of them so they can be reused. Here's an example. Starting with an empty array, insert items a through j (remember, each item is added to the front of the list in our examples). Following is the array that results, with n equal to 10, and front equal to 9.

    -------------------------------------------------------------
    |  a  |  b  |  c  |  d  |  e  |  f  |  g  |  h  |  i  |  j  |
    | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |  
    | -1  |  0  |  1  |  2  |  3  |  4  |  5  |  6  |  7  |  8  |
    -------------------------------------------------------------
       0     1     2     3     4     5     6     7     8     9
                                                             ^
                                                             |
                                                            front
         

If an attempt is made to insert yet another item, it should fail because the array has no more available space. Now imagine deleting c, then g, and then a. (To delete any item, the array is first searched to locate that item, after which it is deleted.) Here's the resulting array:

    -------------------------------------------------------------
    |  a  |  b  |  c  |  d  |  e  |  f  |  g  |  h  |  i  |  j  |
    | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |  
    | -1  |0 -1 |  1  |2 1  |  3  |  4  |  5  |6 5  |  7  |  8  |
    -------------------------------------------------------------
       0     1     2     3     4     5     6     7     8     9
     avail        avail                  avail               ^
                                                             |
                                                            front

Notice how the next indexes of d (right before c in the list sequence), h (right before g in the list sequence), and b (right before a in the list sequence) have been updated to skip the respective deleted entries.

Subsequently, say you want to insert k. You don't want to report the array is full because there are 3 available spaces, i.e. n is 7. How do you know where these spaces are? One way is have another array, avail (of the same capacity as the master array) to hold indexes of all available spaces:

    -----------------------------------------
    | 0 | 2 | 6 |   |   |   |   |   |   |   |     numavail=3
    -----------------------------------------
      0   1   2   3   4   5   6   7   8   9

It doesn't matter in what order the available indexes are stored. When an item is to be added, any of the available spots can be filled. Once you have an avail array with indexes, it is easiest to deal out the spots back to front. So, when inserting the new k the spot 6 will be used up. Since it is inserted at the front of the list, the next index of k will be set to the current front (9), and front will be set to 6, so it points to k:

    -------------------------------------------------------------
    |  a  |  b  |  c  |  d  |  e  |  f  |  k  |  h  |  i  |  j  |
    | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |  
    | -1  | -1  |  1  |  1  |  3  |  4  |  9  |  5  |  7  |  8  |
    -------------------------------------------------------------
       0     1     2     3     4     5     6     7     8     9
     avail       avail                     ^
                                           |
                                         front

The avail array will effectively only have 2 entries now - array length (10) minus n (8).

Implementation

Download the attached allproject.zip file to your computer. DO NOT unzip it. Instead, follow the instructions on the Eclipse page under the section "Importing a Zipped Project into Eclipse" to get the entire project into your Eclipse workspace.

You will see a project called Array Linked List with the classes ArrayLL and ArrayLLDriver in package all.

You need to fill in the implementation of the ArrayLL class where indicated. This includes the following:
Method 	Grading Points
Constructor ArrayLL 	5
addFront 	10
deleteFront 	10
delete 	10
contains 	8
printList 	7

Note: Credit for each of the above will depend on correctness and efficiency. Read the comments before each method header. If you have used an obviously inefficient way to write your code, you will lose at least half the credit.

Observe the following rules while working on ArrayLL.java:

    You may NOT add any import statements to the file.

    Note: Sometimes Eclipse will automatically add import statements to the file, if you are using an unknown class other than the ones you are given, or already used in the given code. It is your responsibility to delete such automatically added import statements. If your code does not compile, we will not be able to test it, and you will get a zero. (See the Course Policies section in the Syllabus page.)
    You may NOT add any new classes (you will only be submitting ArrayLL.java).
    You may NOT add any fields to the ArrayLL class.
    You may NOT modify the headers of any of the given methods.
    You may NOT delete any methods.
    You MAY add helper methods if needed, as long as you make them private.

Running the Program

The class ArrayLLDriver has a main method, so it can be run as an application. It is an interactive program that can be used to perform a sequence of actions on the array linked list you implement in ArrayLL.

Here's a sample run:

1. Add front
2. Delete front
3. Delete name
4. Search name
5. Print List
6. Print Array
7. Print Blanks
8. Quit
Enter Choice => 1

Enter name => earth
Added earth to front of list

1. Add front
2. Delete front
3. Delete name
4. Search name
5. Print List
6. Print Array
7. Print Blanks
8. Quit
Enter Choice => 5

earth

1. Add front
2. Delete front
3. Delete name
4. Search name
5. Print List
6. Print Array
7. Print Blanks
8. Quit
Enter Choice => 2

Deleted earth to front of list

1. Add front
2. Delete front
3. Delete name
4. Search name
5. Print List
6. Print Array
7. Print Blanks
8. Quit
Enter Choice => 2

Could not delete, list is empty

1. Add front
2. Delete front
3. Delete name
4. Search name
5. Print List
6. Print Array
7. Print Blanks
8. Quit
Enter Choice => 1

Enter name => mercury
Added mercury to front of list

1. Add front
2. Delete front
3. Delete name
4. Search name
5. Print List
6. Print Array
7. Print Blanks
8. Quit
Enter Choice => 1

Enter name => venus
Added venus to front of list

1. Add front
2. Delete front
3. Delete name
4. Search name
5. Print List
6. Print Array
7. Print Blanks
8. Quit
Enter Choice => 1

Enter name => earth
Added earth to front of list

1. Add front
2. Delete front
3. Delete name
4. Search name
5. Print List
6. Print Array
7. Print Blanks
8. Quit
Enter Choice => 5

earth,venus,mercury

1. Add front
2. Delete front
3. Delete name
4. Search name
5. Print List
6. Print Array
7. Print Blanks
8. Quit
Enter Choice => 4

Enter name => mercury
mercury found in list

1. Add front
2. Delete front
3. Delete name
4. Search name
5. Print List
6. Print Array
7. Print Blanks
8. Quit
Enter Choice => 3

Enter name => mars
Name mars not in list

1. Add front
2. Delete front
3. Delete name
4. Search name
5. Print List
6. Print Array
7. Print Blanks
8. Quit
Enter Choice => 3

Enter name => venus
Deleted venus from list

1. Add front
2. Delete front
3. Delete name
4. Search name
5. Print List
6. Print Array
7. Print Blanks
8. Quit
Enter Choice => 5

earth,mercury

1. Add front
2. Delete front
3. Delete name
4. Search name
5. Print List
6. Print Array
7. Print Blanks
8. Quit
Enter Choice => 8