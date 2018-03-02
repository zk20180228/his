package cn.honry.statistics.deptstat.operaCondtion.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.inner.system.userMenuDataJuris.dao.DataJurisInInterDAO;
import cn.honry.statistics.deptstat.operaCondtion.dao.OperaCondDao;
import cn.honry.statistics.deptstat.operaCondtion.vo.OperaCondVo;
import cn.honry.utils.ShiroSessionUtils;

@Repository("operaCondDao")
public class OperaCondDaoImpl implements OperaCondDao {
	@Autowired
	@Qualifier(value="dataJurisInInterDAO")
	private DataJurisInInterDAO dataJurisInInterDAO;
	
	public void setDataJurisInInterDAO(DataJurisInInterDAO dataJurisInInterDAO) {
		this.dataJurisInInterDAO = dataJurisInInterDAO;
	}
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	@Override
	public List<OperaCondVo> queryOperaList(List<String> tnL,String begin, String end,
			String menuAlias, String depts,String page,String rows) throws Exception{
		if(tnL!=null&&tnL.size()>0){
			StringBuffer buffer=new StringBuffer();
			buffer.append("select * from (");
			buffer.append("select t.dept_code deptCode,t.name name,t.patient_no patientNo,");
			buffer.append("i.item_name itemName,t.realduation realDuation,t.exec_dept execDept,rownum rn ");
			buffer.append("from (");
			for(int i=0,len=tnL.size();i<len;i++){
				if(i>0){
					buffer.append(" union all ");
				}
				buffer.append("select dept_code,name,patient_no, round((ceil((OUT_DATE-operationdate) * 24 * 60))/60,1) as realduation,exec_dept,operation_id,clinic_code ");
				buffer.append("from "+tnL.get(i));
				buffer.append(" where APPLY_DATE >=to_date('").append(begin).append(" 00:00:00','yyyy-mm-dd HH24:mi:ss') ")
				.append(" and APPLY_DATE <=to_date('").append(end).append(" 23:59:59','yyyy-mm-dd HH24:mi:ss') ");
				if(StringUtils.isNotBlank(depts)){
					buffer.append("and dept_code in('").append(depts.replace(",", "','")).append("') ");
				}else{
					buffer.append("and dept_code in(").append(dataJurisInInterDAO.getJurisDeptSql(menuAlias,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount())+") ");
				}
			}
			buffer.append(" )t left join T_OPERATION_ITEM i on i.operation_id=t.operation_id and i.inpatient_no=t.clinic_code ");
			buffer.append("where rownum<=").append(page+"*"+rows);
			buffer.append(" ) L where L.rn>=(").append(page+"-1)*"+rows);
			Map<String,String> map=new HashMap<String,String>();
			List<OperaCondVo> list=namedParameterJdbcTemplate.query(buffer.toString(), map, new RowMapper<OperaCondVo>(){

				@Override
				public OperaCondVo mapRow(ResultSet rs, int arg1)
						throws SQLException {
					OperaCondVo vo=new OperaCondVo();
					vo.setDeptCode(rs.getString("deptCode"));
					vo.setName(rs.getString("name"));
					vo.setPatientNo(rs.getString("patientNo"));
					vo.setItemName(rs.getString("itemName"));
					vo.setExecDept(rs.getString("execDept"));
					vo.setRealDuation(rs.getDouble("realDuation"));
					return vo;
				}
				
			});
			if(list.size()>0){
				return list;
			}
		
		}
		return new ArrayList<OperaCondVo>();
	}
	@Override
	public int queryOperaTotal(List<String> tnL, String begin, String end,
			String menuAlias, String depts) throws Exception {
		if(tnL!=null&&tnL.size()>0){
			StringBuffer buffer=new StringBuffer();
			buffer.append("select count(1) as total ");
			buffer.append("from (");
			for(int i=0,len=tnL.size();i<len;i++){
				if(i>0){
					buffer.append(" union all ");
				}
				buffer.append("select operation_id,clinic_code ");
				buffer.append("from "+tnL.get(i));
				buffer.append(" where APPLY_DATE >=to_date('").append(begin).append(" 00:00:00','yyyy-mm-dd HH24:mi:ss') ")
				.append(" and APPLY_DATE <=to_date('").append(end).append(" 23:59:59','yyyy-mm-dd HH24:mi:ss') ");
				if(StringUtils.isNotBlank(depts)){
					buffer.append("and dept_code in('").append(depts.replace(",", "','")).append("') ");
				}else{
					buffer.append("and dept_code in(").append(dataJurisInInterDAO.getJurisDeptSql(menuAlias,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount())+") ");
				}
			}
			buffer.append(" )t left join T_OPERATION_ITEM i on i.operation_id=t.operation_id and i.inpatient_no=t.clinic_code ");
			Map<String,String> map=new HashMap<String,String>();
			Integer conut = namedParameterJdbcTemplate.queryForObject(buffer.toString(), map, Integer.class);
			return conut;
		
		
		}
		return 0;
	}

}
