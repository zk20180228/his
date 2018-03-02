package cn.honry.patinent.account.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.PatientAccount;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.patinent.account.dao.AccountDAO;
import cn.honry.patinent.account.service.AccountService;
import cn.honry.utils.ShiroSessionUtils;

@Service("accountService")
@Transactional
@SuppressWarnings({ "all" })
public class AccountServiceImpl implements AccountService{
	/** 账户数据库操作类 **/
	@Autowired
	@Qualifier(value = "accountDAO")
	private AccountDAO accountDAO;
	
	//物理删除
	@Override
	public void removeUnused(String id) {
		accountDAO.removeById(id);
	}

	@Override
	public PatientAccount get(String id) {
		return accountDAO.get(id);
	}

	@Override
	public void saveOrUpdate(PatientAccount entity) {
		if(StringUtils.isBlank(entity.getId())){//保存		
			entity.setId(null);
			entity.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			entity.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getId());
			entity.setCreateTime(new Date());
			OperationUtils.getInstance().conserve(null, "患者账户管理", "INSERT_INTO", "T_PATIENT_ACCOUNT", OperationUtils.LOGACTIONINSERT);
		}else{//修改
			entity.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			entity.setUpdateTime(new Date());
			OperationUtils.getInstance().conserve(entity.getId(), "患者账户管理", "UPDATE", "T_PATIENT_ACCOUNT", OperationUtils.LOGACTIONUPDATE);
		}
		accountDAO.save(entity);
	}

	@Override
	public List<PatientAccount> getPage(String page, String rows,
			PatientAccount entity) {
		return accountDAO.getPage(entity, page, rows);
	}

	@Override
	public int getTotal(PatientAccount entity) {
		return accountDAO.getTotal(entity);
	}

	@Override
	public void del(String id) {
		accountDAO.del(id,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		OperationUtils.getInstance().conserve(id, "患者账户管理", "UPDATE", "T_PATIENT_ACCOUNT", OperationUtils.LOGACTIONUPDATE);
	}

	@Override
	public PatientAccount queryByIdcardId(String idcardId) {
		return accountDAO.queryByIdcardId(idcardId);
	}

	@Override
	public PatientAccount queryById(String id) {
		return accountDAO.queryById(id);
	}

	/**  
	 *  
	 * @Description：   根据病历号获得患者账户
	 * @Author：aizhonghua
	 * @CreateDate：2015-11-5 下午02:37:20  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-11-5 下午02:37:20  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public PatientAccount getAccountByMedicalrecord(String caseNo) {
		return accountDAO.getAccountByMedicalrecord(caseNo);
	}
}
