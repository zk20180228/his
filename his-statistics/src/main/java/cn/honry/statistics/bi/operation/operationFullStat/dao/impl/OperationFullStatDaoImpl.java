package cn.honry.statistics.bi.operation.operationFullStat.dao.impl;

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
import cn.honry.statistics.bi.operation.operatioNum.vo.OperationNumVo;
import cn.honry.statistics.bi.operation.operationFullStat.dao.OperationFullStatDao;
import cn.honry.statistics.bi.operation.operationFullStat.vo.OperationFullStatVo;
import cn.honry.statistics.bi.outpatient.outpatientWorkload.vo.OutpatientWorkloadVo;
import cn.honry.statistics.util.dateVo.DateVo;
@Repository("operationFullStatDao")
@SuppressWarnings({ "all" })
public class OperationFullStatDaoImpl extends HibernateEntityDao<OperationFullStatVo> implements OperationFullStatDao {
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Override
	public List<OperationFullStatVo> queryOperationFullStatVo(String[] diArrayKey, List<Map<String, List<String>>> list,
			int dateType, DateVo datevo) {
	/*	StringBuffer sql=new StringBuffer();//sql语句的StringBuffer对象
		StringBuffer order=new StringBuffer();//最后order by 的顺序（即：选择的维度顺序）StringBuffer对象
		sql.append("select t.opNum,t.timeChose,(case when t.opNumL= '0' then ('去年无手术')  when t.opNum= '0'then ('今年无手术') when t.opNumL!='0' then to_char(t.opNum-t.opNumL/t.opNumL ||'%') end )as scale,");
		for(int i=0;i<diArrayKey.length;i+=2){
			if(diArrayKey[i].equals("exec_dept")){
				sql.append(" t.deptCode");
				order.append(diArrayKey[i]);
			}
			if(diArrayKey[i].equals("ops_docd")){
				sql.append(" t.opsDocd");
				order.append(diArrayKey[i]);
			}
			if(diArrayKey[i].equals("operation_id")){
				sql.append(" t.itemName");
				order.append(diArrayKey[i]);
			}
			if(diArrayKey[i].equals("ops_kind")){
				sql.append(" t.opsKind");
				order.append(diArrayKey[i]);
			}
			//当拼接的不是最后一个查询维度时，order后面拼接一个","（最后一个order条件不需要添加","）
			if(i!=diArrayKey.length-2){
				
				
			}
			order.append(",");
			sql.append(",");
			
		}
		String s = sql.substring(0, sql.length()-1);
		sql.delete(0,sql.length());
		sql.append(s);
	    sql.append(" from (select ");
		//遍历数组，去匹配所选择的维度拼接sql和order
		for(int i=0;i<diArrayKey.length;i+=2){
			if(diArrayKey[i].equals("exec_dept")){
				sql.append(" t.exec_dept as deptCode");
			}
			if(diArrayKey[i].equals("ops_docd")){
				sql.append(" t.ops_docd as opsDocd");
			}
			if(diArrayKey[i].equals("operation_id")){
				sql.append(" t.operation_id as itemName");
			}
			if(diArrayKey[i].equals("ops_kind")){
				sql.append(" t.ops_kind as opsKind");
			}
			sql.append(",");
		}
		int y1=datevo.getYear1();
		int y2=datevo.getYear2();
		int q1=datevo.getQuarter1();
		int q2=datevo.getQuarter2();
		int m1=datevo.getMonth1();
		int m2=datevo.getMonth2();
		int d1=datevo.getDay1();
		int d2=datevo.getDay2();
		int y21=y2-1;
		int y11=y1-1;
		int m21=m2-1;
		int m11=m1-1;
		int d21=d2-1;
		int d11=d1-1;
		sql.append("(select count(1)  from V_T_OPERATION_RECORD f where  1=1 ");
		if(dateType==1){
			sql.append(" and to_char(f.enter_date,'yyyy') between '"+y1+"' and '"+y2+"'  ) as opNum,");
			
		}else if(dateType==2){
			sql.append(" and to_char(f.enter_date,'yyyy') between '"+y1+"' and '"+y2+"'  ");
			sql.append(" and to_char(f.enter_date,'q') between '"+q1+"' and '"+q2+"'  ) as opNum,");
		}else if(dateType==3){
			sql.append(" and to_char(f.enter_date,'yyyy') between '"+y1+"' and '"+y2+"'  ");
			sql.append(" and to_char(f.enter_date,'mm') between '"+(m1>9?m1:"0"+m1)+"' and '"+(m2>9?m2:"0"+m2)+"'  ) as opNum,");
		}else if(dateType==4){
			sql.append(" and to_char(f.enter_date,'yyyy') between '"+y1+"' and '"+y2+"'   ");
			sql.append(" and to_char(f.enter_date,'mm') between '"+(m1>9?m1:"0"+m1)+"' and '"+(m2>9?m2:"0"+m2)+"'");
			sql.append(" and to_char(f.enter_date,'dd') between '"+(d1>9?d1:"0"+d1)+"' and '"+(d2>9?d2:"0"+d2)+"' ) as opNum,");
		}
		sql.append("(select count(1)  from t_operation_record f where  1=1 ");
		if(dateType==1){
			sql.append(" and to_char(f.enter_date,'yyyy') between '"+y11+"' and '"+y21+"'  ) as opNumL,");
			
		}else if(dateType==2){
			sql.append(" and to_char(f.enter_date,'yyyy') between '"+y11+"' and '"+y21+"'  ");
			sql.append(" and to_char(f.enter_date,'q') between '"+q1+"' and '"+q2+"'  ) as opNumL,");
		}else if(dateType==3){
			sql.append(" and to_char(f.enter_date,'yyyy') between '"+y11+"' and '"+y21+"'  ");
			sql.append(" and to_char(f.enter_date,'mm') between '"+(m11>9?m1:"0"+m11)+"' and '"+(m21>9?m21:"0"+m21)+"'  ) as opNumL,");
		}else if(dateType==4){
			sql.append(" and to_char(f.enter_date,'yyyy') between '"+y11+"' and '"+y21+"'   ");
			sql.append(" and to_char(f.enter_date,'mm') between '"+(m11>9?m1:"0"+m11)+"' and '"+(m21>9?m21:"0"+m21)+"'");
			sql.append(" and to_char(f.enter_date,'dd') between '"+(d11>9?d1:"0"+d11)+"' and '"+(d21>9?d21:"0"+d21)+"' ) as opNumL,");
		}
		if(dateType==1){
			sql.append("         to_char(t.enter_date,'yyyy') as timeChose");
		}else if(dateType==2){
			sql.append("         to_char(to_char(t.enter_date,'yyyy')||'/'||to_char(t.enter_date,'q')) as timeChose");
		}else if(dateType==3){
			sql.append("         to_char(t.enter_date,'yyyy/mm') as timeChose");
		}else if(dateType==4){
			sql.append("         to_char(t.enter_date,'yyyy/mm/dd') as timeChose");
		}
		sql.append("	  from V_T_OPERATION_RECORD t where ");
		sql.append("	  t.ynvalid = '1' and t.del_flg = 0 and t.stop_flg = 0  ");
		if(dateType==1){
			sql.append(" and to_char(t.enter_date,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'");
		}else if(dateType==2){
			sql.append(" and to_char(t.enter_date,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'   ");
			sql.append(" and to_char(t.enter_date,'q') between '"+datevo.getQuarter1()+"' and '"+datevo.getQuarter2()+"'");
		}else if(dateType==3){
			sql.append(" and to_char(t.enter_date,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'   ");
			sql.append(" and to_char(t.enter_date,'mm') between '"+(datevo.getMonth1()>9?datevo.getMonth1():"0"+datevo.getMonth1())+"' and '"+(datevo.getMonth2()>9?datevo.getMonth2():"0"+datevo.getMonth2())+"'");
		}else if(dateType==4){
			sql.append(" and to_char(t.enter_date,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'   ");
			sql.append(" and to_char(t.enter_date,'mm') between '"+(datevo.getMonth1()>9?datevo.getMonth1():"0"+datevo.getMonth1())+"' and '"+(datevo.getMonth2()>9?datevo.getMonth2():"0"+datevo.getMonth2())+"'");
			sql.append(" and to_char(t.enter_date,'dd') between '"+(datevo.getDay1()>9?datevo.getDay1():"0"+datevo.getDay1())+"' and '"+(datevo.getDay2()>9?datevo.getDay2():"0"+datevo.getDay2())+"'");
		}
//		sql.append("	   and  t.DATE_KEY in ("+dayKey+")");
		//遍历数组，添加查询条件（匹配所选择的维度拼接）
		for(int i=0;i<diArrayKey.length;i+=2){
//			sql.append(" and ");
			if(diArrayKey[i].equals("exec_dept")){
				StringBuilder value=new StringBuilder();
				for(int j=0;j<list.get(i/2).get(diArrayKey[i]).size();j++){
					if(!value.toString().equals("")){
						value.append(",");
					}
					value.append("'"+list.get(i/2).get(diArrayKey[i]).get(j)+"'");
				}
				sql.append(" and t.exec_dept  in ("+value.toString()+")");
			}
			if(diArrayKey[i].equals("ops_docd")){
				StringBuilder value=new StringBuilder();
				for(int j=0;j<list.get(i/2).get(diArrayKey[i]).size();j++){
					if(!value.toString().equals("")){
						value.append(",");
					}
					value.append("'"+list.get(i/2).get(diArrayKey[i]).get(j)+"'");
				}
				sql.append(" and t.ops_docd  in ("+value.toString()+")");
			}
			if(diArrayKey[i].equals("operation_id")){
				StringBuilder value=new StringBuilder();
				for(int j=0;j<list.get(i/2).get(diArrayKey[i]).size();j++){
					if(!value.toString().equals("")){
						value.append(",");
					}
					value.append("'"+list.get(i/2).get(diArrayKey[i]).get(j)+"'");
				}
				sql.append(" and t.operation_id  in ("+value.toString()+")");
			}
			if(diArrayKey[i].equals("ops_kind")){
				StringBuilder value=new StringBuilder();
				for(int j=0;j<list.get(i/2).get(diArrayKey[i]).size();j++){
					if(!value.toString().equals("")){
						value.append(",");
					}
					value.append("'"+list.get(i/2).get(diArrayKey[i]).get(j)+"'");
				}
				sql.append(" and t.ops_kind  in ("+value.toString()+")");
			}
		}
		sql.append("	 group by  ");
		sql.append(order.toString());
		
		//对于时间的排序放在最后
		if(dateType==1){
			sql.append("         to_char(t.enter_date,'yyyy') ");
		}else if(dateType==2){
			sql.append("         to_char(t.enter_date,'yyyy') ");
			sql.append("         ,to_char(t.enter_date,'q') ");
		}else if(dateType==3){
			sql.append("         to_char(t.enter_date,'yyyy/mm') ");
		}else if(dateType==4){
			sql.append("         to_char(t.enter_date,'yyyy/mm/dd')");
		}
		sql.append("	 order by ");
		sql.append(order.toString());
		//对于时间的排序放在最后
		if(dateType==1){
			sql.append("         to_char(t.enter_date,'yyyy') ");
		}else if(dateType==2){
			sql.append("         to_char(t.enter_date,'yyyy') ");
			sql.append("         ,to_char(t.enter_date,'q') ");
		}else if(dateType==3){
			sql.append("         to_char(t.enter_date,'yyyy/mm') ");
		}else if(dateType==4){
			sql.append("         to"
					+ "_char(t.enter_date,'yyyy/mm/dd')");
		}
		sql.append(") t"); 
		SQLQuery queryObject1=this.getSession().createSQLQuery(sql.toString());
		for(int i=0;i<diArrayKey.length;i+=2){
			if(diArrayKey[i].equals("exec_dept")){
				queryObject1.addScalar("deptCode");
			}
			if(diArrayKey[i].equals("ops_docd")){
				queryObject1.addScalar("opsDocd");
			}
			if(diArrayKey[i].equals("operation_id")){
				queryObject1.addScalar("itemName");
			}
			if(diArrayKey[i].equals("ops_kind")){
				queryObject1.addScalar("opsKind");
			}
		}
		queryObject1.addScalar("opNum",Hibernate.DOUBLE).addScalar("scale").addScalar("timeChose");
		List<OperationFullStatVo> bdl = queryObject1.setResultTransformer(Transformers.aliasToBean(OperationFullStatVo.class)).list();
		if(bdl!=null){
			return bdl;
		}
		return new ArrayList<OperationFullStatVo>();*/
		
		StringBuffer sql=new StringBuffer();//sql语句的StringBuffer对象
		StringBuffer order=new StringBuffer();//最后order by 的顺序（即：选择的维度顺序）StringBuffer对象
		sql.append(" select count(1) as opNum, trunc( (count(1) - a.bl) / a.bl,2)*100 as scale,");
		for(int i=0;i<diArrayKey.length;i+=2){
			if("exec_dept".equals(diArrayKey[i])){
				sql.append(" t.exec_dept as deptCode");
				order.append(diArrayKey[i]);
			}
			if("ops_docd".equals(diArrayKey[i])){
				sql.append(" t.ops_docd as opsDocd");
				order.append(diArrayKey[i]);
			}
			if("operation_id".equals(diArrayKey[i])){
				sql.append(" t.operation_id as itemName");
				order.append(diArrayKey[i]);
			}
			if("ops_kind".equals(diArrayKey[i])){
				sql.append(" t.ops_kind as opsKind");
				order.append(diArrayKey[i]);
			}
			order.append(",");
			sql.append(",");
		}
		/*String s = sql.substring(0, sql.length()-1);
		sql.delete(0,sql.length());
		sql.append(s);*/
		if(dateType==1){
			sql.append("         to_char(t.enter_date,'yyyy') as timeChose");
		}else if(dateType==2){
			sql.append("          to_char(to_char(t.enter_date,'yyyy')||'/'||to_char(t.enter_date,'q')) as timeChose");
		}else if(dateType==3){
			sql.append("          to_char(t.enter_date,'yyyy/mm') as timeChose");
		}else if(dateType==4){
			sql.append("         to_char(t.enter_date,'yyyy/mm/dd') as timeChose");
		}
	    sql.append(" from V_T_OPERATION_RECORD t ");
	    sql.append("left join (select count(1) as bl,");
		    for(int i=0;i<diArrayKey.length;i+=2){
				if("exec_dept".equals(diArrayKey[i])){
					sql.append(" o.exec_dept as deptCode");
				}
				if("ops_docd".equals(diArrayKey[i])){
					sql.append(" o.ops_docd as opsDocd");
				}
				if("operation_id".equals(diArrayKey[i])){
					sql.append(" o.operation_id as itemName");
				}
				if("ops_kind".equals(diArrayKey[i])){
					sql.append(" o.ops_kind as opsKind");
				}
				sql.append(",");
			}
	    if(dateType==1){
			sql.append("         to_char(o.enter_date,'yyyy') as timeChose");
		}else if(dateType==2){
			sql.append("          to_char(to_char(o.enter_date,'yyyy')||'/'||to_char(o.enter_date,'q')) as timeChose");
		}else if(dateType==3){
			sql.append("          to_char(o.enter_date,'yyyy/mm') as timeChose");
		}else if(dateType==4){
			sql.append("         to_char(o.enter_date,'yyyy/mm/dd') as timeChose");
		}
	    		sql.append(" from  V_T_OPERATION_RECORD  o");
	    sql.append("  where  ");
		if(dateType==1){
			sql.append("     to_char(o.enter_date, 'yyyy')=to_char(o.enter_date, 'yyyy') ");
		}else if(dateType==2){
			sql.append(" to_char(o.enter_date,'yyyy')||'/'||to_char(o.enter_date,'q')="
					+ "to_char(o.enter_date,'yyyy')||'/'||to_char(o.enter_date,'q') ");
		}else if(dateType==3){
			sql.append("         to_char(o.enter_date,'yyyy/mm')=to_char(o.enter_date,'yyyy/mm') ");
		}else if(dateType==4){
			sql.append("         to_char(o.enter_date,'yyyy/mm/dd')=to_char(o.enter_date,'yyyy/mm/dd') ");
		}
		sql.append(" group by ");
		sql.append(order.toString());
		 if(dateType==1){
			sql.append("         to_char(o.enter_date,'yyyy') ");
		}else if(dateType==2){
			sql.append("          to_char(to_char(o.enter_date,'yyyy')||'/'||to_char(o.enter_date,'q')) ");
		}else if(dateType==3){
			sql.append("          to_char(o.enter_date,'yyyy/mm') ");
		}else if(dateType==4){
			sql.append("         to_char(o.enter_date,'yyyy/mm/dd') ");
		}
		sql.append(" ) a on");
		
	    if(dateType==1){
			sql.append("    a.timeChose=to_char(add_months(trunc(t.enter_date),-12),'yyyy')");
		}else if(dateType==2){
			sql.append(" to_number(replace(a.timeChose,'/','')) = ( case"
					+ " when substr((replace(to_char(t.enter_date,'yyyy')||'/'||to_char(t.enter_date,'q'),'/','')), 5, 1 ) = '1' then"
					+ " to_number(replace(to_char(t.enter_date,'yyyy')||'/'||to_char(t.enter_date,'q'),'/',''))-7 "
					+ " when substr((replace(to_char(t.enter_date,'yyyy')||'/'||to_char(t.enter_date,'q'),'/','')), 5, 1 ) != '1' then"
					+ " to_number(replace(to_char(t.enter_date,'yyyy')||'/'||to_char(t.enter_date,'q'),'/',''))-1"
					+ " end) ");
		}else if(dateType==3){
			sql.append("  replace(a.timeChose,'/','') = to_char(add_months(trunc(t.enter_date),-1),'yyyymm') ");
		}else if(dateType==4){
			sql.append("   a.timeChose = to_char(t.enter_date - 1,'yyyy/MM/dd')");
		}
	    
	    sql.append(" where ");
	    if(dateType==1){
			sql.append("  to_char(t.enter_date,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'");
		}else if(dateType==2){
			sql.append("  to_char(t.enter_date,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'   ");
			sql.append(" and to_char(t.enter_date,'q') between '"+datevo.getQuarter1()+"' and '"+datevo.getQuarter2()+"'");
		}else if(dateType==3){
			sql.append("  to_char(t.enter_date,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'   ");
			sql.append(" and to_char(t.enter_date,'mm') between '"+(datevo.getMonth1()>9?datevo.getMonth1():"0"+datevo.getMonth1())+"' and '"+(datevo.getMonth2()>9?datevo.getMonth2():"0"+datevo.getMonth2())+"'");
		}else if(dateType==4){
			sql.append("  to_char(t.enter_date,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'   ");
			sql.append(" and to_char(t.enter_date,'mm') between '"+(datevo.getMonth1()>9?datevo.getMonth1():"0"+datevo.getMonth1())+"' and '"+(datevo.getMonth2()>9?datevo.getMonth2():"0"+datevo.getMonth2())+"'");
			sql.append(" and to_char(t.enter_date,'dd') between '"+(datevo.getDay1()>9?datevo.getDay1():"0"+datevo.getDay1())+"' and '"+(datevo.getDay2()>9?datevo.getDay2():"0"+datevo.getDay2())+"'");
		}
	    
	  //遍历数组，添加查询条件（匹配所选择的维度拼接）
  		for(int i=0;i<diArrayKey.length;i+=2){
  			if("exec_dept".equals(diArrayKey[i])){
  				StringBuilder value=new StringBuilder();
  				for(int j=0;j<list.get(i/2).get(diArrayKey[i]).size();j++){
  					if(!"".equals(value.toString())){
  						value.append(",");
  					}
  					value.append("'"+list.get(i/2).get(diArrayKey[i]).get(j)+"'");
  				}
  				sql.append(" and t.exec_dept  in ("+value.toString()+")");
  			}
  			if("ops_docd".equals(diArrayKey[i])){
  				StringBuilder value=new StringBuilder();
  				for(int j=0;j<list.get(i/2).get(diArrayKey[i]).size();j++){
  					if(!"".equals(value.toString())){
  						value.append(",");
  					}
  					value.append("'"+list.get(i/2).get(diArrayKey[i]).get(j)+"'");
  				}
  				sql.append(" and t.ops_docd  in ("+value.toString()+")");
  			}
  			if("operation_id".equals(diArrayKey[i])){
  				StringBuilder value=new StringBuilder();
  				for(int j=0;j<list.get(i/2).get(diArrayKey[i]).size();j++){
  					if(!"".equals(value.toString())){
  						value.append(",");
  					}
  					value.append("'"+list.get(i/2).get(diArrayKey[i]).get(j)+"'");
  				}
  				sql.append(" and t.operation_id  in ("+value.toString()+")");
  			}
  			if("ops_kind".equals(diArrayKey[i])){
  				StringBuilder value=new StringBuilder();
  				for(int j=0;j<list.get(i/2).get(diArrayKey[i]).size();j++){
  					if(!"".equals(value.toString())){
  						value.append(",");
  					}
  					value.append("'"+list.get(i/2).get(diArrayKey[i]).get(j)+"'");
  				}
  				sql.append(" and t.ops_kind  in ("+value.toString()+")");
  			}
  		}
  		for(int i=0;i<diArrayKey.length;i+=2){
			if("exec_dept".equals(diArrayKey[i])){
				sql.append(" and t.exec_dept = a.deptCode ");
			}
			if("ops_docd".equals(diArrayKey[i])){
				sql.append(" and t.ops_docd = a.opsDocd  ");
			}
			if("operation_id".equals(diArrayKey[i])){
				sql.append(" and t.operation_id = a.itemName");
			}
			if("ops_kind".equals(diArrayKey[i])){
				sql.append(" and t.ops_kind = a.opsKind ");
			}
		}
		sql.append("	 group by  ");
		sql.append(order.toString());
		sql.append(" a.bl,");
		//对于时间的排序放在最后
		if(dateType==1){
			sql.append("         to_char(t.enter_date,'yyyy') ");
		}else if(dateType==2){
			sql.append("         to_char(t.enter_date,'yyyy') ");
			sql.append("         ,to_char(t.enter_date,'q') ");
		}else if(dateType==3){
			sql.append("         to_char(t.enter_date,'yyyy/mm') ");
		}else if(dateType==4){
			sql.append("         to_char(t.enter_date,'yyyy/mm/dd')");
		}
		sql.append("	 order by ");
		sql.append(order.toString());
		sql.append(" a.bl,");
		//对于时间的排序放在最后
		if(dateType==1){
			sql.append("         to_char(t.enter_date,'yyyy') ");
		}else if(dateType==2){
			sql.append("         to_char(t.enter_date,'yyyy') ");
			sql.append("         ,to_char(t.enter_date,'q') ");
		}else if(dateType==3){
			sql.append("         to_char(t.enter_date,'yyyy/mm') ");
		}else if(dateType==4){
			sql.append("         to"
					+ "_char(t.enter_date,'yyyy/mm/dd')");
		}
		SQLQuery queryObject1=this.getSession().createSQLQuery(sql.toString());
		for(int i=0;i<diArrayKey.length;i+=2){
			if("exec_dept".equals(diArrayKey[i])){
				queryObject1.addScalar("deptCode");
			}
			if("ops_docd".equals(diArrayKey[i])){
				queryObject1.addScalar("opsDocd");
			}
			if("operation_id".equals(diArrayKey[i])){
				queryObject1.addScalar("itemName");
			}
			if("ops_kind".equals(diArrayKey[i])){
				queryObject1.addScalar("opsKind");
			}
		}
		queryObject1.addScalar("opNum",Hibernate.DOUBLE).addScalar("scale",Hibernate.DOUBLE).addScalar("timeChose");
		List<OperationFullStatVo> bdl = queryObject1.setResultTransformer(Transformers.aliasToBean(OperationFullStatVo.class)).list();
		if(bdl!=null){
			return bdl;
		}
		return new ArrayList<OperationFullStatVo>();
		
	}
	

}
