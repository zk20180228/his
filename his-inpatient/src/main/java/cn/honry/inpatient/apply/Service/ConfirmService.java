package cn.honry.inpatient.apply.Service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.InpatientCancelitemNow;
import cn.honry.base.service.BaseService;
import cn.honry.inpatient.apply.vo.ApplyVo;

public interface ConfirmService extends BaseService<InpatientCancelitemNow> {
	/**
	 * 查询药品退费信息的总条数
	 * @author  lyy
	 * @createDate： 2016年1月30日 下午4:04:49 
	 * @modifier lyy
	 * @modifyDate：2016年1月30日 下午4:04:49  
	 * @modifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	int getTatalDrugConfirm(ApplyVo entity) throws Exception;
	/**
	 * 查询药品退费信息
	 * @author  lyy
	 * @createDate： 2016年1月30日 下午4:05:36 
	 * @modifier lyy
	 * @modifyDate：2016年1月30日 下午4:05:36  
	 * @modifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	List<ApplyVo> getPageDrugConfirm(ApplyVo entity,String page,String rows) throws Exception;
	/**
	 * 查询非药品退费信息的总条数
	 * @author  lyy
	 * @createDate： 2016年1月30日 下午4:04:49 
	 * @modifier lyy
	 * @modifyDate：2016年1月30日 下午4:04:49  
	 * @modifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	int getTatalNotDrugConfirm(ApplyVo entity) throws Exception;
	/**
	 * 查询非药品退费信息
	 * @author  lyy
	 * @createDate： 2016年2月1日 上午10:01:32 
	 * @modifier lyy
	 * @modifyDate：2016年2月1日 上午10:01:32  
	 * @modifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	List<ApplyVo> getPageNotDrugConfirm(ApplyVo apply,String page,String rows) throws Exception;
	/**
	 * 退费通过
	 * @author  lyy
	 * @createDate： 2016年2月3日 上午11:55:22 
	 * @modifier lyy
	 * @modifyDate：2016年2月3日 上午11:55:22  
	 * @modifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	String confirmBack(String ids) throws Exception;
	
/****************************************************************   分割线       *************************************************************************************/

	/***
	 * 确认退费时对申请状态进行判断
	 * @Title: applyState 
	 * @author  WFJ
	 * @createDate ：2016年5月12日
	 * @param ids
	 * @return Map<String,Object>
	 * @version 1.0
	 */
	Map<String, Object> applyState(String[] ids);
	
	
	/***
	 * 退费操作
	 * @Title: applyConfirm 
	 * @author  WFJ
	 * @createDate ：2016年5月18日
	 * @param ids 已存在申请的id --- 适用于确认退费
	 *               在确认退费时，ids不可为null
	 * @param cancelList 申请的集合 --- 适用于直接退费
	 *               在直接退费是，设置ids为null
	 * @return void
	 * @version 1.0
	 * @throws Exception 
	 */
	void applyConfirm(String[] ids,List<InpatientCancelitemNow> cancelList) throws Exception;
	
	/***
	 * 退费操作 移动端调用
	 * @Title: applyConfirm 
	 * @author  WFJ
	 * @createDate ：2016年5月18日
	 * @param ids 已存在申请的id --- 适用于确认退费
	 *               在确认退费时，ids不可为null
	 * @param cancelList 申请的集合 --- 适用于直接退费
	 *               在直接退费是，设置ids为null
	 * @return void
	 * @version 1.0
	 * @throws Exception 
	 */
	void applyConfirm(String[] idsss, List<InpatientCancelitemNow> cancelList, String deptCode,String empJobNo) throws Exception;
}
