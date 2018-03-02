package cn.honry.statistics.drug.queryInstore.dao.impl;

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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.DrugInStore;
import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.DrugSupplycompany;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.statistics.doctor.registerInfoGzltj.vo.RegisterInfoGzltjVo;
import cn.honry.statistics.drug.queryInstore.dao.QueryInstoreDao;
import cn.honry.utils.DateUtils;
import cn.honry.utils.NumberUtil;


@Repository("queryInstoreDao")
@SuppressWarnings({"all"})
public class QueryInstoreDaoImpl extends HibernateEntityDao<DrugInStore> implements QueryInstoreDao{
	// 为父类HibernateDaoSupport注入sessionFactory的值
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Resource
	private JdbcTemplate jdbcTemplate;
	//扩展工具类,支持参数名传参
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	/***
	 * 药品入库查询(统计)记录
	 * @Description:
	 * @author: zpty
	 * @CreateDate: 2016年6月22日 
	 * @version 1.0
	 */
	@Override
	public List<DrugInStore> queryInstore(String Stime, String Etime,
			String drug, String page, String rows,String company,String user) throws Exception{
		String sql=querySql(Stime,Etime,drug,company,user,page,rows);
		Map<String, Object> paraMap = new HashMap<String, Object>();
		final NumberUtil number= NumberUtil.init();
		List<DrugInStore> voList =  namedParameterJdbcTemplate.query(sql.toString(),paraMap,new RowMapper<DrugInStore>() {
			@Override
			public DrugInStore mapRow(ResultSet rs, int rowNum) throws SQLException {
				DrugInStore vo = new DrugInStore();
				vo.setTradeName(rs.getString("tradeName"));
				vo.setSpecs(rs.getString("specs"));
				vo.setPackUnit(rs.getString("packUnit"));
				vo.setInNum(rs.getDouble("inNum"));
				vo.setRetailPrice(Double.valueOf(number.format(rs.getDouble("retailPrice"),2)));
				vo.setPurchasePrice(Double.valueOf(number.format(rs.getDouble("purchasePrice"),2)));
				vo.setPurchaseCost(Double.valueOf(number.format(rs.getDouble("purchaseCost"),2)));
				vo.setCompanyCode(rs.getString("companyCode"));
				vo.setBatchNo(rs.getString("batchNo"));
				vo.setValidDate(rs.getDate("validDate"));
				vo.setInvoiceNo(rs.getString("invoiceNo"));
				vo.setDeliveryNo(rs.getString("deliveryNo"));
				return vo;
		}});
		return voList;
	}
	/***
	 * 药品入库查询(统计)条数
	 * @Description:
	 * @author: zpty
	 * @CreateDate: 2016年6月22日 
	 * @version 1.0
	 */
	@Override
	public int queryInstoreTotle(String Stime, String Etime, String drug,String company,String user) throws Exception{
		String sql="select count(1) "
				+ "  from T_DRUG_INSTORE t1   where t1.del_flg = 0 and t1.stop_flg = 0 ";
		if(StringUtils.isNotBlank(Stime)){
			sql+=" and t1.APPLY_DATE >= to_date( '"+Stime+"','yyyy-MM-dd')";
		}
		if(StringUtils.isNotBlank(Etime)){
			sql+=" and t1.APPLY_DATE <= to_date( '"+Etime+"','yyyy-MM-dd')";
		}
		if(StringUtils.isNotBlank(drug)){
			sql+=" and t1.DRUG_CODE = '"+drug+"' ";
		}
		if(StringUtils.isNotBlank(company)){
			sql+=" and t1.company_code = '"+company+"' ";
		}
		if(StringUtils.isNotBlank(user)){
			sql+=" and t1.createuser = '"+user+"' ";
		}
		return jdbcTemplate.queryForObject(sql, java.lang.Integer.class);
	}
	
	/***
	 * 拼接sql
	 * @Description:
	 * @author: zpty
	 * @CreateDate: 2016年6月22日 
	 * @version 1.0
	 */
	private String querySql(String Stime,String Etime, String drug,String company,String user, String page, String rows) {
		String sql="select * from (select t1.trade_name as tradeName, t1.specs as specs,t1.PACK_UNIT as packUnit,round(t1.in_num / t1.pack_qty, 2) as inNum,"
				+ " t1.retail_price as retailPrice,t1.purchase_price as purchasePrice,(round(t1.in_num / t1.pack_qty, 2) * t1.purchase_price) as purchaseCost,"
				+ " t1.company_code as companyCode, t1.batch_no as batchNo, t1.valid_date as validDate, t1.invoice_no as invoiceNo,t1.DELIVERY_NO as deliveryNo,"
				+ " ROWNUM  as rn from T_DRUG_INSTORE t1   where t1.del_flg = 0 and t1.stop_flg = 0 ";
		if(StringUtils.isNotBlank(Stime)){
			sql+=" and t1.APPLY_DATE >= to_date( '"+Stime+"','yyyy-MM-dd')";
		}
		if(StringUtils.isNotBlank(Etime)){
			sql+=" and t1.APPLY_DATE <= to_date( '"+Etime+"','yyyy-MM-dd')";
		}
		if(StringUtils.isNotBlank(drug)){
			sql+=" and t1.DRUG_CODE = '"+drug+"' ";
		}
		if(StringUtils.isNotBlank(company)){
			sql+=" and t1.company_code = '"+company+"' ";
		}
		if(StringUtils.isNotBlank(user)){
			sql+=" and t1.APPLY_OPERCODE = '"+user+"' ";
		}
		sql+=" )tt where  rn > ('"+page+"' -1) * '"+rows+"' and rn <=('"+page+"') * '"+rows+"' ";
		return sql;
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
		String hql="FROM DrugInfo d where d.del_flg = 0";
		List<DrugInfo> drugList=this.find(hql, null);
		if(drugList!=null && drugList.size()>0){
			return drugList;
		}	
		return new ArrayList<DrugInfo>();
	}

	/**  
	 *  
	 * @Description：  生产厂家下拉框
	 * @Author：zpty
	 * @CreateDate：2016-6-22
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<DrugSupplycompany> getComboboxCompany() throws Exception {
		String hql="FROM DrugSupplycompany d where d.stop_flg = 0 and d.del_flg = 0";
		List<DrugSupplycompany> companyList=this.find(hql, null);
		if(companyList!=null && companyList.size()>0){
			return companyList;
		}	
		return new ArrayList<DrugSupplycompany>();
	}

	/**  
	 *  
	 * @Description：  人员下拉框
	 * @Author：zpty
	 * @CreateDate：2016-6-22
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<User> getComboboxUser() throws Exception{
		String hql="FROM User d where d.stop_flg = 0 and d.del_flg = 0";
		List<User> userList=this.find(hql, null);
		if(userList!=null && userList.size()>0){
			return userList;
		}	
		return new ArrayList<User>();
	}

	/***
	 * 药品入库查询(统计)记录--导出用
	 * @Description:
	 * @author: zpty
	 * @CreateDate: 2016年6月22日 
	 * @version 1.0
	 */
	@Override
	public List<DrugInStore> queryInstoreExp(String Stime, String Etime,
			String drug, String company,String user) throws Exception{
		String sql="select t1.trade_name tradeName,t1.specs as specs,t1.PACK_UNIT as packUnit,t1.retail_price as retailPrice,t1.purchase_price as purchasePrice,round(t1.in_num / t1.pack_qty, 2) as inNum,"
				+"(round(t1.in_num / t1.pack_qty, 2) * t1.purchase_price) as purchaseCost,t1.company_code as companyCode, t1.batch_no as batchNo, t1.valid_date as validDate, t1.invoice_no as invoiceNo,t1.DELIVERY_NO as deliveryNo"
				+ " from T_DRUG_INSTORE t1   where t1.del_flg = 0 and t1.stop_flg = 0 "
				;
		if(StringUtils.isNotBlank(Stime)){
			sql+=" and t1.APPLY_DATE >= to_date( '"+Stime+"','yyyy-MM-dd')";
		}
		if(StringUtils.isNotBlank(Etime)){
			sql+=" and t1.APPLY_DATE <= to_date( '"+Etime+"','yyyy-MM-dd')";
		}
		if(StringUtils.isNotBlank(drug)){
			sql+=" and t1.DRUG_CODE = '"+drug+"' ";
		}
		if(StringUtils.isNotBlank(company)){
			sql+=" and t1.company_code = '"+company+"' ";
		}
		if(StringUtils.isNotBlank(user)){
			sql+=" and t1.createuser = '"+user+"' ";
		}
		
		Map<String, Object> paraMap = new HashMap<String, Object>();
		List<DrugInStore> voList =  namedParameterJdbcTemplate.query(sql.toString(),paraMap,new RowMapper<DrugInStore>() {
			@Override
			public DrugInStore mapRow(ResultSet rs, int rowNum) throws SQLException {
				DrugInStore vo = new DrugInStore();
				vo.setTradeName(rs.getString("tradeName"));
				vo.setSpecs(rs.getString("specs"));
				vo.setInNum(rs.getDouble("inNum"));
				vo.setPackUnit(rs.getString("packUnit"));
				vo.setRetailPrice(rs.getDouble("retailPrice"));
				vo.setPurchasePrice(rs.getDouble("purchasePrice"));
				vo.setPurchaseCost(rs.getDouble("purchaseCost"));
				vo.setCompanyCode(rs.getString("companyCode"));
				vo.setBatchNo(rs.getString("batchNo"));
				vo.setValidDate(rs.getDate("validDate"));
				vo.setInvoiceNo(rs.getString("invoiceNo"));
				vo.setDeliveryNo(rs.getString("deliveryNo"));
				return vo;
		}});
		return voList;
	}
	@Override
	public Map<String, String> getCompanyName() throws Exception{
		String hql="FROM DrugSupplycompany d where d.stop_flg = 0 and d.del_flg = 0";
		Map<String,String> map = new HashMap<String, String>();
		List<DrugSupplycompany> companyList=this.find(hql, null);
		if(companyList!=null && companyList.size()>0){
			for (int i = 0; i < companyList.size(); i++) {
				map.put(companyList.get(i).getId(), companyList.get(i).getCompanyName());
			}
			return map;
		}	
		return null;
	}
}
