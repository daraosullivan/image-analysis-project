package com.aidenkeating.imageanalysis.image;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * An image that can be represented in two colors, using a threshold to
 * distinguish between which color each pixel should be.
 * 
 * @author aidenkeating
 */
public interface BinaryImageFactory {
	/**
	 * Retrieve the primary color for the binary image that can be produced.
	 * 
	 * @return The primary color of the produced binary image.
	 */
	Color primaryColor();

	/**
	 * Retrieve the secondary color for the binary image that can be produced.
	 * 
	 * @return The secondary color of the produced binary image.
	 */
	Color secondaryColor();

	/**
	 * Set the threshold for the factory.
	 */
	void setThreshold(int threshold);

	/**
	 * Retrieve a binary representation of the image, taking into account the
	 * threshold.
	 * 
	 * @return Binary representation of the image.
	 */
	BufferedImage produceBinaryImage(BufferedImage image);
}
