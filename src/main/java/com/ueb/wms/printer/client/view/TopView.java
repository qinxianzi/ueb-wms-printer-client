package com.ueb.wms.printer.client.view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.atomikos.swing.SwingWorker;
import com.ueb.wms.printer.client.pdf.PrintFile;
import com.ueb.wms.printer.client.service.IPrinterService;
import com.ueb.wms.printer.client.util.PrintViewUtil;
import com.ueb.wms.printer.client.util.UebDateTimeUtil;
import com.ueb.wms.printer.client.vo.ReportDataVO;

@SuppressWarnings("serial")
@Component("topView")
public class TopView extends JPanel implements IBaseView {

	@Autowired
	private IPrinterService printerService;

	private final Font font = new Font("微软雅黑", Font.PLAIN, 12);
	private WaveView parent;
	private JTextField tfWaveNum;
	private JButton btnQuery;

	private void initContainer() {
		this.parent = (WaveView) this.getParent();
	}

	private void showResource() {
		JLabel label = new JLabel("波次编号:");
		label.setFont(font);
		tfWaveNum = new JTextField();
		tfWaveNum.setPreferredSize(new Dimension(170, 24));
		tfWaveNum.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (KeyEvent.VK_ENTER == e.getKeyCode()) {
					// String waveNO = tfWaveNum.getText();
					// if (StringUtils.isBlank(waveNO)) {
					// tfWaveNum.requestFocus();
					// return;
					// }
					TopView.this.doSearch();
				}
			}
		});

		btnQuery = new JButton("查询");
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
			PrintViewUtil.showWarningMsg("请输入波次编号");
			this.tfWaveNum.requestFocus();
			return;
		}
		btnQuery.setEnabled(false);
		SwingWorker worker = new SwingWorker() {
			private boolean error = false;

			@Override
			public Object construct() {
				List<ReportDataVO> reportDataVos = null;
				try {
					reportDataVos = printerService.findByWaveNO(waveNO);
				} catch (Exception e) {
					this.error = true;
				}
				return reportDataVos;
			}

			@SuppressWarnings("unchecked")
			@Override
			public void finished() {
				if (this.error) {
					TopView.this.clearTableData();
					PrintViewUtil.showErrorMsg("根据波次号查询面单出现异常");
					tfWaveNum.selectAll();
					tfWaveNum.requestFocus();
					btnQuery.setEnabled(true);
					return;
				}
				List<ReportDataVO> reportDataVos = (List<ReportDataVO>) this.getValue();
				if (null == reportDataVos || reportDataVos.isEmpty()) {
					TopView.this.clearTableData();
					PrintViewUtil.showInformationMsg(String.format("根据\"波次编号%s\"没有查询到任何面单", condition));
					tfWaveNum.selectAll();
					tfWaveNum.requestFocus();
					btnQuery.setEnabled(true);
					return;
				}
				TopView.this.callBack(reportDataVos);
				// TopView.this.parent.refreshTableData(reportDataVos);
				// TopView.this.printReport(reportDataVos);
				btnQuery.setEnabled(true);
			}
		};
		worker.start();
	}

	private void callBack(List<ReportDataVO> reportDataVos) {
		new Thread() {
			public void run() {
				TopView.this.parent.refreshTableData(reportDataVos);
			}
		}.start();
		new Thread() {
			public void run() {
				TopView.this.printReport(reportDataVos);
			}
		}.start();
	}

	private void printReport(List<ReportDataVO> dataList) {
		if (null == dataList || dataList.isEmpty()) {
			return;
		}
		for (Iterator<ReportDataVO> it = dataList.iterator(); it.hasNext();) {
			this.fastPrintReport(it.next());
		}
	}

	private void clearTableData() {
		this.parent.refreshTableData(new ArrayList<ReportDataVO>());
	}

	private void fastPrintReport(ReportDataVO reportDataVo) {
		try {
			int tplType = reportDataVo.getTplType(); // 使用的模板类型
			switch (tplType) {
			case 0: // PDF模板
				this.fastPrintPdf(reportDataVo);
				break;
			case 1: // iReport模板
				TopView.this.parent.fastPrintIReport(reportDataVo);
				break;
			default:
				throw new Exception("使用的模板类型不存在");
			}
		} catch (Exception e) {
			logger.info("打印多品面单出现了异常，，波次编号是：{}，订单编号是：{}，异常详细信息是：{}", reportDataVo.getCOLUMNNAME18(),
					reportDataVo.getCOLUMNNAME1(), e.getMessage());
			PrintViewUtil.showErrorMsg("快速打印快递面单出现了异常");
		}
	}

	private String getPdfPrefix() {
		Map<String, String> values = printerService.getConfigValues();
		String pdfTpl = values.get("client.pdfTpl");
		File pdfTplDir = new File(pdfTpl);
		if (!pdfTplDir.exists()) {
			pdfTplDir.mkdirs();
		}
		return pdfTplDir.getAbsolutePath();
	}

	// private boolean beforePrint(String orderNO) {
	// try {
	// return printerService.beforePrint(orderNO);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return false;
	// }
	//
	// private void afterPrint(String orderNO, boolean isPrintSuccess) {
	// try {
	// printerService.afterPrint(orderNO, isPrintSuccess);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	private void fastPrintPdf(ReportDataVO reportDataVo) throws Exception {
		String orderNo = StringUtils.trim(reportDataVo.getCOLUMNNAME1()); // 订单编号
		// if (!this.beforePrint(orderNo)) {
		// return;
		// }
		// boolean isPrintSuccess = true;
		try {
			// 本地处理好的待打印的pdf文件
			StringBuffer sb = new StringBuffer(this.getPdfPrefix());
			sb.append(UebDateTimeUtil.getNowDateStr("")).append("/").append(orderNo).append(".pdf");
			String tmppdf = sb.toString();

			List<String> contents = new ArrayList<String>(10);
			contents.add(reportDataVo.getCOLUMNNAME18());
			contents.add(reportDataVo.getCOLUMNNAME4());
			contents.add("CW:" + reportDataVo.getCOLUMNNAME39());
			contents.add(reportDataVo.getCOLUMNNAME3());
			printerService.downloadPdf(orderNo, contents, tmppdf);
			PrintFile.pluginPrintPdf(tmppdf);
		} catch (Exception e) {
			// isPrintSuccess = false;
			throw e;
		}
		// finally {
		// this.afterPrint(orderNo, isPrintSuccess);
		// }
	}

	@Override
	public void displayUI() {
		this.initContainer();
		this.showResource();
	}
}
