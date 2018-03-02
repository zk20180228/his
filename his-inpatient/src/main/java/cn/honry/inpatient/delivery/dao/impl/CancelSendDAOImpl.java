package cn.honry.inpatient.delivery.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.DrugApplyout;
import cn.honry.base.bean.model.DrugApplyoutNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inpatient.delivery.dao.CancelSendDAO;
import cn.honry.inpatient.delivery.dao.DeliveryNowDAO;
import cn.honry.utils.DateUtils;
import cn.honry.utils.ShiroSessionUtils;
/**
 * 取消发送数据层
 * @author  lyy
 * @createDate： 2015年12月30日 下午5:37:34 
 * @modifier lyy
 * @modifyDate：2015年12月30日 下午5:37:34  
 * @modifyRmk：  
 * @version 1.0
 */
@Repository(value="cancelSendDAO")
@SuppressWarnings({"all"})
public class CancelSendDAOImpl extends HibernateEntityDao<DrugApplyout> implements CancelSendDAO {
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private DeliveryNowDAO deliveryNowDAO;
	@Override
	public List<DrugApplyout> getPage(DrugApplyout drugApplyout, String page, String rows) throws Exception{
		String hql="From DrugApplyout d where d.del_flg=0";
		List<DrugApplyout> applyList=super.findByObjectProperty(hql, null);
		if(applyList != null && applyList.size() > 0){
			return applyList;
		}
		return new ArrayList<DrugApplyout>();
	}

	@Override
	public int getTotal(DrugApplyout drugApplyout) {
		return 0;
	}
	@Override
	public List<SysDepartment> queryDept() throws Exception{
		String hql="select t.dept_code,t.dept_name,t.dept_pinyin,t.dept_wb,t.dept_inputcode from t_department t where t.dept_type ='P'";
		List<SysDepartment> sysDepartment = jdbcTemplate.query(hql, new RowMapper<SysDepartment>(){
			@Override
			public SysDepartment mapRow(ResultSet rs, int i) throws SQLException {
				SysDepartment vo = new SysDepartment();
				vo.setDeptCode(rs.getString("dept_code"));
				vo.setDeptName(rs.getString("dept_name"));
				vo.setDeptPinyin(rs.getString("dept_pinyin"));
				vo.setDeptWb(rs.getString("dept_wb"));
				vo.setDeptInputcode(rs.getString("dept_inputcode"));
				return vo;
			}
		});
		
		return sysDepartment;
	}
	@Override
	public String enitUpdate(String[] id,String userId) throws Exception{
		String user=ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		Date date=new Date();
		StringBuffer sb=new StringBuffer();
		sb.append("update t_drug_applyout_now t set t.apply_state='0' ,t.cancel_empl=? ,  t.cancel_date=?,")
		.append("t.druged_bill=? ,t.UPDATEUSER = ?, t.UPDATETIME = ?")
		.append(" where t.id = ?");
		int c=0;
		for(int i=0;i<id.length;i++){
			Object args[] = new Object[]{userId,date,null,user,date,id[i]}; 
			c=jdbcTemplate.update(sb.toString(),args);  
		}
		if(c>0){
			return "ok";
		}else{
			return "error";
		}
	}

	@Override
	public List<DrugApplyoutNow> getPageDrugApply(DrugApplyoutNow entity, String page, String rows,Integer parameter,String deptId) throws Exception {
		String s = DateUtils.formatDateY_M_D_H_M_S(DateUtils.addDay(new Date(), - parameter));
		String s1 = DateUtils.formatDateY_M_D_H_M_S(DateUtils.addDay(new Date(), 1));
		String hql=this.joint(entity,parameter,deptId);
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ss", s);
		paramMap.put("ss1", s1);
		paramMap.put("deptId", deptId);
		if(entity!=null){
			if(StringUtils.isNotBlank(entity.getDrugDeptCode())){
				paramMap.put("drugDeptCode", entity.getDrugDeptCode());
			}
			if(StringUtils.isNotBlank(entity.getDrugedBill())){
				paramMap.put("drugedBill", "%"+entity.getDrugedBill()+"%");
			}
			if(entity.getApplyDate() != null){
				String STime = DateUtils.formatDateY_M_D_H_M_S(entity.getApplyDate());
				paramMap.put("STime", STime);
			}
			if(entity.getApplyEnd() != null){
				String ETime = DateUtils.formatDateY_M_D_H_M_S(entity.getApplyEnd());
				paramMap.put("ETime", ETime);
			}
		}
		List<DrugApplyoutNow> drugApplyoutNow = namedParameterJdbcTemplate.query(hql, paramMap,new RowMapper<DrugApplyoutNow>(){
			@Override
			public DrugApplyoutNow mapRow(ResultSet rs, int i) throws SQLException {
				DrugApplyoutNow vo = new DrugApplyoutNow();
				vo.setId(rs.getString("id"));
				vo.setDeptName(rs.getString("dept_name"));
				vo.setDrugedBill(rs.getString("druged_bill"));
				vo.setTradeName(rs.getString("trade_name"));
				vo.setApplyNum(rs.getDouble("apply_num"));
				vo.setMinUnit(rs.getString("min_unit"));
				vo.setApplyDate(rs.getTimestamp("apply_date"));
				vo.setPatientId(rs.getString("patient_id"));	
				vo.setApplyState(rs.getInt("apply_state"));
				return vo;
			}
		});
		
		return drugApplyoutNow;
	}

	@Override
	public int getTotalDrugApply(DrugApplyoutNow entity,Integer parameter,String deptId) throws Exception{
		String hql = joint(entity,parameter,deptId);
		String s = DateUtils.formatDateY_M_D_H_M_S(DateUtils.addDay(new Date(), - parameter));
		String s1 = DateUtils.formatDateY_M_D_H_M_S(DateUtils.addDay(new Date(), 1));
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ss", s);
		paramMap.put("ss1", s1);
		paramMap.put("deptId", deptId);
		String sqls="select count(druged_bill) from ("+hql+")";
		if(entity!=null){
			if(StringUtils.isNotBlank(entity.getDrugDeptCode())){
				paramMap.put("drugDeptCode", entity.getDrugDeptCode());
			}
			if(StringUtils.isNotBlank(entity.getDrugedBill())){
				paramMap.put("drugedBill", "%"+entity.getDrugedBill()+"%");
			}
			if(entity.getApplyDate() != null){
				String STime = DateUtils.formatDateY_M_D_H_M_S(entity.getApplyDate());
				paramMap.put("STime", STime);
			}
			if(entity.getApplyEnd() != null){
				String ETime = DateUtils.formatDateY_M_D_H_M_S(entity.getApplyEnd());
				paramMap.put("ETime", ETime);
			}
		}				
		return namedParameterJdbcTemplate.queryForObject(sqls, paramMap, java.lang.Integer.class);
	}
	/**
	 * 分页所掉的查询hql
	 * @author  lyy
	 * @createDate： 2016年4月19日 下午5:59:22 
	 * @modifier lyy
	 * @modifyDate：2016年4月19日 下午5:59:22
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	private String joint(DrugApplyoutNow entity,Integer parameter,String deptId) throws Exception{
		StringBuffer sb=new StringBuffer();
		sb.append("select   t.id,t.dept_name,t.druged_bill,t.trade_name,t.apply_num,t.min_unit, t.apply_date, t.patient_id,t.apply_state from t_drug_applyout_now t");
		sb.append(" where  t.del_flg=0 and  t.OP_TYPE in (4 , 5) and t.createdept=:deptId and t.APPLY_STATE not in  (0 , 1 , 2 , 7)")
		.append(" and t.print_date between to_date(:ss, 'yyyy-MM-dd hh24:mi:ss') and to_date(:ss1,'yyyy-MM-dd hh24:mi:ss')")
		.append(" and t.DEPT_CODE in (select t1.DEPT_ID from T_DEPARTMENT_CONTACT t1 where (t1.PARDEPT_ID in (select t2.ID from T_DEPARTMENT_CONTACT t2")
		.append("  where t2.REFERENCE_TYPE = '03' and t2.DEPT_ID = :deptId and t2.DEL_FLG = 0) or t1.DEPT_ID = :deptId) and t1.DEL_FLG = 0)");
		
		if(entity!=null){
			if(StringUtils.isNotBlank(entity.getDrugDeptCode())){
				sb.append(" and t.drug_dept_code=:drugDeptCode ");
			}
			if(StringUtils.isNotBlank(entity.getDrugedBill())){
				sb.append(" and t.druged_bill like :drugedBill ");
			}
			if(entity.getApplyDate() != null){
				String STime = DateUtils.formatDateY_M_D_H_M_S(entity.getApplyDate());
				sb.append(" and t.apply_date > to_date(:STime,'yyyy-MM-dd hh24:mi:ss') ");
			}
			if(entity.getApplyEnd() != null){
				String ETime = DateUtils.formatDateY_M_D_H_M_S(entity.getApplyEnd());
				sb.append("  and t.apply_date < to_date(:ETime,'yyyy-MM-dd hh24:mi:ss') ");
			}
		}
		return sb.toString();
	}
}
