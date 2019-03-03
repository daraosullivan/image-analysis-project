package com.aidenkeating.imageanalysis;

import java.awt.image.BufferedImage;
import java.util.List;

import com.aidenkeating.imageanalysis.config.Config;
import com.aidenkeating.imageanalysis.grouping.ImageGrouping;

/**
 * Full report of the image analysis process, containing all information about
 * each step of the analysis.
 * 
 * @author aidenkeating
 */
public class ImageAnalysisReport {
	private final Config config;
	private final BufferedImage originalImage;
	private final BufferedImage resizedImage;
	private final BufferedImage binaryImage;
	private final BufferedImage outlinedImage;
	private final List<ImageGrouping> members;
	private final List<ImageGrouping> swarms;

	public ImageAnalysisReport(final Config config, final BufferedImage originalImage, final BufferedImage resizedImage,
			final BufferedImage binaryImage, final BufferedImage outlinedImage, final List<ImageGrouping> members,
			final List<ImageGrouping> swarms) {
		this.config = config;
		this.originalImage = originalImage;
		this.resizedImage = resizedImage;
		this.binaryImage = binaryImage;
		this.outlinedImage = outlinedImage;
		this.members = members;
		this.swarms = swarms;
	}

	// Generated.
	public Config getConfig() {
		return config;
	}
	
	// Generated.
	public BufferedImage getOriginalImage() {
		return originalImage;
	}

	public BufferedImage getResizedImage() {
		return resizedImage;
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
	public List<ImageGrouping> getMembers() {
		return members;
	}
	
	// Generated.
	public List<ImageGrouping> getSwarms() {
		return swarms;
	}
	
	public int getMembersCount() {
		return this.members.size();
	}
	
	public int getSwarmsCount() {
		return this.swarms.size();
	}
}
