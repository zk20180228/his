package cn.honry.inpatient.bill.dao;

import java.util.List;

import cn.honry.base.bean.model.DrugBillclass;
import cn.honry.base.bean.model.DrugBilllist;
import cn.honry.base.dao.EntityDao;

public interface BillclassDAO extends EntityDao<DrugBillclass>{
	/**
	 * @Description:查询列表
	 * @Author：  lt
	 * @CreateDate： 2015-8-26
	 * @param @param page
	 * @param @param rows
	 * @param @param billclassSerc
	 * @param @return   
	 * @return List<DrugBillclass>  
	 * @version 1.0
	**/
	List<DrugBillclass> getPage(String page, String rows, DrugBillclass billclassSerc);
	
	/**
	 * @Description:查询列表总条数
	 * @Author：  lt
	 * @CreateDate： 2015-8-26
	 * @param @param billclassSerc
	 * @param @return   
	 * @return int  
	 * @version 1.0
	**/
	int getTotal(DrugBillclass billclassSerc);
	/**   
	*  
	* @description：查询修改的那一条
	* @author：ldl
	* @createDate：2015-10-19
	* @modifyRmk：  
	* @version 1.0
	*/
	List<DrugBillclass> findBillEdit(String id);
	/**   
	*  
	* @description：查询修改的那一条(子表)
	* @author：ldl
	* @createDate：2015-10-20
	* @modifyRmk：  
	* @version 1.0
	*/
	List<DrugBilllist> findBillInfoEdit(String id);
	/**
	 * @Author： dh
	 * @CreateDate： 2015-12-25
	 * @param @param DrugBillclass
	 * @param @return   
	 * @return List<DrugBillclass>  
	 * @version 1.0
	**/
	List<DrugBillclass> getPageList(String page, String rows, DrugBillclass drugBillclass);
	/**
	 * 查询摆药单分类代码
	 * @Author： dh
	 * @CreateDate： 2015-12-25
	 * @param @param DrugBillclass
	 * @param @return   
	 * @return List<DrugBillclass>  
	 * @version 1.0
	**/
	List<DrugBillclass> getBillclassCode();
	/**
	 * @Description:查询列表总条数
	 * @Author： dh
	 * @CreateDate： 2015-8-26
	 * @param @param billclassSerc
	 * @param @return   
	 * @return int  
	 * @version 1.0
	**/
	int getTotalList(DrugBillclass billclassSerc);
	/**
	 * 
	 * 
	 * <p>根据摆药单代码判断输入的摆药单是否重复 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月5日 上午10:23:26 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月5日 上午10:23:26 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param Code
	 * @return:
	 *
	 */
	List<DrugBillclass> queryDrugBillclassCode(String Code);
}
