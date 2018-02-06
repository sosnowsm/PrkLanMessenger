package prk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

/**
 * @author Marcin Sosnowski
 * Klasa pozwalaj¹ca na konfiguracjê komunikacji
 */
public class ConfigureCommunication implements ServerIP {

	private PrintWriter writer;
	private Socket socket;
	private BufferedReader reader;
	private String ipSerwera;

	/**
	 * Metoda pozwalaj¹ca na wprowadzenie do klienta adresu serwera
	 */
	@Override
	public void getServerIP() {

		boolean serverOK = false;
		while (!serverOK) {

			ipSerwera = JOptionPane.showInputDialog("Enter Server IP ", "127.0.0.1");
			try {
				System.out.println(ipSerwera);

				while (!ipSerwera.equals("")) {
					try {

						Socket test = new Socket(ipSerwera, 5000);
						test.close();
						serverOK = true;
						return;
					} catch (UnknownHostException e) {
						JOptionPane.showMessageDialog(null, "Unknown host exception");
						serverOK = false;
						ipSerwera = "";
						e.printStackTrace();
					} catch (IOException e) {
						JOptionPane.showMessageDialog(null, "I/O Exception");
						serverOK = false;
						ipSerwera = "";
						e.printStackTrace();
					}
				}
			} catch (NullPointerException e) {
				JOptionPane.showMessageDialog(null, "Null Pointer Exception");
				ipSerwera = "";
				e.printStackTrace();
			}
		}

	}

	/**
	 * Metoda tworzy gniazdo oraz czytelnika i pisarza wiadomoœci
	 */
	public void configureCommunication() {

		try {
			socket = new Socket(ipSerwera, 5000);
			InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
			reader = new BufferedReader(inputStreamReader);
			writer = new PrintWriter(socket.getOutputStream());
			System.out.println("Communication Configured");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public PrintWriter getWriter() {
		return writer;
	}

	public Socket getSocket() {
		return socket;
	}

	public BufferedReader getReader() {
		return reader;
	}

	public String getIpSerwera() {
		return ipSerwera;
	}
}
