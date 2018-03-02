package cn.honry.statistics.bi.inpatient.averageStayDay.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.statistics.bi.inpatient.averageStayDay.dao.AverageStayDayDao;
import cn.honry.statistics.bi.inpatient.averageStayDay.service.AverageStayDayService;
import cn.honry.statistics.bi.inpatient.averageStayDay.vo.AverageStayDayVo;

@Service("averageStayDayService")
@Transactional
@SuppressWarnings({ "all" })
public class AverageStayDayServiceImpl implements AverageStayDayService{

	@Autowired
	@Qualifier(value = "averageStayDayDao")
	private AverageStayDayDao averageStayDayDao;
	
	@Override
	public AverageStayDayVo get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(AverageStayDayVo arg0) {
		
	}

	@Override
	public List<SysDepartment> queryAllDept() {
		return averageStayDayDao.queryAllDept();
	}

	@Override
	public List<AverageStayDayVo> queryAverageStayDay(String deptCode,
			String years) {
		return averageStayDayDao.queryAverageStayDay(deptCode,years);
	}

}
