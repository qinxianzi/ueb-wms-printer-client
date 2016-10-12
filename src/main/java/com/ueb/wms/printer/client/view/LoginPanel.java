package com.ueb.wms.printer.client.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ueb.wms.printer.client.service.IUserService;
import com.ueb.wms.printer.client.util.LoginUtil;
import com.ueb.wms.printer.client.util.PrintViewUtil;
import com.ueb.wms.printer.client.util.UebMd5Util;
import com.ueb.wms.printer.client.vo.BeforeLoginVO;
import com.ueb.wms.printer.client.vo.ComboBoxItemVO;
import com.ueb.wms.printer.client.vo.LoginUserVO;
import com.ueb.wms.printer.client.vo.WarehouseVO;

@SuppressWarnings("serial")
@Component("loginPanel")
public class LoginPanel extends JPanel implements IBaseView {

	@Autowired
	private LoginUtil loginUtil;

	private LoginView parent;

	public void setParent(LoginView parent) {
		this.parent = parent;
	}

	@Autowired
	private IUserService userService;

	@Autowired
	private MainView mainView;

	@Autowired
	private JTextFieldPassword tfPassword;

	@Autowired
	private JTextFieldUser tfUser;

	private JComboBoxExt<ComboBoxItemVO> warehouseCmb;

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

		this.add(vbox, BorderLayout.NORTH);
		this.add(new JPanel(), BorderLayout.CENTER);
	}

	private JPanel createBoxPanel() {
		this.tfUser.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (KeyEvent.VK_ENTER == e.getKeyCode()) {
					LoginPanel.this.readyToLogin();
				}
			}
		});
		this.tfUser.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				LoginPanel.this.readyToLogin();
			}
		});
		this.tfPassword.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (KeyEvent.VK_ENTER == e.getKeyCode()) {
					LoginPanel.this.userLogin();
				}
			}
		});
		warehouseCmb = new JComboBoxExt<ComboBoxItemVO>();

		Box accountBox = PrintViewUtil.createSingleBox("用户名称:", null, this.tfUser);
		Box passwordBox = PrintViewUtil.createSingleBox("用户密码:", null, this.tfPassword);
		Box contextBox = PrintViewUtil.createSingleBox("业务仓库:", null, warehouseCmb);
		Box[] boies = { accountBox, passwordBox, contextBox };

		JButton[] btns = this.createBtns();
		JPanel btnPanel = PrintViewUtil.createButtonBoxPanel(btns);

		JPanel boxPanel = PrintViewUtil.createSingleBoxPanel(5, boies, btnPanel);
		return boxPanel;
	}

	private void readyToLogin() {
		String account = this.tfUser.getText();
		if (StringUtils.isBlank(account)) {
			this.tfUser.requestFocus();
			PrintViewUtil.showErrorMsg("登陆账号不能为空");
			return;
		}
		try {
			BeforeLoginVO resVo = this.userService.readyToLogin(account);
			List<WarehouseVO> warehouseList = resVo.getWarehouse();
			Vector<ComboBoxItemVO> model = new Vector<ComboBoxItemVO>();
			for (WarehouseVO warehouse : warehouseList) {
				model.add(warehouse.adapt2ComboBoxItemVO());
			}
			warehouseCmb.updateData(model);
			this.tfPassword.requestFocus();
		} catch (Exception e) {
			logger.info("用户登陆系统--获取业务仓库信息失败，用户账号是：{}，异常详细信息是：{}", account, e.getMessage());
			PrintViewUtil.showErrorMsg("获取业务仓库信息失败");
			this.tfUser.selectAll();
			this.tfUser.requestFocus();
		}
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
		// String md5Password = DigestUtils.md5Hex(password);
		try {
			// AccountVO accountVo = this.userService.userLogin(account,
			// md5Password);
			// loginUtil.setLoginAccount(accountVo);
			LoginUserVO loginUserVo = this.userService.findUuser(account);
			String md5Pwd = UebMd5Util.getMd5(password, loginUserVo.getSalt());
			if (!StringUtils.equals(md5Pwd, loginUserVo.getPassword())) {
				PrintViewUtil.showErrorMsg("登陆密码错误");
				return;
			}
			loginUtil.setLoginUserVo(loginUserVo);
			String warehouse = warehouseCmb.getSelectedKey();
			loginUtil.setWarehouse(warehouse);

			this.parent.exit();
			this.mainView.displayUI();
		} catch (Exception e) {
			logger.info("用户登陆系统出现异常，用户账号是：{}，异常详细信息是：{}", account, e.getMessage());
			PrintViewUtil.showErrorMsg("用户登陆系统失败");
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
