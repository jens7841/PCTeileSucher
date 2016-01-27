package de.jens7841.PCTeileSucher.search;

public class SearchURL {

	private String searchString;
	private String parsedString;
	private String url;

	public SearchURL(String searchString, String parsedString, String url) {
		this.searchString = searchString;
		this.parsedString = parsedString;
		this.url = url;
	}

	public String getParsedString() {
		return parsedString;
	}

	public String getSearchString() {
		return searchString;
	}

	public String getUrl() {
		return url;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SearchURL) {
			SearchURL url = (SearchURL) obj;
			if (url.getParsedString().equals(parsedString) && url.getSearchString().equals(searchString)
					&& url.getParsedString().equals(parsedString)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "SearchURL [searchString=" + searchString + ", parsedString=" + parsedString + ", url=" + url + "]";
	}

}