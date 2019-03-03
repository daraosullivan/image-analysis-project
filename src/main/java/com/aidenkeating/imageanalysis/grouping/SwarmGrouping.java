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

/**
 * A grouping for a collection of groupings.
 * 
 * @author aidenkeating
 */
public class SwarmGrouping implements ImageGrouping {
	private final Point firstPoint;
	private final Point lastPoint;

	public SwarmGrouping(final List<ImageGrouping> groupings) {
		Point topmostPoint = groupings.get(0).getOutlineRect().getTopLeft();
		Point bottommostPoint = groupings.get(0).getOutlineRect().getBottomLeft();
		Point rightmostPoint = groupings.get(0).getOutlineRect().getTopRight();
		Point leftmostPoint = groupings.get(0).getOutlineRect().getBottomLeft();

		for (final ImageGrouping grouping : groupings) {
			final Rectangle outlineRect = grouping.getOutlineRect();
			if (outlineRect.getTopLeft().getY() < topmostPoint.getY()) {
				topmostPoint = outlineRect.getTopLeft();
			}
			if (outlineRect.getTopLeft().getX() < leftmostPoint.getX()) {
				leftmostPoint = outlineRect.getTopLeft();
			}
			if (outlineRect.getBottomLeft().getY() > bottommostPoint.getY()) {
				bottommostPoint = outlineRect.getBottomLeft();
			}
			if (outlineRect.getBottomRight().getX() > rightmostPoint.getX()) {
				rightmostPoint = outlineRect.getBottomRight();
			}
		}
		this.firstPoint = new Point(leftmostPoint.getX(), topmostPoint.getY());
		this.lastPoint = new Point(rightmostPoint.getX(), bottommostPoint.getY());
	}

	@Override
	public Rectangle getOutlineRect() {
		final int rectWidth = this.lastPoint.getX() - this.firstPoint.getX();
		final int rectHeight = this.lastPoint.getY() - this.firstPoint.getY();
		return new Rectangle(new Point(this.firstPoint.getX(), this.firstPoint.getY()), rectWidth, rectHeight);
	}

	@Override
	public void applyToImage(final BufferedImage image, final Color lineColor) {
		Graphics2D graphs = image.createGraphics();
		graphs.setColor(lineColor);
		graphs.drawRect(this.getOutlineRect().getTopLeft().getX(), this.getOutlineRect().getTopLeft().getY(),
				this.getOutlineRect().getWidth(), this.getOutlineRect().getHeight());
		graphs.dispose();
	}

	@Override
	public void applyScaledToImage(final BufferedImage image, final Color lineColor,
			final Dimension originalDimensions) {
		final Dimension newDimensions = new Dimension(image.getWidth(), image.getHeight());

		// Convert points to pixels so they can be used in the context of a fixed size
		// image.
		final Pixel firstPixel = new Pixel(this.firstPoint.getX(), this.firstPoint.getY());
		final Pixel lastPixel = new Pixel(this.lastPoint.getX(), this.lastPoint.getY());

		final Pixel scaledFirstPixel = ImageUtil.scalePixel(firstPixel, originalDimensions, newDimensions);
		final Pixel scaledLastPixel = ImageUtil.scalePixel(lastPixel, originalDimensions, newDimensions);

		final int outlineWidth = scaledLastPixel.getX() - scaledFirstPixel.getX();
		final int outlineHeight = scaledLastPixel.getY() - scaledFirstPixel.getY();

		Graphics2D graphs = image.createGraphics();
		graphs.setColor(lineColor);
		graphs.drawRect(scaledFirstPixel.getX(), scaledFirstPixel.getY(), outlineWidth, outlineHeight);
		graphs.dispose();

	}

	@Override
	public double distanceTo(ImageGrouping grouping) {
		return this.getOutlineRect().distanceTo(grouping.getOutlineRect());
	}

}
