package de.jens7841.PCTeileSucher.search;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public abstract class SearchPage {

	public abstract double getPrice(Document doc);

	public abstract SearchURL stringToSearchURL(String searchString);

	public abstract String getShopURL(Document doc);

	public abstract String getLinkFromSearch(Document doc);

	public abstract Document getSearchPage(String searchString);

	public Document connect(String url) throws IOException {
		System.out.println(url);
		return Jsoup.connect(url).userAgent("Mozilla").get();
	}

}