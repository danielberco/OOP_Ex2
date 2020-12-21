package api;

import api.NodeD.GeoLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class NodeTest {

    node_data testingNode;

    @BeforeEach
    void setUp() {
        testingNode = new NodeD();
    }

    @Test
    void getKey() {
        assertEquals(0,testingNode.getKey());
        node_data anotherNode = new NodeD(50, new GeoLocation(0, 0, 0) {
        });
        assertEquals(50,anotherNode.getKey());
        node_data copiedNode = new NodeD(anotherNode);
        assertEquals(50,copiedNode.getKey());
    }

    @Test
    void getLocation() {
        geo_location location = testingNode.getLocation();
        double x = location.x();
        double y = location.y();
        double z = location.z();
        assertEquals(x,0.0);
        assertEquals(y,0.0);
        assertEquals(z,0.0);
        node_data anotherNode = new NodeD(51,new GeoLocation(7.9,90.3,4.7));
        node_data copiedNode = new NodeD(anotherNode);
        assertEquals(anotherNode.getLocation(),copiedNode.getLocation());
    }

    @Test
    void setLocation() {
        node_data myNode = new NodeD();
        myNode.setLocation(new GeoLocation(9.8,4.5,20.1));
        assertEquals(myNode.getLocation(),new GeoLocation(9.8,4.5,20.1));
    }

    @Test
    void getWeight() {
        assertEquals(testingNode.getWeight(),Double.MAX_VALUE);
    }

    @Test
    void setWeight() {
        testingNode.setWeight(90.3);
        assertEquals(testingNode.getWeight(),90.3);
    }

    @Test
    void getInfo() {
        assertEquals("notvisited",testingNode.getInfo());
    }

    @Test
    void setInfo() {
        testingNode.setInfo("look");
        assertEquals("look",testingNode.getInfo());
    }

    @Test
    void getTag() {
        assertEquals(testingNode.getTag(), Color.BLUE.getRGB());
    }

    @Test
    void setTag() {
        testingNode.setTag(43);
        assertEquals(testingNode.getTag(),new Color(43).getRGB());
    }

}
