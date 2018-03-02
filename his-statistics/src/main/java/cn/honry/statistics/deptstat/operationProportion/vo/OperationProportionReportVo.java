package cn.honry.statistics.deptstat.operationProportion.vo;

import java.util.List;

/**
 * 手术占比统计打印报表父类
 * */
public class OperationProportionReportVo {
	
	private List<OperationProportionVo> itemList;
	
	public List<OperationProportionVo> getItemList() {
		return itemList;
	}
	public void setItemList(List<OperationProportionVo> itemList) {
		this.itemList = itemList;
	}

}
