package lemmings.ui;

import lemmings.DrawingPanel;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class UITextBox extends UIElement<DrawingPanel>{

    boolean hasFocus = false;
    Rectangle2D hitbox;
    String text;
    String[] codes;
    BufferedImage frame;
    UITextBoxAction action;

    public UITextBox(Rectangle2D hitbox, String[] codes, UITextBoxAction action){
        super((int)hitbox.getX(),(int)hitbox.getY());
        this.hitbox = hitbox;
        this.codes = codes;
        this.action = action;
        text = "";
        setText(text,new Font("Berlin Sans FB",Font.PLAIN,12));
    }

    private void setText(String text, Font font){
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
        frame = new BufferedImage((int) hitbox.getWidth(), (int) hitbox.getHeight(),BufferedImage.TRANSLUCENT);
        Graphics2D g = frame.createGraphics();
        g.setColor(hasFocus? Color.YELLOW:Color.WHITE);
        g.setFont(new Font(font.getFontName(), font.getStyle(), size));
        g.drawString(text, (int) ((frame.getWidth() - bounds.getWidth()) / 2), (int) (frame.getHeight() - (frame.getHeight() - fontMetrics.getAscent())/2 - (bounds.getHeight() - fontMetrics.getAscent())));
        g.drawRect(0,0,frame.getWidth() - 1, frame.getHeight() - 1);
    }

    @Override
    public BufferedImage getFrame() {
        return frame;
    }

    @Override
    public void mouseClicked(DrawingPanel object, int x, int y) {
        hasFocus = hitbox.contains(x,y);
        setText(text,new Font("Berlin Sans FB",Font.PLAIN,12));
    }

    @Override
    public void mouseMoved(DrawingPanel object, int x, int y) {

    }

    @Override
    public void keyPressed(DrawingPanel object, int keyCode) {
        if (hasFocus){
            String key = KeyEvent.getKeyText(keyCode);
            if (keyCode == KeyEvent.VK_ENTER){
                for (int i = 0; i < codes.length;i++){
                    if (text.equals(codes[i].toUpperCase())){
                        action.perform(object,i);
                        text = "";
                        setText(text,new Font("Berlin Sans FB",Font.PLAIN,12));
                        return;
                    }
                }
                text = "";
                setText(text,new Font("Berlin Sans FB",Font.PLAIN,12));
            } else if (keyCode == KeyEvent.VK_BACK_SPACE){
                if (text.length() > 0) {
                    text = text.substring(0, text.length() - 1);
                }
            } else if (key.length() == 1 && (Character.isAlphabetic(key.charAt(0)) || Character.isDigit(key.charAt(0)))){
                text += key.toUpperCase();
            }
            setText(text,new Font("Berlin Sans FB",Font.PLAIN,12));
        }
    }
}
