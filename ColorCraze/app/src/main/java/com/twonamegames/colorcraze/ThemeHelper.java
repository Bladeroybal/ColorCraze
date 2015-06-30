package com.twonamegames.colorcraze;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;

/**
 * ThemeHelper is a class that helps us with all the colors and themes of the game.
 * To most easily and quickly access the colors at runtime, we treat the four
 * primary colors as four bits which can be either ON or OFF. Touching down on
 * the button during the game sets that color's particular bit to ON, and lifting
 * your finger sets that bit to OFF. The resulting bitset (the combination of
 * these bits either OFF or ON) creates an integer that represents that color that
 * should be displayed as the mixture of the pressed colors. This integer
 * corresponds directly to the location in an array of colors with the color that
 * should be the combination of the ones that are pressed.
 *
 * This scheme benefits us in several ways:
 * 	1) Colors are not represented as an arbitrary, unordered combination of the
 * 		buttons that are selected. It does not matter which buttons were pressed
 * 		in which order, or any previous state at all. If that button is pressed,
 * 		it is represented in the resulting color combination, guaranteed.
 * 	2) Colors are identified by the integer that creates it. So regardless of
 * 		the actual button location, but flag used to color that button is the flag
 * 		used to set the bit in the resulting integer, so the mixed color is always
 * 		exactly the mixture of the colors of the buttons pressed. The resulting
 * 		color is not related to the buttons position or anything.
 *	3) It allows for easy theming. Simply setting the primary colors and their
 *		mixtures in the theme XML will get pulled to this class, and those colors
 *		displayed at runtime. Eventually, I hope to calculate all color combinations
 *		given just four primary colors, so that combinations of 3 and 4 colors
 *		can be represented for added difficulty.
 *	4) It is super fast and super responsive, while being super small. Bitwise
 *		operators are as fast as they can get right down to the hardware, so we
 *		really can't speed up the color lookup any more than this. If we
 *		precalculate all colors, then, mixing at runtime is still simply addressing
 *		an array. And since each color is a single integer, we can store all the
 *		games active color information in just a few words in memory.
 */
public class ThemeHelper {
	Context context;
	private int[] themeColors, themeColorsLight;
	public int defaultColor = Color.WHITE;

	public ThemeHelper(Context context) {
		this.context = context;

		themeColors = new int[16];
		themeColorsLight = new int[16];

		TypedArray c = context.getTheme().obtainStyledAttributes(new int[]{
				R.attr.color1,
				R.attr.color2,
				R.attr.color3,
				R.attr.color4,
				R.attr.color12,
				R.attr.color13,
				R.attr.color14,
				R.attr.color23,
				R.attr.color24,
				R.attr.color34,});
		Resources r = context.getResources();

		//Load the game colors from the theme
		try {
			themeColors[0x0] = defaultColor; //0000
			themeColors[0x1] = c.getColor(0,  r.getColor(R.color.color1_pigment));  //0001
			themeColors[0x2] = c.getColor(1,  r.getColor(R.color.color2_pigment));  //0010
			themeColors[0x3] = c.getColor(4, r.getColor(R.color.color12_pigment)); //0011

			themeColors[0x4] = c.getColor(2,  r.getColor(R.color.color3_pigment));  //0100
			themeColors[0x5] = c.getColor(5, r.getColor(R.color.color13_pigment)); //0101
			themeColors[0x6] = c.getColor(7, r.getColor(R.color.color23_pigment)); //0110
			themeColors[0x7] = defaultColor; //0111

			themeColors[0x8] = c.getColor(3,  r.getColor(R.color.color4_pigment));  //1000
			themeColors[0x9] = c.getColor(6, r.getColor(R.color.color14_pigment)); //1001
			themeColors[0xA] = c.getColor(8, r.getColor(R.color.color24_pigment)); //1010
			themeColors[0xB] = defaultColor; //1011

			themeColors[0xC] = c.getColor(9, r.getColor(R.color.color23_pigment)); //1100
			themeColors[0xD] = defaultColor; //1101
			themeColors[0xE] = defaultColor; //1110
			themeColors[0xF] = defaultColor; //1111
		}
		finally {
			c.recycle();
		}

		//Calculate the lightened colors here. Alternatively, load colors from
		//theme exactly as above
		for(int i = 0; i < themeColors.length; i++) {
			int color = themeColors[i];
			float inAmount = 0.5f;

			themeColorsLight[i] = Color.argb(
					Color.alpha(color),
					(int)Math.min(255, Color.red(color) + 255 * inAmount),
					(int)Math.min(255, Color.green(color) + 255 * inAmount),
					(int)Math.min(255, Color.blue(color) + 255 * inAmount)
			);
		}
	}

	public int getColor(int colorId) {
		return (colorId >= 0 && colorId < themeColors.length) ? themeColors[colorId] : defaultColor;
	}

	public int getColorLight(int colorId) {
		return (colorId >= 0 && colorId < themeColorsLight.length) ? themeColorsLight[colorId] : defaultColor;
	}

	private int[] convertRgbToRyb(int rgb) {
		return null;
	}

	private int convertRybToRgb(int[] ryb) {
		return 0;
	}
}
