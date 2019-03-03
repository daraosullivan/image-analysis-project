package com.aidenkeating.imageanalysis;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import com.aidenkeating.imageanalysis.config.Config;
import com.aidenkeating.imageanalysis.grouping.ImageGrouping;
import com.aidenkeating.imageanalysis.grouping.MemberGrouping;
import com.aidenkeating.imageanalysis.grouping.SwarmGrouping;
import com.aidenkeating.imageanalysis.image.BinaryImageFactory;
import com.aidenkeating.imageanalysis.image.ImageUtil;
import com.aidenkeating.imageanalysis.image.Pixel;
import com.aidenkeating.imageanalysis.util.UnionFind;

/**
 * ImageAnalyzer is the main class a client should interact with. They should
 * not be invoking various components of the analysis process independently.
 * Instead, this class provides one function which the end-user can invoke to
 * produce an image with outlined distinct objects.
 * 
 * As the amount of parameters we take is this class are numerous and may expand
 * over time, we use the builder pattern to help the end-user keep a clean code
 * base and also make our own lives easier.
 * 
 * Note that although the image can be resized to improve efficiency, this will
 * reduce the accuracy of the image analysis.
 * 
 * We could have also provided a "config object" as a single parameter if we
 * wanted.
 * 
 * @author aidenkeating
 *
 */
public class ImageAnalyzer {
	private BinaryImageFactory binaryImageFactory;
	private Config config;

	public ImageAnalyzer(final BinaryImageFactory binaryImageFactory, final Config config) {
		this.binaryImageFactory = binaryImageFactory;
		this.config = config;
	}

	/**
	 * Locates distinct objects in a provided image and produces a new image with
	 * them outlined.
	 * 
	 * @param image The image to analyze
	 * @return A scaled copy of the provided image
	 */
	public ImageAnalysisReport compileReport(final BufferedImage image) {
		// The original image should be treated as immutable, make a deep copy
		// we can modify.
		final BufferedImage mutableImageCopy = ImageUtil.deepCopy(image);
		// If resizeImage is not set then use the original image.
		final BufferedImage resizedImage = (this.config.getResizeConfig().isEnabled()
				? ImageUtil.scaleImage(mutableImageCopy, this.config.getResizeConfig().getResizeBounds())
				: mutableImageCopy);
		// Produce an image with only two distinct colors, black and white.
		final BufferedImage binaryImage = this.binaryImageFactory.produceBinaryImage(resizedImage);

		// Retrieve a list of object groupings from the binary image.
		final List<ImageGrouping> groupings = findDistinctObjectsInImage(binaryImage);
		final Dimension resizeImageDimensions = new Dimension(resizedImage.getWidth(), resizedImage.getHeight());

		// Retrieve all groups of groupings, we'll call these swarms for lack
		// of a better word.
		// Outline the groupings in the original color image and return it.
		if (this.config.getMemberConfig().isEnabled()) {
			for (final ImageGrouping grouping : groupings) {
				final Color memberOutlineColor = this.config.getMemberConfig().getOutlineColor();
				if (this.config.getResizeConfig().isEnabled()) {
					grouping.applyScaledToImage(mutableImageCopy, memberOutlineColor, resizeImageDimensions);
				} else {
					grouping.applyToImage(mutableImageCopy, memberOutlineColor);
				}
			}
		}
		// Do the same, but now with members instead of pixels, to find swarms
		// of birds.
		List<ImageGrouping> swarms = new ArrayList<ImageGrouping>(0);
		if (this.config.getSwarmConfig().isEnabled()) {
			swarms = findSwarmsOfGroupings(groupings,
					this.config.getSwarmConfig().getMemberDistanceThreshold());
			for (final ImageGrouping swarm : swarms) {
				final Color swarmOutlineColor = this.config.getSwarmConfig().getOutlineColor();
				if (this.config.getResizeConfig().isEnabled()) {
					swarm.applyScaledToImage(mutableImageCopy, swarmOutlineColor, resizeImageDimensions);
				} else {
					swarm.applyToImage(mutableImageCopy, swarmOutlineColor);
				}
			}
		}
		return new ImageAnalysisReport(this.config, image, resizedImage, binaryImage, mutableImageCopy, groupings,
				swarms);
	}

	/**
	 * Save all contents of a report to a directory, along with a Markdown file
	 * which includes all the information in a single page.
	 * 
	 * @param report     The report to save
	 * @param outputFile The directory to save to
	 * @throws IOException
	 */
	public static void exportReportToFile(final ImageAnalysisReport report, final String outputDir) throws IOException {
		// Save original file
		final File originalFile = new File(outputDir + "/original.png");
		ImageIO.write(report.getOriginalImage(), "png", originalFile);
		// Save binary color file
		final File resizedFile = new File(outputDir + "/resized.png");
		ImageIO.write(report.getResizedImage(), "png", resizedFile);
		// Save binary color file
		final File binaryColorFile = new File(outputDir + "/binary.png");
		ImageIO.write(report.getBinaryImage(), "png", binaryColorFile);
		// Save outlined file
		final File outlineFile = new File(outputDir + "/outlined.png");
		ImageIO.write(report.getOutlinedImage(), "png", outlineFile);

		final List<String> lines = Arrays.asList("# Image Analysis Report", "## Metadata",
				String.format("* Member Detection Enabled: %s", report.getConfig().getMemberConfig().isEnabled()),
				String.format("* Members Detected: %s",
						(report.getConfig().getMemberConfig().isEnabled() ? report.getMembersCount() : 0)),
				String.format("* Swarm Detection Enabled: %s", report.getConfig().getSwarmConfig().isEnabled()),
				String.format("* Swarms Detected: %s", report.getSwarmsCount()),
				String.format("* Image Width: %s", report.getOriginalImage().getWidth()),
				String.format("* Image Height: %s", report.getOriginalImage().getHeight()), "## Images",
				"### Original Image", "![Original Image](./original.png)", "### Resized Image",
				"![Resized Image](./resized.png)", "### Binary Image", "![Binary Image](./binary.png)",
				"### Outlined Image", "![Outlined Image](./outlined.png)");
		final Path file = Paths.get(outputDir, "report.md");
		Files.write(file, lines, Charset.forName("UTF-8"));
	}

	/**
	 * Find all sets of grouped pixels with the primary color in a binary image
	 * 
	 * This is where the UnionFind comes into play.
	 * 
	 * @param image The image to find groups in
	 * @return A list of groups
	 */
	private List<ImageGrouping> findDistinctObjectsInImage(final BufferedImage image) {
		// Initialize a QuickUnionFind struct. This is used to track which
		// pixels are connected to which.
		UnionFind uf = new UnionFind(image.getWidth() * image.getHeight());
		// Retrieve the raster for the image.
		// WritableRaster raster = image.getRaster();
		// Iterate through every pixel of the image, starting from the
		// upper-left.
		for (int row = 0; row < image.getHeight(); row++) {
			for (int col = 0; col < image.getWidth(); col++) {
				// Store some commonly used information about the pixel.
				final int pixelRGB = image.getRGB(col, row);
				final int nCols = image.getWidth();
				final int nRows = image.getHeight();
				final int pixelId = getId(row, col, nCols);

				// We only care about black pixels in this binary image.
				if (pixelRGB != this.binaryImageFactory.primaryColor().getRGB()) {
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

		final List<ImageGrouping> imageGroupings = new ArrayList<ImageGrouping>();
		for (int root : uf.getRoots(this.config.getMemberConfig().getNoiseReduction())) {
			final List<Pixel> pixels = new ArrayList<Pixel>();

			final List<Integer> treeElements = uf.getElementsOfTree(root);
			for (int pixelId : treeElements) {
				pixels.add(new Pixel(pixelId % image.getWidth(), pixelId / image.getWidth()));
			}

			// Uncomment/comment these lines to switch between the two types of
			// pixel grouping implementations.
			// imageGroupings.add(new FirstLastPixelImageGrouping(pixels));
			imageGroupings.add(new MemberGrouping(pixels));
		}

		return imageGroupings;
	}

	/**
	 * Find all sets of grouped members within an image.
	 * 
	 * @param groupings The members to compare
	 * @param threshold The maximum distance between two members to be considered in
	 *                  the same swarm
	 * @return All swarms in the image
	 */
	private List<ImageGrouping> findSwarmsOfGroupings(final List<ImageGrouping> groupings, final double threshold) {
		final UnionFind uf = new UnionFind(groupings.size());
		for (int i = 0; i < groupings.size(); i++) {
			for (int j = i + 1; j < groupings.size(); j++) {
				if (groupings.get(i).distanceTo(groupings.get(j)) < threshold) {
					uf.union(i, j);
				}
			}
		}

		final List<ImageGrouping> swarms = new ArrayList<ImageGrouping>();
		for (final int root : uf.getRoots(1)) {
			final List<ImageGrouping> swarmMembers = new ArrayList<ImageGrouping>();

			final List<Integer> treeElements = uf.getElementsOfTree(root);
			for (final int groupingId : treeElements) {
				swarmMembers.add(groupings.get(groupingId));
			}
			swarms.add(new SwarmGrouping(swarmMembers));
		}
		return swarms;
	}

	// Generated.
	public BinaryImageFactory getBinaryImageFactory() {
		return binaryImageFactory;
	}

	// Generated.
	public void setBinaryImageFactory(final BinaryImageFactory binaryImageFactory) {
		this.binaryImageFactory = binaryImageFactory;
	}

	// Generated.
	public Config getConfig() {
		return this.config;
	}

	// Generated.
	public void setConfig(final Config config) {
		this.config = config;
	}

	// Utility function for retrieving the correct array position in a
	// QuickUnionFind for a specific pixel in an image with nCols columns.
	private static int getId(final int row, final int col, final int nCols) {
		return row * nCols + col;
	}
}
