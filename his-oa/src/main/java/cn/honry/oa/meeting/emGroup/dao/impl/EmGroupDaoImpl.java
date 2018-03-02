package cn.honry.oa.meeting.emGroup.dao.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.User;
import cn.honry.oa.meeting.emGroup.dao.EmGroupDao;
import cn.honry.oa.meeting.emGroup.vo.EmGroupVo;
import cn.honry.oa.meeting.emGroup.vo.GroupVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.SessionUtils;

@Repository("emGroupDao")
public class EmGroupDaoImpl implements EmGroupDao{

	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Override
	public List<GroupVo> loadGroup(String text) {
		
		StringBuffer sb = new StringBuffer();
		sb .append("SELECT CODE_ENCODE AS ID,CODE_NAME AS TEXT FROM T_BUSINESS_DICTIONARY WHERE CODE_TYPE='ybGroup'");
		if(StringUtils.isNoneBlank(text)){
			sb.append(" AND ( CODE_NAME LIKE UPPER('%"+text+"%') ");
			sb.append(" OR CODE_PINYIN LIKE UPPER('%"+text+"%') ");
			sb.append(" OR CODE_ENCODE LIKE UPPER('%"+text+"%') ");
			sb.append(" OR CODE_WB LIKE UPPER('%"+text+"%') )");
		}

		return  namedParameterJdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper(GroupVo.class));
	}



	@Override
	public List<EmGroupVo> groupList(String id, String employee_name,String employee_jobon, String dept_name, String page, String rows) {
		
		Integer p = (page==null?1:Integer.parseInt(page));
		Integer r = (rows==null?20:Integer.parseInt(rows));
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT T.* FROM ( ");
		sb.append(" SELECT ");
		sb.append(" ROWNUM AS IDS, ");
		sb.append(" ID, ");
		sb.append(" EMPLOYEE_NAME, ");
		sb.append(" EMPLOYEE_JOBON, ");
		sb.append(" DEPT_NAME, ");
		sb.append(" DECODE(DUTIES_NAME,'null','',DUTIES_NAME) as DUTIES_NAME, ");
		sb.append(" DECODE(TITLE_NAME,'null','',TITLE_NAME) as TITLE_NAME, ");
		sb.append(" EMPLOYEE_TYPE_NAME, ");
		sb.append(" CREATUESR, ");
		sb.append(" CREATTIME ");
		sb.append(" FROM ");
		sb.append(" T_OA_MEET_YBEMPREF ");
		sb.append(" WHERE ");
		sb.append(" ROWNUM<="+p*r);
		sb.append(" AND EMPLOYEE_GROUP='"+id+"' ");
		if(StringUtils.isNotBlank(employee_name)){
			sb.append(" AND  EMPLOYEE_NAME LIKE '%"+employee_name+"%' ");
		}
		if(StringUtils.isNotBlank(employee_jobon)){
			sb.append(" AND EMPLOYEE_JOBON LIKE '%"+employee_jobon+"%' ");
		}
		if(StringUtils.isNotBlank(dept_name)){
			sb.append(" AND DEPT_NAME LIKE '%"+dept_name+"%' ");
		}
		
		
		sb.append(" ) T ");
		sb.append(" WHERE T.IDS>"+(p-1)*r);
		
		return namedParameterJdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper(EmGroupVo.class));
	}

	@Override
	public int groupCount(String id, String employee_name,String employee_jobon, String dept_name) {
		
		StringBuffer sb = new StringBuffer();		
		sb.append(" SELECT ");
		sb.append(" COUNT(1) ");
		sb.append(" FROM ");
		sb.append(" T_OA_MEET_YBEMPREF ");
		sb.append(" WHERE ");
		sb.append(" EMPLOYEE_GROUP='"+id+"' ");
		if(StringUtils.isNotBlank(employee_name)){
			sb.append(" AND  EMPLOYEE_NAME LIKE '%"+employee_name+"%' ");
		}
		if(StringUtils.isNotBlank(employee_jobon)){
			sb.append(" AND EMPLOYEE_JOBON LIKE '%"+employee_jobon+"%' ");
		}
		if(StringUtils.isNotBlank(dept_name)){
			sb.append(" AND DEPT_NAME LIKE '%"+dept_name+"%' ");
		}
		
		return  namedParameterJdbcTemplate.queryForInt(sb.toString(), new HashMap());
	}

	@Override
	public void delEmployeeById(String id) {
		
		String[] ids = id.split(",");
		List<String> list = Arrays.asList(ids);
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = " DELETE FROM T_OA_MEET_YBEMPREF WHERE ID IN (:IDS) ";
		map.put("IDS", list);//参数区分大小写,结束类型为是集合
		
		namedParameterJdbcTemplate.update(sql, map);
	}



	@Override
	public void addEmp(String id, String text, String employee_jobon) {
		
		User user = (User)SessionUtils.getCurrentUserFromShiroSession();//获取登录人
		
		String[]  employee_jobons= employee_jobon.split(",");
		EmGroupVo vo = null;
		for(String employeeJobon:employee_jobons){
			//如果该成员已经被添加，则不再执行下面的select和insert语句
			String delSql="SELECT * FROM T_OA_MEET_YBEMPREF WHERE EMPLOYEE_JOBON='"+employeeJobon+"' AND EMPLOYEE_GROUP ='"+id+"'";
			List empList = namedParameterJdbcTemplate.query(delSql,new BeanPropertyRowMapper(EmGroupVo.class));
			if(empList!=null&&empList.size()>0){//如果该人员已经被添加
				continue;//跳过当前循环，进入下次循环
			}
			
			//拼接sql，查询每个员工的信息
			StringBuffer sb = new StringBuffer();
			sb.append(" SELECT ");
			sb.append(" t.EMPLOYEE_NAME as EMPLOYEE_NAME, ");
			sb.append(" t.EMPLOYEE_ID as EMPLOYEE_JOBON, ");
			sb.append(" t.DEPT_ID as DEPT_CODE, ");
			sb.append(" (select DEPT_NAME from T_DEPARTMENT where DEPT_ID=t.DEPT_ID) as DEPT_NAME, ");
			
			sb.append(" t.EMPLOYEE_POST as DUTIES_TYPE, ");
			sb.append(" (select CODE_NAME from T_BUSINESS_DICTIONARY where t.EMPLOYEE_POST =CODE_ENCODE and CODE_TYPE='duties' ) as DUTIES_NAME, ");
			sb.append(" t.EMPLOYEE_TITLE as TITLE_TYPE, ");
			sb.append(" (select CODE_NAME from T_BUSINESS_DICTIONARY where t.EMPLOYEE_TITLE=CODE_ENCODE and CODE_TYPE='title' ) as TITLE_NAME, ");
			sb.append(" (select CODE_NAME from T_BUSINESS_DICTIONARY where t.EMPLOYEE_TYPE=CODE_ENCODE and CODE_TYPE='empType' ) as employee_type_name, ");
			sb.append(" to_char(t.CREATETIME,'yyyy-mm-dd hh24:mi:ss') as CREATTIME, ");
			sb.append(" t.CREATEUSER as CREATUESR ");
			sb.append(" FROM ");
			
			sb.append(" T_EMPLOYEE t ");
			sb.append(" WHERE ");
			sb.append(" EMPLOYEE_JOBNO ='"+employeeJobon+"'");
			
			List<EmGroupVo> list = namedParameterJdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper(EmGroupVo.class));
			if(list!=null&&list.size()>0){
				vo=list.get(0);
				String mid = UUID.randomUUID().toString().replace("-", "");
				vo.setId(mid);//设置主键
				vo.setEmployee_group(id);//设置组编号
				vo.setEmployee_group_name(text);//设置组名
				//想会议组表中插入一条数据数据
				String insertSql="insert into T_OA_MEET_YBEMPREF(ID,EMPLOYEE_NAME,EMPLOYEE_JOBON,DEPT_CODE,DEPT_NAME,DUTIES_TYPE,DUTIES_NAME,TITLE_TYPE,TITLE_NAME,"
								 +"EMPLOYEE_TYPE_NAME,Employee_group,Employee_group_NAME,CREATTIME,CREATUESR"
							     +") values('"+vo.getId()+"','"+vo.getEmployee_name()+"','"+vo.getEmployee_jobon()
								 +"','"+vo.getDept_code()+"','"+vo.getDept_name()+"','"+vo.getDuties_type()+"','"+vo.getDuties_name()
								 +"','"+vo.getTitle_type()+"','"+vo.getTitle_name()+"','"+vo.getEmployee_type_name()+"','"+vo.getEmployee_group()
								 +"','"+vo.getEmployee_group_name()+"',sysdate,'"+user.getName()+"')";
				
				namedParameterJdbcTemplate.update(insertSql, new HashMap());
			}
		}
		
	}


	@Override
	public List<GroupVo> loadDept(String id) {
		
		StringBuffer sb  = new StringBuffer();
		sb.append("SELECT DEPT_NAME AS ID, DEPT_NAME AS TEXT FROM T_OA_MEET_YBEMPREF");
		if(StringUtils.isNotBlank(id)){
			sb.append(" WHERE EMPLOYEE_GROUP='"+id+"' ");
		}
		sb.append(" GROUP BY DEPT_NAME ");
		
		return namedParameterJdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper(GroupVo.class));
	}

}
