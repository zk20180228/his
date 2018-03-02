package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yuke
 * @create 2018-02-26 13:56
 * @desc 栏目对照bean
 **/
public class MenuControl extends Entity{

    private String pcCode;
    private String pcName;
    private String mobileCode;
    private String mobileName;
    public String getPcCode() {
        return pcCode;
    }

    public void setPcCode(String pcCode) {
        this.pcCode = pcCode;
    }

    public String getPcName() {
        return pcName;
    }

    public void setPcName(String pcName) {
        this.pcName = pcName;
    }

    public String getMobileCode() {
        return mobileCode;
    }

    public void setMobileCode(String mobileCode) {
        this.mobileCode = mobileCode;
    }

    public String getMobileName() {
        return mobileName;
    }

    public void setMobileName(String mobileName) {
        this.mobileName = mobileName;
    }
}
