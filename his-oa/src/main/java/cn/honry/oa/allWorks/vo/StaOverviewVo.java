package cn.honry.oa.allWorks.vo;

import java.io.Serializable;

/**
 * @Description:
 * @Author: zhangkui
 * @CreateDate: 2018/2/6 10:14
 * @Modifier: zhangkui
 * @version: V1.0
 */
public class StaOverviewVo implements Serializable{
    private static final long serialVersionUID = 498605930818657093L;

    private String pName;//流程名字
    private Integer runNum;//流转中的数量
    private Integer completeNum;//已办毕数量
    private String remindNum;//催办数量


    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public Integer getRunNum() {
        return runNum;
    }

    public void setRunNum(Integer runNum) {
        this.runNum = runNum;
    }

    public Integer getCompleteNum() {
        return completeNum;
    }

    public void setCompleteNum(Integer completeNum) {
        this.completeNum = completeNum;
    }

    public String getRemindNum() {
        return remindNum;
    }

    public void setRemindNum(String remindNum) {
        this.remindNum = remindNum;
    }
}
