package com.aidenkeating.imageanalysis;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.aidenkeating.imageanalysis.image.BinaryImageFactory;
import com.aidenkeating.imageanalysis.image.GrayscaleBinaryImageFactory;

/**
 * Some basic test for obvious images.
 * 
 * We can expand this overtime, and perhaps even implement a learning algorithm
 * for determining the best threshold and noise reduction. But that's not in the
 * spec so we won't do it.
 * 
 * @author aidenkeating
 *
 */
class ImageAnalyzerTest {
	private BinaryImageFactory binaryImageFactory;
	private ImageAnalyzer imageAnalyzer;

	@BeforeEach
	void setUp() throws Exception {
		this.binaryImageFactory = new GrayscaleBinaryImageFactory(130);
		this.imageAnalyzer = new ImageAnalyzer.Builder().withBinaryImageFactory(this.binaryImageFactory)
				.withOutlineColor(Color.RED).build();
	}

	@Test
	void testNineBirds() throws IOException {
		// ClassLoader classLoader = getClass().getClassLoader();
		File file = new File("src/test/resources/birds_1.jpg");
		System.out.println(file.getAbsolutePath());
		BufferedImage image = ImageIO.read(file);
		ImageAnalysisReport report = this.imageAnalyzer.compileReport(image);
		assertEquals(report.getDistinctObjectCount(), 9);
	}

	@Test
	void testOneBird() throws IOException {
		// ClassLoader classLoader = getClass().getClassLoader();
		File file = new File("src/test/resources/birds_2.jpg");
		System.out.println(file.getAbsolutePath());
		BufferedImage image = ImageIO.read(file);
		ImageAnalysisReport report = this.imageAnalyzer.compileReport(image);
		assertEquals(report.getDistinctObjectCount(), 1);
	}
}
