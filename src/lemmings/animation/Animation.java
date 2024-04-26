package lemmings.animation;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Animation {

    public static BufferedImage animationSheet;

    static {
        try {
            animationSheet = ImageIO.read(new File("assets/animations.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    int width,height;
    private BufferedImage[] frames;

    private Animation(BufferedImage[] frames,int width, int height){
        this.width = width;
        this.height = height;
        this.frames = frames;
    }
    public Animation(int x, int y, int width, int height, int num, int wrap, boolean trim){
        this.width = width;
        this.height = height;
        frames = new BufferedImage[num];
        for (int i = 0; i < num;i++){
            if (width < 0){
                frames[i] = animationSheet.getSubimage(x + width * (i % wrap) + width,y + height * (i / wrap),-width,height);
            } else {
                frames[i] = animationSheet.getSubimage(x + width * (i % wrap), y + height * (i / wrap), width, height);
            }
            if (trim) {
                int minX = Math.abs(width), maxX = 0;
                int minY = height, maxY = 0;
                for (int a = 0; a < Math.abs(width); a++) {
                    for (int b = 0; b < height; b++) {
                        if (frames[i].getRGB(a, b) != 0) {
                            minX = Math.min(minX, a);
                            maxX = Math.max(maxX, a);
                            minY = Math.min(minY, b);
                            maxY = Math.max(maxY, b);
                        }
                    }
                }
                frames[i] = frames[i].getSubimage(minX, minY, Math.min(Math.abs(width), maxX - minX + 1), Math.min(height, maxY - minY + 1));
            }
        }
    }

    public Animation merge(Animation animation){
        Animation output = new Animation(Arrays.copyOf(frames,frames.length + animation.frames.length),width,height);
        for (int i = frames.length;i < animation.frames.length + frames.length;i++){
            output.frames[i] = animation.frames[i - frames.length];
        }
        return output;
    }

    public int length(){
        return frames.length;
    }

    public BufferedImage getFrame(int i){
        return frames[i];
    }

    public Rectangle getHitbox(int i){
        BufferedImage frame = getFrame(i);
        return new Rectangle(0,0,frame.getWidth(),frame.getHeight());
    }

    public Animation mirror(){
        BufferedImage[] frames = new BufferedImage[length()];
        Rectangle[] hitboxes = new Rectangle[length()];
        for (int i = 0; i < length();i++){
            frames[i] = new BufferedImage(this.frames[i].getWidth(),this.frames[i].getHeight(),BufferedImage.TRANSLUCENT);
            for (int a = 0; a < this.frames[i].getWidth();a++){
                for (int b = 0; b < this.frames[i].getHeight();b++){
                    frames[i].setRGB(this.frames[i].getWidth() - a - 1,b,this.frames[i].getRGB(a,b));
                }
            }
        }
        return new Animation(frames,width,height);
    }
}
