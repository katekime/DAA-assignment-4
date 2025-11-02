package graph.scc;

import graph.model.Graph;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TarjanSCCTest {

    @Test
    void testSimpleDAG() {
        Graph graph = new Graph(4);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);

        TarjanSCC tarjan = new TarjanSCC(graph);
        List<List<Integer>> sccs = tarjan.findSCCs();

        assertEquals(4, sccs.size());
    }

    @Test
    void testOneCycle() {
        Graph graph = new Graph(3);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(2, 0);

        TarjanSCC tarjan = new TarjanSCC(graph);
        List<List<Integer>> sccs = tarjan.findSCCs();

        assertEquals(1, sccs.size());
        assertEquals(3, sccs.get(0).size());
    }

    @Test
    void testMultipleSCCs() {
        Graph graph = new Graph(7);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(2, 0);
        graph.addEdge(3, 4);
        graph.addEdge(4, 3);
        graph.addEdge(5, 6);

        TarjanSCC tarjan = new TarjanSCC(graph);
        List<List<Integer>> sccs = tarjan.findSCCs();

        assertEquals(4, sccs.size());
    }

    @Test
    void testSingleVertex() {
        Graph graph = new Graph(1);

        TarjanSCC tarjan = new TarjanSCC(graph);
        List<List<Integer>> sccs = tarjan.findSCCs();

        assertEquals(1, sccs.size());
        assertEquals(1, sccs.get(0).size());
    }
}
