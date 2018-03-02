package cn.honry.oa.allWorks.vo;

import java.io.Serializable;

/**
 * @Description:流程所属科室列表
 * @Author: zhangkui
 * @CreateDate: 2018/2/5 17:59
 * @Modifier: zhangkui
 * @version: V1.0
 */
public class DeptVo implements Serializable{
    private static final long serialVersionUID = -7950159622046803115L;

    private String deptName;
    private String deptCode;

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }
}
