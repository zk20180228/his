package cn.honry.statistics.bi.inpatient.hospitalizationExpenses.dao.impl;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import cn.honry.base.bean.model.BiInpatientFeeinfo;
import cn.honry.base.bean.model.BiInpatientInfo;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.statistics.bi.inpatient.hospitalizationExpenses.dao.HospitalizationExpensesDao;
import cn.honry.statistics.bi.inpatient.hospitalizationExpenses.vo.HospitalizationExpensesVo;
import cn.honry.statistics.bi.inpatient.hospitalizationInformation.vo.HospitalizationInformationVo;
import cn.honry.statistics.util.dateVo.DateVo;

@Repository("hospitalizationExpensesDao")
@SuppressWarnings({ "all" })
public class HospitalizationExpensesDaoImpl extends HibernateEntityDao<BiInpatientFeeinfo> implements HospitalizationExpensesDao {
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

   /**
	*	!!!!!!    t_ 之后要全部转为bi_
	*/
	@Override
	public List<HospitalizationExpensesVo> querytDatagrid(String[] diArrayKey,List<Map<String,List<String>>> list,int dateType,DateVo datevo) {
		//查询字段 sql
		StringBuffer selectSql=new StringBuffer();
		StringBuffer orderSql=new StringBuffer(" ");
		String jdString = "";
		String orderString = "";
		selectSql.append(" select dept.dept_code as deptname, ");
		selectSql.append(" nvl(round(sum(t.totcost), 2), 0) as totCost,  ");
		selectSql.append(" nvl(round(sum(t.drugs),2),0)   as drugs, ");
		selectSql.append(" nvl(round(sum(t.nodrug),2),0)   as noDrugs, ");
		selectSql.append(" count(t.code) as Passengers, ");
		if(dateType==1){//年
			selectSql.append(" to_char(t.fee_date,'yyyy') as feeDate ");
		}else if(dateType==2){//季度
			selectSql.append(" to_char(to_char(t.fee_date,'yyyy')||'/'||to_char(t.fee_date,'q')) as feeDate");
		}else if(dateType==3){//月
			selectSql.append(" to_char(t.fee_date,'yyyy/mm') as feeDate ");
		}else if(dateType==4){//天
			selectSql.append(" to_char(t.fee_date,'yyyy/mm/dd') as feeDate ");
		}
		selectSql.append(" from t_department dept ");
		selectSql.append(" left join t_inpatient_info info on info.dept_code = dept.dept_code ");
		selectSql.append(" right join  v_hospitalization_expenses t on t.code =info.inpatient_no ");
		selectSql.append(" where 1=1 ");
		
		if(dateType==1){//年
			String date1=datevo.getYear1()+"";
			String date2=datevo.getYear2()+"";//
			selectSql.append(" and to_char(t.fee_date,'yyyy') between'"+date1+"'  and '"+date2+"'");
			orderSql.append( " ,to_char(t.fee_date,'yyyy') ");
		}else if(dateType==2){//季度
			String date1=datevo.getYear1()+"";
			String date2=datevo.getYear2()+"";
			selectSql.append(" and to_char(t.fee_date,'yyyy') between'"+date1+"'  and '"+date2+"'");
			selectSql.append(" and to_char(t.fee_date,'q') between '"+datevo.getQuarter1()+"' and '"+datevo.getQuarter2()+"' ");
			orderString=",to_char(t.fee_date, 'yyyy')||'/'||to_char(t.fee_date, 'q') ";
			jdString=" ,to_char(t.fee_date, 'yyyy'), to_char(t.fee_date, 'q') ";
		}else if(dateType==3){//月
			String month1=datevo.getMonth1()+"";
			String month2=datevo.getMonth2()+"";
			String date1=datevo.getYear1()+"/"+(month1.length()>1?datevo.getMonth1():"0"+datevo.getMonth1());
			String date2=datevo.getYear2()+"/"+(month2.length()>1?datevo.getMonth2():"0"+datevo.getMonth2());
			selectSql.append(" and to_char(t.fee_date,'yyyy/mm') between '"+date1+"' and '"+date2+"' ");
			orderSql.append(" ,to_char(t.fee_date, 'yyyy/mm') ");
		}else if(dateType==4){//天
			String month1=datevo.getMonth1()+"";
			String month2=datevo.getMonth2()+"";
			String date1=datevo.getYear1()+"/"+(month1.length()>1?datevo.getMonth1():"0"+datevo.getMonth1())+"/"+datevo.getDay1();
			String date2=datevo.getYear2()+"/"+(month2.length()>1?datevo.getMonth2():"0"+datevo.getMonth2())+"/"+datevo.getDay2();
			selectSql.append(" and to_char(t.fee_date,'yyyy/mm/dd') between '"+date1+"' and '"+date2+"' ");
			orderSql.append(" ,to_char(t.fee_date, 'yyyy/mm/dd') ");
		}
		selectSql.append(" and dept.dept_type = 'I' ");
		
		
		//遍历数组，去匹配所选择的维度拼接sql和order
		//home in_sourse status age 维  度
			
		List whereList=list.get(0).get("dept_code");
		String value="";
		selectSql.append( "and dept.dept_code in (");
		for(int j=0;j<whereList.size();j++){
			value=whereList.get(j).toString();
			selectSql.append("'"+value+"'");
			if(j<whereList.size()-1){
				selectSql.append(",");
			}
		}
		selectSql.append(")");
		
		selectSql.append(" group by dept.dept_code,t.code "+jdString+orderSql.toString());
		selectSql.append(" order by dept.dept_code "+orderString+orderSql.toString());
		
		
		
		final String sql=selectSql.toString(); 
			
		SQLQuery queryObject=this.getSession().createSQLQuery(sql);
		queryObject.addScalar("deptname");
		queryObject.addScalar("passengers",Hibernate.INTEGER);
		queryObject.addScalar("totCost",Hibernate.DOUBLE);
		queryObject.addScalar("drugs",Hibernate.DOUBLE);
		queryObject.addScalar("noDrugs",Hibernate.DOUBLE);
		queryObject.addScalar("feeDate");
		queryObject.setResultTransformer(Transformers.aliasToBean(HospitalizationExpensesVo.class));
		List<HospitalizationExpensesVo> result= queryObject.list();
		for(int num=0;num<result.size();num++){
			//总费用
			Double all = result.get(num).getTotCost();
			//药费
			Double i=result.get(num).getDrugs();
			//非药物
			Double m =result.get(num).getNoDrugs();
			//人次
			Integer p=result.get(num).getPassengers();
			if(all==0){
				result.get(num).setDrugsPro(0+"%");
				result.get(num).setNoDrugsPro(0+"%");
				result.get(num).setPassengersAvg(0+"元");
			}else{
				DecimalFormat df = new DecimalFormat("0.00");
				result.get(num).setDrugsPro(i==0?"0%":df.format((i/all*100))+"%");
				result.get(num).setNoDrugsPro(m==0?"0%":df.format((m/all*100))+"%");
				result.get(num).setPassengersAvg(df.format((all/p))+"元");
			}
		}
		if(result!=null){
			return result;
		}
		return new ArrayList<HospitalizationExpensesVo>();
	}
}