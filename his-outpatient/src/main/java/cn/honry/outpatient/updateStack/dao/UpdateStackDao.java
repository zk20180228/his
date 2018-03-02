package cn.honry.outpatient.updateStack.dao;

import java.util.List;

import cn.honry.base.bean.model.BusinessFrequency;
import cn.honry.base.bean.model.BusinessStack;
import cn.honry.base.dao.EntityDao;
import cn.honry.outpatient.updateStack.vo.StackAndStockInfoVo;

public interface UpdateStackDao extends EntityDao<BusinessStack> {

	/**
	 * 查询组套主表
	 * @author  zhenglin
	 * @createDate： 
	 * @modifier lyy
	 * @modifyDate：2016年4月12日 上午9:42:29
	 * @param：   stack 组套实体 id 组套来源  deptId 登录科室 userId 登录人   stackObject(组套对象: 1是财务用，2是医嘱用)  remark(门诊还是住院，收费不区分，医嘱区分)
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<BusinessStack> getStackInfo(BusinessStack stack,String id,String deptId,String userId,String stackObject,String remark,String root);
	/**
	 * 渲染频次
	 * @author  zhenglin
	 * @createDate： 
	 * @modifier lyy
	 * @modifyDate：2016年4月12日 下午12:00:30
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<BusinessFrequency> getFreq();
	/**
	 * 查看药品组套详情
	 * @author  zhenglin
	 * @createDate： 
	 * @modifier lyy
	 * @modifyDate：2016年4月11日 下午2:59:13
	 * @param： id 组套的编号   drugstoreId 诊断的选择的药房 feelType 是否是收费类型
	 * @modifyRmk：  
	 */
	List<StackAndStockInfoVo> queryStackInfoById(String id,String feelType);
}
