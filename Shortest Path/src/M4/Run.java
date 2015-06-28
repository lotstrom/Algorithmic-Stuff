package M4;

import java.util.LinkedList;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Run extends DefaultHandler {
	
	private static PrintWriter writer;
	private Graph<String> graph = new Graph<String>();
	private String id;
	private Edge edge;
	private boolean isNode = false;
	private boolean isPath = false;
			
	public static void main(String args[]) {
		if (args.length == 0) {
			args = new String[3];
			args[0] = "graph-medium.xml";
			args[1] = "AS12182";
			args[2] = "AS13535";
		}
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			Run program = new Run();
			try {
				writer = new PrintWriter(new File("Result.txt"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			writer.flush();

			saxParser.parse(args[0], program);
			Graph<String> graph = program.getGraph();
			Algorithm dijkstra = new Algorithm(graph);

			writer.println("\nTotal number of nodes: " + graph.getNrOfVertexes());
			writer.println("Total number of edges: " + graph.getNrOfEdges());
			
			// Finding shortest path			
			dijkstra.execute(graph.getVertex(args[1].replace("AS", "")));
			LinkedList<Vertex<?>> path = dijkstra.getPath(graph
					.getVertex(args[2].replace("AS", "")));

			writer.println("\nThe shortest distance between " + args[1] + " and "
					+ args[2] + " is: ");
			writer.flush();
			
			for (String s : dijkstra
					.getShortestDistanceFromSToT(graph, path)) {
				writer.println(s);
				writer.flush();
			}
			writer.close();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		if (qName.equalsIgnoreCase("node")) {
			isNode = true;
			id = attributes.getValue("id");

			if (isPath) {
				if (edge.getStarting() == null) {
					edge.setStarting(graph.getVertex(id));
				} else {
					edge.setDestination(graph.getVertex(id));
				}
			}
		}
		// Adding edges
		if (qName.equalsIgnoreCase("edge")) {
			id = attributes.getValue("id");
			graph.addEdge(new Edge(id));
		}
		// Adding weight
		if (qName.equalsIgnoreCase("path")) {
			isPath = true;
			id = attributes.getValue("edge");
			edge = graph.getEdge(id);
			int weight = Integer.parseInt(attributes.getValue("weight"));
			edge.setWeight(weight);
			graph.replaceEdge(edge);
		}
	}
	// Adding nodes
	public void characters(char ch[], int start, int length)
			throws SAXException {
		if (isNode && !isPath) {
			String name = new String(ch, start, length);
			graph.addVertex(new Vertex<String>(id, name));
		}
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		if (qName.equalsIgnoreCase("node")) {
			isNode = false;
		}
		if (qName.equalsIgnoreCase("path")) {
			isPath = false;
		}
	}

	public Graph<String> getGraph() {
		return graph;
	}
}