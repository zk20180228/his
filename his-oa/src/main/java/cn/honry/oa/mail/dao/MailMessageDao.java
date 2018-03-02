package cn.honry.oa.mail.dao;

import java.util.List;

import cn.honry.base.bean.model.MailMessage;
import cn.honry.base.dao.EntityDao;

public interface MailMessageDao extends EntityDao<MailMessage> {

    /**
     * 保存邮件，保证相同的邮件不会重复插入
     * @param mailMessage 邮件对象
     * @Return id
     */
    String insert(MailMessage mailMessage);
    /**
     * 查询某邮箱账号的所有邮件
     * @return
     */
    List<MailMessage> queryByCid(String cid);
    /**
     * 查询某邮箱账号的某文件夹的所有邮件 分页
     * @return
     */
    List<MailMessage> queryByFolder(String cid, Integer folder, Integer page, Integer rows);
    /**
     * 查询某邮箱账号的某文件夹的所有邮件 不分页
     * @return
     */
    List<MailMessage> queryByFolder(String cid, Integer folder);
    /**
     * 查询某邮箱账号的某个邮件夹的某个uid的邮件
     * @return
     */
    MailMessage queryByUid(String cid, Integer folder, String uid);
    /**
     * 查询上次收信的最后一封邮件的uid
     * @param cid 邮箱id
     * @return
     */
    Integer queryImapLastReceivedUid(String cid, Integer folder);
    /**
     * 查询某邮箱账号的未读邮件
     * @return
     */
    List<MailMessage> queryUnreadMail(String cid, Integer page, Integer rows);
    List<MailMessage> queryStarBoxMail(String cid, Integer page, Integer rows);
    /**
     * 查询某邮箱账号的所有邮件数量
     * @param cid
     * @return
     */
    Integer queryCountByCid(String cid);
    /**
     * 查询数据库中某邮箱账号的某文件夹的邮件数量
     * @param cid
     * @param folder
     * @return
     */
    Integer queryCountByFolder(String cid, Integer folder);
    /**
     * 查询数据库中某邮箱账号的未读邮件数量
     * @param cid
     * @return
     */
    Integer queryCountByUnreadMail(String cid);
    /**
     * 查询数据库中某邮箱账号的星标邮件数量
     * @param cid
     * @return
     */
    Integer queryCountByStarBoxMail(String cid);
    /**
     * 删除所有某cid的邮件
     */
    void deleteByCid(String cid);
    /**
     * 根据id删除邮件
     */
    void deleteByid(String id);
    /**
     *
     * @param aid 附件id
     */
    MailMessage queryByAid(String aid);

    void markMail(String ids, Integer markType);

    void moveFolder(String ids, Integer folderType);

    List<MailMessage> searchMail(String content, Integer page, Integer rows);

    Integer searchMailCount(String content);
}
