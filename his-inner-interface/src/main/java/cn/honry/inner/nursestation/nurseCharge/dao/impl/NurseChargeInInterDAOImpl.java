package cn.honry.inner.nursestation.nurseCharge.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.DrugStockinfo;
import cn.honry.base.bean.model.DrugUndrug;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientItemList;
import cn.honry.base.bean.model.InpatientMedicineList;
import cn.honry.base.bean.model.MatUndrugCompare;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.nursestation.nurseCharge.dao.NurseChargeInInterDAO;
import cn.honry.inner.nursestation.nurseCharge.vo.NurseChargeVo;
import cn.honry.utils.HisParameters;
/**
 * @author 护士站收费DAOImpl
 *
 */
@Repository("nurseChargeInInterDAO")
@SuppressWarnings({"all"})
public class NurseChargeInInterDAOImpl extends HibernateEntityDao<DrugUndrug> implements NurseChargeInInterDAO{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}


	/**  
	 *  
	 * @Description：渲染开方医生（根据床号）
	 * @Author：zhangjin
	 * @CreateDate：2016-1-6 
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public NurseChargeVo querNurseDoctor(String id) {
		String hql="select EMPLOYEE_ID as id,a.EMPLOYEE_NAME as name from "+HisParameters.HISPARSCHEMAHISUSER+"T_EMPLOYEE "
				+ "a where a.stop_flg=0 and a.del_flg=0 and a.EMPLOYEE_ID='"+id+"'";
		
		SQLQuery queryObject = this.getSession().createSQLQuery(hql).addScalar("id").addScalar("name");
		List<NurseChargeVo> iList = queryObject.setResultTransformer(Transformers.aliasToBean(NurseChargeVo.class)).list();
		if(iList!=null&&iList.size()>0){
			return iList.get(0);
		}
		return new NurseChargeVo();
	}

	/**  
	 *  
	 * @Description：最小费用与统计大类信息
	 * @Author：zhangjin
	 * @CreateDate：2016-1-9 
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<NurseChargeVo> treeNurseAdvice(String soushu) {
		/*, null as minfeeCode ,null as feename*/
		String hql="select distinct tt.fee_stat_code as feeStatCode,tt.fee_stat_name as feeStatName "+
					"from "+HisParameters.HISPARSCHEMAHISUSER+"T_CHARGE_MINFEETOSTAT tt "+
					"where tt.report_code = 'ZY01' "+
					       "and tt.stop_flg = 0 "+
					       "and tt.del_flg = 0 ";
					       
		if(StringUtils.isNotBlank(soushu)){
			hql+=" and tt.fee_stat_code in ('04', '05', '07', '15','10') "+
				       "and tt.FEE_STAT_CODE not in ('01', '02', '03') ";
		}else{
			hql+=" and tt.fee_stat_code in ('04', '05', '07', '15') "+
				       "and tt.FEE_STAT_CODE not in ('01', '02', '03') ";
		}
		SQLQuery queryObject = this.getSession().createSQLQuery(hql).addScalar("feeStatCode").addScalar("feeStatName");
		List<NurseChargeVo> iList = queryObject.setResultTransformer(Transformers.aliasToBean(NurseChargeVo.class)).list();
		if(iList!=null&&iList.size()>0){
			return iList; 
		}
		
	 return new ArrayList<NurseChargeVo>();
	}
	/**  
	 *  
	 * @Description：最小费用与统计大类信息(子节点)
	 * @Author：zhangjin
	 * @CreateDate：2016-1-9 
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<NurseChargeVo> treeNurseAdvicep(String feeStatCode,String soushu) {
		/*t.fee_stat_code as feeStatCode,t.fee_stat_name as feeStatName,*/
		String hql="select t.MINFEE_CODE as minfeeCode,d.code_name as feename "+
			      "from "+HisParameters.HISPARSCHEMAHISUSER+"T_CHARGE_MINFEETOSTAT t "+
			"LEFT JOIN "+HisParameters.HISPARSCHEMAHISUSER+"T_BUSINESS_DICTIONARY d on t.minfee_code= d.code_encode  "+
			"where t.report_code = 'ZY01' and d.code_type = 'drugMinimumcost' "+
			       "and t.stop_flg = 0 "+
			       "and t.del_flg = 0 ";
		if(StringUtils.isNotBlank(soushu)){
			hql+=" and t.fee_stat_code in ('04', '05', '07', '15','10') "+
				       "and t.FEE_STAT_CODE not in ('01', '02', '03') ";
		}else{
			hql+=" and t.fee_stat_code in ('04', '05', '07', '15') "+
				       "and t.FEE_STAT_CODE not in ('01', '02', '03') ";
		}
		hql+=" and t.fee_stat_code = '"+feeStatCode+"'";
		SQLQuery queryObject = this.getSession().createSQLQuery(hql).addScalar("minfeeCode").addScalar("feename");
		List<NurseChargeVo> iList = queryObject.setResultTransformer(Transformers.aliasToBean(NurseChargeVo.class)).list();
		if(iList!=null&&iList.size()>0){
			return iList; 
		}
		
	 return new ArrayList<NurseChargeVo>();
	}

	/**  
	 *  
	 * @Description：组套信息
	 * @Author：zhangjin
	 * @CreateDate：2016-1-11 
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<NurseChargeVo> treeStackNurseAdvicep(String deptId) {
		String hql="select t.stack_id as stackId,t.stack_name as stackName from "+HisParameters.HISPARSCHEMAHISUSER+"T_BUSINESS_STACK t where t.stop_flg=0 and t.del_flg=0 and t.stack_source=2 and t.stack_object=1 and t.stack_deptid='"+deptId+"'";
		SQLQuery queryObject = this.getSession().createSQLQuery(hql).addScalar("stackId").addScalar("stackName");
		List<NurseChargeVo> iList = queryObject.setResultTransformer(Transformers.aliasToBean(NurseChargeVo.class)).list();
		if(iList!=null&&iList.size()>0){
			return iList; 
	}
		return new ArrayList<NurseChargeVo>();
	}

	/**  
	 *  
	 * @Description：组套信息（子节点)
	 * @Author：zhangjin
	 * @CreateDate：2016-1-11 
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<NurseChargeVo> treeStackSonNurseAdvice(String deptId) {
		String hql="select d.nid,d.name,c.stack_id as stackId from "+HisParameters.HISPARSCHEMAHISUSER+"t_business_stackinfo c,"
				+ " (select a.drug_id as nid, a.drug_name as name from "+HisParameters.HISPARSCHEMAHISUSER+"t_drug_info a union all select b.undrug_id id,"
				+ " b.undrug_name name from t_drug_undrug b) d where c.stop_flg=0 and c.del_flg=0 and c.stack_id in (select a.stack_id from "+HisParameters.HISPARSCHEMAHISUSER+"t_business_stackinfo a "
				+ "where a.stop_flg=0 and a.del_flg=0 and a.stack_id in (select t.stack_id from "+HisParameters.HISPARSCHEMAHISUSER+"t_business_stack t where t.stop_flg=0 and t.del_flg=0 and t.stack_deptid = '"+deptId+"')) and d.nid = c.stackinfo_itemid";
		SQLQuery queryObject = this.getSession().createSQLQuery(hql).addScalar("nid").addScalar("name").addScalar("stackId");
		List<NurseChargeVo> iList = queryObject.setResultTransformer(Transformers.aliasToBean(NurseChargeVo.class)).list();
		if(iList!=null&&iList.size()>0){
			return iList; 
	}
		return new ArrayList<NurseChargeVo>();
	}

	/**  
	 *  
	 * @Description：回显最小费用与统计大类信息
	 * @Author：zhangjin
	 * @CreateDate：2016-1-9 
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<NurseChargeVo> chargeMinfeetostat(String naId) {
		String hql="select b.undrug_code as undrugId,b.undrug_name as undrugName,b.UNDRUG_DEFAULTPRICE as money,b.UNDRUG_UNIT as unit,"
				+ "b.UNDRUG_DEPT as dept,to_char(1) as category,b.UNDRUG_CHILDRENPRICE as undrugChildrenprice,b.UNDRUG_SPECIALPRICE as undrugSpecialprice,"
				+ "b.UNDRUG_MINIMUMCOST as undrugMinimumcost,b.UNDRUG_SPEC as spec,to_char(2) as ty,b.UNDRUG_CODE as itemCode"
				+ " from "+HisParameters.HISPARSCHEMAHISUSER+"T_DRUG_UNDRUG b where b.stop_flg=0 and b.del_flg=0 and b.UNDRUG_MINIMUMCOST='"+naId+"'";
		SQLQuery queryObject = this.getSession().createSQLQuery(hql).addScalar("undrugId").addScalar("undrugName")
				.addScalar("money",Hibernate.DOUBLE).addScalar("unit").addScalar("dept").addScalar("category",Hibernate.STRING)
				.addScalar("undrugChildrenprice",Hibernate.DOUBLE).addScalar("undrugSpecialprice",Hibernate.DOUBLE)
				.addScalar("undrugMinimumcost").addScalar("spec").addScalar("ty").addScalar("itemCode");
		List<NurseChargeVo> iList = queryObject.setResultTransformer(Transformers.aliasToBean(NurseChargeVo.class)).list();
		if(iList!=null&&iList.size()>0){
			return iList; 
	}
		return new ArrayList<NurseChargeVo>();
	}

	/**  
	 *  
	 * @Description：回显组套信息
	 * @Author：zhangjin
	 * @CreateDate：2016-1-9 
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<NurseChargeVo> businessStack(String naId,String deptCode) {
		String hql="select a.drug_id as undrugId,a.drug_name as undrugName,a.DRUG_RETAILPRICE as money,a.DRUG_PACKAGINGUNIT as unit,"
				+ "(select GETDRUG_DEPT_ID from "+HisParameters.HISPARSCHEMAHISUSER+"T_BUSINESS_STORAGE_SETDEPT "
						+ "where stop_flg=0 and del_flg=0 and PUTDRUG_DEPT_ID='"+deptCode+"') as dept,"
						+ "(select CODE_NAME from "+HisParameters.HISPARSCHEMAHISUSER+"T_BUSINESS_DICTIONARY "
								+ "where stop_flg=0 and del_flg=0 and code_encode=a.DRUG_SYSTYPE and code_type='systemType') as category,"
						+ "null as undrugChildrenprice,null as undrugSpecialprice,"
						+ "a.DRUG_MINIMUMCOST as undrugMinimumcost,a.DRUG_SPEC as spec,a.DRUG_SYSTYPE as drugSystype,"
						+ "a.DRUG_NATURE as drugNature,a.DRUG_DOSAGEFORM as drugDosageform,a.DRUG_GRADE as drugGrade,"
						+ "a.DRUG_MINIMUMUNIT as minunit,a.DRUG_BASICDOSE as drugBasicdose,a.DRUG_DOSEUNIT as drugDoseunit,to_char(1) as ty,a.DRUG_ID as itemCode"
						+ " from "+HisParameters.HISPARSCHEMAHISUSER+"T_DRUG_INFO a where a.stop_flg=0 and a.del_flg=0 and a.drug_id='"+naId+"'"
				+ " union all "
				+ "select b.undrug_id as undrugId,b.undrug_name as undrugName,b.UNDRUG_DEFAULTPRICE as money,b.UNDRUG_UNIT as unit,"
				+ "b.UNDRUG_DEPT as dept,to_char(1) as category,b.UNDRUG_CHILDRENPRICE as undrugChildrenprice,b.UNDRUG_SPECIALPRICE as undrugSpecialprice,"
				+ "b.UNDRUG_MINIMUMCOST as undrugMinimumcost,b.UNDRUG_SPEC as spec,null as DRUG_SYSTYPE,"
				+ "null as drugNature,null as drugDosageform,null as drugGrade,"
				+ "null as minunit,0 as drugBasicdose,null as drugDoseunit,to_char(2) as ty,b.UNDRUG_ID as itemCode"
				+ " from "+HisParameters.HISPARSCHEMAHISUSER+"T_DRUG_UNDRUG b where b.stop_flg=0 and b.del_flg=0 and b.undrug_id='"+naId+"'";
		SQLQuery queryObject = this.getSession().createSQLQuery(hql).addScalar("undrugId").addScalar("undrugName").addScalar("money",Hibernate.DOUBLE).addScalar("unit").addScalar("dept")
				.addScalar("category",Hibernate.STRING). addScalar("undrugChildrenprice",Hibernate.DOUBLE).addScalar("undrugSpecialprice",Hibernate.DOUBLE)
				.addScalar("undrugMinimumcost").addScalar("spec").addScalar("drugSystype").addScalar("drugNature").addScalar("drugDosageform")
				.addScalar("drugGrade").addScalar("minunit").addScalar("drugBasicdose",Hibernate.DOUBLE).addScalar("drugDoseunit").addScalar("ty").addScalar("itemCode");
		List<NurseChargeVo> iList = queryObject.setResultTransformer(Transformers.aliasToBean(NurseChargeVo.class)).list();
		if(iList!=null&&iList.size()>0){
			return iList; 
	}
		return new ArrayList<NurseChargeVo>();
	}

	/**  
	 *  
	 * @Description：最小费用与统计大类信息下(3级节点的非药品信息)
	 * @Author：zhangjin
	 * @CreateDate：2016-1-12 
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<NurseChargeVo> treeNurseAdviceUndrug(String naId) {
		String hql ="select a.UNDRUG_ID as nid,a.UNDRUG_NAME as name from "+HisParameters.HISPARSCHEMAHISUSER+"T_DRUG_UNDRUG a where a.stop_flg=0 and a.del_flg=0 and a.UNDRUG_MINIMUMCOST='"+naId+"'";
		SQLQuery queryObject= this.getSession().createSQLQuery(hql).addScalar("nid").addScalar("name");
		List<NurseChargeVo> list=queryObject.setResultTransformer(Transformers.aliasToBean(NurseChargeVo.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<NurseChargeVo>();
	}

	/**  
	 *  
	 * @Description：项目名称（下拉框（datagrid））总数
	 * @Author：zhangjin
	 * @CreateDate：2016-1-13 
	 * @Modifier：liujl
	 * @ModifyDate： 2016-6-3 下午7:10
	 * @ModifyRmk：   修改获取总页数的方法
	 * @version 1.0
	 *
	 */
	@Override
	public int getTotal(String q) {//总数
		String hql=" from DrugUndrug b where b.stop_flg=0 and b.del_flg=0 ";
		     
		if(!"".equals(q)&&q!=null){
			hql+=" and b.undrugPinyin like '%"+q+"%' or b.name like '%"+q+"%'";
		}
//		super.find(hql, null);
		return super.getTotal(hql);
	}
	
	/**  
	 *  
	 * @Description：项目名称（下拉框（datagrid））sql
	 * @Author：zhangjin
	 * @CreateDate：2016-1-13 
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	public String getjoin(String q){
		String hql="select b.undrug_id as nid,b.undrug_name as name,b.UNDRUG_DEFAULTPRICE as money,b.UNDRUG_UNIT as unit,"
				+ "b.UNDRUG_DEPT as dept,to_char(1) as category,b.UNDRUG_CHILDRENPRICE as undrugChildrenprice,b.UNDRUG_SPECIALPRICE as undrugSpecialprice,"
				+ "b.UNDRUG_MINIMUMCOST as undrugMinimumcost,b.UNDRUG_SPEC as spec,null as drugSystype,"
				+ "null as drugNature,null as drugDosageform,null as drugGrade,"
				+ "null as minunit,0 as drugBasicdose,null as drugDoseunit,to_char(2) as ty,b.UNDRUG_ID as itemCode "
				+ "from "+HisParameters.HISPARSCHEMAHISUSER+"T_DRUG_UNDRUG b where b.stop_flg=0 and b.del_flg=0 ";
		     
		if(!"".equals(q)&&q!=null){
			hql+=" and b.UNDRUG_PINYIN like '%"+q+"%' or b.UNDRUG_NAME like '%"+q+"%'";
		}
		return hql;
	}
	/**  
	 *  
	 * @Description：项目名称（下拉框（datagrid））分页
	 * @Author：zhangjin
	 * @CreateDate：2016-1-13 
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<NurseChargeVo> getPageSql(String page, String rows,String q) {//分页
		String hql=getjoin(q);
		SQLQuery queryObject = this.getSession().createSQLQuery(hql)
				.addScalar("nid").addScalar("name").addScalar("money",Hibernate.DOUBLE).addScalar("unit").addScalar("dept")
				.addScalar("category",Hibernate.STRING).addScalar("undrugChildrenprice",Hibernate.DOUBLE).addScalar("undrugSpecialprice",Hibernate.DOUBLE)
				.addScalar("undrugMinimumcost").addScalar("spec").addScalar("drugSystype").addScalar("drugNature").addScalar("drugDosageform")
				.addScalar("drugGrade").addScalar("minunit").addScalar("drugBasicdose",Hibernate.DOUBLE).addScalar("drugDoseunit").addScalar("ty").addScalar("itemCode");
		int start = Integer.parseInt(page==null?"1":page);
		int count = Integer.parseInt(rows==null?"20":rows);
		List<NurseChargeVo> list = queryObject.setFirstResult((start - 1) * count).setMaxResults(count).setResultTransformer(Transformers.aliasToBean(NurseChargeVo.class)).list();
		if(list!=null&&list.size()>0){
			
			return list; 
		}
		return new ArrayList<NurseChargeVo>();
	}

	/**  
	 *  
	 * @Description：渲染单位
	 * @Author：zhangjin
	 * @CreateDate：2016-1-13
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<NurseChargeVo> nurseChargeMoney() {
		String hql="select t.CODE_encode as id, t.code_name as name from "+HisParameters.HISPARSCHEMAHISUSER+"t_business_dictionary t where t.stop_flg=0 and t.del_flg=0 and t.code_type='doseUnit' ";
		SQLQuery queryObject = this.getSession().createSQLQuery(hql).addScalar("id").addScalar("name");
		List<NurseChargeVo> list=queryObject.setResultTransformer(Transformers.aliasToBean(NurseChargeVo.class)).list();
		if(list != null && list.size() > 0){
			return list;
		}
		return new ArrayList<NurseChargeVo>();
	}

	/**  
	 *  
	 * @Description：渲染合同
	 * @Author：zhangjin
	 * @CreateDate：2016-1-13
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<NurseChargeVo> nurseChargePactCode() {
	String hql="select UNIT_ID as id,UNIT_NAME as name from "+HisParameters.HISPARSCHEMAHISUSER+"T_BUSINESS_CONTRACTUNIT where stop_flg=0 and del_flg=0";
	SQLQuery queryObject = this.getSession().createSQLQuery(hql).addScalar("id").addScalar("name");
	List<NurseChargeVo> list=queryObject.setResultTransformer(Transformers.aliasToBean(NurseChargeVo.class)).list();
	if(list != null && list.size() > 0){
		return list;
	}
	return new ArrayList<NurseChargeVo>();
	}

	/**  
	 *  
	 * @Description：加载患者划价未收费信息
	 * @Author：zhangjin
	 * @CreateDate：2016-1-18
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<NurseChargeVo> InpatientMessage(String no) {
		String hql="select a.id as id,a.inpatient_no as medicalrecordId,a.name as patientName,"
				+ "a.execute_deptcode as dept,a.item_name as undrugName,a.unit_price as money,a.qty as amount,null as unumber,"
				+ "a.tot_cost as moneyMount,a.package_code as stackName,to_char(1) as zsd,null as drugType,to_char(1) as category,"
				+ "a.OWN_COST as enseCost,a.PAY_COST as selfCost,a.PUB_COST as pubCost,a.ECO_COST as privilegeCost,a.PAYKIND_CODE as paykindCode,"
				+ "0 as freeCost,a.SEND_DATE as chargeOrder,to_char(2) as ty,a.ITEM_CODE as itemCode,a.RECIPE_NO as recipeNo,a.SEQUENCE_NO as sequenceNo,to_char(1) as huajia"
				+ " from "+HisParameters.HISPARSCHEMAHISUSER+"T_INPATIENT_ITEMLIST a where a.stop_flg=0 and a.del_flg=0 and a.SEND_FLAG=0 and a.BALANCE_STATE=0 and a.inpatient_no ='"+no+"'"
				+ " union all "
				+ "select b.id as id,b.inpatient_no as medicalrecordId,b.name as patientName,"
				+ "b.execute_deptcode as dept, b.drug_name as undrugName,b.unit_price as money,b.qty as amount,b.days as unumber,b.tot_cost as moneyMount,"
				+ " null as stackName, to_char(0) as zsd,b.DRUG_TYPE as drugType,to_char(1) as category,"
				+ "b.OWN_COST as enseCost,b.PAY_COST as selfCost,b.PUB_COST as pubCost,b.ECO_COST as privilegeCost,b.PAYKIND_CODE as paykindCode,0 as freeCost,"
				+ "b.EXEC_DATE as chargeOrder,to_char(1) as ty,b.DRUG_CODE as itemCode,b.RECIPE_NO as recipeNo,b.SEQUENCE_NO as sequenceNo,to_char(1) as huajia "
				+ " from "+HisParameters.HISPARSCHEMAHISUSER+"T_INPATIENT_MEDICINELIST b where b.stop_flg=0 and"
				+ " b.del_flg=0 and b.BALANCE_STATE=0 and b.SENDDRUG_FLAG=0 and b.inpatient_no='"+no+"'";
		SQLQuery queryObject = this.getSession().createSQLQuery(hql).addScalar("id").addScalar("medicalrecordId").addScalar("patientName")
				.addScalar("dept").addScalar("undrugName").addScalar("money",Hibernate.DOUBLE).addScalar("amount",Hibernate.DOUBLE).addScalar("unumber",Hibernate.INTEGER)
				.addScalar("moneyMount",Hibernate.DOUBLE)
				.addScalar("stackName").addScalar("zsd",Hibernate.STRING).addScalar("drugType").addScalar("category",Hibernate.STRING)
				.addScalar("enseCost",Hibernate.DOUBLE).addScalar("selfCost",Hibernate.DOUBLE).addScalar("pubCost",Hibernate.DOUBLE)
				.addScalar("privilegeCost",Hibernate.DOUBLE).addScalar("paykindCode",Hibernate.STRING).addScalar("freeCost",Hibernate.DOUBLE)
				.addScalar("chargeOrder",Hibernate.DATE).addScalar("ty").addScalar("itemCode").addScalar("recipeNo").addScalar("sequenceNo",Hibernate.INTEGER).addScalar("huajia");
		List<NurseChargeVo> iList = queryObject.setResultTransformer(Transformers.aliasToBean(NurseChargeVo.class)).list();
		if(iList!=null&&iList.size()>0){
			return iList; 
	}
		return new ArrayList<NurseChargeVo>();
	}

	/**  
	 *  
	 * @Description：获取价格形式
	 * @Author：zhangjin
	 * @CreateDate：2016-1-19
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<NurseChargeVo> querNursePubRati(String id) {
	      String hql="select PRICE_FORM as priceForm,PUB_RATIO as pubRati from "+HisParameters.HISPARSCHEMAHISUSER+"T_BUSINESS_CONTRACTUNIT "
	      		+ "where stop_flg=0 and del_flg=0 and UNIT_ID='"+id+"'";
	      SQLQuery queryObject=this.getSession().createSQLQuery(hql).addScalar("priceForm",Hibernate.INTEGER).addScalar("pubRati",Hibernate.DOUBLE);
	      List<NurseChargeVo> list=queryObject.setResultTransformer(Transformers.aliasToBean(NurseChargeVo.class)).list();
	      if(list!=null&&list.size()>0){
	    	  return list;
	      }
		return new ArrayList<NurseChargeVo>();
	}

	/**  
	 *  
	 * @Description：加载婴儿是否绑定母亲
	 * @Author：zhangjin
	 * @CreateDate：2016-1-20
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<NurseChargeVo> hospitalParameter() {
		String hql ="select PARAMETER_VALUE as yefyis from "+HisParameters.HISPARSCHEMAHISUSER+""
				+ "T_HOSPITAL_PARAMETER where PARAMETER_ID='402880ae52578914015258be5ec20007'";
		SQLQuery queryObject=this.getSession().createSQLQuery(hql).addScalar("yefyis",Hibernate.INTEGER);
			List<NurseChargeVo> list=queryObject.setResultTransformer(Transformers.aliasToBean(NurseChargeVo.class)).list();
			if(list!=null&&list.size()>0){
				return list;
			}
		return new ArrayList<NurseChargeVo>();
	}

	/**  
	 *  
	 * @Description：加载住院金额
	 * @Author：zhangjin
	 * @CreateDate：2016-1-20
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<NurseChargeVo> nurseInpatBalance(String id) {
		String hql="select INPATIENT_BALANCE as inpatBalance from "+HisParameters.HISPARSCHEMAHISUSER+"T_PATIENT_ACCOUNT "
				+ "where stop_flg=0 and del_flg=0 and MEDICALRECORD_ID='"+id+"'";
	    SQLQuery queryObject=this.getSession().createSQLQuery(hql).addScalar("inpatBalance",Hibernate.DOUBLE);
	    List<NurseChargeVo> list=queryObject.setResultTransformer(Transformers.aliasToBean(NurseChargeVo.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
	    return new ArrayList<NurseChargeVo>();
	}

	

	/**  
	 *  
	 * @Description：获取取药科室
	 * @Author：zhangjin
	 * @CreateDate：2016-1-22
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public NurseChargeVo getInfoID(String deptCode) {
		String hql ="select GETDRUG_DEPT_ID as getdrugDept from "+HisParameters.HISPARSCHEMAHISUSER+"T_BUSINESS_STORAGE_SETDEPT "
				+ "where PUTDRUG_DEPT_ID='"+deptCode+"'";
		SQLQuery query=this.getSession().createSQLQuery(hql).addScalar("getdrugDept");
		List<NurseChargeVo> list=query.setResultTransformer(Transformers.aliasToBean(NurseChargeVo.class)).list();
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return new NurseChargeVo();
	}

	/**  
	 *  
	 * @Description：查询本病区下的医生
	 * @Author：zhangjin
	 * @CreateDate：2016-3-25
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<SysEmployee> queryEmplCode() {
		String hql="select a.EMPLOYEE_ID as id,a.EMPLOYEE_NAME as name,a.EMPLOYEE_PINYIN as pinyin,a.EMPLOYEE_WB as wb,"
				+ "a.EMPLOYEE_INPUTCODE as inputCode from "+HisParameters.HISPARSCHEMAHISUSER+"T_EMPLOYEE a "
				+ "left join "+HisParameters.HISPARSCHEMAHISUSER+"t_business_dictionary f on a.employee_title=f.code_encode  where "
				+ " a.del_flg=0 and a.stop_flg=0 and f.del_flg=0 and f.stop_flg=0 and f.code_type='level' "
						+ "and f.code_name like '%医师%' or f.code_name='主任护师'";
		SQLQuery query=this.getSession().createSQLQuery(hql).addScalar("id").addScalar("name").addScalar("pinyin")
				.addScalar("wb").addScalar("inputCode");
		List<SysEmployee> list=query.setResultTransformer(Transformers.aliasToBean(SysEmployee.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<SysEmployee>();
	}

	/**  
	 *  
	 * @Description：查询当日的收费信息
	 * @Author：zhangjin
	 * @CreateDate：2016-1-22
	 * @Modifier：zhangjin
	 * @ModifyDate：2016-3-22 
	 *  @Modifier：zhangjin
	 * @ModifyDate：2016-3-25  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<NurseChargeVo> dayCharge(String inpatientNo) {
		String date=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String hql="select distinct a.id as id,a.inpatient_no as medicalrecordId,a.name as patientName,"
				+ "a.execute_deptcode as dept,a.item_name as undrugName,a.unit_price as money,a.qty as amount,null as unumber,"
				+ "a.tot_cost as moneyMount,a.package_code as stackName,to_char(1) as zsd,null as drugType,to_char(1) as category,"
				+ "a.OWN_COST as enseCost,a.PAY_COST as selfCost,a.PUB_COST as pubCost,a.ECO_COST as privilegeCost,a.PAYKIND_CODE as paykindCode,"
				+ "a.CURRENT_UNIT as unit,a.FEE_OPERCODE as feeOpercode,a.FEE_DATE as feeDate "
				+ " from "+HisParameters.HISPARSCHEMAHISUSER+"T_INPATIENT_ITEMLIST a where a.stop_flg=0 and a.del_flg=0 and a.inpatient_no in('"+inpatientNo+"') "
						+ "and to_char(a.FEE_DATE,'yyyy-MM-dd')='"+date+"' and a.SEND_FLAG=1"
				+ " union all "
				+ "select b.id as id,b.inpatient_no as medicalrecordId,b.name as patientName,"
				+ "b.execute_deptcode as dept, b.drug_name as undrugName,b.unit_price as money,b.qty as amount,b.days as unumber,b.tot_cost as moneyMount,"
				+ " null as stackName, to_char(0) as zsd,b.DRUG_TYPE as drugType,to_char(1) as category,"
				+ "b.OWN_COST as enseCost,b.PAY_COST as selfCost,b.PUB_COST as pubCost,b.ECO_COST as privilegeCost,b.PAYKIND_CODE as paykindCode,b.CURRENT_UNIT as unit,"
				+ "b.FEE_OPERCODE as feeOpercode,b.FEE_DATE as feeDate "
				+ " from "+HisParameters.HISPARSCHEMAHISUSER+"T_INPATIENT_MEDICINELIST b where b.stop_flg=0 and"
				+ " b.del_flg=0   and b.inpatient_no ='"+inpatientNo+"' "
				+ "and to_char(b.FEE_DATE,'yyyy-MM-dd')='"+date+"'and b.SENDDRUG_FLAG=1";
		SQLQuery queryObject = this.getSession().createSQLQuery(hql).addScalar("id").addScalar("medicalrecordId").addScalar("patientName")
				.addScalar("dept").addScalar("undrugName").addScalar("money",Hibernate.DOUBLE).addScalar("amount",Hibernate.DOUBLE).addScalar("unumber",Hibernate.INTEGER)
				.addScalar("moneyMount",Hibernate.DOUBLE)
				.addScalar("stackName").addScalar("zsd",Hibernate.STRING).addScalar("drugType").addScalar("category",Hibernate.STRING)
				.addScalar("enseCost",Hibernate.DOUBLE).addScalar("selfCost",Hibernate.DOUBLE).addScalar("pubCost",Hibernate.DOUBLE)
				.addScalar("privilegeCost",Hibernate.DOUBLE).addScalar("paykindCode",Hibernate.STRING).addScalar("unit").addScalar("feeOpercode").addScalar("feeDate",Hibernate.DATE);
		List<NurseChargeVo> iList = queryObject.setResultTransformer(Transformers.aliasToBean(NurseChargeVo.class)).list();
		if(iList!=null&&iList.size()>0){
			return iList;
		}
		return new ArrayList<NurseChargeVo>();
	}
	
	/**  
	 * @Description： 根据病历号或住院号查询信息
	 * @Author：donghe
	 * @CreateDate：2016-3-21
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<InpatientInfo> queryInpatientInfoList(String inpatientNo) {
		String hql=" from InpatientInfo where del_flg=0 and stop_flg=0 and inpatientNo like '%"+inpatientNo+"' or medicalrecordId like '%"+inpatientNo+"'";
		List<InpatientInfo> inlist=super.find(hql, null);
		if(inlist.size()>0&&inlist!=null){
			return inlist;
		}
		return new ArrayList<InpatientInfo>();
	}

	/**  
	 *  
	 * @Description：渲染科室
	 * @Author：zhangjin
	 * @CreateDate：2016-3-30
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<SysDepartment> querydeptComboboxs() {
		String hql="from SysDepartment where del_flg=0 and stop_flg=0 ";
		List<SysDepartment> list=super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<SysDepartment>();
	}

	/**
	 * 查询非药品项目名称
	 * @author   zhangjin
	 * @createDate： 
	 * @modifyDate：2016-4-13
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<DrugUndrug> gainUndrug(String id) {
		String hql="from DrugUndrug where del_flg=0 and stop_flg=0 and id='"+id+"'";
		List<DrugUndrug> list=super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<DrugUndrug>();
	}

	/**
	 * 查询计费人
	 * @author   zhangjin
	 * @createDate： 
	 * @modifyDate：2016-4-13
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<User> nurseEmply() {
		String hql="from User where del_flg=0 and stop_flg=0 ";
		List<User> list=super.find(hql,null);
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<User>();
	}

	/**
	 * 查询是否绑定物资
	 * @author   zhangjin
	 * @createDate： 
	 * @modifyDate：2016-4-13
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public MatUndrugCompare queryWz(String nid) {
		String hql="from MatUndrugCompare where del_flg=0 and stop_flg=0 and undrugItemCode='"+nid+"'";
		List<MatUndrugCompare> list=super.find(hql,null);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return new MatUndrugCompare();
	}

	/**
	 * 处方内流水号
	 * @author   zhangjin
	 * @createDate： 
	 * @modifyDate：2016-4-13
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public InpatientItemList getSequenceNo(String nid, String recipeNo) {
		String hql=" from InpatientItemList where stop_flg=0 and del_flg=0 ";
		if(StringUtils.isNotBlank(recipeNo)){
			hql+= "and recipeNo='"+recipeNo+"'";
		}
		if(StringUtils.isNotBlank(nid)){
			hql+=" and itemCode='"+nid+"'";
		}
		List<InpatientItemList> list=super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return new InpatientItemList();
	}

	/**
	 * 获取住院科室对应药房的id和库存数量
	 * @author   zhangjin
	 * @createDate： 
	 * @modifyDate：2016-5-18
	 * @param：   nid(项目ID)
	 * @param2：   deptCode(住院科室)
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public DrugStockinfo getkydept(String deptCode,String nid) {
		String hql="select b.STORAGE_DEPTID as storageDeptid, b.STORE_SUM as storeSum,"
				+ "b.SHOW_UNIT as showUnit from T_DRUG_STOCKINFO b"
				+ " where b.DRUG_ID='"+nid+"'and b.STORAGE_DEPTID in"
				+ "(select t1.DEPT_ID  from t_department_contact t1 where t1.id in "
				+ "(select t.pardept_id from t_department_contact t where  t.dept_id='"+deptCode+"' and "
						+ "t.reference_type='01' and t.stop_flg=0 and t.del_flg=0)"
						+ " and t1.stop_flg=0 and t1.del_flg=0) and b.stop_flg=0 and b.del_flg=0";
		SQLQuery query=this.getSession().createSQLQuery(hql).addScalar("storageDeptid")
				.addScalar("storeSum",Hibernate.DOUBLE).addScalar("showUnit");
		List<DrugStockinfo> list=query.setResultTransformer(Transformers.aliasToBean(DrugStockinfo.class)).list();
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}

	/**
	 * 对成功收费的药品进行查询（扣库存）
	 * @author   zhangjin
	 * @createDate： 
	 * @modifyDate：2016-6-3
	 * @param：   nid(项目ID)
	 * @param2：   deptCode(住院科室)
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public InpatientMedicineList getDrugSequenceNo(String nid, String string) {
		String hql= " from InpatientMedicineList where del_flg=0 and stop_flg=0 "
				+ "and recipeNo='"+string+"' and drugCode='"+nid+"'";
		List<InpatientMedicineList> list=super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}

	
}
