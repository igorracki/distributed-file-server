
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

/**
 * RemoteFileInt provide methods for communication with RMI server
 */
public interface RemoteFileInt extends Remote {

	/**
	 * Download File
	 * 
	 * @param fileName
	 *            String
	 * @return byte[]
	 * @throws RemoteException
	 */
	public byte[] downloadFile(String fileName) throws RemoteException;

	/**
	 * Upload File
	 * 
	 * @param buffer
	 *            byte[]
	 * @param fileInfo
	 *            FileInfo
	 * @return String
	 * @throws RemoteException
	 */
	public String uploadFile(byte[] buffer, FileInfo fileInfo) throws RemoteException;

	/**
	 * Get File List
	 * 
	 * @param filePath
	 *            String
	 * @return Vector<FileInfo>
	 * @throws RemoteException
	 */
	public Vector<FileInfo> getFilelist(String filePath) throws RemoteException;

	/**
	 * Create Folder
	 * 
	 * @param location
	 *            String
	 * @param folderName
	 *            String
	 * @return String
	 * @throws RemoteException
	 */
	public String crateFolder(String location, String folderName) throws RemoteException;

	/**
	 * Delete Folder
	 * 
	 * @param fileInfo
	 * @return String
	 * @throws RemoteException
	 */
	public String deleteFile(FileInfo fileInfo) throws RemoteException;
}
