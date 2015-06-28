package M4;

public class Edge {
	private String id;
	private Vertex<?> starting;
	private Vertex<?> destination;
	private int weight;

	public Edge(String id, Vertex<?> starting, Vertex<?> destination, int weight) {
		this.id = id;
		this.starting = starting;
		this.destination = destination;
		this.weight = weight;
	}
	public Edge(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}
	public Vertex<?> getDestination() {
		return destination;
	}
	public Vertex<?> getStarting() {
		return starting;
	}
	public void setDestination(Vertex<?> des) {		
		this.destination = des;
	}
	public void setStarting(Vertex<?> start) {
		this.starting = start;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	@Override
	public String toString() {
		return starting + " " + destination;
	}
}
