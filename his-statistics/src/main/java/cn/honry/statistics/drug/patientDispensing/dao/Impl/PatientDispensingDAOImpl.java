package cn.honry.statistics.drug.patientDispensing.dao.Impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.DrugApplyout;
import cn.honry.base.bean.model.DrugBillclass;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.system.utli.DataRightUtils;
import cn.honry.statistics.drug.patientDispensing.dao.PatientDispensingDAO;
import cn.honry.statistics.drug.patientDispensing.vo.VinpatientApplyout;

@Repository("patientDispensingDAO")
@SuppressWarnings({ "all" })
public class PatientDispensingDAOImpl extends HibernateEntityDao<DrugApplyout> implements PatientDispensingDAO{
	
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Override
	public List<InpatientInfoNow> queryPatient(String deptId,String type) {
		String hql="from InpatientInfoNow i where  (i.inState='R' or i.inState='I') and "
				+ " i.inpatientNo in(select r.inpatientNo from InpatientOrderNow r where r.stop_flg=0 and r.del_flg=0) ";
		if(StringUtils.isNotBlank(deptId)){
			if("N".equals(type)){
				//hql+=" and i.bedId in (select b.id from InpatientBedinfoNow b where b.nurseCellCode='"+deptId+"')";
				hql+=" and i.nurseCellCode ='"+deptId+"'";
			}
			else{
				hql+=" and i.deptCode in (select tdc.deptId "
						+ "from DepartmentContact tdc "
						+ "where tdc.id in (select dc.pardeptId "
						+ "from DepartmentContact dc "
						+ "where dc.deptId ='"+deptId+"'"
						+ "and dc.referenceType = '03'))";
			}
		}
		List<InpatientInfoNow> iList = super.find(hql, null);
		if(iList!=null&&iList.size()>0){
			return iList;
		}
		return new ArrayList<InpatientInfoNow>();
	}
	@Override
	public SysDepartment queryState(String deptId) {
		String hql="from SysDepartment d where d.deptCode='"+deptId+"'";
		List<SysDepartment> list=super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
	@Override
	public List<VinpatientApplyout> queryVinpatientApplyout(String deptId,
			String type, String page, String rows,String tradeName,String inpatientNo,String endDate,String beginDate) {
		String sql=querySql(deptId,type,tradeName,inpatientNo,endDate,beginDate);
		SQLQuery queryObject = this.getSession().createSQLQuery(sql.toString()).addScalar("useName")
				.addScalar("tradeName").addScalar("specs").addScalar("doseOnce",Hibernate.DOUBLE).addScalar("doseUnit").addScalar("dfqCexp")
				.addScalar("applyNum",Hibernate.DOUBLE).addScalar("drugDeptCode").addScalar("sendType")
				.addScalar("billclassCode").addScalar("applyOpercode").addScalar("applyDate",Hibernate.TIMESTAMP)
				.addScalar("printEmpl").addScalar("printDate",Hibernate.TIMESTAMP).addScalar("validState")
				.addScalar("patientName").addScalar("bedName").addScalar("baiyao");
		if(StringUtils.isNotBlank(beginDate)&&StringUtils.isNotBlank(endDate)){
			queryObject.setParameter("beginDate", beginDate).setParameter("endDate", endDate);
		}
				
		int start = Integer.parseInt(page == null ? "1" : page);
		int count = Integer.parseInt(rows == null ? "20" : rows);
		List<VinpatientApplyout> VinpatientApplyoutList = queryObject.setFirstResult((start - 1) * count).setMaxResults(count).setResultTransformer(Transformers.aliasToBean(VinpatientApplyout.class)).list();
		if(VinpatientApplyoutList!=null&&VinpatientApplyoutList.size()>0){
			return VinpatientApplyoutList;
		}
		return new ArrayList<VinpatientApplyout>();
	}
	@Override
	public int qqueryVinpatientApplyoutTotal(String deptId, String type,String tradeName,String inpatientNo,String endDate,String beginDate) {
		String sql=querySql(deptId,type,tradeName,inpatientNo,endDate,beginDate).toString();
		SQLQuery queryObject = this.getSession().createSQLQuery(sql.toString()).addScalar("useName")
				.addScalar("tradeName").addScalar("specs").addScalar("doseOnce",Hibernate.DOUBLE).addScalar("doseUnit").addScalar("dfqCexp")
				.addScalar("applyNum",Hibernate.DOUBLE).addScalar("drugDeptCode").addScalar("sendType")
				.addScalar("billclassCode").addScalar("applyOpercode").addScalar("applyDate",Hibernate.TIMESTAMP)
				.addScalar("printEmpl").addScalar("printDate",Hibernate.TIMESTAMP).addScalar("validState")
				.addScalar("patientName").addScalar("bedName").addScalar("baiyao");
		if(StringUtils.isNotBlank(beginDate)&&StringUtils.isNotBlank(endDate)){
			queryObject.setParameter("beginDate", beginDate).setParameter("endDate", endDate);
		}
		return queryObject.list()==null?0:queryObject.list().size();
	}
	private String querySql(String deptId, String type,String tradeName,String inpatientNo,String endDate,String beginDate){
		String sql="";
		if("N".equals(type)){
			sql=" select t.use_name as useName,t.trade_name as tradeName, t.specs as specs,"
					+ " t.dose_once as doseOnce, t.dose_unit as doseUnit, t.dfq_cexp as dfqCexp,"
					+ " sum(t.apply_num) as applyNum, t.drug_dept_code as drugDeptCode,"
					+ " DECODE(T.send_type, '1', '集中发送', '2', '临时发送', '全部') as sendType,"
					+ " t.billclass_code as billclassCode, t.apply_opercode as applyOpercode, t.apply_date as applyDate,"
					+ " t.print_empl as printEmpl, t.print_date as printDate,"
					+ " DECODE(T.valid_state, '1', '有效', '无效') as validState, tt.patient_name as patientName, tt.bed_name as bedName,"
					+ " DECODE(T.PRINT_DATE, '', '未摆', '已摆') as baiyao"
					+ " from T_DRUG_APPLYOUT_NOW t"
					+ " join t_inpatient_info_now tt on tt.inpatient_no = t.patient_id "
					+ " where t.del_flg = 0 and t.stop_flg = 0 ";
					if(StringUtils.isNotBlank(tradeName)){
						sql+=" and T.trade_name like '%"+tradeName+"%' ";
					}
					if(StringUtils.isNotBlank(inpatientNo)){
						sql+=" and T.patient_id = '"+inpatientNo+"'";
					}
					if(StringUtils.isNotBlank(beginDate)&&StringUtils.isNotBlank(endDate)){
						sql+=" and t.apply_date >=  to_date(:beginDate ,'yyyy-MM-dd hh24:mi:ss') ";
						sql+=" and t.apply_date <=  to_date(:endDate ,'yyyy-MM-dd hh24:mi:ss') ";
					}
				sql+=" and tt.NURSE_CELL_CODE = '"+deptId+"'"
						+ "group by T.trade_name, T.specs,T.dose_once, T.dose_unit, T.dfq_cexp,T.use_name,"
						+ " T.min_unit, T.drug_dept_code, T.send_type, T.billclass_code, T.apply_opercode,T.apply_date,"
						+ " T.print_empl, T.print_date, T.valid_state,tt.patient_name, tt.bed_name";
		}
		return sql;
	}
	@Override
	public SysDepartment querySysDepartment(String id) {
		String hql=" from SysDepartment c where c.stop_flg=0 and c.del_flg=0 and c.deptCode='"+id+"'";
		List<SysDepartment> list=super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
       return new SysDepartment();
	}
	@Override
	public User queryUser(String id) {
		String hql=" from User c where c.stop_flg=0 and c.del_flg=0 and c.account='"+id+"'";
		List<User> list=super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
       return new User();
	}
	@Override
	public DrugBillclass queryDrugBillclass(String id) {
		String hql=" from DrugBillclass c where c.stop_flg=0 and c.del_flg=0 and c.id='"+id+"'";
		List<DrugBillclass> list=super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
       return new DrugBillclass();
	}
}
