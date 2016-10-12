package com.ueb.wms.printer.client.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ueb.wms.printer.client.service.IPrinterService;
import com.ueb.wms.printer.client.util.PrintViewUtil;

@SuppressWarnings("serial")
@Component("serverInfoView")
public class ServerInfoView extends JPanel implements IBaseView {

	private LoginView parent;

	public void setParent(LoginView parent) {
		this.parent = parent;
	}

	@Autowired
	protected IPrinterService printerService;

	private JTextField urlTf, portTf, contextTf, pdfFileTf;
	private JButton okBtn, cancelBtn;

	protected void initContainer() {
		this.setLayout(new BorderLayout(0, 5));
	}

	protected void showResource() {
		Box vbox = Box.createVerticalBox();
		vbox.add(Box.createVerticalStrut(20));
		vbox.add(this.createServerPanel(true));
		// vbox.add(Box.createVerticalStrut(20));

		this.add(vbox, BorderLayout.NORTH);
		this.add(new JPanel(), BorderLayout.CENTER);
	}

	@Override
	public void displayUI() {
		this.initContainer();
		this.showResource();
	}

	protected JPanel createServerPanel(boolean login) {
		Dimension labelDim = new Dimension(100, 0);
		Map<String, String> values = this.printerService.getConfigValues();

		urlTf = PrintViewUtil.createJTextField();
		urlTf.setText(values.get("client.url"));
		Box urlBox = PrintViewUtil.createSingleBox("服务器地址:", labelDim, this.urlTf);

		portTf = PrintViewUtil.createJTextField();
		portTf.setText(values.get("client.port"));
		Box portBox = PrintViewUtil.createSingleBox("服务器端口:", labelDim, this.portTf);

		contextTf = PrintViewUtil.createJTextField();
		contextTf.setText(values.get("client.context"));
		Box contextBox = PrintViewUtil.createSingleBox("服务器上下文根:", labelDim, this.contextTf);
		// Box[] boies = new Box[] { urlBox, portBox, contextBox };
		List<Box> boies = new ArrayList<Box>(10);
		boies.add(urlBox);
		boies.add(portBox);
		boies.add(contextBox);
		if (!login) {
			pdfFileTf = PrintViewUtil.createJTextField();
			pdfFileTf.setText("d:/pdf_tpl_files/");
			Box pdfFileBox = PrintViewUtil.createSingleBox("PDF模板路径:", labelDim, pdfFileTf);
			pdfFileTf.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (2 == e.getClickCount()) {
						String pdfTpl = PrintViewUtil.createJFileChooser(ServerInfoView.this);
						if (StringUtils.isNotBlank(pdfTpl)) {
							pdfFileTf.setText(pdfTpl);
						}
					}
				}
			});
			boies.add(pdfFileBox);
		}

		// Box[] boies = new Box[] { urlBox, portBox, contextBox, pdfFileBox };
		JButton[] btns = this.createBtns();

		JPanel btnPanel = PrintViewUtil.createButtonBoxPanel(btns);
		JPanel boxPanel = PrintViewUtil.createSingleBoxPanel(5, boies, btnPanel);
		return boxPanel;
	}

	protected void disableContent() {
		Border border = new LineBorder(Color.GRAY, 1);

		this.urlTf.setEditable(false);
		this.portTf.setEditable(false);
		this.contextTf.setEditable(false);

		this.urlTf.setBorder(border);
		this.portTf.setBorder(border);
		this.contextTf.setBorder(border);

		this.okBtn.setEnabled(false);
		this.cancelBtn.setEnabled(false);
	}

	private JButton[] createBtns() {
		okBtn = new JButton("确定");
		okBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ServerInfoView.this.updateHttpServerInfo();
			}
		});
		cancelBtn = new JButton("取消");
		cancelBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (null != ServerInfoView.this.parent) {
					ServerInfoView.this.parent.showLoginPanel();
				}
			}
		});
		JButton[] btns = { okBtn, cancelBtn };
		return btns;
	}

	private void updateHttpServerInfo() {
		String url = this.urlTf.getText();
		if (StringUtils.isBlank(url)) {
			PrintViewUtil.showErrorMsg("服务器地址不能为空");
			this.urlTf.requestFocus();
			return;
		}
		String port = this.portTf.getText();
		if (StringUtils.isBlank(port)) {
			PrintViewUtil.showErrorMsg("服务器端口不能为空");
			this.portTf.requestFocus();
			return;
		}
		String context = this.contextTf.getText();
		if (StringUtils.isBlank(context)) {
			PrintViewUtil.showErrorMsg("服务器上下文根不能为空");
			this.contextTf.requestFocus();
			return;
		}
		String pdfTpl = "";
		if (null != this.pdfFileTf) {
			pdfTpl = this.pdfFileTf.getText();
			if (StringUtils.isBlank(pdfTpl)) {
				PrintViewUtil.showErrorMsg("PDF模板路径不能为空");
				this.pdfFileTf.requestFocus();
				return;
			}
		}
		try {
			this.printerService.updateHttpServerInfo(url, port, context, pdfTpl);
			PrintViewUtil.showInformationMsg("已修改服务器配置信息");
			if (null != parent) {
				parent.showLoginPanel();
			}
		} catch (Exception e) {
			logger.info("修改服务器配置信息出现异常,异常详细信息是:{}", e.getMessage());
			PrintViewUtil.showErrorMsg("修改服务器配置信息出现异常");
		}
	}
}
