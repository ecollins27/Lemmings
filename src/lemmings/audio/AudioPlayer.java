package lemmings.audio;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.random.RandomGenerator;

public class AudioPlayer {

    Clip musicClip;
    Queue<Audio> musicQueue;
    Audio[] musicSelection;
    Set<Clip> sfxClips;

    public AudioPlayer(Audio[] musicSelection){
        this.musicSelection = musicSelection;
        musicQueue = new LinkedList<>();
        sfxClips = new HashSet<>();
        try {
            musicClip = AudioSystem.getClip();
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
        queueMusic();
    }

    public void playSoundEffect(Audio sfx){
        sfx.reset();
        for (Clip clip: sfxClips){
            if (!clip.isActive()){
                clip.close();
                sfx.open(clip);
                clip.setMicrosecondPosition(0);
                clip.start();
                return;
            }
        }
        try {
            Clip clip = AudioSystem.getClip();
            sfx.open(clip);
            clip.start();
            sfxClips.add(clip);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    private void queueMusic(){
        for (int i = 0; i < musicSelection.length * musicSelection.length;i++){
            int index1 = (int)(musicSelection.length * Math.random());
            int index2 = (int)(musicSelection.length * Math.random());
            Audio temp = musicSelection[index1];
            musicSelection[index1] = musicSelection[index2];
            musicSelection[index2] = temp;
        }
        for (int i = 0; i < musicSelection.length;i++){
            musicQueue.add(musicSelection[i]);
        }
        Audio nextSong = musicQueue.poll();
        nextSong.reset();
        nextSong.open(musicClip);
        musicClip.start();
    }

    public void setMusicSelection(Audio[] musicSelection){
        this.musicSelection = musicSelection;
        musicQueue.clear();
    }

    public void update() {
        if (!musicClip.isActive()){
            musicClip.close();
            if (!musicQueue.isEmpty()){
                Audio nextSong = musicQueue.poll();
                nextSong.reset();
                nextSong.open(musicClip);
                musicClip.start();
            } else {
                queueMusic();
            }
        }
    }

    public Set<Audio> getMusicSelection(){
        return new HashSet<>(Arrays.asList(musicSelection));
    }
}
