package com.aidenkeating.imageanalysis.grouping;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

import com.aidenkeating.imageanalysis.geometry.Rectangle;

/**
 * Representation of a distinct object within an image.
 * 
 * @author aidenkeating
 */
public interface ImageGrouping {
	/**
	 * Retrieve the outline of the grouping represented as a rectangle.
	 * 
	 * @return The rectangle of the outline
	 */
	public Rectangle getOutlineRect();

	/**
	 * Draw the image grouping onto a provided image, scaled from the dimensions of
	 * another image.
	 * 
	 * @param image     The image to draw to
	 * @param lineColor The image to draw to
	 */
	public void applyToImage(BufferedImage image, Color lineColor);

	/**
	 * Draw the image grouping onto a provided image, scaled from the dimensions of
	 * another image.
	 * 
	 * This can be used in cases where the image used to collect the image groupings
	 * was a reduced version of the original image, which is a common way to improve
	 * efficiency in image analysis.
	 * 
	 * @param image              The image to draw to
	 * @param lineColor          The color of the outline to draw
	 * @param originalDimensions The dimensions from the image from which the
	 *                           grouping originates
	 */
	public void applyScaledToImage(BufferedImage image, Color lineColor, Dimension originalDimensions);

	/**
	 * The distance between the outline of this grouping and the outline of another
	 * grouping.
	 * 
	 * @param grouping
	 */
	public double distanceTo(ImageGrouping grouping);
}
