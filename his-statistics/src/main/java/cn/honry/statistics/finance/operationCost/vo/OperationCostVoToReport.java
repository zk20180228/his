package cn.honry.statistics.finance.operationCost.vo;

import java.util.List;
/***
 * 住院手术费汇总报表VO 父类
 * @Description:
 * @author:  hedong
 * @CreateDate: 2017年03月01日 
 * @version 1.0
 */
public class OperationCostVoToReport {
    private List<OperationCostVo> itemList;

	public List<OperationCostVo> getItemList() {
		return itemList;
	}
	
	public void setItemList(List<OperationCostVo> itemList) {
		this.itemList = itemList;
	}
   
}
