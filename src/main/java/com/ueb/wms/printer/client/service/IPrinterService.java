package com.ueb.wms.printer.client.service;

import java.awt.image.BufferedImage;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.ueb.wms.printer.client.vo.ReportDataVO;

public interface IPrinterService {

	/**
	 * 根据"订单编号、SO编号、物流单号"查询面单数据
	 * 
	 * @param search
	 *            查询条件
	 * @return
	 * @throws Exception
	 */
	ReportDataVO findReportData(String search) throws Exception;

	/**
	 * 根据波次号查询面单数据
	 * 
	 * @param waveNO
	 *            波次号
	 * @return
	 * @throws Exception
	 */
	List<ReportDataVO> findByWaveNO(String waveNO) throws Exception;

	/**
	 * 下载PDF报表模板文件
	 * 
	 * @param orderNO
	 * @return
	 * @throws Exception
	 */
	BufferedImage downloadPdfTemplate(String orderNO) throws Exception;

	/**
	 * 下载PDF模板文件，增加内容到PDF文件末尾
	 * 
	 * @param orderNO
	 *            订单编号
	 * @param contents
	 *            增加的内容
	 * @param pdffile
	 *            本地待创建的pdf文件(完整路径)
	 * @throws Exception
	 */
	void downloadPdf(String orderNO, List<String> contents, String pdffile) throws Exception;

	/**
	 * 新增承运人使用的模板类型
	 * 
	 * @param carrierid
	 * @param tplType
	 * @throws Exception
	 */
	void addCarrieridTplTypeRel(String carrierid, int tplType) throws Exception;

	/**
	 * 更改连接的服务器信息
	 * 
	 * @param url
	 * @param port
	 * @param context
	 * @param pdfTpl
	 * @throws Exception
	 */
	void updateHttpServerInfo(String url, String port, String context, String pdfTpl) throws Exception;

	/**
	 * 获取服务器连接配置信息
	 * 
	 * @return
	 */
	Map<String, String> getConfigValues();

	// /**
	// * 用户登陆
	// *
	// * @param account
	// * @param password
	// * @throws Exception
	// */
	// void userLogin(String account, String password) throws Exception;

	/**
	 * 单品复核（查询单品001数据列表）
	 * 
	 * @param waveNO
	 * @param sku
	 * @return
	 * @throws SQLException
	 */
	List<ReportDataVO> findSingleProductFirst(String waveNO, String sku) throws Exception;

	/**
	 * 打印面单之前：是否重复打印、占用该订单锁
	 * 
	 * @param orderNO
	 * @return
	 * @throws Exception
	 */
	boolean beforePrint(String orderNO) throws Exception;

	/**
	 * 面单打印完毕：增加打印次数、释放该订单锁
	 * 
	 * @param orderNO
	 * @return
	 * @throws Exception
	 */
	boolean afterPrint(String orderNO, boolean isPrintSuccess) throws Exception;
}
