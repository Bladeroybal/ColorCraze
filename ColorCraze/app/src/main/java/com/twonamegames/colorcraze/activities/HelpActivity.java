package com.twonamegames.colorcraze.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;

import com.twonamegames.colorcraze.AchievementsHelper;
import com.twonamegames.colorcraze.R;

//Casey's Notes
//
//	This class is the help screen. It will likely just be a static class displaying
//	an XML layout. Hitting the back buttons takes us back to the MainMenuActivity.
public class HelpActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_help);

		AchievementsHelper helper = new AchievementsHelper(this);
		helper.unlockSeekingHelp();
	}

	//Pressing the Back button
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if (keyCode == KeyEvent.KEYCODE_BACK){
			finish();
			overridePendingTransition(0, 0);
			return true;
		}
		else {
			return false;
		}
	}
}
