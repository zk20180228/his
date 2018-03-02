package cn.honry.outpatient.updateStack.service;

import java.util.List;

import cn.honry.base.bean.model.BusinessFrequency;
import cn.honry.base.bean.model.BusinessStack;
import cn.honry.base.service.BaseService;
import cn.honry.outpatient.updateStack.vo.StackAndStockInfoVo;
import cn.honry.utils.TreeJson;
/**
 * 组套接口业务层
 * @author  lyy
 * @createDate： 2016年4月12日 上午11:22:48 
 * @modifier lyy
 * @modifyDate：2016年4月12日 上午11:22:48
 * @param：    
 * @modifyRmk：  
 * @version 1.0
 */
public interface UpdateStackService extends BaseService<BusinessStack> {

	/**
	 * 组套树
	 * @author  zhenglin
	 * @createDate： 
	 * @modifier lyy
	 * @modifyDate：2016年4月11日 上午9:37:32
	 * @param： id 组套的id deptId 登录科室的id userid 登录人的id drugType 组套类型  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<TreeJson> stackTree(String id,String deptId,String userId,String drugType,String remark);
	/**
	 * 渲染频次
	 * @author  zhenglin
	 * @createDate： 
	 * @modifier lyy
	 * @modifyDate：2016年4月11日 下午3:10:41
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<BusinessFrequency> getFreq();
	/**
	 * 查看组套药品详情
	 * @author  zhenglin
	 * @createDate： 
	 * @modifier lyy
	 * @modifyDate：2016年4月11日 下午2:59:13
	 * @param： id 组套的编号   drugstoreId 诊断的选择的药房  feelType 是否是收费类型
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<StackAndStockInfoVo> queryStackInfoById(String id,String feelType);
}
