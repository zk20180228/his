  package cn.honry.inpatient.docAdvManage.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessAdvdrugnature;
import cn.honry.base.bean.model.BusinessAdvicetoSystemclass;
import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.BusinessFrequency;
import cn.honry.base.bean.model.BusinessOdditionalitem;
import cn.honry.base.bean.model.DrugBillclass;
import cn.honry.base.bean.model.DrugBilllist;
import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.DrugStorage;
import cn.honry.base.bean.model.DrugUndrug;
import cn.honry.base.bean.model.InpatientKind;
import cn.honry.base.bean.model.InpatientOrder;
import cn.honry.base.bean.model.InpatientOrderNow;
import cn.honry.base.bean.model.OutpatientRecipedetailNow;
import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysDruggraDecontraStrank;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.drug.Billclass.dao.BillclassInInterDAO;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.inpatient.docAdvManage.dao.DocAdvManageDAO;
import cn.honry.inpatient.docAdvManage.dao.InpatientOrderNowDAO;
import cn.honry.inpatient.docAdvManage.vo.AdviceLong;
import cn.honry.inpatient.docAdvManage.vo.ProInfoVo;
import cn.honry.inpatient.docAdvManage.vo.UnitsVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.SessionUtils;
@Repository("docAdvManageDAO")
@SuppressWarnings({ "all" })
/**
 * 
* @ClassName: DocAdvManageDAOImpl
* @Description: 
* @author yeguanqun
* @date 2016年5月10日 下午2:02:16
*
 */
public class DocAdvManageDAOImpl extends HibernateEntityDao<InpatientOrder> implements DocAdvManageDAO{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Autowired
	@Qualifier(value = "inpatientOrderNowDAO")
	private InpatientOrderNowDAO inpatientOrderNowDAO;
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	@Autowired
	@Qualifier(value = "billclassInInterDAO")
	private BillclassInInterDAO billclassInInterDAO;//摆药
	 //基础工具类,不支持参数名传参
  	@Resource
  	private JdbcTemplate jdbcTemplate;
  	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	@Override
	public List<InpatientOrder> queryInpatientOrder(String decmpsState,String inpatientNo,String recordId) {
		Date date = new Date();
		DateFormat d1 = DateFormat.getDateTimeInstance();
		String now=d1.format(date);
		String fnow=d1.format(new Date(date.getTime() + 5 * 24 * 60 * 60 * 1000));
		String onow=null;
		if("06".equals(recordId)){
			onow=d1.format(new Date(date.getTime() - 1 * 24 * 60 * 60 * 1000));
		}
		
		String str = parameterInnerDAO.getParameterByCode("showyzdata");//hedong  20161012 无法显示患者历史医嘱记录  此处报错 .6无此数据
		//hedong 加入默认显示天数判断
		if("".equals(str)){
			str = "7"; //默认显示7天的历史医嘱
		}
		 
		Date d2 = DateUtils.addDay(DateUtils.getCurrentTime(),-Integer.parseInt(str));
		String dtime=d1.format(d2);
		String hql = "select t.id,t.inpatientNo,t.patientNo,t.deptCode,t.nurseCellCode,t.listDpcd,t.moOrder,t.docCode,t.docName,t.moDate,t.babyFlag,t.happenNo,t.setItmattr,t.setSubtbl,t.typeCode,t.typeName,t.decmpsState,t.chargeState,t.needDrug,t.prnExelist,t.prmMorlist,t.needConfirm,t.itemType,t.itemCode,t.itemName,t.classCode,t.className,t.pharmacyCode,t.execDpcd,t.execDpnm,nvl(t.baseDose,'') as baseDose,t.doseUnit,t.minUnit,t.priceUnit,t.packQty,t.specs,t.doseModelCode,t.drugType,t.drugQuality,t.itemPrice,nvl(t.combNo,'') as combNo,t.mainDrug,t.moStat,t.usageCode,t.useName,t.englishAb,t.frequencyCode,t.frequencyName,t.doseOnce,t.stocKin,t.qtyTot,t.useDays,t.dateBgn,t.dateEnd,t.recUsercd,t.recUsernm,t.confirmFlag,t.confirmDate,t.confirmUsercd,t.dcFlag,t.dcDate,t.dcCode,t.dcName,t.dcDoccd,t.dcDocnm,t.dcUsercd,t.dcUsernm,t.executeFlag,t.executeDate,t.executeUsercd,t.decoFlag,t.dateCurmodc,t.dateNxtmodc,t.moNote1,nvl(t.moNote2,'') as moNote2,t.hypotest,t.itemNote,t.applyNo,t.emcFlag,t.getFlag,t.subtblFlag,t.sortId,t.dcConfirmDate,t.dcConfirmOper,t.dcConfirmFlag,t.labCode,t.permission,t.packageCode,t.packageName,t.mark1,t.mark2,t.mark3,t.execTimes,t.execDose,t.createUser,t.createDept,t.createTime,t.updateUser,t.updateTime,t.deleteUser,t.deleteTime,t.del_flg,t.stop_flg,t.drugpackagingUnit from ("
				+ " select a.EXEC_ID as id,a.INPATIENT_NO as inpatientNo,a.PATIENT_NO as patientNo,a.DEPT_CODE as deptCode,a.NURSE_CELL_CODE as nurseCellCode,a.LIST_DPCD as listDpcd,a.MO_ORDER as moOrder,a.DOC_CODE as docCode,a.DOC_NAME as docName,a.MO_DATE as moDate,a.BABY_FLAG as babyFlag,a.HAPPEN_NO as happenNo,a.SET_ITMATTR as setItmattr,a.SET_SUBTBL as setSubtbl,a.TYPE_CODE as typeCode,a.TYPE_NAME as typeName,a.DECMPS_STATE as decmpsState,a.CHARGE_STATE as chargeState,a.NEED_DRUG as needDrug,a.PRN_EXELIST as prnExelist,a.PRM_MORLIST as prmMorlist,a.NEED_CONFIRM as needConfirm,a.ITEM_TYPE as itemType,a.ITEM_CODE as itemCode,a.ITEM_NAME as itemName,a.CLASS_CODE as classCode,a.CLASS_NAME as className,a.PHARMACY_CODE as pharmacyCode,a.EXEC_DPCD as execDpcd,a.EXEC_DPNM as execDpnm,a.BASE_DOSE as baseDose,a.DOSE_UNIT as doseUnit,a.MIN_UNIT as minUnit,a.PRICE_UNIT as priceUnit,a.PACK_QTY as packQty,a.SPECS as specs,a.DOSE_MODEL_CODE as doseModelCode,a.DRUG_TYPE as drugType,a.DRUG_QUALITY as drugQuality,a.ITEM_PRICE as itemPrice,a.COMB_NO as combNo,a.MAIN_DRUG as mainDrug,a.MO_STAT as moStat,a.USAGE_CODE as usageCode,a.USE_NAME as useName,a.ENGLISH_AB as englishAb,a.FREQUENCY_CODE as frequencyCode,a.FREQUENCY_NAME as frequencyName,a.DOSE_ONCE as doseOnce,a.STOCK_MIN as stocKin,a.QTY_TOT as qtyTot,a.USE_DAYS as useDays,a.DATE_BGN as dateBgn,a.DATE_END as dateEnd,a.REC_USERCD as recUsercd,a.REC_USERNM as recUsernm,a.CONFIRM_FLAG as confirmFlag,a.CONFIRM_DATE as confirmDate,a.CONFIRM_USERCD as confirmUsercd,a.DC_FLAG as dcFlag,a.DC_DATE as dcDate,a.DC_CODE as dcCode,a.DC_NAME as dcName,a.DC_DOCCD as dcDoccd,a.DC_DOCNM as dcDocnm,a.DC_USERCD as dcUsercd,a.DC_USERNM as dcUsernm,a.EXECUTE_FLAG as executeFlag,a.EXECUTE_DATE as executeDate,a.EXECUTE_USERCD as executeUsercd,a.DECO_FLAG as decoFlag,a.DATE_CURMODC as dateCurmodc,a.DATE_NXTMODC as dateNxtmodc,a.MO_NOTE1 as moNote1,a.MO_NOTE2 as moNote2,a.HYPOTEST as hypotest,a.ITEM_NOTE as itemNote,a.APPLY_NO as applyNo,a.EMC_FLAG as emcFlag,a.GET_FLAG as getFlag,a.SUBTBL_FLAG as subtblFlag,a.SORT_ID as sortId,a.DC_CONFIRM_DATE as dcConfirmDate,a.DC_CONFIRM_OPER as dcConfirmOper,a.DC_CONFIRM_FLAG as dcConfirmFlag,a.LAB_CODE as labCode,a.PERMISSION as permission,a.PACKAGE_CODE as packageCode,a.PACKAGE_NAME as packageName,a.MARK1 as mark1,a.MARK2 as mark2,a.MARK3 as mark3,a.EXEC_TIMES as execTimes,a.EXEC_DOSE as execDose,a.CREATEUSER as createUser,a.CREATEDEPT as createDept,a.CREATETIME as createTime,a.UPDATEUSER as updateUser,a.UPDATETIME as updateTime,a.DELETEUSER as deleteUser,a.DELETETIME as deleteTime,a.DEL_FLG as del_flg,a.STOP_FLG as stop_flg,a.DRUGPACKAGING_UNIT as drugpackagingUnit from "+HisParameters.HISPARSCHEMAHISUSER+"T_INPATIENT_ORDER_NOW a where a.stop_flg = 0 and a.del_flg = 0 and nvl(to_char(a.SUBTBL_FLAG),'xx')!='1' and a.MO_STAT!=3 and a.mo_stat!=4 "
				+ " union SELECT b.EXEC_ID as id,b.INPATIENT_NO as inpatientNo,b.PATIENT_NO as patientNo,b.DEPT_CODE as deptCode,b.NURSE_CELL_CODE as nurseCellCode,b.LIST_DPCD as listDpcd,b.MO_ORDER as moOrder,b.DOC_CODE as docCode,b.DOC_NAME as docName,b.MO_DATE as moDate,b.BABY_FLAG as babyFlag,b.HAPPEN_NO as happenNo,b.SET_ITMATTR as setItmattr,b.SET_SUBTBL as setSubtbl,b.TYPE_CODE as typeCode,b.TYPE_NAME as typeName,b.DECMPS_STATE as decmpsState,b.CHARGE_STATE as chargeState,b.NEED_DRUG as needDrug,b.PRN_EXELIST as prnExelist,b.PRM_MORLIST as prmMorlist,b.NEED_CONFIRM as needConfirm,b.ITEM_TYPE as itemType,b.ITEM_CODE as itemCode,b.ITEM_NAME as itemName,b.CLASS_CODE as classCode,b.CLASS_NAME as className,b.PHARMACY_CODE as pharmacyCode,b.EXEC_DPCD as execDpcd,b.EXEC_DPNM as execDpnm,b.BASE_DOSE as baseDose,b.DOSE_UNIT as doseUnit,b.MIN_UNIT as minUnit,b.PRICE_UNIT as priceUnit,b.PACK_QTY as packQty,b.SPECS as specs,b.DOSE_MODEL_CODE as doseModelCode,b.DRUG_TYPE as drugType,b.DRUG_QUALITY as drugQuality,b.ITEM_PRICE as itemPrice,b.COMB_NO as combNo,b.MAIN_DRUG as mainDrug,b.MO_STAT as moStat,b.USAGE_CODE as usageCode,b.USE_NAME as useName,b.ENGLISH_AB as englishAb,b.FREQUENCY_CODE as frequencyCode,b.FREQUENCY_NAME as frequencyName,b.DOSE_ONCE as doseOnce,b.STOCK_MIN as stocKin,b.QTY_TOT as qtyTot,b.USE_DAYS as useDays,b.DATE_BGN as dateBgn,b.DATE_END as dateEnd,b.REC_USERCD as recUsercd,b.REC_USERNM as recUsernm,b.CONFIRM_FLAG as confirmFlag,b.CONFIRM_DATE as confirmDate,b.CONFIRM_USERCD as confirmUsercd,b.DC_FLAG as dcFlag,b.DC_DATE as dcDate,b.DC_CODE as dcCode,b.DC_NAME as dcName,b.DC_DOCCD as dcDoccd,b.DC_DOCNM as dcDocnm,b.DC_USERCD as dcUsercd,b.DC_USERNM as dcUsernm,b.EXECUTE_FLAG as executeFlag,b.EXECUTE_DATE as executeDate,b.EXECUTE_USERCD as executeUsercd,b.DECO_FLAG as decoFlag,b.DATE_CURMODC as dateCurmodc,b.DATE_NXTMODC as dateNxtmodc,b.MO_NOTE1 as moNote1,b.MO_NOTE2 as moNote2,b.HYPOTEST as hypotest,b.ITEM_NOTE as itemNote,b.APPLY_NO as applyNo,b.EMC_FLAG as emcFlag,b.GET_FLAG as getFlag,b.SUBTBL_FLAG as subtblFlag,b.SORT_ID as sortId,b.DC_CONFIRM_DATE as dcConfirmDate,b.DC_CONFIRM_OPER as dcConfirmOper,b.DC_CONFIRM_FLAG as dcConfirmFlag,b.LAB_CODE as labCode,b.PERMISSION as permission,b.PACKAGE_CODE as packageCode,b.PACKAGE_NAME as packageName,b.MARK1 as mark1,b.MARK2 as mark2,b.MARK3 as mark3,b.EXEC_TIMES as execTimes,b.EXEC_DOSE as execDose,b.CREATEUSER as createUser,b.CREATEDEPT as createDept,b.CREATETIME as createTime,b.UPDATEUSER as updateUser,b.UPDATETIME as updateTime,b.DELETEUSER as deleteUser,b.DELETETIME as deleteTime,b.DEL_FLG as del_flg,b.STOP_FLG as stop_flg,b.DRUGPACKAGING_UNIT as drugpackagingUnit from "+HisParameters.HISPARSCHEMAHISUSER+"T_INPATIENT_ORDER_NOW b where b.MO_STAT=3 and b.DATE_END>=to_date('"+dtime+"','yyyy-mm-dd hh24:mi:ss'))t where t.mainDrug=1 ";
		if(StringUtils.isNotBlank(inpatientNo)){
			hql = hql+" and t.inpatientNo ='"+inpatientNo+"'";
		}
		if(StringUtils.isNotBlank(decmpsState)){
			hql = hql+" and t.decmpsState ="+decmpsState+"";
		}	
		if("1".equals(decmpsState) && !"01".equals(recordId)){
			if(recordId==null){
				hql = hql+" and  t.moStat!=3 and t.dateEnd >= to_date('"+fnow+"','yyyy-mm-dd hh24:mi:ss') ";
			}
			if("02".equals(recordId)){//开立状态也走这里
				hql = hql+" and t.moStat!=3";
			}
			if("03".equals(recordId)){
				hql = hql+" and t.moStat=3";
			}
			if("04".equals(recordId)){
				hql = hql+" and substr(to_char(t.dateBgn,'yyyy-mm-dd hh24:mi:ss'),1,10)=substr(to_char(to_date('"+now+"','yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss'),1,10) ";
			}
			if("05".equals(recordId)){
				hql = hql+" and t.moStat=0";
			}
		}
		if("0".equals(decmpsState) && !"01".equals(recordId)){
			if(recordId==null){
				hql = hql+" and  (t.moStat=0 or t.moStat=1 or t.moStat=-1 or t.moStat=-3) and t.moDate >=to_date('"+fnow+"','yyyy-mm-dd hh24:mi:ss') ";
			}
			if("02".equals(recordId)){
				hql = hql+" and t.moStat!=3";
			}
			if("03".equals(recordId)){
				hql = hql+" and t.moStat=3";
			}
			if("04".equals(recordId)){
				hql = hql+" and substr(to_char(t.dateBgn,'yyyy-mm-dd hh24:mi:ss'),1,10)=substr(to_char(to_date('"+now+"','yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss'),1,10) ";
			}
			if("05".equals(recordId)){
				hql = hql+" and t.moStat=0";
			}
			if("06".equals(recordId)){//开立状态
				hql = hql+" and t.moDate between to_date('"+onow+"','yyyy-mm-dd hh24:mi:ss') and to_date('"+now+"','yyyy-mm-dd hh24:mi:ss')";
			}
		}
		
		hql = hql+" ORDER BY t.combNo,t.sortId";			
		Query query = getSession().createSQLQuery(hql)
					.addScalar("id")
					.addScalar("inpatientNo")
					.addScalar("patientNo")
					.addScalar("deptCode")
					.addScalar("nurseCellCode")
					.addScalar("listDpcd")
					.addScalar("moOrder")
					.addScalar("docCode")
					.addScalar("docName")
					.addScalar("moDate",Hibernate.TIMESTAMP)
					.addScalar("babyFlag",Hibernate.INTEGER)
					.addScalar("happenNo",Hibernate.INTEGER)
					.addScalar("setItmattr",Hibernate.INTEGER)
					.addScalar("setSubtbl",Hibernate.INTEGER)
					.addScalar("typeCode")
					.addScalar("typeName")
					.addScalar("decmpsState",Hibernate.INTEGER)
					.addScalar("chargeState",Hibernate.INTEGER)
					.addScalar("needDrug",Hibernate.INTEGER)
					.addScalar("prnExelist",Hibernate.INTEGER)
					.addScalar("prmMorlist",Hibernate.INTEGER)
					.addScalar("needConfirm",Hibernate.INTEGER)
					.addScalar("itemType")
					.addScalar("itemCode")
					.addScalar("itemName")
					.addScalar("classCode")
					.addScalar("className")
					.addScalar("pharmacyCode")
					.addScalar("execDpcd")
					.addScalar("execDpnm")
					.addScalar("baseDose",Hibernate.DOUBLE)
					.addScalar("doseUnit")
					.addScalar("minUnit")
					.addScalar("priceUnit")
					.addScalar("packQty",Hibernate.INTEGER)
					.addScalar("specs")
					.addScalar("doseModelCode")
					.addScalar("drugType")
					.addScalar("drugQuality")
					.addScalar("itemPrice",Hibernate.DOUBLE)
					.addScalar("combNo")
					.addScalar("mainDrug",Hibernate.INTEGER)
					.addScalar("moStat",Hibernate.INTEGER)
					.addScalar("usageCode")
					.addScalar("useName")
					.addScalar("englishAb")
					.addScalar("frequencyCode")
					.addScalar("frequencyName")
					.addScalar("doseOnce",Hibernate.DOUBLE)
					.addScalar("stocKin",Hibernate.INTEGER)
					.addScalar("qtyTot",Hibernate.DOUBLE)
					.addScalar("useDays",Hibernate.INTEGER)
					.addScalar("dateBgn",Hibernate.TIMESTAMP)
					.addScalar("dateEnd",Hibernate.TIMESTAMP)
					.addScalar("recUsercd")
					.addScalar("recUsernm")
					.addScalar("confirmFlag",Hibernate.INTEGER)
					.addScalar("confirmDate",Hibernate.TIMESTAMP)
					.addScalar("confirmUsercd")
					.addScalar("dcFlag",Hibernate.INTEGER)
					.addScalar("dcDate",Hibernate.TIMESTAMP)
					.addScalar("dcCode")
					.addScalar("dcName")
					.addScalar("dcDoccd")
					.addScalar("dcDocnm")
					.addScalar("dcUsercd")
					.addScalar("dcUsernm")
					.addScalar("executeFlag",Hibernate.INTEGER)
					.addScalar("executeDate",Hibernate.TIMESTAMP)
					.addScalar("executeUsercd")
					.addScalar("decoFlag",Hibernate.INTEGER)
					.addScalar("dateCurmodc",Hibernate.TIMESTAMP)
					.addScalar("dateNxtmodc",Hibernate.TIMESTAMP)
					.addScalar("moNote1")	
					.addScalar("moNote2")
					.addScalar("hypotest",Hibernate.INTEGER)
					.addScalar("itemNote")
					.addScalar("applyNo")
					.addScalar("emcFlag",Hibernate.INTEGER)
					.addScalar("getFlag",Hibernate.INTEGER)
					.addScalar("subtblFlag",Hibernate.INTEGER)
					.addScalar("sortId",Hibernate.INTEGER)
					.addScalar("dcConfirmDate",Hibernate.TIMESTAMP)
					.addScalar("dcConfirmOper")
					.addScalar("dcConfirmFlag",Hibernate.INTEGER)
					.addScalar("labCode")
					.addScalar("permission",Hibernate.INTEGER)
					.addScalar("packageCode")
					.addScalar("packageName")
					.addScalar("mark1")
					.addScalar("mark2")
					.addScalar("mark3")
					.addScalar("execTimes")
					.addScalar("execDose")
					.addScalar("createUser")
					.addScalar("createDept")
					.addScalar("createTime",Hibernate.TIMESTAMP)
					.addScalar("updateUser")
					.addScalar("updateTime",Hibernate.TIMESTAMP)
					.addScalar("deleteUser")
					.addScalar("deleteTime",Hibernate.TIMESTAMP)
					.addScalar("del_flg",Hibernate.INTEGER)
					.addScalar("stop_flg",Hibernate.INTEGER)
					.addScalar("drugpackagingUnit");				
		List<InpatientOrder> inpatientOrder= query.setResultTransformer(Transformers.aliasToBean(InpatientOrder.class)).list();
		
		if(inpatientOrder!=null && inpatientOrder.size()>0){
			return inpatientOrder;
		}		
		return new ArrayList<InpatientOrder>();
	}

	@Override
	public List<DrugInfo> queryDrugInfo() {
		String hql = "from DrugInfo t where t.stop_flg = 0 and t.del_flg = 0";
		List<DrugInfo> drugInfo=this.getSession().createQuery(hql).list();
		if(drugInfo!=null && drugInfo.size()>0){
			return drugInfo;
		}		
		return new ArrayList<DrugInfo>();
	}

	@Override
	public List<DrugUndrug> queryNoDrugInfo(String id) {
		String hql = "from DrugUndrug t where t.stop_flg = 0 and t.del_flg = 0";
		if(StringUtils.isNotBlank(id)){
			hql =hql+" and t.id='"+id+"'";
		}
		List<DrugUndrug> drugUndrug=this.getSession().createQuery(hql).list();
		if(drugUndrug!=null && drugUndrug.size()>0){
			return drugUndrug;
		}		
		return new ArrayList<DrugUndrug>();
	}

	@Override
	public List<UnitsVo> queryUnits() {
		String sql ="SELECT d.CODE_ENCODE AS id,d.CODE_NAME AS name FROM "+HisParameters.HISPARSCHEMAHISUSER+"T_BUSINESS_DICTIONARY d WHERE d.code_type = 'drugPackagingunit'";
		SQLQuery queryObject = this.getSession().createSQLQuery(sql).addScalar("id").addScalar("name");
		List<UnitsVo> unitsList = queryObject.setResultTransformer(Transformers.aliasToBean(UnitsVo.class)).list();
		if(unitsList==null||unitsList.size()<=0){
			return new ArrayList<UnitsVo>();
		}
		return unitsList;
	}
	public String querySql(String name,String type,String sysTypeName,String typeCode,String id){
		SysDepartment  dept = (SysDepartment)SessionUtils.getCurrentUserLoginPharmacyFromShiroSession();//获取当前用户选择的药房
		String sql="";
		String Value = parameterInnerDAO.getParameterByCode("yzklypdjdm");//医嘱开立药品等级代码
		if("".equals(Value)){
			Value = "0";
		}
		List<BusinessDictionary> codeDrugtype=new ArrayList<BusinessDictionary>();
		if(Value.indexOf("0")!=-1){
			if(StringUtils.isNotBlank(type)){
				String hql = "from BusinessDictionary t where t.stop_flg = 0 and t.del_flg = 0 and t.type = 'systemType' and t.name='"+sysTypeName+"'";
				codeDrugtype=this.getSession().createQuery(hql).list();
			}
			SysDepartment dept1 = (SysDepartment) SessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
			if(dept1==null){//登录科室为空是返回null
				return null;
			}
			if("全部".equals(sysTypeName)){//hedong 20161012 d.STORE_SUM-PREOUT_SUM 改为 (nvl(d.STORE_SUM,0) - nvl(d.PREOUT_SUM,0))
				String sql1 = "select c.CODE_ENCODE as classId from t_business_advicetosystemclass t "
						+ "inner join T_INPATIENT_KIND d on t.typeid = d.TYPE_CODE "
						+ "inner join T_BUSINESS_DICTIONARY c on c.CODE_ENCODE = t.classid "
						+ "where t.stop_flg = 0 and t.del_flg = 0 and c.code_type = 'systemType' and d.type_code='"+typeCode+"'";
				SQLQuery queryObject = this.getSession().createSQLQuery(sql1)
				        .addScalar("classId");
				List<BusinessAdvicetoSystemclass> sysList = queryObject.setResultTransformer(Transformers.aliasToBean(BusinessAdvicetoSystemclass.class)).list();
				String type1 = "";
				if(sysList.size()>0){
					for (BusinessAdvicetoSystemclass businessAdvicetoSystemclass : sysList) {
						if(StringUtils.isBlank(type1)){
							type1 = businessAdvicetoSystemclass.getClassId();
						}else{
							type1 += "','"+businessAdvicetoSystemclass.getClassId();
						}
					}
				}
				sql ="select c.* from (select a.DRUG_CODE as itemCode,a.DRUG_ID as itemId,a.DRUG_COMMONNAME as name,a.DRUG_SYSTYPE as sysType,a.DRUG_SPEC as specs,a.DRUG_RETAILPRICE as defaultprice,a.DRUG_PACKAGINGUNIT as drugPackagingunit,a.DRUG_MINIMUMUNIT as unit,a.DRUG_GRADE as drugGrade,a.DRUG_ISCOOPERATIVEMEDICAL as undrugIsprovincelimit,a.DRUG_ISCITYLIMIT as undrugIscitylimit,a.SELF_FLG as undrugIsownexpense,a.DRUG_RESTRICTIONOFANTIBIOTIC as undrugIsspecificitems,";
				sql =sql+" a.DRUG_CNAMEPINYIN as namepinyin,a.DRUG_CNAMEWB as namewb,a.DRUG_CNAMEINPUTCODE as nameinputcode,a.DRUG_COMMONNAME as drugCommonname,a.DRUG_ID as drugId,d.STORAGE_DEPTID as dept,'' as inspectionSite,'' as diseaseClassification,'' as specialtyName,'' as medicalHistory,'' as requirements,a.DRUG_NOTES as notes,a.DRUG_GBCODE as gbcode,a.DRUG_INSTRUCTION as drugInstruction,a.DRUG_BASICDOSE as drugOncedosage,a.DRUG_DOSEUNIT as drugDoseunit,a.DRUG_FREQUENCY as drugFrequency,d.low_sum as lowSum,a.DRUG_DOSAGEFORM as drugDosageform,a.DRUG_TYPE as drugType,a.DRUG_NATURE as drugNature,a.DRUG_RETAILPRICE as drugRetailprice,a.DRUG_REMARK as remark,a.DRUG_USEMODE as drugUsemode,a.DRUG_BASICDOSE as drugBasicdose,to_number('') as undrugIsinformedconsent,'1' as ty,a.DRUG_RESTRICTIONOFANTIBIOTIC as drugRestrictionofantibiotic,(nvl(d.STORE_SUM,0) - nvl(d.PREOUT_SUM,0)) as storeSum,a.STOP_FLG as stop_flg,d.STOP_FLG as stockStop_flg,a.DRUG_ISTESTSENSITIVITY as drugIstestsensitivity,a.DRUG_PACKAGINGNUM as packagingnum,a.DRUG_SPLITATTR AS splitattr,B.PROPERTY_CODE AS property "
						+ "from T_DRUG_INFO a left join T_DRUG_STOCKINFO d on a.DRUG_CODE = d.DRUG_ID and d.STORAGE_DEPTID='"+dept.getDeptCode()+"' "
						+ " LEFT JOIN T_BUSINESS_DRUGPROPERTY B ON a.DRUG_CODE = B.DRUG_CODE AND B.DEPT_CODE ='"+dept1.getDeptCode()+"' AND B.STOP_FLG = 0 AND B.DEL_FLG = 0";		
				sql =sql+" union select b.UNDRUG_CODE as itemCode,b.UNDRUG_ID as itemId,b.UNDRUG_NAME as name,b.UNDRUG_SYSTYPE as sysType,b.UNDRUG_SPEC as specs,b.UNDRUG_DEFAULTPRICE as defaultprice,b.UNDRUG_UNIT as drugPackagingunit,'' as unit,'' as drugGrade,b.UNDRUG_ISPROVINCELIMIT as undrugIsprovincelimit,b.UNDRUG_ISCITYLIMIT as undrugIscitylimit,b.UNDRUG_ISOWNEXPENSE as undrugIsownexpense,b.UNDRUG_ISSPECIFICITEMS as undrugIsspecificitems,";
				sql =sql+" b.UNDRUG_PINYIN as namepinyin,b.UNDRUG_WB as namewb,b.UNDRUG_INPUTCODE as nameinputcode,'' as drugCommonname,b.UNDRUG_GBCODE as drugId,b.UNDRUG_DEPT as dept,b.UNDRUG_INSPECTIONSITE as inspectionSite,b.UNDRUG_DISEASECLASSIFICATION as diseaseClassification,b.UNDRUG_SPECIALTYNAME as specialtyName,b.UNDRUG_MEDICALHISTORY as medicalHistory,b.UNDRUG_REQUIREMENTS as requirements,b.UNDRUG_NOTES as notes,'' as gbcode,'' as drugInstruction,to_number('') as drugOncedosage,'' as drugDoseunit,'' as drugFrequency,to_number('') as lowSum,'' as drugDosageform,'' as drugType,'' as drugNature,to_number('') as drugRetailprice,b.UNDRUG_REMARK as remark,'' as drugUsemode,to_number('') as drugBasicdose,b.UNDRUG_ISINFORMEDCONSENT as undrugIsinformedconsent,'0' as ty,to_number('') as drugRestrictionofantibiotic,to_number('') as storeSum,b.STOP_FLG as stop_flg,to_number('') as stockStop_flg,to_number('') as drugIstestsensitivity,to_number('') as packagingnum,NULL AS splitattr,NULL AS property from T_DRUG_UNDRUG b) c where 1=1";
				if(StringUtils.isNotBlank(type1)){
					sql =sql+" and c.sysType in('"+type1+"')";
				}
				if(StringUtils.isNotBlank(name)){
					sql =sql+" and (c.name like '%"+name+"%' or c.namepinyin like '%"+name+"%' or c.namewb like '%"+name+"%' or c.nameinputcode like '%"+name+"%')";
				}	
				if(StringUtils.isNotBlank(id)){
					sql =sql+" and c.itemCode = '"+id+"'";
				}	
			}else{
				if("17".equals(type)||"18".equals(type)||"16".equals(type)){//hedong 20161012 d.STORE_SUM-PREOUT_SUM 改为 (nvl(d.STORE_SUM,0) - nvl(d.PREOUT_SUM,0))
					sql ="select c.* from (select a.DRUG_CODE as itemCode,a.DRUG_ID as itemId,a.DRUG_COMMONNAME as name,a.DRUG_SYSTYPE as sysType,a.DRUG_SPEC as specs,a.DRUG_RETAILPRICE as defaultprice,a.DRUG_PACKAGINGUNIT as drugPackagingunit,a.DRUG_MINIMUMUNIT as unit,a.DRUG_GRADE as drugGrade,a.DRUG_ISCOOPERATIVEMEDICAL as undrugIsprovincelimit,a.DRUG_ISCITYLIMIT as undrugIscitylimit,a.SELF_FLG as undrugIsownexpense,a.DRUG_RESTRICTIONOFANTIBIOTIC as undrugIsspecificitems,";
					sql =sql+" a.DRUG_CNAMEPINYIN as namepinyin,a.DRUG_CNAMEWB as namewb,a.DRUG_CNAMEINPUTCODE as nameinputcode,a.DRUG_COMMONNAME as drugCommonname,a.DRUG_ID as drugId,d.STORAGE_DEPTID as dept,'' as inspectionSite,'' as diseaseClassification,'' as specialtyName,'' as medicalHistory,'' as requirements,a.DRUG_NOTES as notes,a.DRUG_GBCODE as gbcode,a.DRUG_INSTRUCTION as drugInstruction,a.DRUG_ONCEDOSAGE as drugOncedosage,a.DRUG_DOSEUNIT as drugDoseunit,a.DRUG_FREQUENCY as drugFrequency,d.low_sum as lowSum,a.DRUG_DOSAGEFORM as drugDosageform,a.DRUG_TYPE as drugType,a.DRUG_NATURE as drugNature,a.DRUG_RETAILPRICE as drugRetailprice,a.DRUG_REMARK as remark,a.DRUG_USEMODE as drugUsemode,a.DRUG_BASICDOSE as drugBasicdose,to_number('') as undrugIsinformedconsent,'1' as ty,a.DRUG_RESTRICTIONOFANTIBIOTIC as drugRestrictionofantibiotic,(nvl(d.STORE_SUM,0) - nvl(d.PREOUT_SUM,0)) as storeSum,a.STOP_FLG as stop_flg,d.STOP_FLG as stockStop_flg,a.DRUG_ISTESTSENSITIVITY as drugIstestsensitivity,a.DRUG_PACKAGINGNUM as packagingnum,a.DRUG_SPLITATTR AS splitattr,B.PROPERTY_CODE AS property "
						+ "from T_DRUG_INFO a left join T_DRUG_STOCKINFO d on a.DRUG_CODE = d.DRUG_ID and d.STORAGE_DEPTID='"+dept.getDeptCode()+"'"
						+ " LEFT JOIN T_BUSINESS_DRUGPROPERTY B ON a.DRUG_CODE = B.DRUG_CODE AND B.DEPT_CODE ='"+dept1.getDeptCode()+"' AND B.STOP_FLG = 0 AND B.DEL_FLG = 0) c where 1=1";
					if(StringUtils.isNotBlank(type)){
						sql =sql+" and c.sysType='"+type+"'";
					}
					if(StringUtils.isNotBlank(name)){
						sql =sql+" and (c.name like '%"+name+"%' or c.namepinyin like '%"+name+"%' or c.namewb like '%"+name+"%' or c.nameinputcode like '%"+name+"%')";
					}	
					if(StringUtils.isNotBlank(id)){
						sql =sql+" and c.itemCode = '"+id+"'";
					}
				}else{//类别下拉时除西药外会走此方法获得不同类别的名称列表数据 hedong 20161012
					sql ="select c.* from (select b.UNDRUG_CODE as itemCode,b.UNDRUG_ID as itemId,b.UNDRUG_NAME as name,"
							+ "b.UNDRUG_SYSTYPE as sysType,b.UNDRUG_SPEC as specs,b.UNDRUG_DEFAULTPRICE as defaultprice,"
							+ "b.UNDRUG_UNIT as drugPackagingunit,'' as unit,'' as drugGrade,b.UNDRUG_ISPROVINCELIMIT as undrugIsprovincelimit,"
							+ "b.UNDRUG_ISCITYLIMIT as undrugIscitylimit,b.UNDRUG_ISOWNEXPENSE as undrugIsownexpense,"
							+ "b.UNDRUG_ISSPECIFICITEMS as undrugIsspecificitems,";
					sql =sql+" b.UNDRUG_PINYIN as namepinyin,b.UNDRUG_WB as namewb,"
							+ "b.UNDRUG_INPUTCODE as nameinputcode,'' as drugCommonname,b.UNDRUG_GBCODE as drugId,"
							+ "b.UNDRUG_DEPT as dept,b.UNDRUG_INSPECTIONSITE as inspectionSite,"
							+ "b.UNDRUG_DISEASECLASSIFICATION as diseaseClassification,"
							+ "b.UNDRUG_SPECIALTYNAME as specialtyName,b.UNDRUG_MEDICALHISTORY as medicalHistory,b.UNDRUG_REQUIREMENTS as requirements,"
							+ "b.UNDRUG_NOTES as notes,'' as gbcode,'' as drugInstruction,to_number('') as drugOncedosage,"
							+ "'' as drugDoseunit,'' as drugFrequency,to_number('') as lowSum,'' as drugDosageform,'' "
							+ "as drugType,'' as drugNature,to_number('') as drugRetailprice,"
							+ "b.UNDRUG_REMARK as remark,'' as drugUsemode,to_number('') as drugBasicdose,"
							+ "b.UNDRUG_ISINFORMEDCONSENT as undrugIsinformedconsent,'0' as ty,to_number('') as drugRestrictionofantibiotic,"
							+ "to_number('') as storeSum,b.STOP_FLG as stop_flg,to_number('') as stockStop_flg,to_number('') as drugIstestsensitivity,"
							+ "to_number('') as packagingnum,NULL AS splitattr,NULL AS property from T_DRUG_UNDRUG b) c where 1=1";
					if(type!=null && !"".equals(type)&&!type.equals("09")){
						sql =sql+" and c.sysType='"+type+"'";
					}
					if(StringUtils.isNotBlank(name)){
						sql =sql+" and (c.name like '%"+name+"%' or c.namepinyin like '%"+name+"%' or c.namewb like '%"+name+"%' or c.nameinputcode like '%"+name+"%')";
					}	
					if(StringUtils.isNotBlank(id)){
						sql =sql+" and c.itemCode = '"+id+"'";
					}
				}
			}
		}
		return sql;
	}
	@Override
	public int querySysInfoTotal(String name,
			String type, String sysTypeName,String typeCode, String id) {
		String sql = querySql(name,type,sysTypeName,typeCode,id);
		return super.getSqlTotal(sql.toString());
	}
	@Override
	public List<ProInfoVo> querySysInfo(String page, String rows,String name,String type,String sysTypeName,String typeCode,String id) {
		String sql = querySql(name,type,sysTypeName,typeCode,id);
		sql=sql+" order by c.storeSum desc,c.drugGrade asc";
		SQLQuery queryObject = this.getSession().createSQLQuery(sql)
				        .addScalar("itemCode")
						.addScalar("itemId")
						.addScalar("name")
						.addScalar("sysType")
						.addScalar("specs")
						.addScalar("defaultprice",Hibernate.DOUBLE)
						.addScalar("drugPackagingunit")
						.addScalar("unit")
						.addScalar("drugGrade")
						.addScalar("undrugIsprovincelimit",Hibernate.INTEGER)
						.addScalar("undrugIscitylimit",Hibernate.INTEGER)
						.addScalar("undrugIsownexpense",Hibernate.INTEGER)
						.addScalar("undrugIsspecificitems",Hibernate.INTEGER)
						.addScalar("namepinyin")
						.addScalar("namewb")
						.addScalar("nameinputcode")
						.addScalar("drugCommonname")
						.addScalar("drugId")
						.addScalar("dept")
						.addScalar("inspectionSite")
						.addScalar("diseaseClassification")
						.addScalar("specialtyName")
						.addScalar("medicalHistory")
						.addScalar("requirements")
						.addScalar("notes")
						.addScalar("gbcode")
						.addScalar("drugInstruction")
						.addScalar("drugOncedosage",Hibernate.DOUBLE)
						.addScalar("drugDoseunit")
						.addScalar("drugFrequency")
						.addScalar("lowSum",Hibernate.DOUBLE)
						.addScalar("drugDosageform")
						.addScalar("drugType")
						.addScalar("drugNature")
						.addScalar("drugRetailprice",Hibernate.DOUBLE)
						.addScalar("remark")
						.addScalar("drugUsemode")
						.addScalar("drugBasicdose",Hibernate.DOUBLE)
						.addScalar("undrugIsinformedconsent",Hibernate.INTEGER)
						.addScalar("ty",Hibernate.INTEGER)
						.addScalar("drugRestrictionofantibiotic",Hibernate.INTEGER)
						.addScalar("storeSum",Hibernate.DOUBLE)
						.addScalar("stop_flg",Hibernate.INTEGER)
						.addScalar("stockStop_flg",Hibernate.INTEGER)
						.addScalar("drugIstestsensitivity",Hibernate.INTEGER)
						.addScalar("packagingnum",Hibernate.INTEGER)
						.addScalar("splitattr",Hibernate.INTEGER)//药品表拆分属性
						.addScalar("property",Hibernate.INTEGER);//药品拆分维护表属性0-不可拆分  1-可拆分,配药时不取整  2-可拆分配药时上取整 3不可拆分，当日取整;
		int start = Integer.parseInt(page==null?"1":page);
		int count = Integer.parseInt(rows==null?"20":rows);
		List<ProInfoVo> sysInfoList = queryObject.setFirstResult((start - 1) * count).setMaxResults(count).setResultTransformer(Transformers.aliasToBean(ProInfoVo.class)).list();
		if(sysInfoList==null||sysInfoList.size()<=0){
			return new ArrayList<ProInfoVo>();
		}
		return sysInfoList;
	}

	@Override
	public Map<String, String> queryDrugpackagingunit() {
		Map<String, String> codeDrugpackagingunitMap = new HashMap<String, String>();
		String hql = "from BusinessDictionary t where t.stop_flg = 0 and t.del_flg = 0 and t.type = 'drugPackagingunit'";
		List<BusinessDictionary> codeDrugpackagingunit=this.getSession().createQuery(hql).list();	
		if(codeDrugpackagingunit!=null&&codeDrugpackagingunit.size()>0){
			for(BusinessDictionary packagingunit : codeDrugpackagingunit){
				codeDrugpackagingunitMap.put(packagingunit.getEncode(), packagingunit.getName());
			}
		}
		return codeDrugpackagingunitMap;
	}
	
	@Override
	public Map<String, String> queryNonmedicineencoding() {
		Map<String, String> nonmedicineencodingMap = new HashMap<String, String>();
		String hql = "from BusinessDictionary t where t.stop_flg = 0 and t.del_flg = 0 and t.type = 'nonmedicineencoding'";
		List<BusinessDictionary> nonmedicineencoding=this.getSession().createQuery(hql).list();	
		if(nonmedicineencoding!=null&&nonmedicineencoding.size()>0){
			for(BusinessDictionary unit : nonmedicineencoding){
				nonmedicineencodingMap.put(unit.getEncode(), unit.getName());
			}
		}
		return nonmedicineencodingMap;
	}
	

	@Override
	public Map<String, String>  queryDruggrade() {
		Map<String, String> dsMap = new HashMap<String, String>();
		String hql = "from BusinessDictionary t where t.stop_flg = 0 and t.del_flg = 0 and t.type = 'drugGrade'";
		List<BusinessDictionary> codeDruggrade=this.getSession().createQuery(hql).list();
		if(codeDruggrade!=null && codeDruggrade.size()>0){
			for(BusinessDictionary cs : codeDruggrade){
				dsMap.put(cs.getEncode(), cs.getName());
			}
		}		
		return dsMap;
	}
	@Override
	public Map<String, String> queryImplDepartment(String deptCode) {
		Map<String, String> dsMap = new HashMap<String, String>();
		String hql = "from SysDepartment t where t.stop_flg = 0 and t.del_flg = 0";
		if(StringUtils.isNotBlank(deptCode)){
			hql = hql+" and t.deptCode ='"+deptCode+"'";
		}
		List<SysDepartment> sysDepartment=this.getSession().createQuery(hql).list();
		if(sysDepartment!=null&&sysDepartment.size()>0){
			for(SysDepartment cs : sysDepartment){
				dsMap.put(cs.getDeptCode(), cs.getDeptName());
			}
		}	
		return dsMap;
	}


	@Override
	public Map<String, String> queryDrugStorage() {
		Map<String, String> dsMap = new HashMap<String, String>(); 
		String hql = "from DrugStorage t where t.stop_flg = 0 and t.del_flg = 0";
		List<DrugStorage> drugStorage=super.find(hql, null);
		if(drugStorage!=null&&drugStorage.size()>0){
			for(DrugStorage ds : drugStorage){
				dsMap.put(ds.getDrugId(), ds.getStoreSum().toString());
			}
		}
		return dsMap;		
	}

	@Override
	public Map<String, String> querySystemtype() {
		Map<String, String> dsMap = new HashMap<String, String>();
		String hql = "from BusinessDictionary t where t.stop_flg = 0 and t.del_flg = 0 and t.type = 'systemType'";
		List<BusinessDictionary> codeSystemtype=this.getSession().createQuery(hql).list();
		if(codeSystemtype!=null&&codeSystemtype.size()>0){
			for(BusinessDictionary cs : codeSystemtype){
				dsMap.put(cs.getEncode(), cs.getName());
			}
		}
		return dsMap;
	}

	@Override
	public Map<String, String> queryFrequency() {
		Map<String, String> dsMap = new HashMap<String, String>();
		String hql = "from BusinessFrequency t where t.stop_flg = 0 and t.del_flg = 0";
		List<BusinessFrequency> frequency=this.getSession().createQuery(hql).list();
		if(frequency!=null&&frequency.size()>0){
			for(BusinessFrequency cs : frequency){
				dsMap.put(cs.getEncode(), cs.getName());
			}
		}
		return dsMap;
	}

	@Override
	public Map<String, String> queryDrugUsemode() {
		Map<String, String> dsMap = new HashMap<String, String>();
		String hql = "from BusinessDictionary t where t.stop_flg = 0 and t.del_flg = 0 and t.type = 'useage'";
		List<BusinessDictionary> codeUseage=this.getSession().createQuery(hql).list();
		if(codeUseage!=null&&codeUseage.size()>0){
			for(BusinessDictionary cs : codeUseage){
				dsMap.put(cs.getEncode(), cs.getName());
			}
		}
		return dsMap;
	}

	@Override
	public List<BusinessOdditionalitem> queryOdditionalitem(String code,String deptId,int drugFlag) {
		String hql = "from BusinessOdditionalitem t where t.stop_flg = 0 and t.del_flg = 0 and t.typeCode='"+code+"' and t.deptCode='"+deptId+"' and t.drugFlag="+drugFlag+"";
		List<BusinessOdditionalitem> odditionalitem=this.getSession().createQuery(hql).list();
		if(odditionalitem!=null && odditionalitem.size()>0){
			return odditionalitem;
		}		
		return new ArrayList<BusinessOdditionalitem>();
	}

	@Override
	public InpatientOrderNow queryInpatientOrder(String Id) {
		String sql = "select * from T_INPATIENT_ORDER_NOW t where t.stop_flg = 0 and t.del_flg = 0 and t.EXEC_ID = '"+Id+"'";
		InpatientOrderNow inpatientOrder = (InpatientOrderNow) getSession().createSQLQuery(sql).addEntity(InpatientOrderNow.class).uniqueResult();		
		if(inpatientOrder != null){
			return inpatientOrder;
		}		
		return null;
	}

	@Override
	public List<SysDruggraDecontraStrank> queryDruggraDecontraStrank(
			String userId, String drugGrade) {
		String hql = "from SysDruggraDecontraStrank t where t.stop_flg = 0 and t.del_flg = 0";
		if(StringUtils.isNotBlank(drugGrade)){
			hql = hql+" and t.druggraade in(select b.encode from CodeDruggrade b where b.id = '"+drugGrade+"')";
		}
		if(StringUtils.isNotBlank(userId)){
			hql = hql+" and t.tpost in (select c.encode from CodeTitle c where c.id in(select b.title from SysEmployee b where b.userId = '"+userId+"'))";
		}
		List<SysDruggraDecontraStrank> druggraDecontraStrank=this.getSession().createQuery(hql).list();
		if(druggraDecontraStrank!=null && druggraDecontraStrank.size()>0){
			return druggraDecontraStrank;
		}		
		return new ArrayList<SysDruggraDecontraStrank>();
	}

	@Override
	public List<BusinessAdvdrugnature> queryAdvdrugnature(String drugNature) {
		String hql = "from BusinessAdvdrugnature t where t.stop_flg = 0 and t.del_flg = 0 and t.encode ='"+drugNature+"'";
		List<BusinessAdvdrugnature> advdrugnature=this.getSession().createQuery(hql).list();
		if(advdrugnature!=null && advdrugnature.size()>0){
			return advdrugnature;
		}		
		return new ArrayList<BusinessAdvdrugnature>();
		
	}

	@Override
	public List<InpatientOrderNow> getPage(String page, String rows,InpatientOrderNow entity,String recordId) {
		String hql = joint(entity,recordId);
		return inpatientOrderNowDAO.getPage( page, rows,hql);
	}

	@Override
	public int getTotal(InpatientOrderNow entity,String recordId) {
		String hql = joint(entity,recordId);
		return inpatientOrderNowDAO.getTotal(hql);
	}
	
	public String joint(InpatientOrderNow entity,String recordId){
		Date date = new Date();
		DateFormat d1 = DateFormat.getDateTimeInstance();
		String now=d1.format(date);
		String fnow=d1.format(new Date(date.getTime() + 5 * 24 * 60 * 60 * 1000));
		String onow=null;
		if("06".equals(recordId)){
			onow=d1.format(new Date(date.getTime() - 1 * 24 * 60 * 60 * 1000));
		}
		String str = parameterInnerDAO.getParameterByCode("showyzdata");//hedong  20161012 无法显示患者历史医嘱记录  此处报错 .6无此数据
		if("".equals(str)){
			str = "7"; //默认显示7天的历史医嘱
		}
		 
		Date d2 = DateUtils.addDay(DateUtils.getCurrentTime(),-Integer.parseInt(str));
		String dtime=d1.format(d2);
		String hql = "from InpatientOrderNow t where t.stop_flg = 0 and t.del_flg = 0 and t.subtblFlag!=1 ";
		if("1".equals(entity.getDecmpsState().toString()) && !"01".equals(recordId)){
			if(StringUtils.isBlank(recordId)){
				hql = hql+" and  t.moStat=2  ";
			}
			if("02".equals(recordId)){//开立状态也走这里
				hql = hql+" and t.moStat!=3";
			}
			if("03".equals(recordId)){
				hql = hql+" and t.moStat=3";
			}
			if("04".equals(recordId)){
				hql = hql+" and substr(to_char(t.dateBgn,'yyyy-mm-dd hh24:mi:ss'),1,10)=substr(to_char(to_date('"+now+"','yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss'),1,10) ";
			}
			if("05".equals(recordId)){
				hql = hql+" and t.moStat=0";
			}
		}
		if("0".equals(entity.getDecmpsState().toString()) && !"01".equals(recordId)){
			if(StringUtils.isBlank(recordId)){
				hql = hql+" and  (t.moStat=2) ";
			}
			if("02".equals(recordId)){
				hql = hql+" and t.moStat!=3";
			}
			if("03".equals(recordId)){
				hql = hql+" and t.moStat=3";
			}
			if("04".equals(recordId)){
				hql = hql+" and substr(to_char(t.dateBgn,'yyyy-mm-dd hh24:mi:ss'),1,10)=substr(to_char(to_date('"+now+"','yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss'),1,10) ";
			}
			if("05".equals(recordId)){
				hql = hql+" and t.moStat=0";
			}
			if("06".equals(recordId)){//开立状态
				hql = hql+" and t.moDate between to_date('"+onow+"','yyyy-mm-dd hh24:mi:ss') and to_date('"+now+"','yyyy-mm-dd hh24:mi:ss')";
			}
		}
		if(StringUtils.isNotBlank(entity.getInpatientNo())){
			hql = hql+" and t.inpatientNo ='"+entity.getInpatientNo()+"'";
		}
		if(entity.getDecmpsState() != null){
			hql = hql+" and t.decmpsState ="+entity.getDecmpsState()+"";
		}
		hql = hql+" ORDER BY t.combNo,t.sortId";
		return hql;
	}

	@Override
	public List<BusinessFrequency> queryBusinessFrequency(String frequencyCode) {
		String hql = "from BusinessFrequency c where c.encode ='"+frequencyCode+"'";//被action层次调用c.id 改为c.encode modified by hedong 20160825
		List<BusinessFrequency> businessFrequency=this.getSession().createQuery(hql).list();
		if(businessFrequency!=null && businessFrequency.size()>0){
			return businessFrequency;
		}		
		return new ArrayList<BusinessFrequency>();
	}

	@Override
	public List<BusinessFrequency> queryFrequencyByEncode(String frequencyEncode) {
		String hql = "from BusinessFrequency c where c.encode ='"+frequencyEncode+"'";		
		List<BusinessFrequency> businessFrequency=this.getSession().createQuery(hql).list();
		if(businessFrequency!=null && businessFrequency.size()>0){
			return businessFrequency;
		}		
		return new ArrayList<BusinessFrequency>();
	}

	@Override
	public List<InpatientOrderNow> queryInpatientOrderById(String id,String combNo,String mainDrug,String subtblFlag) {
		String hql = "from InpatientOrderNow c where 1=1";	
		if(StringUtils.isNotBlank(id)){
			hql = hql+" and c.id ='"+id+"'";
		}
		if(StringUtils.isNotBlank(combNo)){
			hql = hql+" and c.combNo ='"+combNo+"'";
		}
		if(StringUtils.isNotBlank(mainDrug)){		
			hql = hql+" and c.mainDrug = "+mainDrug+"";
		}
		if(StringUtils.isNotBlank(subtblFlag)){
			hql = hql+" and c.subtblFlag = "+subtblFlag+"";
		}
		List<InpatientOrderNow> inpatientOrderList=this.getSession().createQuery(hql).list();
		if(inpatientOrderList!=null && inpatientOrderList.size()>0){
			return inpatientOrderList;
		}		
		return new ArrayList<InpatientOrderNow>();
	}

	@Override
	public List<InpatientOrderNow> queryMaxInpatientOrderSortId() {
		String hql = "from InpatientOrderNow t where t.sortId in(select max(c.sortId) from InpatientOrderNow c)";	
		List<InpatientOrderNow> inpatientOrderList=this.getSession().createQuery(hql).list();
		if(inpatientOrderList!=null && inpatientOrderList.size()>0){
			return inpatientOrderList;
		}		
		return new ArrayList<InpatientOrderNow>();
	}

	@Override
	public Map<String, String> queryCheckpointMap() {
		Map<String, String> dsMap = new HashMap<String, String>();
		String hql = "from BusinessDictionary t where t.stop_flg = 0 and t.del_flg = 0 and t.type = 'checkpoint'";		
		List<BusinessDictionary> codeCheckpoint=this.getSession().createQuery(hql).list();
		if(codeCheckpoint!=null&&codeCheckpoint.size()>0){
			for(BusinessDictionary cs : codeCheckpoint){
				dsMap.put(cs.getEncode(), cs.getName());
			}
		}
		return dsMap;
	}
	
	@Override
	public Map<String, String> querySampleTeptMap() {
		Map<String, String> dsMap = new HashMap<String, String>();
		String hql = "from BusinessDictionary t where t.stop_flg = 0 and t.del_flg = 0 and t.type = 'laboratorysample'";		
		List<BusinessDictionary> laboratorysample=this.getSession().createQuery(hql).list();
		if(laboratorysample!=null&&laboratorysample.size()>0){
			for(BusinessDictionary cs : laboratorysample){
				dsMap.put(cs.getEncode(), cs.getName());
			}
		}
		return dsMap;
	}

	@Override
	public List<InpatientKind> queryDocAdvType(InpatientKind inpatientKind) {
		String hql = "from InpatientKind t where t.stop_flg = 0 and t.del_flg = 0";		
		if(inpatientKind.getFitExtent() != null){
			hql +=" and t.fitExtent = "+inpatientKind.getFitExtent()+"";
		}
		if(inpatientKind.getDecmpsState() != null){
			hql +=" and t.decmpsState = "+inpatientKind.getDecmpsState()+"";
		}
		hql +=" order by t.typeCode";
		List<InpatientKind> kind=this.getSession().createQuery(hql).list();
		if(kind!=null && kind.size()>0){
			return kind;
		}		
		return new ArrayList<InpatientKind>();
	}

	@Override
	public List<BusinessContractunit> queryReglist() {
		String sql = "from BusinessContractunit b where b.stop_flg = 0 and b.del_flg = 0";
		List<BusinessContractunit> list = this.getSession().createQuery(sql).list();
		return list;
	}


	@Override
	public void delInpatientOrder(String id) {
		String sql = "delete InpatientOrderNow t where t.id = ?";		
		this.getSession().createQuery(sql).setString(0, id).executeUpdate();		
	}

	@Override
	public List<BusinessDictionary> queryCodeTitle(String userId) {
		String hql = "from BusinessDictionary c where c.type='title' and c.encode in(select t.title from SysEmployee t where t.stop_flg = 0 and t.del_flg = 0 and t.userId ='"+userId+"')";
		List<BusinessDictionary> codeTitle=this.getSession().createQuery(hql).list();
		if(codeTitle!=null && codeTitle.size()>0){
			return codeTitle;
		}		
		return new ArrayList<BusinessDictionary>();
	}

	@Override
	public List<BusinessFrequency> queryFrequencyList() {
		SysDepartment  loginDept = (SysDepartment)SessionUtils.getCurrentUserLoginDepartmentFromShiroSession();//获取登录科室
		//查询本科室的频次  hedong  20161013
		String hql = "from BusinessFrequency t where t.stop_flg = 0 and t.del_flg = 0 and t.createDept='"+loginDept.getDeptCode()+"'";
		List<BusinessFrequency> frequencyList=this.getSession().createQuery(hql).list();
		if(frequencyList!=null && frequencyList.size()>0){
			return frequencyList;
		}else{//查询所有创建科室为root的频次
			hql = "from BusinessFrequency t where t.stop_flg = 0 and t.del_flg = 0 and t.createDept='ROOT'";
			frequencyList=this.getSession().createQuery(hql).list();
			if(frequencyList!=null && frequencyList.size()>0){
				return frequencyList;
			}
		}		
		return new ArrayList<BusinessFrequency>();
	}

	@Override
	public int updateAdviceList(InpatientOrderNow orderNow) {
		String sql = "update t_inpatient_order_now set MO_STAT = ? where EXEC_ID = ?";  
		Object args[] = new Object[]{"4",orderNow.getId()};  
		int t = jdbcTemplate.update(sql,args);  
		return t;
	}

	@Override
	public InpatientOrderNow getinpaorder(String id) {
		String hql = "from InpatientOrderNow c where c.id='"+id+"'";
		List<InpatientOrderNow> order=this.getSession().createQuery(hql).list();
		if(order!=null && order.size()>0){
			return order.get(0);
		}		
		return new InpatientOrderNow();
	}

	@Override
	public DrugBilllist getListByProperty(String... exist) {
		String hql="SELECT DISTINCT drugBillclass.id "
				+ "FROM DrugBilllist "
				+ "WHERE drugBillclass.id IN "
				+ "(SELECT DISTINCT drugBillclass.id "
				+ "FROM DrugBilllist "
				+ "WHERE typeCode = '"+exist[1]+"') "
				+ "AND drugBillclass.id IN "
				+ "(SELECT DISTINCT drugBillclass.id "
				+ "FROM DrugBilllist "
				+ "WHERE doseModelCode = '"+exist[5]+"' "
				+ "OR drugQuality = '"+exist[4]+"') "
				+ "AND drugBillclass.id IN "
				+ "(SELECT DISTINCT drugBillclass.id "
				+ "FROM DrugBilllist "
				+ "WHERE usageCode = '"+exist[2]+"') "
				+ "AND drugBillclass.id IN "
				+ "(SELECT DISTINCT drugBillclass.id "
				+ "FROM DrugBilllist "
				+ "WHERE drugType = '"+exist[3]+"') "
				+ "and drugBillclass.id != 'R' and drugBillclass.id in(select id "
				+ "from DrugBillclass where controlId in("
				+ "select id from OutpatientDrugcontrol where deptCode='"+exist[0]+"'))";
		List<DrugBilllist> list=super.find(hql, null);
		if(list!=null&&list.size()>0){
			DrugBilllist db;
			db = new DrugBilllist();
			Object a=list.get(0);
			DrugBillclass clas = billclassInInterDAO.get(a.toString());
			db.setDrugBillclass(clas);
			return db;
		}
		return null;
	}

	@Override
	public Map<String, Object> getoutPatient(List<String> tnL,List<String> mainL,String startTime, String endTime,String condition,String type,String page,String rows) {
		Map<String,Object> retMap = new HashMap<String, Object>();
		if(tnL==null||tnL.size()==0||mainL.size()==0){
			retMap.put("total",0);
			retMap.put("rows",new ArrayList<RegistrationNow>());
			return retMap;
		}
		
		StringBuffer sql=new StringBuffer();
		for (int i = 0; i < tnL.size(); i++) {
			if(i>0){
				sql.append("UNION ALL ");
			}
			sql.append("select t"+i+".CLINIC_CODE as clinicCode,t"+i+".PATIENT_NAME as patientName,"
					+ "t"+i+".REG_DATE as regDate,t"+i+".PATIENT_SEXNAME as patientSexName,"
					+ "t"+i+".MEDICALRECORDID as midicalrecordId from ").append(tnL.get(i)).append(" t"+i+" ");
			sql.append(" right join ( ");
			for(int j=0,len=mainL.size();j<len;j++){//关联处方表
				if(j>0){
					sql.append(" union All ");
					}
					sql.append(" select distinct R"+j+".CLINIC_CODE AS clinicCode from "+mainL.get(j)+" R"+j);
					sql.append(" where  R"+j+".STOP_FLG=0 and R"+j+".DEL_FLG=0 ");
				}
				sql.append(") M on M.clinicCode=t"+i+".CLINIC_CODE ");
				sql.append("where t"+i+".del_flg=0 and t"+i+".STOP_FLG=0 ");
				if(StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)){
					sql.append(" and t"+i+".REG_DATE >=  to_date(:startTime ,'yyyy-MM-dd hh24:mi:ss') ");
					sql.append(" and t"+i+".REG_DATE <=  to_date(:endTime ,'yyyy-MM-dd hh24:mi:ss') ");
				}
				if("1".equals(type)){//病历号
					if(StringUtils.isNotBlank(condition)){
						sql.append(" and t"+i+".MEDICALRECORDID = :condition ");
					}
				}else if("2".equals(type)){//门诊号
					if(StringUtils.isNotBlank(condition)){
						sql.append(" and t"+i+".CLINIC_CODE = :condition ");
					}
				}else if("3".equals(type)){
					if(StringUtils.isNotBlank(condition)){
						sql.append(" and t"+i+".PATIENT_NAME = :condition ");
					}
				}
		}
		Map<String, Object> paraMap = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)){
			paraMap.put("startTime", startTime);
			paraMap.put("endTime", endTime);
		}
		if("1".equals(type)){//病历号
			if(StringUtils.isNotBlank(condition)){
				paraMap.put("condition", condition);
			}
		}else if("2".equals(type)){//门诊号
			if(StringUtils.isNotBlank(condition)){
				paraMap.put("condition", condition);
			}
		}else if("3".equals(type)){
			if(StringUtils.isNotBlank(condition)){
				paraMap.put("condition", condition);
			}
		}
		//查询总条数
		StringBuffer bufferTotal = new StringBuffer(sql.toString());
		bufferTotal.insert(0, "SELECT COUNT(1) FROM (" );
		bufferTotal.append(" )");
		int total = namedParameterJdbcTemplate.queryForObject(bufferTotal.toString(), paraMap, java.lang.Integer.class);
		
		//查询对象的sql
		StringBuffer bufferRows = new StringBuffer(sql.toString());
		bufferRows.insert(0, "SELECT * FROM( SELECT tab.*,ROWNUM rn FROM ( ");
		bufferRows.append(") tab WHERE ROWNUM <= :end ) WHERE rn > :start ");
		final int start = Integer.parseInt(page == null ? "1" : page);
		final int count = Integer.parseInt(rows == null ? "20" : rows);
		paraMap.put("start", (start - 1) * count);
		paraMap.put("end", start * count);
		List<RegistrationNow> voList =  namedParameterJdbcTemplate.query(bufferRows.toString(),paraMap,new RowMapper<RegistrationNow>() {
			@Override
			public RegistrationNow mapRow(ResultSet rs, int rowNum) throws SQLException {
				RegistrationNow vo = new RegistrationNow();
				vo.setClinicCode(rs.getString("clinicCode"));
				vo.setPatientName(rs.getString("patientName"));
				vo.setRegDate(rs.getTimestamp("regDate"));
				vo.setPatientSexName(rs.getString("patientSexName"));
				vo.setMidicalrecordId(rs.getString("midicalrecordId"));
				return vo;
		}});
		retMap.put("total", total);
		retMap.put("rows", voList==null&&voList.size()==0?new ArrayList<RegistrationNow>():voList);
		return retMap;
	}

	@Override
	public List<OutpatientRecipedetailNow> queryOutpatientRecipedetail(List<String> tnL,
			String clinicCode,String startTime,String endTime) {
		if(tnL==null||tnL.size()<0){
			return new ArrayList<OutpatientRecipedetailNow>();
		}
		final StringBuffer sb=new StringBuffer();
		for (int i = 0; i < tnL.size(); i++) {
			if(i>0){
				sb.append(" UNION ALL ");
			}
			sb.append("SELECT R"+i+".id AS id, R"+i+".SEE_NO AS seeNo, R"+i+".CLINIC_CODE AS clinicCode, R"+i+".PATIENT_NO AS patientNo, ");
			sb.append("R"+i+".REG_DATE AS regDate, R"+i+".REG_DEPT AS regDept, R"+i+".ITEM_CODE AS itemCode, R"+i+".ITEM_NAME AS itemName, R"+i+".SPECS AS specs, ");
			sb.append("R"+i+".DRUG_FLAG AS drugFlag, R"+i+".CLASS_CODE AS classCode, R"+i+".FEE_CODE AS feeCode, R"+i+".UNIT_PRICE AS unitPrice, R"+i+".QTY AS qty, ");
			sb.append("R"+i+".DAYS AS days, R"+i+".PACK_QTY AS packQty, R"+i+".ITEM_UNIT AS itemUnit, R"+i+".OWN_COST AS ownCost, R"+i+".PAY_COST AS payCost, ");
			sb.append("R"+i+".PUB_COST AS pubCost, R"+i+".BASE_DOSE AS baseDose, R"+i+".SELF_MADE AS selfMade, R"+i+".DRUG_QUANLITY AS drugQuanlity, R"+i+".ONCE_DOSE AS onceDose, ");
			sb.append("R"+i+".ONCE_UNIT AS onceUnit, R"+i+".DOSE_MODEL_CODE AS doseModelCode, R"+i+".FREQUENCY_CODE AS frequencyCode, R"+i+".USAGE_CODE AS usageCode, ");
			sb.append("R"+i+".EXEC_DPCD AS execDpcd, R"+i+".MAIN_DRUG AS mainDrug, R"+i+".COMB_NO AS combNo, R"+i+".HYPOTEST AS hypotest, R"+i+".INJECT_NUMBER AS injectNumber, ");
			sb.append("R"+i+".REMARK AS remark, R"+i+".DOCT_CODE AS doctCode, R"+i+".DOCT_DPCD AS doctDpcd, R"+i+".OPER_DATE AS operDate, R"+i+".STATUS AS status, R"+i+".CANCEL_USERID AS cancelUserid, ");
			sb.append("R"+i+".CANCEL_DATE AS cancelDate, R"+i+".EMC_FLAG AS emcFlag, R"+i+".LAB_TYPE AS labType, R"+i+".CHECK_BODY AS checkBody, R"+i+".APPLY_NO AS applyNo, R"+i+".SUBTBL_FLAG AS subtblFlag, ");
			sb.append("R"+i+".NEED_CONFIRM AS needConfirm, R"+i+".CONFIRM_CODE AS confirmCode, R"+i+".CONFIRM_DEPT AS confirmDept, R"+i+".CONFIRM_DATE AS confirmDate, R"+i+".CHARGE_FLAG AS chargeFlag, ");
			sb.append("R"+i+".CHARGE_CODE AS chargeCode, R"+i+".CHARGE_DATE AS chargeDate, R"+i+".RECIPE_NO AS recipeNo, R"+i+".PHAMARCY_CODE AS phamarcyCode, R"+i+".MINUNIT_FLAG AS minunitFlag, ");
			sb.append("R"+i+".DATAORDER AS dataorder, R"+i+".PRINT_FLAG AS printFlag, R"+i+".CREATEUSER AS createUser, R"+i+".CREATEDEPT AS createDept, R"+i+".CREATETIME AS createTime, ");
			sb.append("R"+i+".UPDATEUSER AS updateUser, R"+i+".UPDATETIME AS updateTime, R"+i+".DELETEUSER AS deleteUser, R"+i+".DELETETIME AS deleteTime, R"+i+".STOP_FLG AS stop_flg, ");
			sb.append("R"+i+".DEL_FLG AS del_flg, R"+i+".SEQUENCE_NO AS sequencenNo, R"+i+".RECIPE_FEESEQ AS recipeFeeseq, R"+i+".RECIPE_SEQ AS recipeSeq, R"+i+".AUDIT_FLG AS auditFlg, ");
			sb.append("R"+i+".AUDIT_REMARK AS auditRemark, R"+i+".REG_DEPT_NAME AS regDeptName, R"+i+".CLASS_NAME AS className, R"+i+".FEE_NAME AS feeName, R"+i+".ONCE_UNIT_NAME AS onceUnitName, ");
			sb.append("R"+i+".DOSE_MODEL_NAME AS doseModelName, R"+i+".FREQUENCY_NAME AS frequencyName, R"+i+".USAGE_NAME AS usageName, R"+i+".EXEC_DPCD_NAME AS execDpcdName, R"+i+".DOCT_NAME AS doctName, ");
			sb.append("R"+i+".DOCT_DPCD_NAME AS doctDpcdName FROM ").append(tnL.get(i));
			sb.append(" R"+i+" ");
			sb.append("WHERE R"+i+".CLINIC_CODE = '"+clinicCode+"' and R"+i+".STOP_FLG=0 and R"+i+".DEL_FLG=0  ");
			
		}
		List<OutpatientRecipedetailNow> voList = (List<OutpatientRecipedetailNow>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public List<OutpatientRecipedetailNow> doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sb.toString())
				.addScalar("id").addScalar("seeNo").addScalar("clinicCode").addScalar("patientNo")
				.addScalar("regDate").addScalar("regDept").addScalar("itemCode").addScalar("itemName").addScalar("specs")
				.addScalar("drugFlag",Hibernate.INTEGER).addScalar("classCode").addScalar("feeCode").addScalar("unitPrice",Hibernate.DOUBLE).addScalar("qty",Hibernate.DOUBLE)
				.addScalar("days",Hibernate.INTEGER).addScalar("packQty",Hibernate.INTEGER).addScalar("itemUnit").addScalar("ownCost",Hibernate.DOUBLE).addScalar("payCost",Hibernate.DOUBLE)
				.addScalar("pubCost",Hibernate.DOUBLE).addScalar("baseDose",Hibernate.DOUBLE).addScalar("selfMade",Hibernate.INTEGER).addScalar("drugQuanlity")
				.addScalar("onceDose",Hibernate.DOUBLE).addScalar("onceUnit").addScalar("doseModelCode").addScalar("frequencyCode").addScalar("usageCode")
				.addScalar("execDpcd").addScalar("mainDrug",Hibernate.INTEGER).addScalar("combNo").addScalar("hypotest",Hibernate.INTEGER).addScalar("injectNumber",Hibernate.INTEGER)
				.addScalar("remark").addScalar("doctCode").addScalar("doctDpcd").addScalar("operDate",Hibernate.TIMESTAMP).addScalar("status",Hibernate.INTEGER)
				.addScalar("cancelUserid").addScalar("cancelDate",Hibernate.DATE).addScalar("emcFlag",Hibernate.INTEGER).addScalar("labType").addScalar("checkBody")
				.addScalar("applyNo").addScalar("subtblFlag",Hibernate.INTEGER).addScalar("needConfirm",Hibernate.INTEGER).addScalar("confirmCode").addScalar("confirmDept")
				.addScalar("confirmDate",Hibernate.DATE).addScalar("chargeFlag",Hibernate.INTEGER).addScalar("chargeCode").addScalar("chargeDate",Hibernate.DATE)
				.addScalar("recipeNo").addScalar("phamarcyCode").addScalar("minunitFlag",Hibernate.INTEGER).addScalar("dataorder",Hibernate.INTEGER)
				.addScalar("printFlag",Hibernate.INTEGER).addScalar("createUser").addScalar("createDept").addScalar("createTime",Hibernate.DATE)
				.addScalar("updateUser").addScalar("updateTime",Hibernate.DATE).addScalar("deleteUser").addScalar("deleteTime",Hibernate.DATE)
				.addScalar("stop_flg",Hibernate.INTEGER).addScalar("del_flg",Hibernate.INTEGER).addScalar("sequencenNo").addScalar("recipeFeeseq")
				.addScalar("recipeSeq",Hibernate.INTEGER).addScalar("auditFlg",Hibernate.INTEGER).addScalar("auditRemark").addScalar("regDeptName")
				.addScalar("className").addScalar("feeName").addScalar("onceUnitName").addScalar("doseModelName")
				.addScalar("frequencyName").addScalar("usageName").addScalar("execDpcdName").addScalar("doctName")
				.addScalar("doctDpcdName");
				return queryObject.setResultTransformer(Transformers.aliasToBean(OutpatientRecipedetailNow.class)).list();
			}
		});
		if(voList!=null&&voList.size()>0){
			return voList;
		}
		return null;
	}

	@Override
	public List<AdviceLong> printAdviceLong(String inpatientNo,String flag) {
		StringBuffer buffer=new StringBuffer(380);
		buffer.append("SELECT T.INPATIENT_NO AS inpatientNo,T.PATIENT_NAME AS patientName,");
		buffer.append("DECODE(T.REPORT_SEX,1,'男',2,'女',3,'未知') AS reportSex,");
		buffer.append("TO_CHAR(T.REPORT_AGE||T.REPORT_AGEUNIT) AS reportAge,T.MEDICALRECORD_ID AS medicalrecordId,");
		buffer.append("T.DEPT_NAME AS deptName,T.BED_NAME AS bedName,");
		buffer.append("T.NURSE_CELL_NAME AS nurseCellCode ");
		buffer.append("FROM T_INPATIENT_INFO_NOW T WHERE T.STOP_FLG = 0 ");
		buffer.append("AND T.DEL_FLG=0 AND T.INPATIENT_NO='"+inpatientNo+"'");
		List<AdviceLong> list=super.getSession().createSQLQuery(buffer.toString()).addScalar("inpatientNo")
				.addScalar("patientName").addScalar("reportSex").addScalar("reportAge").addScalar("medicalrecordId")
				.addScalar("deptName").addScalar("bedName").addScalar("nurseCellCode").setResultTransformer(Transformers.aliasToBean(AdviceLong.class)).list();
		buffer=null;
		
		buffer=new StringBuffer(2320);
		buffer.append("SELECT T.DATE_BGN2 AS dateBgn2,T.DATE_BGN1 AS dateBgn1,TO_CHAR(T.DATE_BGN) AS dateBgn,T.DATE_END2 AS dateEnd2,T.DATE_END1 AS dateEnd1,");
		buffer.append("TO_CHAR(T.DATE_END) AS dateEnd,T.ITEM_NAME AS itemName,T.DOC_CODE AS docCode,T.CONFIRM_USERCD AS confirmUsercd,TO_CHAR(T.CONFIRM_DATE) AS confirmDate,");
		buffer.append("T.DC_DOCNM AS dcDocnm,TO_CHAR(T.SORTID) AS sortId,T.EXECUTE_DATE AS executeDate,T.EXECUTE_USERCD AS executeUsercd,T.COMBNO AS combNo,");
		buffer.append("TO_CHAR(T.DECMPSSTATE) AS decmpsState,TO_CHAR(T.MAINDRUG) AS mainDrug,T.INPATIENTNO AS inpatientNo ");
		buffer.append("FROM (SELECT TO_CHAR(A.DATE_BGN,'hh:mm') AS DATE_BGN2,");
		buffer.append("TO_CHAR(A.DATE_BGN,'yyyy-MM-dd') AS DATE_BGN1,");
		buffer.append("A.DATE_BGN AS DATE_BGN,A.SORT_ID AS SORTID,");
		buffer.append("TO_CHAR(A.DATE_END,'hh:mm') AS DATE_END2,");
		buffer.append("TO_CHAR(A.DATE_END, 'yyyy-MM-dd') AS DATE_END1,");
		buffer.append("A.DATE_END AS DATE_END,A.DECMPS_STATE AS DECMPSSTATE,");
		buffer.append("A.ITEM_NAME AS ITEM_NAME,A.COMB_NO AS COMBNO,");
		buffer.append("(SELECT D.EMPLOYEE_NAME FROM T_EMPLOYEE D WHERE D.EMPLOYEE_JOBNO = A.DOC_CODE) AS DOC_CODE,");
		buffer.append("(SELECT D.EMPLOYEE_NAME FROM T_EMPLOYEE D ");
		buffer.append("WHERE D.EMPLOYEE_JOBNO = A.CONFIRM_USERCD) AS CONFIRM_USERCD,");
		buffer.append("A.CONFIRM_DATE,A.DC_DOCNM,A.EXECUTE_DATE,");
		buffer.append("A.MAIN_DRUG AS MAINDRUG,A.INPATIENT_NO AS INPATIENTNO,");
		buffer.append("(SELECT D.EMPLOYEE_NAME FROM T_EMPLOYEE D ");
		buffer.append("WHERE D.EMPLOYEE_JOBNO = A.EXECUTE_USERCD) AS EXECUTE_USERCD ");
		buffer.append("FROM HONRYHIS.T_INPATIENT_ORDER_NOW A ");
		buffer.append("WHERE A.STOP_FLG = 0 AND A.DEL_FLG = 0 ");
		buffer.append("AND NVL(TO_CHAR(A.SUBTBL_FLAG), 'XX') != '1'");
		buffer.append("AND A.MO_STAT != 3 AND A.MO_STAT != 4 ");
		buffer.append("UNION ");
		buffer.append("SELECT TO_CHAR(B.DATE_BGN,'hh:mm') AS DATE_BGN2,");
		buffer.append("TO_CHAR(B.DATE_BGN,'yyyy-MM-dd') AS DATE_BGN1,");
		buffer.append("b.DATE_BGN as DATE_BGN,b.SORT_ID as sortId,");
		buffer.append("TO_CHAR(B.DATE_END,'hh:mm') AS DATE_END2,");
		buffer.append("TO_CHAR(B.DATE_END,'yyyy-MM-dd') AS DATE_END1,");
		buffer.append("B.DATE_END AS DATE_END,B.DECMPS_STATE AS DECMPSSTATE,");
		buffer.append("B.ITEM_NAME AS ITEM_NAME,B.COMB_NO AS COMBNO,");
		buffer.append("(SELECT D.EMPLOYEE_NAME FROM T_EMPLOYEE D WHERE D.EMPLOYEE_JOBNO = B.DOC_CODE) AS DOC_CODE,");
		buffer.append("(SELECT D.EMPLOYEE_NAME FROM T_EMPLOYEE D ");
		buffer.append("WHERE D.EMPLOYEE_JOBNO = B.CONFIRM_USERCD) AS CONFIRM_USERCD,");
		buffer.append("B.CONFIRM_DATE,B.DC_DOCNM,B.EXECUTE_DATE,");
		buffer.append("B.MAIN_DRUG AS MAINDRUG,");
		buffer.append("B.INPATIENT_NO AS INPATIENTNO,");
		buffer.append("(SELECT D.EMPLOYEE_NAME FROM T_EMPLOYEE D ");
		buffer.append("WHERE D.EMPLOYEE_JOBNO = B.EXECUTE_USERCD) AS EXECUTE_USERCD ");
		buffer.append("FROM HONRYHIS.T_INPATIENT_ORDER_NOW B ");
		if("1".equals(flag)){//如果为1位长期医嘱
			buffer.append("WHERE B.MO_STAT = 3) ");
		}else{//如果为零为临时医嘱
			buffer.append("WHERE B.MO_STAT = 3 AND B.DATE_END >=TO_DATE((SELECT SYSDATE-INTERVAL '7' DAY FROM DUAL), 'yyyy-mm-dd hh24:mi:ss')) ");
		}
		buffer.append("T WHERE T.MAINDRUG = 1 ");
		buffer.append("AND T.INPATIENTNO ='"+inpatientNo+"' ");
		if("1".equals(flag)){
			buffer.append("AND T.DECMPSSTATE = 1 ");
		}else{
			buffer.append("AND T.DECMPSSTATE = 0 ");
		}
		
		
		buffer.append("ORDER BY combNo, sortId");
		
		List<AdviceLong> listBean=super.getSession().createSQLQuery(buffer.toString()).addScalar("dateBgn2").addScalar("dateBgn1")
				.addScalar("dateBgn").addScalar("dateEnd2").addScalar("dateEnd1").addScalar("dateEnd").addScalar("itemName")
				.addScalar("docCode").addScalar("confirmUsercd").addScalar("confirmDate").addScalar("dcDocnm").addScalar("sortId")
				.addScalar("executeDate").addScalar("executeUsercd").addScalar("combNo").addScalar("decmpsState").addScalar("mainDrug")
				.addScalar("inpatientNo").setResultTransformer(Transformers.aliasToBean(AdviceLong.class)).list();
		for(AdviceLong vo:list){
			vo.setAdviceLongList(listBean);
		}
		if(list.size()>0){
			return list;
		}
		return new ArrayList<AdviceLong>();
	}

	@Override
	public List<AdviceLong> printAdvicehis(String inpatientNo, String flag) {
		StringBuffer buffer=new StringBuffer(380);
		buffer.append("SELECT T.INPATIENT_NO AS inpatientNo,T.PATIENT_NAME AS patientName,");
		buffer.append("DECODE(T.REPORT_SEX,1,'男',2,'女',3,'未知') AS reportSex,");
		buffer.append("TO_CHAR(T.REPORT_AGE||T.REPORT_AGEUNIT) AS reportAge,T.MEDICALRECORD_ID AS medicalrecordId,");
		buffer.append("T.DEPT_NAME AS deptName,T.BED_NAME AS bedName,");
		buffer.append("T.NURSE_CELL_NAME AS nurseCellCode ");
		buffer.append("FROM T_INPATIENT_INFO_NOW T WHERE T.STOP_FLG = 0 ");
		buffer.append("AND T.DEL_FLG=0 AND T.INPATIENT_NO='"+inpatientNo+"'");
		List<AdviceLong> list=super.getSession().createSQLQuery(buffer.toString()).addScalar("inpatientNo")
				.addScalar("patientName").addScalar("reportSex").addScalar("reportAge").addScalar("medicalrecordId")
				.addScalar("deptName").addScalar("bedName").addScalar("nurseCellCode").setResultTransformer(Transformers.aliasToBean(AdviceLong.class)).list();
		buffer=null;
		buffer=new StringBuffer(880);
		buffer.append("SELECT TO_CHAR(T.DATE_BGN, 'HH:MM') AS dateBgn2,");
		buffer.append("TO_CHAR(T.DATE_BGN, 'yyyy-MM-dd') AS dateBgn1,");
		buffer.append("TO_CHAR(T.DATE_BGN) AS dateBgn,TO_CHAR(T.DATE_END, 'hh:mm') AS dateEnd2,");
		buffer.append("TO_CHAR(T.DATE_END, 'yyyy-MM-dd') AS dateEnd1,");
		buffer.append("TO_CHAR(T.DATE_END) AS dateEnd,T.ITEM_NAME AS itemName,TO_CHAR(T.SORT_ID) AS sortId,");
		buffer.append("(SELECT D.EMPLOYEE_NAME FROM T_EMPLOYEE D WHERE D.EMPLOYEE_JOBNO = T.DOC_CODE) AS docCode,");
		buffer.append("(SELECT D.EMPLOYEE_NAME FROM T_EMPLOYEE D ");
		buffer.append("WHERE D.EMPLOYEE_JOBNO = T.CONFIRM_USERCD) AS confirmUsercd,");
		buffer.append("TO_CHAR(T.CONFIRM_DATE) AS confirmDate,T.DC_DOCNM AS dcDocnm,TO_CHAR(T.EXECUTE_DATE) AS executeDate,");
		buffer.append("(SELECT D.EMPLOYEE_NAME FROM T_EMPLOYEE D ");
		buffer.append("WHERE D.EMPLOYEE_JOBNO = T.EXECUTE_USERCD) AS executeUsercd ");
		buffer.append("FROM T_INPATIENT_ORDER_NOW T ");
		buffer.append("WHERE T.STOP_FLG = 0 AND T.DEL_FLG = 0 AND T.SUBTBL_FLAG <> 1 ");
		if("1".equals(flag)){
			buffer.append("AND T.DECMPS_STATE = 1 ");
		}else{
			buffer.append("AND T.DECMPS_STATE = 0 ");
		}
		buffer.append("AND T.INPATIENT_NO = '"+inpatientNo+"' ORDER BY T.SORT_ID, T.MO_ORDER DESC");
		
		List<AdviceLong> listBean=super.getSession().createSQLQuery(buffer.toString()).addScalar("dateBgn2").addScalar("dateBgn1")
				.addScalar("dateBgn").addScalar("dateEnd2").addScalar("dateEnd1").addScalar("dateEnd").addScalar("itemName")
				.addScalar("docCode").addScalar("confirmUsercd").addScalar("confirmDate").addScalar("dcDocnm").addScalar("sortId")
				.addScalar("executeDate").addScalar("executeUsercd")
				.setResultTransformer(Transformers.aliasToBean(AdviceLong.class)).list();
		for(AdviceLong vo:list){
			vo.setAdviceLongList(listBean);
		}
		if(list.size()>0){
			return list;
		}
		return new ArrayList<AdviceLong>();
	}
}
