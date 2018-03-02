package cn.honry.inner.outpatient.medicineList.vo;

import cn.honry.base.bean.model.StoTerminal;
import cn.honry.base.bean.model.StoTerminalSpe;

public class SpeNalVo implements java.io.Serializable {

	//配药台
	private String tCode;
	//药房
	private String deptCode;
	//特殊项目代码
	private String itemCode;
	//项目类别
	private Integer itemType;
	
	public SpeNalVo() {
		super();
	}
	public SpeNalVo(StoTerminalSpe terminalSpe) {
		super();
		if(terminalSpe!=null){
			this.tCode = terminalSpe.getCode();
			this.deptCode = terminalSpe.getDeptid();
			this.itemCode = terminalSpe.getItemCode();
			this.itemType = terminalSpe.getItemType();
		}
	}
	public Integer getItemType() {
		return itemType;
	}
	public void setItemType(Integer itemType) {
		this.itemType = itemType;
	}
	
	public String gettCode() {
		return tCode;
	}
	public void settCode(String tCode) {
		this.tCode = tCode;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	
}
