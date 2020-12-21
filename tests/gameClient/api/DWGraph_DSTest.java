package api;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_DSTest {
    static directed_weighted_graph graph;

    @BeforeAll
    static void smallGraph() {
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
    void getNode() {
        node_data node_null = graph.getNode(-1);
        assertNull(node_null);
        int actual = graph.getNode(0).getKey();
        int expected = 0;
        assertEquals(actual,expected);
    }

    @Test
    void getEdge() {
        edge_data edge_null = graph.getEdge(1,9);
        assertNull(edge_null);
        double actual_weight = graph.getEdge(0,1).getWeight();
        double expected_weight = 1.2;
        assertEquals(actual_weight,expected_weight);
    }

    @Test
    void addNode() {
        node_data new_node_first = new NodeD();
        graph.addNode(new_node_first);
        int actual_num_of_nodes = graph.getV().size();
        int expected_num_of_nodes = 6;
        assertEquals(actual_num_of_nodes,expected_num_of_nodes);
    }

    @Test
    void hasNode() {
        boolean flag = ((DWGraph_DS)graph).checkNode(-9);
        assertFalse(flag);
    }

    @Test
    void connect() {
        graph.connect(0,1,1.2);
        graph.connect(0,1,1.2);
        graph.connect(0,1,1.2);
        graph.connect(0,1,1.2);
        graph.connect(3,30,-2);
        int actual_mode_count = graph.getMC();
        int expected_mode_count = 20;
        assertEquals(actual_mode_count,expected_mode_count);
    }

    @Test
    void getV() {
        graph.removeNode(1);
        int actual_size = graph.getV().size();
        int expected_size = 5;
        assertEquals(actual_size,expected_size);
    }

    @Test
    void getE() {
        HashSet<edge_data> edges = new HashSet<>(graph.getE(0));
        edge_data e = graph.getEdge(0,1);
        boolean flag = edges.contains(e);
        assertTrue(flag);
    }

    @Test
    void removeNode() {
        graph.removeNode(1);
        graph.removeNode(1);
        graph.removeNode(1);
        graph.removeNode(1);
        int actual_num_of_nodes = graph.getV().size();
        int expected_num_of_nodes = 5;
        assertEquals(actual_num_of_nodes,expected_num_of_nodes);
        HashSet<edge_data> edges = new HashSet<>(graph.getE(1));
        boolean flag = edges.isEmpty();
        assertTrue(flag);
    }

    @Test
    void removeEdge() {
        graph.removeEdge(0,1);
        HashSet<edge_data> edges = new HashSet<>(graph.getE(0));
        boolean flag = edges.isEmpty();
        assertTrue(flag);
    }

    @Test
    void nodeSize() {
        node_data new_node = new NodeD();
        graph.addNode(new_node);
        int actual_num_of_nodes = graph.getV().size();
        int expected_num_of_nodes = 6;
        assertEquals(actual_num_of_nodes,expected_num_of_nodes);
        graph.removeNode(3);
        assertEquals(5,graph.getV().size());
    }

    @Test
    void edgeSize() {
        int actual_num_of_edges = 0;
        for(node_data runner : graph.getV()) {
            for(edge_data edge :graph.getE(runner.getKey())) {
                actual_num_of_edges++;
            }
        }

        int expected_num_of_edges = 4;
        assertEquals(actual_num_of_edges,expected_num_of_edges);
    }

    @Test
    void getMC() {
    }

    @Test
    void testEquals() {
    }
}