package cn.honry.oa.allWorks.dao.impl;

import cn.honry.oa.allWorks.dao.AllWorksDao;
import cn.honry.oa.allWorks.vo.*;
import cn.honry.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Description:
 * @Author: zhangkui
 * @CreateDate: 2018/2/2 15:56
 * @Modifier: zhangkui
 * @version: V1.0
 */
@Repository("allWorksDao")
public class AllWorksDaoImpl implements AllWorksDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    @Override
    public List<EmpVo> empList() {

        String sql = "SELECT EMPLOYEE_JOBNO AS empJobNo,EMPLOYEE_NAME AS empName FROM T_EMPLOYEE_EXTEND WHERE STOP_FLG = 0 AND DEL_FLG = 0";

        List<EmpVo> list = namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper(EmpVo.class));
        if (list == null || list.size() == 0) {
            list = new ArrayList<EmpVo>();
        }

        return list;
    }

    @Override
    public List<ProcessTypesVo> pTypeList() {

        String sql = "SELECT id as pid,NAME as pType FROM T_OA_BPM_CATEGORY where DEL_FLG=0 and STOP_FLG=0 ORDER BY PRIORITY";

        List<ProcessTypesVo> list = namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper(ProcessTypesVo.class));
        if (list == null || list.size() == 0) {
            list = new ArrayList<ProcessTypesVo>();
        }

        return list;
    }


    @Override
    public List<DeptVo> deptList() {


        String sql = "SELECT DEPT_CODE as deptCode,DEPT_NAME as deptName FROM  T_OA_ACTIVITI_DEPT ORDER BY DEPT_ORDER";

        List<DeptVo> list = namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper(DeptVo.class));
        if (list == null || list.size() == 0) {
            list = new ArrayList<DeptVo>();
        }

        return list;
    }

    @Override
    public List<ProcessComboxVo> processList() {

        String sql = "select id as processId,NAME as processName  from t_oa_bpm_process where del_flg=0 and stop_flg=0 ORDER BY PRIORITY";

        List<ProcessComboxVo> list = namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper(ProcessComboxVo.class));
        if (list == null || list.size() == 0) {
            list = new ArrayList<>();
        }

        return list;
    }

    @Override
    public List<StaOverviewVo> staOverviewList(String processId, String startTime, String endTime, String page, String rows, String work_flag) {

        Integer p = (page == null ? 20 : Integer.parseInt(page));
        Integer r = (rows == null ? 20 : Integer.parseInt(rows));
        StringBuffer sb = new StringBuffer();

        sb.append(" select d.* from ( ");
        sb.append(" select u.*,rownum as r from ( ");
        sb.append(this.getStaOverviewBaseSql(processId, startTime, endTime));
        sb.append(" ) u where rownum <=" + p * r + " ) d where d.r >" + (p - 1) * r + " ");

        List<StaOverviewVo> list = namedParameterJdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper(StaOverviewVo.class));

        if (list != null && list.size() > 0) {
            return list;
        }
        return new ArrayList<>();
    }

    @Override
    public Integer staOverviewNum(String processId, String startTime, String endTime, String work_flag) {

        StringBuffer sb = new StringBuffer();
        sb.append(" select count(1) as num from ( ");
        sb.append(this.getStaOverviewBaseSql(processId, startTime, endTime));
        sb.append(" ) ");

        Integer num = namedParameterJdbcTemplate.queryForObject(sb.toString(), new HashMap(), Integer.class);
        if (num != null) {
            return num;
        }

        return 0;
    }

    public String getStaOverviewBaseSql(String processId, String startTime, String endTime) {

        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT ");
        sb.append(" M .*, A.NAME AS pName ");
        sb.append(" FROM ( ");
        sb.append("                SELECT ");
        sb.append("                 SUM (DECODE(T .endTime, NULL, 1, 0)) AS runNum, ");
        sb.append("                 SUM (DECODE(T .endTime, NULL, 0, 1)) AS completeNum, ");
        sb.append("                 SUM (DECODE(T .rid, NULL, 0, 1)) AS remindNum, ");
        sb.append("                 T . CATEGORY AS CATEGORY ");
        sb.append("                 FROM( ");
        sb.append("                                SELECT ");
        sb.append("                                 s.endTime AS endTime, ");
        sb.append("                                 s. CATEGORY AS CATEGORY, ");
        sb.append("                                 r. ID AS rid ");
        sb.append("                                 FROM ( ");
        sb.append("                                           SELECT ");
        sb.append("                                                 H .PROC_INST_ID_ AS pid, ");
        sb.append("                                                 H .END_TIME_ AS endTime, ");
        sb.append("                                                 K . CATEGORY AS CATEGORY ");
        sb.append("                                                 FROM ");
        sb.append("                                                 ACT_HI_PROCINST H ");
        sb.append("                                                 LEFT JOIN T_OA_KV_RECORD K ON H .BUSINESS_KEY_ = K . ID ");
        sb.append("                                                 WHERE ");
        sb.append("                                                 K .DEL_FLG = 0 ");
        sb.append("                                                 AND K .STOP_FLG = 0 ");
        if (StringUtils.isNotBlank(processId)) {

            sb.append("                                              and K.CATEGORY='" + processId + "' ");
        }
        if (StringUtils.isNotBlank(startTime)) {

            sb.append("                                               and h.START_TIME_>=to_date('" + startTime + "','yyyy-mm-dd hh24:mi:ss') ");
        }
        if (StringUtils.isNotBlank(endTime)) {

            sb.append("                                               and h.START_TIME_<=to_date('" + endTime + "','yyyy-mm-dd hh24:mi:ss') ");
        }
        sb.append("                                         ) s ");
        sb.append("                                 LEFT JOIN T_OA_REMINDERS r ON s.pid = r.PROCEDUREID ");
        sb.append("                                 ORDER BY ");
        sb.append("                                 s.endTime DESC ");
        sb.append("                         ) T ");
        sb.append("                 GROUP BY ");
        sb.append("                 T . CATEGORY ");
        sb.append("         ) M ");
        sb.append(" LEFT JOIN T_OA_BPM_PROCESS A ON A . ID = M . CATEGORY ");
        sb.append(" WHERE ");
        sb.append(" A .STOP_FLG = 0 ");
        sb.append(" AND A .DEL_FLG = 0 ");
        sb.append(" ORDER BY A.NAME ");


        return sb.toString();
    }


    @Override
    public List<ProcessVo> runningList(String pType, String blDept, String empJobNo, String startTime, String endTime, String page, String rows, String work_flag) {

        List<ProcessVo> list = this.getProcessList(pType, blDept, empJobNo, startTime, endTime, page, rows, work_flag);
        if (list != null && list.size() > 0) {
            for (ProcessVo vo : list) {
                //1.对审批人处理，只显示前两个
                String approvers = vo.getApprover();
                if (StringUtils.isNotBlank(approvers)) {
                    String[] approver = approvers.split(",");
                    if (approver.length >= 2) {
                        if(approver.length==2){
                            vo.setApprover(this.getEmp(approver[0]) + "," + this.getEmp(approver[1]));
                        }else {
                            vo.setApprover(this.getEmp(approver[0]) + "," + this.getEmp(approver[1]) + ",...");
                        }
                    } else {
                        vo.setApprover(this.getEmp(approver[0]));
                    }
                }
                //2.停留时长处理
                String createTime = vo.getNodeCreateTime();
                if (StringUtils.isNotBlank(createTime)) {
                    String computeDate = this.computeDate(createTime, DateUtils.formatDateY_M_D_H_M_S(new Date()));
                    vo.setBlockTime(computeDate);
                }
            }

            return list;
        }
        return new ArrayList<>();
    }


    @Override
    public Integer runningNum(String pType, String blDept, String empJobNo, String startTime, String endTime, String work_flag) {

        return this.getTotalPages(pType, blDept, empJobNo, startTime, endTime, work_flag);
    }

    @Override
    public List<ProcessVo> completeList(String pType, String blDept, String empJobNo, String startTime, String endTime, String page, String rows, String work_flag) {

        List<ProcessVo> list = this.getProcessList(pType, blDept, empJobNo, startTime, endTime, page, rows, work_flag);
        if (list != null && list.size() > 0) {
            //1.流程总用时处理
            for (ProcessVo vo : list) {
                if (StringUtils.isNotBlank(vo.getStartTime()) && StringUtils.isNotBlank(vo.getEndTime())) {
                    vo.setTotalTime(this.computeDate(vo.getStartTime(), vo.getEndTime()));
                }
            }

            return list;
        }

        return new ArrayList<>();
    }

    @Override
    public Integer completeNum(String pType, String blDept, String empJobNo, String startTime, String endTime, String work_flag) {

        return this.getTotalPages(pType, blDept, empJobNo, startTime, endTime, work_flag);
    }

    @Override
    public List<RemindVo> remindList(String pType, String blDept, String empJobNo, String startTime, String endTime, String page, String rows, String work_flag) {

        Integer p = (page == null ? 20 : Integer.parseInt(page));
        Integer r = (rows == null ? 20 : Integer.parseInt(rows));
        StringBuffer sb = new StringBuffer();

        sb.append(" select f.* from ( ");
        sb.append(" select q.*,rownum as r from ( ");
        sb.append(this.remindBaseSql(pType, blDept, empJobNo, startTime, endTime));
        sb.append(" ) q where rownum <=" + p * r + " ) f where f.r >" + (p - 1) * r + " ");

        List<RemindVo> list = namedParameterJdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper(RemindVo.class));

        if (list != null && list.size() > 0) {
            //对停留时间做处理
            for (RemindVo vo : list) {
                String startTime1 = vo.getStartTime();
                String endTime1 = vo.getEndTime();
                if (StringUtils.isBlank(endTime1)) {
                    endTime1 = DateUtils.formatDateY_M_D_H_M_S(new Date());//如果没结束，结束时间为当前时间
                }
                vo.setBlockTime(this.computeDate(startTime1, endTime1));
            }

            return list;
        }
        return new ArrayList<>();
    }

    @Override
    public Integer remindNum(String pType, String blDept, String empJobNo, String startTime, String endTime, String work_flag) {

        StringBuffer sb = new StringBuffer();
        sb.append(" select count(1) as num from ( ");
        sb.append(this.remindBaseSql(pType, blDept, empJobNo, startTime, endTime));
        sb.append(" ) ");

        Integer num = namedParameterJdbcTemplate.queryForObject(sb.toString(), new HashMap(), Integer.class);
        if (num != null) {
            return num;
        }

        return 0;
    }

    @Override
    public void updateTask(String taskId,String newAssignee, String newAssigneeName, String updateUser) {

        StringBuffer sb = new StringBuffer();
        sb.append(" UPDATE T_OA_TASK_INFO ");
        sb.append(" SET ASSIGNEE =:ASSIGNEE, ");
        sb.append(" ASSIGNEENAME =:ASSIGNEENAME, ");
        sb.append(" UPDATEUSER =:UPDATEUSER, ");
        sb.append(" UPDATETIME =:UPDATETIME ");
        sb.append(" WHERE ");
        sb.append(" ID =:ID ");

        Map<String, Object> map = new HashMap<>();
        map.put("ASSIGNEE",newAssignee);
        map.put("ASSIGNEENAME",newAssigneeName);
        map.put("UPDATEUSER",updateUser);
        map.put("UPDATETIME",new Date());
        map.put("ID",taskId);

        namedParameterJdbcTemplate.update(sb.toString(),map);
    }

    @Override
    public void addAssigneeChangeRecord(TOaAssigneeChangeRecordVo vo) {

        StringBuffer sb = new StringBuffer();
        sb.append(" INSERT INTO T_OA_ASSIGNEE_CHANGE_RECORD ( ");
        sb.append("         ID, ");
        sb.append("         TASK_INFO_ID, ");
        sb.append("         PROCESS_NAME, ");
        sb.append("         TASK_NAME, ");
        sb.append("         OLD_ASSIGNEE, ");
        sb.append("         OLD_ASSIGNEE_NAME, ");
        sb.append("         NEW_ASSIGNEE, ");
        sb.append("         NEW_ASSIGNEE_NAME, ");
        sb.append("         PROCESS_STARTER, ");
        sb.append("         PROCESS_STARTER_NAME, ");
        sb.append("         PROCESS_START_TIME, ");
        sb.append("         CREATEUSER, ");
        sb.append("         CREATEUSER_NAME, ");
        sb.append("         CREATEDEPT, ");
        sb.append("         CREATETIME, ");
        sb.append("         UPDATEUSER, ");
        sb.append("         UPDATETIME, ");
        sb.append("         STOP_FLG, ");
        sb.append("         DEL_FLG ");
        sb.append(" ) ");
        sb.append(" VALUES ");
        sb.append("         ( ");
        sb.append("                 :ID, ");
        sb.append("                 :TASK_INFO_ID, ");
        sb.append("                 :PROCESS_NAME, ");
        sb.append("                 :TASK_NAME, ");
        sb.append("                 :OLD_ASSIGNEE, ");
        sb.append("                 :OLD_ASSIGNEE_NAME, ");
        sb.append("                 :NEW_ASSIGNEE, ");
        sb.append("                 :NEW_ASSIGNEE_NAME, ");
        sb.append("                 :PROCESS_STARTER, ");
        sb.append("                 :PROCESS_STARTER_NAME, ");
        sb.append("                 :PROCESS_START_TIME, ");
        sb.append("                 :CREATEUSER, ");
        sb.append("                 :CREATEUSER_NAME, ");
        sb.append("                 :CREATEDEPT, ");
        sb.append("                 :CREATETIME, ");
        sb.append("                 :UPDATEUSER, ");
        sb.append("                 :UPDATETIME, ");
        sb.append("                 :STOP_FLG, ");
        sb.append("                 :DEL_FLG ");
        sb.append("         ) ");

        Map<String, Object> map = new HashMap<>();
        map.put("ID",vo.getId());
        map.put("TASK_INFO_ID",vo.getTaskInfoId());
        map.put("PROCESS_NAME",vo.getProcessName());
        map.put("TASK_NAME",vo.getTaskName());
        map.put("OLD_ASSIGNEE",vo.getOldAssignee());
        map.put("OLD_ASSIGNEE_NAME",vo.getOldAssigneeName());
        map.put("NEW_ASSIGNEE",vo.getNewAssignee());
        map.put("NEW_ASSIGNEE_NAME",vo.getNewAssigneeName());
        map.put("PROCESS_STARTER",vo.getProcessStarter());
        map.put("PROCESS_STARTER_NAME",vo.getProcessStarterName());
        map.put("PROCESS_START_TIME",vo.getProcessStartTime());
        map.put("CREATEUSER",vo.getCreateuser());
        map.put("CREATEUSER_NAME",vo.getCreateuserName());
        map.put("CREATEDEPT",vo.getCreatedept());
        map.put("CREATETIME",vo.getCreatetime());
        map.put("UPDATEUSER",vo.getUpdateuser());
        map.put("UPDATETIME",vo.getUpdatetime());
        map.put("STOP_FLG",vo.getStopFlg());
        map.put("DEL_FLG",vo.getDelFlg());

        namedParameterJdbcTemplate.update(sb.toString(),map);
    }

    @Override
    public List<TOaAssigneeChangeRecordVo> assigneeChangeRecordList(String pType,String blDept,String processId, String empJobNo, String startTime, String endTime, String page, String rows, String work_flag) {

        Integer p = (page == null ? 20 : Integer.parseInt(page));
        Integer r = (rows == null ? 20 : Integer.parseInt(rows));
        StringBuffer sb = new StringBuffer();

        sb.append(" select f.* from ( ");
        sb.append(" select q.*,rownum as r from ( ");
        sb.append(this.getAssigneeChangeRecordBaseSql(pType,blDept,processId, empJobNo,startTime, endTime));
        sb.append(" ) q where rownum <=" + p * r + " ) f where f.r >" + (p - 1) * r + " ");

        List<TOaAssigneeChangeRecordVo> list = namedParameterJdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper(TOaAssigneeChangeRecordVo.class));

        if(list!=null&&list.size()>0){
            return list;
        }

        return new ArrayList<>();
    }

    @Override
    public Integer assigneeChangeRecordNum(String pType,String blDept,String processId, String empJobNo, String startTime, String endTime, String work_flag) {

        StringBuffer sb = new StringBuffer();
        sb.append(" select count(1) as num from ( ");
        sb.append(this.getAssigneeChangeRecordBaseSql(pType,blDept,processId, empJobNo,startTime, endTime));
        sb.append(" ) ");

        Integer num = namedParameterJdbcTemplate.queryForObject(sb.toString(), new HashMap(), Integer.class);
        if (num != null) {
            return num;
        }

        return 0;
    }

    @Override
    public void updateHiTask(String id_, String newAssignee) {
        String sql="update  ACT_RU_TASK set ASSIGNEE_='"+newAssignee+"'  where id_='"+id_+"'";
        namedParameterJdbcTemplate.update(sql,new HashMap());
    }

    @Override
    public void updateRuTask(String id_, String newAssignee) {
        String sql="update  ACT_HI_TASKINST set ASSIGNEE_='"+newAssignee+"'  where id_='"+id_+"'";
        namedParameterJdbcTemplate.update(sql,new HashMap());
    }


    public String getAssigneeChangeRecordBaseSql(String pType,String blDept,String processId, String empJobNo, String startTime, String endTime){

        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT ");
        sb.append(" r.PROCESS_NAME as processName, ");
        sb.append(" r.TASK_NAME as taskName, ");
        sb.append(" r.OLD_ASSIGNEE_NAME as oldAssigneeName, ");
        sb.append(" r.NEW_ASSIGNEE_NAME as newAssigneeName, ");
        sb.append(" r.PROCESS_START_TIME as processStartTime, ");
        sb.append(" r.PROCESS_STARTER_NAME as processStarterName, ");
        sb.append(" r.CREATETIME as createtime, ");
        sb.append(" r.CREATEUSER_NAME as createuserName,");
        sb.append(" p.NAME as pName ");
        sb.append(" FROM ");
        sb.append(" T_OA_ASSIGNEE_CHANGE_RECORD r ");
        sb.append(" LEFT JOIN T_OA_TASK_INFO T ON r.TASK_INFO_ID = T . ID ");
        sb.append(" LEFT JOIN T_OA_KV_RECORD D ON D . ID = T .BUSINESS_KEY ");
        sb.append(" LEFT JOIN T_OA_BPM_PROCESS p on p.id=D.CATEGORY ");
        sb.append(" WHERE ");
        sb.append(" r.STOP_FLG = 0 ");
        sb.append(" AND r.DEL_FLG = 0 ");
        if(StringUtils.isNotBlank(pType)){
            sb.append(" AND p.CATEGORY_CODE = '"+pType+"' ");
        }
        if(StringUtils.isNotBlank(blDept)){
            sb.append(" AND p.DEPT_CODE = '"+blDept+"' ");
        }
        if(StringUtils.isNotBlank(empJobNo)){
            sb.append(" AND r.PROCESS_STARTER = '"+empJobNo+"' ");
        }
        if(StringUtils.isNotBlank(startTime)){
            sb.append(" and r.PROCESS_START_TIME >=to_date('"+startTime+"','yyyy-mm-dd hh24:mi:ss') ");
        }
        if(StringUtils.isNotBlank(endTime)){
            sb.append(" and r.PROCESS_START_TIME <=to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss') ");
        }
        if(StringUtils.isNotBlank(processId)){
            sb.append(" and D.CATEGORY='"+processId+"' ");
        }
        sb.append(" ORDER BY r.CREATETIME desc ");


        return  sb.toString();
    }



    public String remindBaseSql(String pType, String blDept, String empJobNo, String startTime, String endTime) {

        StringBuffer sb = new StringBuffer();
        sb.append(" select pp.*,to_char(n.COMPLETE_TIME,'yyyy-mm-dd hh24:mi:ss') as endTime from ( ");
        sb.append(" select b.*,(SELECT NAME FROM T_OA_BPM_CATEGORY where id=o.CATEGORY_CODE and DEL_FLG=0 and STOP_FLG=0) as pType ,(SELECT DEPT_NAME FROM T_OA_ACTIVITI_DEPT where DEPT_CODE=o.DEPT_CODE) as blDept from ( ");
        sb.append("         SELECT s.*,k.CATEGORY as CATEGORY from ( ");
        sb.append("         SELECT ");
        sb.append("         r.PORCEDURENAME as title, ");
        sb.append("         R.REMINDENODENAME as pNode, ");
        sb.append("         R.REMINDERNAME as reminder, ");
        sb.append("         R.REMINDEREDNAME as reminded, ");
        sb.append("         R.REMINDCONTENT as reContent, ");
        sb.append("         p.BUSINESS_KEY_ as buskey, ");
        sb.append("         R.REMINDERNUM as remindNum, ");
        sb.append("         r.TASKINFOID as tid,        ");
        sb.append("         to_char(r.CREATETIME,'yyyy-mm-dd hh24:mi:ss') as startTime, ");
        sb.append("         decode(R.REMINDRESTATUS,1,'是','否') as isReader, ");
        sb.append("         decode(R.REMIDERETIME,null,'否','是') as isResponse ");
        sb.append("         FROM ");
        sb.append("         T_OA_REMINDERS r LEFT JOIN ACT_HI_PROCINST p on p.PROC_INST_ID_=R.PROCEDUREID ");
        sb.append("         WHERE ");
        sb.append("         r.DEL_FLG=0 ");
        sb.append("         AND R. TYPE=0 ");
        if (StringUtils.isNotBlank(empJobNo)) {

            sb.append("         and r.REMINDER='" + empJobNo + "' ");
        }
        if (StringUtils.isNotBlank(startTime)) {

            sb.append("         and r.CREATETIME>=to_date('" + startTime + "','yyyy-mm-dd hh24:mi:ss') ");
        }
        if (StringUtils.isNotBlank(endTime)) {

            sb.append("         and r.CREATETIME<to_date('" + endTime + "','yyyy-mm-dd hh24:mi:ss') ");
        }
        sb.append("         ORDER BY r.CREATETIME desc ");
        sb.append(" ) s LEFT JOIN T_OA_KV_RECORD k on k.id=s.BUSKEY ");
        sb.append(" ) b LEFT JOIN T_OA_BPM_PROCESS o on o.id=b.CATEGORY ");
        sb.append(" where 1=1 ");
        if (StringUtils.isNotBlank(pType)) {

            sb.append(" and o.CATEGORY_CODE='" + pType + "' ");
        }
        if (StringUtils.isNotBlank(blDept)) {

            sb.append(" and o.DEPT_CODE='" + blDept + "' ");
        }

        sb.append(" ) pp LEFT JOIN T_OA_TASK_INFO n on PP.tid=n.id ");
        sb.append("  ORDER BY PP.startTime desc ");

        return sb.toString();
    }


    //获取公共部分sql
    public String getSQL(String pType, String blDept, String empJobNo, String startTime, String endTime, String work_flag) {

        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT ");
        sb.append(" H .*, G .curNode AS curNode, ");
        sb.append(" G .approver AS approver, ");
        sb.append(" g.nodeCreateTime as nodeCreateTime, ");
        sb.append(" G.allApproversName AS allApproversName,");
        sb.append(" G.tid AS tid, ");
        sb.append(" G.taskId as taskId ");
        sb.append("         FROM ");
        sb.append(" ( ");
        sb.append("         SELECT ");
        sb.append(" f.* ");
        sb.append("         FROM ");
        sb.append("                 ( ");
        sb.append("                         SELECT ");
        sb.append("                         D .*, E .END_TIME_ AS endTime ");
        sb.append("                         FROM ");
        sb.append("                                 ( ");
        sb.append("                                         SELECT ");
        sb.append("                                         c.*, P .CATEGORY_CODE AS pType, ");
        sb.append("                                         P .DEPT_CODE AS blDept ");
        sb.append("                                         FROM ");
        sb.append("                                                 ( ");
        sb.append("                                                         SELECT ");
        sb.append("                                                         A .*, b. CATEGORY AS bId ");
        sb.append("                                                         FROM ");
        sb.append("                                                                 ( ");
        sb.append("                                                                         SELECT ");
        sb.append("                                                                         T .ATTR2 AS title, ");
        sb.append("                                                                         T .CREATEUSER AS OWNER, ");
        sb.append("                                                                         T .BUSINESS_KEY AS BUSINESS_KEY, ");
        sb.append("                                                                         T . ID AS businessKey, ");
        sb.append("                                                                         T .PROCESS_INSTANCE_ID AS PROCESS_INSTANCE_ID, ");
        sb.append("                                                                         T .CREATETIME AS startTime ");
        sb.append("                                                                         FROM ");
        sb.append("                                                                         T_OA_TASK_INFO T ");
        sb.append("                                                                         WHERE ");
        sb.append("                                                                         T .DEL_FLG = 0 ");
        sb.append("                                                                         AND T .STOP_FLG = 0 ");
        sb.append("                                                                         AND T .CATALOG = 'start' ");
        if (StringUtils.isNotBlank(empJobNo)) {
            sb.append("                                                                         and  t.CREATEUSER='" + empJobNo + "' ");
        }
        if (StringUtils.isNotBlank(startTime)) {
            sb.append("                                                                         and t.CREATETIME>=to_date('" + startTime + "','yyyy-mm-dd hh24:mi:ss') ");
        }
        if (StringUtils.isNotBlank(endTime)) {
            sb.append("                                                                         and t.CREATETIME<to_date('" + endTime + "','yyyy-mm-dd hh24:mi:ss') ");
        }
        sb.append("                                                                         ORDER BY ");
        sb.append("                                                                         T .CREATETIME DESC ");
        sb.append("                                                                 ) A ");
        sb.append("                                                         LEFT JOIN T_OA_KV_RECORD b ON A .BUSINESS_KEY = b. ID ");
        sb.append("                                                         WHERE ");
        sb.append("                                                         b.STOP_FLG = 0 ");
        sb.append("                                                         AND b.DEL_FLG = 0 ");
        sb.append("                                                 ) c ");
        sb.append("                                         LEFT JOIN T_OA_BPM_PROCESS P ON c.BID = P . ID ");
        sb.append("                                         WHERE ");
        sb.append("                                         P .DEL_FLG = 0 ");
        sb.append("                                         AND P .STOP_FLG = 0 ");
        if (StringUtils.isNotBlank(pType)) {

            sb.append("                                        AND P .CATEGORY_CODE = '" + pType + "' ");
        }
        if (StringUtils.isNotBlank(blDept)) {

            sb.append("                                        and p.DEPT_CODE='" + blDept + "' ");
        }
        sb.append("                                 ) D ");
        sb.append("                         LEFT JOIN ACT_HI_PROCINST E ON D .PROCESS_INSTANCE_ID = E .PROC_INST_ID_ ");
        sb.append("                 ) f ");
        sb.append("         WHERE ");
        sb.append(" 1 = 1 ");
        if ("2".equals(work_flag)) {
            sb.append("         and f.ENDTIME is  null ");//流转中
        } else {
            sb.append("         and f.ENDTIME is not null ");//已办毕
        }

        sb.append("				) H ");
        sb.append(" LEFT JOIN ( ");
        sb.append("         SELECT DISTINCT ");
        sb.append("         j.ASSIGNEENAME AS allApproversName, ");
        sb.append("         j.ID AS tid,");
        sb.append("         j.task_id as taskId,");
        sb.append("         j.PROCESS_INSTANCE_ID AS PROCESS_INSTANCE_ID, ");
        sb.append("         j. NAME AS curNode, ");
        sb.append("         j.ASSIGNEE AS approver, ");
        sb.append("         j.CREATETIME as nodeCreateTime ");
        sb.append("         FROM ");
        sb.append(" T_OA_TASK_INFO j ");
        sb.append(" WHERE ");
        sb.append(" j.DEL_FLG = 0 ");
        sb.append(" AND j.STOP_FLG = 0 ");
        sb.append(" AND j.STATUS = 'active' ");
        sb.append(" ) G ON H .PROCESS_INSTANCE_ID = G .PROCESS_INSTANCE_ID ORDER BY h.startTime desc ");


        return sb.toString();
    }

    //获取查询流转中和已办毕流程列表的列表
    public List<ProcessVo> getProcessList(String pType, String blDept, String empJobNo, String startTime, String endTime, String page, String rows, String work_flag) {

        Integer p = (page == null ? 20 : Integer.parseInt(page));
        Integer r = (rows == null ? 20 : Integer.parseInt(rows));
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT ");
        sb.append(" U .title as title, ");
        sb.append(" (select p.EMPLOYEE_NAME from T_EMPLOYEE_EXTEND p where p.EMPLOYEE_JOBNO=u.owner and p.DEL_FLG=0 and p.STOP_FLG=0 ) as owner, ");
        sb.append(" U.OWNER as ownerJobNum, ");
        sb.append(" u.businessKey as businessKey, ");
        sb.append(" u.PROCESS_INSTANCE_ID as processInstanceId, ");
        sb.append(" to_char(u.startTime,'yyyy-mm-dd hh24:mi:ss') as startTime, ");
        sb.append(" (SELECT NAME FROM T_OA_BPM_CATEGORY where id=u.pType) as pType, ");
        sb.append(" (SELECT DEPT_NAME FROM T_OA_ACTIVITI_DEPT where DEPT_CODE=u.blDept) as blDept, ");
        sb.append(" to_char(u.endTime,'yyyy-mm-dd hh24:mi:ss') as endTime, ");
        sb.append(" u.curNode as curNode, ");
        sb.append(" u.approver as approver, ");
        sb.append(" u.approver as allApproversJobNum, ");
        sb.append(" u.allApproversName as allApproversName, ");
        sb.append(" to_char(u.nodeCreateTime,'yyyy-mm-dd hh24:mi:ss') as nodeCreateTime, ");
        sb.append(" u.tid as tid,  ");
        sb.append(" u.taskId as taskId ");
        sb.append(" FROM ");
        sb.append("         ( ");
        sb.append("                 SELECT ");
        sb.append("                 K .*, ROWNUM AS r ");
        sb.append("                 FROM ");
        sb.append("                         ( ");
        sb.append(this.getSQL(pType, blDept, empJobNo, startTime, endTime, work_flag));
        sb.append("	) k  ");
        sb.append("         WHERE ");
        sb.append(" ROWNUM <= " + p * r + " ");
        sb.append(" ) U ");
        sb.append("         WHERE ");
        sb.append(" U .r > " + (p - 1) * r + "");

        List list = namedParameterJdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper(ProcessVo.class));
        return list;
    }

    //获取查询流转中和已办毕流程的总页数
    public Integer getTotalPages(String pType, String blDept, String empJobNo, String startTime, String endTime, String work_flag) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select count(1) as num from ( ");
        sb.append(this.getSQL(pType, blDept, empJobNo, startTime, endTime, work_flag));
        sb.append(" ) ");

        Integer num = namedParameterJdbcTemplate.queryForObject(sb.toString(), new HashMap(), Integer.class);
        if (num != null) {
            return num;
        }

        return 0;
    }


    public String getEmp(String empJobNo) {

        String sql = "SELECT EMPLOYEE_JOBNO AS empJobNo,EMPLOYEE_NAME AS empName FROM T_EMPLOYEE_EXTEND WHERE STOP_FLG = 0 AND DEL_FLG = 0 and EMPLOYEE_JOBNO='" + empJobNo + "'";

        List<EmpVo> list = namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper(EmpVo.class));
        if (list != null && list.size() > 0) {
            return list.get(0).getEmpName();
        }

        return "";
    }


    /**
     * 格式化停留时间
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public String computeDate(String startTime, String endTime) {
        Date sDate = DateUtils.parseDateY_M_D_H_M_S(startTime);
        Date eDate = DateUtils.parseDateY_M_D_H_M_S(endTime);

        if (DateUtils.compDate(sDate, eDate)) {
            return "";
        }

        Long s = sDate.getTime();
        Long e = eDate.getTime();

        Long m = e - s;
        int i = (int) (m / (24 * 60 * 60 * 1000));
        if (i >= 1) {//时间大于1天
            //按天算，不满一天算一天
            return (i + 1) + "天";
        } else {
            String rs = "";
            Long h = m / (60 * 60 * 1000);
            if (h == 0l) {
                rs += "00:";
            } else if (h < 10l) {
                rs += "0" + h + ":";
            } else {
                rs += h + ":";
            }

            Long _m = (m - h * 60 * 60 * 1000) / (60 * 1000);
            if (_m == 0l) {
                rs += "00:";
            } else if (_m < 10l) {
                rs += "0" + _m + ":";
            } else {
                rs += _m + ":";
            }

            Long se = (m - h * 60 * 60 * 1000 - _m * 60 * 1000) / 1000;
            if (se < 10) {
                rs += "0" + se;
            } else {
                rs += se;
            }
            return rs;
        }
    }
}
