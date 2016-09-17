package com.ueb.wms.printer.client.view;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

import javax.swing.JScrollPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ueb.wms.printer.client.vo.ReportDataVO;

@SuppressWarnings("serial")
@Component("pkView")
public class OrderTableView extends JScrollPane implements IBaseView {

	@Autowired
	@Qualifier("jTableOrder")
	private JTableOrder table;

	private void initContainer() {
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				table.resizeTable(OrderTableView.this.getSize());
			}
		});
		this.getHorizontalScrollBar().setUnitIncrement(5);
		this.getVerticalScrollBar().setUnitIncrement(5);
	}

	private void showResource() {
		this.table.displayUI();
		this.setViewportView(this.table);
	}

	public void refreshData(List<ReportDataVO> dataList) {
		this.table.refreshData(dataList);
	}

	public void measureSize(Dimension parentSize) {
		this.setSize(parentSize);
	}

	public void activationReportView(String orderNO) {
		((WaveView) this.getParent()).activationReportView(orderNO);
	}

	@Override
	public void displayUI() {
		this.initContainer();
		this.showResource();
	}
}
