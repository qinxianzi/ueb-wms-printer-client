package com.ueb.wms.printer.client.service;

import java.awt.image.BufferedImage;
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
	 * @throws Exception
	 */
	void updateHttpServerInfo(String url, String port, String context) throws Exception;

	/**
	 * 获取服务器连接配置信息
	 * 
	 * @return
	 */
	Map<String, String> getConfigValues();

	/**
	 * 用户登陆
	 * 
	 * @param account
	 * @param password
	 * @throws Exception
	 */
	void userLogin(String account, String password) throws Exception;
}
