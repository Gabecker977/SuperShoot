package World;

public class Node {

	public Vector2i tile;
	public Node parent;
	public double gCost, fCost, hCost;
	
	public Node(Vector2i tile, Node parent, double gCost,double hCost) {
		this.tile = tile;
		this.parent = parent;
		this.gCost = gCost;
		this.hCost = hCost;
		this.fCost = gCost + hCost;
	}
}
