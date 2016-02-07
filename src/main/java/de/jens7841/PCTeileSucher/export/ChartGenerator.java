package de.jens7841.PCTeileSucher.export;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class ChartGenerator {

	private File file;

	public ChartGenerator(File csv) {

		if (!csv.exists() || csv.isDirectory()) {
			throw new IllegalStateException("csv must be a file!");
		}

		this.file = csv;
	}

	public static void main(String[] args) {
		JFrame jFrame = new JFrame();
		File csv = new File("src/main/resources/teilDemo.csv");
		ChartPanel panel = new ChartPanel(new ChartGenerator(csv).getLineChart());

		jFrame.setContentPane(panel);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setSize(800, 500);
		jFrame.setVisible(true);
	}

	public JFreeChart getLineChart() {
		TimeSeriesCollection dataset = new TimeSeriesCollection();

		JFreeChart lineChart = ChartFactory.createTimeSeriesChart("Preisentwicklung", "Zeit", "Preis in €", dataset,
				true, true, false);
		try {
			List<String> lines = Files.readAllLines(file.toPath());
			TimeSeries series = new TimeSeries(file.getName());
			for (String line : lines) {
				String[] split = line.split("\\;");
				double price = Double.parseDouble(split[0].replace(",", "."));
				// FIXME
				// http://stackoverflow.com/questions/5522575/how-can-i-update-a-jfreecharts-appearance-after-its-been-made-visible/5522583#5522583
				// ChartPanelDemo

				// dataset.addValue(price, file.getName(), split[1]);

				Date time = new Date();
				time.setTime(Long.parseLong(split[1]));
				series.add(new Minute(time), price + " €");

			}
			dataset.addSeries(series);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return lineChart;
	}

}