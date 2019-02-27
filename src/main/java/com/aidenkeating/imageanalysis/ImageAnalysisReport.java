package com.aidenkeating.imageanalysis;

import java.awt.image.BufferedImage;
import java.util.List;

import com.aidenkeating.imageanalysis.image.ImageGrouping;

/**
 * Full report of the image analysis process, containing all information about
 * each step of the analysis.
 * 
 * @author aidenkeating
 */
public class ImageAnalysisReport {
	private final BufferedImage originalImage;
	private final BufferedImage binaryImage;
	private final BufferedImage outlinedImage;
	private final List<ImageGrouping> disinctObjectGroupings;

	public ImageAnalysisReport(final BufferedImage originalImage, final BufferedImage binaryImage,
			final BufferedImage outlinedImage, final List<ImageGrouping> disinctObjectGroupings) {
		this.originalImage = originalImage;
		this.binaryImage = binaryImage;
		this.outlinedImage = outlinedImage;
		this.disinctObjectGroupings = disinctObjectGroupings;
	}

	// Generated.
	public BufferedImage getOriginalImage() {
		return originalImage;
	}

	// Generated.
	public BufferedImage getBinaryImage() {
		return binaryImage;
	}

	// Generated.
	public BufferedImage getOutlinedImage() {
		return outlinedImage;
	}

	// Generated.
	public int getDistinctObjectCount() {
		return disinctObjectGroupings.size();
	}

	// Generated.
	public List<ImageGrouping> getDisinctObjectGroupings() {
		return disinctObjectGroupings;
	}
}
