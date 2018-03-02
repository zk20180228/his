package cn.honry.outpatient.blacklist.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.EmployeeBlacklist;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.inner.baseinfo.employee.dao.EmployeeInInterDAO;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.outpatient.blacklist.dao.BlackDao;
import cn.honry.outpatient.blacklist.service.BlackService;
import cn.honry.outpatient.blacklist.vo.EmployeeBlackListVo;
import cn.honry.utils.ShiroSessionUtils;
@Service("blackService")
@Transactional
@SuppressWarnings({ "all" })
public class BlackServiceImpl implements BlackService {

	@Autowired
	@Qualifier(value = "blackDAO")
	private BlackDao blackDAO;
	@Autowired
	@Qualifier(value = "employeeInInterDAO")
	private EmployeeInInterDAO employeeInInterDAO;
	
	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public EmployeeBlacklist get(String id) {
		return blackDAO.get(id);
	}

	@Override
	public void saveOrUpdate(EmployeeBlacklist entity) {
		
	}

	@Override
	public void saveOrUpdabalck(EmployeeBlacklist employeeBlacklist) {
		String userId = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String deptId = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
		if(StringUtils.isBlank(employeeBlacklist.getId())){//保存
			employeeBlacklist.setId(null);
			employeeBlacklist.setDel_flg(0);
			if(employeeBlacklist.getStop_flg()==null){
				employeeBlacklist.setStop_flg(0);

			}
			employeeBlacklist.setCreateUser(userId);
			employeeBlacklist.setCreateDept(deptId);
			employeeBlacklist.setCreateTime(new Date());
			OperationUtils.getInstance().conserve(null,"挂号员黑名单","INSERT INTO","T_EMPLOYEE_BLACKLIST",OperationUtils.LOGACTIONINSERT);
		}else{//修改
			employeeBlacklist.setUpdateUser(userId);
			employeeBlacklist.setUpdateTime(new Date());
			OperationUtils.getInstance().conserve(employeeBlacklist.getId(),"挂号员黑名单","UPDATE","T_EMPLOYEE_BLACKLIST",OperationUtils.LOGACTIONUPDATE);

		}
		blackDAO.save(employeeBlacklist); 
	}

	@Override
	public List<EmployeeBlacklist> getPage(String page, String rows,EmployeeBlacklist employeeBlacklist,SysEmployee sysEmployee) {
		List<EmployeeBlackListVo> list = blackDAO.getPage(employeeBlacklist,sysEmployee, page, rows);
		List<EmployeeBlacklist> blacklist = new ArrayList<EmployeeBlacklist>();
		EmployeeBlacklist black = null;
		SysEmployee emp = null;
		for (EmployeeBlackListVo e : list) {
			black = blackDAO.get(e.getBlackListid());
			emp = employeeInInterDAO.findEmpById(e.getEmployeeid());
			black.setDmployeeId(emp);
			blacklist.add(black);
		}
		return blacklist;
	}

	@Override
	public int getTotal(EmployeeBlacklist employeeBlacklist,SysEmployee sysEmployee) {
		return blackDAO.getTotal(employeeBlacklist,sysEmployee);
	}

	@Override
	public boolean save(String stackInfosJson) {
		return false;
	}

	@Override
	public void del(String id) {
		blackDAO.del(id,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		OperationUtils.getInstance().conserve(id,"挂号员黑名单","UPDATE","T_EMPLOYEE_BLACKLIST",OperationUtils.LOGACTIONDELETE);

	}
	/**
	 * @Description：  获取下拉员工名称
	 * @Author：lyy
	 * @CreateDate：2015-11-20 下午05:50:42  
	 * @Modifier：lyy
	 * @ModifyDate：2015-11-20 下午05:50:42  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<SysEmployee> queryEmpName(String name) {
		return blackDAO.queryEmpName(name);
	}

}
