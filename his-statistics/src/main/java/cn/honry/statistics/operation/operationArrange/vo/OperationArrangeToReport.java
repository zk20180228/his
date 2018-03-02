package cn.honry.statistics.operation.operationArrange.vo;

import java.util.List;
/***
 * 手术安排统计-用于报表打印父类
 * @Description:
 * @author: hedong
 * @CreateDate: 2017年3月1日 
 * @version 1.0
 */
public class OperationArrangeToReport {
	private List<OperationArrangeToReportSlave> itemList;

	public List<OperationArrangeToReportSlave> getItemList() {
		return itemList;
	}

	public void setItemList(List<OperationArrangeToReportSlave> itemList) {
		this.itemList = itemList;
	}
}
