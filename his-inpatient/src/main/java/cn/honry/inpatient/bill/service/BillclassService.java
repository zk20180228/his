package cn.honry.inpatient.bill.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.DrugBillclass;
import cn.honry.base.bean.model.DrugBilllist;
import cn.honry.base.service.BaseService;

public interface BillclassService extends BaseService<DrugBillclass> {

	/**
	 * @Description:根据json串保存
	 * @Author：  lt
	 * @CreateDate： 2015-8-26
	 * @param @param infoJson   
	 * @return void  
	 * @version 1.0
	 * @param detailJson 
	 * @throws Exception 
	**/
	void saveOrUpdate(Map<String, String> parameterMap,String billJson) throws Exception;

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
	 * @throws Exception 
	**/
	List<DrugBillclass> getPage(String page, String rows, DrugBillclass billclassSerc) throws Exception;

	/**
	 * @Description:查询列表总条数
	 * @Author：  lt
	 * @CreateDate： 2015-8-26
	 * @param @param billclassSerc
	 * @param @return   
	 * @return int  
	 * @version 1.0
	 * @throws Exception 
	**/
	int getTotal(DrugBillclass billclassSerc) throws Exception;
	/**   
	*  
	* @description：查询修改的那一条
	* @author：ldl
	* @createDate：2015-10-19
	* @modifyRmk：  
	* @version 1.0
	 * @throws Exception 
	*/
	List<DrugBillclass> findBillEdit(String id) throws Exception;
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
	 * @Description:保存(子表)
	 * @Author：  ldl
	 * @CreateDate： 2015-10-20
	 * @param @throws Exception   
	 * @return void  
	 * @version 1.0
	 * @param billId 
	**/
	void saveOrUpdateInfo(String billInfoJson, String billId);
	/**
	 *  
	 * @Description：   根据id查询
	 * @Author：dh
	 * @CreateDate：2015-12-25 上午10:24:49  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<DrugBillclass> getPageList(String page, String rows, DrugBillclass drugBillclass);
	/**
	 *  查询摆药单分类代码
	 * @Description：查询摆药单分类代码  
	 * @Author：dh
	 * @CreateDate：2015-12-25 上午10:24:49  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<DrugBillclass> getBillclassCode();
	/**
	 * 
	 * 
	 * <p>根据摆药单代码判断输入的摆药单是否重复 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月5日 上午10:21:52 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月5日 上午10:21:52 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param Code
	 * @return:
	 *
	 */
	List<DrugBillclass> queryDrugBillclassCode(String Code);
}
