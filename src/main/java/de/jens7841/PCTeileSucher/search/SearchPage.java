package de.jens7841.PCTeileSucher.search;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public abstract class SearchPage {

	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36";

	/**
	 * @param doc
	 *            Das übergebene {@link Document} ist entweder die ShopSeite
	 *            oder eine Zwischenseite mit link zur ShopSeite (Siehe
	 *            Geizhals.de) Dies Kommt auf die spezielle SearchPage an. Es
	 *            kann sich hierbei z.B. um eine Vergleichseeite wie Geizhals
	 *            oder direkt einen Shop wie MindFactory handeln. Hierbei muss
	 *            der Programmierer aufpassen (!)
	 * 
	 * @return {@link Double} den Preis als double der aus der Jeweiligen
	 *         übergebenen Seite ausgelesen wird
	 */
	public abstract double getPrice(Document doc);

	/**
	 * @param searchString
	 *            Der Text der Normalerweise in der Suche der Seite eingegeben
	 *            wird
	 * @return {@link SearchURL} gibt das Object {@link SearchURL} zurück in
	 *         diesem finden sich nützliche Strings wie die URL zur Suche bzw
	 *         auch der searchString der übergeben wurde
	 */
	public abstract SearchURL stringToSearchURL(String searchString);

	/**
	 * @param doc
	 *            Übergeben wird das {@link Document} der jeweiligen Suche oder
	 *            die Zwischenseite mit link zum Shop/Artikel. Dies
	 *            unterscheidet sich wie bei getPrice() durch die Art der
	 *            SearchPage. Also ob es sich um einen Shop handelt oder eine
	 *            Vergleichsseite mit link zum Shop wie Geizhals
	 * @return Die URL als {@link String} zur ShopSeite des Artikels
	 */
	public abstract String getArticleURL(Document doc);

	/**
	 * @param doc
	 *            Übergeben wird die Seite mit der Suche
	 * @return {@link String} man bekommt die URL der Seite, die aufgerufen wird
	 *         nachdem man einen Artikel gesucht hat. Es kann sich hierbei um
	 *         die direkte ShopSeite oder um eine zwischenseite handeln. Dies
	 *         unterscheidet sich wie bei getPrice() durch die Art der
	 *         SearchPage. Also ob es sich um einen Shop handelt oder eine
	 *         Vergleichsseite mit link zum Shop wie Geizhals
	 */
	public abstract String getLinkFromSearch(Document doc);

	/**
	 * @param searchString
	 *            Man übergibt den String mit den Suchgebriffen
	 * @return und bekommt ein {@link Document} mit der Seite der Suche zurück
	 */
	public abstract Document getSearchPage(String searchString);

	/**
	 * @param url
	 *            Die URL als String auf die connected werden soll
	 * @return ein {@link Document} mit der Aufgerufenen Seite
	 * @throws IOException
	 *             by error
	 */
	public static Document connect(String url) throws IOException {
		// SYSO
		System.out.println("Connecting to: " + url);
		return Jsoup.connect(url).userAgent(USER_AGENT).get();
	}

}