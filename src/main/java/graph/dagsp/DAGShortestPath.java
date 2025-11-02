package graph.dagsp;

import graph.metrics.Metrics;
import graph.model.Edge;
import graph.model.Graph;
import graph.topo.KahnTopologicalSort;

import java.util.*;
public class DAGShortestPath {
    private final Graph graph;
    private final Metrics metrics;

    public DAGShortestPath(Graph graph) {
        this.graph = graph;
        this.metrics = new Metrics();
    }
    public PathResult findShortestPaths(int source) {
        return findPaths(source, true);
    }
    public PathResult findLongestPaths(int source) {
        return findPaths(source, false);
    }

    private PathResult findPaths(int source, boolean shortest) {
        int n = graph.getVertices();
        KahnTopologicalSort topoSort = new KahnTopologicalSort(graph);
        List<Integer> topologicalOrder = topoSort.sort();

        if (topologicalOrder == null) {
            throw new IllegalArgumentException("Graph contains cycles - not a DAG!");
        }
        int[] dist = new int[n];
        int[] parent = new int[n];
        Arrays.fill(dist, shortest ? Integer.MAX_VALUE : Integer.MIN_VALUE);
        Arrays.fill(parent, -1);
        dist[source] = 0;

        metrics.reset();
        metrics.startTiming();

        for (int u : topologicalOrder) {
            if (dist[u] != (shortest ? Integer.MAX_VALUE : Integer.MIN_VALUE)) {
                for (Edge edge : graph.getEdges(u)) {
                    int v = edge.to;
                    int newDist = dist[u] + edge.weight;

                    metrics.incrementRelaxations();

                    boolean shouldUpdate = shortest
                            ? (newDist < dist[v])
                            : (newDist > dist[v]);

                    if (shouldUpdate) {
                        dist[v] = newDist;
                        parent[v] = u;
                    }
                }
            }
        }

        metrics.stopTiming();

        return new PathResult(dist, parent, source, shortest);
    }

    public Metrics getMetrics() {
        return metrics;
    }
    public static class PathResult {
        public final int[] distances;
        public final int[] parents;
        public final int source;
        public final boolean isShortest;

        public PathResult(int[] distances, int[] parents, int source, boolean isShortest) {
            this.distances = distances;
            this.parents = parents;
            this.source = source;
            this.isShortest = isShortest;
        }
        public List<Integer> getPath(int target) {
            if (distances[target] == Integer.MAX_VALUE ||
                    distances[target] == Integer.MIN_VALUE) {
                return null;
            }

            LinkedList<Integer> path = new LinkedList<>();
            for (int v = target; v != -1; v = parents[v]) {
                path.addFirst(v);
            }
            return path;
        }
        public int getCriticalPathTarget() {
            int maxDist = Integer.MIN_VALUE;
            int target = -1;
            for (int i = 0; i < distances.length; i++) {
                if (distances[i] != Integer.MIN_VALUE && distances[i] > maxDist) {
                    maxDist = distances[i];
                    target = i;
                }
            }
            return target;
        }

        public void printPaths() {
            System.out.println("\n=== " + (isShortest ? "Shortest" : "Longest") + " Paths from source " + source + " ===");
            for (int i = 0; i < distances.length; i++) {
                if (distances[i] != Integer.MAX_VALUE &&
                        distances[i] != Integer.MIN_VALUE) {
                    System.out.printf("To vertex %d: distance=%d, path=%s\n",
                            i, distances[i], getPath(i));
                }
            }

            if (!isShortest) {
                int criticalTarget = getCriticalPathTarget();
                if (criticalTarget != -1) {
                    System.out.println("CRITICAL PATH");
                    System.out.printf("From %d to %d: length=%d, path=%s\n",
                            source, criticalTarget, distances[criticalTarget], getPath(criticalTarget));
                }
            }
        }
    }
}
