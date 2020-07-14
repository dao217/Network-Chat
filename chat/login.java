package chat;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class login extends JFrame {

	private JPanel contentPane;
	private JTextField txtName;
	private JTextField txtAddress;
	private JTextField txtPort;
 

	public login() {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1) {
			e1.printStackTrace();
		};
		
		
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(320, 380);
		setLocationRelativeTo(null);
		setBounds(100, 100, 320, 380);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtName = new JTextField();
		txtName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtName.setBounds(56, 70, 194, 20);
		contentPane.add(txtName);
		txtName.setColumns(10);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblName.setBounds(130, 45, 56, 14);
		contentPane.add(lblName);
		
		txtAddress = new JTextField();
		txtAddress.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtAddress.setColumns(10);
		txtAddress.setBounds(56, 134, 194, 20);
		contentPane.add(txtAddress);
		
		JLabel lblName_1 = new JLabel("IP address:");
		lblName_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblName_1.setBounds(115, 116, 83, 14);
		contentPane.add(lblName_1);
		
		txtPort = new JTextField();
		txtPort.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtPort.setColumns(10);
		txtPort.setBounds(56, 201, 194, 20);
		contentPane.add(txtPort);
		
		JLabel lblName_2 = new JLabel("Port:");
		lblName_2.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblName_2.setBounds(138, 176, 41, 14);
		contentPane.add(lblName_2);
		
		JButton btnLogin = new JButton("login");
		btnLogin.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = txtName.getText();
				String adress = txtAddress.getText();
				int port = Integer.parseInt(txtPort.getText());
				login(name, adress, port);
			}

			
		});
		btnLogin.setBounds(114, 253, 89, 23);
		contentPane.add(btnLogin);
	}
	
	private void login(String name, String address, int port) {
		dispose();
		new ClientWindow(name, address, port);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					login frame = new login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
