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
import android.widget.Toast;

import com.twonamegames.colorcraze.GameEventListener;
import com.twonamegames.colorcraze.GameSurface;
import com.twonamegames.colorcraze.R;
import com.twonamegames.colorcraze.ThemeUtil;

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
public class GameActivity extends Activity implements GameEventListener {
	public static final int flag1=0x1;
	public static final int flag2=0x2;
	public static final int flag3=0x4;
	public static final int flag4=0x8;

	int flags;
	ImageView button1, button2, button3, button4;
	GameSurface gameSurface;
	ThemeUtil themeUtil;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		context = this;
		flags = 0;

		this.themeUtil = new ThemeUtil(context);

		//find all the buttons
		button1 = (ImageView) findViewById(R.id.button1);
		button2 = (ImageView) findViewById(R.id.button2);
		button3 = (ImageView) findViewById(R.id.button3);
		button4 = (ImageView) findViewById(R.id.button4);

		//color all the buttons
		button1.setColorFilter(new PorterDuffColorFilter(themeUtil.getColor(flag1), PorterDuff.Mode.MULTIPLY));
		button2.setColorFilter(new PorterDuffColorFilter(themeUtil.getColor(flag2), PorterDuff.Mode.MULTIPLY));
		button3.setColorFilter(new PorterDuffColorFilter(themeUtil.getColor(flag3), PorterDuff.Mode.MULTIPLY));
		button4.setColorFilter(new PorterDuffColorFilter(themeUtil.getColor(flag4), PorterDuff.Mode.MULTIPLY));

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

		gameSurface = (GameSurface) findViewById(R.id.gamePanel);
		gameSurface.setGameEventListener(this);
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
	public void onBlockDestroyed(float progress) {
		final Context context = this;
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(context, "Block destroyed", Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public void onBlockFailed() {
		final Context context = this;
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(context, "Block failed", Toast.LENGTH_SHORT).show();
			}
		});
	}
}
