package lemmings.audio;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public final class SoundEffects {

    public final static Audio LETS_GO = new Audio("assets/sfx/LETSGO.wav");
    public final static Audio YIPPEE = new Audio("assets/sfx/YIPPEE.WAV");
    public final static Audio DEATH = new Audio("assets/sfx/DIE.wav");

}
