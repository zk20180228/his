package cn.honry.oa.mail.dao.impl;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.MailAttachment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.oa.mail.dao.MailAttachmentDao;

@SuppressWarnings({ "deprecation", "unchecked" })
@Repository("mailAttachmentDao")
public class MailAttachmentDaoImpl extends HibernateEntityDao<MailAttachment> implements MailAttachmentDao {
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
    public void deleteByCid(String cid) {
        String hql = "delete MailAttachment m where m.cid=:cid";
        Query q = this.getSessionFactory().openSession().createQuery(hql);
        q.setParameter("cid", cid);
        q.executeUpdate();
    }

    /**
     * 删除某邮件的附件
     * @param mid 邮件id
     */
    @Override
    public void deleteByMid(String mid) {
        String hql = "delete MailAttachment m where m.mid=:mid";
        Query q = this.getSessionFactory().openSession().createQuery(hql);
        q.setParameter("mid", mid);
        q.executeUpdate();
    }
}
