package cn.honry.oa.patInformation.dao.impl;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.business.Page;
import cn.honry.base.bean.model.InformationAttachDownAuth;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.SysRole;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.oa.information.dao.InformationAttachDownAuthDao;
import cn.honry.oa.patInformation.dao.PatInformationAttachDownAuthDao;
import cn.honry.utils.ShiroSessionUtils;
@Repository("patInformationAttachDownAuthDao")
@SuppressWarnings({"all"})
public class PatInformationAttachDownAuthDaoImpl extends HibernateEntityDao<InformationAttachDownAuth>
		implements PatInformationAttachDownAuthDao {
	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<InformationAttachDownAuth> getAuthByMenuID(String infoid,boolean flag) {
		String deptCode = "";
		String dutyCode = "";
		String titleCode = "";
		String acount = "";
		SysEmployee employee = ShiroSessionUtils.getCurrentEmployeeFromShiroSession();
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		SysRole role = ShiroSessionUtils.getCurrentUserLoginRoleFromShiroSession();
		if(employee!=null){
			deptCode = employee.getDeptCode();
			dutyCode = employee.getPost();
			titleCode = employee.getTitle();
		}
		if(user!=null){
			acount = user.getAccount();
		}
		StringBuffer sb = new StringBuffer();
		sb.append(" from InformationAttachDownAuth where informationId = ? ");
		if(flag){
			sb.append(" and ( type=0 or (type = 1 and code = ?) or (type=2 and code=?) or (type=3 or code = ?) or (type=4 and code = ?) ) ");
			return super.find(sb.toString(), infoid,acount,titleCode,deptCode,dutyCode);
		}else{
			return super.find(sb.toString(), infoid);
		}
	}

	@Override
	public int delAuth(final String munuid) {
		final StringBuffer sb = new StringBuffer();
		sb.append(" delete from T_OA_INFORMATION_DOWNAURH t where t.INFORMATIONID = :munuid ");
		int num = this.getHibernateTemplate().execute(new HibernateCallback<Integer>() {

			@Override
			public Integer doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sb.toString());
				query.setParameter("munuid", munuid);
				return query.executeUpdate();
			}
		});
		return num;
	}

	@Override
	public int deleteFile(final String fileurl) {
		final StringBuffer sb = new StringBuffer();
		sb.append(" delete from T_OA_INFORMATION_DOWNAURH t where t.FILEURL = :fileurl ");
		int num = this.getHibernateTemplate().execute(new HibernateCallback<Integer>() {

			@Override
			public Integer doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sb.toString());
				query.setParameter("fileurl", fileurl);
				return query.executeUpdate();
			}
		});
		return num;
	}
	
}
