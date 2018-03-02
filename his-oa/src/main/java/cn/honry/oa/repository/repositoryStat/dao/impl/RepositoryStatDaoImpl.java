package cn.honry.oa.repository.repositoryStat.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.oa.repository.repositoryStat.dao.RepositoryStatDao;
import cn.honry.oa.repository.repositoryStat.vo.RepositoryStatVo;

@Repository("repositoryStatDao")
@SuppressWarnings({ "all" })
public class RepositoryStatDaoImpl extends HibernateEntityDao<RepositoryStatVo> implements RepositoryStatDao{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	//基础工具类,不支持参数名传参
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	/**  
	 * 
	 * 知识库统计  科室
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月18日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月18日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 *
	 */
	@Override
	public List<RepositoryStatVo> queryRepositoryStatKS(String deptCode,String menuAlias,String page,String rows) {
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT  ROWNUM AS n, deptName,total FROM( ");
		sql.append("select r.DISEASE_NAME as deptName, count(t.categ_code) as total "
				+ " from t_repository_info t "
				+ "join T_REPOSITORY_CATEG r on t.categ_code=r.code "); 
		if(StringUtils.isNotBlank(deptCode)){
			sql.append(" Where r.DISEASE_NAME like '%"+deptCode+"%' ");
		}
		sql.append(" group by r.DISEASE_NAME )  Where Rownum <= "+rows);
		Map<String, Object> paraMap = new HashMap<String, Object>();
		List<RepositoryStatVo> RepositoryStatList =  namedParameterJdbcTemplate.query(sql.toString(),paraMap,new RowMapper<RepositoryStatVo>() {
			@Override
			public RepositoryStatVo mapRow(ResultSet rs, int rowNum)throws SQLException {
				RepositoryStatVo vo = new RepositoryStatVo();
				vo.setDeptName(rs.getString("deptName")); 
				vo.setTotal (rs.getInt("total")); 
				return vo;
			}
		});
		if(RepositoryStatList!=null&&RepositoryStatList.size()>0){
			return RepositoryStatList;
		}
		return new ArrayList<RepositoryStatVo>();
}

	@Override
	public int getTotalKS(String deptCode, String menuAlias) {
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT  count(1) total FROM( ");
		sql.append("select r.DISEASE_NAME as deptName, count(t.categ_code) as total "
				+ " from t_repository_info t "
				+ "join T_REPOSITORY_CATEG r on t.categ_code=r.code where r.DISEASE_NAME like '%"+deptCode+"%' group by r.DISEASE_NAME ");
		sql.append(" )");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		List<RepositoryStatVo> CriticallyIllPatientVoList =  namedParameterJdbcTemplate.query(sql.toString(),paraMap,new RowMapper<RepositoryStatVo>() {
			@Override
			public RepositoryStatVo mapRow(ResultSet rs, int rowNum)throws SQLException {
				RepositoryStatVo vo = new RepositoryStatVo();
				return vo;
			}
		});
		if(CriticallyIllPatientVoList!=null&&CriticallyIllPatientVoList.size()>0){
			return CriticallyIllPatientVoList.size();
		}
		return 0;
	}

	
	/**  
	 * 
	 * 知识库统计  作者
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月18日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月18日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 *
	 */
	@Override
	public List<RepositoryStatVo> queryRepositoryStatZZ(String deptCode,String menuAlias,String page,String rows) {
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT  ROWNUM AS n, createUser,totalzz FROM( ");
		sql.append("select e.employee_name as createUser, count(t.categ_code) as totalzz  from t_repository_info t Left Join t_employee e On t.createuser = e.employee_jobno "); 
		if(StringUtils.isNoneBlank(deptCode)){
			sql.append(" where e.employee_name like '%"+deptCode+"%' ");
		}
		sql.append(" group by e.employee_name ) Where Rownum <= "+rows+" order by totalzz desc ");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		List<RepositoryStatVo> DeathPatientVoList =  namedParameterJdbcTemplate.query(sql.toString(),paraMap,new RowMapper<RepositoryStatVo>() {
			@Override
			public RepositoryStatVo mapRow(ResultSet rs, int rowNum)throws SQLException {
				RepositoryStatVo vo = new RepositoryStatVo();
				vo.setCreateUser(rs.getString("createUser")); 
				vo.setTotalzz (rs.getInt("totalzz")); 
				return vo;
			}
		});
		if(DeathPatientVoList!=null&&DeathPatientVoList.size()>0){
			return DeathPatientVoList;
		}
		return new ArrayList<RepositoryStatVo>();
	}

	@Override
	public int getTotalZZ(String deptCode, String menuAlias) {
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT  count(1) total FROM( ");
		sql.append("select t.createuser as createUser, count(t.categ_code) as totalzz  from t_repository_info t  group by t.createuser ) ");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		List<RepositoryStatVo> RepositoryStatList =  namedParameterJdbcTemplate.query(sql.toString(),paraMap,new RowMapper<RepositoryStatVo>() {
			@Override
			public RepositoryStatVo mapRow(ResultSet rs, int rowNum)throws SQLException {
				RepositoryStatVo vo = new RepositoryStatVo();
				return vo;
			}
		});
		if(RepositoryStatList!=null&&RepositoryStatList.size()>0){
			return RepositoryStatList.size();
		}
		return 0;
	}

	/**  
	 * 
	 * 知识库统计  阅读量
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月20日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月20日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 *
	 */
	@Override
	public List<RepositoryStatVo> queryRepositoryStatYDL(String deptCode,String menuAlias, String page, String rows) {
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT  ROWNUM AS n, name,categName,views,createUser,createTime  FROM( ");
		sql.append("select  t.name as name,t.categ_name as categName,t.views as views,e.employee_name as createUser,t.createtime as createTime  "
				+ "from t_repository_info t Left Join t_employee e On t.createuser = e.employee_jobno where t.del_flg=0 and t.stop_flg=0 and t.Isovert = 1 and rownum <= "+rows);
		if(StringUtils.isNotBlank(deptCode)){
			sql.append(" and t.name like '%"+deptCode+"%' ");
		}
		sql.append("order by t.views  desc  )"); 
		Map<String, Object> paraMap = new HashMap<String, Object>();
		List<RepositoryStatVo> DeathPatientVoList =  namedParameterJdbcTemplate.query(sql.toString(),paraMap,new RowMapper<RepositoryStatVo>() {
			@Override
			public RepositoryStatVo mapRow(ResultSet rs, int rowNum)throws SQLException {
				RepositoryStatVo vo = new RepositoryStatVo();
				vo.setName(rs.getString("name")); 
				vo.setCategName(rs.getString("categName")); 
				vo.setViews(rs.getString("views")); 
				vo.setCreateUser(rs.getString("createUser")); 
				vo.setCreateTime(rs.getString("createTime")); 
				return vo;
			}
		});
		if(DeathPatientVoList!=null&&DeathPatientVoList.size()>0){
			return DeathPatientVoList;
		}
		return new ArrayList<RepositoryStatVo>();
	}

	@Override
	public int getTotalYDL(String deptCode, String menuAlias) {
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT  count(1) total FROM( ");
		sql.append("select  t.name as name,t.categ_name as categName,t.views as views,t.createuser as createUser,t.createtime as createTime  from t_repository_info t   where t.del_flg=0 and t.stop_flg=0  order by t.views  desc ) ");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		List<RepositoryStatVo> RepositoryStatList =  namedParameterJdbcTemplate.query(sql.toString(),paraMap,new RowMapper<RepositoryStatVo>() {
			@Override
			public RepositoryStatVo mapRow(ResultSet rs, int rowNum)throws SQLException {
				RepositoryStatVo vo = new RepositoryStatVo();
				return vo;
			}
		});
		if(RepositoryStatList!=null&&RepositoryStatList.size()>0){
			return RepositoryStatList.size();
		}
		return 0;
	}


}
