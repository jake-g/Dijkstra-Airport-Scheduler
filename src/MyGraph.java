import java.util.*;
/**
 CSE373 HW 4
 Jake Garrison, NetID: omonoid
 Raymond Mui, NETID: rmui34


 * A representation of a graph.
 * Assumes that we do not have negative cost edges in the graph.
 */
public class MyGraph implements Graph {
    private Collection<Vertex> vert;
    private Collection<Edge> edge;
    private HashMap<Vertex, Set<Edge>> vertMap;
    private Set<Vertex> unknown;
    private Map<Vertex, Integer> costTracker;
    private List<Vertex> path;
    private Map<Vertex, Vertex> lastVert;


    /**
     * Creates a MyGraph object with the given collection of vertices
     * and the given collection of edges.
     *
     * @param v a collection of the vertices in this graph
     * @param e a collection of the edges in this graph
     */
    public MyGraph(Collection<Vertex> v, Collection<Edge> e) {

        vertMap = new HashMap<Vertex, Set<Edge>>();
        unknown = new HashSet<Vertex>();
        costTracker = new HashMap<Vertex, Integer>();
        path = new ArrayList<Vertex>();
        lastVert = new HashMap<Vertex, Vertex>();

        // Create copies to protect abstraction and maintain immutability
        Collection<Vertex> vCopy = new ArrayList<Vertex>();
        Collection<Edge> eCopy = new ArrayList<Edge>();
        vCopy.addAll(v);    // copies the v collection
        v.clear();      // clear original vert ref now that it is copied
        eCopy.addAll(e);    // copies the e collection
        e.clear();      // clear original edge ref now that it is copied

        // Prevents repeat vertices from being in the Collection<Vertex>
        Set<Vertex> vSet = new HashSet<Vertex>();
        vSet.addAll(vCopy);
        if (vCopy.size() != vSet.size()) {
            vCopy.clear();
            vCopy.addAll(vSet);
        }

        // Prevents repeat edges from being in the Collection<Edges>
        Set<Edge> eSet = new HashSet<Edge>();
        eSet.addAll(eCopy);
        if (eCopy.size() != eSet.size()) {
            eCopy.clear();
            eCopy.addAll(eSet);
        }

        // Check for issues in edge set
        List eList = new ArrayList<Edge>();
        eList.addAll(eCopy);
        Collections.sort(eList,ALPHABETICAL_ORDER); // sort alphabetically
        eCopy.clear();
        eCopy.addAll(eList);
        Boolean first = true; // counter used to get the first edge
        Edge prev = null;
        for (Edge ed : eCopy) { //for each edge in Collection of Edges
            if (first) {
                prev = ed;  // set first prev
                first = false;
            }
            // checks if source and dest vertices are contained in vertex collection
            else if(!vCopy.contains(ed.getSource()) || !vCopy.contains(ed.getDestination())){
                throw new IllegalArgumentException("[ERROR] Edge links non-existent  vertices");
            }
            else {
                // Check if duplicate edges have different costs
                if (ed.getWeight() != prev.getWeight() && ed.getDestination().equals(prev.getDestination())
                        && ed.getSource().equals(prev.getSource())) {
                    throw new IllegalArgumentException("[ERROR] Found duplicate directed edges with different weights.");
                }
                prev = ed;
            }
        }

        this.edge = eCopy;
        this.vert = vCopy;
        placeInMap();
    }

    /*
    Makes an Alphabetical Comparator for edges
     */
    private static Comparator<Edge> ALPHABETICAL_ORDER = new Comparator<Edge>() {
        /*
        Compares to Edges and returns the Edge in alphabetical order based on the source vertex.
        @param e1 is an Edge , e2 is an Edge in the collection being compared
        @return an int specifying which Edge is alphabetically first.
         */
        public int compare(Edge e1, Edge e2) {
            int res = String.CASE_INSENSITIVE_ORDER.compare(e1.getSource().toString(), e2.getSource().toString());
            if (res == 0) {
                res = e1.getSource().toString().compareTo(e2.getSource().toString());
            }
            return res;
        }
    };

    /**
     * Return the collection of vertices of this graph
     *
     * @return the vertices as a collection (which is anything iterable)
     */
    public Collection<Vertex> vertices() {
        return vert;
    }


    /**
     * Return the collection of edges of this graph
     *
     * @return the edges as a collection (which is anything iterable)
     */
    public Collection<Edge> edges() {
        return edge;
    }


    /**
     * Builds map where keys are vertices and values are a set of Edges
     */
    private void placeInMap() {
        Iterator<Edge> itrE = edge.iterator();
        while (itrE.hasNext()) {
            Edge e = itrE.next();
            Vertex source = e.getSource();
            Set<Edge> mapSet = new HashSet<Edge>();

            if (!vertMap.containsKey(source)) {     // add new dest to source
                mapSet.add(e);
                vertMap.put(source, mapSet);

            } else{      // append dest to existing source
                Set<Edge> append = vertMap.get(source);
                append.add(e);
            }
        }
    }


    /**
     * Return a collection of vertices adjacent to a given vertex v.
     * i.e., the set of all vertices w where edges v -> w exist in the graph.
     * Return an empty collection if there are no adjacent vertices.
     *
     * @param v one of the vertices in the graph
     * @return an iterable collection of vertices adjacent to v in the graph
     * @throws IllegalArgumentException if v does not exist.
     */
    public Collection<Vertex> adjacentVertices(Vertex v) {
        if (v == null) {
            throw new IllegalArgumentException();
        }

        Collection<Vertex> adjacent = new ArrayList<Vertex>();
        if (!vertMap.containsKey(v)) {
            return adjacent;
        }
            Iterator<Edge> it = vertMap.get(v).iterator();
            while (it.hasNext()) {
                Edge e = it.next();
                adjacent.add(e.getDestination());
            }

        return adjacent;
    }


    /**
     * Test whether vertex b is adjacent to vertex a (i.e. a -> b) in a directed graph.
     * Assumes that we do not have negative cost edges in the graph.
     *
     * @param a one vertex
     * @param b another vertex
     * @return cost of edge if there is a directed edge from a to b in the graph,
     * return Interger.MAX_Value otherwise.
     * @throws IllegalArgumentException if a or b do not exist.
     */
    public int edgeCost(Vertex a, Vertex b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException();
        }
        Iterator<Edge> it = vertMap.get(a).iterator();
        while (it.hasNext()) {
            Edge e = it.next();
            if (e.getDestination().equals(b)) {
                return e.getWeight();
            }
        }
        return Integer.MAX_VALUE;
    }


    /*
     * Returns the shortest path from a to b in the graph, or null if there is
     * no such path.  Assumes all edge weights are nonnegative.
     * Uses Dijkstra's algorithm.
     * @param a the starting vertex
     * @param b the destination vertex
     * @return a Path where the vertices indicate the path from a to b in order
     *   and contains a (first) and b (last) and the cost is the cost of
     *   the path. Returns null if b is not reachable from a.
     * @throws IllegalArgumentException if a or b does not exist.
     */
    public Path shortestPath(Vertex a, Vertex b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException();
        }

        // Clear data structures for next query
        path.clear();
        costTracker.clear();
        lastVert.clear();
        // Initialize Data Structures
        path.add(b);    // Append destination to path
        costTracker.put(a, 0); // Set source cost to 0
        lastVert.put(a, null);  // Set source's last vertex to null
        unknown.addAll(vertices());     // Add all to unknown

        // Run dijkstra
        int cost = dijkstra(a, b);

        List<Vertex> pathList = pathTraversal(b);  // reverse order
        Collections.reverse(pathList);          // correct order
        List<Vertex> pathCopy = new ArrayList<Vertex>();
        pathCopy.addAll(pathList);      // copy out for immutability
        return new Path(pathCopy,cost);
    }


    /*Preforms dijkstra's algorithm recursively
    Keeps track of the overall cost of the path travelled
    Keeps track of the last path visited
    @param takes in the current vertex of the graph and then destination  vertex
    @return recursive call on the minimum cost adjacent Vertex
    */
    private int dijkstra(Vertex curr, Vertex dest) {
        if (unknown.isEmpty()) {
            return costTracker.get(dest);
        }

        Vertex minVert = null;
            //Checks if current vertex is null
            if (curr != null) {
                for (Vertex v : adjacentVertices(curr)) {// Update cost map with adj verts
                    int cost = edgeCost(curr, v) + costTracker.get(curr);   // Sum of costs to reach this vertex
                    if (costTracker.containsKey(v) && costTracker.get(v) < cost) {
                        continue;   // do nothing...
                    } else {
                        costTracker.put(v, cost);
                        lastVert.put(v, curr);
                    }
                }
            }

            unknown.remove(curr);       // Curr node is now known

        // Find min cost vertex to jump to
        int min = Integer.MAX_VALUE;
        for (Map.Entry<Vertex, Integer> entry : costTracker.entrySet()) {
            if (entry.getValue() < min && unknown.contains(entry.getKey())) {
                min = entry.getValue();
                minVert = entry.getKey();
            }
        }

        // Check for dead end
        if (min == Integer.MAX_VALUE) { // reached dead end
            if (unknown.contains(dest)) {
                return -1;       // no path
            } else {
                return costTracker.get(dest);   // dead end is dest!
            }
        }
        return dijkstra(minVert, dest);
    }


    /* Backtraces path  from destination and stores each vertex
       @param takes in a vertex to back track from (backtrack from destination vertex)
       @return a list of vertices of the shortest path taken
    */
    private List<Vertex> pathTraversal(Vertex vert) {
        if (lastVert.get(vert) == null) {   // base case, when we reach start point
            return path;
        }
        path.add(lastVert.get(vert));
        return pathTraversal(lastVert.get(vert));
    }
}

