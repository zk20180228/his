package cn.honry.oa.log.vo;

import java.io.Serializable;

/**
 * @Description:员工扩展表对应的vo
 * @Author: zhangkui
 * @CreateDate: 2018/1/4 19:26
 * @Modifier: zhangkui
 * @version: V1.0
 */
public class EmployeeExtendVo implements Serializable{
    private static final long serialVersionUID = -4354367354216704918L;

    private String id;
    //员工号
    private String jobNo;
    //员工姓名
    private String name;
    //员工所在科室名字
    private String deptName;
    //员工性别
    private String sexName;


    public String getJobNo() {
        return jobNo;
    }

    public void setJobNo(String jobNo) {
        this.jobNo = jobNo;
        this.id=jobNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getSexName() {
        return sexName;
    }

    public void setSexName(String sexName) {
        this.sexName = sexName;
    }
}
