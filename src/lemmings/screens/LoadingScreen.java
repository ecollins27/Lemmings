package lemmings.screens;

import lemmings.DrawingPanel;
import lemmings.animation.AnimationManager;
import lemmings.animation.Animations;
import lemmings.audio.Music;
import lemmings.level.Level;
import lemmings.level.Terrain;
import lemmings.level.Theme;
import lemmings.ui.LevelUI;
import lemmings.ui.TutorialUI;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class LoadingScreen extends Screen {

    AnimationManager animationManager;
    double progress = 0;
    int levelNum;
    private BufferedImage levelImage;
    private int[] startLocation;
    private int[] endLocation;
    private int[] quantities;
    private int numLemmings;
    private int lemmingsRequired;
    private String tutorialText;
    Terrain terrain;
    Theme theme;

    public LoadingScreen(int levelNum){
//        if (levelNum >= 11 && !DrawingPanel.audioPlayer.getMusicSelection().contains(Music.MARS)){
//            DrawingPanel.audioPlayer.setMusicSelection(Music.LAVA_MUSIC_SELECTION);
//        } else if (levelNum < 11 && DrawingPanel.audioPlayer.getMusicSelection().contains(Music.MARS)){
//            DrawingPanel.audioPlayer.setMusicSelection(Music.DEFAULT_MUSIC_SELECTION);
//        }
        animationManager = new AnimationManager(Animations.WALKING_RIGHT,10);
        this.levelNum = levelNum;
        levelImage = null;
        try {
            levelImage = ImageIO.read(new File("levels/level" + levelNum + "/map.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        readDataFile("levels/level" + levelNum + "/data.txt");
        Thread thread = new Thread(new LevelLoader());
        thread.start();
    }
    @Override
    public BufferedImage getFrame(int width, int height) {
        BufferedImage frame = new BufferedImage(width,height,BufferedImage.TRANSLUCENT);
        Graphics2D g = frame.createGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0,0,width,height);
        g.setColor(Color.GREEN);
        g.fillRect(width/30,height/2,(int)(progress * (width - width/15)),height/16);
        BufferedImage lemmingFrame = animationManager.getCurrentFrame();
        g.drawImage(lemmingFrame,width/30 + (int)(progress * (width - width/15)) - 20*lemmingFrame.getWidth()/2,height/2 - height/40 - lemmingFrame.getHeight() * width/75,width / 75 *lemmingFrame.getWidth(),width/75*lemmingFrame.getHeight(),null);
        return frame;
    }

    @Override
    public void update(double dt) {
        animationManager.update(dt);
    }

    @Override
    public void keyPressed(int keyCode) {

    }

    @Override
    public void mouseClicked(int x, int y) {

    }

    @Override
    public void mouseMoved(int x, int y) {

    }

    public boolean isDone(){
        return progress == 1;
    }

    public LevelScreen getLevelScreen(){
        Level level = new Level(startLocation,endLocation,terrain, levelImage.getWidth(), levelImage.getHeight(),levelNum,numLemmings,lemmingsRequired,theme,terrain.getTerrainTexture(levelNum, levelImage.getWidth(), levelImage.getHeight(),theme),quantities);
        LevelUI ui = new LevelUI(level);
        TutorialUI tutorialUI = new TutorialUI(level,tutorialText);
        return new LevelScreen(level,ui,tutorialUI);
    }

    private void readDataFile(String fileName){
        Scanner file = null;
        try {
            file = new Scanner(new File(fileName));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        startLocation = null;
        endLocation = null;
        numLemmings = -1;
        lemmingsRequired = -1;
        quantities = null;
        theme = null;
        while (file.hasNextLine()){
            String line = file.nextLine();
            if (startLocation == null){
                startLocation = new int[]{Integer.parseInt(line.substring(7,line.lastIndexOf(","))),Integer.parseInt(line.substring(line.lastIndexOf(",") + 1))};
            } else if (endLocation == null){
                endLocation = new int[]{Integer.parseInt(line.substring(5,line.lastIndexOf(","))),Integer.parseInt(line.substring(line.lastIndexOf(",") + 1))};
            } else if (numLemmings == -1){
                numLemmings = Integer.parseInt(line.substring(10));
            } else if (lemmingsRequired == -1){
                lemmingsRequired = Integer.parseInt(line.substring(18));
            } else if (theme == null){
                theme = getTheme(line.substring(7));
            } else if (quantities == null){
                String[] split = line.substring(line.indexOf(" ") + 1).split(",");
                quantities = new int[split.length];
                for (int i = 0; i < quantities.length;i++){
                    quantities[i] = Math.max(0,Math.min(99,Integer.parseInt(split[i])));
                }
            } else {
                tutorialText = line;
            }
        }
    }

    private Theme getTheme(String substring) {
        if (substring.equals("default")){
            return Theme.DEFAULT;
        } else if (substring.equals("lava")){
            return Theme.LAVA;
        }
        return null;
    }

    private class LevelLoader implements Runnable {

        @Override
        public void run() {
            terrain = new Terrain(levelImage.getWidth(),levelImage.getHeight());
            for (int i = -1; i <= levelImage.getWidth();i++){
                for (int j = -1; j <= levelImage.getHeight();j++){
                    progress = (double)((i+1) * levelImage.getHeight() + (j+1)) / ((levelImage.getWidth()+2) * (levelImage.getHeight()+2));
                    if (i == -1 || j == -1 || i == levelImage.getWidth() || j == levelImage.getHeight()){
                        terrain.addUnmineable(new Rectangle(i,j,1,1));
                    } else {
                        int color = levelImage.getRGB(i, j);
                        if (color == Color.BLACK.getRGB()) {
                            terrain.add(new Area(new Rectangle(i, j, 1, 1)));
                        } else if (color == Color.GRAY.getRGB()) {
                            terrain.addUnmineable(new Rectangle(i, j, 1, 1));
                        }
                    }
                }
            }
            progress = 1;
        }
    }
}
