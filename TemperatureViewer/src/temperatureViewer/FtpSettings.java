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

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import java.awt.Component;

@SuppressWarnings("serial")
public class FtpSettings extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField serverTextField;
	private JTextField NameTextField;
	private JPasswordField passwordField;
	private String actionCommand;
	
	/**
	 * Create the dialog.
	 */
	public FtpSettings(Window owner) {
		super(owner, "FTP Settings", JDialog.DEFAULT_MODALITY_TYPE);
		setTitle("FTP Settings");
		setResizable(false);
		setLocationRelativeTo(owner);
		setBounds(100, 100, 613, 282);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JPanel loginPanel = new JPanel();
		loginPanel.setBorder(new TitledBorder(null, "Login", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JCheckBox checkConnectionAutomaticallyChckbx = new JCheckBox("Check connection automatically while open");
		
		JPanel selectLogFilesPanel = new JPanel();
		selectLogFilesPanel.setBorder(new TitledBorder(null, "Selected log files", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addComponent(loginPanel, GroupLayout.PREFERRED_SIZE, 236, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(selectLogFilesPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(checkConnectionAutomaticallyChckbx))
					.addContainerGap(76, Short.MAX_VALUE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(6)
					.addComponent(checkConnectionAutomaticallyChckbx)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(loginPanel, GroupLayout.PREFERRED_SIZE, 180, GroupLayout.PREFERRED_SIZE)
						.addComponent(selectLogFilesPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addContainerGap(37, Short.MAX_VALUE))
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
		GroupLayout gl_selectLogFilesPanel = new GroupLayout(selectLogFilesPanel);
		gl_selectLogFilesPanel.setHorizontalGroup(
			gl_selectLogFilesPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_selectLogFilesPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_selectLogFilesPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_selectLogFilesPanel.createSequentialGroup()
							.addComponent(sourseList, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_selectLogFilesPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(addToListBtn)
								.addComponent(removeFromListBtn)))
						.addComponent(lblSource))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_selectLogFilesPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblDestination)
						.addComponent(list, GroupLayout.PREFERRED_SIZE, 127, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_selectLogFilesPanel.setVerticalGroup(
			gl_selectLogFilesPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_selectLogFilesPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_selectLogFilesPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblSource)
						.addComponent(lblDestination))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_selectLogFilesPanel.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_selectLogFilesPanel.createParallelGroup(Alignment.BASELINE, false)
							.addComponent(sourseList, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)
							.addComponent(list, GroupLayout.PREFERRED_SIZE, 114, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_selectLogFilesPanel.createSequentialGroup()
							.addGap(33)
							.addComponent(removeFromListBtn)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(addToListBtn)))
					.addContainerGap())
		);
		gl_selectLogFilesPanel.linkSize(SwingConstants.VERTICAL, new Component[] {addToListBtn, removeFromListBtn});
		gl_selectLogFilesPanel.linkSize(SwingConstants.VERTICAL, new Component[] {sourseList, list});
		gl_selectLogFilesPanel.linkSize(SwingConstants.HORIZONTAL, new Component[] {addToListBtn, removeFromListBtn});
		gl_selectLogFilesPanel.linkSize(SwingConstants.HORIZONTAL, new Component[] {sourseList, list});
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
		
		JLabel lblConnection = new JLabel("Connection:");
		lblConnection.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		JLabel lblConnectionStatus = new JLabel("Connection Status");
		lblConnectionStatus.setBackground(Color.RED);
		lblConnectionStatus.setFont(new Font("Tahoma", Font.BOLD, 12));
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
							.addComponent(lblConnectionStatus))
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
					.addPreferredGap(ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
					.addGroup(gl_loginPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblConnection)
						.addComponent(lblConnectionStatus))
					.addContainerGap())
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
	
	public String getServerName() {
		return serverTextField.getText();
	}

	public void setServerName(String serverName) {
		this.serverTextField.setText(serverName);
	}
	
	public String getUserName() {
		return this.NameTextField.getText();
	}

	public void setUserName(String serverName) {
		this.NameTextField.setText(serverName);
	}
	
	public String getPassword() {
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword("jasypt");
        return encryptor.encrypt(this.passwordField.getText());
	}

	public void setPassword(String password) {
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword("jasypt");
        this.passwordField.setText(encryptor.decrypt(password));
	}

	public String getActionCommand() {
		return actionCommand;
	}

	public void setActionCommand(String actionCommand) {
		this.actionCommand = actionCommand;
	}
}

