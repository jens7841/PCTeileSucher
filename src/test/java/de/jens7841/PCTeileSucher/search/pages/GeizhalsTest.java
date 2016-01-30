package de.jens7841.PCTeileSucher.search.pages;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.jsoup.nodes.Document;
import org.junit.Test;

import de.jens7841.PCTeileSucher.search.SearchPage;

public class GeizhalsTest {

	private static Geizhals geizhals = new Geizhals();
	private static String searchString = "Intel Core i7 5960x";
	private static Document searchPage = geizhals.getSearchPage(searchString);
	private static String LINK_FROM_SEARCH = geizhals.getLinkFromSearch(searchPage);
	private static Document articlePage;

	static {
		try {
			articlePage = SearchPage.connect(LINK_FROM_SEARCH);
		} catch (IOException e) {
			fail("Exception");
			e.printStackTrace();
		}
	}

	@Test
	public void testGetPrice() throws IOException {
		double price = geizhals.getPrice(articlePage);
	}

	@Test
	public void testGetArticleURL() {
		geizhals.getArticleURL(articlePage);
	}

}