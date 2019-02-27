package com.aidenkeating.imageanalysis.image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

/**
 * A factory to produce binary images by using the approach of first converting
 * an image to grayscale and then measuring the levels of "greyness" against a
 * threshold. The binary images produced will always be black and white, the two
 * colors cannot be specified.
 * 
 * @author aidenkeating
 */
public class GrayscaleBinaryImageFactory implements BinaryImageFactory {
	private static final int PIXEL_VALUE_BLACK = 0;
	private static final int PIXEL_VALUE_WHITE = 255;

	private int threshold;

	public GrayscaleBinaryImageFactory(final int thresholdValue) {
		this.threshold = validThreshold(thresholdValue);
	}

	@Override
	public Color primaryColor() {
		return Color.BLACK;
	}

	@Override
	public Color secondaryColor() {
		return Color.WHITE;
	}

	@Override
	public BufferedImage produceBinaryImage(BufferedImage image) {
		// Store the original height and width in a readable name.
		final int originalWidth = image.getWidth();
		final int originalHeight = image.getHeight();

		// Copy over the image data for toConvert to a new BufferedImage.
		final BufferedImage grayscaleImage = new BufferedImage(image.getWidth(), image.getHeight(),
				BufferedImage.TYPE_BYTE_GRAY);
		grayscaleImage.getGraphics().drawImage(image, 0, 0, null);

		WritableRaster raster = grayscaleImage.getRaster();
		final int[] pixels = new int[originalWidth];
		for (int row = 0; row < originalHeight; row++) {
			// Get a full row of pixels, we can modify them all at once.
			// Store them in the pixels array, re-use the same array for each row.
			raster.getPixels(0, row, originalWidth, 1, pixels);
			for (int col = 0; col < pixels.length; col++) {
				// Check if the "grayness" of the pixel is less than the threshold,
				// if it is then make the pixel white, else black.
				if (pixels[col] < threshold) {
					pixels[col] = PIXEL_VALUE_BLACK;
				} else {
					pixels[col] = PIXEL_VALUE_WHITE;
				}
			}
			// Set the entire row of updated pixels all at once.
			raster.setPixels(0, row, originalWidth, 1, pixels);
		}
		// Return the converted binary image.
		return grayscaleImage;
	}

	// Generated.
	public int getThreshold() {
		return threshold;
	}

	// Generated.
	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	private int validThreshold(final int thresholdValue) {
		if (thresholdValue < 0 || thresholdValue > 255) {
			return 127;
		}
		return thresholdValue;
	}

}
