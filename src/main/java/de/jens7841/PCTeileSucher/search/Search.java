package de.jens7841.PCTeileSucher.search;

import java.io.IOException;

import org.jsoup.nodes.Document;

public class Search {

	private SearchPage page;
	private String searchString;
	private Document searchPage;
	private boolean isLoaded;
	private Document articlePage;

	private Article article;

	public Search(SearchPage page, String searchString) {
		this.page = page;
		this.searchString = searchString;
		isLoaded = false;
	}

	public void updateData() {
		searchPage = page.getSearchPage(searchString);
		try {
			articlePage = SearchPage.connect(page.getLinkFromSearch(searchPage));
		} catch (IOException e) {
			e.printStackTrace();
		}
		double price = page.getPrice(articlePage);
		String shopURL = page.getArticleURL(articlePage);
		isLoaded = true;

		this.article = new Article(searchString, price, shopURL);

	}

	public Article getArticle() {
		initializePage();
		return article;
	}

	private void initializePage() {
		if (!isLoaded) {
			updateData();
		}
	}

}