package cn.honry.statistics.deptstat.deptSupervision.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.honry.statistics.deptstat.deptSupervision.dao.DeptSupervisionDao;
import cn.honry.statistics.deptstat.deptSupervision.service.DeptSupervisionService;
import cn.honry.statistics.deptstat.deptSupervision.vo.MonitorIndicatorsVo;

@Service("deptSupervisionService")
public class DeptSupervisionServiceImpl implements DeptSupervisionService {
	@Autowired
	@Qualifier(value="deptSupervisionDao")
	private DeptSupervisionDao deptSupervisionDao;
	
	public void setDeptSupervisionDao(DeptSupervisionDao deptSupervisionDao) {
		this.deptSupervisionDao = deptSupervisionDao;
	}

	@Override
	public List<MonitorIndicatorsVo> queryDayReport(String begin, String end,
			String depts, String menuAlias, String campus) throws Exception {
		return deptSupervisionDao.queryDayReport(begin, end, depts, menuAlias, campus);
	}

}
