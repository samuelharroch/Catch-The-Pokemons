import api.DS_DWGraph;
import api.NodeData;
import api.directed_weighted_graph;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class WGraph_test {
    private static final Random _rnd = null;


    public static directed_weighted_graph graph_creator (int v_size) {
        directed_weighted_graph g = new DS_DWGraph();
        for(int i=0;i<v_size;i++) {
            g.addNode(new NodeData(i));
        }
        return g;
    }


    //test for runtime making a large graph
    @Test
    void AddNodeAndRemove() {
        directed_weighted_graph graph = new DS_DWGraph();
        for (int i = 0; i < 1000000; i++) {
            graph.addNode(new NodeData(i));
            if (i%100==0)
                graph.addNode(new NodeData(i)); //the key exist

        }
        assertEquals(1000000, graph.nodeSize());
        for (int i = 10; i < 20; i++) {
            graph.removeNode(i);
            graph.removeNode(i);
        }
        assertEquals((1000000-10), graph.nodeSize());


    }
    @Test
    void AddEdgesAndRemove() {
        directed_weighted_graph   graph = new DS_DWGraph();
        int v =1000000,e = 1000000;
        graph.addNode(new NodeData(0));
        for (int i = 1; i < e+1; i++) {
            graph.addNode(new NodeData(i));
            graph.connect(i,i-1,i-1);
        }
        assertEquals(e, graph.edgeSize());
        for (int i = 1; i < (30000+1); i++) {
            graph.removeEdge(i,i-1);
            graph.removeEdge(i,i-1);
        }
        e= e-30000;
        assertEquals(e, graph.edgeSize());
    }
    @Test
    void removeNode() {
        directed_weighted_graph g = new DS_DWGraph();
        for (int i=0; i<=3; i++){
            g.addNode(new NodeData(i));
        }
        for (int i=0; i<3; i++) {
            g.connect(0, i+1, i+1);
        }
        g.removeNode(4); //not exist
        g.removeNode(0);
        assertEquals(3,g.nodeSize());


        int e = g.edgeSize();
        assertEquals(0,e);
        int v= g.nodeSize();
        assertEquals(3,v);
    }
    // Test for addNode function

    @Test
    void addNode(){
        directed_weighted_graph   graph = new DS_DWGraph();
        graph.addNode(new NodeData(1));
        graph.addNode(new NodeData(2));
        graph.addNode(new NodeData(3));
        graph.addNode(new NodeData(4));
        graph.addNode(new NodeData(5));
        graph.addNode(new NodeData(6));
        graph.addNode(new NodeData(7));
        graph.connect(1,2,3);
        graph.connect(2,3,3);
        graph.connect(3,4,3);
        graph.connect(4,5,3);
        graph.connect(5,6,3);
        graph.connect(6,7,3);
        assertEquals(7, graph.nodeSize());
        assertNotEquals(8, graph.nodeSize());


    }
    //this test makes sure that we cannot add and exist key to the graph
    @Test
    void addExistsNode (){
        directed_weighted_graph   graph = new DS_DWGraph();
        graph.addNode(new NodeData(1));
        graph.addNode(new NodeData(1));
        graph.addNode(new NodeData(1));
        graph.addNode(new NodeData(1));
        assertEquals(1, graph.nodeSize());
    }

    @Test
    void removeNotExistNode (){
        directed_weighted_graph   graph = new DS_DWGraph();
        graph.addNode(new NodeData(1));
        graph.removeNode(7);
        assertEquals(1, graph.nodeSize());

    }



}

