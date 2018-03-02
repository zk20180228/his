package cn.honry.statistics.deptstat.deathPatient.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.statistics.deptstat.deathPatient.dao.DeathPatientDao;
import cn.honry.statistics.deptstat.deathPatient.service.DeathPatientService;
import cn.honry.statistics.deptstat.deathPatient.vo.DeathPatientVo;

@Service("deathPatientService")
@Transactional
@SuppressWarnings({"all"})
public class DeathPatientServiceImpl implements DeathPatientService{
	@Autowired
	@Qualifier(value = "deathPatientDao")
	private DeathPatientDao deathPatientDao;
	
	
	/**  
	 * 
	 * 患者死亡信息统计
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
	public List<DeathPatientVo> queryDeathPatient(String startTime,String endTime,String deptCode,String sex,String menuAlias,String page,String rows) {
		List<DeathPatientVo> list=new ArrayList<DeathPatientVo>();
			list=deathPatientDao.queryDeathPatient(startTime,endTime,deptCode,sex,menuAlias,page,rows);
		return list;
	}


	@Override
	public int getTotalDeathPatient(String startTime, String endTime,String deptCode,String sex, String menuAlias) {
		return deathPatientDao.getTotalDeathPatient(startTime,endTime,deptCode,sex,menuAlias);
	}


}
