package de.jens7841.PCTeileSucher.export;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.Assert;
import org.junit.Test;

public class CSVGeneratorTest {

	@Test
	public void testSimpleOutput() throws IOException {
		File file = new File("src/test/resources/csvGeneratorTest.csv");
		CSVGenerator csvGenerator = new CSVGenerator(file, false);

		csvGenerator.add("test").add("test1").add("test2").writeToFile();

		byte[] bytes = Files.readAllBytes(file.toPath());

		Assert.assertArrayEquals("test;test1;test2;\n".getBytes(), bytes);
	}

	@Test
	public void testAppendOutput() throws IOException {
		File file = new File("src/test/resources/csvGeneratorAppendTest.csv");
		file.delete();
		CSVGenerator csvGenerator = new CSVGenerator(file, true);

		csvGenerator.add("test").add("test1").add("test2").writeToFile();
		csvGenerator.add("test").add("test1").add("test2").writeToFile();

		byte[] bytes = Files.readAllBytes(file.toPath());

		Assert.assertArrayEquals("test;test1;test2;\ntest;test1;test2;\n".getBytes(), bytes);
	}

}