package com.ueb.wms.printer.client.serial;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

@Component("serialUtil")
public class SerialUtil {

	private static final Logger logger = LoggerFactory.getLogger(SerialUtil.class);

	private Map<String, Integer> devices;

	public SerialUtil() {
		this.initDevices();
	}

	private void initDevices() {
		this.devices = new HashMap<String, Integer>();
		devices.put("TCS-60", 1000);
		devices.put("XK3190-A9+", 1001);
		devices.put("TCS-D31", 1002);
		devices.put("XK3190-A7", 1003);
		devices.put("AWH-SAH3", 1004);
		devices.put("XK3190-A27E", 1005);
		devices.put("ACS", 1006);
		devices.put("TC60KA", 1007);
		devices.put("SZJD", 1008);
		devices.put("YSX-30A", 1009);
		devices.put("LP7510", 1010);
		devices.put("HTW100A", 1011);
		devices.put("JCE-30K", 1012);
		devices.put("DI-162", 1013);
		devices.put("TCS-600CE-W", 1014);
		devices.put("HTW-100KG", 1015);
		devices.put("RS232", 1016);
		devices.put("BS-30KA", 1017);
		devices.put("LNWH-3KG", 1018);
		devices.put("DS-788", 1019);
		devices.put("LWI9900", 1020);
		devices.put("KHW30KG", 1021);
		devices.put("XK3190-A12+E", 1022);
		devices.put("ASC-FJZ-15", 1023);
		devices.put("TC150KA", 1024);
		devices.put("BS-30KA-15KG", 1025);
		devices.put("BT-3", 1026);
	}

	public static void main(String[] args) {
		SerialUtil se = new SerialUtil();
		se.listSerialPort();
		System.out.println("aaaaaaaaaaaaaa");
	}

	/**
	 * 查找所有可用的串口
	 */
	public void listSerialPort() {
		try {
			CommPortIdentifier commPort = null;
			Enumeration<?> en = CommPortIdentifier.getPortIdentifiers();
			int serialCount = 0;

			while (en.hasMoreElements()) {
				commPort = (CommPortIdentifier) en.nextElement();
				if (CommPortIdentifier.PORT_SERIAL == commPort.getPortType()) {
					logger.info("检测串口{}:{}", (++serialCount), commPort.getName());
				}
			}
		} catch (Exception e) {
			logger.info("检测可用串口时遇到异常：", e.getMessage());
		}
	}

	/**
	 * 获取串口设备
	 * 
	 * @param deviceType
	 *            设备类型
	 * @return
	 * @throws Exception
	 */
	public ISerialDevice getSerialDevice(String deviceType) throws Exception {
		if (!devices.containsKey(deviceType)) {
			throw new Exception(String.format("无法识别的设备类型%s", deviceType));
		}
		int typeValue = devices.get(deviceType);
		ISerialDevice device = null;
		switch (typeValue) {
		case 1000:
			device = null;
			break;
		}
		return device;
	}

	/**
	 * 打开串口设备
	 * 
	 * @param parameters
	 *            串口设备参数
	 * @return
	 * @throws Exception
	 */
	public SerialMonitor openSerialPort(SerialParameters parameters) throws Exception {
		SerialMonitor serialMonitor = new SerialMonitor();
		String portName = parameters.getPortName();

		// 识别串口===start
		CommPortIdentifier commPort = null;
		try {
			commPort = CommPortIdentifier.getPortIdentifier(portName); // 通过端口名识别端口
		} catch (NoSuchPortException e) {
			throw new Exception(String.format("没有指定的串口，异常信息是:%s", e.getMessage()));
		}

		// 打开串口===start
		SerialPort serialPort = null;
		try {
			// 打开端口，指定端口名字（任意取名字）和一个timeout（打开操作的超时时间）
			serialPort = (SerialPort) commPort.open("Serial_Communication", 2000);
			logger.info("正在打开串口{}", portName);
		} catch (PortInUseException e) {
			throw new Exception(String.format("串口%s已被另外的应用程序占用，无法打开，异常信息是:%s", serialPort, e.getMessage()));
		}
		serialMonitor.setPortName(portName);
		serialMonitor.setSerialPort(serialPort);

		// 获取串口输入流===start
		try {
			InputStream serialInput = serialPort.getInputStream();
			serialMonitor.setInputStream(serialInput);
			logger.info("获取串口{}的输入流", portName);
		} catch (IOException e) {
			throw new Exception(String.format("获取串口%s的输入流出现异常，异常信息是：%s", portName, e.getMessage()));
		} finally {
			stopSerialPort(serialMonitor);
		}

		try {
			OutputStream serialOut = serialPort.getOutputStream();
			serialMonitor.setOutputStream(serialOut);
			logger.info("获取串口{}的输出流", portName);
		} catch (IOException e) {
			throw new Exception(String.format("获取串口%s的输出流出现异常，异常信息是：%s", portName, e.getMessage()));
		} finally {
			stopSerialPort(serialMonitor);
		}

		try {
			this.setSerialParameters(serialPort, parameters);
		} catch (UnsupportedCommOperationException e) {
			throw new Exception(String.format("设置串口%s的参数出现异常，异常信息是：%s", portName, e.getMessage()));
		} finally {
			stopSerialPort(serialMonitor);
		}

		serialMonitor.setVEL_TYPE(parameters.getVelType());
		serialMonitor.setBAUDRATE(String.valueOf(parameters.getBaudRate()));
		serialMonitor.setFLOWCONTROLIN(parameters.getFlowControlInString());
		serialMonitor.setFLOWCONTROLOUT(parameters.getFlowControlOutString());
		serialMonitor.setDATABITS(parameters.getDatabitsString());
		serialMonitor.setSTOPBITS(parameters.getStopbitsString());
		serialMonitor.setPARITY(parameters.getParityString());
		serialMonitor.setPROP_FILENAME(parameters.getPropfilename());
		return serialMonitor;
	}

	/**
	 * 关闭串口
	 * 
	 * @param serialMonitor
	 */
	public void stopSerialPort(SerialMonitor serialMonitor) {
		if (null == serialMonitor) {
			return;
		}
		serialMonitor.setIS_STOPPED(true);
		try {
			OutputStream serialOutput = serialMonitor.getoutputStream();
			if (null != serialOutput) {
				serialOutput.close();
				serialMonitor.setoutputStream(null);
			}

			InputStream serialInput = serialMonitor.getinputStream();
			if (null != serialInput) {
				serialInput.close();
				serialMonitor.setinputStream(null);
			}

			SerialPort serialPort = serialMonitor.getSerialPort();
			if (null != serialPort) {
				serialPort.removeEventListener();
				serialPort.close();
				serialMonitor.setSerialPort(null);
			}
		} catch (Exception e) {
			logger.info("关闭串口{}出现异常，异常信息是：{}", serialMonitor.getPortName(), e.getMessage());
		}
	}

	/**
	 * 设置串口参数
	 * 
	 * @param serialPort
	 *            串口
	 * @param parameters
	 *            参数
	 * @throws Exception
	 */
	public void setSerialParameters(SerialPort serialPort, SerialParameters parameters) throws Exception {
		int oldBaudRate = serialPort.getBaudRate(); // 波特率
		int oldDatabits = serialPort.getDataBits(); // 数据位数
		int oldStopbits = serialPort.getStopBits(); // 停止位
		int oldParity = serialPort.getParity(); // 偶校验
		try {
			serialPort.setSerialPortParams(parameters.getBaudRate(), parameters.getDatabits(), parameters.getStopbits(),
					parameters.getParity());

			// 输入流控制 | 输出流控制
			serialPort.setFlowControlMode(parameters.getFlowControlIn() | parameters.getFlowControlOut());
		} catch (UnsupportedCommOperationException e) {
			parameters.setBaudRate(oldBaudRate);
			parameters.setDatabits(oldDatabits);
			parameters.setStopbits(oldStopbits);
			parameters.setParity(oldParity);
			throw e;
		}
		serialPort.setOutputBufferSize(512);
	}
}
