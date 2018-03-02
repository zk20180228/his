package cn.honry.inner.system.quartzTiming.dao.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.RegisterDocSource;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.system.quartzTiming.dao.QuartzTiminginnerDao;
import cn.honry.utils.DateUtils;
@Repository("quartzTiminginnerDao")
@SuppressWarnings({"all"})
public class QuartzTiminginnerDaoImpl extends HibernateEntityDao<RegisterDocSource> implements QuartzTiminginnerDao{
	
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

//	@Override
//	public void saveSql(List<RegisterDocSourceVo> reglist) {
//		String day=DateUtils.formatDateY_M_D_H_M_S(new Date());
//		for(RegisterDocSourceVo li:reglist){
//			String sql="insert into t_register_doc_source values("
//			+ "'"+li.getId()+"','"+li.getEmployeeCode()+"','"+li.getEmployeeName()+"','"+li.getGradeCode()+"',"
//			+ "'"+li.getGradeName()+"','"+li.getDeptCode()+"','"+li.getDeptName()+"','"+li.getMiddayCode()+"',"
//			+ "'"+li.getMiddayName()+"','"+li.getClinicCode()+"','"+li.getClinicName()+"','"+li.getLimitSum()+"',"
//			+ "'"+li.getPeciallimitSum()+"','"+li.getClinicSum()+"','"+li.getAppFlag()+"','"+li.getIsStop()+"',"
//					+ " '"+li.getStopReason()+"', '','',to_date('"+day+"','yyyy-MM-dd HH24:mi:ss'),'','','','',0,0  )";
//			this.getSession().createSQLQuery(sql).executeUpdate();
//		}
//		
//	}
	
	@Override
	public RegisterDocSource haveDoc(String SCHEDULE_DOCTOR, String SCHEDULE_MIDDAY, String SCHEDULE_DATE){
		StringBuffer sb=new StringBuffer();
		String date = DateUtils.formatDateY_M_D(new Date());
		String day=DateUtils.formatDateY_M_D(DateUtils.addDay(DateUtils.getCurrentTime(), 1));
		if(date.equals(SCHEDULE_DATE)){//当天数据
			sb.append("from RegisterDocSource t ");
		}else{//非当天数据
			sb.append("select t.id as id from RegisterDocSource t ");
		}
		sb.append("where t.regDate>=to_date(?,'yyyy-mm-dd') and t.regDate<to_date(?,'yyyy-mm-dd')"
				+ "and t.middayCode=? and t.employeeCode=? and t.stop_flg=0 and t.del_flg=0 ");
		Query query = this.createQuery(sb.toString(),SCHEDULE_DATE,day,SCHEDULE_MIDDAY,SCHEDULE_DOCTOR);
		if(!date.equals(SCHEDULE_DATE)){
			query.setResultTransformer(Transformers.aliasToBean(RegisterDocSource.class));
		}
		List<RegisterDocSource> list =query.list();
		if(list!=null&&list.size()>0){
				return list.get(0);
		}
		return null;
	}

}
