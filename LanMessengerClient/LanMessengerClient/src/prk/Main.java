package prk;

/**
 * @author Marcin Sosnowski
 * @author Piotr Sawicki
 * Klasa uruchomieniowa klienta komunikatora LAN
 */
public class Main {
	/**
	 * Metoda main() tworzy nowego klienta
	 * 
	 * @param args
	 *            metoda nie przyjmuje argumentów
	 */
	public static void main(String[] args) {
		Client client = new Client();
		client.create();
	}
}
