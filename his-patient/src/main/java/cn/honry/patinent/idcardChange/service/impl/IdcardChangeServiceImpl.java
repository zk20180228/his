package cn.honry.patinent.idcardChange.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.PatientIdcardChange;
import cn.honry.base.bean.model.User;
import cn.honry.patinent.idcardChange.dao.IdcardChangeDAO;
import cn.honry.patinent.idcardChange.service.IdcardChangeService;

@Service("idcardChangeService")
@Transactional
@SuppressWarnings({ "all" })
public class IdcardChangeServiceImpl implements IdcardChangeService{
	
	@Autowired
	@Qualifier(value = "idcardChangeDAO")
	private IdcardChangeDAO idcardChangeDAO;
	
	

	@Override
	public PatientIdcardChange get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(PatientIdcardChange arg0) {
		
	}

	
	/**
	 * GH
	 * 2017年2月20日
	 * 根据患者id查询就诊卡变更记录
	 * @return
	 */ 
	@Override
	public List<PatientIdcardChange> queryChange(String page,String rows,String patientId,String startTime,String endTime,String type) {
		return idcardChangeDAO.queryChange(page,rows,patientId,startTime,endTime,type);
	}

	@Override
	public int getTotal(String patientId,String startTime, String endTime, String type) {
		return idcardChangeDAO.getTotal(patientId,startTime,endTime,type);
	}
	
	/**
	 * 查询用户Map
	 * @author zpty
	 * @CreateDate：2017-05-02
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<User> queryUserRecord() {
		return idcardChangeDAO.queryUserRecord();
	}
}
