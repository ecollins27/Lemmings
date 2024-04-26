package lemmings.ui;

import lemmings.DrawingPanel;
import lemmings.level.Level;
import lemmings.screens.LevelScreen;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TutorialUI extends GenericUI<Level> {
    public TutorialUI(Level object, String tutorialText) {
        super(object, 0,0);
        int width = (int) (3 * DrawingPanel.screenWidth / (4 * DrawingPanel.scale));
        int height = (int) (7 * DrawingPanel.screenHeight / (8 * DrawingPanel.scale)) - 50;
        this.width = width;
        this.height = height;
        Font berlinSans = new Font("Berlin Sans FB", Font.PLAIN, 12);
        addElement(new UIDisplayText(new Rectangle(width / 4, height / 16, width / 2, height / 6), "Level " + object.levelNum, berlinSans));
        if (tutorialText != null) {
            String[] split = tutorialText.split("   ");
            for (int i = 0; i < split.length; i++) {
                addElement(new UIDisplayText(new Rectangle(width / 8, height / 4 + i * height / 6, width * 3 / 4, height / 8), split[i], berlinSans));
            }
        }
        addElement(new UIDisplayText(new Rectangle(width / 4, height - height / 10, width / 2, height / 10), "Press space to start", berlinSans));
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
