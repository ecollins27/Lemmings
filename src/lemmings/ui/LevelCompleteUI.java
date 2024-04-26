package lemmings.ui;

import lemmings.DrawingPanel;
import lemmings.level.Level;

import java.awt.*;
import java.awt.image.BufferedImage;

public class LevelCompleteUI extends GenericUI<Level> {
    public LevelCompleteUI(Level object) {
        super(object, 0,0);
        int width = (int) (3 * DrawingPanel.screenWidth / (4 * DrawingPanel.scale));
        int height = (int) (2 * DrawingPanel.screenHeight / (3 * DrawingPanel.scale)) - 50;
        this.width = width;
        this.height = height;
        Font berlinSans = new Font("Berlin Sans FB",Font.PLAIN,12);
        addElement(new UIDisplayText(new Rectangle(width/16,0,width * 7 / 8,height * 5 / 6),"Level Complete!",berlinSans));
        addElement(new UIDisplayText(new Rectangle(width/6,height - height/6,2 * width/3,height/6),"Press space to continue",berlinSans));
    }


    public BufferedImage getFrame(){
        BufferedImage frame = new BufferedImage(width,height,BufferedImage.TRANSLUCENT);
        Graphics2D g = frame.createGraphics();
        g.setColor(new Color(0,0,0,128));
        g.fillRect(0,0,width,height);
        for (UIElement element: elements){
            BufferedImage elementFrame = element.getFrame();
            g.drawImage(elementFrame,element.x,element.y,null);
        }
        return frame;
    }
}
