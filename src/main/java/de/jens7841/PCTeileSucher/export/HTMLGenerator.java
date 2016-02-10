package de.jens7841.PCTeileSucher.export;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HTMLGenerator {

	private File destFolder;
	private String fileType;
	private File imgFolder;

	public HTMLGenerator(File imageFolder, String fileType, File destFolder) {
		if (!imageFolder.isDirectory() || !destFolder.isDirectory()) {
			throw new IllegalStateException("ImageFolder and DestFolder must be a folder!");
		}
		this.imgFolder = imageFolder;
		this.fileType = fileType;
		this.destFolder = destFolder;
	}

	public void generateHTMlPage(String pageName) {

		File htmlFile = new File(destFolder.getAbsolutePath() + "/" + pageName + ".html");
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new FileWriter(htmlFile));

			writer.println("<style type=\"text/css\">");
			writer.println("img{width:100%;}");
			writer.println("table{border-collapse:collapse;border-spacing:0;width:100%;}");
			writer.println(
					"td{font-family:Arial, sans-serif;font-size:14px;padding:10px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;}");
			writer.println("td{text-align:center;vertical-align:top}");
			writer.println("</style>");
			writer.println("<table>");

			File[] files = imgFolder.listFiles();
			int i = 0;
			for (File file : files) {
				if (file.getName().endsWith(fileType)) {
					if (i % 2 == 0) {
						writer.println("<tr>");
					}
					Path pathRelative = Paths.get(destFolder.getAbsolutePath())
							.relativize(Paths.get(file.getAbsolutePath()));

					writer.println("<td><img src=\"" + pathRelative + "\"></td>");
					if (i % 2 == 1) {
						writer.println("</tr>");
					}
					i++;
				}
			}
			writer.print("</table>");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

}