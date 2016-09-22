package com.ueb.wms.printer.client.view;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JTabbedPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ueb.wms.printer.client.vo.ReportDataVO;

@SuppressWarnings("serial")
@Component("tabView")
public class TabView extends JTabbedPane implements IBaseView {

	@Autowired
	private PrintView printView;

	@Autowired
	private WaveView waveView;

	@Autowired
	private HomePageView homePageView;

	public TabView() {
		this(TOP, WRAP_TAB_LAYOUT);
	}

	public TabView(int tabPlacement) {
		this(tabPlacement, WRAP_TAB_LAYOUT);
	}

	public TabView(int tabPlacement, int tabLayoutPolicy) {
		super(tabPlacement, tabLayoutPolicy);
	}

	private void initContainer() {
		this.setFont(new Font("微软雅黑", Font.PLAIN, 12));
	}

	private void showResource() {
		this.addTab("首页", this.homePageView);
		this.homePageView.displayUI();

		this.addTab("打印", this.printView);
		this.printView.displayUI();

		this.addTab("波次", this.waveView);
		this.waveView.displayUI();
	}

	@Override
	public void displayUI() {
		this.initContainer();
		this.showResource();
	}

	public void setDefaultFocus() {
		this.printView.setDefaultFocus();
	}

	public void clear() {
		this.printView.clear();
		this.removeAll();
	}

	public void measureSize(Dimension parentSize) {
		waveView.measureSize(parentSize);
	}

	public void activationReportView(String orderNO) {
		this.setSelectedComponent(this.printView);
		this.printView.activation(orderNO);
	}

	public void fastPrintIReport(ReportDataVO reportDataVo) {
		// this.setSelectedComponent(this.printView);
		this.printView.fastPrintReport(reportDataVo);
	}

}
