package cn.honry.oa.mail.dao;

import java.util.List;

import cn.honry.base.bean.model.MailConfig;
import cn.honry.base.dao.EntityDao;

public interface MailConfigDao extends EntityDao<MailConfig>{

    List<MailConfig> queryByEmpCode(String empCode, Integer page, Integer rows);
    List<MailConfig> queryByEmpCode(String empCode);

}
