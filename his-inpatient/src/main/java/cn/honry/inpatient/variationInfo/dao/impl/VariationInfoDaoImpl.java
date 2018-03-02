package cn.honry.inpatient.variationInfo.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessBedward;
import cn.honry.base.bean.model.BusinessIcd10;
import cn.honry.base.bean.model.CpVariation;
import cn.honry.base.bean.model.ModelVsItem;
import cn.honry.base.bean.model.PathApply;
import cn.honry.base.bean.model.PathVsIcd;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inpatient.clinicalPathVsICD.dao.ClinicalPathVsICDDao;
import cn.honry.inpatient.variationInfo.dao.VariationInfoDao;
import cn.honry.inpatient.variationInfo.vo.ComboxVo;

@Repository(value="variationInfoDao")
@SuppressWarnings({"all"})
public class VariationInfoDaoImpl extends HibernateEntityDao<CpVariation> implements VariationInfoDao{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<SysDepartment> listTree() {
		String hql="From SysDepartment where deptType='I' and del_flg=0 and stop_flg=0 order by deptOrder";
		List<SysDepartment> listDeptContact=super.find(hql, null);
		if(listDeptContact!=null&&listDeptContact.size()>0){
			return listDeptContact;
		}
		return new ArrayList<SysDepartment>();
	}

	@Override
	public List<PathApply> findPathApply() {
		String sql="select t.apply_no as applyNo,t.inpatient_no as inpatientNo,t.medicalrecord_id as medicalrecordId,t.cp_id as cpId,"
				+ "t.version_no as versionNo,t.apply_code as applyCode,i.patient_name as patientName "
				+ "from T_PATH_APPLY t left join t_inpatient_info_now i on i.inpatient_no = t.inpatient_no "
				+ "where t.apply_type ='1' and t.apply_status ='1'";
		SQLQuery query = super.getSession().createSQLQuery(sql);
		query.addScalar("applyNo").addScalar("inpatientNo")
		.addScalar("medicalrecordId").addScalar("cpId")
		.addScalar("versionNo").addScalar("applyCode")
		.addScalar("patientName");
		List<PathApply> pathApply = query.setResultTransformer(Transformers.aliasToBean(PathApply.class)).list();
		if(pathApply!=null&&pathApply.size()>0){
			return pathApply;
		}
		return new ArrayList<PathApply>();
	}

	@Override
	public Integer queryPathVsIcdNum(String inpatientNo) {
		StringBuffer buffer=new StringBuffer();
		buffer.append("select count(1) from T_CP_VARIATION t ");
		buffer.append(" where t.DEL_FLG =0 and t.STOP_FLG=0 ");
		if(StringUtils.isNotBlank(inpatientNo)){
			buffer.append(" and t.INPATIENT_NO = '"+inpatientNo+"'");
		}
		Integer total = ((BigDecimal) super.getSession().createSQLQuery(buffer.toString()).uniqueResult()).intValue();
		return total;
	}

	@Override
	public List<CpVariation> queryPathVsIcdList(String inpatientNo, String page, String rows) {
		List<CpVariation> list = new ArrayList<CpVariation>();
		int p = StringUtils.isNotBlank(page)?Integer.valueOf(page):1;
		int r = StringUtils.isNotBlank(rows)?Integer.valueOf(rows):20;
		StringBuffer buffer=new StringBuffer();
		buffer.append("select * from (select row_.*, rownum rownum_ from (");
		buffer.append("select t.ID as id,t.INPATIENT_NO as inpatientNo,t.MEDICALRECORD_ID as medicalrecordId, ");
		buffer.append("t.STAGE_ID as stageId,t.VARIATION_CODE as variationCode,t.VARIATION_DATE as variationDate, ");
		buffer.append("t.VARIATION_DIRECTION as variationDirection,t.VARIATION_REASON as variationReason,t.VARIATION_FACTOR_CODE as variationFactorCode from T_CP_VARIATION t ");
		buffer.append(" where t.DEL_FLG =0 and t.STOP_FLG=0 ");
		if(StringUtils.isNotBlank(inpatientNo)){
			buffer.append(" and t.INPATIENT_NO = '"+inpatientNo+"'");
		}
		buffer.append(") row_ where rownum <= " + (p * r) + ") where rownum_ > " + ((p - 1) * r) + "");
		SQLQuery query = super.getSession().createSQLQuery(buffer.toString());
		query.addScalar("id").addScalar("inpatientNo")
		.addScalar("medicalrecordId").addScalar("stageId")
		.addScalar("variationCode").addScalar("variationDate",Hibernate.TIMESTAMP)
		.addScalar("variationDirection").addScalar("variationReason").addScalar("variationFactorCode");
		list = query.setResultTransformer(Transformers.aliasToBean(CpVariation.class)).list();
		return list;
	}

	@Override
	public List<ComboxVo> queryDictionary(String q, String type) {
		List<ComboxVo> list = new ArrayList<ComboxVo>();
		StringBuffer buffer=new StringBuffer();
		if("variationName".equals(type)){
			buffer.append("select t.variation_code as code,t.variation_name as name from T_Variation_dict t where t.delect_flag =0 and t.stop_flag =0");
			if(StringUtils.isNotBlank(q)){
				buffer.append(" and t.variation_name like '%"+q+"%'");
			}
		}else if("variationFactorCode".equals(type)){
			buffer.append("select t.code_encode as code,t.code_name as name from t_business_dictionary t where code_type = 'cpVit'");
		}
		SQLQuery query = super.getSession().createSQLQuery(buffer.toString());
		query.addScalar("code").addScalar("name");
		list = query.setResultTransformer(Transformers.aliasToBean(ComboxVo.class)).list();
		return list;
	}

	@Override
	public List<ComboxVo> queryStageId(String q, String cpId, String versionNo) {
		List<ComboxVo> list = new ArrayList<ComboxVo>();
		StringBuffer buffer=new StringBuffer();
		buffer.append("select t.stage_id as code,c.stage_name as name from T_CPWay_Plan t left join T_CPStage c on c.stage_id = t.stage_id ");
		buffer.append(" where t.version_no='"+versionNo+"' and t.cp_id ='"+cpId+"'");
		SQLQuery query = super.getSession().createSQLQuery(buffer.toString());
		query.addScalar("code").addScalar("name");
		list = query.setResultTransformer(Transformers.aliasToBean(ComboxVo.class)).list();
		return list;
	}

	@Override
	public PathApply getPathApplyByInNo(String inpatientNo) {
		String sql="select t.apply_no as applyNo,t.inpatient_no as inpatientNo,t.medicalrecord_id as medicalrecordId,t.cp_id as cpId,"
				+ "t.version_no as versionNo,t.apply_code as applyCode "
				+ "from T_PATH_APPLY t "
				+ "where t.apply_type ='1' and t.apply_status ='1' and t.inpatient_no ='"+inpatientNo+"'";
		SQLQuery query = super.getSession().createSQLQuery(sql);
		query.addScalar("applyNo").addScalar("inpatientNo")
		.addScalar("medicalrecordId").addScalar("cpId")
		.addScalar("versionNo").addScalar("applyCode");
		List<PathApply> pathApply = query.setResultTransformer(Transformers.aliasToBean(PathApply.class)).list();
		if(pathApply!=null&&pathApply.size()>0){
			return pathApply.get(0);
		}
		return new PathApply();
	}

	@Override
	public void batchUpDe(String id) {
		StringBuilder sb = new StringBuilder();
		sb.append("update T_CP_VARIATION  set DEL_FLG = 1  where ID ='"+id+"'");
		SQLQuery query = getSession().createSQLQuery(sb.toString());
		query.executeUpdate();
	}

	@Override
	public List<SysDepartment> getDeptName(String queryName) {
		String hql="From SysDepartment d where d.del_flg=0 and d.stop_flg=0 and d.deptType='I'";
		if(queryName!=null){    //根据部门名称、部门code、五笔码、拼音码、自定义码
			hql+=" and (d.deptName like '%"+queryName+"%' or d.deptCode like '%"+queryName+"%' "
				+ "or d.deptWb like '%"+queryName.toUpperCase()+"%'  or d.deptPinyin like '%"+queryName.toUpperCase()+"%' "
				+ "or d.deptInputcode like '%"+queryName.toUpperCase()+"%')";
			hql+="order by abs(length(d.deptName) - length('"+queryName+"')),abs(length(d.deptCode) - length('"+queryName+"')),"
				+ "abs(length(d.deptWb) - length('"+queryName+"')),"
				+ "abs(length(d.deptPinyin) - length('"+queryName+"')),abs(length(d.deptInputcode) - length('"+queryName+"'))";
		}
		List<SysDepartment> listDeptContact=super.find(hql, null);
		if(listDeptContact!=null&&listDeptContact.size()>0){
			return listDeptContact;
		}
		return new ArrayList<SysDepartment>();
	}
}
