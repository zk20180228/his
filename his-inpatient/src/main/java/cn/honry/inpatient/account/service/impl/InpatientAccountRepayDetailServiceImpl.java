package cn.honry.inpatient.account.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.InpatientAccountrepaydetail;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.inpatient.account.dao.InpatientAccountRepayDetailDAO;
import cn.honry.inpatient.account.service.InpatientAccountRepayDetailService;
import cn.honry.utils.ShiroSessionUtils;
@Service("inpatientAccountRepaydetailService")
@Transactional
@SuppressWarnings({"all"})
public class InpatientAccountRepayDetailServiceImpl implements InpatientAccountRepayDetailService{
	@Autowired
	private InpatientAccountRepayDetailDAO inpatientAccountRepaydetailDAO;

	@Override
	public void removeUnused(String id) {
		inpatientAccountRepaydetailDAO.del(id,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		OperationUtils.getInstance().conserve(id, "住院账户管理", "UPDATE", "T_INPATIENT_ACCOUNTREPAYDETAIL", OperationUtils.LOGACTIONUPDATE);
	}

	@Override
	public InpatientAccountrepaydetail get(String id) {
		return inpatientAccountRepaydetailDAO.get(id);
	}

	@Override
	public void saveOrUpdate(InpatientAccountrepaydetail entity) {
		if(StringUtils.isBlank(entity.getId())){
			entity.setId(null);
			entity.setCreateUser("");
			entity.setCreateDept("");
			entity.setCreateTime(new Date());
			OperationUtils.getInstance().conserve(null, "住院账户管理", "INSERT_INTO", "T_INPATIENT_ACCOUNTREPAYDETAIL", OperationUtils.LOGACTIONINSERT);
		}else{
			entity.setUpdateUser("");
			entity.setUpdateTime(new Date());
			OperationUtils.getInstance().conserve(entity.getId(), "住院账户管理", "UPDATE", "T_INPATIENT_ACCOUNTREPAYDETAIL", OperationUtils.LOGACTIONUPDATE);
		}
		inpatientAccountRepaydetailDAO.save(entity);
	}

	@Override
	public List<InpatientAccountrepaydetail> getPage(String page, String rows,
			InpatientAccountrepaydetail entity, String accountId, int ishis) {
		return inpatientAccountRepaydetailDAO.getPage(page,rows,entity, accountId,ishis);
	}

	@Override
	public int getTotal(InpatientAccountrepaydetail entity, String accountId,
			int ishis) {
		return inpatientAccountRepaydetailDAO.getTotal(entity, accountId,ishis);
	}

	@Override
	public void updateIshis(String accountId) {
		inpatientAccountRepaydetailDAO.updateIshis(accountId);
		OperationUtils.getInstance().conserve("pid = " + accountId, "住院账户管理", "UPDATE", "T_INPATIENT_ACCOUNTREPAYDETAIL", OperationUtils.LOGACTIONUPDATE);
	}

	@Override
	public void delByParentId(String id) {
		inpatientAccountRepaydetailDAO.delByParentId(id);
	}	
}
