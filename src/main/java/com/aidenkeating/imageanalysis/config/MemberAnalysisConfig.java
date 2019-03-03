package com.aidenkeating.imageanalysis.config;

import java.awt.Color;

/**
 * Configuration used when working with individual members within an image.
 * 
 * @author aidenkeating
 */
public class MemberAnalysisConfig {
	private boolean enabled = true;
	private Color outlineColor = Color.RED;
	private int noiseReduction = 1;

	public MemberAnalysisConfig(final boolean enabled) {
		this.enabled = enabled;
	}

	public MemberAnalysisConfig(final boolean enabled, final Color outlineColor, final int noiseReduction) {
		this.enabled = enabled;
		this.outlineColor = outlineColor;
		this.noiseReduction = noiseReduction;
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
	public Color getOutlineColor() {
		return outlineColor;
	}

	// Generated.
	public void setOutlineColor(Color outlineColor) {
		this.outlineColor = outlineColor;
	}

	// Generated.
	public int getNoiseReduction() {
		return noiseReduction;
	}

	// Generated.
	public void setNoiseReduction(int noiseReduction) {
		this.noiseReduction = noiseReduction;
	}

}
