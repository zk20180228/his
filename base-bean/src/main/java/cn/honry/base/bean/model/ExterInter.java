package cn.honry.base.bean.model;

import java.io.Serializable;
import java.util.Date;


public class ExterInter implements Serializable {
	private static final long serialVersionUID = 1L;
	/**主键**/
	private String id;
	/**接口编码**/
	private String code;
	/**接口名称**/
	private String name;
	/**接口服务**/
	private String serve;
	/**接口方式（json,webService,xml,view）**/
	private String way;
	/**当前方式**/
	private String curWay;
	/**接口读写0只读1只写2读写**/
	private Integer rwJuri;
	/**接口调用间隔**/
	private Integer callSapce;
	/**接口调用间隔单位S秒M分H时D天W周**/
	private String callUnit;
	/**是否安全认证0是1否**/
	private Integer isAuth;
	/**认证有效期**/
	private Integer authVali;
	/**认证有效期单位M分H时D天W周**/
	private String authUnit;
	/**认证有效期开始时间**/
	private Date authStime;
	/**开始时间格式化**/
	private String authStimeSDF;
	/**认证有效期结束时间**/
	private Date authEtime;
	/**结束时间格式化**/
	private String authEtimeSDF;
	/**状态0正常1停用**/
	private Integer state;
	/**备注**/
	private String remarks;
	/**频次**/
	private String frequency;
	/**服务代码**/
	private String serveCode;
	/**执行sql**/
	private String implementSql;
	/**参数字段**/
	private String parameterField;
	/**厂商编号**/
	private String firmCode;
	
	public String getParameterField() {
		return parameterField;
	}
	public void setParameterField(String parameterField) {
		this.parameterField = parameterField;
	}
	public String getFirmCode() {
		return firmCode;
	}
	public void setFirmCode(String firmCode) {
		this.firmCode = firmCode;
	}
	public String getImplementSql() {
		return implementSql;
	}
	public void setImplementSql(String implementSql) {
		this.implementSql = implementSql;
	}
	public String getAuthStimeSDF() {
		return authStimeSDF;
	}
	public void setAuthStimeSDF(String authStimeSDF) {
		this.authStimeSDF = authStimeSDF;
	}
	public String getAuthEtimeSDF() {
		return authEtimeSDF;
	}
	public void setAuthEtimeSDF(String authEtimeSDF) {
		this.authEtimeSDF = authEtimeSDF;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public String getServeCode() {
		return serveCode;
	}
	public void setServeCode(String serveCode) {
		this.serveCode = serveCode;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
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
	public String getServe() {
		return serve;
	}
	public void setServe(String serve) {
		this.serve = serve;
	}
	public String getWay() {
		return way;
	}
	public void setWay(String way) {
		this.way = way;
	}
	public String getCurWay() {
		return curWay;
	}
	public void setCurWay(String curWay) {
		this.curWay = curWay;
	}
	public Integer getRwJuri() {
		return rwJuri;
	}
	public void setRwJuri(Integer rwJuri) {
		this.rwJuri = rwJuri;
	}
	public Integer getCallSapce() {
		return callSapce;
	}
	public void setCallSapce(Integer callSapce) {
		this.callSapce = callSapce;
	}
	public String getCallUnit() {
		return callUnit;
	}
	public void setCallUnit(String callUnit) {
		this.callUnit = callUnit;
	}
	public Integer getIsAuth() {
		return isAuth;
	}
	public void setIsAuth(Integer isAuth) {
		this.isAuth = isAuth;
	}
	public Integer getAuthVali() {
		return authVali;
	}
	public void setAuthVali(Integer authVali) {
		this.authVali = authVali;
	}
	public String getAuthUnit() {
		return authUnit;
	}
	public void setAuthUnit(String authUnit) {
		this.authUnit = authUnit;
	}
	public Date getAuthStime() {
		return authStime;
	}
	public void setAuthStime(Date authStime) {
		this.authStime = authStime;
	}
	public Date getAuthEtime() {
		return authEtime;
	}
	public void setAuthEtime(Date authEtime) {
		this.authEtime = authEtime;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
}
