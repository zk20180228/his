package cn.honry.oa.log.action;

import cn.honry.oa.log.service.LogService;
import cn.honry.oa.log.vo.DepartMentVo;
import cn.honry.oa.log.vo.EmployeeExtendVo;
import cn.honry.oa.log.vo.ProcessTopicVo;
import cn.honry.oa.log.vo.TaskDelVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:工作流删除日志
 * @Author: zhangkui
 * @CreateDate: 2018/1/4 16:13
 * @Modifier: zhangkui
 * @version: V1.0
 */
@Controller
@Namespace("/oa/log")
@ParentPackage("global")
@Scope("prototype")
public class LogAction extends ActionSupport {

    //开始时间
    private String startTime;
    //结束时间
    private String endTime;
    //当前页
    private String page;
    //每页显示的记录数
    private String rows;
    //姓名
    private String eName;
    //工号
    private String jobNo;
    //科室名
    private String dName;
    //科室编号
    private String deptCode;
    //流程配置的名字
    private String pName;
    //1流程2人员3科室
    private String flag;
    //取决于flag 1流程分类id2人员员工号3科室编号
    private String condition;
    // 流程/人员/科室的名字
    private String t_value;

    @Resource
    private LogService logService;
    public void setLogService(LogService logService) {
        this.logService = logService;
    }


    //跳转到任务删除日志列表页面
    @Action(value="/toActLogUI",results = {@Result(name = "list",location = "/WEB-INF/pages/oa/log/actLogUI.jsp")})
    public String toActLogUI(){

        return "list";
    }

    //跳转删除选项页面
    @Action(value="/toChooseUI",results = {@Result(name = "list",location = "/WEB-INF/pages/oa/log/chooseUI.jsp")})
    public String toChooseUI(){

        return "list";
    }





    /**
     * @desc:查询任务删除日志列表
     * @author:  zhangkui
     * @create:  2018/1/4 16:48
     * @version: V1.0
     * @param
     * @throws:
     */
    @Action("/findTaskDelVoList")
    public void findTaskDelVoList(){

        Map<String, Object> map = new HashMap<>();

        Integer total=0;
        List<TaskDelVo> list=null;
        try {
            list=logService.findTaskDelVoList(startTime,endTime,page,rows);
            //渲染字段值
            for (TaskDelVo vo : list) {
                   String v=vo.getT_value();
                   String rs="";
                   if(StringUtils.isNotBlank(v)){
                       String[] vs =v.split(",");
                       for(int i=0;i<vs.length;i++){
                           if(i==5){
                               rs += vs[i]+",....";
                               break;
                           }else {
                               rs += vs[i]+",";
                           }
                       }
                       if(rs.endsWith(",")){
                           rs=rs.substring(0,rs.length()-2);
                       }
                   }
                   vo.setT_value(rs);
            }

            total=logService.findTaskDelVoCount(startTime,endTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list == null) {
            list=new ArrayList<TaskDelVo>();
        }

        map.put("total",total);
        map.put("rows",list);

        String json = JSONUtils.toJson(map);

        WebUtils.webSendJSON(json);
    }

    //人员列表
    /**
     * @desc:查询员工扩展列表
     * @author:  zhangkui
     * @create:  2018/1/4 16:48
     * @version: V1.0
     * @param
     * @throws:
     */
    @Action("/findEmpList")
    public void findEmpList(){

        Map<String, Object> map = new HashMap<>();

        Integer total=0;
        List<EmployeeExtendVo> list=null;
        try {
            list=logService.findEmpList(eName,jobNo,page,rows);
            total=logService.findEmpCount(eName,jobNo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list == null) {
            list=new ArrayList<EmployeeExtendVo>();
        }

        map.put("total",total);
        map.put("rows",list);

        String json = JSONUtils.toJson(map);

        WebUtils.webSendJSON(json);
    }

    //科室列表
    /**
     * @desc:查询科室列表
     * @author:  zhangkui
     * @create:  2018/1/4 16:48
     * @version: V1.0
     * @param
     * @throws:
     */
    @Action("/findDeptList")
    public void findDeptList(){

        Map<String, Object> map = new HashMap<>();

        Integer total=0;
        List<DepartMentVo> list=null;
        try {
            list=logService.findDeptList(dName,deptCode,page,rows);
            total=logService.findDeptCount(dName,deptCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list == null) {
            list=new ArrayList<DepartMentVo>();
        }

        map.put("total",total);
        map.put("rows",list);

        String json = JSONUtils.toJson(map);

        WebUtils.webSendJSON(json);
    }

    //流程配置列表
    /**
     * @desc:查询流程配置列表
     * @author:  zhangkui
     * @create:  2018/1/4 16:48
     * @version: V1.0
     * @param
     * @throws:
     */
    @Action("/findProcessList")
    public void findProcessList(){

        Map<String, Object> map = new HashMap<>();

        Integer total=0;
        List<ProcessTopicVo> list=null;
        try {
            list=logService.findProcessTopicList(pName,page,rows);
            total=logService.findProcessTopicCount(pName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list == null) {
            list=new ArrayList<ProcessTopicVo>();
        }

        map.put("total",total);
        map.put("rows",list);

        String json = JSONUtils.toJson(map);

        WebUtils.webSendJSON(json);
    }

    /**
     * @desc:更新数据，添加删除日志
     * @author:  zhangkui
     * @create:  2018/1/5 14:19
     * @version: V1.0
     * @param
     * @throws:
     */
    @Action("/updateTaskInfo")
    public void updateTaskInfo(){

        Map<String, Object> map = new HashMap<>();
        map.put("data","false");
        if(StringUtils.isNotBlank(flag)&&StringUtils.isNotBlank(condition)){
            try {
                logService.updateTaskInfo(flag,condition,t_value);
                map.put("data","true");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String json = JSONUtils.toJson(map);

        WebUtils.webSendJSON(json);
    }


    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getRows() {
        return rows;
    }

    public void setRows(String rows) {
        this.rows = rows;
    }

    public String geteName() {
        return eName;
    }

    public void seteName(String eName) {
        this.eName = eName;
    }

    public String getJobNo() {
        return jobNo;
    }

    public void setJobNo(String jobNo) {
        this.jobNo = jobNo;
    }

    public String getdName() {
        return dName;
    }

    public void setdName(String dName) {
        this.dName = dName;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getT_value() {
        return t_value;
    }

    public void setT_value(String t_value) {
        this.t_value = t_value;
    }
}
