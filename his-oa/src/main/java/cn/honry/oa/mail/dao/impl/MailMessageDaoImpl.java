package cn.honry.oa.mail.dao.impl;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.MailMessage;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.oa.mail.dao.MailMessageDao;

@SuppressWarnings({ "deprecation", "unchecked" })
@Repository("mailMessageDao")
public class MailMessageDaoImpl extends HibernateEntityDao<MailMessage> implements MailMessageDao {

    /**
     * 为父类HibernateDaoSupport注入sessionFactory的值
     *
     * @param sessionFactory
     */
    @Resource(name = "sessionFactory")
    public void setSuperSessionFactory(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }

    /**
     *
     * 保存邮件，保证相同的邮件不会重复插入
     * @param mailMessage 邮件对象
     * @return id
     */
    @Override
    public String insert(MailMessage mailMessage){
        String sql = "merge into T_OA_MAIL_MESSAGE M" +
                " using (select count(uidd) c from T_OA_MAIL_MESSAGE where uidd = :uidd) N on (N.c >= 1)" +
                " WHEN NOT MATCHED THEN" +
                " INSERT values (:id, :cid, :receiveType, :uidd, :subject, :content, :sendEmail, :sendName, :sendDate," +
                " :receiveEmail, :receiveName, :receiveDate, :cc, :bcc, :replyTo, :attachFlg, :starFlg, :folder," +
                " :oldFolder, :unreadFlg, :emailSize, :priority, :receipt, :answered, :createTime, null )";
        Query q = this.getSessionFactory().openSession().createSQLQuery(sql);
        String id = UUID.randomUUID().toString().replace("-", "");
        q.setParameter("id", id);
        q.setParameter("cid", mailMessage.getCid());
        q.setParameter("receiveType", mailMessage.getReceiveType());
        q.setParameter("uidd", mailMessage.getUidd());
        q.setParameter("subject", mailMessage.getSubject());
        q.setParameter("content", mailMessage.getContent());
        q.setParameter("sendEmail", mailMessage.getSendEmail());
        q.setParameter("sendName", mailMessage.getSendName());
        q.setParameter("sendDate", new Timestamp(mailMessage.getSendDate().getTime()));
        q.setParameter("receiveEmail", mailMessage.getReceiveEmail());
        q.setParameter("receiveName", mailMessage.getReceiveName());
        q.setParameter("receiveDate", new Timestamp(mailMessage.getReceiveDate().getTime()));
        q.setParameter("cc", mailMessage.getCc());
        q.setParameter("bcc", mailMessage.getBcc());
        q.setParameter("replyTo", mailMessage.getReplyTo());
        q.setParameter("attachFlg", mailMessage.getAttachFlg());
        q.setParameter("starFlg", mailMessage.getStarFlg());
        q.setParameter("folder", mailMessage.getFolder());
        q.setParameter("oldFolder", mailMessage.getOldFolder());
        q.setParameter("unreadFlg", mailMessage.getUnreadFlg());
        q.setParameter("emailSize", mailMessage.getEmailSize());
        q.setParameter("priority", mailMessage.getPriority());
        q.setParameter("receipt", mailMessage.getReceipt());
        q.setParameter("answered", mailMessage.getAnswered());
        q.setParameter("createTime", new Timestamp(mailMessage.getCreateTime().getTime()));
        q.executeUpdate();
        return id;
    }
    @Override
    public List<MailMessage> queryByCid(String cid) {
        String hql = "from MailMessage where cid = ?";
        return (List<MailMessage>) this.getHibernateTemplate().find(hql, cid);
    }

    @Override
    public List<MailMessage> queryByFolder(String cid, Integer folder, Integer page, Integer rows) {
        String hql = "from MailMessage where cid = :cid and folder = :folder order by sendDate desc";
        Query q = this.getSessionFactory().openSession().createQuery(hql);
        q.setParameter("cid", cid).setParameter("folder", folder);
        return (List<MailMessage>) q.setFirstResult((page - 1) * rows).setMaxResults(rows).list();
    }

    @Override
    public List<MailMessage> queryByFolder(String cid, Integer folder) {
        String hql = "from MailMessage where cid = :cid and folder = :folder order by sendDate desc";
        Query q = this.getSessionFactory().openSession().createQuery(hql);
        q.setParameter("cid", cid).setParameter("folder", folder);
        return q.list();
    }

    @Override
    public MailMessage queryByUid(String cid, Integer folder, String uid) {
        String hql = "from MailMessage where cid =:cid and folder =:folder and uidd =:uid";
        Query q = this.getSessionFactory().openSession().createQuery(hql);
        q.setParameter("cid", cid).setParameter("folder", folder).setParameter("uid", uid);
        return (MailMessage)q.uniqueResult();
    }

    @Override
    public List<MailMessage> queryUnreadMail(String cid, Integer page, Integer rows) {
        String hql = "from MailMessage where cid =:cid and folder = 1 and unreadFlg = 0 order by sendDate desc";
        Query q = this.getSessionFactory().openSession().createQuery(hql);
        q.setParameter("cid", cid);
        return (List<MailMessage>) q.setFirstResult((page - 1) * rows).setMaxResults(rows).list();
    }

    @Override
    public List<MailMessage> queryStarBoxMail(String cid, Integer page, Integer rows) {
        String hql = "from MailMessage where cid =:cid and starFlg = 1 order by sendDate desc";
        Query q = this.getSessionFactory().openSession().createQuery(hql);
        q.setParameter("cid", cid);
        return (List<MailMessage>) q.setFirstResult((page - 1) * rows).setMaxResults(rows).list();
    }

    @Override
    public Integer queryImapLastReceivedUid(String cid, Integer folder) {
        String sql = "select max(to_number(uidd)) as maxUid from T_OA_MAIL_MESSAGE where cid = :cid and folder =:folder";
        Query q = this.getSessionFactory().openSession().createSQLQuery(sql);
        q.setParameter("cid", cid);
        q.setParameter("folder", folder);
        Number count = (Number) q.uniqueResult();
        if (count != null) {
            return count.intValue();
        } else {
            return 0;
        }
    }

    @Override
    public Integer queryCountByCid(String cid) {
        String hql = "select count(id) from MailMessage where cid=:cid";
        Query q = this.getSessionFactory().openSession().createQuery(hql);
        q.setParameter("cid", cid);
        Number count = (Number)q.uniqueResult();
        return count.intValue();
    }

    @Override
    public Integer queryCountByFolder(String cid, Integer folder) {
        String hql = "select count(id) from MailMessage where cid =:cid and folder =:folder";
        Query q = this.getSessionFactory().openSession().createQuery(hql);
        q.setParameter("cid", cid).setParameter("folder", folder);
        Number count = (Number)q.uniqueResult();
        return count.intValue();
    }

    @Override
    public Integer queryCountByUnreadMail(String cid) {
        String hql = "select count(id) from MailMessage where cid =:cid and unreadFlg = 0";
        Query q = this.getSessionFactory().openSession().createQuery(hql);
        q.setParameter("cid", cid);
        Number count = (Number)q.uniqueResult();
        return count.intValue();
    }

    @Override
    public Integer queryCountByStarBoxMail(String cid) {
        String hql = "select count(id) from MailMessage where cid =:cid and starFlg = 0";
        Query q = this.getSessionFactory().openSession().createQuery(hql);
        q.setParameter("cid", cid);
        Number count = (Number)q.uniqueResult();
        return count.intValue();
    }

    @Override
    public void deleteByCid(String cid) {
        String hql = "delete from MailMessage m where m.cid =:cid";
        Query q = this.getSessionFactory().openSession().createQuery(hql);
        q.setParameter("cid", cid);
        q.executeUpdate();
    }

    @Override
    public void deleteByid(String id) {
        String hql = "delete from MailMessage m where m.id =:id";
        Query q = this.getSessionFactory().openSession().createQuery(hql);
        q.setParameter("id", id);
        q.executeUpdate();
    }

    @Override
    public MailMessage queryByAid(String aid) {
        String sql = "select m.*  from T_OA_MAIL_MESSAGE m, T_OA_MAIL_ATTACHMENT a where m.id=a.mid and a.id = :aid";
        Query q = this.getSessionFactory().openSession().createSQLQuery(sql).addEntity(MailMessage.class);
        q.setParameter("aid", aid);
        return (MailMessage)q.uniqueResult();
    }

    @Override
    public void markMail(String ids, Integer markType) {
        String[] idArray = ids.split(",");
        String hql = "";
        ids = "";
        for (String id : idArray) {
            ids += "\'" + id + "\',";
        }
        if (ids.endsWith(",")) {
            ids = ids.substring(0, ids.lastIndexOf(","));
        }
        if (markType == 1) {
            hql = "update MailMessage m set m.unreadFlg=1 where m.id in("+ids+")";
        } else if (markType == 2) {
            hql = "update MailMessage m set m.unreadFlg=0 where m.id in("+ids+")";
        } else if (markType == 3) {
            hql = "update MailMessage m set m.starFlg=1 where m.id in("+ids+")";
        } else if (markType == 4) {
            hql = "update MailMessage m set m.starFlg=0 where m.id in("+ids+")";
        }
        Query q = this.getSessionFactory().openSession().createQuery(hql);
        q.executeUpdate();
    }

    @Override
    public void moveFolder(String ids, Integer folderType) {
        String[] idArray = ids.split(",");
        ids = "";
        for (String id : idArray) {
            ids += "\'" + id + "\',";
        }
        if (ids.endsWith(",")) {
            ids = ids.substring(0, ids.lastIndexOf(","));
        }
        String hql = "update MailMessage m set m.folder=:folder where m.id in("+ids+")";
        Query q = this.getSessionFactory().openSession().createQuery(hql);
        q.setParameter("folder", folderType);
        q.executeUpdate();
    }

    @Override
    public List<MailMessage> searchMail(String content, Integer page, Integer rows) {
        String hql = "from MailMessage m where m.subject like :content or m.content like :content " +
                "or m.sendEmail like :content or m.receiveEmail like :content order by m.sendDate desc";
        Query q = this.getSessionFactory().openSession().createQuery(hql);
        q.setParameter("content", "%"+content+"%");
        return q.setFirstResult((page - 1) * rows).setMaxResults(rows).list();
    }

    @Override
    public Integer searchMailCount(String content) {
        String hql = "select count(m.id) from MailMessage m where m.subject like :content or m.content like :content " +
                "or m.sendEmail like :content or m.receiveEmail like :content";
        Query q = this.getSessionFactory().openSession().createQuery(hql);
        q.setParameter("content", "%"+content+"%");
        Number count = (Number)q.uniqueResult();
        return count.intValue();
    }

}
