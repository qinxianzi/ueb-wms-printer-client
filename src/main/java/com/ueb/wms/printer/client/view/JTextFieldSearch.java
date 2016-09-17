package com.ueb.wms.printer.client.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JTextField;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@SuppressWarnings("serial")
@Component("tfSearch")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class JTextFieldSearch extends JTextField {

	protected ImageIcon icon;

	public JTextFieldSearch() {
		icon = new ImageIcon(this.getClass().getResource("/icon/search.png"));
		Insets insets = new Insets(0, 20, 0, 0);
		this.setPreferredSize(new Dimension(0, 26));
		this.setMargin(insets);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Insets insets = this.getInsets();
		int iconWidth = icon.getIconWidth();
		int iconHeight = icon.getIconHeight();
		int height = this.getHeight();
		this.icon.paintIcon(this, g, (insets.left - iconWidth) / 2, (height - iconHeight) / 2);
	}
}
