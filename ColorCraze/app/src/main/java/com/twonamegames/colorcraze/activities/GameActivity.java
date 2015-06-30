package com.twonamegames.colorcraze.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.twonamegames.colorcraze.game.GameEventListener;
import com.twonamegames.colorcraze.game.GameSurface;
import com.twonamegames.colorcraze.R;
import com.twonamegames.colorcraze.ThemeHelper;
import com.twonamegames.colorcraze.game.GameMode;

//Casey's Notes
//
//	This class functions as the logic behind the entire game. It contains Views
//	for the buttons which give us updates about which button was pressed, as
//	well as the MainGamePanel which will be giving us updates about what is going
//	on with respect to what is currently displayed. It is the job on this class
//	to manage what happens when the buttons being pressed match the color falling,
//	what happens when a block hits the bottom, how many points to give, the current
//	level, which level to advance to, etc. Adding a new game mode will likely be
//	something along the lines of extending this class and starting that new activity
//	which handles the majority of the game content the same, but modifies a few
//	of the game's constraints.
//
//	The user cannot hit back to return to the main menu. Instead, if we want to
//	take the user back to the main menu, we must create an intent to start that
//	activity. Upon finishing the game, we should send the user to the GameEndActivity,
//	passing information such as the score, blocks hit, etc. so that we can display
//	that back to the user and ask what they want to do next. If they replay, they
//	simply call back to this activity, and everything should be automatically reset.
//
//	Also note that both the Activity implements GameEventListener, but that GameMode
//	also contains the methods onBlockDestroyed() and onBlockFailed() This is
//	because it will be useful for both to be aware of these events, such that
//	the Activity can manage scores and the passing of data around without
//	actually managing that data. Let the GameMode deal with when the game should
//	speed up, how much, etc. and just deal with drawing stuff on the screen and
//	handing input events.
public class GameActivity extends Activity implements GameEventListener {
	public static final int flag1=0x1;
	public static final int flag2=0x2;
	public static final int flag3=0x4;
	public static final int flag4=0x8;

	int flags;
	TextView level, blocksDestroyed,  score;
	ImageView button1, button2, button3, button4;
	GameSurface gameSurface;
	GameMode gameMode;
	ThemeHelper themeHelper;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		context = this;
		flags = 0;

		this.themeHelper = new ThemeHelper(context);

		level = (TextView) findViewById(R.id.level);
		blocksDestroyed = (TextView) findViewById(R.id.blocks_destroyed);
		score = (TextView) findViewById(R.id.score);

		//find all the buttons
		button1 = (ImageView) findViewById(R.id.button1);
		button2 = (ImageView) findViewById(R.id.button2);
		button3 = (ImageView) findViewById(R.id.button3);
		button4 = (ImageView) findViewById(R.id.button4);

		//color all the buttons
		button1.setColorFilter(new PorterDuffColorFilter(themeHelper.getColor(flag1), PorterDuff.Mode.MULTIPLY));
		button2.setColorFilter(new PorterDuffColorFilter(themeHelper.getColor(flag2), PorterDuff.Mode.MULTIPLY));
		button3.setColorFilter(new PorterDuffColorFilter(themeHelper.getColor(flag3), PorterDuff.Mode.MULTIPLY));
		button4.setColorFilter(new PorterDuffColorFilter(themeHelper.getColor(flag4), PorterDuff.Mode.MULTIPLY));

		//detect touches on all the buttons
		button1.setOnTouchListener(buttonTouchedListener);
		button2.setOnTouchListener(buttonTouchedListener);
		button3.setOnTouchListener(buttonTouchedListener);
		button4.setOnTouchListener(buttonTouchedListener);

		//set initial color of mixer and background, to ensure it initializes as we expect
		findViewById(R.id.pause_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(context, GameEndActivity.class));
				overridePendingTransition(0, 0);
				finish();
			}
		});

		gameMode = new GameMode(this, GameMode.CLASSIC);

		gameSurface = (GameSurface) findViewById(R.id.gamePanel);
		gameSurface.setGameEventListener(this);

		this.onGameStart();
	}

	@Override
	protected void onResume() {
		super.onResume();

		if(gameSurface != null) {
			gameSurface.resume();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

		if(gameSurface != null) {
			gameSurface.pause();
		}
	}

	private View.OnTouchListener buttonTouchedListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			int maskedAction = event.getActionMasked();

			//set a bit flag based on which button was pressed
			if(maskedAction == MotionEvent.ACTION_DOWN) {
				switch(v.getId()) {
				case R.id.button1:
					flags |= flag1;
					break;
				case R.id.button2:
					flags |= flag2;
					break;
				case R.id.button3:
					flags |= flag3;
					break;
				case R.id.button4:
					flags |= flag4;
					break;
				}
			}
			else if(maskedAction == MotionEvent.ACTION_UP) {
				switch(v.getId()) {
				case R.id.button1:
					flags &= ~flag1;
					break;
				case R.id.button2:
					flags &= ~flag2;
					break;
				case R.id.button3:
					flags &= ~flag3;
					break;
				case R.id.button4:
					flags &= ~flag4;
					break;
				}
			}

			gameSurface.setSelectedColor(flags);

			return true;
		}
	};

	//Pressing the Back button
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		return (keyCode == KeyEvent.KEYCODE_BACK);
	}

	//since the GameSurface is running on another thread, these callbacks are also
	//on another thread. The Android Views must all be drawn on the UI thread of the
	//app, so we have to explicity tie these back to the UI thread so that we
	//can do whatever we need to.
	@Override
	public void onBlockDestroyed(final float progress) {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				gameMode.onBlockDestroyed(progress);
				level.setText("Level " + gameMode.getLevel());
				blocksDestroyed.setText("Blocks Destroyed: " + gameMode.getBlocksDestroyed());
				score.setText("Score: " +gameMode.getScore());
			}
		});
	}

	@Override
	public void onBlockFailed() {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				gameMode.onBlockFailed();
				level.setText("Level " + gameMode.getLevel());
				blocksDestroyed.setText("Blocks Destroyed: " + gameMode.getBlocksDestroyed());
				score.setText("Score: " + gameMode.getScore());
			}
		});
	}

	@Override
	public void onSpeedChanged(float newSpeed) {
		gameSurface.setSpeed(newSpeed);
	}

	@Override
	public void onGameStart() {
		final Context context = this;
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(context, "Game Started", Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public void onGamePaused() {
		final Context context = this;
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(context, "Game Paused", Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public void onGameFinished() {
		final Context context = this;
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(context, "Game Finished", Toast.LENGTH_SHORT).show();

				Bundle bundle = gameMode.getGameStats();

				startActivity(new Intent(context, GameEndActivity.class).putExtras(bundle));
				overridePendingTransition(0, 0);
				finish();
			}
		});
	}
}
