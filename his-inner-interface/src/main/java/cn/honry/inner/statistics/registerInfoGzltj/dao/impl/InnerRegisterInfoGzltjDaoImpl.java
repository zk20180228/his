package cn.honry.inner.statistics.registerInfoGzltj.dao.impl;

import java.text.DateFormat;
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

import cn.honry.inner.statistics.registerInfoGzltj.dao.InnerRegisterInfoGzltjDao;
import cn.honry.inner.statistics.registerInfoGzltj.vo.RegisterInnerVo;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;

/**
 * 挂号工作量统计预处理DAO实现类
 * @author user
 *
 */
@Repository("innerRegisterInfoGzltjDao")
@SuppressWarnings({ "all" })
public class InnerRegisterInfoGzltjDaoImpl extends HibernateDaoSupport
		implements InnerRegisterInfoGzltjDao {

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
	public List<RegisterInnerVo> queryRegister(String date){
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
		
			StringBuffer sbf = new StringBuffer("select to_char(t.reg_date,'d') as dateXq,");
			sbf.append(" t.dept_code as deptCode,t.dept_name as deptName,count(t.id) as num, sum(t.sum_cost) as cost ");
			sbf.append(" from "+tableName+" t where t.in_state = 0 and t.stop_flg=0 and t.del_flg =0 ");
			sbf.append(" and t.reg_date between to_date('"+date+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss') ");
			sbf.append(" and to_date('"+date+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss')");
			sbf.append(" group by t.dept_code,t.dept_name,to_char(t.reg_date,'d')");
			List<RegisterInnerVo> list = getSessionFactory().getCurrentSession().createSQLQuery(sbf.toString())
					.addScalar("dateXq",Hibernate.STRING)
					.addScalar("deptCode",Hibernate.STRING)
					.addScalar("deptName",Hibernate.STRING)
					.addScalar("num",Hibernate.INTEGER)
					.addScalar("cost",Hibernate.DOUBLE)
			.setResultTransformer(Transformers.aliasToBean(RegisterInnerVo.class)).list();
			return list;
		}catch(Exception e){
			e.printStackTrace();
		}
		return new ArrayList<RegisterInnerVo>();
	}
	
	/**
	 * 保存方法
	 * @param obj
	 */
	public void save(Object obj){
		getHibernateTemplate().save(obj);
	}
}
