package cn.honry.oa.allWorks.dao;

import cn.honry.oa.allWorks.vo.*;

import java.util.List;

/**
 * @Description:
 * @Author: zhangkui
 * @CreateDate: 2018/2/2 15:56
 * @Modifier: zhangkui
 * @version: V1.0
 */
public interface AllWorksDao {

    List<EmpVo> empList();

    List<ProcessTypesVo> pTypeList();

    List<DeptVo> deptList();

    List<ProcessComboxVo> processList();

    List<StaOverviewVo> staOverviewList(String processId, String startTime, String endTime, String page, String rows, String work_flag);

    Integer staOverviewNum(String processId, String startTime, String endTime, String work_flag);

    List<ProcessVo> runningList(String pType, String blDept, String empJobNo, String startTime, String endTime, String page, String rows, String work_flag);

    Integer runningNum(String pType, String blDept, String empJobNo, String startTime, String endTime, String work_flag);

    List<ProcessVo> completeList(String pType, String blDept, String empJobNo, String startTime, String endTime, String page, String rows, String work_flag);

    Integer completeNum(String pType, String blDept, String empJobNo, String startTime, String endTime, String work_flag);

    List<RemindVo> remindList(String pType, String blDept, String empJobNo, String startTime, String endTime, String page, String rows, String work_flag);

    Integer remindNum(String pType, String blDept, String empJobNo, String startTime, String endTime, String work_flag);

    void updateTask(String taskId,String newAssignee, String newAssigneeName, String updateUser);

    void addAssigneeChangeRecord(TOaAssigneeChangeRecordVo vo);

    List<TOaAssigneeChangeRecordVo> assigneeChangeRecordList(String pType,String blDept,String processId, String empJobNo, String startTime, String endTime, String page, String rows, String work_flag);

    Integer assigneeChangeRecordNum(String pType,String blDept,String processId, String empJobNo, String startTime, String endTime, String work_flag);

    void updateHiTask(String id_, String newAssignee);

    void updateRuTask(String id_, String newAssignee);
}
