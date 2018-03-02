package cn.honry.statistics.bi.inpatient.bedUseCondition.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.statistics.bi.inpatient.bedUseCondition.dao.BedUseConditionDao;
import cn.honry.statistics.bi.inpatient.bedUseCondition.service.BedUseConditionService;
import cn.honry.statistics.bi.inpatient.bedUseCondition.vo.BedUseConditionVo;
import cn.honry.statistics.bi.inpatient.dischargePerson.vo.DischargePersonVo;

@Service("bedUseConditionService")
@Transactional
@SuppressWarnings({ "all" })
public class BedUseConditionServiceImpl  implements BedUseConditionService{

	@Autowired
	@Qualifier(value = "bedUseConditionDao")
	private BedUseConditionDao bedUseConditionDao;

	@Override
	public void removeUnused(String arg0) {
	}


	@Override
	public BedUseConditionVo get(String arg0) {
		return null;
	}

	@Override
	public void saveOrUpdate(BedUseConditionVo arg0) {
	}


	@Override
	public List<SysDepartment> queryAllDept() {
		return bedUseConditionDao.queryAllDept();
	}


	@Override
	public List<BedUseConditionVo> queryBedUseCondition(String deptCode,
			String years) {
		List<BedUseConditionVo> lists=bedUseConditionDao.queryBedUseCondition(deptCode, years);
		List<BedUseConditionVo> bedUseConditionList=new ArrayList<BedUseConditionVo>();
		for(int i=0;i<lists.size();i++ ){
			BedUseConditionVo disper=lists.get(i);
			DecimalFormat df = new DecimalFormat("#.##");
			Integer prs=0;
			Integer day=0;
			double beddoorEmergency=0;
			double disPatAveDays=0;
			if(disper.getAveOpenBed()!=0&&disper.getOutHospital()!=0){
				prs=disper.getOutHospital()/disper.getAveOpenBed();
			}
			disper.setBedTurnTimes(prs);
			if(disper.getActOccBedDay()!=0&&disper.getAveOpenBed()!=0){
				day=disper.getActOccBedDay()/disper.getAveOpenBed();
			}
			disper.setBedWorkDay(day);
			if(disper.getOutHospital()!=0&&disper.getDisPatOccBedDay()!=0){
				disPatAveDays=disper.getDisPatOccBedDay()/disper.getOutHospital();
			}
			disper.setDisPatAveDay(disPatAveDays);
			if(disper.getSum()!=0&&disper.getAveOpenBed()!=0){
				double d =disper.getSum()/disper.getAveOpenBed();
				BigDecimal b = new BigDecimal(d);
				beddoorEmergency = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			}
			disper.setBeddoorEmergency(beddoorEmergency);
			bedUseConditionList.add(disper);
			
		}
		return bedUseConditionList;
	}


}
