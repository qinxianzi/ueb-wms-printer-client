package com.ueb.wms.printer.client.util;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Hours;
import org.joda.time.format.DateTimeFormat;

public class UebDateTimeUtil {

	public final static String YYYYMMDD = "yyyyMMdd";
	public final static String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
	public final static String YYYYMMDDHHMMSSSSSZ = "yyyyMMddHHmmssSSSZ";

	private static final DateTimeZone CTT = DateTimeZone.forID("Asia/Shanghai");

	public static String getNowDateStr(String ymdFormat) {
		DateTime now = getNow();
		ymdFormat = StringUtils.isBlank(ymdFormat) ? ymdFormat : YYYYMMDD;
		return now.toString(DateTimeFormat.forPattern(ymdFormat));
	}

	public static String getNowStr(String format) {
		DateTime now = getNow();
		format = StringUtils.isBlank(format) ? format : YYYYMMDDHHMMSS;
		return now.toString(DateTimeFormat.forPattern(format));
	}

	public static DateTime getNow() {
		DateTime now = new DateTime(CTT);
		return now;
	}

	/**
	 * java.util.Date转换成 org.joda.time.DateTime
	 * 
	 * @param date
	 *            java.util.Date对象，不能为null
	 * @return
	 */
	public static DateTime convert2DateTime(Date date) {
		if (null == date) {
			throw new IllegalArgumentException("date parameters cannot be null");
		}
		return new DateTime(date.getTime(), CTT);
	}

	/**
	 * 日期减少小时数
	 * 
	 * @param date
	 *            默认为当前时间
	 * @param hours
	 *            小时数，必须大于0
	 * @return
	 */
	public static DateTime minusHours(DateTime date, int hours) {
		if (hours < 0) {
			throw new IllegalArgumentException("hours parameters must be greater than zero");
		}
		date = (null == date ? getNow() : date);
		return date.minusHours(hours);
	}

	/**
	 * 日期增加小时数
	 * 
	 * @param date
	 *            默认为当前时间
	 * @param hours
	 *            小时数，必须大于0
	 * @return
	 */
	public static DateTime plusHours(DateTime date, int hours) {
		if (hours < 0) {
			throw new IllegalArgumentException("hours parameters must be greater than zero");
		}
		date = (null == date ? getNow() : date);
		return date.plusHours(hours);
	}

	/**
	 * 计算两个日期相差的小时数
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static int hoursBetween(DateTime d1, DateTime d2) {
		if (null == d1 || null == d2) {
			throw new IllegalArgumentException("parameters cannot be null");
		}
		return Hours.hoursBetween(d1, d2).getHours();
	}

	/**
	 * Date转换成Timestamp
	 * 
	 * @param date
	 * @return
	 */
	public static Timestamp parseDate2Timestamp(DateTime date) {
		if (null == date) {
			date = getNow();
		}
		return new Timestamp(date.getMillis());
	}

	public static Date parse2Date(String dateStr, String format) {
		format = StringUtils.isBlank(format) ? YYYYMMDDHHMMSSSSSZ : format;
		DateTime d = DateTime.parse(dateStr, DateTimeFormat.forPattern(format));
		return d.toDate();
	}

	public static Date parse2Date(String dateSt) {
		return parse2Date(dateSt, YYYYMMDDHHMMSSSSSZ);
	}
}
