package com.aidenkeating.imageanalysis.config;

import java.awt.Color;

/**
 * Configuration used when working with swarms within an image.
 * 
 * @author aidenkeating
 */
public class SwarmAnalysisConfig {
	private boolean enabled = false;
	private Color outlineColor = Color.BLUE;
	private double memberDistanceThreshold = 50;

	public SwarmAnalysisConfig(final boolean enabled) {
		this.enabled = enabled;
	}

	public SwarmAnalysisConfig(final boolean enabled, final Color outlineColor, final double memberDistanceThreshold) {
		this.enabled = enabled;
		this.outlineColor = outlineColor;
		this.memberDistanceThreshold = memberDistanceThreshold;
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
	public double getMemberDistanceThreshold() {
		return memberDistanceThreshold;
	}

	// Generated.
	public void setMemberDistanceThreshold(double memberDistanceThreshold) {
		this.memberDistanceThreshold = memberDistanceThreshold;
	}
}
