package de.jens7841.PCTeileSucher;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import de.jens7841.PCTeileSucher.FileManagement.CSVCreator;
import de.jens7841.PCTeileSucher.search.pages.Geizhals;

public class Main {

	private static boolean timedRunRunning;
	private static int timeInSecounds;
	private static long timeInMillis;

	public static void main(String[] args) throws Throwable {
		timedRunRunning = false;

		String command = "";
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		File read = getFile("Datei mit Suchanfragen", false, false);
		File write = getFile("Datei in der die aktuellen Ergebnisse gespeichert werden sollen", true, false);
		File statsFolder = getFile("Ordner, in der die Statistiken gespeichert werden sollen", true, true);

		// File read = new File("teile.txt");
		// File write = new File("out.csv");
		// File statsFolder = new File("stats");

		while (!command.equalsIgnoreCase("end")) {
			command = reader.readLine();

			if (command.equalsIgnoreCase("run")) {
				new Thread() {
					@Override
					public void run() {
						long start = System.currentTimeMillis();
						CSVCreator creator = new CSVCreator(new Geizhals(), read, write, statsFolder);
						creator.startOutput();
						System.out.println("Finished run in " + (System.currentTimeMillis() - start) / 1000.0);
					};
				}.start();
			}
			if (command.equalsIgnoreCase("runthread")) {
				new Thread() {
					@Override
					public void run() {
						long start = System.currentTimeMillis();
						CSVCreator creator = new CSVCreator(new Geizhals(), read, write, statsFolder);
						creator.startThreadedOutput();
						System.out.println("Finished threaded run in " + (System.currentTimeMillis() - start) / 1000.0);
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
							CSVCreator creator = new CSVCreator(new Geizhals(), read, write, statsFolder);
							long start = System.currentTimeMillis();
							creator.startThreadedOutput();
							System.out.println(
									"Finished threaded run in " + (System.currentTimeMillis() - start) / 1000.0);
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

			}

			if (command.equalsIgnoreCase("end") || command.equalsIgnoreCase("exit")
					|| command.equalsIgnoreCase("stop")) {
				timedRunRunning = false;
				command = "end";
			}

		}

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

	private static File getFile(String out, boolean create, boolean isDirectory) {

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		File file = null;

		do {
			System.out.print(out + ": ");

			try {
				file = new File(in.readLine());
				if (create && !isDirectory)
					file.createNewFile();

				if (create && isDirectory)
					file.mkdirs();

			} catch (IOException e) {
			}
		} while (file == null || !file.exists());
		return file;
	}
}