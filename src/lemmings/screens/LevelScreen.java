package lemmings.screens;

import lemmings.DrawingPanel;
import lemmings.audio.SoundEffects;
import lemmings.level.Job;
import lemmings.level.Lemming;
import lemmings.level.Level;
import lemmings.ui.LevelCompleteUI;
import lemmings.ui.LevelUI;
import lemmings.ui.TutorialUI;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class LevelScreen extends Screen {

    Level level;
    LevelUI ui;
    TutorialUI tutorialUI;
    LevelCompleteUI levelCompleteUI;
    Lemming selectedLemming = null;

    int[] mouseCoords = new int[]{0,0};
    private int width,height;
    private boolean useCursorOn = false;
    private double uiWidth,uiHeight;
    private boolean start = false;
    private boolean finish = false;
    double[] terrainShift = new double[2];
    final static Job[] jobs = new Job[]{Job.WALKER, Job.DIGGER, Job.TUNNELER, Job.FLOATER, Job.BLOCKER, Job.BUILDER};

    public LevelScreen(Level level, LevelUI ui, TutorialUI tutorialUI){
        uiWidth = Math.min(DrawingPanel.screenWidth,330 * DrawingPanel.scale);
        uiHeight = 50 * uiWidth / 330.0;
        this.tutorialUI = tutorialUI;
        this.level = level;
        this.ui = ui;
        this.levelCompleteUI = new LevelCompleteUI(level);
    }
    @Override
    public BufferedImage getFrame(int width, int height) {
        this.width = width;
        this.height = height;
        BufferedImage frame = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        Graphics2D g = frame.createGraphics();
        BufferedImage bufferedImage = level.getFrame();
        if (selectedLemming != null) {
            Graphics2D g2 = bufferedImage.createGraphics();
            g2.setColor(Color.WHITE);
            Font berlinSans = new Font("Berlin Sans FB", Font.PLAIN, 12);
            g2.setFont(berlinSans);
            Rectangle2D bounds = g.getFontMetrics().getStringBounds(String.valueOf(selectedLemming.getJob()),g2);
            g2.drawString(String.valueOf(selectedLemming.getJob()),(int)(selectedLemming.pos[0] - bounds.getWidth()/2),(int)(selectedLemming.pos[1] - 13));
        }
        g.drawImage(bufferedImage,(int)terrainShift[0],(int)terrainShift[1],(int)(DrawingPanel.scale * bufferedImage.getWidth()),(int)(DrawingPanel.scale * bufferedImage.getHeight()),null);
        BufferedImage uiFrame = ui.getFrame();
        if (!start){
            BufferedImage tutorialUIFrame = tutorialUI.getFrame();
            g.drawImage(tutorialUIFrame,DrawingPanel.screenWidth/8, (int) (DrawingPanel.scale * tutorialUIFrame.getHeight()/12), (int) (tutorialUIFrame.getWidth() * DrawingPanel.scale), (int) (tutorialUIFrame.getHeight() * DrawingPanel.scale),null);
        } else if (level.lemmingsCompleted >= level.lemmingsRequired){
            BufferedImage levelCompleteUIFrame = levelCompleteUI.getFrame();
            g.drawImage(levelCompleteUIFrame,
                    DrawingPanel.screenWidth/8,
                    (int) (DrawingPanel.scale * levelCompleteUIFrame.getHeight()/3),
                    (int) (levelCompleteUIFrame.getWidth() * DrawingPanel.scale),
                    (int) (levelCompleteUIFrame.getHeight() * DrawingPanel.scale),
                    null);
        }
        g.setColor(Color.BLACK);
        g.fillRect(0,(int)(height - uiHeight),DrawingPanel.screenWidth,(int)uiHeight);
        g.drawImage(uiFrame,0,(int)(height - uiHeight),(int)(uiWidth),(int)(uiHeight),null);
        g.drawImage(useCursorOn? mouseOn:mouseOff,mouseCoords[0] - width/50,mouseCoords[1] - width/50,width/25,width/25,null);
        return frame;
    }

    @Override
    public void update(double dt) {
        for (int i = 0; i < level.playSpeed;i++) {
            level.update(dt,i == level.playSpeed - 1);
        }
        selectedLemming = level.getLemming(new Rectangle2D.Double(mouseCoords[0] - 10 - terrainShift[0],mouseCoords[1] - 10 - terrainShift[1],20,20));
        useCursorOn = selectedLemming != null;
        ui.update(level,dt);
        uiWidth = Math.min(DrawingPanel.screenWidth,330.0 * DrawingPanel.scale);
        uiHeight = 50 * uiWidth / 330.0;
    }

    @Override
    public void keyPressed(int keyCode) {
        switch (keyCode){
            case KeyEvent.VK_A:
                terrainShift[0] += 2 * DrawingPanel.scale;
                break;
            case KeyEvent.VK_S:
                terrainShift[1] -= 2 * DrawingPanel.scale;
                break;
            case KeyEvent.VK_D:
                terrainShift[0] -= 2 * DrawingPanel.scale;
                break;
            case KeyEvent.VK_W:
                terrainShift[1] += 2 * DrawingPanel.scale;
                break;
            case KeyEvent.VK_SPACE:
                if (!start) {
                    level.startLevel();
                    start = true;
                    DrawingPanel.audioPlayer.playSoundEffect(SoundEffects.LETS_GO);
                } else if (level.lemmingsCompleted >= level.lemmingsRequired){
                    finish = true;
                }
                break;
        }
        terrainShift[0] = Math.min(0,Math.max(terrainShift[0],DrawingPanel.screenWidth - level.width * DrawingPanel.scale));
        terrainShift[1] = Math.min(0,Math.max(terrainShift[1],DrawingPanel.screenHeight - level.height * DrawingPanel.scale));
        ui.keyPressed(keyCode);
        tutorialUI.keyPressed(keyCode);
    }

    public LoadingScreen getNextScreen(){
        return new LoadingScreen(level.levelNum + 1);
    }

    @Override
    public void mouseClicked(int x, int y) {
        int selectedIndex = ui.getSelectedIndex();
        if (selectedLemming != null && level.quantities[selectedIndex] > 0 && selectedLemming.getJob() != jobs[ui.getSelectedIndex()]){
            selectedLemming.setJob(jobs[ui.getSelectedIndex()]);
            level.quantities[selectedIndex]--;
        }
        ui.mouseClicked(x,y,0, (int) (height - uiHeight),uiWidth / 330.0);
    }

    @Override
    public void mouseMoved(int x, int y) {
        ui.mouseMoved(x,y,0, (int) (height - uiHeight),uiWidth / 330.0);
        mouseCoords = new int[]{x,y};
    }

    public boolean isFinished(){
        return finish;
    }

    public Level getLevel(){
        return level;
    }
}
