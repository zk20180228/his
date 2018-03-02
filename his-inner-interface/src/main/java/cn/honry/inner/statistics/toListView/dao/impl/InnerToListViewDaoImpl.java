package cn.honry.inner.statistics.toListView.dao.impl;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import cn.honry.inner.statistics.toListView.dao.InnerToListViewDao;
import cn.honry.inner.statistics.toListView.vo.ToListViewVo;
import cn.honry.inner.statistics.totalIncomeCount.vo.MapVo;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;

@Repository("innerToListViewDao")
@SuppressWarnings({ "all" })
public class InnerToListViewDaoImpl  extends HibernateDaoSupport
	implements InnerToListViewDao {

	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	
	@Resource(name = "sessionFactory")
	// 为HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	/**
	 * 查询当天的统计数据
	 * @param date
	 * @return
	 */
	public ToListViewVo queryVo(String date) {
		Date dateGet = DateUtils.parseDateY_M_D(date);
		
		String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date dTime;
		String tableName;
		try {
			dTime = df.parse(df.format(new Date()));
			Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
			if(DateUtils.compareDate(dateGet, cTime)==-1){//传入时间小于最小时间
				tableName = "T_REGISTER_MAIN";
			}else{//传入时间大于最小时间
				tableName = "T_REGISTER_MAIN_NOW";
			}
		
			StringBuffer sb = new StringBuffer();
			sb.append(" select count(t.id) as outpatientD,nvl(sum(case when instr(t.dept_name,'急诊')> 0 then 1 else 0 end),0) as emergencyD");
			sb.append(" from "+tableName+" t");
			sb.append(" where t.valid_flag = 1 and t.in_state = 0");
			sb.append(" and t.trans_type = 1 and t.del_flg = 0 and t.stop_flg = 0");
			sb.append(" and t.reg_date between to_date('"+date+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss')");
			sb.append(" and to_date('"+date+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss')");
			ToListViewVo vo = (ToListViewVo) getSessionFactory().getCurrentSession().createSQLQuery(sb.toString())
					.addScalar("outpatientD",Hibernate.INTEGER)  
					.addScalar("emergencyD",Hibernate.INTEGER) 
			.setResultTransformer(Transformers.aliasToBean(ToListViewVo.class)).uniqueResult();
			return vo;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new ToListViewVo();
	}
	@Override
	public List<MapVo> queryPieVO(String date) {
		Date dateGet = DateUtils.parseDateY_M_D(date);
		
		String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date dTime;
		String tableName;
		try {
			dTime = df.parse(df.format(new Date()));
			Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
			if(DateUtils.compareDate(dateGet, cTime)==-1){//传入时间小于最小时间
				tableName = "T_REGISTER_MAIN";
			}else{//传入时间大于最小时间
				tableName = "T_REGISTER_MAIN_NOW";
			}
		
			StringBuffer sb = new StringBuffer();
			sb.append(" select count(1) as value,d.code_name as name ");
			sb.append(" from "+tableName+" t left join t_business_dictionary d on t.area_code = d.code_encode");
			sb.append(" where d.code_type='hospitalArea' and t.valid_flag = 1 and t.in_state = 0");
			sb.append(" and t.trans_type = 1 and t.del_flg = 0 and t.stop_flg = 0");
			sb.append(" and t.reg_date between to_date('"+date+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss')");
			sb.append(" and to_date('"+date+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss')");
			sb.append(" group by d.code_name");
			List<MapVo> list = getSessionFactory().getCurrentSession().createSQLQuery(sb.toString())
					.addScalar("name",Hibernate.STRING)
					.addScalar("value",Hibernate.DOUBLE)
			.setResultTransformer(Transformers.aliasToBean(MapVo.class)).list();
			return list;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new ArrayList<MapVo>();
	}
}
