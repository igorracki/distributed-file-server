import java.io.Serializable;
/**
 * FileInfo class
 * Provide information about file name, parent path and if file is directory 
 * 
 */
public class FileInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	private String fileName;
	private boolean isDir;
	private String parentPath;

	/**
	 * Constructor
	 * @param fileName
	 * @param isDir
	 * @param parentPath
	 */
	public FileInfo (String fileName, boolean isDir, String parentPath){
		this.fileName = fileName;
		this.setDir(isDir);
		this.parentPath = parentPath;
	}

	/**
	 * Get File Name
	 * @return String
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Set File Name
	 * @param fileName String
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Is Dir
	 * @return boolean
	 */
	public boolean isDir() {
		return isDir;
	}

	/**
	 * Set is Dir
	 * @param isDir boolean
	 */
	public void setDir(boolean isDir) {
		this.isDir = isDir;
	}

	/**
	 * Get Parent Path
	 * @return
	 */
	public String getParentPath() {
		return parentPath;
	}

	/**
	 * set Parent Path
	 * @param paretPath
	 */
	public void setParentPath(String paretPath) {
		this.parentPath = paretPath;
	}
	
	/**
	 * To String
	 * @return String
	 */
	public String toString() {
		return "Is dir: " + this.isDir() + " , FileName: " + this.getFileName() + " , File parent : "+this.getParentPath();
	}
	
}
