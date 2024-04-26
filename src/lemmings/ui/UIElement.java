package lemmings.ui;

import java.awt.image.BufferedImage;

public abstract class UIElement<T> {
    int x,y;
    public UIElement(int x, int y){
        this.x = x;
        this.y = y;
    }


    public abstract BufferedImage getFrame();
    public abstract void mouseClicked(T object, int x, int y);
    public abstract void mouseMoved(T object, int x, int y);
    public abstract void keyPressed(T object, int keyCode);
}
