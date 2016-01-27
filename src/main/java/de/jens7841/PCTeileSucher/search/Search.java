package de.jens7841.PCTeileSucher.search;

import java.io.IOException;

import org.jsoup.nodes.Document;

public class Search {

	private SearchPage page;
	private String searchString;
	private Document searchPage;
	private boolean isLoaded;
	private Document articlePage;
	private double price;
	private String shopURL;

	public Search(SearchPage page, String searchString) {
		this.page = page;
		this.searchString = searchString;
		isLoaded = false;
	}

	public double getPrice() {
		initializePage();
		return price;
	}

	public String getShopURL() {
		initializePage();
		return shopURL;
	}

	private void initializePage() {
		if (!isLoaded) {
			searchPage = page.getSearchPage(searchString);
			try {
				articlePage = page.connect(page.getLinkFromSearch(searchPage));
			} catch (IOException e) {
				e.printStackTrace();
			}
			price = page.getPrice(articlePage);
			shopURL = page.getShopURL(articlePage);
			isLoaded = true;
		}
	}

	public String getSearchString() {
		return searchString;
	}

}