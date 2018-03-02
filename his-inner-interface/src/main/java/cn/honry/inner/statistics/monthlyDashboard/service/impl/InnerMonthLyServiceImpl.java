package cn.honry.inner.statistics.monthlyDashboard.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.honry.inner.statistics.monthlyDashboard.dao.InnerMonthLyDao;
import cn.honry.inner.statistics.monthlyDashboard.service.InnerMonthLyService;
@Service("innerMonthLyService")
public class InnerMonthLyServiceImpl implements InnerMonthLyService {
	@Autowired
	@Qualifier(value="innerMonthLyDao")
	private InnerMonthLyDao innerMonthLyDao;
	@Override
	public void init_MYZHYBP(String menuAlias, String type, String date) {
		innerMonthLyDao.init_MYZHYBP_HospExpenses(menuAlias, type, date);
		innerMonthLyDao.init_MYZHYBP_Inpatient(menuAlias, type, date);
		innerMonthLyDao.init_MYZHYBP_Surgery(menuAlias, type, date);
		innerMonthLyDao.init_MYZHYBP_Treatment(menuAlias, type, date);
		innerMonthLyDao.init_MYZHYBP_WardApply(menuAlias, type, date);
		
	}

}
