package gameClient;

import jaco.mp3.player.MP3Player;
import javazoom.jl.player.Player;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.io.FileInputStream;

public class Audio extends Thread {
    @Override
    public void run() {
        try {
            FileInputStream file = new FileInputStream("./src/gameClient/sound/101-opening.mp3"); //initialize the FileInputStream
            Player player= new Player(file); //initialize the player
            player.play(); //start the player
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Audio audio =new Audio();
        audio.start();
    }
}
