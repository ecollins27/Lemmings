package lemmings.ui;

import lemmings.DrawingPanel;
import lemmings.audio.Music;
import lemmings.screens.LoadingScreen;

import java.awt.*;
import java.util.Date;

public class PauseUI extends GenericUI<DrawingPanel> {
    public PauseUI(DrawingPanel object) {
        super(object, 130,150);
        Font berlinSans = new Font("Berlin Sans FB",Font.PLAIN,12);
        addElement(new UIButton<>(new Rectangle(20, 0, 90, 30), "Resume", berlinSans, new UIAction<DrawingPanel>() {
            @Override
            public void perform(DrawingPanel object) {
                object.screen = object.currentLevelScreen;
            }
        }));
        addElement(new UIButton<>(new Rectangle(30, 40, 70, 30), "Reset", berlinSans, new UIAction<DrawingPanel>() {
            @Override
            public void perform(DrawingPanel object) {
                object.screen = new LoadingScreen(object.currentLevel);
            }
        }));
        addElement(new UITextBox(new Rectangle(15, 80, 100, 30), DrawingPanel.codes, new UITextBoxAction() {
            @Override
            public void perform(DrawingPanel drawingPanel, int index) {
                drawingPanel.screen = new LoadingScreen(index + 1);
            }
        }));
        addElement(new UIButton<>(new Rectangle(0, 120, 130, 30), "Quit To Menu", berlinSans, new UIAction<DrawingPanel>() {
            @Override
            public void perform(DrawingPanel object) {
                object.screen = object.titleScreen;
//                if (DrawingPanel.audioPlayer.getMusicSelection().contains(Music.MARS)){
//                    DrawingPanel.audioPlayer.setMusicSelection(Music.DEFAULT_MUSIC_SELECTION);
//                }
            }
        }));
    }
}
