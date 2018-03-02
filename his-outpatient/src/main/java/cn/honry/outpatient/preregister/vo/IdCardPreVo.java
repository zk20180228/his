package cn.honry.outpatient.preregister.vo;

import java.util.Date;

public class IdCardPreVo {

	//姓名
	private String voPname;
	//性别
	private Integer voPsex;
	//年龄
	private Integer voPage;
	//年龄单位
	private String voPageUnit;
	//证件类型
	private String voPtype;
	//出生日期
	private Date voPdata;
	//证件号码
	private String voPtypeNo;
	//通讯地址
	private String voPaddrss;
	//电话号码
	private String voPphone;
	//病例号
	private String voPmedicalrecordId;
	//就诊卡号
	private String idCardNo;
	//建卡时间
	private Date cIdCardNoTime;
	
	
	public Date getcIdCardNoTime() {
		return cIdCardNoTime;
	}
	public void setcIdCardNoTime(Date cIdCardNoTime) {
		this.cIdCardNoTime = cIdCardNoTime;
	}
	public String getVoPname() {
		return voPname;
	}
	public void setVoPname(String voPname) {
		this.voPname = voPname;
	}
	public Integer getVoPsex() {
		return voPsex;
	}
	public void setVoPsex(Integer voPsex) {
		this.voPsex = voPsex;
	}
	public Integer getVoPage() {
		return voPage;
	}
	public void setVoPage(Integer voPage) {
		this.voPage = voPage;
	}
	
	public String getVoPageUnit() {
		return voPageUnit;
	}
	public void setVoPageUnit(String voPageUnit) {
		this.voPageUnit = voPageUnit;
	}
	public String getVoPtype() {
		return voPtype;
	}
	public void setVoPtype(String voPtype) {
		this.voPtype = voPtype;
	}
	public String getVoPtypeNo() {
		return voPtypeNo;
	}
	public void setVoPtypeNo(String voPtypeNo) {
		this.voPtypeNo = voPtypeNo;
	}
	public String getVoPaddrss() {
		return voPaddrss;
	}
	public void setVoPaddrss(String voPaddrss) {
		this.voPaddrss = voPaddrss;
	}
	public String getVoPphone() {
		return voPphone;
	}
	public void setVoPphone(String voPphone) {
		this.voPphone = voPphone;
	}
	public String getVoPmedicalrecordId() {
		return voPmedicalrecordId;
	}
	public void setVoPmedicalrecordId(String voPmedicalrecordId) {
		this.voPmedicalrecordId = voPmedicalrecordId;
	}
	public String getIdCardNo() {
		return idCardNo;
	}
	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}
	public Date getVoPdata() {
		return voPdata;
	}
	public void setVoPdata(Date voPdata) {
		this.voPdata = voPdata;
	}
	
	
}
