package lemmings.ui;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class UIButton<T> extends UIElement<T> {

    public static BufferedImage rightArrowOff,rightArrowOn,leftArrowOff,leftArrowOn;

    private Rectangle2D hitbox;
    UIAction<T> action;
    BufferedImage buttonOff,buttonOn;
    boolean on = false;

    static {
        try {
            rightArrowOff = ImageIO.read(new File("assets/right_arrow_off.png"));
            rightArrowOn = ImageIO.read(new File("assets/right_arrow_on.png"));
            leftArrowOff = ImageIO.read(new File("assets/left_arrow_off.png"));
            leftArrowOn = ImageIO.read(new File("assets/left_arrow_on.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public UIButton(Rectangle2D hitbox, BufferedImage buttonOff, BufferedImage buttonOn, UIAction<T> action){
        super((int)hitbox.getX(),(int)hitbox.getY());
        this.hitbox = hitbox;
        this.buttonOff = buttonOff;
        this.buttonOn = buttonOn;
        this.action = action;
    }

    public UIButton(Rectangle2D hitbox, String text, Font font, UIAction<T> action){
        super((int) hitbox.getX(), (int) hitbox.getY());
        this.hitbox = hitbox;
        this.action = action;
        setTextIcon(text,font);
    }

    private void setTextIcon(String text, Font font){
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
        buttonOff = new BufferedImage((int) hitbox.getWidth(), (int) hitbox.getHeight(),BufferedImage.TRANSLUCENT);
        Graphics2D g = buttonOff.createGraphics();
        g.setColor(Color.WHITE);
        g.setFont(new Font(font.getFontName(), font.getStyle(), size));
        g.drawString(text, (int) ((buttonOff.getWidth() - bounds.getWidth()) / 2), (int) (buttonOff.getHeight() - (buttonOff.getHeight() - fontMetrics.getAscent())/2 - (bounds.getHeight() - fontMetrics.getAscent())));

        buttonOn = new BufferedImage((int) hitbox.getWidth(), (int) hitbox.getHeight(),BufferedImage.TRANSLUCENT);
        g = buttonOn.createGraphics();
        g.setColor(Color.YELLOW);
        g.setFont(new Font(font.getFontName(), font.getStyle(), size));
        g.drawString(text, (int) ((buttonOn.getWidth() - bounds.getWidth()) / 2), (int) (buttonOn.getHeight() - (buttonOn.getHeight() - fontMetrics.getAscent())/2 - (bounds.getHeight() - fontMetrics.getAscent())));
    }

    @Override
    public BufferedImage getFrame() {
        return on? buttonOn:buttonOff;
    }

    @Override
    public void mouseClicked(T object, int x, int y) {
        if (contains(x,y)){
            action.perform(object);
        }
    }

    @Override
    public void mouseMoved(T object, int x, int y) {
        on = contains(x,y);
    }

    @Override
    public void keyPressed(T object, int keyCode) {

    }

    private boolean contains(int x, int y){
        return hitbox.contains(x,y);
    }
}
