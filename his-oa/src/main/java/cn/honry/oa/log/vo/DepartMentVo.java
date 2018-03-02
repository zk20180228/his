package cn.honry.oa.log.vo;

import java.io.Serializable;

/**
 * @Description:
 * @Author: zhangkui
 * @CreateDate: 2018/1/4 19:33
 * @Modifier: zhangkui
 * @version: V1.0
 */
public class DepartMentVo implements Serializable {


    private static final long serialVersionUID = -4598929165426784590L;

    private String id;
    //科室编号
    private String deptCode;
    //科室名字
    private String deptName;
    //科室所在院区名字
    private String areaName;
    //冗余字段，值等于deptName
    private String name;


    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
        this.id=deptCode;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
        this.name=deptName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }


}
