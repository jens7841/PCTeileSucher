package de.jens7841.PCTeileSucher.FileManagement;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import de.jens7841.PCTeileSucher.search.Search;
import de.jens7841.PCTeileSucher.search.SearchPage;

public class CSVCreator {

	private File statsFolder;
	private SearchPage page;
	private File writeFile;
	private File readFile;
	private long scanStartTime;

	private SynchronizedFileWriter tableFileWriter = null;

	public CSVCreator(SearchPage page, File read, File write, File statsFolder) {

		if (!read.isFile() || write.isDirectory() || !statsFolder.isDirectory())
			throw new IllegalArgumentException(
					"Read File and Write File must be a File and statsFolder must be a Direcory!");

		this.statsFolder = statsFolder;
		this.page = page;
		this.readFile = read;
		this.writeFile = write;
		this.tableFileWriter = new SynchronizedFileWriter(write);

	}

	public synchronized void startOutput() {

		scanStartTime = System.currentTimeMillis();

		statsFolder.mkdirs();

		List<String> linesToList = FileToArrayList.linesToList(readFile);

		try {

			{ // Datei leeren
				FileWriter w = new FileWriter(writeFile);
				w.close();
			}

			for (String searchString : linesToList) {
				performSearch(new Search(page, searchString.replaceAll("\\/", " ")));
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (tableFileWriter != null) {
				tableFileWriter.close();
			}
		}
	}

	public synchronized void startThreadedOutput() {

		scanStartTime = System.currentTimeMillis();

		statsFolder.mkdirs();

		List<String> linesToList = FileToArrayList.linesToList(readFile);
		CountDownLatch latch = new CountDownLatch(linesToList.size());

		try {

			{ // Datei leeren
				FileWriter w = new FileWriter(writeFile);
				w.close();
			}

			for (String searchString : linesToList) {

				new Thread() {
					@Override
					public void run() {
						try {
							performSearch(new Search(page, searchString.replaceAll("\\/", " ")));
						} finally {
							latch.countDown();
						}
					};
				}.start();

			}

			try {
				latch.await();
				Thread.sleep(400);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (tableFileWriter != null) {
				tableFileWriter.close();
			}
		}
	}

	private void performSearch(Search search) {
		try {
			FileWriter stats = new FileWriter(statsFolder.getAbsolutePath() + "/" + search.getSearchString() + ".csv",
					true);
			stats.append(search.getPrice() + ";");
			stats.append(scanStartTime + "\n");
			stats.close();

			tableFileWriter.append(search.getSearchString() + ";"
					+ new String(search.getPrice() + "").replaceAll("\\.", ",") + ";" + search.getShopURL() + "\n");
		} catch (IOException e) {
			System.err.println(search.getSearchString());
			e.printStackTrace();
		}
	}

}