package cn.honry.oa.log.vo;
import java.io.Serializable;
/**
 * @Description:用户删除自己创建的任务的vo
 * @Author: zhangkui
 * @CreateDate: 2018/1/4 16:19
 * @Modifier: zhangkui
 * @version: V1.0
 */
public class TaskDelVo implements Serializable{


    private static final long serialVersionUID = 8348868141297303501L;

    //主键
    private String id;
    //删除维度(1流程2人员3科室)
    private Integer type;
    //纬度值(流程id人员jobno科室code)
    private String code;
    //删除数量
    private Integer num;
    //创建人
    private String createUser;
    //创建人所在科室
    private String createDept;
    //创建时间
    private String createTime;
    //更新人
    private String updateUser;
    //更新时间
    private String updateTime;
    //删除人
    private String deleteUser;
    //删除时间
    private String deleteTime;
    //是否删除：0否,1是
    private Integer stop_flg=0;
    //是否停用：0否,1是
    private Integer del_flg=0;
    //人员,科室,流程名字
    private String t_value;

    //纬度值的名字(人员,科室,流程名字)：用于查询
    private String deptName;
    //创建人的名字
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreateDept() {
        return createDept;
    }

    public void setCreateDept(String createDept) {
        this.createDept = createDept;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getDeleteUser() {
        return deleteUser;
    }

    public void setDeleteUser(String deleteUser) {
        this.deleteUser = deleteUser;
    }

    public String getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(String deleteTime) {
        this.deleteTime = deleteTime;
    }

    public Integer getStop_flg() {
        return stop_flg;
    }

    public void setStop_flg(Integer stop_flg) {
        this.stop_flg = stop_flg;
    }

    public Integer getDel_flg() {
        return del_flg;
    }

    public void setDel_flg(Integer del_flg) {
        this.del_flg = del_flg;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getT_value() {
        return t_value;
    }

    public void setT_value(String t_value) {
        this.t_value = t_value;
    }
}
