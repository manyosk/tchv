package temperatureViewer;

import java.awt.EventQueue;

import org.apache.commons.net.ftp.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;



@SuppressWarnings("serial")
public class MainWnd extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWnd frame = new MainWnd();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainWnd() {
		setTitle("Temperature Viewer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 535, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBorderPainted(false);
		menuBar.setBounds(0, 0, 527, 21);
		contentPane.add(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmClose = new JMenuItem("Close");
		mntmClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false); 
				dispose();
			}
		});
		mnFile.add(mntmClose);
		
		JMenu mnNewMenu = new JMenu("Ftp");
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmFtpSettings = new JMenuItem("Ftp settings");
		mntmFtpSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		mnNewMenu.add(mntmFtpSettings);
		
		JMenuItem mntmUpdate = new JMenuItem("Update files");
		mntmUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FTPClient ftpClient = new FTPClient();
				try {
					ftpClient.connect("10.165.129.21");
					int replyCode = ftpClient.getReplyCode();
		            if (!FTPReply.isPositiveCompletion(replyCode)) {
		                System.out.println("Operation failed. Server reply code: " + replyCode);
		                return;
		            }
		            
		            ftpClient.login("skmaryb", "GkmhajSb");
		            replyCode = ftpClient.getReplyCode();
		            if (!FTPReply.isPositiveCompletion(replyCode)) {
		                System.out.println("Operation failed. Server reply code: " + replyCode);
		                return;
		            }
		            
		            
					ftpClient.logout();
		            ftpClient.disconnect();
				} catch (IOException ex) {
					System.out.println("Oops! Something wrong happened");
		            ex.printStackTrace();
				}
			}
		});
		mnNewMenu.add(mntmUpdate);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.3);
		splitPane.setBounds(0, 22, 527, 239);
		contentPane.add(splitPane);
		
		JTree tree = new JTree();
		splitPane.setLeftComponent(tree);
		
		JPanel panel = new JPanel();
		splitPane.setRightComponent(panel);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGap(0, 448, Short.MAX_VALUE)
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGap(0, 237, Short.MAX_VALUE)
		);
		panel.setLayout(gl_panel);
	}
}
