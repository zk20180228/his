package cn.honry.statistics.deptstat.criticallyIllPatient.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.statistics.deptstat.criticallyIllPatient.dao.CriticallyIllPatientDao;
import cn.honry.statistics.deptstat.criticallyIllPatient.service.CriticallyIllPatientService;
import cn.honry.statistics.deptstat.criticallyIllPatient.vo.CriticallyIllPatientVo;

@Service("criticallyIllPatientService")
@Transactional
@SuppressWarnings({"all"})
public class CriticallyIllPatientServiceImpl implements CriticallyIllPatientService{
	@Autowired
	@Qualifier(value = "criticallyIllPatientDao")
	private CriticallyIllPatientDao criticallyIllPatientDao;
	
	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	
	/**  
	 * 
	 * 病危患者信息统计
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月14日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月14日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 *
	 */
	@Override
	public List<CriticallyIllPatientVo> queryCriticallyIllPatient(String startTime,String endTime,String deptCode,String sex,String menuAlias,String page,String rows) {
		List<CriticallyIllPatientVo> list=new ArrayList<CriticallyIllPatientVo>();
			list=criticallyIllPatientDao.queryCriticallyIllPatient(startTime,endTime,deptCode,sex,menuAlias,page,rows);
		return list;
	}
	/**  
	 * 
	 * 病危患者信息统计 总条数
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月14日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月14日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 *
	 */
	@Override
	public int getTotalCriticallyIllPatient(String startTime,String endTime,String deptCode,String sex, String menuAlias) {
		return criticallyIllPatientDao.getTotalCriticallyIllPatient(startTime,endTime,deptCode,sex,menuAlias);
	}

}
