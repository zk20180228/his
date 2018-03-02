package cn.honry.statistics.deptstat.criticallyPatientsAnalyse.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.system.userMenuDataJuris.dao.DataJurisInInterDAO;
import cn.honry.statistics.deptstat.criticallyPatientsAnalyse.dao.CriticallyPatientsAnalyseDao;
import cn.honry.statistics.deptstat.criticallyPatientsAnalyse.vo.CriticallyPatientsVO;
import cn.honry.utils.NumberUtil;
import cn.honry.utils.ShiroSessionUtils;

@Repository("criticallyPatientsAnalyseDao")
@SuppressWarnings("all")
public class CriticallyPatientsAnalyseDaoImpl extends HibernateEntityDao<CriticallyPatientsVO> implements CriticallyPatientsAnalyseDao{

		@Resource(name = "sessionFactory")
		public void setSuperSessionFactory(SessionFactory sessionFactory) {
			super.setSessionFactory(sessionFactory);
		}
		@Resource
		private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
		@Autowired
		@Qualifier(value="dataJurisInInterDAO")
		private DataJurisInInterDAO dataJurisInInterDAO;
		
		public void setDataJurisInInterDAO(DataJurisInInterDAO dataJurisInInterDAO) {
				this.dataJurisInInterDAO = dataJurisInInterDAO;
			}
	@Override
	public List<CriticallyPatientsVO> queryCriticallyPatients(String deptName,String begin,String end,String menuAlias) throws Exception {
		StringBuilder sb = new StringBuilder();
		//TODO 待用
//		sb.append("select ti.deptCode as deptName,sum(ti.total) as allPatient,sum(ti.cost) as aveCost,"
//				+ "sum(ti.aveInpatient) as aveInpatient,sum(ti.solo) as solo,sum(ti.cure) as cure,sum(ti.better) as better, sum(ti.nocure) as nocure, "
//				+ "sum(ti.death) as death, sum(ti.other) as other from (");
//		sb.append("select  t.dept_code deptCode,count(1) total,sum(t.BALANCE_PREPAY) cost, sum(trunc(t.out_date - t.in_date )) aveInpatient,");
//		sb.append("sum(case when b.diag_kind = '3' then 1 else 0 end) solo,");
//		sb.append("sum(case when b.diag_outstate = '0' then 1 else 0 end) cure,");
//		sb.append("sum(case when b.diag_outstate = '1' then 1 else 0 end) better,");
//		sb.append("sum(case when b.diag_outstate = '2' then 1 else 0 end) nocure,");
//		sb.append("sum(case when b.diag_outstate = '3' then 1 else 0 end) death,");
//		sb.append("sum(case when b.diag_outstate = '4' then 1 else 0 end) other ");
//		sb.append("FROM T_INPATIENT_INFO t ");
//		sb.append(" left join t_business_diagnose b on t.inpatient_no = b.inpatient_no ");
//		if (StringUtils.isNotBlank(deptName)) {
//			sb.append(" where t.dept_Code = "+ deptName);
//		}
//		sb.append(" group by t.dept_code");
//		sb.append(" union all ");
//		
//		sb.append("select  t1.dept_code deptCode,count(1) total,sum(t1.BALANCE_PREPAY) cost, sum(trunc(t1.out_date - t1.in_date )) aveInpatient,");
//		sb.append("sum(case when b.diag_kind = '3' then 1 else 0 end) solo,");
//		sb.append("sum(case when b.diag_outstate = '0' then 1 else 0 end) cure,");
//		sb.append("sum(case when b.diag_outstate = '1' then 1 else 0 end) better,");
//		sb.append("sum(case when b.diag_outstate = '2' then 1 else 0 end) nocure,");
//		sb.append("sum(case when b.diag_outstate = '3' then 1 else 0 end) death,");
//		sb.append("sum(case when b.diag_outstate = '4' then 1 else 0 end) other ");
//		sb.append("FROM T_INPATIENT_INFO_NOW t1 ");
//		sb.append(" left join t_business_diagnose b on t1.inpatient_no = b.inpatient_no");
//		if (StringUtils.isNotBlank(deptName)) {
//			sb.append(" where t1.dept_Code = "+ deptName);
//		}
//		sb.append(" group by t1.dept_code) ti ");
//		sb.append(" group by ti.deptCode");
		
		sb.append("Select deptNo As deptNo,nvl((select t.dept_name from t_department t where t.dept_code=deptName),'合计') As deptName,sum(num) As num,sum(cure) As cure,sum(better) As better,");
		sb.append("sum(nocure) As nocure,sum(death) As death,sum(other) As other,trim(to_char(sum(curePercent),9999999990.99)||'%') As curePercent,");
		sb.append("trim(to_char(sum(deathPercent),9999999990.99)||'%') As deathPercent,sum(aveInpatient) As aveInpatient,sum(aveCost) As aveCost,sum(solo) As solo,");
		sb.append("sum(allPatient) As allPatient,to_char(sum(numPercent),'0.99')||'%' As numPercent ");
		sb.append("From (select deptNo deptNo,");
		sb.append("nvl(deptNo,'合计') deptName,num,cure,better,nocure,death,other,curePercent,deathPercent,aveInpatient,aveCost,solo,allPatient,numPercent ");
		sb.append("from "
				+ "(select cb.dept_code deptNo,cb.dept_name deptName,num,cure,better,nocure,");
		sb.append("death,(num - cure - better - nocure - death) other,decode(nvl(num,0),0,0,round(((num - better - nocure - death) * 100) / decode(num,0,1,num),1)) curePercent,");
		sb.append("round(((death) * 100) / num, 1) deathPercent,");
		sb.append("round(countDay / num, 1) aveInpatient,");
		sb.append("round(AllCost / num, 1) aveCost,");
		sb.append("solo,allPatient, round((num * 100) / allPatient, 2) numPercent ");
		sb.append("from (select BB.*,(select sum(ccg.out_normal) from t_inpatient_dayreport ccg ");
		sb.append("where ccg.date_stat >= TO_DATE('"+begin+" 0:00:00','yyyy-mm-dd hh24:mi:ss') ");
		sb.append("and ccg.date_stat <=TO_DATE('"+end+" 23:59:59','yyyy-mm-dd hh24:mi:ss')) allPatient,");
		sb.append("row_number() over(partition by BB.dept_code order by num desc) as row_index ");
		sb.append("from (select cg.dept_code dept_code,cg.dept_name,count(1) num,sum(cg.pi_days) countDay,sum(cg.feesCost) AllCost,");
		sb.append("sum(DECODE(cg.zg, '1', 1, 0)) cure,sum(DECODE(cg.zg, '2', 1, 0)) better,sum(DECODE(cg.zg, '3', 1, 0)) nocure, sum(DECODE(cg.zg, '4', 1, 0)) death,");
		sb.append("sum(decode(cg.OPS_COMPLICATIONS,'2',1,0)) solo ");
		sb.append("from (select BB.*,mcg.OPS_COMPLICATIONS,mcg.OPS_COMPLICATIONS_DESC,mcg.MED_REACTIONS,");
		sb.append("mcg.MED_REACTIONS_DESC,mcg.HOS_INFECTION,mcg.HOS_INFECTION_DESC,mcg.DRUG_REACTIONS,mcg.DRUG_REACTIONS_DESC,");
		sb.append("mcg.TUMORSTAGE_T,mcg.TUMORSTAGE_N,mcg.TUMORSTAGE_M,mcg.CLINICAL_PATHWAYS,mcg.VENTILATORUSE,");
		sb.append("mcg.VENTILATORUSETIME,mcg.OPS_ELECTIVE,mcg.ZZ_HXJXGFY, mcg.ZZ_XLGR,mcg.ZZ_NNXTGR ");
		sb.append("from (select c.*,(nvl(fii.tot_cost,0) + nvl(fii.balance_cost,0)) feesCost ");
		sb.append("from t_emr_base c left join t_inpatient_info fii on c.inpatient_no = fii.inpatient_no  ");
		if(StringUtils.isNotBlank(deptName)){
			sb.append("where c.DEPT_CODE in('").append(deptName.replace(",", "','")).append("') ");
		}else{
			sb.append("where c.DEPT_CODE in(").append(dataJurisInInterDAO.getJurisDeptSql(menuAlias,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount())).append(") ");
		}
		sb.append(" ) BB ");
		sb.append("left join t_emr_base_ext mcg on mcg.inpatient_no =BB.inpatient_no) cg ");
		sb.append("where trunc(cg.out_date) >=TO_DATE('"+begin+" 00:00:00','yyyy-mm-dd hh24:mi:ss') ");
		sb.append("and trunc(cg.out_date) <=TO_DATE('"+end+" 23:59:59','yyyy-mm-dd hh24:mi:ss') ");
		sb.append("and cg.case_stus in ('3', '4') and (cg.medical_type in ('D', 'C')) ");
		sb.append("and (fun_splitstring(cg.henanadd2012,'|',29) != 'DOC' or fun_splitstring(cg.henanadd2012,'|',29) is null) ");
		sb.append("group by rollup(cg.dept_code) , cg.dept_name) BB) CB) AA ");
		sb.append(") group by deptName,deptNo order by decode(deptName,'合计',1,0) asc ");
		
		 SQLQuery sqlQuery= super.getSession().createSQLQuery(sb.toString()).addScalar("deptName").addScalar("deptNo")
				.addScalar("aveCost",Hibernate.DOUBLE).addScalar("cure",Hibernate.INTEGER).addScalar("better",Hibernate.INTEGER)
				.addScalar("nocure",Hibernate.INTEGER).addScalar("death",Hibernate.INTEGER).addScalar("other",Hibernate.INTEGER)
				.addScalar("solo",Hibernate.INTEGER).addScalar("aveInpatient",Hibernate.DOUBLE).addScalar("allPatient",Hibernate.INTEGER).addScalar("numPercent")
				.addScalar("curePercent").addScalar("deathPercent").addScalar("num",Hibernate.INTEGER);
		 List<CriticallyPatientsVO> list=sqlQuery.setResultTransformer(Transformers.aliasToBean(CriticallyPatientsVO.class)).list();
//		 Iterator<CriticallyPatientsVO> iterator = list.iterator();
//		while (iterator.hasNext()) {
//			CriticallyPatientsVO vo = iterator.next();
//			if (vo.getCure()+vo.getBetter()+vo.getNocure()+vo.getDeath()+vo.getOther() == 0) {
//				iterator.remove();
//			}
//		}
//		int a = this.queryALL();
//		for (CriticallyPatientsVO vo : list) {
//			vo.setAllPatient(a);
//			vo.setNumber(vo.getCure()+vo.getBetter()+vo.getNocure()+vo.getDeath()+vo.getOther());
//			if(vo.getNumber() != 0 && vo.getAllPatient() != 0){
//				int b = vo.getNumber();
//				NumberUtil num = NumberUtil.init();
//				vo.setCurePercent(num.format(((double)((vo.getCure()+vo.getOther()))/b)*100, 2));
//				vo.setDeathPercent(num.format(((double)vo.getDeath()/vo.getNumber())*100, 2));
//				vo.setAveCost(Double.valueOf(num.format(vo.getAveCost()/vo.getNumber(), 2)));
//				vo.setNumPercent(num.format(((double)b/vo.getAllPatient())*100, 2));
//				vo.setAveInpatient(Double.valueOf(num.format(vo.getAveInpatient()/vo.getNumber(), 2)));
//			}
//		}
		 
		 if(list.size()>0){
			 int len=list.size()-1;
			 CriticallyPatientsVO vo=list.get(len);
			 vo.setCurePercent(NumberUtil.init().format(Double.valueOf(vo.getCurePercent().replace("%", ""))/len,2)+"%");
			 vo.setDeathPercent(NumberUtil.init().format(Double.valueOf(vo.getDeathPercent().replace("%", ""))/len,2)+"%");
			 vo.setAveInpatient(Double.valueOf(NumberUtil.init().format(vo.getAveInpatient()/len,2)));
			 vo.setAllPatient(list.get(len-1).getAllPatient());
//			 list.add(len,vo);
			 return list;
		 }
		return new ArrayList<CriticallyPatientsVO>();
	}
	@Override
	public List<SysDepartment> queryDeptList()  throws Exception{
		String hql="from SysDepartment where del_flg=0 and stop_flg=0";
		List<SysDepartment> dl=super.find(hql, null);
		if(dl!=null&&dl.size()>=0){
			return dl;
		}
		return null;
	}
	@Override
	public void initMongoDb()  throws Exception{
		
		StringBuilder sb = new StringBuilder();
		sb.append("select ti.deptCode as deptName,sum(ti.total) as allPatient,sum(ti.cost) as aveCost,"
				+ "sum(ti.aveInpatient) as aveInpatient,sum(ti.solo) as solo,sum(ti.cure) as cure,sum(ti.better) as better, sum(ti.nocure) as nocure, "
				+ "sum(ti.death) as death, sum(ti.other) as other from (");
		sb.append("select  t.dept_code deptCode,count(1) total,sum(t.BALANCE_PREPAY) cost, sum(trunc(t.out_date - t.in_date )) aveInpatient,");
		sb.append("sum(case when b.diag_kind = '3' then 1 else 0 end) solo,");
		sb.append("sum(case when b.diag_outstate = '0' then 1 else 0 end) cure,");
		sb.append("sum(case when b.diag_outstate = '1' then 1 else 0 end) better,");
		sb.append("sum(case when b.diag_outstate = '2' then 1 else 0 end) nocure,");
		sb.append("sum(case when b.diag_outstate = '3' then 1 else 0 end) death,");
		sb.append("sum(case when b.diag_outstate = '4' then 1 else 0 end) other ");
		sb.append("FROM T_INPATIENT_INFO t left join t_business_diagnose b on t.inpatient_no = b.inpatient_no group by t.dept_code");
		sb.append(" union all ");
		
		sb.append("select  t1.dept_code deptCode,count(1) total,sum(t1.BALANCE_PREPAY) cost, sum(trunc(t1.out_date - t1.in_date )) aveInpatient,");
		sb.append("sum(case when b.diag_kind = '3' then 1 else 0 end) solo,");
		sb.append("sum(case when b.diag_outstate = '0' then 1 else 0 end) cure,");
		sb.append("sum(case when b.diag_outstate = '1' then 1 else 0 end) better,");
		sb.append("sum(case when b.diag_outstate = '2' then 1 else 0 end) nocure,");
		sb.append("sum(case when b.diag_outstate = '3' then 1 else 0 end) death,");
		sb.append("sum(case when b.diag_outstate = '4' then 1 else 0 end) other ");
		sb.append("FROM T_INPATIENT_INFO_NOW t1 left join t_business_diagnose b on t1.inpatient_no = b.inpatient_no group by t1.dept_code) ti ");
		sb.append("group by ti.deptCode");
		
		
		
		List<CriticallyPatientsVO> list = super.getSession().createSQLQuery(sb.toString()).addScalar("deptName")
				.addScalar("aveCost",Hibernate.DOUBLE).addScalar("cure",Hibernate.INTEGER).addScalar("better",Hibernate.INTEGER)
				.addScalar("nocure",Hibernate.INTEGER).addScalar("death",Hibernate.INTEGER).addScalar("other",Hibernate.INTEGER)
				.addScalar("solo",Hibernate.INTEGER).addScalar("aveInpatient",Hibernate.DOUBLE).addScalar("allPatient",Hibernate.INTEGER)
				.setResultTransformer(Transformers.aliasToBean(CriticallyPatientsVO.class)).list();
		Iterator<CriticallyPatientsVO> iterator = list.iterator();
		while (iterator.hasNext()) {
			CriticallyPatientsVO vo = iterator.next();
			if (vo.getCure()+vo.getBetter()+vo.getNocure()+vo.getDeath()+vo.getOther() == 0) {
				iterator.remove();
			}
		}
		int a = this.queryALL();
		for (CriticallyPatientsVO vo : list) {
			vo.setAllPatient(a);
			vo.setNumber(vo.getCure()+vo.getBetter()+vo.getNocure()+vo.getDeath()+vo.getOther());
			if(vo.getNumber() != 0 && vo.getAllPatient() != 0){
				int b = vo.getNumber();
				NumberUtil num = NumberUtil.init();
				vo.setCurePercent(num.format(((double)((vo.getCure()+vo.getOther()))/b)*100, 2));
				vo.setDeathPercent(num.format(((double)vo.getDeath()/vo.getNumber())*100, 2));
				vo.setAveCost(Double.valueOf(num.format(vo.getAveCost()/vo.getNumber(), 2)));
				vo.setNumPercent(num.format(((double)b/vo.getAllPatient())*100, 2));
				vo.setAveInpatient(Double.valueOf(num.format(vo.getAveInpatient()/vo.getNumber(), 2)));
			}
		}
		
		if(list!=null && list.size()>=0){
			 List<DBObject> userList = new ArrayList<DBObject>();
				for(CriticallyPatientsVO vo:list){
					Document document1 = new Document();
					document1.append("deptName", vo.getDeptName());//科室
					Document document = new Document();
					document.append("number", vo.getNumber());
					document.append("cure", vo.getCure());
					document.append("better", vo.getBetter());
					document.append("nocure", vo.getNocure());
					document.append("death", vo.getDeath());
					document.append("other", vo.getOther());
					document.append("curePercent", vo.getCurePercent());
					document.append("deathPercent", vo.getDeathPercent());
					document.append("aveInpatient", vo.getAveInpatient());
					document.append("aveCost", vo.getAveCost());
					document.append("solo", vo.getSolo());
					document.append("allPatient", vo.getAllPatient());
					document.append("numPercent", vo.getNumPercent());
					new MongoBasicDao().update("WZYNHZRSBLTJFXDB", document1, document, true);
				}
		 }
		
	}
	
	public Integer queryALL() throws Exception{
		StringBuilder sb = new StringBuilder();
		sb.append("select sum(count) allPatient  from(");
		sb.append("select count(1) count from T_INPATIENT_INFO ");
		sb.append(" union ALL ");
		sb.append("select count(1) count from T_INPATIENT_INFO_now)");
		List<CriticallyPatientsVO> list = super.getSession().createSQLQuery(sb.toString()).addScalar("allPatient",Hibernate.INTEGER)
				.setResultTransformer(Transformers.aliasToBean(CriticallyPatientsVO.class)).list();
		if (list!=null && list.size()>=0) {
			return list.get(0).getAllPatient();
		}
		return 0;
	}
	@Override
	public List<CriticallyPatientsVO> queryCriticallyPatientsFromMongo()  throws Exception{
		new MongoBasicDao().findAlldata("WZYNHZRSBLTJFXDB", new BasicDBObject());
		return null;
	}

}