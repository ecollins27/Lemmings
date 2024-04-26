package lemmings.ui;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class UIToggle<T> extends UIElement<T>{

    Rectangle2D hitbox;
    BufferedImage[] icons;
    int selectedIndex = 0;
    int buttonWidth;

    public UIToggle(Rectangle2D hitbox, BufferedImage[] icons){
        super((int)hitbox.getX(),(int)hitbox.getY());
        this.hitbox = hitbox;
        buttonWidth = (int) (hitbox.getWidth() / icons.length);
        this.icons = icons;
    }

    @Override
    public BufferedImage getFrame() {
        BufferedImage frame = new BufferedImage((int) hitbox.getWidth(), (int) hitbox.getHeight(),BufferedImage.TRANSLUCENT);
        Graphics2D g = frame.createGraphics();
        for (int i = 0; i < icons.length;i++){
            BufferedImage icon = icons[i];
            g.drawImage(icon,(int)(i * buttonWidth + (buttonWidth - icon.getWidth())/2),(int)((hitbox.getHeight() - icon.getHeight())/2),null);
        }
        g.setColor(Color.YELLOW);
        g.drawRect(selectedIndex * buttonWidth,0,buttonWidth - 1, (int) hitbox.getHeight() - 1);
        return frame;
    }

    @Override
    public void mouseClicked(T object, int x, int y) {
        if (hitbox.contains(x,y)) {
            selectedIndex = (int) ((x - hitbox.getX()) / buttonWidth);
        }
    }

    @Override
    public void mouseMoved(T object, int x, int y) {

    }

    @Override
    public void keyPressed(T object, int keyCode) {

    }
}
