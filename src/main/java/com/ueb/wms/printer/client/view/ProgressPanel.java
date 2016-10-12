package com.ueb.wms.printer.client.view;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.FlowLayout;
import java.awt.Label;

public class ProgressPanel extends JPanel {

	public ProgressPanel() {
		Label label = new Label();
		label.setPreferredSize(new Dimension(5, 5));
		
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(10);
		this.setPreferredSize(new Dimension(480, 23));

		JTextField textField = new JTextField();
		textField.setBackground(Color.YELLOW);
		textField.setPreferredSize(new Dimension(150, 23));
		add(textField);
//		this.add(label);

		JTextField textField_1 = new JTextField();
		add(textField_1);
//		this.add(label);
		textField_1.setBackground(Color.GREEN);
		textField_1.setPreferredSize(new Dimension(150, 23));

		JTextField textField_2 = new JTextField();
		add(textField_2);
		textField_2.setBackground(Color.RED);
		textField_2.setPreferredSize(new Dimension(150, 23));
	}
}
