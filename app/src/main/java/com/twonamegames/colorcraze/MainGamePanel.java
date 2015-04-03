package com.twonamegames.colorcraze;

import com.twonamegames.colorcraze.model.Droid;
import com.twonamegames.colorcraze.model.Gate;
import com.twonamegames.colorcraze.model.components.Speed;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.nfc.Tag;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

public class MainGamePanel extends SurfaceView implements
        SurfaceHolder.Callback {

    private static final String TAG = MainGamePanel.class.getSimpleName();

    private MainThread thread;
    private Droid droid;
    private Gate gate;
//    private Gate whitebutton;
//    private Gate redbutton;
//    private Gate yellowbutton;
//    private Gate bluebutton;
    private static SharedPreferences prefs;
    private Gate button;
    private Gate startbutton;
    private Gate backbutton;
    private Speed speedup;
    private Paint paint;
    int counter;
    int level = 4;
    int randomizer;
    int score;
    int highscore = 0;
    int titlebuttons =0; //To render new pages. 0 = Main, 1 = Instructions, 2 = Score Page, 3 = startup, 4 Color Map, 5 EXIT, 6 Settings
    Context context;


    public MainGamePanel(Context context) {
        super(context);
        // adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);

        this.context = context;

        // create the game loop thread
        thread = new MainThread(getHolder(), this);

        // make the GamePanel focusable so it can handle events
        setFocusable(true);

        //Using SharedPreferences
       //SharedPreferences prefs;



    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // at this point the surface is created and
        // we can safely start the game loop
        thread.setRunning(true);
        thread.start();


        //create loading screen
        /* complete

         */

        //create start button
        startbutton = new Gate("menu", BitmapFactory.decodeResource(getResources(), R.drawable.logo), getWidth()/2, getHeight()/2);

        //Title & Score Initializing
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(64);
        paint.setTextAlign(Paint.Align.CENTER);

        //Load High Score
        SharedPreferences prefs = this.context.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        highscore = prefs.getInt("key", 0); //0 is the default value


    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "Surface is being destroyed");

        titlebuttons = 5;
//        ((Activity) getContext()).finish();
        // tell the thread to shut down and wait for it to finish
        // this is a clean shutdown
        boolean retry = true;
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                // try again shutting down the thread
            }
        }
        Log.d(TAG, "Thread was shut down cleanly");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //------------------
        //Information on Buttons
        //50x50 pixels with a gap of 24 pixels
        //That means +-12 from getwidth/2

        int blueleft = getWidth()/2 + 225;   //86
        int blueright =  getWidth()/2 + 425; //136
        int yellowright = getWidth()/2+200; //62
        int yellowleft = getWidth()/2 + 25; //12
        int redright = getWidth()/2-25; //-12
        int redleft = getWidth()/2-200; //-62
        int whiteright = getWidth()/2-225; //-86
        int whiteleft = getWidth()/2-425; //-136

        //------------------

        //NEW IDEA-----------------------
        //get pointer index from the event object
        int pointerIndex = event.getActionIndex();

        //get pointer ID
        int pointerId = event.getPointerId(pointerIndex);

        //get masked (not specific to a pointer) action
        int maskedAction = event.getActionMasked();

        int index = MotionEventCompat.getActionIndex(event);

        if (event.getPointerCount() > 1) {
            int xPos = (int) MotionEventCompat.getX(event, index);
            int yPos = (int) MotionEventCompat.getY(event, index);
            //Log.d(TAG, "Multitouch event occurred");

            switch (maskedAction) {
                case MotionEvent.ACTION_POINTER_DOWN: {

                    //GREEN
                    if (xPos > yellowleft && event.getX() > yellowleft && yPos > getHeight()-300) {
                        gate = new Gate("green", BitmapFactory.decodeResource(getResources(), R.drawable.box_green), getWidth() / 2, getHeight() - 325);
                        Log.d(TAG, "Creating GREEN Block");
                    }

                    //PURPLE
                    if ((xPos > blueleft && event.getX() > redleft && event.getX() < redright && yPos > getHeight()-300) || (event.getX() > blueleft && xPos > redleft && xPos < redright && yPos > getHeight()-300)) {
                        gate = new Gate("purple", BitmapFactory.decodeResource(getResources(), R.drawable.box_purple), getWidth() / 2, getHeight() - 325);
                        Log.d(TAG, "Creating PURPLE Block");
                    }

                    //ORANGE
                    if (xPos < blueleft && xPos > redleft && event.getX() < blueleft && event.getX() > redleft && yPos > getHeight()-300) {
                        gate = new Gate("orange", BitmapFactory.decodeResource(getResources(), R.drawable.box_orange), getWidth() / 2, getHeight() - 325);
                        Log.d(TAG, "Creating ORANGE Block");
                    }

                    //PINK
                    if (xPos < redright && event.getX() < redright && yPos > getHeight()-300) {
                        gate = new Gate("pink", BitmapFactory.decodeResource(getResources(), R.drawable.box_pink), getWidth() / 2, getHeight() - 325);
                        Log.d(TAG, "Creating PINK Block");
                    }

                    //LIGHT YELLOW
                    if ((xPos > yellowleft && xPos < yellowright && event.getX() < whiteright && yPos > getHeight()-300) || (event.getX() > yellowleft && event.getX() < yellowright && xPos < whiteright && yPos > getHeight()-300)) {
                        gate = new Gate("lightyellow", BitmapFactory.decodeResource(getResources(), R.drawable.box_lightyellow), getWidth() / 2, getHeight() - 325);
                        Log.d(TAG, "Creating LIGHT YELLOW Block");
                    }

                    //SKY BLUE
                    if ((xPos > blueleft && event.getX() < whiteright && yPos > getHeight()-300) || (event.getX() > blueleft && xPos < whiteright && yPos > getHeight()-300)) {
                        gate = new Gate("skyblue", BitmapFactory.decodeResource(getResources(), R.drawable.box_skyblue), getWidth() / 2, getHeight() - 325);
                        Log.d(TAG, "Creating SKY BLUE Block");
                    }
                }
            }


        }
        else {
            //Single Touches
            switch (maskedAction) {
                case MotionEvent.ACTION_DOWN: {

                    //Safe Close
                    if (event.getY() < 200){
                        thread.setRunning(false);
                        ((Activity) getContext()).finish();
                    }


                    //CLICK TO BEGIN GAME
                    if (counter == 0 && event.getX() > (getWidth()/2-100) && event.getX() < (getWidth()/2+100) && event.getY() > (getHeight()/2-100) && event.getY() < (getHeight()/2+100)){
                        // create droid and load bitmap
                        droid = new Droid("blue", BitmapFactory.decodeResource(getResources(), R.drawable.blue), 500, 0);
                        // create gate and load bitmap
                        gate = new Gate("start", BitmapFactory.decodeResource(getResources(), R.drawable.box_start), getWidth()/2, getHeight()-325);

                        //Creating the "buttons"
                        button = new Gate("white", BitmapFactory.decodeResource(getResources(), R.drawable.button), getWidth()/2, getHeight()-110);

                        counter = 1;

                    }
                    //Go to instruction Screen
                    if (counter == 0 && titlebuttons == 0  && event.getY() > (getHeight()/2+200) && event.getY() < (getHeight()/2+300)){
                        paint.setTextSize(48);
                        titlebuttons = 1;
                        backbutton = new Gate("back", BitmapFactory.decodeResource(getResources(), R.drawable.box_purple), getWidth()-100, getHeight()-100);
                        Log.d(TAG, "MAIN TO INSTRUCTIONS");
                    }

                    //Go to Settings
                    if (counter == 0 && titlebuttons == 0  && event.getY() > (getHeight()/2+300) && event.getY() < (getHeight()/2+400)) {
                        paint.setTextSize(48);
                        titlebuttons = 6;
                        backbutton = new Gate("back", BitmapFactory.decodeResource(getResources(), R.drawable.box_purple), getWidth() - 100, getHeight() - 100);
                        Log.d(TAG, "MAIN TO INSTRUCTIONS");
                    }

                    //In Settings Screen - Delete High Score
                    if (titlebuttons ==6 && event.getY() > getHeight()/2-100 && event.getY() < getHeight()/2+100){
                        highscore = 0;
                        Log.d(TAG, "High Score RESET");
                    }


                    //Return from instruction Screen
                    if (titlebuttons ==1 && event.getY() > getHeight()-200 && event.getX() > getWidth()-200){
                        paint.setTextSize(64);
                        titlebuttons = 0;
                        Log.d(TAG, "INSTRUCTION SCREEN BACK");


                    }

                    //Return from Settings Screen
                    if (titlebuttons ==6 && event.getY() > getHeight()-200 && event.getX() > getWidth()-200){
                        paint.setTextSize(64);
                        titlebuttons = 0;
                        Log.d(TAG, "SETTINGS SCREEN BACK");
                    }

                    //Score Screen - RESTART Command
                    if (titlebuttons ==2 && event.getY() > getHeight()/2+200 && event.getX() < getWidth()/2-200) {

                        droid = new Droid("blue", BitmapFactory.decodeResource(getResources(), R.drawable.blue), 500, 0);
                        // create gate and load bitmap
                        gate = new Gate("start", BitmapFactory.decodeResource(getResources(), R.drawable.box_start), getWidth()/2, getHeight()-325);

                        //Creating the "buttons"
                        button = new Gate("white", BitmapFactory.decodeResource(getResources(), R.drawable.button), getWidth()/2, getHeight()-110);

                        titlebuttons = 0;
                        counter = 1;
                        Log.d(TAG, "Score Screen RESTART");

                    }

                    //Score Screen - HOME Command
                    if (titlebuttons ==2 && event.getY() > getHeight()/2+200 && event.getX() > getWidth()/2+200) {
                        titlebuttons = 0;
                        counter = 0;
                        Log.d(TAG, "Score Screen HOME");

                    }


                    //WHITE
                    if ((event.getY() > (getHeight() - 300)) && event.getX() < whiteright && event.getX() > whiteleft) {
                        gate = new Gate("white", BitmapFactory.decodeResource(getResources(), R.drawable.box_white), getWidth() / 2, getHeight() - 325);
                        Log.d(TAG, "Creating WHITE Block");
                    }
                    //RED
                    if ((event.getY() > (getHeight() - 300)) && redleft < event.getX() && event.getX() < redright) {
                        gate = new Gate("red", BitmapFactory.decodeResource(getResources(), R.drawable.box_red), getWidth() / 2, getHeight() - 325);
                        Log.d(TAG, "Creating RED Block");
                    }
                    //BLUE
                    if ((event.getY() > (getHeight() - 300)) && event.getX() > blueleft && event.getX() < blueright) {
                        gate = new Gate("blue", BitmapFactory.decodeResource(getResources(), R.drawable.box_blue), getWidth() / 2, getHeight() - 325);
                        Log.d(TAG, "Creating BLUE Block");
                    }
                    //YELLOW
                    if ((event.getY() > (getHeight() - 300)) && yellowleft < event.getX() && event.getX() < yellowright) {
                        gate = new Gate("yellow", BitmapFactory.decodeResource(getResources(), R.drawable.box_yellow), getWidth() / 2, getHeight() - 325);
                        Log.d(TAG, "Creating YELLOW Block");
                    }
                }
            }
        }
        return true;
    }

    public void render(Canvas canvas) {

        //Shutting Down
        if (titlebuttons == 5){
            Log.d(TAG, "No More Canvas");
            thread.setRunning(false);
            ((Activity) getContext()).finish();
        }

        //All cases when not being shut down
        if (titlebuttons != 5){
            canvas.drawColor(Color.WHITE);
        }
        //HOME SCREEN
        if (counter ==0 && titlebuttons == 0){
            startbutton.draw(canvas);
            canvas.drawText("High Score: " + highscore, getWidth()/2, getHeight()/2-300, paint);
            canvas.drawText("Tap Here for Instructions", getWidth()/2, getHeight()/2+250, paint);
            canvas.drawText("Settings", getWidth()/2, getHeight()/2+350, paint);

        }
        //Instruction Screen
        if (titlebuttons == 1){

            canvas.drawText("You have 4 buttons at the bottom to use:", getWidth()/2, getHeight()/2-500, paint);
            canvas.drawText("White, Red, Yellow, Blue.", getWidth()/2, getHeight()/2-400, paint);
            canvas.drawText("Tap the buttons to change the color", getWidth()/2, getHeight()/2-300, paint);
            canvas.drawText("BEFORE it hits the block!", getWidth()/2, getHeight()/2-200, paint);
            canvas.drawText("You can tap two buttons at the same time!", getWidth()/2, getHeight()/2, paint);
            canvas.drawText("Learn all the color combos!", getWidth()/2, getHeight()/2+100, paint);
            canvas.drawText("(Example: Blue + Yellow = Green)", getWidth()/2, getHeight()/2+200, paint);
            canvas.drawText("Tap PURPLE to go back and play!", getWidth()/2, getHeight()/2+450, paint);
            backbutton.draw(canvas);
        }
        //GAME SCREEN
        if (counter >=1 && titlebuttons == 0){
            button.draw(canvas);
            gate.draw(canvas);
            droid.draw(canvas);

            canvas.drawText("" +score, getWidth()/2+225, getHeight()-300, paint);
            canvas.drawText("" +highscore, getWidth()/2+225, getHeight()-375, paint);
        }
        //SCORE SCREEN
        if (titlebuttons == 2){
            if (score == 0){
                canvas.drawText("Oooh, not even a single one", getWidth()/2, getHeight()/2-200, paint);
                canvas.drawText("Your score was " +score, getWidth()/2, getHeight()/2-100, paint);
            }
            if (score == highscore && score != 0) {
                canvas.drawText("NEW HIGH SCORE!!!", getWidth()/2, getHeight()/2-200, paint);
                canvas.drawText("Your score was " +score, getWidth()/2, getHeight()/2-100, paint);
            }
            if (score != 0 && score < highscore) {
                canvas.drawText("Good Run!", getWidth()/2, getHeight()/2-200, paint);
                canvas.drawText("Your score was " +score, getWidth()/2, getHeight()/2-100, paint);
            }
            canvas.drawText("RESTART", getWidth()/2-250, getHeight()/2+300, paint); //Need to make a button
            canvas.drawText("HOME", getWidth()/2+250, getHeight()/2+300, paint); //need to make a button

        }

        //SETTINGS SCREEN
        if (titlebuttons == 6){
            canvas.drawText("Reset High Score", getWidth()/2, getHeight()/2, paint);
            backbutton.draw(canvas);
            if (highscore == 0){
                canvas.drawText("High Score is 0", getWidth()/2, getHeight()/2+100, paint);
            }

        }

    }

    /**
     * This is the game update method. It iterates through all the objects
     * and calls their update method if they have one or calls specific
     * engine's update method.
     */
    public void update() {

        if (counter >=1){
            score = counter*100-100;
            // check collision with bottom wall if heading down
            if ((droid.getY() + droid.getBitmap().getHeight()>= getHeight()-260) && (droid.Color() != gate.Color())) {
                Log.d(TAG, "GAME OVER");

                //Set new High Score
                if (score > highscore){
                    highscore = score;
                    //setting preferences
                    SharedPreferences prefs = this.context.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("key", highscore);
                    editor.commit();
                }
                titlebuttons = 2;
                counter = 0;
                level = 4;
                randomizer = 0;
//                thread.setRunning(false);
//                ((Activity)getContext()).finish();
            }

            if ((droid.getY() + droid.getBitmap().getHeight()>= getHeight()-250) && (droid.Color() == gate.Color())) {
                Log.d(TAG, "passed");
                counter++;
                Random r = new Random();
                int i1 = r.nextInt(level - randomizer) + randomizer;
                if (i1==0){
                    droid = new Droid("white", BitmapFactory.decodeResource(getResources(), R.drawable.white), getWidth()/2, 0);
                }
                if (i1==1){
                    droid = new Droid("red", BitmapFactory.decodeResource(getResources(), R.drawable.red), getWidth()/2, 0);
                }
                if (i1==2){
                    droid = new Droid("yellow", BitmapFactory.decodeResource(getResources(), R.drawable.yellow), getWidth()/2, 0);
                }
                if (i1==3){
                    droid = new Droid("blue", BitmapFactory.decodeResource(getResources(), R.drawable.blue), getWidth()/2, 0);
                }
                if (i1==4){
                    droid = new Droid("green", BitmapFactory.decodeResource(getResources(), R.drawable.green), getWidth()/2, 0);
                }
                if (i1==5){
                    droid = new Droid("purple", BitmapFactory.decodeResource(getResources(), R.drawable.purple), getWidth()/2, 0);
                }
                if (i1==6){
                    droid = new Droid("orange", BitmapFactory.decodeResource(getResources(), R.drawable.orange), getWidth()/2, 0);
                }
                if (i1==7){
                    droid = new Droid("pink", BitmapFactory.decodeResource(getResources(), R.drawable.pink), getWidth()/2, 0);
                }
                if (i1==8){
                    droid = new Droid("lightyellow", BitmapFactory.decodeResource(getResources(), R.drawable.lightyellow), getWidth()/2, 0);
                }
                if (i1==9){
                    droid = new Droid("skyblue", BitmapFactory.decodeResource(getResources(), R.drawable.skyblue), getWidth()/2, 0);
                }

            }
            //Updating Speeds
            //LEVEL 2
            if (counter >= 5 && counter < 10){
                level = 7;
                randomizer = 2;
                speedup = new Speed(0,16);
                droid.setSpeed(speedup);
            }
            //LEVEL 3
            if (counter >= 10 && counter < 18){
                level = 10;
                randomizer = 1;
                speedup = new Speed(0,19);
                droid.setSpeed(speedup);
            }
            //LEVEL 4
            if (counter >= 18 && counter < 30){
                randomizer = 0;
                speedup = new Speed(0,23);
                droid.setSpeed(speedup);
            }
            //LEVEL 5
            if (counter >= 30 && counter <45){
                speedup = new Speed(0,26);
                droid.setSpeed(speedup);
            }
            //LEVEL 6
            if (counter >= 45){
                speedup = new Speed(0,29);
                droid.setSpeed(speedup);
            }

            // Update the lone droid
            droid.update();
        }



    }

}
