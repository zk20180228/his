/**
 * 
 */
package cn.honry.statistics.bi.inpatient.patientsCost.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.statistics.bi.inpatient.patientsCost.dao.PatientsCostDao;
import cn.honry.statistics.bi.inpatient.patientsCost.vo.PatientCostVo;
import cn.honry.statistics.util.dateVo.DateVo;
import cn.honry.utils.HisParameters;

/**
 * 在院病人费用分析DAO实现类
 * @author luyanshou
 *
 */
@Repository("patientsCostDao")
@SuppressWarnings({"all"})
public class PatientsCostDaoImpl extends HibernateEntityDao<InpatientInfo> implements PatientsCostDao {

	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	/**
	 * 查询住院下的科室信息
	 */
	public List<SysDepartment> getDept(){
		String sql="select t.DEPT_ID as id,t.DEPT_NAME as deptName from "+HisParameters.HISPARSCHEMAHISUSER+"T_DEPARTMENT t where t.DEPT_TYPE='I'";
		SQLQuery queryObject = this.getSession().createSQLQuery(sql)
				.addScalar("id")
				.addScalar("deptName");
		List<SysDepartment> list =queryObject.setResultTransformer(Transformers.aliasToBean(SysDepartment.class)).list();
		if(list!=null && list.size()>0){
			return list;
		}
		return new ArrayList<SysDepartment>();
	}
	
	/**
	 * 查询科室名称
	 * @param code
	 * @return
	 */
	public String getdeptName(String code){
		String sql="select t.DEPT_NAME from "+HisParameters.HISPARSCHEMAHISUSER+"T_DEPARTMENT t where t.DEPT_ID= '"+code+"'";
		SQLQuery queryObject = this.getSession().createSQLQuery(sql);
		List<String> list = queryObject.list();
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 查询统计费用名称列表
	 * 
	 */
	public List<MinfeeStatCode> getFeeName(){
		String sql="select distinct t.fee_stat_name as feeStatName from "+HisParameters.HISPARSCHEMAHISUSER+" t_charge_minfeetostat t";
		List<MinfeeStatCode> list = this.getSession().createSQLQuery(sql).addScalar("feeStatName").
				setResultTransformer(Transformers.aliasToBean(MinfeeStatCode.class)).list();
		return list;
	}
	
	/**
	 * 查询非药品费用
	 * @param dept 科室
	 * @param feeName 统计费用名称
	 * @param datevo 时间vo
	 * @param dateType 统计方式
	 * @return
	 */
	public List<PatientCostVo> getItemCost(String dept,String feeName,DateVo datevo,int dateType){
		StringBuffer sbf= new StringBuffer("select sum(t.tot_cost) as itemCost,sum(t.tot_cost) as totCost");
		String[]feeNames = null;
		String[]depts = null;
		String s=" group by ";
		if(dept!=null &&  dept.contains(",")){
			depts = dept.split(",");
		}
		if(StringUtils.isNotBlank(dept)){
			sbf.append(",d.dept_name as inhos_deptcode ");
			s+="d.dept_name ";
		}
		if(feeName!=null && feeName.contains(",")){
			 feeNames = feeName.split(",");
			sbf.append(",c.fee_stat_name as cost ");
		}
		if(dateType==1){
			sbf.append(",to_char(t.fee_date,'yyyy') as timeChose");
		}else if(dateType==2){
			sbf.append(",to_char(to_char(t.fee_date,'yyyy')||'/'||to_char(t.fee_date,'q')) as timeChose");
		}else if(dateType==3){
			sbf.append(",to_char(t.fee_date,'yyyy/mm') as timeChose");
		}else if(dateType==4){
			sbf.append(",to_char(t.fee_date,'yyyy/mm/dd') as timeChose");
		}
		
		sbf.append(" from v_inpatient_itemlist t inner join t_charge_minfeetostat c on t.fee_code = c.minfee_code ");
		
		if(StringUtils.isNotBlank(feeName)){
			if(!feeName.contains(",")){
				sbf.append(" and c.fee_stat_name= '"+feeName+"' ");
			}else{
				sbf.append(" and (");
				for (String str : feeNames) {
					if(str.equals(feeNames[feeNames.length-1])){
						sbf.append("  c.fee_stat_name= '"+str+"') ");
					}else{
						sbf.append("  c.fee_stat_name= '"+str+"' or ");
					}
				}
			}
		}
		sbf.append(" left join T_DEPARTMENT d on t.inhos_deptcode= d.dept_id where 1=1 ");
		if(StringUtils.isNotBlank(dept)){
			if(!dept.contains(",")){
				sbf.append(" and d.dept_name = '"+dept+"' ");
			}else{
				sbf.append(" and (");
				for (String str : depts) {
					if(str.equals(depts[depts.length-1])){
						sbf.append("  d.dept_name= '"+str+"') ");
					}else{
						sbf.append("  d.dept_name= '"+str+"' or ");
					}
				}
			}
		}
		
		if(dateType==1){
			sbf.append(" and  to_char(t.fee_date,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'");
		}else if(dateType==2||dateType==3){
			sbf.append(" and  to_char(t.fee_date,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'   ");
			if(dateType==2){
				sbf.append(" and to_char(t.fee_date,'q') between '"+(datevo.getQuarter1())+"' and '"+(datevo.getQuarter2())+"' ");
			}
			if(dateType==3){
				sbf.append(" and  to_char(t.fee_date,'mm') between '"+(datevo.getMonth1()>9?datevo.getMonth1():"0"+datevo.getMonth1())+"' and '"+(datevo.getMonth2()>9?datevo.getMonth2():"0"+datevo.getMonth2())+"'");
			}
		}else if(dateType==4){
			sbf.append(" and  to_char(t.fee_date,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'   ");
			sbf.append(" and  to_char(t.fee_date,'mm') between '"+(datevo.getMonth1()>9?datevo.getMonth1():"0"+datevo.getMonth1())+"' and '"+(datevo.getMonth2()>9?datevo.getMonth2():"0"+datevo.getMonth2())+"'");
			sbf.append(" and  to_char(t.fee_date,'dd') between '"+(datevo.getDay1()>9?datevo.getDay1():"0"+datevo.getDay1())+"' and '"+(datevo.getDay2()>9?datevo.getDay2():"0"+datevo.getDay2())+"'");
		}
		sbf.append(s);
		if(feeName!=null && feeName.contains(",")){
			sbf.append(",c.fee_stat_name ");
		}
		if(dateType==1){
			sbf.append(",to_char(t.fee_date,'yyyy') ");
		}else if(dateType==2){
			sbf.append(",to_char(t.fee_date,'yyyy'),to_char(t.fee_date,'q') ");
		}else if(dateType==3){
			sbf.append(",to_char(t.fee_date,'yyyy/mm') ");
		}else if(dateType==4){
			sbf.append(",to_char(t.fee_date,'yyyy/mm/dd')");
		}
		System.out.println(sbf.toString());
		SQLQuery query = this.getSession().createSQLQuery(sbf.toString());
		if(feeName.contains(",")){
			query.addScalar("cost");
		}
		List<PatientCostVo> list = query.addScalar("itemCost",Hibernate.DOUBLE).addScalar("totCost",Hibernate.DOUBLE)
				.addScalar("inhos_deptcode").addScalar("timeChose").
				setResultTransformer(Transformers.aliasToBean(PatientCostVo.class)).list();
		return list;
	}
	
	/**
	 * 查询药品费用
	 * @param dept 科室
	 * @param feeName 统计费用名称
	 * @param datevo 时间vo
	 * @param dateType 统计方式
	 * @return
	 */
	public List<PatientCostVo> getMedicineCost(String dept,String feeName,DateVo datevo,int dateType){
		StringBuffer sbf= new StringBuffer("select sum(t.tot_cost) as medicineCost,sum(t.tot_cost) as totCost");
		String[]feeNames = null;
		String[]depts = null;
		String s=" group by ";
		if(dept!=null &&  dept.contains(",")){
			depts = dept.split(",");
			
		}
		if(StringUtils.isNotBlank(dept)){
			sbf.append(",d.dept_name as inhos_deptcode ");
			s+="d.dept_name ";
		}
		if(feeName!=null && feeName.contains(",")){
			 feeNames = feeName.split(",");
			sbf.append(",c.fee_stat_name as cost ");
		}
		if(dateType==1){
			sbf.append(",to_char(t.fee_date,'yyyy') as timeChose");
		}else if(dateType==2){
			sbf.append(",to_char(to_char(t.fee_date,'yyyy')||'/'||to_char(t.fee_date,'q')) as timeChose");
		}else if(dateType==3){
			sbf.append(",to_char(t.fee_date,'yyyy/mm') as timeChose");
		}else if(dateType==4){
			sbf.append(",to_char(t.fee_date,'yyyy/mm/dd') as timeChose");
		}
		
		sbf.append(" from v_inpatient_medicinelist t inner join t_charge_minfeetostat c on t.fee_code = c.minfee_code ");
		if(StringUtils.isNotBlank(feeName)){
			if(!feeName.contains(",")){
				sbf.append(" and c.fee_stat_name= '"+feeName+"' ");
			}else{
				sbf.append(" and (");
				for (String str : feeNames) {
					if(str.equals(feeNames[feeNames.length-1])){
						sbf.append("  c.fee_stat_name= '"+str+"') ");
					}else{
						sbf.append("  c.fee_stat_name= '"+str+"' or ");
					}
				}
			}
		}
		sbf.append(" left join T_DEPARTMENT d on t.inhos_deptcode= d.dept_id where 1=1 ");
		if(StringUtils.isNotBlank(dept)){
			if(!dept.contains(",")){
				sbf.append(" and d.dept_name = '"+dept+"' ");
			}else{
				sbf.append(" and (");
				for (String str : depts) {
					if(str.equals(depts[depts.length-1])){
						sbf.append("  d.dept_name= '"+str+"') ");
					}else{
						sbf.append("  d.dept_name= '"+str+"' or ");
					}
				}
			}
		}
		
		
		if(dateType==1){
			sbf.append(" and  to_char(t.fee_date,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'");
		}else if(dateType==2||dateType==3){
			sbf.append(" and  to_char(t.fee_date,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'   ");
			if(dateType==2){
				sbf.append(" and to_char(t.fee_date,'q') between '"+(datevo.getQuarter1())+"' and '"+(datevo.getQuarter2())+"' ");
			}
			if(dateType==3){
				sbf.append(" and  to_char(t.fee_date,'mm') between '"+(datevo.getMonth1()>9?datevo.getMonth1():"0"+datevo.getMonth1())+"' and '"+(datevo.getMonth2()>9?datevo.getMonth2():"0"+datevo.getMonth2())+"'");
			}
		}else if(dateType==4){
			sbf.append(" and  to_char(t.fee_date,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'   ");
			sbf.append(" and  to_char(t.fee_date,'mm') between '"+(datevo.getMonth1()>9?datevo.getMonth1():"0"+datevo.getMonth1())+"' and '"+(datevo.getMonth2()>9?datevo.getMonth2():"0"+datevo.getMonth2())+"'");
			sbf.append(" and  to_char(t.fee_date,'dd') between '"+(datevo.getDay1()>9?datevo.getDay1():"0"+datevo.getDay1())+"' and '"+(datevo.getDay2()>9?datevo.getDay2():"0"+datevo.getDay2())+"'");
		}
		sbf.append(s);
		if(feeName!=null && feeName.contains(",")){
			sbf.append(",c.fee_stat_name ");
		}
		if(dateType==1){
			sbf.append(",to_char(t.fee_date,'yyyy') ");
		}else if(dateType==2){
			sbf.append(",to_char(t.fee_date,'yyyy') ,to_char(t.fee_date,'q') ");
		}else if(dateType==3){
			sbf.append(",to_char(t.fee_date,'yyyy/mm') ");
		}else if(dateType==4){
			sbf.append(",to_char(t.fee_date,'yyyy/mm/dd')");
		}
		System.out.println(sbf.toString());
		SQLQuery query = this.getSession().createSQLQuery(sbf.toString());
		if(feeName.contains(",")){
			query.addScalar("cost");
		}
		List<PatientCostVo> list = query.addScalar("medicineCost",Hibernate.DOUBLE).addScalar("totCost",Hibernate.DOUBLE)
				.addScalar("inhos_deptcode").addScalar("timeChose").
				setResultTransformer(Transformers.aliasToBean(PatientCostVo.class)).list();
		return list;
	}
}
