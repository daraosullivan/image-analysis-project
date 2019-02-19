package com.aidenkeating.imageanalysis;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.BufferedImage;

import com.aidenkeating.imageanalysis.image.ImageGrouping;
import com.aidenkeating.imageanalysis.util.QuickUnionFind;

/**
 * A utility class for analyzing groups of adjoining pixels in an image, to
 * determine whether the groups are part of a single entity and to report on the
 * number of entities within an image.
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
	public static List<ImageGrouping> findGroupsInImage(final BufferedImage image) {
		// Initialize a QuickUnionFind struct. This is used to track which
		// pixels are connected to which.
		QuickUnionFind uf = new QuickUnionFind(image.getWidth()*image.getHeight());
		// Retrieve the raster for the image.
		//WritableRaster raster = image.getRaster();
		// Iterate through every pixel of the image, starting from the
		// upper-left.
		for(int row = 0; row < image.getHeight(); row++) {
			for(int col = 0; col < image.getWidth(); col++) {
				// Store some commonly used information about the pixel.
				final int pixelRGB = image.getRGB(col, row);
				final int nCols = image.getWidth();
				final int nRows = image.getHeight();
				final int pixelId = getId(row, col, nCols);
				
				// We only care about black pixels in this binary image.
				if (pixelRGB != Color.BLACK.getRGB()) {
					continue;
				}
				// Check left.
				if (col > 0 && pixelRGB == image.getRGB(col - 1, row)) {
					uf.union(pixelId, getId(row, col - 1, nCols));
				}
				// Check right.
				if (col < nCols - 1 && pixelRGB == image.getRGB(col + 1, row)) {
					uf.union(pixelId, getId(row, col + 1, nCols));
				}
				// Check up.
				if (row > 0 && pixelRGB == image.getRGB(col, row - 1)) {
					uf.union(pixelId, getId(row - 1, col, nCols));
				}
				// Check down.
				if (row < nRows - 1 && pixelRGB == image.getRGB(col, row + 1)) {
					uf.union(pixelId, getId(row + 1, col, nCols));
				}
				// Check SE.
				if (row < nRows - 1 && col < nCols - 1 && pixelRGB == image.getRGB(col + 1, row + 1)) {
					uf.union(pixelId, getId(row + 1, col + 1, nCols));
				}
				// Check NW.
				if (row > 0 && col > 0 && pixelRGB == image.getRGB(col - 1, row - 1)) {
					uf.union(pixelId, getId(row - 1, col - 1, nCols));
				}
				// Check SW.
				if (row > 0 && col < nCols - 1 && pixelRGB == image.getRGB(col + 1, row - 1)) {
					uf.union(pixelId, getId(row - 1, col + 1, nCols));
				}
				// Check NE.
				if (row < nRows - 1 && col > 0 && pixelRGB == image.getRGB(col - 1, row + 1)) {
					uf.union(pixelId, getId(row + 1, col - 1, nCols));
				}
			}
		}
		
		List<ImageGrouping> imageGroupings = new ArrayList<ImageGrouping>();
		for(int root: uf.getRoots(1)) {
			final List<Integer> treeElements = uf.getElementsOfTree(root);
			final int firstNode = treeElements.get(0);
			final int lastNode = treeElements.get(treeElements.size() - 1);

			// Get the coordinates for the first and last pixel in the tree.
			// These will be the first and last encountered, giving us a rough
			// set of coordinates to draw a box.
			final int x1 = firstNode%image.getWidth();
			final int y1 = firstNode/image.getWidth();
			final int x2 = lastNode%image.getWidth();
			final int y2 = lastNode/image.getWidth();
			
			imageGroupings.add(new ImageGrouping(x1, y1, x2, y2));
		}
		
		return imageGroupings;
	}

	// Utility function for retrieving the correct array position in a
	// QuickUnionFind for a specific pixel in an image with nCols columns.
	private static int getId(int row, int col, int nCols) {
		return row * nCols + col;
	}
}
