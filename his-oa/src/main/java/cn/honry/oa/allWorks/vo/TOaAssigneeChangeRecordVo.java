package cn.honry.oa.allWorks.vo;
import java.io.Serializable;
import java.util.Date;

/**
 * @Description: T_OA_ASSIGNEE_CHANGE_RECORD表对应的vo
 * @Author: zhangkui
 * @CreateDate: 2018/2/28 10:14
 * @Modifier: zhangkui
 * @version: V1.0
 */
public class TOaAssigneeChangeRecordVo implements Serializable {

  private static final long serialVersionUID = 176225411068378257L;
  private String id;//主键
  private String taskInfoId;//流程任务表主键--->t_oa_task_info的主键
  private String processName;//流程名称
  private String taskName;//当前环节
  private String oldAssignee;//原审批人
  private String oldAssigneeName;//原审批人名称
  private String newAssignee;//现审批人
  private String newAssigneeName;//现审批人名称
  private String processStarter;//流程发起人
  private String processStarterName;//流程发起人名称
  private Date processStartTime;//流程发起时间
  private String createuser;//操作人(创建人)
  private String createuserName;//创建人名称
  private String createdept;//创建科室
  private Date createtime;//创建时间
  private String updateuser;//修改人
  private Date updatetime;//修改时间
  private String deleteuser;//删除人
  private Date deletetime;//删除时间
  private int stopFlg = 0;//停用标记
  private int delFlg = 0;//删除标记

  //以下字段做查询用
  private String pName;//流程名字
  private String taskId;//t_oa_task_info的task_id


  public String getTaskId() {
    return taskId;
  }

  public void setTaskId(String taskId) {
    this.taskId = taskId;
  }

  public String getpName() {
    return pName;
  }

  public void setpName(String pName) {
    this.pName = pName;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTaskInfoId() {
    return taskInfoId;
  }

  public void setTaskInfoId(String taskInfoId) {
    this.taskInfoId = taskInfoId;
  }

  public String getProcessName() {
    return processName;
  }

  public void setProcessName(String processName) {
    this.processName = processName;
  }

  public String getTaskName() {
    return taskName;
  }

  public void setTaskName(String taskName) {
    this.taskName = taskName;
  }

  public String getOldAssignee() {
    return oldAssignee;
  }

  public void setOldAssignee(String oldAssignee) {
    this.oldAssignee = oldAssignee;
  }

  public String getOldAssigneeName() {
    return oldAssigneeName;
  }

  public void setOldAssigneeName(String oldAssigneeName) {
    this.oldAssigneeName = oldAssigneeName;
  }

  public String getNewAssignee() {
    return newAssignee;
  }

  public void setNewAssignee(String newAssignee) {
    this.newAssignee = newAssignee;
  }

  public String getNewAssigneeName() {
    return newAssigneeName;
  }

  public void setNewAssigneeName(String newAssigneeName) {
    this.newAssigneeName = newAssigneeName;
  }

  public String getProcessStarter() {
    return processStarter;
  }

  public void setProcessStarter(String processStarter) {
    this.processStarter = processStarter;
  }

  public String getProcessStarterName() {
    return processStarterName;
  }

  public void setProcessStarterName(String processStarterName) {
    this.processStarterName = processStarterName;
  }

  public Date getProcessStartTime() {
    return processStartTime;
  }

  public void setProcessStartTime(Date processStartTime) {
    this.processStartTime = processStartTime;
  }

  public String getCreateuser() {
    return createuser;
  }

  public void setCreateuser(String createuser) {
    this.createuser = createuser;
  }

  public String getCreateuserName() {
    return createuserName;
  }

  public void setCreateuserName(String createuserName) {
    this.createuserName = createuserName;
  }

  public String getCreatedept() {
    return createdept;
  }

  public void setCreatedept(String createdept) {
    this.createdept = createdept;
  }

  public Date getCreatetime() {
    return createtime;
  }

  public void setCreatetime(Date createtime) {
    this.createtime = createtime;
  }

  public String getUpdateuser() {
    return updateuser;
  }

  public void setUpdateuser(String updateuser) {
    this.updateuser = updateuser;
  }

  public Date getUpdatetime() {
    return updatetime;
  }

  public void setUpdatetime(Date updatetime) {
    this.updatetime = updatetime;
  }

  public String getDeleteuser() {
    return deleteuser;
  }

  public void setDeleteuser(String deleteuser) {
    this.deleteuser = deleteuser;
  }

  public Date getDeletetime() {
    return deletetime;
  }

  public void setDeletetime(Date deletetime) {
    this.deletetime = deletetime;
  }

  public int getStopFlg() {
    return stopFlg;
  }

  public void setStopFlg(int stopFlg) {
    this.stopFlg = stopFlg;
  }

  public int getDelFlg() {
    return delFlg;
  }

  public void setDelFlg(int delFlg) {
    this.delFlg = delFlg;
  }
}
