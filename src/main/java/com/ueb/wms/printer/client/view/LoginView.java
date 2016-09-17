package com.ueb.wms.printer.client.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ueb.wms.printer.client.constants.WmsConstants;
import com.ueb.wms.printer.client.util.PrintViewUtil;

@SuppressWarnings("serial")
@Component("loginView")
public class LoginView extends JFrame implements IBaseView {

	@Autowired
	private LoginPanel loginPanel;

	@Autowired
	private ServerInfoView serverInfoView;

	private boolean isExitOnClose = true;

	public LoginView() {
		this(false);
	}

	public LoginView(boolean isExitOnClose) {
		this.isExitOnClose = isExitOnClose;
		this.setResizable(false);
		this.setSize(362, 208);
		this.setLocationCenter();
	}

	private void showResource() {
		Container content = this.getContentPane();
		this.loginPanel.setParent(this);
		this.serverInfoView.setParent(this);
		this.setTitle("用户登陆");
		content.add(this.loginPanel, BorderLayout.CENTER);

		this.loginPanel.displayUI();
		this.serverInfoView.displayUI();
		this.setVisible(true);
	}

	public void showLoginPanel() {
		Container content = this.getContentPane();
		this.setSize(this.getWidth() + 1, this.getHeight() - 1);
		this.setTitle("用户登陆");
		content.remove(this.serverInfoView);
		content.add(this.loginPanel, BorderLayout.CENTER);
	}

	public void showServerInfoView() {
		Container content = this.getContentPane();
		this.setSize(this.getWidth() - 1, this.getHeight() + 1);
		this.setTitle("服务器信息");
		content.remove(this.loginPanel);
		content.add(this.serverInfoView, BorderLayout.CENTER);
	}

	private void initContainer() {
		this.setDefaultCloseOperation(MainView.DO_NOTHING_ON_CLOSE);
		URL url = this.getClass().getResource(WmsConstants.UEB_PRINT_VIEW_ICON);
		this.setIconImage(new ImageIcon(url).getImage());

		MainView.setDefaultLookAndFeelDecorated(false);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				LoginView.this.exitForm();
			}
		});
	}

	public boolean exitForm() {
		int result = PrintViewUtil.showOptionDialog("您确认要退出系统吗?");
		if (result == JOptionPane.YES_OPTION) {
			exit();
			return true;
		}
		return false;
	}

	public void exit() {
		if (this.isExitOnClose) {
			System.exit(0);
		} else {
			this.setVisible(false);
			this.getContentPane().removeAll();
			this.dispose();
		}
	}

	@Override
	public void displayUI() {
		this.initContainer();
		this.showResource();
	}

	public void setLocationCenter() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension compSize = this.getSize();
		if (compSize.height > screenSize.height) {
			compSize.height = screenSize.height;
		}
		if (compSize.width > screenSize.width) {
			compSize.width = screenSize.width;
		}
		this.setLocation((screenSize.width - compSize.width) / 2, (screenSize.height - compSize.height) / 2);
	}
}
