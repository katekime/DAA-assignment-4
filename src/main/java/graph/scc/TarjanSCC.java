package graph.scc;

import graph.metrics.Metrics;
import graph.model.Graph;

import java.util.*;
public class TarjanSCC {
    private final Graph graph;
    private final Metrics metrics;

    private int index;
    private int[] indexes;
    private int[] lowlinks;
    private boolean[] onStack;
    private Stack<Integer> stack;
    private List<List<Integer>> sccList;

    public TarjanSCC(Graph graph) {
        this.graph = graph;
        this.metrics = new Metrics();
    }
    public List<List<Integer>> findSCCs() {
        int n = graph.getVertices();
        index = 0;
        indexes = new int[n];
        lowlinks = new int[n];
        onStack = new boolean[n];
        stack = new Stack<>();
        sccList = new ArrayList<>();

        Arrays.fill(indexes, -1);

        metrics.reset();
        metrics.startTiming();

        for (int v = 0; v < n; v++) {
            if (indexes[v] == -1) {
                strongConnect(v);
            }
        }

        metrics.stopTiming();

        return sccList;
    }

    private void strongConnect(int v) {
        indexes[v] = index;
        lowlinks[v] = index;
        index++;
        stack.push(v);
        onStack[v] = true;

        metrics.incrementDfsVisits();
        metrics.incrementStackPushes();

        for (var edge : graph.getEdges(v)) {
            int w = edge.to;
            metrics.incrementEdgeTraversals();

            if (indexes[w] == -1) {
                strongConnect(w);
                lowlinks[v] = Math.min(lowlinks[v], lowlinks[w]);
            } else if (onStack[w]) {
                lowlinks[v] = Math.min(lowlinks[v], indexes[w]);
            }
        }
        if (lowlinks[v] == indexes[v]) {
            List<Integer> scc = new ArrayList<>();
            int w;
            do {
                w = stack.pop();
                onStack[w] = false;
                scc.add(w);
                metrics.incrementStackPops();
            } while (w != v);

            sccList.add(scc);
        }
    }

    public Metrics getMetrics() {
        return metrics;
    }
    public void printSCCs(List<List<Integer>> sccs) {
        System.out.println("Strongly Connected Components");
        for (int i = 0; i < sccs.size(); i++) {
            List<Integer> scc = sccs.get(i);
            System.out.printf("SCC %d (size=%d): %s\n", i, scc.size(), scc);
        }
        System.out.println("Total SCCs: " + sccs.size());
    }
}
