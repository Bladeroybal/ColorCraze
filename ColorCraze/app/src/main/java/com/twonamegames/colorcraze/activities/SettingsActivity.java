package com.twonamegames.colorcraze.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.twonamegames.colorcraze.AchievementsHelper;
import com.twonamegames.colorcraze.HighScoreHelper;
import com.twonamegames.colorcraze.R;

//Casey's Notes
//
//	This class is the Settings screen. From here the user can do things like change
//	the theme, enable colorblind mode, reset their high score, view achievements,
//	etc. Most of the UI should be defined entirely in XML. Hitting the back button
//	//or the back arrow should take the user back to the main menu.
public class SettingsActivity extends Activity {
	Context context;

	ImageView backButton;
	Button resetHighScoreButton;
	Button tabletModeButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = this;
		setContentView(R.layout.activity_settings);

		backButton = (ImageView) findViewById(R.id.back_button);
		backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(0, 0);
			}
		});

		resetHighScoreButton = (Button) findViewById(R.id.reset_high_score_button);
		resetHighScoreButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				HighScoreHelper.saveHighScore(context, 0);
				HighScoreHelper.saveCounter(context, 0);
			}
		});

		tabletModeButton = (Button) findViewById(R.id.tablet_mode_button);
		tabletModeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AchievementsHelper helper = new AchievementsHelper(context);
				helper.unlockFoundTabletMode();
			}
		});
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
