package com.twonamegames.colorcraze;

import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;

//Casey's Notes
//
//	Like the HighScoreHelper class, this class just ensures a consistent way to
//	access the Google Play Games acheivement framework. We will always sign in
//	the same, and it makes it very easy to see which achievements we have available
//	or add new ones.
public class AchievementsHelper {
	GoogleApiClient mGoogleApiClient;
	Context context;

	public AchievementsHelper(Context context) {
		this.context = context;

//		GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(context)
//				.addApi(Plus.API)
//				.addScope(Plus.SCOPE_PLUS_LOGIN)
//				.setAccountName("bladeroybal@gmail.com")
//				.build();
//		mGoogleApiClient.connect();
	}

	public void unlockSeekingHelp() {
//		Games.Achievements.unlock(mGoogleApiClient, "CgkI-_LMrrYOEAIQAQ");
	}

	public void unlockFoundTabletMode() {
//		Games.Achievements.unlock(mGoogleApiClient, "CgkI-_LMrrYOEAIQAg");
	}

	public void unlockReachedLevel3() {
//		Games.Achievements.unlock(mGoogleApiClient, "CgkI-_LMrrYOEAIQAw");
	}

	public void unlockReachedLevel5() {
//		Games.Achievements.unlock(mGoogleApiClient, "CgkI-_LMrrYOEAIQBA");
	}

	public void unlockReachedLevel7() {
//		Games.Achievements.unlock(mGoogleApiClient, "CgkI-_LMrrYOEAIQBQ");
	}
}
