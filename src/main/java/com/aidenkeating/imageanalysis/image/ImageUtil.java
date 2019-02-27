package com.aidenkeating.imageanalysis.image;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public abstract class ImageUtil {
	public static BufferedImage scaleImage(final BufferedImage image, final Dimension boundary) {
		final Dimension originalDimension = new Dimension(image.getWidth(), image.getHeight());
		final Dimension scaledDimension = getScaledDimension(originalDimension, boundary);

		final int newWidth = (int) scaledDimension.getWidth();
		final int newHeight = (int) scaledDimension.getHeight();

		BufferedImage resized = new BufferedImage(newWidth, newHeight, image.getType());
		Graphics2D g = resized.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(image, 0, 0, newWidth, newHeight, 0, 0, image.getWidth(), image.getHeight(), null);
		g.dispose();
		return resized;
	}

	public static BufferedImage deepCopy(final BufferedImage image) {
		final BufferedImage copy = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		Graphics copyGraphics = copy.getGraphics();
		copyGraphics.drawImage(image, 0, 0, null);
		copyGraphics.dispose();
		return copy;
	}

	private static Dimension getScaledDimension(Dimension imageSize, Dimension boundary) {
		double widthRatio = boundary.getWidth() / imageSize.getWidth();
		double heightRatio = boundary.getHeight() / imageSize.getHeight();
		double ratio = Math.min(widthRatio, heightRatio);

		return new Dimension((int) (imageSize.width * ratio), (int) (imageSize.height * ratio));
	}
}
