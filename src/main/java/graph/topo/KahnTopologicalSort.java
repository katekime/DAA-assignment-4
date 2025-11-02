package graph.topo;

import graph.metrics.Metrics;
import graph.model.Graph;

import java.util.*;

public class KahnTopologicalSort {
    private final Graph graph;
    private final Metrics metrics;

    public KahnTopologicalSort(Graph graph) {
        this.graph = graph;
        this.metrics = new Metrics();
    }
    public List<Integer> sort() {
        int n = graph.getVertices();
        int[] inDegree = new int[n];
        for (int v = 0; v < n; v++) {
            for (var edge : graph.getEdges(v)) {
                inDegree[edge.to]++;
            }
        }
        Queue<Integer> queue = new LinkedList<>();
        for (int v = 0; v < n; v++) {
            if (inDegree[v] == 0) {
                queue.offer(v);
                metrics.incrementStackPushes();
            }
        }

        List<Integer> topologicalOrder = new ArrayList<>();

        metrics.reset();
        metrics.startTiming();

        while (!queue.isEmpty()) {
            int v = queue.poll();
            topologicalOrder.add(v);
            metrics.incrementStackPops();
            metrics.incrementDfsVisits();
            for (var edge : graph.getEdges(v)) {
                int w = edge.to;
                inDegree[w]--;
                metrics.incrementEdgeTraversals();

                if (inDegree[w] == 0) {
                    queue.offer(w);
                    metrics.incrementStackPushes();
                }
            }
        }

        metrics.stopTiming();
        if (topologicalOrder.size() != n) {
            return null;
        }

        return topologicalOrder;
    }

    public Metrics getMetrics() {
        return metrics;
    }

    public void printTopologicalOrder(List<Integer> order) {
        if (order == null) {
            System.out.println("Graph contains cycles - no topological order exists!");
        } else {
            System.out.println("Topological Order");
            System.out.println(order);
        }
    }
}
