package cn.honry.base.bean.model;

import java.sql.Clob;
import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**  
 * @className：RecordToHISException.java 
 * @Author：hedong
 * @CreateDate：2017-03-13
 * @version 1.0
 * @remark:用于平台异常信息的记录
 */
public class RecordToHIASException extends Entity implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 模块标识  如 ZYSF_HSZSF 即代表的是住院收费_护士站收费 模块，如果模块层次较深 在ZYSF_HSZSF_后继续添加。
	 */
	private String code;
	/**
	 * 模块名称 如住院收费_护士站收费 
	 */
	private String codeName;
	/**
	 * 告警级别  共分四个级别  1>2>3>4
	 */
	private String warnLevel;
	/**
	 * 告警类别  0：业务告警，1：平台告警
	 */
	private String warnType;
	/**
	 * 告警时间 new
	 */
	private Date warnDate;
	/**
	 * 开始处理时间 new
	 */
	private Date processTime;
	/**
	 * 处理状态     0:未处理（默认值） ，1：处理中，2：处理完成  
	 */
	private String processStatus;
	
	/**
	 * 处理完成时间 new
	 */
	private Date finishedTime;
	/**
	 * 异常详细信息
	 */
	private Clob msg;
	
	private Integer version;
	public RecordToHIASException(){
	}
	/**
	 * @param code      模块标识  如 ZYSF_HSZSF 即代表的是住院收费_护士站收费 模块，如果模块层次较深可在ZYSF_HSZSF_ 后继续添加。
	 * @param codeName  模块名称 如住院收费_护士站收费 
	 * @param warnLevel 告警级别  共分四个级别  1>2>3>4
	 * @param warnType  告警类别  0：业务告紧，1：平台告警
	 * @param warnDate  告警时间
	 */
	public RecordToHIASException(String code,String codeName,String warnLevel,String warnType,Date warnDate){
		this.code=code;
		this.warnLevel=warnLevel;
		this.warnType=warnType;
		this.codeName=codeName;
		this.warnDate=warnDate;
	}
	/**
	 * @param code      模块标识  如 ZYSF_HSZSF 即代表的是住院收费_护士站收费 模块，如果模块层次较深可在ZYSF_HSZSF_ 后继续添加。
	 * @param codeName  模块名称 如住院收费_护士站收费 
	 * @param warnLevel 告警级别  共分四个级别  1>2>3>4
	 * @param warnType  告警类别  0：业务告警，1：平台告警
	 */
	public RecordToHIASException(String code,String codeName,String warnLevel,String warnType){
		this.code=code;
		this.warnLevel=warnLevel;
		this.warnType=warnType;
		this.codeName=codeName;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public String getCodeName() {
		return codeName;
	}
	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}
	public Date getWarnDate() {
		return warnDate;
	}
	public void setWarnDate(Date warnDate) {
		this.warnDate = warnDate;
	}
	public Date getProcessTime() {
		return processTime;
	}
	public void setProcessTime(Date processTime) {
		this.processTime = processTime;
	}
	public Date getFinishedTime() {
		return finishedTime;
	}
	public void setFinishedTime(Date finishedTime) {
		this.finishedTime = finishedTime;
	}
	public String getWarnLevel() {
		return warnLevel;
	}
	public void setWarnLevel(String warnLevel) {
		this.warnLevel = warnLevel;
	}
	public String getWarnType() {
		return warnType;
	}
	public void setWarnType(String warnType) {
		this.warnType = warnType;
	}
	public String getProcessStatus() {
		return processStatus;
	}
	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
	}
	public Clob getMsg() {
		return msg;
	}
	public void setMsg(Clob msg) {
		this.msg = msg;
	}
	
	
}
