package gameClient;

import api.edge_data;
import gameClient.util.Point3D;

public class CL_Pokemon {
    private edge_data edges;
    private final double val;
    private final int type;
    private final Point3D pos;
    private double dis;

    public CL_Pokemon(Point3D p,int t, double v, edge_data e) {
        this.type = t;
        this.val = v;
        this.pos = p;
        setEdges(e);
    }



    public edge_data getEdges() {
        return edges;
    }

    public void setEdges(edge_data edges) {
        this.edges = edges;
    }

    public double getVal() {
        return val;
    }

    public Point3D getPos() {
        return pos;
    }

    public int getType () {
        return this.type;
    }

    public int getDest() {
        return edges.getDest();
    }

    public double getDis() {
        return dis;
    }

    public void setDis(double dis) {
        this.dis = dis;
    }

}
