package com.aidenkeating.imageanalysis.ui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.aidenkeating.imageanalysis.ImageAnalysisReport;
import com.aidenkeating.imageanalysis.ImageAnalyzer;
import com.aidenkeating.imageanalysis.config.Config;
import com.aidenkeating.imageanalysis.config.ImageResizeConfig;
import com.aidenkeating.imageanalysis.config.MemberAnalysisConfig;
import com.aidenkeating.imageanalysis.config.SwarmAnalysisConfig;
import com.aidenkeating.imageanalysis.image.BinaryImageFactory;
import com.aidenkeating.imageanalysis.image.GrayscaleBinaryImageFactory;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ImageAnalysisApplication extends Application {
	private final FileChooser fileChooser = new FileChooser();
	private final DirectoryChooser dirChooser = new DirectoryChooser();
	private BufferedImage image;

	private ImageView imageView = new ImageView();
	private ImageAnalyzer imageAnalyzer;

	@Override
	public void start(Stage primaryStage) {
		final Config config = new Config(new MemberAnalysisConfig(true), new SwarmAnalysisConfig(true),
				new ImageResizeConfig(true));
		final BinaryImageFactory binaryImageFactory = new GrayscaleBinaryImageFactory(130);
		this.imageAnalyzer = new ImageAnalyzer(binaryImageFactory, config);

		final BorderPane mainPane = new BorderPane();

		// Setup load button.
		final BorderPane controlPane = new BorderPane();
		final Button imageSelectButton = buildImageSelectButton(primaryStage, fileChooser);
		controlPane.setLeft(imageSelectButton);
		final Button exportReportButton = buildReportExportButton(primaryStage, dirChooser);
		controlPane.setRight(exportReportButton);
		mainPane.setTop(controlPane);

		// Setup central ImageView.
		imageView.setPreserveRatio(true);
		imageView.setFitHeight(400);
		imageView.setFitWidth(700);
		mainPane.setCenter(imageView);

		// Setup sliders.
		final BorderPane thresholdPane = new BorderPane();
		final Label thresholdLabel = new Label("Threshold");
		thresholdPane.setBottom(thresholdLabel);
		final Slider thresholdSlider = buildThresholdSlider(20);
		thresholdPane.setCenter(thresholdSlider);
		mainPane.setLeft(thresholdPane);

		final BorderPane noiseReductionPane = new BorderPane();
		final Label noiseReductionLabel = new Label("Noise reduction");
		noiseReductionPane.setBottom(noiseReductionLabel);
		final Slider noiseReductionSlider = buildNoiseReductionSlider(1);
		noiseReductionPane.setCenter(noiseReductionSlider);
		mainPane.setRight(noiseReductionPane);

		// Show scene.
		primaryStage.setScene(new Scene(mainPane));
		primaryStage.show();

		// Open the dialog immediately if a file isn't selected (which it
		// probably won't be).
		if (image == null) {
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
						image = ImageIO.read(imageFile);
						refreshWithImage(image);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		return button;
	}

	/**
	 * Create a button for exporting an image analysis report.
	 */
	private Button buildReportExportButton(final Stage stage, final DirectoryChooser dirChooser) {
		final Button button = new Button("Export report");
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (image != null) {
					final File dir = dirChooser.showDialog(stage);
					if (dir != null) {
						try {
							final ImageAnalysisReport report = imageAnalyzer.compileReport(image);
							exportAnalysisReportToFile(report, dir.getAbsolutePath());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}

		});
		return button;
	}

	private Slider buildThresholdSlider(final int defaultValue) {
		final Slider slider = buildGenericSlider(defaultValue, 0, 255);
		slider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				imageAnalyzer.getBinaryImageFactory().setThreshold(newValue.intValue());
				try {
					refreshWithImage(image);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
		return slider;
	}

	private Slider buildNoiseReductionSlider(final int defaultValue) {
		final Slider slider = buildGenericSlider(defaultValue, 1, 100);
		slider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				imageAnalyzer.getConfig().getMemberConfig().setNoiseReduction(newValue.intValue());
				try {
					refreshWithImage(image);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
		return slider;
	}

	private Slider buildGenericSlider(final int initialVal, final int minVal, final int maxVal) {
		final Slider slider = new Slider();
		slider.setMin(minVal);
		slider.setMax(maxVal);
		slider.setValue(initialVal);
		slider.setShowTickLabels(true);
		slider.setShowTickMarks(true);
		slider.setMajorTickUnit(50);
		slider.setMinorTickCount(5);
		slider.setBlockIncrement(10);
		slider.setSnapToTicks(true);
		slider.setOrientation(Orientation.VERTICAL);
		return slider;
	}

	/**
	 * Refresh the image view on the screen from the contents of a file.
	 * 
	 * @param file The file to load
	 * @throws IOException
	 */
	private void refreshWithImage(final BufferedImage image) throws IOException {
		final ImageAnalysisReport report = this.imageAnalyzer.compileReport(image);

		final Image renderableImage = SwingFXUtils.toFXImage(report.getOutlinedImage(), null);
		imageView.setImage(renderableImage);
	}

	private void exportAnalysisReportToFile(final ImageAnalysisReport report, final String exportDir)
			throws IOException {
		ImageAnalyzer.exportReportToFile(report, exportDir);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
