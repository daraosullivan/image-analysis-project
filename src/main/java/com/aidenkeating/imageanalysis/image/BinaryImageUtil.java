package com.aidenkeating.imageanalysis.image;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

/**
 * A utility class for managing binary images, including conversion.
 * 
 * @author aidenkeating
 */
public class BinaryImageUtil {
	// Values for our black and white pixels, to make it more readable.
	public static final int COLOR_BLACK = 0;
	public static final int COLOR_WHITE = 255;
	
	/**
	 * A default threshold to be used when converting images.
	 */
	public static final int DEFAULT_THRESHOLD = 127;
		
	/**
	 * Convert a provided BufferedImage to a binary representation, with the
	 * specified threshold being used to determine which pixels become black
	 * and which become white.
	 * 
	 * The algorithm works as follows:
	 * - Convert the entire image to greyscale, so each pixel is now a level
	 * of "greyness" between 0 and 255.
	 * - Determine if each pixel is above or below the level of "greyness" in
	 * the threshold. If it's above, make the pixel black, if it's below make
	 * the pixel white.
	 * 
	 * This is basically bringing the image to two extremes. Experimentation
	 * may need to be done with the provided threshold.
	 * 
	 * @param toConvert The image to convert
	 * @param threshold The "greyness" threshold for the conversion
	 * @return A converted, binary-like, image
	 */
	public static BufferedImage convert(final BufferedImage toConvert, final int threshold) {
		// Store the original height and width in a readable name.
		final int originalWidth = toConvert.getWidth();
		final int originalHeight = toConvert.getHeight();
		
		// Copy over the image data for toConvert to a new BufferedImage.
		final BufferedImage convertedImage = new BufferedImage(
				toConvert.getWidth(),
				toConvert.getHeight(),
				BufferedImage.TYPE_BYTE_GRAY);
		convertedImage.getGraphics().drawImage(toConvert, 0, 0, null);
	    
		
		WritableRaster raster = convertedImage.getRaster();
	    int[] pixels = new int[originalWidth];
	    for (int row = 0; row < originalHeight; row++) {
	    	// Get a full row of pixels, we can modify them all at once.
	    	// Store them in the pixels array, re-use the same array for each row.
	        raster.getPixels(0, row, originalWidth, 1, pixels);
	        for (int col = 0; col < pixels.length; col++) {
	            // Check if the "grayness" of the pixel is less than the threshold,
	        	// if it is then make the pixel white, else black.
	        	if (pixels[col] < threshold) {
	            	pixels[col] = COLOR_BLACK;
	            } else {
	            	pixels[col] = COLOR_WHITE;
	            }
	        }
	        // Set the entire row of updated pixels all at once.
	        raster.setPixels(0, row, originalWidth, 1, pixels);
	    }
	    // Return the converted binary image.
	    return convertedImage;
	}

}
