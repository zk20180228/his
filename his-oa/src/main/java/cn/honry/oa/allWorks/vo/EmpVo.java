package cn.honry.oa.allWorks.vo;

import java.io.Serializable;

/**
 * @Description:
 * @Author: zhangkui
 * @CreateDate: 2018/2/5 10:42
 * @Modifier: zhangkui
 * @version: V1.0
 */
public class EmpVo implements Serializable {

    private static final long serialVersionUID = 6032002268364382479L;
    private String empJobNo;
    private String empName;

    public String getEmpJobNo() {
        return empJobNo;
    }

    public void setEmpJobNo(String empJobNo) {
        this.empJobNo = empJobNo;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }
}
