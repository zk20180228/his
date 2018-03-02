package cn.honry.inner.inpatient.inpatientOrder.vo;

import java.util.List;

import cn.honry.base.bean.model.InpatientExecdrug;
import cn.honry.base.bean.model.InpatientExecundrug;

public class MsgInInterVO {
	private InpatientOrderInInterVO orderVO;
	private String errorMsg;//错误或异常提示信息
	private String confirmMsg;//需要前面确认提示信息
	private String advId;//分解失败的医嘱id
	private Integer msgStatus;// 1-errorMsg 2-confirmMsg 3-执行记录
	private List<InpatientExecdrug> execDrugList=null;//未执行的药品执行记录
	private List<InpatientExecundrug> execUndrugList=null;//未执行的非药品执行记录
	public InpatientOrderInInterVO getOrderVO() {
		return orderVO;
	}
	public void setOrderVO(InpatientOrderInInterVO orderVO) {
		this.orderVO = orderVO;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public String getConfirmMsg() {
		return confirmMsg;
	}
	public void setConfirmMsg(String confirmMsg) {
		this.confirmMsg = confirmMsg;
	}
	public Integer getMsgStatus() {
		return msgStatus;
	}
	public String getAdvId() {
		return advId;
	}
	public void setAdvId(String advId) {
		this.advId = advId;
	}
	/**
	 * 
	 * @param msgStatus  1-错误或异常信息 2-确认信息
	 */
	public void setMsgStatus(Integer msgStatus) {
		this.msgStatus = msgStatus;
	}
	public List<InpatientExecdrug> getExecDrugList() {
		return execDrugList;
	}
	public void setExecDrugList(List<InpatientExecdrug> execDrugList) {
		this.execDrugList = execDrugList;
	}
	public List<InpatientExecundrug> getExecUndrugList() {
		return execUndrugList;
	}
	public void setExecUndrugList(List<InpatientExecundrug> execUndrugList) {
		this.execUndrugList = execUndrugList;
	}

	
}
