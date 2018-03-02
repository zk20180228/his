package cn.honry.inpatient.recall.dao;

import java.util.List;

import cn.honry.base.bean.model.BusinessBedward;
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
import cn.honry.base.dao.EntityDao;
import cn.honry.inpatient.recall.vo.Vhinfo;

@SuppressWarnings({"all"})
public interface RecallDAO extends EntityDao<InpatientInfoNow>{
	/**  
	 *  
	 * @Description：  查询（住院号&&医保号）
	 * @Author：liudelin
	 * @CreateDate：2015-8-31上午9:12
	 * @ModifyRmk：
	 * @version 1.0
	 * @throws Exception 
	 */
	VidBedInfo queryRecallList(String mcardNo, String inpatientNo,String bedId, String patientName) throws Exception;
	
	/**
	 * 根据病历号查询（新表）
	 * @param mid
	 * @return
	 * @throws Exception 
	 */
	List<InpatientInfoNow> queryInfoList(String mid) throws Exception;
	
	/**
	 * 根据病床id查询到病床的医疗流水号
	 * @throws Exception 
	 * 
	 */
	InpatientBedinfoNow getInfoByBedId(String bedID) throws Exception;
	
	
	/**
	 * 根据病历号查询
	 * @param motherName
	 * @return
	 * @throws Exception 
	 */
	VidBedInfo queryInfoListBYmotherName(String motherName) throws Exception;
	/**
	 * 根据姓名来查询患者信息
	 * @param name
	 * @return
	 * @throws Exception 
	 */
	Patient getInfoBYName(String name) throws Exception;
	/**  
	 *  
	 * @Description：  业务判断查询（住院号）
	 * @Author：liudelin
	 * @CreateDate：2015-8-31上午9:12
	 * @ModifyRmk：
	 * @version 1.0
	 * @throws Exception 
	 */
	VidBedInfo RecallByInpatientNo(String inpatientNos) throws Exception;
	/**  
	 *  
	 * @Description：  业务判断查询（床号）
	 * @Author：liudelin
	 * @CreateDate：2015-8-31上午9:12
	 * @ModifyRmk：
	 * @version 1.0
	 * @throws Exception 
	 */
	VidBedInfo RecallBedByName(String bedIds) throws Exception;
	
	
	
	/**
	 * 根据床号获取当前床的状态
	 * @param bedId
	 * @return
	 * @throws Exception 
	 */
	BusinessHospitalbed getBedByName(String bedId) throws Exception;
	
	/**
	 * 做病床下拉框
	 * @throws Exception 
	 */
    List<BusinessHospitalbed> getcombobox(String deptId) throws Exception;
    
    /**
     * 病房下拉框
     * @throws Exception 
     */
    List<BusinessBedward> getBEdWardCombobox() throws Exception;
    
	/**  
	 *  
	 * @Description：  更新病床表信息
	 * @Author：liudelin
	 * @CreateDate：2015-8-31上午9:12
	 * @ModifyRmk：
	 * @version 1.0
	 * @param houseDocCode 
	 * @param chargeDocCode 
	 * @param chiefDocCode 
	 * @param dutyNurseCode 
	 * @throws Exception 
	 */
	void updateBedid(String bedIds, String inpatientNos, String dutyNurseCode, String chiefDocCode, String chargeDocCode, String houseDocCode) throws Exception;
    /**
     * 查询医生护士
     * @throws Exception 
     */
	List<SysEmployee> getEmpCombobox(String empType) throws Exception;
	/**
	 * 查询婴儿信息表
	 * @throws Exception 
	 */
	InpatientBabyInfoNow babyInfoByInpatientNo(String inpatientNo) throws Exception;
	/**
	 * 根据住院流水号查询住院主表
	 * @throws Exception 
	 */
	InpatientInfoNow getInfoByMotherid(String inpatientNO) throws Exception;
	/**
	 * 根据病历号查询病人id
	 * @throws Exception 
	 */
	Patient getIdByMedId(String mid) throws Exception;
	/**
	 * 根据住院主表的床号查询病床信息
	 * @throws Exception 
	 */
	List<Vhinfo> getBedidByBusinessHospitalbed(String bedId) throws Exception;

	/**
	 * 根据病历号查询（旧表）
	 * @param mid
	 * @return
	 * @throws Exception 
	 */
	List<InpatientInfo> queryOldInfoList(String mid) throws Exception;

	/**
	 * 根据id查询（旧表）
	 * @param mid
	 * @return
	 * @throws Exception 
	 */
	InpatientInfo queryOldbyId(String id) throws Exception;

	/**
	 * 更新旧表数据
	 * @param oid
	 * @return
	 * @throws Exception 
	 */
	void updateOld(String oid) throws Exception;
}
