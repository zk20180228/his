package cn.honry.oa.activiti.operation.vo;

import java.io.Serializable;

/**
 * @Description:用户的学历
 * @Author: zhangkui
 * @CreateDate: 2018/1/22 20:37
 * @Modifier: zhangkui
 * @version: V1.0
 */
public class EducationVo implements Serializable{

    private static final long serialVersionUID = 6416765462120123206L;

    private String eCode;
    private String eName;

    public String geteCode() {
        return eCode;
    }

    public void seteCode(String eCode) {
        this.eCode = eCode;
    }

    public String geteName() {
        return eName;
    }

    public void seteName(String eName) {
        this.eName = eName;
    }
}
