package cn.honry.oa.mail.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import cn.honry.base.bean.model.MailConfig;
import cn.honry.base.bean.model.MailMessage;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.inner.baseinfo.employee.dao.EmployeeInInterDAO;
import cn.honry.oa.mail.dao.MailConfigDao;
import cn.honry.oa.mail.dao.MailMessageDao;
import cn.honry.oa.mail.service.MailConfigService;
import cn.honry.oa.mail.vo.SenderVo;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.TreeJson;
@Service("mailConfigService")
public class MailConfigServiceImpl implements MailConfigService{

    @Resource(name ="mailConfigDao")
    MailConfigDao mailConfigDao;

    @Resource(name ="mailMessageDao")
    MailMessageDao mailMessageDao;

    @Resource(name ="employeeInInterDAO")
    EmployeeInInterDAO employeeInInterDAO;

    @Override
    public MailConfig queryById(String id) {
        return this.mailConfigDao.get(id);
    }

    @Override
    public void addOrUpdate(MailConfig mailConfig) {
        MailConfig config = this.queryByEmail(mailConfig.getEmail());
        if (config == null) {
            mailConfig.setId(null);
            mailConfig.setCreateTime(new Date());
        } else {
            mailConfig.setCreateTime(config.getCreateTime());
            mailConfig.setUpdateTime(new Date());
        }
        String type = this.getMailType(mailConfig.getEmail());
        mailConfig.setType(type);
        SysEmployee employee = ShiroSessionUtils.getCurrentEmployeeFromShiroSession();
        if (mailConfig.getEmail().equals(employee.getEmail())) {
            mailConfig.setCompanyFlg(0);
        } else {
            mailConfig.setCompanyFlg(1);
        }
        mailConfig.setEmployeeCode(employee.getCode());
        this.mailConfigDao.save(mailConfig);
    }

    @Override
    public void delete(MailConfig mailConfig) {
        this.mailConfigDao.remove(mailConfig);
    }

    @Override
    public List<MailConfig> queryByEmpCode(String empCode) {
        return this.mailConfigDao.queryByEmpCode(empCode);
    }
    @Override
    public List<MailConfig> queryByEmpCode(String empCode, Integer page, Integer rows) {
        return this.mailConfigDao.queryByEmpCode(empCode, page, rows);
    }

    @Override
    public String queryMailFromEmp(String empCode) {
        SysEmployee employee = this.employeeInInterDAO.querNurseDoctor(empCode);
        return employee.getEmail();
    }

    @Override
    public MailConfig queryByEmail(String email) {
        return this.mailConfigDao.findUniqueBy("email", email);
    }

    @Override
    public MailConfig queryMailFromConf(String empCode) {
        Map<String, Object> map = new HashMap<>();
        map.put("employeeCode", empCode);
//        map.put("companyFlg", 0);
        List<MailConfig> list = this.mailConfigDao.findByObjectProperty(map);
        if(list != null && list.size() != 0){
            return list.get(0);
        }
        return new MailConfig();
    }

    @Override
    public void update(MailConfig mailConfig) {
        String type = this.getMailType(mailConfig.getEmail());
        mailConfig.setType(type);
        mailConfig.setUpdateTime(new Date());
        this.mailConfigDao.update(mailConfig);
    }

    @Override
    public List<SenderVo> querySenderByEmpCode(String empCode, String mid) {
        List<SenderVo> voList = new ArrayList<>();
        MailMessage mailMessage = null;
        if (StringUtils.isNotBlank(mid)) {
            mailMessage = this.mailMessageDao.get(mid);
        }
        List<MailConfig> list = this.mailConfigDao.queryByEmpCode(empCode);
        for(int i=0; i<list.size(); i++){
            SenderVo vo = new SenderVo();
            vo.setId(list.get(i).getId());
            vo.setNickName(list.get(i).getNickName());
            vo.setEmail(list.get(i).getEmail());
            vo.setSender(list.get(i).getNickName()+"<"+list.get(i).getEmail()+">");
            if (mailMessage == null) {
                if (i == 0) {
                    vo.setSelected(true);
                } else {
                    vo.setSelected(false);
                }
            } else if (mailMessage.getCid().equals(list.get(i).getId())) {
                vo.setSelected(true);
            } else {
                vo.setSelected(false);
            }
            voList.add(vo);
        }
        //Selected为true的排到第一位
        Collections.sort(voList, new Comparator<SenderVo>(){
            @Override
            public int compare(SenderVo o1, SenderVo o2) {
                if (!o1.getSelected() && o2.getSelected()) {
                    return 1;
                } else if (o1.getSelected() && !o2.getSelected()) {
                    return -1;
                }
                return 0;
            }
        });
        return voList;
    }
    private String getMailType(String email){
        String type = email.substring(email.indexOf("@"));
        if (type.contains("qq")) {
            type = "qq";
        } else if (type.contains("163")) {
            type = "163";
        } else if (type.contains("126")) {
            type = "";
        } else if (type.contains("hotmail")) {
            type = "hotmail";
        } else if (type.contains("gmail")) {
            type = "gmail";
        } else if (type.contains("189")) {
            type = "189";
        } else {
            if(type.startsWith("vip")){
                type = type.substring(4).substring(0, type.substring(4).indexOf("."));
            }else {
                type = type.substring(0, type.indexOf("."));
            }
        }
        return type;
    }

    @Override
    public List<TreeJson> mailTree(String empCode) {
        List<TreeJson> list = new ArrayList<>();
        List<MailConfig> configlist = queryByEmpCode(empCode);
        for(MailConfig config : configlist){
            TreeJson tree = new TreeJson();
            tree.setId(config.getId());
            tree.setText(config.getEmail());
//            tree.setIconCls("icon-house");
            Map<String, String> map = new HashMap<>();
            map.put("email", config.getEmail());
            tree.setAttributes(map);
            tree.setState(TreeJson.STATEOPEN);
            tree.setChildren(this.folderTree());
            list.add(tree);
        }
        return list;
    }

    private List<TreeJson> folderTree(){
        List<TreeJson> list = new ArrayList<>();
        TreeJson tree = new TreeJson();
        tree.setText("未读邮件");
        list.add(tree);
        tree = new TreeJson();
        tree.setText("收件箱");
        list.add(tree);
        tree = new TreeJson();
        tree.setText("草稿箱");
        list.add(tree);
        tree = new TreeJson();
        tree.setText("星标邮件");
        list.add(tree);
        tree = new TreeJson();
        tree.setText("已发送");
        list.add(tree);
        tree = new TreeJson();
        tree.setText("已删除");
        list.add(tree);
        tree = new TreeJson();
        tree.setText("垃圾邮件");
        list.add(tree);
        return list;
    }
}
