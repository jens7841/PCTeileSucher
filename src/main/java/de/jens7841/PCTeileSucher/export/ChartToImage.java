package de.jens7841.PCTeileSucher.export;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.jfree.chart.JFreeChart;

public class ChartToImage {

	private JFreeChart chart;
	private File outputFolder;
	private String fileName;

	public ChartToImage(JFreeChart chart, File outputFolder, String fileName) {

		if (!outputFolder.isDirectory()) {
			throw new IllegalStateException("outputFolder must be a Folder!");
		}
		this.chart = chart;
		this.outputFolder = outputFolder;
		this.fileName = fileName;
	}

	public void saveImage() {

		FileOutputStream out = null;
		try {
			out = new FileOutputStream(new File(outputFolder.getAbsolutePath() + "/" + fileName + ".png"));

			BufferedImage image = chart.createBufferedImage(900, 500);

			ImageIO.write(image, "png", out);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

}