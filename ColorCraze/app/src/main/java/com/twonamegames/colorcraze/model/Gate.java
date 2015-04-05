package com.twonamegames.colorcraze.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.twonamegames.colorcraze.R;
import com.twonamegames.colorcraze.model.components.Speed;

public class Gate {

    private Bitmap bitmap;    // the actual bitmap
    private int x;            // the X coordinate
    private float y;            // the Y coordinate
    private String color;
    private int colormatch;

    public Gate(String color, Bitmap bitmap, int x, float y) {
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;


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


    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x - (bitmap.getWidth() / 2), y - (bitmap.getHeight() / 2), null);
    }

}