package gameClient;

import Server.Game_Server_Ex2;
import api.DWGraph_Algo;
import api.directed_weighted_graph;
import api.edge_data;
import api.game_service;
import gameClient.util.Point3D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArenaTest {
    static Arena manageGame;
    static CL_Agent testingAgent;
    static directed_weighted_graph graph;
    static DWGraph_Algo graphAlgo;
    static String json;

    @BeforeEach
    void setUp() {
        manageGame = new Arena();
        graphAlgo = new DWGraph_Algo();
        graphAlgo.load("data/A0");
        graph = graphAlgo.getGraph();

    }

    @Test
    void setPokemons() {
    edge_data e1 = graph.getEdge(0,1);
        CL_Pokemon p1 = new CL_Pokemon(new Point3D(3.3,1.6,0),1,15,e1);
        CL_Pokemon p2 = new CL_Pokemon(new Point3D(3.13,14.6,0),1,15,e1);
    List<CL_Pokemon> p_list = new LinkedList<>();
    p_list.add(p1); p_list.add(p2);
    manageGame.setPokemons(p_list);
    List<CL_Pokemon> actual = manageGame.getPokemons();
    assertEquals(actual,p_list);

    }

    @Test
    void setAgents() {
        CL_Agent a1 = new CL_Agent(graph,0);
    List<CL_Agent> a_list = new LinkedList<>();
    a_list.add(a1);
    manageGame.setAgents(a_list);
    List<CL_Agent> actual = manageGame.getAgents();
    assertEquals(actual,a_list);
    }

    @Test
    void setGraph() {
        manageGame.setGraph(graph);
        assertEquals(graph,manageGame.getGraph());
    }
    @Test
    void getAgents() {
        setAgents();
        List<CL_Agent> actual = manageGame.getAgents();
        int actual_source_node = actual.get(0).getSrcNode();
        int expected = 0;
        assertEquals(actual_source_node,expected);
    }

    @Test
    void getPokemons() {
        setPokemons();
        List<CL_Pokemon> actual = manageGame.getPokemons();
        double actual_weight = actual.get(0).getEdges().getWeight();
        double expected_weight = 1.4004465106761335;
        assertEquals(actual_weight,expected_weight);
    }

    @Test
    void getGraph() {
        manageGame.setGraph(graph);
        assertEquals(graph,manageGame.getGraph());
        int expected_num_of_nodes = graph.getV().size();
        int actual_num_of_nodes = manageGame.getGraph().getV().size();
        assertEquals(expected_num_of_nodes,actual_num_of_nodes);
    }

    @Test
    void setGame() {

    }

    @Test
    void getGame() {
        game_service game = Game_Server_Ex2.getServer(0);
        manageGame.setGame(game);
        assertEquals(game,manageGame.getGame());
    }

    @Test
    void testGetAgents() {
        String json = "{\"Agents\":[{\"Agent\":{\"id\":0,\"value\":0.0,\"src\":17,\"dest\":-1,\"speed\":1.0,\"pos\":\"35.20737758999193,32.101092638655466,0.0\"}},{\"Agent\":{\"id\":1,\"value\":0.0,\"src\":33,\"dest\":-1,\"speed\":1.0,\"pos\":\"35.20476669410815,32.108579959663864,0.0\"}}]}\n";
        graphAlgo.load("data/A4");
        graph = graphAlgo.getGraph();
        List<CL_Agent> agentList = Arena.getAgents(json,graph);
        CL_Agent agent0 = agentList.get(0);
        CL_Agent agent1 = agentList.get(1);

        assertAll("Check Agent0: ",
                ()-> assertEquals(0,agent0.getID()),
                ()-> assertEquals(0.0,agent0.getPoints()),
                ()-> assertEquals(17,agent0.getSrcNode()),
                ()-> assertEquals(-1,agent0.getNextNode()),
                ()-> assertEquals(1.0,agent0.getSpeed()),
                ()-> assertEquals(35.20737758999193,agent0.getLocation().x()),
                ()-> assertEquals(32.101092638655466,agent0.getLocation().y()),
                ()-> assertEquals(0.0,agent0.getLocation().z())
                );
        assertAll("Check Agent1: ",
                ()-> assertEquals(1,agent1.getID()),
                ()-> assertEquals(0.0,agent1.getPoints()),
                ()-> assertEquals(33,agent1.getSrcNode()),
                ()-> assertEquals(-1,agent1.getNextNode()),
                ()-> assertEquals(1.0,agent1.getSpeed()),
                ()-> assertEquals(35.20476669410815,agent1.getLocation().x()),
                ()-> assertEquals(32.108579959663864,agent1.getLocation().y()),
                ()-> assertEquals(0.0,agent1.getLocation().z())
        );

    }

    @Test
    void testGetPokemons() {
        String json = "{\"Pokemons\":[{\"Pokemon\":{\"value\":5.0,\"type\":-1,\"pos\":\"35.197656770719604,32.10191878639921,0.0\"}},{\"Pokemon\":{\"value\":9.0,\"type\":-1,\"pos\":\"35.19038634163924,32.10748920705224,0.0\"}},{\"Pokemon\":{\"value\":12.0,\"type\":-1,\"pos\":\"35.1992728373109,32.105605979924384,0.0\"}}]}";
        List<CL_Pokemon> pokemonList = Arena.json2Pokemons(json);
        CL_Pokemon pokemon0 = pokemonList.get(0);
        CL_Pokemon pokemon1 = pokemonList.get(1);
        CL_Pokemon pokemon2 = pokemonList.get(2);


        assertAll("Check pokemon0",
                ()-> assertEquals(5.0,pokemon0.getVal()),
                ()-> assertEquals(-1,pokemon0.getType()),
                ()-> assertEquals(35.197656770719604,pokemon0.getPos().x()),
                ()-> assertEquals(32.10191878639921,pokemon0.getPos().y()),
                ()-> assertEquals(0.0,pokemon0.getPos().z())
                );
        assertAll("Check pokemon0",
                ()-> assertEquals(9.0,pokemon1.getVal()),
                ()-> assertEquals(-1,pokemon1.getType()),
                ()-> assertEquals(35.19038634163924,pokemon1.getPos().x()),
                ()-> assertEquals(32.10748920705224,pokemon1.getPos().y()),
                ()-> assertEquals(0.0,pokemon1.getPos().z())
        );
        assertAll("Check pokemon0",
                ()-> assertEquals(12.0,pokemon2.getVal()),
                ()-> assertEquals(-1,pokemon2.getType()),
                ()-> assertEquals(35.1992728373109,pokemon2.getPos().x()),
                ()-> assertEquals(32.105605979924384,pokemon2.getPos().y()),
                ()-> assertEquals(0.0,pokemon2.getPos().z())
        );

    }

}
