package cn.honry.statistics.deptstat.criticallyPatientsAnalyse.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.statistics.bi.inpatient.averageStayDay.dao.AverageStayDayDao;
import cn.honry.statistics.deptstat.criticallyPatientsAnalyse.dao.CriticallyPatientsAnalyseDao;
import cn.honry.statistics.deptstat.criticallyPatientsAnalyse.service.CriticallyPatientsAnalyseService;
import cn.honry.statistics.deptstat.criticallyPatientsAnalyse.vo.CriticallyPatientsVO;
import cn.honry.statistics.deptstat.operationProportion.vo.OperationProportionVo;
import cn.honry.utils.CommonStringUtils;
import cn.honry.utils.FileUtil;

@Service("criticallyPatientsAnalyseService")
@Transactional
@SuppressWarnings({ "all" })
public class CriticallyPatientsAnalyseServiceImpl implements CriticallyPatientsAnalyseService {
	
	@Autowired
	@Qualifier(value = "criticallyPatientsAnalyseDao")
	private CriticallyPatientsAnalyseDao criticallyPatientsAnalyseDao;

	@Override
	public List<CriticallyPatientsVO> queryCriticallyPatients(String deptName,String begin,String end,String menuAlias) throws Exception {
		List<CriticallyPatientsVO> list = criticallyPatientsAnalyseDao.queryCriticallyPatients(deptName,begin,end,menuAlias);
//		for (CriticallyPatientsVO vo : list) {
//			vo.setNumber(vo.getCure()+vo.getBetter()+vo.getNocure()+vo.getDeath()+vo.getOther());
//			
//		}
		return list;
	}

	@Override
	public Map<String, String> queryDeptMap() throws Exception {
		Map<String,String> map=new HashMap<String,String>();
		List<SysDepartment> list=criticallyPatientsAnalyseDao.queryDeptList();
		if(list!=null&&list.size()>0){
			for(SysDepartment s:list){
				map.put(s.getDeptCode(), s.getDeptName());
			}
		}
		return map;
	}

	@Override
	public FileUtil exportList(List<CriticallyPatientsVO> list, FileUtil fUtil) {
		if(list != null&&list.size()>0){
			for (CriticallyPatientsVO model : list) {
				String record="";
					record =""+CommonStringUtils.trimToEmpty(model.getDeptName()) + ",";
					record += model.getNum() + ",";
					record += model.getCure() + ",";
					record += model.getBetter() + ",";
					record += model.getNocure() + ",";
					record += model.getDeath() + ",";
					record += model.getOther() + ",";
					record += model.getCurePercent() + ",";
					record += model.getDeathPercent() + ",";
					record += model.getAveInpatient() + ",";
					record += model.getAveCost() + ",";
					record += model.getSolo() + ",";
					record += model.getAllPatient() + ",";
					record += model.getNumPercent() + ",";
					try {
						fUtil.write(record);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
		}
			return fUtil;
	}

	@Override
	public void initMongoDb() throws Exception {
		criticallyPatientsAnalyseDao.initMongoDb();
	}

	@Override
	public List<CriticallyPatientsVO> queryCriticallyPatientsFromMongo() throws Exception {
		return criticallyPatientsAnalyseDao.queryCriticallyPatientsFromMongo();
	}
}
