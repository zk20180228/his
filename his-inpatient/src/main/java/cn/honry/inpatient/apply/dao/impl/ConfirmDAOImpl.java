package cn.honry.inpatient.apply.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InpatientCancelitem;
import cn.honry.base.bean.model.InpatientCancelitemNow;
import cn.honry.base.bean.model.InpatientItemList;
import cn.honry.base.bean.model.InpatientItemListNow;
import cn.honry.base.bean.model.InpatientMedicineList;
import cn.honry.base.bean.model.InpatientMedicineListNow;
import cn.honry.base.bean.model.MatOutput;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inpatient.apply.dao.ConfirmDAO;
import cn.honry.inpatient.apply.vo.ApplyVo;
import cn.honry.utils.HisParameters;
import cn.honry.utils.ShiroSessionUtils;
/**
 * 确认退费信息
 * @author  lyy
 * @createDate： 2016年1月30日 下午4:00:21 
 * @modifier lyy
 * @modifyDate：2016年1月30日 下午4:00:21  
 * @modifyRmk：  
 * @version 1.0
 */
@Repository("confirmDao")
@SuppressWarnings({ "all" })
public class ConfirmDAOImpl extends HibernateEntityDao<InpatientCancelitemNow> implements ConfirmDAO {
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	/**
	 * 查询药品退费信息的总条数
	 * @author  lyy
	 * @createDate： 2016年1月30日 下午4:04:49 
	 * @modifier lyy,liujl
	 * @modifyDate：2016年1月30日 下午4:04:49  2016-6-6 上午09:56:35  
	 * @modifyRmk：  修改获取记录总条数方法
	 * @version 1.0
	 */
	@Override
	public int getTatalDrugConfirm(ApplyVo entity) throws Exception{
		StringBuffer sql=new StringBuffer();
		sql.append("select c.apply_no as applyNo,"  //申请退费主键
				+ "c.inpatient_no as inpatientNo,"  //住院流水号
				+ "c.name as name,"    //患者名称
				+ "c.confirm_flag as confirmFlag," //是否退药     
				+ "c.item_name as drugName,"   //药品名称
				+ "c.item_code as drugCode,"  //药品编码
				+ "c.specs as specs,"  //规格
				+ "c.sale_price as price,"   //价格
				+ "c.qty as qty,"  //数量
				+ "c.price_unit as drugPackagingunit," //单位
				+ "c.sale_price * c.qty as chargeMoney,"    //金额
				+ "c.oper_date as execDate,"   //记账日期
				+ "m.senddrug_flag as babyFlag,"  //是否发药
				+ "m.recipe_no as recipeNo,"    //处方号
				+ "m.sequence_no as sequenceNo ,"   //处方流水号
				+ "c.drug_flag as drugFlag "   //药品状态
				+ "from "+HisParameters.HISPARSCHEMAHISUSER+"t_inpatient_cancelitem_now c "
				+ "join "+HisParameters.HISPARSCHEMAHISUSER+"t_inpatient_medicinelist_now m on m.inpatient_no=c.inpatient_no "
				+ "where m.del_flg=0 and c.del_flg=0 and c.inpatient_no like '%"+entity.getInpatientNo()+"%'");
		sql.append(" and c.charge_flag=0 and c.drug_flag=1 order by c.createtime desc");
		return super.getSqlTotal(sql.toString());
	}
	
	/**
	 * 查询药品退费信息
	 * @author  lyy
	 * @createDate： 2016年1月30日 下午4:05:36 
	 * @modifier lyy
	 * @modifyDate：2016年1月30日 下午4:05:36  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<ApplyVo> getPageDrugConfirm(ApplyVo entity, String page, String rows) throws Exception{
		StringBuffer sql=new StringBuffer();
		sql.append("select c.apply_no as applyNo,"  //申请退费主键
				+ "c.inpatient_no as inpatientNo,"  //住院流水号
				+ "c.name as name,"    //患者名称
				+ "c.confirm_flag as confirmFlag," //是否退药     
				+ "c.item_name as drugName,"   //药品名称
				+ "c.item_code as drugCode,"  //药品编码
				+ "c.specs as specs,"  //规格
				+ "c.sale_price as price,"   //价格
				+ "c.qty as qty,"  //数量
				+ "c.price_unit as drugPackagingunit,"  //单位
				+ "c.sale_price * c.qty as chargeMoney,"    //金额
				+ "c.oper_date as execDate,"   //记账日期
				+ "m.senddrug_flag as babyFlag,"  //是否发药
				+ "m.recipe_no as recipeNo,"    //处方号
				+ "m.sequence_no as sequenceNo ,"   //处方流水号
				+" c.drug_flag as drugFlag "   //药品状态
				+ "from "+HisParameters.HISPARSCHEMAHISUSER+"t_inpatient_cancelitem_now c "
				+ "join "+HisParameters.HISPARSCHEMAHISUSER+"t_inpatient_medicinelist_now m on m.inpatient_no=c.inpatient_no "
				+ "where m.del_flg=0 and c.del_flg=0 and c.inpatient_no like '%"+entity.getInpatientNo()+"%'");
		sql.append(" and c.charge_flag=0 and c.drug_flag=1 order by c.createtime desc");
		SQLQuery query=this.getSession().createSQLQuery(sql.toString());
		query.addScalar("applyNo")   //申请退费主键
	     .addScalar("inpatientNo")   //住院流水号
	     .addScalar("name")   //患者名称
	     .addScalar("confirmFlag")   //是否退药
	     .addScalar("drugName")   //药品名称
	     .addScalar("drugCode")   //药品编号
	     .addScalar("specs")	     //规格
	     .addScalar("price",Hibernate.DOUBLE)  //价格
	     .addScalar("qty",Hibernate.DOUBLE)   //数量
	     .addScalar("drugPackagingunit")   //单位
	     .addScalar("chargeMoney",Hibernate.DOUBLE)  //金额
	     .addScalar("execDate",Hibernate.TIMESTAMP)  //记账日期
	     .addScalar("babyFlag")    //是否发药
	     .addScalar("recipeNo")   //处方号
	     .addScalar("sequenceNo",Hibernate.INTEGER)  //处方流水号
	     .addScalar("drugFlag",Hibernate.INTEGER);   //药品状态
		query.setResultTransformer(Transformers.aliasToBean(ApplyVo.class));
		int state=Integer.parseInt(page==null?"1":page);
		int count=Integer.parseInt(rows==null?"20":rows);
		query.setFirstResult((state-1)*count).setMaxResults(count);
		return query.list();
	}
	@Override
	/**
	 * @Modifier：liujl
	 * @ModifyDate：2016-6-6 上午09:56:35  
	 * @ModifyRmk：  修改获取记录总条数方法
	 */	
	public int getTatalNotDrugConfirm(ApplyVo entity) throws Exception{
		StringBuffer sql=new StringBuffer();
		sql.append("select c.apply_no as applyNo,"  //申请退费主键
				+ "c.inpatient_no as inpatientNo,"  //住院流水号
				+ "c.name as name,"    //患者名称
				+ "c.confirm_flag as confirmFlag," //是否退药     
				+ "c.item_name as drugName,"   //药品名称
				+ "c.item_code as drugCode,"  //药品编码
				+ "c.specs as specs,"  //规格
				+ "c.sale_price as price,"   //价格
				+ "c.qty as qty,"  //数量
				+ "c.price_unit as drugPackagingunit,"  //单位
				+ "c.sale_price * c.qty as chargeMoney,"    //金额
				+ "c.oper_date as execDate,"   //记账日期
				+ "i.Baby_Flag as  babyFlag,"  //是否发药
				+ "i.recipe_no as recipeNo,"    //处方号
				+ "i.sequence_no as sequenceNo,"   //处方流水号
				+" c.drug_flag as drugFlag "   //药品状态
				+ "from "+HisParameters.HISPARSCHEMAHISUSER+"t_inpatient_cancelitem_now c "
				+ "join "+HisParameters.HISPARSCHEMAHISUSER+"t_inpatient_itemlist_now i on c.inpatient_no = i.inpatient_no "
				+ "where c.del_flg=0 and i.del_flg=0 and c.inpatient_no like '%"+entity.getInpatientNo()+"%'");
		sql.append(" and c.charge_flag=0 and c.drug_flag=2 order by c.createtime desc");
		return super.getSqlTotal(sql.toString());
	}
	@Override
	public List<ApplyVo> getPageNotDrugConfirm(ApplyVo entity, String page, String rows) throws Exception{
		StringBuffer sql=new StringBuffer();
		sql.append("select c.apply_no as applyNo,"  //申请退费主键
				+ "c.inpatient_no as inpatientNo,"  //住院流水号
				+ "c.name as name,"    //患者名称
				+ "c.confirm_flag as confirmFlag," //是否退药     
				+ "c.item_name as drugName,"   //药品名称
				+ "c.item_code as drugCode,"  //药品编码
				+ "c.specs as specs,"  //规格
				+ "c.sale_price as price,"   //价格
				+ "c.qty as qty,"  //数量
				+ "c.price_unit as drugPackagingunit,"  //单位
				+ "c.sale_price * c.qty as chargeMoney,"    //金额
				+ "c.oper_date as execDate,"  //记账日期
				+ "i.Baby_Flag as  babyFlag,"  //是否发药
				+ "i.recipe_no as recipeNo,"    //处方号
				+ "i.sequence_no as sequenceNo,"   //处方流水号
				+" c.drug_flag as drugFlag "   //药品状态
				+ "from "+HisParameters.HISPARSCHEMAHISUSER+"t_inpatient_cancelitem_now c "
				+ "join "+HisParameters.HISPARSCHEMAHISUSER+"t_inpatient_itemlist_now i on c.inpatient_no = i.inpatient_no "
				+ "where c.del_flg=0 and i.del_flg=0 and c.inpatient_no like '%"+entity.getInpatientNo()+"%'");
		sql.append(" and c.charge_flag=0 and c.drug_flag=2 order by c.createtime desc");
		SQLQuery query=this.getSession().createSQLQuery(sql.toString());
		query.addScalar("applyNo")   //申请退费主键
	     .addScalar("inpatientNo")   //住院流水号
	     .addScalar("name")   //患者名称
	     .addScalar("confirmFlag")   //是否退药
	     .addScalar("drugName")   //药品名称
	     .addScalar("drugCode")   //药品编号
	     .addScalar("specs")	     //规格
	     .addScalar("price",Hibernate.DOUBLE)  //价格
	     .addScalar("qty",Hibernate.DOUBLE)   //数量
	     .addScalar("drugPackagingunit")   //单位
	     .addScalar("chargeMoney",Hibernate.DOUBLE) //金额
	     .addScalar("execDate",Hibernate.TIMESTAMP)  //记账日期
	     .addScalar("babyFlag")    //是否发药
	     .addScalar("recipeNo")   //处方号
	     .addScalar("sequenceNo",Hibernate.INTEGER)  //处方流水号
	     .addScalar("drugFlag",Hibernate.INTEGER);   //药品状态
		query.setResultTransformer(Transformers.aliasToBean(ApplyVo.class));
		int state=Integer.parseInt(page==null?"1":page);
		int count=Integer.parseInt(rows==null?"20":rows);
		query.setFirstResult((state-1)*count).setMaxResults(count);
		return query.list();
	}
	@Override
	public String confirmBack(String ids,String userId, String deptId,String userName, String deptName) throws Exception{
		int c = getSession().createQuery("UPDATE InpatientCancelitemNow d SET chargeFlag=1,operCode='"+userId+"',operName='"+userName+"',operDpcd='"+deptId+"',operDpcdName='"+deptName+"',operDate=?  WHERE d.id IN ('"+ids+"')")
			.setTimestamp(0, new Date())
			.executeUpdate();
		if(c>0){
			return "ok";
		}else{
			return "error";
		}
	}
	@Override
	public List<InpatientCancelitemNow> getChildByIds(String ids) throws Exception{
		String hql="FROM InpatientCancelitemNow d WHERE d.del_flg = 0";
		if(ids.contains(",")){
			String[] id = ids.split(",");
		}
		List<InpatientCancelitemNow> litemList = super.find(hql, null);
		if(litemList!=null&&litemList.size()>0){
			return litemList;
		}
		return new ArrayList<InpatientCancelitemNow>();
	}
	
/****************************************************************   分割线       *************************************************************************************/
	
	
	@Override
	public InpatientMedicineListNow getChildByRecipe(String recipeNo, Integer sequenceNo) throws Exception{
		
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append(" select t.drug_code,t.inpatient_no,t.nurse_cell_code,t.recipe_deptcode,t.execute_deptcode,t.medicalteam_code,t.recipe_doccode,t.fee_code ,");
		sBuffer.append(" t.center_code,t.home_made_flag,t.current_unit,t.noback_num,t.senddrug_flag,t.baby_flag,t.qty,t.pact_code,t.recipe_no,t.sequence_no ,t.OPERATION_ID ");
		sBuffer.append(" from T_INPATIENT_MEDICINELIST_NOW t where t.stop_flg=0 and t.del_flg=0 and t.recipe_no=:recipeNo and t.sequence_no=:sequenceNo AND T.EXT_FLAG1=0 ");
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("recipeNo", recipeNo);
		paramMap.put("sequenceNo", sequenceNo);
		InpatientMedicineListNow mlist = namedParameterJdbcTemplate.queryForObject(sBuffer.toString(), paramMap, new RowMapper<InpatientMedicineListNow>(){
			@Override
			public InpatientMedicineListNow mapRow(ResultSet rs, int i) throws SQLException {
				InpatientMedicineListNow vo = new InpatientMedicineListNow();
				vo.setDrugCode(rs.getString("drug_code"));
				vo.setInpatientNo(rs.getString("inpatient_no"));
				vo.setNurseCellCode(rs.getString("nurse_cell_code"));
				vo.setRecipeDeptCode(rs.getString("recipe_deptcode"));
				vo.setExecuteDeptCode(rs.getString("execute_deptcode"));
				vo.setMedicalteamCode(rs.getString("medicalteam_code"));
				vo.setRecipeDocCode(rs.getString("recipe_doccode"));
				vo.setFeeCode(rs.getString("fee_code"));
				vo.setCenterCode(rs.getString("center_code"));
				vo.setHomeMadeFlag(rs.getInt("home_made_flag"));
				vo.setCurrentUnit(rs.getString("current_unit"));
				vo.setNobackNum(rs.getDouble("noback_num"));
				vo.setSenddrugFlag(rs.getInt("senddrug_flag"));
				vo.setBabyFlag(rs.getInt("baby_flag"));
				vo.setQty(rs.getDouble("qty"));
				vo.setPactCode(rs.getString("pact_code"));
				vo.setRecipeNo(rs.getString("recipe_no"));
				vo.setSequenceNo(rs.getInt("sequence_no"));
				vo.setOperationId(rs.getString("OPERATION_ID"));
				return vo;
			}
		});
		return mlist;
	}
	
	@Override 
	public InpatientItemListNow getItemListByRecipe(String recipeNo, Integer sequenceNo) throws Exception {
		
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append(" select t.item_code, t.inpatient_no,t.nurse_cell_code,t.recipe_deptcode,t.execute_deptcode,t.medicalteam_code,t.recipe_doccode,t.fee_code,");
		sBuffer.append(" t.center_code,t.current_unit,t.noback_num,t.send_flag,t.baby_flag,t.qty,t.pact_code,t.item_flag,t.recipe_no,t.sequence_no,t.OPERATION_ID  ");
		sBuffer.append(" from t_inpatient_itemlist_now t where t.stop_flg=0 and t.del_flg=0 and t.recipe_no=:recipeNo and t.sequence_no=:sequenceNo AND T.EXT_FLAG1=0");
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("recipeNo", recipeNo);
		paramMap.put("sequenceNo", sequenceNo);
		InpatientItemListNow mlist = namedParameterJdbcTemplate.queryForObject(sBuffer.toString(), paramMap, new RowMapper<InpatientItemListNow>(){
			@Override
			public InpatientItemListNow mapRow(ResultSet rs, int i) throws SQLException {
				InpatientItemListNow vo = new InpatientItemListNow();
				vo.setItemCode(rs.getString("item_code"));
				vo.setInpatientNo(rs.getString("inpatient_no"));
				vo.setNurseCellCode(rs.getString("nurse_cell_code"));
				vo.setRecipeDeptcode(rs.getString("recipe_deptcode"));
				vo.setExecuteDeptcode(rs.getString("execute_deptcode"));
				vo.setMedicalteamCode(rs.getString("medicalteam_code"));
				vo.setRecipeDoccode(rs.getString("recipe_doccode"));
				vo.setFeeCode(rs.getString("fee_code"));
				vo.setCenterCode(rs.getString("center_code"));
				vo.setCurrentUnit(rs.getString("current_unit"));
				vo.setNobackNum(rs.getDouble("noback_num"));
				vo.setSendFlag(rs.getInt("send_flag"));
				vo.setBabyFlag(rs.getInt("baby_flag"));
				vo.setQty(rs.getDouble("qty"));
				vo.setPactCode(rs.getString("pact_code"));
				vo.setItemFlag(rs.getInt("item_flag"));
				vo.setRecipeNo(rs.getString("recipe_no"));
				vo.setSequenceNo(rs.getInt("sequence_no"));
				vo.setOperationId(rs.getString("OPERATION_ID"));
				return vo;
			}
		});
		return mlist;
		
	}
	@Override
	public MatOutput getOutputByRecAndSeq(String recipeNo, Integer sequenceNo) throws Exception{
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append(" select t.id,t.recipe_no,t.sequence_no from t_mat_output t where t.stop_flg=0 and t.del_flg=0 and t.trans_type = 1 ");
		sBuffer.append(" and t.recipe_no=:recipeNo and  t.sequence_no=:sequenceNo ");
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("recipeNo", recipeNo);
		paramMap.put("sequenceNo", sequenceNo);
		try {
			MatOutput matOutput = namedParameterJdbcTemplate.queryForObject(sBuffer.toString(), paramMap, new RowMapper<MatOutput>(){
				@Override
				public MatOutput mapRow(ResultSet rs, int i) throws SQLException {
					MatOutput vo = new MatOutput();
					vo.setId(rs.getString("id"));
					vo.setRecipeNo(rs.getString("recipe_no"));
					vo.setSequenceNo(rs.getObject("sequence_no")==null?null:rs.getInt("sequence_no"));
					return vo;
				}
			});
			return matOutput;
		} catch (Exception e) {
			return null;
		}
	}
	@Override
	public void updateMatOutput(MatOutput output) throws Exception{
			StringBuffer sb=new StringBuffer();
			String user=ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
			sb.append("update  t_mat_output t set t.recipe_no =?,t.sequence_no=? ,t.UPDATEUSER = ?, t.UPDATETIME = ?  where  t.id=? ");
			Object args[] = new Object[]{output.getRecipeNo(),output.getSequenceNo(),user,new Date(), output.getId()}; 
		    jdbcTemplate.update(sb.toString(),args);  
		
	}

	@Override
	public void updateOperApply(List<String> newList) throws Exception{
		StringBuffer sb=new StringBuffer();
		String user=ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
	    for(int i=0;i<newList.size();i++){
	    	sb.append("update  t_operation_apply t set t.YNFEE =0 ,t.UPDATEUSER = ?, t.UPDATETIME = ?  where  t.op_id =?  ");
			Object args[] = new Object[]{user,new Date(),newList.get(i)}; 
		    jdbcTemplate.update(sb.toString(),args);  
		}
	}

	@Override
	public void updateOperRecord(List<String> newList) throws Exception{
		StringBuffer sb=new StringBuffer();
		String user=ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		for(int i=0;i<newList.size();i++){
			sb.append("update  T_OPERATION_RECORD t set t.YNFEE =0,t.UPDATEUSER = ?, t.UPDATETIME = ?  where  t.operation_id =?  ");
			Object args[] = new Object[]{user,new Date(), newList.get(i)}; 
		    jdbcTemplate.update(sb.toString(),args);  
		}
		
		
	}

	@Override
	public void updateOperApply(List<String> newList, String empJobNo)
			throws Exception {
		StringBuffer sb=new StringBuffer();
	    for(int i=0;i<newList.size();i++){
	    	sb.append("update  t_operation_apply t set t.YNFEE =0 ,t.UPDATEUSER = ?, t.UPDATETIME = ?  where  t.op_id =?  ");
			Object args[] = new Object[]{empJobNo,new Date(),newList.get(i)}; 
		    jdbcTemplate.update(sb.toString(),args);  
		}
		
	}
	@Override
	public void updateOperRecord(List<String> newList,String empJobNo) throws Exception{
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<newList.size();i++){
			sb.append("update  T_OPERATION_RECORD t set t.YNFEE =0,t.UPDATEUSER = ?, t.UPDATETIME = ?  where  t.operation_id =?  ");
			Object args[] = new Object[]{empJobNo,new Date(), newList.get(i)}; 
		    jdbcTemplate.update(sb.toString(),args);  
		}
		
		
	}
	
}
