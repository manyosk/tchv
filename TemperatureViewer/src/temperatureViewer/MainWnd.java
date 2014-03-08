package temperatureViewer;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

@SuppressWarnings("serial")
public class MainWnd extends JFrame {

	private JPanel contentPane;
	private SettingsData settingsData = null;
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
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				if(settingsData == null)
				{
					settingsData = new SettingsData();
					try {
						ObjectInputStream in = new ObjectInputStream(new FileInputStream("Data/Settings.dat"));
						settingsData = (SettingsData) in.readObject();
						in.close();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
				}
			}
		});
		setTitle("Temperature Viewer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 799, 560);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBorderPainted(false);
		
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
				FtpSettingsWnd ftpSettings = new FtpSettingsWnd(MainWnd.this);
				ftpSettings.setCheckConnectionAutomaticallyChckbxSelected(settingsData.getCheckServerConnection());
				ftpSettings.setServerName(settingsData.getServer());
				ftpSettings.setUserName(settingsData.getUserName());
				ftpSettings.setPassword(settingsData.getPassword());
				ftpSettings.setLogFileList(settingsData.getLogFileList());
				ftpSettings.setVisible(true);
				if(ftpSettings.getActionCommand().equalsIgnoreCase("OK"))
				{
					settingsData.setCheckServerConnection(ftpSettings.getCheckConnectionAutomaticallyChckbxSelected());
					settingsData.setUserName(ftpSettings.getUserName());
					settingsData.setServer(ftpSettings.getServerName());
					settingsData.setPassword(ftpSettings.getPassword());
					settingsData.setLogFileList(ftpSettings.getLogFileList());
					
					ObjectOutputStream out;
					try {
						out = new ObjectOutputStream(new FileOutputStream("Data/Settings.dat"));
						out.writeObject(settingsData);  
						out.close();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						System.out.println("Settings.dat file sa nenasiel ");
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						System.out.println("IOException - spracovanie FtpSettings-OK button.");
						e.printStackTrace();
					}  
				}
				
				ftpSettings.dispose();
			}
		});
		mnNewMenu.add(mntmFtpSettings);
		
		JMenuItem mntmUpdate = new JMenuItem("Update files");
		mntmUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FTPClient ftpClient = new FTPClient();
				try {
					ftpClient.connect(settingsData.getServer());
					int replyCode = ftpClient.getReplyCode();
		            if (!FTPReply.isPositiveCompletion(replyCode)) {
		                System.out.println("Operation failed. Server reply code: " + replyCode);
		                return;
		            }
		            
		            StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		            encryptor.setPassword("jasypt");
		            String str =  encryptor.decrypt(settingsData.getPassword());
		            
		            
		            ftpClient.login(settingsData.getUserName(), str);
		            replyCode = ftpClient.getReplyCode();
		            if (!FTPReply.isPositiveCompletion(replyCode)) {
		                System.out.println("Operation failed. Server reply code: " + replyCode);
		                return;
		            }
		            
		            ftpClient.enterLocalPassiveMode();
                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                    
		            for(FileListBoxItem item : settingsData.getLogFileList())
			        {
		            	ftpClient.setRestartOffset(item.position);
	                    String remoteFile2 = "/" + item.fileFullPath;
	                    File downloadFile2 = new File(item.fileName);
	                    OutputStream outputStream2 = new BufferedOutputStream(new FileOutputStream(downloadFile2));
	                    InputStream inputStream = ftpClient.retrieveFileStream(remoteFile2);
	                    byte[] bytesArray = new byte[4096];
	                    int bytesRead = -1;
	                    while ((bytesRead = inputStream.read(bytesArray)) != -1) 
	                    {
	                    	item.position += bytesRead;
	                        outputStream2.write(bytesArray, 0, bytesRead);
	                    }
	         
	                    boolean success = ftpClient.completePendingCommand();
	                    if (success) 
	                    {
	                        System.out.println("File #2 has been downloaded successfully.");
	                    }
	                    outputStream2.close();
	                    inputStream.close();     
			        } 
		            
		            ObjectOutputStream out;
					out = new ObjectOutputStream(new FileOutputStream("Data/Settings.dat"));
					out.writeObject(settingsData);  
					out.close();
						
		            if (ftpClient.isConnected()) 
		            {
		            	ftpClient.logout();
		            	ftpClient.disconnect();
		            }
		            
				} catch (IOException ex) {
					System.out.println("Oops! Something wrong happened");
		            ex.printStackTrace();
				}
			}
		});
		mnNewMenu.add(mntmUpdate);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.3);
		
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
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(menuBar, GroupLayout.DEFAULT_SIZE, 773, Short.MAX_VALUE)
				.addComponent(splitPane, GroupLayout.DEFAULT_SIZE, 773, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(menuBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(1)
					.addComponent(splitPane, GroupLayout.DEFAULT_SIZE, 489, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
	}
}
