package cn.honry.statistics.drug.inventoryLog.dao.Impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.DrugChecklogs;
import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.DrugOutstore;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.doctor.registerInfoGzltj.vo.RegisterInfoGzltjVo;
import cn.honry.statistics.drug.inventoryLog.dao.InventoryLogDao;
import cn.honry.statistics.drug.inventoryLog.vo.InventoryLogVo;
import cn.honry.statistics.drug.nurseDrugDispens.vo.DrugDispensSumVo;
import cn.honry.utils.HisParameters;


@Repository("inventoryLogDao")
@SuppressWarnings({"all"})
public class InventoryLogDaoImpl extends HibernateEntityDao<DrugChecklogs> implements InventoryLogDao{
	// 为父类HibernateDaoSupport注入sessionFactory的值
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	/***
	 * 盘点日志查询(统计)记录
	 * @Description:
	 * @author: zpty
	 * @CreateDate: 2016年6月22日 
	 * @version 1.0
	 */
	@Override
	public List<InventoryLogVo> queryInventoryLog(List<String> tnL,String Stime, String Etime,
			String dept, String page, String rows,String drug) throws Exception{
		if(tnL==null||tnL.size()<0){
			return new ArrayList<InventoryLogVo>();
		}
		String sql=querySql(tnL,Stime,Etime,dept,drug);
		sql="SELECT　* FROM (SELECT　t1.*,ROWNUM as n FROM ( "+sql+"  ) t1 where ROWNUM<=:row  )　WHERE n>:page ORDER BY createtime DESC";
		Map paraMap=new HashMap();
		
		int start = Integer.parseInt(page == null ? "1" : page);
		int count = Integer.parseInt(rows == null ? "20" : rows);
		if(StringUtils.isNotBlank(Stime)){
			paraMap.put("begin", Stime);
		}
		if(StringUtils.isNotBlank(Etime)){
			paraMap.put("end", Etime);
		}
		if(StringUtils.isNotBlank(dept)){
			paraMap.put("dept", dept);
		}
		if(StringUtils.isNotBlank(drug)){
			paraMap.put("drug", drug);
		}
		paraMap.put("row", count*start);
		paraMap.put("page", (start - 1) * count);
		List<InventoryLogVo> DrugChecklogsList= namedParameterJdbcTemplate.query(sql,paraMap,new RowMapper<InventoryLogVo>() {
			@Override
			public InventoryLogVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				InventoryLogVo vo = new InventoryLogVo();
//				vo.setCheckCode(rs.getString("checkCode"));
				vo.setDeptName(rs.getString("deptName"));
				vo.setDrugCode(rs.getString("drugCode"));
				vo.setBatchNo(rs.getString("batchNo"));
				vo.setTradeName(rs.getString("tradeName"));
				vo.setSpecs(rs.getString("specs"));
				vo.setRetailPrice(rs.getDouble("retailPrice"));
				vo.setPackUnit(rs.getString("packUnit"));
				vo.setPackQty(rs.getDouble("packQty"));
				vo.setAdjustNum(rs.getDouble("adjustNum"));
				vo.setAdjustUnit(rs.getString("adjustUnit"));
				vo.setPlaceNo(rs.getString("placeNo"));
				vo.setRemark(rs.getString("remark"));
				vo.setCreateuser(rs.getString("createuser"));
				vo.setCreatetime(rs.getTimestamp("createtime"));
				vo.setUserName(rs.getString("userName"));
				return vo;
			}
			
		});
		
		if(DrugChecklogsList!=null&&DrugChecklogsList.size()>0){
			return DrugChecklogsList;
		}
		return new ArrayList<InventoryLogVo>();
	}
	/***
	 * 盘点日志查询(统计)条数
	 * @Description:
	 * @author: zpty
	 * @CreateDate: 2016年6月22日 
	 * @version 1.0
	 */
	@Override
	public int queryInventoryLogTotle(List<String> tnL,String Stime, String Etime, String dept,String drug) throws Exception{
		if(tnL==null||tnL.size()<0){
			return 0;
		}
		String sql=" SELECT COUNT(1) AS specs FROM ( "+querySql(tnL,Stime,Etime,dept,drug).toString()+" )";
		Map paraMap=new HashMap();
		if(StringUtils.isNotBlank(Stime)){
			paraMap.put("begin", Stime);
		}
		if(StringUtils.isNotBlank(Etime)){
			paraMap.put("end", Etime);
		}
		if(StringUtils.isNotBlank(dept)){
			paraMap.put("dept", dept);
		}
		if(StringUtils.isNotBlank(drug)){
			paraMap.put("drug", drug);
		}
		List<InventoryLogVo> DrugChecklogsList= namedParameterJdbcTemplate.query(sql,paraMap,new RowMapper<InventoryLogVo>() {

			@Override
			public InventoryLogVo mapRow(ResultSet arg0, int arg1)
					throws SQLException {
				InventoryLogVo vo=new InventoryLogVo();
				vo.setSpecs(arg0.getString("specs"));
				return vo;
			}
			
		});
		if(null!=DrugChecklogsList&&DrugChecklogsList.size()>0){
			return Integer.parseInt(DrugChecklogsList.get(0).getSpecs());
		}
		return 0;
	}
	
	/***
	 * 拼接sql
	 * @Description:
	 * @author: zpty 
	 * @CreateDate: 2016年6月22日 
	 * @version 1.0
	 */
	private String querySql(List<String> tnL,String Stime,String Etime, String dept,String drug) throws Exception{
		final StringBuffer sb = new StringBuffer();
		for(int i=0;i<tnL.size();i++){
			if(i!=0){
				sb.append("UNION ");
			}
			//(select ch.CHECK_CODE from T_DRUG_CHECKSTATIC ch where ch.id in (select c.check_id from T_DRUG_CHECKDETAIL c where c.DRUG_DEPT_CODE=t").append(i).append(".DRUG_DEPT_CODE and c.DRUG_CODE=t").append(i).append(".DRUG_CODE and c.del_flg=0 and c.stop_flg=0) and ch.del_flg=0 and ch.stop_flg=0) as checkCode,
			sb.append("select ");
			sb.append("t").append(i).append(".DRUG_DEPT_CODE  as deptName,");
			sb.append("t").append(i).append(".DRUG_CODE  as drugCode,");
			sb.append("t").append(i).append(".BATCH_NO as batchNo,t").append(i).append(".TRADE_NAME as tradeName,t").append(i).append(".SPECS as specs,t").append(i).append(".RETAIL_PRICE as retailPrice,");
			sb.append("t").append(i).append(".PACK_UNIT as packUnit,");
			sb.append("t").append(i).append(".PACK_QTY as packQty,t").append(i).append(".ADJUST_NUM as adjustNum,");
			sb.append("t").append(i).append(".ADJUST_UNIT as adjustUnit,");
			sb.append("t").append(i).append(".PLACE_NO placeNo,t").append(i).append(".REMARK as remark,t").append(i).append(".CREATEUSER as createuser,t").append(i).append(".CREATETIME as createtime,");
			sb.append("t").append(i).append(".CREATEUSER  as userName");
			sb.append(" from ").append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" t").append(i).append(" where t").append(i).append(".del_flg=0 and t").append(i).append(".stop_flg=0 ");
			if(StringUtils.isNotBlank(Stime)){
				sb.append(" and trunc(t").append(i).append(".CREATETIME,'dd') >= to_date(:begin,'yyyy-MM-dd hh24:mi:ss')");
			}
			if(StringUtils.isNotBlank(Etime)){
				sb.append(" and trunc(t").append(i).append(".CREATETIME,'dd') <= to_date(:end,'yyyy-MM-dd hh24:mi:ss') ");
			}
			if(StringUtils.isNotBlank(dept)){
				sb.append(" and t").append(i).append(".drug_dept_code = :dept ");
			}
			if(StringUtils.isNotBlank(drug)){
				sb.append(" and t").append(i).append(".DRUG_CODE = :drug ");
			}
		}
		return sb.toString();
	}
	
	/**  
	 *  
	 * @Description：  科室下拉框
	 * @Author：zpty
	 * @CreateDate：2016-6-22
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<SysDepartment> getComboboxdept() throws Exception{
		String hql=" FROM SysDepartment d where d.stop_flg = 0 and d.del_flg = 0 and d.deptType in('P','PI') ";
		List<SysDepartment> deptList=this.find(hql, null);
		if(deptList!=null && deptList.size()>0){
			return deptList;
		}	
		return new ArrayList<SysDepartment>();
	}
	
	/**  
	 *  
	 * @Description：  药品下拉框
	 * @Author：zpty
	 * @CreateDate：2016-6-22
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<DrugInfo> getComboboxdrug() throws Exception{
		String hql="FROM DrugInfo d where d.stop_flg = 0 and d.del_flg = 0";
		List<DrugInfo> drugList=this.find(hql, null);
		if(drugList!=null && drugList.size()>0){
			return drugList;
		}	
		return new ArrayList<DrugInfo>();
	}

	/**
	 * @Description:导出 
	 * @Author： lt @CreateDate： 2015-9-10
	 * @param @throws Exception
	 * @return void
	 * @version 1.0
	 **/
	@Override
	public List<InventoryLogVo> queryInvLogExp(List<String> tnL,String Stime, String Etime,
			String dept, String drug) throws Exception{
		if(tnL==null||tnL.size()<0){
			return new ArrayList<InventoryLogVo>();
		}
		String sql=" SELECT * FROM ( "+querySql(tnL,Stime,Etime,dept,drug).toString()+" )";
		Map paraMap=new HashMap();
		
		if(StringUtils.isNotBlank(Stime)){
			paraMap.put("begin", Stime);
		}
		if(StringUtils.isNotBlank(Etime)){
			paraMap.put("end", Etime);
		}
		if(StringUtils.isNotBlank(dept)){
			paraMap.put("dept", dept);
		}
		if(StringUtils.isNotBlank(drug)){
			paraMap.put("drug", drug);
		}
		List<InventoryLogVo> DrugChecklogsList= namedParameterJdbcTemplate.query(sql,paraMap,new RowMapper<InventoryLogVo>() {
			@Override
			public InventoryLogVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				InventoryLogVo vo = new InventoryLogVo();
//				vo.setCheckCode(rs.getString("checkCode"));
				vo.setDeptName(rs.getString("deptName"));
				vo.setDrugCode(rs.getString("drugCode"));
				vo.setBatchNo(rs.getString("batchNo"));
				vo.setTradeName(rs.getString("tradeName"));
				vo.setSpecs(rs.getString("specs"));
				vo.setRetailPrice(rs.getDouble("retailPrice"));
				vo.setPackUnit(rs.getString("packUnit"));
				vo.setPackQty(rs.getDouble("packQty"));
				vo.setAdjustNum(rs.getDouble("adjustNum"));
				vo.setAdjustUnit(rs.getString("adjustUnit"));
				vo.setPlaceNo(rs.getString("placeNo"));
				vo.setRemark(rs.getString("remark"));
				vo.setCreateuser(rs.getString("createuser"));
				vo.setCreatetime(rs.getTimestamp("createtime"));
				vo.setUserName(rs.getString("userName"));
				return vo;
			}
			
		});
		
		if(DrugChecklogsList!=null&&DrugChecklogsList.size()>0){
			return DrugChecklogsList;
		}
		return new ArrayList<InventoryLogVo>();
	}

	@Override
	public StatVo findMaxMin() throws Exception{
		final String sql = "SELECT MAX(mn.CREATETIME) AS eTime ,MIN(mn.CREATETIME) AS sTime FROM T_DRUG_CHECKLOGS mn";
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
}
