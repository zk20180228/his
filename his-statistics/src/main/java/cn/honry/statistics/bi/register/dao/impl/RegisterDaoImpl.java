package cn.honry.statistics.bi.register.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BiBaseOrganization;
import cn.honry.base.bean.model.BiRegister;
import cn.honry.base.bean.model.BiRegisterGrade;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.statistics.bi.inpatient.dischargePerson.vo.DischargePersonVo;
import cn.honry.statistics.bi.register.dao.RegisterDao;
import cn.honry.statistics.bi.register.vo.RegisterVo;
import cn.honry.statistics.util.dateVo.DateVo;

@Repository("registerDao")
@SuppressWarnings({"all"})
public class RegisterDaoImpl extends HibernateEntityDao<BiRegister> implements RegisterDao{

	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	
	public List<SysDepartment> queryAllDept() {
		//以后会将dept_id更改为dept_code
				String sql="select bd.dept_code as deptCode,bd.DEPT_NAME as deptName from T_DEPARTMENT bd ";
				SQLQuery queryObject=this.getHibernateTemplate().getSessionFactory().openSession().createSQLQuery(sql).addScalar("deptCode").addScalar("deptName");
				List<SysDepartment> bdl=queryObject.setResultTransformer(Transformers.aliasToBean(SysDepartment.class)).list();
				if(bdl!=null&&bdl.size()>0){
					SysDepartment bd=new SysDepartment();
					bd.setDeptCode("1");
					bd.setDeptName("全部");
					bdl.add(0, bd);
					return bdl;
				}
				return new ArrayList<SysDepartment>();
	}


	public List<BiRegisterGrade> queryAllGrade() {
		
		String sql="select a.grade_code as gradeCode ,a.grade_name as gradeName from BI_REGISTER_GRADE a";
		SQLQuery queryObject = this.getHibernateTemplate().getSessionFactory().openSession().createSQLQuery(sql).addScalar("gradeCode").addScalar("gradeName");
		List<BiRegisterGrade> bdl = queryObject.setResultTransformer(Transformers.aliasToBean(BiRegisterGrade.class)).list();
		if(bdl!=null&&bdl.size()>0){
			BiRegisterGrade bd=new BiRegisterGrade();
			bdl.add(0, bd);
			return bdl;
		}
		return new ArrayList<BiRegisterGrade>();
	}


	@Override
	public List<RegisterVo> queryregisterid(String[] diArrayKey,List<Map<String,List<String>>> list,int dateType,DateVo datevo ) {
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
		//查询时间
		StringBuffer sql=new StringBuffer();//sql语句的StringBuffer对象
		StringBuffer order=new StringBuffer();//最后order by 的顺序（即：选择的维度顺序）StringBuffer对象
	    sql.append(" select distinct  ");
	    for(int i=0;i<diArrayKey.length;i+=2){
	    	if("dept_code".equals(diArrayKey[i])){
	    		sql.append(" t.dept_code as deptName");
	    		order.append("t."+diArrayKey[i]);
	    	}
	    	if("reglevl_code".equals(diArrayKey[i])){
	    		sql.append(" t.reglevl_code as deptType");
	    		order.append("t."+diArrayKey[i]);
	    	}
	    	//当拼接的不是最后一个查询维度时，order后面拼接一个","（最后一个order条件不需要添加","）
			if(i!=(diArrayKey.length-2)){
				order.append(",");
			}
			sql.append(",");
	    }
	    sql.append("  count(t.reglevl_name) as registerPerson, ");
	    sql.append("  min(t.REG_FEE) as fee,  ");
	    if(dateType==1){
			sql.append("         to_char(t.REG_DATE,'yyyy') as timeChose");
		}else if(dateType==2){
			sql.append("        to_char(to_char(t.REG_DATE,'yyyy')||'/'||to_char(t.REG_DATE,'q')) as timeChose ");
		}else if(dateType==3){
			sql.append("         to_char(t.REG_DATE,'yyyy/mm') as timeChose");
		}else if(dateType==4){
			sql.append("         to_char(t.REG_DATE,'yyyy/mm/dd') as timeChose");
		}
		sql.append("	  from  VIEW_REGISTER_MAIN t  ");
		sql.append("	  where t.trans_type='1' and t.in_state='0' and t.ynregchrg='1'  ");
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
		int a = 0;
		//遍历数组，添加查询条件（匹配所选择的维度拼接）
		for(int i=0;i<diArrayKey.length;i+=2){
			if("dept_code".equals(diArrayKey[i])){
				String strings = list.get(a).get(diArrayKey[i]).toString().replace(",", "','").replace(" ","").replace("[","").replace("]","");
				sql.append(" and t.dept_code  in ('"+strings+"')");
			}
			if("reglevl_code".equals(diArrayKey[i])){
				String strings = list.get(a).get(diArrayKey[i]).toString().replace(",", "','").replace(" ","").replace("[","").replace("]","");
				sql.append(" and t.reglevl_code  in ('"+strings+"')");
			}
			a++;
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
		SQLQuery queryObject=this.getSession().createSQLQuery(sql.toString());
		for(int i=0;i<diArrayKey.length;i+=2){
			if("dept_code".equals(diArrayKey[i])){
				queryObject.addScalar("deptName");
			}
			if("reglevl_code".equals(diArrayKey[i])){
				queryObject.addScalar("deptType");
			}
		}
		queryObject.addScalar("registerPerson",Hibernate.INTEGER).addScalar("fee",Hibernate.DOUBLE).addScalar("timeChose");
		List<RegisterVo> dischargePersonVo=queryObject.setResultTransformer(Transformers.aliasToBean(RegisterVo.class)).list();
		if(dischargePersonVo!=null&&dischargePersonVo.size()>0){
			return dischargePersonVo;
		}
		
		return new ArrayList<RegisterVo>();
	}
	@Override
	public List<RegisterVo> queryStatDate(String timeString, String dateType, String nameString) {
		final StringBuffer sqlNew=new StringBuffer();
		sqlNew.append("select dept.dept_name as deptName,count(dept.dept_code) as registerPerson  ");
		sqlNew.append("from BI_REGISTER t left join T_DEPARTMENT dept on dept.dept_code=t.dept_code where t.trans_type='1' and t.in_state='0' and t.ynregchrg='1'");
		if(nameString!=null&&!nameString.equals("")){
			sqlNew.append(" and dept.dept_code in("+nameString+")");
		}
		if("1".equals(dateType)){//同比
			sqlNew.append(" and to_char(t.REG_DATE, 'yyyy')='"+timeString+"'");
		}else if("2".equals(dateType)){//环比
			sqlNew.append("and to_char(t.REG_DATE, 'mm')='"+timeString+"' ");
		}
		sqlNew.append("group by dept.dept_name");
		List<RegisterVo> list = (List<RegisterVo>) this.getHibernateTemplate().execute(new HibernateCallback(){
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sqlNew.toString())
						.addScalar("deptName").addScalar("registerPerson",Hibernate.INTEGER);
				return queryObject.setResultTransformer(Transformers.aliasToBean(RegisterVo.class)).list();
			}
		});
		return list;
	}


	@Override
	public List<BiBaseOrganization> queryDeptForBiPublic() {
		String sql="select o.ORG_CODE as orgCode , o.ORG_NAME as orgName from BI_BASE_ORGANIZATION o";
		SQLQuery queryObject =this.getSession().createSQLQuery(sql);
		queryObject.addScalar("orgCode").addScalar("orgName");
		List<BiBaseOrganization> bdl = queryObject.setResultTransformer(Transformers.aliasToBean(BiBaseOrganization.class)).list();
		if(bdl!=null){
			return bdl;
		}
		return new ArrayList<BiBaseOrganization>();
	}


	@Override
	public List<BiBaseOrganization> queryreglevlForBiPublic() {
		String sql="select a.grade_code as orgCode ,a.grade_name as orgName from BI_REGISTER_GRADE a";
		SQLQuery queryObject =this.getSession().createSQLQuery(sql);
		queryObject.addScalar("orgCode").addScalar("orgName");
		List<BiBaseOrganization> bdl = queryObject.setResultTransformer(Transformers.aliasToBean(BiBaseOrganization.class)).list();
		if(bdl!=null){
			return bdl;
		}
		return new ArrayList<BiBaseOrganization>();
	}

}
