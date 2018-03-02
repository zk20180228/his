package cn.honry.oa.allWorks.action;
import cn.honry.oa.allWorks.service.AllWorksService;
import cn.honry.oa.allWorks.vo.*;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @Description: OA管理员用来查询所有流程的控制器
 * @Author: zhangkui
 * @CreateDate: 2018/2/2 14:28
 * @Modifier: zhangkui
 * @version: V1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/activiti/allWorks")
public class AllWorksAction extends ActionSupport {

    private String page;//当前页
    private String rows;//每页显示的记录数
    private String empJobNo;//员工号
    private String processId;//流程名字对应的id
    private String startTime;//开始时间
    private String endTime;//结束时间
    private String pType;//流程分类
    private String blDept;//所属科室
    private String work_Flag;//选项卡标记，1：统计概况，2：流转中，3：已办毕，4：催办记录 5:操作记录
    private TOaAssigneeChangeRecordVo vo;//流程任务负责人变更记录表对应的实体
    private String voStr;//封装了TOaAssigneeChangeRecordVo的json串

    @Resource
    private AllWorksService allWorksService;
    public void setAllWorksService(AllWorksService allWorksService) {
        this.allWorksService = allWorksService;
    }

    /**
     * http://localhost:8080/activiti/allWorks/toWorkMonitorUI.action
     * 流程查询界面
     * @return
     */
    @Action(value = "toWorkMonitorUI", results = { @Result(name = "list",
            location = "/WEB-INF/pages/oa/activiti/allWorks/workMonitorUI.jsp")},
            interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
    public String toWorkMonitorUI(){

        return "list";
    }

    @Action("workList")
    public void workList(){
        if(StringUtils.isNotBlank(work_Flag)){
            if("1".equals(work_Flag)){
                this.staOverviewList();
            }
            if("2".equals(work_Flag)){
                this.runningList();
            }
            if("3".equals(work_Flag)){
                this.completeList();
            }
            if("4".equals(work_Flag)){
                this.remindList();
            }
            if("5".equals(work_Flag)){
                this.assigneeChangeRecordList();
            }
        }else{
            Map<String, Object> map = new HashMap<>();
            map.put("total",0);
            map.put("rows",new ArrayList());

            String json = JSONUtils.toJson(map);

            WebUtils.webSendJSON(json);
        }
    }


    /**
     * 流程发起人/催办人下拉框
     */
    @Action("empList")
    public void empList(){

        List<EmpVo> list= null;
        try {
            list = allWorksService.empList();
        } catch (Exception e) {
            list=new ArrayList<>();
            e.printStackTrace();
        }

        String json = JSONUtils.toJson(list);
        WebUtils.webSendJSON(json);
    }


    //流程分类下拉框
    @Action("pTypeList")
    public void pTypeList(){

        List<ProcessTypesVo> list= null;
        try {
            list = allWorksService.pTypeList();
        } catch (Exception e) {
            list=new ArrayList<>();
            e.printStackTrace();
        }

        String json = JSONUtils.toJson(list);
        WebUtils.webSendJSON(json);
    }


    //所属科室下拉框
    @Action("deptList")
    public void deptList(){
        List<DeptVo> list= null;
        try {
            list = allWorksService.deptList();
        } catch (Exception e) {
            list=new ArrayList<>();
            e.printStackTrace();
        }

        String json = JSONUtils.toJson(list);
        WebUtils.webSendJSON(json);
    }

    //流程名称下拉框
    @Action("processList")
    public void processList(){
        List<ProcessComboxVo> list= null;
        try {
            list = allWorksService.processList();
        } catch (Exception e) {
            list=new ArrayList<>();
            e.printStackTrace();
        }

        String json = JSONUtils.toJson(list);
        WebUtils.webSendJSON(json);

    }


    //统计概况列表
    public void staOverviewList(){

        List<StaOverviewVo>  list=null;
        Integer total=0;
        try {
            list= allWorksService.staOverviewList(processId,startTime,endTime,page,rows,work_Flag);
            total= allWorksService.staOverviewNum(processId,startTime,endTime,work_Flag);
        } catch (Exception e) {
            list=new ArrayList<>();
            e.printStackTrace();
        }

        Map<String, Object> map = new HashMap<>();
        map.put("total",total);
        map.put("rows",list);

        String json = JSONUtils.toJson(map);

        WebUtils.webSendJSON(json);

    }

    //运转中的流程
    public void runningList(){
        List<ProcessVo>  list=null;
        Integer total=0;
        try {
            list= allWorksService.runningList(pType,blDept,empJobNo,startTime,endTime,page,rows,work_Flag);
            total= allWorksService.runningNum(pType,blDept,empJobNo,startTime,endTime,work_Flag);
        } catch (Exception e) {
            list=new ArrayList<>();
            e.printStackTrace();
        }

        Map<String, Object> map = new HashMap<>();
        map.put("total",total);
        map.put("rows",list);

        String json = JSONUtils.toJson(map);

        WebUtils.webSendJSON(json);
    }

    //OA管理员通过运行中的流程列表下的指定审批人功能
    @Action("appointAssignees")
    public void appointAssignees(){

        String flag="false";//是否成功的标记

        try {
            if(null!=vo){
                allWorksService.appointAssignees(vo);
                flag="true";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        WebUtils.webSendString(flag);

    }

    @Action(value = "toappointAssigneesUI", results = { @Result(name = "list",
            location = "/WEB-INF/pages/oa/activiti/allWorks/appointAssignees.jsp")},
            interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
    public String toappointAssigneesUI(){

        HttpServletRequest request = ServletActionContext.getRequest();
        try {
            voStr = new String(request.getParameter("voStr").getBytes("iso-8859-1"), "utf-8");
            if(StringUtils.isNotBlank(voStr)){
                //替换掉前台有可能传递过来的 undefined
                voStr=voStr.replace("undefined","");
            }
        } catch (UnsupportedEncodingException e) {
            voStr = "";
            e.printStackTrace();
        }

        request.setAttribute("voStr",voStr);
        return "list";
    }







    //办毕流程
    public void completeList(){

        List<ProcessVo>  list=null;
        Integer total=0;
        try {
            list= allWorksService.completeList(pType,blDept,empJobNo,startTime,endTime,page,rows,work_Flag);
            total= allWorksService.completeNum(pType,blDept,empJobNo,startTime,endTime,work_Flag);
        } catch (Exception e) {
            list=new ArrayList<>();
            e.printStackTrace();
        }

        Map<String, Object> map = new HashMap<>();
        map.put("total",total);
        map.put("rows",list);

        String json = JSONUtils.toJson(map);

        WebUtils.webSendJSON(json);
    }

    //催办记录列表
    public void remindList(){

        List<RemindVo>  list=null;
        Integer total=0;
        try {
            list= allWorksService.remindList(pType,blDept,empJobNo,startTime,endTime,page,rows,work_Flag);
            total= allWorksService.remindNum(pType,blDept,empJobNo,startTime,endTime,work_Flag);
        } catch (Exception e) {
            list=new ArrayList<>();
            e.printStackTrace();
        }

        Map<String, Object> map = new HashMap<>();
        map.put("total",total);
        map.put("rows",list);

        String json = JSONUtils.toJson(map);

        WebUtils.webSendJSON(json);

    }

    //任务负责人变更列表
    public void assigneeChangeRecordList(){

        List<TOaAssigneeChangeRecordVo>  list=null;
        Integer total=0;
        try {
            list= allWorksService.assigneeChangeRecordList(pType,blDept,processId,empJobNo,startTime,endTime,page,rows,work_Flag);
            total= allWorksService.assigneeChangeRecordNum(pType,blDept,processId,empJobNo,startTime,endTime,work_Flag);
        } catch (Exception e) {
            list=new ArrayList<>();
            e.printStackTrace();
        }

        Map<String, Object> map = new HashMap<>();
        map.put("total",total);
        map.put("rows",list);

        String json = JSONUtils.toJson(map);

        WebUtils.webSendJSON(json);

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

    public String getEmpJobNo() {
        return empJobNo;
    }

    public void setEmpJobNo(String empJobNo) {
        this.empJobNo = empJobNo;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
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

    public String getpType() {
        return pType;
    }

    public void setpType(String pType) {
        this.pType = pType;
    }

    public String getBlDept() {
        return blDept;
    }

    public void setBlDept(String blDept) {
        this.blDept = blDept;
    }

    public String getWork_Flag() {
        return work_Flag;
    }

    public void setWork_Flag(String work_Flag) {
        this.work_Flag = work_Flag;
    }

    public TOaAssigneeChangeRecordVo getVo() {
        return vo;
    }

    public void setVo(TOaAssigneeChangeRecordVo vo) {
        this.vo = vo;
    }
}
