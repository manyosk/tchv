package temperatureViewer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.TitledBorder;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;
import javax.swing.JPasswordField;

import java.awt.Font;
import java.awt.Color;

import javax.swing.JCheckBox;
import javax.swing.JList;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.SwingConstants;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

@SuppressWarnings("serial")
public class FtpSettingsWnd extends JDialog {

	//Members
	private final JPanel contentPanel = new JPanel();
	private JCheckBox checkConnectionAutomaticallyChckbx;
	private JTextField serverTextField;
	private JTextField NameTextField;
	private JPasswordField passwordField;
	private String actionCommand = "Cancel";
	private JLabel connectionStatusLbl;
	private JLabel conectedLbl;
	
	//Properties
	public String getServerName() {
		String str = serverTextField.getText();
		return str;
	}
	public void setServerName(String serverName) {
		this.serverTextField.setText(serverName);
	}
	public String getUserName() {
		String str = NameTextField.getText();
		return str;
	}
	public void setUserName(String serverName) {
		this.NameTextField.setText(serverName);
	}
	public String getPassword() {
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword("jasypt");
        char[] input = passwordField.getPassword();
        String str = String.valueOf(input);
        str =  encryptor.encrypt(String.valueOf(input));
        return str;
	}
	public String getOrigPassword() {
		char[] input = passwordField.getPassword();
		String str = String.valueOf(input);
        return str;
	}
	public void setPassword(String password) {
		if(!password.isEmpty())
		{
			StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
	        encryptor.setPassword("jasypt");
	        String str = encryptor.decrypt(password);
	        this.passwordField.setText(str);
		}
	}
	public String getActionCommand() {
		return actionCommand;
	}
	public void setActionCommand(String actionCommand) {
		this.actionCommand = actionCommand;
	}
	public boolean getCheckConnectionAutomaticallyChckbxSelected()
	{
		return checkConnectionAutomaticallyChckbx.isSelected();
	}
	public void setCheckConnectionAutomaticallyChckbxSelected(boolean selected)
	{
		checkConnectionAutomaticallyChckbx.setSelected(selected);
	}
	
	/**
	 * Create the dialog.
	 */
	public FtpSettingsWnd(Window owner) {
		super(owner, JDialog.DEFAULT_MODALITY_TYPE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				if(getCheckConnectionAutomaticallyChckbxSelected())
				{
					MakeConnection();
				}
			}
		});
		setTitle("FTP Settings");
		setResizable(false);
		setLocationRelativeTo(owner);
		setBounds(100, 100, 626, 326);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JPanel loginPanel = new JPanel();
		loginPanel.setBorder(new TitledBorder(null, "Login", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		checkConnectionAutomaticallyChckbx = new JCheckBox("Make connection automatically while open");
		
		JPanel selectLogFilesPanel = new JPanel();
		selectLogFilesPanel.setBorder(new TitledBorder(null, "Selected log files", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(0, 10, Short.MAX_VALUE)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addComponent(loginPanel, GroupLayout.PREFERRED_SIZE, 236, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(selectLogFilesPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(checkConnectionAutomaticallyChckbx))
					.addGap(15))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(6)
					.addComponent(checkConnectionAutomaticallyChckbx)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(loginPanel, GroupLayout.PREFERRED_SIZE, 212, GroupLayout.PREFERRED_SIZE)
						.addComponent(selectLogFilesPanel, GroupLayout.PREFERRED_SIZE, 212, Short.MAX_VALUE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		
		JList sourseList = new JList();
		
		JButton addToListBtn = new JButton(">>");
		addToListBtn.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		JButton removeFromListBtn = new JButton("<<");
		removeFromListBtn.setFont(new Font("Tahoma", Font.BOLD, 11));
		removeFromListBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		
		JList list = new JList();
		
		JLabel lblDestination = new JLabel("Selected destination files:");
		
		JLabel lblSource = new JLabel("Source files:");
		
		JLabel connectedStatusLbl = new JLabel("Connection status:");
		connectedStatusLbl.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		conectedLbl = new JLabel("Disconected");
		conectedLbl.setForeground(Color.RED);
		conectedLbl.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MakeConnection();
			}
		});
		GroupLayout gl_selectLogFilesPanel = new GroupLayout(selectLogFilesPanel);
		gl_selectLogFilesPanel.setHorizontalGroup(
			gl_selectLogFilesPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_selectLogFilesPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_selectLogFilesPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_selectLogFilesPanel.createSequentialGroup()
							.addGroup(gl_selectLogFilesPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_selectLogFilesPanel.createSequentialGroup()
									.addComponent(sourseList, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(gl_selectLogFilesPanel.createParallelGroup(Alignment.LEADING)
										.addComponent(addToListBtn)
										.addComponent(removeFromListBtn)))
								.addComponent(lblSource, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_selectLogFilesPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblDestination)
								.addComponent(list, GroupLayout.PREFERRED_SIZE, 127, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_selectLogFilesPanel.createSequentialGroup()
							.addComponent(connectedStatusLbl)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(conectedLbl)
							.addGap(18)
							.addComponent(btnConnect, GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)))
					.addGap(14))
		);
		gl_selectLogFilesPanel.setVerticalGroup(
			gl_selectLogFilesPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_selectLogFilesPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_selectLogFilesPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(connectedStatusLbl)
						.addComponent(conectedLbl)
						.addComponent(btnConnect))
					.addGap(11)
					.addGroup(gl_selectLogFilesPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblSource)
						.addComponent(lblDestination))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_selectLogFilesPanel.createParallelGroup(Alignment.TRAILING, false)
						.addGroup(gl_selectLogFilesPanel.createSequentialGroup()
							.addComponent(removeFromListBtn)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(addToListBtn)
							.addGap(25))
						.addComponent(sourseList, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(list, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_selectLogFilesPanel.linkSize(SwingConstants.HORIZONTAL, new Component[] {addToListBtn, removeFromListBtn});
		selectLogFilesPanel.setLayout(gl_selectLogFilesPanel);
		
		JLabel lblServer = new JLabel("Server");
		
		JLabel lblName = new JLabel("Name");
		
		JLabel lblPassword = new JLabel("Password");
		
		serverTextField = new JTextField();
		serverTextField.setColumns(10);
		
		NameTextField = new JTextField();
		NameTextField.setColumns(10);
		
		passwordField = new JPasswordField();
		
		JButton btnTestConnection = new JButton("Test connection");
		btnTestConnection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				TestConnection();
			}
		});
		
		JLabel lblConnection = new JLabel("Connection:");
		lblConnection.setFont(new Font("Tahoma", Font.BOLD, 12));
		connectionStatusLbl = new JLabel("...");
		connectionStatusLbl.setForeground(Color.RED);
		connectionStatusLbl.setFont(new Font("Tahoma", Font.BOLD, 12));
		GroupLayout gl_loginPanel = new GroupLayout(loginPanel);
		gl_loginPanel.setHorizontalGroup(
			gl_loginPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_loginPanel.createSequentialGroup()
					.addGroup(gl_loginPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_loginPanel.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_loginPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblServer)
								.addComponent(lblName)
								.addComponent(lblPassword))
							.addGap(12)
							.addGroup(gl_loginPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(passwordField, 146, 146, 146)
								.addComponent(NameTextField, GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
								.addComponent(serverTextField, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_loginPanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblConnection)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(connectionStatusLbl))
						.addGroup(gl_loginPanel.createSequentialGroup()
							.addGap(50)
							.addComponent(btnTestConnection)))
					.addContainerGap())
		);
		gl_loginPanel.setVerticalGroup(
			gl_loginPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_loginPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_loginPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblServer)
						.addComponent(serverTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_loginPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblName)
						.addComponent(NameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_loginPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPassword)
						.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnTestConnection)
					.addPreferredGap(ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
					.addGroup(gl_loginPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblConnection)
						.addComponent(connectionStatusLbl))
					.addGap(25))
		);
		loginPanel.setLayout(gl_loginPanel);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						setActionCommand("OK");
						setVisible(false);
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{				
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						setActionCommand("Cancel");
						setVisible(false);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	private void TestConnection() {
		// TODO Auto-generated method stub
		connectionStatusLbl.setText("...");
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.connect(getServerName());
			int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                System.out.println("Operation failed. Server reply code: " + replyCode);
                connectionStatusLbl.setText("Fail!");
                return;
            }
            
            ftpClient.login(getUserName(), getOrigPassword());
            replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                System.out.println("Operation failed. Server reply code: " + replyCode);
                connectionStatusLbl.setText("Fail!");
                return;
            }
            connectionStatusLbl.setText("Success");
            
			ftpClient.logout();
            ftpClient.disconnect();
		} catch (IOException ex) {
			connectionStatusLbl.setText("Fail!");
			System.out.println("Oops! Something wrong happened");
            ex.printStackTrace();
		}
	}
	
	private void MakeConnection() {
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.connect(getServerName());
			int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                System.out.println("Operation failed. Server reply code: " + replyCode);
                conectedLbl.setText("Disconnected");
                return;
            }
            
            ftpClient.login(getUserName(), getOrigPassword());
            replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                System.out.println("Operation failed. Server reply code: " + replyCode);
                conectedLbl.setText("Disconnected");
                return;
            }
            conectedLbl.setText("Connected");
            
			ftpClient.logout();
            ftpClient.disconnect();
		} catch (IOException ex) {
			conectedLbl.setText("Disconnected");
			System.out.println("Oops! Something wrong happened");
            ex.printStackTrace();
		}
		
	}
}

