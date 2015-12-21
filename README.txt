CSE373 HW 4
Jake Garrison, NetID: omonoid
Raymond Mui, NETID: rmui34

1.Describe the worst-case asymptotic running times of your methods adjacentVertices, edgeCost, and shortestPath.
In your answers, use |E| for the number of edges and |V| for the number of vertices. Explain and justify your answers.

adjacentVertices:
Our adjacentVertices relies on a predefined map where keys are vertices and values are an unsorted set of adjacent edges. Building this map in the placeInMap method is O(|E|) since it iterates through the edge collection and places edges in the map based off source. Calling adjacentVertices the gets the set of adjacent edges, iterates through each entry and simply calls edge.getDestination for the adjacent vertex to add to the returned collection. Map.get is O(1), and the set iteration is O(adjacent edges) =< O(|E|) since it must visit all edges in the set, but this relies on a predefined map with O(|E|), so the final runtime would be at worst O(|E|). If you don't count the preset map, it would be O(adjacent edges) =< O(|E|).

edgeCost:
to determine the cost from a to b, we use the map described above that placeInMap O(|E|) creates. We get the value (set of edges) for Vertex a, then iterate through the set to find an edge.getDestination equal to b. Then the weight of that edge is returned. Aside from iterating through the set which is at most O(adjacent edges) =< O(|E|), all the other operations to find the right edge are trivial O(1). Therefore the runtime is at worst O(|E|). If this Map()<Vertex>,<Set<Edges>>) used a sorted set (tree set), the iteration through the set would be reduced to find the needed edge. This, however wasn't implemented due to time constraints.

shortestPath:
The non trivial calls that shortestPath relies on are 1) dijkstras method and 2) pathTraversal. 1) Dijkstras makes a recursive call on each vertex and iterates through each adjacent vertex, preforms some simple map and set operations, and uses the adjacentVertices and edgeCost (defined above). Worst case for dijkstras method is O(|V||E|) since each vertex called recursively may have worst case E amount of edges to search through. 2) The path traversal has worst case O(|V|) if the path happened to be all vertices. This list needs to be reversed, but that is trivial given the runtime outlined above. Given all of this, the worst case is O(|V||E|).



2.Describe how your code ensures the graph abstraction is protected. If your code makes extra copies of objects for (only)
this purpose, explain why. If not, explain why it is not necessary.

We made all of our data structures holding graph information private and as many variables final as we could (to ensure immutability). In Java, when an object reference is passed to a method, the method will can not track who else might have a reference to that same object, or what they may to do with it. Additionally, if a method returns an object reference, there's no telling what the recipient might do with it. To prevent our object references from getting exposed to the client (breaking our abstraction) we created copies of the input MyGraph edge and vertex input. We also created a copy of the path returned so that the original path object wasn't exposed. With this flow, the information retrieval method would accept a mutable object and populate it with information received by the client. Creating copies obviously takes more memory and runtime to populate the copy, but ensures protected abstraction.



3.Describe how you tested your code.
We had several tests as we developed our code. First of all we manually computed several shortest paths and their costs. This way we were certain that our code outputted a correct result and got some practice. To test edge cases and exceptions, we edited the data set to have things like duplicate edges with same and different costs, costs of 0, negative and very large. We also tested different input sizes and scenarios where a vertex had no edges. To speed up testing and discover bugs, we automated the input by scanning the file (nested for loop) to test every possible source and destination. Though we didn’t manually compute all the possible paths, this testing exposed some bugs that we fixed related to us not clearing our data structures. Additionally for the above and beyond aspect, we created our  own graph of UW campus and this gave us insight on unaccounted for bugs and exceptions.



4.If you worked with a partner, describe how you worked together. If you divided up the tasks, explain how you did so.
If you worked on parts together, describe the actual process. Discuss how much time you worked together and how you spent
that time (planning, coding, testing, ...). Be sure to describe at least one good thing and one bad thing about the process
of working with a partner.
First as partners we broke down the specification and determined what needed to be completed. To get the shortestPath working
we decided to draw out the graph of the given data and work though the pseudo code together. Once we were on the same page, we switched off on our contributions until we had basic functionality. We utilized a git repo to keep things organized and synchronized. Once the basic code was working, we split and worked in parallel on bug testing, exception adding, the write-up, comments, ect. We also spent a lot of time looking over each other’s shoulder working on a single laptop.

This assignment was tricky and had a lot of room for error. We probably spent 10+ hours together and 5+ hours alone. Honestly for a 3 credit class this is a lot of work and I think the scope of these HW should slightly be truncated. Making along assignment a 'group' project doesn’t necessarily cut the workload in half. I expected this course to be more general about implementing several algorithms and data structures in no particular languages rather than worry about abstraction, clients and java specific stuff, but maybe I'm wrong (EEs don't need java or client code really and this course is required for DSP EE engineers). Im not sure the ratio of EE to iSchool, so I imagine it is hard to satisfy both concentrations.

We spent a long time planning and testing, but most of the time was spent wrestling through coding solutions to our problems. With two people writing the code, the purpose and method is not super obvious to the person who didn't write it. This is how it is in the real world, so It is good practice, I just would have like a bit more than a week to work on it. Not everyone can devote enough time, and in a group situation it can be frustrating when schedules are difficult to synchronize. Group work, when both people are functional, free, awake and focused can be great. Having another set of eyes or dishing out tasks in parallel can be incredibly effective, or detrimental when one is trying to follow another's code.

5. Above and Beyond
Not sure if this is above and beyond, but we prevented duplicate vertex or edges (same cost) from appearing in the original print statement. The spec said ignore, but we removed to make it clear to the client.
We used Google maps to create a mock up of UW campus connecting buildings and landmarks with cost set to be the approximate distance in (ft). This wasn't too difficult to generate using the Maps API and some python parsing (i wish this assignment was python..). These are stored in UW_edge.txt and UW_vertex.txt
