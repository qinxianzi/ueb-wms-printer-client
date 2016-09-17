package com.ueb.wms.printer.client.view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ueb.wms.printer.client.service.IPrinterService;
import com.ueb.wms.printer.client.vo.ReportDataVO;

@SuppressWarnings("serial")
@Component("topView")
public class TopView extends JPanel implements IBaseView {

	@Autowired
	private IPrinterService printerService;

	private final Font font = new Font("微软雅黑", Font.PLAIN, 12);
	private JTextField tfWaveNum;
	private WaveView parent;

	private void initContainer() {
		// this.setBorder(new LineBorder(Color.GRAY, 1));
		this.parent = (WaveView) this.getParent();
	}

	private void showResource() {
		JLabel label = new JLabel("波次号:");
		label.setFont(font);
		tfWaveNum = new JTextField();
		tfWaveNum.setPreferredSize(new Dimension(170, 24));
		tfWaveNum.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (KeyEvent.VK_ENTER == e.getKeyCode()) {
					TopView.this.doSearch();
				}
			}
		});

		JButton btnQuery = new JButton("查询");
		btnQuery.setFont(font);
		btnQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TopView.this.doSearch();
			}
		});
		btnQuery.setPreferredSize(new Dimension(60, 24));

		this.add(label);
		this.add(tfWaveNum);
		this.add(btnQuery);
	}

	protected void doSearch() {
		String waveNO = this.tfWaveNum.getText();
		String condition = StringUtils.trim(waveNO);
		if (StringUtils.isBlank(condition)) {
			JOptionPane.showMessageDialog(null, "请输入波次号", "系统提示", JOptionPane.WARNING_MESSAGE);
			this.tfWaveNum.requestFocus();
			return;
		}
		this.doSearch(condition);
	}

	protected void doSearch(String waveNO) {
		try {
			List<ReportDataVO> reportDataVos = printerService.findByWaveNO(waveNO);
			this.parent.refreshTableData(reportDataVos);
		} catch (Exception e) {
			logger.info("按波次号包裹出现了异常，波次号是：{}，异常详细信息是：{}", waveNO, e.getMessage());
			JOptionPane.showMessageDialog(null, "按波次号包裹出现了异常", "系统提示", JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void displayUI() {
		this.initContainer();
		this.showResource();
	}
}
