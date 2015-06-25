package com.twonamegames.colorcraze.model;

//--------------------------------------------
//Droid.java is any moving component
//
//In other words its just the gates as they slide down the screen.
//Some code here is worthless. It came when learning speed work
//and I never changed it.
//-------------------------------------

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.twonamegames.colorcraze.R;
import com.twonamegames.colorcraze.ThemeUtil;
import com.twonamegames.colorcraze.model.components.Speed;

//Casey's Notes
//
//	In all liklihood, I don't think we will need this class. The surfaceView can
//	draw primitive shapes very well, so we could just draw the appropriate shapes
//	there right as we need them, and cut out this middle man. I'll keep this around
//	to refer to how to migrate the moving part into the main game panel when that
//	time comes.
public class Droid {

    private Bitmap bitmap;	// the actual bitmap
    private int x;			// the X coordinate
    private float y;			// the Y coordinate
    private boolean touched;	// if droid is touched/picked up
    private Speed speed;	// the speed with its directions
    private String color;
    private int colormatch;
	private int drawingColor;
	private Paint paint;
	private Drawable drawable;

    public Droid(int colormatch, Context context, int x, float y) {
        this.x = x;
        this.y = y;
        this.speed = new Speed();
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);

		ThemeUtil util = new ThemeUtil(context);
		drawingColor = util.getColor(colormatch);
		ColorFilter filter = new PorterDuffColorFilter(drawingColor, PorterDuff.Mode.SRC_IN);
		paint.setColorFilter(filter);

		bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.white);
		this.colormatch = colormatch;

		drawable = context.getResources().getDrawable(R.drawable.gate);
		drawable.setColorFilter(filter);
		drawable.setBounds(new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()));
    }

    public int getColorMatch(){
        return colormatch;
    }

	public int getDrawingColor() {
		return drawingColor;
	}

    public Bitmap getBitmap() {
        return bitmap;
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
        canvas.drawBitmap(bitmap, x - (bitmap.getWidth() / 2), y - (bitmap.getHeight() / 2), paint);
//		drawable.draw(canvas);
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
