package cn.honry.inpatient.info.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessHospitalbed;
import cn.honry.base.bean.model.DepartmentContact;
import cn.honry.base.bean.model.InpatientBedinfo;
import cn.honry.base.bean.model.InpatientBedinfoNow;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inpatient.info.dao.InpatientBedInfoDAO;

@Repository("inpatientBedInfoDAO")
@SuppressWarnings({"all"})
public class InpatientBedInfoDAOImpl extends HibernateEntityDao<InpatientBedinfoNow> implements InpatientBedInfoDAO{
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	//基础工具类,不支持参数名传参
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public InpatientBedinfoNow getByBedId(String bedId) {
		String sql = "from InpatientBedinfoNow b where b.bedId = '" + bedId + "' and b.stop_flg = 0 and b.del_flg = 0";
		List<InpatientBedinfoNow> list = this.getSession().createQuery(sql).list();
		return list.size() == 0 ? null :list.get(0);
	}

	@Override
	public List<InpatientBedinfoNow> list() {
		String sql = "from InpatientBedinfoNow b where b.stop_flg = 0 and b.del_flg = 0";
		List<InpatientBedinfoNow> list = this.getSession().createQuery(sql).list();
		return list;
	}
	@Override
	public List<BusinessHospitalbed> bedlist() {
		String sql = "from BusinessHospitalbed b where b.stop_flg = 0 and b.del_flg = 0";
		List<BusinessHospitalbed> list = this.getSession().createQuery(sql).list();
		return list;
	}
	@Override
	public List<DepartmentContact> deplist() {
		String sql = "from DepartmentContact b where b.stop_flg = 0 and b.del_flg = 0";
		List<DepartmentContact> list = this.getSession().createQuery(sql).list();
		return list;
	}
	@Override
	public List<BusinessContractunit> reglist() {
		String sql ="select t.UNIT_CODE as encode,t.UNIT_NAME as name from T_BUSINESS_CONTRACTUNIT t "
				+ "where t.DEL_FLG=0 and t.STOP_FLG=0";
	    List<BusinessContractunit> list =  jdbcTemplate.query(sql,new RowMapper<BusinessContractunit>() {

			@Override
			public BusinessContractunit mapRow(ResultSet rs, int rowNum) throws SQLException {
				BusinessContractunit businessContractunit = new BusinessContractunit();
				businessContractunit.setEncode(rs.getString("encode"));
				businessContractunit.setName(rs.getString("name"));
				return businessContractunit;
			}});
	    if(list !=null && list.size()>0){
	    	return list;
	    }
		return new ArrayList<BusinessContractunit>() ;
	}
	@Override
	public List<SysEmployee> bedinfolist(String id) {
		String sql = "from SysEmployee e where e.id in (select i.houseDocCode FROM InpatientBedinfoNow i "
				+ "WHERE i.id = :id) and e.stop_flg=0 AND e.del_flg=0"; 
		
		try {
			List<SysEmployee> list = this.getSession().createQuery(sql).setParameter("id", id).list();
			return list;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
		
	}
	/**  
	 *  
	 * @Description：  查询住院床位使用记录
	 * @Author：aizhonghua
	 * @CreateDate：2015-8-21 上午11:57:26  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-8-21 上午11:57:26  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<InpatientBedinfoNow> queryBedinfo(String id) {
		String hql = "FROM InpatientBedinfoNow i WHERE i.stop_flg=0 AND i.del_flg=0 AND i.bedId = '"+id+"'"; 
		List<InpatientBedinfoNow> iList = super.findByObjectProperty(hql, null);
		if(iList!=null&&iList.size()>0){
			return iList;
		}
		return new ArrayList<InpatientBedinfoNow>();
	}
	
	/**
	 * @Description:通过主键ID获取InpatientBedinfo
	 * @Author：  TCJ
	 * @CreateDate： 2016-1-6
	 * @return InpatientBedinfo  
	 * @version 1.0
	**/
	@Override
	public InpatientBedinfoNow queryBedInfoByMainID(String id) {
		String hql = "from InpatientBedinfoNow i where i.del_flg = 0 and i.id='"+id+"'";
		List<InpatientBedinfoNow> list  = this.getSession().createQuery(hql).list();
			if(list!=null&&list.size()>0){
				return list.get(0);
			}
		return new InpatientBedinfoNow();
	}

}
