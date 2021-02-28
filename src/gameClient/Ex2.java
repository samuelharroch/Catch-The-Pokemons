package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;




public class Ex2 implements Runnable{
    private static GUI_Frame _win;
    private static   Arena _ar;
    private final long dt=100;
    private static int scenario_num=23;
    private static long id=0;
    private static String [] levels;

    public static void main(String[] a) {
        Audio audio= new Audio();
        audio.start();

        if(a.length>0){
            id=Long.parseLong(a[0]);
            try {
                scenario_num = Integer.parseInt(a[1]);
            }
            catch (NumberFormatException e){
                System.out.println("Bad input");
                System.exit(0);
            }
        }
        else { entrance_pop_up(); }
        Thread client = new Thread(new Ex2());
        client.start();

    }
    public static void entrance_pop_up(){
        levels=new String[24];
        for(int i=0;i<24;i++){
            levels[i]=Integer.toString(i);
        }
        String ID_l="ID";
        String Choose_level="Choose a level";
        Icon errorIcon = UIManager.getIcon("OptionPane.errorIcon");
        id=Long.parseLong(JOptionPane.showInputDialog(null,ID_l));
        scenario_num=JOptionPane.showOptionDialog(null,Choose_level,Choose_level,JOptionPane.PLAIN_MESSAGE,1,errorIcon,levels,0);

    }

    @Override
    public void run() {

        game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games

        game.login(id);
        String JsonGraph = game.getGraph();
        System.out.println(JsonGraph);


        try {
            PrintWriter pw = new PrintWriter(new File("JsonGraph.json"));
            pw.write((JsonGraph));
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        }
        String pks = game.getPokemons();
        DWGraph_Algo graph_algo= new DWGraph_Algo();
        graph_algo.load("JsonGraph.json");
        directed_weighted_graph graph =graph_algo.getGraph() ;

        init(game,graph);

        game.startGame();

        int ind=0;

        while(game.isRunning()) {
          moveAgents(game, graph);
            //System.out.println(game.getAgents());
            try {
                if(ind%1==0) {_win.repaint();}
                Thread.sleep(dt);
                ind++;
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        String res = game.toString();
        System.out.println(res);
      System.exit(0);
    }
    /**
     * Moves each of the agents along the edge
      * @param game
     * @param
     */
    public void moveAgents(game_service game, directed_weighted_graph graph) {
        game.move();

        String lg = game.getAgents();
        List<CL_Agent> log = Arena.getAgents(lg, graph);
        _ar.setAgents(log);

        String fs =  game.getPokemons();
        List<CL_Pokemon> ffs = Arena.json2Pokemons(fs);
        _ar.setPokemons(ffs);

        String JsonGraph = game.getGraph();

        String JsonAgents= game.getAgents();

        //ArrayList<CL_Agent> Agents= Arena.getAgents(JsonAgents,graph);
        for(int i=0;i<log.size();i++) {
            CL_Agent ag = log.get(i);
            int id = ag.getID();
            int dest = ag.getNextNode();
            int src = ag.getSrcNode();
            double v = ag.getValue();
            if(dest==-1) {
                dest = destNode(graph, src,i,game);
                //System.out.println(log.get(i).getID()"");
                game.chooseNextEdge(ag.getID(), dest);
                System.out.println("Agent: "+id+", from "+src+  " turned to node: "+dest);
            }
        }

//        for (CL_Agent agent: Agents) {
//            int destNode = destNode(graph,agent.getSrcNode(),game);
//            game.chooseNextEdge(agent.getID(),destNode);
//        }

    }
    /**
     * a very simple random walk implementation!
     * @param g
     * @param src
     * @return
     */
    public int destNode(directed_weighted_graph g, int src,int agentNumber, game_service game) {
     Collection<edge_data> edges = g.getE(src);

      ArrayList<CL_Pokemon> PokemonOnSrcEdges= PokemonOnEdges(edges,game,g);


        Comparator<CL_Pokemon> comparator= new Comparator<CL_Pokemon>() {
            @Override
            public int compare(CL_Pokemon o1, CL_Pokemon o2) {

                return Double.compare(o2.getValue(),o1.getValue());
            }
        };

        PokemonOnSrcEdges.sort(comparator);

        if(PokemonOnSrcEdges.iterator().hasNext()) {
            return PokemonOnSrcEdges.get(0).get_edge().getDest();
        }
        else {
            DWGraph_Algo graph_algo= new DWGraph_Algo();
            graph_algo.init(g);

            ArrayList<CL_Pokemon> allPokemons = Arena.json2Pokemons(game.getPokemons());

            for(int a = 0;a<allPokemons.size();a++) {
                Arena.updateEdge(allPokemons.get(a),g);}

            allPokemons.sort(new Comparator<CL_Pokemon>() {
                @Override
                public int compare(CL_Pokemon o1, CL_Pokemon o2) {
                    double cost1= o1.getValue()/(graph_algo.shortestPathDist(src,o1.get_edge().getSrc())*(graph_algo.shortestPath(src,o1.get_edge().getSrc()).size()));
                    double cost2= o2.getValue()/(graph_algo.shortestPathDist(src,o2.get_edge().getSrc())*(graph_algo.shortestPath(src,o2.get_edge().getSrc()).size()));

                    return Double.compare(cost2,cost1);
                }
            });

            ArrayList<Integer> srcPokemon = new ArrayList<>();
            for (CL_Pokemon pk : allPokemons) {
                srcPokemon.add(pk.get_edge().getSrc());
            }

          if (agentNumber>0){
              srcPokemon = removeDuplicates(srcPokemon);
            }


            // if there is more agents than pokemons
            if (srcPokemon.size()<agentNumber)
                return src;

           // CL_Pokemon destPokemon= allPokemons.get(agentNumber);

            int destNodePokemon= srcPokemon.get(agentNumber);

             return graph_algo.shortestPath(src,destNodePokemon).get(1).getKey();
        }

    }
    public ArrayList<Integer> removeDuplicates(ArrayList<Integer> list) {

        ArrayList<Integer> newList = new ArrayList<>();
        for (Integer a : list) {
            if (!newList.contains(a)) {

                newList.add(a);
            }
        }
        return newList;
    }


    public ArrayList<CL_Pokemon> PokemonOnEdges(Collection<edge_data> edges, game_service game, directed_weighted_graph g  ){
        ArrayList<CL_Pokemon> PokemonOnEdges = new ArrayList<>();

        ArrayList<CL_Pokemon> allPokemons = Arena.json2Pokemons(game.getPokemons());

        for(int a = 0;a<allPokemons.size();a++) {
            Arena.updateEdge(allPokemons.get(a),g);}

        for(CL_Pokemon pk:allPokemons ){
           if (edges.contains(pk.get_edge()))
               PokemonOnEdges.add(pk);
        }
       return PokemonOnEdges;

    }

    private void init(game_service game,directed_weighted_graph graph) {
        String g = game.getGraph();
        String fs = game.getPokemons();
        directed_weighted_graph gg = graph;
        //gg.init(g);
        _ar = new Arena();
        _ar.setGraph(gg);
        _ar.setGame(game);
        _ar.setPokemons(Arena.json2Pokemons(fs));
        _ar.setLevel(scenario_num);
        _win= new GUI_Frame(_ar);
        _win.setSize(1000, 700);


        _win.show();
        String info = game.toString();
        JSONObject line;
        try {
            line = new JSONObject(info);
            JSONObject ttt = line.getJSONObject("GameServer");
            int rs = ttt.getInt("agents");
            System.out.println(info);
            System.out.println(game.getPokemons());
            int src_node = 0;  // arbitrary node, you should start at one of the pokemon

            ArrayList<CL_Pokemon> cl_fs = Arena.json2Pokemons(game.getPokemons());


            for(int a = 0;a<cl_fs.size();a++) {
                Arena.updateEdge(cl_fs.get(a),gg);}

            Comparator<CL_Pokemon> comparator= new Comparator<CL_Pokemon>() {
                @Override
                public int compare(CL_Pokemon o1, CL_Pokemon o2) {
                    return Double.compare(o2.getValue(),o1.getValue());
                }
            };

            cl_fs.sort(comparator);

            int agentsNumber= rs;
            int pokemonsNumbers= cl_fs.size();

            if (agentsNumber<= pokemonsNumbers){
                for(int a = 0;a<rs;a++) {
                    int nodeLocation=cl_fs.get(a).get_edge().getSrc();
                    game.addAgent(nodeLocation);

                }
            }else {
                for(int a = 0;a<pokemonsNumbers;a++) {
                    int nodeLocation=cl_fs.get(a).get_edge().getSrc();
                    game.addAgent(nodeLocation);

                }
                int rest = agentsNumber-pokemonsNumbers;
                for(int a =0 ;a<rest;a++) {
                    game.addAgent((int) (graph.nodeSize()*Math.random()));
                  }
            }

        }
        catch (JSONException e) {e.printStackTrace();}
    }

}
