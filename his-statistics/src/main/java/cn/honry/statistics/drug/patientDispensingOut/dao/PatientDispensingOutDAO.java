package cn.honry.statistics.drug.patientDispensingOut.dao;

import java.util.Date;
import java.util.List;

import cn.honry.base.bean.model.DrugApplyout;
import cn.honry.base.bean.model.DrugBillclass;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.EntityDao;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.drug.patientDispensing.vo.VinpatientApplyout;

@SuppressWarnings({"all"})
public interface PatientDispensingOutDAO extends EntityDao<DrugApplyout>{
	/**  
	 * @Description： 本区 患者树 .
	 */
	List<InpatientInfoNow> queryPatient(String deptId,String type,String flag) throws Exception;
	/**
	 * 查询科室
	 * @param deptId
	 * @return
	 */
	SysDepartment queryState(String deptId) throws Exception;
	/***
	 * 
	 * @Description:查询病区摆药信息
	 * @author:  donghe
	 * @CreateDate: 2016年6月23日 
	 * @version 1.0
	 */
	List<VinpatientApplyout> queryVinpatientApplyout(List<String> tnL,String deptId,String type,String page,String rows,String tradeName,String inpatientNo,String etime,String stime,String flag,String sign) throws Exception;
	/***
	 * 
	 * @Description:查询病区摆药信息  总条数
	 * @author:  donghe
	 * @CreateDate: 2016年6月23日 
	 * @version 1.0
	 */
	int qqueryVinpatientApplyoutTotal(List<String> tnL,String deptId,String type,String tradeName,String inpatientNo,String etime,String stime,String flag) throws Exception;
	/**
	 * 渲染科室
	 */
	SysDepartment querySysDepartment(String id) throws Exception;
	/**
	 * 渲染人员
	 */
	User queryUser(String id) throws Exception;
	/**
	 * 渲染摆药单
	 */
	DrugBillclass queryDrugBillclass(String id) throws Exception;
	
	/**  
	 * 
	 * <p> 获取业务表中最大及最小时间 </p>
	 * @Author: wsj
	 * @CreateDate: 2016年12月02日 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	StatVo findMaxMin() throws Exception;
	/**  
	 * 
	 * <p>查询病区患者 </p>
	 * @Author: donghe
	 * @CreateDate: 2016年12月09日 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	List<InpatientInfoNow> querypatient(String flag,String medicalrecordId) throws Exception;
	/***
	 * 
	 * @Description:单位渲染
	 * @author:  wangshujuan
	 * @CreateDate: 2017年7月4日 
	 * @version 1.0
	 */
	List queryUnitMap() throws Exception;
}
