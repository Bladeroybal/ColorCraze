package com.twonamegames.colorcraze.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.twonamegames.colorcraze.R;

//Casey's Notes
//
//	This class is the screen that will be displayed when the user loses a game.
//	We will pass it the information from the game, including the final score,
//	how many blocks were hit, or anything else relevant. From here, we must start
//	the activity to navigate to, because the game activity would have called finish().
public class GameEndActivity extends Activity {
	Context context;

	Button replayButton;
	Button exitButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_end);

		context = this;

		replayButton = (Button) findViewById(R.id.replay_button);
		replayButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(context, GameActivity.class)
						.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
				overridePendingTransition(0, 0);
				finish();
			}
		});

		exitButton = (Button) findViewById(R.id.exit_button);
		exitButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(context, MainMenuActivity.class)
						.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
				overridePendingTransition(0, 0);
				finish();
			}
		});
	}
}
