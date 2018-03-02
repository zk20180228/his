package cn.honry.statistics.emr.emrStat.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.DrugOutstoreNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.statistics.drug.admissionStatistics.dao.AdmissionStatisticsDAO;
import cn.honry.statistics.drug.admissionStatistics.vo.AdmissionStatisticsVo;
import cn.honry.statistics.emr.emrStat.dao.EmrStatDAO;
import cn.honry.statistics.emr.emrStat.vo.EmrBaseVo;

/***
 * 电子病历统计DAO实现层
 * @Description:
 * @author: dutianlaing
 * @CreateDate: 2016年6月22日 
 * @version 1.0
 */
@Repository("emrStatDAO")
@SuppressWarnings({ "all" })
public class EmrStatDaoImpl extends HibernateEntityDao<AdmissionStatisticsVo> implements EmrStatDAO{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public Integer getNum(String beginTime, String endTime, int type) {
		String sql = "select count(1) from t_emr_base t where t.firstsubmitdate between to_date('" + beginTime + "','yyyy/mm/dd') and to_date('" + endTime + "','yyyy/mm/dd')";
		switch (type) {
		case 1://输血
			sql += " and t.blood_other <>'0'";
			break;
		case 2://危重
			sql += " and t.ever_sickintodeath = '1'";
			break;
		case 3://死亡
			sql += " and t.leave_type = '5'";
			break;
		case 4://检查
			sql += " and t.yn_first = '0'";
			break;
		case 5://手术
			sql += " and t.operation_code is not null";
			break;
		case 6://抗生素
			sql += " and t.reaction_liquid = '2'";
			break;
		default:
			return 0;
		}
		Object count=getSession().createSQLQuery(sql).uniqueResult();
		if(count == null){
			return 0;
		}
		return Integer.valueOf(count.toString());
	}
	
	
	@Override
	public List<EmrBaseVo> getList(String beginTime, String endTime, int type,
			String page, String rows) {
		String sql = "select t.name name,d.code_name sex,t.inpatient_no inpatientNo,t.dept_innm deptInnm,e.employee_name firstSubmitOper,";
		sql = sql + "t.firstsubmitdate firstSubmitDate from t_emr_base t "
				+ "left join t_business_dictionary d on t.sex_code = d.code_encode and d.code_type = 'sex' "
				+ "left join t_employee e on t.firstsubmitoper = e.employee_jobno and e.del_flg = 0 and e.stop_flg = 0 "
				+ "where t.firstsubmitdate between to_date('" + beginTime + "','yyyy/mm/dd') and to_date('" + endTime + "','yyyy/mm/dd')";
		switch (type) {
		case 1://输血
			sql += " and t.blood_other <>'0'";
			break;
		case 2://危重
			sql += " and t.ever_sickintodeath = '1'";
			break;
		case 3://死亡
			sql += " and t.leave_type = '5'";
			break;
		case 4://检查
			sql += " and t.yn_first = '0'";
			break;
		case 5://手术
			sql += " and t.operation_code is not null";
			break;
		case 6://抗生素
			sql += " and t.reaction_liquid = '2'";
			break;
		case 7://全部
			sql += " and (t.blood_other <>'0' or t.ever_sickintodeath = '1' or t.leave_type = '5'"
					+ " or t.yn_first = '0' or t.operation_code is not null or t.reaction_liquid = '2')";
			break;
		default:
			return new ArrayList<EmrBaseVo>();
		}
		
		int start = Integer.parseInt(page==null?"1":page);
		int count = Integer.parseInt(rows==null?"20":rows);
		
		List<EmrBaseVo> list = getSession().createSQLQuery(sql).addScalar("name").addScalar("sex").addScalar("inpatientNo")
				.addScalar("deptInnm").addScalar("firstSubmitOper").addScalar("firstSubmitDate", Hibernate.DATE)
				.setFirstResult((start - 1) * count).setMaxResults(count)
				.setResultTransformer(Transformers.aliasToBean(EmrBaseVo.class)).list();
		if(list != null && list.size() >= 0){
			return list;
		}
		return new ArrayList<EmrBaseVo>();
	}
	@Override
	public Integer getCount(String beginTime, String endTime, int type) {
		String sql = "select count(1) from t_emr_base t "
				+ "left join t_business_dictionary d on t.sex_code = d.code_encode and d.code_type = 'sex' "
				+ "left join t_employee e on t.firstsubmitoper = e.employee_jobno and e.del_flg = 0 and e.stop_flg = 0 "
				+ "where t.firstsubmitdate between to_date('" + beginTime + "','yyyy/mm/dd') and to_date('" + endTime + "','yyyy/mm/dd')";
		switch (type) {
		case 1://输血
			sql += " and t.blood_other <>'0'";
			break;
		case 2://危重
			sql += " and t.ever_sickintodeath = '1'";
			break;
		case 3://死亡
			sql += " and t.leave_type = '5'";
			break;
		case 4://检查
			sql += " and t.yn_first = '0'";
			break;
		case 5://手术
			sql += " and t.operation_code is not null";
			break;
		case 6://抗生素
			sql += " and t.reaction_liquid = '2'";
			break;
		case 7://全部
			sql += " and (t.blood_other <>'0' or t.ever_sickintodeath = '1' or t.leave_type = '5'"
					+ " or t.yn_first = '0' or t.operation_code is not null or t.reaction_liquid = '2')";
			break;
		default:
			return 0;
		}
		
		Object count = getSession().createSQLQuery(sql).uniqueResult();
		if(count == null){
			return 0;
		}
		return Integer.valueOf(count.toString());
	}
	
}
