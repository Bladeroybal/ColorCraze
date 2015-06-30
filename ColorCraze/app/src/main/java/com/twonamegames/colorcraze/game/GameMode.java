package com.twonamegames.colorcraze.game;

import android.app.Activity;
import android.os.Bundle;

public class GameMode {
	private int level;
	private int score;
	private final String mode;
	private Activity activity;
	private int blocksDestroyed;
	private float speed;
	GameEventListener listener;

	public GameMode(Activity activity, String mode) {
		this.activity = activity;
		this.mode = mode;

		listener = (GameEventListener) activity;

		score = 0;
		level = 1;
		blocksDestroyed = 0;
		speed = 1.0f;
	}

	/**
	 * Get the rate at which blocks should advance down the screen, in terms
	 * of px/frame.
	 *
	 * @return speed to move blocks in px/frame
	 */
	public float getSpeed() {
		return speed;
	}

	public int getLevel() {
		return level;
	}

	public int getBlocksDestroyed() {
		return blocksDestroyed;
	}

	public int getScore() {
		return score;
	}

	public void onBlockDestroyed(float progress) {
		blocksDestroyed++;

		score += 100 + 25*level;

		updateSpeed();
	}

	public void onBlockFailed() {
		if(mode.equals(CLASSIC)) {
			listener.onGameFinished();
		}
	}

	//TODO: Change these numbers, make math functions to represent the difficulty curve
	//	We will want to play with these numbers. More accurately, we will want to
	//	find a mathematical model that represents a good difficulty curve here,
	//	both in terms of how often to increase the level, and of the rate to
	//	increase it to and top off the speed. I think we should strive for a game
	//	that is quick, on average less than 30 seconds, but with potential for games
	//	lasting many minutes given enough skill. So the final speed should be fast,
	//	fast enough to trip up novice players for quite a while, but not so fast
	//	that it is just impossible. This makes the end game less a game of "play
	//	until the program decides you can no longer keep up" and more a game
	//	of "play until you reach the highest level, then survive as long as you
	//	can until you get tired or lose focus for even a split second".
	private void updateSpeed() {
		if(blocksDestroyed == 7) {
			level = 2;
			listener.onSpeedChanged(1.2f);
		}
		else if(blocksDestroyed == 18) {
			level = 3;
			listener.onSpeedChanged(1.5f);
		}
		else if(blocksDestroyed == 35) {
			level = 4;
			listener.onSpeedChanged(2.0f);
		}
		else if(blocksDestroyed == 50) {
			level = 5;
			listener.onSpeedChanged(3.0f);
		}
		else if(blocksDestroyed == 100) {
			level = 6;
			listener.onSpeedChanged(3.5f);
		}
		else if(blocksDestroyed == 150) {
			level = 7;
			listener.onSpeedChanged(4.0f);
		}
	}

	public Bundle getGameStats() {
		Bundle bundle = new Bundle();
		bundle.putInt(BLOCKS_DESTROYED, blocksDestroyed);
		bundle.putInt(SCORE, score);
		bundle.putInt(LEVEL, level);
		bundle.putString(MODE, mode);

		return bundle;
	}

	public static final String BLOCKS_DESTROYED = "BLOCKS_DESTROYED";
	public static final String SCORE = "SCORE";
	public static final String LEVEL = "LEVEL";
	public static final String MODE = "MODE";

	public static final String CLASSIC = "CLASSIC";
	public static final String ENDLESS = "ENDLESS";
}
