package graph.model;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private final int vertices;
    private final List<List<Edge>> adjacencyList;

    public Graph(int vertices) {
        this.vertices = vertices;
        this.adjacencyList = new ArrayList<>(vertices);
        for (int i = 0; i < vertices; i++) {
            adjacencyList.add(new ArrayList<>());
        }
    }
    public void addEdge(int from, int to, int weight) {
        adjacencyList.get(from).add(new Edge(from, to, weight));
    }
    public void addEdge(int from, int to) {
        addEdge(from, to, 1);
    }

    public int getVertices() {
        return vertices;
    }

    public List<Edge> getEdges(int vertex) {
        return adjacencyList.get(vertex);
    }

    public List<List<Edge>> getAdjacencyList() {
        return adjacencyList;
    }
    public int getEdgeCount() {
        int count = 0;
        for (List<Edge> edges : adjacencyList) {
            count += edges.size();
        }
        return count;
    }
    public Graph transpose() {
        Graph reversed = new Graph(vertices);
        for (int v = 0; v < vertices; v++) {
            for (Edge edge : adjacencyList.get(v)) {
                reversed.addEdge(edge.to, edge.from, edge.weight);
            }
        }
        return reversed;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Graph(").append(vertices).append(" vertices, ")
                .append(getEdgeCount()).append(" edges)\n");
        for (int v = 0; v < vertices; v++) {
            sb.append(v).append(": ");
            for (Edge edge : adjacencyList.get(v)) {
                sb.append(edge.to).append("(w=").append(edge.weight).append(") ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
