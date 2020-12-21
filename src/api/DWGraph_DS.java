package api;

import java.util.*;

public class DWGraph_DS implements directed_weighted_graph {
    public HashMap<Integer, node_data> graph;
    public HashMap<Integer,HashMap<node_data,edge_data>> neighbors;
    public HashSet<edge_data> edges;
    private int n;
    private int mc;

    public DWGraph_DS() {
        this.graph = new HashMap<>();
        this.neighbors = new HashMap<>();
        this.edges = new HashSet<>();
        this.n = 0;
        this.mc = 0;
    }

    /**
     * returns the node_data by the node_id,
     *
     * @param key - the node_id
     * @return the node_data by the node_id, null if none.
     */
    @Override
    public node_data getNode(int key) {
        if (checkNode(key)) {
            return graph.get(key);
        }
        return null;
    }

    /**
     * returns the data of the edge (src,dest), null if none.
     * Note: this method should run in O(1) time.
     *
     * @param src
     * @param dest
     * @return
     */
    @Override
    public edge_data getEdge(int src, int dest) {
        node_data _src = getNode(src);
        node_data _dest = getNode(dest);

        if (graph.containsValue(_src) && graph.containsValue(_dest) && neighbors.get(src).containsKey(_dest))
            return neighbors.get(src).get(_dest);
        return null;
    }

    /**
     * @param key
     * @return
     * This method returns true if the specific node is exists.
     */
    public boolean checkNode (int key) {
        return this.graph.containsKey(key);
    }

    /**
     * adds a new node to the graph with the given node_data.
     * Note: this method should run in O(1) time.
     *
     * @param n
     */
    @Override
    public void addNode(node_data n) {
        if (!checkNode(n.getKey()) && n != null) {
            graph.put(n.getKey(), n);
            neighbors.put(n.getKey(), new HashMap<>());
            mc++;
        }
    }

    /**
     * Connects an edge with weight w between node src to node dest.
     * * Note: this method should run in O(1) time.
     *
     * @param src  - the source of the edge.
     * @param dest - the destination of the edge.
     * @param w    - positive weight representing the cost (aka time, price, etc) between src-->dest.
     */
    @Override
    public void connect(int src, int dest, double w) {
        if (src != dest && w >= 0 && checkNode(src) && checkNode(dest)) {
            node_data _src = getNode(src);
            node_data _dest = getNode(dest);
            if (neighbors.get(src).containsKey(_dest)) {
                if (getEdge(src,dest).getWeight() == w)
                    return;
                ((EdgeD)getEdge(src, dest)).setWeight(w);
                edges.add(getEdge(src,dest));
                mc++;
            }
            else {
                neighbors.get(src).put(_dest, new EdgeD(_src,_dest,w));
                edges.add(getEdge(src, dest));
                mc++;
                n++;
            }
        }
    }

    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the nodes in the graph.
     * Note: this method should run in O(1) time.
     *
     * @return Collection<node_data>
     */
    @Override
    public Collection<node_data> getV() {
        return new HashSet<>(graph.values());
    }

    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the edges getting out of
     * the given node (all the edges starting (source) at the given node).
     * Note: this method should run in O(k) time, k being the collection size.
     *
     * @param node_id
     * @return Collection<edge_data>
     */
    @Override
    public Collection<edge_data> getE(int node_id) {
        if (checkNode(node_id))
            return new HashSet<>(neighbors.get(node_id).values());
        return new HashSet<>();
    }

    /**
     * Deletes the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     * This method should run in O(k), V.degree=k, as all the edges should be removed.
     *
     * @param key
     * @return the data of the removed node (null if none).
     */
    @Override
    public node_data removeNode(int key) {
        node_data src = getNode(key);
        if (checkNode(key)) {
            for (edge_data e : getE(key)) {
                node_data dest = getNode(e.getDest());
                removeEdge(src.getKey(), dest.getKey());
                if (neighbors.get(dest.getKey()).containsKey(src))
                    removeEdge(dest.getKey(),src.getKey());
            }
            mc++;
            return graph.remove(key);
        }
        return null;
    }

    /**
     * Deletes the edge from the graph,
     * Note: this method should run in O(1) time.
     *
     * @param src
     * @param dest
     * @return the data of the removed edge (null if none).
     */
    @Override
    public edge_data removeEdge(int src, int dest) {
        if (neighbors.get(src).containsKey(getNode(dest)) && checkNode(src) && checkNode(dest)) {
            mc++;
            n--;
            edges.remove(neighbors.get(src).remove(getNode(dest)));
            return neighbors.get(src).remove(getNode(dest));
        }
        return null;
    }

    /**
     * Returns the number of vertices (nodes) in the graph.
     * Note: this method should run in O(1) time.
     *
     * @return
     */
    @Override
    public int nodeSize() {
        return graph.size();
    }

    /**
     * Returns the number of edges (assume directional graph).
     * Note: this method should run in O(1) time.
     *
     * @return
     */
    @Override
    public int edgeSize() {
        return n;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DWGraph_DS)) return false;
        DWGraph_DS that = (DWGraph_DS) o;
        return n == that.n &&
                mc == that.mc &&
                graph.equals(that.graph) &&
                neighbors.equals(that.neighbors) &&
                edges.equals(that.edges);
    }

    @Override
    public String toString() {
        return "DWGraph_DS{" +
                "graph=" + graph +
                ", neighbors=" + neighbors +
                ", edges=" + edges +
                ", n=" + n +
                ", mc=" + mc +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(graph, neighbors, edges, n, mc);
    }

    /**
     * Returns the Mode Count - for testing changes in the graph.
     *
     * @return
     */
    @Override
    public int getMC() {
        return mc;
    }


}
