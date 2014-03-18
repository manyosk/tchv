package temperatureViewer;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.Window;

import javax.swing.DefaultListModel;
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
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JScrollPane;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

@SuppressWarnings("serial")
class FtpSettingsWnd extends JDialog {

	//Members
	private final JPanel contentPanel = new JPanel();
	private JCheckBox checkConnectionAutomaticallyChckbx;
	private JCheckBox permitRestartDownloadChckbx;
	private JTextField serverTextField;
	private JTextField NameTextField;
	private JPasswordField passwordField;
	private String actionCommand = "Cancel";
	private JLabel connectionStatusLbl;
	private JLabel conectedLbl;
	private FTPClient ftpClient = null; 
	private JButton btnConnect = null;
	private JButton btnTestConnection = null;
	private JList<FileListBoxItem> sourceList = null;
	private JList<FileListBoxItem> destinationList = null;
	
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
	public boolean getPermitRestartDownloadChckbx() 
	{
		return permitRestartDownloadChckbx.isSelected();
	}
	public void setPermitRestartDownloadChckbx(boolean permitRestartDownloadChckbx) 
	{
		this.permitRestartDownloadChckbx.setSelected(permitRestartDownloadChckbx);
	}
	public List<FileListBoxItem> getLogFileList()
	{
		List<FileListBoxItem> logFileList = new ArrayList<FileListBoxItem>();
		DefaultListModel<FileListBoxItem> listModel = (DefaultListModel<FileListBoxItem>) destinationList.getModel();
		for(int i = 0; i < listModel.getSize(); ++i)
		{
			logFileList.add(listModel.get(i));
		}
		return logFileList;
	}
	public void setLogFileList(List<FileListBoxItem> logFileList)
	{
		if(logFileList == null)
			return;
		
		DefaultListModel<FileListBoxItem> listModel = (DefaultListModel<FileListBoxItem>) destinationList.getModel();
		listModel.removeAllElements();
		for(FileListBoxItem item : logFileList)
		{
			listModel.addElement(item);
		}
	}
	/**
	 * Create the dialog.
	 */
	public FtpSettingsWnd(Window owner) {
		super(owner, JDialog.DEFAULT_MODALITY_TYPE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				CheckConnectionButtonsState();
				if(getCheckConnectionAutomaticallyChckbxSelected())
				{
					MakeConnection();
				}
			}
			@Override
			public void windowClosing(WindowEvent e) {
				Disconnect();
			}
		});
		setTitle("FTP Settings");
		setResizable(false);
		setLocationRelativeTo(owner);
		setBounds(100, 100, 640, 346);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JPanel loginPanel = new JPanel();
		loginPanel.setBorder(new TitledBorder(null, "Login", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		checkConnectionAutomaticallyChckbx = new JCheckBox("Make connection automatically while open");
		
		JPanel selectLogFilesPanel = new JPanel();
		selectLogFilesPanel.setBorder(new TitledBorder(null, "Selected log files", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		permitRestartDownloadChckbx = new JCheckBox("Permit restart download");
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(10)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(permitRestartDownloadChckbx)
								.addComponent(checkConnectionAutomaticallyChckbx)))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(loginPanel, GroupLayout.PREFERRED_SIZE, 236, GroupLayout.PREFERRED_SIZE)
							.addGap(6)
							.addComponent(selectLogFilesPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(23, Short.MAX_VALUE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(6)
					.addComponent(checkConnectionAutomaticallyChckbx)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(permitRestartDownloadChckbx)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(loginPanel, GroupLayout.PREFERRED_SIZE, 212, GroupLayout.PREFERRED_SIZE)
						.addComponent(selectLogFilesPanel, GroupLayout.PREFERRED_SIZE, 212, GroupLayout.PREFERRED_SIZE))
					.addGap(25))
		);
		
		DefaultListModel<FileListBoxItem> listModel = new DefaultListModel<FileListBoxItem>();
		sourceList = new JList<FileListBoxItem>(listModel);
		sourceList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				@SuppressWarnings("unchecked")
				JList<FileListBoxItem> list = (JList<FileListBoxItem>) e.getSource();
		        if (e.getClickCount() == 2)
		        {
		            
		            Rectangle r = list.getCellBounds(0, list.getLastVisibleIndex()); 
		            if(r != null && r.contains(e.getPoint())) 
		            { 
		            	FileListBoxItem item = list.getSelectedValue();
		            	if(item != null && item.isDirectory)
		            	{
		            		String strNewPath = item.fileFullPath;
		            		if(0 == item.fileName.compareTo(".."))
		            		{
		            			strNewPath = item.GetRootDirectory();
		            		}
		            		FillSourceListBox(strNewPath);
		            	}
		            }
		        }
			}
		});
		JScrollPane scrollPane = new JScrollPane(sourceList);
		
		JButton addToListBtn = new JButton(">>");
		addToListBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MoveSelectedItemToDestinationList();
			}

			
		});
		addToListBtn.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		JButton removeFromListBtn = new JButton("<<");
		removeFromListBtn.setFont(new Font("Tahoma", Font.BOLD, 11));
		removeFromListBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				RemoveSelectedItemFromDestinationList();
			}
		});
		
		JLabel lblDestination = new JLabel("Destination files:");
		
		JLabel lblSource = new JLabel("Source files:");
		
		JLabel connectedStatusLbl = new JLabel("Connection status:");
		connectedStatusLbl.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		conectedLbl = new JLabel("Disconected");
		conectedLbl.setForeground(Color.RED);
		conectedLbl.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		btnConnect = new JButton("Connect");
		btnConnect.setEnabled(false);
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MakeConnection();
			}
		});
		
		DefaultListModel<FileListBoxItem> destinationListModel = new DefaultListModel<FileListBoxItem>();
		destinationList = new JList<FileListBoxItem>(destinationListModel);
		JScrollPane scrollPane_1 = new JScrollPane(destinationList);
		
		
		GroupLayout gl_selectLogFilesPanel = new GroupLayout(selectLogFilesPanel);
		gl_selectLogFilesPanel.setHorizontalGroup(
			gl_selectLogFilesPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_selectLogFilesPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_selectLogFilesPanel.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_selectLogFilesPanel.createSequentialGroup()
							.addGroup(gl_selectLogFilesPanel.createParallelGroup(Alignment.LEADING, false)
								.addGroup(gl_selectLogFilesPanel.createSequentialGroup()
									.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addGroup(gl_selectLogFilesPanel.createParallelGroup(Alignment.LEADING)
										.addComponent(addToListBtn)
										.addComponent(removeFromListBtn)))
								.addComponent(lblSource, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_selectLogFilesPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblDestination)))
						.addGroup(gl_selectLogFilesPanel.createSequentialGroup()
							.addComponent(connectedStatusLbl)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(conectedLbl)
							.addGap(18)
							.addComponent(btnConnect, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
					.addContainerGap(16, Short.MAX_VALUE))
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
					.addGroup(gl_selectLogFilesPanel.createParallelGroup(Alignment.TRAILING, false)
						.addGroup(gl_selectLogFilesPanel.createSequentialGroup()
							.addGroup(gl_selectLogFilesPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblSource)
								.addComponent(lblDestination))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_selectLogFilesPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE)
								.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 111, GroupLayout.PREFERRED_SIZE))
							.addContainerGap())
						.addGroup(gl_selectLogFilesPanel.createSequentialGroup()
							.addComponent(addToListBtn)
							.addGap(15)
							.addComponent(removeFromListBtn)
							.addGap(36))))
		);
		gl_selectLogFilesPanel.linkSize(SwingConstants.HORIZONTAL, new Component[] {addToListBtn, removeFromListBtn});
		selectLogFilesPanel.setLayout(gl_selectLogFilesPanel);
		
		JLabel lblServer = new JLabel("Server");
		
		JLabel lblName = new JLabel("Name");
		
		JLabel lblPassword = new JLabel("Password");
		
		serverTextField = new JTextField();
		serverTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				CheckConnectionButtonsState();
			}
		});
			
		NameTextField = new JTextField();
		NameTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				CheckConnectionButtonsState();
			}
		});
		
		passwordField = new JPasswordField();
		passwordField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				CheckConnectionButtonsState();
			}
		});
		
		
		btnTestConnection = new JButton("Test connection");
		btnTestConnection.setEnabled(false);
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
						if(ftpClient != null && ftpClient.isConnected())
						{
							Disconnect();
						}
						
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
						if(ftpClient != null && ftpClient.isConnected())
						{
							Disconnect();
						}
						
						setActionCommand("Cancel");
						setVisible(false);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	private void TestConnection() 
	{
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		connectionStatusLbl.setText("...");
		FTPClient testFtpClient = new FTPClient();
		try 
		{
			testFtpClient.connect(getServerName());
			int replyCode = testFtpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                System.out.println("Operation failed. Server reply code: " + replyCode);
                connectionStatusLbl.setText("Fail!");
                return;
            }
            
            testFtpClient.login(getUserName(), getOrigPassword());
            replyCode = testFtpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                System.out.println("Operation failed. Server reply code: " + replyCode);
                connectionStatusLbl.setText("Fail!");
                return;
            }
            connectionStatusLbl.setText("Success");
            
			testFtpClient.logout();
            testFtpClient.disconnect();
            testFtpClient = null;
		} 
		catch (IOException ex) 
		{
			connectionStatusLbl.setText("Fail!");
			System.out.println("Oops! Something wrong happened");
            ex.printStackTrace();
		}
		finally
		{
			this.setCursor(Cursor.getDefaultCursor());
		}
		
	}
	
	private void MakeConnection() 
	{
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		if(ftpClient == null)
		{
			ftpClient = new FTPClient();
			try 
			{
				ftpClient.connect(getServerName());
				int replyCode = ftpClient.getReplyCode();
	            if (!FTPReply.isPositiveCompletion(replyCode)) 
	            {
	                System.out.println("Operation failed. Server reply code: " + replyCode);
	                conectedLbl.setText("Disconnected");
	                ftpClient = null;
	                return;
	            }
	            
	            ftpClient.login(getUserName(), getOrigPassword());
	            replyCode = ftpClient.getReplyCode();
	            if (!FTPReply.isPositiveCompletion(replyCode)) 
	            {
	                System.out.println("Operation failed. Server reply code: " + replyCode);
	                conectedLbl.setText("Disconnected");
	                ftpClient = null;
	                return;
	            }
	            conectedLbl.setText("Connected");
	            btnConnect.setText("Disconnect");
	            
	            FillSourceListBox("");
			} 
			catch (IOException ex) {
				conectedLbl.setText("Disconnected");
				System.out.println("MakeConnection - nepodarilo sa vytvorit conekciu.");
	            ex.printStackTrace();
			}
			finally
			{
				this.setCursor(Cursor.getDefaultCursor());
			}
		}
		else
		{
			Disconnect();
		}
	}
	
	private void FillSourceListBox(String strPath) 
	{ 
		try
		{
			this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			DefaultListModel<FileListBoxItem> listModel = (DefaultListModel<FileListBoxItem>) sourceList.getModel();
			listModel.removeAllElements();
			
			FTPFile[] strListNames;
			strListNames = ftpClient.listFiles(strPath);
			
			if(!strPath.isEmpty())
			{
				listModel.addElement(new FileListBoxItem("..", strPath, true));
			}
			
			for(FTPFile item : strListNames)
	        {
				if(item.isDirectory())
					listModel.addElement(new FileListBoxItem(item.getName(), strPath, true));
	        }  
			
			for(FTPFile item : strListNames)
	        {
				if(!item.isDirectory())
					listModel.addElement(new FileListBoxItem(item.getName(), strPath, false));
	        } 
		} 
		catch (IOException e) 
		{
			Disconnect();
			System.out.println("FillDestinationListBox - failure.");
			e.printStackTrace();
		}   
		finally
		{
			this.setCursor(Cursor.getDefaultCursor());
		}
	}
	
	private void Disconnect()
	{
		if(ftpClient != null && ftpClient.isConnected())
		{
			try
			{
				this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				ftpClient.logout();
				ftpClient.disconnect();
								
				DefaultListModel<FileListBoxItem> listModel = (DefaultListModel<FileListBoxItem>) sourceList.getModel();
				listModel.removeAllElements();
			} 
			catch (IOException ex)
			{
				System.out.println("MakeConnection - chyba pri Disconnecte.");
				ex.printStackTrace();
			} 
			finally
			{
				ftpClient = null;
				conectedLbl.setText("Disconnected");
				btnConnect.setText("Connect");
				this.setCursor(Cursor.getDefaultCursor());
			}
		}
	}
	
	void CheckConnectionButtonsState()
	{
		if(getServerName().isEmpty() || getUserName().isEmpty() || getOrigPassword().isEmpty())
		{
			btnConnect.setEnabled(false);
			btnTestConnection.setEnabled(false);
		}
		else
		{
			btnConnect.setEnabled(true);
			btnTestConnection.setEnabled(true);
		}
	
	}
	
	private void MoveSelectedItemToDestinationList() {
		// TODO Auto-generated method stub
		FileListBoxItem item = sourceList.getSelectedValue();
		if(item != null && !item.isDirectory)
		{
			DefaultListModel<FileListBoxItem> listModel = (DefaultListModel<FileListBoxItem>) destinationList.getModel();
			listModel.addElement(item);
		}
	}
	
	private void RemoveSelectedItemFromDestinationList() {
		// TODO Auto-generated method stub
		FileListBoxItem item = destinationList.getSelectedValue();
		if(item != null)
		{
			DefaultListModel<FileListBoxItem> listModel = (DefaultListModel<FileListBoxItem>) destinationList.getModel();
			listModel.removeElement(item);
		}
	}
}

