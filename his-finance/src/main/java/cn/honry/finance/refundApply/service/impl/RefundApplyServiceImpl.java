package cn.honry.finance.refundApply.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.InpatientCancelitem;
import cn.honry.base.bean.model.InpatientCancelitemNow;
import cn.honry.base.bean.model.OutpatientFeedetail;
import cn.honry.base.bean.model.OutpatientFeedetailNow;
import cn.honry.base.bean.model.Registration;
import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.TecApply;
import cn.honry.base.bean.model.TecTerminalApply;
import cn.honry.finance.refund.dao.RefundDAO;
import cn.honry.finance.refund.vo.MedicinelDrugList;
import cn.honry.finance.refundApply.dao.RefundApplyDAO;
import cn.honry.finance.refundApply.service.RefundApplyServcie;
import cn.honry.inner.baseinfo.code.dao.CodeInInterDAO;
import cn.honry.inner.baseinfo.department.dao.DeptInInterDAO;
import cn.honry.utils.DateUtils;
import cn.honry.utils.ShiroSessionUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@SuppressWarnings("all")
@Transactional
@Service("refundApplyService")
public class RefundApplyServiceImpl implements RefundApplyServcie{
	
	@Autowired
	@Qualifier("refundApplyDAO")
	private RefundApplyDAO refundApplyDAO;
	@Autowired
	@Qualifier("refundDAO")
	private RefundDAO refundDAO;

	@Autowired
	@Qualifier(value="deptInInterDAO")
	private DeptInInterDAO deptInInterDAO;

	
	@Override
	public InpatientCancelitem get(String arg0) {
		return null;
	}
	
	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(InpatientCancelitem arg0) {
	}

	@Override
	public Map<String, String> saveRefund(String invoiceNo, String drugList,String unDrugList,String clinicCode) {
		String billCode = refundApplyDAO.getSeqByNameorNum("SEQ_INPATIENT_CAN_BILLCODE", 14);
		Map<String,String> map = new HashMap<String, String>();
		RegistrationNow info = refundApplyDAO.findInfo(clinicCode);
		Gson gson = new Gson();
		List<MedicinelDrugList> drugListVo = gson.fromJson(drugList, new TypeToken<List<MedicinelDrugList>>(){}.getType());
		List<MedicinelDrugList> unDrugListVo = gson.fromJson(unDrugList, new TypeToken<List<MedicinelDrugList>>(){}.getType());
		if(drugListVo.size()>0){
			for(MedicinelDrugList modls : drugListVo){
				OutpatientFeedetailNow feedetail = refundApplyDAO.queryFeedetail(modls.getId());
				InpatientCancelitemNow cancelitem = new InpatientCancelitemNow();
				cancelitem.setId(null);
				cancelitem.setBillCode(billCode); //申请单据号
				cancelitem.setInpatientNo(clinicCode); //门诊号
				cancelitem.setApplyFlag(1);//门诊
				cancelitem.setName(info.getPatientName());//患者姓名
				cancelitem.setDeptCode(info.getDeptCode());//患者所在科室
				cancelitem.setDeptName(info.getDeptName());//患者所在科室名称
				cancelitem.setDrugFlag(1);//药品标志,1药品/2非药
				cancelitem.setItemCode(modls.getItemCode());//项目编码
				cancelitem.setItemName(modls.getItemName());//项目名称
				cancelitem.setSpecs(modls.getSpecs());//规格
				cancelitem.setSalePrice(modls.getUnitPrice());//零售价
				cancelitem.setQuantity(modls.getNobackNums()-modls.getNobackNum());//申请退药数量（乘以付数后的总数量）
				cancelitem.setDays(1);//付数
				cancelitem.setPriceUnit(modls.getPriceUnit()+"");//计价单位
				SysDepartment sys= deptInInterDAO.getDeptCode(modls.getExecDpcd());
				cancelitem.setExecDpcd(modls.getExecDpcd());//执行科室
				cancelitem.setExecDpcdName(sys.getDeptName());//执行科室名称
				cancelitem.setOperCode(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//操作员编码
				cancelitem.setOperName(ShiroSessionUtils.getCurrentUserFromShiroSession().getName());//操作员名称
				cancelitem.setOperDate(DateUtils.getCurrentTime());//操作时间
				cancelitem.setOperDpcd(ShiroSessionUtils.getCurrentUserDepartmentFromShiroSession().getDeptCode());//操作员所在科室
				cancelitem.setOperDpcdName(ShiroSessionUtils.getCurrentUserDepartmentFromShiroSession().getDeptName());//操作员所在科室名称
				cancelitem.setRecipeNo(modls.getRecipeNo());//对应收费明细处方号
				cancelitem.setSequenceNo(modls.getSequenceNo());//对应处方内流水号
				cancelitem.setBillNo(feedetail.getInvoiceNo());//确认单号
				cancelitem.setConfirmFlag(0); //退药确认标识 0未确认/1确认
				cancelitem.setChargeFlag(0);//退费标识 0未退费/1退费/2作废
				cancelitem.setExtFlag(modls.getExtFlag3());//1 包装 单位 0, 最小单位
				cancelitem.setQty(modls.getNobackNum());//数量[22]
				cancelitem.setCardNo(info.getMidicalrecordId());//病历卡号
				cancelitem.setReturnReason("门诊退费");
				cancelitem.setCreateTime(DateUtils.getCurrentTime());
				cancelitem.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				cancelitem.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				cancelitem.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
				cancelitem.setUpdateTime(DateUtils.getCurrentTime());
				cancelitem.setInvoCode(feedetail.getInvoCode());
				refundApplyDAO.save(cancelitem);
				
				
				feedetail.setNobackNum(feedetail.getQty()-modls.getNobackNum());
				refundApplyDAO.update(feedetail);
				//医技项目处理
				if(StringUtils.isNotBlank(feedetail.getSampleId())){
					//确认项目处理
					TecTerminalApply ter = refundDAO.getTerByapplyNum(feedetail.getSampleId());
					if(ter!=null){
						if(ter.getConfirmNum()!=null&&ter.getConfirmNum()>0){//存在已经确认的项目，把已经确认的项目重新生成
							TecTerminalApply t = new TecTerminalApply();
							BeanUtils.copyProperties(ter, t);
							t.setQty(ter.getConfirmNum().doubleValue());
							t.setUpdateTime(new Date());
							t.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
							t.setExtFlag1("1");
							t.setId(null);
							refundDAO.save(t);
						}
						ter.setExtFlag1("0");
						refundDAO.save(ter);
					}
				}
			}
		}
		
		if(unDrugListVo.size()>0){
			for(MedicinelDrugList modls : unDrugListVo){
				OutpatientFeedetailNow feedetail = refundApplyDAO.queryFeedetail(modls.getId());
				InpatientCancelitemNow cancelitem = new InpatientCancelitemNow();
				cancelitem.setId(null);
				cancelitem.setBillCode(billCode); //申请单据号
				cancelitem.setApplyFlag(1);//门诊
				cancelitem.setInpatientNo(clinicCode); //门诊号
				cancelitem.setName(info.getPatientName());//患者姓名
				cancelitem.setDeptCode(info.getDeptCode());//患者所在科室
				cancelitem.setDeptName(info.getDeptName());//患者所在科室名称
				cancelitem.setDrugFlag(2);//药品标志,1药品/2非药
				cancelitem.setItemCode(modls.getItemCode());//项目编码
				cancelitem.setItemName(modls.getItemName());//项目名称
				cancelitem.setSpecs(modls.getSpecs());//规格
				cancelitem.setSalePrice(modls.getUnitPrice());//零售价
				cancelitem.setQuantity(modls.getNobackNums()-modls.getNobackNum());//申请退药数量（乘以付数后的总数量）
				cancelitem.setPriceUnit(modls.getPriceUnit()+"");//计价单位
				cancelitem.setExecDpcd(modls.getExecDpcd());//执行科室
				cancelitem.setOperCode(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//操作员编码
				cancelitem.setOperName(ShiroSessionUtils.getCurrentUserFromShiroSession().getName());//操作员名称
				cancelitem.setOperDate(DateUtils.getCurrentTime());//操作时间
				cancelitem.setOperDpcd(ShiroSessionUtils.getCurrentUserDepartmentFromShiroSession().getDeptCode());//操作员所在科室
				cancelitem.setOperDpcdName(ShiroSessionUtils.getCurrentUserDepartmentFromShiroSession().getDeptName());//操作员所在科室名称
				cancelitem.setRecipeNo(modls.getRecipeNo());//对应收费明细处方号
				cancelitem.setSequenceNo(modls.getSequenceNo());//对应处方内流水号
				cancelitem.setBillNo(feedetail.getInvoiceNo());//确认单号
				cancelitem.setConfirmFlag(0); //退药确认标识 0未确认/1确认
				cancelitem.setChargeFlag(0);//退费标识 0未退费/1退费/2作废
				cancelitem.setExtFlag(modls.getExtFlag3());//1 包装 单位 0, 最小单位
				cancelitem.setQty(modls.getNobackNum());//数量[22]
				cancelitem.setCardNo(info.getMidicalrecordId());//病历卡号
				cancelitem.setReturnReason("门诊退费");
				cancelitem.setCreateTime(DateUtils.getCurrentTime());
				cancelitem.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				cancelitem.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				cancelitem.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
				cancelitem.setUpdateTime(DateUtils.getCurrentTime());
				refundApplyDAO.save(cancelitem);
				
				feedetail.setNobackNum(feedetail.getQty() - modls.getNobackNum());
				refundApplyDAO.update(feedetail);
				//医技项目处理
				if(StringUtils.isNotBlank(feedetail.getSampleId())){
					//确认项目处理
					TecTerminalApply ter = refundDAO.getTerByapplyNum(feedetail.getSampleId());
					if(ter!=null){
						if(ter.getConfirmNum()!=null&&ter.getConfirmNum()>0){//存在已经确认的项目，把已经确认的项目重新生成
							TecTerminalApply t = new TecTerminalApply();
							BeanUtils.copyProperties(ter, t);
							t.setQty(ter.getConfirmNum().doubleValue());
							t.setUpdateTime(new Date());
							t.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
							t.setExtFlag1("1");
							t.setId(null);
							refundDAO.save(t);
						}
						
						ter.setExtFlag1("0");
						refundDAO.save(ter);
					}
					//预约项目处理
					TecApply tec = refundDAO.getTecByapplyNum(feedetail.getSampleId());
					if(tec!=null){
						tec.setStatus(3);//更新状态为作废
						refundDAO.save(tec);
					}
				}
			}
		}
		map.put("resMsg", "success");
		map.put("resCode", "申请成功");
		return map;
	}
	

}
