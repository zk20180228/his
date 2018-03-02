package cn.honry.inpatient.bill.service;

import java.util.List;

import cn.honry.base.bean.model.DrugBilllist;
import cn.honry.base.service.BaseService;

public interface BilllistService extends BaseService<DrugBilllist> {

	/**
	 * @Description:查询列表
	 * @Author：  lt
	 * @CreateDate： 2015-8-28
	 * @param @param page
	 * @param @param rows
	 * @param @param billlistSerc
	 * @param @return   
	 * @return List<DrugBillclass>  
	 * @version 1.0
	**/
	List<DrugBilllist> getPage(String page, String rows, DrugBilllist billlistSerc);

	/**
	 * @Description:列表总条数
	 * @Author：  lt
	 * @CreateDate： 2015-8-28
	 * @param @param billlistSerc
	 * @param @return   
	 * @return int  
	 * @version 1.0
	**/
	int getTotal(DrugBilllist billlistSerc);

}
