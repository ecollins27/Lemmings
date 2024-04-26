package lemmings.animation;

public final class Animations {

    public final static Animation WALKING_RIGHT = new Animation(0,0,20,10,8,16,true);
    public final static Animation WALKING_LEFT = WALKING_RIGHT.mirror();
    public final static Animation DIGGING_RIGHT = new Animation(0,117,20,20,32,16,true);
    public final static Animation DIGGING_LEFT = DIGGING_RIGHT.mirror();
    public final static Animation DIGGING_DOWN1 = new Animation(0,160,20,20,8,16,true);
    public final static Animation DIGGING_DOWN2 = DIGGING_DOWN1.mirror();
    public final static Animation FALLING = new Animation(0,40,20,10,4,16,true);
    public final static Animation DEATH = new Animation(0,220,20,20,15,16,true);
    public final static Animation SHRUG_RIGHT = new Animation(180,0,20,20,7,16,true);
    public final static Animation SHRUG_LEFT = SHRUG_RIGHT.mirror();
    public final static Animation LEMMING_EXIT = new Animation(0,18,20,20,9,16,true);
    public final static Animation OPENING_UMBRELLA_RIGHT = new Animation(77,40,20,15,4,16,true);
    public final static Animation OPENING_UMBRELLA_LEFT = OPENING_UMBRELLA_RIGHT.mirror();
    public final static Animation FLOATING_RIGHT = new Animation(157,40,20,15,4,16,true).merge(new Animation(237,40,-20,15,4,16,true));
    public final static Animation FLOATING_LEFT = FLOATING_RIGHT.mirror();
    public final static Animation ENTRANCE_OPENING_DEFAULT = new Animation(327,12,40,39,10,1,false);
    public final static Animation ENTRANCE_OPENING_LAVA = new Animation(387,12,40,39,10,1,false);
    public final static Animation EXPLOSION = new Animation(0,258,20,20,13,16,true);
    public final static Animation DEFAULT_EXIT = new Animation(317,405,40,40,6,3,true);
    public final static Animation LAVA_EXIT = new Animation(400,469,37,40,6,1,true);
    public final static Animation PLACING_STAIR_RIGHT = new Animation(0,100,20,13,16,16,false);
    public final static Animation PLACING_STAIR_LEFT = PLACING_STAIR_RIGHT.mirror();
    public final static Animation BLOCKING = new Animation(0,60,20,20,16,16,true);
}
