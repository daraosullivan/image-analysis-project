package com.aidenkeating.imageanalysis.image;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Class to define a grouping that can also project itself onto an image.
 * @author aidenkeating
 *
 */
public class FirstLastPixelImageGrouping implements ImageGrouping {
	private int x1;
	private int y1;
	private int x2;
	private int y2;
	
	public FirstLastPixelImageGrouping(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	public int getX1() {
		return x1;
	}

	public int getY1() {
		return y1;
	}

	public int getX2() {
		return x2;
	}

	public int getY2() {
		return y2;
	}

	public void applyToImage(final BufferedImage image, final Color lineColor) {
		Graphics2D graphs = image.createGraphics();
		graphs.setColor(lineColor);
		graphs.drawRect(this.getX1(), this.getY1(), this.getX2() - this.getX1(), this.getY2() - this.getY1());
		graphs.dispose();
	}
	
	@Override
	public String toString() {
		return "ImageGrouping [x1=" + x1 + ", y1=" + y1 + ", x2=" + x2 + ", y2=" + y2 + "]";
	}
}
