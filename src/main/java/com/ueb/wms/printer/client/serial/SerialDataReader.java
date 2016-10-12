package com.ueb.wms.printer.client.serial;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ueb.wms.printer.client.constants.WmsConstants;

@Component("serialDataReader")
public class SerialDataReader {

	private static final Logger logger = LoggerFactory.getLogger(SerialDataReader.class);

	@Autowired
	private SerialUtil serialUtil;

	private SerialParameters serialParam;
	private ISerialDevice serialDevice;
	private SerialMonitor serialPortObj = null;
	private long readCount;
	private static final long MAX_LONG_VALUE = 999999L;
	private String waitTime;

	public SerialDataReader() {
		try {
			this.serialParam = this.readSerialParameters();
		} catch (Exception e) {
			logger.info("读取串口初始化信息出现异常，异常信息是：{}", e.getMessage());
		}
	}

	public SerialPortData readData() throws Exception {
		if (this.readCount == MAX_LONG_VALUE) {
			this.readCount = 0L;
		}
		this.readCount += 1L;

		String deviceType = StringUtils.trim(this.serialParam.getVelType());
		ISerialDevice serialDevice = serialUtil.getSerialDevice(deviceType);
		SerialMonitor serialMonitor = serialUtil.openSerialPort(this.serialParam);
		float serialData = serialDevice.readDataFromSerialDevice(serialMonitor, this);

		SerialPortData serialPortData = new SerialPortData();
		serialPortData.setOk(true);
		serialPortData.setFloatValue(serialData);
		serialPortData.setStringValue(String.valueOf(serialData));
		if (StringUtils.equalsIgnoreCase("HTW-100KG", deviceType) || StringUtils.equalsIgnoreCase("DS-788", deviceType)
				|| StringUtils.equalsIgnoreCase("DI-162", deviceType)
				|| StringUtils.equalsIgnoreCase("AWH-SAH3", deviceType)) {
			serialUtil.stopSerialPort(serialMonitor);
			// this.serialPortObj = null;
		}
		// if ((serialPortData.isOk()) && (Float.compare(serialData, 0.0f) > 0))
		// {
		// playUDFWav();
		// }
		return serialPortData;
	}

	// public void playUDFWav() {
	// String dir = "tmpcfg\\config.properties";
	//
	// String propID = "MSG_SON_DIR";
	//
	// String path = Util.getConfig(dir, propID);
	// String wavFile = "CatchWeightOK.wav";
	// Util.playWav(path, wavFile);
	// }

	public long getReadCount() {
		return this.readCount;
	}

	private SerialParameters readSerialParameters() throws Exception {
		InputStream input = null;
		try {
			input = new FileInputStream(WmsConstants.SERIAL_FILE_PATH);
			Properties properties = new Properties();
			properties.load(input);
			return this.buildSerialParameters(properties);
		} finally {
			if (null != input) {
				input.close();
			}
		}
	}

	private SerialParameters buildSerialParameters(Properties properties) throws Exception {
		if (null == properties || properties.isEmpty()) {
			throw new Exception("串口参数配置信息文件不存在");
		}

		SerialParameters parameters = new SerialParameters();
		String propValue = properties.getProperty("SerialPort.portName", "").trim().toUpperCase();
		parameters.setPortName(propValue);

		propValue = properties.getProperty("SerialPort.baudRate", "9600").trim();
		parameters.setBaudRate(propValue);

		propValue = properties.getProperty("SerialPort.flowControlIn", "None").trim();
		parameters.setFlowControlIn(propValue);

		propValue = properties.getProperty("SerialPort.flowControlOut", "None").trim();
		parameters.setFlowControlOut(propValue);

		propValue = properties.getProperty("SerialPort.databits", "8").trim();
		parameters.setDatabits(propValue);

		propValue = properties.getProperty("SerialPort.stopbits", "1").trim();
		parameters.setStopbits(propValue);

		propValue = properties.getProperty("SerialPort.parity", "None").trim();
		parameters.setParity(propValue);

		propValue = properties.getProperty("SerialPort.veltype", "XXX").trim().toUpperCase();
		parameters.setVelType(propValue);

		this.waitTime = StringUtils.trim(properties.getProperty("SerialPort.waitTime", ""));
		return parameters;
	}

	public String getWeigthWaitTime() {
		return this.waitTime;
	}

	public void readWaitTime() {
		long threadWaitTime = -1L;
		if (StringUtils.isNoneBlank(this.waitTime)) {
			long tmpTime = Long.valueOf(this.waitTime);
			threadWaitTime = ((tmpTime > 0) && (tmpTime <= 1000)) ? tmpTime : 1000L;
		} else if (1L == this.readCount) { // 第一次读取时等待1秒钟
			threadWaitTime = 1000L;
		}
		try {
			if (threadWaitTime > 0) {
				logger.info("线程{}从输入设备读入数据需要等待{}秒钟", Thread.currentThread().getName(), threadWaitTime);
				Thread.sleep(threadWaitTime);
			}
		} catch (InterruptedException e) {
			logger.info("线程{}从输入设备读入数据出现异常，异常信息是：{}", Thread.currentThread().getName(), e.getMessage());
		}
	}
}
