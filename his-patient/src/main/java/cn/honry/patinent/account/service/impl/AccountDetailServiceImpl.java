package cn.honry.patinent.account.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.PatientAccountdetail;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.patinent.account.dao.AccountDetailDAO;
import cn.honry.patinent.account.service.AccountDetailService;
import cn.honry.utils.ShiroSessionUtils;

@Service("detailService")
@Transactional
@SuppressWarnings({ "all" })
public class AccountDetailServiceImpl implements AccountDetailService{
	/** 账户数据库操作类 **/
	@Autowired
	@Qualifier(value = "detailDAO")
	private AccountDetailDAO detailDAO;
	
	@Override
	public void removeUnused(String id) {
	}

	@Override
	public PatientAccountdetail get(String id) {
		return detailDAO.get(id);
	}

	@Override
	public void saveOrUpdate(PatientAccountdetail entity) {
		if(StringUtils.isBlank(entity.getId())){//保存		
			entity.setId(null);
			entity.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			entity.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getId());
			entity.setCreateTime(new Date());
			OperationUtils.getInstance().conserve(null, "患者账户管理", "INSERT_INTO", "T_PATIENT_ACCOUNTDETAIL", OperationUtils.LOGACTIONINSERT);
		}else{//修改
			entity.setUpdateUser("");
			entity.setUpdateTime(new Date());
			OperationUtils.getInstance().conserve(entity.getId(), "患者账户管理", "UPDATE", "T_PATIENT_ACCOUNTDETAIL", OperationUtils.LOGACTIONUPDATE);
		}
		detailDAO.save(entity);
	}

	@Override
	public List<PatientAccountdetail> getPage(PatientAccountdetail entity, String page, String rows,String accountId) {
		return detailDAO.getPage(entity, page, rows,accountId);
	}

	@Override
	public int getTotal(PatientAccountdetail entity,String accountId) {
		return detailDAO.getTotal(entity,accountId);
	}

	@Override
	public void del(String id) {
		detailDAO.del(id,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		OperationUtils.getInstance().conserve(id, "患者账户管理", "UPDATE", "T_PATIENT_ACCOUNTDETAIL", OperationUtils.LOGACTIONUPDATE);
	}

	@Override
	public void delByParentId(String id) {
		detailDAO.delByParentId(id);
	}
}
