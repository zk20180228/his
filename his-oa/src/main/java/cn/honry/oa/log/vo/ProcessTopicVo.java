package cn.honry.oa.log.vo;

import java.io.Serializable;

/**
 * @Description:查询流程分类的vo，比如销假申请(婚假)，销假申请(事假)
 * @Author: zhangkui
 * @CreateDate: 2018/1/4 20:33
 * @Modifier: zhangkui
 * @version: V1.0
 */
public class ProcessTopicVo implements Serializable {
    private static final long serialVersionUID = -2603447560529165496L;

    private String id;//T_OA_BPM_PROCESS的id
    private String processName;//T_OA_BPM_PROCESS的name
    private String remark;//备注
    private String areaName;
    //冗余字段，processName
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
        this.name=processName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
