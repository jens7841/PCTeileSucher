package de.jens7841.PCTeileSucher.search;

public class Article {

	private String name;
	private double price;
	private String shopURL;
	private long createTimestamp;

	public Article(String name, double price, String shopURL, long timeStamp) {
		this.name = name;
		this.price = price;
		this.shopURL = shopURL;
		this.createTimestamp = timeStamp;
	}

	public Article(String name, double price, String shopURL) {
		this(name, price, shopURL, System.currentTimeMillis());
	}

	public long getCreateTimestamp() {
		return createTimestamp;
	}

	public String getName() {
		return name;
	}

	public double getPrice() {
		return price;
	}

	public String getShopURL() {
		return shopURL;
	}

}