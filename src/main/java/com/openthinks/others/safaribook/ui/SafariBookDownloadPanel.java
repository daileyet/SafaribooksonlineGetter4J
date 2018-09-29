package com.openthinks.others.safaribook.ui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;

class SafariBookDownloadPanel extends LanucherPanel {
	private static final long serialVersionUID = -7538087659350471378L;
	private List<LanucherPanel> children = new ArrayList<>();
	/**
	 * Create the panel.
	 */
	public SafariBookDownloadPanel() {
		setLayout(new BorderLayout(0, 0));

		JMenuBar menuBar = new JMenuBar();
		add(menuBar, BorderLayout.NORTH);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmLoad = new JMenuItem("Load");
		mntmLoad.setToolTipText("Load Configuration");
		mnFile.add(mntmLoad);

		JMenuItem mntmCreate = new JMenuItem("Create");
		mntmCreate.setToolTipText("Create Configuration");
		mnFile.add(mntmCreate);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);

		JSplitPane splitPaneMain = new JSplitPane();
		splitPaneMain.setEnabled(false);
		splitPaneMain.setOrientation(JSplitPane.VERTICAL_SPLIT);
		add(splitPaneMain);

		OperatePanel operatePanel = new OperatePanel();
		splitPaneMain.setLeftComponent(operatePanel);
		children.add(operatePanel);
		JSplitPane splitPaneTip = new JSplitPane();
		splitPaneTip.setResizeWeight(0.1);
		splitPaneMain.setRightComponent(splitPaneTip);

		WorklistPanel worklistPanel = new WorklistPanel();
		splitPaneTip.setLeftComponent(worklistPanel);
		children.add(worklistPanel);
		
		LogPanel logPanel = new LogPanel();
		splitPaneTip.setRightComponent(logPanel);
		children.add(logPanel);
	}
	
	@Override
	void setLauncher(SafariBookUILauncher launcher) {
		super.setLauncher(launcher);
		children.forEach(panel->panel.setLauncher(launcher));
	}

}
