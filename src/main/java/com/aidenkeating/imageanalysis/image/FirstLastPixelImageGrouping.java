package com.aidenkeating.imageanalysis.image;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Class to define a grouping that can also project itself onto an image.
 * 
 * @author aidenkeating
 *
 */
public class FirstLastPixelImageGrouping implements ImageGrouping {
	private Pixel firstPixel;
	private Pixel lastPixel;

	public FirstLastPixelImageGrouping(final List<Pixel> pixels) {
		final Pixel firstPixel = pixels.get(0);
		final Pixel lastPixel = pixels.get(pixels.size() - 1);

		if (lastPixel.getX() < firstPixel.getX()) {
			final int tempX = firstPixel.getX();
			firstPixel.setX(lastPixel.getX());
			lastPixel.setX(tempX);
		}
		this.firstPixel = firstPixel;
		this.lastPixel = lastPixel;
	}

	public void applyToImage(final BufferedImage image, final Color lineColor) {
		final int outlineWidth = this.lastPixel.getX() - this.firstPixel.getX();
		final int outlineHeight = this.lastPixel.getY() - this.firstPixel.getY();

		Graphics2D graphs = image.createGraphics();
		graphs.setColor(lineColor);
		graphs.drawRect(this.firstPixel.getX(), this.firstPixel.getY(), outlineWidth, outlineHeight);
		graphs.dispose();
	}
}
