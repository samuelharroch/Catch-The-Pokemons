package gameClient;


import javax.swing.*;
import java.awt.*;

/**
 * This Class represents the frame on which the game operates.
 */
public class GUI_Frame extends JFrame {


    private GUI gui;
    private Arena _ar;

    private static boolean gameStarted=false;
    /**
     * Create our game based on the arena.
     * @param ar
     */
    public GUI_Frame(Arena ar){
        super();
        init();


        this.add(gui);



        //this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        //this.setLayout(new BorderLayout());

        gui.update(ar);
        this.pack();


    }


    public void update(Arena ar) {
        this._ar = ar;
        gui.update(ar);
    }
    public void init(){
        gui=new GUI();
        this.setUndecorated(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1400,800));
        this.setVisible(true);
        this.setSize(1400,800);
    }







}
