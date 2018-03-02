package cn.honry.inpatient.outBalance.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.InpatientBalancePay;
import cn.honry.inpatient.outBalance.dao.InpatientBalancePayListDAO;
import cn.honry.inpatient.outBalance.service.InpatientBalancePayListService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.ShiroSessionUtils;

@Service("inpatientBalancePayListService")
@Transactional
@SuppressWarnings({"all"})
public class InpatientBalancePayListServiceImpl implements InpatientBalancePayListService{
		@Autowired
		@Qualifier(value = "inpatientBalancePayListDAO")
		private InpatientBalancePayListDAO inpatientBalancePayListDAO;

		@Override
		public InpatientBalancePay get(String arg0) {
			return null;
		}

		@Override
		public void removeUnused(String arg0) {
			
		}

		@Override
		public void saveOrUpdate(InpatientBalancePay arg0) {
			
		}
		@Override
		public void saveInpatientBalancePay(InpatientBalancePay inpatientBalancePay) {
			inpatientBalancePay.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			inpatientBalancePay.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
			inpatientBalancePay.setCreateTime(DateUtils.getCurrentTime());
			inpatientBalancePayListDAO.save(inpatientBalancePay);
		}
}
