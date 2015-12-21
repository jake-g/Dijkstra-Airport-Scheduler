# [CSE373 Fall 2015](..): Homework 4 - Graphs and Shortest Paths

For this assignment, you will develop a graph representation and use it to implement Dijkstra's algorithm for finding shortest paths. Unlike previous assignments, you will use some classes in the Java standard libraries, gaining valuable experience reading documentation and understanding provided APIs. Also, part of the grade for your code will depend on you "protecting the graph abstraction" leveraging copy-in-copy-out and immutability as we discussed (or will discuss) in class.


## Outline

*   [Due Dates, Turn-In, and Rules](#Due)
*   [Provided Files](#Code)
*   [Programming, Part 1](#ProgrammingOne)
*   [Programming, Part 2](#ProgrammingTwo)
*   [Write-Up Questions](#Questions)
*   [Above and Beyond](#AboveAndBeyond)
*   [What to Turn In](#Turnin)

## Due Dates, Turn-In, and Rules

**Different for this assignment:** You may use anything in the Java standard collections (or anything else in the standard library) for any part of this assignment. Take a look at the [Java API](http://docs.oracle.com/javase/7/docs/api/) as you are thinking about your solutions. At the very least, look at the [Collection](http://docs.oracle.com/javase/7/docs/api/java/util/Collection.html) and [List](http://docs.oracle.com/javase/7/docs/api/java/util/List.html) interfaces to see what operations are allowable on them and what classes implement those interfaces.

**Assignment Due 11:00PM Wednesday November 18, 2015**

For this assignment, you may work with **one** partner. If you do so, the two of you will turn in only one assignment and, except in extraordinary situations, receive the same grade (and, if applicable, have the same number of late days applied). Working with a partner is optional; it is fine to complete the assignment by yourself.

If you choose to work with a partner, you may divide the work however you wish, but both partners **must** understand and be responsible for everything that is submitted. Beyond working with a partner, all the usual [collaboration policies](../collaboration_cheating.html) apply.

Read [What to Turn In](#Turnin) before you submit — poor submissions may lose points.

[Turn in your assignment here](https://catalyst.uw.edu/collectit/dropbox/kchq/36414)

## Provided Files

Download these files into a new directory:

*   <tt>[Graph.java](Graph.java)</tt> -- Graph interface. _Do **not** modify._
*   <tt>[Vertex.java](Vertex.java)</tt> -- Vertex class
*   <tt>[Edge.java](Edge.java)</tt> -- Edge class
*   <tt>[MyGraph.java](MyGraph.java)</tt> -- Implementation of the <tt>Graph</tt> interface: you will need to fill in code here
*   <tt>[Path.java](Path.java)</tt> -- Class with two fields for returning the result of a shortest-path computation. _Do **not** modify._
*   <tt>[FindPaths.java](FindPaths.java)</tt> -- A client of the graph interface: Needs small additions
*   <tt>[vertext.txt](vertex.txt)</tt> and <tt>[edge.txt](edge.txt)</tt> -- an example graph in the correct input format

## Programming, Part 1

In this part of the assignment, you will implement a graph representation that you will use in Part 2\. Add code to the provided-but-incomplete <tt>MyGraph</tt> class to implement the <tt>Graph</tt> interface. Do not change the arguments to the constructor of <tt>MyGraph</tt> and do not add other constructors. Otherwise, you are free to add things to the <tt>Vertex</tt>, <tt>Edge</tt>, and <tt>MyGraph</tt> classes, but please do not remove code already there and do not modify <tt>Graph.java</tt>. You may also create other classes if you find it helpful.

As always, your code should be correct (implement a graph) and efficient (in particular, good asymptotic complexity for the requested operations), so choose a good graph representation for computing shortest paths in Part 2.

We will also grade your graph representation on how well it protects its abstraction from bad clients. In particular this means:

*   The constructor should check that the arguments make sense and throw an appropriate exception otherwise. You can define your own exceptions if you see fit. A couple possible places to check for exception:
    *   The edges should involve only vertices with labels that are in the vertices of the graph. That is, there should be no edge from or to a vertex labeled A if there is no vertex with label A.
    *   Edge weights should not be negative.
    *   Do _not_ throw an exception if the collection of vertices has repeats in it: If two vertices in the collection have the same label, just ignore the second one encountered as redundant information.
    *   _Do_ throw an exception if the collection of edges has the same _directed_ edge more than once with a different weight. Remember in a directed graph an edge from A to B is _not_ the same as an edge from B to A. Do _not_ throw an exception if an edge appears redundantly with the same weight; just ignore the redundant edge information.
*   As discussed in class, it should not be possible for clients of a graph to break the abstraction by adding edges, making illegal weights, etc. So the code most have enough copy-in-copy-out and immutability to prevent clients from doing such things.

Other useful information:

*   The <tt>Vertex</tt> and <tt>Edge</tt> classes have already defined an appropriate <tt>equals</tt> method (and, therefore, as we discussed in class, they also define <tt>hashCode</tt> appropriately). If you need to decide if two <tt>Vertex</tt> objects are "the same", you probably want to use the <tt>equals</tt> method and not <tt>==</tt>.
*   You will likely want some sort of [Map](http://docs.oracle.com/javase/7/docs/api/java/util/Map.html) in your program so you can easily and efficiently look up information stored about some <tt>Vertex</tt>. (This would be much more efficient than, for example, having a <tt>Vertex[]</tt> and iterating through it every time you needed to look for a particular <tt>Vertex</tt>.)
*   As you are debugging your program, you may find it useful to print out your data structures. There are <tt>toString</tt> methods for <tt>Edge</tt> and <tt>Vertex</tt>. Remember that things like <tt>ArrayList</tt>s and <tt>Set</tt>s can also be printed.

## Programming, Part 2

In this part of the assignment, you will use your graph from Part 1 to compute shortest paths. The <tt>MyGraph</tt> class has a method <tt>shortestPath</tt> you should implement to return the lowest-cost path from its first argument to its second argument. Return a <tt>Path</tt> object as follows:

*   If there is no path, return <tt>null</tt>.
*   If the start and end vertex are equal, return a path containing one vertex and a cost of 0.
*   Otherwise, the path will contain at least two vertices -- the start and end vertices and any other vertices along the lowest-cost path. The vertices should be in the order they appear on the path.

Because you know the graph contains no negative-weight edges, Dijkstra's algorithm is what you should implement. Additional implementation notes:

*   One convenient way to represent infinity is with <tt>Integer.MAX_VALUE</tt>.
*   Using a priority queue is above-and-beyond. You are not required to use a priority queue for this assignment. Feel free to use any structure you would like to keep track of distances and then search it to find the one with the smallest distance that is also unknown.
*   You definitely need to be careful to use <tt>equals</tt> instead of <tt>==</tt> to compare <tt>Vertex</tt> objects. The way the <tt>FindPaths</tt> class works (see below) is to create multiple <tt>Vertex</tt> objects for the same graph vertex as it reads input files. You may want to refer to your old notes on the <tt>equals</tt> method from CSE143\. Remember that <tt>equals</tt> lets us compare values (e.g. do two <tt>Vertex</tt> objects have the same label) as opposed to just checking if two things refer to the exact same object.

The program in <tt>FindPaths.java</tt> is _mostly_ provided to you. When the program begins execution, it reads two data files and creates a representation of the graph. It then prints out the graph's vertices and edges, which can be helpful for debugging to help ensure that the graph has been read and stored properly. Once the graph has been built, the program loops repeatedly and allows the user to ask shortest-path questions by entering two vertex names. **The part you need to add is to take these vertex names, call <tt>shortestPath</tt>, and print out the result.** Your output should be as follows:

*   If the start and end vertices are X and Y, first print a line <tt>Shortest path from X to Y:</tt>
*   If there is no path from the start to end vertex, print exactly one more line <tt>does not exist</tt>
*   Else print exactly two more lines. On the first additional line, print the path with vertices separated by spaces. For example, you might print <tt>X Foo Bar Baz Y</tt>. (Do not print a period, that is just ending the sentence.) On the second additional line, print the cost of the path (i.e., just a single number).

The <tt>FindPaths</tt> code expects two input files in a particular format. The names of the files are passed as command-line arguments. The provided files <tt>vertex.txt</tt> and <tt>edge.txt</tt> have the right formate to serve as one (small) example data set where the vertices are 3-letter airport codes. Here is the file format:

*   The file of vertices (the first argument to the program) has one line per vertex and each line contains a string with the name of a vertex.
*   The file of edges (the second argument to the program) has three lines per directed edge (so lines 1-3 describe the first edge, lines 4-6 describe the second edge, etc.) The first line gives the source vertex. The second line gives the destination vertex. The third line is a string of digits that give the weight of the edge (this line should be converted to a number to be stored in the graph).

Note data files represent directed graphs, so if there is an edge from A to B there may or may not be an edge from B to A. Moreover, if there is an edge from A to B and an edge from B to A, the edges may or may not have the same weight.

## Write-Up Questions

1.  Describe the worst-case asymptotic running times of your methods <tt>adjacentVertices</tt>, <tt>edgeCost</tt>, and <tt>shortestPath</tt>. In your answers, use <tt>|E|</tt> for the number of edges and <tt>|V|</tt> for the number of vertices. _Explain and justify your answers._
2.  Describe how your code ensures the graph abstraction is protected. If your code makes extra copies of objects for (only) this purpose, explain why. If not, explain why it is not necessary.
3.  Describe how you tested your code.
4.  **If you worked with a partner**, describe how you worked together. If you divided up the tasks, explain how you did so. If you worked on parts together, describe the actual process. Discuss how much time you worked together and how you spent that time (planning, coding, testing, ...). _Be sure to describe at least one good thing and one bad thing about the process of working with a partner._
5.  If you did any above-and-beyond, describe what you did.

## Above and Beyond

*   Find an interesting real-world data set and convert it into the right format for your program. Describe in your write-up questions what the data is and what a shortest path means. Turn in your data set in the right format as two additional files.
*   Improve your implementation of Dijkstra's algorithm by using a priority queue. Note that for Dijkstra's algorithm, we need to find items in the priority queue and update their priorities (the <tt>decreaseKey</tt> operation). We would like to find items in constant time (and then logarithmic time for changing the priority). There are various ways to do this, including keeping a back-pointer from each vertex to its entry in the priority queue. Note: If you implement this above and beyond, you are not required to also implement Dijkstra's without a priority queue. You may submit the priority queue version as your only submission, but be sure to indicate in your write-up that you did this.
*   Extend <tt>MyGraph</tt> with a method for computing minimum spanning trees using one of the efficient algorithms discussed in class. Also write a driver program that reads in a graph and prints a minimum spanning tree. This driver will be much like <tt>FindPaths</tt>, but make a separate file and do not prompt the user for vertices or have a loop -- just print one minimum spanning tree. Explain in your write-up the format of what you print.

## What to Turn In

If you work with a partner, only one partner should submit the files. Be sure to list both partners' names in your files.

You will turn in everything electronically. This should include **all your code files**, including the files provided to you (but you do not need to turn in <tt>vertex.txt</tt> and <tt>edge.txt</tt>). This should also include a file with your write-up; do not forget to **turn in your write-up**.

* * *

[![Valid CSS!](http://jigsaw.w3.org/css-validator/images/vcss)](http://jigsaw.w3.org/css-validator/check/referer) [![Valid XHTML 1.1](http://www.w3.org/Icons/valid-xhtml11-blue)](http://validator.w3.org/check?uri=referer)
