package cn.honry.inpatient.apply.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.impl.CriteriaImpl;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessHospitalbed;
import cn.honry.base.bean.model.DrugApplyoutNow;
import cn.honry.base.bean.model.InpatientBedinfoNow;
import cn.honry.base.bean.model.InpatientCancelitemNow;
import cn.honry.base.bean.model.InpatientFeeInfoNow;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientItemListNow;
import cn.honry.base.bean.model.InpatientMedicineListNow;
import cn.honry.base.bean.model.InpatientStoMsgNow;
import cn.honry.base.bean.model.MatBaseinfo;
import cn.honry.base.bean.model.MatOutput;
import cn.honry.base.bean.model.MatStockdetail;
import cn.honry.base.bean.model.MatUndrugCompare;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inpatient.apply.dao.DrugApplyDAO;
import cn.honry.inpatient.apply.vo.ApplyVo;
import cn.honry.utils.ShiroSessionUtils;
/**
 * 申请退费数据库层
 * @author  lyy
 * @createDate： 2016年1月6日 下午2:19:38 
 * @modifier lyy
 * @modifyDate：2016年1月6日 下午2:19:38  
 * @modifyRmk：  
 * @version 1.0
 */
@Repository("drugApplyDao")
@SuppressWarnings({ "all" })
public class DrugApplyDAOImpl extends HibernateEntityDao<InpatientCancelitemNow> implements DrugApplyDAO {
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Resource
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 登录科室下的患者树
	 * @author  lyy
	 * @createDate： 2016年1月6日 下午2:36:54 
	 * @modifier lyy
	 * @modifyDate：2016年1月6日 下午2:36:54  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<InpatientInfoNow> treeQuery(String deptId) throws Exception{
		SysDepartment dept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		String deptCode =null;
		if(dept != null){
			deptCode = dept.getDeptCode();
		}
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("select t.dept_code,t.dept_type from t_department t where t.dept_id= :deptId");
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("deptId", deptCode);
		SysDepartment department = namedParameterJdbcTemplate.queryForObject(sBuilder.toString(), paramMap, new RowMapper<SysDepartment>(){
			@Override
			public SysDepartment mapRow(ResultSet rs, int i) throws SQLException {
				SysDepartment vo = new SysDepartment();
				vo.setDeptCode(rs.getString("dept_code"));
				vo.setDeptType(rs.getString("dept_type"));
				return vo;
			}
		});
		List<InpatientInfoNow> list = new ArrayList<InpatientInfoNow>();
		String deptType = null;
		String sdeptCode = null;
		if(department != null){
			deptType = department.getDeptType();
			sdeptCode = department.getDeptCode();
		}
		/***
		 * 判断部门类型是否为病区（护士站）N
		 * 若为病区，查找病区下所有科室的患者
		 * 若为科室，查找当前登录科室下患者
		 */
		if("N".equals(deptType)){
				sBuilder.delete(0, sBuilder.length());
				/**R-住院登记  I-病房接诊 B-出院登记 O-出院结算 P-预约出院,N-无费退院,C-婴儿封账*/
				sBuilder.append(" select * from t_inpatient_info_now t where t.in_state in ('R','I') and t.nurse_cell_code=:nurseCellCode");
				
				RowMapper<InpatientInfoNow> rm = new BeanPropertyRowMapper<InpatientInfoNow>(InpatientInfoNow.class);
				List<InpatientInfoNow> list1 = jdbcTemplate.query(sBuilder.toString(), rm,department.getDeptCode());
				list.addAll(list1);
			return list;
		}else{
			sBuilder.delete(0, sBuilder.length());
			sBuilder.append(" select * from t_inpatient_info_now t where t.in_state in ('R','I') and t.DEPT_CODE=:nurseCellCode");
			RowMapper<InpatientInfoNow> rm = new BeanPropertyRowMapper<InpatientInfoNow>(InpatientInfoNow.class);
			List<InpatientInfoNow> lists = jdbcTemplate.query(sBuilder.toString(), rm,sdeptCode);
			return lists;
		}
	}
	/***
	 * 根据病历号，查询患者住院流水号
	 * @Title: findInpatientNo 
	 * @author  WFJ
	 * @createDate ：2016年4月21日
	 * @param medicalrecordId
	 * @return String
	 * @version 1.0
	 */
	@Override
	public String findInpatientNo(String medicalrecordId) {
		String hql = "from InpatientInfoNow t where  "
		+"  t.medicalrecordId='"+medicalrecordId+"'";
		List<InpatientInfoNow> info = super.find(hql, null);
		return info == null ?"":info.get(0).getInpatientNo();
	}
	
	/***
	 * 查询药品列表信息的条件拼接
	 * @Title: queryDrugApply 
	 * @author  WFJ
	 * @createDate ：2016年4月21日
	 * @param entity
	 * 			条件的封装类：vo
	 * @return String
	 * 			拼接完成的sql查询语句
	 * @version 1.0
	 */
	public String queryDrugApply(ApplyVo entity) {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("select t.id as id,");
		//药品code  
		sBuilder.append(" t.drug_code as drugCode,");
		//药品名称
		sBuilder.append(" t.drug_name as drugName,");
		//规格
		sBuilder.append(" t.specs as specs,");
		//费用代码
		sBuilder.append(" t.fee_code as costName,");
		//单价
		sBuilder.append(" t.unit_price as price,");
		//可退数量
		sBuilder.append(" t.noback_num as nobackNum,");
		//当前单位
		sBuilder.append(" t.current_unit as unit,");
		//费用金额
		sBuilder.append(" t.tot_cost as moneySum,");
		//执行科室代码
		sBuilder.append(" t.execute_deptcode as executeDept,");
		//执行科室名称
		sBuilder.append(" t.execute_deptName as executeDeptName,");
		//开立医师代码
		sBuilder.append(" t.recipe_doccode as recipeDoc,");
		//开立医师名称
		sBuilder.append(" t.RECIPE_DOCNAME as recipeDocName,");
		//记账时间
		sBuilder.append(" t.EXEC_DATE as execDate,");
		//发药状态
		sBuilder.append(" t.senddrug_flag as senddrugFlag,");
		//编码
		sBuilder.append(" i.drug_nameinputcode as code,");
		//医嘱流水号
		sBuilder.append(" t.mo_order as moOrder,");
		//医嘱执行单号
		sBuilder.append(" t.mo_exec_sqn as moExecSqn,");
		//处方号
		sBuilder.append(" t.recipe_no as recipeNo,");
		//处方内流水号
		sBuilder.append(" t.sequence_no as sequenceNo,");
		//包装数量
		sBuilder.append(" t.pack_qty as packQty,");
		//拼音码
		sBuilder.append(" i.drug_namepinyin as pinyin,");
		//开方科室
		sBuilder.append(" t.recipe_deptcode as recipeDept,");
		//开方科室名称
		sBuilder.append(" t.RECIPE_DEPTNAME as recipeDeptName, ");
		//手术序号
		sBuilder.append(" t.operation_id as operationId,EXT_FLAG3 as extFlag3 ");
		sBuilder.append(" from t_inpatient_medicinelist_now t,t_drug_info i");
		sBuilder.append(" where t.del_flg=0 and t.stop_flg=0 and i.stop_flg=0 and i.del_flg=0");
		sBuilder.append(" and t.drug_code=i.drug_code");
		//住院流水号
		sBuilder.append(" and t.inpatient_no = :inpatientNo");
		//药品状态限制：批费或已摆药
		sBuilder.append(" and t.senddrug_flag in (1,2)");
		sBuilder.append(" and t.trans_type = 1");
		sBuilder.append(" and t.EXT_FLAG1 != 1");
		
		if(StringUtils.isNotBlank(entity.getObjName()))
			sBuilder.append(" and t.drug_name like :objName");
		if(StringUtils.isNotBlank(entity.getFirstDate())){
			sBuilder.append(" and t.EXEC_DATE>=to_date(:firstDate,'yyyy-MM-dd hh24:mi:ss') ");
		}
		if(StringUtils.isNotBlank(entity.getEndDate())){
			sBuilder.append(" and t.EXEC_DATE<=to_date(:endDate,'yyyy-MM-dd hh24:mi:ss') ");
		}
		//按照记账时间倒叙排列
		return sBuilder.toString();
	}
	
	
	@Override
	public List<ApplyVo> getPage(ApplyVo entity) {
		String sql = queryDrugApply(entity);
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("inpatientNo", entity.getInpatientNo());
		if(StringUtils.isNotBlank(entity.getObjName()))
			paraMap.put("objName", "%"+entity.getObjName()+"%");
		if(StringUtils.isNotBlank(entity.getFirstDate())){
			paraMap.put("firstDate",entity.getFirstDate());
		}
		if(StringUtils.isNotBlank(entity.getEndDate())){
			paraMap.put("endDate",entity.getEndDate());
		}
		List<ApplyVo> voList =namedParameterJdbcTemplate.query(sql.toString(), paraMap,new RowMapper<ApplyVo>(){
			@Override
			public ApplyVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				ApplyVo vo = new ApplyVo();
				vo.setId(rs.getString("id"));
				vo.setDrugCode(rs.getString("drugCode"));
				vo.setDrugName(rs.getString("drugName"));
				vo.setSpecs(rs.getString("specs"));
				vo.setCostName(rs.getString("costName"));
				vo.setPrice(rs.getDouble("price"));
				vo.setNobackNum(rs.getInt("nobackNum"));
				vo.setUnit(rs.getString("unit"));
				vo.setMoneySum(rs.getDouble("moneySum"));
				vo.setExecuteDept(rs.getString("executeDept"));
				vo.setExecuteDeptName(rs.getString("executeDeptName"));
				vo.setRecipeDocName(rs.getString("recipeDoc"));
				vo.setRecipeDocName(rs.getString("recipeDocName"));
				vo.setExecDate(rs.getTimestamp("execDate"));
				vo.setSenddrugFlag(rs.getInt("senddrugFlag"));
				vo.setCode(rs.getString("code"));
				vo.setMoOrder(rs.getString("moOrder"));
				vo.setMoExecSqn(rs.getString("moExecSqn"));
				vo.setRecipeNo(rs.getString("recipeNo"));
				vo.setSequenceNo(rs.getInt("sequenceNo"));
				vo.setPackQty(rs.getInt("packQty"));
				vo.setPinyin(rs.getString("pinyin"));
				vo.setRecipeDept(rs.getString("recipeDept"));
				vo.setRecipeDeptName(rs.getString("recipeDeptName"));
				vo.setOperationId(rs.getString("operationId"));
				vo.setExtFlag3(rs.getString("extFlag3"));
				return vo;
		}});
		return voList ;
	}
	
	@Override
	public List<ApplyVo> getPageBack(ApplyVo entity) {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("select t.apply_no as applyNo,");
		//申请id   项目编码
		sBuffer.append(" t.item_code as drugCode,");
		//项目名称
		sBuffer.append(" t.item_name as drugName,");
		//药品规格
		sBuffer.append(" t.specs as specs,");
		//零售价
		sBuffer.append(" t.sale_price as price,");
		//申请退药数量
		sBuffer.append(" t.quantity as quantity,");
		//单位（计价单位）
		sBuffer.append(" t.price_unit as unit,");
		//是否发药状态
		sBuffer.append(" m.senddrug_flag as senddrugFlag,");
		//处方号
		sBuffer.append(" t.recipe_no as recipeNo,");
		//处方内流水号
		sBuffer.append(" t.sequence_no as sequenceNo,"); 
		//手术序号
		sBuffer.append(" m.operation_id as operationId"); 
		sBuffer.append(" from t_inpatient_cancelitem_now t join  t_inpatient_medicinelist_now m on t.recipe_no=m.recipe_no and t.sequence_no=m.sequence_no");
		sBuffer.append(" where t.stop_flg=0 and t.del_flg=0 and m.del_flg=0 and m.stop_flg=0");
		//申请的药品
		sBuffer.append(" and t.drug_flag=1");
		//退费标识：未确认
		sBuffer.append(" and t.charge_flag=0");
		//住院流水
		sBuffer.append(" and t.inpatient_no=:inpatientNo");
		sBuffer.append(" and t.inpatient_no=m.inpatient_no");
		//条件查询
		if(StringUtils.isNotBlank(entity.getObjName()))
			sBuffer.append(" and t.item_name like :objName");
	
		if(StringUtils.isNotBlank(entity.getFirstDate())){
			sBuffer.append(" and t.EXEC_DATE>=to_date(:firstDate,'yyyy-MM-dd hh24:mi:ss') ");
		}
		if(StringUtils.isNotBlank(entity.getEndDate())){
			sBuffer.append(" and t.EXEC_DATE<=to_date(:endDate,'yyyy-MM-dd hh24:mi:ss') ");
		}
		
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("inpatientNo", entity.getInpatientNo());
		if(StringUtils.isNotBlank(entity.getObjName()))
			paraMap.put("objName", "%"+entity.getObjName()+"%");
		if(StringUtils.isNotBlank(entity.getFirstDate())){
			paraMap.put("firstDate",entity.getFirstDate());
		}
		if(StringUtils.isNotBlank(entity.getEndDate())){
			paraMap.put("endDate",entity.getEndDate());
		}
		List<ApplyVo> voList =namedParameterJdbcTemplate.query(sBuffer.toString(), paraMap,new RowMapper<ApplyVo>(){
			@Override
			public ApplyVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				ApplyVo vo = new ApplyVo();
				vo.setApplyNo(rs.getString("applyNo"));
				vo.setDrugCode(rs.getString("drugCode"));
				vo.setDrugName(rs.getString("drugName"));
				vo.setSpecs(rs.getString("specs"));
				vo.setPrice(rs.getDouble("price"));
				vo.setQuantity(rs.getInt("quantity"));
				vo.setUnit(rs.getString("unit"));
				vo.setSenddrugFlag(rs.getInt("senddrugFlag"));
				vo.setRecipeNo(rs.getString("recipeNo"));
				vo.setSequenceNo(rs.getInt("sequenceNo"));
				vo.setOperationId(rs.getString("operationId"));
				return vo;
		}});
		return voList ;
	}
	
	@Override
	public List<ApplyVo> getPageNotDrug(ApplyVo entity) {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("select t.id as id,");
		//项目编码
		sBuffer.append(" t.item_code as objCode,");
		//项目名称
		sBuffer.append(" t.item_name as objName,");
		//费用代码
		sBuffer.append(" t.fee_code as costName,");
		//单价
		sBuffer.append(" t.unit_price as price,");
		//可退数量
		sBuffer.append(" t.noback_num as nobackNum,");
		//单位
		sBuffer.append(" t.current_unit as unit,");
		//费用金额
		sBuffer.append(" t.tot_cost as moneySum,");
		//执行科室
		sBuffer.append(" t.execute_deptcode as executeDept,");
		//执行科室名称
		sBuffer.append(" t.execute_deptName as executeDeptName,");
		//开方医生
		sBuffer.append(" t.recipe_doccode as recipeDoc,");
		//开立医师名称
		sBuffer.append(" t.RECIPE_DOCNAME as recipeDocName,");
		//收费日期
		sBuffer.append(" t.EXEC_DATE as execDate,");
		//发送标识
		sBuffer.append(" t.send_flag as senddrugFlag,");
		//编码（自定义码）
		sBuffer.append(" u.undrug_code as code,");
		//医嘱流水号
		sBuffer.append(" t.mo_order as moOrder,");
		//医嘱执行号
		sBuffer.append(" t.mo_exec_sqn as moExecSqn,");
		//处方号
		sBuffer.append(" t.recipe_no as recipeNo,");
		//处方流水号
		sBuffer.append(" t.sequence_no as sequenceNo,");
		//拼音码
		sBuffer.append(" u.undrug_pinyin as pinyin,");
		//开方科室
		sBuffer.append(" t.recipe_deptcode as recipeDept,");
		//开方科室名称
		sBuffer.append(" t.RECIPE_DEPTNAME as recipeDeptName,");
		//出库流水号
		sBuffer.append(" t.update_sequenceno as updateSequenceno,");
		//库存序号
		//sBuffer.append(" ");
		//标识
		sBuffer.append(" t.item_flag as itemFlag,");
		//标识
		sBuffer.append(" t.operation_id as operationId");
		sBuffer.append(" from t_inpatient_itemlist_now t,t_drug_undrug u");
		sBuffer.append(" where t.del_flg=0 and t.stop_flg=0 and u.del_flg=0 and u.stop_flg=0");
		sBuffer.append(" and t.item_code= u.undrug_code");
		sBuffer.append(" and t.inpatient_no=:inpatientNo");
		//状态限制
		sBuffer.append(" and t.send_flag in (1,2)");
		sBuffer.append(" and t.trans_type = 1");
		sBuffer.append(" and t.EXT_FLAG1 = 0");
		
		if(StringUtils.isNotBlank(entity.getObjName())){
			sBuffer.append(" and t.item_name like :objName");
		}
			
		if(StringUtils.isNotBlank(entity.getFirstDate())){
			sBuffer.append(" and to_char(t.EXEC_DATE,'yyyy-mm-dd') >='"+entity.getFirstDate()+"'");
		}
		if(StringUtils.isNotBlank(entity.getEndDate())){
			sBuffer.append(" and to_char(t.EXEC_DATE,'yyyy-mm-dd') <='"+entity.getEndDate()+"'");
		}
		
		sBuffer.append(" order by t.EXEC_DATE desc");
		
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("inpatientNo", entity.getInpatientNo());
		if(StringUtils.isNotBlank(entity.getObjName()))
			paraMap.put("objName", "%"+entity.getObjName()+"%");
		if(StringUtils.isNotBlank(entity.getFirstDate())){
			paraMap.put("firstDate",entity.getFirstDate());
		}
		if(StringUtils.isNotBlank(entity.getEndDate())){
			paraMap.put("endDate",entity.getEndDate());
		}
		List<ApplyVo> voList =namedParameterJdbcTemplate.query(sBuffer.toString(), paraMap,new RowMapper<ApplyVo>(){
			@Override
			public ApplyVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				ApplyVo vo = new ApplyVo();
				vo.setId(rs.getString("id"));
				vo.setObjCode(rs.getString("objCode"));
				vo.setObjName(rs.getString("objName"));
				vo.setCostName(rs.getString("costName"));
				vo.setPrice(rs.getDouble("price"));
				vo.setNobackNum(rs.getInt("nobackNum"));
				vo.setUnit(rs.getString("unit"));
				vo.setMoneySum(rs.getDouble("moneySum"));
				vo.setExecuteDept(rs.getString("executeDept"));
				vo.setExecuteDeptName(rs.getString("executeDeptName"));
				vo.setRecipeDocName(rs.getString("recipeDoc"));
				vo.setRecipeDocName(rs.getString("recipeDocName"));
				vo.setExecDate(rs.getTimestamp("execDate"));
				vo.setSenddrugFlag(rs.getInt("senddrugFlag"));
				vo.setCode(rs.getString("code"));
				vo.setMoOrder(rs.getString("moOrder"));
				vo.setMoExecSqn(rs.getString("moExecSqn"));
				vo.setRecipeNo(rs.getString("recipeNo"));
				vo.setSequenceNo(rs.getInt("sequenceNo"));
				vo.setPinyin(rs.getString("pinyin"));
				vo.setRecipeDept(rs.getString("recipeDept"));
				vo.setRecipeDeptName(rs.getString("recipeDeptName"));
				vo.setUpdateSequenceno(rs.getString("updateSequenceno"));
				vo.setItemFlag(rs.getInt("itemFlag"));
				vo.setOperationId(rs.getString("operationId"));
				return vo;
		}});
		return voList ;
	}
	
	@Override
	public String findStockNo(String stockCode) {
		String sql = "select t.stock_no from t_mat_stockdetail t where t.del_flg=0 and t.stop_flg=0 and t.stock_code=:stockCode";
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("stockCode", stockCode);
		String stockNo=namedParameterJdbcTemplate.queryForObject(sql, paraMap, java.lang.String.class);
		return stockNo;
	}
	
	@Override
	public List<ApplyVo> getPageDrugBack(ApplyVo entity) {
		StringBuffer sBuffer = new StringBuffer();
		//id
		sBuffer.append("select t.apply_no as applyNo,");
		//项目code
		sBuffer.append(" t.item_code as objCode,");
		//项目名称
		sBuffer.append(" t.item_name as objName,");
		//最小费用代码
		sBuffer.append(" i.fee_code as costName,");
		//价格
		sBuffer.append(" t.sale_price as price,");
		//退费数量
		sBuffer.append(" t.quantity as quantity,");
		//单位
		sBuffer.append(" t.price_unit as unit,");
		//执行科室
		sBuffer.append(" t.exec_dpcd as executeDept,");
		//执行科室名称
		sBuffer.append(" t.EXEC_DPCD_NAME as executeDeptName,");
		//是否执行
		sBuffer.append(" i.send_flag as senddrugFlag,");
		//处方号
		sBuffer.append(" t.recipe_no as recipeNo,");
		//处方内部流水号
		sBuffer.append(" t.sequence_no as sequenceNo,");
		//手术序号
		sBuffer.append(" i.operation_id as operationId");
		sBuffer.append(" from t_inpatient_cancelitem_now t join  t_inpatient_itemlist_now i on t.recipe_no=i.recipe_no and t.sequence_no=i.sequence_no");
		sBuffer.append(" where t.del_flg=0 and t.stop_flg=0 and i.del_flg=0 and t.stop_flg=0 ");
		//申请的非药品
		sBuffer.append(" and t.drug_flag=2");
		//退费标识：未确认
		sBuffer.append(" and t.charge_flag=0");
		sBuffer.append(" and t.inpatient_no=i.inpatient_no");
		sBuffer.append(" and t.inpatient_no=:inpatientNo");
		
		//条件查询
		if(StringUtils.isNotBlank(entity.getObjName()))
			sBuffer.append(" and t.item_name like :objName");
	
		if(StringUtils.isNotBlank(entity.getFirstDate())){
			sBuffer.append(" and t.EXEC_DATE>=to_date(:firstDate,'yyyy-MM-dd hh24:mi:ss') ");
		}
		if(StringUtils.isNotBlank(entity.getEndDate())){
			sBuffer.append(" and t.EXEC_DATE<=to_date(:endDate,'yyyy-MM-dd hh24:mi:ss') ");
		}
		
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("inpatientNo", entity.getInpatientNo());
		if(StringUtils.isNotBlank(entity.getObjName()))
			paraMap.put("objName", "%"+entity.getObjName()+"%");
		if(StringUtils.isNotBlank(entity.getFirstDate())){
			paraMap.put("firstDate",entity.getFirstDate());
		}
		if(StringUtils.isNotBlank(entity.getEndDate())){
			paraMap.put("endDate",entity.getEndDate());
		}
		List<ApplyVo> voList =namedParameterJdbcTemplate.query(sBuffer.toString(), paraMap,new RowMapper<ApplyVo>(){
			@Override
			public ApplyVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				ApplyVo vo = new ApplyVo();
				vo.setApplyNo(rs.getString("applyNo"));
				vo.setObjCode(rs.getString("objCode"));
				vo.setObjName(rs.getString("objName"));
				vo.setCostName(rs.getString("costName"));
				vo.setPrice(rs.getDouble("price"));
				vo.setQuantity(rs.getInt("quantity"));
				vo.setUnit(rs.getString("unit"));
				vo.setSenddrugFlag(rs.getInt("senddrugFlag"));
				vo.setRecipeNo(rs.getString("recipeNo"));
				vo.setSequenceNo(rs.getInt("sequenceNo"));
				vo.setExecuteDeptName(rs.getString("executeDeptName"));
				vo.setExecuteDept(rs.getString("executeDept"));
				vo.setOperationId(rs.getString("operationId"));
				return vo;
		}});
		return voList ;
	}
	
	@Override
	public InpatientMedicineListNow getChildById(String id) {
		InpatientMedicineListNow model = (InpatientMedicineListNow) this.getSession().get(InpatientMedicineListNow.class, id);
		return model;
	}
	
	@Override
	public DrugApplyoutNow obtainApplyout(String recipeNo, Integer sequenceNo) {
		StringBuffer sb=new StringBuffer();
	    sb.append("select * from T_DRUG_APPLYOUT_NOW t  ");
		sb.append(" where t.STOP_FLG = 0  and t.DEL_FLG = 0 and t.OP_TYPE = 4 and t.VALID_STATE = 1 and t.RECIPE_NO =:recipeNo and t.SEQUENCE_NO=:sequenceNo");
		RowMapper<DrugApplyoutNow> rm = new BeanPropertyRowMapper<DrugApplyoutNow>(DrugApplyoutNow.class);
		List<DrugApplyoutNow> drugApplyoutNowList = jdbcTemplate.query(sb.toString(), rm, recipeNo,sequenceNo);
		if(drugApplyoutNowList.size()>0){
			return drugApplyoutNowList.get(0);
		}
		return null;
	}
	
	@Override
	public DrugApplyoutNow obtainApplyoutdesc(String recipeNo, Integer sequenceNo) {
		StringBuffer sb=new StringBuffer();
		 sb.append("select * from T_DRUG_APPLYOUT_NOW t  ");
		sb.append(" where t.STOP_FLG = 0  and t.DEL_FLG = 0 and t.OP_TYPE = 4 and t.VALID_STATE = 0 and t.RECIPE_NO =:recipeNo and t.SEQUENCE_NO=:sequenceNo");
		RowMapper<DrugApplyoutNow> rm = new BeanPropertyRowMapper<DrugApplyoutNow>(DrugApplyoutNow.class);
		List<DrugApplyoutNow> drugApplyoutNowList = jdbcTemplate.query(sb.toString(), rm,recipeNo,sequenceNo);
		if(drugApplyoutNowList.size()>0){
			return drugApplyoutNowList.get(0);
		}
		return new DrugApplyoutNow();
		
	}
	
	@Override
	public void saveOrUpdate(List<DrugApplyoutNow> listApplyout) {
		for(DrugApplyoutNow model: listApplyout){
			this.getSession().saveOrUpdate(model);
		}
	}

	@Override
	public MatBaseinfo querybaseInfo(String compareId) {
		String hql="from MatBaseinfo where del_flg=0 and itemCode='"+compareId+"'";
		List<MatBaseinfo> baseInfoList=super.find(hql, null);
		if(baseInfoList!=null&&baseInfoList.size()>0){
			return baseInfoList.get(0);
		}
		return null;
	}
	
	@Override
	public MatUndrugCompare queryUndrugCompare(String itemCode) {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("select t.id,t.undrug_item_code from t_mat_undrug_compare t where t.stop_flg=0 and t.del_flg=0 and t.undrug_item_code=:itemCode");
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("itemCode", itemCode);
		List<MatUndrugCompare> compareList = namedParameterJdbcTemplate.query(sBuffer.toString(), paramMap,new RowMapper<MatUndrugCompare>(){
			@Override
			public MatUndrugCompare mapRow(ResultSet rs, int i) throws SQLException {
				MatUndrugCompare vo = new MatUndrugCompare();
				vo.setId(rs.getString("id"));
				vo.setUndrugItemCode(rs.getString("undrug_item_code"));
				return vo;
			}
		});
		if(compareList != null && compareList.size() > 0){
			return compareList.get(0);
		}
		return null;
	}
	
	@Override
	public InpatientMedicineListNow getChildByRecipe(String recipeNo, Integer sequenceNo) {
		String hql="from InpatientMedicineListNow t where t.recipeNo=? and t.sequenceNo=? and t.extFlag1=0 ";
		List<InpatientMedicineListNow> baseInfoList=super.find(hql, recipeNo,sequenceNo);
		if(baseInfoList!=null&&baseInfoList.size()>0){
			return baseInfoList.get(0);
		}
		return new InpatientMedicineListNow();
	}
	
	
	@Override
	public InpatientItemListNow getItemListById(String id) {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("select t.id,t.execute_deptcode,t.inpatient_no,t.item_code,t.recipe_no,t.sequence_no,t.update_sequenceno,t.noback_num,t.updateuser,t.updatetime ,")
			   .append("t.name,t.baby_flag,t.inhos_deptcode,t.nurse_cell_code,t.item_name,t.unit_price,t.current_unit,t.nurse_cell_name,t.execute_deptname,t.inhos_deptname  ")
			   .append(" from t_inpatient_itemlist_now t ")
			   .append(" where t.stop_flg=0 and t.del_flg=0 and  t.id=:ids");
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ids", id);
		InpatientItemListNow inpatientItemListNow = namedParameterJdbcTemplate.queryForObject(sBuffer.toString(), paramMap, new RowMapper<InpatientItemListNow>(){
			@Override
			public InpatientItemListNow mapRow(ResultSet rs, int i) throws SQLException {
				InpatientItemListNow vo = new InpatientItemListNow();
				vo.setId(rs.getString("id"));
				vo.setExecuteDeptcode(rs.getString("execute_deptcode"));
				vo.setExecuteDeptname(rs.getString("execute_deptname"));
				vo.setInpatientNo(rs.getString("inpatient_no"));
				vo.setItemCode(rs.getString("item_code"));
				vo.setItemName(rs.getString("item_name"));
				vo.setRecipeNo(rs.getString("recipe_no"));
				vo.setSequenceNo(rs.getInt("sequence_no"));
				vo.setUpdateSequenceno(rs.getString("update_sequenceno"));
				vo.setNobackNum(rs.getDouble("noback_num"));
				vo.setUpdateUser(rs.getString("updateuser"));
				vo.setUpdateTime(rs.getDate("updatetime"));
				vo.setName(rs.getString("name"));
				vo.setBabyFlag(rs.getInt("baby_flag"));
				vo.setInhosDeptcode(rs.getString("inhos_deptcode"));
				vo.setInhosDeptname(rs.getString("inhos_deptname"));
				vo.setNurseCellCode(rs.getString("nurse_cell_code"));
				vo.setNurseCellName(rs.getString("nurse_cell_name"));
				vo.setUnitPrice(rs.getDouble("unit_price"));
				vo.setCurrentUnit(rs.getString("current_unit"));
				return vo;
			}
		});
		return inpatientItemListNow;
	}
	
	
	@Override
	public MatOutput findMatOPbyNO(String outNo) {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("from MatOutput t");
		sBuffer.append(" where t.stop_flg=0 and t.del_flg=0");
		sBuffer.append(" and t.applyNo=?");

		MatOutput model = (MatOutput) this.getSession().createQuery(sBuffer.toString())
				.setParameter(0, outNo);
		return model;
	}
	
	
	@Override
	public InpatientItemListNow getItemListByRecipe(String recipeNo, Integer sequenceNo) {
		String hql="from InpatientItemListNow t where t.recipeNo=? and t.sequenceNo=? and t.extFlag1=0 ";
		List<InpatientItemListNow> baseInfoList=super.find(hql, recipeNo,sequenceNo);
		if(baseInfoList!=null&&baseInfoList.size()>0){
			return baseInfoList.get(0);
		}
		return new InpatientItemListNow();
	}

	
	@Override
	public List<InpatientCancelitemNow> findCancelitemByIds(String[] ids) {
		List<InpatientCancelitemNow> list = new ArrayList<InpatientCancelitemNow>();
		for(String id : ids){
			InpatientCancelitemNow model = (InpatientCancelitemNow) this.getSession().get(InpatientCancelitemNow.class, id);
			list.add(model);
		}
		return list;
	}

	
	@Override
	public DrugApplyoutNow findByApplyout(String recipeNo, Integer sequenceNo) {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("from DrugApplyoutNow t");
		sBuffer.append(" where t.stop_flg=0 and t.del_flg=0");
		sBuffer.append(" and t.opType = 5");
		sBuffer.append(" and t.validState = 1");
		sBuffer.append(" and t.recipeNo=?");
		sBuffer.append(" and t.sequenceNo=?");
  		
		List<DrugApplyoutNow> list = find(sBuffer.toString(), recipeNo,sequenceNo);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	
	@Override
	public DrugApplyoutNow oldApplyout(String recipeNo, Integer sequenceNo) throws Exception{
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("from DrugApplyoutNow t");
		sBuffer.append(" where t.stop_flg=0 and t.del_flg=0");
		sBuffer.append(" and t.opType = 4");
		sBuffer.append(" and t.validState = 0");
		sBuffer.append(" and t.recipeNo=?");
		sBuffer.append(" and t.sequenceNo=?");
		DrugApplyoutNow model = (DrugApplyoutNow) getSession().createQuery(sBuffer.toString())
				.setParameter(0, recipeNo)
				.setParameter(1,sequenceNo);
		return model;
	}
	
	
	
	@Override
	public List<ApplyVo> queryInpatientInfo(String medicalrecordId) throws Exception{
		//模糊查询本科室（病区）下的患者信息
		SysDepartment sys = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		String deptid=null;
		if(sys != null){
			deptid = sys.getId();
		}
		List<InpatientInfoNow> list = this.treeQuery(deptid);
		List<ApplyVo> listVo = new ArrayList<ApplyVo>();
		for(InpatientInfoNow model : list){
			if(StringUtils.isNotEmpty(model.getMedicalrecordId())){
				int index = model.getMedicalrecordId().indexOf(medicalrecordId);
				if(model.getMedicalrecordId() != null && model.getMedicalrecordId().equals(medicalrecordId)){
					ApplyVo vo = new ApplyVo();
					vo.setInpatientNo(model.getInpatientNo());
					vo.setMedicalrecordId(model.getMedicalrecordId());
					vo.setName(model.getPatientName());
					vo.setReportSex(model.getReportSex());
					vo.setReportSexName(model.getReportSexName());
					vo.setCertificatesNo(model.getCertificatesNo());
					vo.setPactCode(model.getPactCode());
					vo.setInhosDept(model.getDeptCode());
					vo.setInhosDeptName(model.getDeptName());
					vo.setBedId(model.getBedId());
					vo.setBedName(model.getBedName());
					listVo.add(vo);
				}
			}
		}
		return listVo;
	}
	
	
/*-----------------------------------------------------	渲染  -------------------------------------------------------------------------------------*/
	
	/**
	 * 合同单位下拉框
	 * @author  lyy
	 * @createDate： 2016年1月11日 下午8:05:31 
	 * @modifier lyy
	 * @modifyDate：2016年1月11日 下午8:05:31  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<BusinessContractunit> likeContractunit() {
		String hql="from BusinessContractunit where del_flg=0";
		List<BusinessContractunit> contunitList=super.find(hql, null);
		if(contunitList != null && contunitList.size() > 0){
			return contunitList;
		}
		return new ArrayList<BusinessContractunit>();
	}
	
	
	@Override
	public InpatientBedinfoNow getBedinfo(String bedinfoId) {
		InpatientBedinfoNow model = (InpatientBedinfoNow) this.getSession().get(InpatientBedinfoNow.class, bedinfoId);
		return model;
	}
	
	@Override
	public BusinessHospitalbed getBed(String bedId) {
		String hql="from BusinessHospitalbed b where b.del_flg=0 and b.id='"+bedId+"'";
		List<BusinessHospitalbed> bed = super.find(hql, null);
		if(bed != null && bed.size() > 0){
			return bed.get(0);
		}
		return null;
	}
	
	/***
	 * 获取摆药通知实体
	 * @Title: getMsg 
	 * @author  WFJ
	 * @createDate ：2016年5月19日
	 * @param billclassCode 摆药单分类代码
	 * @param sendType 摆药类型  1集中发送，2临时发送，3全部 
	 * @param sendFlag 摆药标记0-通知1-已摆
	 * @param medDeptCode 取药科室
	 * @return InpatientStoMsg 摆药通知实体
	 * @version 1.0
	 */
	@Override
	public InpatientStoMsgNow getMsg(String billclassCode, String sendType, String sendFlag, String medDeptCode) {
		String hql = "from InpatientStoMsgNow t where del_flg=0  "
				+ "and t.billclassCode = ? and t.sendType = ? and t.sendFlag = ? and t.medDeptCode = ?";
		
		List<InpatientStoMsgNow> list = this.find(hql, billclassCode,sendType,sendFlag,medDeptCode);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	
	/***
	 * 根据摆药通知类型，查询出库申请表中相关类型的通知数据
	 * @Title: getApplyout 
	 * @author  WFJ
	 * @createDate ：2016年5月19日
	 * @param billclassCode 摆药单分类代码
	 * @param sendType 摆药类型  1集中发送，2临时发送，3全部 
	 * @param sendFlag 摆药标记0-通知1-已摆
	 * @param medDeptCode 取药科室
	 * @return DrugApplyout 摆药通知实体
	 * @version 1.0
	 */
	@Override
	public List<DrugApplyoutNow> getApplyout(String billclassCode, String sendType, String medDeptCode) {
		StringBuffer sb=new StringBuffer();
		sb.append("select t.apply_number,t.billclass_code from t_drug_applyout_now t where t.del_flg=0 and t.stop_flg=0 and t.druged_bill is not null and t.valid_state=1")
		.append("and t.billclass_code =:billclassCode and t.send_type=:sendType and t.DRUG_DEPT_CODE=:medDeptCode");
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("billclassCode", billclassCode);
		paramMap.put("sendType", sendType);
		paramMap.put("medDeptCode", medDeptCode);
		 List<DrugApplyoutNow> drugApplyoutNowList = namedParameterJdbcTemplate.query(sb.toString(), paramMap,new RowMapper<DrugApplyoutNow>(){
			@Override
			public DrugApplyoutNow mapRow(ResultSet rs, int i) throws SQLException {
				DrugApplyoutNow vo = new DrugApplyoutNow();
				vo.setApplyNumber(rs.getInt("apply_number"));
				vo.setBillclassCode(rs.getString("billclass_code"));
				return vo;
			}
		});
		
		return drugApplyoutNowList;
		
	}
/*-----------------------------------------------------		以下是未知领域  		--------------------------------------------------------------------------------------*/		
	
	
	
	/**
	 * 药品的总数
	 * @author  lyy
	 * @createDate： 2016年1月7日 下午3:43:25 
	 * @modifier lyy
	 * @modifyDate：2016年1月21日 下午3:43:25  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public int getTotal(ApplyVo vo) {
		String sql = queryDrugApply(vo);
		List list = this.getSession().createSQLQuery(sql).list();
		return list == null ? 0:list.size(); 
	}
	
	/**
	 * 非药品的总数
	 * @author  lyy
	 * @createDate： 2016年1月8日 下午5:06:03 
	 * @modifier lyy,liujl
	 * @modifyDate：2016年1月21日 下午5:06:03  2016-6-6 上午09:56:35  
	 * @modifyRmk：  修改获取记录总条数方法
	 * @version 1.0
	 */
	@Override
	public int getTotalNotDrug(ApplyVo vo) {
		StringBuffer sql=new StringBuffer();
		sql.append("select i.inpatient_no as  inpatientNo,"  //住院流水号
				+ "i.id as id,"     //非药品明细表id
				+ "i.name as name,"  //患者名称
				+ "i.item_name as objName,"   //项目名称
				+ "i.fee_code  as costName,"   //费用名称
				+ "i.unit_price as price,"   //价格
				+ "i.noback_num as nobackNum,"  //可退数量
				+ "i.current_unit as unit,"   //单位
				+ "(i.tot_cost-i.own_cost-i.pay_cost-i.pub_cost-i.eco_cost) as moneySum,"   //金额
				+ "i.execute_deptcode as executeDept,"  //执行科室
				+ "i.exec_opercode  as execOpercode,"   //执行人 
				+ "i.exec_date  as exeDate,"  //执行时间
				+ "i.recipe_doccode as recipeDoc,"   //开立医师
				+ "i.EXEC_DATE as execDate,"   //记账日期
				+ "i.fee_opercode as execPerson,"   //记账人
				+ "i.item_code as objCode,"   //项目编号
				+ "i.mo_order  as moOrder,"  //医嘱流水号
				+ "i.mo_exec_sqn as moExecSqn,"  //医嘱执行单流水号
				+ "i.recipe_no   as recipeNo,"  //处方号
				+ "i.SEQUENCE_NO as sequenceNo,"  //处方流水号
				+ "u.undrug_pinyin as pinyin," //拼音码  
				+ "i.recipe_deptcode as recipeDept," //开立科室
				+ "i.pact_code as  pactCode,"  //合同单位
				+ "i.sendmat_sequence as sendmatSequence,"  //出库单
				+ "i.invoice_no as  invoiceNo,"   //标识
				+ "info.medicalrecord_id medicalrecordId,"   //病历号
				+ "i.inhos_deptcode as inhosDept,"   //住院科室 
				+ "info.bedinfo_id  bedId "    //床号    
				+ "from t_inpatient_itemlist_now i "
				+ "join t_drug_undrug u on i.item_code = u.undrug_id "
				+ "join t_inpatient_info_now info on info.inpatient_no=i.inpatient_no "
				+ "where i.del_flg=0 and u.del_flg=0 and i.name ='"+vo.getName()+"'");
		return super.getSqlTotal(sql.toString());
	}


	/**
	 * 住院科室下拉框
	 * @author  lyy
	 * @createDate： 2016年1月11日 下午8:05:31 
	 * @modifier lyy
	 * @modifyDate：2016年1月11日 下午8:05:31  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<SysDepartment> likeDept() {
		String hql="from SysDepartment where del_flg=0 ";
		List<SysDepartment> deptList=super.find(hql, null);
		if(deptList != null && deptList.size() > 0){
			return deptList;
		}
		return new ArrayList<SysDepartment>();
	}
	@Override
	public List<InpatientMedicineListNow> getChildByIds(String ids) {
		String hql="FROM InpatientMedicineListNow d WHERE d.del_flg = 0";
		if(ids.contains(",")){
			String[] id = ids.split(",");
		}
		List<InpatientMedicineListNow> medList = super.find(hql, null);
		if(medList!=null&&medList.size()>0){
			return medList;
		}
		return new ArrayList<InpatientMedicineListNow>();
	}
	@Override
	public String updateMediceList(int amount,String id) {
		int b = getSession().createQuery("UPDATE InpatientMedicineListNow m SET m.nobackNum = '"+amount+"',m.updateTime = ?  WHERE m.id = '"+id+"'").setTimestamp(0, new Date()).executeUpdate();
		if(b>0){
			return "ok";
		}else{
			return "error";
		}
	}
	@Override
	public String updateApplyOutState(String ids) {
		ids=ids.replaceAll(",", "','");
		int b = getSession().createQuery("UPDATE DrugApplyoutNow d SET d.applyState='3',d.cancelDate = ?  WHERE d.drugCode IN ('"+ids+"')").setTimestamp(0, new Date()).executeUpdate();
		if(b>0){
			return "ok";
		}else{
			return "error";
		}
	}
	@Override
	public String updateStorage(int num,String drug, String dept) {
		int a = getSession().createQuery("UPDATE DrugStorage s SET s.preoutSum= '"+num+"',s.updateTime = ?  WHERE s.drugId = '"+drug+"' and s.storageDeptid='"+dept+"'" ).setTimestamp(0, new Date()).executeUpdate();
		if(a>0){
			return "ok";
		}else{
			return "error";
		}
	}
	@Override
	public String updateStockInfo(int num, String drugId, String dept) {
		int a = getSession().createQuery("UPDATE DrugStockinfo s SET s.preoutSum= '"+num+"',s.updateTime = ?  WHERE s.drugId = '"+drugId+"' and s.storageDeptid='"+dept+"'" ).setTimestamp(0, new Date()).executeUpdate();
		if(a>0){
			return "ok";
		}else{
			return "error";
		}
	}

	@Override
	public int getTotalBack(ApplyVo vo) {
		StringBuffer sql=new StringBuffer();
		sql.append("select t.apply_no as applyNo," //退费主键
				+ "t.inpatient_no as inpatientNo,"    //流水号
				+ "info.patient_name as name,"   //患者名称
				+ "t.item_name as drugName,"  //项目名称
				+ "t.item_code as drugCode,"  //项目编号
				+ "t.specs as specs,"   //规格
				+ "t.sale_price as price,"   //单价
				+ "t.qty as qty,"   //退费数量
				+ "t.qty as num,"   //原退费数量
				+ "t.ext_flag3 as drugPackagingunit,"   //包装单位
				+ "t.charge_flag as chargeFlag,"  //退费标识
				+ "t.recipe_no as recipeNo,"  //处方号
				+ "t.sequence_no as sequenceNo,"  //处方流水号
				+ "(t.sale_price*t.qty) as chargeMoney,"//金额
				+" m.senddrug_flag as senddrugFlag,"       //摆药状态
				+ "m.balance_state as balanceState,"   //结算状态
				+ "t.drug_flag as drugFlag  "  //药品状态
				+ "from t_inpatient_cancelitem_now t "
				+ "inner join t_inpatient_medicinelist_now m on m.inpatient_no=t.inpatient_no "
				+ "inner join t_inpatient_info_now info on t.inpatient_no=info.inpatient_no "
				+ "where t.drug_flag=1 and t.charge_flag=0 and t.del_flg=0 and m.del_flg=0  ");
		return super.getSqlTotal(sql.toString());
	}

	@Override
	/**
	 * @Modifier：liujl
	 * @ModifyDate：2016-6-6 上午09:56:35  
	 * @ModifyRmk：  修改获取记录总条数方法
	 */	
	public int getTotalDrugBack(ApplyVo vo) {
		StringBuffer sql=new StringBuffer();
		sql.append("select t.apply_no as applyNo,"  //退费主键
				+ "t.inpatient_no as inpatientNo,"  //住院流水号
				+ "info.patient_name as name,"   //患者名称
				+ "t.item_name as drugName,"   //项目名称
				+ "t.item_code as drugCode,"   //项目编号
				+ "i.fee_code as costName,"     //费用名称
				+ "t.sale_price as price,"   //单价
				+ "t.qty as qty,"   //退费数量
				+ "t.qty as num,"   //原退费数量
				+ "t.ext_flag3 as drugPackagingunit,"   //单位
				+ "(t.sale_price * t.qty) as chargeMoney,"  //金额
				+ "t.exec_dpcd as executeDept,"  //执行科室
				+ "i.exec_opercode as execOpercode,"  //执行人
				+ "i.exec_date as exeDate,"    //执行时间
				+ "t.charge_flag as chargeFlag,"    //退费标识
				+ "t.recipe_no as recipeNo,"  //处方号
				+ "t.sequence_no as sequenceNo,"  //处方流水号
				+ "t.drug_flag as drugFlag "  //药品类型
				+ " from t_inpatient_cancelitem_now t "
				+ "inner join t_inpatient_itemlist_now i on t.inpatient_no=i.inpatient_no "
				+ "inner join t_inpatient_info_now info on t.inpatient_no=info.inpatient_no "
				+ "where t.drug_flag=2 and t.charge_flag=0 and t.del_flg=0 and i.del_flg=0 ");
		return super.getSqlTotal(sql.toString());
	}

	@Override
	public List<InpatientMedicineListNow> getChild(String recipeNo, String sequenceNo) {
		String hql="from InpatientMedicineListNow med where med.del_flg=0 and med.recipeNo='"+recipeNo+"' and med.sequenceNo="+sequenceNo+"";
		List<InpatientMedicineListNow> medicineList=super.find(hql, null);
		if(medicineList!=null&&medicineList.size()>0){
			return medicineList;
		}
		return new ArrayList<InpatientMedicineListNow>();
	}
	@Override
	public String updateInpatientMedList(String recipeNo, String sequenceNo, int amount, int balanceState) {
		int b = getSession().createQuery("UPDATE InpatientMedicineListNow m SET m.nobackNum = ('m.nobackNum+"+amount+"'),m.updateTime = ?  WHERE m.recipeNo = '"+recipeNo+"' and m.sequenceNo="+sequenceNo+" and m.balanceState='"+balanceState+"'").setTimestamp(0, new Date()).executeUpdate();
		if(b>0){
			return "ok";
		}else{
			return "error";
		}
	}
	@Override 
	public void updateApplyState(String deptId,String recipeNo, String sequenceNo) {
		int a= getSession().createQuery("UPDATE DrugApplyoutNow d SET d.applyState='3',d.cancelEmpl='"+deptId+"',d.cancelDate=? WHERE m.recipeNo = '"+recipeNo+"' and sequenceNo="+sequenceNo+"").setTimestamp(0, new Date()).executeUpdate();
		if(a>0){
			System.out.println("ok");
		}else{
			System.out.println("err");
		}
	}
	@Override
	public DrugApplyoutNow QueryApplyOut(String recipeNo, String sequenceNo) {
		String hql="from DrugApplyoutNow d where d.del_flg=0 and d.applyState in(0,1) and d.recipeNo='"+recipeNo+"' and d.sequenceNo="+sequenceNo+"";
		List<DrugApplyoutNow> drugList=super.find(hql, null);
		if(drugList!=null&&drugList.size()>0){
			return drugList.get(0);
		}
		return new DrugApplyoutNow();
	}
	@Override
	public String amendStorage(int amount, String drugId) {
		int a= getSession().createQuery("UPDATE	DrugStorage s SET s.preoutSum  = s.preoutSum +"+amount+",s.preoutCost = (s.preoutSum + "+amount+") *s.retailPrice /s.packQty  where s.drugId='"+drugId+"'").executeUpdate();
		if(a>0){
			return "ok";
		}else{
			return "err";
		}
		
	}
	@Override
	public String amendStockInfo(int amount, String drugId) {
		int b= getSession().createQuery("UPDATE	DrugStockinfo s SET	s.preoutSum  = s.preoutSum +"+amount+",s.preoutCost = (s.preoutSum + "+amount+") *s.retailPrice /s.packQty where s.drugId='"+drugId+"'").executeUpdate();
		if(b>0){
			return "ok";
		}else{
			return "err";
		}
	}
	@Override
	public DrugApplyoutNow queryDrdugApply(String recipeNo, String sequenceNo) {
		String  hql="from DrugApplyoutNow where del_flg=0 and recipeNo='"+recipeNo+"' and sequenceNo="+sequenceNo+"";
		List<DrugApplyoutNow> app=super.find(hql, null);
		if(app!=null&&app.size()>0){
			return app.get(0);
		}
		return new DrugApplyoutNow();
	}
	@Override
	public List<InpatientItemListNow> getChildNotDrug(String recipeNo, String sequenceNo) {
		String hql="from InpatientItemListNow med where med.del_flg=0 and med.recipeNo='"+recipeNo+"' and med.sequenceNo="+sequenceNo+"";
		List<InpatientItemListNow> itemList=super.find(hql, null);
		if(itemList!=null&&itemList.size()>0){
			return itemList;
		}
		return new ArrayList<InpatientItemListNow>();
	}
	@Override
	public void updateApplyOut(String recipeNo, String sequenceNo) {
		int b= getSession().createQuery("UPDATE DrugApplyoutNow  SET  validState=1 where recipeNo='"+recipeNo+"' and sequenceNo="+sequenceNo+"").executeUpdate();
		if(b>0){
			System.out.println("ok");
		}else{
			System.out.println("err");
		}
	}
	@Override
	public void updateStoMsg(String bill) {
		int b= getSession().createQuery("UPDATE InpatientStoMsg  SET sendFlag=1 where billclassCode='"+bill+"'").executeUpdate();
		if(b>0){
			System.out.println("ok");
		}else{
			System.out.println("err");
		}
	}
	@Override
	public String updateInpatientItemList(String recipeNo, String sequenceNo, int amount, int balanceState) {
		int b = getSession().createQuery("UPDATE InpatientItemListNow i SET i.nobackNum = ('i.nobackNum+"+amount+"'),i.extFlag2=6,i.updateTime = ?  WHERE i.recipeNo = '"+recipeNo+"' and i.sequenceNo="+sequenceNo+" and i.balanceState='"+balanceState+"'").setTimestamp(0, new Date()).executeUpdate();
		if(b>0){
			return "ok";
		}else{
			return "error";
		}
	}
	@Override
	public MatOutput queryOutPut(String recipeNo, String sequenceNo) {
		String hql="from MatOutput where del_flg=0 and recipeNo='"+recipeNo+"' and sequenceNo="+sequenceNo+"";
		List<MatOutput>  put=super.find(hql, null);
		if(put!=null&&put.size()>0){
			return put.get(0);
		}
		return new MatOutput();
	}
	@Override
	public String cancelmetList(int amount,String applyNo,String outNo, String stockNo) {
		int a=getSession().createQuery("update MaterialsCancelmetlist mc set mc.outNum='"+amount+"',cancelflag='1' where mc.applyNo='"+applyNo+"' and outNo='"+outNo+"' and stockCode='"+stockNo+"'").executeUpdate();
		if(a>0){
			return "ok";
		}else{
			return "err";
		}
	}

	@Override
	public List<MatBaseinfo> queryUnDrugCode(String compareId) {
		String hql="from MatBaseinfo where del_flg=0 and itemCode='"+compareId+"'";
		List<MatBaseinfo> baseinfoList=super.find(hql, null);
		if(baseinfoList!=null&&baseinfoList.size()>0){
			return baseinfoList;
		}
		return new ArrayList<MatBaseinfo>();
	}
	@Override
	public MatStockdetail queryStockNo(String compareId) {
		String hql="from MatStockdetail where del_flg=0 and stockNo='"+compareId+"'";
		List<MatStockdetail> stockdetailList=super.find(hql, null);
		if(stockdetailList!=null&&stockdetailList.size()>0){
			return stockdetailList.get(0);
		}
		return new MatStockdetail();
	}

	
	
	@Override
	public MatBaseinfo queryMatBase(String baseId, int batchFlag) {
		String hql="from MatBaseinfo where del_flg=0 and id='"+baseId+"' and batchFlag='"+batchFlag+"'";
		List<MatBaseinfo> baseInfoList=super.find(hql, null);
		if(baseInfoList!=null&&baseInfoList.size()>0){
			return baseInfoList.get(0);
		}
		return new MatBaseinfo();
	}
	@Override
	public String updateItemList(int amount, String id) {
		int b = getSession().createQuery("UPDATE InpatientItemListNow i SET i.nobackNum = '"+amount+"',i.updateTime = ?  WHERE i.id = '"+id+"'").setTimestamp(0, new Date()).executeUpdate();
		if(b>0){
			return "ok";
		}else{
			return "error";
		}
	}
	@Override
	public InpatientCancelitemNow queryCancelItem(String id) {
		String hql="from InpatientCancelitemNow where del_flg=0 and id='"+id+"'";
		List<InpatientCancelitemNow> cancelitem=super.find(hql, null);
		if(cancelitem!=null&&cancelitem.size()>0){
			return cancelitem.get(0);
		}
		return new InpatientCancelitemNow();
	}
	@Override
	public InpatientFeeInfoNow queryFeeInfo(String ipatientNo) {
		String hql="from InpatientFeeInfoNow where del_flg=0 and inpatientNo='"+ipatientNo+"'";
		List<InpatientFeeInfoNow> feeInfo=super.find(hql, null);
		if(feeInfo!=null&&feeInfo.size()>0){
			return feeInfo.get(0);
		}
		return new InpatientFeeInfoNow();
	}
	@Override   
	public String updateInpatientInfo(Double money,String inpatientNo) {
		int b = getSession().createQuery("UPDATE InpatientInfoNow i SET i.totCost='"+money+"' ,i.updateTime = ?  WHERE i.inpatientNo = '"+inpatientNo+"'").setTimestamp(0, new Date()).executeUpdate();
		if(b>0){
			return "ok";
		}else{
			return "error";
		}
	}
	public List<InpatientInfoNow> getInfoList(InpatientInfoNow info) {
		String hql=" from InpatientInfoNow o where 1=1 ";
		if(StringUtils.isNotBlank(info.getInpatientNo())){
			hql+=" and o.inpatientNo like '%"+info.getInpatientNo()+"' or o.medicalrecordId like '%"+info.getInpatientNo()+"'";
		}
		List<InpatientInfoNow> feeInfo=super.find(hql, null);
		if(feeInfo!=null&&feeInfo.size()>0){
			return feeInfo;
		}
		return new ArrayList<InpatientInfoNow>();
	}
	public BusinessHospitalbed getBedName(String bedInfoId) {
		String hql=" from BusinessHospitalbed d where d.stop_flg=0 and d.del_flg=0"
				+ "and d.id in ("
				+ "select * from InpatientBedInfoNow o where o.id='"+bedInfoId+"')";
		List<BusinessHospitalbed> feeInfo=super.find(hql, null);
		if(feeInfo!=null&&feeInfo.size()>0){
			return feeInfo.get(0);
		}
		return new BusinessHospitalbed();

	}
	
	@Override
	public InpatientInfoNow patientBasicData(String inpatientNo) {
		String hql = "from InpatientInfoNow t where "
					+"  t.inpatientNo='"+inpatientNo+"'";
		List<InpatientInfoNow> list = super.find(hql.toString(), null);
		if(list !=null && list.size() > 0){
			return list.get(0);
		}
		return new InpatientInfoNow();
	}
	@Override
	public InpatientCancelitemNow getById(String applyNo) {
		String hql = "from InpatientCancelitemNow t where "
				+"  t.stop_flg=0 and t.del_flg=0 and  t.id=? ";
		List<InpatientCancelitemNow> list = super.find(hql.toString(), applyNo);
		if(list !=null && list.size() > 0){
			return list.get(0);
		}
		return new InpatientCancelitemNow();
	}
	
	@Override
	public String getSeqByNameorNumNew(String seq, int i) {
		String sql="SELECT LPAD("+seq+".nextval,:i,'0') FROM dual";
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("i", i);
		String no=namedParameterJdbcTemplate.queryForObject(sql, paramMap, java.lang.String.class);
		return no;
	}
	
	@Override
	public void updateMedicine(InpatientMedicineListNow medicine) {
		String sql ="update T_INPATIENT_MEDICINELIST_NOW set NOBACK_NUM =?, UPDATEUSER = ?, UPDATETIME = ? where id=?";
		Object args[] = new Object[]{medicine.getNobackNum(),medicine.getUpdateUser(),medicine.getUpdateTime(),medicine.getId()};  
	    jdbcTemplate.update(sql,args);  
	}
	@Override
	public void updateDrugApplyout(DrugApplyoutNow applyout) {
		String user=ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String sql ="update t_drug_applyout_now set valid_state =? ,UPDATEUSER = ?, UPDATETIME = ?  where id=?";
		Object args[] = new Object[]{applyout.getValidState(),user,new Date(),applyout.getId()};  
	    jdbcTemplate.update(sql,args);  
		
	}
	@Override
	public void updateInpatientItem(InpatientItemListNow itemList) {
		String sql ="update t_inpatient_itemlist_now set NOBACK_NUM =?,update_sequenceno =?, UPDATEUSER = ?, UPDATETIME = ? where id=?";
		Object args[] = new Object[]{itemList.getNobackNum(),itemList.getUpdateSequenceno(),itemList.getUpdateUser(),itemList.getUpdateTime(),itemList.getId()};  
	    jdbcTemplate.update(sql,args);  
	}
	@Override
	public void saveOrUpdateCancelitemList( List<InpatientCancelitemNow> list) {
		String user=ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		for(InpatientCancelitemNow inpatientCancelitem:list){
			final InpatientCancelitemNow ss=inpatientCancelitem;
	
			if(StringUtils.isNotBlank(ss.getId())){
				String sql ="update T_INPATIENT_CANCELITEM_NOW set QUANTITY =? ,CHARGE_FLAG=?,DRUG_FLAG=?,UPDATEUSER = ?, UPDATETIME = ? where APPLY_NO=?";
				Object args[] = new Object[]{ss.getQuantity(),ss.getChargeFlag(),ss.getDrugFlag(),user,new Date(),ss.getId()};  
			    jdbcTemplate.update(sql,args);  
			}else{
				UUID uuid = UUID.randomUUID();//生成一个uuid
      			final String disId=uuid.toString().replace("-", "");//uuid变成字符串
				 int t = jdbcTemplate.update("insert into T_INPATIENT_CANCELITEM_NOW (APPLY_NO,bill_code,inpatient_no,name,baby_flag,dept_code,nurse_cell_code,drug_flag,item_code,item_name,specs,sale_price,quantity,price_unit,exec_dpcd,oper_code,oper_date,oper_dpcd,recipe_no,sequence_no,charge_flag,ext_flag3,createuser,createdept,createtime,updateuser,updatetime,stop_flg,del_flg,DEPT_NAME,NURSE_CELL_NAME,EXEC_DPCD_NAME,OPER_NAME,OPER_DPCD_NAME,APPLY_FLAG,HOSPITAL_ID,AREA_CODE) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",new PreparedStatementSetter() {    
					   public void setValues(PreparedStatement ps) throws SQLException { 
						  ps.setString(1, disId);
						  ps.setString(2, (StringUtils.isNotBlank(ss.getBillCode())?ss.getBillCode():""));
					      ps.setString(3, (StringUtils.isNotBlank(ss.getInpatientNo())?ss.getInpatientNo():""));
					      ps.setString(4, (StringUtils.isNotBlank(ss.getName())?ss.getName():""));
					      ps.setInt(5, ss.getBabyFlag());
					      ps.setString(6,(StringUtils.isNotBlank(ss.getDeptCode())?ss.getDeptCode():""));
					      ps.setString(7, (StringUtils.isNotBlank(ss.getCellCode())?ss.getCellCode():""));
					      ps.setInt(8, ss.getDrugFlag());
					      ps.setString(9, (StringUtils.isNotBlank(ss.getItemCode())?ss.getItemCode():""));
					      ps.setString(10, (StringUtils.isNotBlank(ss.getItemName())?ss.getItemName():""));
					      ps.setString(11,(StringUtils.isNotBlank(ss.getSpecs())?ss.getSpecs():"") );
					      ps.setDouble(12, ss.getSalePrice());
					      ps.setDouble(13, ss.getQuantity());
					      ps.setString(14, (StringUtils.isNotBlank(ss.getPriceUnit())?ss.getPriceUnit():""));
					      ps.setString(15, (StringUtils.isNotBlank(ss.getExecDpcd())?ss.getExecDpcd():""));
					      ps.setString(16, (StringUtils.isNotBlank(ss.getOperCode())?ss.getOperCode():""));
					      ps.setDate(17, new java.sql.Date(ss.getOperDate().getTime()) );
					      ps.setString(18,  (StringUtils.isNotBlank(ss.getOperDpcd())?ss.getOperDpcd():""));
					      ps.setString(19, (StringUtils.isNotBlank(ss.getRecipeNo())?ss.getRecipeNo():""));
					      ps.setInt(20, ss.getSequenceNo());
					      ps.setInt(21, ss.getChargeFlag());
					      if(ss.getExtFlag()==null){
					    	  ps.setNull(22, Types.INTEGER);
					      }else{
					    	  ps.setInt(22,  ss.getExtFlag());
					      }
					      ps.setString(23, ss.getCreateUser());
					      ps.setString(24,ss.getCreateDept());
					      ps.setDate(25, new java.sql.Date(ss.getCreateTime().getTime()));
					      ps.setString(26, ss.getUpdateUser());
					      ps.setDate(27,  new java.sql.Date(ss.getUpdateTime().getTime()));
					      ps.setInt(28, 0);
					      ps.setInt(29, 0);
					      ps.setString(30, (StringUtils.isNotBlank(ss.getDeptName())?ss.getDeptName():""));
					      ps.setString(31, (StringUtils.isNotBlank(ss.getNurseCellName())?ss.getNurseCellName():""));
					      ps.setString(32, (StringUtils.isNotBlank(ss.getExecDpcdName())?ss.getExecDpcdName():""));
					      ps.setString(33, (StringUtils.isNotBlank(ss.getOperName())?ss.getOperName():""));
					      ps.setString(34, (StringUtils.isNotBlank(ss.getOperDpcdName())?ss.getOperDpcdName():""));
					      ps.setInt(35,ss.getApplyFlag());
					      ps.setInt(36, ss.getHospitalId());
					      ps.setString(37, ss.getAreaCode());
					   }
				 });
			}
		
		}
	}
	@Override
	public Object getDrugApplyoutSequece(String seq) {
		String sql="SELECT "+seq+".nextval FROM dual";
		String no=jdbcTemplate.queryForObject(sql, java.lang.String.class);
		return no;
	}
	@Override
	public void saveOrUpdateListApplyout(List<DrugApplyoutNow> listApplyout) {
		String user=ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		for(DrugApplyoutNow drugApplyoutNow:listApplyout){
			final DrugApplyoutNow ss=drugApplyoutNow;
				String sql ="update T_DRUG_APPLYOUT_NOW t set t.recipe_no =?,t.sequence_no=? ,t.UPDATEUSER = ?, t.UPDATETIME = ?  where  t.id=?";
				Object args[] = new Object[]{ss.getRecipeNo(),ss.getSequenceNo(),user,new Date(),ss.getId()};  
			    jdbcTemplate.update(sql,args);  
			}
		
	}
	@Override
	public void saveOrUpdateList1(List<DrugApplyoutNow> listApplyout) {
		String user=ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		for(DrugApplyoutNow drugApplyoutNow:listApplyout){
			final DrugApplyoutNow ss=drugApplyoutNow;
				String sql ="update T_DRUG_APPLYOUT_NOW t set t.APPLY_STATE=? ,t.UPDATEUSER = ?, t.UPDATETIME = ?  where  t.id=?";
				Object args[] = new Object[]{3,user,new Date(),ss.getId()};  
			    jdbcTemplate.update(sql,args);  
			}
	}
	@Override
	public List queryInpatientByMedicalRecordId(String medicalrecordId,String page,String rows) {
		DetachedCriteria cr = DetachedCriteria.forClass(InpatientInfoNow.class);
		cr.add(Restrictions.eq("medicalrecordId", medicalrecordId));
        int p=Integer.parseInt(page);
        int r=Integer.parseInt(rows);
        int firstResult=(p-1)*r;
		List<InpatientInfoNow> list = (List<InpatientInfoNow>) this.getHibernateTemplate().findByCriteria(cr, firstResult, r);
		if(list!=null&&list.size()>0){
			return list;
		}else{
			DetachedCriteria cr2 = DetachedCriteria.forClass(InpatientInfo.class);
			cr2.add(Restrictions.eq("medicalrecordId", medicalrecordId));
	        int p2=Integer.parseInt(page);
	        int r2=Integer.parseInt(rows);
	        int firstResult2=(p2-1)*r2;
			List<InpatientInfo> list2 = (List<InpatientInfo>) this.getHibernateTemplate().findByCriteria(cr2, firstResult2, r2);
			if(list2!=null&&list2.size()>0){
			List<InpatientInfoNow> list3=new ArrayList<InpatientInfoNow>();
				for(InpatientInfo info:list2){
					InpatientInfoNow infoNow=new InpatientInfoNow();
					infoNow.setPatientName(info.getPatientName());
					infoNow.setReportSexName(info.getReportSexName());
					infoNow.setInDate(info.getInDate());
					infoNow.setOutDate(info.getOutDate());
					infoNow.setInpatientNo(info.getInpatientNo());
					list3.add(infoNow);
				}
				return list3;
			}
			return new ArrayList<InpatientInfoNow>();
		}
	}
	@Override
	public List queryInpatientReturns(
			String inpatientNo,String page ,String rows) {
		    int p=Integer.parseInt(page);
	        int r=Integer.parseInt(rows);
	        int fromIndex=(p-1)*r;
	        if(StringUtils.isBlank(inpatientNo)){
	        	inpatientNo="'";
	        }
		    String sql="select * from (select t.name,t.dept_name,t.item_name,t.specs,t.sale_price,t.price_unit,t.quantity,"
		    		+ "t.oper_date,t.oper_name,t.RECIPE_NO,t.SEQUENCE_NO from t_inpatient_cancelitem_now t  where "
		    		+ "t.CHARGE_FLAG=1 and t.inpatient_no = '"+inpatientNo+"')  union all select * from  (select t1.name,t1.dept_name,t1.item_name,"
		    		+ "t1.specs,t1.sale_price,t1.price_unit,t1.quantity,t1.oper_date,t1.oper_name,t1.RECIPE_NO,t1.SEQUENCE_NO "
		    		+ "from t_inpatient_cancelitem t1  where t1.CHARGE_FLAG=1  and t1.inpatient_no = '"+inpatientNo+"') ";
		    SQLQuery query = this.getSession().createSQLQuery(sql);
		    query.setFirstResult(fromIndex).setMaxResults(r);
		    List list = query.list();
		if(list !=null&&list.size()>0){
			return list;
		}
		return new ArrayList<>();
	}
	@Override
	public int queryTotal(String medicalrecordId) {
		String sql="select count(*) from  t_inpatient_info_now  where MEDICALRECORD_ID='"+medicalrecordId+"'";
		SQLQuery createSQLQuery = this.getSession().createSQLQuery(sql);
		List list = createSQLQuery.list();
		if(list!=null&&list.size()>0){
		  int total=Integer.parseInt(list.get(0).toString());
		  if(total==0){
			    String sql2="select count(*) from  t_inpatient_info  where MEDICALRECORD_ID='"+medicalrecordId+"'";
				SQLQuery createSQLQuery2 = this.getSession().createSQLQuery(sql2);
				List list2 = createSQLQuery2.list();
				if(list2!=null&&list2.size()>0){
				 int total2=Integer.parseInt(list2.get(0).toString());
				 return total2;
				}
		  }
		  return total;
		}
		return 0;
	}
	@Override
	public int queryTotalBy(String inpatientNo) {
		if(StringUtils.isBlank(inpatientNo)){
			inpatientNo="'" ;
		}
		String sql="select count(*) from (select * from (select t.name,t.dept_name,t.item_name,t.specs,t.sale_price,t.price_unit,t.quantity,"
	    		+ "t.oper_date,t.oper_name,t.return_reason from t_inpatient_cancelitem_now t  where "
	    		+ " t.CHARGE_FLAG=1 and t.inpatient_no = '"+inpatientNo+"') union all select * from (select t1.name,t1.dept_name,t1.item_name,"
	    		+ "t1.specs,t1.sale_price,t1.price_unit,t1.quantity,t1.oper_date,t1.oper_name,t1.return_reason "
	    		+ "from t_inpatient_cancelitem t1  where t1.CHARGE_FLAG=1 and t1.inpatient_no = '"+inpatientNo+"' ))";
		SQLQuery createSQLQuery = this.getSession().createSQLQuery(sql);
		List list = createSQLQuery.list();
		if(list!=null){
		  int total=Integer.parseInt(list.get(0).toString());
			return total;
		}
		return 0;
	}
	@Override
	public void saveOrUpdateCancelitemList(List<InpatientCancelitemNow> list,
			String empJobNo) {

		String user=empJobNo;
		for(InpatientCancelitemNow inpatientCancelitem:list){
			final InpatientCancelitemNow ss=inpatientCancelitem;
	
			if(StringUtils.isNotBlank(ss.getId())){
				String sql ="update T_INPATIENT_CANCELITEM_NOW set QUANTITY =? ,CHARGE_FLAG=?,DRUG_FLAG=?,UPDATEUSER = ?, UPDATETIME = ? where APPLY_NO=?";
				Object args[] = new Object[]{ss.getQuantity(),ss.getChargeFlag(),ss.getDrugFlag(),user,new Date(),ss.getId()};  
			    jdbcTemplate.update(sql,args);  
			}else{
				UUID uuid = UUID.randomUUID();//生成一个uuid
      			final String disId=uuid.toString().replace("-", "");//uuid变成字符串
				 int t = jdbcTemplate.update("insert into T_INPATIENT_CANCELITEM_NOW (APPLY_NO,bill_code,inpatient_no,name,baby_flag,dept_code,nurse_cell_code,drug_flag,item_code,item_name,specs,sale_price,quantity,price_unit,exec_dpcd,oper_code,oper_date,oper_dpcd,recipe_no,sequence_no,charge_flag,ext_flag3,createuser,createdept,createtime,updateuser,updatetime,stop_flg,del_flg,DEPT_NAME,NURSE_CELL_NAME,EXEC_DPCD_NAME,OPER_NAME,OPER_DPCD_NAME,APPLY_FLAG,HOSPITAL_ID,AREA_CODE) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",new PreparedStatementSetter() {    
					   public void setValues(PreparedStatement ps) throws SQLException { 
						  ps.setString(1, disId);
						  ps.setString(2, (StringUtils.isNotBlank(ss.getBillCode())?ss.getBillCode():""));
					      ps.setString(3, (StringUtils.isNotBlank(ss.getInpatientNo())?ss.getInpatientNo():""));
					      ps.setString(4, (StringUtils.isNotBlank(ss.getName())?ss.getName():""));
					      ps.setInt(5, ss.getBabyFlag());
					      ps.setString(6,(StringUtils.isNotBlank(ss.getDeptCode())?ss.getDeptCode():""));
					      ps.setString(7, (StringUtils.isNotBlank(ss.getCellCode())?ss.getCellCode():""));
					      ps.setInt(8, ss.getDrugFlag());
					      ps.setString(9, (StringUtils.isNotBlank(ss.getItemCode())?ss.getItemCode():""));
					      ps.setString(10, (StringUtils.isNotBlank(ss.getItemName())?ss.getItemName():""));
					      ps.setString(11,(StringUtils.isNotBlank(ss.getSpecs())?ss.getSpecs():"") );
					      ps.setDouble(12, ss.getSalePrice());
					      ps.setDouble(13, ss.getQuantity());
					      ps.setString(14, (StringUtils.isNotBlank(ss.getPriceUnit())?ss.getPriceUnit():""));
					      ps.setString(15, (StringUtils.isNotBlank(ss.getExecDpcd())?ss.getExecDpcd():""));
					      ps.setString(16, (StringUtils.isNotBlank(ss.getOperCode())?ss.getOperCode():""));
					      ps.setDate(17, new java.sql.Date(ss.getOperDate().getTime()) );
					      ps.setString(18,  (StringUtils.isNotBlank(ss.getOperDpcd())?ss.getOperDpcd():""));
					      ps.setString(19, (StringUtils.isNotBlank(ss.getRecipeNo())?ss.getRecipeNo():""));
					      ps.setInt(20, ss.getSequenceNo());
					      ps.setInt(21, ss.getChargeFlag());
					      if(ss.getExtFlag()==null){
					    	  ps.setNull(22, Types.INTEGER);
					      }else{
					    	  ps.setInt(22,  ss.getExtFlag());
					      }
					      ps.setString(23, ss.getCreateUser());
					      ps.setString(24,ss.getCreateDept());
					      ps.setDate(25, new java.sql.Date(ss.getCreateTime().getTime()));
					      ps.setString(26, ss.getUpdateUser());
					      ps.setDate(27,  new java.sql.Date(ss.getUpdateTime().getTime()));
					      ps.setInt(28, 0);
					      ps.setInt(29, 0);
					      ps.setString(30, (StringUtils.isNotBlank(ss.getDeptName())?ss.getDeptName():""));
					      ps.setString(31, (StringUtils.isNotBlank(ss.getNurseCellName())?ss.getNurseCellName():""));
					      ps.setString(32, (StringUtils.isNotBlank(ss.getExecDpcdName())?ss.getExecDpcdName():""));
					      ps.setString(33, (StringUtils.isNotBlank(ss.getOperName())?ss.getOperName():""));
					      ps.setString(34, (StringUtils.isNotBlank(ss.getOperDpcdName())?ss.getOperDpcdName():""));
					      ps.setInt(35,ss.getApplyFlag());
					      ps.setInt(36, ss.getHospitalId());
					      ps.setString(37, ss.getAreaCode());
					   }
				 });
			}
		
		}
	
		
	}


}
