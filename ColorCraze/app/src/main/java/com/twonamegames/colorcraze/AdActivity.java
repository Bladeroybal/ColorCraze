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

        //Trying out ads
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-9744314937651224/7738580116");
    }

    @Override
    public void onStart(){
        super.onStart();
        if (mInterstitialAd.isLoaded()){
            mInterstitialAd.show();
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
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
        }

    }

    private void requestNewInterstitial(){
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mInterstitialAd.loadAd(adRequest);
    }

    private void goBack(){
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
    @Override
    public void onBackPressed(){
        MainGamePanel.backpress(0, 0);
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }


}