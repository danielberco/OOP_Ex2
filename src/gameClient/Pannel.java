package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;
import gameClient.util.Range2Range;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * This class is our panel in the game, by using we design how our game will look.
 */

public class Pannel extends JPanel {
    Arena manage;
    Range2Range range;
    final int r = 7;
    int moves = 0;
    double grade = 0;

    /**
     * Constructor
     * @param a
     */
    public Pannel (Arena a) {
        this.manage = a;
        importImage();
    }


    /**
     * Using Graphics class, we painting our pannel
     * @param g
     */
    public void paint (Graphics g) {
        int width = this.getWidth();
        int height = this.getHeight();
        resizePic();
        g.drawImage(background,0,0,width,height,this);
        drawGraph((Graphics2D)g);
        drawPokemon((Graphics2D)g);
        drawAgents((Graphics2D)g);
        long time = TimeUnit.MILLISECONDS.toSeconds(manage.getGame().timeToEnd());
        g.setColor(Color.red);
        g.drawImage(sideScreen,-5,0,175,175,this);
        g.drawImage(logo,5,0,150,80,this);
        g.drawString("Time : 00:" + time,25,90);
        g.drawString("Moves:"+ moves,25,130);
        g.drawString("Grade:" + grade,25,110);
    }


    private void drawPokemon (Graphics2D graphics) {
        java.util.List<CL_Pokemon> manageG = manage.getPokemons();
        if (manageG != null) {
            for (CL_Pokemon poke : manageG) {
                Point3D p = poke.getPos();
                Color c = Color.green;
                if (poke.getType() < 0)
                    c = Color.RED;
                if ( p != null) {
                    geo_location g = this.range.world2frame(p);
                    if (poke.getVal() < 6) {
                        drawPokemon2(graphics,g,poke,c);
                    }
                    else if (poke.getVal() < 11)
                        drawPokemon1(graphics,g,poke,c);
                    else
                        drawPokemon3(graphics,g,poke,c);
                }
            }
        }
    }

    private void drawAgents(Graphics g) {
        List<CL_Agent> rs = manage.getAgents();
        int i =0;
        while (rs != null && i < rs.size()) {
            geo_location agentLoc = rs.get(i).getLocation();
            String val = String.valueOf((int)rs.get(i).getPoints());
            int num = 8;
            if (agentLoc != null) {
                geo_location fp = range.world2frame(agentLoc);
                g.drawImage(ash,(int) fp.x() - 30, (int) fp.y() -30 , num * 5 , 5* num, this);
                g.drawImage(sideScreen,(int) fp.x() - 75, (int) fp.y() -75 , 140 , 35, this);
                g.setColor(Color.BLUE);
                g.setFont(new Font("Segoe UI", Font.BOLD, 18));
                g.drawString("AI " ,(int) fp.x() - 70, (int) fp.y() -44);
                g.setColor(Color.WHITE);
                g.setFont(new Font("Segoe UI", Font.BOLD, 15));
                g.drawString("Points:" +val,(int) fp.x() - 20, (int) fp.y() -7*r);
                g.drawString("Speed:" +(int)rs.get(i).getSpeed(),(int) fp.x() - 20, (int) fp.y() -5*r);
                g.setFont(new Font("Segoe UI", Font.BOLD, 25));
            }
            i++;
        }
    }

    void drawArrow(Graphics g1, int x1, int y1, int x2, int y2) {
        Graphics2D g = (Graphics2D) g1.create();
        double dx = x2 - x1, dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx * dx + dy * dy);
        AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
        at.concatenate(AffineTransform.getRotateInstance(angle));
        g.transform(at);
        int size = 10;
        g.setStroke(new BasicStroke(5));
        g.drawLine(0, 0, len, 0);
        g.fillPolygon(new int[]{len - 10, len - size - 20, len - size - 20, len - 10}, new int[]{0, -size, size, 0}, 4);
    }

    private void drawEdge (edge_data e, Graphics2D g) {
        directed_weighted_graph graph = manage.getGraph();
        geo_location src = graph.getNode(e.getSrc()).getLocation();
        geo_location dest = graph.getNode(e.getDest()).getLocation();
        geo_location srcRange = this.range.world2frame(src);
        geo_location destRange = this.range.world2frame(dest);
        g.setColor(new Color(68, 79, 173));
        drawArrow(g, (int) srcRange.x(), (int) srcRange.y(), (int) destRange.x(), (int) destRange.y());
    }

    private void drawNode (node_data node, Graphics2D g) {
        g.setColor(new Color(155, 80, 73));
        geo_location pos = node.getLocation();
        geo_location fp = this.range.world2frame(pos);
        g.drawImage(pokeBall,(int) fp.x() -15 , (int) fp.y()-30 , 4 * r, 5* r,this);
        g.setFont(new Font("Segoe UI", Font.BOLD,20));
        g.setColor(Color.black);
        g.drawString(" "+ node.getKey(), (int) fp.x()-14, (int) fp.y()-34);
    }



    private void drawGraph(Graphics2D g) {
        directed_weighted_graph graph = manage.getGraph();
        for (node_data node : graph.getV()) {
            for (edge_data edge : graph.getE(node.getKey())) {
                drawEdge(edge,g);
            }
        }
        for (node_data node : graph.getV())
            drawNode(node,g);
    }

    public void setMoves (int move ) {
        this.moves = move;
    }

    public void setGrade (int g) {
        this.grade = g;
    }

    /**
     * Importing the images files
     */

    static BufferedImage background = null;
    static BufferedImage logo = null;
    static BufferedImage sideScreen = null;
    static BufferedImage pokeBall = null;
    static Image ash = null;
    static Image pokemon1 = null;
    static Image pokemon2 = null;
    static Image pokemon3 = null;

    public static void importImage() {
        try {
            background = ImageIO.read(new File("resources/background.jpg"));
            logo = ImageIO.read(new File("resources/logo.png"));
            sideScreen = ImageIO.read(new File("resources/sideScreen.png"));
            ash = Toolkit.getDefaultToolkit().createImage("resources/agent.gif");
            pokemon1 = Toolkit.getDefaultToolkit().createImage("resources/poke3.gif");
            pokemon2 = Toolkit.getDefaultToolkit().createImage("resources/poke2.1.gif");
            pokemon3 = Toolkit.getDefaultToolkit().createImage("resources/pokemonLeg.gif");
            pokeBall = ImageIO.read(new File("resources/2.png"));
            }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Drawing pokemons
     * @param g
     * @param fp
     * @param p
     * @param c
     */

    public void drawPokemon1(Graphics g, geo_location fp , CL_Pokemon p, Color c) {
        g.drawImage(pokemon1,(int)fp.x() -30 ,(int)fp.y() -30 , 9 * r , 8* r, this);
        g.drawImage(sideScreen, (int) fp.x() -36 , (int) fp.y()-70,120,50,this);
        g.drawString("Pokemon 1", (int) fp.x() -12 , (int) fp.y() -60);
        g.setColor(Color.WHITE);
        g.drawString("Value:" + p.getVal(), (int) fp.x()-18,(int) fp.y() -39 );
    }

    public void drawPokemon2(Graphics g, geo_location fp , CL_Pokemon p, Color c) {
        g.drawImage(pokemon3,(int)fp.x() -30 ,(int)fp.y() -30 , 9 * r , 8* r, this);
        g.drawImage(sideScreen, (int) fp.x() -36 , (int) fp.y()-70,120,50,this);
        g.drawString("Pokemon 2", (int) fp.x() -12 , (int) fp.y() -40);
        g.setColor(Color.WHITE);
        g.drawString("Value:" + p.getVal(), (int) fp.x()-18,(int) fp.y() -39 );
    }

    public void drawPokemon3(Graphics g, geo_location fp , CL_Pokemon p, Color c) {
        g.drawImage(pokemon2,(int)fp.x() -30 ,(int)fp.y() -30 , 9 * r , 8* r, this);
        g.drawImage(sideScreen, (int) fp.x() -36 , (int) fp.y()-70,120,50,this);
        g.drawString("Pokemon 3", (int) fp.x() -12 , (int) fp.y() -60);
        g.setColor(Color.WHITE);
        g.drawString("Value:" + p.getVal(), (int) fp.x()-18,(int) fp.y() -39 );
    }

    /**
     * This method is resizing the frames of the image.
     */

    private void resizePic() {
        Range x = new Range(120, this.getWidth()-120);
        Range y = new Range(this.getHeight()-70,175);
        Range2D frame = new Range2D(x,y);
        directed_weighted_graph graph = manage.getGraph();
        range = Arena.w2f(graph,frame);
    }
}
