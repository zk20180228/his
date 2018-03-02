package cn.honry.outpatient.registerDeptManage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysMenufunction;
import cn.honry.outpatient.registerDeptManage.dao.RegisterDeptManageDAO;
import cn.honry.outpatient.registerDeptManage.service.RegisterDeptManageService;
/**  
 *  
 * @Description：  栏目功能
 * @Author：liudelin
 * @CreateDate：2015-7-28 下午02:00:00
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Service("registerDeptManageService")
@Transactional
@SuppressWarnings({ "all" })
public class RegisterDeptManageServiceImpl implements RegisterDeptManageService{

	@Autowired
	@Qualifier(value = "registerDeptManageDAO")
	private RegisterDeptManageDAO registerDeptManageDAO;
	
	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public SysDepartment get(String id) {
		SysDepartment sysDepartment = registerDeptManageDAO.get(id);
		return sysDepartment;
	}


	/**
	 * 获取记录条数
	 * @param entity 查询条件封装实体类
	 * @author  zpty
	 * @date 2015-11-16
	 * @version 1.0
	 * @return
	 * @throws Exception
	 */
	@Override
	public int getTotal(SysDepartment entity) {
		int total = registerDeptManageDAO.getTotal(entity);
		return total;
	}
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
	@Override
	public List<SysDepartment> DepartmentList(String string,String string2, SysDepartment entity) {
		List lst =  registerDeptManageDAO.query(string,string2,entity);
		return lst;
	}

	/**  
	 *  
	 * @Description：上移下移
	 * @Author：zpty
	 * @CreateDate：2015-11-16 上午5:30 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public void editOrder(String currentId, String otherId)throws Exception {
		SysDepartment current=registerDeptManageDAO.get(currentId);
		SysDepartment other=registerDeptManageDAO.get(otherId);
		Integer currentOrder=current.getDeptRegisterOrder();
		Integer otherOrder=other.getDeptRegisterOrder();
		current.setDeptRegisterOrder(otherOrder);
		other.setDeptRegisterOrder(currentOrder);
		registerDeptManageDAO.update(current);
		registerDeptManageDAO.update(other);
	}

	@Override
	public void saveOrUpdate(SysDepartment entity) {
		
	}

}
