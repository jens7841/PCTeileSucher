package de.jens7841.PCTeileSucher.export;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

public class ChartGenerator {

	private File file;

	/**
	 * @param csv
	 *            csv kann ein Verzeichnis oder eine Datei sein. Bei einem
	 *            Verzeichnis werden alle Dateien darin gelesen und in ein
	 *            Diagramm gepackt. Bei einer Datei gibt es nur ein Diagramm mit
	 *            einer Datei.
	 */
	public ChartGenerator(File csv) {

		if (!csv.exists()) {
			throw new IllegalStateException("csv must be a file!");
		}

		this.file = csv;
	}

	private ChartPanel createChart() {

		XYDataset roiData = createDataset();
		JFreeChart chart = null;
		if (file.isDirectory()) {
			chart = ChartFactory.createTimeSeriesChart("Preisentwicklung", "Zeit", "Preis", roiData, true, true, false);
		} else {
			chart = ChartFactory.createTimeSeriesChart(file.getName() + " - Preisentwicklung", "Zeit", "Preis", roiData,
					true, true, false);

		}
		XYPlot plot = chart.getXYPlot();
		XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
		renderer.setBaseShapesVisible(true);
		NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.GERMANY);
		currency.setMaximumFractionDigits(0);
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setNumberFormatOverride(currency);
		return new ChartPanel(chart);

	}

	private XYDataset createDataset() {
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		try {
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				for (File file : files) {

					List<String> lines = Files.readAllLines(file.toPath());
					TimeSeries series = new TimeSeries(file.getName());
					for (String line : lines) {
						String[] split = line.split("\\;");
						double price = Double.parseDouble(split[0].replace(",", "."));

						Date time = new Date();
						time.setTime(Long.parseLong(split[1]));
						series.add(new Second(time), price);

					}
					dataset.addSeries(series);
				}
			} else {
				List<String> lines = Files.readAllLines(file.toPath());
				TimeSeries series = new TimeSeries(file.getName());
				for (String line : lines) {
					String[] split = line.split("\\;");
					double price = Double.parseDouble(split[0].replace(",", "."));

					Date time = new Date();
					time.setTime(Long.parseLong(split[1]));
					series.add(new Second(time), price);

				}
				dataset.addSeries(series);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return dataset;
	}

	public ChartPanel getLineChart() {
		ChartPanel chartPanel = createChart();
		return chartPanel;
	}

}