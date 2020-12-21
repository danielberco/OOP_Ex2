package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.List;

public class DWGraph_Algo implements dw_graph_algorithms {
     directed_weighted_graph graph;
    public static Set<edge_data> edgePath = new HashSet<>();
    static final int color = Color.YELLOW.getRGB();

    /**
     * Init the graph on which this set of algorithms operates on.
     *
     * @param g
     */
    @Override
    public void init(directed_weighted_graph g) {
        this.graph = g;
    }

    /**
     * Return the underlying graph of which this class works.
     *
     * @return
     */
    @Override
    public directed_weighted_graph getGraph() {
        return this.graph;
    }

    /**
     * Compute a deep copy of this weighted graph.
     *
     * @return
     */
    @Override
    public directed_weighted_graph copy() {
        DWGraph_DS clone = new DWGraph_DS();
        if (graph.getV().size() != 0) {
            for (node_data node : graph.getV()) {
                clone.addNode(new NodeD(node));
                for (edge_data edge : graph.getE(node.getKey())) {
                    if (!clone.checkNode(edge.getDest()))
                        clone.addNode(new NodeD(((EdgeD) edge).dst));
                    clone.connect(node.getKey(),edge.getDest(),edge.getWeight());
                }
            }
            return clone;
        }
        return null;
    }

    /**
     * Returns true if and only if (iff) there is a valid path from each node to each
     * other node. NOTE: assume directional graph (all n*(n-1) ordered pairs).
     *
     * @return
     */
    @Override
    public boolean isConnected() {
        if (graph.getV().size() > 1) {
            resetNode();
            directed_weighted_graph clone = new DWGraph_DS();
            node_data node = graph.getV().iterator().next();
            recDFS(graph,node);
            if (graph.edgeSize() != edgePath.size())
                return false;
            reverseGraph(clone);
            node_data nextNode = graph.getNode(node.getKey());
            edgePath = new HashSet<>();
            recDFS(clone,nextNode);
            return (edgePath.size() == graph.edgeSize());
        }
        return true;
    }

    /**
     * returns the length of the shortest path between src to dest
     * Note: if no such path --> returns -1
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        node_data _src = graph.getNode(src);
        node_data _dest = graph.getNode(dest);
        LinkedList<node_data> nodeList = new LinkedList<>(DijAlgo(_src,_dest));
        if (!nodeList.isEmpty())
            return nodeList.getLast().getWeight();
        return -1;
    }

    /**
     * returns the the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest
     * see: https://en.wikipedia.org/wiki/Shortest_path_problem
     * Note if no such path --> returns null;
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public List<node_data> shortestPath(int src, int dest) {
        node_data _src = graph.getNode(src);
        node_data _dest = graph.getNode(dest);
        LinkedList<node_data> nodeList = new LinkedList<>(DijAlgo(_src,_dest));
        if (!nodeList.isEmpty())
            return nodeList;
        return null;
    }

    /**
     * Saves this weighted (directed) graph to the given
     * file name - in JSON format
     *
     * @param file - the file name (may include a relative path).
     * @return true - iff the file was successfully saved
     */
    @Override
    public boolean save(String file) {
        JsonObject jGraph = new JsonObject();
        JsonArray edgeAr = new JsonArray();
        JsonArray nodeAr = new JsonArray();
        Gson gson = new Gson();

        for (node_data node : graph.getV()) {
            String loc = node.getLocation().x()+ "," + node.getLocation().y()+ "," + node.getLocation().z();
            JsonObject o = new JsonObject();
            o.addProperty("position" , loc);
            o.addProperty("index", node.getKey());
            nodeAr.add(o);
        }

        for (edge_data edge:  ((DWGraph_DS) graph).edges) {
            JsonObject o2 = new JsonObject();
            o2.addProperty("src", edge.getSrc());
            o2.addProperty("dest", edge.getDest());
            o2.addProperty("weight", edge.getWeight());
            edgeAr.add(o2);
        }
        jGraph.add("Nodes", nodeAr);
        jGraph.add("Edges", edgeAr);
        String jsonString = gson.toJson(jGraph);
        try {
            PrintWriter printer = new PrintWriter(file);
            printer.write(jsonString);
            printer.close();
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     *
     * @param file - file name of JSON file
     * @return true - iff the graph was successfully loaded.
     */
    @Override
    public boolean load(String file) {
        try {
            GsonBuilder gsonBuilder = new GsonBuilder().registerTypeAdapter(directed_weighted_graph.class, new graphToObject());
            Gson g = gsonBuilder.create();
            FileReader read = new FileReader(file);
            this.graph = g.fromJson(read, directed_weighted_graph.class);
            return true;
        }
        catch (FileNotFoundException notFound) {
            return false;
        }
    }

    private void recDFS (directed_weighted_graph graph, node_data node) {
        node.setTag(color);
        for (edge_data e : graph.getE(node.getKey())) {
            node_data dst = ((EdgeD)e).getNodeDest();
            edgePath.add(e);
            if (dst.getTag() != color)
                recDFS(graph,dst);
        }
    }

    private void resetNode() {
        for (node_data node : graph.getV()) {
            node.setTag(0);
            node.setWeight(Double.MAX_VALUE);
            node.setInfo("notvisited");
        }
    }

    private Collection<node_data> DijAlgo (node_data src, node_data dest) {
        if (src != null && dest != null && src != dest && graph.getV().contains(src) && graph.getV().contains(dest)) {
            resetNode();
            edgePath = new HashSet<>();
            Queue<node_data> nodeQ = new PriorityQueue<>(new Comparator());
            src.setWeight(0);
            nodeQ.add(src);
            while (!nodeQ.isEmpty()) {
                node_data n = nodeQ.poll();
                n.setInfo("visit");
                for (edge_data edge : graph.getE(n.getKey())) {
                    node_data dst = ((EdgeD) edge).getNodeDest();
                    edgePath.add(edge);
                    if (dest.getInfo().equals("notvisited")) {
                        double w = n.getWeight() + edge.getWeight();
                        if (w < dst.getWeight() + edge.getWeight() ) {
                            dst.setWeight(w);
                            nodeQ.add(dst);
                        }
                    }
                }
            }
            directed_weighted_graph clone = new DWGraph_DS();
            reverseGraph(clone);
            node_data _src = clone.getNode(src.getKey());
            node_data _dest = clone.getNode(dest.getKey());
            return minPath(clone,_src,_dest);
        }
        return new LinkedList<>();
    }

    private static class Comparator implements java.util.Comparator<node_data> {
        @Override
        public int compare(node_data o1, node_data o2) {
            return Double.compare(o1.getWeight(), o2.getWeight());
        }
    }

    private void reverseGraph (directed_weighted_graph _graph) {
        for (node_data node : graph.getV()) {
            node.setTag(0);
            _graph.addNode(new NodeD(node));
        }
        for (edge_data edge : edgePath)
            _graph.connect(edge.getDest(),edge.getSrc(),edge.getWeight());
    }


    private Collection<node_data> minPath (directed_weighted_graph g, node_data src, node_data dest) {
        Queue<node_data> qNode = new LinkedList<>();
        Stack<node_data> sNode = new Stack<>();
        LinkedList<node_data> lNode = new LinkedList<>();
        final double bound = 0.001;
        if (dest.getWeight() != Double.MAX_VALUE) {
            sNode.add(dest);
            node_data destNode = dest;
            while (destNode != src) {
                if (destNode != null) {
                    for (edge_data edge : g.getE(destNode.getKey())) {
                        node_data _dest = ((EdgeD) edge).getNodeDest();
                        double w = (destNode.getWeight() - edge.getWeight());
                        if (Math.abs(_dest.getWeight() - w) < bound) {
                            sNode.push(_dest);
                            qNode.add(_dest);
                            break;
                        }
                    }
                    destNode = qNode.poll();
                }
            }
            while (!sNode.isEmpty())
                lNode.add(sNode.pop());
        }
        return lNode;
    }




}
