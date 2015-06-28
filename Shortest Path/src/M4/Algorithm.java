package M4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Algorithm {
	private final List<Vertex<?>> vertex;
	private final List<Edge> edges;
	private Set<Vertex<?>> settledVertex;
	private Set<Vertex<?>> unSettledVertex;
	private Map<Vertex<?>, Vertex<?>> predecessors;
	private Map<Vertex<?>, Integer> distance;

	public Algorithm(Graph<?> graph) {
		this.vertex = new ArrayList<Vertex<?>>(graph.getVertexes());
		this.edges = new ArrayList<Edge>(graph.getEdges());
	}

	public void execute(Vertex<?> source) {
		settledVertex = new HashSet<Vertex<?>>();
		unSettledVertex = new HashSet<Vertex<?>>();
		distance = new HashMap<Vertex<?>, Integer>();
		predecessors = new HashMap<Vertex<?>, Vertex<?>>();
		distance.put(source, 0);
		unSettledVertex.add(source);
		while (unSettledVertex.size() > 0) {
			Vertex<?> node = getMinimum(unSettledVertex);
			settledVertex.add(node);
			unSettledVertex.remove(node);
			findMinimalDistances(node);
		}
	}

	private void findMinimalDistances(Vertex<?> node) {
		List<Vertex<?>> adjacentVertex = getNeighbors(node);
		for (Vertex<?> target : adjacentVertex) {
			if (getShortestDistance(target) > getShortestDistance(node)
					+ getDistance(node, target)) {
				distance.put(target,
						getShortestDistance(node) + getDistance(node, target));
				predecessors.put(target, node);
				unSettledVertex.add(target);
			}
		}
	}

	private int getDistance(Vertex<?> node, Vertex<?> target) {
		for (Edge edge : edges) {
			if (edge.getStarting().equals(node)
					&& edge.getDestination().equals(target)) {
				return edge.getWeight();
			}
		}
		throw new RuntimeException("Error");
	}

	private List<Vertex<?>> getNeighbors(Vertex<?> node) {
		List<Vertex<?>> neighbors = new ArrayList<Vertex<?>>();
		for (Edge edge : edges) {
			if (edge.getStarting().equals(node)
					&& !isSettled(edge.getDestination())) {
				neighbors.add(edge.getDestination());
			}
		}
		return neighbors;
	}

	private Vertex<?> getMinimum(Set<Vertex<?>> vertexes) {
		Vertex<?> minimum = null;
		for (Vertex<?> vertex : vertexes) {
			if (minimum == null) {
				minimum = vertex;
			} else {
				if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
					minimum = vertex;
				}
			}
		}
		return minimum;
	}

	private boolean isSettled(Vertex<?> vertex) {
		return settledVertex.contains(vertex);
	}

	private int getShortestDistance(Vertex<?> minimum) {
		Integer d = distance.get(minimum);
		if (d == null) {
			return Integer.MAX_VALUE;
		} else {
			return d;
		}
	}

	// Returns path or 'null' for non-existing path.
	public LinkedList<Vertex<?>> getPath(Vertex<?> target) {
		LinkedList<Vertex<?>> path = new LinkedList<Vertex<?>>();
		Vertex<?> step = target;

		if (predecessors.get(step) == null) {
			return null;
		}
		path.add(step);
		while (predecessors.get(step) != null) {
			step = predecessors.get(step);
			path.add(step);
		}
		Collections.reverse(path);
		return path;
	}

	// Calculating the total distance of a path, the starting and connecting
	// nodes, and the edges between them.

	public ArrayList<String> getShortestDistanceFromSToT(Graph<?> graph,
			LinkedList<Vertex<?>> path) {
		Integer totalDistance = 0;
		ArrayList<String> stepRoute = new ArrayList();
		Edge edgeTemp;

		for (int i = 0; i < path.size() - 1; i++) {
			totalDistance += getDistance(path.get(i), path.get(i + 1));
		}
		stepRoute.add(0, totalDistance.toString());
		stepRoute.add("\nStarting at: ");
		
		for (int i = 0; i < path.size() - 1; i++) {
			stepRoute.add("\nNode: " + path.get(i).getName());

			for (Edge e : graph.getEdges()) {
				edgeTemp = e;

				if (edgeTemp.getStarting().equals(path.get(i))
						&& edgeTemp.getDestination().equals(path.get(i + 1))) {
					stepRoute.add(String.format("\nThrough %s, Weight: %d to:",
							edgeTemp.getId(), edgeTemp.getWeight()));
				}
			}
			if (i == (path.size() - 2)) {
				stepRoute.add("\nFinal node: " + path.get(i + 1).getName());
			}
		}
		return stepRoute;
	}
}