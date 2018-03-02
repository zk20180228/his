package cn.honry.oa.log.dao.impl;



import cn.honry.oa.log.dao.LogDao;
import cn.honry.oa.log.vo.DepartMentVo;
import cn.honry.oa.log.vo.EmployeeExtendVo;
import cn.honry.oa.log.vo.ProcessTopicVo;
import cn.honry.oa.log.vo.TaskDelVo;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Hash;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.security.ProtectionDomain;
import java.util.*;

/**
 * @Description:
 * @Author: zhangkui
 * @CreateDate: 2018/1/4 16:54
 * @Modifier: zhangkui
 * @version: V1.0
 */
@Repository("logDao")
public class LogDaoImpl implements LogDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<TaskDelVo> findTaskDelVoList(String startTime, String endTime, String page, String rows) {

        int p=(page==null?1:Integer.parseInt(page));
        int r=(rows==null?20:Integer.parseInt(rows));

        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT ");
        sb.append(" H .* ");
        sb.append("         FROM ");
        sb.append("                 ( ");
        sb.append("                         SELECT ");
        sb.append("                         f.*, ROWNUM AS r ");
        sb.append("                         FROM ");
        sb.append("                                 ( ");
        sb.append("                                         SELECT ");
        sb.append("                                         T .*, E .EMPLOYEE_NAME AS NAME, ");
        sb.append("                                         D .DEPT_Name AS deptName ");
        sb.append("                                         FROM ");
        sb.append("                                         T_OA_ACTIVITI_LOG T ");
        sb.append("                                         LEFT JOIN T_EMPLOYEE_EXTEND E ON T .CREATEUSER = E .EMPLOYEE_JOBNO ");
        sb.append("                                         LEFT JOIN T_DEPARTMENT D ON T .CREATEDEPT = D .DEPT_CODE ");
        sb.append("                                         WHERE ");
        sb.append("                                         T .STOP_FLG = 0 ");
        sb.append("                                         AND T .DEL_FLG = 0 ");
        if(StringUtils.isNotBlank(startTime)){
            sb.append("                                     AND T.CREATETIME >=to_date('"+startTime+"','yyyy-mm-dd hh24:mi:ss') ");
        }
        if(StringUtils.isNotBlank(endTime)){
            sb.append("                                     AND T.CREATETIME <=to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss') ");
        }
        sb.append("                                         AND D .STOP_FLG = 0 ");
        sb.append("                                         AND D .DEL_FLG = 0 ");
        sb.append("                                         ORDER BY ");
        sb.append("                                         T .CREATETIME DESC ");
        sb.append("                                 ) f ");
        sb.append("                         WHERE ");
        sb.append("                         ROWNUM <= "+p*r+" ");
        sb.append("                 ) H ");
        sb.append("         WHERE ");
        sb.append(" H .r > "+(p-1)*r+" ");

        return namedParameterJdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper(TaskDelVo.class));
    }

    @Override
    public Integer findTaskDelVoCount(String startTime, String endTime) {

        StringBuffer sb = new StringBuffer();

        sb.append(" SELECT ");
        sb.append(" count(1) ");
        sb.append(" FROM ");
        sb.append(" T_OA_ACTIVITI_LOG T ");
        sb.append(" LEFT JOIN T_EMPLOYEE_EXTEND E ON T .CREATEUSER = E .EMPLOYEE_JOBNO ");
        sb.append(" LEFT JOIN T_DEPARTMENT D ON T .CREATEDEPT = D .DEPT_CODE ");
        sb.append(" WHERE ");
        sb.append(" T .STOP_FLG = 0 ");
        sb.append(" AND T .DEL_FLG = 0 ");
        if(StringUtils.isNotBlank(startTime)){
            sb.append("                                     AND T.CREATETIME >=to_date('"+startTime+"','yyyy-mm-dd hh24:mi:ss') ");
        }
        if(StringUtils.isNotBlank(endTime)){
            sb.append("                                     AND T.CREATETIME <=to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss') ");
        }
        sb.append(" AND D .STOP_FLG = 0 ");
        sb.append(" AND D .DEL_FLG = 0 ");
        sb.append(" ORDER BY ");
        sb.append(" T .CREATETIME DESC ");

        return namedParameterJdbcTemplate.queryForObject(sb.toString(),new HashMap(),Integer.class);
    }

    @Override
    public List<EmployeeExtendVo> findEmpList(String eName, String jobNO, String page, String rows) {

        if(eName==null){
            eName="";
        }
        if(jobNO==null){
            jobNO="";
        }
        int p=(page==null?1:Integer.parseInt(page));
        int r=(rows==null?20:Integer.parseInt(rows));

        StringBuffer sb = new StringBuffer();

        sb.append(" select m.* from ( ");
        sb.append(" select t.*,rownum as r from ( ");
        sb.append(" SELECT ");
        sb.append(" EMPLOYEE_JOBNO as jobNo, ");
        sb.append(" EMPLOYEE_NAME as name, ");
        sb.append(" DEPARTMENT as deptName ");
        sb.append("         FROM ");
        sb.append(" T_EMPLOYEE_EXTEND ");
        sb.append("         WHERE ");
        sb.append(" EMPLOYEE_JOBNO IS NOT NULL ");
        sb.append(" AND STOP_FLG = 0 ");
        sb.append(" AND DEL_FLG = 0 ");
        sb.append(" AND (EMPLOYEE_JOBNO LIKE '%"+jobNO+"%' OR EMPLOYEE_NAME LIKE '%"+eName+"%') ");
        sb.append(" ) t ");
        sb.append(" where rownum<="+p*r+" ");
        sb.append(" ) m where m.r>"+(p-1)*r+" ");

        return namedParameterJdbcTemplate.query(sb.toString(),new BeanPropertyRowMapper(EmployeeExtendVo.class));
    }

    @Override
    public Integer findEmpCount(String eName, String jobNO) {

        if(eName==null){
            eName="";
        }
        if(jobNO==null){
            jobNO="";
        }
        StringBuffer sb = new StringBuffer();

        sb.append(" SELECT ");
        sb.append(" count(1) ");
        sb.append("  FROM ");
        sb.append(" T_EMPLOYEE_EXTEND ");
        sb.append("         WHERE ");
        sb.append(" EMPLOYEE_JOBNO IS NOT NULL ");
        sb.append(" AND STOP_FLG = 0 ");
        sb.append(" AND DEL_FLG = 0 ");
        sb.append(" AND (EMPLOYEE_JOBNO LIKE '%"+jobNO+"%' OR EMPLOYEE_NAME LIKE '%"+eName+"%') ");

        return namedParameterJdbcTemplate.queryForObject(sb.toString(),new HashMap(),Integer.class);
    }

    @Override
    public List<DepartMentVo> findDeptList(String dName, String deptCode, String page, String rows) {

        if(dName==null){
            dName="";
        }
        if(deptCode==null){
            deptCode="";
        }
        int p=(page==null?1:Integer.parseInt(page));
        int r=(rows==null?20:Integer.parseInt(rows));

        StringBuffer sb = new StringBuffer();
        sb.append(" select m.* from ( ");
        sb.append(" select t.*,rownum as r from ( ");
        sb.append(" SELECT ");
        sb.append(" DEPT_CODE AS deptCode, ");
        sb.append(" DEPT_NAME AS deptName, ");
        sb.append(" DEPT_AREA_NAME AS areaName ");
        sb.append("         FROM ");
        sb.append(" T_DEPARTMENT ");
        sb.append("         WHERE ");
        sb.append(" DEL_FLG = 0 ");
        sb.append(" AND STOP_FLG = 0 ");
        sb.append(" AND (DEPT_NAME LIKE '%"+dName+"%' OR DEPT_CODE  LIKE '%"+deptCode+"%') ");
        sb.append(" ) t ");
        sb.append(" where rownum<="+p*r+" ");
        sb.append(" ) m where m.r>"+(p-1)*r+" ");

        return namedParameterJdbcTemplate.query(sb.toString(),new BeanPropertyRowMapper(DepartMentVo.class));
    }

    @Override
    public Integer findDeptCount(String dName, String deptCode) {
        if(dName==null){
            dName="";
        }
        if(deptCode==null){
            deptCode="";
        }
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT ");
        sb.append(" count(1) ");
        sb.append("   FROM ");
        sb.append(" T_DEPARTMENT ");
        sb.append("    WHERE ");
        sb.append(" DEL_FLG = 0 ");
        sb.append(" AND STOP_FLG = 0 ");
        sb.append(" AND (DEPT_NAME LIKE '%"+dName+"%' OR DEPT_CODE  LIKE '%"+deptCode+"%') ");

        return namedParameterJdbcTemplate.queryForObject(sb.toString(),new HashMap(),Integer.class);
    }

    @Override
    public List<ProcessTopicVo> findProcessTopicList(String pName, String page, String rows) {

        if(pName==null){
            pName="";
        }
        int p=(page==null?1:Integer.parseInt(page));
        int r=(rows==null?20:Integer.parseInt(rows));

        StringBuffer sb = new StringBuffer();

        sb.append(" select m.* from ( ");
        sb.append(" select t.*,rownum as r from ( ");
        sb.append(" SELECT ");
        sb.append(" ID AS ID, ");
        sb.append(" NAME AS processName, ");
        sb.append(" DESCN AS remark ");
        sb.append(" FROM ");
        sb.append(" T_OA_BPM_PROCESS ");
        sb.append(" WHERE ");
        sb.append(" DEL_FLG = 0 ");
        sb.append(" AND STOP_FLG = 0 ");
        sb.append(" AND NAME LIKE '%"+pName+"%' ");
        sb.append(" ) t ");
        sb.append(" where rownum<="+p*r+" ");
        sb.append(" ) m where m.r>"+(p-1)*r+" ");

        return namedParameterJdbcTemplate.query(sb.toString(),new BeanPropertyRowMapper(ProcessTopicVo.class));
    }

    @Override
    public Integer findProcessTopicCount(String pName) {
        if(pName==null){
            pName="";
        }
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT ");
        sb.append(" count(1) ");
        sb.append(" FROM ");
        sb.append(" T_OA_BPM_PROCESS ");
        sb.append(" WHERE ");
        sb.append(" DEL_FLG = 0 ");
        sb.append(" AND STOP_FLG = 0 ");
        sb.append(" AND NAME LIKE '%"+pName+"%' ");


        return namedParameterJdbcTemplate.queryForObject(sb.toString(),new HashMap(),Integer.class);
    }

    //更新此表
    @Override
    public Integer updateTaskInfo(String flag, String condition) {
        //这里主要说明flag=1的情况
        //根据condition（此时condition为T_OA_BPM_PROCESS的id）查询T_OA_KV_RECORD的id，并更新该表
        //得到T_OA_KV_RECORD的id更新T_OA_KV_PROP表中外键为T_OA_KV_RECORD的id的数据
        //更新T_OA_TASK_INFO中BUSINESS_KEY为T_OA_KV_RECORD的id的数据
        StringBuffer sb = new StringBuffer();

        Map<String, List<String>> map = new HashMap<>();
        sb.append(" UPDATE T_OA_TASK_INFO ");
        sb.append(" SET DEL_FLG = 1, ");
        sb.append(" STOP_FLG = 1 ");
        sb.append(" WHERE ");
        sb.append(" DEL_FLG = 0 ");
        sb.append(" AND STOP_FLG = 0 ");
        String key=null;

        if("2".equals(flag)){
            sb.append(" and  CREATEUSER IN (:users)");
            key="users";
        }
        if("3".equals(flag)){
            sb.append(" and CREATEDEPT IN (:depts)");
            key="depts";
        }

        List<String> list =new ArrayList<String>();
        if("1".equals(flag)){
            list=updateKVRecord(condition,flag,null);
        }else{
            String[] conditions = condition.split(",");
            list= Arrays.asList(conditions);
            //更新kv记录表--查询kr-r表的id
            StringBuffer sb1 = new StringBuffer();
            List<String> keys = new ArrayList<String>();
            Map<String, Object> map1 = new HashMap<>();
            sb1.append(" SELECT ");
            sb1.append("    DISTINCT BUSINESS_KEY ");
            sb1.append(" FROM ");
            sb1.append("         T_OA_TASK_INFO ");
            sb1.append(" WHERE ");
            sb1.append("         DEL_FLG = 0 ");
            sb1.append(" AND STOP_FLG = 0 ");
            if("2".equals(flag)){
                sb1.append(" AND CREATEUSER IN (:us) ");
                map1.put("us",list);
            }
            if("3".equals(flag)){
                sb1.append(" AND CREATEDEPT IN (:dps)");
                map1.put("dps",list);
            }
            keys = namedParameterJdbcTemplate.queryForList(sb1.toString(), map1, String.class);

            //根据id更新kv表
            updateKVRecord(null,flag,keys);
        }

        if(list==null||list.size()==0){
            return 0;
        }
        map.put(key,list);
        //删除催办信息T_OA_REMINDERS
        StringBuffer sb2 = new StringBuffer();
        sb2.append(" SELECT PROCESS_INSTANCE_ID AS processId FROM T_OA_TASK_INFO ");
        sb2.append(" WHERE ");
        sb2.append(" DEL_FLG = 0 ");
        sb2.append(" AND STOP_FLG = 0 ");
        if("1".equals(flag)){
            //sb2.append(" and BUSINESS_KEY IN (:bids) ");
            List<List<String>> lists = this.columnOption(list);
            Map<String, Object> inSql = this.getInSql("BUSINESS_KEY", lists);
            sb2.append(inSql.get("sql"));
            map= (Map<String, List<String>>) inSql.get("map");
        }
        if("2".equals(flag)){
            sb2.append(" and  CREATEUSER IN (:users)");
        }
        if("3".equals(flag)){
            sb2.append(" and CREATEDEPT IN (:depts)");
        }

        List<String> processIds = namedParameterJdbcTemplate.queryForList(sb2.toString(), map, String.class);
        //根据运行中的流程实例id,更新催办表
        updateReMind(processIds);

        if("1".equals(flag)){
            //sb.append(" and BUSINESS_KEY IN (:bids) ");
            //key="bids";
            List<List<String>> lists = this.columnOption(list);
            Map<String, Object> inSql = this.getInSql("BUSINESS_KEY", lists);
            sb.append(inSql.get("sql"));
            map= (Map<String, List<String>>) inSql.get("map");
        }

        //更新任务表
        return namedParameterJdbcTemplate.update(sb.toString(),map);
    }

    //更新此表
    public List<String> updateKVRecord(String condition,String flag,List<String> keys){

        List<String> rs =null;
        if("1".equals(flag)) {
            String[] conditions = condition.split(",");
            List<String> list= Arrays.asList(conditions);
            if(list==null||list.size()==0){
                return new ArrayList<String>();
            }

            //查询id
            Map<String, Object> map1 = new HashMap<>();

            StringBuffer sb1 = new StringBuffer();
            sb1.append(" SELECT id FROM ");
            sb1.append(" T_OA_KV_RECORD ");
            sb1.append(" WHERE ");
            sb1.append(" DEL_FLG = 0 ");
            sb1.append(" AND STOP_FLG = 0 ");
            sb1.append(" AND CATEGORY IN (:pids) ");

            map1.put("pids",list);
            rs = namedParameterJdbcTemplate.queryForList(sb1.toString(),map1, String.class);
        }else{
            rs=keys;
        }
        if(rs==null||rs.size()==0){
            return new ArrayList<String>();
        }

        //更新kv-r
        StringBuffer sb2 = new StringBuffer();
        sb2.append(" UPDATE T_OA_KV_RECORD ");
        sb2.append(" SET DEL_FLG = 1, ");
        sb2.append(" STOP_FLG = 1 ");
        sb2.append(" WHERE ");
        sb2.append(" 1=1 ");
        //sb2.append(" id IN (:ids) ");

        List<List<String>> lists = this.columnOption(rs);
        Map<String, Object> map = this.getInSql("ID", lists);
        sb2.append(map.get("sql"));

        namedParameterJdbcTemplate.update(sb2.toString(),(Map<String, List<String>>) map.get("map"));

        //更新kv-p表
        updateKVProp(rs);

        return rs;
    }

    //更新此表
    public void updateKVProp(List<String> rids){

        if(rids!=null&&rids.size()>0) {
            StringBuffer sb = new StringBuffer();
            sb.append(" UPDATE T_OA_KV_PROP ");
            sb.append(" SET DEL_FLG = 1, ");
            sb.append(" STOP_FLG = 1 ");
            sb.append(" WHERE ");
            sb.append(" 1=1 ");

            //sb.append(" RECORD_ID IN (:rids) ");
            List<List<String>> lists = this.columnOption(rids);
            Map<String, Object> map = this.getInSql("RECORD_ID", lists);
            sb.append(map.get("sql"));

            namedParameterJdbcTemplate.update(sb.toString(), (Map<String, List<String>>) map.get("map"));
        }
    }




    //根据流程实例id，删除催办信息T_OA_REMINDERS
    public void updateReMind(List<String> processIds){

        if(processIds!=null&&processIds.size()>0){
            //update T_OA_REMINDERS set DEL_FLG=1 where PROCEDUREID IN(:processIds)
            String sql=" update T_OA_REMINDERS set DEL_FLG=1 where 1=1 ";
            //processIds
            List<List<String>> lists = this.columnOption(processIds);
            Map<String, Object> map = this.getInSql("PROCEDUREID", lists);
            sql+=map.get("sql");

            namedParameterJdbcTemplate.update(sql,(Map<String, List<String>>) map.get("map"));
        }

    }

    //对于：数据库列表中的最大表达式数为 1000 错误处理方法columnOption
    public  List<List<String>> columnOption(List<String> list){

        List<List<String>> rsList = new ArrayList<>();
        List<String> ops = new ArrayList<>();
        //每1000个为一批
        for(int i=0;i<list.size();i++){

            if(i%1000==0&&i!=0){
                rsList.add(ops);
                ops=new ArrayList<>();
                ops.add(list.get(i));
                if(i==list.size()-1){//如果是最后一个
                    rsList.add(ops);
                }
            }else{
                ops.add(list.get(i));
                if(i==list.size()-1){//如果是最后一个
                    rsList.add(ops);
                }
            }

        }

        return rsList;
    }


    //对于：数据库列表中的最大表达式数为 1000 错误处理后，返回条件句
    public Map<String,Object> getInSql(String field,List<List<String>> lists){

        String sql =" AND ";
        Map<String, List<String>> map = new HashMap<>();
        for(int i=0;i<lists.size();i++){
            if(i==lists.size()-1){
                sql+=" "+field+" IN(:id"+i+") ";
                map.put("id"+i,lists.get(i));
            }else{
                sql+=" "+field+" IN(:id"+i+") or ";
                map.put("id"+i,lists.get(i));
            }
        }

        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("sql",sql);
        hashMap.put("map",map);


        return hashMap;
    }



    /**
     * @desc:添加工作流删除日志
     * @author:  zhangkui
     * @create:  2018/1/4 20:58
     * @version: V1.0
     * @param vo
     * @throws:
     */
    public void insertLog(TaskDelVo vo) {
        if(vo.getCreateDept()==null){
            vo.setCreateDept("");
        }

        StringBuffer sb = new StringBuffer();

        sb.append("   INSERT INTO T_OA_ACTIVITI_LOG ( ");
        sb.append("           ID, ");
        sb.append("           TYPE, ");
        sb.append("           CODE, ");
        sb.append("           NUM, ");
        sb.append("           CREATEUSER, ");
        sb.append("           CREATEDEPT, ");
        sb.append("           CREATETIME, ");
        sb.append("           UPDATEUSER, ");
        sb.append("           UPDATETIME, ");
        sb.append("           STOP_FLG, ");
        sb.append("           DEL_FLG, ");
        sb.append("           T_VALUE ");
        sb.append("   ) ");
        sb.append("   VALUES ");
        sb.append("           ( ");
        sb.append("                   '"+vo.getId()+"', ");
        sb.append("                   '"+vo.getType()+"', ");
        sb.append("                   '"+vo.getCode()+"', ");
        sb.append("                   '"+vo.getNum()+"', ");
        sb.append("                   '"+vo.getCreateUser()+"', ");
        sb.append("                   '"+vo.getCreateDept()+"',");
        sb.append("                   to_date('"+vo.getCreateTime()+"','yyyy-mm-dd hh24:mi:ss'), ");
        sb.append("                   '"+vo.getUpdateUser()+"', ");
        sb.append("                   to_date('"+vo.getUpdateTime()+"','yyyy-mm-dd hh24:mi:ss'), ");
        sb.append("                   "+vo.getStop_flg()+", ");
        sb.append("                   "+vo.getDel_flg()+", ");
        sb.append("                   '"+vo.getT_value()+"' ");
        sb.append("           ) ");

        namedParameterJdbcTemplate.update(sb.toString(),new HashMap());

    }


}
