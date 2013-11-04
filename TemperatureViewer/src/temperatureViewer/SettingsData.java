package temperatureViewer;

import java.io.Serializable;

public class SettingsData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2028687321166811731L;
	private String server = "";
	private String name = "";
	private String password = "";
	
	public String getServer() {
		return server;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
