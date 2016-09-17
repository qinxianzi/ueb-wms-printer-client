package com.ueb.wms.printer.client.view;

import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.ImageIcon;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@SuppressWarnings("serial")
@Component("tfUser")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class JTextFieldUser extends JTextFieldSearch {

	public JTextFieldUser() {
		icon = new ImageIcon(this.getClass().getResource("/icon/user.png"));
		Insets insets = new Insets(0, 20, 0, 0);
		this.setPreferredSize(new Dimension(0, 26));
		this.setMargin(insets);
	}
}
