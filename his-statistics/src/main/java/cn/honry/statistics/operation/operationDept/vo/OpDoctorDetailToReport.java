package cn.honry.statistics.operation.operationDept.vo;

import java.util.List;

/***
 * 手术科室汇总>手术医生明细报表vo 父类
 * @Description:
 * @author:  hedong
 * @CreateDate: 2017年02月28日 
 * @version 1.0
 */
public class OpDoctorDetailToReport {
	private List<OpDoctorDetailVo> itemList;

	public List<OpDoctorDetailVo> getItemList() {
		return itemList;
	}

	public void setItemList(List<OpDoctorDetailVo> itemList) {
		this.itemList = itemList;
	}
	
}
