package com.ueb.wms.printer.client.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ueb.wms.printer.client.constants.WmsConstants;
import com.ueb.wms.printer.client.util.PrintViewUtil;

@SuppressWarnings("serial")
@Component("mainView")
public class MainView extends JFrame implements IBaseView {

	@Autowired
	private TabView tabView;

	private boolean isExitOnClose = true;

	public MainView() {
		this(false);
	}

	public MainView(boolean isExitOnClose) {
		this.isExitOnClose = isExitOnClose;
	}

	@Override
	public void displayUI() {
		this.initContainer();
		this.showResource();
	}

	private void initContainer() {
		this.setDefaultCloseOperation(MainView.DO_NOTHING_ON_CLOSE);
		this.setTitle(WmsConstants.UEB_PRINT_VIEW_Title);
		URL url = this.getClass().getResource(WmsConstants.UEB_PRINT_VIEW_ICON);
		this.setIconImage(new ImageIcon(url).getImage());

		// this.fullScreen();
		MainView.setDefaultLookAndFeelDecorated(false);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				MainView.this.exitForm();
			}
			// public void windowOpened(WindowEvent e) {
			// MainView.this.setDefaultFocus();
			// }
		});
	}

	// protected void setDefaultFocus() {
	// this.pnlMain.setDefaultFocus();
	// }

	protected void fullScreen() {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Insets insets = toolkit.getScreenInsets(
				GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration());
		Dimension screenSize = toolkit.getScreenSize();
		int width = screenSize.width - insets.left - insets.right;
		int height = screenSize.height - insets.top - insets.bottom;

		Dimension size = new Dimension(width, height);
		this.setSize(size);
		this.setPreferredSize(size);
		this.tabView.measureSize(size);
	}

	private void showResource() {
		Container content = this.getContentPane();
		content.add(this.tabView, BorderLayout.CENTER);

		JLabel label = new JLabel("17:35:03 登陆系统，陆账号是：，用户名是：");
		// label.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		JPanel footer = new JPanel();
		footer.add(label);
		content.add(footer, BorderLayout.NORTH);

		this.tabView.displayUI();

		this.fullScreen();
		this.setVisible(true);
	}

	private boolean exitForm() {
		int result = PrintViewUtil.showOptionDialog("您确认要退出系统吗?");
		if (result == JOptionPane.YES_OPTION) {
			exit();
			return true;
		}
		return false;
	}

	private void exit() {
		if (this.isExitOnClose) {
			System.exit(0);
		} else {
			this.setVisible(false);
			this.tabView.clear();
			this.tabView = null;
			this.getContentPane().removeAll();
			this.dispose();
		}
	}
}
