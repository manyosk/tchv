package temperatureViewer;

import java.io.Serializable;

class FileListBoxItem implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6918091835669987236L;
	String filePath;
	String fileFullPath;
	String fileName;
	boolean isDirectory;
	
	FileListBoxItem(String strName, String strPath, boolean isDir)
	{
		filePath     = strPath;
		fileName     = strName;
		
		if(strPath.isEmpty())
		{
			fileFullPath = strName;
		}
		else
		{
			fileFullPath = strPath + "/" + strName;
		}
		
		filePath     = strPath;
		fileName     = strName;
		isDirectory  = isDir;
	}
	
	public String toString()
	{
		if(isDirectory)
		{
			return "[" + fileName + "]"; 
		}
		else
		{
			return fileName;
		}
	}
	
	public String GetRootDirectory()
	{
		String strRootDirectory = "";
		
		
		int index = filePath.lastIndexOf('/');
		if(index > 0)
		{
			strRootDirectory = filePath.substring(0, index);
		}	
		
		return strRootDirectory;
	}
}