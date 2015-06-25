package com.twonamegames.colorcraze;

import android.content.Context;

//Casey's Notes
//
//	This class is a helper for saving and retrieving the high score. It is basically
//	a wrapper around SharedPreferences and doesn't do anything expect ensure that
//	we access this data consistently. For example, we no longer need to remember
//	which SharedPReferences file we are working with, and which key is the data
//	we want to access. We just ask for the high score, and it will be the same
//	every time.
public class HighScoreHelper {
	public static int getHighScore(Context context) {
		return context.getSharedPreferences("color_craze_prefs", Context.MODE_PRIVATE)
				.getInt("high_score", 0);
	}

	public static void saveHighScore(Context context, int score) {
		context.getSharedPreferences("color_craze_prefs", Context.MODE_PRIVATE)
				.edit().putInt("high_score", score).commit();
	}

	public static int getCounter(Context context) {
		return context.getSharedPreferences("color_craze_prefs", Context.MODE_PRIVATE)
				.getInt("counter", 0);
	}

	public static void saveCounter(Context context, int counter) {
		context.getSharedPreferences("color_craze_prefs", Context.MODE_PRIVATE)
				.edit().putInt("counter", counter).commit();
	}
}
