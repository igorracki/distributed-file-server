import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;

/**
 * Class ClientGUI
 * Provide GUI for Client 
 */
public class ClientGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private JLabel systemJL;
	private JLabel homeFolderJL;
	private String sysMsg = "System messsage: ";
	private String homeMsg = "Home Folder: ";

	private JMenuBar mb;

	private JMenuItem downloadJM;
	private JMenuItem uploadJM;
	private JMenuItem deleteJM;
	private JMenuItem newFolderJM;
	private JMenuItem openFolderJM;
	private JMenuItem upFolderJM;

	private JMenuItem connectJMI;
	private JMenuItem exitJMI;
	private JMenuItem homeFolderJMI;

	private JList<String> list;
	private DefaultListModel<String> model;

	/**
	 * Constructor
	 */
	public ClientGUI() {
		super("DIstrubited File Clent");

		downloadJM = new JMenuItem("Download");
		uploadJM = new JMenuItem("Upload");
		deleteJM = new JMenuItem("Delete");
		newFolderJM = new JMenuItem("New Folder");
		openFolderJM = new JMenuItem("Open Folder");
		upFolderJM = new JMenuItem("UP Folder");

		systemJL = new JLabel(sysMsg);
		homeFolderJL = new JLabel(homeMsg);

		mb = new JMenuBar();
		setJMenuBar(mb);
		JMenu fileMenu = new JMenu("File", false);
		connectJMI = new JMenuItem("Connect to server");
		exitJMI = new JMenuItem("Exit");
		homeFolderJMI = new JMenuItem("Select home folder");

		fileMenu.add(connectJMI);
		fileMenu.add(homeFolderJMI);
		fileMenu.addSeparator();
		fileMenu.add(exitJMI);
		mb.add(fileMenu);
		mb.add(upFolderJM);
		mb.add(openFolderJM);
		mb.add(downloadJM);
		mb.add(uploadJM);
		mb.add(deleteJM);
		mb.add(newFolderJM);

		model = new DefaultListModel<String>();

		list = new JList<String>(model); 
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		JScrollPane filePanel = new JScrollPane(list);

		getContentPane().add(homeFolderJL, BorderLayout.PAGE_START);
		getContentPane().add(filePanel, BorderLayout.CENTER);
		getContentPane().add(systemJL, BorderLayout.PAGE_END);

		// frame properties
		setSize(600, 400);
		setLocation(300, 300);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * Set Model To List
	 * 
	 * @param model
	 *            DefaultListModel<String>
	 */
	public void setModelToList(DefaultListModel<String> model) {
		list.setModel(model);
	}

	/**
	 * Set SysMsg
	 * 
	 * @param str
	 *            String
	 */
	public void setSysMsg(String str) {
		systemJL.setText(sysMsg + str);
	}

	/**
	 * Set Home Folder
	 * 
	 * @param str
	 *            String
	 */
	public void setHomeFolderJL(String str) {
		homeFolderJL.setText(homeMsg + str);
	}

	/**
	 * Add List Listener
	 * 
	 * @param listSelectionlistener
	 *            ListSelectionListener
	 */
	public void addListListener(ListSelectionListener listSelectionlistener) {
		list.addListSelectionListener(listSelectionlistener);

	}

	/**
	 * addUpFolderListener
	 * 
	 * @param listenerForUpFolder
	 *            ActionListener
	 */
	public void addUpFolderListener(ActionListener listenerForUpFolder) {
		upFolderJM.addActionListener(listenerForUpFolder);
	}

	/**
	 * addHomeFolderListener
	 * 
	 * @param listenerForHomeFolder
	 *            ActionListener
	 */
	public void addHomeFolderListener(ActionListener listenerForHomeFolder) {
		homeFolderJMI.addActionListener(listenerForHomeFolder);
	}

	/**
	 * Add Open Folder Listener
	 * 
	 * @param listenerOpenFolderFolder
	 *            ActionListener
	 */
	public void addOpenFolderListener(ActionListener listenerOpenFolderFolder) {
		openFolderJM.addActionListener(listenerOpenFolderFolder);
	}

	/**
	 * Add Connect Listener
	 * 
	 * @param listenerForConnect
	 *            ActionListener
	 */
	public void addConnectListener(ActionListener listenerForConnect) {
		connectJMI.addActionListener(listenerForConnect);
	}

	/**
	 * 
	 * @param listenerForExit
	 *            ActionListener
	 */
	public void addExitListener(ActionListener listenerForExit) {
		exitJMI.addActionListener(listenerForExit);
	}

	/**
	 * 
	 * @param listenerForDownload
	 *            ActionListener
	 */
	public void addDownloadListener(ActionListener listenerForDownload) {
		downloadJM.addActionListener(listenerForDownload);
	}

	/**
	 * Add Upload Listener
	 * 
	 * @param listenerForUpload
	 *            ActionListener
	 */
	public void addUploadListener(ActionListener listenerForUpload) {
		uploadJM.addActionListener(listenerForUpload);
	}

	/**
	 * Add Delete Listener
	 * 
	 * @param listenerForDelete
	 *            ActionListener
	 */
	public void addDeleteListener(ActionListener listenerForDelete) {
		deleteJM.addActionListener(listenerForDelete);
	}

	/**
	 * Add New Folder Listener
	 * 
	 * @param listenerForNewFolder
	 *            ActionListener
	 */
	public void addNewFolderListener(ActionListener listenerForNewFolder) {
		newFolderJM.addActionListener(listenerForNewFolder);
	}
}
