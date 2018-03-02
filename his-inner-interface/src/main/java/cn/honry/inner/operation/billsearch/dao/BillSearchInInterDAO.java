package cn.honry.inner.operation.billsearch.dao;

import java.util.List;

import cn.honry.base.bean.model.DepartmentContact;
import cn.honry.base.bean.model.DrugBillclass;
import cn.honry.base.dao.EntityDao;
import cn.honry.inner.operation.billsearch.vo.OperationBillingInfoVo;

/**   
* @className：摆药单查询
* @description：  摆药单查询 dao
* @author：tangfeishuai
* @createDate：2016-5-30 上午10:52:19  
* @modifier：tangfeishuai
* @modifyDate：2016-5-30 上午10:52:19  
* @modifyRmk：  
* @version 1.0
*/
@SuppressWarnings({"all"}) 
public interface BillSearchInInterDAO extends EntityDao<OperationBillingInfoVo>{
	
	/**   
	     根据id 查询登录病区关联的科室
	* @author：tangfeishuai
	* @createDate：2016-6-12 上午10:52:19  
	* @modifier：tangfeishuai
	* @modifyDate：2016-6-12 上午10:52:19  
	* @modifyRmk：  
	* @version 1.0
	*/
	public List<DepartmentContact> getDepConByPid(String pid);
	
	/**   
	    查询医院维护的所有摆药单分类
	* @author：tangfeishuai
	* @createDate：2016-6-12 上午10:52:19  
	* @modifier：tangfeishuai
	* @modifyDate：2016-6-12 上午10:52:19  
	* @modifyRmk：  
	* @version 1.0
	*/
	public List<DrugBillclass> getDrugBillclass(); 
	
	
}
