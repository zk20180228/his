package cn.honry.statistics.operation.operationDetails.vo;

import java.util.List;

/***
 * 手术耗材统计明细报表VO 父类
 * @Description:
 * @author:  hedong
 * @CreateDate: 2017年02月28日 
 * @version 1.0
 */
public class OperationDetailsVoToIReport {
	List<OperationDetailsVo> itemList;
	public List<OperationDetailsVo> getItemList() {
		return itemList;
	}
	public void setItemList(List<OperationDetailsVo> itemList) {
		this.itemList = itemList;
	}
}
