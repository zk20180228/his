package cn.honry.statistics.bi.outpatient.outpatientWorkload.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BiBaseOrganization;
import cn.honry.base.bean.model.BiRegister;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.statistics.bi.outpatient.outpatientWorkload.dao.OutpatientWorkloadDao;
import cn.honry.statistics.bi.outpatient.outpatientWorkload.vo.OutpatientWorkloadVo;
import cn.honry.statistics.util.dateVo.DateVo;
import freemarker.template.utility.StringUtil;
@Repository("outpatientWorkloadDao")
@SuppressWarnings({ "all" })
public class OutpatientWorkloadDaoImpl extends HibernateEntityDao<BiRegister> implements OutpatientWorkloadDao {
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}


	@Override
	public List<OutpatientWorkloadVo> querytWordloadDatagrid(String[] diArrayKey,List<Map<String,List<String>>> list,int dateType,DateVo datevo ) {
		//天数
		int num=365;
		String sqldate=null;
		//通过判断时间维度种类确定天数和
		if(dateType==1){
			num=365;
			String date1=datevo.getYear1()+"";
			String date2=datevo.getYear2()+"";//
			sqldate=" select  d.ID_KEY from BI_DATE  d where d.YEAR between '"+date1+"'  and '"+date2+"'";
		}else if(dateType==2){
			String date1=datevo.getYear1()+"/"+datevo.getQuarter1();
			String date2=datevo.getYear2()+"/"+datevo.getQuarter2();
			num=90;
			sqldate=" select  d.ID_KEY from BI_DATE  d where d.YEAR between '"+date1+"'  and '"+date2+"' ";
		}else if(dateType==3){
			num=30;
			String date1=datevo.getYear1()+"/"+datevo.getMonth1();
			String date2=datevo.getYear2()+"/"+datevo.getMonth2();
			sqldate=" select  d.ID_KEY from BI_DATE  d where d.YEAR_MONTH between '"+date1+"' and '"+date2+"' ";
		}else if(dateType==4){
			num=1;
			String date11=datevo.getYear1()+"/"+datevo.getMonth1();
			String date12=datevo.getYear2()+"/"+datevo.getMonth2();
			String date21=datevo.getDay1()+"";
			String date22=datevo.getDay2()+"";
			sqldate="select  d.ID_KEY from BI_DATE  d where d.YEAR_MONTH between '"+date11+"' and '"+date12+"' and d.day_of_month between '"+date21+"' and '"+date22+"'";
		}
//		 SQLQuery queryObject=this.getSession().createSQLQuery(sqldate.toString());
//		 List list2 = queryObject.list();
//		 //声明dayKey字符串 拼接时间表里的主键Id，用","进行分隔
//		 String dayKey="";
//		 for(int i=0;i<list2.size();i++){
//			 if(dayKey!=""){
//				 dayKey +=",";
//			 }
//			 dayKey +=list2.get(i);
//		 }
		 
		//查询时间
		StringBuilder sql=new StringBuilder();//sql语句的StringBuffer对象
		StringBuilder order=new StringBuilder();//最后order by 的顺序（即：选择的维度顺序）StringBuffer对象
	    sql.append(" select ");
		//遍历数组，去匹配所选择的维度拼接sql和order
		for(int i=0;i<diArrayKey.length;i+=2){
			if("dept_code".equals(diArrayKey[i])){
				sql.append(" t.dept_code as deptDimensionality");
				order.append(diArrayKey[i]);
			}
			if("doct_code".equals(diArrayKey[i])){
				sql.append(" t.DOCT_CODE as doctorDimensionality");
				order.append(diArrayKey[i]);
			}
			if("reglevl_code".equals(diArrayKey[i])){
				sql.append(" t.reglevl_code as doctorlevelDimensionality");
				order.append(diArrayKey[i]);
			}
			//当拼接的不是最后一个查询维度时，order后面拼接一个","（最后一个order条件不需要添加","）
			if(i!=(diArrayKey.length-2)){
				order.append(",");
			}
			sql.append(",");
		}
		sql.append("	         count(1) as outpatientGross, ");
		sql.append("	       sum(decode(t.EMERGENCY_FLAG, '0', 1, 0)) as outpatientNum, ");
		sql.append("	       sum(decode(t.EMERGENCY_FLAG, '1', 1, 0)) as emergencyNum, ");
		sql.append("	      cast(count(1)*1./"+num+" as int) as outpatientAvernum, ");
		if(dateType==1){
			sql.append("         to_char(t.REG_DATE,'yyyy') as timeChose");
		}else if(dateType==2){
			sql.append("        to_char(to_char(t.REG_DATE,'yyyy')||'/'||to_char(t.REG_DATE,'q')) as timeChose ");
		}else if(dateType==3){
			sql.append("         to_char(t.REG_DATE,'yyyy/mm') as timeChose");
		}else if(dateType==4){
			sql.append("         to_char(t.REG_DATE,'yyyy/mm/dd') as timeChose");
		}
		sql.append("	  from T_REGISTER_MAIN t ");
		sql.append("	 where t.Ynsee='0'and t.trans_type='1'  ");
		if(dateType==1){
			sql.append(" and to_char(t.REG_DATE,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'");
		}else if(dateType==2){
			sql.append(" and to_char(t.REG_DATE,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'   ");
			sql.append(" and to_char(t.REG_DATE,'q') between '"+datevo.getQuarter1()+"' and '"+datevo.getQuarter2()+"'   ");
		}else if(dateType==3){
			sql.append(" and to_char(t.REG_DATE,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'   ");
			sql.append(" and to_char(t.REG_DATE,'mm') between '"+(datevo.getMonth1()>9?datevo.getMonth1():"0"+datevo.getMonth1())+"' and '"+(datevo.getMonth2()>9?datevo.getMonth2():"0"+datevo.getMonth2())+"'");
		}else if(dateType==4){
			sql.append(" and to_char(t.REG_DATE,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'   ");
			sql.append(" and to_char(t.REG_DATE,'mm') between '"+(datevo.getMonth1()>9?datevo.getMonth1():"0"+datevo.getMonth1())+"' and '"+(datevo.getMonth2()>9?datevo.getMonth2():"0"+datevo.getMonth2())+"'");
			sql.append(" and to_char(t.REG_DATE,'dd') between '"+(datevo.getDay1()>9?datevo.getDay1():"0"+datevo.getDay1())+"' and '"+(datevo.getDay2()>9?datevo.getDay2():"0"+datevo.getDay2())+"'");
		}
//		sql.append("	   and  t.DATE_KEY in ("+dayKey+")");
		//遍历数组，添加查询条件（匹配所选择的维度拼接）
		int n=0;
		for(int i=0;i<diArrayKey.length;i+=2){
//			sql.append(" and ");
			if("dept_code".equals(diArrayKey[i])){
				StringBuilder value=new StringBuilder();
				for(int j=0;j<list.get(n).get(diArrayKey[i]).size();j++){
					if(!"".equals(value.toString())){
						value.append(",");
					}
					value.append("'"+list.get(n).get(diArrayKey[i]).get(j)+"'");
				}
				sql.append(" and t.dept_code  in ("+value.toString()+")");
			}
			if("doct_code".equals(diArrayKey[i])){
				StringBuilder value=new StringBuilder();
				for(int j=0;j<list.get(n).get(diArrayKey[i]).size();j++){
					if(!"".equals(value.toString())){
						value.append(",");
					}
					value.append("'"+list.get(n).get(diArrayKey[i]).get(j)+"'");
				}
				sql.append("and t.doct_code in ("+value.toString()+")");
			}
			if("reglevl_code".equals(diArrayKey[i])){
				StringBuilder value=new StringBuilder();
				for(int j=0;j<list.get(n).get(diArrayKey[i]).size();j++){
					if(!"".equals(value.toString())){
						value.append(",");
					}
					value.append("'"+list.get(n).get(diArrayKey[i]).get(j)+"'");
				}
				sql.append("and t.reglevl_code in ("+value.toString()+")");
			}
			n++;
		}
		sql.append("	 group by  ");
		sql.append(order.toString());
		sql.append(",");
		//对于时间的排序放在最后
		if(dateType==1){
			sql.append("         to_char(t.REG_DATE,'yyyy') ");
		}else if(dateType==2){
			sql.append("         to_char(t.REG_DATE,'yyyy')  ");
			sql.append("        ,to_char(t.REG_DATE,'q')   ");
		}else if(dateType==3){
			sql.append("         to_char(t.REG_DATE,'yyyy/mm') ");
		}else if(dateType==4){
			sql.append("         to_char(t.REG_DATE,'yyyy/mm/dd')");
		}
		sql.append("	 order by ");
		sql.append(order.toString());
		sql.append(",");
		//对于时间的排序放在最后
		if(dateType==1){
			sql.append("         to_char(t.REG_DATE,'yyyy') ");
		}else if(dateType==2){
			sql.append("         to_char(to_char(t.REG_DATE,'yyyy')||'/'||to_char(t.REG_DATE,'q'))   ");
		}else if(dateType==3){
			sql.append("         to_char(t.REG_DATE,'yyyy/mm') ");
		}else if(dateType==4){
			sql.append("         to_char(t.REG_DATE,'yyyy/mm/dd')");
		}
		SQLQuery queryObject1=this.getSession().createSQLQuery(sql.toString());
		for(int i=0;i<diArrayKey.length;i+=2){
			if("dept_code".equals(diArrayKey[i])){
				queryObject1.addScalar("deptDimensionality");
			}
			if("doct_code".equals(diArrayKey[i])){
				queryObject1.addScalar("doctorDimensionality");
			}
			if("reglevl_code".equals(diArrayKey[i])){
				queryObject1.addScalar("doctorlevelDimensionality");
			}
			
		}
		queryObject1.addScalar("outpatientGross",Hibernate.INTEGER).addScalar("outpatientNum",Hibernate.INTEGER)
				.addScalar("emergencyNum",Hibernate.INTEGER).addScalar("outpatientAvernum",Hibernate.INTEGER).addScalar("timeChose");
		List<OutpatientWorkloadVo> bdl=bdl = queryObject1.setResultTransformer(Transformers.aliasToBean(OutpatientWorkloadVo.class)).list();
		if(bdl!=null){
			return bdl;
		}
		return new ArrayList<OutpatientWorkloadVo>();
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
