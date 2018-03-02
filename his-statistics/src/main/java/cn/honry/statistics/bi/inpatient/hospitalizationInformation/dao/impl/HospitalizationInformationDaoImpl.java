package cn.honry.statistics.bi.inpatient.hospitalizationInformation.dao.impl;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.internal.compiler.ast.AND_AND_Expression;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BIBaseDistrict;
import cn.honry.base.bean.model.BiBaseOrganization;
import cn.honry.base.bean.model.BiInpatientInfo;
import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.statistics.bi.inpatient.hospitalizationInformation.dao.HospitalizationInformationDao;
import cn.honry.statistics.bi.inpatient.hospitalizationInformation.vo.HospitalizationInformationVo;
import cn.honry.statistics.bi.outpatient.outpatientWorkload.vo.OutpatientWorkloadVo;
import cn.honry.statistics.util.dateVo.DateVo;
import freemarker.core.ReturnInstruction.Return;
@Repository("hospitalizationInformationDao")
@SuppressWarnings({ "all" })
public class HospitalizationInformationDaoImpl extends HibernateEntityDao<BiInpatientInfo> implements HospitalizationInformationDao {
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<BiBaseOrganization> queryAllDept() {
		String sql="select o.ORG_CODE as orgCode , o.ORG_NAME as orgName from BI_BASE_ORGANIZATION o";
		SQLQuery queryObject =this.getSession().createSQLQuery(sql);
		queryObject.addScalar("orgCode").addScalar("orgName");
		List<BiBaseOrganization> bdl=bdl = queryObject.setResultTransformer(Transformers.aliasToBean(BiBaseOrganization.class)).list();
		if(bdl!=null){
			return bdl;
		}
		return new ArrayList<BiBaseOrganization>();
	}
	/**
	*	!!!!!!    t_ 之后要全部转为bi_
	*/
	@Override
	public List<HospitalizationInformationVo> querytDatagrid(String[] diArrayKey,List<Map<String,List<String>>> list,int dateType,DateVo datevo) {
		//明细表 别名 info  科室表别名 dept 
		//业务表中 入院时间字段为  in_date  统计表（bi）中为INPATIENT_TIME
		String sqldate=null;
		//查询字段 sql
		StringBuffer selectSql=new StringBuffer("select ");
		//from 表sql
		StringBuffer fromSql=new StringBuffer();
		//from 表sql
//		StringBuffer joinSql=new StringBuffer();
		//查询条件sql
		StringBuffer whereSql=new StringBuffer(" where 1=1 ");
		//排序sql
		StringBuffer orderSql=new StringBuffer("");
		String jdString = "";
		String orderString = "";
		//通过判断时间维度种类确定天数和
		if(dateType==1){//年
			String date1=datevo.getYear1()+"";
			String date2=datevo.getYear2()+"";//
			whereSql.append(" and to_char( info.IN_DATE,'yyyy') between '"+date1+"'  and '"+date2+"'");
			selectSql.append(" to_char( info.IN_DATE, 'yyyy') as inpatientTime, ");
			orderSql.append("  to_char( info.IN_DATE, 'yyyy') ");
		}else if(dateType==2){//季度
			String date1=datevo.getYear1()+"";
			String date2=datevo.getYear2()+"";
			whereSql.append(" and to_char( info.IN_DATE,'yyyy') between'"+date1+"'  and '"+date2+"'");
			whereSql.append(" and to_char( info.IN_DATE,'q') between '"+datevo.getQuarter1()+"' and '"+datevo.getQuarter2()+"' ");
			selectSql.append(" to_char( info.IN_DATE, 'yyyy')||'/'||to_char( info.IN_DATE, 'q') as inpatientTime , ");
			orderString="  to_char( info.IN_DATE, 'yyyy')||'/'||to_char( info.IN_DATE, 'q') ";
			jdString=" to_char( info.IN_DATE, 'yyyy'), to_char( info.IN_DATE, 'q') ";
		}else if(dateType==3){//月
			String date1=datevo.getMonth1()+"";
			String date2=datevo.getMonth2()+"";
			whereSql.append(" and to_char( info.IN_DATE,'yyyy/mm') between '"+datevo.getYear1()+"/"+(date1.length()==1?"0"+date1:date1)+"' and '" +datevo.getYear2()+"/"+(date2.length()==1?"0"+date2:date2)+"' ");
			selectSql.append(" to_char( info.IN_DATE, 'yyyy/mm') as inpatientTime, ");
			orderSql.append("  to_char( info.IN_DATE, 'yyyy/mm') ");
		}else if(dateType==4){//天
			String date1=datevo.getMonth1()+"";
			String date2=datevo.getMonth2()+"";
			whereSql.append(" and to_char(info.IN_DATE,'yyyy/mm/dd') between '"+datevo.getYear1()+"/"+(date1.length()==1?"0"+date1:date1)+"/"+datevo.getDay1()+"' and '" +datevo.getYear2()+"/"+(date2.length()==1?"0"+date2:date2)+"/"+datevo.getDay2()+"' ");
			selectSql.append(" to_char(info.IN_DATE, 'yyyy/mm/dd') as inpatientTime, ");
			orderSql.append("  to_char(info.IN_DATE, 'yyyy/mm/dd') ");
		}
		int n=0;
		List whereList=null;
		//遍历数组，去匹配所选择的维度拼接sql和order
		//home in_sourse status age 维  度
		for(int i=0;i<diArrayKey.length;i+=2){
			
			if("dept_code".equals(diArrayKey[i])){
				whereList=list.get(n).get(diArrayKey[i]);
				selectSql.append(" info.dept_code as deptName");
				if(i<diArrayKey.length-2){
					selectSql.append(",");
				}
				orderSql.append(" ,"+diArrayKey[i]);
				if(i==diArrayKey.length-2){
					selectSql.append(", count(info.dept_code) as passengers");
				}
				String value="";
				whereSql.append( "and info.dept_code in (");
				for(int j=0;j<whereList.size();j++){
					value=whereList.get(j).toString();
					whereSql.append("'"+value+"'");
					if(j<whereList.size()-1){
						whereSql.append(",");
					}
				}
				whereSql.append(")");
			}
			if("home".equals(diArrayKey[i])){
				selectSql.append(" info.patient_nativeplace as home");
				if(i<diArrayKey.length-2){
					selectSql.append(",");
				}
				whereList=list.get(n).get(diArrayKey[i]);
				orderSql.append(" ,info.patient_nativeplace");
				if(i==diArrayKey.length-2){
					selectSql.append(", count(info.patient_nativeplace) as passengers");
				}
				String value="";
				whereSql.append("and info.patient_nativeplace in ( ");
				for(int j=0;j<whereList.size();j++){
					whereSql.append("'"+whereList.get(j)+"'");
					if(j<whereList.size()-2){
						whereSql.append(",");
					}
				}
				whereSql.append(")");
			}
			if("in_source".equals(diArrayKey[i])){
				whereList=list.get(n).get(diArrayKey[i]);
				selectSql.append(" info.in_source as insource");
				if(i<diArrayKey.length-2){
					selectSql.append(",");
				}
				orderSql.append(" ,info.in_source");
				if(i==diArrayKey.length-2){
					selectSql.append(", count(info.in_source) as passengers");
				}
				String value="";
				whereSql.append( " and info.in_source in (");
				for(int j=0;j<whereList.size();j++){
					value=whereList.get(j).toString();
					whereSql.append("'"+value+"'");
					if(j<whereList.size()-1){
						whereSql.append(",");
					}
				}
				whereSql.append(")");
			}
			if("status".equals(diArrayKey[i])){
				whereList=list.get(n).get(diArrayKey[i]);
				selectSql.append(" info.PATIENT_STATUS as patientstatus");
				if(i<diArrayKey.length-2){
					selectSql.append(",");
				}
				orderSql.append("  ,info.PATIENT_STATUS");
				if(i==diArrayKey.length-2){
					selectSql.append("  ,count(info.PATIENT_STATUS) as passengers");
				}
				String value="";
				whereSql.append( "and info.PATIENT_STATUS in (");
				for(int j=0;j<whereList.size();j++){
					value=whereList.get(j).toString();
					whereSql.append("'"+value+"'");
					if(j<whereList.size()-1){
						whereSql.append(",");
					}
				}
				whereSql.append(")");
			}
			if("age".equals(diArrayKey[i])){
				selectSql.append(" info.age as reportBirthday");
				if(i<diArrayKey.length-2){
					selectSql.append(",");
				}
				orderSql.append("  ,info.age");
				if(i==diArrayKey.length-2){
					selectSql.append("  ,count(info.age) as passengers");
				}
				for(int j=0;j<list.get(n).get(diArrayKey[i]).size();j++){
					String age=list.get(n).get(diArrayKey[i]).get(j);
					if(age.contains("-"))	{
						String []ageArr=age.split("-");
						whereSql.append((j!=0?" or ":" and ")+" (info.age between '"+ageArr[0]+"' and '"+ageArr[1]+"')");
					}else{
						whereSql.append((j!=0?" or ":" and ")+" (info.age = '"+age+"'  )");
					}
						
				}
				//当拼接的不是最后一个查询维度时，order后面拼接一个","（最后一个order条件不需要添加","）
			}

			
 			n++;
		}

		
//		fromSql.append(" from bi_INPATIENT_INFO info "); 更改为查询视图
		fromSql.append(" from v_hospitalization_information info ");
//		joinSql.append(" left join T_DEPARTMENT dept on dept.dept_id=info.dept_code  ");
//		whereSql.append(" and info.in_state='I' and dept.dept_type = 'I'");
		
		
		final String sql=selectSql.append(fromSql.toString()).append(whereSql.toString()).append(" group by "+jdString+orderSql.toString()).append(" order by "+orderString+orderSql.toString()).toString(); 
		
			SQLQuery queryObject=this.getSession().createSQLQuery(sql.toString()).addScalar("deptName").addScalar("passengers",Hibernate.INTEGER).addScalar("inpatientTime");
			//动态追加scalar
			for(int i=0;i<diArrayKey.length;i+=2){
				if("home".equals(diArrayKey[i])){
					queryObject.addScalar("home");
				}
				if("in_source".equals(diArrayKey[i])){
					queryObject.addScalar("insource");
				}
				if("status".equals(diArrayKey[i])){
					queryObject.addScalar("patientstatus");
				}
				if("age".equals(diArrayKey[i])){
					queryObject.addScalar("reportBirthday");
				}
			}
			List<HospitalizationInformationVo> result = queryObject.setResultTransformer(Transformers.aliasToBean(HospitalizationInformationVo.class)).list();
			if(result!=null){
				return result;
			}
			return new ArrayList<HospitalizationInformationVo>();
	}
	
	@Override
	public List<BIBaseDistrict> getHome() {
		String sql = "select t.city_name as cityName,t.city_code as cityCode from bi_base_district t where t.city_level='1'";
		SQLQuery queryObject=this.getSession().createSQLQuery(sql).addScalar("cityCode").addScalar("cityName");
		List<BIBaseDistrict> dictionaryList=queryObject.setResultTransformer(Transformers.aliasToBean(BIBaseDistrict.class)).list();
		if(dictionaryList!=null&&dictionaryList.size()>0){
			return dictionaryList;
		}
		return new ArrayList<BIBaseDistrict>();
	}
	
	@Override
	public List<BiBaseOrganization> queryDeptForBiPublic(String deptType) {
		String sql="select o.ORG_CODE as orgCode , o.ORG_NAME as orgName from BI_BASE_ORGANIZATION o";
		//判断参数deptType科室类型是否有值
		if(StringUtils.isNotBlank(deptType)){
			if(deptType.indexOf(",")!=-1){
				deptType= deptType.replace(",", "','");
				sql += " where o.org_kind_code in ('"+deptType+"') and o.org_Code <>'0000001' and o.org_Code <> '0000002'";
			}else{
				sql +=" where o.org_kind_code= '"+deptType+"' and o.org_Code <>'0000001' and o.org_Code <> '0000002'";
			}
			
		}
		SQLQuery queryObject =this.getSession().createSQLQuery(sql);
		queryObject.addScalar("orgCode").addScalar("orgName");
		List<BiBaseOrganization> bdl=bdl = queryObject.setResultTransformer(Transformers.aliasToBean(BiBaseOrganization.class)).list();
		if(bdl!=null){
			return bdl;
		}
		return new ArrayList<BiBaseOrganization>();
	}

	
}