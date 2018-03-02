package cn.honry.oa.mail.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.MailConfig;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.oa.mail.dao.MailConfigDao;

@SuppressWarnings({ "deprecation", "unchecked" })
@Repository("mailConfigDao")
public class MailConfigDaoImpl extends HibernateEntityDao<MailConfig> implements MailConfigDao {
    /**
     * 为父类HibernateDaoSupport注入sessionFactory的值
     * 
     * @param sessionFactory
     */
    @Resource(name = "sessionFactory")
    public void setSuperSessionFactory(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }

    @Override
    public List<MailConfig> queryByEmpCode(String empCode, Integer page, Integer rows) {
        String hql = "from MailConfig where employeeCode = :empCode order by companyFlg";
        Query q = this.getSessionFactory().openSession().createQuery(hql);
        q.setParameter("empCode", empCode);
        return (List<MailConfig>) q.setFirstResult((page - 1) * rows).setMaxResults(rows).list();
    }
    @Override
    public List<MailConfig> queryByEmpCode(String empCode) {
        String hql = "from MailConfig where employeeCode = ? order by companyFlg";
        return (List<MailConfig>) this.getHibernateTemplate().find(hql, empCode);
    }

}
