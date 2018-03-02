package cn.honry.oa.allWorks.service.impl;

import cn.honry.base.bean.model.SysEmployee;
import cn.honry.oa.allWorks.dao.AllWorksDao;
import cn.honry.oa.allWorks.service.AllWorksService;
import cn.honry.oa.allWorks.vo.*;
import cn.honry.utils.ShiroSessionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Description:
 * @Author: zhangkui
 * @CreateDate: 2018/2/2 15:55
 * @Modifier: zhangkui
 * @version: V1.0
 */
@Service("allWorksService")
public class AllWorksServiceImpl implements AllWorksService {

    @Resource
    private AllWorksDao allWorksDao;

    @Override
    public List<EmpVo> empList() {
        return allWorksDao.empList();
    }

    @Override
    public List<ProcessTypesVo> pTypeList() {
        return allWorksDao.pTypeList();
    }

    @Override
    public List<DeptVo> deptList() {
        return allWorksDao.deptList();
    }

    @Override
    public List<ProcessComboxVo> processList() {

        return allWorksDao.processList();
    }

    @Override
    public List<StaOverviewVo> staOverviewList(String processId, String startTime, String endTime, String page, String rows, String work_flag) {
        return allWorksDao.staOverviewList(processId,startTime,endTime,page,rows,work_flag);
    }

    @Override
    public Integer staOverviewNum(String processId, String startTime, String endTime, String work_flag) {
        return allWorksDao.staOverviewNum(processId,startTime,endTime,work_flag);
    }

    @Override
    public List<ProcessVo> runningList(String pType, String blDept, String empJobNo, String startTime, String endTime, String page, String rows, String work_flag) {
        return allWorksDao.runningList(pType,blDept,empJobNo,startTime,endTime,page,rows,work_flag);
    }

    @Override
    public Integer runningNum(String pType, String blDept, String empJobNo, String startTime, String endTime, String work_flag) {
        return allWorksDao.runningNum(pType,blDept,empJobNo,startTime,endTime,work_flag);
    }

    @Override
    public List<ProcessVo> completeList(String pType, String blDept, String empJobNo, String startTime, String endTime, String page, String rows, String work_flag) {
        return allWorksDao.completeList(pType,blDept,empJobNo,startTime,endTime,page,rows,work_flag);
    }

    @Override
    public Integer completeNum(String pType, String blDept, String empJobNo, String startTime, String endTime, String work_flag) {
        return allWorksDao.completeNum(pType,blDept,empJobNo,startTime,endTime,work_flag);
    }

    @Override
    public List<RemindVo> remindList(String pType, String blDept, String empJobNo, String startTime, String endTime, String page, String rows, String work_flag) {
        return allWorksDao.remindList(pType,blDept,empJobNo,startTime,endTime,page,rows,work_flag);
    }

    @Override
    public Integer remindNum(String pType, String blDept, String empJobNo, String startTime, String endTime, String work_flag) {
        return allWorksDao.remindNum(pType,blDept,empJobNo,startTime,endTime,work_flag);
    }

    @Override
    public void appointAssignees(TOaAssigneeChangeRecordVo vo) {

        //1.更新当前节点的审批人(更新t_oa_task_info表)
        String newAssignee=vo.getNewAssignee();//现审批人
        String newAssigneeName=vo.getNewAssigneeName();//现审批人名称
        SysEmployee employee = ShiroSessionUtils.getCurrentEmployeeFromShiroSession();
        String updateUser="";
        if(null!=employee){
            updateUser= employee.getJobNo();
        }
        String taskId=vo.getTaskInfoId();
        allWorksDao.updateTask(taskId,newAssignee,newAssigneeName,updateUser);

        //2.根据id更新ACT_HI_TASKINST表
        String id_=vo.getTaskId();
        allWorksDao.updateHiTask(id_,newAssignee);

        //3.根据id更新ACT_RU_TASK表
        allWorksDao.updateRuTask(id_,newAssignee);

        //4.添加流程任务负责人变更记录
        String id = UUID.randomUUID().toString().replace("-", "");
        vo.setId(id);
        vo.setCreateuser(updateUser);

        String userName="";
        String deptCode="";
        if(null!=employee){
            userName= employee.getName();
            deptCode=employee.getDeptCode();
        }

        vo.setCreateuserName(userName);
        vo.setCreatedept(deptCode);
        vo.setCreatetime(new Date());
        vo.setUpdateuser(updateUser);
        vo.setUpdatetime(new Date());

        allWorksDao.addAssigneeChangeRecord(vo);
    }

    @Override
    public List<TOaAssigneeChangeRecordVo> assigneeChangeRecordList(String pType,String blDept,String processId, String empJobNo, String startTime, String endTime, String page, String rows, String work_flag) {
        return allWorksDao.assigneeChangeRecordList(pType,blDept,processId,empJobNo,startTime,endTime,page,rows,work_flag);
    }

    @Override
    public Integer assigneeChangeRecordNum(String pType,String blDept,String processId, String empJobNo, String startTime, String endTime, String work_flag) {
        return allWorksDao.assigneeChangeRecordNum(pType,blDept,processId,empJobNo,startTime,endTime,work_flag);
    }


}
