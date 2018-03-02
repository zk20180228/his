package cn.honry.oa.allWorks.vo;

import java.io.Serializable;

/**
 * @Description:
 * @Author: zhangkui
 * @CreateDate: 2018/2/6 10:17
 * @Modifier: zhangkui
 * @version: V1.0
 */
public class RemindVo implements Serializable {
    private static final long serialVersionUID = 6028525903014457740L;

    private String title;//申请标题
    private String pNode;//催办环节
    private String reminder;//催办人
    private String reminded;//被催办人
    private String reContent;//催办内容
    private String pType;//流程分类
    private String blDept;//所属科室
    private String startTime;//创建时间
    private String endTime;//该节点办毕时间
    private String blockTime;//停留时间

    private Integer remindNum;//催办次数
    private String isReader;//是否已读
    private String isResponse;//是否已回


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getpNode() {
        return pNode;
    }

    public void setpNode(String pNode) {
        this.pNode = pNode;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public String getReminded() {
        return reminded;
    }

    public void setReminded(String reminded) {
        this.reminded = reminded;
    }

    public String getReContent() {
        return reContent;
    }

    public void setReContent(String reContent) {
        this.reContent = reContent;
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

    public Integer getRemindNum() {
        return remindNum;
    }

    public void setRemindNum(Integer remindNum) {
        this.remindNum = remindNum;
    }

    public String getIsReader() {
        return isReader;
    }

    public void setIsReader(String isReader) {
        this.isReader = isReader;
    }

    public String getIsResponse() {
        return isResponse;
    }

    public void setIsResponse(String isResponse) {
        this.isResponse = isResponse;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getBlockTime() {
        return blockTime;
    }

    public void setBlockTime(String blockTime) {
        this.blockTime = blockTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
