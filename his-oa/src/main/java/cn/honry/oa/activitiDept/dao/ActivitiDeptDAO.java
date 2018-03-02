package cn.honry.oa.activitiDept.dao;

import java.util.List;

import cn.honry.base.bean.model.Information;
import cn.honry.base.bean.model.MApkVersion;
import cn.honry.base.bean.model.OaActivitiDept;
import cn.honry.base.bean.model.OaPortalWidget;
import cn.honry.base.bean.model.OaTaskInfo;
import cn.honry.base.bean.model.OaUserPortal;
import cn.honry.base.bean.model.Schedule;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.SysInfo;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.EntityDao;
/**
 * 工作流科室维护
 * @author  zpty
 * @date 2017-8-13 15：40
 * @version 1.0
 */
@SuppressWarnings({"all"})
public interface ActivitiDeptDAO extends EntityDao<OaActivitiDept>{
	/**  
	 * 
	 * 查询不包含流程科室在内的科室,从科室表中查询
	 * @Author: zpty
	 * @CreateDate: 2017年8月13日 上午11:45:23 
	 * @version: V1.0
	 * @return:
	 * @throws:
	 * @return: List<SysDepartment> 返回值类型
	 *
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
	 * 删除工作流科室
	 * @Author: zpty
	 * @CreateDate: 2017年8月13日 下午7:35:26 
	 * @version: V1.0
	 * @throws:
	 * @return: void 返回值类型
	 */
	void delActivitiDept();
	/**  
	 * 
	 * 查询所有的科室,从科室表中查询
	 * @Author: zpty
	 * @CreateDate: 2017年8月13日 上午11:45:23 
	 * @version: V1.0
	 * @return:
	 * @throws:
	 * @return: List<SysDepartment> 返回值类型
	 *
	 */
	List<SysDepartment> queryAllDept();

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
	 *  
	 * @Description：通过科室ID查询出科室
	 * @Author：zpty
	 * @CreateDate：2017-8-13 下午03:45:17  
	 * @version: V1.0
	 * @return: SysDepartment 返回值类型
	 * 
	 */
	SysDepartment queryDeptById(String dIds);
	/**  
	 * 
	 * 获取所有工作流科室
	 * @Author: zpty
	 * @CreateDate: 2017年8月21日 上午10:26:01 
	 * @version: V1.0
	 * @throws:
	 * @return: 
	 *
	 */
	List<OaActivitiDept> queryAllTreeDept();
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
	 *  物理删除工作流科室
	 * @Author：zpty
	 * @CreateDate：2017-8-21 上午11:21:55 
	 * @param： dIds 
	 * @version 1.0
	 *
	 */
	void delete(String dId);
	
}
