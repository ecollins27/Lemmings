package lemmings;

import lemmings.audio.Audio;
import lemmings.audio.AudioPlayer;
import lemmings.audio.Music;
import lemmings.screens.*;

import javax.sound.sampled.AudioInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DrawingPanel extends JPanel implements ActionListener, MouseListener, MouseMotionListener,KeyListener {

    Timer timer;
    private long lastUpdate;
    public Screen screen;
    public static double scale;
    public static int screenWidth;
    public static int screenHeight;
    public int currentLevel = 1;
    public Screen currentLevelScreen = null;
    public Screen pauseScreen,titleScreen;
    public static String[] codes;
    public static AudioPlayer audioPlayer;

    static {
        audioPlayer = new AudioPlayer(new Audio[]{Music.LEMMING1});
        audioPlayer.setMusicSelection(Music.DEFAULT_MUSIC_SELECTION);
//        codes = new String[100];
//        for (int i = 0; i < codes.length;i++){
//            codes[i] = String.valueOf(i + 1);
//        }
        Scanner file = null;
        try {
            file = new Scanner(new File("assets/codes.txt"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        codes = new String[100];
        int counter = 0;
        while (file.hasNextLine()){
            codes[counter] = file.nextLine().toUpperCase();
            counter++;
        }
    }

    public DrawingPanel(){
        screenWidth = this.getWidth();
        screenHeight = this.getHeight();
        scale = screenHeight / 250.0;
        pauseScreen = new PauseScreen(this);
        titleScreen = new TitleScreen(this);
        Cursor c = Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(1,1,BufferedImage.TRANSLUCENT),new Point(this.getX(),this.getY()),"img");
        this.setCursor(c);
        screen = titleScreen;
        addMouseMotionListener(this);
        addMouseListener(this);
        addKeyListener(this);
        timer = new Timer(1000/30,this);
        lastUpdate = System.currentTimeMillis();
        timer.start();
    }

    public void paintComponent(Graphics g){
        BufferedImage frame = screen.getFrame(this.getWidth(),this.getHeight());
        g.drawImage(frame,0,0,null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        screenWidth = this.getWidth();
        screenHeight = this.getHeight();
        scale = screenHeight / 250.0;
        double dt = (System.currentTimeMillis() - lastUpdate) / 1000.0;
        screen.update(dt);
        if (screen instanceof LoadingScreen && ((LoadingScreen) screen).isDone()){
            screen = ((LoadingScreen) screen).getLevelScreen();
            currentLevelScreen = screen;
        } else if (screen instanceof LevelScreen && ((LevelScreen) screen).isFinished()){
            screen = ((LevelScreen) screen).getNextScreen();
        }
        if (screen instanceof LevelScreen){
            currentLevel = ((LevelScreen) screen).getLevel().levelNum;
        }
        lastUpdate = System.currentTimeMillis();
        audioPlayer.update();
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE && !(screen instanceof TitleScreen || screen instanceof LoadingScreen)){
            if (screen instanceof PauseScreen){
                screen = currentLevelScreen;
            } else {
                screen = pauseScreen;
            }
        } else {
            screen.keyPressed(e.getKeyCode());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        screen.mouseClicked(e.getX(),e.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void addNotify() {
        super.addNotify();
        this.requestFocus();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        screen.mouseMoved(e.getX(),e.getY());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        screen.mouseMoved(e.getX(),e.getY());
    }
}
