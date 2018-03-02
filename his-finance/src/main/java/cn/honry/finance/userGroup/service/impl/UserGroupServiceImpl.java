package cn.honry.finance.userGroup.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.FinanceUsergroup;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.finance.userGroup.dao.UserGroupDao;
import cn.honry.finance.userGroup.service.UserGroupService;
import cn.honry.finance.userGroup.vo.EmployeeGroupVo;
import cn.honry.inner.system.utli.OperationUtils;

@Service("userGroupService")
@Transactional
@SuppressWarnings({ "all" })
public class UserGroupServiceImpl implements UserGroupService{
	
	@Autowired
	@Qualifier(value = "userGroupDao")
	private UserGroupDao userGroupDao;
	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public FinanceUsergroup get(String id) {
		return userGroupDao.get(id);
	}

	@Override
	public void saveOrUpdate(FinanceUsergroup entity) {
		userGroupDao.save(entity);
	}

	@Override
	public List<FinanceUsergroup> queryUsergroup(
			FinanceUsergroup financeUsergroup)  throws Exception {
		return userGroupDao.queryUsergroup(financeUsergroup);
	}

	@Override
	public int getUsergroupCount(FinanceUsergroup financeUsergroup)  throws Exception {
		return userGroupDao.getUsergroupCount(financeUsergroup);
	}

	@Override
	public void delete(String ids)  throws Exception {
		userGroupDao.delete(ids);
		OperationUtils.getInstance().conserve(ids,"财务组管理","UPDATE","T_FINANCE_USERGROUP",OperationUtils.LOGACTIONDELETE);
	}

	@Override
	public void save(String ids, FinanceUsergroup financeUsergroup)  throws Exception {
		userGroupDao.save(ids, financeUsergroup);
		OperationUtils.getInstance().conserve(null,"财务组管理","INSERT INTO","T_FINANCE_USERGROUP",OperationUtils.LOGACTIONINSERT);
	}

	@Override
	public List<SysEmployee> findGroupEmployee(String groupName)  throws Exception {
		return userGroupDao.findGroupEmployee(groupName);
	}

	@Override
	public void update(String ids, FinanceUsergroup financeUsergroup,String oldGroupName) throws Exception  {
		userGroupDao.update(ids, financeUsergroup,oldGroupName);
		OperationUtils.getInstance().conserve(ids,"财务组管理","UPDATE","T_FINANCE_USERGROUP",OperationUtils.LOGACTIONUPDATE);
	}

	@Override
	public void delete(String employeeId, String groupName) throws Exception  {
		userGroupDao.delete(employeeId, groupName);
		OperationUtils.getInstance().conserve(null,"财务组管理","UPDATE","T_FINANCE_USERGROUP",OperationUtils.LOGACTIONDELETE);
	}

	@Override
	public FinanceUsergroup findGroup(String groupName)  throws Exception {
		return userGroupDao.findGroup(groupName);
	}

	@Override
	public List<FinanceUsergroup> findGroupAll(String groupName) throws Exception  {
		return userGroupDao.findGroupAll(groupName);
	}

	@Override
	public List<EmployeeGroupVo> queryGroupEmployee(String groupName) throws Exception  {
		return userGroupDao.queryGroupEmployee(groupName);
	}


}
