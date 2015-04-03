package com.twonamegames.colorcraze.model.components;

public class Speed {


    public static final int DIRECTION_DOWN	= 1;


    private float yv = 1;	// velocity value on the Y axis

    private int yDirection = DIRECTION_DOWN;

    public Speed() {
        this.yv = 14;
    }

    public Speed(float xv, float yv) {
        this.yv = yv;
    }


    public float getYv() {
        return yv;
    }
    public void setYv(float yv) {
        this.yv = yv;
    }


    public int getyDirection() {
        return yDirection;
    }
    public void setyDirection(int yDirection) {
        this.yDirection = yDirection;
    }


}
