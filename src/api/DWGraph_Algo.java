package api;

import java.io.*;
import java.util.*;
import com.google.gson.*;

public class DWGraph_Algo implements dw_graph_algorithms {
    directed_weighted_graph myGraph;// our graph we will work with for the algorithms

    public DWGraph_Algo() {
        myGraph=new DS_DWGraph();
    }//constructor

    @Override
    public void init(directed_weighted_graph g) {
        this.myGraph=g;
    }

    @Override
    public directed_weighted_graph getGraph() {
        return this.myGraph;
    }

    @Override
    public directed_weighted_graph copy() {
        return new DS_DWGraph(myGraph);
    }
    //helper method which transpose a graph ( changing dest to source and source to dest in the edges)
    public directed_weighted_graph transpose() {
        DS_DWGraph transpose = new DS_DWGraph();
        for (node_data n : myGraph.getV()) {
            transpose.addNode(new NodeData(n.getKey()));//first we add the ndes
        }
        for (node_data n : myGraph.getV()) {
            for (edge_data e : myGraph.getE(n.getKey())) {
                transpose.connect(e.getDest(), e.getSrc(), e.getWeight());//src=dest,dest=src in the transpose graph)
            }
        }
        return transpose;
    }

    //set tags to 0
    private void setTags(Collection<node_data> nodeList) {
        for (node_data node : nodeList) {
            node.setTag(0);
        }
    }

    // implements the Dfs algorithms - using on transpose graph
    public void DFS(node_data n,Set<node_data> component,directed_weighted_graph transpose){
        n.setTag(1);
             component.add(n);
             for(edge_data e : transpose.getE(n.getKey()) ){
                 if (transpose.getNode(e.getDest()).getTag()!=1)
                     DFS(transpose.getNode(e.getDest()),component,transpose);

        }
    }

    // implements the Dfs algorithms - using on the original  graph
    public void fillOrder(node_data n, Stack<node_data> stack){
        n.setTag(1);
        for(edge_data e : myGraph.getE(n.getKey()) ){
            if (myGraph.getNode(e.getDest()).getTag()!=1)
                fillOrder(myGraph.getNode(e.getDest()),stack);
        }
        stack.push(n);
    }

    @Override
    //this method checks if a graph is strongly connected by using a transpose graph and comparing the components
    public boolean isConnected() {
        Stack<node_data> stack =new Stack();
        setTags(myGraph.getV());//set tags to 0

        directed_weighted_graph transpose = transpose();
        setTags(transpose.getV());//set tags to 0

        for (node_data n:myGraph.getV()) {
            if (myGraph.getNode(n.getKey()).getTag()==0)
                fillOrder(n,stack);//run dfs while there are still unvisited vertex
        }


        Set<Set<node_data>> components= new HashSet<>();

        while (!stack.isEmpty()){
            node_data current = transpose.getNode(stack.pop().getKey());
            if(current.getTag()==0){//unvisited vertex
                Set<node_data> currentComponent= new HashSet<>();
                DFS(current,currentComponent,transpose);
                components.add(currentComponent);

            }
        }

        return components.size()<=1;
    }

    @Override
    public double shortestPathDist(int src, int dest) {
        // if one node doesn't exist
        if (myGraph.getNode(src)==null || myGraph.getNode(dest)==null) return -1;
        // path node to himself
        if (src==dest)return 0;

        node_data[] path = dijkstra(src);
        if(path[(int)myGraph.getNode(dest).getTag()]== null) return -1;

        Stack<node_data> s= new Stack<>(); // to reconstruct the path
        s.push(myGraph.getNode(dest)); // push the dest node

        // such of backtracking from the dest to the scr
        for (int i = (int) myGraph.getNode(dest).getTag(); path[i]!=null; i= (int) path[i].getTag())
            s.push(path[i]);

        double dist = 0;
        while (s.size()>1){
            dist+= myGraph.getEdge(s.pop().getKey(),s.peek().getKey()).getWeight();
        }
        return dist;
    }

    private node_data[] dijkstra(int src){
        setTags(myGraph.getV());

        int index;
        double minValue, newDist;
        Pair curr;

        // using the tag parameters of each node
        int tag=0;
        for (node_data n: myGraph.getV()) {
            n.setTag(tag++);

        }
        // array of boolean representing the nodes we visit using the tag/index
        boolean [] visited = new boolean[myGraph.nodeSize()];
        // array of nodes using the index as the tag of visited node and
        // prev[i]= The node we pass through to visit the node associate with the tag using the index
        node_data[] prev= new node_data[myGraph.nodeSize()];
        // array of distance between the src node to the other node connected to him (not directly)
        double[] dist = new double[myGraph.nodeSize()];
        Arrays.fill(dist, Double.MAX_VALUE); // fill the array to infinity

        dist[(int) myGraph.getNode(src).getTag()]=0;

        // PriorityQueue using pair of (node , and the distance from the src to him)
        PriorityQueue < Pair > pq = new PriorityQueue();
        pq.add(new Pair(myGraph.getNode(src),0));

        while (pq.size()!=0){
            curr= pq.poll();// poll the first node in the queue, we don't want to visit him, again
            index= (int) curr.n.getTag(); // keep the index/tag of the polled node
            minValue= curr.weight; // keep  the distance from the src to the polled node
            visited[index]=true;
            if(dist[index]< minValue) continue; // if the current distance is better than the proposing one
            for (edge_data e: myGraph.getE(curr.n.getKey())) //loop over the neighbors of the polled node
            {
                if(visited[(int)myGraph.getNode(e.getDest()).getTag()]) continue;
                newDist= dist[index] + myGraph.getEdge(curr.n.getKey(), myGraph.getNode(e.getDest()).getKey()).getWeight();
                if(newDist< dist[(int) myGraph.getNode(e.getDest()).getTag()]) //if the new dist from the src to the polled node is better - update
                {
                    prev[(int) myGraph.getNode(e.getDest()).getTag()]= curr.n ;
                    dist[(int) myGraph.getNode(e.getDest()).getTag()]= newDist;
                    pq.add(new Pair(myGraph.getNode(e.getDest()),newDist));
                }
            }
        }
        return prev; // returning the path
    }

    @Override
    public List<node_data> shortestPath(int src, int dest) {
        // if one node doesn't exist
        if (myGraph.getNode(src)==null || myGraph.getNode(dest)==null) return null;

        // reverse the path
        List<node_data> l= new ArrayList<node_data>();
        if (src==dest){
            l.add(myGraph.getNode(src));
            return l;
        }

        node_data[] path = dijkstra(src);
        if(path[(int)myGraph.getNode(dest).getTag()]== null) return null;

        Stack<node_data> s= new Stack<>(); // to reconstruct the path
        s.push(myGraph.getNode(dest)); // push the dest node

        // such of backtracking from the dest to the scr
        for (int i = (int) myGraph.getNode(dest).getTag(); path[i]!=null; i= (int) path[i].getTag())
            s.push(path[i]);

        while (!s.isEmpty())
            l.add(s.pop());

        return l;
    }

    @Override
    public boolean save(String file) {
        JsonObject jsonGraph = new JsonObject();
        JsonArray jsonArrayNodes= new JsonArray();
        JsonArray jsonArrayEdges = new JsonArray();
        jsonGraph.add("Nodes", jsonArrayNodes);
        jsonGraph.add("Edges", jsonArrayEdges);
        for (node_data node: myGraph.getV()) {
            JsonObject jsonNode= new JsonObject();
            jsonNode.addProperty("id", node.getKey());
            jsonNode.addProperty("pos",""+node.getLocation().x()+","+node.getLocation().y()+","+node.getLocation().z()+"");
            jsonArrayNodes.add(jsonNode);

            for (edge_data edge_data:myGraph.getE(node.getKey())) {
                JsonObject JsonEdge = new JsonObject();
                JsonEdge.addProperty("src",edge_data.getSrc());
                JsonEdge.addProperty("dest",edge_data.getDest());
                JsonEdge.addProperty("w",edge_data.getWeight());
                jsonArrayEdges.add(JsonEdge);

            }
        }
        try {
            Gson gson=new Gson();
            PrintWriter pw = new PrintWriter(new File(file));
            pw.write(gson.toJson(jsonGraph));
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean load(String file) {
        try {


        Gson gson = new Gson();
        JsonObject json = new JsonParser().parse(new FileReader(file)).getAsJsonObject();

        directed_weighted_graph graph= new DS_DWGraph();

        JsonArray jsonArrayNodes= json.getAsJsonArray("Nodes");
        JsonArray jsonArrayEdges= json.getAsJsonArray("Edges");
            for (JsonElement jsoneNode:jsonArrayNodes) {
                String [] geo_location= ((JsonObject)jsoneNode).get("pos").getAsString().split(",");
                geo_location gl = new GeoLocation(Double.parseDouble(geo_location[0]),Double.parseDouble(geo_location[1]),Double.parseDouble(geo_location[2]));
                node_data node= new NodeData(((JsonObject)jsoneNode).get("id").getAsInt());
                node.setLocation(gl);
                graph.addNode(node);
            }
            for (JsonElement jsonEdge: jsonArrayEdges){
                graph.connect(((JsonObject)jsonEdge).get("src").getAsInt(),((JsonObject)jsonEdge).get("dest").getAsInt(),
                ((JsonObject)jsonEdge).get("w").getAsDouble());

            }


            init(graph);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    static class Pair implements Comparable {

        node_data n;
        double weight;
        public Pair(node_data n, double weight) {
            this.n = n;
            this.weight = weight;
        }

        @Override
        public int compareTo(Object o) {
            Pair pr = (Pair)o;

            if(weight > pr.weight)
                return 1;
            else
                return -1;

        }

    }



}
