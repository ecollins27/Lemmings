package lemmings.level;

import lemmings.DrawingPanel;
import lemmings.animation.AnimationManager;
import lemmings.animation.Animations;
import lemmings.audio.SoundEffects;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Level {

    int[] startLocation;
    int[] endLocation;
    Terrain terrain;
    public int width,height;
    public int levelNum;
    int numLemmings;
    int lemmingsAdded = 0;
    public int lemmingsRequired;
    public int lemmingsCompleted = 0;
    BufferedImage terrainTexture;
    private AnimationManager startManager,endManager;
    private boolean startLevel = false;
    ArrayList<Lemming> lemmings;
    private double lastLemming;
    public int[] quantities;
    public int playSpeed = 1;
    private boolean hasWon = false;
    boolean updateTerrain = false;
    Theme theme;

    final private static int LEMMINGS_PER_SEC = 1;

    public Level(int[] startLocation, int[] endLocation, Terrain terrain, int width, int height, int levelNum, int numLemmings, int lemmingsRequired, Theme theme, BufferedImage terrainTexture, int[] quantities) {
        this.quantities = quantities;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.terrain = terrain;
        this.width = width;
        this.height = height;
        this.levelNum = levelNum;
        this.numLemmings = numLemmings;
        this.lemmingsRequired = lemmingsRequired;
        this.theme = theme;
        this.terrainTexture = terrainTexture;
        lemmingsAdded = 0;
        lastLemming = 0;
        lemmings = new ArrayList<>();
        startManager = new AnimationManager(theme == Theme.DEFAULT? Animations.ENTRANCE_OPENING_DEFAULT: Animations.ENTRANCE_OPENING_LAVA,10,1);
        endManager = new AnimationManager(theme == Theme.DEFAULT? Animations.DEFAULT_EXIT:Animations.LAVA_EXIT,10);
    }

    public void draw(Graphics2D g){
        if (theme == Theme.DEFAULT) {
            g.setColor(Color.BLACK);
        } else if (theme == Theme.LAVA){
            g.setColor(new Color(255,166,77));
        }
        g.fillRect(0,0,width,height);
        BufferedImage exitFrame = endManager.getCurrentFrame();
        BufferedImage entranceFrame = startManager.getCurrentFrame();
        g.drawImage(entranceFrame,startLocation[0] - entranceFrame.getWidth()/2,startLocation[1] - entranceFrame.getHeight()/2,null);
        g.drawImage(exitFrame,endLocation[0] - exitFrame.getWidth()/2,endLocation[1] - exitFrame.getHeight()/2,null);
        for (Lemming lemming: lemmings){
            lemming.draw(g);
        }
        BufferedImage entranceFront = entranceFrame.getSubimage(0,0,entranceFrame.getWidth(),4);
        g.drawImage(entranceFront,startLocation[0] - entranceFrame.getWidth()/2,startLocation[1] - entranceFrame.getHeight()/2,null);
        g.drawImage(terrainTexture,0,0,width, height,null);

    }

    public BufferedImage getFrame(){
        BufferedImage frame = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        draw(frame.createGraphics());
        return frame;
    }

    public void update(double dt, boolean updateTexture){
        for (int i = lemmings.size() - 1; i >= 0;i--){
            Lemming lemming = lemmings.get(i);
            boolean result = lemming.update(terrain,theme,endLocation,dt);
            if (result){
                updateTerrain = true;
            } if (lemming.isDead()){
                lemmings.remove(i);
            } else if (lemming.hasCompleted()){
                lemmings.remove(i);
                lemmingsCompleted++;
                DrawingPanel.audioPlayer.playSoundEffect(SoundEffects.YIPPEE);
            } else if (lemming.pos[0] < - 10 || lemming.pos[0] > width || lemming.pos[1] <  -10 || lemming.pos[1] > height + 10){
                lemming.kill();
            }
        }
        endManager.update(dt);
        if (lemmingsAdded < numLemmings && startManager.getCurrentAnimation() == null){
            lastLemming += dt;
        }
        if (startLevel){
            startManager.update(dt);
        } if (startManager.getCurrentAnimation() == null && lastLemming > 1.0 / LEMMINGS_PER_SEC && lemmingsAdded < numLemmings){
            Lemming lemming = new Lemming(startLocation[0],startLocation[1] - 10);
            lemmings.add(lemming);
            lemmingsAdded++;
            lastLemming = lastLemming % (1.0 / LEMMINGS_PER_SEC);
        }
        if (updateTerrain && updateTexture){
            terrainTexture = terrain.getTerrainTexture(levelNum,width,height,theme);
            updateTerrain = false;
        }
        hasWon = lemmingsCompleted >= lemmingsRequired;
    }

    public Lemming getLemming(Rectangle2D mouse){
        double xCoord = mouse.getX() / DrawingPanel.scale;
        double yCoord = mouse.getY() / DrawingPanel.scale;
        double mouseWidth = mouse.getWidth() / DrawingPanel.scale;
        double mouseHeight = mouse.getHeight() / DrawingPanel.scale;
        Rectangle2D newMouse = new Rectangle2D.Double(xCoord,yCoord,mouseWidth,mouseHeight);
        for (Lemming lemming: lemmings){
            Rectangle2D hitbox = lemming.getCurrentHitbox();
            Rectangle2D actingHitbox = new Rectangle2D.Double(lemming.pos[0] - hitbox.getWidth()/2,lemming.pos[1] - hitbox.getHeight(),hitbox.getWidth(),hitbox.getHeight());
            if (actingHitbox.intersects(newMouse) || actingHitbox.contains(newMouse)){
                return lemming;
            }
        }
        return null;
    }

    public void startLevel(){
        startLevel = true;
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
        while (file.hasNextLine()){
            String line = file.nextLine();
            if (startLocation == null){
                startLocation = new int[]{Integer.parseInt(line.substring(7,line.lastIndexOf(","))),Integer.parseInt(line.substring(line.lastIndexOf(",") + 1))};
            } else if (endLocation == null){
                endLocation = new int[]{Integer.parseInt(line.substring(5,line.lastIndexOf(","))),Integer.parseInt(line.substring(line.lastIndexOf(",") + 1))};
            } else {
                numLemmings = Integer.parseInt(line.substring(10));
            }
        }
    }
}
