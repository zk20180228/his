package cn.honry.oa.allWorks.vo;

import java.io.Serializable;

/**
 * @Description:
 * @Author: zhangkui
 * @CreateDate: 2018/2/6 9:58
 * @Modifier: zhangkui
 * @version: V1.0
 */
public class ProcessComboxVo implements Serializable {
    private static final long serialVersionUID = -2126304492334759208L;

    private String processId;
    private String processName;

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }
}
