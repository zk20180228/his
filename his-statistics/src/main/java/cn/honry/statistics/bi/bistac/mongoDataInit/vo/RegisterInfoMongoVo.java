package cn.honry.statistics.bi.bistac.mongoDataInit.vo;

import java.util.List;

import org.bson.types.ObjectId;

import cn.honry.inner.statistics.registerInfoGzltj.vo.RegisterInfoVo;

public class RegisterInfoMongoVo {
	private String date;
	private String deptCode;
	private RegisterInfoVo value;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public RegisterInfoVo getValue() {
		return value;
	}
	public void setValue(RegisterInfoVo value) {
		this.value = value;
	}
	
}
