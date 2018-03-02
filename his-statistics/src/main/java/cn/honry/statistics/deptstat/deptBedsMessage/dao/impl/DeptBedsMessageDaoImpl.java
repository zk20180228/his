package cn.honry.statistics.deptstat.deptBedsMessage.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.userMenuDataJuris.dao.DataJurisInInterDAO;
import cn.honry.statistics.deptstat.deptBedsMessage.dao.DeptBedsMessageDao;
import cn.honry.statistics.deptstat.deptBedsMessage.vo.DeptBedsMessageVo;
import cn.honry.utils.ShiroSessionUtils;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@Repository("deptBedsMessageDao")
@SuppressWarnings({ "all" })
public class DeptBedsMessageDaoImpl extends HibernateEntityDao<DeptBedsMessageVo> implements DeptBedsMessageDao{
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
	
	@Autowired
	@Qualifier(value = "dataJurisInInterDAO")
	private DataJurisInInterDAO dataJurisInInterDAO;
	/**  
	 * 
	 * 科室床位信息查询
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月6日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月6日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 *
	 */
	@Override
	public List<DeptBedsMessageVo> queryDeptBedsMessage(String deptCode,String page,String rows,String menuAlias) {
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT * from (");
		sql.append("SELECT  ROWNUM AS n, bedName,bedWardName,bedNum,deptName,bedLevel,bedState  FROM( ");
		sql.append("select b.bed_name as bedName, ward.bedward_name as bedWardName, substr(b.bed_name, 5) as bedNum, (select t.dept_name from t_department_contact t where t.pardept_id=t.deptId and rownum<=1) as deptName,"
		+ "(select dd.code_name from t_business_dictionary dd where dd.code_type = 'bedGrade' and dd.code_encode = b.bed_level) as bedLevel,"
		+ "(select dd.code_name from t_business_dictionary dd where dd.code_type = 'bedtype' and dd.code_encode = b.bed_state) as bedState "
		+ "from t_business_hospitalbed b, ");
		if(StringUtils.isNotBlank(deptCode)){
			String [] dept = deptCode.split(",");
			String code = "";
			for (int i = 0; i < dept.length; i++) {
				if(StringUtils.isNotBlank(code)){
					code += "','"+dept[i];
				}else{
					code = dept[i];
				}
			}
			sql.append(" (select t.dept_code,t.dept_name,t.id deptId from t_department_contact t "
			+ "where t.reference_type='03' and t.id in (select t.pardept_id from t_department_contact t "
			+ "where t.reference_type='03' and t.dept_code in ('"+code+"'))) t,");
		}else{
			String sql1="";
			sql1 = dataJurisInInterDAO.getJurisDeptSql(menuAlias,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			Map<String, Object> paraMap1 = new HashMap<String, Object>();
			List<DeptBedsMessageVo> DeptList =  namedParameterJdbcTemplate.query(sql1.toString(),paraMap1,new RowMapper<DeptBedsMessageVo>() {
				@Override
				public DeptBedsMessageVo mapRow(ResultSet rs, int rowNum)throws SQLException {
					DeptBedsMessageVo vo = new DeptBedsMessageVo();
					vo.setBedName(rs.getString("DEPT_CODE")); 
					return vo;
				}
			});
			if(DeptList.size()==0){
				return new ArrayList<DeptBedsMessageVo>();
			}
			for (int i = 0; i < DeptList.size(); i++) {
				if(StringUtils.isNotBlank(deptCode)){
					deptCode += "','"+DeptList.get(i).getDEPT_CODE();
				}else{
					deptCode = DeptList.get(i).getDEPT_CODE();
				}
			}
			
			sql.append(" (select t.dept_code,t.dept_name from t_department_contact t "
			+ "where t.reference_type='03' and t.id in (select t.pardept_id from t_department_contact t "
			+ "where t.reference_type='03' and t.dept_code in ('"+deptCode+"'))) t,");
		}
		sql.append(" t_business_bedward ward "
		+ "where b.nurse_cell_code = t.dept_code and ward.bedward_id=b.bedward_id "
		+ "order by "
		+ "length(decode(substr(b.bed_name, 5, 1),'+',substr(b.bed_name, 1, 4) || '1000' ||substr(b.bed_name, 6, length(b.bed_name) - 5),b.bed_name)), "
		+ "decode(substr(b.bed_name, 5, 1),'+',substr(b.bed_name, 1, 4) || '1000' ||substr(b.bed_name, 6, length(b.bed_name) - 5),b.bed_name)"); 
		sql.append(") where rownum <= :page * :rows )   where n > (:page - 1) * :rows");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		int start = Integer.parseInt(page == null ? "1" : page);
		int count = Integer.parseInt(rows == null ? "20" : rows);
		paraMap.put("page", start);
		paraMap.put("rows", count);
		List<DeptBedsMessageVo> DeptBedsMessageVoList =  namedParameterJdbcTemplate.query(sql.toString(),paraMap,new RowMapper<DeptBedsMessageVo>() {
			@Override
			public DeptBedsMessageVo mapRow(ResultSet rs, int rowNum)throws SQLException {
				DeptBedsMessageVo vo = new DeptBedsMessageVo();
				vo.setBedName(rs.getString("bedName")); 
				vo.setBedWardName(rs.getString("bedWardName")); 
				vo.setBedNum(rs.getString("bedNum")); 
				vo.setDeptName(rs.getString("deptName"));
				vo.setBedLevel(rs.getString("bedLevel")); 
				vo.setBedState(rs.getString("bedState")); 
				return vo;
			}
		});
		if(DeptBedsMessageVoList!=null&&DeptBedsMessageVoList.size()>0){
			return DeptBedsMessageVoList;
		}
		return new ArrayList<DeptBedsMessageVo>();
	}
	
	/**  
	 * 
	 * 科室床位信息查询总条数
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月6日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月6日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 *
	 */
	@Override
	public int getTotalDeptBedsMessage(String deptCode,String menuAlias) {
		StringBuffer sql=new StringBuffer();
		sql.append("select b.bed_name as bedName from t_business_hospitalbed b, ");
				if(StringUtils.isNotBlank(deptCode)){
					String [] dept = deptCode.split(",");
					String code = "";
					for (int i = 0; i < dept.length; i++) {
						if(StringUtils.isNotBlank(code)){
							code += "','"+dept[i];
						}else{
							code = dept[i];
						}
					}
					sql.append(" (select t.dept_code,t.dept_name from t_department_contact t "
				+ "where t.reference_type='03' and t.id in (select t.pardept_id from t_department_contact t "
				+ "where t.reference_type='03' and t.dept_code in ('"+code+"'))) t,");
				}else{
					String sql1="";
					sql1 = dataJurisInInterDAO.getJurisDeptSql(menuAlias,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
					Map<String, Object> paraMap1 = new HashMap<String, Object>();
					List<DeptBedsMessageVo> DeptList =  namedParameterJdbcTemplate.query(sql1.toString(),paraMap1,new RowMapper<DeptBedsMessageVo>() {
						@Override
						public DeptBedsMessageVo mapRow(ResultSet rs, int rowNum)throws SQLException {
							DeptBedsMessageVo vo = new DeptBedsMessageVo();
							vo.setBedName(rs.getString("DEPT_CODE")); 
							return vo;
						}
					});
					for (int i = 0; i < DeptList.size(); i++) {
						if(StringUtils.isNotBlank(deptCode)){
							deptCode += "','"+DeptList.get(i).getDEPT_CODE();
						}else{
							deptCode = DeptList.get(i).getDEPT_CODE();
						}
					}
					
					sql.append(" (select t.dept_code,t.dept_name from t_department_contact t "
					+ "where t.reference_type='03' and t.id in (select t.pardept_id from t_department_contact t "
					+ "where t.reference_type='03' and t.dept_code in ('"+deptCode+"'))) t,");
				}
				sql.append(" t_business_bedward ward "
				+ "where b.nurse_cell_code = t.dept_code and ward.bedward_id=b.bedward_id ");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		List<DeptBedsMessageVo> DeptBedsMessageVoList =  namedParameterJdbcTemplate.query(sql.toString(),paraMap,new RowMapper<DeptBedsMessageVo>() {
			@Override
			public DeptBedsMessageVo mapRow(ResultSet rs, int rowNum)throws SQLException {
				DeptBedsMessageVo vo = new DeptBedsMessageVo();
				vo.setBedName(rs.getString("bedName")); 
				return vo;
			}
		});
		if(DeptBedsMessageVoList!=null&&DeptBedsMessageVoList.size()>0){
			return DeptBedsMessageVoList.size();
		}
		return 0;
	}

	@Override
	public List<DeptBedsMessageVo> queryDeptBedsMessageForDB(String deptCode) {
		
		BasicDBObject bdObject = new BasicDBObject();
		List<DeptBedsMessageVo> list=new ArrayList<DeptBedsMessageVo>();
		if(StringUtils.isNotBlank(deptCode)){
			BasicDBList deptList=new BasicDBList();
			String[] deptArr=deptCode.split(",");
			for(int i=0,len=deptArr.length;i<len;i++){
				deptList.add(new BasicDBObject("deptCode", deptArr[i]));
			}
			bdObject.append("$or", deptList);
		}
		DBCursor cursor = new MongoBasicDao().findAlldata("WZBLRSBLTJFX", bdObject);
		DBObject dbCursor;
		while(cursor.hasNext()){
			DeptBedsMessageVo vo=new  DeptBedsMessageVo();
			 dbCursor = cursor.next();
			 vo.setDeptCode((String)dbCursor.get("deptCode"));
			 vo.setDeptName((String)dbCursor.get("deptName"));
			list.add(vo);
			}
		return list;
	}

}
