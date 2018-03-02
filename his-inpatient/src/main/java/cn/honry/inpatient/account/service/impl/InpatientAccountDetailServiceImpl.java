package cn.honry.inpatient.account.service.impl;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.InpatientAccountdetail;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.inpatient.account.dao.InpatientAccountDetailDAO;
import cn.honry.inpatient.account.service.InpatientAccountDetailService;
import cn.honry.utils.ShiroSessionUtils;
@Service("inpatientAccountdetailService")
@Transactional
@SuppressWarnings({"all"})
public class InpatientAccountDetailServiceImpl implements InpatientAccountDetailService{
	@Autowired
	private InpatientAccountDetailDAO inpatientAccountdetailDAO;

	@Override
	public void removeUnused(String id) {
		inpatientAccountdetailDAO.del(id,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		OperationUtils.getInstance().conserve(id, "住院账户管理", "UPDATE", "T_INPATIENT_ACCOUNTDETAIL", OperationUtils.LOGACTIONUPDATE);
	}

	@Override
	public InpatientAccountdetail get(String id) {
		return inpatientAccountdetailDAO.get(id);
	}

	@Override
	public void saveOrUpdate(InpatientAccountdetail entity) {
		if(StringUtils.isBlank(entity.getId())){
			entity.setId(null);
			entity.setCreateUser("");
			entity.setCreateDept("");
			entity.setCreateTime(new Date());
			OperationUtils.getInstance().conserve(null, "住院账户管理", "INSERT_INTO", "T_INPATIENT_ACCOUNTDETAIL", OperationUtils.LOGACTIONINSERT);
		}else{
			entity.setUpdateUser("");
			entity.setUpdateTime(new Date());
			OperationUtils.getInstance().conserve(entity.getId(), "住院账户管理", "UPDATE", "T_INPATIENT_ACCOUNTDETAIL", OperationUtils.LOGACTIONUPDATE);
		}
		inpatientAccountdetailDAO.save(entity);
	}

	@Override
	public void delByParentId(String id) {
		inpatientAccountdetailDAO.delByParentId(id);
		
	}	
}
