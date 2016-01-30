package de.jens7841.PCTeileSucher.export;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class CSVGenerator {

	private static final char CSV_SEPARATOR = ';';
	private boolean append;
	private File file;
	private Map<Thread, StringBuilder> builders;

	public CSVGenerator(File file, boolean append) {

		if (file.isDirectory())
			throw new IllegalStateException("File can not be a direcotry!");

		this.file = file;
		this.append = append;
		this.builders = new HashMap<>();
	}

	public CSVGenerator add(String string) {
		StringBuilder builder = getBuilder();
		builder.append(string);
		builder.append(CSV_SEPARATOR);
		return this;
	}

	public CSVGenerator add(int i) {
		add(i + "");
		return this;
	}

	private StringBuilder getBuilder() {

		Thread currentThread = Thread.currentThread();
		StringBuilder builder = builders.get(currentThread);

		if (builder == null) {
			builder = new StringBuilder();
			builders.put(currentThread, builder);
		}

		return builder;
	}

	public synchronized void writeToFile() throws IOException {
		try {
			createFile();

			PrintWriter printWriter = new PrintWriter(new FileWriter(file, append));

			printWriter.print(getBuilder().toString() + "\n");

			printWriter.close();
		} finally {
			builders.remove(Thread.currentThread());
		}
	}

	private void createFile() {
		if (!file.exists()) {
			file.mkdirs();
			file.delete();
		}
	}

}