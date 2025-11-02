package graph.topo;

import graph.model.Graph;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TopologicalSortTest {

    @Test
    void testSimpleDAG() {
        Graph graph = new Graph(4);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 3);

        KahnTopologicalSort topo = new KahnTopologicalSort(graph);
        List<Integer> order = topo.sort();

        assertNotNull(order);
        assertEquals(4, order.size());
        assertTrue(order.indexOf(0) < order.indexOf(1));
        assertTrue(order.indexOf(0) < order.indexOf(2));
        assertTrue(order.indexOf(1) < order.indexOf(3));
        assertTrue(order.indexOf(2) < order.indexOf(3));
    }

    @Test
    void testGraphWithCycle() {
        Graph graph = new Graph(3);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(2, 0);

        KahnTopologicalSort topo = new KahnTopologicalSort(graph);
        List<Integer> order = topo.sort();

        assertNull(order);
    }

    @Test
    void testLinearDAG() {
        Graph graph = new Graph(5);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);

        KahnTopologicalSort topo = new KahnTopologicalSort(graph);
        List<Integer> order = topo.sort();

        assertNotNull(order);
        assertEquals(List.of(0, 1, 2, 3, 4), order);
    }
}
