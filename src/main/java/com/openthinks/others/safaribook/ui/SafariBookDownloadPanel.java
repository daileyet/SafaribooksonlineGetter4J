package com.openthinks.others.safaribook.ui;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JSplitPane;
import java.awt.GridBagConstraints;

public class SafariBookDownloadPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7538087659350471378L;

	/**
	 * Create the panel.
	 */
	public SafariBookDownloadPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JSplitPane splitPaneMain = new JSplitPane();
		splitPaneMain.setEnabled(false);
		splitPaneMain.setOrientation(JSplitPane.VERTICAL_SPLIT);
		GridBagConstraints gbc_splitPaneMain = new GridBagConstraints();
		gbc_splitPaneMain.fill = GridBagConstraints.BOTH;
		gbc_splitPaneMain.gridx = 0;
		gbc_splitPaneMain.gridy = 0;
		add(splitPaneMain, gbc_splitPaneMain);
		
		OperatePanel operatePanel = new OperatePanel();
		splitPaneMain.setLeftComponent(operatePanel);
		
		JSplitPane splitPaneTip = new JSplitPane();
		splitPaneTip.setResizeWeight(0.1);
		splitPaneMain.setRightComponent(splitPaneTip);
		
		WorklistPanel worklistPanel = new WorklistPanel();
		splitPaneTip.setLeftComponent(worklistPanel);
		
		LogPanel logPanel = new LogPanel();
		splitPaneTip.setRightComponent(logPanel);

	}

}
