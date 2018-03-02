package cn.honry.oa.messagersend.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.oa.messagersend.dao.MessagerSendDao;

@Repository("messagerSendDao")
public class MessagerSendDaoImpl extends HibernateEntityDao<SysEmployee> implements MessagerSendDao {
	// 为父类HibernateDaoSupport注入sessionFactory的值
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<String> getEmployeBysome( String dutyCode,String titleCode,String account,String deptCode) {
		final StringBuffer sb = new StringBuffer();
		Boolean flag = false;
		if(StringUtils.isBlank(dutyCode)&&StringUtils.isBlank(titleCode)&&StringUtils.isBlank(account)&&StringUtils.isBlank(deptCode)){
			return new ArrayList<String>();
		}else{
			if(StringUtils.isBlank(dutyCode)&&StringUtils.isBlank(titleCode)&&StringUtils.isBlank(account)){
				flag = false;
			}else{
				flag = true;
				sb.append("Select distinct t.employee_mobile as mobile From t_employee t Where t.stop_flg = 0 And t.del_flg = 0 ");
//				if(StringUtils.isNotBlank(dutyCode)&&StringUtils.isNotBlank(titleCode)){
					sb.append(" and (t.employee_post in('"+dutyCode+"') or t.employee_title in('"+titleCode+"') or t.EMPLOYEE_JOBNO in ('"+account+"'))");
//				}else if(StringUtils.isNotBlank(dutyCode)){
//					sb.append(" and t.employee_post='"+dutyCode+"'");
//				}else if(StringUtils.isNotBlank(titleCode)){
//					sb.append(" and t.employee_title='"+titleCode+"'");
//				}
			}
			if(StringUtils.isNotBlank(deptCode)){
				if(flag){
					sb.append(" union ");
				}
				sb.append("Select distinct e.employee_mobile as mobile From t_employee e Left Join T_DEPARTMENT d On e.dept_id = d.dept_id");
				sb.append(" Where  d.dept_code in ('"+deptCode+"') And e.stop_flg = 0 And e.del_flg = 0 And d.stop_flg = 0 And d.del_flg = 0 ");
			}
			
		}
		@SuppressWarnings({ "unchecked", "deprecation" })
		List<String> list = this.getHibernateTemplate().execute(new HibernateCallback<List<String>>() {

			@Override
			public List<String> doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sb.toString());
				return query.list();
			}
		});
		if(list!=null && list.size()>0){
			return list;
		}
		return new ArrayList<String>();
	}

	@Override
	public List<SysEmployee> getAllEmployee() {
		StringBuffer sb = new StringBuffer();
		sb.append("from SysEmployee t where t.stop_flg = 0 and t.del_flg = 0");
		List<SysEmployee> list = super.find(sb.toString());
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<SysEmployee>();
	}
	
}
