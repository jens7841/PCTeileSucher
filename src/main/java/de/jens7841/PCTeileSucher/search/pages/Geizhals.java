package de.jens7841.PCTeileSucher.search.pages;

import java.io.IOException;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import de.jens7841.PCTeileSucher.search.SearchPage;
import de.jens7841.PCTeileSucher.search.SearchURL;

public class Geizhals extends SearchPage {

	private static final String GEIZHALS_PAGE_PREFIX = "https://geizhals.de/";
	private static final String GEIZHALS_SEARCH_PREFIX = GEIZHALS_PAGE_PREFIX + "?fs=";

	@Override
	public double getPrice(Document articlePage) {

		Element priceElement = articlePage.getElementById("offer__price-0").getElementsByClass("gh_price").first()
				.getElementsByClass("gh_price").first();

		String priceString = priceElement.text();
		priceString = priceString.replaceAll("\\€", "");
		priceString = priceString.replaceAll("\\ ", "");
		priceString = priceString.replaceAll("\\,", ".");
		priceString = priceString.replaceAll("\\-", "");

		return Double.parseDouble(priceString);

	}

	@Override
	public SearchURL stringToSearchURL(String searchString) {
		String parsedSearch = searchString.replaceAll("\\ +", " ");
		parsedSearch = parsedSearch.replaceAll("\\ ", "+");
		return new SearchURL(searchString, parsedSearch, GEIZHALS_SEARCH_PREFIX + parsedSearch);
	}

	@Override
	public String getShopURL(Document doc) {

		return GEIZHALS_PAGE_PREFIX + doc.getElementById("offer__price-0").getElementsByClass("offer__clickout").first()
				.select("a[href]").attr("href");

	}

	@Override
	public String getLinkFromSearch(Document doc) {
		Element elementById = doc.getElementById("gh_content_wrapper").getElementsByClass("gh_fstbl").first()
				.select("a[href]").first();
		String link = elementById.attr("href");

		return GEIZHALS_PAGE_PREFIX + link;
	}

	@Override
	public Document getSearchPage(String searchString) {
		SearchURL searchurl = stringToSearchURL(searchString);
		Document doc = null;
		try {
			doc = connect(searchurl.getUrl());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}

}