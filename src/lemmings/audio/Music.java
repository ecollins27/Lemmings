package lemmings.audio;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public final class Music {

    public final static Audio CAN_CAN = new Audio("assets/music/can-can.wav");
    public final static Audio LITTLE_SWANS = new Audio("assets/music/dance of the little swans.wav");
    public final static Audio REED_FLUTES = new Audio("assets/music/dance of the reed flutes.wav");
    public final static Audio FOREST_GREEN = new Audio("assets/music/forest green.wav");
    public final static Audio LEMMING1 = new Audio("assets/music/lemming-1.wav");
    public final static Audio LEMMING2 = new Audio("assets/music/lemming-2.wav");
    public final static Audio LEMMING3 = new Audio("assets/music/lemming-3.wav");
    public final static Audio TURKISH_RONDO = new Audio("assets/music/turkish rondo.wav");
    public final static Audio[] DEFAULT_MUSIC_SELECTION = new Audio[]{CAN_CAN,LITTLE_SWANS,REED_FLUTES,FOREST_GREEN,LEMMING1,LEMMING2,LEMMING3,TURKISH_RONDO};
}
