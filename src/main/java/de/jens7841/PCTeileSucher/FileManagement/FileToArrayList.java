package de.jens7841.PCTeileSucher.FileManagement;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class FileToArrayList {

	public static List<String> linesToList(File file) {
		List<String> list = null;
		try {
			list = Files.readAllLines(file.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

}