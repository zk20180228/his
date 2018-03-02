package cn.honry.statistics.bi.operation.operationCostList.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.statistics.bi.operation.operationCostList.dao.OperationCostListDao;
import cn.honry.statistics.bi.operation.operationCostList.vo.OperationCostListvo;
import cn.honry.statistics.bi.outpatient.outpatientWorkload.vo.OutpatientWorkloadVo;
import cn.honry.statistics.util.dateVo.DateVo;
@Repository("operationCostListDao")
@SuppressWarnings({"all"})
public class OperationCostListDaoImpl extends HibernateEntityDao<OperationCostListvo> implements OperationCostListDao{
	
	@Resource(name="sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory  sessionFactory){
		super.setSessionFactory(sessionFactory);
	}

	/**
	 * 加载统计
	 * @author zhangjin
	 * @createDate：2016/8/16
	 * @version 1.0
	 */
	@Override
	public List<OperationCostListvo> queryOperationCost(
			String[] dimStringArray, List<Map<String, List<String>>> list,
			int dateType, DateVo datevo) {
		//查询时间
				StringBuilder sql=new StringBuilder();//sql语句的StringBuffer对象
				StringBuilder order=new StringBuilder();//最后order by 的顺序（即：选择的维度顺序）StringBuffer对象
			    sql.append(" select ");
				//遍历数组，去匹配所选择的维度拼接sql和order
				for(int i=0;i<dimStringArray.length;i+=2){
					if("ops_kind".equals(dimStringArray[i])){
						sql.append(" t.ops_kind as kind ");
						order.append(dimStringArray[i]);
					}
					//当拼接的不是最后一个查询维度时，order后面拼接一个","（最后一个order条件不需要添加","）
					if(i!=(dimStringArray.length-2)){
						order.append(",");
					}
					sql.append(",");
				}
				sql.append("	count(1) as operationnNum,d.personNum,sum(t.tot_cost) as feeCost,"
						+ "round(sum(t.tot_cost)/personNum,2) as personAvg ,round(sum(t.medcost)/sum(t.tot_cost),2) as drugzyb,  ");
				if(dateType==1){
					sql.append("         to_char(t.operationdate,'yyyy') as timeChose");
				}else if(dateType==2){
					sql.append(" to_char(t.operationdate,'yyyy')||'/'||to_char(t.operationdate,'q') as timeChose");
				}else if(dateType==3){
					sql.append("         to_char(t.operationdate,'yyyy/mm') as timeChose");
				}else if(dateType==4){
					sql.append("         to_char(t.operationdate,'yyyy/mm/dd') as timeChose");
				}
				sql.append(" from  v_operation_feecost t ");
				sql.append(" left join (select ");
				for(int i=0;i<dimStringArray.length;i+=2){
					if("ops_kind".equals(dimStringArray[i])){
						sql.append(" b.kind as kind ");
					}
					sql.append(",");
				}
				sql.append(" count(1) as personNum, b.timeChose ");
				sql.append(" from (select distinct  ");
				for(int i=0;i<dimStringArray.length;i+=2){
					if("ops_kind".equals(dimStringArray[i])){
						sql.append(" t.ops_kind as kind ");
					}
					sql.append(",");
				}
				sql.append("  t.patient_no as no, ");
				if(dateType==1){
					sql.append("         to_char(t.operationdate,'yyyy') as timeChose");
				}else if(dateType==2){
					sql.append(" to_char(t.operationdate,'yyyy')||'/'||to_char(t.operationdate,'q') as timeChose");
				}else if(dateType==3){
					sql.append("         to_char(t.operationdate,'yyyy/mm') as timeChose");
				}else if(dateType==4){
					sql.append("         to_char(t.operationdate,'yyyy/mm/dd') as timeChose");
				}
				sql.append(" from v_operation_feecost t where ");
				sql.append("  to_char(t.operationdate, 'yyyy') = to_char(t.operationdate, 'yyyy') ");
				sql.append(" ) b ");
				sql.append("  group by timeChose,kind ");
				sql.append(" ) d on d.timeChose=to_char(t.operationdate, 'yyyy') ");
				sql.append("  where ");
				for(int i=0;i<dimStringArray.length;i+=2){
					if("ops_kind".equals(dimStringArray[i])){
						sql.append("   d.kind=t.ops_kind  and ");
					}
				}	
				if(dateType==1){
					sql.append("  to_char(t.operationdate,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'");
				}else if(dateType==2){
					sql.append(" to_char(t.operationdate,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'   ");
					sql.append(" and to_char(t.operationdate,'q') between '"+datevo.getQuarter1()+"' and '"+datevo.getQuarter2()+"'   ");
				}else if(dateType==3){
					sql.append("  to_char(t.operationdate,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'   ");
					sql.append(" and  to_char(t.operationdate,'mm') between '"+(datevo.getMonth1()>9?datevo.getMonth1():"0"+datevo.getMonth1())+"' and '"+(datevo.getMonth2()>9?datevo.getMonth2():"0"+datevo.getMonth2())+"'");
				}else if(dateType==4){
					sql.append("  to_char(t.operationdate,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'   ");
					sql.append(" and to_char(t.operationdate,'mm') between '"+(datevo.getMonth1()>9?datevo.getMonth1():"0"+datevo.getMonth1())+"' and '"+(datevo.getMonth2()>9?datevo.getMonth2():"0"+datevo.getMonth2())+"'");
					sql.append(" and  to_char(t.operationdate,'dd') between '"+(datevo.getDay1()>9?datevo.getDay1():"0"+datevo.getDay1())+"' and '"+(datevo.getDay2()>9?datevo.getDay2():"0"+datevo.getDay2())+"'");
				}
				sql.append(" group by  d.personNum, ");
				sql.append(order.toString());
				sql.append(",");
				//对于时间的排序放在最后
				if(dateType==1){
					sql.append("         to_char(t.operationdate,'yyyy') ");
				}else if(dateType==2){
					sql.append(" to_char(t.operationdate,'yyyy')||'/'||to_char(t.operationdate,'q') ");
				}else if(dateType==3){
					sql.append("         to_char(t.operationdate,'yyyy/mm') ");
				}else if(dateType==4){
					sql.append("         to_char(t.operationdate,'yyyy/mm/dd') ");
				}
				
				SQLQuery queryObject1=this.getSession().createSQLQuery(sql.toString());
				for(int i=0;i<dimStringArray.length;i+=2){
					if("ops_kind".equals(dimStringArray[i])){
						queryObject1.addScalar("kind");
					}
				}
				queryObject1.addScalar("operationnNum",Hibernate.INTEGER).addScalar("personNum",Hibernate.INTEGER)
						.addScalar("feeCost",Hibernate.DOUBLE).addScalar("personAvg",Hibernate.DOUBLE)
						.addScalar("drugzyb",Hibernate.DOUBLE).addScalar("timeChose");
				List<OperationCostListvo> bdl=bdl = queryObject1.setResultTransformer(Transformers.aliasToBean(OperationCostListvo.class)).list();
				if(bdl!=null){
					return bdl;
				}
				return new ArrayList<OperationCostListvo>();
	}
}
