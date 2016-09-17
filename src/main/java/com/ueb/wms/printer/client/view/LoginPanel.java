package com.ueb.wms.printer.client.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ueb.wms.printer.client.constants.WmsConstants;
import com.ueb.wms.printer.client.service.IPrinterService;
import com.ueb.wms.printer.client.util.PrintViewUtil;

@SuppressWarnings("serial")
@Component("loginPanel")
public class LoginPanel extends JPanel implements IBaseView {

	private LoginView parent;

	public void setParent(LoginView parent) {
		this.parent = parent;
	}

	@Autowired
	private IPrinterService printerService;

	@Autowired
	private MainView mainView;

	@Autowired
	private JTextFieldPassword tfPassword;

	@Autowired
	private JTextFieldUser tfUser;

	@Override
	public void displayUI() {
		this.initContainer();
		this.showResource();
	}

	private void initContainer() {
		this.setLayout(new BorderLayout(0, 5));
	}

	private void showResource() {
		Box vbox = Box.createVerticalBox();
		vbox.add(Box.createVerticalStrut(20));
		vbox.add(this.createBoxPanel());
		// vbox.add(Box.createVerticalStrut(20));

		this.add(vbox, BorderLayout.NORTH);
		this.add(new JPanel(), BorderLayout.CENTER);
	}

	private JPanel createBoxPanel() {
		Box accountBox = PrintViewUtil.createSingleBox("登陆账号:", null, this.tfUser);
		Box passwordBox = PrintViewUtil.createSingleBox("登陆密码:", null, this.tfPassword);

		Box[] boies = { accountBox, passwordBox };
		// ActionListener okBtnListener = new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent e) {
		// LoginPanel.this.userLogin();
		// }
		// };

		JButton[] btns = this.createBtns();
		JPanel btnPanel = PrintViewUtil.createButtonBoxPanel(btns);
		// JPanel btnPanel = this.createButtonPanel(okBtnListener);

		JPanel boxPanel = PrintViewUtil.createSingleBoxPanel(15, boies, btnPanel);
		// JPanel boxPanel = this.createSingleBoxPanel(boies, btnPanel);
		return boxPanel;
	}

	private void userLogin() {
		String account = this.tfUser.getText();
		if (StringUtils.isBlank(account)) {
			this.tfUser.requestFocus();
			PrintViewUtil.showErrorMsg("登陆账号不能为空");
			return;
		}
		String password = String.valueOf(this.tfPassword.getPassword());
		if (StringUtils.isBlank(password)) {
			this.tfPassword.requestFocus();
			PrintViewUtil.showErrorMsg("登陆密码不能为空");
			return;
		}
		String md5Password = Md5Crypt.md5Crypt(password.getBytes(WmsConstants.UTF8));
		try {
			// this.printerService.userLogin(account, md5Password);
			this.parent.exit();
			this.mainView.displayUI();
		} catch (Exception e) {
			logger.info("用户登陆系统出现异常，用户账号是：{}，异常详细信息是：{}", account, e.getMessage());
			PrintViewUtil.showErrorMsg("登陆系统出现异常");
		}
	}

	// private JPanel createSingleBoxPanel(Box[] boies, JPanel btnPanel) {
	// JPanel boxPanel = new JPanel();
	// boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS));
	// for (int i = 0, len = boies.length; i < len; i++) {
	// boxPanel.add(Box.createVerticalStrut(15));
	// boxPanel.add(boies[i]);
	// }
	// boxPanel.add(Box.createVerticalStrut(15));
	// if (null != btnPanel) {
	// boxPanel.add(btnPanel);
	// boxPanel.add(Box.createVerticalStrut(15));
	// }
	// return boxPanel;
	// }

	private JButton[] createBtns() {
		JButton okBtn = new JButton("登陆");
		okBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				LoginPanel.this.userLogin();
			}
		});
		JButton configBtn = new JButton("设置");
		configBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				LoginPanel.this.parent.showServerInfoView();
			}
		});
		JButton[] btns = { okBtn, configBtn };
		return btns;
	}

	// private JPanel createButtonPanel(ActionListener okBtnListener) {
	// JButton okBtn = new JButton("登陆");
	// if (null != okBtnListener) {
	// okBtn.addActionListener(okBtnListener);
	// }
	// JButton configBtn = new JButton("设置");
	//
	// JPanel buttonPane = new JPanel();
	// buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
	// buttonPane.add(Box.createHorizontalGlue());
	// buttonPane.add(okBtn);
	// buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
	// buttonPane.add(configBtn);
	// buttonPane.add(Box.createRigidArea(new Dimension(30, 0)));
	// return buttonPane;
	// }
	//
	// private JTextField createJTextField() {
	// JTextField textFiled = new JTextField();
	// textFiled.setPreferredSize(new Dimension(0, 26));
	// return textFiled;
	// }
	//
	// private Box createSingleBox(String text, java.awt.Component component) {
	// JLabel label = new JLabel(text, SwingConstants.RIGHT);
	// label.setPreferredSize(new Dimension(70, 0));
	// if (null == component) {
	// component = this.createJTextField();
	// }
	//
	// Box box = Box.createHorizontalBox();
	// box.add(Box.createHorizontalStrut(20));
	// box.add(label);
	// box.add(Box.createHorizontalStrut(10));
	// box.add(component);
	// box.add(Box.createHorizontalStrut(30));
	// return box;
	// }
}
