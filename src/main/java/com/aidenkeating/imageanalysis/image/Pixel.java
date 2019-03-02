package com.aidenkeating.imageanalysis.image;

/**
 * A simple wrapper for the coordinates of a pixel in an image, to allow for
 * expansion at a later date if needed.
 * 
 * @author aidenkeating
 */
public class Pixel {
	private int x;
	private int y;

	public Pixel(final int x, final int y) {
		this.x = (x < 0 ? 0 : x);
		this.y = (y < 0 ? 0 : y);
	}

	// Generated.
	public int getX() {
		return x;
	}

	// Generated.
	public int getY() {
		return y;
	}

	// Generated.
	public void setX(int x) {
		this.x = x;
	}

	// Generated.
	public void setY(int y) {
		this.y = y;
	}
}
