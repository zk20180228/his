package cn.honry.outpatient.registerDeptManage.dao;

import java.util.List;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysMenufunction;
import cn.honry.base.dao.EntityDao;

/**  
 *  
 * @Description：  栏目功能
 * @Author：liudelin
 * @CreateDate：2015-7-28 下午02:00:00
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@SuppressWarnings({"all"})
public interface RegisterDeptManageDAO extends EntityDao<SysDepartment>{
	/**
	 * 获取记录条数
	 * @param entity 查询条件封装实体类
	 * @author  zpty
	 * @date 2015-11-16
	 * @version 1.0
	 * @return
	 * @throws Exception
	 */
	int getTotal(SysDepartment entity);
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
	List query(String string, String string2, SysDepartment entity);


}
