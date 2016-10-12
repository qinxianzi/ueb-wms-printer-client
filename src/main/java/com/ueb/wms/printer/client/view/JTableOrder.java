package com.ueb.wms.printer.client.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.List;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import org.springframework.stereotype.Component;

import com.ueb.wms.printer.client.vo.ReportDataVO;

@SuppressWarnings("serial")
@Component("jTableOrder")
public class JTableOrder extends JTable implements IBaseView {
	// private String[] headers = { "订单编号", "SO编号", "来源平台", "物流编号", "物流单号",
	// "承运人", "收货人名称", "收货人地址", "国家外文", "国家中文", "城市",
	// "省份", "邮编", "收件联系人", "收件联系电话", "货主联系电话", "产品", "产品库位", "产品数量", "产品英文描述",
	// "订单重量kg", "订单金额", "平台订单号",
	// "卖家账号" };

	private String[] headers = { "订单编号", "SO编号", "承运人", "产品", "产品库位", "产品数量", "来源平台", "物流编号", "物流单号", "收货人名称", "收货人地址",
			"国家外文", "国家中文", "城市", "省份", "邮编", "收件联系人", "收件联系电话", "货主联系电话", /* "产品","产品库位", "产品数量",*/ "产品英文描述", "订单重量kg",
			"订单金额"/*, "平台订单号", "卖家账号"*/ };
	private DefaultTableModel defaultModel;

	private void initContainer() {
		this.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				// TODO:2016/9/22，使用连打功能
//				if (2 == e.getClickCount()) {
//					JTableOrder that = JTableOrder.this;
//					int row = that.getSelectedRow();
//					String orderNO = (String) that.getValueAt(row, 0);
//
//					OrderTableView parent = (OrderTableView) that.getParent().getParent();
//					parent.activationReportView(StringUtils.trim(orderNO));
//				}
			}
		});
		// this.addKeyListener(new KeyAdapter() {
		// public void keyPressed(KeyEvent e) {
		// if (KeyEvent.VK_ENTER == e.getKeyCode()) {
		// int selectedRow = JTableOrder.this.getSelectedRow();
		// String orderNO = (String) defaultModel.getValueAt(selectedRow, 0);
		// }
		// }
		// });

		this.setDragEnabled(false);
		// this.setRowSelectionAllowed(false);
		// this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		this.setShowVerticalLines(true);
		this.setShowHorizontalLines(true);
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 一次只选中一行
	}

	private void showResource() {
		this.defaultModel = new DefaultTableModel(null, this.headers) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		this.setModel(defaultModel);
		this.getSelectionModel().addListSelectionListener(new RowListener(this)); // 增加行选中事件

		JTableHeader tableHeader = this.getTableHeader();
		tableHeader.setBackground(new Color(0xffffff));
		tableHeader.setReorderingAllowed(false);// 禁止移动列的位置
		tableHeader.setFont(new Font("微软雅黑", Font.PLAIN, 12));
	}

	public void refreshData(List<ReportDataVO> dataList) {
		this.defaultModel.getDataVector().clear();
		for (Iterator<ReportDataVO> it = dataList.iterator(); it.hasNext();) {
			ReportDataVO reportDataVO = it.next();
			this.defaultModel.addRow(reportDataVO.getTableRowData());
		}

		// 默认选中第1行数据
		if (this.getRowCount() > 0) {
			this.setRowSelectionInterval(0, 0);
		}
		this.resizeTable(this.getParent().getSize());
	}

	public void resizeTable(Dimension parentSize) {
		int allwidth = this.getIntercellSpacing().width;
		int cols = this.getColumnCount(), rows = this.getRowCount();
		for (int j = 0; j < cols; j++) {
			int max = 0;
			for (int i = 0; i < rows; i++) {
				int width = this.getCellRenderer(i, j)
						.getTableCellRendererComponent(this, this.getValueAt(i, j), false, false, i, j)
						.getPreferredSize().width;
				max = width > max ? width : max;
			}
			int headerwidth = this.getTableHeader()
					.getDefaultRenderer().getTableCellRendererComponent(this,
							this.getColumnModel().getColumn(j).getIdentifier(), false, false, -1, j)
					.getPreferredSize().width;
			max += headerwidth;
			max = headerwidth > max ? headerwidth : max;
			this.getColumnModel().getColumn(j).setPreferredWidth(max);
			allwidth += (max + this.getIntercellSpacing().width);
		}
		allwidth += this.getIntercellSpacing().width;
		if (allwidth > parentSize.width) {
			this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		} else {
			this.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
		}
	}

	@Override
	public void displayUI() {
		this.initContainer();
		this.showResource();
	}

	/**
	 * https://www.oschina.net/question/2418490_2181545
	 *
	 * @author liangxf
	 *
	 */
	// class SelectionListener implements ListSelectionListener {
	// private JTableOrder that;
	//
	// SelectionListener() {
	// this.that = JTableOrder.this;
	// }
	//
	// @Override
	// public void valueChanged(ListSelectionEvent e) {
	// // if ((e.getSource() == that.getColumnModel().getSelectionModel())
	// // && that.getRowSelectionAllowed()) {
	// // int firstRow = e.getFirstIndex();
	// // int lastRow = e.getLastIndex();
	// //
	// // // TODO:处理事件
	// // System.out.println("处理事件");
	// // }
	// System.out.println("处理事件");
	// }
	// }
	// /**
	// * http://www.open-open.com/lib/view/open1381034153955.html
	// *
	// * @author liangxf
	// *
	// */
	private class RowListener implements ListSelectionListener {
		private JTableOrder that;

		public RowListener(JTableOrder that) {
			this.that = that;
		}

		public void valueChanged(ListSelectionEvent event) {
			if (event.getValueIsAdjusting()) {
				return;
			}
			//TODO:先注销
//			int selectedRow = that.getSelectedRow();
//			String productCount = (String) that.defaultModel.getValueAt(selectedRow, 5);
//			System.out.println("处理事件，产品数量" + productCount);
		}
	}
}
