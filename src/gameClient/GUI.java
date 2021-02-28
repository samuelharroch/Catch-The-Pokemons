package gameClient;
import javax.swing.*;
import java.awt.*;

import java.util.Iterator;
import java.util.List;

import api.*;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;

/**
 * GUI : Used the given Myframe and converted it to panel,
 * as panel uses double buffer will keep our graphics from stuttering.
 */

public class GUI extends JPanel {
    private int _ind;
    private Arena _ar;
    private gameClient.util.Range2Range _w2f;

    /**
     *  Construtctor
     */
    public GUI() {
        super();
        int _ind = 0;
        this.setSize(1400,800);
        this.setVisible(true);
        setPreferredSize(new Dimension(1400,800));

    }


    /**
     * Update our arena.
     * @param ar
     */
    public void update(Arena ar) {
        this._ar = ar;
        updateFrame();
    }

    /**
     * Update our frame.
     */
    private void updateFrame() {
        Range rx = new Range(20,this.getWidth()-20);
        Range ry = new Range(this.getHeight()-10,150);
        Range2D frame = new Range2D(rx,ry);
        directed_weighted_graph g = _ar.getGraph();
        _w2f = Arena.w2f(g,frame);
    }

    /**
     * Paint our game.
     * @param g
     */
    public void paint(Graphics g) {
        int w = this.getWidth();
        int h = this.getHeight();
        g.clearRect(0, 0, w, h);
        Image img = new ImageIcon("./src/gameClient/images/background.png").getImage();
        g.drawImage(img, 0, 0,w,h, null);
        updateFrame();
        drawPokemons(g);
        drawGraph(g);
        drawAgants(g);
        drawInfo(g);
        drawScore(g);
    }
    private void drawScore(Graphics g){
        double Total_Score=0;
        List<CL_Agent> rs = _ar.getAgents();
        for(int i=0;rs!=null && i<rs.size() ;i++){
            Total_Score+=rs.get(i).getValue();

        }
        g.setColor(Color.BLACK);
        g.fillRect(this.getWidth()*6/100, this.getHeight()*2/100,this.getWidth()*26/100,this.getHeight()*10/100);
        g.setColor(Color.red);
        g.drawRect(this.getWidth()*6/100, this.getHeight()*2/100,this.getWidth()*26/100,this.getHeight()*10/100);
        g.setColor(Color.WHITE);
        double ratio=this.getHeight()/this.getWidth();
        Font bigFont = new Font("Dialog", Font.BOLD, (int)this.getWidth()/70);
        g.setFont(bigFont);
        g.drawString("Moves : "+ _ar.getMoves(),this.getWidth()*6/100,this.getHeight()*6/100);
        g.drawString("Time elapsed : "+ _ar.get_time()/1000,this.getWidth()*6/100,this.getHeight()*10/100);
        g.drawString("Score : "+ Total_Score,this.getWidth()*20/100,this.getHeight()*6/100);
        g.drawString("Level : "+ _ar.getLevel(),this.getWidth()*20/100,this.getHeight()*10/100);

    }
    private void drawInfo(Graphics g) {
        java.util.List<String> str = _ar.get_info();
        String dt = "none";
        for(int i=0;i<str.size();i++) {
            g.drawString(str.get(i)+" dt: "+dt,100,60+i*20);
        }

    }

    /**
     * Draws the graph : We iterate the nodes draw them and for each node we draw
     * the edges coming out of it.
     * @param g
     */
    private void drawGraph(Graphics g) {
        directed_weighted_graph gg = _ar.getGraph();
        Iterator<node_data> iter = gg.getV().iterator();
        while(iter.hasNext()) {
            node_data n = iter.next();
            g.setColor(Color.blue);
            drawNode(n,5,g);
            Iterator<edge_data> itr = gg.getE(n.getKey()).iterator();
            while(itr.hasNext()) {
                edge_data e = itr.next();
                g.setColor(Color.gray);
                drawEdge(e, g);
            }
        }
    }

    /**
     * Draw the pokemons
     * @param g
     */
    private void drawPokemons(Graphics g) {
        java.util.List<CL_Pokemon> fs = _ar.getPokemons();
        if(fs!=null) {
            Iterator<CL_Pokemon> itr = fs.iterator();

            while(itr.hasNext()) {

                CL_Pokemon f = itr.next();
                Point3D c = f.getLocation();
                int r=10;
                g.setColor(Color.green);
                if(f.getType()<0) {g.setColor(Color.orange);}
                if(c!=null) {

                    geo_location fp = this._w2f.world2frame(c);
                    //g.fillOval((int)fp.x()-r, (int)fp.y()-r, 2*r, 2*r);
                    if(f.getValue()>=14) {
                        Image img = new ImageIcon("./src/gameClient/images/dragonite.png").getImage();

                        g.drawImage(img, (int) fp.x() - r, (int) fp.y() - r-30,5*r,5*r, null);
                    }
                    if(f.getValue()<14 && f.getValue()>=10) {
                        Image img = new ImageIcon("./src/gameClient/images/grav.png").getImage();
                        g.drawImage(img, (int) fp.x() - r, (int) fp.y() - r-30, 5*r ,5*r ,null);
                    }
                    if(f.getValue()<10 && f.getValue()>5) {
                        Image img = new ImageIcon("./src/gameClient/images/char.png").getImage();
                        g.drawImage(img, (int) fp.x() - r, (int) fp.y() - r-30, 5*r, 5*r,null);
                    }
                    if(f.getValue()<=5 ) {
                        Image img = new ImageIcon("./src/gameClient/images/pichu.png").getImage();
                        g.drawImage(img, (int) fp.x() - r, (int) fp.y() - r-30, 5*r, 5*r, null);
                    }

                    g.drawString(""+f.getValue(), (int)fp.x(), (int)fp.y()-4*r);

                }
            }
        }
    }

    /**
     * Draw the agents of the graph.
     * @param g
     */
    private void drawAgants(Graphics g) {
        List<CL_Agent> rs = _ar.getAgents();
        //Iterator<OOP_Point3D> itr = rs.iterator();
        g.setColor(Color.red);
        int i=0;
        while(rs!=null && i<rs.size()) {
            geo_location c = rs.get(i).getLocation();
            int r=8;
            i++;
            if(c!=null) {

                geo_location fp = this._w2f.world2frame(c);
                Image img = new ImageIcon("./src/gameClient/images/Poke_Ball.png").getImage();
                //Image scaleImage = img.getScaledInstance(50, 50,Image.SCALE_DEFAULT);
                g.drawImage(img,(int)fp.x()-r, (int)fp.y()-r, 3*r, 3*r, null);
                //g.fillOval((int)fp.x()-r, (int)fp.y()-r, 2*r, 2*r);
                g.drawString(""+i, (int)fp.x(), (int)fp.y()-4*r);
                g.drawString("value : "+rs.get(i-1).getValue(), (int)fp.x(), (int)fp.y()-4*r -40);
            }
        }
    }
    private void drawNode(node_data n, int r, Graphics g) {
        geo_location pos = n.getLocation();
        geo_location fp = this._w2f.world2frame(pos);
        if(n.getKey()%3==0) {
            Image img = new ImageIcon("./src/gameClient/images/b1.png").getImage();
            //Image scaleImage = img.getScaledInstance(50, 50,Image.SCALE_DEFAULT);
            g.drawImage(img, (int) fp.x() - r, (int) fp.y() - r, 6*r,6*r,null);
        }
        if(n.getKey()%2==0) {
            Image img = new ImageIcon("./src/gameClient/images/b2.png").getImage();
            //Image scaleImage = img.getScaledInstance(50, 50,Image.SCALE_DEFAULT);
            g.drawImage(img, (int) fp.x() - r, (int) fp.y() - r,6*r, 6*r, null);
        }
        if(n.getKey()%5==0) {
            Image img = new ImageIcon("./src/gameClient/images/b3.png").getImage();
            //Image scaleImage = img.getScaledInstance(50, 50,Image.SCALE_DEFAULT);
            g.drawImage(img, (int) fp.x() - r, (int) fp.y() - r,6*r, 6*r, null);
        }
        if(n.getKey()%7==0) {
            Image img = new ImageIcon("./src/gameClient/images/b4.png").getImage();
            //Image scaleImage = img.getScaledInstance(50, 50,Image.SCALE_DEFAULT);
            g.drawImage(img, (int) fp.x() - r, (int) fp.y() - r,6*r, 6*r, null);
        }
        //g.fillOval((int)fp.x()-r, (int)fp.y()-r, 2*r, 2*r);
        Font bigFont = new Font("Dialog", Font.BOLD, 16);
        g.setFont(bigFont);
        g.drawString(""+n.getKey(), (int)fp.x(), (int)fp.y()-4*r);
    }
    private void drawEdge(edge_data e, Graphics g) {
        directed_weighted_graph gg = _ar.getGraph();
        geo_location s = gg.getNode(e.getSrc()).getLocation();
        geo_location d = gg.getNode(e.getDest()).getLocation();
        geo_location s0 = this._w2f.world2frame(s);
        geo_location d0 = this._w2f.world2frame(d);
        g.setColor(Color.BLACK);
        g.drawLine((int)s0.x(), (int)s0.y(), (int)d0.x(), (int)d0.y());

    }
}
