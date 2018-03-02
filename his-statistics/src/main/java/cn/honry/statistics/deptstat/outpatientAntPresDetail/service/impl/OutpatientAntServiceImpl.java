package cn.honry.statistics.deptstat.outpatientAntPresDetail.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.honry.inner.statistics.outpatientAntPresDetail.dao.OutpatientAntDao;
import cn.honry.statistics.deptstat.outpatientAntPresDetail.dao.OutpatientAntPresDao;
import cn.honry.statistics.deptstat.outpatientAntPresDetail.service.OutpatientAntService;
import cn.honry.utils.DateUtils;
@Service("outpatientAntService")
public class OutpatientAntServiceImpl implements OutpatientAntService {
	@Autowired
	@Qualifier("outpatientAntDao")
	private OutpatientAntDao outpatientAntDao;
	@Autowired
	@Qualifier(value="outpatientAntPresDao")
	private OutpatientAntPresDao outpatientAntPresDao;
	
	public void setOutpatientAntDao(OutpatientAntDao outpatientAntDao) {
		this.outpatientAntDao = outpatientAntDao;
	}

	public void setOutpatientAntPresDao(OutpatientAntPresDao outpatientAntPresDao) {
		this.outpatientAntPresDao = outpatientAntPresDao;
	}

	@Override
	public void init_MZKJYWCFBL(String begin, String end, Integer type) {
		if(StringUtils.isNotBlank(begin)&&StringUtils.isNotBlank(end)&&type!=null){
			String menuAlias="MZKJYWCFBL";
			if(1==type){//日数据 dateformate:yyyy-MM-dd
				outpatientAntDao.init_MZKJYWCFBL(menuAlias, null, begin);
			}else if(2==type){//月数据 dateformate:yyyy-MM 
				Date beginDate=DateUtils.parseDateY_M(begin);
				Calendar ca=Calendar.getInstance();
				ca.setTime(beginDate);
				Date endDate=DateUtils.parseDateY_M(end);
				while(DateUtils.compareDate(ca.getTime(), endDate)<=0){
					begin=DateUtils.formatDateY_M(ca.getTime());//本月第一天
					ca.add(Calendar.MONTH, 1);//下一月
				}
			}else if(3==type){//年数据dateformate:yyyy
				Date beginDate=DateUtils.parseDateY(begin);
				Date endDate=DateUtils.parseDateY(end);
				Calendar ca=Calendar.getInstance();
				ca.setTime(beginDate);
				while(DateUtils.compareDate(ca.getTime(), endDate)<=0){
					begin=DateUtils.formatDateY(ca.getTime());
					ca.add(Calendar.YEAR, 1);
				}
			}
		}
	}

	@Override
	public Map<String,Object> queryList(String searchBegin,
			String searchEnd, String deptCodes,String menuAlias,String rows,String page) {
		return outpatientAntPresDao.queryList(searchBegin, searchEnd, deptCodes, menuAlias,rows,page);
	}
	
}
