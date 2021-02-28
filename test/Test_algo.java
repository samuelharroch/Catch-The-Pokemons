import api.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Test_algo {
    public static directed_weighted_graph graph_creator (int v_size) {
        directed_weighted_graph g = new DS_DWGraph();
        for(int i=0;i<v_size;i++) {
            g.addNode(new NodeData(i));

        }
        return g;

    }
    @Test
    void isConnected() {
        directed_weighted_graph g0 = graph_creator(0);
        dw_graph_algorithms ag0 = new DWGraph_Algo();

        ag0.init(g0);
        assertTrue(ag0.isConnected());
        //one vertex
        g0 = graph_creator(1);
        ag0 = new DWGraph_Algo();
        ag0.init(g0);
        assertTrue(ag0.isConnected());


        g0.addNode(new NodeData(1));
        assertFalse(ag0.isConnected());

        // 2 nodes with one edge
        g0.connect(0,1,5);
        ag0.init(g0);
        assertFalse(ag0.isConnected());
        ag0.init(g0);
        g0 = graph_creator(5);
        ag0.init(g0);
        assertFalse(ag0.isConnected());
        //connect
        for (int i=0; i<4; i++){
            g0.connect(i,i+1,i+4);
        }
        ag0.init(g0);
        assertFalse(ag0.isConnected());
    }

    @Test
    void shortestPathDist() {
        directed_weighted_graph g0 = CreateGraph();
        dw_graph_algorithms ag0 = new DWGraph_Algo();
        ag0.init(g0);
        assertFalse(ag0.isConnected());
        double d = ag0.shortestPathDist(0,4);
        assertEquals(d, 6.0);
        d = ag0.shortestPathDist(10,43);
        assertNotEquals(d, 184.0);
        d = ag0.shortestPathDist(0,46);
        assertEquals(d, 7.0);

    }
    @Test
    void shortestPathDistBasicCases() {
        directed_weighted_graph graph = new DS_DWGraph();
        dw_graph_algorithms ag0 = new DWGraph_Algo();
        ag0.init(graph);
        for (int i = 0; i < 1; i++) {
            graph.addNode(new NodeData(i));

        }
        ag0.init(graph);
        assertEquals(0,ag0.shortestPathDist(0,0));

    }

    @Test
    void shortestPath() {
        directed_weighted_graph g0 = CreateGraph();
        dw_graph_algorithms ag0 = new DWGraph_Algo();
        ag0.init(g0);
        List<node_data> sp = ag0.shortestPath(0,10);
        //double[] checkTag = {0.0, 1.0, 2.0, 3.1, 5.1};
        int[] checkKey=new int[11];
        for (int i=0; i<11; i++)
            checkKey[i] = i;
        int i = 0;
        for(node_data n: sp) {
//            assertEquals(n.getTag(), checkTag[i]);
            assertEquals(n.getKey(), checkKey[i]);
            i++;
        }


    }

    @Test
    void save_load() {
        directed_weighted_graph g0 = Test_algo.graph_creator(10);
        dw_graph_algorithms ag0 = new DWGraph_Algo();
        ag0.init(g0);
        String str = "g0.obj";
        ag0.save(str);
        directed_weighted_graph g1 = Test_algo.graph_creator(10);
        ag0.load(str);
        assertEquals(g0,g1);
        g0.removeNode(0);
        assertNotEquals(g0,g1);
    }
    @Test
    void save_loadHugeGraph() {
        directed_weighted_graph g0 = Test_algo.graph_creator(100000);
        dw_graph_algorithms ag0 = new DWGraph_Algo();
        ag0.init(g0);
        String str = "g0.obj";
        ag0.save(str);
        directed_weighted_graph g1 = Test_algo.graph_creator(100000);
        ag0.load(str);
        assertEquals(g0, g1);
        g0.removeNode(0);
        assertNotEquals(g0, g1);
    }
    @Test
    void copy(){
        directed_weighted_graph g = Test_algo.graph_creator (10);
        dw_graph_algorithms ag0 = new DWGraph_Algo();
        ag0.init(g);
        directed_weighted_graph copy = ag0.copy();
        assertEquals(copy.getV().size(), g.getV().size());
        g.addNode(new NodeData(100));
        assertNotEquals(copy.getV().size(), g.getV().size());
        g.removeNode(100);
        assertEquals(copy.getV().size(), g.getV().size());


    }

    private directed_weighted_graph CreateGraph() {
        directed_weighted_graph g0 = new DS_DWGraph();
        int V =100;
        for (int i = 0; i<V; i++){
            g0.addNode(new NodeData(i));
        }
        // connect edge
        int E = 50;
        for (int i = 0; i<E; i++){
            g0.connect(i,i+1,i);
        }
        g0.connect(0,46,7);

        return g0;
    }
}