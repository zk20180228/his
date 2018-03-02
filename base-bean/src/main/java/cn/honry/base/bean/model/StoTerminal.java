package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * 配药台与发药窗口表
 * @author zpty
 * Date:2016/01/16 15:30
 */

public class StoTerminal extends Entity {	
	/**终端编号**/
	private String code;
	/**终端名称**/
	private String name;
	/**所属药房编码**/
	private String deptCode;
	/**类型: 0发药窗口/1配药台**/
	private Integer type;
	/**替代终端编码(替代配药台或者替代发药窗口)**/
	private String replaceCode;
	/**是否关闭标记: 0开放/1关闭**/
	private Integer closeFlag;
	/**是否自动打印: 0否/1是**/
	private Integer autopringFlag;
	/**刷新时间间隔1(秒),用于配药台或者发药窗口程序的刷新时间**/
	private Integer refreshInterval1;
	/**刷新时间间隔2(秒),用于打印标签或者大屏幕刷新的时间间隔**/
	private Integer refreshInterval2;
	/**终端性质:0普通/1专科/2特殊**/
	private Integer property;
	/**警戒线（排队人数报警数）**/
	private Integer alertNum;
	/**大屏幕显示患者数(默认5个)**/
	private Integer showNum;
	/**发药窗口编码(只有配药台才有此属性)**/
	private String sendWindow;
	/**操作员**/
	private String operCode;
	/**操作日期**/
	private Date operDate;
	/**备注  ‘1’ 配药台当前已使用 不能再登陆 0没人使用**/
	private String mark;
	/**对于配药台已发送的药品品种数**/
	private Double sendQty = 0D;
	/**对于配药台待配药的药品种数**/
	private Double drugQty = 0D;
	/**处方调剂过程中对于竞争调剂的均分次数参数**/
	private Integer averageNum = 0;
	/**打印方式0 标签 1 清单 2 扩展**/
	private Integer printType;
	/**是否允许回车发药**/
	private Integer isallowentersend;
	/**发药窗口名称**/
	private String sendWindowName;
	/**0-默认，非0-已登陆**/
	private Integer loginMark;
	/**登陆主机IP地址（用于绑定配药或发药窗口所在主机的IP）**/
	private String loginIp;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getReplaceCode() {
		return replaceCode;
	}
	public void setReplaceCode(String replaceCode) {
		this.replaceCode = replaceCode;
	}
	public Integer getCloseFlag() {
		return closeFlag;
	}
	public void setCloseFlag(Integer closeFlag) {
		this.closeFlag = closeFlag;
	}
	public Integer getAutopringFlag() {
		return autopringFlag;
	}
	public void setAutopringFlag(Integer autopringFlag) {
		this.autopringFlag = autopringFlag;
	}
	public Integer getRefreshInterval1() {
		return refreshInterval1;
	}
	public void setRefreshInterval1(Integer refreshInterval1) {
		this.refreshInterval1 = refreshInterval1;
	}
	public Integer getRefreshInterval2() {
		return refreshInterval2;
	}
	public void setRefreshInterval2(Integer refreshInterval2) {
		this.refreshInterval2 = refreshInterval2;
	}
	public Integer getProperty() {
		return property;
	}
	public void setProperty(Integer property) {
		this.property = property;
	}
	public Integer getAlertNum() {
		return alertNum;
	}
	public Double getSendQty() {
		return sendQty;
	}
	public Double getDrugQty() {
		return drugQty;
	}
	public void setAlertNum(Integer alertNum) {
		this.alertNum = alertNum;
	}
	public Integer getShowNum() {
		return showNum;
	}
	public void setShowNum(Integer showNum) {
		this.showNum = showNum;
	}
	public String getSendWindow() {
		return sendWindow;
	}
	public void setSendWindow(String sendWindow) {
		this.sendWindow = sendWindow;
	}
	public String getOperCode() {
		return operCode;
	}
	public void setOperCode(String operCode) {
		this.operCode = operCode;
	}
	public Date getOperDate() {
		return operDate;
	}
	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public void setSendQty(Double sendQty) {
		this.sendQty = sendQty;
	}
	public void setDrugQty(Double drugQty) {
		this.drugQty = drugQty;
	}
	public Integer getAverageNum() {
		return averageNum;
	}
	public void setAverageNum(Integer averageNum) {
		this.averageNum = averageNum;
	}
	public Integer getPrintType() {
		return printType;
	}
	public void setPrintType(Integer printType) {
		this.printType = printType;
	}
	public Integer getIsallowentersend() {
		return isallowentersend;
	}
	public void setIsallowentersend(Integer isallowentersend) {
		this.isallowentersend = isallowentersend;
	}
	public String getSendWindowName() {
		return sendWindowName;
	}
	public void setSendWindowName(String sendWindowName) {
		this.sendWindowName = sendWindowName;
	}
	
	/** 
	* @Title: getLoginMark 
	* @Description: 0-默认，非0-已登陆 
	* @date 2017年2月7日
	*/
	public Integer getLoginMark() {
		return loginMark;
	}
	/** 
	* @Title: setLoginMark 
	* @Description: 0-默认，非0-已登陆
	* @date 2017年2月7日
	*/
	public void setLoginMark(Integer loginMark) {
		this.loginMark = loginMark;
	}
	/** 
	* @Title: getLoginIp 
	* @Description: 登陆主机IP地址（用于绑定配药或发药窗口所在主机的IP）
	* @date 2017年2月7日
	*/
	public String getLoginIp() {
		return loginIp;
	}
	/** 
	* @Title: setLoginIp 
	* @Description: 登陆主机IP地址（用于绑定配药或发药窗口所在主机的IP） 
	* @date 2017年2月7日
	*/
	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
	
}