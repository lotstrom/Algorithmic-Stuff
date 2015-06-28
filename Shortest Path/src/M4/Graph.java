package M4;

import java.util.ArrayList;
import java.util.List;

public class Graph<L> {
	private final List<Vertex<L>> vertexes;
	private final List<Edge> edges;

	public Graph() {
		this.vertexes = new ArrayList<Vertex<L>>();
		this.edges = new ArrayList<Edge>();
	}

	public Graph(List<Vertex<L>> vertexes, List<Edge> edges) {
		this.vertexes = vertexes;
		this.edges = edges;
	}

	public void addVertex(Vertex<L> vertex) {
		vertexes.add(vertex);
	}

	public void addEdge(Edge edge) {
		edges.add(edge);
	}

	public Vertex<L> getVertex(String id) {
		for (Vertex<L> v : vertexes) {
			if (v.getId().equals(id)) {
				return v;
			}
		}
		return null;
	}

	public Edge getEdge(String id) {
		for (Edge e : edges) {
			if (e.getId().equals(id)) {
				return e;
			}
		}
		return null;
	}

	public List<Vertex<L>> getVertexes() {
		return vertexes;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public void replaceEdge(Edge edge) {
		if (edge.equals(getEdge(edge.getId()))) {
			edges.remove(getEdge(edge.getId()));
			edges.add(edge);
		}
	}

	public Integer getNrOfVertexes() {
		return vertexes.size();
	}

	public Integer getNrOfEdges() {
		return edges.size();
	}
}
