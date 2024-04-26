package lemmings.ui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GenericUI<T> {

    ArrayList<UIElement<T>> elements;
    int width,height;
    T object;

    public GenericUI(T object, int width, int height){
        this.object = object;
        this.width = width;
        this.height = height;
        elements = new ArrayList<>();
    }

    public BufferedImage getFrame(){
        BufferedImage frame = new BufferedImage(width,height,BufferedImage.TRANSLUCENT);
        Graphics2D g = frame.createGraphics();
        for (UIElement<T> element: elements){
            BufferedImage elementFrame = element.getFrame();
            g.drawImage(elementFrame,element.x,element.y,null);
        }
        return frame;
    }

    public void update(T object, double dt){

    }

    public void addElement(UIElement<T> element){
        elements.add(element);
    }

    public final void mouseClicked(int x, int y, int uiX, int uiY, double scale){
        x = (int) ((x - uiX)/scale);
        y = (int) ((y - uiY)/scale);
        for (UIElement<T> element: elements){
            element.mouseClicked(object,x,y);
        }
    }

    public final void mouseMoved(int x, int y, int uiX, int uiY, double scale){
        x = (int) ((x - uiX)/scale);
        y = (int) ((y - uiY)/scale);
        for (UIElement<T> element: elements){
            element.mouseMoved(object,x,y);
        }
    }

    public void keyPressed(int keyCode){
        for (UIElement<T> element: elements){
            element.keyPressed(object,keyCode);
        }
    }
}
