package lemmings.screens;

import lemmings.DrawingPanel;
import lemmings.ui.PauseUI;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PauseScreen extends Screen {

    PauseUI ui;
    double uiX,uiY;
    int[] mouseCoords = new int[2];

    public PauseScreen(DrawingPanel drawingPanel){
        ui = new PauseUI(drawingPanel);
        uiX = (DrawingPanel.screenWidth - 130.0 * DrawingPanel.scale) / 2.0;
        uiY = (DrawingPanel.screenHeight - 150.0 * DrawingPanel.scale) / 2.0;
    }


    @Override
    public BufferedImage getFrame(int width, int height) {
        BufferedImage bufferedImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bufferedImage.createGraphics();
        BufferedImage uiFrame = ui.getFrame();
        g.drawImage(uiFrame,(int)uiX,(int)uiY,(int)(uiFrame.getWidth() * DrawingPanel.scale), (int)(uiFrame.getHeight() * DrawingPanel.scale),null);
        g.drawImage(mouseOff,mouseCoords[0] - width/50,mouseCoords[1] - width/50,width/25,width/25,null);
        return bufferedImage;
    }

    @Override
    public void update(double dt) {
        uiX = (DrawingPanel.screenWidth - 130.0 * DrawingPanel.scale) / 2.0;
        uiY = (DrawingPanel.screenHeight - 150.0 * DrawingPanel.scale) / 2.0;
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
