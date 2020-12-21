package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.util.*;
import java.util.List;

public class Ex2 implements Runnable {
    private static Frame Frame;
    private static Arena manage;
    private static long id;
    private static int level;
    private static directed_weighted_graph graph;
    private static final dw_graph_algorithms algo = new DWGraph_Algo();
    static long time;
    static int moves = 0;
    static HashMap<Integer, Integer> stat;

    public static void main(String[] a) {
        try {
            id = Long.parseLong(a[0]);
            level = Integer.parseInt(a[1]);
        } catch (Exception e) {
            id = -1;
            level = -1;
        }
        Thread client = new Thread(new Ex2());
        client.run();
    }

    @Override
    public void run() {
        listId();
        game_service game = Game_Server_Ex2.getServer(level);
        loadG(game.getGraph());
        init(game);
        game.login(id);
        game.startGame();
        Frame.setTitle("Ex2 : Pokemons ,  Game Number: " + level);
        while (game.isRunning()) {
            moves++;
            time = 100;
            importAgents(game);
            try {
                Frame.getPanel().setMoves(moves);
                Frame.repaint();
                Thread.sleep(time);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String res = game.toString();
        System.out.println(res);
        System.exit(0);
    }

    /**
     * Creating a login system by using id number and Json and connecting to the server
     */
    private static void listId() {
        if ((id == -1 && level == -1) || (Game_Server_Ex2.getServer(level) == null)) {
            try {
                String id = JOptionPane.showInputDialog("Enter your ID", "ID : ");
                String level = JOptionPane.showInputDialog("Enter level number", "Type a level ");
                Ex2.id = Long.parseLong(id);
                Ex2.level = Integer.parseInt(level);
                if (Game_Server_Ex2.getServer(Ex2.level) == null) {
                    throw new RuntimeException();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(new Frame(), "Invalid input.\nTry again." +
                                "\nPlaying level 0.", "Error!",
                        JOptionPane.ERROR_MESSAGE);
                level = 0;
            }
        }
    }


    /**
     * Loading the fraph with Gson build
     * @param str
     */
    private void loadG(String str) {
        try {
            GsonBuilder builder = new GsonBuilder()
                    .registerTypeAdapter(directed_weighted_graph.class, new graphToObject());
            Gson gson = builder.create();
            graph = gson.fromJson(str, directed_weighted_graph.class);
        } catch (Exception f) {
            f.printStackTrace();
        }
    }

    /**
     * init the game in the first run.
     * Connecting to Ex2 server and setting the frame.
     * Making the pokemons showing by order of their level
     */
    private void init(game_service game) {
        stat = new HashMap<>();
        String fs = game.getPokemons();
        manage = new Arena();
        manage.setGraph(graph);
        manage.setPokemons(Arena.json2Pokemons(fs));
        manage.setGame(game);
        /// frame init
        Frame = new Frame();
        Frame.setSize(1000, 700);
        Frame.initFrame(manage);
        Frame.setVisible(true);
        String info = game.toString();
        try {
            JSONObject line = new JSONObject(info);
            JSONObject ttt = line.getJSONObject("GameServer");
            int num_of_agents = ttt.getInt("agents");
            System.out.println(info);
            System.out.println(game.getPokemons());
            Arena.json2Pokemons(game.getPokemons());
            LinkedList<Integer> strongest = manage.getValueList();
            for (int a = 0; a < num_of_agents; a++) {
                boolean flag = false;
                for (Integer key : strongest) {
                    if (!flag) {
                        LinkedList<CL_Pokemon> pokemonsOnEdge = manage.getPokemonEdge(key);
                        CL_Pokemon p = pokemonsOnEdge.getFirst();
                        int pos = key;
                        if (pokemonsOnEdge.getFirst().getType() < 0)
                            pos = p.getEdges().getSrc();
                        if (!stat.containsValue(pos)) {
                            flag = true;
                            stat.put(a, pos);
                            game.addAgent(pos);
                        }
                    }
                }
            }
            for (int a = 0; a < num_of_agents; a++) {
                if(!stat.containsKey(a)){
                    for(node_data node : graph.getV()) {
                        if(!stat.containsValue(node.getKey())) {
                            stat.put(a,node.getKey());
                            game.addAgent(node.getKey());
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * import the Agents and their status from the game server and update the Agents in the Arena.
     * @param game a game_service type.
     */
    private static void importAgents(game_service game) {
        String lg = game.move();
        List<CL_Agent> log = Arena.getAgents(lg, graph);
        manage.setAgents(log);
        String fs = game.getPokemons();
        List<CL_Pokemon> ffs = Arena.json2Pokemons(fs);
        manage.setPokemons(ffs);
        algo.init(graph);
        int grade = 0;
        for (CL_Agent ag : log) {
            int id = ag.getID();
            double v = ag.getPoints();
            grade += (int) v;
            int dest = ag.getNextNode();
            if (dest == -1) {
                if (ag.getSrcNode() == stat.get(ag.getID())) {
                    stat.put(ag.getID(), -1);
                }
                int nextNode = agentAlgo(ag, ffs);
                game.chooseNextEdge(ag.getID(), nextNode);
                System.out.println("Agent: " + id + ", val: " + v + "   turned to node: " + nextNode);
            }
        }
        Frame.getPanel().setGrade(grade);
    }


    /**
     * By using Dijikstra algorithem , this method finding the shortest path distance from the agent to the nearest pokemon
     * @param agent     an Agent.
     * @param pokemons a List of the Pokemons in the game.
     * @return the key of a node to the next move of the Agent.
     */
    private static int agentAlgo(CL_Agent agent, List<CL_Pokemon> pokemons) {
        PriorityQueue<CL_Pokemon> closest = new PriorityQueue<>(new ComparatorDist());
        for (CL_Pokemon p : pokemons) {
            Arena.updateEdge(p, graph);
            if (!stat.containsValue(p.getEdges().getDest()) || stat.get(agent.getID()) == p.getEdges().getDest()) {
                double distance = algo.shortestPathDist(agent.getSrcNode(), p.getEdges().getDest());
                p.setDis(distance);
                closest.add(p);
            }
        }
        ArrayList<node_data> path = null;
        if (!closest.isEmpty()) {
            CL_Pokemon target = closest.poll();
            stat.put(agent.getID(), target.getEdges().getDest());
            if (manage.slowAgent(agent, target) >= 8)
                time = 30;

            if (agent.getSrcNode() == target.getEdges().getDest()) {
                return target.getEdges().getSrc();
            } else {
                path = new ArrayList<>(algo.shortestPath(agent.getSrcNode(), target.getEdges().getDest()));
            }
        }
        if (path == null || path.isEmpty()) {
            LinkedList<edge_data> edgeData = new LinkedList<>(graph.getE(agent.getSrcNode()));
            return edgeData.getFirst().getDest();
        }
        return path.get(1).getKey();
    }

    public static class ComparatorDist implements java.util.Comparator<CL_Pokemon> {
        @Override
        public int compare(CL_Pokemon o1, CL_Pokemon o2) {
            return Double.compare(o1.getDis(), o2.getDis());
        }
    }

}