package cn.honry.statistics.bi.outpatient.outpatientPassengers.dao.impl;

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

import cn.honry.base.bean.model.BIBaseDistrict;
import cn.honry.base.bean.model.District;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.statistics.bi.outpatient.outpatientPassengers.dao.PassengersDAO;
import cn.honry.statistics.bi.outpatient.outpatientPassengers.vo.DimensionVO;
import cn.honry.statistics.doctor.regisdocscheinfo.vo.RegisDocScheInfoVo;
import cn.honry.statistics.util.dateVo.DateVo;

@Repository("passengersDAO")
@SuppressWarnings({"all"})
public class PassengersDAOImpl extends HibernateEntityDao<RegisDocScheInfoVo> implements PassengersDAO{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<DimensionVO> findDimensionList(DimensionVO dimensionVO) {
		String dept = dimensionVO.getDept().replace(",","','");
		dimensionVO.setDept(dept);
		String sql = "select ";
					 if(StringUtils.isNotBlank(dimensionVO.getDept())){
						 sql += " d.dept as dept,";
					 }
					 if(StringUtils.isNotBlank(dimensionVO.getSex())){
						 sql += " d.sex as sex,";
					 }
					 if(StringUtils.isNotBlank(dimensionVO.getYears())){
						 sql += " d.years as years,";
					 }
			  sql += "		 decode(e.sumPeople,null,0,e.sumPeople) as sumPeople,"+
					 "		 decode(e.ordinaryPeople,null,0,e.ordinaryPeople) as ordinaryPeople,"+
					 "		 decode(e.emerGencyPeople,null,0,e.emerGencyPeople) as emerGencyPeople"+
					 " from (select ";
					 if(StringUtils.isNotBlank(dimensionVO.getDept())){
						 sql += " b.dept,";
					 }
					 if(StringUtils.isNotBlank(dimensionVO.getSex())){
						 sql += " a.sex,";
					 }
					 if(StringUtils.isNotBlank(dimensionVO.getYears())){
						 sql += " c.years,";
					 }
			 sql +=	 " 1 from ( select 1 from dual k ) full join " ;
					if(StringUtils.isNotBlank(dimensionVO.getSex())){
						 sql +=	 " (select decode(p1.patient_sex, 1, '男', 2, '女', '未知') as sex from bi_base_patient p1 where p1.patient_sex_code in (1, 2)  group by decode(p1.patient_sex_code, 1, '男', 2, '女', '未知')) a on 1=1 full join";
					}
					if(StringUtils.isNotBlank(dimensionVO.getDept())){
						 sql +=  "  (select o.DEPT_NAME as dept from T_DEPARTMENT o where o.DEPT_NAME in ('"+dimensionVO.getDept()+"')) b on 1=1  full join";
					}
					if(StringUtils.isNotBlank(dimensionVO.getYears())){
						 sql +=  "  (select to_char(t.reg_date, 'yyyy') as years from t_register_main t  where to_char(t.reg_date, 'yyyy') in ('2014', '2013', '2012','2011')  group by to_char(t.reg_date, 'yyyy')  order by to_char(t.reg_date, 'yyyy')) c on 1=1 full join ";
					}
			 sql +=  " ( select 1 from dual k ) on 1=1   order by ";
                     if(StringUtils.isNotBlank(dimensionVO.getDept())){
						 sql += " b.dept,";
					 }
					 if(StringUtils.isNotBlank(dimensionVO.getSex())){
						 sql += " a.sex,";
					 }
					 if(StringUtils.isNotBlank(dimensionVO.getYears())){
						 sql += " c.years,";
					 }
			 sql += " 1 ) d "+
                     " left join (select ";
                     if(StringUtils.isNotBlank(dimensionVO.getDept())){
						 sql += " o.DEPT_NAME as dept,";
					 }
					 if(StringUtils.isNotBlank(dimensionVO.getSex())){
						 sql += " decode(p.PATIENT_SEX, 1, '男', 2, '女', '未知') as sex,";
					 }
					 if(StringUtils.isNotBlank(dimensionVO.getYears())){
						 sql += " substr(to_char(t.reg_date, 'yyyy'), 1, 4) as years,";
					 }
              sql += " count(1) as sumPeople,"+
                     " sum(decode(t.DEL_FLG, '0', 1, 0)) as ordinaryPeople,"+
                     " sum(decode(t.DEL_FLG, '1', 1, 0)) as emerGencyPeople from T_REGISTER_MAIN t"+
                     " left join T_PATIENT p on t.card_no = p.card_no"+
                     " left JOIN T_DEPARTMENT o on t.dept_name = o.DEPT_NAME where 1=1 ";
                    
                     if(StringUtils.isNotBlank(dimensionVO.getDept())){
                    	 sql +=" and t.dept_name in ('"+dimensionVO.getDept()+"') ";
                     }
                     if(StringUtils.isNotBlank(dimensionVO.getSex())){
						 sql += "  and p.PATIENT_SEX in (1,2)";
					 }
                     if(StringUtils.isNotBlank(dimensionVO.getYears())){
						 sql += " and to_char(t.reg_date, 'yyyy') >= '2011' and to_char(t.reg_date, 'yyyy') <= '2014'";
					 }
               sql +=" group by";
	                 if(StringUtils.isNotBlank(dimensionVO.getDept())){
	              	     sql +="  o.DEPT_NAME, ";
	                 }
	                 if(StringUtils.isNotBlank(dimensionVO.getSex())){
						 sql += "  p.PATIENT_SEX,";
				     }
	                 if(StringUtils.isNotBlank(dimensionVO.getYears())){
						 sql += " substr(to_char(t.reg_date, 'yyyy'), 1, 4),";
					 }
	           sql +=" 1 order by";
	           		 if(StringUtils.isNotBlank(dimensionVO.getDept())){
	           			 sql +="  o.DEPT_NAME, ";
	           		 }
	           		 if(StringUtils.isNotBlank(dimensionVO.getSex())){
	           			 sql += "  p.PATIENT_SEX,";
	           		 }
	           		if(StringUtils.isNotBlank(dimensionVO.getYears())){
					     sql += " substr(to_char(t.reg_date, 'yyyy'), 1, 4),";
	           		 }
	           sql +=" 1 ) e on 1=1 ";
	         		 if(StringUtils.isNotBlank(dimensionVO.getDept())){
	         			 sql +=" and  d.dept =e.dept  ";
	         		 }
	         		 if(StringUtils.isNotBlank(dimensionVO.getSex())){
	         			 sql += "  and  d.sex = e.sex";
	         		 }
	         		 if(StringUtils.isNotBlank(dimensionVO.getYears())){
				         sql += " and d.years = e.years";
	           		 }
		SQLQuery queryObject = this.getSession().createSQLQuery(sql);
					 if(StringUtils.isNotBlank(dimensionVO.getDept())){
						 queryObject.addScalar("dept");
					 }
					 if(StringUtils.isNotBlank(dimensionVO.getSex())){
						 queryObject.addScalar("sex");
					 }
					 if(StringUtils.isNotBlank(dimensionVO.getYears())){
						 queryObject.addScalar("years");
			  		 }
					 queryObject.addScalar("sumPeople",Hibernate.INTEGER).addScalar("ordinaryPeople",Hibernate.INTEGER).addScalar("emerGencyPeople",Hibernate.INTEGER);
		List<DimensionVO> infoVoList = queryObject.setResultTransformer(Transformers.aliasToBean(DimensionVO.class)).list();
	    if(infoVoList!=null&&infoVoList.size()>0){
	    	return infoVoList;
	    }
	    return new ArrayList<DimensionVO>();
	}

	@Override
	public List<District> findDistrict() {
		String hql = " from District where  level = 1";
		List<District> districtList = super.find(hql, null);
		if(districtList==null||districtList.size()<=0){
			return new ArrayList<District>();
		}
		return districtList;
	}

	@Override
	public List<SysDepartment> findAllDept() {
		String hql = " from SysDepartment where  1 = 1 and deptIsforregister = 1";
		List<SysDepartment> districtList = super.find(hql, null);
		if(districtList==null||districtList.size()<=0){
			return new ArrayList<SysDepartment>();
		}
		return districtList;
	}

	@Override
	public List<DimensionVO> queryPassengersoadDatagrid(String[] diArrayKey,List<Map<String, List<String>>> list, int dateType, DateVo datevo) {
		//查询时间
		StringBuffer sql=new StringBuffer();//sql语句的StringBuffer对象
		StringBuffer order=new StringBuffer();//最后order by 的顺序（即：选择的维度顺序）StringBuffer对象
		sql.append(" select ");
		//遍历数组，去匹配所选择的维度拼接sql和order
		for(int i=0;i<diArrayKey.length;i+=2){
			if("dept_code".equals(diArrayKey[i])){
				sql.append(" t.dept_code as dept");
				order.append(diArrayKey[i]);
			}
			if("patient_sex".equals(diArrayKey[i])){
				sql.append(" to_char(t.patient_sex) as sex");
				order.append("t.");
				order.append(diArrayKey[i]);
			}
			if("patient_birthplace".equals(diArrayKey[i])){
				sql.append(" p.patient_birthplace as region");
				order.append(diArrayKey[i]);
			}
			if("patient_age".equals(diArrayKey[i])){
				sql.append(" t.patient_age as age");
				sql.append(" ,t.patient_ageunit as ageUnit");
				order.append("t.");
				order.append(diArrayKey[i]);
				order.append(",t.patient_ageunit");
			}
			//当拼接的不是最后一个查询维度时，order后面拼接一个","（最后一个order条件不需要添加","）
			if(i!=diArrayKey.length-2){
				order.append(",");
			}
			sql.append(",");
		}
		sql.append(" count(1) as sumPeople, ");
		sql.append(" sum(decode(t.emergency_flag, '0', 1, 0)) as ordinaryPeople, ");
		sql.append("  sum(decode(t.emergency_flag, '1', 1, 0)) as emerGencyPeople, ");
		if(dateType==1){
			sql.append("         to_char(t.reg_date,'yyyy') as years");
		}else if(dateType==2){
			sql.append("        to_char(to_char(t.reg_date,'yyyy')||'/'||to_char(t.reg_date,'q')) as years ");
		}else if(dateType==3){
			sql.append("         to_char(t.reg_date,'yyyy/mm') as years");
		}else if(dateType==4){
			sql.append("         to_char(t.reg_date,'yyyy/mm/dd') as years");
		}
		sql.append(" from view_register_main t  left join T_PATIENT p on t.MEDICALRECORDID = p.MEDICALRECORD_ID   where 1 = 1 and t.trans_type = 1 ");
		if(dateType==1){
			sql.append(" and   to_char(t.reg_date,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'");
		}else if(dateType==2){
			sql.append(" and   to_char(t.reg_date,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'   ");
			sql.append(" and to_char(t.reg_date,'q') between '"+datevo.getQuarter1()+"' and '"+datevo.getQuarter2()+"'   ");
		}else if(dateType==3){
			sql.append(" and   to_char(t.reg_date,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'   ");
			sql.append(" and   to_char(t.reg_date,'mm') between '"+(datevo.getMonth1()>9?datevo.getMonth1():"0"+datevo.getMonth1())+"' and '"+(datevo.getMonth2()>9?datevo.getMonth2():"0"+datevo.getMonth2())+"'");
		}else if(dateType==4){
			sql.append(" and   to_char(t.reg_date,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'   ");
			sql.append(" and   to_char(t.reg_date,'mm') between '"+(datevo.getMonth1()>9?datevo.getMonth1():"0"+datevo.getMonth1())+"' and '"+(datevo.getMonth2()>9?datevo.getMonth2():"0"+datevo.getMonth2())+"'");
			sql.append(" and   to_char(t.reg_date,'dd') between '"+(datevo.getDay1()>9?datevo.getDay1():"0"+datevo.getDay1())+"' and '"+(datevo.getDay2()>9?datevo.getDay2():"0"+datevo.getDay2())+"'");
		}
		int a = 0;
		for(int i=0;i<diArrayKey.length;i+=2){
			
			sql.append(" and ");
			if("dept_code".equals(diArrayKey[i])){
				String strings = list.get(a).get(diArrayKey[i]).toString().replace(",", "','").replace(" ","").replace("[","").replace("]","");
				sql.append(" t.dept_code in  ('"+strings+"')");
			}
			if("patient_sex".equals(diArrayKey[i])){
				String strings = list.get(a).get(diArrayKey[i]).toString().replace(",", "','").replace(" ","").replace("[","").replace("]","");
				sql.append(" t.patient_sex  in ('"+strings+"')");
			}
			if("patient_birthplace".equals(diArrayKey[i])){
				String strings = list.get(a).get(diArrayKey[i]).toString().replace(",", "','").replace(" ","").replace("[","").replace("]","");
				sql.append(" p.patient_birthplace in  ('"+strings+"')");
			}
			if("patient_age".equals(diArrayKey[i])){
				for(int n=0;n<list.get(a).get(diArrayKey[i]).size();n++){
					if(n==0){
						String age=list.get(i/2).get(diArrayKey[i]).get(n);
						String agenum=age.substring(0,age.length()-1);
						String agennit=age.substring(age.length()-1,age.length());
						if(agenum.contains("-"))	{
							String []ageArr=agenum.split("-");
							sql.append(" (t.patient_age between '"+ageArr[0]+"' and '"+ageArr[1]+"'  and t.patient_ageunit = '"+agennit+"')");
						}else{
							sql.append(" (t.patient_age = '"+agenum+"' and t.patient_ageunit = '"+agennit+"' )");
						}
					}else{
						String age=list.get(i/2).get(diArrayKey[i]).get(n);
						String agenum=age.substring(0,age.length()-1);
						String agennit=age.substring(age.length()-1,age.length());
						if(agenum.contains("-"))	{
							String []ageArr=agenum.split("-");
							sql.append(" or (t.patient_age between '"+ageArr[0]+"' and '"+ageArr[1]+"'  and t.patient_ageunit = '"+agennit+"')");
						}else{
							sql.append(" or (t.patient_age = '"+agenum+"' and t.patient_ageunit = '"+agennit+"' )");
						}
					}
				}
			}
			a++;
		}
		sql.append("	 group by  ");
		sql.append(order.toString());
		sql.append(",");
		//对于时间的排序放在最后 
		if(dateType==1){
			sql.append("         to_char(t.reg_date,'yyyy') ");
		}else if(dateType==2){
			sql.append("         to_char(t.reg_date,'yyyy') ");
			sql.append("        ,to_char(t.reg_date,'q')   ");
		}else if(dateType==3){
			sql.append("         to_char(t.reg_date,'yyyy/mm') ");
		}else if(dateType==4){
			sql.append("         to_char(t.reg_date,'yyyy/mm/dd')");
		}
		sql.append("	 order by ");
		sql.append(order.toString());
		sql.append(",");
		//对于时间的排序放在最后
		if(dateType==1){
			sql.append("         to_char(t.reg_date,'yyyy') ");
		}else if(dateType==2){
			sql.append("         to_char(to_char(t.reg_date,'yyyy')||'/'||to_char(t.reg_date,'q'))   ");
		}else if(dateType==3){
			sql.append("         to_char(t.reg_date,'yyyy/mm') ");
		}else if(dateType==4){
			sql.append("         to_char(t.reg_date,'yyyy/mm/dd')");
		}
		SQLQuery queryObject1=this.getSession().createSQLQuery(sql.toString());
		for(int i=0;i<diArrayKey.length;i+=2){
			if("dept_code".equals(diArrayKey[i])){
				queryObject1.addScalar("dept");
			}
			if("patient_sex".equals(diArrayKey[i])){
				queryObject1.addScalar("sex");
			}
			if("patient_birthplace".equals(diArrayKey[i])){
				queryObject1.addScalar("region");
			}
			if("patient_age".equals(diArrayKey[i])){
				queryObject1.addScalar("age");
				queryObject1.addScalar("ageUnit");
			}
		}
		queryObject1.addScalar("sumPeople",Hibernate.INTEGER).addScalar("ordinaryPeople",Hibernate.INTEGER).addScalar("emerGencyPeople",Hibernate.INTEGER).addScalar("years");
		List<DimensionVO> bdl = queryObject1.setResultTransformer(Transformers.aliasToBean(DimensionVO.class)).list();
		if(bdl!=null){
			return bdl;
		}
		return new ArrayList<DimensionVO>();
	}

	@Override
	public List<BIBaseDistrict> queryCity() {
		String hql =" from BIBaseDistrict where level = 1";
		List<BIBaseDistrict> districtList = super.find(hql, null);
		if(districtList==null||districtList.size()<=0){
			return new ArrayList<BIBaseDistrict>();
		}
		return districtList;
	}
}
