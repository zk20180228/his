package cn.honry.statistics.bi.inpatient.settlementInHospital.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BiInpatientBalancelist;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.statistics.bi.inpatient.expensesAnaly.vo.ExpensesAnalyVO;
import cn.honry.statistics.bi.inpatient.settlementInHospital.dao.SettlementInHospitalDAO;
import cn.honry.statistics.util.dateVo.DateVo;

@Repository("settlementInHospitalDAO")
@SuppressWarnings({"all"})
public class SettlementInHospitalDAOImpl extends HibernateEntityDao<BiInpatientBalancelist> implements SettlementInHospitalDAO{
	
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Override
	public List<ExpensesAnalyVO> querytInHospitalDatagrid(String[] diArrayKey,
			List<Map<String, List<String>>> list, int dateType, DateVo datevo) {
		//天数
				int num=365;
				String sqldate=null;
				//查询时间
				StringBuffer sql=new StringBuffer();//sql语句的StringBuffer对象
				StringBuffer order=new StringBuffer();//最后order by 的顺序（即：选择的维度顺序）StringBuffer对象
			    sql.append(" select ");
				//遍历数组，去匹配所选择的维度拼接sql和order
				for(int i=0;i<diArrayKey.length;i+=2){
					if("inhos_deptcode".equals(diArrayKey[i])){
						sql.append(" t.inhos_deptcode as deptDimensionality");
						order.append(diArrayKey[i]);
					}
					if("report_sex".equals(diArrayKey[i])){
						sql.append(" aa.report_sex as sex");
						order.append(diArrayKey[i]);
					}
					if("fee_code".equals(diArrayKey[i])){
						sql.append(" t.fee_code as codeName");
						order.append(diArrayKey[i]);
					}
					//当拼接的不是最后一个查询维度时，order后面拼接一个","（最后一个order条件不需要添加","）
					if(i!=(diArrayKey.length-2)){
						order.append(",");
					}
					sql.append(",");
				}
				sql.append("	         sum(t.tot_cost) as totCost, ");
				sql.append("	       round((sum(t.tot_cost))/(select sum(t1.tot_cost) from t_inpatient_feeinfo t1 where t1.trans_type = 1 and ");
				if(dateType==1){
					sql.append(" to_char(t1.fee_date,'yyyy')= to_char(t.fee_date,'yyyy'))*100,2) " );
				}else if(dateType==2){
					sql.append(" to_char(t1.fee_date,'q')= to_char(t.fee_date,'q'))*100,2) " );
				}else if(dateType==3){
					sql.append(" to_char(t1.fee_date,'yyyy/mm')= to_char(t.fee_date,'yyyy/mm'))*100,2) " );
				}else if(dateType==4){
					sql.append(" to_char(t1.fee_date,'yyyy/mm/dd')= to_char(t.fee_date,'yyyy/mm/dd'))*100,2) " );
				}
				sql.append("as expensesProportion, ");
				if(dateType==1){
					sql.append("         to_char(t.fee_date,'yyyy') as timeChose");
				}else if(dateType==2){
					sql.append("         to_char(to_char(t.fee_date,'yyyy')||'/'||to_char(t.fee_date,'q')) as timeChose");
				}else if(dateType==3){
					sql.append("         to_char(t.fee_date,'yyyy/mm') as timeChose");
				}else if(dateType==4){
					sql.append("         to_char(t.fee_date,'yyyy/mm/dd') as timeChose");
				}
				if(dateType==1){
					sql.append(", trunc((sum(t.tot_cost)/(select sum(ss.TOT_COST) from view_inpatient_feeinfo ss "
							+ "left join view_charge_minfeetostat mm on mm.MINFEE_CODE=ss.FEE_CODE "
							+ "left join view_inpatient_info qq on qq.inpatient_no = ss.inpatient_no "
							+ "where to_number((replace(to_char(ss.FEE_DATE, 'yyyy'),'/',''))) = "
							+ "to_number(replace((to_char(t.FEE_DATE, 'yyyy')),'/','')) - 1 "
							+ "and ss.trans_type = 1 and mm.report_code = 'ZY01' and qq.in_state<>'O' ");
					int a = 0;
					//遍历数组，添加查询条件（匹配所选择的维度拼接）
					for(int i=0;i<diArrayKey.length;i+=2){
						if("inhos_deptcode".equals(diArrayKey[i])){
							String strings = list.get(a).get(diArrayKey[i]).toString().replace(",", "','").replace(" ","").replace("[","").replace("]","");
							sql.append(" and ss.inhos_deptcode  in ('"+strings+"')");
						}
						if("fee_code".equals(diArrayKey[i])){
							String strings = list.get(a).get(diArrayKey[i]).toString().replace(",", "','").replace(" ","").replace("[","").replace("]","");
							sql.append(" and ss.fee_code  in ('"+strings+"')");
						}
						if("report_sex".equals(diArrayKey[i])){
							String strings = list.get(a).get(diArrayKey[i]).toString().replace(",", "','").replace(" ","").replace("[","").replace("]","");
							sql.append(" and qq.report_sex in ('"+strings+"')");
						}
						a++;
					}
					sql.append("   )) , 2 )  *100    ");
				}else if(dateType==2){
					sql.append(", trunc((sum(t.tot_cost)/(select sum(ss.TOT_COST) from view_inpatient_feeinfo ss "
							+ " left join view_charge_minfeetostat mm on mm.MINFEE_CODE=ss.FEE_CODE "
							+ " left join view_inpatient_info qq on qq.inpatient_no = ss.inpatient_no"
							+ " where to_number((replace(to_char(ss.FEE_DATE, 'yyyy'),'/',''))) = "
							+ " to_number(replace((to_char(t.FEE_DATE, 'yyyy')),'/','')) - 1 "
							+ " and to_number((replace(to_char(ss.FEE_DATE, 'q'),'/',''))) = "
							+ " to_number(replace((to_char(t.FEE_DATE, 'q')),'/','')) "
							+ " and ss.trans_type = 1 and mm.report_code = 'ZY01'  and qq.in_state<>'O' ");
					int a = 0;
					//遍历数组，添加查询条件（匹配所选择的维度拼接）
					for(int i=0;i<diArrayKey.length;i+=2){
						if("inhos_deptcode".equals(diArrayKey[i])){
							String strings = list.get(a).get(diArrayKey[i]).toString().replace(",", "','").replace(" ","").replace("[","").replace("]","");
							sql.append(" and ss.inhos_deptcode  in ('"+strings+"')");
						}
						if("fee_code".equals(diArrayKey[i])){
							String strings = list.get(a).get(diArrayKey[i]).toString().replace(",", "','").replace(" ","").replace("[","").replace("]","");
							sql.append(" and ss.fee_code  in ('"+strings+"')");
						}
						if("report_sex".equals(diArrayKey[i])){
							String strings = list.get(a).get(diArrayKey[i]).toString().replace(",", "','").replace(" ","").replace("[","").replace("]","");
							sql.append(" and qq.report_sex in ('"+strings+"')");
						}
						a++;
					}
					sql.append("   )) , 2 )  *100    ");
				}else if(dateType==3){
					sql.append(", trunc((sum(t.tot_cost)/(select sum(ss.TOT_COST) from view_inpatient_feeinfo ss "
							+ " left join view_charge_minfeetostat mm on mm.MINFEE_CODE=ss.FEE_CODE "
							+ " left join view_inpatient_info qq on qq.inpatient_no = ss.inpatient_no"
							+ " where to_number((replace(to_char(ss.FEE_DATE, 'yyyy/mm'),'/',''))) = "
							+ " to_number(replace((to_char(t.FEE_DATE, 'yyyy/mm')),'/','')) - 100 "
							+ " and ss.trans_type = 1 and mm.report_code = 'ZY01' and qq.in_state<>'O' ");
					int a = 0;
					//遍历数组，添加查询条件（匹配所选择的维度拼接）
					for(int i=0;i<diArrayKey.length;i+=2){
						if("inhos_deptcode".equals(diArrayKey[i])){
							String strings = list.get(a).get(diArrayKey[i]).toString().replace(",", "','").replace(" ","").replace("[","").replace("]","");
							sql.append(" and ss.inhos_deptcode  in ('"+strings+"')");
						}
						if("fee_code".equals(diArrayKey[i])){
							String strings = list.get(a).get(diArrayKey[i]).toString().replace(",", "','").replace(" ","").replace("[","").replace("]","");
							sql.append(" and ss.fee_code  in ('"+strings+"')");
						}
						if("report_sex".equals(diArrayKey[i])){
							String strings = list.get(a).get(diArrayKey[i]).toString().replace(",", "','").replace(" ","").replace("[","").replace("]","");
							sql.append(" and qq.report_sex in ('"+strings+"')");
						}
						a++;
					}
					sql.append("   )) , 2 ) *100     ");
				}else if(dateType==4){
					sql.append(", trunc((sum(t.tot_cost)/(select sum(ss.TOT_COST) from view_inpatient_feeinfo ss "
							+ " left join view_charge_minfeetostat mm on mm.MINFEE_CODE=ss.FEE_CODE "
							+ " left join view_inpatient_info qq on qq.inpatient_no = ss.inpatient_no"
							+ " where to_number((replace(to_char(ss.FEE_DATE, 'yyyy/mm/dd'),'/',''))) = "
							+ " to_number(replace((to_char(t.FEE_DATE, 'yyyy/mm/dd')),'/','')) - 10000 "
							+ " and ss.trans_type = 1 and mm.report_code = 'ZY01'  and qq.in_state<>'O' ");
					int a = 0;
					//遍历数组，添加查询条件（匹配所选择的维度拼接）
					for(int i=0;i<diArrayKey.length;i+=2){
						if("inhos_deptcode".equals(diArrayKey[i])){
							String strings = list.get(a).get(diArrayKey[i]).toString().replace(",", "','").replace(" ","").replace("[","").replace("]","");
							sql.append(" and ss.inhos_deptcode  in ('"+strings+"')");
						}
						if("fee_code".equals(diArrayKey[i])){
							String strings = list.get(a).get(diArrayKey[i]).toString().replace(",", "','").replace(" ","").replace("[","").replace("]","");
							sql.append(" and ss.fee_code  in ('"+strings+"')");
						}
						if("report_sex".equals(diArrayKey[i])){
							String strings = list.get(a).get(diArrayKey[i]).toString().replace(",", "','").replace(" ","").replace("[","").replace("]","");
							sql.append(" and qq.report_sex in ('"+strings+"')");
						}
						a++;
					}
					sql.append("   )) , 2 )*100      ");
				}
				sql.append("  as rose ");
				sql.append("  from view_inpatient_feeinfo t left join view_charge_minfeetostat m on t.fee_code = m.minfee_code "
						+ "left join view_inpatient_info aa on aa.inpatient_no=t.inpatient_no");
				sql.append("	 where t.trans_type = 1 and m.report_code='ZY01' and aa.in_state<>'O' ");
				if(dateType==1){
					sql.append(" and to_char(t.fee_date,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'");
				}else if(dateType==2){
					sql.append(" and to_char(t.fee_date,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'   ");
					sql.append(" and to_char(t.fee_date,'q') between '"+datevo.getQuarter1()+"' and '"+datevo.getQuarter2()+"'");
				}else if(dateType==3){
					sql.append(" and to_char(t.fee_date,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'   ");
					sql.append(" and to_char(t.fee_date,'mm') between '"+(datevo.getMonth1()>9?datevo.getMonth1():"0"+datevo.getMonth1())+"' and '"+(datevo.getMonth2()>9?datevo.getMonth2():"0"+datevo.getMonth2())+"'");
				}else if(dateType==4){
					sql.append(" and to_char(t.fee_date,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'   ");
					sql.append(" and to_char(t.fee_date,'mm') between '"+(datevo.getMonth1()>9?datevo.getMonth1():"0"+datevo.getMonth1())+"' and '"+(datevo.getMonth2()>9?datevo.getMonth2():"0"+datevo.getMonth2())+"'");
					sql.append(" and to_char(t.fee_date,'dd') between '"+(datevo.getDay1()>9?datevo.getDay1():"0"+datevo.getDay1())+"' and '"+(datevo.getDay2()>9?datevo.getDay2():"0"+datevo.getDay2())+"'");
				}
				int a = 0;
				//遍历数组，添加查询条件（匹配所选择的维度拼接）
				for(int i=0;i<diArrayKey.length;i+=2){
					if("inhos_deptcode".equals(diArrayKey[i])){
						String strings = list.get(a).get(diArrayKey[i]).toString().replace(",", "','").replace(" ","").replace("[","").replace("]","");
						sql.append(" and t.inhos_deptcode  in ('"+strings+"')");
					}
					if("fee_code".equals(diArrayKey[i])){
						String strings = list.get(a).get(diArrayKey[i]).toString().replace(",", "','").replace(" ","").replace("[","").replace("]","");
						sql.append(" and t.fee_code  in ('"+strings+"')");
					}
					if("report_sex".equals(diArrayKey[i])){
						String strings = list.get(a).get(diArrayKey[i]).toString().replace(",", "','").replace(" ","").replace("[","").replace("]","");
						sql.append(" and aa.report_sex in ('"+strings+"')");
					}
					a++;
				}
				sql.append("	 group by  ");
				sql.append(order.toString());
				sql.append(",");
				//对于时间的排序放在最后 
				if(dateType==1){
					sql.append("         to_char(t.fee_date,'yyyy') ");
				}else if(dateType==2){
					sql.append("         to_char(t.fee_date,'yyyy') ");
					sql.append("         ,to_char(t.fee_date,'q') ");
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
					sql.append("         to_char(t.fee_date,'yyyy') ");
					sql.append("         ,to_char(t.fee_date,'q') ");
				}else if(dateType==3){
					sql.append("         to_char(t.fee_date,'yyyy/mm') ");
				}else if(dateType==4){
					sql.append("         to_char(t.fee_date,'yyyy/mm/dd')");
				}
				SQLQuery queryObject1=this.getSession().createSQLQuery(sql.toString());
				for(int i=0;i<diArrayKey.length;i+=2){
					if("inhos_deptcode".equals(diArrayKey[i])){
						queryObject1.addScalar("deptDimensionality");
					}
					if("report_sex".equals(diArrayKey[i])){
						queryObject1.addScalar("sex");
					}
					if("fee_code".equals(diArrayKey[i])){
						queryObject1.addScalar("codeName");
					}
				}
				queryObject1.addScalar("totCost",Hibernate.DOUBLE).addScalar("expensesProportion",Hibernate.BIG_DECIMAL).addScalar("timeChose").addScalar("rose",Hibernate.DOUBLE);
				List<ExpensesAnalyVO> bdl=bdl = queryObject1.setResultTransformer(Transformers.aliasToBean(ExpensesAnalyVO.class)).list();
				if(bdl!=null){
					return bdl;
				}
				return new ArrayList<ExpensesAnalyVO>();
	}
}
