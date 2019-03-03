package com.aidenkeating.imageanalysis.config;

import java.awt.Dimension;

/**
 * Configuration for resizing the image during analysis;
 * 
 * @author aidenkeating
 */
public class ImageResizeConfig {
	private boolean enabled = false;
	private Dimension resizeBounds = new Dimension(500, 500);

	public ImageResizeConfig(final boolean enabled) {
		this.enabled = enabled;
	}

	public ImageResizeConfig(final boolean enabled, final Dimension resizeBounds) {
		this.enabled = enabled;
		this.resizeBounds = resizeBounds;
	}

	// Generated.
	public boolean isEnabled() {
		return enabled;
	}

	// Generated.
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	// Generated.
	public Dimension getResizeBounds() {
		return resizeBounds;
	}

	// Generated.
	public void setResizeBounds(Dimension resizeBounds) {
		this.resizeBounds = resizeBounds;
	}
}
