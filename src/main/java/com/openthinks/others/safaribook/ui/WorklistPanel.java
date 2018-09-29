package com.openthinks.others.safaribook.ui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import com.openthinks.others.safaribook.DownloadBookTaskInfo;

class WorklistPanel extends LanucherPanel {

	private static final long serialVersionUID = -5106895977773266220L;
	 JList<DownloadBookTaskInfo> listWorklist;

	/**
	 * Create the panel.
	 */
	public WorklistPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JLabel lblWorklistTitle = new JLabel("Worklist");
		lblWorklistTitle.setFont(new Font("Tahoma", Font.BOLD, 12));
		GridBagConstraints gbc_lblWorklistTitle = new GridBagConstraints();
		gbc_lblWorklistTitle.anchor = GridBagConstraints.WEST;
		gbc_lblWorklistTitle.insets = new Insets(5, 5, 5, 0);
		gbc_lblWorklistTitle.gridx = 0;
		gbc_lblWorklistTitle.gridy = 0;
		add(lblWorklistTitle, gbc_lblWorklistTitle);

		JScrollPane scrollPaneWorklist = new JScrollPane();
		GridBagConstraints gbc_scrollPaneWorklist = new GridBagConstraints();
		gbc_scrollPaneWorklist.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneWorklist.gridx = 0;
		gbc_scrollPaneWorklist.gridy = 1;
		add(scrollPaneWorklist, gbc_scrollPaneWorklist);

		listWorklist = new JList<>();
		listWorklist.setModel(new DefaultListModel<DownloadBookTaskInfo>());
		scrollPaneWorklist.setViewportView(listWorklist);
		listWorklist.setFont(new Font("Tahoma", Font.PLAIN, 12));
		listWorklist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

	}

	
}
