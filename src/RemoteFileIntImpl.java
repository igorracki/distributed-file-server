import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

/**
 * class RemoteFileIntImpl
 * Implements RemoteFileInt 
 * Extends UnicastRemoteObject
 */
public class RemoteFileIntImpl extends UnicastRemoteObject implements RemoteFileInt {

	private static final long serialVersionUID = 1L;

	//private String serverPath = "c:\\server\\";

	protected RemoteFileIntImpl() throws RemoteException {
		super();
	}

	@Override
	public String deleteFile(FileInfo fileInfo) throws RemoteException {
		
			Path pathToDelet = Paths.get(fileInfo.getParentPath() +"\\"+ fileInfo.getFileName());
			try {
				Files.deleteIfExists(pathToDelet);
				return "File was deleted " + pathToDelet.toString();
			} catch (DirectoryNotEmptyException e1){
				e1.printStackTrace();
				return "Not empty Directory cannot delete"; 
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		
		return null;
	}
	
	@Override
	public String uploadFile(byte[] buffer, FileInfo fileInfo) throws RemoteException {
		try {
			File downloadedFile = new File(fileInfo.getParentPath() +"\\"+ fileInfo.getFileName());
			BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(downloadedFile.getAbsolutePath()));
			output.write(buffer, 0, buffer.length);
			output.flush();
			output.close();
			return "File "+ fileInfo.getFileName() + " to " + downloadedFile.getAbsolutePath();
		} catch (Exception ex) {
			System.out.println("Client: " + ex.getMessage());
			ex.printStackTrace();
		}
		return "Error: File not uploaded";
	}
	
	@Override
	public String crateFolder(String location, String folderName) throws RemoteException {
		
		Path dir = Paths.get(location+"\\"+folderName);
		try {
			Path newDir = Files.createDirectories(dir);
			return "New Foledr name is : "+ newDir.getFileName();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Error! Folder not created";
	}

	@Override
	public byte[] downloadFile(String fileName) throws RemoteException {
		try {
			File file = new File(fileName);
			//System.out.println(file.getAbsolutePath());
			byte buffer[] = new byte[(int) file.length()];
			BufferedInputStream input = new BufferedInputStream(new FileInputStream(fileName));
			input.read(buffer, 0, buffer.length);
			input.close();
			return (buffer);
		} catch (Exception e) {
			System.out.println("RemoteFileIntImpl: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Vector<FileInfo> getFilelist(String filePath) throws RemoteException {
		Vector<FileInfo> vector = new Vector<FileInfo>();
		try {
			Path path = Paths.get(filePath);
			DirectoryStream<Path> ds = Files.newDirectoryStream(path);
			for (Path file : ds) {
				vector.addElement(new FileInfo(file.getFileName().toString(), Files.isDirectory(file), file.getParent().toString()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return vector;
	}
}