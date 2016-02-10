package de.jens7841.PCTeileSucher;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JFrame;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import de.jens7841.PCTeileSucher.export.ChartGenerator;
import de.jens7841.PCTeileSucher.export.ChartToImage;
import de.jens7841.PCTeileSucher.export.HTMLGenerator;
import de.jens7841.PCTeileSucher.search.SearchFromFileToCSV;
import de.jens7841.PCTeileSucher.search.SearchPage;
import de.jens7841.PCTeileSucher.search.pages.Geizhals;

public class Main {

	private static boolean timedRunRunning;
	private static int timeInSecounds;
	private static long timeInMillis;

	public static void main(String[] args) throws Throwable {

		timedRunRunning = false;

		String command = "";
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		File read = getFile("Datei mit Suchanfragen", false, false, true, "teile.txt");
		File write = getFile("Datei mit der Aktuellen Übersicht", true, false, false, "tabelle.csv");
		File statsFolder = getFile("Ordner für Statistiken", true, true, false, "stats");
		File htmlFolder = getFile("Ordner für die HTML-Seite", true, true, false, "html");
		deleteFile(htmlFolder);
		htmlFolder.mkdirs();

		SearchPage page = new Geizhals();

		// File read = new File("teile.txt");
		// File write = new File("out.csv");
		// File statsFolder = new File("stats");
		SearchFromFileToCSV search = new SearchFromFileToCSV(read, write, statsFolder, page);
		while (!command.equalsIgnoreCase("end")) {
			command = reader.readLine();

			if (command.equalsIgnoreCase("run")) {
				new Thread() {
					@Override
					public void run() {

						search.performFullSearch();

						System.out.println("Finished run in " + (search.getLastSearchDuration()) / 1000.0);
					};
				}.start();
			}
			if (command.equalsIgnoreCase("runthread")) {
				new Thread() {
					@Override
					public void run() {
						search.performFullThreadedSearch();
						System.out.println("Finished threaded run in " + (search.getLastSearchDuration()) / 1000.0);
					};
				}.start();
			}

			if (command.equalsIgnoreCase("timedrun")) {

				boolean correctInput = false;
				timeInSecounds = 0;
				do {
					try {
						System.out.print("Zeit des timedruns in Sekunden: ");
						timeInSecounds = Integer.parseInt(reader.readLine());
						correctInput = true;
					} catch (NumberFormatException e) {
					}
				} while (!correctInput);

				timeInMillis = timeInSecounds * 1000;

				timedRunRunning = true;
				new Thread() {
					@Override
					public void run() {
						while (timedRunRunning) {
							search.performFullThreadedSearch();

							generateHtml(statsFolder, htmlFolder);

							System.out.println("Finished threaded run in " + (search.getLastSearchDuration()) / 1000.0);
							System.err.println("Waiting " + timeInSecounds + " sec ("
									+ Math.round((timeInSecounds / 60.0) * 100.) / 100. + "min "
									+ Math.round((timeInSecounds / 60.0 / 60.0) * 100.) / 100. + "h)");
							try {
								Thread.sleep(timeInMillis);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					};
				}.start();
			}

			if (command.equalsIgnoreCase("del")) {
				File f = null;

				while (f == null || !f.exists()) {
					System.out.print("Delete: ");
					f = new File(reader.readLine());
				}

				deleteFile(f);
				System.out.println("Delete successful!");
			}

			if (command.equalsIgnoreCase("html") || command.equalsIgnoreCase("createhtml")) {
				generateHtml(statsFolder, htmlFolder);
			}

			if (command.equalsIgnoreCase("diagram") || command.equalsIgnoreCase("diagramm")
					|| command.equalsIgnoreCase("chart") || command.equalsIgnoreCase("getchart")
					|| command.equalsIgnoreCase("createchart")) {

				ChartPanel lineChart = new ChartGenerator(statsFolder).getLineChart();
				JFrame frame = new JFrame("Preisentwicklung");

				frame.setContentPane(lineChart);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setSize(800, 500);
				frame.setVisible(true);

			}

			if (command.equalsIgnoreCase("end") || command.equalsIgnoreCase("exit")
					|| command.equalsIgnoreCase("stop")) {
				timedRunRunning = false;
				command = "end";
				System.out.println("Stopping now!");
				Thread.sleep(1500);
				System.exit(0);
			}

		}

	}

	private static void generateHtml(File statsFolder, File htmlFolder) {
		File[] files = statsFolder.listFiles();
		for (File file : files) {
			JFreeChart chart = new ChartGenerator(file).getLineChart().getChart();

			new ChartToImage(chart, htmlFolder, file.getName().substring(0, file.getName().lastIndexOf('.')))
					.saveImage();
		}

		new HTMLGenerator(htmlFolder, ".png", htmlFolder).generateHTMlPage("index");
		System.out.println("Html page is ready!");
	}

	private static String getInput() {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String line = "";
		while (line.isEmpty()) {
			try {
				line = in.readLine();
			} catch (IOException e) {
				e.printStackTrace();
				line = "fail";
			}
		}
		return line;
	}

	public static void deleteFile(File file) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File f : files) {
				if (f.isDirectory())
					deleteFile(f);
				f.delete();
			}
		}
		file.delete();
	}

	private static File getFile(String out, boolean create, boolean isDirectory, boolean mustExist,
			String defaultValue) {

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		File file = null;

		boolean successful = true;
		do {
			successful = true;
			System.out.print(out + ": ");

			try {
				String line = in.readLine();
				if (line.isEmpty() && !defaultValue.isEmpty()) {
					file = new File(defaultValue);
				} else {
					file = new File(line);
				}

				if (create) {
					file.mkdirs();
					if (!isDirectory) {
						file.delete();
						file.createNewFile();
					}
				}

				if (mustExist) {
					if (isDirectory && !file.isDirectory()) {
						successful = false;
					}
					if (!isDirectory && file.isDirectory()) {
						successful = false;
					}
					if (!file.exists()) {
						successful = false;
					}
				}

			} catch (IOException e) {
			}
		} while (!successful);
		return file;
	}
}