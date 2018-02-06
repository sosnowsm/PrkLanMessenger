package prk;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.html.HTMLDocument;

/**
 * @author Piotr Sawicki
 * Klasa GUI ustawia elementy graficznego interfejsu u¿ytkownika
 */
public class GUI implements LocalIP {

	private JLabel ipLabel;
	private JTextPane incomingMessages2;
	private JLabel nickLabel;
	private JLabel nick;
	private JTextField message;
	private String ip;
	private HTMLDocument doc;
	private JFileChooser fileChooser = new JFileChooser();
	private JFileChooser imageChooser = new JFileChooser();
	private FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & PNG Images", "jpg", "png");
	private String encodedString = null;
	private Map<String, String> encodedStrings = new HashMap<>();
	private ConfigureCommunication config = new ConfigureCommunication();

	/**
	 * metoda makeGUI() ustawia elementy graficznego interfejsu u¿ytkownika
	 */
	public void makeGUI() {
		ipLabel = new JLabel(ip);
		JFrame ramka = new JFrame("LAN Messenger Client");
		ramka.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension minimumSize = new Dimension(300, 350);
		ramka.setMinimumSize(minimumSize);

		JPanel mainPanel = new JPanel(new BorderLayout());
		JPanel bottomPanel1 = new JPanel();
		bottomPanel1.setLayout(new BoxLayout(bottomPanel1, BoxLayout.X_AXIS));

		JPanel bottomPanel2 = new JPanel();
		bottomPanel2.setLayout(new BoxLayout(bottomPanel2, BoxLayout.X_AXIS));
		JPanel bottomPanel3 = new JPanel();
		bottomPanel3.setLayout(new BoxLayout(bottomPanel3, BoxLayout.Y_AXIS));

		incomingMessages2 = new JTextPane();
		incomingMessages2.setContentType("text/html");
		incomingMessages2.setEditable(false);
		incomingMessages2.addHyperlinkListener(new LinkListener());
		doc = (HTMLDocument) incomingMessages2.getStyledDocument();
		imageChooser.setFileFilter(filter);
		imageChooser.setAcceptAllFileFilterUsed(false);
		JScrollPane scrollPane = new JScrollPane(incomingMessages2);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		nickLabel = new JLabel("Nick: ");
		nick = new JLabel();
		message = new JTextField(20);
		JButton sendButton = new JButton("Send");
		JButton imageButton = new JButton("Image");
		JButton uploadButton = new JButton("Upload");
		uploadButton.addActionListener(new UploadButtonListener());
		sendButton.addActionListener(new SendButtonListener());
		imageButton.addActionListener(new ImageButtonListener());
		mainPanel.add(ipLabel, BorderLayout.PAGE_START);
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		bottomPanel1.add(nickLabel, bottomPanel1);
		bottomPanel1.add(nick, bottomPanel1);
		bottomPanel1.add(message, bottomPanel1);
		bottomPanel1.add(sendButton, bottomPanel1);
		bottomPanel2.add(imageButton, bottomPanel2);
		bottomPanel2.add(uploadButton, bottomPanel2);
		bottomPanel3.add(bottomPanel1, bottomPanel3);
		bottomPanel3.add(bottomPanel2, bottomPanel3);
		bottomPanel3.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
		ramka.getContentPane().setLayout(new BorderLayout(5, 5));
		ramka.getContentPane().add(BorderLayout.PAGE_START, ipLabel);
		ramka.getContentPane().add(BorderLayout.CENTER, scrollPane);
		ramka.getContentPane().add(BorderLayout.PAGE_END, bottomPanel3);
		ramka.setSize(435, 500);
		ramka.setVisible(true);
	}

	/**
	 * setNick() pozwala ustawiæ nick dla klienta
	 */
	public void setNick() {
		String nick2 = "";
		while (nick2.length() < 3) {
			nick2 = JOptionPane.showInputDialog("Enter nick (min 3 characters)");
		}
		nick.setText(nick2);

	}

	/**
	 * S³uchacz naciœniêcia przycisku wysy³ania wiadomoœci tekstowej
	 */
	public class SendButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			try {
				config.getWriter().println("<b>" + nick.getText() + "</b>" + ": " + message.getText() + "<br>");
				config.getWriter().flush();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			message.setText("");
			message.requestFocus();
		}
	}

	/**
	 * S³uchacz naciœniêcia przycisku wysy³ania obrazka
	 */
	public class ImageButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			int returnVal = imageChooser.showOpenDialog(null); // czemu null?
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File image = new File(imageChooser.getSelectedFile().getPath());
				String name = imageChooser.getSelectedFile().getName();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				InputStream in = null;
				try {
					in = new FileInputStream(image);
					int count;
					byte[] bytes = new byte[8192];
					while ((count = in.read(bytes)) > 0) {
						out.write(bytes, 0, count);
					}
					in.close();
				} catch (FileNotFoundException e) {
					JOptionPane.showMessageDialog(null, "Brak pliku");
				} catch (IOException e) {
					e.printStackTrace();
				}
				String encodedString = Base64.getEncoder().encodeToString(out.toByteArray());
				config.getWriter().println("(name)" + name);
				config.getWriter().println("<b>" + nick.getText() + "</b>" + ": ");
				config.getWriter().println("(img)" + encodedString);
				config.getWriter().flush();
			}
		}
	}

	/**
	 * S³uchacz naciœniêcia przycisku wysy³ania pliku
	 */
	public class UploadButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			int returnVal = fileChooser.showOpenDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = new File(fileChooser.getSelectedFile().getPath());
				String name = fileChooser.getSelectedFile().getName();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				InputStream in = null;
				try {
					in = new FileInputStream(file);
					int count;
					byte[] bytes = new byte[8192];
					while ((count = in.read(bytes)) > 0) {
						out.write(bytes, 0, count);
					}
					in.close();
				} catch (FileNotFoundException e) {
					JOptionPane.showMessageDialog(null, "Brak pliku");
				} catch (IOException e) {
					e.printStackTrace();
				}
				encodedString = Base64.getEncoder().encodeToString(out.toByteArray());

				config.getWriter().println("(name)" + name);
				config.getWriter().println("<b>" + nick.getText() + "</b>" + ": ");
				config.getWriter().println("(fil)" + encodedString);
				config.getWriter().flush();

			}
		}
	}

	/**
	 * S³uchacz klikniêcia linku w oknie wiadomosci otrzymanych
	 */
	public class LinkListener implements HyperlinkListener {
		public void hyperlinkUpdate(HyperlinkEvent event) {
			if (HyperlinkEvent.EventType.ACTIVATED.equals(event.getEventType())) {
				try {
					String fileName = event.getDescription();
					fileChooser.setSelectedFile(new File(fileName));
					int returnVal = fileChooser.showSaveDialog(null);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						byte[] decodedBytes = Base64.getDecoder().decode(encodedStrings.get(fileName));
						File saveFile = new File(fileChooser.getSelectedFile().getAbsolutePath());
						FileOutputStream out;
						out = new FileOutputStream(saveFile);
						out.write(decodedBytes);
						out.close();
					}
				} catch (FileNotFoundException e) {
					JOptionPane.showMessageDialog(null, "Brak pliku");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public String getEncodedString() {
		return encodedString;
	}

	/**
	 * Metoda ustalaj¹ca lokalny adres IP
	 */
	@Override
	public void getLocalIP() {
		try {
			ip = Inet4Address.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public ConfigureCommunication getConfig() {
		return config;
	}

	public JLabel getIpLabel() {
		return ipLabel;
	}

	public JLabel getNickLabel() {
		return nickLabel;
	}

	public JLabel getNick() {
		return nick;
	}

	public JTextField getMessage() {
		return message;
	}

	public JTextPane getIncomingMessages2() {
		return incomingMessages2;
	}

	public HTMLDocument getDoc() {
		return doc;
	}

	public JFileChooser getChooser() {
		return fileChooser;
	}

	public JFileChooser getImageChooser() {
		return imageChooser;
	}

	public Map<String, String> getEncodedStrings() {
		return encodedStrings;
	}

	public String getIp() {
		return ip;
	}
}
