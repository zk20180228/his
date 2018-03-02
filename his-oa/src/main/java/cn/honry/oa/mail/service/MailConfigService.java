package cn.honry.oa.mail.service;

import java.util.List;

import cn.honry.base.bean.model.MailConfig;
import cn.honry.oa.mail.vo.SenderVo;
import cn.honry.utils.TreeJson;

public interface MailConfigService {

    MailConfig queryById(String id);

    void addOrUpdate(MailConfig mailConfig);

    void delete(MailConfig mailConfig);

    /**
     * 通过登录员工code查找所有邮箱
     * 
     * @param empCode 员工code
     * @return
     */
    List<MailConfig> queryByEmpCode(String empCode);
    /**
     * 通过登录员工code查找所有邮箱 分页
     *
     * @param empCode 员工code
     * @param page
     * @param rows
     * @return
     */
    List<MailConfig> queryByEmpCode(String empCode, Integer page, Integer rows);

    List<SenderVo> querySenderByEmpCode(String empCode, String mid);

    /**
     * 从员工表中通过登录员工code查找个人资料的邮箱账号
     * 
     * @param empCode 员工code
     * @return
     */
    String queryMailFromEmp(String empCode);

    MailConfig queryByEmail(String email);

    /**
     * 从邮箱配置表中查询邮箱
     * 
     * @param empCode 员工code
     * @return
     */
    MailConfig queryMailFromConf(String empCode);

    void update(MailConfig mailConfig);

    /**
     * 获取当前用户邮箱tree
     * 
     * @return
     */
    List<TreeJson> mailTree(String empCode);

}
