
import java.rmi.Naming;

/**
 * Class RemoteFileServer
 */
public class RemoteFileServer {

	/**
	 * Main method
	 * @param args
	 */
	public static void main(String[] args) {
 
		String hostName = "localhost";
		String serviceName = "FileService";
		try {
			RemoteFileInt rfi = new RemoteFileIntImpl();
			Naming.rebind("rmi://" + hostName + "/" + serviceName, rfi);
			System.out.println("RMI File Server is running...");
		} catch (Exception e) {
			System.out.println("RemoteFileServer: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
