package com.twonamegames.colorcraze;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

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

//    private MainThread thread;
//    private Droid droid;
//    private Speed speedup;
//    private Paint paint;
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
//        thread = new MainThread(getHolder(), this);
    }


    //Changing DIP TO Pixels
    //----------
    //Android code reads pixels as they are, however not all devices have same amount of pixels or pixel density.
    //This code converts all pixel calculations below in code to be density independent for every device.
    //This fixed a previous bug where larger devices couldn't correctly press areas on the screen
	//also supports converting a proportion of the screen as a number of pixels.
    //------------
    public float dpToPixels(float dp) {
        return context.getResources().getDisplayMetrics().density * dp;
    }

	public float widthFraction(float value) {
		return context.getResources().getDisplayMetrics().widthPixels * value;
	}

	public float heightFraction(float value) {
		return context.getResources().getDisplayMetrics().heightPixels * value;
	}

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // at this point the surface is created and
        // we can safely start the game loop
//        thread.setRunning(true);
//        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

//        titlebuttons = 5;
////        ((Activity) getContext()).finish();
//        // tell the thread to shut down and wait for it to finish
//        // this is a clean shutdown
//        boolean retry = true;
//        while (retry) {
//            try {
//                thread.join();
//                retry = false;
//            } catch (InterruptedException e) {
//                // try again shutting down the thread
//            }
//        }
    }


    /**
     * This is the game update method. It iterates through all the objects
     * and calls their update method if they have one or calls specific
     * engine's update method.
     */
    public void update() {

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