package lemmings.ui;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class UIDisplayText extends UIElement {

    BufferedImage imageText;
    Rectangle2D hitbox;
    Font font;

    public UIDisplayText(Rectangle2D hitbox, String text){
        this(hitbox,text,new Font("plain",Font.PLAIN,12));
    }

    public UIDisplayText(Rectangle2D hitbox, String text, Font font){
        super((int)hitbox.getX(),(int)hitbox.getY());
        this.hitbox = hitbox;
        this.font = font;
        setText(text);
    }

    public void setText(String text){
        Canvas c = new Canvas();
        int size = 12;
        FontMetrics fontMetrics = c.getFontMetrics(new Font(font.getFontName(), font.getStyle(), size));
        Rectangle2D bounds = fontMetrics.getStringBounds(text,c.getGraphics());
        while (bounds.getWidth() > hitbox.getWidth() && bounds.getHeight() > hitbox.getHeight()){
            size--;
            fontMetrics = c.getFontMetrics(new Font(font.getFontName(), font.getStyle(), size));
            bounds = fontMetrics.getStringBounds(text,c.getGraphics());
        }
        while (bounds.getWidth() < hitbox.getWidth() && bounds.getHeight() < hitbox.getHeight()){
            size++;
            fontMetrics = c.getFontMetrics(new Font(font.getFontName(), font.getStyle(), size));
            bounds = fontMetrics.getStringBounds(text,c.getGraphics());
        }
        size--;
        bounds = fontMetrics.getStringBounds(text,c.getGraphics());
        imageText = new BufferedImage((int) hitbox.getWidth(), (int) hitbox.getHeight(),BufferedImage.TRANSLUCENT);
        Graphics2D g = imageText.createGraphics();
        g.setColor(Color.WHITE);
        g.setFont(new Font(font.getFontName(), font.getStyle(), size));
        g.drawString(text, (int) ((imageText.getWidth() - bounds.getWidth()) / 2), (int) (imageText.getHeight() - (imageText.getHeight() - fontMetrics.getAscent())/2 - (bounds.getHeight() - fontMetrics.getAscent())));
    }

    @Override
    public BufferedImage getFrame() {
        return imageText;
    }

    @Override
    public void mouseClicked(Object object, int x, int y) {

    }

    @Override
    public void mouseMoved(Object object, int x, int y) {

    }

    @Override
    public void keyPressed(Object object, int keyCode) {

    }
}
