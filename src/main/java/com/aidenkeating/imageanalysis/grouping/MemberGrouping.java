package com.aidenkeating.imageanalysis.grouping;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import com.aidenkeating.imageanalysis.geometry.Point;
import com.aidenkeating.imageanalysis.geometry.Rectangle;
import com.aidenkeating.imageanalysis.image.ImageUtil;
import com.aidenkeating.imageanalysis.image.Pixel;

public class MemberGrouping implements ImageGrouping {
	private Pixel firstPixel;
	private Pixel lastPixel;

	public MemberGrouping(final List<Pixel> pixels) {
		final Pixel topmostPixel = pixels.get(0);
		final Pixel bottommostPixel = pixels.get(pixels.size() - 1);
		Pixel leftmostPixel = topmostPixel;
		Pixel rightmostPixel = topmostPixel;

		for (final Pixel pixel : pixels) {
			if (pixel.getX() < leftmostPixel.getX()) {
				leftmostPixel = pixel;
			}
			if (pixel.getX() > rightmostPixel.getX()) {
				rightmostPixel = pixel;
			}
		}

		this.firstPixel = new Pixel(leftmostPixel.getX(), topmostPixel.getY());
		this.lastPixel = new Pixel(rightmostPixel.getX(), bottommostPixel.getY());
	}

	@Override
	public void applyToImage(final BufferedImage image, final Color lineColor) {
		final int outlineWidth = this.lastPixel.getX() - this.firstPixel.getX();
		final int outlineHeight = this.lastPixel.getY() - this.firstPixel.getY();

		Graphics2D graphs = image.createGraphics();
		graphs.setColor(lineColor);
		graphs.drawRect(this.firstPixel.getX(), this.firstPixel.getY(), outlineWidth, outlineHeight);
		graphs.dispose();
	}

	@Override
	public void applyScaledToImage(BufferedImage image, Color lineColor, Dimension originalDimensions) {
		final Dimension newDimensions = new Dimension(image.getWidth(), image.getHeight());
		final Pixel scaledFirstPixel = ImageUtil.scalePixel(this.firstPixel, originalDimensions, newDimensions);
		final Pixel scaledLastPixel = ImageUtil.scalePixel(this.lastPixel, originalDimensions, newDimensions);

		final int outlineWidth = scaledLastPixel.getX() - scaledFirstPixel.getX();
		final int outlineHeight = scaledLastPixel.getY() - scaledFirstPixel.getY();

		Graphics2D graphs = image.createGraphics();
		graphs.setColor(lineColor);
		graphs.drawRect(scaledFirstPixel.getX(), scaledFirstPixel.getY(), outlineWidth, outlineHeight);
		graphs.dispose();
	}

	@Override
	public Rectangle getOutlineRect() {
		final int rectWidth = this.lastPixel.getX() - this.firstPixel.getX();
		final int rectHeight = this.lastPixel.getY() - this.firstPixel.getY();
		return new Rectangle(new Point(this.firstPixel.getX(), this.firstPixel.getY()), rectWidth, rectHeight);
	}

	@Override
	public double distanceTo(final ImageGrouping grouping) {
		return this.getOutlineRect().distanceTo(grouping.getOutlineRect());
	}

}
