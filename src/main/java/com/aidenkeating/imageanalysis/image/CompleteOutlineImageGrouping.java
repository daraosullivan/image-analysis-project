package com.aidenkeating.imageanalysis.image;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

public class CompleteOutlineImageGrouping implements ImageGrouping {
	private final List<Pixel> pixels;

	public CompleteOutlineImageGrouping(final List<Pixel> pixels) {
		this.pixels = pixels;
	}

	@Override
	public void applyToImage(final BufferedImage image, final Color lineColor) {
		final Pixel topmostPixel = pixels.get(0);
		final Pixel bottommostPixel = pixels.get(this.pixels.size() - 1);
		Pixel leftmostPixel = topmostPixel;
		Pixel rightmostPixel = topmostPixel;

		for (final Pixel pixel : this.pixels) {
			if (pixel.getX() < leftmostPixel.getX()) {
				leftmostPixel = pixel;
			}
			if (pixel.getX() > rightmostPixel.getX()) {
				rightmostPixel = pixel;
			}
		}

		final Pixel outlineTopRight = new Pixel(leftmostPixel.getX(), topmostPixel.getY());
		final int outlineWidth = rightmostPixel.getX() - leftmostPixel.getX();
		final int outlineHeight = bottommostPixel.getY() - topmostPixel.getY();

		Graphics2D graphs = image.createGraphics();
		graphs.setColor(lineColor);
		graphs.drawRect(outlineTopRight.getX(), outlineTopRight.getY(), outlineWidth, outlineHeight);
		graphs.dispose();
	}

}
