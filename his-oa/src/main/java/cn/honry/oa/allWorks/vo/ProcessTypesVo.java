package cn.honry.oa.allWorks.vo;

import java.io.Serializable;

/**
 * @Description:流程分类Vo
 * @Author: zhangkui
 * @CreateDate: 2018/2/5 17:58
 * @Modifier: zhangkui
 * @version: V1.0
 */
public class ProcessTypesVo implements Serializable{

    private static final long serialVersionUID = 5938057168013336274L;

    private String pType;
    private String pId;

    public String getpType() {
        return pType;
    }

    public void setpType(String pType) {
        this.pType = pType;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }
}
