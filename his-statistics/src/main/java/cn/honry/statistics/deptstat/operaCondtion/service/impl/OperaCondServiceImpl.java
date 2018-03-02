package cn.honry.statistics.deptstat.operaCondtion.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.statistics.deptstat.operaCondtion.dao.OperaCondDao;
import cn.honry.statistics.deptstat.operaCondtion.service.OperaCondService;
import cn.honry.statistics.deptstat.operaCondtion.vo.OperaCondVo;
import cn.honry.utils.HisParameters;
@Service("operaCondService")
public class OperaCondServiceImpl implements OperaCondService{
	@Autowired
	@Qualifier(value="operaCondDao")
	private OperaCondDao operaCondDao;
	
	public void setOperaCondDao(OperaCondDao operaCondDao) {
		this.operaCondDao = operaCondDao;
	}

	@Override
	public List<OperaCondVo> queryOperaList(String begin, String end,
			String menuAlias, String depts,String page,String rows)throws Exception {
		List<String> tnL=ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_OPERATION_RECORD",begin,end);
		
		return operaCondDao.queryOperaList(tnL, begin, end, menuAlias, depts,page,rows);
	}

	@Override
	public int queryOperaTotal(String begin, String end, String menuAlias,
			String depts) throws Exception {
			List<String> tnL=ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_OPERATION_RECORD",begin,end);
		return operaCondDao.queryOperaTotal(tnL, begin, end, menuAlias, depts);
	}

}
