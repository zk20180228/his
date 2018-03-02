package cn.honry.statistics.bi.bistac.outpatientUseMedic.dao.impl;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.statistics.bi.bistac.monthlyDashboard.vo.MonthlyDashboardVo;
import cn.honry.statistics.bi.bistac.outpatientStac.dao.OutpatientStacVoDao;
import cn.honry.statistics.bi.bistac.outpatientStac.vo.OutpatientStacVo;
import cn.honry.statistics.bi.bistac.outpatientUseMedic.dao.OutpatientUseMedicDao;
import cn.honry.statistics.bi.bistac.outpatientUseMedic.vo.OutpatientUseMedicVo;
import cn.honry.statistics.bi.bistac.treatmentEffect.vo.TreatmentEffectVo;
import cn.honry.utils.DateUtils;

@Repository("outpatientUseMedicDao")
@SuppressWarnings({ "all" })
public class OutpatientUseMedicDaoImpl extends HibernateEntityDao<OutpatientUseMedicVo> implements OutpatientUseMedicDao{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	//把数据存入mongodb中
		public static final String TABLENAME1 = "YZB";//药占比
	/**  
	 * 
	 * 门诊药品收入 和 门诊总收入
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月12日 下午5:33:37 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月12日 下午5:33:37 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public OutpatientUseMedicVo queryCost(List<String> tnL,final String begin,final String end) {
		if(tnL==null||tnL.size()<0){
			return new OutpatientUseMedicVo();
		}
		final StringBuffer sb = new StringBuffer();
		sb.append("select nvl(sum(drugFee),0) as drugFee,nvl(sum(totCost),0) as totCost,nvl(sum(total),0) as total from ( ");
		for (int i = 0; i < tnL.size(); i++) {
		if(i!=0){
			sb.append(" union all ");
		}
		sb.append(" select distinct sum(decode(t.Drug_Flag, '1', t.tot_cost, 0)) as drugFee,sum(t.tot_cost) as totCost,count(t.CLINIC_CODE) as total from ").append(tnL.get(i)).append(" t ");
		sb.append(" where ");
		sb.append(" t.fee_date >= to_date(:begin,'yyyy-mm-dd hh24:mi:ss') ");
		sb.append(" and t.fee_date <= to_date(:end,'yyyy-mm-dd hh24:mi:ss') ");
		sb.append(" and t.PAY_FLAG != 0  and t.TRANS_TYPE=1 and t.CANCEL_FLAG = 1  and t.stop_flg = 0 and t.del_flg = 0 ");
		}
		sb.append(" ) ");
		OutpatientUseMedicVo vo = (OutpatientUseMedicVo) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public OutpatientUseMedicVo doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sb.toString());
				queryObject.addScalar("totCost",Hibernate.DOUBLE).addScalar("drugFee ",Hibernate.DOUBLE).addScalar("total",Hibernate.INTEGER);
				queryObject.setParameter("begin", begin);
				queryObject.setParameter("end", end);
				return (OutpatientUseMedicVo) queryObject.setResultTransformer(Transformers.aliasToBean(OutpatientUseMedicVo.class)).uniqueResult();
			}
		});
		return vo;
	}
	/**  
	 * 
	 * 最近12月的人均药费
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月13日 上午9:57:27 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月13日 上午9:57:27 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public List<OutpatientUseMedicVo> queryStatisticsCost(List<String> tnL,final String begin, final String end) {
		if(tnL==null||tnL.size()<0){
			return new ArrayList<OutpatientUseMedicVo>();
		}
		final StringBuffer sb = new StringBuffer();
		sb.append("select * from");
		sb.append(" (select sum(totCost) as totCost, sum(total) as total , sum(num) as num,type, selectTime from ( ");
		for (int i = 0; i < tnL.size(); i++) {
			if(i!=0){
				sb.append(" union all ");
			}
			sb.append("select a.total as total,a.totCost as totCost,a.num as num,b.code_name as type,a.mon as selectTime from (");
			sb.append(" select count(distinct t.clinic_code) as total,nvl( sum(t.tot_cost),0) as totCost,to_char(t.reg_date, 'yyyy-mm') as mon,");
			sb.append(" sum(case  when t.ext_flag3 =1 then decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY)) else decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY))/decode(t.pack_qty, NULL,1,DECODE(t.pack_qty, 0, 1, t.pack_qty)) end) as num, d.DRUG_TYPE ");
			sb.append(" from ").append(tnL.get(i)).append(" t  left join t_drug_info d on t.ITEM_CODE = d.DRUG_CODE where ");
			sb.append(" t.fee_date >= to_date('"+begin+" 00:00:00','yyyy-mm-dd hh24:mi:ss') ");
			sb.append(" and t.fee_date <= to_date('"+end+" 23:59:59','yyyy-mm-dd hh24:mi:ss') ");
			sb.append(" and t.PAY_FLAG != 0  and t.DRUG_FLAG = '1' and t.TRANS_TYPE = 1 and t.CANCEL_FLAG = 1 and t.stop_flg = 0 and t.del_flg = 0 group by t.reg_date,DRUG_TYPE");
			sb.append(" )a");
			sb.append("  left join t_business_dictionary b on  b.CODE_ENCODE = a.DRUG_TYPE where b.code_type='drugType'");
		}
		sb.append(" ) group by type,selectTime)");
		List<OutpatientUseMedicVo> voList = (List<OutpatientUseMedicVo>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public List<OutpatientUseMedicVo> doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sb.toString());
				queryObject.addScalar("selectTime",Hibernate.STRING);
				queryObject.addScalar("totCost",Hibernate.DOUBLE);
				queryObject.addScalar("total",Hibernate.INTEGER);
				queryObject.addScalar("num",Hibernate.DOUBLE);
				queryObject.addScalar("type",Hibernate.STRING);
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
	 * 最近12月的人均要用药天数
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月13日 上午9:57:27 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月13日 上午9:57:27 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public List<OutpatientUseMedicVo> queryMedicationDays(List<String> tnL, String begin, String end) {
		if(tnL==null||tnL.size()<0){
			return new ArrayList<OutpatientUseMedicVo>();
		}
		final StringBuffer sb = new StringBuffer();
		sb.append("select * from");
		sb.append("(select selectTime,avg(avgDays) as avgDays from (");
		for (int i = 0; i < tnL.size(); i++) {
			if(i!=0){
				sb.append(" union all ");
			}
			sb.append(" select o.CLINIC_CODE,  to_char(o.REG_DATE, 'yyyy-mm') as selectTime, avg(nvl(case  when o.ext_flag3 = 1 then (case ");
			sb.append("  when t.FREQUENCY_UNIT = 'D' then (decode(o.pack_qty,NULL,1,DECODE(o.pack_qty, 0, 1, o.pack_qty)) * decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) ");
			sb.append(" / (decode(o.dose_once, NULL,1,DECODE(o.dose_once, 0, 1, o.dose_once))/ decode(o.base_dose,NULL,1,DECODE(o.base_dose, 0, 1, o.base_dose))) ");
			sb.append(" / (decode(t.FREQUENCY_NUM, NULL,1,DECODE(t.FREQUENCY_NUM,0,1,t.FREQUENCY_NUM)))) ");
			sb.append(" when t.FREQUENCY_UNIT = 'H' then (decode(o.pack_qty, NULL,1,DECODE(o.pack_qty, 0, 1, o.pack_qty)) *decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) ");
			sb.append(" /(decode(o.dose_once,NULL,1,DECODE(o.dose_once, 0, 1, o.dose_once))/decode(o.base_dose, NULL, 1,DECODE(o.base_dose, 0, 1, o.base_dose))) ");
			sb.append(" /(decode(t.FREQUENCY_NUM,NULL,1,DECODE(t.FREQUENCY_NUM,0,1,t.FREQUENCY_NUM))) / 24)");
			sb.append(" when t.FREQUENCY_UNIT = 'W' then (decode(o.pack_qty,NULL,1, DECODE(o.pack_qty, 0, 1, o.pack_qty)) * decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) * 7  ");
			sb.append(" / (decode(o.dose_once,NULL,1, DECODE(o.dose_once, 0, 1, o.dose_once)) / decode(o.base_dose, NULL,1,DECODE(o.base_dose, 0, 1, o.base_dose))) ");
			sb.append(" / (decode(t.FREQUENCY_NUM,NULL,1,DECODE(t.FREQUENCY_NUM, 0,1, t.FREQUENCY_NUM)))) ");
			sb.append(" end) else(case");
			sb.append(" when t.FREQUENCY_UNIT = 'D' then (decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) ");
			sb.append(" / (decode(o.dose_once,NULL,1, DECODE(o.dose_once, 0, 1, o.dose_once)) / decode(o.base_dose, NULL, 1,DECODE(o.base_dose, 0, 1, o.base_dose))) ");
			sb.append(" / (decode(t.FREQUENCY_NUM, NULL,1,DECODE(t.FREQUENCY_NUM,0,1, t.FREQUENCY_NUM)))) ");
			sb.append(" when t.FREQUENCY_UNIT = 'H' then (decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) ");
			sb.append(" / (decode(o.dose_once, NULL,1,DECODE(o.dose_once, 0, 1, o.dose_once))  / decode(o.base_dose, NULL,1,DECODE(o.base_dose, 0, 1, o.base_dose))) ");
			sb.append(" / (decode(t.FREQUENCY_NUM,NULL,1, DECODE(t.FREQUENCY_NUM,0, 1, t.FREQUENCY_NUM))) / 24)");
			sb.append(" when t.FREQUENCY_UNIT = 'W' then (decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) * 7 ");
			sb.append(" / (decode(o.dose_once, NULL, 1,DECODE(o.dose_once, 0, 1, o.dose_once)) / decode(o.base_dose, NULL,1, DECODE(o.base_dose, 0, 1, o.base_dose))) ");
			sb.append(" / (decode(t.FREQUENCY_NUM,NULL,1,DECODE(t.FREQUENCY_NUM,0,1,t.FREQUENCY_NUM))))");
			sb.append(" end) end, 1)) as avgDays ");
			sb.append(" from ").append(tnL.get(i)).append(" o");
			sb.append(" left join T_BUSINESS_FREQUENCY t on o.FREQUENCY_CODE = t.FREQUENCY_ENCODE ");
			sb.append(" where o.REG_DATE >= to_date('"+begin+" 00:00:00','yyyy-mm-dd hh24:mi:ss')");
			sb.append(" and o.REG_DATE <= to_date('"+end+" 23:59:59','yyyy-mm-dd hh24:mi:ss')");
			sb.append(" and o.PAY_FLAG != 0 and o.DRUG_FLAG = '1' and o.TRANS_TYPE = 1  and o.CANCEL_FLAG = 1 and o.DAYS = 1 and o.stop_flg = 0 and o.del_flg = 0 and t.stop_flg = 0 and t.stop_flg = 0 and t.del_flg = 0  and (o.reg_dpcd = t.createdept or t.createdept = 'ROOT') ");
			sb.append(" group by o.CLINIC_CODE,o.REG_DATE");
			
			}
			sb.append(" ) group by selectTime) ");
			List<OutpatientUseMedicVo> voList = (List<OutpatientUseMedicVo>) this.getHibernateTemplate().execute(new HibernateCallback() {
				@Override
				public List<OutpatientUseMedicVo> doInHibernate(Session session) throws HibernateException,SQLException {
					SQLQuery queryObject = session.createSQLQuery(sb.toString());
					queryObject.addScalar("selectTime",Hibernate.STRING);
					queryObject.addScalar("avgDays",Hibernate.DOUBLE);
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
	 * 医生用药前5名
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月13日 下午4:41:22 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月13日 下午4:41:22 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public List<OutpatientUseMedicVo> queryDoctCost(List<String> tnL, String begin, String end) {
		if(tnL==null||tnL.size()<0){
			return new ArrayList<OutpatientUseMedicVo>();
		}
		final StringBuffer sb = new StringBuffer();
		sb.append("select * from (select a.totCost as totCost, a.num as num, o.employee_name as doctCodeName  from ( ");
		sb.append(" select doct_code as doct_code,sum(totCost) as totCost,sum(num) as num from ( ");
		for (int i = 0; i < tnL.size(); i++) {
		if(i!=0){
			sb.append(" union all ");
		}
			sb.append(" select t.tot_cost as totCost,t.doct_code,(case  when t.ext_flag3 =1 then decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY)) else decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY))/decode(t.pack_qty, NULL,1,DECODE(t.pack_qty, 0, 1, t.pack_qty)) end) as num from ").append(tnL.get(i)).append(" t ").append(" where ");;
			sb.append(" t.fee_date >= to_date('"+begin+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and t.fee_date <= to_date('"+end+" 23:59:59','yyyy-MM-dd hh24:mi:ss')");
			sb.append(" and t.PAY_FLAG != 0 and t.DRUG_FLAG = '1' and t.TRANS_TYPE = 1 and t.CANCEL_FLAG = 1  and t.stop_flg = 0 and t.del_flg = 0 ");
		}
		
		sb.append(" ) group by doct_code ");
		sb.append(" ) a");
		sb.append(" left join T_EMPLOYEE o on a.doct_code = o.employee_jobno  where rownum <= 5 order by totCost desc) ");
		List<OutpatientUseMedicVo> voList = (List<OutpatientUseMedicVo>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public List<OutpatientUseMedicVo> doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sb.toString())
						.addScalar("doctCodeName",Hibernate.STRING).addScalar("totCost",Hibernate.DOUBLE).addScalar("num",Hibernate.DOUBLE);
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
	 * 科室用药前5名
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月13日 下午4:41:22 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月13日 下午4:41:22 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public List<OutpatientUseMedicVo> queryDeptCost(List<String> tnL,
			final String begin, final String end) {
		if(tnL==null||tnL.size()<0){
			return new ArrayList<OutpatientUseMedicVo>();
		}
		final StringBuffer sb = new StringBuffer();
		sb.append("select * from (select a.totCost as totCost, a.num as num, o.DEPT_NAME as regDpcdName  from ( ");
		sb.append(" select DOCT_DEPT as DOCT_DEPT,sum(totCost) as totCost,sum(num) as num from ( ");
		for (int i = 0; i < tnL.size(); i++) {
		if(i!=0){
			sb.append(" union all ");
		}
			sb.append(" select t.tot_cost as totCost,(case  when t.ext_flag3 =1 then decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY)) else decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY))/decode(t.pack_qty, NULL,1,DECODE(t.pack_qty, 0, 1, t.pack_qty)) end) as num,t.DOCT_DEPT from ").append(tnL.get(i)).append(" t ").append(" where ");;
			sb.append(" t.fee_date >= to_date('"+begin+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and t.fee_date <= to_date('"+end+" 23:59:59','yyyy-MM-dd hh24:mi:ss') ");
			sb.append(" and t.PAY_FLAG != 0 and t.DRUG_FLAG = '1' and t.TRANS_TYPE = 1 and t.CANCEL_FLAG = 1 and t.stop_flg=0 and t.del_flg=0 ");
		}
		sb.append(" ) group by DOCT_DEPT)a  ");
		sb.append(" left join T_DEPARTMENT o on a.DOCT_DEPT = o.DEPT_CODE where rownum <= 5 order by totCost desc )");
		List<OutpatientUseMedicVo> voList = (List<OutpatientUseMedicVo>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public List<OutpatientUseMedicVo> doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sb.toString())
						.addScalar("regDpcdName",Hibernate.STRING).addScalar("totCost",Hibernate.DOUBLE).addScalar("num",Hibernate.DOUBLE);
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
	 * 获取门诊处方明细表的最大和最小时间
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月12日 下午5:33:37 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月12日 下午5:33:37 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public OutpatientUseMedicVo findMaxMin() {
		final String sql = "SELECT MAX(mn.REG_DATE) AS eTime ,MIN(mn.REG_DATE) AS sTime FROM t_outpatient_feedetail_now mn";
		OutpatientUseMedicVo vo = (OutpatientUseMedicVo) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public OutpatientUseMedicVo doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sql.toString());
				queryObject.addScalar("eTime",Hibernate.DATE).addScalar("sTime",Hibernate.DATE);
				return (OutpatientUseMedicVo) queryObject.setResultTransformer(Transformers.aliasToBean(OutpatientUseMedicVo.class)).uniqueResult();
			}
		});
		return vo;
	}
	
	
}
