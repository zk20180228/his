package cn.honry.inpatient.bill.dao;

import java.util.List;

import cn.honry.base.bean.model.DrugBilllist;
import cn.honry.base.dao.EntityDao;

public interface BilllistDAO extends EntityDao<DrugBilllist>{
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

	/**
	 * @Description:根据主表id删除子表
	 * @Author：  lt
	 * @CreateDate： 2015-8-29
	 * @param @param id   
	 * @return void  
	 * @version 1.0
	**/
	void deleteBypid(String id);
	/**
	 * 保存
	 */
	void saveStatesObject(Object object);
}
