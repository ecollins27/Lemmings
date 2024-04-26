package lemmings.ui;

import lemmings.DrawingPanel;
import lemmings.screens.LoadingScreen;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TitleUI extends GenericUI<DrawingPanel> {
    public TitleUI(DrawingPanel object) {
        super(object, 250, 150);
        Font berlinSans = new Font("Berlin Sans FB",Font.PLAIN,12);
        addElement(new UIDisplayText(new Rectangle(0,0,250,50),"Lemmings",new Font("Berlin Sans FB",Font.ITALIC,12)));
        addElement(new UIButton<>(new Rectangle(90, 60, 70, 20), "Start", berlinSans, new UIAction<DrawingPanel>() {
            @Override
            public void perform(DrawingPanel object) {
                object.screen = new LoadingScreen(1);
            }
        }));
        addElement(new UITextBox(new Rectangle(75, 90, 100, 20), DrawingPanel.codes, new UITextBoxAction() {
            @Override
            public void perform(DrawingPanel drawingPanel, int index) {
                drawingPanel.screen = new LoadingScreen(index + 1);
            }
        }));
        addElement(new UIButton<>(new Rectangle(90, 120, 70, 20), "Quit", berlinSans, new UIAction<DrawingPanel>() {
            @Override
            public void perform(DrawingPanel object) {
                System.exit(0);
            }
        }));
    }

    @Override
    public BufferedImage getFrame(){
        BufferedImage frame = new BufferedImage(width,height,BufferedImage.TRANSLUCENT);
        Graphics2D g = frame.createGraphics();
        for (UIElement<DrawingPanel> element: elements){
            BufferedImage elementFrame = element.getFrame();
            g.drawImage(elementFrame,element.x,element.y,null);
        }
        return frame;
    }
}
