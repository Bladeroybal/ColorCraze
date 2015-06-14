package com.twonamegames.colorcraze;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
//adding these for ads
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
//------------------------------
//Original before changing found at:
//http://www.javacodegeeks.com/2011/07/android-game-development-displaying.html
//The above link is where I originally learned how to make game basics
//
//Below is all altered code to get Color Craze working properly
//------------------------------

public class MainActivity extends Activity {


    private static final String TAG = MainActivity.class.getSimpleName();
//    InterstitialAd mInterstitialAd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "ACTIVITY CREATED");
        // requesting to turn the title OFF
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // making it full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // set our MainGamePanel as the View
        //This if statement calls the advertisement banner located on activity_main.
        //I think I need a separate intent to call it properly since .advertisement doesn't work since it's static
        //The else statement is how the game runs

    }

    @Override
    public void onStart(){
        super.onStart();
        //Loading
        setContentView(new MainGamePanel(this));
        Log.d(TAG, "View added");
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "Destroying...");
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "Stopping...");
        super.onStop();
    }
    @Override
    public void onBackPressed(){
        Log.d(TAG, "KEY PRESSED: BACK");
        MainGamePanel.backpress(0, 0);
    }


}