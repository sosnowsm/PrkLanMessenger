package prk;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Base64;
import javax.imageio.ImageIO;

/**
 * @author Piotr Sawicki
 * Klasa klienta
 */
public class Client {
	private GUI gui = new GUI();
	private String fileName;
	private File saveFile;

	/**
	 * Metoda create() tworzy klienta za pomoc¹ klasy GUI oraz w nowym w¹tku pozwala
	 * na odbieranie wiadomoœci od serwera.
	 */
	public void create() {

		gui.getLocalIP();
		gui.getConfig().getServerIP();
		gui.makeGUI();
		gui.setNick();
		gui.getConfig().configureCommunication();
		Thread recipientThread = new Thread(new CommunicatsRecipient());
		recipientThread.start();
	}

	/**
	 * Klasa wewnêtrzna odbiorcy wiadomoœci
	 */
	public class CommunicatsRecipient implements Runnable {
		String message2;
		/**
		 * Metoda run() na podstawie przedrostka stwierdza typ odbieranej wiadomoœci i
		 * odpowiednio wyœwietla wiadomoœæ tekstow¹, obrazek lub link do pliku z
		 * mo¿liwoœci¹ zapisu
		 */
		public void run() {
			try {
				while ((message2 = gui.getConfig().getReader().readLine()) != null) {
					if (message2.length() > 5 && (message2.substring(0, 6)).equals("(name)")) {
						fileName = message2.substring(6);
					} else if (message2.length() > 5 && (message2.substring(0, 5)).equals("(fil)")) {
						String mess2Encode = message2.substring(5);
						gui.getEncodedStrings().put(fileName, mess2Encode);
						gui.getDoc().insertAfterEnd(gui.getDoc().getCharacterElement(gui.getDoc().getLength()),
								"<a href=" + fileName + ">" + fileName + "</a><br>");
					} else if (message2.length() > 5 && (message2.substring(0, 5)).equals("(img)")) {
						String mess2Encode = message2.substring(5);
						byte[] decodedBytes = Base64.getDecoder().decode(mess2Encode);
						File image = new File(fileName);
						FileOutputStream out = new FileOutputStream(image);
						out.write(decodedBytes);
						out.close();
						BufferedImage img = ImageIO.read(image);
						int szerokosc;
						if (img.getWidth() > 360) {
							szerokosc = 360;
						} else {
							szerokosc = img.getWidth();
						}
						int wysokosc = szerokosc * img.getHeight() / img.getWidth();
						gui.getDoc().insertAfterEnd(gui.getDoc().getCharacterElement(gui.getDoc().getLength()),
								"<img src=file:///" + image.getAbsolutePath().replaceAll(" ", "%20") + " width=\""
										+ szerokosc + "\" height=\"" + wysokosc + "\"><br>");
						System.out.println(wysokosc + " " + img.getWidth() + " " + img.getHeight());

					} else {
						gui.getDoc().insertAfterEnd(gui.getDoc().getCharacterElement(gui.getDoc().getLength()),
								message2);
					}
					gui.getIncomingMessages2().setCaretPosition(gui.getDoc().getLength());
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		public String getMessage2() {
			return message2;
		}
		public void setMessage2(String message2) {
			this.message2 = message2;
		}
	}

	public String getFileName() {
		return fileName;
	}

	public File getSaveFile() {
		return saveFile;
	}
}
