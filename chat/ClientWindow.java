package chat;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

public class ClientWindow extends JFrame implements Runnable{

	private JPanel contentPane;
	private JTextField txtMessage;
	private JTextArea history;
	private DefaultCaret caret;
	private Thread run, listen;
	private Client client;
	
	
	private boolean running = false;

	public ClientWindow(String name, String address, int port) {
		setTitle("Chat Client");
		client = new Client(name, address, port);

		if (!client.openConnection(address)) {
			System.err.println("connection error");
			console("connection error");
		}
		createWindow();
		console("Attempting a connection to " + address + ":" + port + ", user: " + name);
		String connection = "/c/" + name + "/e/";
		client.send(connection.getBytes());
		running = true;
		run = new Thread(this, "Running");
		run.start();
	}
	
	private void createWindow() {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1) {
			e1.printStackTrace();
		};
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(880, 550);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{50, 830,30};
		gbl_contentPane.rowHeights = new int[]{50, 460, 40};
		gbl_contentPane.columnWeights = new double[]{1.0, 1.0};
		gbl_contentPane.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		history = new JTextArea();
		caret = (DefaultCaret)history.getCaret();
		history.setEditable(false);
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		JScrollPane scroll = new JScrollPane(history);
		history.setFont(new Font("Monosp aced", Font.PLAIN, 16));
		GridBagConstraints gbc_txtrHistory = new GridBagConstraints();
		gbc_txtrHistory.insets = new Insets(0, 0, 5, 5);
		gbc_txtrHistory.fill = GridBagConstraints.BOTH;
		gbc_txtrHistory.gridx = 0;
		gbc_txtrHistory.gridy = 0;
		gbc_txtrHistory.gridwidth = 3;
		gbc_txtrHistory.gridheight= 2;
		gbc_txtrHistory.insets = new Insets(0, 0, 0, 0);
		contentPane.add(scroll, gbc_txtrHistory); 
		
		txtMessage = new JTextField();
		txtMessage.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					send(txtMessage.getText(), true);
				}
			}
		});
		
		txtMessage.setFont(new Font("Tahoma", Font.PLAIN, 16));
		GridBagConstraints scrollConsnraints = new GridBagConstraints();
		//gbc_txtMessage.insets = new Insets(0, 0, 0, 5);
		scrollConsnraints.fill = GridBagConstraints.HORIZONTAL;
		scrollConsnraints.gridx = 0;
		scrollConsnraints.gridy = 2;
		scrollConsnraints.gridwidth= 2;
		contentPane.add(txtMessage, scrollConsnraints);
		txtMessage.setColumns(10);
		
		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				send(txtMessage.getText(), true);
			}
		});
		btnSend.setFont(new Font("Tahoma", Font.PLAIN, 16));
		GridBagConstraints gbc_btnSend = new GridBagConstraints();
		gbc_btnSend.insets = new Insets(0, 0, 0, 5);
		gbc_btnSend.gridx = 2;
		gbc_btnSend.gridy = 2;
		contentPane.add(btnSend, gbc_btnSend);
		
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				String disconect = "/d/" + client.ID + "/e/";
				send(disconect, false);
				client.close();
				running = false;
			}
		});
		
		setVisible(true);
		txtMessage.requestFocusInWindow();
	}
	
	public void run() {
		listen();
	}
	
	public void listen() {
		listen = new Thread("Listen") {
			public void run() {
				while (running) {
					String message = client.receive();
					if (message.startsWith("/c/")) {
						client.ID = Integer.parseInt(message.split("/c/|/e/")[1]);
						console("Succcesfully connected to server. Client id:" + client.ID);
					} else if (message.startsWith("/m/")) {
						String text = message.substring(3).split("/e/")[0];
						console(text);
					} else if (message.startsWith("/i/")) {
						String text = "/i/" + client.ID + "/e/";
						send (text, false);
					}
					
				}
			}
		};
		listen.start();
	}
	
	
	private void send (String message, boolean text) {
		if (message.equals("")) return;
		if (text) {
			message = client.getName()+ ": " + message; 
			message = "/m/" + message; 
			txtMessage.setText("");
		}
		client.send(message.getBytes());	
	}
	
	public void console(String message) {
		history.append(message + "\n\r");
		history.setCaretPosition(history.getDocument().getLength());
	}
	

}
