package cn.honry.inpatient.delivery.service;

import java.util.List;

import cn.honry.base.bean.model.DrugApplyout;
import cn.honry.base.bean.model.DrugApplyoutNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.service.BaseService;
	/**
	 * 取消发送业务层
	 * @author  lyy
	 * @createDate： 2015年12月30日 下午5:37:34 
	 * @modifier lyy
	 * @modifyDate：2015年12月30日 下午5:37:34  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	public interface CancelSendService extends BaseService<DrugApplyout> {
	/**
	 * 查询所有的药房的科室
	 * @author  lyy
	 * @createDate： 2015年12月31日 上午10:42:23 
	 * @modifier lyy
	 * @modifyDate：2015年12月31日 上午10:42:23  
	 * @modifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	 List<SysDepartment> queryDept() throws Exception;
	 /**
	  * 分页查询列表list
	  * @author  lyy
	  * @createDate： 2015年12月31日 下午3:14:00 
	  * @modifier lyy
	  * @modifyDate：2015年12月31日 下午3:14:00  
	  * @modifyRmk：  entity 出库申请表  page 分页总页数  rows 总条数
	  * @version 1.0
	 * @throws Exception 
	  */
	 List<DrugApplyoutNow> getPageDrugApply(DrugApplyoutNow entity,String page,String rows) throws Exception;
	 /**
	  * 分页查询总条数
	  * @author  lyy
	  * @createDate： 2016年4月19日 下午5:41:46 
	  * @modifier lyy
	  * @modifyDate：2016年4月19日 下午5:41:46
	  * @param：    entity 出库申请表
	  * @modifyRmk：  
	  * @version 1.0
	 * @throws Exception 
	  */
	 int getTotalDrugApply(DrugApplyoutNow entity) throws Exception;
	 /**
	  * 取消发送
	  * @author  lyy
	  * @createDate： 2016年1月4日 下午2:00:09 
	  * @modifier lyy
	  * @modifyDate：2016年1月4日 下午2:00:09  
	  * @modifyRmk：  id 出库申请表主键id数据
	  * @version 1.0
	 * @throws Exception 
	  */
	 String editUpdate(String id) throws Exception;

}
