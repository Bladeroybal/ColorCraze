package com.twonamegames.colorcraze.activities;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.ads.InterstitialAd;
import com.twonamegames.colorcraze.R;

//adding these for ads
//------------------------------
//Original before changing found at:
//http://www.javacodegeeks.com/2011/07/android-game-development-displaying.html
//The above link is where I originally learned how to make game basics
//
//Below is all altered code to get Color Craze working properly
//------------------------------

//Casey's Notes
//
//	I commented all this out, but I can't remember why. I may have been getting a
//	crash on this activity and just decided to ignore ads for now.
public class AdActivity extends Activity {
    InterstitialAd mInterstitialAd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Give it a screen
        setContentView(R.layout.activity_main);

        //Trying out ads
//        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId("ca-app-pub-9744314937651224/7738580116");
    }

    @Override
    public void onResume(){
        super.onResume();
//        if (mInterstitialAd.isLoaded()){
//            mInterstitialAd.show();
//            mInterstitialAd.setAdListener(new AdListener() {
//                @Override
//                public void onAdClosed() {
//                    goBack();
//                }
//            });
//        }
//        else{
//            requestNewInterstitial();
//            MainGamePanel.backpress(0, 0);
//            Intent intent = new Intent(this, MainMenuActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            this.startActivity(intent);
//        }

    }

//    private void requestNewInterstitial(){
//        AdRequest adRequest = new AdRequest.Builder()
//                .build();
//        mInterstitialAd.loadAd(adRequest);
//    }

    private void goBack(){
		finish();
		overridePendingTransition(0, 0);
    }

    @Override
    public void onBackPressed(){
        goBack();
    }
}