package cn.honry.base.bean.model;

import java.util.Date;

/**
 * 日志查询实体
 * @Author: wangshujuan
 * @CreateDate: 2017年9月19日 下午4:09:43 
 */

public class LogManage implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String id;//主键
	private String serverCode;//服务代码
	private String tableName;//表名
	private String tableZhname;//表注释
	private Date synchTime;//迁移任务起始时间
	private Integer interVal;//频次
	private String interNuit;//频次单位s秒m分h时d天w周
	private Date dataStime;//任务开始时间
	private Date dataEtime;//任务结束时间
	private Integer synchNum;//同步数量
	private Integer status;//状态(0成功1失败)
	private Date createtime;//创建时间
	private Date updatetime;//更新时间
	private String remarks;//备注
	private String code;//同步代码
	private Date hissyschStartTime;//同步历史数据开始时间
	private Date hissyschUpdateTime;//同步历史数据更新时间
	private Date nowsyschStartTime;//同步当前数据开始时间
	private Date nowsyschUpdateTime;//同步当前数据更新时间
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getServerCode() {
		return serverCode;
	}
	public void setServerCode(String serverCode) {
		this.serverCode = serverCode;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getTableZhname() {
		return tableZhname;
	}
	public void setTableZhname(String tableZhname) {
		this.tableZhname = tableZhname;
	}
	public Date getSynchTime() {
		return synchTime;
	}
	public void setSynchTime(Date synchTime) {
		this.synchTime = synchTime;
	}
	public String getInterNuit() {
		return interNuit;
	}
	public void setInterNuit(String interNuit) {
		this.interNuit = interNuit;
	}
	public Date getDataStime() {
		return dataStime;
	}
	public void setDataStime(Date dataStime) {
		this.dataStime = dataStime;
	}
	public Date getDataEtime() {
		return dataEtime;
	}
	public void setDataEtime(Date dataEtime) {
		this.dataEtime = dataEtime;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public Date getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Date getHissyschStartTime() {
		return hissyschStartTime;
	}
	public void setHissyschStartTime(Date hissyschStartTime) {
		this.hissyschStartTime = hissyschStartTime;
	}
	public Date getHissyschUpdateTime() {
		return hissyschUpdateTime;
	}
	public void setHissyschUpdateTime(Date hissyschUpdateTime) {
		this.hissyschUpdateTime = hissyschUpdateTime;
	}
	public Date getNowsyschStartTime() {
		return nowsyschStartTime;
	}
	public void setNowsyschStartTime(Date nowsyschStartTime) {
		this.nowsyschStartTime = nowsyschStartTime;
	}
	public Date getNowsyschUpdateTime() {
		return nowsyschUpdateTime;
	}
	public void setNowsyschUpdateTime(Date nowsyschUpdateTime) {
		this.nowsyschUpdateTime = nowsyschUpdateTime;
	}
	public Integer getInterVal() {
		return interVal;
	}
	public void setInterVal(Integer interVal) {
		this.interVal = interVal;
	}
	public Integer getSynchNum() {
		return synchNum;
	}
	public void setSynchNum(Integer synchNum) {
		this.synchNum = synchNum;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
}