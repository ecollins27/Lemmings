package lemmings.screens;

import lemmings.animation.Animation;

import java.awt.image.BufferedImage;

public abstract class Screen {

    static BufferedImage mouseOff;
    static BufferedImage mouseOn;
    static {
        mouseOn = Animation.animationSheet.getSubimage(0,280,15,15);
        mouseOff = Animation.animationSheet.getSubimage(20,280,15,15);
    }

    public abstract BufferedImage getFrame(int width, int height);
    public abstract void update(double dt);
    public abstract void keyPressed(int keyCode);
    public abstract void mouseClicked(int x, int y);
    public abstract void mouseMoved(int x, int y);
}
