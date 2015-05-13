package com.twonamegames.colorcraze.model;

//--------------------------------------------
//Droid.java is any moving component
//
//In other words its just the gates as they slide down the screen.
//Some code here is worthless. It came when learning speed work
//and I never changed it.
//-------------------------------------

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.twonamegames.colorcraze.R;
import com.twonamegames.colorcraze.model.components.Speed;

public class Droid {

    private Bitmap bitmap;	// the actual bitmap
    private int x;			// the X coordinate
    private float y;			// the Y coordinate
    private boolean touched;	// if droid is touched/picked up
    private Speed speed;	// the speed with its directions
    private String color;
    private int colormatch;


    public Droid(String color, Bitmap bitmap, int x, float y) {
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
        this.speed = new Speed();

        //Providing int comparison to know if correct color or not when reaching block

        if (color.equals("white")){
            colormatch= 0;
        }
        if (color.equals("red")){
            colormatch=  1;
        }
        if (color.equals("yellow")){
            colormatch= 2;
        }
        if (color.equals("blue")){
            colormatch= 3;
        }
        if (color.equals("green")){
            colormatch= 4;
        }
        if (color.equals("purple")){
            colormatch= 5;
        }
        if (color.equals("orange")){
            colormatch= 6;
        }
        if (color.equals("pink")){
            colormatch= 7;
        }
        if (color.equals("lightyellow")){
            colormatch= 8;
        }
        if (color.equals("skyblue")){
            colormatch= 9;
        }

    }

    public int Color(){
        return colormatch;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public float getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }

    public Speed getSpeed() {
        return speed;
    }

    public void setSpeed(Speed speed) {
        this.speed = speed;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x - (bitmap.getWidth() / 2), y - (bitmap.getHeight() / 2), null);
    }

    /**
     * Method which updates the droid's internal state every tick
     */
    public void update() {
        if (!touched) {
            y += (speed.getYv() * speed.getyDirection());
        }
    }
}
