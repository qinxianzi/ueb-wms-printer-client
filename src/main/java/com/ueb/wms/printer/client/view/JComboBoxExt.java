package com.ueb.wms.printer.client.view;

import java.awt.Component;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.apache.commons.lang3.StringUtils;

import com.ueb.wms.printer.client.vo.ComboBoxItemVO;

public class JComboBoxExt<E> extends JComboBox<E> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4264382926257676905L;

	public JComboBoxExt(ComboBoxModel<E> model) {
		super(model);
		this.rendererData();
	}

	public JComboBoxExt(E[] items) {
		this(new DefaultComboBoxModel<E>(items));
	}

	public JComboBoxExt(Vector<E> items) {
		this(new DefaultComboBoxModel<E>(items));
	}

	public JComboBoxExt() {
		this(new DefaultComboBoxModel<E>());
	}

	public void rendererData() {
		ListCellRenderer<? super E> render = new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (null != value && value instanceof ComboBoxItemVO) {
					ComboBoxItemVO itemVo = (ComboBoxItemVO) value;
					this.setText(itemVo.getValue());
				}
				return this;
			}
		};
		this.setRenderer(render);
	}

	public void updateData(Vector<E> values) {
		this.setModel(new DefaultComboBoxModel<E>(values));
		this.rendererData();
	}

	@Override
	public void setSelectedItem(Object obj) { // 选中value与传入的参数相同的项
		if (null == obj || obj instanceof ComboBoxItemVO) {
			super.setSelectedItem(obj);
			return;
		}
		if (obj instanceof String) {
			for (int index = 0, len = this.getItemCount(); index < len; index++) {
				ComboBoxItemVO itemVo = (ComboBoxItemVO) getItemAt(index);
				if (itemVo.getValue().equals(obj.toString())) {
					super.setSelectedIndex(index);
				}
			}
		}
	}

	public void setSelectedValue(Object obj) {
		if (null == obj || obj instanceof ComboBoxItemVO) {
			super.setSelectedItem(obj);
			return;
		}
		if (obj instanceof String) {
			for (int index = 0, len = this.getItemCount(); index < len; index++) {
				ComboBoxItemVO itemVo = (ComboBoxItemVO) getItemAt(index);
				if (StringUtils.equals(itemVo.getKey(), obj.toString())) {
					super.setSelectedIndex(index);
				}
			}
		}
	}

	// 获得选中项的key
	public String getSelectedKey() {
		Object obj = getSelectedItem();
		if (null != obj) {
			if (obj instanceof ComboBoxItemVO) {
				ComboBoxItemVO itemVo = (ComboBoxItemVO) obj;
				return itemVo.getKey();
			}
			obj.toString();
		}
		return "";
	}

	// 获得选中项的value
	public String getSelectedValue() {
		Object obj = getSelectedItem();
		if (null != obj) {
			if (obj instanceof ComboBoxItemVO) {
				ComboBoxItemVO itemVo = (ComboBoxItemVO) obj;
				return itemVo.getValue();
			}
			obj.toString();
		}
		return "";
	}
}
