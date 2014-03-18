package temperatureViewer;

import java.io.Serializable;
import java.util.List;
import temperatureViewer.FileListBoxItem;

public class SettingsData implements Serializable {
	/**
	 * Members
	 */
	private static final long serialVersionUID = -2028687321166811731L;
	private boolean checkServerConnection = false;
	private boolean permitRestDownload = false;
	private String  server = "";
	private String  userName = "";
	private String  password = "";
	private List<FileListBoxItem>    logFileList = null;
	
	//Properties
	public String getServer() {
		return server;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String name) {
		this.userName = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean getCheckServerConnection() {
		return checkServerConnection;
	}
	public void setCheckServerConnection(boolean checkServerConnection) {
		this.checkServerConnection = checkServerConnection;
	}
	public List<FileListBoxItem> getLogFileList() {
		return logFileList;
	}
	public void setLogFileList(List<FileListBoxItem> logFileList) {
		this.logFileList = logFileList;
	}
	public boolean isPermitRestartDownload() {
		return permitRestDownload;
	}
	public void setPermitRestDownload(boolean permitRestDownload) {
		this.permitRestDownload = permitRestDownload;
	}

}
