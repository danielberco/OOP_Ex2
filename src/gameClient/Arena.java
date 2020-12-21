package gameClient;

import api.*;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;
import gameClient.util.Range2Range;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * This class represents a multi Agents Arena which move on a graph - grabs Pokemons and avoid the Zombies.
 * @author boaz.benmoshe
 *
 */
public class Arena {
    public static final double EPS1 = 0.001, EPS2=EPS1*EPS1, EPS=EPS2;
    private static directed_weighted_graph graph;
    private List<CL_Agent> _agents;
    private List<CL_Pokemon> _pokemons;
    private static game_service game;
    static HashMap<Integer, LinkedList<CL_Pokemon>> edgeMap;
    static HashMap<Integer , HashMap<edge_data, Integer>> pass;
    static PriorityQueue<Integer> maxEdge;

    public Arena() {;
        pass = new HashMap<>();
    }




    public void setPokemons(List<CL_Pokemon> f) {
        this._pokemons = f;
    }
    public void setAgents(List<CL_Agent> f) {
        this._agents = f;
    }


    public void setGraph(directed_weighted_graph g) {
        this.graph = g;
    }

    public List<CL_Agent> getAgents() {return _agents;}


    public List<CL_Pokemon> getPokemons() {return _pokemons;}




    public directed_weighted_graph getGraph() {
        return graph;
    }

    public game_service getGame() {
        return game;
    }

    public void setGame(game_service game1) {
        game = game1;
    }




    ////////////////////////////////////////////////////
    public static List<CL_Agent> getAgents(String aa, directed_weighted_graph gg) {
        ArrayList<CL_Agent> ans = new ArrayList<CL_Agent>();
        try {
            JSONObject ttt = new JSONObject(aa);
            JSONArray ags = ttt.getJSONArray("Agents");
            for(int i=0;i<ags.length();i++) {
                CL_Agent c = new CL_Agent(gg,i);
                c.update(ags.get(i).toString());
                ans.add(c);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ans;
    }



    public static ArrayList<CL_Pokemon> json2Pokemons(String fs) {
        ArrayList<CL_Pokemon> ans = new  ArrayList<>();
        edgeMap = new HashMap<>();
        maxEdge = new PriorityQueue<>(new ComparatorValue());

        try {
            JSONObject ttt = new JSONObject(fs);
            JSONArray ags = ttt.getJSONArray("Pokemons");
           for (node_data node: graph.getV()) {
               edgeMap.put(node.getKey(), new LinkedList<>());
           }

            for(int i=0;i<ags.length();i++) {
                JSONObject pp = ags.getJSONObject(i);
                JSONObject pk = pp.getJSONObject("Pokemon");
                int t = pk.getInt("type");
                double v = pk.getDouble("value");
                //double s = 0;//pk.getDouble("speed");
                String p = pk.getString("pos");
                CL_Pokemon f = new CL_Pokemon(new Point3D(p), t,v, null);
                updateEdge(f,graph);
                ans.add(f);
                edgeMap.get(f.getEdges().getDest()).add(f);
            }
            for (CL_Pokemon p : ans) {
                maxEdge.add(p.getEdges().getDest());
            }
        }
        catch (JSONException e) {e.printStackTrace();}
        return ans;
    }


    public static void updateEdge(CL_Pokemon fr, directed_weighted_graph g) {
        //	oop_edge_data ans = null;
        Iterator<node_data> itr = g.getV().iterator();
        while(itr.hasNext()) {
            node_data v = itr.next();
            Iterator<edge_data> iter = g.getE(v.getKey()).iterator();
            while(iter.hasNext()) {
                edge_data e = iter.next();
                boolean f = isOnEdge(fr.getPos(), e,fr.getType(), g);
                if(f) {fr.setEdges(e);}
            }
        }
    }



    private static boolean isOnEdge(geo_location g, edge_data e, int type , directed_weighted_graph _gg) {
        int src = _gg.getNode(e.getSrc()).getKey();
        int dest = _gg.getNode(e.getDest()).getKey();
        if (type < 0 && dest > src)
            return false;
        if (src > dest && type > 0)
            return false;
        geo_location srcLoc = _gg.getNode(src).getLocation();
        geo_location destLoc = _gg.getNode(dest).getLocation();
        boolean ans = false;
        double distance = srcLoc.distance(destLoc);
        double n = srcLoc.distance(g) + g.distance(destLoc);
        if (distance > n - EPS2)
            ans = true;
        return ans;
    }



    private static Range2D GraphRange(directed_weighted_graph g) {
        Iterator<node_data> itr = g.getV().iterator();
        double x0=0,x1=0,y0=0,y1=0;
        boolean first = true;
        while(itr.hasNext()) {
            geo_location p = itr.next().getLocation();
            if(first) {
                x0=p.x(); x1=x0;
                y0=p.y(); y1=y0;
                first = false;
            }
            else {
                if(p.x()<x0) {x0=p.x();}
                if(p.x()>x1) {x1=p.x();}
                if(p.y()<y0) {y0=p.y();}
                if(p.y()>y1) {y1=p.y();}
            }
        }
        Range xr = new Range(x0,x1);
        Range yr = new Range(y0,y1);
        return new Range2D(xr,yr);
    }


    public static Range2Range w2f(directed_weighted_graph g, Range2D frame) {
        Range2D world = GraphRange(g);
        Range2Range ans = new Range2Range(world, frame);
        return ans;
    }

    public LinkedList<Integer> getValueList() {
        return new LinkedList<>(maxEdge);
    }

    public static int edgeVal (int key) {
        int val =0;
        for (CL_Pokemon pokemon : edgeMap.get(key))
            val += pokemon.getVal();
        return val;
    }

    public LinkedList<CL_Pokemon> getPokemonEdge (int key) {
        return edgeMap.getOrDefault(key,null);
    }

    public int slowAgent (CL_Agent ag, CL_Pokemon poke) {
        if (pass.containsKey(ag.getID()) && pass.get(ag.getID()).containsKey(poke.getEdges())) {
            int lastPoke = pass.get(ag.getID()).get(poke.getEdges());
            pass.get(ag.getID()).put(poke.getEdges(),lastPoke++);
            return lastPoke;
        }
        else {
            pass.put(ag.getID(), new HashMap<>());
            pass.get(ag.getID()).put(poke.getEdges(),1);
            return 1;
        }
    }

    public static class ComparatorValue implements java.util.Comparator<Integer> {

        @Override
        public int compare(Integer o1, Integer o2) {
            return Integer.compare(edgeVal(o2),edgeVal(o1));
        }
    }

}