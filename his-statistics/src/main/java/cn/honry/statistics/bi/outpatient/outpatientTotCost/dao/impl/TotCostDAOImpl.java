package cn.honry.statistics.bi.outpatient.outpatientTotCost.dao.impl;

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

import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.statistics.bi.outpatient.outpatientPassengers.vo.DimensionVO;
import cn.honry.statistics.bi.outpatient.outpatientTotCost.dao.TotCostDAO;
import cn.honry.statistics.bi.outpatient.outpatientTotCost.vo.TotCostDimension;
import cn.honry.statistics.doctor.regisdocscheinfo.vo.RegisDocScheInfoVo;
import cn.honry.statistics.util.dateVo.DateVo;

@Repository("totCostDAO")
@SuppressWarnings({"all"})
public class TotCostDAOImpl extends HibernateEntityDao<RegisDocScheInfoVo> implements TotCostDAO{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<DimensionVO> findDimensionList(DimensionVO dimensionVO) {
		String sql = "select ";
			if(StringUtils.isNotBlank(dimensionVO.getDept())){
				sql += " gs.dept as dept,";
			}
			if(StringUtils.isNotBlank(dimensionVO.getCodeName())){
				sql += "  gs.codename as codename,";
			}
			if(StringUtils.isNotBlank(dimensionVO.getYears())){
				sql += " gs.years as years,";
			}
		sql += " decode(sj.totcost, null, 0, sj.totcost) as sumPeople," +
			   " decode(sj.sumtotcost, null, 0||'%', sj.sumtotcost) as sumtotcosts"+
			   " from ( select ";
			 if(StringUtils.isNotBlank(dimensionVO.getDept())){
				 sql += " o1.dept,";
			 }
			 if(StringUtils.isNotBlank(dimensionVO.getCodeName())){
				 sql += " c1.codeName,";
			 }
			 if(StringUtils.isNotBlank(dimensionVO.getYears())){
				 sql += " d1.years,";
			 }
		sql +=" 1 from ( select 1 from dual k ) full join";
			 if(StringUtils.isNotBlank(dimensionVO.getDept())){
				 sql +=  "  (select o.org_name as dept from bi_base_organization o where o.org_code in ('1201501','2100101') group by o.org_name order by o.org_name ) o1 on 1 = 1 full join ";
			 }
			 if(StringUtils.isNotBlank(dimensionVO.getCodeName())){
				 sql +=	 "  (select c.code_name as codeName from bi_base_dictionary c where c.code_type='casminfee' and c.code_encode in ('01','07','10') group by c.code_name order by c.code_name ) c1 on 1=1 full join";
			 }
			 if(StringUtils.isNotBlank(dimensionVO.getYears())){
				 sql +=  "  (select d.year as years from bi_date d where d.year in ('2014','2015','2016') group by d.year order by d.year) d1 on 1=1 full join ";
			 }
		sql +=  " ( select 1 from dual k ) on 1=1  ) gs left join (select ";
			 if(StringUtils.isNotBlank(dimensionVO.getDept())){
				 sql += " a.org_name as dept,";
			 }
			 if(StringUtils.isNotBlank(dimensionVO.getCodeName())){
				 sql += " b.code_name as codeName,";
			 }
			 if(StringUtils.isNotBlank(dimensionVO.getYears())){
				 sql += " to_char(t.reg_date,'yyyy') as years,";
			 }
		sql +=   " sum(t.tot_cost) as totCost,"+
                 " round( (sum(t.tot_cost)/(select sum(t1.tot_cost)  from v_outpatient_feedetail t1 where to_char(t1.reg_date,'yyyy') = to_char(t.reg_date,'yyyy') and t1.trans_type = 1 and t1.pay_flag = 1 and t1.cancel_flag = 1 ) )*100,2)||'%' sumtotcost"+
                 " from v_outpatient_feedetail t"+
                 " left join bi_base_organization a on t.doct_dpcd = a.org_code"+
                 " left join bi_base_dictionary b on t.invo_code = b.code_encode where 1=1 and t.trans_type = 1  and t.pay_flag = 1  and t.cancel_flag = 1 and b.code_type='casminfee' ";
			 if(StringUtils.isNotBlank(dimensionVO.getDept())){
				 sql += " and a.org_code in ('1201501','2100101') ";
			 }
			 if(StringUtils.isNotBlank(dimensionVO.getCodeName())){
				 sql += "  and b.code_encode in ('01','07','10')";
			 }
			 if(StringUtils.isNotBlank(dimensionVO.getYears())){
				 sql += " and to_char(t.reg_date,'yyyy') in ('2014','2015','2016') ";
			 }
		sql += " group by  ";
			  if(StringUtils.isNotBlank(dimensionVO.getDept())){
	       	     sql +="  a.org_name, ";
	          }
	          if(StringUtils.isNotBlank(dimensionVO.getCodeName())){
				 sql += "  b.code_name,";
			  }
	          if(StringUtils.isNotBlank(dimensionVO.getYears())){
				 sql += " to_char(t.reg_date,'yyyy'),";
	          }
        sql +=" 1 order by";
    		 if(StringUtils.isNotBlank(dimensionVO.getDept())){
    			 sql +="  a.org_name, ";
    		 }
    		 if(StringUtils.isNotBlank(dimensionVO.getCodeName())){
    			 sql += "  b.code_name,";
    		 }
    		 if(StringUtils.isNotBlank(dimensionVO.getYears())){
			     sql += " to_char(t.reg_date,'yyyy') ,";
    		 }
    	sql +=" 1 ) sj on 1=1 ";
	    	 if(StringUtils.isNotBlank(dimensionVO.getDept())){
	      	   sql +=" and  gs.dept = sj.dept  ";
	 		   }
	 		   if(StringUtils.isNotBlank(dimensionVO.getCodeName())){
	 			   sql += "  and gs.codename = sj.codename";
	 		   }
	 		   if(StringUtils.isNotBlank(dimensionVO.getYears())){
	 			   sql += " and   gs.years = sj.years";
	 		   }
 		sql +="  order by";
       		 if(StringUtils.isNotBlank(dimensionVO.getDept())){
       			 sql +="  gs.dept , ";
       		 }
       		 if(StringUtils.isNotBlank(dimensionVO.getCodeName())){
       			 sql += " gs.codename,";
       		 }
       		 if(StringUtils.isNotBlank(dimensionVO.getYears())){
			     sql += " gs.years,";
       		 }
       	sql +="1";
       	SQLQuery queryObject = this.getSession().createSQLQuery(sql);
			 if(StringUtils.isNotBlank(dimensionVO.getDept())){
				 queryObject.addScalar("dept");
			 }
			 if(StringUtils.isNotBlank(dimensionVO.getCodeName())){
				 queryObject.addScalar("codeName");
			 }
			 if(StringUtils.isNotBlank(dimensionVO.getYears())){
				 queryObject.addScalar("years");
	 		 }
			 queryObject.addScalar("sumPeople",Hibernate.INTEGER).addScalar("sumtotcosts");
				List<DimensionVO> infoVoList = queryObject.setResultTransformer(Transformers.aliasToBean(DimensionVO.class)).list();
			    if(infoVoList!=null&&infoVoList.size()>0){
			    	return infoVoList;
			    }
			 return new ArrayList<DimensionVO>();
	}

	@Override
	public List<TotCostDimension> querytWordloadDatagrid(String[] diArrayKey, List<Map<String, List<String>>> list,int dateType, DateVo datevo) {
		//查询时间
		StringBuffer sql=new StringBuffer();//sql语句的StringBuffer对象
		StringBuffer order=new StringBuffer();//最后order by 的顺序（即：选择的维度顺序）StringBuffer对象
		sql.append(" select ");
		//遍历数组，去匹配所选择的维度拼接sql和order
		for(int i=0;i<diArrayKey.length;i+=2){
			if("reg_dpcd".equals(diArrayKey[i])){
				sql.append(" t.reg_dpcd as dept");	
				order.append(diArrayKey[i]);
			}
			if("invo_code".equals(diArrayKey[i])){
				sql.append(" t.invo_code as feeName");
				order.append(diArrayKey[i]);
			}
			if("doct_code".equals(diArrayKey[i])){
				sql.append(" t.doct_code as emp");
				order.append(diArrayKey[i]);
			}
			//当拼接的不是最后一个查询维度时，order后面拼接一个","（最后一个order条件不需要添加","）
			if(i!=diArrayKey.length-2){
				order.append(",");
			}
			sql.append(",");
		}
		sql.append(" sum(t.tot_cost) as sumCost, ");
		sql.append(" round( (sum(t.tot_cost)/ (select sum(t1.tot_cost)  from v_outpatient_feedetail t1 where  ");
		
		if(dateType==1){
			sql.append("         to_char(t1.reg_date,'yyyy') = to_char(t.reg_date,'yyyy')");
		}else if(dateType==2){
			sql.append("         to_char(t1.reg_date, 'q') = to_char(t.reg_date, 'q')");
		}else if(dateType==3){
			sql.append("         to_char(t1.reg_date,'yyyy') = to_char(t.reg_date,'yyyy/mm')");
		}else if(dateType==4){
			sql.append("         to_char(t1.reg_date,'yyyy') = to_char(t.reg_date,'yyyy/mm/dd')");
		}
		sql.append(" and t1.trans_type = 1 and t1.pay_flag = 1 and t1.cancel_flag = 1 ) )*100,2 )||'%' as proportion , ");
		
		
		if(dateType==1){
			sql.append("         to_char(t.reg_date,'yyyy') as years");
		}else if(dateType==2){
			sql.append("        to_char(to_char(t.reg_date,'yyyy')||'/'||to_char(t.reg_date,'q')) as years ");
		}else if(dateType==3){
			sql.append("         to_char(t.reg_date,'yyyy/mm') as years");
		}else if(dateType==4){
			sql.append("         to_char(t.reg_date,'yyyy/mm/dd') as years");
		}
		sql.append("     from v_outpatient_feedetail t  ");
		sql.append("	where 1=1 and t.trans_type = 1 and t.pay_flag = 1 and t.cancel_flag = 1   ");
		if(dateType==1){
			sql.append(" and  to_char(t.reg_date,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'");
		}else if(dateType==2){
			sql.append(" and to_char(t.reg_date,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'   ");
			sql.append(" and to_char(t.reg_date,'q') between '"+datevo.getQuarter1()+"' and '"+datevo.getQuarter2()+"'   ");
		}else if(dateType==3){
			sql.append(" and  to_char(t.reg_date,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'   ");
			sql.append(" and  to_char(t.reg_date,'mm') between '"+(datevo.getMonth1()>9?datevo.getMonth1():"0"+datevo.getMonth1())+"' and '"+(datevo.getMonth2()>9?datevo.getMonth2():"0"+datevo.getMonth2())+"'");
		}else if(dateType==4){
			sql.append(" and  to_char(t.reg_date,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'   ");
			sql.append(" and  to_char(t.reg_date,'mm') between '"+(datevo.getMonth1()>9?datevo.getMonth1():"0"+datevo.getMonth1())+"' and '"+(datevo.getMonth2()>9?datevo.getMonth2():"0"+datevo.getMonth2())+"'");
			sql.append(" and  to_char(t.reg_date,'dd') between '"+(datevo.getDay1()>9?datevo.getDay1():"0"+datevo.getDay1())+"' and '"+(datevo.getDay2()>9?datevo.getDay2():"0"+datevo.getDay2())+"'");
		}
		int a = 0;
		for(int i=0;i<diArrayKey.length;i+=2){
			
			sql.append(" and ");
			if("reg_dpcd".equals(diArrayKey[i])){
				String strings = list.get(a).get(diArrayKey[i]).toString().replace(",", "','").replace(" ","").replace("[","").replace("]","");
				sql.append(" t.reg_dpcd  in ('"+strings+"')");
			}
			if("invo_code".equals(diArrayKey[i])){
				String strings = list.get(a).get(diArrayKey[i]).toString().replace(",", "','").replace(" ","").replace("[","").replace("]","");
				sql.append(" t.invo_code in ('"+strings+"')");
			}
			if("doct_code".equals(diArrayKey[i])){
				String strings = list.get(a).get(diArrayKey[i]).toString().replace(",", "','").replace(" ","").replace("[","").replace("]","");
				sql.append(" t.doct_code in ('"+strings+"')");
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
			sql.append("         to_char(t.reg_date,'yyyy')  ");
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
			if("reg_dpcd".equals(diArrayKey[i])){
				queryObject1.addScalar("dept");
			}
			if("doct_code".equals(diArrayKey[i])){
				queryObject1.addScalar("emp");
			}
			if("invo_code".equals(diArrayKey[i])){
				queryObject1.addScalar("feeName");
			}
			
		}
		queryObject1.addScalar("sumCost",Hibernate.DOUBLE).addScalar("proportion").addScalar("years");
		List<TotCostDimension> bdl = queryObject1.setResultTransformer(Transformers.aliasToBean(TotCostDimension.class)).list();
		if(bdl!=null){
			return bdl;
		}
		return new ArrayList<TotCostDimension>();
	}
}
