package cn.honry.inner.drug.applyout.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.DrugApplyoutNow;
import cn.honry.base.bean.model.DrugBillclass;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.base.service.BaseService;
import cn.honry.inner.drug.applyout.vo.VinpatientApplyout;

public interface ApplyoutInInterService extends BaseService<DrugApplyoutNow>{

	/**  
	 * @Description：行程出库申请单接口
	 * @Author：ldl
	 * @CreateDate：2016-04-22
	 * @ModifyRmk：  
	 * @param: applyOut 出库申请单List
	 * @param: type 1门诊摆药 2内部入库 3 门诊退药 4 住院摆药 5 住院退药
	 * @param: start 1审批 2核准
	 * @version 1.0
	 * 返回的map可以存储任意在接口中查到的数据 如success 是成功出库提示（如有增加可在map中增加，并在接口注释中进行注释）
	 * outBillCode 是出库单号 
	 * groupCode 是该药品最后一个批次号
	 * batchNo 是改药品最后一个批号
	 */
	Map<String,String> outDrugInterface(DrugApplyoutNow applyout,String type,Integer start);
	
	/** 
	* 根据出库申请表实际扣除库存（生成出库记录，减少库存明细药品库存数量）
	* @param applyout 出库申请记录
	* @param type 1门诊摆药 2内部入库 3 门诊退药 4 住院摆药 
	* @return Map<String,String> outBillCode 是出库单号  groupCode 是该药品最后一个批次号 batchNo 是改药品最后一个批号
	* 返回的map可以存储任意在接口中查到的数据 如success 是成功出库提示（如有增加可在map中增加，并在接口注释中进行注释）
	* @author dtl 
	* @date 2017年2月21日
	*/
	Map<String,String> actualOutDrugInterface(DrugApplyoutNow applyout,String type);
	
	/**  
	 *  患者树
	 *
	 */
	String queryPatientTree(String deptId);
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

	String getSequece(String string);
	
	
	/** 查询出库申请单明细（申请科室，发药科室，申请单号，单据来源，申请状态）
	* @Title: queryDrugApplyoutNow 
	* @Description: 查询出库申请单明细（申请科室，发药科室，申请单号，单据来源，申请状态）
	* @param applyoutNow 查询实体
	* @param flag 是否要同时查询库存数量与申请金额（1是0否）
	* @author dtl 
	* @date 2016年12月29日
	*/
	List<DrugApplyoutNow> queryDrugApplyoutNow(DrugApplyoutNow applyoutNow, int flag);
}
