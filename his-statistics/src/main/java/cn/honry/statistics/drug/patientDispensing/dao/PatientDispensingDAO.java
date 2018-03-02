package cn.honry.statistics.drug.patientDispensing.dao;

import java.util.List;

import cn.honry.base.bean.model.DrugApplyout;
import cn.honry.base.bean.model.DrugBillclass;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.EntityDao;
import cn.honry.statistics.drug.patientDispensing.vo.VinpatientApplyout;

@SuppressWarnings({"all"})
public interface PatientDispensingDAO extends EntityDao<DrugApplyout>{
	/**  
	 * @Description： 本区 患者树 .
	 */
	List<InpatientInfoNow> queryPatient(String deptId,String type);
	/**
	 * 查询科室
	 * @param deptId
	 * @return
	 */
	SysDepartment queryState(String deptId);
	/***
	 * 
	 * @Description:查询病区摆药信息
	 * @author:  donghe
	 * @CreateDate: 2016年6月23日 
	 * @version 1.0
	 */
	List<VinpatientApplyout> queryVinpatientApplyout(String deptId,String type,String page,String rows,String tradeName,String inpatientNo,String endDate,String beginDate);
	/***
	 * 
	 * @Description:查询病区摆药信息  总条数
	 * @author:  donghe
	 * @CreateDate: 2016年6月23日 
	 * @version 1.0
	 */
	int qqueryVinpatientApplyoutTotal(String deptId,String type,String tradeName,String inpatientNo,String endDate,String beginDate);
	/**
	 * 渲染科室
	 */
	SysDepartment querySysDepartment(String id);
	/**
	 * 渲染人员
	 */
	User queryUser(String id);
	/**
	 * 渲染摆药单
	 */
	DrugBillclass queryDrugBillclass(String id);
}
