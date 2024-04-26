package lemmings.screens;

import lemmings.DrawingPanel;
import lemmings.animation.AnimationManager;
import lemmings.animation.Animations;
import lemmings.ui.TitleUI;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

public class TitleScreen extends Screen {


    ArrayList<AnimationManager> animationManagers;
    ArrayList<double[]> positions;
    int[] mouseCoords = new int[2];
    TitleUI ui;
    private double uiX, uiY;

    public TitleScreen(DrawingPanel drawingPanel){
        ui = new TitleUI(drawingPanel);
        uiX = (DrawingPanel.screenWidth - 250.0 * DrawingPanel.scale) / 2.0;
        uiY = (DrawingPanel.screenHeight - 150.0 * DrawingPanel.scale) / 2.0;
        animationManagers = new ArrayList<>();
        positions = new ArrayList<>();
    }
    @Override
    public BufferedImage getFrame(int width, int height) {
        BufferedImage bufferedImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bufferedImage.createGraphics();
        drawLemmings(g);
        BufferedImage uiFrame = ui.getFrame();
        g.drawImage(uiFrame,(int)uiX,(int)uiY,(int)(uiFrame.getWidth() * DrawingPanel.scale), (int)(uiFrame.getHeight() * DrawingPanel.scale),null);
        g.drawImage(mouseOff,mouseCoords[0] - width/50,mouseCoords[1] - width/50,width/25,width/25,null);
        return bufferedImage;
    }

    private void drawLemmings(Graphics2D g){
        double scale = DrawingPanel.scale * 3;
        for (int i = 0; i < animationManagers.size();i++){
            BufferedImage frame = animationManagers.get(i).getCurrentFrame();
            g.drawImage(frame, (int) positions.get(i)[0], (int) positions.get(i)[1], (int) (frame.getWidth() * scale), (int) (frame.getHeight() * scale),null);
        }
    }

    @Override
    public void update(double dt) {
        uiX = (DrawingPanel.screenWidth - 250.0 * DrawingPanel.scale) / 2.0;
        uiY = (DrawingPanel.screenHeight - 150.0 * DrawingPanel.scale) / 2.0;
        if (animationManagers.size() == 0){
            for (int i = 0; i < 6;i++){
                double rand = Math.random();
                animationManagers.add(new AnimationManager(rand < 0.5? Animations.FLOATING_LEFT:Animations.FLOATING_RIGHT,5));
                positions.add(new double[]{(DrawingPanel.screenWidth - 6 * DrawingPanel.scale * 3) * i / 5.0,-DrawingPanel.screenHeight * Math.random()});
            }
        }
        for (int i = 0; i < animationManagers.size();i++){
            animationManagers.get(i).update(dt);
            if (positions.get(i)[1] > DrawingPanel.screenHeight){
                positions.get(i)[1] = -DrawingPanel.screenHeight * Math.random() - 10 * DrawingPanel.scale * 3;
            } else {
                positions.get(i)[1] += 15 * DrawingPanel.scale * 3 * dt;
            }
        }
        for (AnimationManager animationManager: animationManagers){
            animationManager.update(dt);
        }
    }

    @Override
    public void keyPressed(int keyCode) {
        ui.keyPressed(keyCode);
    }

    @Override
    public void mouseClicked(int x, int y) {
        ui.mouseClicked(x,y, (int) uiX, (int) uiY,DrawingPanel.scale);
    }

    @Override
    public void mouseMoved(int x, int y) {
        mouseCoords = new int[]{x,y};
        ui.mouseMoved(x,y, (int) uiX, (int) uiY,DrawingPanel.scale);
    }
}
