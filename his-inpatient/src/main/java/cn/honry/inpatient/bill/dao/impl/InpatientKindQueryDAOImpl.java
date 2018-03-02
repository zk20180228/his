package cn.honry.inpatient.bill.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.DrugBillclass;
import cn.honry.base.bean.model.DrugBilllist;
import cn.honry.base.bean.model.InpatientKind;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inpatient.bill.dao.InpatientKindQueryDAO;
import cn.honry.utils.JSONUtils;

import com.google.gson.reflect.TypeToken;

@Repository("inpatientKindQueryDAO")
@SuppressWarnings({ "all" })
public class InpatientKindQueryDAOImpl extends HibernateEntityDao<InpatientKind> implements InpatientKindQueryDAO {
	@Resource(name="sessionFactory")
	//为父类superSessionFactory注入sessionFactory的值
	public void setsuperSessionFactory(SessionFactory sessionFactory){
		super.setSessionFactory(sessionFactory);
	}
	
	private String inpatientkind;
	
	/**
	 * 查询所有
	 * @param inpatientKind 参数  传递到实体 
	 * @return  List
	 */
	@Override
	public List searchInpatientKindList(InpatientKind inpatientKind) {
		String hql="from InpatientKind as i where i.del_flg=0 and i.stop_flg=0";
		 hql=this.joinhql(inpatientKind,hql);
		List<InpatientKind> list=super.getPage(hql, inpatientKind.getPage(), inpatientKind.getRows());
		return list;
	}
	/**
	 * 获得到总条数
	 * @param InpatientKind 参数  传递到实体 
	 * @return  int
	 */
	@Override
	public int getTotal(InpatientKind inpatientKind) {
		String hql="from InpatientKind i where i.del_flg=0 and i.stop_flg=0";
	    hql=this.joinhql(inpatientKind,hql);
		int total=super.getTotal(hql);
		return total;
	}

	/**
	 * 下拉框查询
	 * @param code 参数  查询条件的参数
	 * @return List<InpatientKind>
	 */
	@Override
	public List<InpatientKind> likeSearch(String code) {
		String hql=" from InpatientKind cs  where cs.del_flg=0 and cs.stop_flg=0";
		if(StringUtils.isNotBlank(code)){
			hql = hql+" and (  cs.typeName like '%"+code+"%') ";
		}
		List<InpatientKind> list=super.findByObjectProperty(hql, null);
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<InpatientKind>();
	}
	/**
	 * 查询要用的的hql语句
	 * @param codeOrdercategory 参数  传递到实体 
	 * @param hql 执行的hql语句
	 * @return String
	 * @modifiedTime 2015-06-24
	 * @modifiedContent 查询条件改成一个
	 */
	public String  joinhql(InpatientKind inpatientKind,String hql){
		
		if(inpatientKind!=null){
			if(StringUtils.isNotBlank(inpatientKind.getTypeName())){
				String queryName = inpatientKind.getTypeName().toUpperCase();
				hql+=" and (upper(i.typeName) like '%"+queryName+"%'"
					+")";
			}
			
		}
		return hql;
	}
	@Override
	public List<InpatientKind> getKind(String code) {
		String hql=" from InpatientKind cu  where cu.del_flg=0 and cu.stop_flg=0";
		if(StringUtils.isNotBlank(code)){
			hql = hql+" and (  cu.typeName like '%"+code+"%') ";
			hql = hql+" or (  cu.typeCode like '%"+code+"%') ";
		}
		hql = hql+" order by cu.typeCode ";
		List<InpatientKind> list=super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<InpatientKind>();
	}
	@Override
	public List<DrugBilllist> queryBilllist(String BillclassId) {
		String hql=" from DrugBilllist cu  where cu.del_flg=0 and cu.stop_flg=0 and cu.drugBillclass.id=? ";
		List<DrugBilllist> list=super.find(hql, BillclassId);
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<DrugBilllist>();
	}
	@Override
	public List<DrugBillclass> queryBillclass(String BillclassId) {
		String hql=" from DrugBillclass cu  where cu.del_flg=0 and cu.stop_flg=0 and cu.id='"+BillclassId+"'";
		List<DrugBillclass> list=super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<DrugBillclass>();
	}
	@Override
	public void UpdateBillclass(String infoJson) {
		List<DrugBillclass> modelList = null;
		try {
			infoJson=infoJson.replaceAll("\"[a-zA-Z0-9]+\":\"\",", "")
					.replaceAll(",\"[a-zA-Z0-9]+\":\"\"", "")
					.replaceAll(",\"[a-zA-Z0-9]+\":\"\",", "");
			modelList =JSONUtils.fromJson(infoJson,  new TypeToken<List<DrugBillclass>>(){}, "yyyy-MM-dd hh:mm:ss");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String classId = modelList.get(0).getId();
		String billclassName = modelList.get(0).getBillclassName();
		String billclassAttr = modelList.get(0).getBillclassAttr();
		String printType = modelList.get(0).getPrintType();
		int validFlag = modelList.get(0).getValidFlag();
		String mark = modelList.get(0).getMark();
		String sql="update t_drug_billclass set billclass_name=?,billclass_attr=?,print_type=?,valid_flag=?,mark=? where billclass_id= ? ";
		this.getSession().createSQLQuery(sql).setString(0, billclassName).setString(1, billclassAttr).setString(2, printType)
		.setInteger(3, validFlag).setString(4, mark).setString(5, classId).executeUpdate();
	}
	@Override
	public void UpdateBilllist(Map<String, String> parameterMap,String infolistId) {
		String typeCode = parameterMap.get("yzlbid");
		String usageCode = parameterMap.get("yzyfid");
		String drugType = parameterMap.get("yplbid");
		String drugQuality = parameterMap.get("ypxzid");
		String doseModelCode = parameterMap.get("ypyjid");
		String sql="update t_drug_billlist set type_code=?,usage_code=?,drug_type=?,drug_quality=?,dose_model_code=? where id= ? ";
		this.getSession().createSQLQuery(sql).setString(0, typeCode).setString(1, usageCode).setString(2, drugType)
		.setString(3, drugQuality).setString(4, doseModelCode).setString(5, infolistId).executeUpdate();
	}
	@Override
	public void deleteBilllist(String BillclassId) {
		String sql="delete from t_drug_billlist where billclass_id = ?";
		this.getSession().createSQLQuery(sql).setString(0, BillclassId).executeUpdate();
	}
	@Override
	public List<DrugBilllist> queryTypeCode(String billssId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select distinct type_code as typeCode from T_DRUG_BILLLIST t where t.del_flg=0 and t.stop_flg=0 and t.billclass_id='"+billssId+"'"
				+ " order by type_code nulls last");
		SQLQuery queryObject = this.getSession().createSQLQuery(sql.toString()).addScalar("typeCode");
		List<DrugBilllist> list = queryObject.setResultTransformer(Transformers.aliasToBean(DrugBilllist.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<DrugBilllist>();
	}
	@Override
	public List<DrugBilllist> queryDrugType(String billssId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select distinct drug_type as drugType from T_DRUG_BILLLIST t where t.del_flg=0 and t.stop_flg=0 and t.billclass_id='"+billssId+"' "
				+ "order by drug_type nulls last");
		SQLQuery queryObject = this.getSession().createSQLQuery(sql.toString()).addScalar("drugType");
		List<DrugBilllist> list = queryObject.setResultTransformer(Transformers.aliasToBean(DrugBilllist.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<DrugBilllist>();
	}
	@Override
	public List<DrugBilllist> queryDoseModelCode(String billssId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select distinct dose_model_code as doseModelCode from T_DRUG_BILLLIST t where t.del_flg=0 and t.stop_flg=0 and t.billclass_id='"+billssId+"' "
				+ "order by dose_model_code nulls last");
		SQLQuery queryObject = this.getSession().createSQLQuery(sql.toString()).addScalar("doseModelCode");
		List<DrugBilllist> list = queryObject.setResultTransformer(Transformers.aliasToBean(DrugBilllist.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<DrugBilllist>();
	}
	@Override
	public List<DrugBilllist> queryUsageCode(String billssId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select distinct usage_code as usageCode from T_DRUG_BILLLIST t where t.del_flg=0 and t.stop_flg=0 and t.billclass_id='"+billssId+"' "
				+ "order by usage_code nulls last");
		SQLQuery queryObject = this.getSession().createSQLQuery(sql.toString()).addScalar("usageCode");
		List<DrugBilllist> list = queryObject.setResultTransformer(Transformers.aliasToBean(DrugBilllist.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<DrugBilllist>();
	}
	@Override
	public List<DrugBilllist> queryDrugQuality(String billssId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select distinct drug_quality as drugQuality from T_DRUG_BILLLIST t where t.del_flg=0 and t.stop_flg=0 and t.billclass_id='"+billssId+"' "
				+ "order by drug_quality nulls last");
		SQLQuery queryObject = this.getSession().createSQLQuery(sql.toString()).addScalar("drugQuality");
		List<DrugBilllist> list = queryObject.setResultTransformer(Transformers.aliasToBean(DrugBilllist.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<DrugBilllist>();
	}
	@Override
	public List<BusinessDictionary> likeSearch(String type, String code) {
		String hql=" from BusinessDictionary cs  where cs.del_flg=0 and cs.stop_flg=0 and cs.type='"+type+"'";
		if(StringUtils.isNotBlank(code)){
			hql = hql+" and (  cs.name like '%"+code+"%' or cs.pinyin like '%"+code+"%' or cs.wb like '%"+code+"%' or cs.inputCode like '%"+code+"%' or cs.encode like '%"+code+"%') ";
		}
		hql=hql+" order by cs.order";
		List<BusinessDictionary> list=super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<BusinessDictionary>();
	}
}
