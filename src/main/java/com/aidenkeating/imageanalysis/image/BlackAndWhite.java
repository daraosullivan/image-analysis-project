package com.aidenkeating.imageanalysis.image;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class BlackAndWhite implements BinaryImageFactory {
	public BlackAndWhite() {
	}

	@Override
	public Color primaryColor() {
		return Color.BLACK;
	}

	@Override
	public Color secondaryColor() {
		return Color.WHITE;
	}

	@Override
	public BufferedImage produceBinaryImage(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		// sepia
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int p = image.getRGB(x, y);
				int a = (p >> 24) & 0xff;
				int R = (p >> 16) & 0xff;
				int G = (p >> 8) & 0xff;
				int B = p & 0xff;
				int newRed = (int) (0.393 * R + 0.769 * G + 0.189 * B);
				int newGreen = (int) (0.349 * R + 0.686 * G + 0.168 * B);
				int newBlue = (int) (0.272 * R + 0.534 * G + 0.131 * B);
				if (newRed > 255) {
					R = 255;
				} else {
					R = newRed;
				}
				if (newGreen > 255) {
					G = 255;
				} else {
					G = newGreen;
				}
				if (newBlue > 255) {
					B = 255;
				} else {
					B = newBlue;
				}
				p = (a << 24) | (R << 16) | (G << 8) | B;
				image.setRGB(x, y, p);
			}
		}
		return image;
	}
}