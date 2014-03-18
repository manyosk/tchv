package temperatureViewer;

import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;


import javax.swing.tree.TreeSelectionModel;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Day;
import org.jfree.data.time.Minute;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.border.LineBorder;

import java.awt.Color;


@SuppressWarnings("serial")
public class MainWnd extends JFrame {

	private JPanel contentPane;
	private SettingsData settingsData = null;
	private JTree tree = null;
	private ChartViewPanel chartViewPanel = null;
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
						
						FillTree();
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
		setBounds(100, 100, 831, 560);
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
				ftpSettings.setPermitRestartDownloadChckbx(settingsData.isPermitRestartDownload());
				ftpSettings.setVisible(true);
				if(ftpSettings.getActionCommand().equalsIgnoreCase("OK"))
				{
					settingsData.setCheckServerConnection(ftpSettings.getCheckConnectionAutomaticallyChckbxSelected());
					settingsData.setUserName(ftpSettings.getUserName());
					settingsData.setServer(ftpSettings.getServerName());
					settingsData.setPassword(ftpSettings.getPassword());
					settingsData.setLogFileList(ftpSettings.getLogFileList());
					settingsData.setPermitRestDownload(ftpSettings.getPermitRestartDownloadChckbx());
					
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
				FillTree();
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
                    ftpClient.setAutodetectUTF8(true);
                    
		            for(FileListBoxItem item : settingsData.getLogFileList())
			        {
		            	if(settingsData.isPermitRestartDownload() == true)
		            	{
		            		ftpClient.setRestartOffset(item.position);
		            	}
	                    String strTmpFile = "Data/" + item.fileName + ".tmp";
	                    File fTmpFile = new File(strTmpFile);
	                    OutputStream outputTmpStream = new BufferedOutputStream(new FileOutputStream(fTmpFile));
	                    InputStream inputStream = ftpClient.retrieveFileStream(item.fileName);
	                    	                    
	                    byte[] bytesArray = new byte[4096];
	                    int bytesRead = -1;
	                    while(null != inputStream && (bytesRead = inputStream.read(bytesArray)) != -1) 
	                    {
	                    	item.position += bytesRead;
	                        outputTmpStream.write(bytesArray, 0, bytesRead);
	                        
	                    }
	         
	                    if(settingsData.isPermitRestartDownload() == false)
		            	{
	                    	item.position = 0;
		            	}
	                    
	                    //ukonci stahovanie log suboru
	                    boolean success = ftpClient.completePendingCommand();
	                    if (success) 
	                    {
	                        System.out.println("File " + item.fileName + " has been downloaded successfully.");
	                    }
	                    
	                    inputStream.close();    
	                    outputTmpStream.close();
	                } 
		            
		            if (ftpClient.isConnected()) 
		            {
		            	ftpClient.logout();
		            	ftpClient.disconnect();
		            }
		            
		            ProcessDownloadedData();
		            
		            ObjectOutputStream out;
					out = new ObjectOutputStream(new FileOutputStream("Data/Settings.dat"));
					out.writeObject(settingsData);  
					out.close();
				
				} catch (IOException ex) {
					System.out.println("Oops! Something wrong happened");
		            ex.printStackTrace();
				}
			}

			private void ProcessDownloadedData() throws IOException 
			{
				for(FileListBoxItem item : settingsData.getLogFileList())
		        {
					List<String[]> cvsFileLines = new ArrayList<String[]>();
					
					Boolean bCreateHeader = false;
	                Path path = Paths.get("Data/" + item.fileName);
	                if (Files.notExists(path) || settingsData.isPermitRestartDownload() == false) 
	                {
	                	bCreateHeader = true;
	                }
	                 
	                BufferedReader reader = null;
	                try
	                {
						String strInputLine = null;
						reader = new BufferedReader(new FileReader("Data/" + item.fileName + ".tmp"));
						while(null != (strInputLine = reader.readLine()))
						{
							if(bCreateHeader) 
			                {
								String strHeader = "Date";   
			                    String[] parsedLine = strInputLine.split(";");
			                    		                    
			                    for(int i = 2; i < parsedLine.length; ++i)
			                    {
			                    	String[] strTmp = parsedLine[i].trim().split("=");
			                    	if(strTmp.length == 2 && strTmp[1].split(" ").length == 2 && strTmp[1].trim().split(" ")[1].compareToIgnoreCase("C") == 0)
			                    	{
			                    		strHeader += ";" + parsedLine[i].trim().split("=")[0];
			                    	}
			                    }
			                    
			                    cvsFileLines.add(strHeader.split(";"));
			                    bCreateHeader = false;
			                }
							
							String[] parsedLine = strInputLine.split(";");
		                	if(parsedLine.length <= 1)
		                	{
		                		continue;
		                	}
		                	
		                    String strLine = parsedLine[0].trim() + "-" + parsedLine[1].trim();
		                    for(int i = 2; i < parsedLine.length; ++i)
		                    {
		                    	
		                    	String[] strTmp = parsedLine[i].trim().split("=");
		                    	if(strTmp.length == 2 && strTmp[1].split(" ").length == 2 && strTmp[1].trim().split(" ")[1].compareToIgnoreCase("C") == 0)
		                    	{
		                    		strLine += ";" + strTmp[1].trim().split(" ")[0];
		                    	}
		                    }
		                    
		                    cvsFileLines.add(strLine.split(";"));
						}
						reader.close();
						
						Path tmpFilePath = Paths.get("Data/" + item.fileName + ".tmp");
						Files.delete(tmpFilePath);
	                }
	                catch(IOException e)
	                {
	                	System.out.println("Oops! Something wrong happened");
			            e.printStackTrace();
	                }
	                					
	                WriteCSVData("Data/" + item.fileFullPath, cvsFileLines);
	            }
			}
			
			private void WriteCSVData(String strFileFullPath, List<String[]> cvsFileLines)
			{
				CSVWriter writer;
				try 
				{
					writer = new CSVWriter(new FileWriter(strFileFullPath, settingsData.isPermitRestartDownload()), ';');
					writer.writeAll(cvsFileLines);
	                writer.close();
	                
	                cvsFileLines.clear();
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			
		});
		mnNewMenu.add(mntmUpdate);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.9);
		
		tree = new JTree();
		tree.setBorder(new LineBorder(new Color(0, 0, 0)));
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                        tree.getLastSelectedPathComponent();

			 /* if nothing is selected */ 
			     if (node == null) return;
			
			 /* retrieve the node that was selected */ 
			     TreeUserObject treeUserObject = (TreeUserObject) node.getUserObject();
			     if(treeUserObject != null && treeUserObject.getTreeLevel() == 2)
			     {
			    	 DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) node.getParent();
			    	 TreeUserObject parentUserObject = (TreeUserObject) treeNode.getUserObject();
			    	 UpdateChart(parentUserObject.getTreeNodeName(), treeUserObject.getTreeNodeName());
			     }
			}

			private void UpdateChart(String strLogFileName, String strCollumnName) 
			{
				try 
				{
					
					contentPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					if(strLogFileName.isEmpty() || strCollumnName.isEmpty())
					{
						return;
					}
					
					int iIndex = 0;
					String [] csvFileLine = null;
					CSVReader reader = new CSVReader(new FileReader("Data/" + strLogFileName), ';');
					
					//read header
					csvFileLine = reader.readNext();
					for(Integer i=1; i < csvFileLine.length; ++i)
				    {
				    	if(csvFileLine[i].trim().compareToIgnoreCase(strCollumnName) == 0)
				    	{
				    		iIndex = i;
				    		break;
				    	}
				    }
					
					
					int iMinRangeSliderRange = 0;
					
					SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy-HH:mm:ss");
					
					//read first line
					double minTemp = 0.0;
					double maxTemp = 0.0;
					double avgTemp = 0.0;
					double dTmp = 0.0;
					int    iCount = 0;
					
					TimeSeries pop = new TimeSeries(strCollumnName, org.jfree.data.time.Second.class);
					csvFileLine = reader.readNext();
					Date date = sdf.parse(csvFileLine[0].trim());
					long lTime = (date.getTime() / 1000) - 1262300400;
					iMinRangeSliderRange = (int) lTime;
					
					dTmp = Double.parseDouble((csvFileLine[iIndex].trim()));
					minTemp = dTmp;
					maxTemp = dTmp;
					avgTemp = dTmp;
					
					pop.add(new Second(date), dTmp);
					++iCount;
						
					while ((csvFileLine = reader.readNext()) != null) 
					{
						date = sdf.parse(csvFileLine[0].trim());
						lTime = (date.getTime() / 1000) - 1262300400;
						dTmp = Double.parseDouble((csvFileLine[iIndex].trim()));
						
						pop.addOrUpdate(new Second(date), dTmp);
						
						++iCount;
						if(minTemp > dTmp)
						{
							minTemp = dTmp;
						}
					
						if(maxTemp < dTmp)
						{
							maxTemp = dTmp;
						}
						
						avgTemp += dTmp;
						
				    }
					
					avgTemp /= iCount;
					reader.close();
					
					chartViewPanel.SetRangeSliderRange(iMinRangeSliderRange, (int) lTime);
					chartViewPanel.SetTemperatureData(minTemp, maxTemp, avgTemp);
					
					TimeSeriesCollection dataset = new TimeSeriesCollection();
					dataset.addSeries(pop);
					
					String strChartName = strLogFileName.substring(0, strLogFileName.indexOf("."));
					chartViewPanel.DisplayChart(strChartName, "Time", "Temperature", dataset);
					contentPane.setCursor(Cursor.getDefaultCursor());
				} 
				/*catch (ParseException ee)
				{
					// TODO Auto-generated catch block
					ee.printStackTrace();
				}*/
				catch (FileNotFoundException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (ParseException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		tree.setRootVisible(false);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setShowsRootHandles(true);
		splitPane.setLeftComponent(tree);
		
		JPanel rightSplitedPanePanel = new JPanel();
		splitPane.setRightComponent(rightSplitedPanePanel);
		
		chartViewPanel = new ChartViewPanel();
		GroupLayout gl_rightSplitedPanePanel = new GroupLayout(rightSplitedPanePanel);
		gl_rightSplitedPanePanel.setHorizontalGroup(
			gl_rightSplitedPanePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_rightSplitedPanePanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(chartViewPanel, GroupLayout.DEFAULT_SIZE, 796, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_rightSplitedPanePanel.setVerticalGroup(
			gl_rightSplitedPanePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_rightSplitedPanePanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(chartViewPanel, GroupLayout.DEFAULT_SIZE, 464, Short.MAX_VALUE)
					.addGap(12))
		);
		rightSplitedPanePanel.setLayout(gl_rightSplitedPanePanel);
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
		
		JMenu mnSettings = new JMenu("Settings");
		menuBar.add(mnSettings);
		
		JMenuItem mntmProperties = new JMenuItem("Properties");
		mnSettings.add(mntmProperties);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AboutDialog ftpSettings = new AboutDialog();
				ftpSettings.setVisible(true);
				ftpSettings.dispose();
			}
		});
		mnHelp.add(mntmAbout);
		contentPane.setLayout(gl_contentPane);
	}

	protected void FillTree() 
	{
		try
		{
			if(settingsData != null && tree != null)
			{
				tree.removeAll();
				
				DefaultMutableTreeNode root = new DefaultMutableTreeNode(new TreeUserObject(0, "Root"));
				List<FileListBoxItem> lLogFileList = settingsData.getLogFileList();
				for(FileListBoxItem item : lLogFileList)
				{
					DefaultMutableTreeNode tmpNode = new DefaultMutableTreeNode(new TreeUserObject(1, item.fileName));
					//tmpNode.setUserObject(null);
					if(new File("Data/" + item.fileName).exists())
					{	
						CSVReader reader = new CSVReader(new FileReader("Data/" + item.fileName), ';');
						String [] header = reader.readNext();
					    reader.close();
					    for(Integer i=1; i < header.length; ++i)
					    {
					    	DefaultMutableTreeNode tmpNode2 = new DefaultMutableTreeNode(new TreeUserObject(2, header[i]));
					    	//tmpNode2.setUserObject(item.fileName);
					    	tmpNode.add(tmpNode2);
					    }
					}
			        root.add(tmpNode);
			        
				}
				tree.setModel(new DefaultTreeModel(root));
				
			}
			
		
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
