package lemmings.animation;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Queue;

public class AnimationManager {

    Queue<AnimationData> queue;
    int index = 0;
    AnimationData defaultAnimationData;
    private double frameDelay;
    private double lastFrameChange;

    BufferedImage lastFrame;
    Rectangle lastHitbox;

    public AnimationManager(Animation defaultAnimation, int fps){
        defaultAnimationData = new AnimationData(defaultAnimation,fps,-1);
        queue = new LinkedList<>();
        frameDelay = 1.0 / getCurrentAnimationData().fps;
        lastFrameChange = 0;
    }

    public AnimationManager(Animation firstAnimation, int fps, int numCycles){
        defaultAnimationData = null;
        queue = new LinkedList<>();
        queue.add(new AnimationData(firstAnimation,fps,numCycles));
        frameDelay = 1.0 / fps;
        lastFrameChange = 0;
    }

    public BufferedImage getCurrentFrame(){
        AnimationData currentData = getCurrentAnimationData();
        if (currentData == null){
            return lastFrame;
        }
        return currentData.animation.getFrame(index % currentData.animation.length());
    }

    public Rectangle getCurrentHitbox(){
        AnimationData currentData = getCurrentAnimationData();
        if (currentData == null){
            return lastHitbox;
        }
        return currentData.animation.getHitbox(index % currentData.animation.length());
    }

    public Animation update(double dt){
        Animation result = null;
        lastFrameChange += dt;
        int numIterations = (int)(lastFrameChange / frameDelay);
        if (numIterations > 0){
            lastFrameChange = lastFrameChange % frameDelay;
        }
        for (int i = 0; i < numIterations;i++){
            Animation iterationResult = iterate();
            if (iterationResult != null){
                result = iterationResult;
            }
        }
        return result;
    }

    public void add(Animation animation, int fps, int numCycles){
        queue.add(new AnimationData(animation,fps,numCycles));
    }

    public Animation iterate(){
        if (lastFrame != null){
            return null;
        }
        index++;
        AnimationData currentData = getCurrentAnimationData();
        if (index / currentData.animation.length() == currentData.numCycles){
            AnimationData polledData = poll();
            index = 0;
            if (lastFrame != null){
                return null;
            }
            currentData = getCurrentAnimationData();
            frameDelay = 1.0 / currentData.fps;
            return polledData.animation;
        }
        return index % currentData.animation.length() == 0? currentData.animation:null;
    }

    public AnimationData poll(){
        if (queue.size() == 1 && defaultAnimationData == null){
            lastFrame = getCurrentAnimation().getFrame(getCurrentAnimation().length() - 1);
            lastHitbox = getCurrentAnimation().getHitbox(getCurrentAnimation().length() - 1);
        }
        return queue.poll();
    }

    public void setDefaultAnimation(Animation defaultAnimation, int fps){
        if (getDefaultAnimation() == Animations.DEATH){
            return;
        }
        if (defaultAnimation == defaultAnimationData.animation){
            return;
        }
        index = index % defaultAnimation.length();
        defaultAnimationData = new AnimationData(defaultAnimation,fps,-1);
        frameDelay = 1.0 / getCurrentAnimationData().fps;
    }

    public int getCurrentIndex(){
        return index % getDefaultAnimation().length();
    }

    public Animation getDefaultAnimation(){
        return defaultAnimationData.animation;
    }

    public int queueSize(){
        return queue.size();
    }

    public void clear(){
        while (queueSize() != 0){
            poll();
        }
        index = 0;
    }

    private AnimationData getCurrentAnimationData(){
        if (queue.isEmpty()){
            return defaultAnimationData;
        }
        return queue.peek();
    }

    public Animation getCurrentAnimation(){
        AnimationData currentData = getCurrentAnimationData();
        if (currentData == null){
            return null;
        }
        return currentData.animation;
    }

    public void setCurrentIndex(int i) {
        index = i;
    }

    public boolean isEmpty(){
        return queue.isEmpty();
    }

    private record AnimationData(Animation animation, int fps, int numCycles){}
}
