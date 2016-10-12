package com.ueb.wms.printer.client.vo;

import java.io.Serializable;
import java.util.List;

public class BeforeLoginVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -926411456719830061L;

	private String subsystem_id;

	private String subsystem_name_disp;

	private String showsequence;

	private String db_name;

	private List<WarehouseVO> warehouse;

	public String getSubsystem_id() {
		return subsystem_id;
	}

	public void setSubsystem_id(String subsystem_id) {
		this.subsystem_id = subsystem_id;
	}

	public String getSubsystem_name_disp() {
		return subsystem_name_disp;
	}

	public void setSubsystem_name_disp(String subsystem_name_disp) {
		this.subsystem_name_disp = subsystem_name_disp;
	}

	public String getShowsequence() {
		return showsequence;
	}

	public void setShowsequence(String showsequence) {
		this.showsequence = showsequence;
	}

	public String getDb_name() {
		return db_name;
	}

	public void setDb_name(String db_name) {
		this.db_name = db_name;
	}

	public List<WarehouseVO> getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(List<WarehouseVO> warehouse) {
		this.warehouse = warehouse;
	}
}
