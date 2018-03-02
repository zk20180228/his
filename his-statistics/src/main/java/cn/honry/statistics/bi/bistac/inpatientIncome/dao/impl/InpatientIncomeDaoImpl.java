package cn.honry.statistics.bi.bistac.inpatientIncome.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.statistics.bi.bistac.inpatientIncome.dao.InpatientIncomeDao;
import cn.honry.statistics.bi.bistac.inpatientIncome.vo.InpatientIncomeVo;
import cn.honry.statistics.bi.bistac.monthlyDashboard.vo.MonthlyDashboardVo;
import cn.honry.statistics.bi.bistac.outpatientUseMedic.vo.OutpatientUseMedicVo;
@Repository("inpatientIncomeDao")
@SuppressWarnings({ "all" })
public class InpatientIncomeDaoImpl extends HibernateEntityDao<InpatientIncomeVo> implements InpatientIncomeDao{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	//基础工具类,不支持参数名传参
	@Resource
	private JdbcTemplate jdbcTemplate;
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	/**  
	 * 
	 * 住院收入分析
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月20日 上午11:10:02 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月20日 上午11:10:02 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public List<InpatientIncomeVo> queryInpatientIncomeVo(List<String> list)  throws Exception{
		
		if(list==null||list.size()<0){
			return new ArrayList<InpatientIncomeVo>();
		}
		String[] str1= list.get(0).split("/");
		String[] str2= list.get(1).split("/");
		String[] str3= list.get(2).split("/");
		String[] str4= list.get(3).split("/");
		final StringBuffer sb = new StringBuffer();
		sb.append(" select q0.deptName, nvl(q1.tot1,0) as totCost1,nvl(q1.supply1,0) as supplyCost1, nvl(q2.tot2,0) as totCost2,nvl(q2.supply2,0) as supplyCost2,nvl(q3.supply3,0) as lastSupplyCost1,nvl(q4.supply4,0) as lastSupplyCost2 from ( ");
		sb.append(" select distinct balanceoper_deptcode,BALANCEOPER_DEPTNAME as deptName from (");
		sb.append(" select distinct balanceoper_deptcode, BALANCEOPER_DEPTNAME from ").append(str1[0]).append(" t0 ").append(" where  ");
		sb.append(" to_char(t0.balance_date, 'yyyy-MM') = '").append(str1[1]).append("' and t0.stop_flg=0 and t0.del_flg=0");
		sb.append(" union all ");
		sb.append(" select distinct balanceoper_deptcode, BALANCEOPER_DEPTNAME from ").append(str2[0]).append(" t0 ").append(" where  ");
		sb.append(" to_char(t0.balance_date, 'yyyy-MM') = '").append(str2[1]).append("' and t0.stop_flg=0 and t0.del_flg=0))q0");
		
		sb.append(" left join (select t1.balanceoper_deptcode,nvl(sum(t1.tot_cost),0) as tot1,nvl(sum(t1.SUPPLY_COST),0) as supply1 from ").append(str1[0]).append(" t1 ").append(" where  ");
		sb.append(" to_char(t1.balance_date,'yyyy-MM') = '").append(str1[1]).append("' and ");
		sb.append(" t1.stop_flg=0 and t1.del_flg=0 group by t1.balanceoper_deptcode)");
		sb.append(" q1 on q0.balanceoper_deptcode =q1.balanceoper_deptcode ");
		
		sb.append(" left join (select t3.balanceoper_deptcode,nvl(sum(t3.tot_cost),0) as supply3 from ").append(str3[0]).append(" t3 ").append(" where  ");
		sb.append(" to_char(t3.balance_date,'yyyy-MM') = '").append(str3[1]).append("' and ");
		sb.append(" t3.stop_flg=0 and t3.del_flg=0 group by t3.balanceoper_deptcode)");
		sb.append(" q3 on q0.balanceoper_deptcode =q3.balanceoper_deptcode ");
		
		sb.append(" left join (select t4.balanceoper_deptcode,nvl(sum(t4.tot_cost),0) as supply4 from ").append(str4[0]).append(" t4 ").append(" where  ");
		sb.append(" to_char(t4.balance_date,'yyyy-MM') = '").append(str4[1]).append("' and ");
		sb.append(" t4.stop_flg=0 and t4.del_flg=0 group by t4.balanceoper_deptcode)");
		sb.append(" q4 on q0.balanceoper_deptcode =q4.balanceoper_deptcode ");
		
		sb.append(" left join (select t2.balanceoper_deptcode,nvl(sum(t2.tot_cost),0) as tot2,nvl(sum(t2.SUPPLY_COST),0) as supply2 from ").append(str2[0]).append(" t2 ").append(" where  ");
		sb.append(" to_char(t2.balance_date,'yyyy-MM') = '").append(str2[1]).append("' and ");
		sb.append(" t2.stop_flg=0 and t2.del_flg=0 group by t2.balanceoper_deptcode)");
		sb.append(" q2 on q0.balanceoper_deptcode =q2.balanceoper_deptcode ");
		sb.append(" where  q0.deptName is not null ");
		List<InpatientIncomeVo> voList = (List<InpatientIncomeVo>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public List<InpatientIncomeVo> doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sb.toString());
				queryObject.addScalar("deptName",Hibernate.STRING)
				           .addScalar("totCost1",Hibernate.DOUBLE)
				           .addScalar("supplyCost1",Hibernate.DOUBLE)
				           .addScalar("totCost2",Hibernate.DOUBLE)
				           .addScalar("supplyCost2",Hibernate.DOUBLE)
						   .addScalar("lastSupplyCost1",Hibernate.DOUBLE)
						   .addScalar("lastSupplyCost2",Hibernate.DOUBLE);
				return queryObject.setResultTransformer(Transformers.aliasToBean(InpatientIncomeVo.class)).list();
			}
		});
		if(voList!=null&&voList.size()>0){
			return voList;
		}
		return new ArrayList<InpatientIncomeVo>();
	}
	/**  
	 * 
	 * 获取住院结算表的最大和最小时间
	 * @Author: huzhenguo
	 * @CreateDate: 2017年3月29日 上午10:51:26 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年3月29日 上午10:51:26 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public InpatientIncomeVo findMaxMin()  throws Exception{
		final String sql = "SELECT MAX(mn.BALANCE_DATE) AS eTime ,MIN(mn.BALANCE_DATE) AS sTime FROM T_INPATIENT_BALANCEHEAD_NOW mn";
		InpatientIncomeVo vo = (InpatientIncomeVo) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public InpatientIncomeVo doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sql.toString());
				queryObject.addScalar("eTime",Hibernate.DATE).addScalar("sTime",Hibernate.DATE);
				return (InpatientIncomeVo) queryObject.setResultTransformer(Transformers.aliasToBean(InpatientIncomeVo.class)).uniqueResult();
			}
		});
		return vo;
	}
	/**  
	 * 
	 *  住院收入分析（new）
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月22日 上午10:49:54 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月22日 上午10:49:54 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public List<OutpatientUseMedicVo> queryOutpatientUseMedicVo(
			List<String> tnL,List<String> tnL2,String begin,String end)  throws Exception{
		if(tnL==null || tnL.size()==0 || tnL2==null||tnL2.size()==0){
			return new ArrayList<OutpatientUseMedicVo>();
		}
		 final StringBuffer sb = new StringBuffer();
		 sb.append(" select b.cost1 as cost1,b.cost2 as cost2, o.DEPT_NAME as regDpcdName,b.mon as selectTime from ( ");
		 sb.append(" select sum(a.cost1) as cost1,sum(a.cost2) as cost2, a.deptCode ,a.mon  from ( ");
		 for (int i = 0; i < tnL.size(); i++) {
				if(i!=0){
					sb.append(" union all ");
				}
			 sb.append(" select nvl(sum(t.tot_cost),0)  as cost1,0 as cost2, t.execute_deptCode as deptCode,to_char(t.FEE_DATE, 'yyyy-mm') as mon ");
			 sb.append(" from ").append(tnL.get(i)).append(" t  where ");
			 sb.append(" t.FEE_DATE >= to_date('"+begin+" 00:00:00','yyyy-mm-dd hh24:mi:ss') ");
			 sb.append(" and t.FEE_DATE <= to_date('"+end+" 23:59:59','yyyy-mm-dd hh24:mi:ss') ");
			 sb.append(" and t.senddrug_flag = 1 and t.stop_flg = 0 and t.del_flg = 0 ");
			 sb.append(" group by t.execute_deptCode,t.FEE_DATE ");
		 }
		 sb.append(" union all ");
		 for (int j = 0; j < tnL2.size(); j++) {
				if(j!=0){
					sb.append(" union all ");
				}
			 sb.append(" select 0 as cost1,nvl(sum(t.tot_cost),0) as cost2, t.execute_deptCode as deptCode,to_char(t.FEE_DATE, 'yyyy-mm') as mon ");
			 sb.append(" from ").append(tnL2.get(j)).append(" t where ");
			 sb.append(" t.FEE_DATE >= to_date('"+begin+" 00:00:00','yyyy-mm-dd hh24:mi:ss')");
			 sb.append(" and t.FEE_DATE <= to_date('"+end+" 23:59:59','yyyy-mm-dd hh24:mi:ss')");
			 sb.append(" and t.send_flag = 1 and t.stop_flg = 0 and t.del_flg = 0 ");
			 sb.append(" group by t.execute_deptCode,t.FEE_DATE ");
		 }
		 sb.append(" ) a group by a.deptCode,a.mon ) b");
		 sb.append(" left join T_DEPARTMENT o on o.DEPT_CODE=b.deptCode ");
		 List<OutpatientUseMedicVo> voList = (List<OutpatientUseMedicVo>) this.getHibernateTemplate().execute(new HibernateCallback() {
				@Override
				public List<OutpatientUseMedicVo> doInHibernate(Session session) throws HibernateException,SQLException {
					SQLQuery queryObject = session.createSQLQuery(sb.toString());
					queryObject.addScalar("cost1",Hibernate.DOUBLE)
					           .addScalar("cost2",Hibernate.DOUBLE)
					           .addScalar("regDpcdName",Hibernate.STRING)
					           .addScalar("selectTime",Hibernate.STRING);
					return queryObject.setResultTransformer(Transformers.aliasToBean(OutpatientUseMedicVo.class)).list();
				}
			});
			if(voList!=null&&voList.size()>0){
				return voList;
			}
			return new ArrayList<OutpatientUseMedicVo>();
	}
	/**  
	 * 
	 *  住院收入分析（new）,根据时间和科室查
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月22日 上午10:49:54 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月22日 上午10:49:54 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public OutpatientUseMedicVo queryOneOutpatientUseMedicVo(
			List<String> tnL, List<String> tnL2, String begin, String end,
			String deptName)  throws Exception{
		if(tnL==null || tnL.size()<0 || tnL2==null||tnL2.size()<0){
			return new OutpatientUseMedicVo();
		}
		 final StringBuffer sb = new StringBuffer();
		 sb.append(" select sum(cost1) as cost1,sum(cost2) as cost2  from ( ");
		 for (int i = 0; i < tnL.size(); i++) {
				if(i!=0){
					sb.append(" union all ");
				}
			 sb.append(" select sum(t.tot_cost) as cost1,0 as cost2 ");
			 sb.append(" from ").append(tnL.get(i)).append(" t  where ");
			 sb.append(" t.execute_deptCode ='"+deptName+"' ");
			 sb.append(" and t.FEE_DATE >= to_date('"+begin+" 00:00:00','yyyy-mm-dd hh24:mi:ss') ");
			 sb.append(" and t.FEE_DATE <= to_date('"+end+" 23:59:59','yyyy-mm-dd hh24:mi:ss') ");
			 sb.append(" and t.senddrug_flag = 1 and t.stop_flg = 0 and t.del_flg = 0 ");
		 }
		 sb.append(" union all ");
		 for (int j = 0; j < tnL2.size(); j++) {
				if(j!=0){
					sb.append(" union all ");
				}
			 sb.append(" select 0 as cost1,sum(t.tot_cost) as cost2 ");
			 sb.append(" from ").append(tnL2.get(j)).append(" t where ");
			 sb.append(" t.execute_deptCode ='"+deptName+"' ");
			 sb.append(" and t.FEE_DATE >= to_date('"+begin+" 00:00:00','yyyy-mm-dd hh24:mi:ss')");
			 sb.append(" and t.FEE_DATE <= to_date('"+end+" 23:59:59','yyyy-mm-dd hh24:mi:ss')");
			 sb.append(" and t.send_flag = 1 and t.stop_flg = 0 and t.del_flg = 0 ");
		 }
		 sb.append(" ) ");
		 OutpatientUseMedicVo vo = (OutpatientUseMedicVo) this.getHibernateTemplate().execute(new HibernateCallback() {
				@Override
				public OutpatientUseMedicVo doInHibernate(Session session) throws HibernateException,SQLException {
					SQLQuery queryObject = session.createSQLQuery(sb.toString());
					queryObject.addScalar("cost1",Hibernate.DOUBLE).addScalar("cost2",Hibernate.DOUBLE);
					return (OutpatientUseMedicVo) queryObject.setResultTransformer(Transformers.aliasToBean(OutpatientUseMedicVo.class)).uniqueResult();
				}
			});
			return vo;
	}
	

}
