package temperatureViewer;

public class TreeUserObject {
	private int iTreeLevel;
	private String strTreeNodeName;

	public int getTreeLevel() {
		return iTreeLevel;
	}

	public void setTreeLevel(int iTreeLevel) {
		this.iTreeLevel = iTreeLevel;
	}

	public String getTreeNodeName() {
		return strTreeNodeName;
	}

	public void setTreeNodeName(String strTreeListName) {
		this.strTreeNodeName = strTreeListName;
	}

	public TreeUserObject(int iTreeLevel, String strTreeListName)
	{
		setTreeLevel(iTreeLevel);
		setTreeNodeName(strTreeListName);
	}
	
	public String toString()
	{
		return getTreeNodeName();
	}
	
}
