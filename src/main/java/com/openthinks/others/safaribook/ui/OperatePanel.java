package com.openthinks.others.safaribook.ui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

class OperatePanel extends LanucherPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4561306544547395699L;
	JTextField textBookName;
	JTextField textURL;
	private JLabel lblBookName;
	private JLabel lblCat;
	private JButton btnAdd;
	private JButton btnStop;
	private JButton btnStart;

	/**
	 * Create the panel.
	 */
	public OperatePanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 5, 39, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0 };
		gridBagLayout.rowHeights = new int[] { 5, 0, 0, 5, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		lblBookName = new JLabel("Book Name");
		lblBookName.setFont(new Font("Tahoma", Font.BOLD, 11));
		GridBagConstraints gbc_lblBookName = new GridBagConstraints();
		gbc_lblBookName.insets = new Insets(0, 0, 5, 5);
		gbc_lblBookName.anchor = GridBagConstraints.WEST;
		gbc_lblBookName.gridx = 1;
		gbc_lblBookName.gridy = 1;
		add(lblBookName, gbc_lblBookName);

		textBookName = new JTextField();
		GridBagConstraints gbc_textBookName = new GridBagConstraints();
		gbc_textBookName.fill = GridBagConstraints.HORIZONTAL;
		gbc_textBookName.gridwidth = 9;
		gbc_textBookName.insets = new Insets(0, 0, 5, 5);
		gbc_textBookName.gridx = 2;
		gbc_textBookName.gridy = 1;
		add(textBookName, gbc_textBookName);
		textBookName.setColumns(10);

		btnAdd = new JButton("Add");
		GridBagConstraints gbc_btnAdd = new GridBagConstraints();
		gbc_btnAdd.fill = GridBagConstraints.VERTICAL;
		gbc_btnAdd.gridheight = 2;
		gbc_btnAdd.insets = new Insets(0, 0, 5, 5);
		gbc_btnAdd.gridx = 11;
		gbc_btnAdd.gridy = 1;
		add(btnAdd, gbc_btnAdd);

		btnStart = new JButton("Start");
		GridBagConstraints gbc_btnStart = new GridBagConstraints();
		gbc_btnStart.gridheight = 2;
		gbc_btnStart.insets = new Insets(0, 0, 5, 5);
		gbc_btnStart.fill = GridBagConstraints.BOTH;
		gbc_btnStart.gridx = 12;
		gbc_btnStart.gridy = 1;
		add(btnStart, gbc_btnStart);

		btnStop = new JButton("Stop");

		GridBagConstraints gbc_btnStop = new GridBagConstraints();
		gbc_btnStop.fill = GridBagConstraints.VERTICAL;
		gbc_btnStop.gridheight = 2;
		gbc_btnStop.insets = new Insets(0, 0, 5, 5);
		gbc_btnStop.gridx = 13;
		gbc_btnStop.gridy = 1;
		add(btnStop, gbc_btnStop);

		lblCat = new JLabel("Catalog URL");
		lblCat.setFont(new Font("Tahoma", Font.BOLD, 11));
		GridBagConstraints gbc_lblCat = new GridBagConstraints();
		gbc_lblCat.insets = new Insets(0, 0, 5, 5);
		gbc_lblCat.anchor = GridBagConstraints.WEST;
		gbc_lblCat.gridx = 1;
		gbc_lblCat.gridy = 2;
		add(lblCat, gbc_lblCat);

		textURL = new JTextField();
		GridBagConstraints gbc_textURL = new GridBagConstraints();
		gbc_textURL.fill = GridBagConstraints.HORIZONTAL;
		gbc_textURL.gridwidth = 9;
		gbc_textURL.insets = new Insets(0, 0, 5, 5);
		gbc_textURL.gridx = 2;
		gbc_textURL.gridy = 2;
		add(textURL, gbc_textURL);
		textURL.setColumns(10);

	}

	@Override
	void setLauncher(SafariBookUILauncher launcher) {
		super.setLauncher(launcher);
		btnAdd.addActionListener(launcher.BOOK_ADD);
		btnStart.addActionListener(launcher.START);
		btnStop.addActionListener(launcher.STOP);
	}

}
