package com.ueb.wms.printer.client.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ueb.wms.printer.client.pdf.PrintFile;
import com.ueb.wms.printer.client.service.IPrinterService;
import com.ueb.wms.printer.client.service.ISingleSkuMatchService;
import com.ueb.wms.printer.client.util.PrintViewUtil;
import com.ueb.wms.printer.client.util.UebDateTimeUtil;
import com.ueb.wms.printer.client.vo.ProductReviewVO;
import com.ueb.wms.printer.client.vo.ReportDataVO;
import com.ueb.wms.printer.client.vo.SingleSkuMatchResVO;

/**
 * 单品复核UI
 *
 */
@SuppressWarnings("serial")
@Component("singleProductView")
public class SingleProductView extends JPanel implements IBaseView {

	@Autowired
	private IPrinterService printerService;

	@Autowired
	private ISingleSkuMatchService singleSkuMatchService;

	private JTextField waveTf, productTf, toidTf, skuCnNameTf, skuEnNameTf;

	private TabView parent;

	protected void initContainer() {
		this.setLayout(new BorderLayout(0, 5));
		this.parent = (TabView) this.getParent();
	}

	private void selectProduct() {
		this.productTf.requestFocus();
		this.productTf.selectAll();
	}

	private void setCheckQty() {
		String waveNO = StringUtils.trim(this.waveTf.getText());
		if (StringUtils.isBlank(waveNO)) {
			PrintViewUtil.showErrorMsg("波次编号不能为空");
			return;
		}
		try {
			ProductReviewVO reviewVo = singleSkuMatchService.findCheckQty(waveNO);
			this.totalQtyTf.setText(String.valueOf(reviewVo.getChecktotalqty()));
			this.checkPackedQtyTf.setText(String.valueOf(reviewVo.getCheckpackedqty()));
			this.noCheckPackedQtyTf.setText(String.valueOf(reviewVo.getChecknopackedqty()));
			this.selectProduct();
		} catch (Exception e) {
			this.waveTf.selectAll();
			logger.info("根据波次编号查询复核进度出现异常，波次编号是：{}，异常信息是：{}", waveNO, e.getMessage());
			PrintViewUtil.showErrorMsg("根据波次编号查询复核进度出现异常");
			this.waveTf.requestFocus();
		}
	}

	/**
	 * 单品复核
	 */
	private void singleSkuMatch() {
		String waveNO = StringUtils.trim(this.waveTf.getText());
		if (StringUtils.isBlank(waveNO)) {
			PrintViewUtil.showErrorMsg("波次编号不能为空");
			return;
		}
		String sku = StringUtils.trim(this.productTf.getText());
		if (StringUtils.isBlank(sku)) {
			PrintViewUtil.showErrorMsg("产品代码不能为空");
			return;
		}
		try {
			SingleSkuMatchResVO resVo = singleSkuMatchService.singleSkuMatch(waveNO, sku);
			String code = resVo.getCode();

			skuCnNameTf.setText(resVo.getDescr_c());
			skuEnNameTf.setText(resVo.getDescr_e());
			this.selectProduct();
			if (StringUtils.isNotBlank(code) && StringUtils.equals("1001", code)) {
				// skuCnNameTf.setText(resVo.getDescr_c());
				// skuEnNameTf.setText(resVo.getDescr_e());
				// this.selectProduct();
				PrintViewUtil.showWarningMsg(resVo.getMessage());
				return;
			}
			if (StringUtils.isNotBlank(code) && StringUtils.equals("1002", code)) {
				PrintViewUtil.showWarningMsg(resVo.getMessage());
				// this.productTf.selectAll();
				this.waveTf.requestFocus();
				this.waveTf.selectAll();
				return;
			}
			// skuCnNameTf.setText(resVo.getDescr_c());
			// skuEnNameTf.setText(resVo.getDescr_e());
			// this.selectProduct();

			ReportDataVO reportVo = resVo.getReportVo();
			// this.printReport(reportVo);
			this.printSfReport(reportVo);

			toidTf.setText(resVo.getToid());
			this.setOtherInfo(resVo);
			this.setCheckQty();
		} catch (Exception e) {
			logger.info("单品复核出现异常，波次编号是：{}，产品代码是：{}，异常信息是：{}", waveNO, sku, e.getMessage());
			PrintViewUtil.showErrorMsg("单品复核出现异常");
			this.selectProduct();
		}
	}

	private void printSfReport(ReportDataVO reportVo) {
		if (null == reportVo) {
			return;
		}
		SingleProductView that = this;
		new Thread() {
			public void run() {
				// TODO:先注销
				// that.printReport(reportVo);
			}
		}.start();
	}

	private void setOtherInfo(SingleSkuMatchResVO resVo) {
		packingCountTf.setText(String.valueOf(resVo.getPackingQty()));
		waveSkuCountTf.setText(String.valueOf(resVo.getWaveSkuCount()));

		String tmpCount1 = cumulativeScanTf.getText();
		tmpCount1 = StringUtils.isBlank(tmpCount1) ? "0" : tmpCount1;
		cumulativeScanTf.setText(String.valueOf(Float.valueOf(tmpCount1) + 1));

		String tmpCount2 = thisScanTf.getText();
		tmpCount2 = StringUtils.isBlank(tmpCount2) ? "0" : tmpCount2;
		thisScanTf.setText(String.valueOf(Float.valueOf(tmpCount2) + 1));
	}

	private void printReport(ReportDataVO reportVo) {
		try {
			this.fastPrintReport(reportVo);
		} catch (Exception e) {
			logger.info("打印快递面单出现了异常，，波次编号是：{}，产品代码是：{}，订单编号是：{}，异常详细信息是：{}", reportVo.getCOLUMNNAME18(),
					reportVo.getCOLUMNNAME19(), reportVo.getCOLUMNNAME1(), e.getMessage());
			PrintViewUtil.showErrorMsg("快速打印快递面单出现了异常");
		}
	}

	private void fastPrintReport(ReportDataVO reportDataVo) throws Exception {
		int tplType = reportDataVo.getTplType(); // 使用的模板类型
		switch (tplType) {
		case 0: // PDF模板
			this.fastPrintPdf(reportDataVo);
			break;
		case 1: // iReport模板
			this.parent.fastPrintIReport(reportDataVo);
			break;
		default:
			throw new Exception("使用的模板类型不存在");
		}
	}

	private void fastPrintPdf(ReportDataVO reportDataVo) throws Exception {
		String orderNo = StringUtils.trim(reportDataVo.getCOLUMNNAME1()); // 订单编号
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
			throw e;
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

	protected void showResource() {
		Box vbox = Box.createVerticalBox();
		vbox.add(this.createProductPanel());
		vbox.add(Box.createVerticalStrut(15));
		vbox.add(this.createProgressPanel());

		this.add(vbox, BorderLayout.NORTH);
		this.add(new JPanel(), BorderLayout.CENTER);
	}

	private void setIDSequence() {
		try {
			String idSeq = singleSkuMatchService.getIDSequence("");
			this.toidTf.setText(StringUtils.trim(idSeq));
		} catch (Exception e) {
			logger.info("获取装箱箱号出现异常，异常信息是：{}", e);
			PrintViewUtil.showErrorMsg("获取装箱箱号出现异常");
		}
	}

	protected JPanel createProductPanel() {
		Dimension labelDim = new Dimension(100, 0);

		JTextField customerTf = PrintViewUtil.createJTextField();
		customerTf.setText("UEB");
		Box customerBox = PrintViewUtil.createSingleBox("货主", labelDim, customerTf);

		waveTf = PrintViewUtil.createJTextField();
		waveTf.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (KeyEvent.VK_ENTER == e.getKeyCode()) {
					SingleProductView.this.setCheckQty();
				}
			}
		});
		Box waveBox = PrintViewUtil.createSingleBox("波次编号", labelDim, waveTf);

		productTf = PrintViewUtil.createJTextField();
		productTf.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (KeyEvent.VK_ENTER == e.getKeyCode()) {
					SingleProductView.this.singleSkuMatch();
				}
			}
		});
		Box productBox = PrintViewUtil.createSingleBox("产品代码", labelDim, productTf);

		JButton numberBtn = new JButton("新增箱号");
		numberBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SingleProductView.this.setIDSequence();
			}
		});
		toidTf = PrintViewUtil.createJTextField();
		Box numberBox = PrintViewUtil.createSingleBox("箱号", labelDim, toidTf);
		numberBox.remove(numberBox.getComponentCount() - 1);
		numberBox.add(Box.createHorizontalStrut(10));
		numberBox.add(numberBtn);
		numberBox.add(Box.createHorizontalStrut(5));
		JButton okBtn = new JButton("确定");
		okBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				singleSkuMatch();
			}
		});
		numberBox.add(okBtn);
		numberBox.add(Box.createHorizontalStrut(30));

		skuCnNameTf = PrintViewUtil.createJTextField();
		skuCnNameTf.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		Box skuCnNameBox = PrintViewUtil.createSingleBox("中文品名", labelDim, skuCnNameTf);

		skuEnNameTf = PrintViewUtil.createJTextField();
		Box skuEnNameBox = PrintViewUtil.createSingleBox("英文品名", labelDim, skuEnNameTf);

		Box[] boies = new Box[] { customerBox, waveBox, productBox, numberBox, skuCnNameBox, skuEnNameBox };

		JPanel boxPanel = PrintViewUtil.createSingleBoxPanel(5, boies, null);
		boxPanel.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.GRAY, 1), ""));
		return boxPanel;
	}

	private JTextField packingCountTf, waveSkuCountTf, cumulativeScanTf, thisScanTf;

	private JPanel createProgressPanel() {
		Dimension labelDim = new Dimension(100, 0);
		waveSkuCountTf = PrintViewUtil.createJTextField();
		waveSkuCountTf.setEditable(false);
		Box waveSkuCountBox = PrintViewUtil.createSingleBox("波次SKU总数", labelDim, waveSkuCountTf);

		packingCountTf = PrintViewUtil.createJTextField();
		packingCountTf.setEditable(false);
		Box packingCountBox = PrintViewUtil.createSingleBox("装箱量", labelDim, packingCountTf);

		cumulativeScanTf = PrintViewUtil.createJTextField();
		cumulativeScanTf.setEditable(false);
		Box cumulativeScanBox = PrintViewUtil.createSingleBox("累计扫描", labelDim, cumulativeScanTf);

		thisScanTf = PrintViewUtil.createJTextField();
		thisScanTf.setEditable(false);
		Box thisScanBox = PrintViewUtil.createSingleBox("本次扫描", labelDim, thisScanTf);

		JTextField tf01 = PrintViewUtil.createJTextField();
		tf01.setBackground(Color.YELLOW);
		JTextField tf02 = PrintViewUtil.createJTextField();
		tf02.setBackground(Color.GREEN);
		JTextField tf03 = PrintViewUtil.createJTextField();
		tf03.setBackground(new Color(255, 73, 3));
		// tf03.setFont(new Font("微软雅黑", Font.BOLD, 12));

		Box progressBox = PrintViewUtil.createSingleBox("复核进度", labelDim, createProgress());
		progressBox.remove(2);

		Box[] boies = new Box[] { waveSkuCountBox, packingCountBox, cumulativeScanBox, thisScanBox, progressBox };
		JPanel progressPanel = PrintViewUtil.createSingleBoxPanel(5, boies, null);
		progressPanel.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.GRAY, 1), ""));
		return progressPanel;
	}

	private JTextField totalQtyTf, checkPackedQtyTf, noCheckPackedQtyTf;

	/**
	 * 复核进度
	 * 
	 * @return
	 */
	private JPanel createProgress() {
		FlowLayout layout = new FlowLayout(FlowLayout.LEFT, 12, 5);
		JPanel panel = new JPanel(layout);
		panel.setPreferredSize(new Dimension(480, 36));

		totalQtyTf = new JTextField();
		totalQtyTf.setBackground(Color.YELLOW);
		totalQtyTf.setPreferredSize(new Dimension(150, 23));
		panel.add(totalQtyTf);

		checkPackedQtyTf = new JTextField();
		checkPackedQtyTf.setBackground(new Color(153, 180, 209));
		checkPackedQtyTf.setPreferredSize(new Dimension(150, 23));
		panel.add(checkPackedQtyTf);

		noCheckPackedQtyTf = new JTextField();
		noCheckPackedQtyTf.setBackground(Color.RED);
		noCheckPackedQtyTf.setPreferredSize(new Dimension(150, 23));
		panel.add(noCheckPackedQtyTf);
		return panel;
	}

	@Override
	public void displayUI() {
		this.initContainer();
		this.showResource();
	}
}
