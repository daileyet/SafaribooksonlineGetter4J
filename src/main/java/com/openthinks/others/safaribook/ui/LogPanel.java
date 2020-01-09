package com.openthinks.others.safaribook.ui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

 class LogPanel extends LanucherPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5100967008524290644L;
	JTextArea textAreaLog;

	/**
	 * Create the panel.
	 */
	public LogPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblLog = new JLabel("Logs");
		lblLog.setFont(new Font("Tahoma", Font.BOLD, 12));
		GridBagConstraints gbc_lblLog = new GridBagConstraints();
		gbc_lblLog.anchor = GridBagConstraints.WEST;
		gbc_lblLog.insets = new Insets(5, 5, 5, 0);
		gbc_lblLog.gridx = 0;
		gbc_lblLog.gridy = 0;
		add(lblLog, gbc_lblLog);
		
		JScrollPane scrollPaneLog = new JScrollPane();
		GridBagConstraints gbc_scrollPaneLog = new GridBagConstraints();
		gbc_scrollPaneLog.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneLog.gridx = 0;
		gbc_scrollPaneLog.gridy = 1;
		add(scrollPaneLog, gbc_scrollPaneLog);
		
		textAreaLog = new JTextArea();
		textAreaLog.setEditable(false);
		scrollPaneLog.setViewportView(textAreaLog);

	}

}
