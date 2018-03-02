package cn.honry.outpatient.blacklist.service;

import java.util.List;

import cn.honry.base.bean.model.EmployeeBlacklist;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.service.BaseService;

/**  
 *  
 * @className：BlackService 
 * @Description：  挂号员黑名单Service 
 * @Author：aizhonghua
 * @CreateDate：2015-6-18 下午04:14:18  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-18 下午04:14:18  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
public interface BlackService extends BaseService<EmployeeBlacklist>{
	
	/**  
	 *  
	 * @Description：   挂号员黑名单添加&修改
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-18 下午04:14:43  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-18 下午04:14:43  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	void saveOrUpdabalck(EmployeeBlacklist employeeBlacklist);
	
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
	List<EmployeeBlacklist> getPage(String page, String rows,EmployeeBlacklist employeeBlacklist,SysEmployee sysEmployee);

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
	int getTotal(EmployeeBlacklist employeeBlacklist,SysEmployee sysEmployee);
	
	/**  
	 *  
	 * @Description：   挂号员黑名单保存
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-18 下午04:14:43  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-18 下午04:14:43  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	public boolean save( String stackInfosJson);
	
	/**  
	 *  
	 * @Description：   挂号员黑名单删除
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-18 下午04:14:43  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-18 下午04:14:43  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	void del(String id);
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
