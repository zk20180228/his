package cn.honry.inner.drug.applyout.dao;

import java.util.List;

import cn.honry.base.bean.model.DrugApplyoutNow;
import cn.honry.base.bean.model.DrugBillclass;
import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.DrugStorage;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.EntityDao;
import cn.honry.inner.drug.applyout.vo.VinpatientApplyout;

public interface ApplyoutInInterDAO extends EntityDao<DrugApplyoutNow>{
	/**  
	 * @Description：根据药品ID查询药品是否存在
	 * @Author：ldl
	 * @CreateDate：2016-05-04
	 * @ModifyRmk：  
	 * @param：drugCode 药品ID
	 * @version 1.0
	 */
	DrugInfo queryDrugInfo(String drugCode);
	/**  
	 * @Description： 根据药品查询库存中批次
	 * @Author：ldl
	 * @CreateDate：2016-05-04
	 * @ModifyRmk：  
	 * @param： drugDeptCode 药房
	 * @param : drugCode 药品id
	 * @version 1.0
	 */
	List<DrugStorage> findDrugStorageByDrugId(String drugDeptCode,String drugCode);
	
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
	
	/** 查询出库申请单明细（申请科室，发药科室，申请单号，单据来源，申请状态）无记录返回null
	* @Title: queryDrugApplyoutNow 
	* @Description: 查询出库申请单明细（申请科室，发药科室，申请单号，单据来源，申请状态）
	* @param applyoutNow 查询实体
	* @param flag 是否要同时查询库存数量与申请金额（1是0否）
	* @author dtl 
	* @date 2016年12月29日
	*/
	List<DrugApplyoutNow> queryDrugApplyoutNow(DrugApplyoutNow applyoutNow, int flag);
}
