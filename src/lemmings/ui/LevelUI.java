package lemmings.ui;

import lemmings.DrawingPanel;
import lemmings.animation.Animations;
import lemmings.level.Level;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class LevelUI extends GenericUI<Level> {
    public LevelUI(Level object) {
        super(object, 330, 50);
        Font berlinSans = new Font("Berlin Sans FB",Font.PLAIN,12);
        BufferedImage[] icons = new BufferedImage[]{
                Animations.WALKING_RIGHT.getFrame(1),
                Animations.DIGGING_DOWN1.getFrame(0),
                Animations.DIGGING_RIGHT.getFrame(20),
                Animations.FLOATING_RIGHT.getFrame(2),
                Animations.BLOCKING.getFrame(0),
                Animations.PLACING_STAIR_RIGHT.getFrame(7)
        };
        addElement(new UIToggle<>(new Rectangle(0,20,180,30),icons));
        addElement(new UIDisplayText(new Rectangle(0,0,30,20),object.quantities[0] < 10? ("0" + object.quantities[0]):("" + object.quantities[0]),berlinSans));
        addElement(new UIDisplayText(new Rectangle(30,0,30,20),object.quantities[1] < 10? ("0" + object.quantities[1]):("" + object.quantities[1]),berlinSans));
        addElement(new UIDisplayText(new Rectangle(60,0,30,20),object.quantities[2] < 10? ("0" + object.quantities[2]):("" + object.quantities[2]),berlinSans));
        addElement(new UIDisplayText(new Rectangle(90,0,30,20),object.quantities[3] < 10? ("0" + object.quantities[3]):("" + object.quantities[3]),berlinSans));
        addElement(new UIDisplayText(new Rectangle(120,0,30,20),object.quantities[4] < 10? ("0" + object.quantities[4]):("" + object.quantities[4]),berlinSans));
        addElement(new UIDisplayText(new Rectangle(150,0,30,20),object.quantities[5] < 10? ("0" + object.quantities[5]):("" + object.quantities[5]),berlinSans));
        addElement(new UIButton<>(new Rectangle2D.Double(180,25,25,25),UIButton.leftArrowOff,UIButton.leftArrowOn,
        new UIAction<Level>() {
            @Override
            public void perform(Level object) {
                object.playSpeed = Math.max(1, object.playSpeed - 1);
            }
        }));
        addElement(new UIButton<>(new Rectangle2D.Double(205,25,25,25),UIButton.rightArrowOff,UIButton.rightArrowOn,
                new UIAction<Level>() {
                    @Override
                    public void perform(Level object) {
                        object.playSpeed = Math.min(5, object.playSpeed + 1);
                    }
        }));
        addElement(new UIDisplayText(new Rectangle(180,0,50,25),"x" + object.playSpeed,berlinSans));
        addElement(new UIDisplayText(new Rectangle(230,0,100,25),"Goal: " + (object.lemmingsRequired - object.lemmingsCompleted),berlinSans));
        addElement(new UIDisplayText(new Rectangle(230,25,100,25),"" + DrawingPanel.codes[object.levelNum - 1],berlinSans));
    }

    public int getSelectedIndex(){
        return ((UIToggle)elements.get(0)).selectedIndex;
    }

    @Override
    public BufferedImage getFrame(){
        BufferedImage frame = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        Graphics2D g = frame.createGraphics();
        for (UIElement<Level> element: elements){
            BufferedImage elementFrame = element.getFrame();
            g.drawImage(elementFrame,element.x,element.y,null);
        }
        return frame;
    }

    @Override
    public void update(Level object, double dt) {
        int counter = 0;
        for (UIElement<Level> element: elements){
            if (element instanceof UIDisplayText && counter < 6){
                ((UIDisplayText) element).setText(object.quantities[counter] < 10? ("0" + object.quantities[counter]):("" + object.quantities[counter]));
                counter++;
            } else if (element instanceof UIDisplayText && counter == 6){
                ((UIDisplayText) element).setText("x" + object.playSpeed);
                counter++;
            } else if (element instanceof UIDisplayText && counter == 7){
                ((UIDisplayText) element).setText("Goal: " + (object.lemmingsRequired - object.lemmingsCompleted));
                counter++;
            }
        }
    }

    @Override
    public void keyPressed(int keyCode) {
        super.keyPressed(keyCode);
        switch (keyCode){
            case KeyEvent.VK_1:
                ((UIToggle)elements.get(0)).selectedIndex = 0;
                break;
            case KeyEvent.VK_2:
                ((UIToggle)elements.get(0)).selectedIndex = 1;
                break;
            case KeyEvent.VK_3:
                ((UIToggle)elements.get(0)).selectedIndex = 2;
                break;
            case KeyEvent.VK_4:
                ((UIToggle)elements.get(0)).selectedIndex = 3;
                break;
            case KeyEvent.VK_5:
                ((UIToggle)elements.get(0)).selectedIndex = 4;
                break;
            case KeyEvent.VK_6:
                ((UIToggle)elements.get(0)).selectedIndex = 5;
                break;
        }
    }
}
