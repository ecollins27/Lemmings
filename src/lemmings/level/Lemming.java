package lemmings.level;

import lemmings.DrawingPanel;
import lemmings.animation.Animation;
import lemmings.animation.AnimationManager;
import lemmings.animation.Animations;
import lemmings.audio.SoundEffects;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Lemming {

    public double[] pos;
    private int direction;
    private Rectangle2D down;
    private Rectangle2D center;
    private Rectangle2D left;
    private Rectangle2D right;
    private boolean kill = false;
    private boolean completed = false;
    private double[] velocity;
    private AnimationManager animationManager;
    private Job job = Job.WALKER;
    private double lastY;
    private final static double G = 50;
    private final static double TERMINAL_VEL = 100;
    private final static double FLOATING_TERMINAL_VEL = 25;
    private final static boolean DRAW_HITBOXES = false;
    private final static double WALKING_SPEED = 20;
    private final static double MAX_FALL_DISTANCE = 60;
    public Lemming(double x, double y){
        pos = new double[]{x,y};
        lastY = y;
        animationManager = new AnimationManager(Animations.WALKING_RIGHT,10);
        direction = 1;
        velocity = new double[]{0,0};
        updateHitboxes();
    }

    private void updateHitboxes(){
//        Rectangle frameHitbox = animationManager.getCurrentHitbox();
        down = getDownHitbox(pos);
        left = new Rectangle2D.Double((pos[0] - 3), pos[1] - 10, -direction, 7);
        right = new Rectangle2D.Double((pos[0] + 2), pos[1] - 10, direction, 7);
        center = getCenterHitbox(pos);
    }

    private Rectangle2D getDownHitbox(double[] pos){
        return new Rectangle2D.Double((pos[0] - 1), pos[1], 2,1);
    }

    private Rectangle2D getCenterHitbox(double[] pos){
        return new Rectangle2D.Double(pos[0] - 2,pos[1] - 10,4,10);
    }


    public boolean isTouchingLeft(Terrain terrain){
        return terrain.intersects(left,false,true,true,true);
    }
    public boolean isTouchingRight(Terrain terrain){
        return terrain.intersects(right,true,false,true,true);
    }

    public void draw(Graphics g){
        BufferedImage frame = animationManager.getCurrentFrame();
        g.drawImage(frame,(int)(pos[0] - frame.getWidth()/2),(int)(pos[1] - frame.getHeight()),null);
    }

    private boolean isFloating(){
        return animationManager.getCurrentAnimation() == Animations.FLOATING_RIGHT || animationManager.getCurrentAnimation() == Animations.FLOATING_LEFT;
    }

    private boolean isDigging(){
        Animation currentAnimation = animationManager.getCurrentAnimation();
        return currentAnimation == Animations.DIGGING_RIGHT || currentAnimation == Animations.DIGGING_LEFT;
    }

    public boolean update(Terrain terrain, Theme theme, int[] exitLocation, double dt){
        updateHitboxes();
        Animation result = animationManager.update(dt);
        Animation currentAnimation = animationManager.getCurrentAnimation();
        boolean shouldUpdateTerrain = false;
        int currentIndex = animationManager.getCurrentIndex();
        if (result == Animations.DEATH || result == Animations.EXPLOSION){
            kill();
            return false;
        } else if (result == Animations.LEMMING_EXIT){
            completed = true;
            return false;
        } else if (currentAnimation == Animations.DEATH || currentAnimation == Animations.EXPLOSION || kill){
            return false;
        } else if (currentAnimation == Animations.BLOCKING){
            if (job != Job.BLOCKER){
                terrain.subtractInvisible(new Rectangle2D.Double(pos[0] - 5,pos[1] - 10,10,10));
                startWalking();
            } else {
                terrain.addInvisible(new Rectangle2D.Double(pos[0] - 5, pos[1] - 10, 10, 10));
                return false;
            }
        } else if (animationManager.isEmpty() && (result == Animations.SHRUG_RIGHT || result == Animations.SHRUG_LEFT)){
            startWalking();
        } else if (currentAnimation == Animations.SHRUG_RIGHT || currentAnimation == Animations.SHRUG_LEFT){
            if (result == Animations.PLACING_STAIR_RIGHT || result == Animations.PLACING_STAIR_LEFT){
                job = Job.WALKER;
            }
            return false;
        } else if (result == Animations.OPENING_UMBRELLA_LEFT || result == Animations.OPENING_UMBRELLA_RIGHT){
            job = Job.WALKER;
        } else if (currentAnimation == Animations.DIGGING_RIGHT && currentIndex % 16 == 0){
            Rectangle areaRemoved = new Rectangle((int) (pos[0] - 4), (int) (pos[1] - 15),10,15);
            boolean shouldStop = !terrain.intersects(areaRemoved,false,false,false,false);
            terrain.subtract(new Area(areaRemoved));
            if (shouldStop){
                job = Job.WALKER;
                startWalking();
            } else {
                pos[0] += 2;
                return true;
            }
        } else if (currentAnimation == Animations.DIGGING_LEFT && currentIndex % 16 == 0){
            Rectangle areaRemoved = new Rectangle((int) (pos[0] - 6), (int) (pos[1] - 15),10,15);
            boolean shouldStop = !terrain.intersects(areaRemoved,false,false,false,false);
            terrain.subtract(new Area(areaRemoved));
            if (shouldStop){
                startWalking();
                job = Job.WALKER;
            } else {
                pos[0] -= 2;
                return true;
            }
        } else if (currentAnimation == Animations.DIGGING_LEFT || currentAnimation == Animations.DIGGING_RIGHT){
            return false;
        } else if (currentAnimation == Animations.DIGGING_DOWN1 || currentAnimation == Animations.DIGGING_DOWN2){
            if (result == Animations.DIGGING_DOWN1 || result == Animations.DIGGING_DOWN2) {
                Rectangle areaRemoved = new Rectangle((int) pos[0] - 5, (int) (pos[1]), 10, 1);
                boolean shouldStop = !terrain.intersects(areaRemoved, false, false,false,false);
                terrain.subtract(areaRemoved);
                if (shouldStop) {
                    job = Job.WALKER;
                    startWalking();
                } else {
                    pos[1] += 1;
                    lastY = pos[1];
                    if (result == Animations.DIGGING_DOWN2){
                        animationManager.add(Animations.DIGGING_DOWN1,20,1);
                        animationManager.add(Animations.DIGGING_DOWN2,20,1);
                    }
                    return true;
                }
            }
            return false;
        } else if (currentAnimation == Animations.LEMMING_EXIT){
            pos[1] -= 0.4 * WALKING_SPEED * dt;
            lastY = pos[1];
            return false;
        } else if (currentAnimation == Animations.PLACING_STAIR_RIGHT){
            if (job != Job.BUILDER){
                animationManager.clear();
                startWalking();
            } else if (result == Animations.PLACING_STAIR_RIGHT) {
                Rectangle2D stair = new Rectangle2D.Double(pos[0], pos[1] - 1, 4, 1);
                boolean shouldStop = terrain.intersects(stair,false,false,true,false);
                terrain.addStair(stair,true);
                pos[0] += 2;
                pos[1]--;
                lastY = pos[1];
                if (shouldStop){
                    pos[0] -= 2;
                    animationManager.clear();
                    job = Job.WALKER;
                    startWalking();
                }
                return true;
            }
            return false;
        } else if (currentAnimation == Animations.PLACING_STAIR_LEFT){
            if (job != Job.BUILDER){
                animationManager.clear();
                startWalking();
            } else if (result == Animations.PLACING_STAIR_LEFT) {
                Rectangle2D stair = new Rectangle2D.Double(pos[0] - 4, pos[1] - 1, 4, 1);
                boolean shouldStop = terrain.intersects(stair,false,false,true,false);
                terrain.addStair(stair,false);
                pos[0] -= 2;
                pos[1]--;
                lastY = pos[1];
                if (shouldStop){
                    pos[0] += 2;
                    animationManager.clear();
                    job = Job.WALKER;
                    startWalking();
                }
                return true;
            }
            return false;
        }

        if (Math.abs(pos[0] - exitLocation[0]) <= 1 && Math.abs(pos[1] - exitLocation[1]) <= 12){
            animationManager.clear();
            animationManager.add(direction == 1? Animations.SHRUG_RIGHT:Animations.SHRUG_LEFT,10,1);
            animationManager.add(Animations.LEMMING_EXIT,10,1);
        }

        boolean centerCollided = terrain.intersects(center,direction == 1,direction == -1,true,false);
        boolean downCollided = terrain.intersects(down,true,true,true,false);
        if (centerCollided){
            velocity[1] = 0;
            pos[1] = Math.floor(pos[1]);
            Rectangle2D newHitbox = getCenterHitbox(pos);
            while (terrain.intersects(newHitbox,direction == 1,direction == -1,true,false)){
                pos[1]--;
                newHitbox = getCenterHitbox(pos);
            }
            if (terrain.intersectsUnmineable(center) && theme == Theme.LAVA){
                animationManager.clear();
                animationManager.setDefaultAnimation(Animations.EXPLOSION,30);
                DrawingPanel.audioPlayer.playSoundEffect(SoundEffects.DEATH);
                velocity = new double[]{0,0};
                lastY = pos[1];
                return false;
            } else if (pos[1] - lastY >= MAX_FALL_DISTANCE){
                animationManager.clear();
                animationManager.setDefaultAnimation(Animations.DEATH,30);
                DrawingPanel.audioPlayer.playSoundEffect(SoundEffects.DEATH);
                lastY = pos[1];
                return false;
            } else {
                startWalking();
            }
            updateHitboxes();
        } else if (!downCollided){
            Rectangle2D updateHitbox = getDownHitbox(new double[]{pos[0],pos[1] + 1});
            if (terrain.intersects(updateHitbox,true,true,true,false)){
                if (pos[1] - lastY >= MAX_FALL_DISTANCE){
                    animationManager.clear();
                    animationManager.setDefaultAnimation(Animations.DEATH,30);
                    DrawingPanel.audioPlayer.playSoundEffect(SoundEffects.DEATH);
                    velocity = new double[]{0,0};
                    pos[1]++;
                    lastY = pos[1];
                    return false;
                } else {
                    pos[1]++;
                }
                lastY = pos[1];
            } else {
                velocity[0] = 0;
                velocity[1] = Math.min(isFloating()? FLOATING_TERMINAL_VEL:TERMINAL_VEL,velocity[1] + G * dt);
                if (job == Job.FLOATER && animationManager.getDefaultAnimation() != Animations.FLOATING_LEFT && animationManager.getDefaultAnimation() != Animations.FLOATING_RIGHT){
                    animationManager.setDefaultAnimation(direction == 1? Animations.FLOATING_RIGHT:Animations.FLOATING_LEFT,10);
                    animationManager.clear();
                    animationManager.add(Animations.FALLING,10,1);
                    animationManager.add(direction == 1? Animations.OPENING_UMBRELLA_RIGHT:Animations.OPENING_UMBRELLA_LEFT,10,1);
                } else if (job != Job.FLOATER && !isFloating() && animationManager.getDefaultAnimation() != Animations.FALLING){
                    animationManager.setDefaultAnimation(Animations.FALLING,10);
                }
            }
        } if (isTouchingRight(terrain)){
            if (job == Job.TUNNELER && !terrain.intersectsUnmineable(right) && !terrain.intersectsInvisible(right)){
                animationManager.clear();
                animationManager.clear();
                animationManager.setDefaultAnimation(Animations.DIGGING_RIGHT,30);
            } else {
                direction *= -1;
                startWalking();
            }
        } if (isTouchingLeft(terrain)){
            if (job == Job.TUNNELER && !terrain.intersectsUnmineable(left) && !terrain.intersectsInvisible(left)){
                animationManager.clear();
                animationManager.setDefaultAnimation(Animations.DIGGING_LEFT,30);
            } else {
                direction *= -1;
                startWalking();
            }
        } if (downCollided && !centerCollided){
            if (pos[1] - lastY >= MAX_FALL_DISTANCE){
                animationManager.clear();
                animationManager.setDefaultAnimation(Animations.DEATH,30);
                DrawingPanel.audioPlayer.playSoundEffect(SoundEffects.DEATH);
                lastY = pos[1];
                return false;
            } else if (terrain.intersectsUnmineable(down) && theme == Theme.LAVA){
                animationManager.clear();
                animationManager.setDefaultAnimation(Animations.EXPLOSION,30);
                DrawingPanel.audioPlayer.playSoundEffect(SoundEffects.DEATH);
                velocity = new double[]{0,0};
                lastY = pos[1];
                return false;
            }
            lastY = pos[1];
            if (job == Job.BUILDER && currentAnimation != Animations.PLACING_STAIR_RIGHT && currentAnimation != Animations.PLACING_STAIR_LEFT){
                startBuilding();
            } else if (job == Job.DIGGER && animationManager.getDefaultAnimation() != Animations.DIGGING_DOWN1 && !terrain.intersectsUnmineable(down)){
                startDigging();
            } else if (job == Job.BLOCKER){
                animationManager.setDefaultAnimation(Animations.BLOCKING,10);
            }
        }
        pos[0] += velocity[0] * dt;
        pos[1] += velocity[1] * dt;
        if (isFloating()){
            lastY = pos[1];
        }
        return shouldUpdateTerrain;
    }

    private void startDigging() {
        pos = new double[]{Math.floor(pos[0]),Math.ceil(pos[1])};
        velocity = new double[]{0,0};
        animationManager.clear();
        animationManager.setDefaultAnimation(Animations.DIGGING_DOWN1,20);
        animationManager.add(Animations.DIGGING_DOWN1,20,1);
        animationManager.add(Animations.DIGGING_DOWN2,20,1);
    }

    public void startBuilding(){
        pos = new double[]{Math.floor(pos[0]),Math.ceil(pos[1])};
        velocity = new double[]{0,0};
        animationManager.setDefaultAnimation(direction == 1? Animations.PLACING_STAIR_RIGHT:Animations.PLACING_STAIR_LEFT,10);
        animationManager.setCurrentIndex(0);
    }
    private void startWalking(){
        velocity = new double[]{direction * WALKING_SPEED,0};
        animationManager.setDefaultAnimation(direction == 1? Animations.WALKING_RIGHT:Animations.WALKING_LEFT,10);
    }
    public void kill(){
        kill = true;
    }

    public boolean isDead(){
        return kill;
    }

    public void setJob(Job job){
        this.job = job;
        if (job == Job.BUILDER){
            pos[0] = Math.floor(pos[0]);
        }
    }

    public Job getJob() {
        return job;
    }

    public Rectangle2D getCurrentHitbox() {
        return animationManager.getCurrentHitbox();
    }

    public boolean hasCompleted() {
        return completed;
    }
}
