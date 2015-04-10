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
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.security.Key;
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
    private Gate frame;
    private Gate backbutton;
    private Gate instructions;
    private Gate settings;
    private Gate ad;
    private Speed speedup;
    private Paint paint;
    static int counter;
    int level = 4;
    int randomizer;
    int score;
    int highscore = 0;
    boolean tablet = false; //This changes from Phone to Tablet mode
    static int titlebuttons =0; //To render new pages. 0 = Main, 1 = Instructions, 2 = Score Page, 3 = startup, 4 Color Map, 5 EXIT, 6 Settings
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
        //create start button
        startbutton = new Gate("menu", BitmapFactory.decodeResource(getResources(), R.drawable.logo), getWidth()/2, getHeight()/2);
        frame = new Gate("frame", BitmapFactory.decodeResource(getResources(), R.drawable.frame), getWidth()/2, getHeight()/2);
        //create instructions button
        instructions = new Gate("instructions", BitmapFactory.decodeResource(getResources(), R.drawable.instructions), getWidth()*1/3, getHeight()/2);
        //create settings button
        settings = new Gate("settings", BitmapFactory.decodeResource(getResources(), R.drawable.settings), getWidth()*2/3, getHeight()/2);
        //create advertisement banner
        ad = new Gate("settings", BitmapFactory.decodeResource(getResources(), R.drawable.purple), getWidth()/2, getHeight()-200);

        //Title & Score Initializing
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(64);
        paint.setTextAlign(Paint.Align.CENTER);



    }

    //Changing DIP TO Pixels
    public static float dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
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


        //DIP to Pixel Main Screen


        //create start button
        startbutton = new Gate("play", BitmapFactory.decodeResource(getResources(), R.drawable.logo), getWidth()/2, getHeight()*2/5);
        //create instructions button
        instructions = new Gate("instructions", BitmapFactory.decodeResource(getResources(), R.drawable.instructions), getWidth()*1/3, getHeight()/2);
        //create settings button
        settings = new Gate("settings", BitmapFactory.decodeResource(getResources(), R.drawable.settings), getWidth()*2/3, getHeight()/2);
        //create advertisement banner
        ad = new Gate("settings", BitmapFactory.decodeResource(getResources(), R.drawable.purple), getWidth()/2, getHeight()-200);
        //Back Button
        backbutton = new Gate("back", BitmapFactory.decodeResource(getResources(), R.drawable.back), getWidth() - 150, getHeight() - 150);

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

    //Pressing the Back button >2.0
    public static void backpress(int countback, int titleback){
        counter = countback;
        titlebuttons = titleback;
    }

    //Pressing the Back button
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        Log.d(TAG, "KEY PRESSED: " + keyCode);
        if ((keyCode == KeyEvent.KEYCODE_BACK && titlebuttons!= 0) || (keyCode == KeyEvent.KEYCODE_BACK && titlebuttons==0 && counter != 0)){
            counter = 0;
            titlebuttons = 0;
            Log.d(TAG, "Back to Menu");
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK && titlebuttons ==0  && counter == 0){
            titlebuttons = 5;
            Log.d(TAG, "Back Button QUIT");
            return true;
        }
        return true;
    }

    //Pressing on the screen
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //------------------
        //Information on Buttons
        //50x50 pixels with a gap of 24 pixels
        //That means +-12 from getwidth/2

        //Comments to the right of the getWidth()/2 is what it should be according to how I made the artwork
        //Yet they aren't...

//        int blueleft = getWidth()/2 + 225;   //86
//        int blueright =  getWidth()/2 + 425; //136
//        int yellowright = getWidth()/2+200; //62
//        int yellowleft = getWidth()/2 + 25; //12
//        int redright = getWidth()/2-25; //-12
//        int redleft = getWidth()/2-200; //-62
//        int whiteright = getWidth()/2-225; //-86
//        int whiteleft = getWidth()/2-425; //-136

        //dip version
        float dipblueleft =  dipToPixels(context, 86) ;   //86
        float dipblueright =  dipToPixels(context, 136); //136
        float dipyellowright = dipToPixels(context, 62); //62
        float dipyellowleft = dipToPixels(context, 12); //12
        float dipredright = dipToPixels(context, -12); //-12
        float dipredleft = dipToPixels(context, -62); //-62
        float dipwhiteright = dipToPixels(context, -86); //-86
        float dipwhiteleft = dipToPixels(context, -136);

        float blueleft = getWidth()/2 + dipblueleft;   //86
        float blueright =  getWidth()/2 + dipblueright; //136
        float yellowright = getWidth()/2+dipyellowright; //62
        float yellowleft = getWidth()/2 + dipyellowleft; //12
        float redright = getWidth()/2+dipredright; //-12
        float redleft = getWidth()/2+dipredleft; //-62
        float whiteright = getWidth()/2+dipwhiteright; //-86
        float whiteleft = getWidth()/2+dipwhiteleft; //-136


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
//                    if (event.getY() < 200){
//                        thread.setRunning(false);
//                        ((Activity) getContext()).finish();
//                    }


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
                //MENU OPTIONS, ETC.
                case MotionEvent.ACTION_UP:{
                    //CLICK TO BEGIN GAME
                    if (titlebuttons == 0 && counter == 0 && event.getX() > (getWidth()/2-100) && event.getX() < (getWidth()/2+100) && event.getY() > (getHeight()*2/5-100) && event.getY() < (getHeight()*2/5+100)){
                        // create droid and load bitmap
                        droid = new Droid("blue", BitmapFactory.decodeResource(getResources(), R.drawable.blue), 500, 0);
                        // create gate and load bitmap
                        gate = new Gate("start", BitmapFactory.decodeResource(getResources(), R.drawable.box_start), getWidth()/2, getHeight()-325);

                        //Creating the "buttons"
                        button = new Gate("white", BitmapFactory.decodeResource(getResources(), R.drawable.button), getWidth()/2, getHeight()-110);

                        counter = 1;
                        speedup = new Speed(0, dipToPixels(context, 5));
                        droid.setSpeed(speedup);

                    }
                    //Go to instruction Screen
                    if (counter == 0 && titlebuttons == 0  && event.getY() > (getHeight()/2-100) && event.getY() < (getHeight()/2+100) && event.getX() < (getWidth()*1/3+100)){
                        paint.setTextSize(48);
                        titlebuttons = 1;
                        backbutton = new Gate("back", BitmapFactory.decodeResource(getResources(), R.drawable.back), getWidth()-150, getHeight()-150);
                        Log.d(TAG, "MAIN TO INSTRUCTIONS");
                    }

                    //Go to Settings
                    if (counter == 0 && titlebuttons == 0  && event.getY() > (getHeight()/2-100) && event.getY() < (getHeight()/2+100) && event.getX() > (getWidth()*2/3-100)) {
                        paint.setTextSize(48);
                        titlebuttons = 6;
                        backbutton = new Gate("back", BitmapFactory.decodeResource(getResources(), R.drawable.back), getWidth() - 150, getHeight() - 150);
                        Log.d(TAG, "MAIN TO INSTRUCTIONS");
                    }

                    //In Settings Screen - Delete High Score
                    if (titlebuttons ==6 && event.getY() > getHeight()/2-350 && event.getY() < getHeight()/2-250){
                        highscore = 0;
                        counter = 0;
                        Log.d(TAG, "High Score RESET");
                    }

                    //In Settings Screen - Change Mode to Tablet/INSANE MODE
                    if (titlebuttons ==6 && event.getY() > getHeight()/2-250 && event.getY() < getHeight()/2-150){
                        counter = 0;
                        tablet = true;
                        Log.d(TAG, "MODE Changed");
                    }
                    //In Settings Screen - Change Mode to Mobile MODE
                    if (titlebuttons ==6 && event.getY() > getHeight()/2-150 && event.getY() < getHeight()/2-50){
                        counter = 0;
                        tablet = false;
                        Log.d(TAG, "MODE Changed");
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
                    if (titlebuttons ==2 && event.getY() > getHeight()/2+200 && event.getY() < getHeight()/2+400 && event.getX() < getWidth()/2-200) {

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
                    if (titlebuttons ==2 && event.getY() > getHeight()/2+200 && event.getY() < getHeight()/2+400 && event.getX() > getWidth()/2+200) {
                        titlebuttons = 0;
                        counter = 0;
                        Log.d(TAG, "Score Screen HOME");

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

        //All cases when not being shut down - FRAME
        if (titlebuttons != 5){
            frame.draw(canvas);
        }

        if (titlebuttons != 5 && counter ==0){
            canvas.drawColor(Color.WHITE);
        }

        //Changing background color based on the gate coming down
        if (titlebuttons != 5 && counter >= 1){
            //WHITE
            if (droid.Color() ==0){
                canvas.drawColor(Color.WHITE);
            }
            //Pastel RED
            if (droid.Color() ==1){
                canvas.drawColor(Color.rgb(246, 206, 213));
            }
            //Pastel YELLOW
            if (droid.Color() ==2){
                canvas.drawColor(Color.rgb(255, 253, 193));
            }
            //Pastel Blue Background
            if (droid.Color() ==3){
                canvas.drawColor(Color.rgb(112,134,222));
            }
            //Pastel GREEN
            if (droid.Color() ==4){
                canvas.drawColor(Color.rgb(170,232,165));
            }
            //PURPLE
            if (droid.Color() ==5){
                canvas.drawColor(Color.rgb(233,163,252));
            }
            //ORANGE
            if (droid.Color() ==6){
                canvas.drawColor(Color.rgb(251, 172, 116));
            }
            //PINK
            if (droid.Color() ==7){
                canvas.drawColor(Color.rgb(255, 211, 231));
            }
            //LIGHTYELLOW
            if (droid.Color() ==8){
                canvas.drawColor(Color.rgb(255,255,243));
            }
            //SKYBLUE
            if (droid.Color() ==9){
                canvas.drawColor(Color.rgb(198,234,248));
            }
        }

        //HOME SCREEN
        if (counter ==0 && titlebuttons == 0){
            startbutton.draw(canvas);
            settings.draw(canvas);
            instructions.draw(canvas);
            ad.draw(canvas);
            canvas.drawText("COLOR CRAZE", getWidth()/2, 300, paint);
            canvas.drawText("High Score: " + highscore, getWidth()/2, getHeight()/2+250, paint);
//            canvas.drawText("Play", getWidth()/2, getHeight()/2+250, paint);
//            canvas.drawText("Instructions", getWidth()/2, getHeight()/2+350, paint);
//            canvas.drawText("Settings", getWidth()/2, getHeight()/2+450, paint);


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
            droid.draw(canvas);
            gate.draw(canvas);

            canvas.drawText("Score: " +score + " High Score: " + highscore, getWidth()/2, 100, paint);
            //canvas.drawText("" +highscore, getWidth()/2+225, getHeight()-375, paint);
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
            canvas.drawText("Reset High Score", getWidth()/2, getHeight()/2-300, paint);
            backbutton.draw(canvas);
            if (highscore == 0){
                canvas.drawText("High Score has been Reset", getWidth()/2, getHeight()/2, paint);
            }
            if (tablet == false){
                canvas.drawText("Mode: Mobile", getWidth()/2, getHeight()/2-200, paint);
            }
            if (tablet == true){
                canvas.drawText("Mode: Tablet/INSANE MOBILE", getWidth()/2, getHeight()/2-100, paint);
            }

        }

    }

    /**
     * This is the game update method. It iterates through all the objects
     * and calls their update method if they have one or calls specific
     * engine's update method.
     */
    public void update() {

        //Board edge
        //Buttons = 20px wide
        //Player piece = 30px wide

        if (counter >=1){
            score = counter*100-100;
            // check collision with bottom wall if heading down
            if ((droid.getY() + droid.getBitmap().getHeight()>= getHeight()-275) && (droid.Color() != gate.Color())) {
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

            if ((droid.getY() + droid.getBitmap().getHeight()>= getHeight()-275) && (droid.Color() == gate.Color())) {
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
            float lvl2 = dipToPixels(context, 6);//Actually 6 and increment by 1
            float lvl3 = dipToPixels(context, 7);
            float lvl4 = dipToPixels(context, 8);
            float lvl5 = dipToPixels(context, 9);
            float lvl6 = dipToPixels(context, 10);
            float lvl7 = dipToPixels(context, 12);

            //Updating Speeds - Tablet
            float lvl12 = dipToPixels(context, 12);//Actually 6 and increment by 1
            float lvl13 = dipToPixels(context, 13);
            float lvl14 = dipToPixels(context, 14);
            float lvl15 = dipToPixels(context, 15);
            float lvl16 = dipToPixels(context, 16);
            float lvl17 = dipToPixels(context, 17);

            //LEVEL 2
            if (counter >= 3 && counter < 10){
                level = 7;
                randomizer = 2;
                speedup = new Speed(0, lvl2);
                if (tablet == true){
                    speedup = new Speed(0,lvl12);
                }
                droid.setSpeed(speedup);
            }
            //LEVEL 3
            if (counter >= 10 && counter < 20){
                level = 10;
                randomizer = 1;
                speedup = new Speed(0,lvl3);
                if (tablet == true){
                    speedup = new Speed(0,lvl13);
                }
                droid.setSpeed(speedup);
            }
            //LEVEL 4
            if (counter >= 20 && counter < 35){
                randomizer = 0;
                speedup = new Speed(0,lvl4);
                if (tablet == true){
                    speedup = new Speed(0,lvl14);
                }
                droid.setSpeed(speedup);
            }
            //LEVEL 5
            if (counter >= 35 && counter <50){
                speedup = new Speed(0,lvl5);
                if (tablet == true){
                    speedup = new Speed(0,lvl15);
                }
                droid.setSpeed(speedup);
            }
            //LEVEL 6
            if (counter >= 50 && counter <100){
                speedup = new Speed(0,lvl6);
                if (tablet == true){
                    speedup = new Speed(0,lvl16);
                }
                droid.setSpeed(speedup);
            }
            //LEVEL 7
            if (counter >= 100){
                speedup = new Speed(0,lvl7);
                if (tablet == true){
                    speedup = new Speed(0,lvl17);
                }
                droid.setSpeed(speedup);
            }

            // Update the lone droid
            droid.update();
        }



    }

}
