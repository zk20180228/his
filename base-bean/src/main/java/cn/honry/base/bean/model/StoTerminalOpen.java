package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

/**
 * 配药台,发药窗开放模板维护
 * @author zpty
 * Date:2016/01/15 15:30
 */

public class StoTerminalOpen extends Entity {	
	/**所属科室号**/
	private String deptCode;
	/**模板编码**/
	private String templetCode;
	/**模板名称**/
	private String templetName;
	/**配药台编码（对应的特殊配药台编码）**/
	private String code;
	/**是否关闭标记: 0开放1关闭**/
	private Integer closeFlag;
	/**备注**/
	private String mark;
	
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getTempletCode() {
		return templetCode;
	}
	public void setTempletCode(String templetCode) {
		this.templetCode = templetCode;
	}
	public String getTempletName() {
		return templetName;
	}
	public void setTempletName(String templetName) {
		this.templetName = templetName;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Integer getCloseFlag() {
		return closeFlag;
	}
	public void setCloseFlag(Integer closeFlag) {
		this.closeFlag = closeFlag;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}

}