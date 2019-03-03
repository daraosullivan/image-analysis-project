package com.aidenkeating.imageanalysis.geometry;

import java.awt.geom.Point2D;

/**
 * A simple representation of a point on a 2D plane that can be used to
 * construct other shapes.
 * 
 * This could be replaced with the Java AWT Point class.
 * 
 * @author aidenkeating
 */
public class Point {
	private final int x;
	private final int y;

	public Point(final int x, final int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public double distanceTo(final Point point) {
		return Point2D.distance(this.getX(), this.getY(), point.getX(), point.getY());
	}

	@Override
	public String toString() {
		return "Point [x=" + x + ", y=" + y + "]";
	}

	/**
	 * Euclidean translation of a provided Point.
	 * 
	 * @param point The point to translate
	 * @param x
	 * @param y
	 * @return A translated Point
	 */
	public static Point translate(final Point point, final int x, final int y) {
		return new Point(point.getX() + x, point.getY() + y);
	}
}
