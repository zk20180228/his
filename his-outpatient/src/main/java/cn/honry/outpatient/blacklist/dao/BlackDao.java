package cn.honry.outpatient.blacklist.dao;

import java.util.List;

import cn.honry.base.bean.model.EmployeeBlacklist;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.EntityDao;
import cn.honry.outpatient.blacklist.vo.EmployeeBlackListVo;
@SuppressWarnings({"all"})
public interface BlackDao extends EntityDao<EmployeeBlacklist>{
	
	/**  
	 *  
	 * @Description：   挂号员黑名单分页查询 - 查询列表
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-18 下午04:14:43  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-18 下午04:14:43  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<EmployeeBlackListVo> getPage(EmployeeBlacklist entity,SysEmployee sysEmployee, String page, String rows);

	/**  
	 *  
	 * @Description：   挂号员黑名单分页查询 - 统计总条数
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-18 下午04:14:43  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-18 下午04:14:43  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	int getTotal(EmployeeBlacklist entity,SysEmployee sysEmployee);
	/**
	 * @Description：  获取下拉员工名称
	 * @Author：lyy
	 * @CreateDate：2015-11-20 下午05:50:42  
	 * @Modifier：lyy
	 * @ModifyDate：2015-11-20 下午05:50:42  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<SysEmployee> queryEmpName(String name);
	

}
