package cn.honry.finance.refund.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessPayMode;
import cn.honry.base.bean.model.BusinessPayModeNow;
import cn.honry.base.bean.model.DrugApplyout;
import cn.honry.base.bean.model.DrugApplyoutNow;
import cn.honry.base.bean.model.DrugUndrug;
import cn.honry.base.bean.model.FinanceInvoiceInfo;
import cn.honry.base.bean.model.FinanceInvoiceInfoNow;
import cn.honry.base.bean.model.FinanceInvoicedetailNow;
import cn.honry.base.bean.model.HospitalParameter;
import cn.honry.base.bean.model.InpatientCancelitemNow;
import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.base.bean.model.OutpatientAccount;
import cn.honry.base.bean.model.OutpatientDaybalance;
import cn.honry.base.bean.model.OutpatientFeedetail;
import cn.honry.base.bean.model.OutpatientFeedetailNow;
import cn.honry.base.bean.model.Registration;
import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.base.bean.model.StoRecipe;
import cn.honry.base.bean.model.StoRecipeNow;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.TecApply;
import cn.honry.base.bean.model.TecTerminalApply;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.finance.refund.dao.RefundDAO;
import cn.honry.utils.DateUtils;
@SuppressWarnings("all")
@Repository("refundDAO")
public class RefundDAOImpl extends HibernateEntityDao<OutpatientFeedetail> implements RefundDAO{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<FinanceInvoiceInfoNow> findInvoiceInfoByInvoiceNo(String invoiceNo) {
		String hql = "from FinanceInvoiceInfoNow where invoiceNo = ? and transType='1'  and del_flg=0 and stop_flg=0";
		List<FinanceInvoiceInfoNow> invoiceInfoList = super.find(hql, invoiceNo);
		if(invoiceInfoList==null||invoiceInfoList.size()<=0){
			return new ArrayList<FinanceInvoiceInfoNow>();
		}
		return invoiceInfoList;
	}

	@Override
	public List<FinanceInvoiceInfoNow> findInvoiceInfoByInvoiceSeqs(String invoiceSeqs) {
		String hql = "from FinanceInvoiceInfoNow where invoiceSeq in ('"+invoiceSeqs+"') and transType='1' and cancelFlag = 1  and del_flg=0 and stop_flg=0";
		List<FinanceInvoiceInfoNow> invoiceInfoList = super.find(hql, null);
		if(invoiceInfoList==null||invoiceInfoList.size()<=0){
			return new ArrayList<FinanceInvoiceInfoNow>();
		}
		return invoiceInfoList;
	}
	public List<FinanceInvoiceInfoNow> findInvoiceInfoByInvoiceNos(String invoiceNos){
		String hql = "from FinanceInvoiceInfoNow where invoiceNo in ('"+invoiceNos+"') and transType='1'  and del_flg=0 and stop_flg=0";
		List<FinanceInvoiceInfoNow> invoiceInfoList = super.find(hql, null);
		if(invoiceInfoList==null||invoiceInfoList.size()<=0){
			return new ArrayList<FinanceInvoiceInfoNow>();
		}
		return invoiceInfoList;
	}
	@Override
	public HospitalParameter queryParameter() {
		String hql = "from HospitalParameter where parameterCode = 'backValidDay' ";
		List<HospitalParameter> parameterList = super.find(hql, null);
		if(parameterList==null||parameterList.size()<=0){
			return new HospitalParameter();
		}
		return parameterList.get(0);
	}
	
	@Override
	public List<OutpatientFeedetailNow> findFeedetailByInvoiceNos(String invoiceNos, String value,String clinicCode) {
		SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy/MM/dd");
		Date date=new Date();
		Integer dates = Integer.parseInt(value);
		//延后日期
		Date date2 = DateUtils.addDay(date, -dates);
		String hql = "select f.id as id,"+
					 "f.recipe_no as recipeNo,"+
					 "to_char(f.sequence_no) as sequenceNo, "+
					 "f.drug_flag as drugFlag,"+
					 "f.unit_price as unitPrice,"+
					 "f.comb_no as combNo,"+
					 "f.specs as specs,"+
					 "f.qty as qty,"+
					 "f.price_unit as priceUnit,"+
					 "f.noback_num as nobackNum,"+
					 "f.tot_cost as totCost,"+
					 "f.dose_once as doseOnce,"+
					 "f.FEE_CPCDNAME as feeCpcdname,"+
					 "f.item_code as itemCode,"+
				     "f.item_name as itemName,"+
					 "f.exec_dpcd as execDpcd,"+
					 "f.INVOICE_NO as invoiceNo,"+
				     "f.ext_flag3 as extFlag3,"+
					 "f.SUBJOB_FLAG as subjobFlag,"+
				     "f.EXTEND_ONE as extendOne,"+
					 "f.noback_num as nobackNums"+
					 ", (select r.id from t_sto_recipe_now r where r.recipe_no = f.recipe_no and r.trans_type = '1' and r.class_meaning_code = '1' and r.recipe_state not in ('3','4') and r.valid_state = '1' and r.modify_flag = '0') as flay "+
					 " from t_outpatient_feedetail_Now f where " ;
		if(StringUtils.isNotBlank(clinicCode)){
			hql += " f.CLINIC_CODE = '"+clinicCode+"' and ";
		}
		hql += " f.invoice_no in ('"+invoiceNos+"') and f.pay_flag = 1 and f.cancel_flag = 1 and f.trans_type = 1 and to_char(f.fee_date,'yyyy-MM-dd') <= '"+DateUtils.formatDateY_M_D(DateUtils.getCurrentTime())+"' and to_char(f.fee_date,'yyyy-MM-dd') >= '"+DateUtils.formatDateY_M_D(date2)+"' and f.stop_flg = 0 and f.del_flg = 0";
		SQLQuery queryObject = this.getSession().createSQLQuery(hql).addScalar("id").addScalar("recipeNo").addScalar("sequenceNo",Hibernate.INTEGER).addScalar("drugFlag").addScalar("unitPrice",Hibernate.DOUBLE).addScalar("combNo").addScalar("specs").addScalar("qty",Hibernate.DOUBLE)
				.addScalar("priceUnit").addScalar("invoiceNo").addScalar("nobackNum",Hibernate.DOUBLE).addScalar("totCost",Hibernate.DOUBLE).addScalar("doseOnce",Hibernate.DOUBLE).addScalar("feeCpcdname").addScalar("itemCode").addScalar("itemName").addScalar("execDpcd")
				.addScalar("extFlag3",Hibernate.INTEGER).addScalar("nobackNums",Hibernate.INTEGER).addScalar("flay").addScalar("extendOne").addScalar("subjobFlag", Hibernate.INTEGER);
		List<OutpatientFeedetailNow> feedetailList = queryObject.setResultTransformer(Transformers.aliasToBean(OutpatientFeedetailNow.class)).list();
		if(feedetailList!=null&&feedetailList.size()>0){
			  return feedetailList;
		  }
		return new ArrayList<OutpatientFeedetailNow>();
	}

	@Override
	public BusinessContractunit queryContractunit(String paykindCode) {
		String hql = "from BusinessContractunit where encode = ? ";
		List<BusinessContractunit> contractunitList = super.find(hql, paykindCode);
		if(contractunitList==null||contractunitList.size()<=0){
			return new BusinessContractunit();
		}
		return contractunitList.get(0);
	}

	@Override
	public List<SysEmployee> queryEmpFunction() {
		String hql = "from SysEmployee where del_flg=0 and stop_flg=0";
		List<SysEmployee> employeeList = super.find(hql, null);
		if(employeeList==null||employeeList.size()<=0){
			return new ArrayList<SysEmployee>();
		}
		return employeeList;
	}

	@Override
	public HospitalParameter queryParameterByRj() {
		String hql = "from HospitalParameter where parameterCode = 'isFeeUser' ";
		List<HospitalParameter> parameterList = super.find(hql, null);
		if(parameterList==null||parameterList.size()<=0){
			return new HospitalParameter();
		}
		return parameterList.get(0);
	}

	@Override
	public HospitalParameter queryParameterByPayType() {
		String hql = "from HospitalParameter where parameterCode = 'setReturnPath' ";
		List<HospitalParameter> parameterList = super.find(hql, null);
		if(parameterList==null||parameterList.size()<=0){
			return new HospitalParameter();
		}
		return parameterList.get(0);
	}

	@Override
	public List<FinanceInvoicedetailNow> findInvoiceTailByInvoiceSeq(String invoiceSeq) {
		String hql = "from FinanceInvoicedetailNow where invoiceSeq in ('"+invoiceSeq+"') and del_flg=0 and stop_flg=0 and transType = '1'";
		List<FinanceInvoicedetailNow> invoicedetailList = super.find(hql, null);
		if(invoicedetailList==null||invoicedetailList.size()<=0){
			return new ArrayList<FinanceInvoicedetailNow>();
		}
		return invoicedetailList;
	}
	public List<FinanceInvoicedetailNow> findInvoiceTailByInvoiceNos(String invoiceNos){
		String hql = "from FinanceInvoicedetailNow where invoiceNo in ('"+invoiceNos+"') and del_flg=0 and stop_flg=0 and transType = '1'";
		List<FinanceInvoicedetailNow> invoicedetailList = super.find(hql, null);
		if(invoicedetailList==null||invoicedetailList.size()<=0){
			return new ArrayList<FinanceInvoicedetailNow>();
		}
		return invoicedetailList;
	}
	@Override
	public List<BusinessPayModeNow> findPayModeListBySeq(String invoiceSeq) {
		String hql = "from BusinessPayModeNow where invoiceSeq in ('"+invoiceSeq+"') and del_flg=0 and stop_flg=0 and transType = '1' and cancelFlag = 1";
		List<BusinessPayModeNow> payModeList = super.find(hql, null);
		if(payModeList==null||payModeList.size()<=0){
			return new ArrayList<BusinessPayModeNow>();
		}
		return payModeList;
	}
	public List<BusinessPayModeNow> findPayModeListByNos(String invoiceNos){
		String hql = "from BusinessPayModeNow where invoiceNo in ('"+invoiceNos+"') and del_flg=0 and stop_flg=0 and transType = '1' and cancelFlag = 1";
		List<BusinessPayModeNow> payModeList = super.find(hql, null);
		if(payModeList==null||payModeList.size()<=0){
			return new ArrayList<BusinessPayModeNow>();
		}
		return payModeList;
	}
	@Override
	public List<OutpatientFeedetailNow> findFeedetailListBySeq(String invoiceSeq) {
		String hql = "from OutpatientFeedetailNow where invoiceSeq in ('"+invoiceSeq+"') and del_flg=0 and stop_flg=0 and  transType = '1' and cancelFlag = 1";
		List<OutpatientFeedetailNow> feedetailList = super.find(hql, null);
		if(feedetailList==null||feedetailList.size()<=0){
			return new ArrayList<OutpatientFeedetailNow>();
		}
		return feedetailList;
	}
	public List<OutpatientFeedetailNow> findFeedetailListByNos(String invoiceNos){
		String hql = "from OutpatientFeedetailNow where invoiceNo in ('"+invoiceNos+"') and del_flg=0 and stop_flg=0 and  transType = '1' and cancelFlag = 1";
		List<OutpatientFeedetailNow> feedetailList = super.find(hql, null);
		if(feedetailList==null||feedetailList.size()<=0){
			return new ArrayList<OutpatientFeedetailNow>();
		}
		return feedetailList;
	}
	@Override
	public List<StoRecipeNow> findStoRecipeByRecipNo(String recipeNo) {
		String hql = "from StoRecipeNow where recipeNo in ('"+recipeNo+"') and del_flg=0 and stop_flg=0 and  classMeaningCode = '1' and transType not in ('3','4') and validState = 1 and ModifyFlag = 0";
		List<StoRecipeNow> stoRecipeList = super.find(hql, null);
		if(stoRecipeList==null||stoRecipeList.size()<=0){
			return new ArrayList<StoRecipeNow>();
		}
		return stoRecipeList;
	}
	public StoRecipeNow findStoRecipeByRecipNoOne(String recipeNo){
		String hql = "from StoRecipeNow where recipeNo = ? and del_flg=0 and stop_flg=0 and  classMeaningCode = '1' and transType ='1' and validState = 1 and ModifyFlag = 0 and recipeState not in ('3', '4') Order By createTime Desc";
		List<StoRecipeNow> stoRecipeList = super.find(hql, recipeNo);
		if(stoRecipeList==null||stoRecipeList.size()<=0){
			return new StoRecipeNow();
		}
		return stoRecipeList.get(0);
	}
	@Override
	public List<DrugApplyoutNow> findApplyoutByItemCode(String drugIds,String moOrder) {
		String hql = " from DrugApplyoutNow t where t.drugCode =? and t.moOrder =? and t.del_flg=0 and t.stop_flg=0 and t.opType = 1 and t.applyState != 3 and t.chargeFlag = 1 and t.validState = 1 ";
		List<DrugApplyoutNow> applyoutList = super.find(hql, drugIds,moOrder);
		if(applyoutList!=null||applyoutList.size()>0){
			return applyoutList;
		}
		return new ArrayList<DrugApplyoutNow>();
	}

	@Override
	public List<OutpatientFeedetailNow> findOutFeedetail(String drugIds) {
		String hql = "from OutpatientFeedetailNow where itemCode in ('"+drugIds+"') and del_flg=0 and stop_flg=0 and  transType = '1' and cancelFlag = 1";
		List<OutpatientFeedetailNow> feedetailList = super.find(hql, null);
		if(feedetailList==null||feedetailList.size()<=0){
			return new ArrayList<OutpatientFeedetailNow>();
		}
		return feedetailList;
	}

	@Override
	public List<OutpatientFeedetailNow> findFeedetailListById(String feeId,String refundId) {
		String hql = "from OutpatientFeedetailNow where id in ('"+feeId+"') and id not in ('"+refundId+"')";
		List<OutpatientFeedetailNow> feedetailList = super.find(hql, null);
		if(feedetailList==null||feedetailList.size()<=0){
			return new ArrayList<OutpatientFeedetailNow>();
		}
		return feedetailList;
	}

	@Override
	public MinfeeStatCode findMinfeeStatCode(String encode) {
		String hql = "from MinfeeStatCode where minfeeCode = ? and del_flg=0 and stop_flg=0";
		List<MinfeeStatCode> minfeeStatCodeList = super.find(hql, encode);
		if(minfeeStatCodeList==null||minfeeStatCodeList.size()<=0){
			return new MinfeeStatCode();
		}
		return minfeeStatCodeList.get(0);
	}

	@Override
	public RegistrationNow queryRegisterInfo(String clinicCode) {
		String hql = "from RegistrationNow where clinicCode = ? and del_flg=0 and stop_flg=0 and transType = 1";
		List<RegistrationNow> registerInfoList = super.find(hql, clinicCode);
		if(registerInfoList==null||registerInfoList.size()<=0){
			return new RegistrationNow();
		}
		return registerInfoList.get(0);
	}

	@Override
	public List<OutpatientFeedetailNow> findFeedetailListByIdAndType(String feeId,String refundId) {
		String hql = "from OutpatientFeedetailNow where id in ('"+feeId+"') and id not in ('"+refundId+"') and drugFlag = '1'";
		List<OutpatientFeedetailNow> feedetailList = super.find(hql, null);
		if(feedetailList==null||feedetailList.size()<=0){
			return new ArrayList<OutpatientFeedetailNow>();
		}
		return feedetailList;
	}

	@Override
	public OutpatientFeedetailNow queryFeedetailById(String id) {
		String hql = "from OutpatientFeedetailNow where id =?";
		List<OutpatientFeedetailNow> feedetailList = super.find(hql, id);
		if(feedetailList==null||feedetailList.size()<=0){
			return new OutpatientFeedetailNow();
		}
		return feedetailList.get(0);
	}

	@Override
	public BusinessContractunit queryContractunitById(String contractunit) {
		String hql = "from BusinessContractunit where id =? and del_flg=0 and stop_flg=0";
		List<BusinessContractunit> contractunitList = super.find(hql, contractunit);
		if(contractunitList==null||contractunitList.size()<=0){
			return new BusinessContractunit();
		}
		return contractunitList.get(0);
	}

	@Override
	public OutpatientAccount getAccountByMedicalrecord(String patientNo) {
		String hql = "from OutpatientAccount where medicalrecordId =? ";
		List<OutpatientAccount> account = super.find(hql, patientNo);
		if(account==null||account.size()<=0){
			return new OutpatientAccount();
		}
		return account.get(0);
	}

	@Override
	public OutpatientDaybalance getDayBalancebyNO(String balanceNo) {
		String hql = " from OutpatientDaybalance where blanceNo = ? ";
		List<OutpatientDaybalance> list = super.find(hql, balanceNo);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<InpatientCancelitemNow> getCancelItemByInvoiceNo(String invoiceNo) {
		String hql = "from InpatientCancelitemNow where billNo in ('"+invoiceNo+"') and confirmFlag = 0 and chargeFlag = 0 and del_flg=0 and stop_flg=0 and applyFlag = 1";
		List<InpatientCancelitemNow> cancelitemList = super.find(hql);
		if(cancelitemList==null||cancelitemList.size()<=0){
			return new ArrayList<InpatientCancelitemNow>();
		}
		return cancelitemList;
	}

	@Override
	public TecTerminalApply getTerByapplyNum(String no) {
		String hql = " from TecTerminalApply where applyNumber = ? and extFlag1 = '1'";
		List<TecTerminalApply> list = super.find(hql, no);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public TecApply getTecByapplyNum(String no) {
		String hql = "from TecApply where bookId = ? and status != 3 ";
		List<TecApply> list = super.find(hql, no);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public String checkISsend(String moorder) {
		String sql = "select s.RECIPE_STATE as recipeState from t_outpatient_feedetail_now t Left Join T_STO_RECIPE_NOW s"
				+ " On t.recipe_no = s.RECIPE_NO And t.clinic_code = s.clinic_code "
				+ "Where t.MO_ORDER = '"+moorder+"' And t.trans_type = 1 and t.cancel_flag = 1 and s.VALID_STATE = 1 And s.trans_type = 1 "
				+ "And t.stop_flg = 0 And t.del_flg = 0 " + "And s.stop_flg = 0 And s.del_flg = 0";
		 SQLQuery query = this.getSession().createSQLQuery(sql);
		 query.addScalar("recipeState", Hibernate.INTEGER);
		 List<StoRecipeNow> list = query.setResultTransformer(Transformers.aliasToBean(StoRecipeNow.class)).list();
		 if(list!=null&&list.size()>0){
			 return list.get(0).getRecipeState().toString();
		 }
		 return null;
	}

	@Override
	public List<TecApply> getTecByapplyNumList(String no) {
		String hql = "from TecApply where bookId in ('"+no+"') and status != 3 ";
		List<TecApply> list = super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}

	@Override
	public List<TecTerminalApply> getTerByapplyNumList(String no) {
		String hql = " from TecTerminalApply where applyNumber in ('"+no+"') and extFlag1 = '1'";
		List<TecTerminalApply> list = super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}

	@Override
	public String getDeptArea(String deptCode) {
		String sql="select t.dept_area_code as clinicNo from t_department t where t.dept_code='"+deptCode+"'";
		List<TecTerminalApply> list=super.getSession().createSQLQuery(sql)
				.addScalar("clinicNo").setResultTransformer(Transformers.aliasToBean(TecTerminalApply.class)).list();
				if(list.size()>0){
					return list.get(0).getClinicNo();
				}
		return "";
	}
}
