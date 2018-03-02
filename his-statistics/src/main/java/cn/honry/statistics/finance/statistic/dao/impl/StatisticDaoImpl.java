package cn.honry.statistics.finance.statistic.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.inner.baseinfo.department.dao.DeptInInterDAO;
import cn.honry.inner.vo.MenuListVO;
import cn.honry.inner.vo.MenuVO;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.finance.statistic.dao.StatisticDao;
import cn.honry.statistics.finance.statistic.vo.QueryVo;
import cn.honry.statistics.finance.statistic.vo.StatisticVo;
import cn.honry.utils.HisParameters;

/**  
 *  收入统计汇总数据访问类
 * @Author:luyanshou
 * @version 1.0
 */
@Repository("statisticDao")
public class StatisticDaoImpl extends HibernateDaoSupport implements StatisticDao {

	@Resource(name = "sessionFactory")
	// 为HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	//基础工具类,不支持参数名传参
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	//扩展工具类,支持参数名传参
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
		
	@Autowired
	@Qualifier(value = "deptInInterDAO")
	private DeptInInterDAO deptInInterDAO;
	
	/**
	 * 查询住院下的科室信息
	 */
	@Override
	public List<SysDepartment> getDept() {
		String sql="SELECT t.DEPT_ID AS id,t.DEPT_CODE AS deptCode,t.DEPT_NAME AS deptName FROM T_DEPARTMENT t WHERE t.DEPT_TYPE='I'";
		SQLQuery queryObject = this.getSession().createSQLQuery(sql)
				.addScalar("id")
				.addScalar("deptCode")
				.addScalar("deptName");
		List<SysDepartment> list =queryObject.setResultTransformer(Transformers.aliasToBean(SysDepartment.class)).list();
		if(list!=null && list.size()>0){
			return list;
		}
		return new ArrayList<SysDepartment>();
	}
	
	/**
	 * 查询非药品明细统计信息
	 * @param vo 封装的查询条件
	 */
    @Override
	public List<StatisticVo> getCostData(List<String> tnLMed, List<String> tnLItem, QueryVo vo) throws Exception{
		if((tnLMed==null||tnLMed.size()<0)&&(tnLItem==null||tnLItem.size()<0)){
			return new ArrayList();
		}
		List<StatisticVo> list= new ArrayList();//返回值
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String begin=null;//开始时间
		String end=null;//结束时间
		if(null!=vo){
			if(null!=vo.getStartDate()){
				begin=sdf.format(vo.getStartDate());
			}
			if(null!=vo.getEndDate()){
				end=sdf.format(vo.getEndDate());
			}
		}
		final StringBuffer sb = new StringBuffer(900);
		if(tnLMed.size()>1||tnLItem.size()>1){
			sb.append("SELECT inhosDeptcode,deptName,minfeeCode,totCost from (");
		}
		
		for(int i=0;i<tnLMed.size();i++){
			if(i!=0){
				sb.append("UNION ");
			}
			sb.append("SELECT DISTINCT rm").append(i).append(".inhos_deptcode AS inhosDeptcode,rm").append(i).append(".INHOS_DEPTNAME AS deptName,rm")
			.append(i).append(".fee_code AS minfeeCode,sum(rm").append(i).append(".tot_cost) AS totCost ");
			sb.append("FROM ").append(HisParameters.HISPARSCHEMAHISUSER).append(tnLMed.get(i)).append(" rm").append(i).append(" WHERE 1=1 ");
			if(vo!=null){
				if(StringUtils.isNotBlank(vo.getId())&& !"0".equals(vo.getId())){
					sb.append(" AND rm").append(i).append(".inhos_deptcode in ("+vo.getId()+")");
				}
				if(null!=begin){
					sb.append(" AND rm").append(i).append(".fee_date >= to_date('"+begin+"','yyyy-mm-dd hh24:mi:ss') ");
				}
				if(null!=end){
					sb.append(" AND rm").append(i).append(".fee_date < to_date('"+end+"','yyyy-mm-dd hh24:mi:ss') ");
				}
			}
			sb.append("group by rm").append(i).append(".inhos_deptcode ,rm").append(i).append(".INHOS_DEPTNAME ,rm").append(i).append(".fee_code  ");
		}
		
		sb.append(" UNION  all ");
		for(int i=0;i<tnLItem.size();i++){
			if(i!=0){
				sb.append("UNION ");
			}
			sb.append(" SELECT DISTINCT rm").append(i).append(".inhos_deptcode AS inhosDeptcode,rm").append(i).append(".INHOS_DEPTNAME AS deptName,rm")
			.append(i).append(".fee_code AS minfeeCode,sum(rm").append(i).append(".tot_cost) AS totCost ");
			sb.append("FROM ").append(HisParameters.HISPARSCHEMAHISUSER).append(tnLItem.get(i)).append(" rm").append(i).append(" WHERE 1=1 ");
			if(vo!=null){
				if(StringUtils.isNotBlank(vo.getId()) && !"0".equals(vo.getId())){
					sb.append(" AND rm").append(i).append(".inhos_deptcode in ("+vo.getId()+")");
				}
				if(null!=begin){
					sb.append(" AND rm").append(i).append(".fee_date > to_date('"+begin+"','yyyy-mm-dd hh24:mi:ss') ");
				}
				if(null!=end){
					sb.append(" AND rm").append(i).append(".fee_date < to_date('"+end+"','yyyy-mm-dd hh24:mi:ss') ");
				}
			}
			sb.append("group by rm").append(i).append(".inhos_deptcode ,rm").append(i).append(".INHOS_DEPTNAME ,rm").append(i).append(".fee_code  ");
		}

		if(tnLMed.size()>1||tnLItem.size()>1){
			sb.append(") ");
		}
		List<StatisticVo> li = super.getSession().createSQLQuery(sb.toString()).addScalar("inhosDeptcode")
								.addScalar("deptName").addScalar("minfeeCode").addScalar("totCost",Hibernate.DOUBLE).setResultTransformer(Transformers.aliasToBean(StatisticVo.class)).list();
				
		
		list.addAll(li);
	    if(list!=null && list.size()>0){
			return list;
	    }
	    return new ArrayList<StatisticVo>();
	}
	
	/**
	 * 根据科室id查询科室名称
	 */
    @Override
	public String getDeptName(String id) throws Exception{
		SysDepartment dept = deptInInterDAO.get(id);
		if(StringUtils.isNotBlank(dept.getDeptName())){
			return dept.getDeptName();
		}
		return null;
	}
	
	/**
	 * 根据最小费用代码查询最小费用与统计大类对照信息
	 */
    @Override
	public List<MinfeeStatCode> getMinfee(String minfeeCode) throws Exception{
		DetachedCriteria dc=DetachedCriteria.forClass(MinfeeStatCode.class);
		if(StringUtils.isNotBlank(minfeeCode)){
			dc.add(Restrictions.eq("minfeeCode", minfeeCode));
		}
		//TODO 迁移问题
		List<MinfeeStatCode> list =(List<MinfeeStatCode>) getHibernateTemplate().findByCriteria(dc);
		return list;
	}
	
	/**
	 * 根据报表代码查询最小费用与统计大类对照信息
	 */
    @Override
	public List<StatisticVo> getfeetStat(String reportCode) throws Exception{
		String sql="select t.REPORT_CODE as reportCode,t.REPORT_NAME as reportName,t.MINFEE_CODE as minfeeCode "
				+ ",t.MINFEE_NAME as minfeeName,t.FEE_STAT_NAME as feeStatName from T_CHARGE_MINFEETOSTAT t where t.STOP_FLG= 0 and t.DEL_FLG= 0";
		if(StringUtils.isNotBlank(reportCode)){
			sql+=" and t.REPORT_CODE= '"+reportCode+"'";
		}
		Query query = this.getSession().createSQLQuery(sql)
				.addScalar("reportCode")
				.addScalar("reportName")
				.addScalar("minfeeCode")
				.addScalar("minfeeName")
				.addScalar("feeStatName");
		List<StatisticVo> list = query.setResultTransformer(Transformers.aliasToBean(StatisticVo.class)).list();
		if(list!=null && list.size()>0){
			return list;
		}
		return new ArrayList<StatisticVo>();
	}
	
	/**
	 * 查询报表记录信息
	 */
    @Override
	public List<StatisticVo> getreport() throws Exception{
		String sql="select distinct t.REPORT_CODE as reportCode,t.REPORT_NAME as reportName "
				+ "from T_CHARGE_MINFEETOSTAT t where t.STOP_FLG= 0 and t.DEL_FLG= 0";
		Query query = this.getSession().createSQLQuery(sql)
				.addScalar("reportCode")
				.addScalar("reportName");
		List<StatisticVo> list = query.setResultTransformer(Transformers.aliasToBean(StatisticVo.class)).list();
		if(list!=null && list.size()>0){
			return list;
		}
		return new ArrayList<StatisticVo>();
	}
	
	/**
	 * 根据报表代码查询 统计费用名称
	 */
    @Override
	public List<StatisticVo> getfeeStatName(String reportCode) throws Exception{
		String sql="select distinct t.FEE_STAT_NAME as feeStatName,t.FEE_STAT_CODE as feeStatCode "
				+ "from T_CHARGE_MINFEETOSTAT t where t.STOP_FLG= 0 and t.DEL_FLG= 0";
		if(StringUtils.isNotBlank(reportCode)){
			sql+=" and t.REPORT_CODE= '"+reportCode+"'";
		}
		Query query = this.getSession().createSQLQuery(sql)
				.addScalar("feeStatCode")
				.addScalar("feeStatName");
		List<StatisticVo> list = query.setResultTransformer(Transformers.aliasToBean(StatisticVo.class)).list();
		if(list!=null && list.size()>0){
			return list;
		}
		return new ArrayList<StatisticVo>();
	}
	
	
	/**  
	 * 
	 * <p> 获取业务表中最大及最小时间 </p>
	 * @Author: zhuxiaolu
	 * @CreateDate: 2016年11月29日 下午03:58:02 
	 * @Modifier: zhuxiaolu
	 * @ModifyDate: 2016年11月29日 下午03:58:02 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public StatVo findMaxMinItem() throws Exception{
		final String sql = "SELECT MAX(mn.fee_date) AS eTime ,MIN(mn.fee_date) AS sTime FROM T_INPATIENT_ITEMLIST_NOW mn";
		StatVo vo = (StatVo) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public StatVo doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sql.toString());
				queryObject.addScalar("eTime",Hibernate.DATE).addScalar("sTime",Hibernate.DATE);
				return (StatVo) queryObject.setResultTransformer(Transformers.aliasToBean(StatVo.class)).uniqueResult();
			}
		});
		return vo;
	}
	
	
	/**  
	 * 
	 * <p> 获取业务表中最大及最小时间 </p>
	 * @Author: zhuxiaolu
	 * @CreateDate: 2016年11月29日 下午03:58:02 
	 * @Modifier: zhuxiaolu
	 * @ModifyDate: 2016年11月29日 下午03:58:02 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public StatVo findMaxMinMed() throws Exception{
		final String sql = "SELECT MAX(mn.fee_date) AS eTime ,MIN(mn.fee_date) AS sTime FROM T_INPATIENT_MEDICINELIST_NOW mn";
		StatVo vo = (StatVo) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public StatVo doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sql.toString());
				queryObject.addScalar("eTime",Hibernate.DATE).addScalar("sTime",Hibernate.DATE);
				return (StatVo) queryObject.setResultTransformer(Transformers.aliasToBean(StatVo.class)).uniqueResult();
			}
		});
		return vo;
	}

	@Override
	public List<MenuListVO> getDeptList() throws Exception{
		StringBuffer buffer = new StringBuffer("SELECT ");
		buffer.append(" distinct t.FEE_STAT_NAME as name,t.FEE_STAT_CODE as id,t.REPORT_CODE as type "
				+ "from T_CHARGE_MINFEETOSTAT t where t.STOP_FLG= 0 and t.DEL_FLG= 0 and t.REPORT_CODE= 'ZY01'");
		List<MenuListVO> depts=new ArrayList<MenuListVO>();
		List<MenuVO> voList =  namedParameterJdbcTemplate.query(buffer.toString(),new RowMapper<MenuVO>() {
			@Override
			public MenuVO mapRow(ResultSet rs, int rowNum) throws SQLException {
				MenuVO vo=new MenuVO();
				vo.setId(rs.getString("id"));
				vo.setName(rs.getString("name"));
				vo.setType(rs.getString("type"));
				return vo;
		}});
		String[] arr=new String[]{"ZY01-住院发票"};
		
		for(int i=0;i<arr.length;i++){
			String[] arr1=arr[i].split("-");
			MenuListVO d=new MenuListVO();
			d.setParentMenu(arr1[1]);
			List<MenuVO> rs=new ArrayList<MenuVO>();
			for(MenuVO v:voList){
				if(arr1[0].equals(v.getType())){
					rs.add(v);
				}				
			}
			d.setMenus(rs);
			depts.add(d);
		}
		return depts;
	}
}
