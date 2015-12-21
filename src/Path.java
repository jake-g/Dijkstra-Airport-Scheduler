import java.util.List;

/**
 CSE373 HW 4
 Jake Garrison, NetID: omonoid
 Raymond Mui, NETID: rmui34
 */

public class Path {
    // we use public fields fields here since this very simple class is
    // used only for returning multiple results from shortestPath
    public final List<Vertex> vertices;
    public final int cost;
    
    public Path(List<Vertex> vertices, int cost) {
	this.vertices = vertices;
	this.cost = cost;
    }

    public int getCost() {
        return cost;
    }

    public List<Vertex> getList() {
        return vertices;
    }
}
