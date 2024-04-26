package lemmings.audio;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Audio {
    String fileName;
    AudioInputStream audioInputStream;

    public Audio(String fileName){
        this.fileName = fileName;
        reset();
    }

    public void reset(){
        try {
            audioInputStream = AudioSystem.getAudioInputStream(new File(fileName));
        } catch (UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void open(Clip clip){
        try {
            clip.open(audioInputStream);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
