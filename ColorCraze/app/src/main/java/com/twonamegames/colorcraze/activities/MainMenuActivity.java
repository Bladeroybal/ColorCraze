package com.twonamegames.colorcraze.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.twonamegames.colorcraze.HighScoreHelper;
import com.twonamegames.colorcraze.R;

//Casey's Notes
//
//	This class is the Main Menu. It routes the user to whichever parts of the app
//	it needs to. It is important which click events call finish() and which
//	don't: the ones that do not call finish expect the user to hit back and return
//	here. The ones that don't expect that the user cannot hit back to return here,
//	such as while playing the game. Thus, we end this activity so that the other
//	activities can then start this activity when they need to according to their
//	lifecycle.
public class MainMenuActivity extends Activity {
	Context context;
	ImageView helpButton;
	ImageView settingsButton;
	ImageView playButton;

	TextView highScore;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main_menu);

		context = this;

		helpButton = (ImageView) findViewById(R.id.help);
		helpButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(context, HelpActivity.class));
				overridePendingTransition(0, 0);
			}
		});

		settingsButton = (ImageView) findViewById(R.id.settings);
		settingsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(context, SettingsActivity.class));
				overridePendingTransition(0, 0);
			}
		});

		playButton = (ImageView) findViewById(R.id.play);
		playButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(context, GameActivity.class));
				overridePendingTransition(0, 0);
				finish();
			}
		});

		highScore = (TextView) findViewById(R.id.high_score);
		highScore.setText("High Score " + HighScoreHelper.getHighScore(context));
	}
}
