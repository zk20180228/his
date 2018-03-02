package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/***
 * 门诊收款日结实体
 * @ClassName: OutpatientDaybalance 
 * @Description: 
 * @author wfj
 * @date 2016年6月13日 下午3:45:23 
 *
 */
public class OutpatientDaybalance extends Entity implements java.io.Serializable{
	private static final long serialVersionUID = 1L;

	/**日结序号 **/
	private String blanceNo;
	/**开始时间 **/
	private Date beginDate;
	/**结束时间 **/
	private Date endDate;
	/**收款员代码 **/
	private String operCode;
	/**收款员姓名 **/
	private String operName;
	/**操作时间 **/
	private Date operDate;
	/**总收入 **/
	private Double totCost;
	/**现金金额**/
	private Double caCost;
	/**银联金额**/
	private Double cdCost;
	/**支票金额**/
	private Double chCost;
	/**院内账户**/
	private Double ysCost;
	/**日结员**/
	private String oper;
	/**张发票数 **/
	private Double balanceInvoiceNum;
	/**退票据张数 **/
	private Double backInvoiceNum;
	/**作废票据张数 **/
	private Double cancelInvoiceNum;
	/**日结收项目数，即收统计大类数 **/
	private Double balanceItem;
	/**日结退项目数，即退统计大类数 **/
	private Double backItem;
	/**退金额**/
	private Double backFee;
	/**退药品金额**/
	private Double backDrug;
	/**退非药品金额**/
	private Double backUndrug;
	/**备注1**/
	private String ext1;
	/**备注2**/
	private String ext2;
	/**备注3**/
	private String ext3;
	/**备注4**/
	private String ext4;
	/**备注5**/
	private String ext5;
	/**账户预交金现金**/
	private Double ysPrepayca;
	/**账户预交金银联**/
	private Double ysPrepaydb;
	/**账户支付**/
	private Double ysPay;
	/**账户结清金额**/
	private Double ysBalanceCost;
	/**上交现金金额**/
	private Double sjTotcaCost;
	/**上缴银联金额**/
	private Double sjTotdbCost;
	/**财务审核，1未审核/2已审核 **/
	private Integer checkFlag = 1;
	/**数据库无关字段 ----开始**/
	private Double backysundrug;
	private Double backysdrug;
	
	/**数据库无关字段 ----结束**/
	
	public String getBlanceNo() {
		return blanceNo;
	}
	public String getOper() {
		return oper;
	}
	public void setOper(String oper) {
		this.oper = oper;
	}
	public Double getBackysundrug() {
		return backysundrug;
	}
	public void setBackysundrug(Double backysundrug) {
		this.backysundrug = backysundrug;
	}
	public Double getBackysdrug() {
		return backysdrug;
	}
	public void setBackysdrug(Double backysdrug) {
		this.backysdrug = backysdrug;
	}
	public void setBlanceNo(String blanceNo) {
		this.blanceNo = blanceNo;
	}
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Double getTotCost() {
		return totCost;
	}
	public void setTotCost(Double totCost) {
		this.totCost = totCost;
	}
	public String getOperCode() {
		return operCode;
	}
	public void setOperCode(String operCode) {
		this.operCode = operCode;
	}
	public String getOperName() {
		return operName;
	}
	public void setOperName(String operName) {
		this.operName = operName;
	}
	public Date getOperDate() {
		return operDate;
	}
	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}
	
	public Integer getCheckFlag() {
		return checkFlag;
	}
	public void setCheckFlag(Integer checkFlag) {
		this.checkFlag = checkFlag;
	}
	public Double getCaCost() {
		return caCost;
	}
	public void setCaCost(Double caCost) {
		this.caCost = caCost;
	}
	public Double getCdCost() {
		return cdCost;
	}
	public void setCdCost(Double cdCost) {
		this.cdCost = cdCost;
	}
	public Double getChCost() {
		return chCost;
	}
	public void setChCost(Double chCost) {
		this.chCost = chCost;
	}
	public Double getYsCost() {
		return ysCost;
	}
	public void setYsCost(Double ysCost) {
		this.ysCost = ysCost;
	}
	public Double getBalanceInvoiceNum() {
		return balanceInvoiceNum;
	}
	public void setBalanceInvoiceNum(Double balanceInvoiceNum) {
		this.balanceInvoiceNum = balanceInvoiceNum;
	}
	public Double getBackInvoiceNum() {
		return backInvoiceNum;
	}
	public void setBackInvoiceNum(Double backInvoiceNum) {
		this.backInvoiceNum = backInvoiceNum;
	}
	public Double getCancelInvoiceNum() {
		return cancelInvoiceNum;
	}
	public void setCancelInvoiceNum(Double cancelInvoiceNum) {
		this.cancelInvoiceNum = cancelInvoiceNum;
	}
	public Double getBalanceItem() {
		return balanceItem;
	}
	public void setBalanceItem(Double balanceItem) {
		this.balanceItem = balanceItem;
	}
	public Double getBackItem() {
		return backItem;
	}
	public void setBackItem(Double backItem) {
		this.backItem = backItem;
	}
	public Double getBackFee() {
		return backFee;
	}
	public void setBackFee(Double backFee) {
		this.backFee = backFee;
	}
	public Double getBackDrug() {
		return backDrug;
	}
	public void setBackDrug(Double backDrug) {
		this.backDrug = backDrug;
	}
	public Double getBackUndrug() {
		return backUndrug;
	}
	public void setBackUndrug(Double backUndrug) {
		this.backUndrug = backUndrug;
	}
	public String getExt1() {
		return ext1;
	}
	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}
	public String getExt2() {
		return ext2;
	}
	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}
	public String getExt3() {
		return ext3;
	}
	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}
	public String getExt4() {
		return ext4;
	}
	public void setExt4(String ext4) {
		this.ext4 = ext4;
	}
	public String getExt5() {
		return ext5;
	}
	public void setExt5(String ext5) {
		this.ext5 = ext5;
	}
	public Double getYsPrepayca() {
		return ysPrepayca;
	}
	public void setYsPrepayca(Double ysPrepayca) {
		this.ysPrepayca = ysPrepayca;
	}
	public Double getYsPrepaydb() {
		return ysPrepaydb;
	}
	public void setYsPrepaydb(Double ysPrepaydb) {
		this.ysPrepaydb = ysPrepaydb;
	}
	public Double getYsPay() {
		return ysPay;
	}
	public void setYsPay(Double ysPay) {
		this.ysPay = ysPay;
	}
	public Double getYsBalanceCost() {
		return ysBalanceCost;
	}
	public void setYsBalanceCost(Double ysBalanceCost) {
		this.ysBalanceCost = ysBalanceCost;
	}
	public Double getSjTotcaCost() {
		return sjTotcaCost;
	}
	public void setSjTotcaCost(Double sjTotcaCost) {
		this.sjTotcaCost = sjTotcaCost;
	}
	public Double getSjTotdbCost() {
		return sjTotdbCost;
	}
	public void setSjTotdbCost(Double sjTotdbCost) {
		this.sjTotdbCost = sjTotdbCost;
	}

}
