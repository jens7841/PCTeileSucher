package de.jens7841.PCTeileSucher.search;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import de.jens7841.PCTeileSucher.export.CSVGenerator;

public class SearchFromFileToCSV {

	private File readFile;
	private File statsFolder;
	private File outputFile;
	private Map<Thread, Long> lastSearchDuration;
	private SearchPage page;

	public SearchFromFileToCSV(File in, File out, File statsFolder, SearchPage page) {
		if (!in.isFile() || out.isDirectory() || !statsFolder.isDirectory())
			throw new IllegalArgumentException(
					"Read File and Write File must be a File and statsFolder must be a Direcory!");
		this.lastSearchDuration = new HashMap<Thread, Long>();
		this.readFile = in;
		this.outputFile = out;
		this.statsFolder = statsFolder;
		this.page = page;
	}

	public void performFullSearch() {
		long startMillis = System.currentTimeMillis();

		statsFolder.mkdirs();
		outputFile.mkdirs();
		outputFile.delete();

		try {
			List<String> lines = Files.readAllLines(readFile.toPath());
			CSVGenerator tableOutput = new CSVGenerator(outputFile, true);
			Calendar calendar = Calendar.getInstance();

			for (String searchString : lines) {
				performSearch(tableOutput, calendar, searchString);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		lastSearchDuration.put(Thread.currentThread(), System.currentTimeMillis() - startMillis);
	}

	public void performFullThreadedSearch() {

		long startMillis = System.currentTimeMillis();

		statsFolder.mkdirs();
		outputFile.mkdirs();
		outputFile.delete();

		try {
			List<String> lines = Files.readAllLines(readFile.toPath());
			CSVGenerator tableOutput = new CSVGenerator(outputFile, true);
			Calendar calendar = Calendar.getInstance();

			CountDownLatch latch = new CountDownLatch(lines.size());

			for (String s : lines) {

				new Thread() {

					@Override
					public void run() {
						try {

							performSearch(tableOutput, calendar, s);
						} finally {
							latch.countDown();
						}
					}

				}.start();

			}

			try {
				latch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		lastSearchDuration.put(Thread.currentThread(), System.currentTimeMillis() - startMillis);

	}

	private void performSearch(CSVGenerator tableOutput, Calendar calendar, String s) {
		String searchString = s;
		searchString = searchString.replaceAll("\\/", " ");
		String pathToStatsFile = statsFolder.getAbsolutePath() + "/" + searchString + ".csv";
		CSVGenerator statsOut = new CSVGenerator(new File(pathToStatsFile), true);

		Article article = new Search(page, searchString).getArticle();

		calendar.setTimeInMillis(article.getCreateTimestamp());
		StringBuilder builder = new StringBuilder();
		builder.append(calendar.get(Calendar.DAY_OF_MONTH));
		builder.append('.');
		builder.append(calendar.get(Calendar.MONTH) + 1);
		builder.append('.');
		builder.append(calendar.get(Calendar.YEAR));
		builder.append(' ');
		builder.append(calendar.get(Calendar.HOUR_OF_DAY));
		builder.append(':');
		builder.append(calendar.get(Calendar.MINUTE));

		String priceString = ("" + article.getPrice()).replaceAll("\\.", ",");

		tableOutput.add(searchString);
		tableOutput.add(priceString);
		tableOutput.add(article.getShopURL());
		tableOutput.add(builder.toString());
		try {
			tableOutput.writeToFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		statsOut.add(priceString);
		statsOut.add(article.getCreateTimestamp());
		statsOut.add(article.getShopURL());
		statsOut.add(builder.toString());
		try {
			statsOut.writeToFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public long getLastSearchDuration() {

		Long l = lastSearchDuration.get(Thread.currentThread());
		if (l == null)
			return 0;
		return l;
	}

}