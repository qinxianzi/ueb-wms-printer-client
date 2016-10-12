package com.ueb.wms.printer.client.serial;

public interface ISerialDevice {
	float readDataFromSerialDevice(SerialMonitor serialMonitor, SerialDataReader serialReader);
}
