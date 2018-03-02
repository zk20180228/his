package cn.honry.outpatient.registerDeptManage.service;

import java.util.List;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.service.BaseService;
/**  
 *  
 * @Description：  栏目功能
 * @Author：liudelin
 * @CreateDate：2015-7-28 下午02:00:00
 * @ModifyRmk：  
 * @version 1.0
 *
 */
public interface RegisterDeptManageService extends BaseService<SysDepartment>{
	/**
	 * 获取记录条数
	 * @param entity 查询条件封装实体类
	 * @author  zpty
	 * @date 2015-11-16
	 * @version 1.0
	 * @return
	 * @throws Exception
	 */
	int getTotal(SysDepartment sysDepartment);
	/**
	 * 列表查询
	 * @param page 页码
	 * @param rows 显示列表数据
	 * @param entity 查询条件封装实体类
	 * @author  zpty
	 * @date 2015-11-16
	 * @version 1.0
	 * @param string2 
	 * @param string 
	 * @return
	 * @throws Exception
	 */
	List<SysDepartment> DepartmentList(String string, String string2,SysDepartment sysDepartment);

	/**  
	 *  
	 * @Description：上移下移
	 * @Author：zpty
	 * @CreateDate：2015-11-16 上午5:30 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	void editOrder(String currentId,String otherId)throws Exception;
	

}
