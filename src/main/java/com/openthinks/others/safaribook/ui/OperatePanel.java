package com.openthinks.others.safaribook.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Font;

public class OperatePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4561306544547395699L;
	private JTextField textField;
	private JTextField textField_1;
	private JButton btnStop;
	private JLabel lblBookName;
	private JLabel lblCat;

	/**
	 * Create the panel.
	 */
	public OperatePanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 39, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		lblBookName = new JLabel("Book Name");
		lblBookName.setFont(new Font("Tahoma", Font.BOLD, 11));
		GridBagConstraints gbc_lblBookName = new GridBagConstraints();
		gbc_lblBookName.insets = new Insets(0, 0, 5, 5);
		gbc_lblBookName.anchor = GridBagConstraints.WEST;
		gbc_lblBookName.gridx = 1;
		gbc_lblBookName.gridy = 1;
		add(lblBookName, gbc_lblBookName);
		
		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridwidth = 9;
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.gridx = 2;
		gbc_textField.gridy = 1;
		add(textField, gbc_textField);
		textField.setColumns(10);
		
		lblCat = new JLabel("Catalog URL");
		lblCat.setFont(new Font("Tahoma", Font.BOLD, 11));
		GridBagConstraints gbc_lblCat = new GridBagConstraints();
		gbc_lblCat.insets = new Insets(0, 0, 5, 5);
		gbc_lblCat.anchor = GridBagConstraints.WEST;
		gbc_lblCat.gridx = 1;
		gbc_lblCat.gridy = 2;
		add(lblCat, gbc_lblCat);
		
		textField_1 = new JTextField();
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridwidth = 9;
		gbc_textField_1.insets = new Insets(0, 0, 5, 5);
		gbc_textField_1.gridx = 2;
		gbc_textField_1.gridy = 2;
		add(textField_1, gbc_textField_1);
		textField_1.setColumns(10);
		
		JButton btnNewButton = new JButton("Start");
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton.gridwidth = 4;
		gbc_btnNewButton.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewButton.gridx = 3;
		gbc_btnNewButton.gridy = 3;
		add(btnNewButton, gbc_btnNewButton);
		
		btnStop = new JButton("Stop");
		GridBagConstraints gbc_btnStop = new GridBagConstraints();
		gbc_btnStop.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnStop.gridwidth = 4;
		gbc_btnStop.insets = new Insets(0, 0, 0, 5);
		gbc_btnStop.gridx = 7;
		gbc_btnStop.gridy = 3;
		add(btnStop, gbc_btnStop);

	}
}
