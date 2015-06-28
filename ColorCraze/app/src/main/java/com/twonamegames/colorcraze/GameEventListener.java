package com.twonamegames.colorcraze;

public interface GameEventListener {
	/**
	 * Called whenever a block has been destroyed
	 *
	 * @param progress how far down the screen the block had travelled before being destroyed
	 */
	public void onBlockDestroyed(float progress);

	/**
	 * Called whenever a block hits the point of failure before being destroyed
	 */
	public void onBlockFailed();
}
