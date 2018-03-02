package cn.honry.statistics.deptstat.inpatientCount.service.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.statistics.deptstat.antimicrobialDrugAccess.vo.AntimicrobialDrugAccessVo;
import cn.honry.statistics.deptstat.inpatientCount.dao.InpatientCountDao;
import cn.honry.statistics.deptstat.inpatientCount.service.InpatientCountService;
import cn.honry.utils.CommonStringUtils;
import cn.honry.utils.FileUtil;
@Service("inpatientCountService")
@Transactional
@SuppressWarnings({ "all" })
public class InpatientCountServiceImpl implements InpatientCountService{

	@Autowired
	@Qualifier(value = "inpatientCountDao")
	private InpatientCountDao inpatientCountDao;
	
	public void setInpatientCountDao(InpatientCountDao inpatientCountDao) {
		this.inpatientCountDao = inpatientCountDao;
	}

	@Override
	public List<InpatientInfoNow> queryInpatientCountList(String dept,String page,String rows) {
		return inpatientCountDao.queryInpatientCountList(dept,page,rows);
	}

	@Override
	public int queryTotal(String dept) {
		return inpatientCountDao.queryTotal(dept);
	}

	@Override
	public FileUtil exportList(List<InpatientInfoNow> list,
			FileUtil fUtil) throws Exception{
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (InpatientInfoNow model : list) {
			String record="";
				record =CommonStringUtils.trimToEmpty("住院号:"+model.getInpatientNo())+ ",";
				record +=CommonStringUtils.trimToEmpty(model.getPatientName()) + ",";
				record +=CommonStringUtils.trimToEmpty(model.getReportSexName())+ ",";
				record +=CommonStringUtils.trimToEmpty(model.getReportAge()==null?"":model.getReportAge()+model.getReportAgeunit())+ ",";
				record +=CommonStringUtils.trimToEmpty(model.getDeptName())+ ",";
				record +=("床号:"+CommonStringUtils.trimToEmpty(model.getBedName()))+ ",";
				record +=CommonStringUtils.trimToEmpty(model.getChiefDocName())+ ",";
				record +=CommonStringUtils.trimToEmpty(model.getChargeDocName())+ ",";
				record +=format.format(model.getInDate())+ ",";
		        fUtil.write(record);
			}
			return fUtil;
	}

}
