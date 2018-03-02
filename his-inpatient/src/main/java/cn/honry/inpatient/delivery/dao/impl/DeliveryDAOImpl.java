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
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.DepartmentContact;
import cn.honry.base.bean.model.DrugApplyout;
import cn.honry.base.bean.model.DrugApplyoutNow;
import cn.honry.base.bean.model.DrugBillclass;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientStoMsgNow;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inpatient.delivery.dao.DeliveryDAO;
import cn.honry.inpatient.delivery.vo.DeliveryVo;
import cn.honry.utils.HisParameters;
import cn.honry.utils.ShiroSessionUtils;
/**
 * 摆药单数据库层
 * @author  lyy
 * @createDate： 2015年12月28日 上午10:51:13 
 * @modifier lyy
 * @modifyDate：2015年12月28日 上午10:51:13  
 * @modifyRmk：  
 * @version 1.0
 */
@Repository(value="deliveryDao")
@SuppressWarnings({"all"})
public class DeliveryDAOImpl extends HibernateEntityDao<DrugApplyout> implements DeliveryDAO {
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<DrugBillclass> queryBillclass(String billclasss) {
		String hql="From DrugBillclass db where db.del_flg=0 and db.stop_flg=0 and db.id in ('"+billclasss+"')";
		List<DrugBillclass> billList=super.find(hql, null);
		if(billList!=null&&billList.size()>0){
			return billList;
		}
		return new ArrayList<DrugBillclass>();
	}
	@Override
	public List<DeliveryVo> getPage(DeliveryVo deliveryVo, String page, String rows,String deptCode) {
		StringBuilder sql=jointDeliverySql(deliveryVo,deptCode,"1");
		Map<String,Object> paramMap = new HashMap<String, Object>();						
		paramMap.put("billclassCode", deliveryVo.getBillclassCode());					
		 List<DeliveryVo> DeliveryVo = namedParameterJdbcTemplate.query(sql.toString(), paramMap,new RowMapper<DeliveryVo>(){		
			@Override				
			public DeliveryVo mapRow(ResultSet rs, int i) throws SQLException {				
				DeliveryVo vo = new DeliveryVo();			
				vo.setId(rs.getString("id"));			
				vo.setBedId(rs.getString("bedId"));			
				vo.setName(rs.getString("name"));	
				vo.setInpatientNo(rs.getString("inpatientNo"));
				vo.setApplyNumber(rs.getInt("applyNumber"));
				vo.setDrugName(rs.getString("drugName"));
				vo.setSpecs(rs.getString("specs"));
				vo.setDfqFreq(rs.getString("dfqFreq"));
				vo.setUseName(rs.getString("useName"));
				vo.setApplyNumSum(rs.getInt("applyNumSum"));
				vo.setMinUnit(rs.getString("minUnit"));
				vo.setShowUnit(rs.getString("showUnit"));
				vo.setShowFlag(rs.getInt("showFlag"));
				vo.setPackQty(rs.getInt("packQty"));
				vo.setDeptCode(rs.getString("deptCode"));
				vo.setApplyDeptName(rs.getString("applyDeptName"));
				vo.setDeptName(rs.getString("deptName"));
				vo.setDrugDeptName(rs.getString("drugDeptName"));
				vo.setSendType(rs.getInt("sendType"));
				vo.setBillclassName(rs.getString("billclassName"));
				vo.setApplyOpercode(rs.getString("applyOpercode"));
				vo.setDrugedEmplName(rs.getString("drugedEmplName"));
				vo.setApplyDate(rs.getTimestamp("applyDate"));
				vo.setPrintEmpl(rs.getString("printEmpl"));
				vo.setPrintEmplName(rs.getString("printEmplName"));
				vo.setPrintDate(rs.getTimestamp("printDate"));
				vo.setValidState(rs.getInt("validState"));
				vo.setPinyin(rs.getString("pinyin"));
				vo.setWb(rs.getString("wb"));
				vo.setPrint(rs.getInt("print"));
				vo.setDrugEdBill(rs.getString("drugEdBill"));
				vo.setCombNo(rs.getString("combNo"));
				return vo;			
			}				
		});					
		return DeliveryVo;					
	}
	@Override
	/**
	 * @Modifier：liujl
	 * @ModifyDate：2016-6-6 上午09:56:35  
	 * @ModifyRmk：  修改获取记录总条数方法
	 */	
	public int getTotal(DeliveryVo deliveryVo,String deptCode) {
		StringBuilder sql =jointDeliverySql(deliveryVo,deptCode,"0");
		String sqls="select count(id) from ("+sql.toString()+")";
		Map<String,Object> paramMap = new HashMap<String, Object>();						
		paramMap.put("billclassCode", deliveryVo.getBillclassCode());					
		return namedParameterJdbcTemplate.queryForObject(sqls, paramMap, java.lang.Integer.class);
	}
	/**
	 * 集中发送的药品的sql查询
	 * @author  lyy
	 * @createDate： 2016年4月20日 下午5:09:08 
	 * @modifier lyy
	 * @modifyDate：2016年4月20日 下午5:09:08
	 * @param：    deliveryVo 集中发药的虚拟实体
	 * @modifyRmk：  deliveryVo 集中发送虚拟实体
	 * @version 1.0
	 */
	private StringBuilder jointDeliverySql(DeliveryVo deliveryVo,String deptCode,String type){
		StringBuilder sql = new StringBuilder();
		sql.append("select t.id as id,"     //住院主表主键
				+ "r.bed_name as bedId,"    //病床名称
				+ "r.patient_name as name,"    //患者姓名
				+ "r.inpatient_no as inpatientNo,"    //住院流水号
				+ "t.APPLY_NUMBER as applyNumber,"    //申请流水号
				+ "s.drug_name as drugName,"    //药品名称
				+ "t.specs as specs,"    //规格
				+ "t.dfq_cexp as dfqFreq,"    //频次
				+ "t.use_name as useName,"    //用法
				+ "sum(t.apply_num) as applyNumSum,"    //总量
				+ "t.min_unit as minUnit,"    //单位
				+ "t.show_unit as showUnit,"    //显示的单位
				+ "t.show_flag as showFlag,"    //显示的单位标记 1 包装单位2最小单位
				+ "t.pack_qty as packQty," 
				+ "t.dept_code as deptCode,"    //申请科室
				+ "t.DEPT_NAME as applyDeptName,"    //申请科室
				+ "t.drug_dept_code as deptName,"    //取药科室(发药科室)
				+ "t.DRUG_DEPT_NAME as drugDeptName,"    //取药科室(发药科室名称)
				+ "t.send_type as sendType,"    //发送类别
				+ "b.billclass_name as billclassName,"     //摆药单名称
				+ "t.print_empl as printEmpl,"    //发送人
				+ "t.PRINT_EMPL_NAME as printEmplName,"    //发送人名称
				+ "t.print_date as printDate,"    //发送时间
				+ "t.druged_empl as applyOpercode,"    //发药人
				+ "t.DRUGED_EMPL_NAME as drugedEmplName,"    //发药人
				+ "t.druged_date as applyDate, "    //发药时间
				+ "t.valid_state as validState,"    //有效性
				+ "s.drug_namepinyin as pinyin,"    //拼音码
				+ "s.drug_namewb as wb,"    //五笔码
				+ "t.apply_state as print,"    //根据摆药状态为2即摆药（摆药）
				+ "t.druged_bill as drugEdBill,"    //摆药单号
				+ "t.comb_no as combNo "    //组合号
				+ " from "+HisParameters.HISPARSCHEMAHISUSER+"T_DRUG_APPLYOUT_NOW t"    //出库申请表
				+ ","+HisParameters.HISPARSCHEMAHISUSER+"T_DRUG_INFO s"    //药品表
				+ ","+HisParameters.HISPARSCHEMAHISUSER+"T_INPATIENT_INFO_NOW r"    //住院主表
				+ ","+HisParameters.HISPARSCHEMAHISUSER+"T_DRUG_BILLCLASS b "    //摆药分类
				+ "where t.drug_code = s.drug_code and t.apply_state = '0' and t.op_type in (4,5) and t.valid_state=1 "
				+ "and t.billclass_code=b.billclass_code  "
				+ "and t.patient_id=r.inpatient_no and s.del_flg=0 and t.del_flg=0 "
				+ "and s.stop_flg=0 and t.stop_flg=0  AND b.billclass_code = :billclassCode and t.dept_code in ('"+deliveryVo.getDeptCode()+"')");
		sql.append(" group by t.id,r.bed_name,r.inpatient_no,r.patient_name,s.drug_name,t.specs,t.dfq_cexp,"
				+ "t.use_name,t.min_unit,t.show_unit,t.show_Flag,t.pack_qty,t.dept_code,t.DEPT_NAME,t.drug_dept_code,t.DRUG_DEPT_NAME,t.send_type,b.billclass_name,"
				+ " t.print_empl,t.PRINT_EMPL_NAME,t.print_date,t.druged_empl,t.DRUGED_EMPL_NAME,t.druged_date,t.valid_state,s.drug_namepinyin,s.drug_namewb,"
				+ " t.apply_opercode,t.apply_date,t.APPLY_NUMBER,t.apply_state,t.druged_bill,t.comb_no");
		sql.append(" order by r.bed_name ,t.comb_no");
		return sql;
	}
	@Override
	public String applyOutUpdate(String[] ids,String drugEdBill,Integer sendType,String userId) {
		 String user=ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		 String name = ShiroSessionUtils.getCurrentUserFromShiroSession().getName();
		Date date=new Date();
		StringBuffer sb=new StringBuffer();
		sb.append("update t_drug_applyout_now t set t.apply_state='6' ,t.print_state=0 , ")
		.append("t.druged_bill=? ,t.send_type=? ,t.print_empl=?, t.PRINT_EMPL_NAME=?,t.print_date=? ,t.UPDATEUSER = ?, t.UPDATETIME = ?")
		.append(" where t.id = ?");
		int c=0;
		for(int i=0;i<ids.length;i++){
			Object args[] = new Object[]{drugEdBill,sendType,userId,name,date,user,date, ids[i]}; 
			 c=jdbcTemplate.update(sb.toString(),args);  
		}
		if(c>0){
			return "ok";
		}else{
			return "error";
		}
	}
	@Override
	public List<DrugApplyoutNow> queryApplyOut(List patientidlist) {
		String hql="select d.druged_bill as drugedBill "
				+ "from "+HisParameters.HISPARSCHEMAHISUSER+"t_drug_applyout_now d "
						+ " left join t_inpatient_info_now t on t.inpatient_no = d.patient_id  "
						+ "where "
						+ " d.op_type=4 and d.apply_state in (2,5,6) "
						+ " and t.in_state ='I' and d.PATIENT_DEPT in (:patientidlist)"
						+ "group by d.druged_bill";
		Map<String,Object> paramMap = new HashMap<String, Object>();						
		paramMap.put("patientidlist", patientidlist);	
		 List<DrugApplyoutNow> applyList = namedParameterJdbcTemplate.query(hql, paramMap,new RowMapper<DrugApplyoutNow>(){

			@Override
			public DrugApplyoutNow mapRow(ResultSet rs, int rowNum) throws SQLException {
				DrugApplyoutNow drugApplyoutNow = new DrugApplyoutNow();
				drugApplyoutNow.setDrugedBill(rs.getString("drugedBill"));
				return drugApplyoutNow;
			}
		 });	
		System.err.println(applyList.size());
		if(applyList!=null&&applyList.size()>0){
			return applyList;
		}
		return new ArrayList<DrugApplyoutNow>();
	}
	@Override
	public List<DrugApplyoutNow> queryReturnDrug(List patientidlist) {
		String hql="select d.druged_bill as drugedBill "
				+ "from "+HisParameters.HISPARSCHEMAHISUSER+"t_drug_applyout_now d "
				+ " left join t_inpatient_info_now t on t.inpatient_no = d.patient_id  "
				+ "where d.del_flg=0 and d.stop_flg=0 and d.op_type=5 "
		+ "and d.apply_state in (2,5,6) and t.in_state ='I' and d.PATIENT_DEPT in (:patientidlist) group by d.druged_bill";
		Map<String,Object> paramMap = new HashMap<String, Object>();						
		paramMap.put("patientidlist", patientidlist);	
		 List<DrugApplyoutNow> applyList = namedParameterJdbcTemplate.query(hql, paramMap,new RowMapper<DrugApplyoutNow>(){

			@Override
			public DrugApplyoutNow mapRow(ResultSet rs, int rowNum) throws SQLException {
				DrugApplyoutNow drugApplyoutNow = new DrugApplyoutNow();
				drugApplyoutNow.setDrugedBill(rs.getString("drugedBill"));
				return drugApplyoutNow;
			}
		 });
		if(applyList!=null&&applyList.size()>0){
			System.err.println(applyList.size());
			return applyList;
		}
		return new ArrayList<DrugApplyoutNow>();
	}

	@Override
	public int total() {
		String hql="select nvl(count(id),0) From DrugApplyout";
		Integer query = (Integer)this.getSession().createQuery(hql).uniqueResult();
		return query;
	}
	@Override
	public List<DepartmentContact> queryDeptContact(String deptId) {
		String hql="from DepartmentContact t1 where (t1.pardeptId in "
				+ "(select t.id from DepartmentContact t where t.referenceType = '03'"
				+ " and t.deptId = ? and t.del_flg=0 and t.stop_flg=0) or t1.deptId= ?) and t1.del_flg=0 and t1.stop_flg=0";
		List<DepartmentContact> list=super.find(hql,deptId,deptId);
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}
	@Override
	public List<InpatientInfoNow> getQueryInpatientNo(List deptContactList) {
		String hql="From InpatientInfoNow i where  i.inState='I' and i.deptCode in (:deptContactList )";
		Query query=this.getSession().createQuery(hql);
		query.setParameterList("deptContactList",deptContactList);
		List<InpatientInfoNow> list=query.list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}
	@Override
	public List<DrugApplyoutNow> getQueryDrugApply(String patientid) {
		String hql="from DrugApplyoutNow d where d.del_flg=0 "
				+ "and d.stop_flg=0 and d.applyState=0 and d.patientId in ('"+patientid+"')";
		List<DrugApplyoutNow> list=super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}
	@Override
	public String applyOutStamp(String ids,Integer sendType,String userId) {
		ids=ids.replaceAll(",", "','");
		ids = "'"+ids+"'";
		    String user=ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
			Date date=new Date();
			StringBuffer sb=new StringBuffer();
			sb.append("update t_drug_applyout_now t set t.apply_state='5' ,t.print_state=1 , ")
			.append(" t.print_date=? ,t.send_type=? ,t.print_empl=?, t.UPDATEUSER = ?, t.UPDATETIME = ?")
			.append(" where t.id in(?)");
			int c=0;
				Object args[] = new Object[]{new Date(),sendType,userId,user,new Date(date.getTime()), ids}; 
				 c=jdbcTemplate.update(sb.toString(),args);  
		if(c>0){
			return "ok";
		}else{
			return "error";
		}
	}
	@Override
	/**
	 * @Modifier：liujl
	 * @ModifyDate：2016-6-6 上午09:56:35  
	 * @ModifyRmk：  修改获取记录总条数方法
	 */	
	public int getTotalBill(DeliveryVo deliveryVo) {
		StringBuffer sql =jointBillSql(deliveryVo,"0");
		String sqls="select count(id) from ("+sql.toString()+")";
		Map<String,Object> paramMap = new HashMap<String, Object>();						
		if(StringUtils.isNotBlank(deliveryVo.getInpatientNo())){
			paramMap.put("inpatientNo", deliveryVo.getInpatientNo());	
		}else{
			paramMap.put("drugEdBill", deliveryVo.getDrugEdBill());	

		}			
		return namedParameterJdbcTemplate.queryForObject(sqls, paramMap, java.lang.Integer.class);
	}
	@Override
	public List<DeliveryVo> getPageBill(DeliveryVo deliveryVo, String page, String rows) {
		StringBuffer sql=jointBillSql(deliveryVo,"1");
		Map<String,Object> paramMap = new HashMap<String, Object>();						
		if(StringUtils.isNotBlank(deliveryVo.getInpatientNo())){
			paramMap.put("inpatientNo", deliveryVo.getInpatientNo());	
		}else{
			paramMap.put("drugEdBill", deliveryVo.getDrugEdBill());	

		}	
		int start = Integer.parseInt(page==null?"1":page);
		int count = Integer.parseInt(rows==null?"20":rows);
		paramMap.put("page", start);	
		paramMap.put("rows", count);	
		 List<DeliveryVo> DeliveryVo = namedParameterJdbcTemplate.query(sql.toString(), paramMap,new RowMapper<DeliveryVo>(){		
			@Override				
			public DeliveryVo mapRow(ResultSet rs, int i) throws SQLException {				
				DeliveryVo vo = new DeliveryVo();			
				vo.setId(rs.getString("id"));			
				vo.setBedId(rs.getString("bedId"));			
				vo.setName(rs.getString("name"));	
				vo.setApplyNumber(rs.getInt("applyNumber"));
				vo.setDrugName(rs.getString("drugName"));
				vo.setSpecs(rs.getString("specs"));
				vo.setDfqFreq(rs.getString("dfqFreq"));
				vo.setUseName(rs.getString("useName"));
				vo.setApplyNumSum(rs.getInt("applyNumSum"));
				vo.setMinUnit(rs.getString("minUnit"));
				vo.setShowUnit(rs.getString("showUnit"));
				vo.setShowFlag(rs.getInt("showFlag"));
				vo.setPackQty(rs.getInt("packQty"));
				vo.setDeptCode(rs.getString("deptCode"));
				vo.setApplyDeptName(rs.getString("applyDeptName"));
				vo.setDeptName(rs.getString("deptName"));
				vo.setDrugDeptName(rs.getString("drugDeptName"));
				vo.setSendType(rs.getInt("sendType"));
				vo.setBillclassName(rs.getString("billclassName"));
				vo.setApplyOpercode(rs.getString("applyOpercode"));
				vo.setDrugedEmplName(rs.getString("drugedEmplName"));
				vo.setApplyDate(rs.getTimestamp("applyDate"));
				vo.setPrintEmpl(rs.getString("printEmpl"));
				vo.setPrintEmplName(rs.getString("printEmplName"));
				vo.setPrintDate(rs.getTimestamp("printDate"));
				vo.setValidState(rs.getInt("validState"));
				vo.setPinyin(rs.getString("pinyin"));
				vo.setWb(rs.getString("wb"));
				vo.setPrint(rs.getInt("print"));
				vo.setDrugEdBill(rs.getString("drugEdBill"));
				vo.setCombNo(rs.getString("combNo"));
				return vo;			
			}				
		});					
							
		return DeliveryVo;					
	}
	/**
	 * 发送后根据摆药单号查询
	 * @author  lyy
	 * @createDate： 2016年4月20日 下午5:10:02 
	 * @modifier lyy
	 * @modifyDate：2016年4月20日 下午5:10:02
	 * @param：    deliveryVo 集中发送虚拟实体
	 * @modifyRmk：  
	 * @version 1.0
	 */
	private StringBuffer jointBillSql(DeliveryVo deliveryVo,String type ){
		StringBuffer sql=new StringBuffer();
		if("1".equals(type)){
			sql.append("select * from (select t.*,rownum n from (");
		}
		sql.append("select t.id as id,"    //住院主表主键
				+ "r.bed_name as bedId,"    //病床名称
				+ "r.patient_name as name,"    //患者姓名
				+ "t.APPLY_NUMBER as applyNumber,"    //申请流水号
				+ " s.drug_name as drugName,"    //药品名称
				+ "t.specs as specs,"    //规格
				+ "t.dfq_cexp as dfqFreq,"    //频次
				+ "t.use_name as useName,"    //用法
				+ "sum(t.apply_num) as applyNumSum,"    //总量
				+ "t.min_unit as minUnit,"    //单位
				+ "t.show_unit as showUnit,"    //显示的单位
				+ "t.show_flag as showFlag,"    //显示的单位标记 1 包装单位2最小单位
				+ "t.pack_qty as packQty,"
				+ "t.dept_code as deptCode,"    //申请科室
				+ "t.DEPT_NAME as applyDeptName,"    //申请科室
				+ " t.drug_dept_code as deptName,"    //取药科室 （发药科室）
				+ "t.DRUG_DEPT_NAME as drugDeptName,"    //取药科室(发药科室名称)
				+ "t.send_type as sendType,"    //发送类别
				+ "b.billclass_name as billclassName,"    //摆药单名称
				+ "t.print_empl as printEmpl,"    //发送人
				+ "t.PRINT_EMPL_NAME as printEmplName,"    //发送人名称
				+ "t.print_date as printDate,"    //发送时间
				+ "t.druged_empl as applyOpercode,"    //发药人
				+ "t.DRUGED_EMPL_NAME as drugedEmplName,"    //发药人
				+ "t.druged_date as applyDate, "    //发药时间
				+ "t.valid_state as validState,"    //有效性
				+ "s.drug_namepinyin as pinyin,"    //拼音码
				+ "s.drug_namewb as wb,"    //五笔码
				+ "t.apply_state as print,"
				+ "t.druged_bill as drugEdBill,"    //摆药单号
				+ "t.comb_no as combNo "    //组合号
				+ "from "+HisParameters.HISPARSCHEMAHISUSER+"T_DRUG_APPLYOUT_NOW t"    //药品出库申请表
				+ ","+HisParameters.HISPARSCHEMAHISUSER+"T_DRUG_INFO s"    //药品表
				+ ","+HisParameters.HISPARSCHEMAHISUSER+"T_INPATIENT_INFO_NOW r "    //住院主表
				+ ","+HisParameters.HISPARSCHEMAHISUSER+"T_DRUG_BILLCLASS b "    //摆药分类
				+ "where t.drug_code = s.drug_code and t.apply_state in (2,5,6) and t.op_type in (4,5) "
				+ "and t.billclass_code=b.billclass_code  "
				+ "and t.patient_id=r.inpatient_no and s.del_flg=0 and t.del_flg=0 "
				+ "and s.stop_flg=0 and t.stop_flg=0 ");
			if(StringUtils.isNotBlank(deliveryVo.getInpatientNo())){
				sql.append("  AND r.inpatient_no in (:inpatientNo)");
			}else{
				sql.append("  AND t.druged_bill = :drugEdBill");

			}				
		sql.append(" group by t.id,r.bed_name,r.patient_name,s.drug_name,t.specs,t.dfq_cexp,"
				+ "t.use_name,t.min_unit,t.show_unit,t.show_Flag,t.pack_qty, t.dept_code,t.DEPT_NAME,t.drug_dept_code,t.DRUG_DEPT_NAME,t.send_type,b.billclass_name,"
				+ " t.print_empl,t.PRINT_EMPL_NAME,t.print_date,t.druged_empl,t.DRUGED_EMPL_NAME,t.druged_date,t.valid_state,s.drug_namepinyin,s.drug_namewb,"
				+ " t.apply_opercode,t.apply_date,t.APPLY_NUMBER,t.apply_state,t.druged_bill,t.comb_no");
		if("1".equals(type)){
			sql.append(")t where rownum <=:rows* :page) where n>(:page - 1) * :rows");
		}
		
		return sql;
	}
	@Override
	public List<User> queryEmpName() {
		String hql="From User emp where emp.del_flg=0 and emp.stop_flg=0 ";
		List<User> empList=super.find(hql, null);
		if(empList!=null&&empList.size()>0){
			return empList;
		}
		return new ArrayList<User>();
	}
	@Override
	public List<DrugApplyoutNow> queryDrugBill(String[] ids) {
		String hql="From DrugApplyoutNow d where d.del_flg=0 and d.stop_flg=0 and d.id in (:ids)";
		Query query=this.getSession().createQuery(hql);
		query.setParameterList("ids", ids);
		List<DrugApplyoutNow> list=query.list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<DrugApplyoutNow>();
	}
	@Override
	public InpatientStoMsgNow queryStoMsg(String billclassCode,String drugCode,Integer sendType) {
		String type=String.valueOf(sendType);
		String hql="From InpatientStoMsgNow d where d.del_flg=0 and d.sendFlag='0' and d.billclassCode =? and d.medDeptCode=? and sendType=?";
		List<InpatientStoMsgNow> list=super.find(hql, billclassCode,drugCode,type);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
	@Override
	public List<DrugBillclass> queryBillClassName() {
		String hql="select t.billclass_id,t.billclass_name,t.billclass_code from t_drug_billclass t where  t.del_flg=0 and t.stop_flg=0 ";
		 List<DrugBillclass> drugBillclass = jdbcTemplate.query(hql,new RowMapper<DrugBillclass>(){		
			@Override				
			public DrugBillclass mapRow(ResultSet rs, int i) throws SQLException {		
				DrugBillclass vo =new DrugBillclass();
				vo.setId(rs.getString("billclass_id"));
				vo.setBillclassName(rs.getString("billclass_name"));
				vo.setBillclassCode(rs.getString("billclass_code"));
				return vo;			
			}				
		});					
		if(drugBillclass!=null&&drugBillclass.size()>0){
			return drugBillclass;
		}
		return new ArrayList<DrugBillclass>();
	}
}
