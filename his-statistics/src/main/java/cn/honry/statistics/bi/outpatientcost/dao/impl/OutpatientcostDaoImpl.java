package cn.honry.statistics.bi.outpatientcost.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javassist.convert.Transformer;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BiOptFeedetail;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.statistics.bi.outpatient.outpatientWorkload.vo.OutpatientWorkloadVo;
import cn.honry.statistics.bi.outpatientcost.dao.OutpatientcostDao;
import cn.honry.statistics.bi.outpatientcost.vo.OutpatientcostVo;
import cn.honry.statistics.util.dateVo.DateVo;

@Repository("outpatientcostDao")
@SuppressWarnings({"all"})
public class OutpatientcostDaoImpl extends HibernateEntityDao<BiOptFeedetail> implements OutpatientcostDao{
		@Resource(name="sessionFactory")
		public void setSuperSessionFactory(SessionFactory sessionFactory) {
			super.setSessionFactory(sessionFactory);
		}

		/**
		 * @Description:页面加载时加载table
		 * @Author: zhangjin
		 * @CreateDate: 2016年7月15日
		 * @param:date：当前时间
		 * @return 
		 * @Modifier:
		 * @ModifyDate:
		 * @ModifyRmk:
		 * @version: 1.0
		 * --挂号表--收费明细--组织机构
		 */
//		@Override
//		public List<OutpatientcostVo> getOutpatientcostlist(String log,String end,String dept) {
//			StringBuffer str=new StringBuffer();
//			int logTime=Integer.parseInt(log);
//			int endTime=Integer.parseInt(end);
//			int num=logTime-endTime;
//					String sql="select count(*) as trips,"+
//					 "sum(gh.sum_cost) as regFee,"+
//					 "sum(gh.sum_cost)/sum( "+
//					    " fee.tot_cost + gh.sum_cost ) as   regRatio,"+
//					" sum(decode(fee.drug_flag, '2', fee.tot_cost, 0)) as undrugFee,"+
//					 "sum(decode(fee.drug_flag, '2', fee.tot_cost, 0)) /"+
//					 "sum( fee.tot_cost + gh.sum_cost )  as  undrugRatio,"+
//					" sum(decode(fee.drug_flag, '1', fee.tot_cost, 0)) as drugFee,"+
//					 "sum(decode(fee.drug_flag, '1', fee.tot_cost, 0))/"+
//					      "sum(fee.tot_cost + gh.sum_cost ) as drugRatio, "+
//					 "sum( fee.tot_cost + gh.sum_cost ) as totcost,"+
//					" sum(fee.tot_cost + gh.sum_cost )/ count(*) as avg, "+                          
//					 "gh.dept_name as dept,to_char(fee.fee_date,'yyyy') as dat "+
//
//					  " from bi_register          gh ,"+
//					       "bi_opt_feedetail     fee ,"+
//					      
//					       "bi_base_organization org "+
//					" where gh.trans_type = '1' "+
//					"and fee.pay_flag='1'"+
//					   " and gh.ynregchrg = '1' "+
//					   " and org.org_kind_code = 'C' "+
//					   " and gh.id_key = org.id_key"+
//					   " and gh.id_key=fee.id_key"+
//					   " and fee.id_key = org.id_key";
//					
//					if(StringUtils.isNotBlank(log)&&StringUtils.isNotBlank(end)){
//						sql+=" and to_char(fee.fee_date,'yyyy')>='"+log+"' "
//								+ "and to_char(fee.fee_date,'yyyy')<='"+end+"' ";
//					}
//					if(StringUtils.isNotBlank(dept)){
//						String dep=dept.replace(",", "','");
//						sql+=" and gh.dept_code in ('"+dep+"')";
//					}
//					
//					 sql+="group by gh.dept_name,to_char(fee.fee_date,'yyyy') order by to_char(fee.fee_date,'yyyy'), gh.dept_name";
//			SQLQuery query=this.getSession().createSQLQuery(sql).addScalar("trips",Hibernate.INTEGER).addScalar("regFee",Hibernate.DOUBLE).addScalar("regRatio",Hibernate.DOUBLE)
//					.addScalar("undrugFee",Hibernate.DOUBLE).addScalar("undrugRatio",Hibernate.DOUBLE).addScalar("drugFee",Hibernate.DOUBLE).addScalar("drugRatio",Hibernate.DOUBLE)
//					.addScalar("totcost",Hibernate.DOUBLE).addScalar("avg",Hibernate.DOUBLE).addScalar("dept").addScalar("dat");
//			List<OutpatientcostVo> outlist=query.setResultTransformer(Transformers.aliasToBean(OutpatientcostVo.class)).list();
//			if(outlist!=null&&outlist.size()>0){
//				return outlist;
//			}
//			return null;
//		}
		/**
		 * @Description:页面加载时加载table
		 * @Author: zhangjin
		 * @CreateDate: 2016年8月4日
		 * @param:date：当前时间
		 * @return 
		 * @Modifier:zhangjin
		 * @ModifyDate:2016-8-20
		 * @ModifyRmk:
		 * @version: 1.0
		 */
		@Override
		public List<OutpatientcostVo> getOutpatientfeelist(String[] dimStringArray, List<Map<String, List<String>>> list,
				int dateType, DateVo datevo) {
			//查询时间
			StringBuilder sql=new StringBuilder();//sql语句的StringBuffer对象
			StringBuilder order=new StringBuilder();//最后order by 的顺序（即：选择的维度顺序）StringBuffer对象
		    sql.append(" select ");
			//遍历数组，去匹配所选择的维度拼接sql和order
			for(int i=0;i<dimStringArray.length;i+=2){
				if("dept_code".equals(dimStringArray[i])){
					sql.append(" t.dept_code as deptDimensionality");
					order.append(dimStringArray[i]);
				}
				if("fee_stat_code".equals(dimStringArray[i])){
					sql.append(" t.fee_stat_code as feecode");
					order.append(dimStringArray[i]);
				}
				if("reglevl_code".equals(dimStringArray[i])){
					sql.append(" t.reglevl_code as regcode");
					order.append(dimStringArray[i]);
				}
				//当拼接的不是最后一个查询维度时，order后面拼接一个","（最后一个order条件不需要添加","）
				if(i!=(dimStringArray.length-2)){
					order.append(",");
				}
				sql.append(",");
			}
			sql.append("	     count(1) as trips,round(sum(t.tot_cost),2) as totcost,round(sum(t.tot_cost)/count(1),2) as travgcost, ");
			if(dateType==1){
				sql.append("         to_char(t.fee_date,'yyyy') as timeChose");
			}else if(dateType==2){
				sql.append("        to_char(to_char(t.fee_date,'yyyy')||'/'||to_char(t.fee_date,'q')) as timeChose ");
			}else if(dateType==3){
				sql.append("         to_char(t.fee_date,'yyyy/mm') as timeChose ");
			}else if(dateType==4){
				sql.append("         to_char(t.fee_date,'yyyy/mm/dd') as timeChose ");
			}
			sql.append(" from v_outpantient_feecost t");
			sql.append(" where ");
			if(dateType==1){
				sql.append("  to_char(t.fee_date,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'");
			}else if(dateType==2){
				sql.append("  to_char(t.fee_date,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'   ");
				sql.append(" and to_char(t.fee_date,'q') between '"+datevo.getQuarter1()+"' and '"+datevo.getQuarter2()+"'   ");
			}else if(dateType==3){
				sql.append("  to_char(t.fee_date,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'   ");
				sql.append(" and to_char(t.fee_date,'mm') between '"+(datevo.getMonth1()>9?datevo.getMonth1():"0"+datevo.getMonth1())+"' and '"+(datevo.getMonth2()>9?datevo.getMonth2():"0"+datevo.getMonth2())+"'");
			}else if(dateType==4){
				sql.append("  to_char(t.fee_date,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'   ");
				sql.append(" and to_char(t.fee_date,'mm') between '"+(datevo.getMonth1()>9?datevo.getMonth1():"0"+datevo.getMonth1())+"' and '"+(datevo.getMonth2()>9?datevo.getMonth2():"0"+datevo.getMonth2())+"'");
				sql.append(" and to_char(t.fee_date,'dd') between '"+(datevo.getDay1()>9?datevo.getDay1():"0"+datevo.getDay1())+"' and '"+(datevo.getDay2()>9?datevo.getDay2():"0"+datevo.getDay2())+"'");
			}
			int n=0;
			for(int i=0;i<dimStringArray.length;i+=2){
//				sql.append(" and ");
				if("dept_code".equals(dimStringArray[i])){
					StringBuilder value=new StringBuilder();
					for(int j=0;j<list.get(n).get(dimStringArray[i]).size();j++){
						if(!"".equals(value.toString())){
							value.append(",");
						}
						value.append("'"+list.get(n).get(dimStringArray[i]).get(j)+"'");
					}
					sql.append(" and t.dept_code  in ("+value.toString()+")");
				}
				if("fee_stat_code".equals(dimStringArray[i])){
					StringBuilder value=new StringBuilder();
					for(int j=0;j<list.get(n).get(dimStringArray[i]).size();j++){
						if(!"".equals(value.toString())){
							value.append(",");
						}
						value.append("'"+list.get(n).get(dimStringArray[i]).get(j)+"'");
					}
					sql.append("and t.fee_stat_code in ("+value.toString()+")");
				}
				if("reglevl_code".equals(dimStringArray[i])){
					StringBuilder value=new StringBuilder();
					for(int j=0;j<list.get(n).get(dimStringArray[i]).size();j++){
						if(!"".equals(value.toString())){
							value.append(",");
						}
						value.append("'"+list.get(n).get(dimStringArray[i]).get(j)+"'");
					}
					sql.append("and t.reglevl_code in ("+value.toString()+")");
				}
				n++;
			}
			sql.append(" group by ");
			sql.append(order.toString());
 			sql.append(",");
 			if(dateType==1){
				sql.append("         to_char(t.fee_date,'yyyy') ");
			}else if(dateType==2){
				sql.append("         to_char(t.fee_date,'yyyy')  ");
				sql.append("        ,to_char(t.fee_date,'q')   ");
			}else if(dateType==3){
				sql.append("         to_char(t.fee_date,'yyyy/mm') ");
			}else if(dateType==4){
				sql.append("         to_char(t.fee_date,'yyyy/mm/dd')");
			}
			sql.append("	 order by ");
			sql.append(order.toString());
			sql.append(",");
			//对于时间的排序放在最后
			if(dateType==1){
				sql.append("         to_char(t.fee_date,'yyyy') ");
			}else if(dateType==2){
				sql.append("         to_char(to_char(t.fee_date,'yyyy')||'/'||to_char(t.fee_date,'q'))   ");
			}else if(dateType==3){
				sql.append("         to_char(t.fee_date,'yyyy/mm') ");
			}else if(dateType==4){
				sql.append("         to_char(t.fee_date,'yyyy/mm/dd')");
			}
			SQLQuery queryObject1=this.getSession().createSQLQuery(sql.toString());
			for(int i=0;i<dimStringArray.length;i+=2){
				if("dept_code".equals(dimStringArray[i])){
					queryObject1.addScalar("deptDimensionality");
				}
				if("fee_stat_code".equals(dimStringArray[i])){
					queryObject1.addScalar("feecode");
				}
				if("reglevl_code".equals(dimStringArray[i])){
					queryObject1.addScalar("regCode");
				}
				
			}
			queryObject1.addScalar("trips",Hibernate.INTEGER).addScalar("totcost",Hibernate.DOUBLE)
			.addScalar("travgcost",Hibernate.DOUBLE).addScalar("timeChose");
			List<OutpatientcostVo> bdl=bdl = queryObject1.setResultTransformer(Transformers.aliasToBean(OutpatientcostVo.class)).list();
			if(bdl!=null){
				return bdl;
			}
			return new ArrayList<OutpatientcostVo>();
			
		}
		

		/**
		 * @Description:页面加载时加载table
		 * @Author: zhangjin
		 * @CreateDate: 2016年7月15日
		 * @param:date：当前时间
		 * @return 
		 * @Modifier:
		 * @ModifyDate:
		 * @ModifyRmk:
		 * @version: 1.0
		 * --挂号表--收费明细--组织机构
		 */
		@Override
		public List<OutpatientcostVo> getOutpatientcostlist(
				String[] dimStringArray, List<Map<String, List<String>>> list,
				int dateType, DateVo datevo) {
			//查询时间
			StringBuilder sql=new StringBuilder();//sql语句的StringBuffer对象
			StringBuilder order=new StringBuilder();//最后order by 的顺序（即：选择的维度顺序）StringBuffer对象
		    sql.append(" select ");
			//遍历数组，去匹配所选择的维度拼接sql和order
			for(int i=0;i<dimStringArray.length;i+=2){
				if("dept_code".equals(dimStringArray[i])){
					sql.append(" t.dept_code as deptDimensionality");
					order.append(dimStringArray[i]);
				}
				if("reglevl_code".equals(dimStringArray[i])){
					sql.append(" t.reglevl_code as regcode");
					order.append(dimStringArray[i]);
				}
				//当拼接的不是最后一个查询维度时，order后面拼接一个","（最后一个order条件不需要添加","）
				if(i!=(dimStringArray.length-2)){
					order.append(",");
				}
				sql.append(",");
			}
			sql.append("	      count(1) as trips,sum(t.sum_cost) as regFee,sum( t.tot_cost + t.sum_cost ) as totcost,"
					+ "  round(sum(t.sum_cost)/sum( t.tot_cost + t.sum_cost ),2) as regRatio, ");
			sql.append("	sum(decode(t.drug_flag, '2', t.tot_cost, 0)) as undrugFee, ");
			sql.append(" sum(decode(t.drug_flag, '2', t.tot_cost, 0)) /"
					+ "  sum( t.tot_cost + t.sum_cost )  as  undrugRatio, ");
			sql.append(" sum(decode(t.drug_flag, '1', t.tot_cost, 0)) as drugFee,"
					+ "  sum(decode(t.drug_flag, '1', t.tot_cost, 0))/"
					+ "  sum(t.tot_cost + t.sum_cost ) as drugRatio, ");
			sql.append(" round(sum(t.tot_cost + t.sum_cost )/ count(1),2) as avg,");
			if(dateType==1){
				sql.append("         to_char(t.fee_date,'yyyy') as timeChose");
			}else if(dateType==2){
				sql.append("        to_char(to_char(t.fee_date,'yyyy')||'/'||to_char(t.fee_date,'q')) as timeChose ");
			}else if(dateType==3){
				sql.append("         to_char(t.fee_date,'yyyy/mm') as timeChose ");
			}else if(dateType==4){
				sql.append("         to_char(t.fee_date,'yyyy/mm/dd') as timeChose ");
			}
			sql.append(" from v_outpantient_feecost t  ");
			sql.append(" where ");
			
			if(dateType==1){
				sql.append("  to_char(t.fee_date,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'");
			}else if(dateType==2){
				sql.append("  to_char(t.fee_date,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'   ");
				sql.append(" and to_char(t.fee_date,'q') between '"+datevo.getQuarter1()+"' and '"+datevo.getQuarter2()+"'   ");
			}else if(dateType==3){
				sql.append("  to_char(t.fee_date,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'   ");
				sql.append(" and to_char(t.fee_date,'mm') between '"+(datevo.getMonth1()>9?datevo.getMonth1():"0"+datevo.getMonth1())+"' and '"+(datevo.getMonth2()>9?datevo.getMonth2():"0"+datevo.getMonth2())+"'");
			}else if(dateType==4){
				sql.append("  to_char(t.fee_date,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'   ");
				sql.append(" and to_char(t.fee_date,'mm') between '"+(datevo.getMonth1()>9?datevo.getMonth1():"0"+datevo.getMonth1())+"' and '"+(datevo.getMonth2()>9?datevo.getMonth2():"0"+datevo.getMonth2())+"'");
				sql.append(" and to_char(t.fee_date,'dd') between '"+(datevo.getDay1()>9?datevo.getDay1():"0"+datevo.getDay1())+"' and '"+(datevo.getDay2()>9?datevo.getDay2():"0"+datevo.getDay2())+"'");
			}
//			sql.append("	   and  t.DATE_KEY in ("+dayKey+")");
			//遍历数组，添加查询条件（匹配所选择的维度拼接）
			int n=0;
			for(int i=0;i<dimStringArray.length;i+=2){
//				sql.append(" and ");
				if("dept_code".equals(dimStringArray[i])){
					StringBuilder value=new StringBuilder();
					for(int j=0;j<list.get(n).get(dimStringArray[i]).size();j++){
						if(!"".equals(value.toString())){
							value.append(",");
						}
						value.append("'"+list.get(n).get(dimStringArray[i]).get(j)+"'");
					}
					sql.append(" and t.dept_code  in ("+value.toString()+")");
				}
				if("reglevl_code".equals(dimStringArray[i])){
					StringBuilder value=new StringBuilder();
					for(int j=0;j<list.get(n).get(dimStringArray[i]).size();j++){
						if(!"".equals(value.toString())){
							value.append(",");
						}
						value.append("'"+list.get(n).get(dimStringArray[i]).get(j)+"'");
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
				sql.append("         to_char(t.fee_date,'yyyy') ");
			}else if(dateType==2){
				sql.append("         to_char(t.fee_date,'yyyy')  ");
				sql.append("        ,to_char(t.fee_date,'q')   ");
			}else if(dateType==3){
				sql.append("         to_char(t.fee_date,'yyyy/mm') ");
			}else if(dateType==4){
				sql.append("         to_char(t.fee_date,'yyyy/mm/dd')");
			}
			sql.append("	 order by ");
			sql.append(order.toString());
			sql.append(",");
			//对于时间的排序放在最后
			if(dateType==1){
				sql.append("         to_char(t.fee_date,'yyyy') ");
			}else if(dateType==2){
				sql.append("         to_char(to_char(t.fee_date,'yyyy')||'/'||to_char(t.fee_date,'q'))   ");
			}else if(dateType==3){
				sql.append("         to_char(t.fee_date,'yyyy/mm') ");
			}else if(dateType==4){
				sql.append("         to_char(t.fee_date,'yyyy/mm/dd')");
			}
			SQLQuery queryObject1=this.getSession().createSQLQuery(sql.toString());
			for(int i=0;i<dimStringArray.length;i+=2){
				if("dept_code".equals(dimStringArray[i])){
					queryObject1.addScalar("deptDimensionality");
				}
				if("fee_stat_code".equals(dimStringArray[i])){
					queryObject1.addScalar("feecode");
				}
				if("reglevl_code".equals(dimStringArray[i])){
					queryObject1.addScalar("regcode");
				}
				
			}
			queryObject1.addScalar("trips",Hibernate.INTEGER).addScalar("regFee",Hibernate.DOUBLE)
			.addScalar("regRatio",Hibernate.DOUBLE).addScalar("undrugFee",Hibernate.DOUBLE)
			.addScalar("undrugRatio",Hibernate.DOUBLE).addScalar("drugFee",Hibernate.DOUBLE).addScalar("drugRatio",Hibernate.DOUBLE)
			.addScalar("totcost",Hibernate.DOUBLE).addScalar("avg",Hibernate.DOUBLE).addScalar("timeChose")
			;
			List<OutpatientcostVo> bdl=bdl = queryObject1.setResultTransformer(Transformers.aliasToBean(OutpatientcostVo.class)).list();
			if(bdl!=null){
				return bdl;
			}
			return new ArrayList<OutpatientcostVo>();
		}
		
		
	
		
		
	
}
