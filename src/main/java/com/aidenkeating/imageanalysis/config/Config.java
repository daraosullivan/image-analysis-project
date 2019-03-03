package com.aidenkeating.imageanalysis.config;

public class Config {
	private MemberAnalysisConfig memberConfig;
	private SwarmAnalysisConfig swarmConfig;
	private ImageResizeConfig resizeConfig;

	public Config(MemberAnalysisConfig memberConfig, SwarmAnalysisConfig swarmConfig, ImageResizeConfig resizeConfig) {
		this.memberConfig = memberConfig;
		this.swarmConfig = swarmConfig;
		this.resizeConfig = resizeConfig;
	}

	// Generated.
	public MemberAnalysisConfig getMemberConfig() {
		return memberConfig;
	}

	// Generated.
	public void setMemberConfig(MemberAnalysisConfig memberConfig) {
		this.memberConfig = memberConfig;
	}

	// Generated.
	public SwarmAnalysisConfig getSwarmConfig() {
		return swarmConfig;
	}

	// Generated.
	public void setSwarmConfig(SwarmAnalysisConfig swarmConfig) {
		this.swarmConfig = swarmConfig;
	}

	// Generated.
	public ImageResizeConfig getResizeConfig() {
		return resizeConfig;
	}

	// Generated.
	public void setResizeConfig(ImageResizeConfig resizeConfig) {
		this.resizeConfig = resizeConfig;
	}
}
