package com.twonamegames.colorcraze.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.twonamegames.colorcraze.ThemeHelper;

import java.util.Calendar;
import java.util.Random;

public class GameSurface extends SurfaceView implements Runnable {
//Members
//------------------------------------------------------------------------------
	boolean isRunning;
	Thread thread;
	SurfaceHolder holder;
	Context context;
	Paint paint;
	GameEventListener listener;

	float currentPosition;
	float pointOfFailure;

	ThemeHelper themeHelper;
	Random random;
	int blockColor;
	int selectedColor;
	float speed;

	//Constructors
//------------------------------------------------------------------------------
	public GameSurface(Context context) {
		super(context);

		initialize(context);
	}

	public GameSurface(Context context, AttributeSet attrs) {
		super(context, attrs);

		initialize(context);
	}

	public GameSurface(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		initialize(context);
	}

//Lifecycle
//------------------------------------------------------------------------------
	public void initialize(Context context) {
		this.context = context;
		this.holder = getHolder();

		this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		this.themeHelper = new ThemeHelper(context);
		random = new Random(Calendar.getInstance().getTimeInMillis());
		getNextBlockColor();
		speed = 1.0f;
	}

	public void setGameEventListener(GameEventListener listener) {
		this.listener = listener;
	}

	public void pause() {
		isRunning = false;
		if(thread != null) {
			while(true) {
				try {
					thread.join();
				}
				catch(InterruptedException e) {
					e.printStackTrace();
				}

				break;
			}

			thread = null;
		}

	}

	public void resume() {
		isRunning = true;

		thread = new Thread(this);
		thread.start();
	}

//Running and drawing logic
//------------------------------------------------------------------------------

	// desired fps
	private final static int MAX_FPS = 50;
	// maximum number of frames to be skipped
	private final static int MAX_FRAME_SKIPS = 3;
	// the frame period
	private final static int FRAME_PERIOD = 1000 / MAX_FPS;

	@Override
	public void run() {
		while(isRunning) {
			if(!holder.getSurface().isValid()) {
				continue;
			}

			Canvas canvas = holder.lockCanvas();
			try {
				long beginTime = System.currentTimeMillis();
				int framesSkipped = 0;

				drawFrame(canvas);

				long timeDiff = System.currentTimeMillis() - beginTime;
				// calculate sleep time
				long sleepTime = FRAME_PERIOD - timeDiff;

				if(sleepTime > 0) {
					// if sleepTime > 0 we're OK
					try {
						// send the thread to sleep for a short period
						// very useful for battery saving
						Thread.sleep(sleepTime);
					}
					catch(InterruptedException e) {
					}
				}

				while(sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS) {
					// we need to catch up
					// update without rendering
					drawFrame(canvas);

					sleepTime += FRAME_PERIOD; // add frame period to check if in next frame
					framesSkipped++;
				}
			}
			finally {
				holder.unlockCanvasAndPost(canvas);
			}
		}
	}

	private void drawFrame(Canvas canvas) {
		pointOfFailure = canvas.getHeight() * 0.9f;

		//update the position first to ensure that whatever is drawn on screen
		//for that frame is what is represented by the data. We don't want the
		//possibility for an event to go off while it looks like the it hasn't yet
		updatePosition(canvas);

		clearCanvas(canvas);

		//choose either block or filled arc by commenting out the other. Could
		//always draw a Drawable here instead (if so, let's create another method
		//for that so that we preserve the filled curve and the box).
		drawFilledArc(canvas);
//		drawBlock(canvas);

		drawMixer(canvas);
		drawPointOfFailure(canvas);

		checkForEvents(canvas);
	}

	/**
	 * update the numbers for the position of the block to be drawn. No events
	 * are fired at this time, since the screen does not yet reflect these changes
	 */
	public void updatePosition(Canvas canvas) {
		float dy = (speed)*(0.01f)*(canvas.getHeight());
		currentPosition = currentPosition + dy;

		Log.i("GmaeSurface", "%" + dy / canvas.getHeight());
	}

	/**
	 * ensure that the canvas is blank before drawing to it again, so that we
	 * dont accidentally show artifacts from the previous frame
	 */
	private void clearCanvas(Canvas canvas) {
		paint.setColor(themeHelper.getColorLight(selectedColor));
		canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);
	}

	/**
	 * draw a fixed-height block to drop from the top of the screen
	 */
	private void drawBlock(Canvas canvas) {
		paint.setColor(themeHelper.getColor(blockColor));
		canvas.drawRect(0, currentPosition - canvas.getHeight()*0.2f, canvas.getWidth(), currentPosition, paint);
	}

	/**
	 * draw a curve where everything above the curve has been filled in, as if
	 * paint of something is being poured down the screen
	 */
	private void drawFilledArc(Canvas canvas) {
		RectF rect = new RectF();
		rect.left = 0;
		rect.right = canvas.getWidth();
		rect.top = currentPosition - canvas.getHeight()*0.1f; //change depth of curve by altering float value
		rect.bottom = currentPosition;

		paint.setColor(themeHelper.getColor(blockColor));
		canvas.drawRect(0, 0, canvas.getWidth(), currentPosition - rect.height()/2, paint);
		canvas.drawArc(rect, 0, 180, false, paint);
	}

	private void drawMixer(Canvas canvas) {
		paint.setColor(themeHelper.getColor(selectedColor));
		canvas.drawRect(0, pointOfFailure, canvas.getWidth(), canvas.getHeight(), paint);
	}

	/**
	 * draw a line which represents the point at which a block is considered to
	 * have reached the bottom, thus sending an event to the Activity of the failure
	 */
	private void drawPointOfFailure(Canvas canvas) {
		paint.setColor(Color.BLACK);
		canvas.drawRect(0, pointOfFailure, canvas.getWidth(), pointOfFailure + dpToPixels(1), paint);
	}

	/**
	 * Now that the screen reflects the numbers inside, sent any necessary events
	 * to the Activity to be handled
	 */
	private void checkForEvents(Canvas canvas) {
		if(currentPosition >= pointOfFailure) {
			if(selectedColor == blockColor) {
				listener.onBlockDestroyed(0);
			}
			else {
				listener.onBlockFailed();
			}

			currentPosition = 0;
			getNextBlockColor();
		}
	}

	private void getNextBlockColor() {
		int temp = blockColor;

		//ensures that we don't get two blocks of the same color in a row, and
		//that the color is actually defined
		while(temp == blockColor || themeHelper.getColor(temp) == themeHelper.defaultColor) {
			temp = random.nextInt(24);
		}

		blockColor = temp;
	}

//Helper methods
//------------------------------------------------------------------------------
	//ultimately, DP are nice to use for an app's interface, but for rendering a
	//game that we want to work exactly the same on all devices, we better use
	//percentages of screen width instead. This way, we are guaranteed to not
	//have a certain device be easier to play on because it has more DPs than
	//another, etc. If it takes 1 second to advance half the screen, it will
	//seem faster or slower on different devices, but ultimately not change the
	//difficulty in any way because the actual time to advance that distance
	//will be constant
	public float dpToPixels(float dp) {
		return context.getResources().getDisplayMetrics().density * dp;
	}

	public void setSelectedColor(int colorId) {
		selectedColor = colorId;
	}

	/**
	 * Set speed as a multiplier, where 1x = 1% of the screen per frame, 2x = 2%, etc.
	 *
	 * @param speed
	 */
	public void setSpeed(float speed) {
		this.speed = speed;
	}
}
