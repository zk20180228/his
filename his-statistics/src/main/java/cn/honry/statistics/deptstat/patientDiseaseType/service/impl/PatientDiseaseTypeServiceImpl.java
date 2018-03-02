package cn.honry.statistics.deptstat.patientDiseaseType.service.impl;

import groovy.ui.Console;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.statistics.deptstat.patientDiseaseType.dao.PatientDiseaseTypeDAO;
import cn.honry.statistics.deptstat.patientDiseaseType.service.PatientDiseaseTypeService;
import cn.honry.statistics.deptstat.patientDiseaseType.vo.PatientDiseaseType;
import cn.honry.utils.NumberUtil;

@Service("patientDiseaseTypeService")
@Transactional
@SuppressWarnings({ "all" })
public class PatientDiseaseTypeServiceImpl implements PatientDiseaseTypeService{
	@Autowired
	@Qualifier(value = "patientDiseaseTypeDAO")
	private PatientDiseaseTypeDAO patientDiseaseTypeDAO;
	
	@Override
	public PatientDiseaseType get(String arg0) {
		return patientDiseaseTypeDAO.get(arg0);
	}

	@Override
	public void removeUnused(String arg0) {
	}

	@Override
	public void saveOrUpdate(PatientDiseaseType arg0) {
	}
	/**  
	 * 
	 * 患者疾病类型统计分析list
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月13日 下午8:56:54 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月13日 下午8:56:54 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public List<PatientDiseaseType> queryPatientDisease(String deptCode,
			String sex, String startTime, String endTime,String page, String rows) throws Exception {
		List<PatientDiseaseType> list = patientDiseaseTypeDAO.queryPatientDisease(deptCode, sex, startTime, endTime,page,rows);
		for (PatientDiseaseType vo : list) {
			vo.setTotal(vo.getBetter()+vo.getCure()+vo.getDeath()+vo.getHealed()+vo.getNormal()+vo.getOther()+vo.getPlanning());
			vo.setBetterPer(vo.getTotal()==0?0:NumberUtil.init().div(vo.getBetter()*100, vo.getTotal(), 2));
			vo.setCurePer(vo.getTotal()==0?0:NumberUtil.init().div(vo.getCure()*100,vo.getTotal(),2));
			vo.setHealedPer(vo.getTotal()==0?0:NumberUtil.init().div(vo.getHealed()*100,vo.getTotal(),2));
			vo.setDeathPer(vo.getTotal()==0?0:NumberUtil.init().div(vo.getDeath()*100,vo.getTotal(),2));
			vo.setNormalPer(vo.getTotal()==0?0:NumberUtil.init().div(vo.getNormal()*100,vo.getTotal(),2));
			vo.setPlanningPer(vo.getTotal()==0?0:NumberUtil.init().div(vo.getPlanning()*100,vo.getTotal(),2));
			vo.setOtherPer(vo.getTotal()==0?0:NumberUtil.init().div(vo.getOther()*100,vo.getTotal(),2));
		}
		return list;
		
	}
	/**  
	 * 
	 * 患者疾病类型统计分析total
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月13日 下午8:58:12 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月13日 下午8:58:12 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public Integer queryTotal(String deptCode, String sex, String startTime,
			String endTime) throws Exception {
		return patientDiseaseTypeDAO.queryTotal(deptCode, sex, startTime, endTime);
	}
	/**  
	 * 
	 * 未治愈ICD
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月14日 上午10:27:50 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月14日 上午10:27:50 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public List<PatientDiseaseType> queryIcdHealed(String deptCode, String sex,
			String startTime, String endTime) throws Exception{
		return patientDiseaseTypeDAO.queryIcdHealed(deptCode, sex, startTime, endTime);
	}
	/**  
	 * 
	 * 未死亡ICD
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月14日 上午10:27:50 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月14日 上午10:27:50 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public List<PatientDiseaseType> queryIcdDeath(String deptCode, String sex,
			String startTime, String endTime) throws Exception{
		return patientDiseaseTypeDAO.queryIcdDeath(deptCode, sex, startTime, endTime);
	}

}
