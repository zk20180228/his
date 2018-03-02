package cn.honry.statistics.sys.empWorkStats.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.base.bean.model.OutpatientFeedetail;
import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.system.userMenuDataJuris.dao.DataJurisInInterDAO;
import cn.honry.statistics.sys.empWorkStats.dao.EmpWorkStatsDao;
import cn.honry.statistics.sys.empWorkStats.vo.EmpWorkStatsVo;
import cn.honry.utils.ShiroSessionUtils;

@Repository("empWorkStatsDAO")
@SuppressWarnings({ "all" })
public class EmpWorkStatsDaoImpl extends HibernateEntityDao<RegisterInfo> implements EmpWorkStatsDao{
	
	// 为父类HibernateDaoSupport注入sessionFactory的值
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	//扩展工具类,支持参数名传参
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	
	@Autowired
	@Qualifier(value = "dataJurisInInterDAO")
	private DataJurisInInterDAO dataJurisInInterDAO;
	
	@Override
	public List<SysDepartment> deptCombobox() {
		
		String hql = "from SysDepartment where del_flg=0 and stop_flg=0 and deptIsforregister = 1";
		List<SysDepartment> departmentList = super.find(hql, null);
		if(departmentList==null||departmentList.size()<=0){
			return new ArrayList<SysDepartment>();
		}
		
		return departmentList;
	}

	@Override
	public List<SysEmployee> empCombobox(String ids) {
		
		String hql = "from SysEmployee where del_flg=0 and stop_flg=0 and type = '1' ";
		if(StringUtils.isNotBlank(ids)){
			hql += "and deptId in ('"+ids+"')";
		}
	
		List<SysEmployee> employeeList = super.find(hql, null);
		
		if(employeeList==null||employeeList.size()<=0){
			
			return new ArrayList<SysEmployee>();
		}
		
		return employeeList;
	}

	@Override
	public List<EmpWorkStatsVo> queryList(String beginTime, String endTime,String dept, String emp) {
		
		StringBuffer sb  = new StringBuffer();
		sb.append(" select a.empname as empName,a.deptcode as deptCode,a.deptname as deptName,a.empcode as empCode,count(*) as infoSum , ");
		sb.append(" count(case when a.seeflag=1 then -1 end) as seeSum ");
		sb.append(" from (select d.dept_code as deptCode, ");
		sb.append(" d.dept_name as deptName, ");
		sb.append(" e.employee_code as empCode, ");
		sb.append(" e.employee_name as empName,");
		sb.append(" i.register_seeflag as seeFlag ");
		sb.append(" from t_register_info i left join t_department d on i.register_dept = d.dept_id ");
		sb.append(" left join t_employee e on i.register_expert = e.employee_id where 1=1 ");
		
		
		if(StringUtils.isNotBlank(beginTime)){
			sb.append(" and to_char(i.register_date,'yyyy-MM-dd HH-mm-ss') >= '"+beginTime+"' ");
		}
		if(StringUtils.isNotBlank(endTime)){
			sb.append(" and to_char(i.register_date,'yyyy-MM-dd HH-mm-ss') <= '"+endTime+"' ");
		}
		if(StringUtils.isNotBlank(dept)){
			sb.append(" and i.register_dept = '"+dept+"' ");
		}
		if(StringUtils.isNotBlank(emp)){
			sb.append(" and i.register_expert = '"+emp+"' ");
		}
		sb.append(" ) a	group by deptcode,deptname,empcode,a.empname");
		SQLQuery queryObject = this.getSession().createSQLQuery(sb.toString()).addScalar("empName").addScalar("deptCode").addScalar("deptName").addScalar("empCode").addScalar("infoSum",Hibernate.INTEGER).addScalar("seeSum",Hibernate.INTEGER);
		List<EmpWorkStatsVo> empWorkStatsVoList = queryObject.setResultTransformer(Transformers.aliasToBean(EmpWorkStatsVo.class)).list();
		
		if(empWorkStatsVoList!=null&&empWorkStatsVoList.size()>0){
			
			return empWorkStatsVoList;
		  }
		
		return new ArrayList<EmpWorkStatsVo>();
	}
	
	public List<EmpWorkStatsVo> queryListNow(List<String> tnl,String beginTime, String endTime,String dept, String emp,String menuType,String rows,String page){
		
		StringBuffer sb = new StringBuffer(500);
		if(tnl!=null||tnl.size()>0){
			sb.append("Select * from ( ");
			sb.append(" SELECT rownum rn,dCode as deptCode,dName as deptName,cCode as empCode,cName as empName,SUM(zs) as infoSum,SUM(ynsee) as seeSum FROM ( ");
			for (int i = 0; i < tnl.size(); i++) {
				if(i!=0){
					sb.append(" UNION ALL ");
				}
				sb.append(" SELECT m.DEPT_CODE dCode,m.DEPT_NAME dName,m.DOCT_CODE cCode,");
				sb.append(" m.DOCT_NAME cName,COUNT(1) zs,sum(decode(m.YNSEE,1,1,0)) ynsee ");
				sb.append(" FROM ").append(tnl.get(i)).append(" m ").append(",T_DEPARTMENT t ");
				sb.append(" WHERE m.IN_STATE = 0 ");
				sb.append(" AND T.DEPT_ID=M.DEPT_CODE AND T.DEPT_TYPE='C' ");
				if(StringUtils.isNotBlank(beginTime)){
					sb.append(" AND m.REG_DATE>=TO_DATE(:beginTime, 'yyyy-MM-dd') ");
				}
				if(StringUtils.isNotBlank(endTime)){
					sb.append(" AND m.REG_DATE<TO_DATE(:endTime, 'yyyy-MM-dd') ");
				}
				if(StringUtils.isNotBlank(dept)&&!"all".equals(dept)){
					sb.append(" AND m.Dept_Code IN (:dept) ");
				}else{
					sb.append(" AND m.Dept_Code IN (").append(dataJurisInInterDAO.getJurisDeptSql(menuType,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount())).append(") ");
				}
				if(StringUtils.isNotBlank(emp)){
					sb.append(" AND m.Doct_Code in (:emp) ");
				}
				sb.append(" GROUP BY M.DEPT_CODE, M.DEPT_NAME, M.DOCT_CODE, M.DOCT_NAME ");
			}
			sb.append(" ) where rownum <"+rows+" * "+page+" GROUP BY dCode,dName,cCode,cName,rownum ORDER BY deptCode ) where rn>=").append(rows).append("*(").append(page+"-1)");
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("beginTime", beginTime);
		paramMap.put("endTime", endTime);
		if(StringUtils.isNotBlank(dept)){
			List<String> deptList = Arrays.asList(dept.split(","));
			paramMap.put("dept", deptList);
		}
		if(StringUtils.isNotBlank(emp)){
			List<String> empList = Arrays.asList(emp.split(","));
			paramMap.put("emp", empList);
		}
		List<EmpWorkStatsVo> list = namedParameterJdbcTemplate.query(sb.toString(), paramMap,new RowMapper<EmpWorkStatsVo>() {

			@Override
			public EmpWorkStatsVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				EmpWorkStatsVo vo = new EmpWorkStatsVo();
				vo.setDeptCode(rs.getString("deptCode"));
				vo.setDeptName(rs.getString("deptName"));
				vo.setEmpCode(rs.getString("empCode"));
				vo.setEmpName(rs.getString("empName"));
				vo.setInfoSum(rs.getInt("infoSum"));
				vo.setSeeSum(rs.getInt("seeSum"));
				return vo;
			}
		});
		
		return list;
	}

	@Override
	public int getPage(List<String> tnl, String beginTime, String endTime,
			String dept, String emp, String menuType) {
		StringBuffer sb = new StringBuffer(500);
		if(tnl!=null||tnl.size()>0){
			sb.append("Select count(1) total from ( ");
			sb.append(" SELECT dCode as deptCode,dName as deptName,cCode as empCode,cName as empName,SUM(zs) as infoSum,SUM(ynsee) as seeSum FROM ( ");
			for (int i = 0; i < tnl.size(); i++) {
				if(i!=0){
					sb.append(" UNION ALL ");
				}
				sb.append(" SELECT m.DEPT_CODE dCode,m.DEPT_NAME dName,m.DOCT_CODE cCode,");
				sb.append(" m.DOCT_NAME cName,COUNT(1) zs,m.YNSEE ynsee ");
				sb.append(" FROM ").append(tnl.get(i)).append(" m ").append(",T_DEPARTMENT t ");
				sb.append(" WHERE m.IN_STATE = 0 ");
				sb.append(" AND T.DEPT_ID=M.DEPT_CODE AND T.DEPT_TYPE='C' ");
				if(StringUtils.isNotBlank(beginTime)){
					sb.append(" AND m.REG_DATE>=TO_DATE(:beginTime, 'yyyy-MM-dd') ");
				}
				if(StringUtils.isNotBlank(endTime)){
					sb.append(" AND m.REG_DATE<TO_DATE(:endTime, 'yyyy-MM-dd') ");
				}
				if(StringUtils.isNotBlank(dept)&&!"all".equals(dept)){
					sb.append(" AND m.Dept_Code IN (:dept) ");
				}else if(StringUtils.isBlank(dept)){
				}else{
					sb.append(" AND m.Dept_Code IN (").append(dataJurisInInterDAO.getJurisDeptSql(menuType,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount())).append(") ");
				}
				if(StringUtils.isNotBlank(emp)){
					sb.append(" AND m.Doct_Code in (:emp) ");
				}
				sb.append(" GROUP BY M.DEPT_CODE, M.DEPT_NAME, M.DOCT_CODE, M.DOCT_NAME, M.YNSEE ");
			}
			sb.append(" )  GROUP BY dCode,dName,cCode,cName,rownum ORDER BY deptCode ) ");
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("beginTime", beginTime);
		paramMap.put("endTime", endTime);
		if(StringUtils.isNotBlank(dept)){
			List<String> deptList = Arrays.asList(dept.split(","));
			paramMap.put("dept", deptList);
		}
		if(StringUtils.isNotBlank(emp)){
			List<String> empList = Arrays.asList(emp.split(","));
			paramMap.put("emp", empList);
		}
		List<Integer> list = namedParameterJdbcTemplate.query(sb.toString(), paramMap,new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				Integer vo =rs.getInt("total");
				return vo;
			}
		});
		return list.get(0);
	}

}
