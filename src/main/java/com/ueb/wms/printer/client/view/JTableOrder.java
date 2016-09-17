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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.ueb.wms.printer.client.vo.ReportDataVO;

@SuppressWarnings("serial")
@Component("jTableOrder")
public class JTableOrder extends JTable implements IBaseView {
	private String[] headers = { "订单编号", "SO编号", "来源平台", "物流编号", "物流单号", "承运人", "收货人名称", "收货人地址", "国家外文", "国家中文", "城市",
			"省份", "邮编", "收件联系人", "收件联系电话", "货主联系电话", "产品", "产品库位", "产品数量", "产品英文描述", "订单重量kg", "订单金额", "平台订单号",
			"卖家账号" };
	private DefaultTableModel defaultModel;

	private void initContainer() {
		this.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (2 == e.getClickCount()) {
					JTableOrder that = JTableOrder.this;
					int row = that.getSelectedRow();
					String orderNO = (String) that.getValueAt(row, 0);

					OrderTableView parent = (OrderTableView) that.getParent().getParent();
					parent.activationReportView(StringUtils.trim(orderNO));
				}
			}
		});

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
}
