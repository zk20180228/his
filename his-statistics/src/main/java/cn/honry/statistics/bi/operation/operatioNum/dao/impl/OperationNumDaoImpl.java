package cn.honry.statistics.bi.operation.operatioNum.dao.impl;

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
import cn.honry.statistics.bi.operation.operatioNum.dao.OperationNumDao;
import cn.honry.statistics.bi.operation.operatioNum.vo.OperationNumVo;
import cn.honry.statistics.bi.outpatient.outpatientFeeType.vo.OutpatientFeeTypeVo;
import cn.honry.statistics.bi.outpatient.outpatientWorkload.vo.OutpatientWorkloadVo;
import cn.honry.statistics.util.dateVo.DateVo;
@Repository("operationNumDao")
@SuppressWarnings({ "all" })
public class OperationNumDaoImpl extends HibernateEntityDao<OperationNumVo> implements OperationNumDao{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

/*	@Override
	public OperationNumVo getOperationNum(String stime,String etime,String opExecdept,String opKind) {
		String opk="";
		if(opKind=="jz"||"jz".equals(opKind)){
			opk="4028809754177a040154177adeb40003";
		}else{
			opk=opKind;
		}
		StringBuffer sb=new StringBuffer();
		sb.append(" select count(tor.operation_id) as opNum ");
		sb.append(" from t_operation_record tor where tor.ynvalid = '1' and tor.del_flg = 0 and tor.stop_flg = 0 ");
		//时间 只能按年份查询
		if(StringUtils.isNotBlank(stime)){
			sb.append("and to_char(tor.enter_date,'yyyy') = '"+stime+"'");
		}
		//科室
		if(StringUtils.isNotBlank(opExecdept)){
			sb.append("and tor.exec_dept = '"+opExecdept+"'");
		}
		//手术类别
		if(StringUtils.isNotBlank(opk)){
			sb.append("and tor.ops_kind = '"+opk+"'");
		}
		
		sb.append(" group by ");
		
		if(StringUtils.isNotBlank(opExecdept)){
			sb.append("tor.exec_dept,");
		}
		if(StringUtils.isNotBlank(opKind)){
			sb.append("tor.ops_kind,");
		}
		
		//截取掉最后一位 ","。
		String s1 = sb.substring(0, sb.length()-1);
		sb.delete(0,sb.length());
		sb.append(s1);
		
		sb.append(" order by ");
		
		if(StringUtils.isNotBlank(opExecdept)){
			sb.append("tor.exec_dept,");
		}
		
		if(StringUtils.isNotBlank(opKind)){
			sb.append("tor.ops_kind,");
		}
	
		//截取掉最后一位 ","。
		String s2 = sb.substring(0, sb.length()-1);
		sb.delete(0,sb.length());
		sb.append(s2);
		SQLQuery query=this.getSession().createSQLQuery(sb.toString()).addScalar("opNum",Hibernate.INTEGER);
		OperationNumVo vo = (OperationNumVo) query.setResultTransformer(Transformers.aliasToBean(OperationNumVo.class)).uniqueResult();
		return vo;
	}*/

	@Override
	public List<OperationNumVo> queryOperationNum(String[] diArrayKey, List<Map<String, List<String>>> list,
			int dateType, DateVo datevo) {
		//查询时间
		StringBuffer sql=new StringBuffer();//sql语句的StringBuffer对象
		StringBuffer order=new StringBuffer();//最后order by 的顺序（即：选择的维度顺序）StringBuffer对象
	    sql.append(" select ");
		//遍历数组，去匹配所选择的维度拼接sql和order
		for(int i=0;i<diArrayKey.length;i+=2){
			if("exec_dept".equals(diArrayKey[i])){
				sql.append(" tor.exec_dept as opExecdept");
				order.append(diArrayKey[i]);
			}
			if("ops_kind".equals(diArrayKey[i])){
				sql.append(" tor.ops_kind as opKind");
				order.append(diArrayKey[i]);
			}
			//当拼接的不是最后一个查询维度时，order后面拼接一个","（最后一个order条件不需要添加","）
			if(i!=(diArrayKey.length-2)){
				order.append(",");
			}
			sql.append(",");
		}
		sql.append("	         count(tor.operation_id) as opNum, ");
		if(dateType==1){
			sql.append("         to_char(tor.enter_date,'yyyy') as timeChose");
		}else if(dateType==2){
			sql.append("         to_char(to_char(tor.enter_date,'yyyy')||'/'||to_char(tor.enter_date,'q')) as timeChose");
		}else if(dateType==3){
			sql.append("         to_char(tor.enter_date,'yyyy/mm') as timeChose");
		}else if(dateType==4){
			sql.append("         to_char(tor.enter_date,'yyyy/mm/dd') as timeChose");
		}
		sql.append("	  from V_T_OPERATION_RECORD tor ");
		sql.append("	  where tor.ynvalid = '1' and tor.del_flg = 0 and tor.stop_flg = 0  ");
		if(dateType==1){
			sql.append(" and to_char(tor.enter_date,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'");
		}else if(dateType==2){
			sql.append(" and to_char(tor.enter_date,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'   ");
			sql.append(" and to_char(tor.enter_date,'q') between '"+datevo.getQuarter1()+"' and '"+datevo.getQuarter2()+"'");
		}else if(dateType==3){
			sql.append(" and to_char(tor.enter_date,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'   ");
			sql.append(" and to_char(tor.enter_date,'mm') between '"+(datevo.getMonth1()>9?datevo.getMonth1():"0"+datevo.getMonth1())+"' and '"+(datevo.getMonth2()>9?datevo.getMonth2():"0"+datevo.getMonth2())+"'");
		}else if(dateType==4){
			sql.append(" and to_char(tor.enter_date,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'   ");
			sql.append(" and to_char(tor.enter_date,'mm') between '"+(datevo.getMonth1()>9?datevo.getMonth1():"0"+datevo.getMonth1())+"' and '"+(datevo.getMonth2()>9?datevo.getMonth2():"0"+datevo.getMonth2())+"'");
			sql.append(" and to_char(tor.enter_date,'dd') between '"+(datevo.getDay1()>9?datevo.getDay1():"0"+datevo.getDay1())+"' and '"+(datevo.getDay2()>9?datevo.getDay2():"0"+datevo.getDay2())+"'");
		}
//		sql.append("	   and  t.DATE_KEY in ("+dayKey+")");
		//遍历数组，添加查询条件（匹配所选择的维度拼接）
		for(int i=0;i<diArrayKey.length;i+=2){
//			sql.append(" and ");
			if("exec_dept".equals(diArrayKey[i])){
				StringBuilder value=new StringBuilder();
				for(int j=0;j<list.get(i/2).get(diArrayKey[i]).size();j++){
					if(!"".equals(value.toString())){
						value.append(",");
					}
					value.append("'"+list.get(i/2).get(diArrayKey[i]).get(j)+"'");
				}
				sql.append(" and tor.exec_dept  in ("+value.toString()+")");
			}
			if("ops_kind".equals(diArrayKey[i])){
				StringBuilder value=new StringBuilder();
				for(int j=0;j<list.get(i/2).get(diArrayKey[i]).size();j++){
					if(!"".equals(value.toString())){
						value.append(",");
					}
					value.append("'"+list.get(i/2).get(diArrayKey[i]).get(j)+"'");
				}
				sql.append(" and tor.ops_kind  in ("+value.toString()+")");
			}
		}
		sql.append("	 group by  ");
		sql.append(order.toString());
			sql.append(",");
		//对于时间的排序放在最后
		if(dateType==1){
			sql.append("         to_char(tor.enter_date,'yyyy') ");
		}else if(dateType==2){
			sql.append("         to_char(tor.enter_date,'yyyy') ");
			sql.append("         ,to_char(tor.enter_date,'q') ");
		}else if(dateType==3){
			sql.append("          to_char(tor.enter_date,'yyyy/mm') ");
		}else if(dateType==4){
			sql.append("         to_char(tor.enter_date,'yyyy/mm/dd')");
		}
		sql.append("	 order by ");
		sql.append(order.toString());
			sql.append(",");
		//对于时间的排序放在最后
		if(dateType==1){
			sql.append("         to_char(tor.enter_date,'yyyy') ");
		}else if(dateType==2){
			sql.append("         to_char(tor.enter_date,'yyyy') ");
			sql.append("         ,to_char(tor.enter_date,'q') ");
		}else if(dateType==3){
			sql.append("         to_char(tor.enter_date,'yyyy/mm') ");
		}else if(dateType==4){
			sql.append("         to_char(tor.enter_date,'yyyy/mm/dd')");
		}
		SQLQuery queryObject1=this.getSession().createSQLQuery(sql.toString());
		for(int i=0;i<diArrayKey.length;i+=2){
			if("exec_dept".equals(diArrayKey[i])){
				queryObject1.addScalar("opExecdept");
			}
			if("ops_kind".equals(diArrayKey[i])){
				queryObject1.addScalar("opKind");
			}
		}
		queryObject1.addScalar("opNum",Hibernate.INTEGER).addScalar("timeChose");
		List<OperationNumVo> bdl = queryObject1.setResultTransformer(Transformers.aliasToBean(OperationNumVo.class)).list();
		if(bdl!=null){
			return bdl;
		}
		return new ArrayList<OperationNumVo>();
	}
}
