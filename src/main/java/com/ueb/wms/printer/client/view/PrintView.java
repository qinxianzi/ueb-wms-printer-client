package com.ueb.wms.printer.client.view;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ueb.wms.printer.client.constants.WmsConstants;
import com.ueb.wms.printer.client.service.IPrinterService;
import com.ueb.wms.printer.client.util.PrintUtil;
import com.ueb.wms.printer.client.util.PrintViewUtil;
import com.ueb.wms.printer.client.vo.ReportDataVO;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JRViewer;

@SuppressWarnings("serial")
@Component("printView")
public class PrintView extends JRViewer implements IBaseView {

	@Autowired
	private IPrinterService printerService;

	@Autowired
	private JTextFieldSearch tfSearch;

	protected JasperPrint jasperPrint;

	public PrintView() {
		super(null, null);
	}

	public void loadReport(JasperPrint jasperPrint) {
		this.jasperPrint = jasperPrint;
		super.loadReport(jasperPrint);
		this.refreshPage();

		// boolean fastPrinting = true;
		// if (fastPrinting) {
		// this.fastPrintReport();
		// }
	}

	// public void loadReport(InputStream input, boolean isXmlReport) {
	// try {
	// super.loadReport(input, isXmlReport);
	// this.refreshPage();
	// } catch (JRException e) {
	// logger.error("预览文件出现异常，详细信息是：" + e.getMessage());
	// e.printStackTrace();
	// }
	// }
	//
	// public void loadReport(String filename, boolean isXmlReport) {
	// try {
	// super.loadReport(filename, isXmlReport);
	// this.refreshPage();
	// } catch (JRException e) {
	// logger.error("预览文件出现异常，详细信息是：" + e.getMessage());
	// e.printStackTrace();
	// }
	// }

	private void initContainer() {

	}

	private void showResource() {
		tfSearch.setPreferredSize(new Dimension(170, 24));
		tfSearch.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (KeyEvent.VK_ENTER == e.getKeyCode()) {
					PrintView.this.doSearch();
				}
			}
		});
		tfSearch.setToolTipText("请输入\"订单编号、SO编号、物流单号\"进行查询");

		JLabel label = new JLabel("");
		label.setPreferredSize(new Dimension(8, 24));

		this.tlbToolBar.remove(this.btnSave);
		this.tlbToolBar.add(label);
		this.tlbToolBar.add(tfSearch);

		// 重写打印事件
		ActionListener[] listeners = this.btnPrint.getActionListeners();
		if (null != listeners && listeners.length > 0) {
			for (ActionListener listener : listeners) {
				this.btnPrint.removeActionListener(listener);
			}
		}
		this.btnPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				PrintView.this.printReport();
			}
		});
	}

	/**
	 * 普通打印
	 */
	protected void printReport() {
		if (!beforePrint()) {
			return;
		}
		PrintView that = this;
		Thread thread = new Thread(new Runnable() {
			public void run() {
				try {
					btnPrint.setEnabled(false);
					that.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					JasperPrintManager.getInstance(jasperReportsContext).print(that.jasperPrint, true); // 弹出打印窗口
					that.afterPrint();
				} catch (Exception e) {
					logger.error("Print error.", e);
					JOptionPane.showMessageDialog(that, that.getBundleString("error.printing"));
				} finally {
					that.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					btnPrint.setEnabled(true);
				}
			}
		});
		thread.start();
	}

	/**
	 * 快速打印
	 */
	protected void fastPrintReport() {
		this.btnPrint.setEnabled(false);
		if (!this.beforePrint()) {
			return;
		}
		PrintView that = this;
		Thread thread = new Thread(new Runnable() {
			public void run() {
				try {
					JasperPrintManager.getInstance(jasperReportsContext).print(that.jasperPrint, false); // 快速打印
					that.afterPrint();
				} catch (Exception e) {
					logger.error("Print error.", e);
					JOptionPane.showMessageDialog(that, that.getBundleString("error.printing"));
				}
			}
		});
		thread.start();
	}

	public void fastPrintReport(ReportDataVO reportDataVo) {
		if (null == reportDataVo) {
			return;
		}
		JasperPrint jasperPrint = null;
		try {
			jasperPrint = this.loadReportTemplate(reportDataVo);
		} catch (Exception e) {
			logger.error("加载iReport面单模板出现异常：{}", e.getMessage());
			PrintViewUtil.showErrorMsg("加载iReport面单模板出现异常");
		}
		this.fastPrintReport(jasperPrint);
	}

	protected void fastPrintReport(JasperPrint jasperPrint) {
		if (null == jasperPrint) {
			return;
		}
		this.jasperPrint = jasperPrint;
		this.btnPrint.setEnabled(false);
		if (!this.beforePrint()) {
			return;
		}
		try {
			// this.loadReport(jasperPrint);
			JasperPrintManager.getInstance(jasperReportsContext).print(jasperPrint, false); // 快速打印
			this.afterPrint();
		} catch (Exception e) {
			logger.error("打印iReport面单出现异常：{}", e.getMessage());
			PrintViewUtil.showErrorMsg(this.getBundleString("error.printing"));
		}
	}

	private boolean beforePrint() {
		// TODO:读取该面单打印日志（即打印次数），若已经打印，则不能重复打印
		return true;
	}

	private void afterPrint() {
		// TODO:新增面单打印日志
	}

	@Override
	public void displayUI() {
		this.initContainer();
		this.showResource();
	}

	public void setDefaultFocus() {
		this.tfSearch.requestFocus();
	}

	public void activation(String orderNO) {
		this.tfSearch.setText(orderNO);
		this.doSearch();
	}

	protected void doSearch() {
		String search = this.tfSearch.getText();
		String condition = StringUtils.trim(search);
		if (StringUtils.isBlank(condition)) {
			JOptionPane.showMessageDialog(null, "请输入查询条件", "系统提示", JOptionPane.WARNING_MESSAGE);
			this.setDefaultFocus();
			return;
		}
		this.doSearch(search);
	}

	protected void doSearch(String search) {
		try {
			ReportDataVO reportDataVo = printerService.findReportData(search);
			this.showReportView(reportDataVo);
		} catch (Exception e) {
			this.loadReport(null);
			logger.info("预览快递面单出现了异常，查询条件是：{}，异常详细信息是：{}", search, e.getMessage());
			JOptionPane.showMessageDialog(null, "预览面单出现了异常", "系统提示", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void showReportView(ReportDataVO reportDataVo) throws Exception {
		int tplType = reportDataVo.getTplType();
		JasperPrint jasperPrint = null;
		switch (tplType) {
		case 0: // PDF模板
			jasperPrint = this.loadPdfTemplate(reportDataVo);
			break;
		case 1: // iReport模板
			jasperPrint = this.loadReportTemplate(reportDataVo);
			break;
		default:
			throw new Exception("使用的模板类型不存在");
		}
		this.loadReport(jasperPrint);
	}

	/**
	 * 加载PDF模板
	 * 
	 * @param reportDataVo
	 * @return
	 * @throws Exception
	 */
	private JasperPrint loadPdfTemplate(ReportDataVO reportDataVo) throws Exception {
		String orderNO = reportDataVo.getCOLUMNNAME1();
		BufferedImage pdfimage = printerService.downloadPdfTemplate(orderNO);
		reportDataVo.setCOLUMNNAME101(pdfimage);

		FileInputStream inputStream = null;
		try {
			// Resource resource = new
			// ClassPathResource("/report/ueb_wms_pdf_report_tpl.jasper");
			// // String reportFile =
			// //
			// this.getClass().getResource("/report/ueb_wms_pdf_report_tpl.jasper").getFile();
			// // File file = new File(reportFile);
			// // JasperReport jasperReport = (JasperReport)
			// // JRLoader.loadObject(file);
			// JasperReport jasperReport = (JasperReport)
			// JRLoader.loadObject(resource.getInputStream());

			inputStream = loadTpInputStream("resources/report/ueb_wms_pdf_report_tpl.jasper");
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(inputStream);

			List<ReportDataVO> datas = new ArrayList<ReportDataVO>(1);
			datas.add(reportDataVo);
			JRBeanCollectionDataSource jrrsds = new JRBeanCollectionDataSource(datas);

			Map<String, Object> parameters = PrintUtil.getImageParams();
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jrrsds);
			return jasperPrint;
		} finally {
			this.closeReportTplInput(inputStream);
		}
	}

	/**
	 * 加载iReport模板
	 * 
	 * @param reportDataVo
	 * @return
	 * @throws Exception
	 */
	private JasperPrint loadReportTemplate(ReportDataVO reportDataVo) throws Exception {
		String carrierID = reportDataVo.getCOLUMNNAME6(); // 承运人id
		// File reportFile = this.getReportTemplate(carrierID);
		// JasperReport jasperReport = (JasperReport)
		// JRLoader.loadObject(reportFile);
		FileInputStream inputStream = null;
		try {
			// Resource resource = new
			// ClassPathResource(this.getReportTemplate(carrierID));
			// JasperReport jasperReport = (JasperReport)
			// JRLoader.loadObject(resource.getInputStream());

			inputStream = getReportTplInput(carrierID);
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(inputStream);

			List<ReportDataVO> datas = new ArrayList<ReportDataVO>(1);
			datas.add(reportDataVo);
			JRBeanCollectionDataSource jrrsds = new JRBeanCollectionDataSource(datas);

			Map<String, Object> parameters = PrintUtil.getImageParams();
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jrrsds);
			return jasperPrint;
		} finally {
			closeReportTplInput(inputStream);
		}
	}

	// private File getReportTemplate(String carrierId) throws Exception {
	// String report = StringUtils.replace(WmsConstants.REPORT_TEMPLATE,
	// "{CarrierID}", carrierId);
	// URL url = this.getClass().getResource("/report/");
	// StringBuffer path = new StringBuffer(url.getPath());
	// path.append(report);
	//
	// File file = new File(path.toString());
	// if (!file.exists()) {
	// throw new Exception("SO快递面单模板文件不存在");
	// }
	// return file;
	// }
	// private String getReportTemplate(String carrierId) throws Exception {
	// String report = StringUtils.replace(WmsConstants.REPORT_TEMPLATE,
	// "{CarrierID}",
	// StringUtils.upperCase(carrierId));
	// // URL url = this.getClass().getResource("/report/");
	// // StringBuffer path = new StringBuffer(url.getPath());
	// // path.append(report);
	// //
	// // File file = new File(path.toString());
	// // if (!file.exists()) {
	// // throw new Exception("SO快递面单模板文件不存在");
	// // }
	// // return file;
	// StringBuffer path = new StringBuffer("/report/");
	// path.append(report);
	// return path.toString();
	// }

	private FileInputStream getReportTplInput(String carrierId) throws Exception {
		String report = StringUtils.replace(WmsConstants.REPORT_TEMPLATE, "{CarrierID}",
				StringUtils.upperCase(carrierId));
		StringBuffer path = new StringBuffer("resources/report/");
		path.append(report);
		logger.info("===========report=================={}", path.toString());

		// FileInputStream inputStream = new FileInputStream(report);
		return this.loadTpInputStream(path.toString());
		// return inputStream;
	}

	private void closeReportTplInput(FileInputStream inputStream) throws Exception {
		if (null != inputStream) {
			inputStream.close();
		}
	}

	private FileInputStream loadTpInputStream(String filename) throws Exception {
		FileInputStream inputStream = new FileInputStream(filename);
		return inputStream;
	}

	// private void loadPdf2Report() throws Exception {
	// String pdffile =
	// this.getClass().getResource("/pdf/PK160912024098.pdf").getFile();
	// BufferedImage image = this.tranferPdf2Image(pdffile);
	//
	// String reportFile =
	// this.getClass().getResource("/report/my_pdf_report_test.jasper").getFile();
	// File file = new File(reportFile);
	// JasperReport jasperReport = (JasperReport) JRLoader.loadObject(file);
	//
	// ReportDataVO reportDataVo = new ReportDataVO();
	// reportDataVo.setCOLUMNNAME101(image);
	// List<ReportDataVO> datas = new ArrayList<ReportDataVO>(1);
	// datas.add(reportDataVo);
	// JRBeanCollectionDataSource jrrsds = new
	// JRBeanCollectionDataSource(datas);
	//
	// Map<String, Object> parameters = PrintUtil.getImageParams();
	// JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
	// parameters, jrrsds);
	// this.loadReport(jasperPrint);
	// }
	//
	// public BufferedImage tranferPdf2Image(String pdffile) throws Exception {
	// ImageType imageType = ImageType.RGB;
	// PDDocument document = PDDocument.load(new File(pdffile));PDDocument.load
	//
	// PDFRenderer renderer = new PDFRenderer(document);
	// BufferedImage image = renderer.renderImageWithDPI(0, 150, imageType); //
	// 只取第1页
	// return image;
	// }
}
