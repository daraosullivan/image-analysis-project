package com.aidenkeating.imageanalysis.geometry;

/**
 * A simple rectange class including some helper functions for image analysis.
 * This could be replaced with the Java AWT Rectangle class, but it would
 * require some extensions to include the functionality we have here.
 * 
 * @author aidenkeating
 */
public class Rectangle {
	private Point topLeft;
	private int width;
	private int height;

	public Rectangle(final Point topLeft, final int width, final int height) {
		this.topLeft = topLeft;
		this.width = (width < 0 ? 0 : width);
		this.height = (height < 0 ? 0 : height);
	}

	// Generated.
	public int getWidth() {
		return width;
	}

	// Generated.
	public int getHeight() {
		return height;
	}

	public Point getTopLeft() {
		return this.topLeft;
	}

	public Point getBottomLeft() {
		return Point.translate(this.getTopLeft(), 0, this.getHeight());
	}

	public Point getTopRight() {
		return Point.translate(this.getTopLeft(), this.getWidth(), 0);
	}

	public Point getBottomRight() {
		return Point.translate(this.getTopLeft(), this.getWidth(), this.getHeight());
	}

	/**
	 * Whether this Rectangle contains a specified Rectangle.
	 * 
	 * @param rect The Rectangle to compare against
	 * @return If the specified Rectangle is inside this Rectangle
	 */
	public boolean contains(final Rectangle rect) {
		return this.getTopLeft().getX() < rect.getTopLeft().getX()
				&& this.getTopLeft().getY() < rect.getTopLeft().getY()
				&& (this.getBottomRight().getX() > rect.getBottomRight().getX())
				&& (this.getBottomRight().getY() > rect.getBottomRight().getY());
	}

	public double distanceTo(final Rectangle rect) {
		// Whether rect is above this Rectangle.
		final boolean isAbove = rect.getBottomRight().getY() < this.getTopLeft().getY();
		final boolean isBelow = rect.getTopLeft().getY() > this.getBottomRight().getY();
		final boolean isLeft = rect.getBottomRight().getX() < this.getTopLeft().getX();
		final boolean isRight = rect.getTopLeft().getX() > this.getBottomRight().getX();

		if (isAbove && isLeft) {
			return this.getTopLeft().distanceTo(rect.getBottomRight());
		}
		if (isAbove && isRight) {
			return this.getTopRight().distanceTo(rect.getBottomLeft());
		}
		if (isBelow && isLeft) {
			return this.getBottomLeft().distanceTo(rect.getTopRight());
		}
		if (isBelow && isRight) {
			return this.getBottomRight().distanceTo(rect.getTopLeft());
		}
		if (isAbove) {
			return this.getTopLeft().getY() - rect.getBottomLeft().getY();
		}
		if (isBelow) {
			return rect.getTopLeft().getY() - this.getBottomLeft().getY();
		}
		if (isLeft) {
			return this.getTopLeft().getX() - rect.getTopRight().getX();
		}
		if (isRight) {
			return rect.getTopLeft().getX() - this.getTopRight().getX();
		}
		// They intersect.
		return 0;
	}
}
