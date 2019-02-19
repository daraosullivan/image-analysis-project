package com.aidenkeating.imageanalysis;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;

import com.aidenkeating.imageanalysis.image.BinaryImageUtil;
import com.aidenkeating.imageanalysis.image.ImageGrouping;

/**
 * Class to act as a test run for the image analysis work.
 * @author aidenkeating
 */
public class MyImageAnalysis {

	public static void main(String[] args) {
		try {
			System.out.println("Reading image from input.png");
	        File inputFile = new File("testimages/morebirds.jpg");
			BufferedImage inputImage = ImageIO.read(inputFile);
			// Convert the image with a threshold of 130.
			BufferedImage image = BinaryImageUtil.convert(inputImage, 130);
			
			// This will print out some basic info, the function is there, but
			// requires some more work for larger images.
			List<ImageGrouping> igs = ImageGroupAnalysisUtil.findGroupsInImage(image);
			
			// For each grouping, draw a blue box.
			// The grouping coordinates are still rather rough at the moment.
			for (ImageGrouping ig : igs) {
				Graphics2D graphs = inputImage.createGraphics();
				graphs.setColor(Color.BLUE);
				graphs.drawRect(ig.getX1(), ig.getY1(), ig.getX2() - ig.getX1(), ig.getY2() - ig.getY1());
				graphs.dispose();
			}
			
			System.out.println("Writing black-white image to output.png");
	        File outputfile = new File("output.png");
	        ImageIO.write(inputImage, "png", outputfile);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
