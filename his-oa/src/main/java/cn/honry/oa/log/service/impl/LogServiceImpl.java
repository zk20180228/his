package cn.honry.oa.log.service.impl;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.oa.log.dao.LogDao;
import cn.honry.oa.log.service.LogService;
import cn.honry.oa.log.vo.DepartMentVo;
import cn.honry.oa.log.vo.EmployeeExtendVo;
import cn.honry.oa.log.vo.ProcessTopicVo;
import cn.honry.oa.log.vo.TaskDelVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.ShiroSessionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Description:
 * @Author: zhangkui
 * @CreateDate: 2018/1/4 16:52
 * @Modifier: zhangkui
 * @version: V1.0
 */
@Service("logService")
public class LogServiceImpl implements LogService {

    @Resource
    private LogDao logDao;

    @Override
    public List<TaskDelVo> findTaskDelVoList(String startTime, String endTime, String page, String rows) {

        return logDao.findTaskDelVoList(startTime,endTime,page,rows);
    }

    @Override
    public Integer findTaskDelVoCount(String startTime, String endTime) {

        return logDao.findTaskDelVoCount(startTime,endTime);
    }

    @Override
    public List<EmployeeExtendVo> findEmpList(String eName, String jobNO, String page, String rows) {

        return logDao.findEmpList(eName,jobNO,page,rows);
    }

    @Override
    public Integer findEmpCount(String eName, String jobNO) {
        return logDao.findEmpCount(eName,jobNO);
    }

    @Override
    public List<DepartMentVo> findDeptList(String dName, String deptCode, String page, String rows) {
        return logDao.findDeptList(dName,deptCode,page,rows);
    }

    @Override
    public Integer findDeptCount(String dName, String deptCode) {
        return logDao.findDeptCount(dName,deptCode);
    }

    @Override
    public List<ProcessTopicVo> findProcessTopicList(String pName, String page, String rows) {
        return logDao.findProcessTopicList(pName,page,rows);
    }

    @Override
    public Integer findProcessTopicCount(String pName) {
        return logDao.findProcessTopicCount(pName);
    }

    @Override
    public void updateTaskInfo(String flag, String condition,String t_value) {

        if(StringUtils.isBlank(flag)||StringUtils.isBlank(condition)){
            return ;
        }
        //flag 1流程2人员3科室
        //这里主要说明flag=1的情况
        //根据condition（此时condition为T_OA_BPM_PROCESS的id）查询T_OA_KV_RECORD的id，并更新该表
        //得到T_OA_KV_RECORD的id更新T_OA_KV_PROP表中外键为T_OA_KV_RECORD的id的数据
        //更新T_OA_TASK_INFO中BUSINESS_KEY为T_OA_KV_RECORD的id的数据
        Integer num = logDao.updateTaskInfo(flag, condition);

        TaskDelVo vo = new TaskDelVo();
        vo.setCode(condition);
        vo.setType(Integer.parseInt(flag));
        vo.setNum(num);
        SysEmployee employee = ShiroSessionUtils.getCurrentEmployeeFromShiroSession();
        SysDepartment department = ShiroSessionUtils.getCurrentUserDepartmentFromShiroSession();
        vo.setCreateDept(department.getDeptCode());
        vo.setCreateTime(DateUtils.formatDateY_M_D_H_M_S(new Date()));
        vo.setCreateUser(employee.getJobNo());
        vo.setUpdateTime(DateUtils.formatDateY_M_D_H_M_S(new Date()));
        vo.setUpdateUser(employee.getJobNo());
        String id = UUID.randomUUID().toString().replace("-", "");
        vo.setId(id);
        vo.setT_value(t_value);

        logDao.insertLog(vo);
    }
}
