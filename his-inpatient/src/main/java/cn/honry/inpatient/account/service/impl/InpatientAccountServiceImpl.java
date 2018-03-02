package cn.honry.inpatient.account.service.impl;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.InpatientAccount;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.inpatient.account.dao.InpatientAccountDAO;
import cn.honry.inpatient.account.service.InpatientAccountService;
import cn.honry.utils.ShiroSessionUtils;
@Service("inpatientAccountService")
@Transactional
@SuppressWarnings({"all"})
public class InpatientAccountServiceImpl implements InpatientAccountService{
	@Autowired
	private InpatientAccountDAO inpatientAccountDAO;

	@Override
	public void removeUnused(String id) {
		inpatientAccountDAO.del(id,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		OperationUtils.getInstance().conserve(id, "住院账户管理", "UPDATE", "T_INPATIENT_ACCOUNT", OperationUtils.LOGACTIONUPDATE);
	}

	@Override
	public InpatientAccount get(String id) {
		return inpatientAccountDAO.get(id);
	}

	@Override
	public void saveOrUpdate(InpatientAccount entity) {
		if(StringUtils.isBlank(entity.getId())){
			entity.setId(null);
			entity.setCreateUser("");
			entity.setCreateDept("");
			entity.setCreateTime(new Date());
			OperationUtils.getInstance().conserve(null, "住院账户管理", "INSERT_INTO", "T_INPATIENT_ACCOUNT", OperationUtils.LOGACTIONINSERT);
		}else{
			entity.setUpdateUser("");
			entity.setUpdateTime(new Date());
			OperationUtils.getInstance().conserve(entity.getId(), "住院账户管理", "UPDATE", "T_INPATIENT_ACCOUNT", OperationUtils.LOGACTIONUPDATE);
		}
		inpatientAccountDAO.save(entity);
	}

	@Override
	public InpatientAccount queryByMedical(String string) {
		return inpatientAccountDAO.queryByMedical(string);
	}

	@Override
	public void del(String id) {
		inpatientAccountDAO.removeById(id);
	}

	@Override
	public InpatientAccount queryByIdcardId(String idcardId) throws Exception {
		return inpatientAccountDAO.queryByIdcardId(idcardId);
	}	
}
