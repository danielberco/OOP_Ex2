package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import org.json.JSONObject;

public class CL_Agent {
    private geo_location pos;
    private node_data nodes;
    private edge_data edges;
    private final directed_weighted_graph graph;
    private int key;
    private double speed;
    private double points;

    public CL_Agent(directed_weighted_graph g, int num) {
        this.graph = g;
        setMoney(0);
        this.nodes = graph.getNode(num);
        this.pos = nodes.getLocation();
        this.key = -1;
        setSpeed(0);
    }

    public void update(String json) {
        JSONObject line;
        try {
            // "GameServer":{"graph":"A0","pokemons":3,"agents":1}}
            line = new JSONObject(json);
            JSONObject ttt = line.getJSONObject("Agent");
            int id = ttt.getInt("id");
            if(id==this.getID() || this.getID() == -1) {
                if(this.getID() == -1) {key = id;}
                double speed = ttt.getDouble("speed");
                String p = ttt.getString("pos");
                Point3D pp = new Point3D(p);
                int src = ttt.getInt("src");
                int dest = ttt.getInt("dest");
                double value = ttt.getDouble("value");
                this.pos = pp;
                this.setCurrNode(src);
                this.setSpeed(speed);
                this.setNextNode(dest);
                this.setMoney(value);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public int getSrcNode () {
        return nodes.getKey();
    }

    private void setMoney (double m) {
        points = m;
    }

    public void setNextNode (int dst ) {
        int src = this.nodes.getKey();
        if (this.graph.getEdge(src,dst) != null) {
            this.edges = graph.getEdge(src,dst);
        }
    }

    public void setCurrNode(int src) {
        this.nodes = graph.getNode(src);
    }

    public int getID() {
        return this.key;
    }

    public double getPoints() {
        return this.points;
    }

    public geo_location getLocation (){
        return this.pos;
    }

    public int getNextNode () {
        return this.edges == null ? -1 : this.edges.getDest();
    }

    public double getSpeed() {
        return this.speed;
    }

    public void setSpeed (double  n) {
        this.speed = n;
    }

}
