package graph.dagsp;

import graph.model.Graph;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DAGShortestPathTest {

    @Test
    void testShortestPath() {
        Graph graph = new Graph(4);
        graph.addEdge(0, 1, 2);
        graph.addEdge(0, 2, 6);
        graph.addEdge(1, 2, 3);
        graph.addEdge(1, 3, 5);
        graph.addEdge(2, 3, 1);

        DAGShortestPath dagSP = new DAGShortestPath(graph);
        DAGShortestPath.PathResult result = dagSP.findShortestPaths(0);

        assertEquals(0, result.distances[0]);
        assertEquals(2, result.distances[1]);
        assertEquals(5, result.distances[2]);
        assertEquals(6, result.distances[3]);

        List<Integer> path = result.getPath(3);
        assertNotNull(path);
        assertTrue(path.contains(0));
        assertTrue(path.contains(3));
    }

    @Test
    void testLongestPath() {
        Graph graph = new Graph(4);
        graph.addEdge(0, 1, 2);
        graph.addEdge(0, 2, 6);
        graph.addEdge(1, 2, 3);
        graph.addEdge(1, 3, 5);
        graph.addEdge(2, 3, 1);

        DAGShortestPath dagSP = new DAGShortestPath(graph);
        DAGShortestPath.PathResult result = dagSP.findLongestPaths(0);

        assertTrue(result.distances[3] >= 7);
    }

    @Test
    void testDisconnectedGraph() {
        Graph graph = new Graph(4);
        graph.addEdge(0, 1, 1);
        graph.addEdge(2, 3, 1);

        DAGShortestPath dagSP = new DAGShortestPath(graph);
        DAGShortestPath.PathResult result = dagSP.findShortestPaths(0);

        assertEquals(0, result.distances[0]);
        assertEquals(1, result.distances[1]);
        assertEquals(Integer.MAX_VALUE, result.distances[2]);
        assertEquals(Integer.MAX_VALUE, result.distances[3]);
    }
}
