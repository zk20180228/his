package cn.honry.oa.mail.service;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.Store;
import javax.mail.Transport;

import org.apache.commons.mail.EmailException;

import cn.honry.base.bean.model.MailAttachment;
import cn.honry.base.bean.model.MailConfig;
import cn.honry.base.bean.model.MailMessage;

public interface MailMessageService {

    Store createStore(MailConfig config) throws MessagingException;

    Transport createTransport(MailConfig config) throws MessagingException, EmailException;

    void receive(MailConfig config) throws MessagingException;

    /**
     * 发信
     * @param mailConfig 邮箱配置对象
     * @param mailMessage 邮件信息对象
     * @throws Exception 异常
     */
    void send(MailConfig mailConfig, MailMessage mailMessage) throws Exception;
    void saveToDraft(MailConfig mailConfig, MailMessage mailMessage) throws Exception;

    List<MailMessage> queryUnreadMail(String cid, Integer page, Integer rows);

    List<MailMessage> queryByFolder(String cid, Integer folder, Integer page, Integer rows);

    List<MailMessage> queryByFolder(String cid, Integer folder);

    List<MailMessage> queryStarBoxMail(String cid, Integer page, Integer rows);

    /**
     * 查询服务器中某邮箱账号的所有邮件数量
     *
     * @param config 邮箱配置对象
     * @return
     */
    Integer queryCountByCidFromServer(MailConfig config) throws MessagingException;

    /**
     * 查询数据库中某邮箱账号的所有邮件数量
     *
     * @param cid
     * @return
     */
    Integer queryCountByCid(String cid);

    /**
     * 查询数据库中某邮箱账号的某文件夹的邮件数量
     *
     * @param cid
     * @param folder
     * @return
     */
    Integer queryCountByFolder(String cid, Integer folder);

    /**
     * 查询数据库中某邮箱账号的未读邮件数量
     *
     * @param cid
     * @return
     */
    Integer queryCountByUnreadMail(String cid);

    /**
     * 查询数据库中某邮箱账号的星标邮件数量
     *
     * @param cid
     * @return
     */
    Integer queryCountByStarBoxMail(String cid);

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    MailMessage queryById(String id);

    /**
     * 删除所有某cid的邮件
     */
    void deleteByCid(String cid);
    /**
     * 根据id删除邮件
     */
    void deleteById(String id);

    List<MailAttachment> queryAttachById(String id);

    MailMessage queryByAid(String aid);

    InputStream downImapAttachByAid(MailConfig mailConfig, MailMessage mailMessage, String fileName) throws Exception;

    void testSend(MailConfig mailConfig) throws EmailException, UnsupportedEncodingException;

    void markMail(String ids, Integer markType) throws Exception;

    void moveFolder(String ids, Integer folderType) throws Exception;

    List<MailMessage> searchMail(String content, Integer page, Integer rows);

    Integer searchMailCount(String content);
}
