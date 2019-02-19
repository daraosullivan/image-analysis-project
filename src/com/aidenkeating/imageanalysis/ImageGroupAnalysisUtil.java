package com.aidenkeating.imageanalysis;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import com.aidenkeating.imageanalysis.image.BinaryImageUtil;
import com.aidenkeating.imageanalysis.util.QuickUnionFind;

/**
 * A utility class for analyzing groups of adjoining pixels in an image, to
 * determine whether the groups are part of a single entity and to report on
 * the number of entities within an image.
 * 
 * @author aidenkeating
 */
public class ImageGroupAnalysisUtil {
	/**
	 * Find and report on groups of pixels found within a binary image.
	 * 
	 * This function will only process images based on whether a pixel is black
	 * or not. All other colors are ignored. It is suggested to convert an
	 * image to a black-white/binary image before using this function.
	 * 
	 * This function will currently print out the amount of groups it thinks it
	 * has found within an image. The function is currently incomplete, it does
	 * not check all neighbours for a pixel, instead only checking one pixel
	 * ahead on the row and column.
	 * 
	 * @param image The image to analyze
	 */
	public static void findGroupsInImage(final BufferedImage image) {
		// Initialize a QuickUnionFind struct. This is used to track which
		// pixels are connected to which.
		QuickUnionFind uf = new QuickUnionFind(image.getWidth()*image.getHeight());
		// Retrieve the raster for the image.
		WritableRaster raster = image.getRaster();
		// Iterate through every pixel of the image, starting from the
		// upper-left.
		for(int row = 0; row < image.getHeight(); row++) {
			for(int col = 0; col < image.getWidth(); col++) {
				// Check the next pixel on the X axis, if it's also black then
				// create a union in the QuickUnionFind so we know they're
				// connected.
				if (col < image.getWidth() - 1 &&
						raster.getSample(col, row, 0) == BinaryImageUtil.COLOR_BLACK &&
						raster.getSample(col + 1, row, 0) == BinaryImageUtil.COLOR_BLACK) {
					uf.union(getId(row, col, image.getWidth()), getId(row, col + 1, image.getWidth()));
				}
				// Check the next pixel on the Y axis, if it's also black then
				// create a union in the QuickUnionFind so we know they're
				// connected.
				if (row < image.getHeight() - 1 &&
						raster.getSample(col, row, 0) == BinaryImageUtil.COLOR_BLACK &&
						raster.getSample(col, row + 1, 0) == BinaryImageUtil.COLOR_BLACK) {
					uf.union(getId(row, col, image.getWidth()), getId(row + 1, col, image.getWidth()));
				}
			}
		}
		// Retrieve the number of trees with noise reduction of 1. This will
		// remove all trees with only one element in the QuickUnionFind
		// instance, so we should only be left with larger groupings of pixels.
		// Noise reduction can be adjusted to retrieve desired results.
		System.out.println("Number of trees: " + uf.getNumberOfTrees(1));
		
		// TODO: Retrieve the top and bottom of each tree we care about and get
		// the coordinates for it. This will allow us to draw a box around the
		// object. Earlier parts of this function may need to be adjusted to
		// store some extra info on pixels etc.
	}
	
	// Utility function for retrieving the correct array position in a
	// QuickUnionFind for a specific pixel in an image with nCols columns.
	private static int getId(int row, int col, int nCols) {
		return row*nCols + col;
	}
}
