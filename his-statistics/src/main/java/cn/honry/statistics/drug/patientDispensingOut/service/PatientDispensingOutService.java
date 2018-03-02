package cn.honry.statistics.drug.patientDispensingOut.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.DrugApplyout;
import cn.honry.base.bean.model.DrugBillclass;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.base.service.BaseService;
import cn.honry.statistics.drug.patientDispensing.vo.VinpatientApplyout;
import cn.honry.utils.TreeJson;

public interface PatientDispensingOutService extends BaseService<DrugApplyout>{
	/**  
	 *  患者树
	 *
	 */
	List<TreeJson> queryPatientTree(String deptId,String flag) throws Exception;
	/***
	 * 
	 * @Description:查询病区摆药信息
	 * @author:  丛林
	 * @CreateDate: 2016年6月23日 
	 * @version 1.1
	 * @See 添加了sign 判断是打印全部数据还是分页查询
	 */
	List<VinpatientApplyout> queryVinpatientApplyout(String deptId,String type,String page,String rows,String tradeName,String inpatientNo,String endDate,String beginDate,String flag,String sign) throws Exception;
	/***
	 * 
	 * @Description:查询病区摆药信息  总条数
	 * @author:  donghe
	 * @CreateDate: 2016年6月23日 
	 * @version 1.0
	 */
	int qqueryVinpatientApplyoutTotal(String deptId,String type,String tradeName,String inpatientNo,String endDate,String beginDate,String flag) throws Exception;
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
	Map<String, String> queryUnitMap() throws Exception;
}
