package com.aidenkeating.imageanalysis;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.aidenkeating.imageanalysis.image.BinaryImageUtil;

/**
 * Class to act as a test run for the image analysis work.
 * @author aidenkeating
 */
public class MyImageAnalysis {

	public static void main(String[] args) {
		try {
			System.out.println("Reading image from input.png");
	        File inputFile = new File("input.png");
			BufferedImage inputImage = ImageIO.read(inputFile);
			// Convert the image with a threshold of 130.
			BufferedImage image = BinaryImageUtil.convert(inputImage, 130);
			
			// This will print out some basic info, the function is there, but
			// requires some more work for larger images.
			ImageGroupAnalysisUtil.findGroupsInImage(image);
			
			System.out.println("Writing black-white image to output.png");
	        File outputfile = new File("output.png");
	        ImageIO.write(image, "png", outputfile);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
