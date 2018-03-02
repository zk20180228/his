package cn.honry.inpatient.recall.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessHospitalbed;
import cn.honry.base.bean.model.InpatientBabyInfo;
import cn.honry.base.bean.model.InpatientBabyInfoNow;
import cn.honry.base.bean.model.InpatientBedinfo;
import cn.honry.base.bean.model.InpatientBedinfoNow;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.VidBedInfo;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.inpatient.recall.dao.RecallDAO;
import cn.honry.inpatient.recall.service.RecallService;
import cn.honry.inpatient.recall.vo.Vhinfo;

@Service("recallService")
@Transactional
@SuppressWarnings({ "all" })
public class RecallServiceImpl implements RecallService{
	
	@Autowired
	@Qualifier(value = "recallDAO")
	private RecallDAO recallDAO;

	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public InpatientInfoNow get(String id) {
		return recallDAO.get(id);
	}

	public void saveOrUpdate(InpatientInfoNow entity) {
		if(StringUtils.isBlank(entity.getId())){
			entity.setId(null);
			entity.setCreateUser("");
			entity.setCreateDept("");
			entity.setCreateTime(new Date());
		}else{
			entity.setUpdateUser("");
			entity.setUpdateTime(new Date());
		}
		recallDAO.save(entity);
		if(entity.getId()==null){
			OperationUtils.getInstance().conserve(null,"住院召回信息管理","INSERT INTO","T_INPATIENT_INFO_NOW",OperationUtils.LOGACTIONINSERT);
		}else{
			OperationUtils.getInstance().conserve(entity.getId(),"住院召回信息管理","UPDATE","T_INPATIENT_INFO_NOW",OperationUtils.LOGACTIONUPDATE);
		}
	}
	/**  
	 *  
	 * @Description：  查询（住院号&&医保号）
	 * @Author：liudelin
	 * @CreateDate：2015-8-31上午9:12
	 * @ModifyRmk：
	 * @version 1.0
	 * @throws Exception 
	 */
	@Override
	public VidBedInfo queryRecallList(String mcardNo, String inpatientNo,String bedId, String patientName) throws Exception {
		VidBedInfo lst = recallDAO.queryRecallList(mcardNo,inpatientNo,bedId,patientName);
		return lst;
	}
	
	/**  
	 *  
	 * @Description：  业务判断查询（住院号）
	 * @Author：liudelin
	 * @CreateDate：2015-8-31上午9:12
	 * @ModifyRmk：
	 * @version 1.0
	 * @throws Exception 
	 */
	@Override
	public VidBedInfo RecallByInpatientNo(String inpatientNos) throws Exception {
		VidBedInfo lst = recallDAO.RecallByInpatientNo(inpatientNos);
		return lst;
	}
	/**  
	 *  
	 * @Description：  业务判断查询（床号）
	 * @Author：liudelin
	 * @CreateDate：2015-8-31上午9:12
	 * @ModifyRmk：
	 * @version 1.0
	 * @throws Exception 
	 */
	@Override
	public VidBedInfo RecallBedByName(String bedIds) throws Exception {
		VidBedInfo lst = recallDAO.RecallBedByName(bedIds);
		return lst;
	}
	/**  
	 *  
	 * @Description：  更新病床表信息&&更新住院主表信息
	 * @Author：liudelin
	 * @CreateDate：2015-8-31上午9:12
	 * @ModifyRmk：
	 * @version 1.0
	 * @throws Exception 
	 */
	@Override
	public void updateBedid(String bedIds, String inpatientNos, String chiefDocCode, String dutyNurseCode, String chargeDocCode, String houseDocCode) throws Exception {
		recallDAO.updateBedid(bedIds,inpatientNos,dutyNurseCode,chiefDocCode,chargeDocCode,houseDocCode);
		OperationUtils.getInstance().conserve(bedIds,"出院召回","UPDATE","T_INPATIENT_PREPAYIN",OperationUtils.LOGACTIONRECALL);
	}
	/**
	 * 根据病历号查询住院登记的患者信息+病床信息
	 * @throws Exception 
	 */
	public List<InpatientInfoNow> queryInfoList(String mid) throws Exception {
		return recallDAO.queryInfoList(mid);
	}
	/**
	 * 根据姓名查询
	 * @throws Exception 
	 */
	public Patient getInfoBYName(String name) throws Exception {
		return recallDAO.getInfoBYName(name);
	}
	/**
	 * 根据患者妈妈姓名查询
	 * @throws Exception 
	 */
	public VidBedInfo queryInfoListBYmotherName(String motherName) throws Exception {
		return recallDAO.queryInfoListBYmotherName(motherName);
	}
	/**
	 * 获得病床号下拉框
	 * @throws Exception 
	 * 
	 */
	public List<BusinessHospitalbed> getcombobox(String deptId) throws Exception {
		return recallDAO.getcombobox(deptId);
	}
	/**
	 * 根据病床Id查询
	 * @throws Exception 
	 */
	public BusinessHospitalbed getBedByName(String bedId) throws Exception {
		return recallDAO.getBedByName(bedId);
	}
	/**
	 * 根据病床id查询病床信息
	 * @throws Exception 
	 */
	public InpatientBedinfoNow getInfoByBedId(String bedID) throws Exception {
		return recallDAO.getInfoByBedId(bedID);
	}
	public List<SysEmployee> getEmpCombobox(String empType) throws Exception {
		return recallDAO.getEmpCombobox( empType);
	}
	public InpatientBabyInfoNow babyInfoByInpatientNo(String inpatientNo) throws Exception {
		return recallDAO.babyInfoByInpatientNo(inpatientNo);
	}
	public InpatientInfoNow getInfoByMotherid(String inpatientNO) throws Exception {
		return recallDAO.getInfoByMotherid(inpatientNO);
	}
	public Patient getIdByMedId(String mid) throws Exception {
		return recallDAO.getIdByMedId(mid);
	}

	@Override
	public List<Vhinfo> getBedidByBusinessHospitalbed(String bedId) throws Exception {
		return recallDAO.getBedidByBusinessHospitalbed(bedId);
	}

	@Override
	public List<InpatientInfo> queryOldInfoList(String mid) throws Exception {
		return recallDAO.queryOldInfoList(mid);
	}

	@Override
	public InpatientInfo queryOldbyId(String id) throws Exception {
		return recallDAO.queryOldbyId(id);
	}

	@Override
	public void updateOld(String oid) throws Exception {
		 recallDAO.updateOld(oid);
		
	}

}
