package cn.honry.statistics.drug.billsearch.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.DepartmentContact;
import cn.honry.base.bean.model.DrugApplyout;
import cn.honry.base.bean.model.DrugBillclass;
import cn.honry.base.bean.model.DrugOutstore;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.statistics.drug.billsearch.dao.BillSearchDAO;
import cn.honry.statistics.drug.billsearch.service.BillSearchService;
import cn.honry.statistics.drug.billsearch.vo.BillClassHzVo;
import cn.honry.statistics.drug.billsearch.vo.BillClassMxVo;
import cn.honry.statistics.operation.operationBillingInfo.vo.OperationBillingInfoVo;
import cn.honry.utils.CommonStringUtils;
import cn.honry.utils.FileUtil;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.TreeJson;

/**   
* 摆药单分类service实现类
* @className：OperationBillingInfoService
* @description：  手术计费信息Service实现层
* @author：tangfeishuai
* @createDate：2016-6-12 上午10:52:19  
* @modifier：tangfeishuai
* @modifyRmk：  
* @version 1.0
 */
@Service("billSearchService")
@Transactional
@SuppressWarnings({ "all" })
public class BillSearchServiceImpl implements BillSearchService{
	/** 
	 *摆药单dao
	 */
	@Autowired
	@Qualifier(value = "billSearchDAO")
	private BillSearchDAO billSearchDAO;
	
	@Override
	public OperationBillingInfoVo get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(OperationBillingInfoVo arg0) {
		
	}

	/***
	 * 登录病区下的各科室摆药单信息
	 * @Description:
	 * @author:  tangfeishuai
	 * @CreateDate: 2016年6月12日 
	 * @version 1.0
	 * @parameter beganTime 开始时间,endTime 结束时间, drugedBill 摆药单号，applyState 申请状态
	 * @since 
	 * @return List<TreeJson>
	 * @throws Exception 
	 */
	@Override
	public List<TreeJson> treeBillSearch(String beginTime, String endTime, String drugedBill, String applyState) throws Exception {
		//一级目录当前病区
		SysDepartment sysDept=ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		String type=sysDept.getDeptType();
		List<DepartmentContact> depList = billSearchDAO.getDepConByPid(sysDept.getId());
		List<TreeJson> treeJsonList=new ArrayList<TreeJson>();
		TreeJson topTreeJson = new TreeJson();
		topTreeJson.setId("1");
		topTreeJson.setText("病区"+"("+sysDept.getDeptName()+")");
		
		//二级目录显示医院维护的摆药单分类
		TreeJson aTreeJson=null;
		List<DrugBillclass> drugList= billSearchDAO.getDrugBillclass();
		if(drugList!=null&&drugList.size()>=0){
			topTreeJson.setState("open");
			List<TreeJson> aTreeJsonList=new ArrayList<TreeJson>();
			for (DrugBillclass drugBill : drugList) {
				aTreeJson = new TreeJson();
				aTreeJson.setId(drugBill.getId());
				aTreeJson.setText(drugBill.getBillclassName());
				if(depList!=null&&depList.size()>0){
					aTreeJson.setState("open");
					List<TreeJson> bTreeJsonList=new ArrayList<TreeJson>();
					TreeJson bTreeJson= null;
					for (DepartmentContact dc : depList) {
						bTreeJson = new TreeJson();
						bTreeJson.setId(dc.getDeptId()+drugBill.getId());
						bTreeJson.setText(dc.getDeptName());
						List<DrugApplyout> drugOutList = billSearchDAO.getDrugOutstore(dc.getDeptId(), drugBill.getId(), drugedBill,beginTime, endTime, applyState);
						if(drugOutList!=null&&drugOutList.size()>0){
							bTreeJson.setState("open");
							TreeJson cTreeJson = null;
							List<TreeJson> cTreeJsonList=new ArrayList<TreeJson>();
							for (DrugApplyout drugApply : drugOutList) {
								cTreeJson = new TreeJson();
								cTreeJson.setId("2");
								cTreeJson.setText(StringUtils.isNotBlank(drugApply.getDrugedBill())?drugApply.getDrugedBill():"未知");
								Map<String,String> drugApplyMap = new HashMap<String,String>();
								drugApplyMap.put("pid", dc.getDeptId()+drugBill.getId());
								drugApplyMap.put("drugedBill",drugApply.getDrugedBill());
								cTreeJson.setAttributes(drugApplyMap);
								cTreeJsonList.add(cTreeJson);
							}
							bTreeJson.setChildren(cTreeJsonList);
						}else{
							bTreeJson.setState("closed");
						}
						Map<String,String> deptMap = new HashMap<String,String>();
						deptMap.put("pid", drugBill.getId());
						bTreeJson.setAttributes(deptMap);
						bTreeJsonList.add(bTreeJson);
					}
					aTreeJson.setChildren(bTreeJsonList);
				}else{
					aTreeJson.setState("closed");
				}
				Map<String,String> billMap = new HashMap<String,String>();
				billMap.put("pid","1");
				aTreeJson.setAttributes(billMap);
				aTreeJsonList.add(aTreeJson);
			}
			topTreeJson.setChildren(aTreeJsonList);
		}else{
			topTreeJson.setState("closed");
		}
		treeJsonList.add(topTreeJson);

		return treeJsonList;
	}
	
	/***
	 * 摆药单汇总信息
	 * @Description:
	 * @author:  tangfeishuai
	 * @CreateDate: 2016年6月13日 
	 * @version 1.0
	 * @parameter  drugedBill 摆药单号，applyState 申请状态
	 * @since 
	 * @return List<BillClassHzVo>
	 * @throws Exception 
	 */
	@Override
	public List<BillClassHzVo> getBillClassHzVo(String drugedBill, String applyState,String bname,String page,String rows) throws Exception {
		return billSearchDAO.getBillClassHzVo(drugedBill, applyState, bname,page, rows);
	}
	/***
	 * 摆药单汇总信息
	 * @Description:
	 * @author:  tangfeishuai
	 * @CreateDate: 2016年6月13日 
	 * @version 1.0
	 * @parameter drugedBill 摆药单号，applyState 申请状态
	 * @since 
	 * @return List<BillClassMxVo>
	 * @throws Exception 
	 */
	@Override
	public List<BillClassMxVo> getBillClassMxVo(String drugedBill, String applyState,String bname,String page,String rows) throws Exception {
		return billSearchDAO.getBillClassMxVo(drugedBill, applyState, bname,page, rows);
	}
	/**
	 * @Description:根据条件查询摆药单汇总记录数
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月8日 上午9:47:41 
	 * @param:beganTime 开始时间； endTime 结束时间； patientNo 住院流水号； execDept 执行科室
	 * @return List<OperationCostVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 * @throws Exception 
	 */
	@Override
	public int getBillHzTotal(String drugedBill, String bname,String applyState) throws Exception {
		return billSearchDAO.getBillHzTotal(drugedBill,bname, applyState);
	}
	/**
	 * @Description:根据条件查询摆药单明细记录数
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月8日 上午9:47:41 
	 * @param:beganTime 开始时间； endTime 结束时间； patientNo 住院流水号； execDept 执行科室
	 * @return List<OperationCostVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 * @throws Exception 
	 */
	@Override
	public int getBillMxTotal(String drugedBill,String bname, String applyState) throws Exception {
		return billSearchDAO.getBillMxTotal(drugedBill,bname, applyState);
	}
	
	/**
	 * @Description:根据条件查询摆药单明细记录数
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月8日 上午9:47:41 
	 * @param:beganTime 开始时间； endTime 结束时间； patientNo 住院流水号； execDept 执行科室
	 * @return List<OperationCostVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public FileUtil exportBillClassHzVo(List<BillClassHzVo> list, FileUtil fUtil) {
		for (BillClassHzVo model : list) {
			String record="";
			record = CommonStringUtils.trimToEmpty(model.getDrugName()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getSpecs()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getDrugedNum()+"") + ",";
			record += CommonStringUtils.trimToEmpty(model.getDeptCode()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getDrugDeptCode()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getBillClassName()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getDrugPinYin()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getDrugWb()) + ",";
			try {
				fUtil.write(record);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fUtil;
	}

	/**
	 * @Description:导出摆药单明细列表
	 * @Description:
	 * @author: tangfeishuai
	 * @CreateDate: 2016年6月29日 
	 * @version 1.0
	 **/
	@Override
	public FileUtil exportBillClassMxVo(List<BillClassMxVo> list, FileUtil fUtil) {
		for (BillClassMxVo model : list) {
			String record="";
			record = CommonStringUtils.trimToEmpty(model.getBedName()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getPatientName()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getInpatientNo()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getTradeName()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getSpecs()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getDoseOnce()+"") + ",";
			record += CommonStringUtils.trimToEmpty(model.getDoseUnit()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getDfqFerq()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getUseName()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getDrugedNum()+"") + ",";
			record += CommonStringUtils.trimToEmpty(model.getDeptCode()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getDrugDeptCode()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getDrugedBill()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getPrintDate()+"") + ",";
			record += CommonStringUtils.trimToEmpty(model.getDrugPinYin()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getDrugWb()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getValidState()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getState()) + ",";
			try {
				fUtil.write(record);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fUtil;
	}

	@Override
	public List<BillClassHzVo> getAllBillClassHzVo(String drugedBill, String applyState, String bname) throws Exception {
		return billSearchDAO.getAllBillClassHzVo(drugedBill, applyState, bname);
	}

	@Override
	public List<BillClassMxVo> getAllBillClassMxVo(String drugedBill, String applyState, String bname) throws Exception {
		return billSearchDAO.getAllBillClassMxVo(drugedBill, applyState, bname);
	}

}
