package cn.honry.oa.allWorks.vo;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @Description:流转中和已办毕vo
 * @Author: zhangkui
 * @CreateDate: 2018/2/5 20:08
 * @Modifier: zhangkui
 * @version: V1.0
 */
public class ProcessVo implements Serializable {
    private static final long serialVersionUID = 141011066447017287L;

    private String nodeCreateTime;//当前节点创建时间
    private String endTime;//办毕时间
    private String totalTime;//流程总用时
    private String blockTime;//停留时间
    private String approver;//当前审批人
    private String pType;//流程分类
    private String blDept;//所属科室

    private String processInstanceId;//PROCESS_INSTANCE_ID
    private String businessKey;//t_oa_task_info的id
    private String option;//操作


    private String tid;//t_oa_task_info的id
    private String title;//申请标题
    private String curNode;//当前环节(名称)
    private String allApproversJobNum;//所有原审批人审批人员工号
    private String allApproversName;//所有原审批人审批人名字
    private String startTime;//流程开始时间
    private String owner;//发起人名字
    private String ownerJobNum;//发起人员工号

    private String taskId;//t_oa_task_info的task_id


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCurNode() {
        return curNode;
    }

    public void setCurNode(String curNode) {
        this.curNode = curNode;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getBlockTime() {
        return blockTime;
    }

    public void setBlockTime(String blockTime) {
        this.blockTime = blockTime;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getpType() {
        return pType;
    }

    public void setpType(String pType) {
        this.pType = pType;
    }

    public String getBlDept() {
        return blDept;
    }

    public void setBlDept(String blDept) {
        this.blDept = blDept;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getNodeCreateTime() {
        return nodeCreateTime;
    }

    public void setNodeCreateTime(String nodeCreateTime) {
        this.nodeCreateTime = nodeCreateTime;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }


    public String getAllApproversJobNum() {
        return allApproversJobNum;
    }

    public void setAllApproversJobNum(String allApproversJobNum) {
        if(StringUtils.isNotBlank(allApproversJobNum)){
            if(allApproversJobNum.endsWith(",")){
                allApproversJobNum=allApproversJobNum.substring(0,allApproversJobNum.length()-2);
            }
        }

        this.allApproversJobNum = allApproversJobNum;
    }

    public String getAllApproversName() {
        return allApproversName;
    }

    public void setAllApproversName(String allApproversName) {
        if(StringUtils.isNotBlank(allApproversName)){
            if(allApproversName.endsWith(",")){
                allApproversName=allApproversName.substring(0,allApproversName.length()-2);
            }
        }

        this.allApproversName = allApproversName;
    }

    public String getOwnerJobNum() {
        return ownerJobNum;
    }

    public void setOwnerJobNum(String ownerJobNum) {
        this.ownerJobNum = ownerJobNum;
    }


    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
