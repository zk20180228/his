package cn.honry.inpatient.settlementRecall.dao.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessBedward;
import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessHospitalbed;
import cn.honry.base.bean.model.FinanceInvoice;
import cn.honry.base.bean.model.FinanceUsergroup;
import cn.honry.base.bean.model.InpatientBalanceHeadNow;
import cn.honry.base.bean.model.InpatientBalanceListNow;
import cn.honry.base.bean.model.InpatientBalancePayNow;
import cn.honry.base.bean.model.InpatientBedinfoNow;
import cn.honry.base.bean.model.InpatientDerate;
import cn.honry.base.bean.model.InpatientFeeInfoNow;
import cn.honry.base.bean.model.InpatientInPrepayNow;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientItemListNow;
import cn.honry.base.bean.model.InpatientMedicineListNow;
import cn.honry.base.bean.model.InpatientShiftData;
import cn.honry.base.bean.model.PatientAccountrepaydetail;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.inpatient.docAdvManage.dao.InpatientOrderNowDAO;
import cn.honry.inpatient.outBalance.dao.OutBalanceDAO;
import cn.honry.inpatient.settlementRecall.dao.SettlementRecallDAO;
import cn.honry.inpatient.settlementRecall.vo.VSettlementRecall;
import cn.honry.utils.DateUtils;
import cn.honry.utils.ShiroSessionUtils;
@Repository("settlementRecallDAO")
@SuppressWarnings({"all"})
public class SettlementRecallDAOImpl extends HibernateEntityDao<VSettlementRecall> implements SettlementRecallDAO{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Autowired
	@Qualifier(value = "outBalanceDAO")
	private OutBalanceDAO outBalanceDAO;
	@Override
	public InpatientBalanceHeadNow queryHeadByInvoiceNo(String invoiceNoSearch) throws Exception {
		InpatientBalanceHeadNow head = null;
		String hql="FROM InpatientBalanceHeadNow i WHERE  i.del_flg = 0 and i.wasteFlag=1 ";
		hql = hql +" and i.invoiceNo='"+invoiceNoSearch+"'";
		List<InpatientBalanceHeadNow> lst = null;
		try {
			lst =  this.getSession().createQuery(hql).list();
			if(lst!=null){
				if(lst.size()>0){
					head = lst.get(0);//获得第一条记录的头表信息
				}
			}
		} catch (Exception e) {
			throw e;
		} 
		return head;
	}
	@Override
	public List<InpatientInfoNow> queryInfo(String inpatientNo) throws Exception {
		String hql="FROM InpatientInfoNow i WHERE ";
		hql = hql +" i.inpatientNo = '"+ inpatientNo +"'";
		List<InpatientInfoNow> iList = super.find(hql, null);
		if(iList!=null&&iList.size()>0){
			return iList;
		}
		return new ArrayList<InpatientInfoNow>();
	}
	@Override
	public List<InpatientInfoNow> queryInfomedicalrecordId(String medicalrecordId) throws Exception {
		InpatientInfo info = null;
		String hql="FROM InpatientInfoNow i WHERE ";
		hql = hql +" (i.medicalrecordId = '"+ medicalrecordId +"' )";
		List<InpatientInfoNow> iList = super.find(hql, null);
		if(iList!=null&&iList.size()>0){
			return iList;
		}
		return new ArrayList<InpatientInfoNow>();
	}
	@Override
	public List<InpatientBalanceHeadNow> queryHeadByInpatientNoAndBalanceNo(
			String inpatientNoSearch, String balanceNoSearch) throws Exception {
		String hql="FROM InpatientBalanceHeadNow i WHERE  i.del_flg = 0 and i.wasteFlag=1 ";
		hql = hql +" and i.inpatientNo='"+ inpatientNoSearch +"' and i.balanceNo="+Integer.parseInt(balanceNoSearch)+"";
		List<InpatientBalanceHeadNow> lst = null;
		try {
			lst =  this.getSession().createQuery(hql).list();
		} catch (Exception e) {
			throw e;
		} 
		if(lst==null){
			return null;
		}
		return lst;
	}

	@Override
	public List<InpatientBalanceListNow> getBalanceListInpatientNoAndBalanceNo(
			String inpatientNoSearch, String balanceNoSearch) throws Exception {
		String hql="FROM InpatientBalanceListNow i WHERE i.del_flg = 0 ";
			   hql = hql+" AND i.inpatientNo = '"+inpatientNoSearch+"' ";
			   hql = hql+" AND i.balanceNo="+balanceNoSearch+"";
	           hql = hql+" ORDER BY i.balanceNo";
		List<InpatientBalanceListNow> balanceList = null;
		try {
			balanceList = getSession().createQuery(hql).list();
		} catch (Exception e) {
			throw e;
		} 
		if(balanceList!=null&&balanceList.size()>0){
			return balanceList;
		}
		return new ArrayList<InpatientBalanceListNow>();
	}

	@Override
	public List<InpatientInPrepayNow>  getPrepayByInpatientNoAndBalanceNo(
			String inpatientNoSearch, String balanceNoSearch) throws Exception  {
		String hql="FROM InpatientInPrepayNow i WHERE i.del_flg = 0 ";
		       hql = hql+" AND i.inpatientNo = '"+inpatientNoSearch+"' ";
			   hql = hql+" AND i.balanceNo="+balanceNoSearch+" and i.prepayState=0 and i.balanceState=1 ";
			   hql = hql+" ORDER BY i.id ";
		List<InpatientInPrepayNow> detailList = getSession().createQuery(hql).list();
		if(detailList!=null&&detailList.size()>0){
			return detailList;
		}
		return new ArrayList<InpatientInPrepayNow>();
	}
	@Override
	public List<InpatientBalanceHeadNow> queryBalanceHead(String inpatientNo) throws Exception {
		String hql="FROM InpatientBalanceHeadNow i WHERE  i.del_flg = 0 and i.wasteFlag=1 ";
			   hql = hql +" and i.inpatientNo = '"+ inpatientNo +"' order by i.balanceDate";
		List<InpatientBalanceHeadNow> lst = null;
		try {
			lst =  this.getSession().createQuery(hql).list();
		} catch (Exception e) {
			throw e;
		} 
		return lst;
	}
	@Override
	public List<InpatientBalancePayNow> getBlancePayList(String invoiceNo,
			String balanceNo) throws Exception {
		String hql="FROM InpatientBalancePayNow i WHERE  i.del_flg = 0  ";
		   hql = hql +" and i.invoiceNo = '"+ invoiceNo +"' and i.balanceNo="+Integer.parseInt(balanceNo)+" and i.transKind = 1 ";
		List<InpatientBalancePayNow> lst = null;
		try {
			lst =  this.getSession().createQuery(hql).list();
		} catch (Exception e) {
			throw e;
		} 
		return lst;
		}
	
	@Override
	public List<InpatientFeeInfoNow> getFeeInfo(String invoiceNo, String inpatientNo) throws Exception {
		String hql="FROM InpatientFeeInfoNow i WHERE  i.del_flg = 0 ";
		   hql = hql +" and i.invoiceNo = '"+ invoiceNo +"' and i.inpatientNo='"+inpatientNo+"' and i.balanceState=1 ";
		List<InpatientFeeInfoNow> lst = null;
		try {
			lst =  this.getSession().createQuery(hql).list();
		} catch (Exception e) {
			throw e;
		} 
		return lst;
		}
	@Override
	public int getNewBalanceNo(String inpatientNo) throws Exception {
		String hql="select NVL(balanceNo,0) from InpatientInfoNow e WHERE  ";
		hql = hql +"  e.inpatientNo = '"+ inpatientNo +"'";
		String maxValue = getSession().createQuery(hql).uniqueResult().toString();
		Integer retVal = Integer.parseInt(maxValue.toString());
		return retVal+1;
	}
	@Override
	public void updateMedicineList(String balanceNo, String inpatientNo, int newBalanceNo) throws Exception {
		List<InpatientMedicineListNow> inpatientMedicineList = getInpatientMedicineList(balanceNo,inpatientNo);
		if(inpatientMedicineList!=null){
			if(inpatientMedicineList.size()>0){
				for(int i=0;i<inpatientMedicineList.size();i++){
					InpatientMedicineListNow medicine = inpatientMedicineList.get(i);
					medicine.setBalanceState(0);
					medicine.setInvoiceNo(null);
					medicine.setBalanceNo(newBalanceNo);
					super.update(medicine);
					OperationUtils.getInstance().conserve(medicine.getId(),"结算召回","UPDATE","T_INPATIENT_MEDICINELIST_NOW",OperationUtils.LOGACTIONUPDATE);
				}
			}
		}
	}
    
	public List<InpatientMedicineListNow> getInpatientMedicineList(
			String balanceNo, String inpatientNo) throws Exception {
		String hql="FROM InpatientMedicineListNow i WHERE i.del_flg = 0 ";
			   hql = hql+" AND i.inpatientNo = '"+inpatientNo+"' ";
			   hql = hql+" AND i.balanceNo="+Integer.parseInt(balanceNo)+" and i.balanceState=1 ";
		List<InpatientMedicineListNow> balanceList = null;
		try {
			balanceList = getSession().createQuery(hql).list();
		} catch (Exception e) {
			throw e;
		} 
		if(balanceList!=null&&balanceList.size()>0){
			return balanceList;
		}
		return new ArrayList<InpatientMedicineListNow>();
	}
	@Override
	public void updateItemList(String balanceNo, String inpatientNo,
			int newBalanceNo) throws Exception {
		List<InpatientItemListNow> inpatientItemList = getInpatientItemList(balanceNo,inpatientNo);
		if(inpatientItemList!=null){
			if(inpatientItemList.size()>0){
				for(int i=0;i<inpatientItemList.size();i++){
					InpatientItemListNow item = inpatientItemList.get(i);
					item.setBalanceState(0);
					item.setInvoiceNo(null);
					item.setBalanceNo(newBalanceNo);
					super.update(item);
					OperationUtils.getInstance().conserve(item.getId(),"结算召回","UPDATE","T_INPATIENT_ITEMLIST_NOW",OperationUtils.LOGACTIONUPDATE);
				}
			}
		}
	}
	private List<InpatientItemListNow> getInpatientItemList(String balanceNo,
			String inpatientNo) throws Exception {
		String hql="FROM InpatientItemListNow i WHERE i.del_flg = 0 ";
		       hql = hql+" AND i.inpatientNo = '"+inpatientNo+"' ";
		       hql = hql+" AND i.balanceNo="+Integer.parseInt(balanceNo)+" and i.balanceState=1 ";
	List<InpatientItemListNow> balanceList = null;
	try {
		balanceList = getSession().createQuery(hql).list();
	} catch (Exception e) {
		throw e;
	} 
	if(balanceList!=null&&balanceList.size()>0){
		return balanceList;
	}
	return new ArrayList<InpatientItemListNow>();
	}
	@Override
	public void updateInpatientInfo(String inpatientNo, String balanceHeadIds, int newBalanceNo,String patientInfoId) throws Exception {
		//获得患者住院信息的住院状态
		String inState="";
		InpatientInfoNow inpatientInfo = super.get(InpatientInfoNow.class,patientInfoId);
		inState=inpatientInfo.getInState();
		List<InpatientBalanceHeadNow> balanceHeadList = new ArrayList();
		if(balanceHeadIds.indexOf("_")!=-1){
			String[] balanceIdArr  = balanceHeadIds.split("_");
			for(int i=0;i<balanceIdArr.length;i++){
				InpatientBalanceHeadNow payObj = this.get(InpatientBalanceHeadNow.class,balanceIdArr[i]);//根据页面结算对象id获得结算头表信息 结算对象
				balanceHeadList.add(payObj);
			}
		}else{
			InpatientBalanceHeadNow payObj = this.get(InpatientBalanceHeadNow.class,balanceHeadIds);//根据页面结算对象id获得结算头表信息 结算对象
			balanceHeadList.add(payObj);
		}
		for(int x=0;x<balanceHeadList.size();x++){
			InpatientBalanceHeadNow head = balanceHeadList.get(x);
			if(head.getForegiftCost()!=null&&head.getForegiftCost()>0){//转押金
				//根据住院号更新住院主表信息
				updateInfoPatientInfo(inpatientNo,newBalanceNo,head,inState);
			}else{
				updatePatientInfo(inpatientNo,newBalanceNo,head,inState);
			}
		}
		
	}
	
	private void updatePatientInfo(String inpatientNo, int newBalanceNo,
			InpatientBalanceHeadNow head, String inState) throws Exception {
		List<InpatientInfoNow> info = queryInpatientInfoList(inpatientNo);
		if(null!=info&&info.size()>0){
			for(int i=0;i<info.size();i++){
				InpatientInfoNow inpatientInfo = info.get(i);
				//住院状态
				if(inState!=null){
					if(!"".equals(inState)){
						if("O".equalsIgnoreCase(inState)||"Q".equalsIgnoreCase(inState)){
							inpatientInfo.setInState("B"); 
						}
					}
				}
				//预交金额=预交金额+结算对象.预交金
				double prepayCost = 0;
				if(inpatientInfo.getPrepayCost()!=null){
					prepayCost = inpatientInfo.getPrepayCost();
				}
				double headPrepayCost = 0;
				if(head.getPrepayCost()!=null){
					if(head.getPrepayCost()==0){
						headPrepayCost = 0;
					}else{
						headPrepayCost = inpatientInfo.getPrepayCost();
					}
				}
				inpatientInfo.setPrepayCost(prepayCost+headPrepayCost);
				//费用金额=费用金额+结算对象.总金额
				double totCost=0;
				double headTotalCost=0;
				if(inpatientInfo.getTotCost()!=null){
					totCost = inpatientInfo.getTotCost().doubleValue();
				}
				if(head.getTotCost()!=null){
					headTotalCost=head.getTotCost().doubleValue() ;
				}
				inpatientInfo.setTotCost(totCost+headTotalCost);
				//自费金额=自费金额+结算对象.自费金额
				double ownCost = 0;
				double headOwnCost = 0;
				if(inpatientInfo.getOwnCost()!=null){
					ownCost= inpatientInfo.getOwnCost().doubleValue();
				}
				if(head.getOwnCost()!=null){
			    	headOwnCost=head.getOwnCost().doubleValue();
			    }
				inpatientInfo.setOwnCost(ownCost+headOwnCost);
				//公费金额=公费金额+结算对象.公费金额
				double pubCost = 0;
				double headPubCost = 0;
				if(inpatientInfo.getPubCost()!=null){
					pubCost=inpatientInfo.getPubCost().doubleValue();
				}
				if(head.getPubCost()!=null){
					headPubCost=head.getPubCost().doubleValue();
				}
				inpatientInfo.setPubCost(pubCost+headPubCost);
				//自付金额=自付金额+结算对象.自付金额
				double payCost =0;
				double headPayCost=0;
				if(inpatientInfo.getPayCost()!=null){
					payCost=inpatientInfo.getPayCost().doubleValue();
				}
				if(head.getPayCost()!=null){
					headPayCost=head.getPayCost().doubleValue();
				}
				inpatientInfo.setPayCost(payCost+headPayCost);
				//优惠金额=优惠金额+结算对象.优惠金额
				double ecoCost =0;
				double headEcoCost=0;
				if(inpatientInfo.getEcoCost()!=null){
					ecoCost=inpatientInfo.getEcoCost().doubleValue();
				}
				if(head.getEcoCost()!=null){
					headEcoCost=head.getEcoCost().doubleValue();
				}
				inpatientInfo.setEcoCost(ecoCost+headEcoCost);
				//余额=预交金+结算对象.预交金-自费-结算对象.自费-自付-结算对象.自付
				inpatientInfo.setFreeCost(prepayCost+headPrepayCost-ownCost-headOwnCost-payCost-headPayCost);
				//转入预交金额=转入预交金额+结算对象.转入总费用
				double changePrepaycost = 0;
				double headChangeTotcost=0;
				if(inpatientInfo.getChangePrepaycost()!=null){
			    	changePrepaycost=inpatientInfo.getChangePrepaycost().doubleValue();
			    }
				if(head.getChangeTotcost()!=null){
					headChangeTotcost=head.getChangeTotcost().doubleValue();
				}
				inpatientInfo.setChangePrepaycost(changePrepaycost+headChangeTotcost);
				//转入费用金额=转入费用+结算对象.转入总费用
				double changeTotcost=0;
				if(inpatientInfo.getChangeTotcost()!=null){
					changeTotcost=inpatientInfo.getChangeTotcost().doubleValue();
				}
				inpatientInfo.setChangeTotcost(changeTotcost+headChangeTotcost);
				//已结
				//预交金额=预交金额-结算对象.预交金-结算对象.转入总费用-结算对象.转入预交金
				double balancePrepay =0;
				if(inpatientInfo.getBalancePrepay()!=null){
					balancePrepay=inpatientInfo.getBalancePrepay().doubleValue();
				}
				double headChangePrepaycost=0;
				if(head.getChangePrepaycost()!=null){
					headChangePrepaycost=head.getChangePrepaycost().doubleValue();
				}
				inpatientInfo.setBalancePrepay(balancePrepay-headPrepayCost-headChangeTotcost-headChangePrepaycost);
				//结算日期(上次)
				inpatientInfo.setBalanceDate(null);
				//结算序号为新结算序号
				inpatientInfo.setBalanceNo(newBalanceNo);
				super.update(inpatientInfo);
				OperationUtils.getInstance().conserve(inpatientInfo.getId(),"结算召回","UPDATE","T_INPATIENT_INFO_NOW",OperationUtils.LOGACTIONUPDATE);
			}
		}
	}
	private void updateInfoPatientInfo(String inpatientNo,int newBalanceNo,InpatientBalanceHeadNow head, String inState) throws Exception {
		List<InpatientInfoNow> info = queryInpatientInfoList(inpatientNo);
		if(null!=info&&info.size()>0){
			for(int i=0;i<info.size();i++){
				InpatientInfoNow inpatientInfo = info.get(i);
				//住院状态
				if(inState!=null){
					if(!"".equals(inState)){
						if("O".equalsIgnoreCase(inState)||"Q".equalsIgnoreCase(inState)){
							inpatientInfo.setInState("B"); 
						}
					}
				}
				//结算日期(上次)
				inpatientInfo.setBalanceDate(null);
				//结算序号为新结算序号
				inpatientInfo.setBalanceNo(newBalanceNo);
				//转交金额(未接)=转交金额未接+结算对象中的自费金额
			    double changePrepaycost = 0;
			    double headOwnCost = 0;
			    if(inpatientInfo.getChangePrepaycost()!=null){
			    	changePrepaycost=inpatientInfo.getChangePrepaycost().doubleValue();
			    }
			    if(head.getOwnCost()!=null){
			    	headOwnCost=head.getOwnCost().doubleValue();
			    }
				inpatientInfo.setChangePrepaycost(changePrepaycost+headOwnCost);
				//费用金额=费用金额+结算对象.总金额
				double totCost=0;
				double headTotalCost=0;
				if(inpatientInfo.getTotCost()!=null){
					totCost = inpatientInfo.getTotCost().doubleValue();
				}
				if(head.getTotCost()!=null){
					headTotalCost=head.getTotCost().doubleValue() ;
				}
				inpatientInfo.setTotCost(totCost+headTotalCost);
				//自费金额=自费金额+结算对象.自费金额
				double ownCost = 0;
				if(head.getOwnCost()!=null){
					ownCost= inpatientInfo.getOwnCost().doubleValue();
				}
				inpatientInfo.setOwnCost(ownCost+headOwnCost);
				//公费金额=公费金额+结算对象.公费金额
				double pubCost = 0;
				double headPubCost = 0;
				if(inpatientInfo.getPubCost()!=null){
					pubCost=inpatientInfo.getPubCost().doubleValue();
				}
				if(head.getPubCost()!=null){
					headPubCost=head.getPubCost().doubleValue();
				}
				inpatientInfo.setPubCost(pubCost+headPubCost);
				//自付金额=自付金额+结算对象.自付金额
				double payCost =0;
				double headPayCost=0;
				if(inpatientInfo.getPayCost()!=null){
					payCost=inpatientInfo.getPayCost().doubleValue();
				}
				if(head.getPayCost()!=null){
					headPayCost=head.getPayCost().doubleValue();
				}
				inpatientInfo.setPayCost(payCost+headPayCost);
				//优惠金额=优惠金额+结算对象.优惠金额
				double ecoCost =0;
				double headEcoCost=0;
				if(inpatientInfo.getEcoCost()!=null){
					ecoCost=inpatientInfo.getEcoCost().doubleValue();
				}
				if(head.getEcoCost()!=null){
					headEcoCost=head.getEcoCost().doubleValue();
				}
				inpatientInfo.setEcoCost(ecoCost+headEcoCost);
				//余额=预交金额-自费金额
				double prepayCost = 0;
				if(inpatientInfo.getPrepayCost()!=null){
					prepayCost=inpatientInfo.getPrepayCost().doubleValue();
				}
				inpatientInfo.setFreeCost(prepayCost-ownCost);
				//转入预交金额=转入预交金额+结算对象.转入总费用
				double headChangeTotcost=0;
				if(head.getChangeTotcost()!=null){
					headChangeTotcost=head.getChangeTotcost().doubleValue();
				}
				inpatientInfo.setChangePrepaycost(changePrepaycost+headChangeTotcost);
				//转入费用金额=转入费用金额+结算对象.转入总费用
				double changeTotcost=0;
				if(inpatientInfo.getChangeTotcost()!=null){
					changeTotcost=inpatientInfo.getChangeTotcost().doubleValue();
				}
				inpatientInfo.setChangeTotcost(changeTotcost+headChangeTotcost);
				//已结
				//预交金额=预交金额-结算对象.预交金
				double balancePrepay =0;
				if(inpatientInfo.getBalancePrepay()!=null){
					balancePrepay=inpatientInfo.getBalancePrepay().doubleValue();
				}
				double headPrepayCost=0;
				if(head.getPrepayCost()!=null){
					headPrepayCost=head.getPrepayCost().doubleValue();
				}
				inpatientInfo.setBalancePrepay(balancePrepay-headPrepayCost);
				//费用金额=费用金额-结算对象.总金额-结算对象.转入总费用
				double balanceCost =0;
				if(inpatientInfo.getBalanceCost()!=null){
					balanceCost=inpatientInfo.getBalanceCost().doubleValue();
				}
				inpatientInfo.setBalanceCost(balanceCost-headTotalCost-headChangeTotcost);
				super.update(inpatientInfo);
				OperationUtils.getInstance().conserve(inpatientInfo.getId(),"结算召回","UPDATE","T_INPATIENT_INFO_NOW",OperationUtils.LOGACTIONUPDATE);
			}
		}
	}
	public List<InpatientInfoNow> queryInpatientInfoList(String inpatientNo) throws Exception {
		String hql="FROM InpatientInfoNow i WHERE ";
		hql = hql +"  i.inpatientNo = '"+ inpatientNo +"'";
		List<InpatientInfoNow> lst = super.find(hql, null);
		if(lst!=null&&lst.size()>0){
			return lst;
		}
		return new ArrayList<InpatientInfoNow>();
	}
	
	@Override
	public void dealPrepay(String inpatientNo,String payObjId,Date curDate, int newBalanceNo, String prePayIds, String financeGroupId, String maxInvoiceNo) throws Exception {
		InpatientBalanceHeadNow payObj = this.get(InpatientBalanceHeadNow.class,payObjId);//根据页面结算对象id获得结算头表信息 结算对象
		String curUserId = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String curDeptId = ShiroSessionUtils.getCurrentUserDepartmentFromShiroSession().getDeptCode();
		String [] prePayId = prePayIds.split("_");
		List<InpatientInfoNow> inpatientInfo = queryInfo(inpatientNo);
		for (String Id : prePayId) {
		//1.将收取预交金状态设置为3（召回）
			InpatientInPrepayNow prepayNow = outBalanceDAO.queryInpatientInPrepayById(Id);
			prepayNow.setPrepayState(3);
			super.save(prepayNow);
		//2.然后生成冲账记录，预交金额取原记录的负值；预交金状态为1作废；
			InpatientInPrepayNow prepayNow1 = new InpatientInPrepayNow();
			prepayNow1.setId(null);
			prepayNow1.setInpatientNo(inpatientNo);
			//预交金=预交金对象取反
			double prepayCost=0;
			if(prepayNow.getPrepayCost()!=null){
				prepayCost=-prepayNow.getPrepayCost().doubleValue();
			}
			prepayNow1.setPrepayCost(prepayCost);
			prepayNow1.setPayWay(prepayNow.getPayWay());
			//发生序号
			InpatientInPrepayNow PrepayList = queryHappenNo(inpatientNo);
			prepayNow1.setHappenNo(PrepayList.getHappenNo()+1);
			prepayNow1.setBalanceState(2);
			//姓名
			prepayNow1.setName(inpatientInfo.get(0).getPatientName());
			//科室代码 
			prepayNow1.setDeptCode(inpatientInfo.get(0).getDeptCode());
			prepayNow1.setDeptName(inpatientInfo.get(0).getDeptName());
			//交易类型=反交易 
			prepayNow1.setTransType(2);
			//预交金状态=‘1’
			prepayNow1.setPrepayState(1);
			//原始发票号->原票据号 预交金对象.预交金票据号
			prepayNow1.setReceiptNo(prepayNow.getReceiptNo());
			
			//结算序号=新结算序号
			prepayNow1.setBalanceNo(newBalanceNo);
			//预交金操作人->>>建立人员
			prepayNow1.setCreateUser(curUserId);
			//预交金操作时间 ->>>建立时间
			prepayNow1.setCreateTime(curDate);
			//预交金操作科室  ->>>建立科室
			prepayNow1.setCreateDept(curDeptId);
			super.save(prepayNow1);
		//3.然后生成正交易记录
			InpatientInPrepayNow prepayNow2 = new InpatientInPrepayNow();
			prepayNow2.setId(null);
			prepayNow2.setInpatientNo(inpatientNo);
			//预交金=预交金对象取反
			double prepayCost1=0;
			if(prepayNow.getPrepayCost()!=null){
				prepayCost1=prepayNow.getPrepayCost().doubleValue();
			}
			prepayNow2.setPrepayCost(prepayCost1);
			prepayNow2.setPayWay(prepayNow.getPayWay());
			//发生序号
			InpatientInPrepayNow PrepayList1 = queryHappenNo(inpatientNo);
			prepayNow2.setHappenNo(PrepayList1.getHappenNo()+1);
			//姓名
			prepayNow2.setName(inpatientInfo.get(0).getPatientName());
			//科室代码 
			prepayNow2.setDeptCode(inpatientInfo.get(0).getDeptCode());
			prepayNow2.setDeptName(inpatientInfo.get(0).getDeptName());
			//交易类型=反交易 
			prepayNow2.setTransType(1);
			//预交金状态=‘1’
			prepayNow2.setPrepayState(0);
			//原始发票号->原票据号 预交金对象.预交金票据号
			prepayNow2.setReceiptNo(prepayNow.getReceiptNo());
			prepayNow2.setBalanceState(0);
			//结算序号=新结算序号
			prepayNow2.setBalanceNo(newBalanceNo);
			//预交金操作人->>>建立人员
			prepayNow2.setCreateUser(curUserId);
			//预交金操作时间 ->>>建立时间
			prepayNow2.setCreateTime(curDate);
			//预交金操作科室  ->>>建立科室
			prepayNow2.setCreateDept(curDeptId);
			super.save(prepayNow2);
		}
		OperationUtils.getInstance().conserve(null,"结算召回","INSERT INTO","T_INPATIENT_INPREPAY_NOW",OperationUtils.LOGACTIONUPDATE);
	}
	@Override
	public void dealBalanceList(String balanceListIds,int newBalanceNo,Date curDate) throws Exception {
		try {
			//循环处理页面中结算明细列表中的 数据
			String curUserId = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
			List<InpatientBalanceListNow> lst = new ArrayList();
			if(balanceListIds.indexOf("_")!=-1){
				String[]balanceListArr = balanceListIds.split("_");
				for(int i=0;i<balanceListArr.length;i++){
					InpatientBalanceListNow inpatientBalanceList = this.get(InpatientBalanceListNow.class, balanceListArr[i]);
					lst.add(inpatientBalanceList);
				}
			}else{
				InpatientBalanceListNow inpatientBalanceList = this.get(InpatientBalanceListNow.class, balanceListIds);
					lst.add(inpatientBalanceList);
			}
			
			if(lst!=null){
				if(lst.size()>0){
					for(int x=0;x<lst.size();x++){
						InpatientBalanceListNow balanceList = lst.get(x);
						//生成负交易记录
						InpatientBalanceListNow newBalanceList = new InpatientBalanceListNow();
						//交易类型  反交易
						newBalanceList.setTransType(2);
						newBalanceList.setInpatientNo(balanceList.getInpatientNo());
						newBalanceList.setDeptName(balanceList.getDeptName());						
						newBalanceList.setName(balanceList.getName());				
						//结算序号=新结算序号
						newBalanceList.setBalanceNo(newBalanceNo);
						//费用金额=住院结算明细.费用金额
						newBalanceList.setTotCost(balanceList.getTotCost());
						//自付=- 结算明细.自付
						double payCost =0;
						if(balanceList.getPayCost()!=null){
							payCost=-balanceList.getPayCost().doubleValue();
						}
						newBalanceList.setPayCost(payCost);
						
						newBalanceList.setTotCost(balanceList.getTotCost());
						//自费=- 结算明细.自费
						double ownCost =0;
						if(balanceList.getOwnCost()!=null){
							ownCost=-balanceList.getOwnCost().doubleValue();
						}
						newBalanceList.setOwnCost(ownCost);
						//公共=-结算明细.公共
						double pubCost = 0;
						if(balanceList.getPubCost()!=null){
							pubCost=-balanceList.getPubCost().doubleValue();
						}
						newBalanceList.setPubCost(pubCost);
						//优惠金额=-结算明细.优惠金额
						double ecoCost =0;
						if(balanceList.getEcoCost()!=null){
							ecoCost=-balanceList.getEcoCost().doubleValue();
						}
						newBalanceList.setEcoCost(ecoCost);
						//结算人
						newBalanceList.setBalanceOpercode(curUserId);
						//结算时间
						newBalanceList.setBalanceDate(curDate);
					    super.save(newBalanceList);
					    OperationUtils.getInstance().conserve(null,"ICD管理","INSERT INTO","T_INPATIENT_BALANCELIST_NOW",OperationUtils.LOGACTIONINSERT);
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	public void dealBalanceHead(String balanceHeadIds,int newBalanceNo, Date curDate,String financeGroupId) throws Exception {
		try {
			String curUserId = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
			//循环处理页面中结算明细列表中的 数据
			List<InpatientBalanceHeadNow> lst = new ArrayList();
			if(balanceHeadIds.indexOf("_")!=-1){
				String[] balanceHeadArr = balanceHeadIds.split("_");
				for(int i=0;i<balanceHeadArr.length;i++){
					InpatientBalanceHeadNow inpatientBalanceHead = this.get(InpatientBalanceHeadNow.class, balanceHeadArr[i]);
					lst.add(inpatientBalanceHead);
				}
			}else{
				InpatientBalanceHeadNow inpatientBalanceHead = this.get(InpatientBalanceHeadNow.class, balanceHeadIds);
				   lst.add(inpatientBalanceHead);
			}
			if(lst!=null&&lst.size()>0){
				for(int x=0;x<lst.size();x++){
					InpatientBalanceHeadNow balanceHead = lst.get(x);
					/*******插入负交易记录到住院结算头表**************************************************************/
					InpatientBalanceHeadNow newBalanceHead = new InpatientBalanceHeadNow();
					//补收金额=结算对象.返还金额
					newBalanceHead.setSupplyCost(balanceHead.getReturnCost());
					//返还金额=结算对象.补收金额
					newBalanceHead.setReturnCost(balanceHead.getSupplyCost());
					//结算序号=新结算序号
					newBalanceHead.setBalanceNo(newBalanceNo);
					//交易类型=反交易
					newBalanceHead.setTransType(2);
					//结算人
					newBalanceHead.setBalanceOpercode(curUserId);
					//结算时间
					newBalanceHead.setBalanceDate(curDate);
					//发票类别为0？？>>>暂存至ext_code
					newBalanceHead.setExtCode("0");
					//财务组
					newBalanceHead.setFingrpCode(financeGroupId);
					//自付=- 结算明细.自付
					double payCost =0;
					if(balanceHead.getPayCost()!=null){
						payCost=-balanceHead.getPayCost().doubleValue();
					}
					newBalanceHead.setPayCost(payCost);
					//自费=- 结算明细.自费
					double ownCost =0;
					if(balanceHead.getOwnCost()!=null){
						ownCost=-balanceHead.getOwnCost().doubleValue();
					}
					newBalanceHead.setOwnCost(ownCost);
					//费用金额=-结算.总金额（费用）
					double totCost=0;
					if(balanceHead.getTotCost()!=null){
						totCost=-balanceHead.getTotCost().doubleValue();
					}
					newBalanceHead.setTotCost(totCost);
					//公共=-结算明细.公共
					double pubCost = 0;
					if(balanceHead.getPubCost()!=null){
						pubCost=-balanceHead.getPubCost().doubleValue();
					}
					newBalanceHead.setPubCost(pubCost);
					//优惠金额=-结算明细.优惠金额
					double ecoCost =0;
					if(balanceHead.getEcoCost()!=null){
						ecoCost=-balanceHead.getEcoCost().doubleValue();
					}
					newBalanceHead.setEcoCost(ecoCost);
					//减免金额=-结算对象.减免
					double derCost=0;
					if(balanceHead.getDerCost()!=null){
						derCost=-balanceHead.getDerCost().doubleValue();
					}
					newBalanceHead.setDerCost(derCost);
					//转入费用=-结算.转入费用
					double changeTotcost = 0;
					if(balanceHead.getChangeTotcost()!=null){
						changeTotcost=-balanceHead.getChangeTotcost().doubleValue();
					}
					newBalanceHead.setChangeTotcost(changeTotcost);
					//转入预交金=-结算.转入预交金额
					double changePrepaycost =0;
					if(balanceHead.getChangePrepaycost()!=null){
						changePrepaycost=-balanceHead.getChangePrepaycost().doubleValue();
					}
					newBalanceHead.setChangePrepaycost(changePrepaycost);
					//转入预交金=-结算.转入预交金额
					double prepayCost =0;
					if(balanceHead.getPrepayCost()!=null){
						prepayCost=-balanceHead.getPrepayCost().doubleValue();
					}
					newBalanceHead.setPrepayCost(prepayCost);
					super.save(newBalanceHead);
					OperationUtils.getInstance().conserve(null,"结算召回","INSERT INTO","T_INPATIENT_BALANCEHEAD_NOW",OperationUtils.LOGACTIONINSERT);
					/*******原始记录作废**************************************************************/
					balanceHead.setWasteFlag(0);
					//作废操作人员
					balanceHead.setWasteOpercode(curUserId);
					super.update(balanceHead);
					OperationUtils.getInstance().conserve(balanceHead.getId(),"结算召回","UPDATE","T_INPATIENT_BALANCEHEAD_NOW",OperationUtils.LOGACTIONUPDATE);
					
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	public void dealBalancePay(List<InpatientBalancePayNow> payModeList,
			int newBalanceNo, Date curDate) throws Exception {
		try {
			String curUserId = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
			for(int i=0;i<payModeList.size();i++){
				InpatientBalancePayNow balancePay = payModeList.get(i);
				//插入一条新的负交易记录
				InpatientBalancePayNow newPay = new InpatientBalancePayNow();
				//交易类型为反交易
				newPay.setTransType(2);
				//费用金额=-payMode.费用总额
				double cost = 0;
				if(balancePay.getCost()!=null){
					cost=-balancePay.getCost().doubleValue();
				}
				newPay.setCost(cost);
				//张数=-payMode.张数
				int countNum=0;
				if(balancePay.getCountNum()!=null){
					countNum=-balancePay.getCountNum().intValue();
				}
				newPay.setCountNum(countNum);
				//结算人
				newPay.setBalanceOpercode(curUserId);
				//结算时间为当前时间
				newPay.setBalanceDate(curDate);
				newPay.setBalanceNo(newBalanceNo);
				super.save(newPay);
				OperationUtils.getInstance().conserve(null,"结算召回","INSERT INTO","T_INPATIENT_BALANCEPAY_NOW",OperationUtils.LOGACTIONINSERT);
			}
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	public void dealDerate(String inpatientNo, String balanceNo, Date curDate, int newBalanceNo) throws Exception {
			List<InpatientDerate> lst = getDerateList(inpatientNo,balanceNo);
			if(lst!=null){
				if(lst.size()>0){
					for(int i=0;i<lst.size();i++){
						InpatientDerate derate = lst.get(i);
						InpatientDerate fDerate = getNewDerate(derate,curDate,newBalanceNo,"-");
						InpatientDerate zDerate = getNewDerate(derate,curDate,newBalanceNo,"+");
					    super.save(fDerate);
					    OperationUtils.getInstance().conserve(null,"结算召回","INSERT INTO","T_INPATIENT_DERATE",OperationUtils.LOGACTIONINSERT);
					    super.save(zDerate);
					    OperationUtils.getInstance().conserve(null,"结算召回","INSERT INTO","T_INPATIENT_DERATE",OperationUtils.LOGACTIONINSERT);
					}
				}
			}
	}
	private InpatientDerate getNewDerate(InpatientDerate derate, Date curDate,
			int newBalanceNo, String zfFlg) throws ParseException {
		String curUserId = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		InpatientDerate newDerate = new InpatientDerate(); 
		//减免金额=-减免记录.减免金额
		double derateCost= 0;
		if(derate.getDerateCost()!=null){
			derateCost=-derate.getDerateCost().doubleValue();
		}
		
		if("-".equals(zfFlg)){
			newDerate.setDerateCost(derateCost);
			newDerate.setBalanceNo(newBalanceNo);
			//交易类型>>>去除
			//发票号
			newDerate.setInvoiceNo(derate.getInvoiceNo());
		}else{
			newDerate.setDerateCost(derateCost);
			newDerate.setBalanceNo(0);
			//交易类型 >>去除
			newDerate.setBalanceState("0");
			newDerate.setInvoiceNo(null);
		}
		//医疗流水号
		newDerate.setClinicNo(derate.getClinicNo());
		//发生序号（通过序列表生成）
		String sequece = this.getSequece("SEQ_COM_DERATE");
		newDerate.setHappenNo(Integer.parseInt(sequece));
		//减免种类
		newDerate.setDerateKind(derate.getDerateKind());
		//处方号 处方内项目流水号 
		newDerate.setSequenceNo(derate.getSequenceNo());
		//减免类型
		newDerate.setDerateType(derate.getDerateType());
		//最小费用
		newDerate.setFeeCode(derate.getFeeCode());
		//减免原因
		newDerate.setDerateCause(derate.getDerateCause());
		//批准人员 
		newDerate.setConfirmOpercode(derate.getConfirmOpercode());
		//科室
		newDerate.setDeptCode(derate.getDeptCode());
		return newDerate;
	}
	private List<InpatientDerate> getDerateList(String inpatientNo,
			String balanceNo) throws Exception {//clinicNo? 医疗流水号？->住院号？
		String hql="FROM InpatientDerate i WHERE  i.del_flg = 0 ";
		   hql = hql +" and i.clinicNo = '"+ inpatientNo +"' and balanceNo="+Integer.parseInt(balanceNo)+"";
		List<InpatientDerate> lst = null;
		try {
			lst =  this.getSession().createQuery(hql).list();
		} catch (Exception e) {
			throw e;
		} 
		return lst;
	}
	@Override
	public void insertShiftData(String inpatientNo, String balanceNo,
			int newBalanceNo) throws Exception {
		try {
			InpatientShiftData inpatientShiftData = new InpatientShiftData();
			String sequece = this.getSequece("SEQ_COM_SHIFTDATA");
			inpatientShiftData.setClinicNo(inpatientNo);
			inpatientShiftData.setHappenNo(new Integer(sequece));
			inpatientShiftData.setShiftType("BB");
			inpatientShiftData.setOldDataCode(balanceNo);
			inpatientShiftData.setOldDataName("原结算序号");
			inpatientShiftData.setNewDataCode(newBalanceNo+"");
			inpatientShiftData.setNewDataName("新结算序号");
			inpatientShiftData.setShiftCause("结算召回");
			inpatientShiftData.setMark("结算召回");
			this.save(inpatientShiftData);
			OperationUtils.getInstance().conserve(null,"结算召回","INSERT INTO","T_INPATIENT_SHIFTDATA",OperationUtils.LOGACTIONINSERT);
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	public String getdeptNameById(String deptCode) {
		return super.get(SysDepartment.class,deptCode).getDeptName();
	}
	@Override
	public String getBedNameById(String bedId) {
		String bedName = null;
		try {
			bedName =super.get(BusinessHospitalbed.class,bedId).getBedName();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return bedName;
	}
	@Override
	public String getEmployeeName(String houseDocCode) {
		return super.get(SysEmployee.class,houseDocCode).getName();
	}
	/**
	 * 根据用户id获取所在财务组id
	 * @param userId
	 * @return
	 */
	public String getFinanceGroupIdByUserId(String userId){
		String employeeId = getEmployeeId(userId);
		if(null!=employeeId){
			return getFinanceGroupId(employeeId);
		}
		return null;
	}
	/**
	 * 根据用户id获取其员工id
	 * @param userId
	 * @return
	 */
	public String getEmployeeId(String userId){
		String hql="FROM SysEmployee d WHERE d.del_flg = 0 AND d.stop_flg = 0 and d.userId.id='"+userId+"' ";
		List<SysEmployee> lst =  this.getSession().createQuery(hql).list();
		if(lst!=null){
			if(lst.size()>0){
				SysEmployee sysEmployee = lst.get(0);
				return sysEmployee.getId();
			}
		}
		return null;
	}
	/**
	 * 当前员工所在财务组
	 * @return
	 */
	public String getFinanceGroupId(String employeeId){
		String hql="FROM FinanceUsergroup d WHERE d.del_flg = 0 AND d.stop_flg = 0 and d.employee.id='"+employeeId+"' ";
		List<FinanceUsergroup> lst =  this.getSession().createQuery(hql).list();
		if(lst!=null){
			if(lst.size()>0){
				FinanceUsergroup financeUsergroup = lst.get(0);
				return financeUsergroup.getId();
			}
		}
		return null;
	}
	/**
	 * 根据当前用户的员工id，发票类型id 获得最大的未使用到的发票号  其获取过程模拟来自RegisterDAOImpl saveOrUpdate 
	 */
	public String getMaxInvoiceNo(String employeeId,String invoiceTypeId){
		String hql = "from FinanceInvoice  where invoiceGetperson = '"+employeeId+"' and  invoiceType= '"+invoiceTypeId+"'";
		List<FinanceInvoice> financeInvoiceList = super.find(hql, null);
		//发票终止号
		String invoiceEndno =financeInvoiceList.get(0).getInvoiceEndno().substring(1);
		//发票已用号并截取字母后面的数字
		String invoiceUsedno = financeInvoiceList.get(0).getInvoiceUsedno().substring(1);
		String invoiceUsednoa = financeInvoiceList.get(0).getInvoiceUsedno().substring(0, 1);
		//String 转型为 int
		int endno = Integer.parseInt(invoiceEndno);
		int usedno = Integer.parseInt(invoiceUsedno);
		//当进行保存时 发票号加1
		String maxusedno = usedno+1+"";
		//得到长度差
		int lengths = invoiceUsedno.length()-maxusedno.length();
		//根据长度差 补齐缺失的0
		for(int i =0;i<lengths;i++){
			invoiceUsednoa=invoiceUsednoa+"0";
		}
		//拼成完整的发票号
		String invoiceUsednoend = invoiceUsednoa+maxusedno;
		if(usedno<endno){
			return invoiceUsednoend;
		}else{
			return null;
		}
	}
	/**
	 * 根据当前用户的员工id，发票类型id 已使用发票号 更新发票状态及已使用发票号
	 * @param employeeId
	 * @param invoiceTypeId
	 * @param invoiceUseNO
	 */
	public void updateFinanceInvoiceState(String employeeId,String invoiceTypeId,String invoiceUseNO){
		String hql = "update FinanceInvoice set invoiceUsedno= ?,invoiceUsestate= ? where invoiceGetperson = '"+employeeId+"' and  invoiceType= '"+invoiceTypeId+"' ";
		this.getSession().createQuery(hql).setString(0,invoiceUseNO).setLong(1,1).executeUpdate();
	}
	@Override
	public List<PatientAccountrepaydetail> getPatientAccountrepaydetail(
			String id, String balanceNoSearch) {
		String hql="from PatientAccountrepaydetail where account.id='"+id+"' "
				+ "and balanceSeq='"+balanceNoSearch+"' and del_flg = 0 and detailDepitamounttType = 7 and detailAccounttype= 2 and detailOptype =1";
		List<PatientAccountrepaydetail> iList = super.find(hql, null);
		if(iList!=null&&iList.size()>0){
			return iList;
		}
		return new ArrayList<PatientAccountrepaydetail>();
	}
	@Override
	public List<InpatientInfoNow> querybalanceNo(String inpatientNo)
			throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select nvl(balance_no,0) + 1 as balanceNo from T_INPATIENT_INFO_NOW "
				+ "where  inpatient_no = '"+inpatientNo+"'");
		SQLQuery queryObject = this.getSession().createSQLQuery(sql.toString());
		queryObject.addScalar("balanceNo");
		List<InpatientInfoNow> list = queryObject.setResultTransformer(Transformers.aliasToBean(InpatientInfoNow.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<InpatientInfoNow>();
	}
	@Override
	public List<InpatientBedinfoNow> getInpatientBedinfo(String benId)throws Exception {
		String hql="FROM InpatientBedinfoNow i WHERE i.del_flg = 0 ";
		hql = hql +" and i.id = '"+ benId +"'";
		List<InpatientBedinfoNow> iList = super.find(hql, null);
		if(iList!=null&&iList.size()>0){
			return iList;
		}
		return new ArrayList<InpatientBedinfoNow>();
	}
	@Override
	public List<BusinessHospitalbed> getBusinessHospitalbed(String bedId)
			throws Exception {
		String hql="FROM BusinessHospitalbed i WHERE i.del_flg = 0 ";
		hql = hql +" and i.id = '"+ bedId +"'";
		List<BusinessHospitalbed> iList = super.find(hql, null);
		if(iList!=null&&iList.size()>0){
			return iList;
		}
		return new ArrayList<BusinessHospitalbed>();
	}
	@Override
	public List<BusinessBedward> getBusinessBedward(String businessBedward)
			throws Exception {
		String hql="FROM BusinessBedward i WHERE i.del_flg = 0 ";
		hql = hql +" and i.id = '"+ businessBedward +"'";
		List<BusinessBedward> iList = super.find(hql, null);
		if(iList!=null&&iList.size()>0){
			return iList;
		}
		return new ArrayList<BusinessBedward>();
	}
	@Override
	public List<SysDepartment> getSysDepartment(String nursestation)
			throws Exception {
		String hql = "from SysDepartment where  del_flg=0 and stop_flg=0 and deptType = 'N' and id='"+nursestation+"'";
		List<SysDepartment> deptList = super.find(hql, null);
		if(deptList==null||deptList.size()<=0){
			return new ArrayList<SysDepartment>();
		}
		return deptList;
	}
	@Override
	public List<BusinessContractunit> getBusinessContractunit(
			String encode) throws Exception {
		String hql="FROM BusinessContractunit i WHERE i.del_flg = 0 ";
		hql = hql +" and i.encode = '"+ encode +"'";
		List<BusinessContractunit> iList = super.find(hql, null);
		if(iList!=null&&iList.size()>0){
			return iList;
		}
		return new ArrayList<BusinessContractunit>();
	}
	@Override
	public List<SysEmployee> getSysEmployee(String houseDocCode)
			throws Exception {
		String hql="FROM SysEmployee i WHERE i.del_flg = 0 ";
		hql = hql +" and i.id = '"+ houseDocCode +"'";
		List<SysEmployee> iList = super.find(hql, null);
		if(iList!=null&&iList.size()>0){
			return iList;
		}
		return new ArrayList<SysEmployee>();
	}
	@Override
	public List<SysEmployee> getSysEmpName() {
		String hql="FROM SysEmployee i WHERE i.del_flg = 0 ";
		List<SysEmployee> iList = super.find(hql, null);
		if(iList!=null&&iList.size()>0){
			return iList;
		}
		return null;
	}
	@Override
	public SysEmployee queryEmployee(String userId) {
		String hql = " from SysEmployee where userId ='"+userId+"' and del_flg=0 and stop_flg=0 ";
		List<SysEmployee> employeeList = super.find(hql, null);
		if(employeeList==null||employeeList.size()<=0){
			return new SysEmployee();
		}
		return employeeList.get(0);
	}
	@Override
	public Map<String, Object> queryFinanceInvoiceNo(String id,String invoiceType) throws Exception {
		int invoiceNo = 0;
		String invoiceNosq = "";
		String invoiceNoas = "";
		String invoiceNosh = "";
		String invoiceUsednoa ="";	
		Map<String,Object> map=new HashMap<String,Object>();
		String hql = " from FinanceInvoice where invoiceGetperson= '"+id+"' and invoiceType ='"+invoiceType+"' and  del_flg=0 and stop_flg=0 and  invoiceUsestate in ('0','1') order by invoiceStartno";
		List<FinanceInvoice> invoiceList = super.find(hql, null);
		String invoiceNos = invoiceList.get(0).getInvoiceUsedno();//获得当前已使用号
		if(StringUtils.isNotBlank(invoiceNoas)){
			invoiceNosq = invoiceNos.substring(1);//截取后面的数字
			invoiceNoas = invoiceNos.substring(0, 1);//前面的字母
			int invoiceNoa = Integer.parseInt(invoiceNosq);//转为int类型
			invoiceNo = invoiceNoa+1;//加1是下一个要使用的还未使用的发票号
			invoiceNosh = invoiceNo+"";
			int lengths = 13-invoiceNosh.length();
			for(int a=0;a<lengths;a++){
				invoiceUsednoa=invoiceUsednoa+"0";
			}
		}
		
		String invoiceUserNo = invoiceNoas + invoiceUsednoa + invoiceNosh;
		if(invoiceNo==0){
			map.put("resMsg", "error");
			map.put("resCode", "发票已用完请重新领取!");
		}else{
			map.put("resMsg", "success");
			map.put("resCode", invoiceUserNo);
		}
		return map;
	}
	@Override
	public void saveInvoiceFinance(String id, String invoiceNo, String invoiceType) throws Exception {
		//判断这个发票号是不是本号段最后一位
		String hql = "from FinanceInvoice  where invoiceGetperson = '"+id+"' and  invoiceType= '"+invoiceType+"'";
		List<FinanceInvoice> financeInvoiceList = super.find(hql, null);
		if(financeInvoiceList.size()>0){
			String invoiceEndno = financeInvoiceList.get(0).getInvoiceEndno();//得到终止号
			String invoiceStartno = financeInvoiceList.get(0).getInvoiceStartno();//得到开始号
			if(invoiceEndno.equals(invoiceNo)){//当等于终止号的时候相当最后一个
				String hql2 = "update FinanceInvoice set invoiceUsedno= ?,invoiceUsestate= ? where invoiceGetperson = '"+id+"' and  invoiceType= '"+invoiceType+"' ";
				this.createQuery(hql2).setString(0,invoiceNo).setLong(1,-1 ).executeUpdate();
			}else if(invoiceStartno.equals(invoiceNo)){//当等于开始号的时候相当于第一个
				String hql3 = "update FinanceInvoice set invoiceUsedno= ?,invoiceUsestate= ? where invoiceGetperson = '"+id+"' and  invoiceType= '"+invoiceType+"' ";
				this.createQuery(hql3).setString(0,invoiceNo).setLong(1,1 ).executeUpdate();
			}else{
				String hql4 = "update FinanceInvoice set invoiceUsedno= ?,invoiceUsestate= ? where invoiceGetperson = '"+id+"' and  invoiceType= '"+invoiceType+"'";
				this.createQuery(hql4).setString(0,invoiceNo).setLong(1,1 ).executeUpdate();
			}
		}
	}
	@Override
	public List<User> getUserIdEmpName() throws Exception {
		String hql = " from User where del_flg=0 and stop_flg=0 ";
		List<User> iList = super.find(hql, null);
		if(iList!=null&&iList.size()>0){
			return iList;
		}
		return null;
	}
	@Override
	public InpatientInPrepayNow queryHappenNo(String inpatientNo) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select max(t.happen_no) as happenNo from t_inpatient_inprepay_now t where t.INPATIENT_NO='"+inpatientNo+"'");
		SQLQuery queryObject = this.getSession().createSQLQuery(sql.toString());
		queryObject.addScalar("happenNo",Hibernate.INTEGER);
		List<InpatientInPrepayNow> list = queryObject.setResultTransformer(Transformers.aliasToBean(InpatientInPrepayNow.class)).list();
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return new InpatientInPrepayNow();
	}
}
