package api;

import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

public class DS_DWGraph implements directed_weighted_graph {
    private HashMap<Integer,node_data> myNodes; //will represent us our graph (keys-int , value -node data)
    private HashMap<Integer,HashMap<Integer,edge_data>> InEdges;//will represent us the inner edges in the graph(from which node it arrived)
    private int NumOfEdges;//will count us the total amount of the edges in the graph
    private HashMap<Integer,HashMap<Integer,edge_data>> OutEdges;//will represent us the external edges in the graph(to which node it goes)
    private int MC;//a variable that represent us the changes tha were made during the work on the graph
    //constructor
    public DS_DWGraph(){
        myNodes = new HashMap<>();
        OutEdges= new HashMap<>();
        InEdges=new HashMap<>();

    }
    //copy constructor
    public DS_DWGraph(directed_weighted_graph g){
        myNodes= new HashMap<>();
        OutEdges=new HashMap<>();
        InEdges=new HashMap<>();
        for (node_data n:g.getV() ) {
            addNode(new NodeData(n));
        }

        for(node_data n: g.getV()){
            for (edge_data e:g.getE(n.getKey())) {
                connect(n.getKey(),e.getDest(),e.getWeight());
            }
        }
    }

    @Override
    public node_data getNode(int key) {
        return myNodes.get(key);
    } // This method returns the node_data by the node_id,null if the node doesn't exist

    @Override
    public edge_data getEdge(int src, int dest) {//This method returns the edge data by the her source and her destination,null if the node doesn't exist

        return  OutEdges.get(src).get(dest);
    }

    @Override
    ///method to add an node to our graph
    public void addNode(node_data n) {
        if (!myNodes.containsKey(n.getKey())) {//checking if this node already exist
            //informing our lists that there is a new node(still not connecting him to another node)
            InEdges.put(n.getKey(),new HashMap<Integer,edge_data>());
            myNodes.put(n.getKey(), new NodeData(n));
            OutEdges.put(n.getKey(),new HashMap<Integer,edge_data>());
            InEdges.put(n.getKey(),new HashMap<Integer,edge_data>());
            MC++;// a change was made - updating MC
        }
    }

    @Override
    public void connect(int src, int dest, double w) {
        if(w>=0&&src!=dest&&myNodes.containsKey(src)&&myNodes.containsKey(dest)&&getEdge(src,dest)==null){//basic cases
            NumOfEdges++;//there is no an edge between the nodes so we add one and updating the num of edge
            OutEdges.get(src).put(dest,new EdgeData(src,dest,w));//connecting and updating our inner edges
            InEdges.get(dest).put(src,new EdgeData(src,dest,w));//connecting and updating our external edges
            MC++;// a change was made - updating MC

        }

    }

    @Override
    public Collection<node_data> getV() {
        return myNodes.values();
    }    //this method gives us the list of all our nodes


    @Override
    //This method returns a pointer (shallow copy) for the
    //	 collection representing all the edges getting out of
    public Collection<edge_data> getE(int node_id) {
       if(myNodes.containsKey(node_id))
           return OutEdges.get(node_id).values();
       return null;
    }

    @Override
    //this method removes a node of the graph
    public node_data removeNode(int key) {
        if (myNodes.containsKey(key)){//checking that he node exist in the graph
            //updating the lists:
            for (int keyNei :OutEdges.keySet()){
                InEdges.get(keyNei).remove(key);//
            }
            for (int keyNei :InEdges.keySet()){
                OutEdges.get(keyNei).remove(key);
            }
            NumOfEdges-= InEdges.remove(key).size();//a edge was removed - updating the size
            NumOfEdges-= OutEdges.remove(key).size();//a edge was removed - updating the size
            MC++;//change was made
            return myNodes.remove(key);
        }
        return null;
    }

    @Override

    //this method removes a edge of the graph
    public edge_data removeEdge(int src, int dest) {
        if(src!=dest&&myNodes.containsKey(src)&&myNodes.containsKey(dest)&&getEdge(src,dest)!=null){
            NumOfEdges--;//a edge was removed
            MC++;//a change was made
            InEdges.get(dest).remove(src);//removing and updating our list
          return   OutEdges.get(src).remove(dest);
        }
            return null;
    }

    @Override
    // this method return us how many nodes we have in our graph
    public int nodeSize() {
        return myNodes.size();
    }

    @Override
    // this method return us how many edges we have in our graph
    public int edgeSize() {
        return NumOfEdges;
    }

    @Override
    // this method return us how much changes were made in our graph
    public int getMC() {
        return MC;
    }

    @Override
    // a method that checks if 2 graphs are the same (by keys and by values)
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DS_DWGraph graph = (DS_DWGraph) o;
        return NumOfEdges == graph.NumOfEdges &&
                Objects.equals(myNodes, graph.myNodes) &&
                Objects.equals(InEdges, graph.InEdges) &&
                Objects.equals(OutEdges, graph.OutEdges);
    }

    @Override
    public int hashCode() {
        return Objects.hash(myNodes, InEdges, NumOfEdges, OutEdges);
    }
}
