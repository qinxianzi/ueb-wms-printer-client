package com.ueb.wms.printer.client.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class PrintViewUtil {
	private static final Font font = new Font("微软雅黑", Font.PLAIN, 12);

	public static void showInformationMsg(String message) {
		JLabel label = new JLabel(message);
		label.setFont(font);
		JOptionPane.showMessageDialog(null, label, "系统提示", JOptionPane.INFORMATION_MESSAGE);
	}

	public static void showErrorMsg(String message) {
		JLabel label = new JLabel(message);
		label.setFont(font);
		JOptionPane.showMessageDialog(null, label, "错误提示", JOptionPane.ERROR_MESSAGE);
	}

	public static int showOptionDialog(String message) {
		JLabel label = new JLabel(message);
		label.setFont(font);
		int result = JOptionPane.showOptionDialog(null, label, "系统提示", JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, new String[] { "是", "否", "取消" }, "是");
		return result;
	}

	public static JTextField createJTextField() {
		JTextField textFiled = new JTextField();
		textFiled.setPreferredSize(new Dimension(0, 26));
		return textFiled;
	}

	/**
	 * 创建一行（JLabel:JTextField/JComboBox）
	 * 
	 * @param text
	 * @param labelDim
	 * @param component
	 * @return
	 */
	public static Box createSingleBox(String text, Dimension labelDim, Component component) {
		JLabel label = new JLabel(text, SwingConstants.RIGHT);
		labelDim = null == labelDim ? new Dimension(70, 0) : labelDim;
		label.setPreferredSize(labelDim);
		component = null == component ? createJTextField() : component;

		Box box = Box.createHorizontalBox();
		box.add(Box.createHorizontalStrut(20));
		box.add(label);
		box.add(Box.createHorizontalStrut(10));
		box.add(component);
		box.add(Box.createHorizontalStrut(30));
		return box;
	}

	/**
	 * 创建一组Box（一个Box表示一行）
	 * 
	 * @param title
	 * @param boies
	 * @return
	 */
	public static JPanel createSingleBoxPanel(int height, Box[] boies, JPanel btnPanel) {
		// TitledBorder border = BorderFactory.createTitledBorder(new
		// LineBorder(Color.GRAY, 1), title);
		JPanel boxPanel = new JPanel();
		// boxPanel.setBorder(border);
		boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS));
		for (int i = 0, len = boies.length; i < len; i++) {
			boxPanel.add(Box.createVerticalStrut(height));
			boxPanel.add(boies[i]);
		}
		boxPanel.add(Box.createVerticalStrut(height));
		if (null != btnPanel) {
			boxPanel.add(btnPanel);
			boxPanel.add(Box.createVerticalStrut(height));
		}
		return boxPanel;
	}

	public static JPanel createButtonBoxPanel(JButton[] btns) {
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
		buttonPane.add(Box.createHorizontalGlue());
		for (int i = 0, len = btns.length; i < len; i++) {
			buttonPane.add(btns[i]);
			if (i == len - 1) {
				continue;
			}
			buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		}
		// buttonPane.add(okBtn);
		// buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		// buttonPane.add(configBtn);
		buttonPane.add(Box.createRigidArea(new Dimension(30, 0)));
		return buttonPane;
	}
}
