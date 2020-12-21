package api;
import gameClient.util.Point3D;

import java.awt.*;

public class NodeD implements node_data {
    /**
     * Returns the key (id) associated with this node.
     *
     * @return
     */
    private final int key;
    static int count = 0;
    public geo_location location;
    private double weight;
    private String info;
    private Color tag;


    public NodeD () {
        key = count++;
        location = new GeoLocation(0,0,0);
        info = "notvisited";
        weight = Double.MAX_VALUE;
        tag = Color.BLUE;
    }



    public NodeD(node_data node) {
        this.key = node.getKey();
        this.tag = new Color(node.getTag());
        this.location = node.getLocation();
        this.weight = node.getWeight();
        this.info = node.getInfo();
    }

    public NodeD(int index, geo_location geo) {
        this.location = geo;
        this.key = index;
        this.weight = Double.MAX_VALUE;
        info = "notvisit";
        tag = Color.BLUE;
    }




    @Override
    public int getKey() {
        return key;
    }

    /**
     * Returns the location of this node, if
     * none return null.
     *
     * @return
     */
    @Override
    public geo_location getLocation() {
        return location;
    }

    /**
     * Allows changing this node's location.
     *
     * @param p - new new location  (position) of this node.
     */
    @Override
    public void setLocation(geo_location p) {
        location = new GeoLocation(p.x(), p.y(), p.z());
    }

    /**
     * Returns the weight associated with this node.
     *
     * @return
     */
    @Override
    public double getWeight() {
        return weight;
    }

    /**
     * Allows changing this node's weight.
     *
     * @param w - the new weight
     */
    @Override
    public void setWeight(double w) {
        weight = w;
    }

    /**
     * Returns the remark (meta data) associated with this node.
     *
     * @return
     */
    @Override
    public String getInfo() {
        return info;
    }

    /**
     * Allows changing the remark (meta data) associated with this node.
     *
     * @param s
     */
    @Override
    public void setInfo(String s) {
        info = s;
    }

    /**
     * Temporal data (aka color: e,g, white, gray, black)
     * which can be used be algorithms
     *
     * @return
     */
    @Override
    public int getTag() {
        return tag.getRGB();
    }

    /**
     * Allows setting the "tag" value for temporal marking an node - common
     * practice for marking by algorithms.
     *
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {
        tag = new Color(t);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NodeD)) return false;
        NodeD nodeD = (NodeD) o;
        return key == nodeD.key;
    }

    @Override
    public int hashCode() {
        return key;
    }

    public static class GeoLocation implements geo_location {
        public Point3D p;

        public GeoLocation (double x, double y,double z) {
            p = new Point3D(x,y,z);
        }


        @Override
        public double x() {
            return p.x();
        }

        @Override
        public double y() {
            return p.y();
        }

        @Override
        public double z() {
            return p.z();
        }

        @Override
        public double distance(geo_location g) {
            return p.distance(g);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof GeoLocation)) return false;
            GeoLocation that = (GeoLocation) o;
            return p.equals(that.p);
        }
    }

}


