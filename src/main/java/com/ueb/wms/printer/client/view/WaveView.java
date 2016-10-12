package com.ueb.wms.printer.client.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JPanel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ueb.wms.printer.client.vo.ReportDataVO;

@SuppressWarnings("serial")
@Component("waveView")
public class WaveView extends JPanel implements IBaseView {

	@Autowired
	private OrderTableView pkView;

	@Autowired
	private TopView topView;

	private void initContainer() {
		this.setLayout(new BorderLayout(5, 0));
	}

	private void showResource() {
		this.add(this.topView, BorderLayout.NORTH);
		this.topView.displayUI();

		this.add(this.pkView, BorderLayout.CENTER);
		this.pkView.displayUI();
	}

	public void refreshTableData(List<ReportDataVO> dataList) {
		this.pkView.refreshData(dataList);
	}

	public void measureSize(Dimension parentSize) {
		Dimension topSize = this.topView.getSize();
		int width = parentSize.width - topSize.width;
		int height = parentSize.height - topSize.height;

		this.pkView.measureSize(new Dimension(width, height));
	}

	public void activationReportView(String orderNO) {
		((TabView) this.getParent()).activationReportView(orderNO);
	}

	public void fastPrintIReport(ReportDataVO reportDataVo) throws Exception {
		((TabView) this.getParent()).fastPrintIReport(reportDataVo);
	}

	@Override
	public void displayUI() {
		this.initContainer();
		this.showResource();
	}
}
