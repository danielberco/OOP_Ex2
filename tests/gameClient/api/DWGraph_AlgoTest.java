package api;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_AlgoTest {
    static dw_graph_algorithms graph_algorithms;
    static directed_weighted_graph graph;
    static final double EPSILON = 0.001;

    @BeforeAll
    static void smallGraph() {
        graph_algorithms = new DWGraph_Algo();
        graph = new DWGraph_DS();
        for(int i = 0 ; i < 6 ; i++) {
            node_data new_node = new NodeD();
            graph.addNode(new_node);
        }
        graph.connect(0,1,1.2);
        graph.connect(1,0,4);
        graph.connect(1,2,4.32);
        graph.connect(1,5,7.11);
        graph.connect(2,0,5.1);
        graph.connect(2,4,4.9);
        graph.connect(4,3,12.4);
        graph.connect(3,2,1.06);
        graph.connect(5,2,6.1);

    }
    @Test
    void init() {
        graph_algorithms.init(graph);
        assertSame(graph,graph_algorithms.getGraph());
    }

    @Test
    void getGraph() {
    assertEquals(graph,graph_algorithms.getGraph());
    }

    @Test
    void copy() {
        graph_algorithms.init(graph);
        directed_weighted_graph copied = graph_algorithms.copy();
        assertNotSame(graph,copied); // check whether two graphs object do not refer to the same object.
        assertEquals(graph,copied);
    }

    @Test
    void isConnected() {
        graph_algorithms.init(graph);
        boolean actual = graph_algorithms.isConnected();
        assertTrue(actual);
        graph.removeNode(5);
        assertFalse(graph_algorithms.isConnected());
    }

    @Test
    void shortestPathDist() {
        graph_algorithms.init(graph);
        double actual_shortest_path = graph_algorithms.shortestPathDist(0,2);
        double expected_shortest_path = 5.52;
        assertEquals(actual_shortest_path,expected_shortest_path,EPSILON);
        double expected = -1;
        graph.removeNode(5);
        double actual = graph_algorithms.shortestPathDist(0,5);
        assertEquals(expected,actual);
    }

    @Test
    void shortestPath() {
        graph_algorithms.init(graph);
        directed_weighted_graph copied = graph_algorithms.copy();
        copied.removeNode(5);
        graph_algorithms.init(copied);
        assertNull(graph_algorithms.shortestPath(0,5));
        node_data n0 = copied.getNode(0);
        node_data n1 = copied.getNode(1);
        node_data n2 = copied.getNode(2);
        List<node_data> actual_path = graph_algorithms.shortestPath(0,2);
        List<node_data> expected_path = new LinkedList<>();
        expected_path.add(n0); expected_path.add(n1); expected_path.add(n2);
        assertEquals(actual_path,expected_path);
    }

    @Test
    void save() {
        String file_name = "newGraph.bin";
        try {
            graph_algorithms.save(file_name);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void load() {
        try {
            graph_algorithms.load("data/A0");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        int actual_num_of_nodes = graph_algorithms.getGraph().getV().size();
        int expected_num_of_nodes = 11;
        assertEquals(actual_num_of_nodes,expected_num_of_nodes);
    }

}