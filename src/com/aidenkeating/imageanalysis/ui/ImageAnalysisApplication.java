package com.aidenkeating.imageanalysis.ui;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.aidenkeating.imageanalysis.ImageAnalyzer;
import com.aidenkeating.imageanalysis.image.GrayscaleBinaryImageFactory;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ImageAnalysisApplication extends Application {
	private final FileChooser fileChooser = new FileChooser();
	private File file;
	private ImageView imageView = new ImageView();
	private GrayscaleBinaryImageFactory binaryImageFactory;
	private ImageAnalyzer imageAnalyzer;

	@Override
	public void start(Stage primaryStage) {
		this.binaryImageFactory = new GrayscaleBinaryImageFactory(130);
		this.imageAnalyzer = new ImageAnalyzer.Builder()
				.withBinaryImageFactory(this.binaryImageFactory).withOutlineColor(Color.RED).build();

		final BorderPane borderPane = new BorderPane();

		// Setup load button.
		final Button imageSelectButton = buildImageSelectButton(primaryStage, fileChooser);
		borderPane.setTop(imageSelectButton);

		// Setup central ImageView.
		imageView.setPreserveRatio(true);
		imageView.setFitHeight(200);
		imageView.setFitWidth(500);
		borderPane.setCenter(imageView);

		// Setup sliders.
		final Slider thresholdSlider = buildThresholdSlider(20);
		borderPane.setBottom(thresholdSlider);

		// Show scene.
		primaryStage.setScene(new Scene(borderPane));
		primaryStage.show();

		// Open the dialog immediately if a file isn't selected (which it
		// probably won't be).
		if (file == null) {
			imageSelectButton.fire();
		}
	}

	/**
	 * Create a button used for selecting images.
	 */
	private Button buildImageSelectButton(final Stage stage, final FileChooser fileChooser) {
		final Button button = new Button("Open image");
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				final File imageFile = fileChooser.showOpenDialog(stage);
				if (imageFile != null) {
					try {
						file = imageFile;
						setImageViewFromFile(imageFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		return button;
	}

	private Slider buildThresholdSlider(final int defaultValue) {
		Slider slider = new Slider();
		slider.setMin(0);
		slider.setMax(100);
		slider.setValue(40);
		slider.setShowTickLabels(true);
		slider.setShowTickMarks(true);
		slider.setMajorTickUnit(50);
		slider.setMinorTickCount(5);
		slider.setBlockIncrement(10);
		slider.setSnapToTicks(true);
		slider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				binaryImageFactory.setThreshold(newValue.intValue());
				try {
					setImageViewFromFile(file);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		return slider;
	}

	/**
	 * Refresh the image view on the screen from the contents of a file.
	 * 
	 * @param file The file to load
	 * @throws IOException
	 */
	private void setImageViewFromFile(final File file) throws IOException {
		final BufferedImage bufferedImage = ImageIO.read(file);

		final BufferedImage outlinedImage = this.imageAnalyzer.outlineDistinctObjects(bufferedImage);

		final Image image = SwingFXUtils.toFXImage(outlinedImage, null);
		imageView.setImage(image);
	}

	private void setImageViewFromImage(final BufferedImage newImage) {
		final BufferedImage outlinedImage = this.imageAnalyzer.outlineDistinctObjects(newImage);

		final Image image = SwingFXUtils.toFXImage(outlinedImage, null);
		imageView.setImage(image);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
