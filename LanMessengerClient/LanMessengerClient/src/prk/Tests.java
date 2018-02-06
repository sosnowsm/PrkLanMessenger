package prk;

import static org.junit.Assert.*;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import org.junit.Test;
import prk.Client.CommunicatsRecipient;
/**
 * @author Piotr Sawicki
 * Klasa testuj¹ca
 */
public class Tests {
	@Test
	public void testSetNick() {
		GUI gui = new GUI();
		gui.makeGUI();
		gui.setNick();
		String name = gui.getNick().getText();
		String nameExpected = "test";
		assertEquals(nameExpected,name);
		assertTrue(!name.equals(null));
		assertTrue(!(name.length()<3));
	}
	@Test
	public void testGetLocalIP() throws UnknownHostException {
		GUI gui = new GUI();
		gui.getLocalIP();
		String localIp = gui.getIp();
		String localIpExpected = Inet4Address.getLocalHost().getHostAddress();
		assertEquals(localIpExpected,localIp);
	}
	@Test
	public void testGetServerIP() {
		ConfigureCommunication cC = new ConfigureCommunication();
		cC.getServerIP();
		String ipSerwera = cC.getIpSerwera();
		String ipSerweraExpected = "127.0.0.1";
		assertEquals(ipSerweraExpected,ipSerwera);
		assertTrue(!ipSerwera.equals(null));
	}
	@Test
	public void testConfigureCommunication() {
		ConfigureCommunication cC = new ConfigureCommunication();
		cC.getServerIP();
		cC.configureCommunication();
		String socketIp = cC.getSocket().getInetAddress().toString();
		String socketIpExpected = "/127.0.0.1";
		assertEquals(socketIpExpected,socketIp);
	}
	@Test
	public void testRun1() {
		Client client = new Client();
		CommunicatsRecipient cr = client.new CommunicatsRecipient();
		cr.run();
		String message = cr.getMessage2();
		String messageExpected = null;
		assertEquals(messageExpected,message);
	}
	@Test
	public void testRun2() {
		Client client = new Client();
		CommunicatsRecipient cr = client.new CommunicatsRecipient();
		cr.setMessage2("test");
		cr.run();
		String message = cr.getMessage2();
		String messageExpected = "test";
		assertEquals(messageExpected,message);
	}
}
