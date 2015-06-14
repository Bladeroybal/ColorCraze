package com.twonamegames.colorcraze;

import android.app.Activity;
import android.content.Context;
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

public class AdActivity extends Activity {


    private static final String TAG = MainActivity.class.getSimpleName();
    InterstitialAd mInterstitialAd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requesting to turn the title OFF
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // making it full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Give it a screen
        setContentView(R.layout.activity_main);
        Log.d(TAG, "NEW SCREEN STARTED");

        //Trying out ads
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
//        requestNewInterstitial();

//        mInterstitialAd.show();
//        Log.d(TAG, "AD SHOWING");
//        mInterstitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                    requestNewInterstitial();
//                    MainGamePanel.backpress(0, 2);
//            }
//        });
        // making it full screen
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // set our MainGamePanel as the View
        //This if statement calls the advertisement banner located on activity_main.
        //I think I need a separate intent to call it properly since .advertisement doesn't work since it's static
        //The else statement is how the game runs

//            AdView mAdView = (AdView) findViewById(R.id.adView);
//            AdRequest adRequest = new AdRequest.Builder().build();
//            mAdView.loadAd(adRequest);
//            Log.d(TAG, "Ad loaded");
    }

    @Override
    public void onStart(){
        super.onStart();
        if (mInterstitialAd.isLoaded()){
            mInterstitialAd.show();
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    // requestNewInterstitial();
                    goBack();
                }
            });
        }
        else{
            requestNewInterstitial();
            MainGamePanel.backpress(0, 0);
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            this.startActivity(intent);
//            setContentView(new MainGamePanel(this));
            Log.d(TAG, "AD VIEW ADDED");
        }

    }

    private void requestNewInterstitial(){
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("C813B5AAD86536B97515EE8D75D4C3E6")
                .build();
        mInterstitialAd.loadAd(adRequest);
        Log.d(TAG, "Ad Requested");
    }

    private void goBack(){
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
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
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
        Log.d(TAG, "View added");
    }


}