package cn.honry.oa.mail.dao;

import cn.honry.base.bean.model.MailAttachment;
import cn.honry.base.dao.EntityDao;

public interface MailAttachmentDao extends EntityDao<MailAttachment> {

    /**
     * 删除某邮箱的附件
     * @param cid 邮箱配置id
     */
    void deleteByCid(String cid);

    /**
     * 删除某邮件的附件
     * @param mid 邮件id
     */
    void deleteByMid(String mid);
}
