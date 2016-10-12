package com.ueb.wms.printer.client.serial;

import java.io.InputStream;
import java.io.OutputStream;

import gnu.io.SerialPort;

public class SerialMonitor {

	public String START_SEQ;

	/**
	 * 串口名称
	 */
	public String portName;

	public String getPortName() {
		return portName;
	}

	public void setPortName(String portName) {
		this.portName = portName;
	}

	public String BAUDRATE;
	public String FLOWCONTROLIN;
	public String FLOWCONTROLOUT;
	public String DATABITS;
	public String STOPBITS;
	public String PARITY;
	public String PROP_FILENAME;

	/**
	 * 串口对象
	 */
	public SerialPort serialPort;

	/**
	 * 串口输入流
	 */
	public InputStream inputStream;

	/**
	 * 串口输出流
	 */
	public OutputStream outputStream;

	public boolean IS_STOPPED = false;
	public String VEL_TYPE;

	public String getVEL_TYPE() {
		return this.VEL_TYPE;
	}

	public void setVEL_TYPE(String vel_type) {
		this.VEL_TYPE = vel_type;
	}

	public boolean isIS_STOPPED() {
		return this.IS_STOPPED;
	}

	public boolean getIS_STOPPED() {
		return this.IS_STOPPED;
	}

	public void setIS_STOPPED(boolean is_stopped) {
		this.IS_STOPPED = is_stopped;
	}

	public String getSTART_SEQ() {
		return this.START_SEQ;
	}

	public void setSTART_SEQ(String start_seq) {
		this.START_SEQ = start_seq;
	}

	public String getBAUDRATE() {
		return this.BAUDRATE;
	}

	public void setBAUDRATE(String baudrate) {
		this.BAUDRATE = baudrate;
	}

	public String getFLOWCONTROLIN() {
		return this.FLOWCONTROLIN;
	}

	public void setFLOWCONTROLIN(String flowcontrolin) {
		this.FLOWCONTROLIN = flowcontrolin;
	}

	public String getFLOWCONTROLOUT() {
		return this.FLOWCONTROLOUT;
	}

	public void setFLOWCONTROLOUT(String flowcontrolout) {
		this.FLOWCONTROLOUT = flowcontrolout;
	}

	public String getDATABITS() {
		return this.DATABITS;
	}

	public void setDATABITS(String databits) {
		this.DATABITS = databits;
	}

	public String getSTOPBITS() {
		return this.STOPBITS;
	}

	public void setSTOPBITS(String stopbits) {
		this.STOPBITS = stopbits;
	}

	public String getPARITY() {
		return this.PARITY;
	}

	public void setPARITY(String parity) {
		this.PARITY = parity;
	}

	public String getPROP_FILENAME() {
		return this.PROP_FILENAME;
	}

	public void setPROP_FILENAME(String prop_filename) {
		this.PROP_FILENAME = prop_filename;
	}

	public SerialPort getSerialPort() {
		return this.serialPort;
	}

	public void setSerialPort(SerialPort serialPort) {
		this.serialPort = serialPort;
	}

	public InputStream getInputStream() {
		return this.inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public OutputStream getOutputStream() {
		return this.outputStream;
	}

	public void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	public InputStream getinputStream() {
		return this.inputStream;
	}

	public void setinputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public OutputStream getoutputStream() {
		return this.outputStream;
	}

	public void setoutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}
}
