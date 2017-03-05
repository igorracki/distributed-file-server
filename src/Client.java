
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Class Client Starts Client program to connect with RMI server
 * 
 */
public class Client {
	private ClientGUI clientGUI;
	private RemoteFileInt rfs;
	private String hostName = "localhost";
	private String serviceName = "FileService";
	private String clientPath = "c:\\client\\";
	private JFileChooser fc;
	private JFileChooser fc2;
	private Vector<FileInfo> rootVector;
	private FileInfo selectedFileInfo;
	private Vector<String> backPath;
	private String separator = "\\";

	/**
	 * Constructor
	 * 
	 * @param clientGUI
	 */
	public Client(ClientGUI clientGUI) {
		this.clientGUI = clientGUI;
		this.clientGUI.addConnectListener(new ListenerForConnect());
		this.clientGUI.addExitListener(new ListenerForExit());
		this.clientGUI.addDownloadListener(new ListenerForDownload());
		this.clientGUI.addUploadListener(new ListenerForUpload());
		this.clientGUI.addNewFolderListener(new ListenerForNewFolder());
		this.clientGUI.addDeleteListener(new ListenerForDelete());
		this.clientGUI.addHomeFolderListener(new ListenerForHomeFolder());
		this.clientGUI.addOpenFolderListener(new ListenerForOpenFolder());
		this.clientGUI.addUpFolderListener(new ListenerForUpFolder());
		this.clientGUI.addListListener(new ListSelectionlistener());
		clientGUI.setSysMsg("Wellcome");
		clientGUI.setHomeFolderJL(this.clientPath);
		selectedFileInfo = new FileInfo("server", true, "c:\\");
		backPath = new Vector<String>();
		backPath.addElement("c:\\server");
	}

	/**
	 * main method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new Client(new ClientGUI());

	}

	/**
	 * Get Back Path
	 * 
	 * @return String
	 */
	private String getBackPath() {
		String str = backPath.elementAt(0);
		for (int i = 1; i < backPath.size(); i++) {
			str += separator + backPath.elementAt(i);
		}
		return str;
	}

	/**
	 * Show File List
	 * 
	 * @param vector
	 */
	private void showFileList(Vector<FileInfo> vector) {
		DefaultListModel<String> model = new DefaultListModel<String>();
		for (int i = 0; i < vector.size(); i++) {
			FileInfo fi = (vector.get(i));
			model.addElement(fi.getFileName());
		}
		clientGUI.setModelToList(model);
	}

	/**
	 * Listener For UpFolder
	 */
	class ListenerForUpFolder implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// create vector with fileInfo list
			if (rfs != null) {
				if (backPath.size() > 1) {
					backPath.removeElementAt(backPath.size() - 1);
					selectedFileInfo = null;
				} else {
					selectedFileInfo = null;
				}
				try {
					rootVector = rfs.getFilelist(getBackPath());
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
				showFileList(rootVector);
			} else {
				clientGUI.setSysMsg("Error: You need to connect first");
			}
		}
	}

	/**
	 * Listener For Open Folder
	 */
	class ListenerForOpenFolder implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// create vector with fileInfo list
			if (rfs != null) {
				if (selectedFileInfo != null) {
					if (selectedFileInfo.isDir()) {
						try {
							rootVector = rfs.getFilelist(
									selectedFileInfo.getParentPath() + "\\" + selectedFileInfo.getFileName());
							backPath.addElement(selectedFileInfo.getFileName());
							showFileList(rootVector);
							selectedFileInfo = null;
						} catch (RemoteException e1) {
							e1.printStackTrace();
						}
					} else {
						clientGUI.setSysMsg("Error: You cannot expand a file");
					}
				} else {
					clientGUI.setSysMsg("Warning: Select folder first");
				}
			} else {
				clientGUI.setSysMsg("Error: You need to connect first");
			}
		}
	}

	/**
	 * List Selection listener
	 */
	class ListSelectionlistener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			@SuppressWarnings("unchecked")
			JList<String> list = (JList<String>) e.getSource();
			String selectedStr = list.getSelectedValue();
			// fech fileinfo of selected file name
			for (int i = 0; i < rootVector.size(); i++) {
				if (rootVector.elementAt(i).getFileName().equals(selectedStr)) {
					selectedFileInfo = rootVector.elementAt(i);
				}
			}
		}
	}

	/**
	 * Listener For Upload
	 */
	class ListenerForUpload implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (rfs != null) {
				//select file to upload
				fc2 = new JFileChooser();
				fc2.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				int returnVal = fc2.showOpenDialog(clientGUI);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc2.getSelectedFile();
					if (file.isFile()) {
						//preparing FileInfo for sending
						// in case of sending file parent dir is a location on the server default c:/server/
						String location = getBackPath();
						FileInfo fileInfo = new FileInfo(file.getName(), false, location);
						// preparing buffer for sending
						byte buffer[] = new byte[(int) file.length()];
						BufferedInputStream input;
						try {
							input = new BufferedInputStream(new FileInputStream(file));
							input.read(buffer, 0, buffer.length);
							input.close();
							String msg = rfs.uploadFile(buffer, fileInfo);
							rootVector = rfs.getFilelist(getBackPath());
							showFileList(rootVector);
							clientGUI.setSysMsg(msg);
						} catch (FileNotFoundException e1) {
							clientGUI.setSysMsg("Error: File Not Found");
							e1.printStackTrace();
						} catch (IOException e1) {
							clientGUI.setSysMsg("Error: I/O Exception");
							e1.printStackTrace();
						}
					} else {
						clientGUI.setSysMsg("You cannot upload folder");
					}
				} else {
					clientGUI.setSysMsg("Open command cancelled by user.");
				}
			} else {
				clientGUI.setSysMsg("Error: You need to connect first");
			}
		}
	}

	/**
	 * Listener For New Folder
	 */
	class ListenerForNewFolder implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (rfs != null) {
				String folderName = JOptionPane.showInputDialog("Enter the Folder Name:\n", "newFolder");
				try {
					String msg = rfs.crateFolder("", folderName);
					rootVector = rfs.getFilelist(getBackPath());
					showFileList(rootVector);
					clientGUI.setSysMsg(msg);
				} catch (RemoteException e1) {
					clientGUI.setSysMsg("Error-comunication");
					e1.printStackTrace();
				}
			} else {
				clientGUI.setSysMsg("Error: You need to connect first");
			}
		}
	}

	/**
	 * Listener For Delete
	 */
	class ListenerForDelete implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (rfs != null) {
				if (selectedFileInfo != null) {
					//FileInfo fileInfo = new FileInfo("newFolder", false, "c:\\server\\");
					try {

						String msg = rfs.deleteFile(selectedFileInfo);
						if (rootVector.size() == 1) {
							backPath.removeElementAt(backPath.size() - 1);
						}
						rootVector = rfs.getFilelist(getBackPath());
						showFileList(rootVector);

						clientGUI.setSysMsg(msg);
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
				} else {
					clientGUI.setSysMsg("Warning: Select folder first");
				}
			} else {
				clientGUI.setSysMsg("Error: You need to connect first");
			}
		}
	}

	/**
	 * Listener For Home Folder
	 */
	class ListenerForHomeFolder implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal = fc.showOpenDialog(clientGUI);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				clientPath = file.getAbsolutePath();
				// This is where a real application would open the file.
				clientGUI.setSysMsg("Opening: " + file.getName() + ".");
				clientGUI.setHomeFolderJL(file.getAbsolutePath());
			} else {
				clientGUI.setSysMsg("Open command cancelled by user.");
			}
			clientGUI.setSysMsg("Home folder Selected");
		}
	}

	/**
	 * Listener For Download
	 */
	class ListenerForDownload implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (rfs != null) {
				if (selectedFileInfo != null) {
					if (!selectedFileInfo.isDir()) {
						try {
							clientGUI.setSysMsg("Downloading...");
							String fileName = selectedFileInfo.getFileName();
							String serverLocation = selectedFileInfo.getParentPath();
							// fileName need to be from  GUI list selected element
							byte[] fileData = rfs.downloadFile(serverLocation + separator + fileName);
							File downloadedFile = new File(clientPath + fileName);
							BufferedOutputStream output = new BufferedOutputStream(
									new FileOutputStream(downloadedFile.getAbsolutePath()));
							output.write(fileData, 0, fileData.length);
							output.flush();
							output.close();

						} catch (Exception ex) {
							System.out.println("Client: " + ex.getMessage());
							ex.printStackTrace();
						}
						clientGUI.setSysMsg("File was downloaded.");
					} else {
						clientGUI.setSysMsg("Warning: You cannot download folder");
					}
				} else {
				}
			} else {
				clientGUI.setSysMsg("Error: You need to connect first");
			}
		}
	}

	/**
	 * Listener For Connect
	 */
	class ListenerForConnect implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			hostName = JOptionPane.showInputDialog("Enter IP address of file server:\n", "localhost");
			try {
				rfs = (RemoteFileInt) Naming.lookup("rmi://" + hostName + "/" + serviceName);
				selectedFileInfo = new FileInfo("server", true, "c:\\");
				backPath.removeAllElements();
				backPath.addElement("c:\\server");
				rootVector = rfs.getFilelist(getBackPath());
			} catch (Exception ex) {
				System.err.println("Client: " + ex.getMessage());
				clientGUI.setSysMsg("Unable to connect to " + hostName);
			}
			clientGUI.setSysMsg("...connected ... to " + hostName);
			showFileList(rootVector);
		}
	}

	/**
	 * Listener For Exit 
	 */
	class ListenerForExit implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int n = JOptionPane.showConfirmDialog(null, "Do you want to exit", "Exit", JOptionPane.YES_NO_OPTION);
			if (n == 0) {
				System.exit(0);
			}
		}
	}
}
