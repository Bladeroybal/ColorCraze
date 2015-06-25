package com.twonamegames.colorcraze;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.twonamegames.colorcraze.model.Droid;
import com.twonamegames.colorcraze.model.Gate;
import com.twonamegames.colorcraze.model.components.Speed;

//----------------------------------------------------
// This is the core of the game. All logic of the game
//functioning overall can be found in this section.
//
// It is broken down into several components
//There is the main branch of logic, all activity based(finger press screen) components,
// Rendering of each page, and the speed/levels
//-----------------------------------------------------

//Casey's Notes:
//
//WHAT THIS CLASS WILL DO
//	As the game is played, this class will continuously create a object and draw
//	it as it falls does the screen. Exactly how that is done will be left to the
//	implementation, but as it draws, we will fire events to the calling activity.
//	These interface events will allow us to separate the concern between drawing
//	the screen, and what happens when we draw. Thus, we draw the falling object
//	in its position, then tell the Activity when it hits the bottom. The Activity
//	can then choose to terminate the game then, or continue (in the case of an
//	endless mode, for example). We will also send out events for the start of
//	an object falling, and its position when each frame is drawn, and the position
//	of the object when its color has been matched. It is important to separate the
//	concerns of each component, so that we can reuse them in various ways without
//	having to write in special use cases for each component. Each special case
//	only needs to worry about its own special case, but everything else will just
//	continue doing what it always does.
//
//	This class will only manage what is necessary to actually draw items to the
//	main game. That is, it will run the thread that draws the UI, determine the
//	speed with which to send the blocks, and draw them with the appropriate colors.
//	This class is NOT responsible for determining whether a block matches the
//	buttons being pressed, how many points are collected for matching a block,
//	or when to speed up.
//
//	As it currently stands, everything is commented out so that I could ignore
//	this class as I began removing images from the drawables. I believe all the
//	code related to the actual gameplay will be fine to leave untouched, I just
//	haven't gotten to where I need so that I can pull that out from everything
//	else that I have moved elsewhere. There was some code I deleted outright
//	before I began moving stuff around, so I don't know exactly which parts still
//	remain in this class, and what was partially broken before I started to
//	comment it all out and pick it up later.
public class MainGamePanel extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = MainGamePanel.class.getSimpleName();

    private MainThread thread;
    private Droid droid;
    private Gate gate;
    private Gate button;
//    private Gate ad;
    private Speed speedup;
    private Paint paint;
    static int counter;
    int level = 4;
    int randomizer;
    int adcount = 0;
    int score;
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

        //Below is both here and in SurfaceCreated. That is because for some reason I was getting bugs that would disappear
        //when I had this code only in both positions. It fixed a lot of alignment issues except one.
        //The first moving block in the game is always skewed slightly to the left.

        //initialize first gate
        droid = new Droid(4, context, getWidth()/2, 0);

        //Title & Score Initializing
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(64);
        paint.setTextAlign(Paint.Align.CENTER);
    }


    //Changing DIP TO Pixels
    //----------
    //Android code reads pixels as they are, however not all devices have same amount of pixels or pixel density.
    //This code converts all pixel calculations below in code to be density independent for every device.
    //This fixed a previous bug where larger devices couldn't correctly press areas on the screen
    //------------
    public static float dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // at this point the surface is created and
        // we can safely start the game loop
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

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
    }

    //Pressing the Back button >2.0 version. Rest of code on MainActivity
    public static void backpress(int countback, int titleback){
        counter = countback;
        titlebuttons = titleback;
    }

    //creating advertisement. Didn't work....


    //Pressing the Back button
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
		return true;
    }

    //Pressing on the screen
    @Override
    public boolean onTouchEvent(MotionEvent event) {

//        //------------------
//        //Information on Buttons
//        //50x50 pixels with a gap of 24 pixels
//        //That means +-12 from getwidth/2
//
//        //Comments to the right of the getWidth()/2 is what it should be according to how I made the artwork
//
//        //dip version
//        float dipblueleft =  dipToPixels(context, 86) ;   //86
//        float dipblueright =  dipToPixels(context, 136); //136
//        float dipyellowright = dipToPixels(context, 62); //62
//        float dipyellowleft = dipToPixels(context, 12); //12
//        float dipredright = dipToPixels(context, -12); //-12
//        float dipredleft = dipToPixels(context, -62); //-62
//        float dipwhiteright = dipToPixels(context, -86); //-86
//        float dipwhiteleft = dipToPixels(context, -136);
//
//        float blueleft = getWidth()/2 + dipblueleft;   //86
//        float blueright =  getWidth()/2 + dipblueright; //136
//        float yellowright = getWidth()/2+dipyellowright; //62
//        float yellowleft = getWidth()/2 + dipyellowleft; //12
//        float redright = getWidth()/2+dipredright; //-12
//        float redleft = getWidth()/2+dipredleft; //-62
//        float whiteright = getWidth()/2+dipwhiteright; //-86
//        float whiteleft = getWidth()/2+dipwhiteleft; //-136
//
//
//        //------------------
//        //Below is all code for every single finger press on areas of the screen
//        //This selects different colors and different areas of the screen
//
//        //-----------------------
//        //get pointer index from the event object
//        int pointerIndex = event.getActionIndex();
//
//        //get pointer ID
//        int pointerId = event.getPointerId(pointerIndex);
//
//        //get masked (not specific to a pointer) action
//        int maskedAction = event.getActionMasked();
//
//        int index = MotionEventCompat.getActionIndex(event);
//
//        if (event.getPointerCount() > 1) {
//            int xPos = (int) MotionEventCompat.getX(event, index);
//            int yPos = (int) MotionEventCompat.getY(event, index);
//
//            switch (maskedAction) {
//                case MotionEvent.ACTION_POINTER_DOWN: {
//
//                    //GREEN
//                    if (xPos > yellowleft && event.getX() > yellowleft && yPos > getHeight()-300) {
//                        gate = new Gate("green", BitmapFactory.decodeResource(getResources(), R.drawable.box_green), getWidth() / 2, getHeight() - 325);
//                    }
//
//                    //PURPLE
//                    if ((xPos > blueleft && event.getX() > redleft && event.getX() < redright && yPos > getHeight()-300) || (event.getX() > blueleft && xPos > redleft && xPos < redright && yPos > getHeight()-300)) {
//                        gate = new Gate("purple", BitmapFactory.decodeResource(getResources(), R.drawable.box_purple), getWidth() / 2, getHeight() - 325);
//                    }
//
//                    //ORANGE
//                    if (xPos < blueleft && xPos > redleft && event.getX() < blueleft && event.getX() > redleft && yPos > getHeight()-300) {
//                        gate = new Gate("orange", BitmapFactory.decodeResource(getResources(), R.drawable.box_orange), getWidth() / 2, getHeight() - 325);
//                    }
//
//                    //PINK
//                    if (xPos < redright && event.getX() < redright && yPos > getHeight()-300) {
//                        gate = new Gate("pink", BitmapFactory.decodeResource(getResources(), R.drawable.box_pink), getWidth() / 2, getHeight() - 325);
//                    }
//
//                    //LIGHT YELLOW
//                    if ((xPos > yellowleft && xPos < yellowright && event.getX() < whiteright && yPos > getHeight()-300) || (event.getX() > yellowleft && event.getX() < yellowright && xPos < whiteright && yPos > getHeight()-300)) {
//                        gate = new Gate("lightyellow", BitmapFactory.decodeResource(getResources(), R.drawable.box_lightyellow), getWidth() / 2, getHeight() - 325);
//                    }
//
//                    //SKY BLUE
//                    if ((xPos > blueleft && event.getX() < whiteright && yPos > getHeight()-300) || (event.getX() > blueleft && xPos < whiteright && yPos > getHeight()-300)) {
//                        gate = new Gate("skyblue", BitmapFactory.decodeResource(getResources(), R.drawable.box_skyblue), getWidth() / 2, getHeight() - 325);
//                    }
//                }
//            }
//
//
//        }
//        else {
//            //Single Touches
//            switch (maskedAction) {
//                case MotionEvent.ACTION_DOWN: {
//
//                    //Safe Close
////                    if (event.getY() < 200){
////                        thread.setRunning(false);
////                        ((Activity) getContext()).finish();
////                    }
//
//
//                    //WHITE
//                    if ((event.getY() > (getHeight() - 300)) && event.getX() < whiteright && event.getX() > whiteleft) {
//                        gate = new Gate("white", BitmapFactory.decodeResource(getResources(), R.drawable.box_white), getWidth() / 2, getHeight() - 325);
//                    }
//                    //RED
//                    if ((event.getY() > (getHeight() - 300)) && redleft < event.getX() && event.getX() < redright) {
//                        gate = new Gate("red", BitmapFactory.decodeResource(getResources(), R.drawable.box_red), getWidth() / 2, getHeight() - 325);
//                    }
//                    //BLUE
//                    if ((event.getY() > (getHeight() - 300)) && event.getX() > blueleft && event.getX() < blueright) {
//                        gate = new Gate("blue", BitmapFactory.decodeResource(getResources(), R.drawable.box_blue), getWidth() / 2, getHeight() - 325);
//                    }
//                    //YELLOW
//                    if ((event.getY() > (getHeight() - 300)) && yellowleft < event.getX() && event.getX() < yellowright) {
//                        gate = new Gate("yellow", BitmapFactory.decodeResource(getResources(), R.drawable.box_yellow), getWidth() / 2, getHeight() - 325);
//                    }
//                }
//                //MENU OPTIONS, ETC.
//                case MotionEvent.ACTION_UP:{
//                    //CLICK TO BEGIN GAME
//                    if (titlebuttons == 0 && counter == 0 && event.getX() > (getWidth()/2-100) && event.getX() < (getWidth()/2+100) && event.getY() > (getHeight()*2/5-100) && event.getY() < (getHeight()*2/5+100)){
//                        // create droid and load bitmap
//                        droid = new Droid(4, context, getWidth()/2, 0);
//                        // create gate and load bitmap
//                        gate = new Gate("start", BitmapFactory.decodeResource(getResources(), R.drawable.box_start), getWidth()/2, getHeight()-325);
//
//                        //Creating the "buttons"
//                        button = new Gate("white", BitmapFactory.decodeResource(getResources(), R.drawable.button), getWidth()/2, getHeight()-110);
//
//                        counter = 1;
//                        speedup = new Speed(0, dipToPixels(context, 5));
//                        droid.setSpeed(speedup);
//
//                    }
////                    //Go to instruction Screen
////                    if (counter == 0 && titlebuttons == 0  && event.getY() > (getHeight()/2-100) && event.getY() < (getHeight()/2+100) && event.getX() < (getWidth()*1/3+100)){
////                        paint.setTextSize(48);
////                        titlebuttons = 1;
////                        backbutton = new Gate("back", BitmapFactory.decodeResource(getResources(), R.drawable.back), getWidth()-150, getHeight()-150);
////                    }
//
////                    //Go to Settings
////                    if (counter == 0 && titlebuttons == 0  && event.getY() > (getHeight()/2-100) && event.getY() < (getHeight()/2+100) && event.getX() > (getWidth()*2/3-100)) {
////                        paint.setTextSize(48);
////                        titlebuttons = 6;
////                        backbutton = new Gate("back", BitmapFactory.decodeResource(getResources(), R.drawable.back), getWidth() - 150, getHeight() - 150);
////                    }
//
//                    //In Settings Screen - Delete High Score
////                    if (titlebuttons ==6 && event.getY() > getHeight()/2-350 && event.getY() < getHeight()/2-250){
////                        highscore = 0;
////                        counter = 0;
////                    }
//
//                    //In Settings Screen - Change Mode to Tablet/INSANE MODE
//                    if (titlebuttons ==6 && event.getY() > getHeight()/2-250 && event.getY() < getHeight()/2-150){
//                        counter = 0;
//                        tablet = true;
//                    }
//                    //In Settings Screen - Change Mode to Mobile MODE
//                    if (titlebuttons ==6 && event.getY() > getHeight()/2-150 && event.getY() < getHeight()/2-50){
//                        counter = 0;
//                        tablet = false;
//                    }
//
//                    //Return from instruction Screen
//                    if (titlebuttons ==1 && event.getY() > getHeight()-200 && event.getX() < getWidth()/2+100 && event.getX() > getWidth()/2-100){
//                        paint.setTextSize(64);
//                        titlebuttons = 0;
//
//
//                    }
//
//                    //Return from Settings Screen
//                    if (titlebuttons ==6 && event.getY() > getHeight()-200 && event.getX() > getWidth()-200){
//                        paint.setTextSize(64);
//                        titlebuttons = 0;
//                    }
//
//                    //Score Screen - RESTART Command
//                    if (titlebuttons ==2 && event.getY() > getHeight()/2+200 && event.getY() < getHeight()/2+400 && event.getX() < getWidth()/2-200) {
//
//                        droid = new Droid(4, context, getWidth()/2, 0);
//                        // create gate and load bitmap
//                        gate = new Gate("start", BitmapFactory.decodeResource(getResources(), R.drawable.box_start), getWidth()/2, getHeight()-325);
//
//                        //Creating the "buttons"
//                        button = new Gate("white", BitmapFactory.decodeResource(getResources(), R.drawable.button), getWidth()/2, getHeight()-110);
//
//                        titlebuttons = 0;
//                        counter = 1;
//
//                    }
//
//                    //Score Screen - HOME Command
//                    if (titlebuttons ==2 && event.getY() > getHeight()/2+200 && event.getY() < getHeight()/2+400 && event.getX() > getWidth()/2+200) {
//                        titlebuttons = 0;
//                        counter = 0;
//
//                    }
//
//                }
//            }
//        }
        return true;
    }

    //This draws everything. These are the different screens in the game
    public void render(Canvas canvas) {
//
//        //Shutting Down
//        if (titlebuttons == 5){
//            thread.setRunning(false);
//            ((Activity) getContext()).finish();
//        }

        //All cases when not being shut down - FRAME
//        if (titlebuttons != 5){
//            frame.draw(canvas);
//        }
//
//        if (titlebuttons != 5 && counter ==0){
//            canvas.drawColor(Color.WHITE);
//        }
//
//        //Changing background color based on the gate coming down
//        if (titlebuttons != 5 && counter >= 1) {
//			int inColor = droid.getDrawingColor();
//			float inAmount = 0.5f;
//
//			int backgroundColor = Color.argb(
//					Color.alpha(inColor),
//					(int)Math.min(255, Color.red(inColor) + 255 * inAmount),
//					(int)Math.min(255, Color.green(inColor) + 255 * inAmount),
//					(int)Math.min(255, Color.blue(inColor) + 255 * inAmount)
//			);
//
//			canvas.drawColor(backgroundColor);
//        }
//
//        //GAME SCREEN
//        if (counter >=1 && titlebuttons == 0){
//            button.draw(canvas);
//            droid.draw(canvas);
//            gate.draw(canvas);
//
//            canvas.drawText("Score: " +score + " High Score: " + highscore, getWidth()/2, 100, paint);
//            canvas.drawText("" +highscore, getWidth()/2+225, getHeight()-375, paint);
//        }
//        //SCORE SCREEN
//        if (titlebuttons == 2){
//            if (score == 0){
//                canvas.drawText("Oooh, not even a single one", getWidth()/2, getHeight()/2-200, paint);
//                canvas.drawText("Your score was " +score, getWidth()/2, getHeight()/2-100, paint);
//            }
//            if (score == highscore && score != 0) {
//                canvas.drawText("NEW HIGH SCORE!!!", getWidth()/2, getHeight()/2-200, paint);
//                canvas.drawText("Your score was " +score, getWidth()/2, getHeight()/2-100, paint);
//            }
//            if (score != 0 && score < highscore) {
//                canvas.drawText("Good Run!", getWidth()/2, getHeight()/2-200, paint);
//                canvas.drawText("Your score was " +score, getWidth()/2, getHeight()/2-100, paint);
//            }
//            canvas.drawText("RESTART", getWidth()/2-250, getHeight()/2+300, paint); //Need to make a button
//            canvas.drawText("HOME", getWidth()/2+250, getHeight()/2+300, paint); //need to make a button
//
//
//            //advertisement
//            if (adcount == 3){
//                //New Intent to activity_main? Is that the best approach?
//                adcount = 0;
//                Intent intent = new Intent(getContext(), AdActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                getContext().startActivity(intent);
//
//            }
//
//
//        }
//
//
        //SETTINGS SCREEN
//        if (titlebuttons == 6){
//            canvas.drawText("Reset High Score", getWidth()/2, getHeight()/2-300, paint);
//            backbutton.draw(canvas);
//            if (highscore == 0){
//                canvas.drawText("High Score has been Reset", getWidth()/2, getHeight()/2, paint);
//            }
//            if (tablet == false){
//                canvas.drawText("Mode: Mobile", getWidth()/2, getHeight()/2-200, paint);
//            }
//            if (tablet == true){
//                canvas.drawText("Mode: Tablet/INSANE MOBILE", getWidth()/2, getHeight() / 2 - 100, paint);
//                Games.Achievements.unlock(mGoogleApiClient, "CgkI-_LMrrYOEAIQAg");
//            }
//        }
    }
//
//    /**
//     * This is the game update method. It iterates through all the objects
//     * and calls their update method if they have one or calls specific
//     * engine's update method.
//     */
    public void update() {
//
//        //Board edge
//        //Buttons = 20px wide
//        //Player piece = 30px wide
//
////        if (counter >=1){
////            score = counter*100-100;
////            // check collision with bottom wall if heading down
////            if ((droid.getY() + droid.getBitmap().getHeight()>= getHeight()-275) && (droid.getColorMatch() != gate.Color())) {
////                Log.d(TAG, "GAME OVER");
////
////                //Set new High Score
////                if (score > highscore){
////                    highscore = score;
////                    //setting preferences
////                    HighScore.saveHighScore(context, highscore);
////                }
////                titlebuttons = 2;
////                adcount ++;
////                counter = 0;
////                level = 4;
////                randomizer = 0;
//////                thread.setRunning(false);
//////                ((Activity)getContext()).finish();
////            }
//
//            if ((droid.getY() + droid.getBitmap().getHeight()>= getHeight()-275) && (droid.getColorMatch() == gate.Color())) {
//                Log.d(TAG, "passed");
//                counter++;
//                Random r = new Random();
//                int i1 = r.nextInt(level - randomizer) + randomizer;
//                if (i1==0){
//                    droid = new Droid(1, context, getWidth()/2, 0);
//                }
//                if (i1==1){
//                    droid = new Droid(2, context, getWidth()/2, 0);
//                }
//                if (i1==2){
//                    droid = new Droid(3, context, getWidth()/2, 0);
//                }
//                if (i1==3){
//                    droid = new Droid(4, context, getWidth()/2, 0);
//                }
//				if (i1==7){
//					droid = new Droid(12, context, getWidth()/2, 0);
//				}
//				if (i1==8){
//					droid = new Droid(13, context, getWidth()/2, 0);
//				}
//				if (i1==9){
//					droid = new Droid(14, context, getWidth()/2, 0);
//				}
//				if (i1==6){
//					droid = new Droid(23, context, getWidth()/2, 0);
//				}
//				if (i1==5){
//					droid = new Droid(24, context, getWidth()/2, 0);
//				}
//				if (i1==4){
//					droid = new Droid(34, context, getWidth()/2, 0);
//				}
//            }
//            //Updating Speeds
//            float lvl2 = dipToPixels(context, 5);//Actually 6 and increment by 1
//            float lvl3 = dipToPixels(context, 6);
//            float lvl4 = dipToPixels(context, 7);
//            float lvl5 = dipToPixels(context, 8);
//            float lvl6 = dipToPixels(context, 9);
//            float lvl7 = dipToPixels(context, 11);
//
//            //Updating Speeds - Tablet
//            float lvl12 = dipToPixels(context, 12);//Actually 6 and increment by 1
//            float lvl13 = dipToPixels(context, 13);
//            float lvl14 = dipToPixels(context, 14);
//            float lvl15 = dipToPixels(context, 15);
//            float lvl16 = dipToPixels(context, 16);
//            float lvl17 = dipToPixels(context, 17);
//
//			AchievementsHelper helper = new AchievementsHelper(context);
//
//			//LEVEL 1
//            if (counter >= 0 && counter < 7){
//                level = 7;
//                randomizer = 2;
//                speedup = new Speed(0, lvl2);
//                if (tablet == true){
//                    speedup = new Speed(0,lvl12);
//                }
//                droid.setSpeed(speedup);
//            }
//            //LEVEL 2
//            if (counter >= 7 && counter < 18){
//                level = 10;
//                randomizer = 1;
//                speedup = new Speed(0,lvl3);
//                if (tablet == true){
//                    speedup = new Speed(0,lvl13);
//                }
//                droid.setSpeed(speedup);
//            }
//            //LEVEL 3
//            if (counter >= 18 && counter < 35){
//                randomizer = 0;
//                speedup = new Speed(0,lvl4);
//                if (tablet == true){
//                    speedup = new Speed(0,lvl14);
//                }
//                droid.setSpeed(speedup);
//				helper.unlockReachedLevel3();
//            }
//            //LEVEL 4
//            if (counter >= 35 && counter <50){
//                speedup = new Speed(0,lvl5);
//                if (tablet == true){
//                    speedup = new Speed(0,lvl15);
//                }
//                droid.setSpeed(speedup);
//            }
//            //LEVEL 5
//            if (counter >= 50 && counter <100){
//                speedup = new Speed(0,lvl6);
//                if (tablet == true){
//                    speedup = new Speed(0,lvl16);
//                }
//                droid.setSpeed(speedup);
//                helper.unlockReachedLevel5();
//            }
//            //LEVEL 6
//            if (counter >= 100){
//                speedup = new Speed(0,lvl7);
//                if (tablet == true){
//                    speedup = new Speed(0,lvl17);
//                }
//                droid.setSpeed(speedup);
//            }
//            //Last achievement
//            if (counter == 150){
//                helper.unlockReachedLevel7();
//            }
//
//            // Update the lone droid
//            droid.update();
//        }
    }
}