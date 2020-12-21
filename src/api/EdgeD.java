package api;

import java.awt.*;
import java.util.Objects;

public class EdgeD implements edge_data {
    /**
     * The id of the source node of this edge.
     *
     * @return
     */
     int src;
     int dest;
     double weight;
     String info;
     Color tag;
     node_data source;
     node_data dst;

    public EdgeD(node_data _src, node_data _dest, double w) {
        source = _src;
        dst = _dest;
        src = source.getKey();
        dest = dst.getKey();
        tag = Color.BLUE;
        info = "";
        weight = w;
    }

    public EdgeD (EdgeD edge) {
        info = edge.getInfo();
        tag = new Color(edge.getTag());
        weight = edge.getWeight();
        src = edge.getSrc();
        dest = edge.getDest();
        source =edge.getNodeSrc();
        dst = edge.getNodeDest();
    }





    @Override
    public int getSrc() {
        return source.getKey();
    }

    /**
     * The id of the destination node of this edge
     *
     * @return
     */
    @Override
    public int getDest() {
        return dst.getKey();
    }

    /**
     *
     * @return
     */

    public node_data getNodeSrc () {
        return this.source;
    }

    public node_data getNodeDest () {
        return this.dst;
    }

    /**
     * @return the weight of this edge (positive value).
     */
    @Override
    public double getWeight() {
        return weight;
    }

    /**
     * Allows changing the weight of this edge.
     * @param w
     */
    public void setWeight(double w) {
        this.weight = w;
    }

    /**
     * Returns the remark (meta data) associated with this edge.
     *
     * @return
     */
    @Override
    public String getInfo() {
        return info;
    }

    /**
     * Allows changing the remark (meta data) associated with this edge.
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
     * This method allows setting the "tag" value for temporal marking an edge - common
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
        if (!(o instanceof EdgeD)) return false;
        EdgeD edgeD = (EdgeD) o;
        return src == edgeD.src &&
                dest == edgeD.dest &&
                Double.compare(edgeD.weight, weight) == 0 &&
                tag == edgeD.tag &&
                Objects.equals(info, edgeD.info);
    }

    @Override
    public int hashCode() {
        return Objects.hash(src, dest, weight, info, tag);
    }

    @Override
    public String toString() {
        return "EdgeD{" +
                "src=" + src +
                ", dest=" + dest +
                ", weight=" + weight +
                ", info='" + info + '\'' +
                ", tag=" + tag +
                ", source=" + source +
                ", dst=" + dst +
                '}';
    }
}
