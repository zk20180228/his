package cn.honry.oa.activitiDept.service;

import java.util.List;

import cn.honry.base.bean.model.OaActivitiDept;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.service.BaseService;
import cn.honry.utils.TreeJson;

/**
 * 工作流科室维护
 * @author  zpty
 * @date 2017-8-13 15：40
 * @version 1.0
 */
public interface ActivitiDeptService extends BaseService<OaActivitiDept>{
	/**
	 * 查询不包含工作流科室在内的科室,从科室表中查询
	 * @Author: zpty
	 * @CreateDate: 2017年8月13日 下午7:35:26 
	 * @version: V1.0
	 * @throws:
	 * @return: List<SysDepartment> 返回值类型
	 */
	List<SysDepartment> queryDept();
	/**
	 * 查询工作流科室
	 * @Author: zpty
	 * @CreateDate: 2017年8月13日 下午7:35:26 
	 * @version: V1.0
	 * @throws:
	 * @return: List<OaActivitiDept> 返回值类型
	 */
	List<OaActivitiDept> queryActivitiDept();
	/**
	 * 初始化工作流科室
	 * @Author: zpty
	 * @CreateDate: 2017年8月13日 下午7:35:26 
	 * @version: V1.0
	 * @throws:
	 * @return: List<OaActivitiDept> 返回值类型
	 */
	void initialization(String[] butName);
	
	/**  
	 *  
	 * @Description：工作流科室分页查询-获得列表
	 * @Author：zpty
	 * @CreateDate：2017-8-13 下午03:45:17  
	 * @version: V1.0
	 * @throws:
	 * @return: List<OaActivitiDept> 返回值类型
	 */
	List<OaActivitiDept> getPage(String page, String rows,OaActivitiDept department);

	/**  
	 *  
	 * @Description：工作流科室分页查询-获得总条数
	 * @Author：zpty
	 * @CreateDate：2017-8-13 下午03:45:17  
	 * @version: V1.0
	 * @return: int 返回值类型
	 * 
	 */
	int getTotal(OaActivitiDept department);
	/**
	 * 保存工作流科室
	 * @Author: zpty
	 * @CreateDate: 2017年8月13日 下午7:35:26 
	 * @version: V1.0
	 * @throws:
	 * @return: void 返回值类型
	 */
	void saveActivitiDeptTree(String dId);
	
	/**
	 * 删除工作流科室
	 * @Author: zpty
	 * @CreateDate: 2017年8月13日 下午7:35:26 
	 * @version: V1.0
	 * @throws:
	 * @return: void 返回值类型
	 */
	void delActivitiDept(String dId);
	/**  
	 *  获取工作流科室树
	 * @Author：zpty
	 * @CreateDate：2017-8-21 上午11:21:55 
	 * @param： did 
	 * @version 1.0
	 *
	 */
	List<TreeJson> QueryTree(String did);
	/**  
	 *  判断code是否重复
	 * @Author：zpty
	 * @CreateDate：2017-8-21 上午11:21:55 
	 * @param： activitiDept 
	 * @version 1.0
	 *
	 */
	String searchDouble(OaActivitiDept activitiDept);
	/**  
	 *  保存工作流科室
	 * @Author：zpty
	 * @CreateDate：2017-8-21 上午11:21:55 
	 * @param： activitiDept 
	 * @version 1.0
	 *
	 */
	void saveActivitiDept(String dId, OaActivitiDept activitiDept);
}
