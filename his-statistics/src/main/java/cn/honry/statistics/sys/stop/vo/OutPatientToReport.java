package cn.honry.statistics.sys.stop.vo;

import java.util.List;
/***
 * 科室门诊停诊原因-用于报表打印父类
 */
public class OutPatientToReport {
	private List<OutPatientVo> itemList;
	private String firstData;
	private String endData;
	public String getEndData() {
		return endData;
	}

	public void setEndData(String endData) {
		this.endData = endData;
	}
	public String getFirstData() {
		return firstData;
	}

	public void setFirstData(String firstData) {
		this.firstData = firstData;
	}

	public List<OutPatientVo> getItemList() {
		return itemList;
	}

	public void setItemList(List<OutPatientVo> itemList) {
		this.itemList = itemList;
	}
	
}
