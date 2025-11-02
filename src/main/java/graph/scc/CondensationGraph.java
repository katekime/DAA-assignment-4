package graph.scc;

import graph.model.Edge;
import graph.model.Graph;

import java.util.*;
public class CondensationGraph {
    private final Graph originalGraph;
    private final List<List<Integer>> sccs;
    private final Map<Integer, Integer> vertexToSccMap;
    private Graph condensationGraph;

    public CondensationGraph(Graph originalGraph, List<List<Integer>> sccs) {
        this.originalGraph = originalGraph;
        this.sccs = sccs;
        this.vertexToSccMap = new HashMap<>();
        buildVertexToSccMap();
    }

    private void buildVertexToSccMap() {
        for (int sccId = 0; sccId < sccs.size(); sccId++) {
            for (int vertex : sccs.get(sccId)) {
                vertexToSccMap.put(vertex, sccId);
            }
        }
    }
    public Graph buildCondensation() {
        condensationGraph = new Graph(sccs.size());
        Set<String> addedEdges = new HashSet<>();

        for (int v = 0; v < originalGraph.getVertices(); v++) {
            int sccFrom = vertexToSccMap.get(v);

            for (Edge edge : originalGraph.getEdges(v)) {
                int sccTo = vertexToSccMap.get(edge.to);

                if (sccFrom != sccTo) {
                    String edgeKey = sccFrom + "->" + sccTo;
                    if (!addedEdges.contains(edgeKey)) {
                        condensationGraph.addEdge(sccFrom, sccTo, edge.weight);
                        addedEdges.add(edgeKey);
                    }
                }
            }
        }

        return condensationGraph;
    }

    public Graph getCondensationGraph() {
        return condensationGraph;
    }

    public int getSccId(int vertex) {
        return vertexToSccMap.get(vertex);
    }

    public void printCondensation() {
        System.out.println("Condensation Graph (DAG)");
        System.out.println("Vertices: " + condensationGraph.getVertices());
        System.out.println("Edges: " + condensationGraph.getEdgeCount());
        for (int v = 0; v < condensationGraph.getVertices(); v++) {
            if (!condensationGraph.getEdges(v).isEmpty()) {
                System.out.print("SCC " + v + " -> ");
                for (Edge edge : condensationGraph.getEdges(v)) {
                    System.out.print("SCC " + edge.to + " ");
                }
                System.out.println();
            }
        }
    }
}
