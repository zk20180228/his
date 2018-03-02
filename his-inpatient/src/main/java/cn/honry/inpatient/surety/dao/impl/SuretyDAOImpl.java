package cn.honry.inpatient.surety.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InpatientSurety;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inpatient.surety.dao.SuretyDAO;

/**  
 *  
 * @className：SuretyDAOImpl
 * @Description： 担保金DAOImpl 
 * @Author：aizhonghua
 * @CreateDate：2016-2-24 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-2-24 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Repository("suretyDAO")
public class SuretyDAOImpl extends HibernateEntityDao<InpatientSurety> implements SuretyDAO{
	
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	//基础工具类,不支持参数名传参
	@Resource
	private JdbcTemplate jdbcTemplate;
	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/**
	 *
	 * @Description：查询担保金信息 - 分页查询 - 获得总条数
	 * @Author：aizhonghua
	 * @CreateDate：2016年4月12日 上午9:47:41 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0
	 * @param：inpatientNo住院号
	 * @return 总条数
	 */
	@Override
	public int getSuretyTotal(String inpatientNo) {
		String sql ="select count(1) from ( select t.ID as id,t.SURETY_COST as suretyCost, t.PAY_WAY as payWay,t.OPEN_ACCOUNTS as openAccounts, t.OPEN_BANK as openBank,"
				+ "t.SURETY_TYPE as suretyType, t.STATE as state,t.CREATETIME as createTime "
				+ "from T_INPATIENT_SURETY t where t.INPATIENT_NO =:inpatientNo and t.del_flg=0 and t.stop_flg=0 )";
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("inpatientNo", inpatientNo);
		int c =  namedParameterJdbcTemplate.queryForObject(sql,paraMap, java.lang.Integer.class);
		return c;
	}

	/**
	 *
	 * @Description：查询担保金信息 - 分页查询 - 获得信息
	 * @Author：aizhonghua
	 * @CreateDate：2016年4月12日 上午9:47:41 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0
	 * @param1：inpatientNo住院号
	 * @param2：page当前页数
	 * @param3：rows分页数量
	 * @return：分页信息
	 * 
	 */
	@Override
	public List<InpatientSurety> getSuretyPage(String inpatientNo, String page,String rows) {
		String sql ="select * from ( select t.ID as id,t.SURETY_COST as suretyCost, t.PAY_WAY as payWay,t.OPEN_ACCOUNTS as openAccounts, t.OPEN_BANK as openBank,"
				+ "t.SURETY_TYPE as suretyType, t.STATE as state,t.CREATETIME as createTime,ROWNUM as n  "
				+ "from T_INPATIENT_SURETY t where t.INPATIENT_NO =:inpatientNo and t.del_flg=0 and t.stop_flg=0  and ROWNUM  <= (:page) * :row   "
				+ " ) where n > (:page -1) * :row ORDER BY createTime DESC";
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("page", page);
		paraMap.put("row", rows);
		paraMap.put("inpatientNo", inpatientNo);
		List<InpatientSurety> inpatientSuretyList =  namedParameterJdbcTemplate.query(sql,paraMap,new RowMapper<InpatientSurety>() {

			@Override
			public InpatientSurety mapRow(ResultSet rs, int rowNum) throws SQLException {
				InpatientSurety inpatientSurety = new InpatientSurety();
				inpatientSurety.setId(rs.getString("id"));
				inpatientSurety.setSuretyCost(rs.getObject("suretyCost")==null?null:rs.getDouble("suretyCost"));
				inpatientSurety.setPayWay(rs.getString("payWay"));
				inpatientSurety.setOpenAccounts(rs.getString("openAccounts"));
				inpatientSurety.setOpenBank(rs.getString("openBank"));
				inpatientSurety.setSuretyType(rs.getString("suretyType"));
				inpatientSurety.setState(rs.getObject("state")==null?null:rs.getInt("state"));
				inpatientSurety.setCreateTime(rs.getTimestamp("createTime"));
				return inpatientSurety;
			}
			
		});
		return inpatientSuretyList;
	}
	
	/**
	 *
	 * @Description：查询担保金信息 - 分页查询 - 获得查询语句
	 * @Author：aizhonghua
	 * @CreateDate：2016年4月12日 上午9:47:41 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0
	 * @param1：inpatientNo住院号
	 * @return：分页信息
	 * 
	 */
	private String jointHql(String inpatientNo) {
		String hql = "FROM InpatientSurety i WHERE i.inpatientNo = '"+inpatientNo+"' AND i.stop_flg = 0 AND i.del_flg = 0 ORDER BY i.createTime DESC";
		return hql;
	}

	@Override
	public void updateInpatientSurety(InpatientSurety ins) {
		String sql ="update T_INPATIENT_SURETY set SURETY_COST=?,STATE=? where ID=?";
		Object args[] = new Object[]{ins.getSuretyCost(),ins.getState(),ins.getId()};  
		jdbcTemplate.update(sql,args); 
		
	}
}
