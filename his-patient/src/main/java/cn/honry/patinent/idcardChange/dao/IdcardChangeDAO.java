package cn.honry.patinent.idcardChange.dao;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import cn.honry.base.bean.model.PatientIdcardChange;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.EntityDao;

@SuppressWarnings({"all"})
public interface IdcardChangeDAO extends EntityDao<PatientIdcardChange>{
	
	/**
	 * GH
	 * 2017年2月20日
	 * 根据患者id查询就诊卡变更记录
	 * @return
	 */ 
	public List<PatientIdcardChange> queryChange(String page,String rows, String patientId,String startTime,String endTime,String type);
	
	
	/**
	 * GH
	 * 2017年2月20日
	 * 根据患者id查询就诊卡总条数
	 * @return
	 */ 
	public int getTotal(String patientId,String startTime,String endTime,String type);
	
	/**
	 * 查询用户Map
	 * @author zpty
	 * @CreateDate：2017-05-02
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<User> queryUserRecord();
}

