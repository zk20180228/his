package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;


/**
 * @Description 
 * @author  marongbin
 * @createDate： 2017年4月12日 下午8:11:16 
 * @modifier 
 * @modifyDate：
 * @param：  
 * @modifyRmk：  
 * @version 1.0
 */
public class OutpatientDaybalanceDetial extends Entity implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	/**日结序号**/
	private String blanceNo;
	/**开始时间**/
	private Date beginDate;
	/**结束时间**/
	private Date endDate;
	/**收费员代码**/
	private String operCode;
	/**收费员姓名**/
	private String operName;
	/**操作时间**/
	private Date operDate;
	/**类型编码**/
	private String detailType;
	/**类型名称**/
	private String detailName;
	/**金额**/
	private Double totCost;
	/**省生育pub**/
	private Double ex1;
	/**省生育own**/
	private Double ex2;
	/**省生育pay**/
	private Double ex3;
	/**省医保个人负担**/
	private Double pri1Own;
	/**省医保医保支付*/
	private Double pri1PayPub;
	/**省慢性病个人负担**/
	private Double pri2Own;
	/**省慢性病个人自付**/
	private Double pri2Pay;
	/**省慢性病医保支付**/
	private Double pri2Pub;
	/**省离休个人负担**/
	private Double pri3Own;
	/**省离休医保支付**/
	private Double pri3PayPub;
	/**市医保个人负担**/
	private Double cityOwn;
	/**市医保医保支付**/
	private Double cityPubPay;
	/**自费**/
	private Double ownPay;
	/**农合大病个人负担**/
	private Double ncsiOwn;
	/**农村大病个人自付**/
	private Double ncsiPay;
	/**农村大病医保支付**/
	private Double ncsiPub;
	/**能源化工个人负担**/
	private Double nysiOwn;
	/**能源化工个人自付**/
	private Double nysiPay;
	/**能源化工医保支付**/
	private Double nysiPub;
	/**省保健局保险个人负担**/
	private Double sbjjbcOwn;
	/**省保健局保险个人自付**/
	private Double sbjjbcPay;
	/**省保健局保险优惠**/
	private Double sbjjbcPub;
	/**省级新农合平台个人负担**/
	private Double sjxnhOwn;
	/**省级新农合平台个人自付**/
	private Double sjxnhPay;
	/**省级新农合平台 医保支付**/
	private Double sjxnhPub;
	/**异地就医(试点)个人负担**/
	private Double ydjyOwn;
	/**异地就医(试点)个人自付**/
	private Double ydjyPay;
	/**异地就医(试点)医保支付**/
	private Double ydjyPub;
	/**郑州铁路医疗保险个人负担**/
	private Double railwayOwn;
	/**郑州铁路医疗保险个人自付**/
	private Double railwayPay;
	/**郑州铁路医疗保险医保支付**/
	private Double railwayPub;
	
	
	
	public String getBlanceNo() {
		return blanceNo;
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
	public String getDetailType() {
		return detailType;
	}
	public void setDetailType(String detailType) {
		this.detailType = detailType;
	}
	public String getDetailName() {
		return detailName;
	}
	public void setDetailName(String detailName) {
		this.detailName = detailName;
	}
	public Double getTotCost() {
		return totCost;
	}
	public void setTotCost(Double totCost) {
		this.totCost = totCost;
	}
	public Double getEx1() {
		return ex1;
	}
	public void setEx1(Double ex1) {
		this.ex1 = ex1;
	}
	public Double getEx2() {
		return ex2;
	}
	public void setEx2(Double ex2) {
		this.ex2 = ex2;
	}
	public Double getEx3() {
		return ex3;
	}
	public void setEx3(Double ex3) {
		this.ex3 = ex3;
	}
	public Double getPri1Own() {
		return pri1Own;
	}
	public void setPri1Own(Double pri1Own) {
		this.pri1Own = pri1Own;
	}
	public Double getPri1PayPub() {
		return pri1PayPub;
	}
	public void setPri1PayPub(Double pri1PayPub) {
		this.pri1PayPub = pri1PayPub;
	}
	public Double getPri2Own() {
		return pri2Own;
	}
	public void setPri2Own(Double pri2Own) {
		this.pri2Own = pri2Own;
	}
	public Double getPri2Pay() {
		return pri2Pay;
	}
	public void setPri2Pay(Double pri2Pay) {
		this.pri2Pay = pri2Pay;
	}
	public Double getPri2Pub() {
		return pri2Pub;
	}
	public void setPri2Pub(Double pri2Pub) {
		this.pri2Pub = pri2Pub;
	}
	public Double getPri3Own() {
		return pri3Own;
	}
	public void setPri3Own(Double pri3Own) {
		this.pri3Own = pri3Own;
	}
	public Double getPri3PayPub() {
		return pri3PayPub;
	}
	public void setPri3PayPub(Double pri3PayPub) {
		this.pri3PayPub = pri3PayPub;
	}
	public Double getCityOwn() {
		return cityOwn;
	}
	public void setCityOwn(Double cityOwn) {
		this.cityOwn = cityOwn;
	}
	public Double getCityPubPay() {
		return cityPubPay;
	}
	public void setCityPubPay(Double cityPubPay) {
		this.cityPubPay = cityPubPay;
	}
	public Double getOwnPay() {
		return ownPay;
	}
	public void setOwnPay(Double ownPay) {
		this.ownPay = ownPay;
	}
	public Double getNcsiOwn() {
		return ncsiOwn;
	}
	public void setNcsiOwn(Double ncsiOwn) {
		this.ncsiOwn = ncsiOwn;
	}
	public Double getNcsiPay() {
		return ncsiPay;
	}
	public void setNcsiPay(Double ncsiPay) {
		this.ncsiPay = ncsiPay;
	}
	public Double getNcsiPub() {
		return ncsiPub;
	}
	public void setNcsiPub(Double ncsiPub) {
		this.ncsiPub = ncsiPub;
	}
	public Double getNysiOwn() {
		return nysiOwn;
	}
	public void setNysiOwn(Double nysiOwn) {
		this.nysiOwn = nysiOwn;
	}
	public Double getNysiPay() {
		return nysiPay;
	}
	public void setNysiPay(Double nysiPay) {
		this.nysiPay = nysiPay;
	}
	public Double getNysiPub() {
		return nysiPub;
	}
	public void setNysiPub(Double nysiPub) {
		this.nysiPub = nysiPub;
	}
	public Double getSbjjbcOwn() {
		return sbjjbcOwn;
	}
	public void setSbjjbcOwn(Double sbjjbcOwn) {
		this.sbjjbcOwn = sbjjbcOwn;
	}
	public Double getSbjjbcPay() {
		return sbjjbcPay;
	}
	public void setSbjjbcPay(Double sbjjbcPay) {
		this.sbjjbcPay = sbjjbcPay;
	}
	public Double getSbjjbcPub() {
		return sbjjbcPub;
	}
	public void setSbjjbcPub(Double sbjjbcPub) {
		this.sbjjbcPub = sbjjbcPub;
	}
	public Double getSjxnhOwn() {
		return sjxnhOwn;
	}
	public void setSjxnhOwn(Double sjxnhOwn) {
		this.sjxnhOwn = sjxnhOwn;
	}
	public Double getSjxnhPay() {
		return sjxnhPay;
	}
	public void setSjxnhPay(Double sjxnhPay) {
		this.sjxnhPay = sjxnhPay;
	}
	public Double getSjxnhPub() {
		return sjxnhPub;
	}
	public void setSjxnhPub(Double sjxnhPub) {
		this.sjxnhPub = sjxnhPub;
	}
	public Double getYdjyOwn() {
		return ydjyOwn;
	}
	public void setYdjyOwn(Double ydjyOwn) {
		this.ydjyOwn = ydjyOwn;
	}
	public Double getYdjyPay() {
		return ydjyPay;
	}
	public void setYdjyPay(Double ydjyPay) {
		this.ydjyPay = ydjyPay;
	}
	public Double getYdjyPub() {
		return ydjyPub;
	}
	public void setYdjyPub(Double ydjyPub) {
		this.ydjyPub = ydjyPub;
	}
	public Double getRailwayOwn() {
		return railwayOwn;
	}
	public void setRailwayOwn(Double railwayOwn) {
		this.railwayOwn = railwayOwn;
	}
	public Double getRailwayPay() {
		return railwayPay;
	}
	public void setRailwayPay(Double railwayPay) {
		this.railwayPay = railwayPay;
	}
	public Double getRailwayPub() {
		return railwayPub;
	}
	public void setRailwayPub(Double railwayPub) {
		this.railwayPub = railwayPub;
	}
}
