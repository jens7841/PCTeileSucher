package de.jens7841.PCTeileSucher.FileManagement;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SynchronizedFileWriter extends Thread {

	private File file;
	private FileWriter fileWriter;

	public SynchronizedFileWriter(File file) {
		this.file = file;
	}

	public synchronized void append(CharSequence csq) {

		if (fileWriter == null) {
			try {
				fileWriter = new FileWriter(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		boolean error = false;
		int failures = 0;
		do {

			try {
				fileWriter.append(csq);
				error = false;
			} catch (IOException e) {
				error = true;
				failures++;
				e.printStackTrace();
				try {
					fileWriter = new FileWriter(file);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

		} while (error && failures < 2);
	}

	public synchronized void close() {
		try {
			if (fileWriter != null)
				fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}