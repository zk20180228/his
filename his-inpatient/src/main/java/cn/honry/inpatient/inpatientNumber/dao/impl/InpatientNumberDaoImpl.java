package cn.honry.inpatient.inpatientNumber.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Namespace;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessHospitalbed;
import cn.honry.base.bean.model.InpatientNumber;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inpatient.inpatientNumber.dao.InpatientNumberDao;
import cn.honry.utils.DateUtils;

@Repository("inpatientNumberDao")
@SuppressWarnings({"all"})
public class InpatientNumberDaoImpl extends HibernateEntityDao<InpatientNumber> implements InpatientNumberDao{
	
	@Resource(name="sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory){
		super.setSessionFactory(sessionFactory);
	}
	
	
	/**  
	 * @Description：  初始化
	 * @Author：zhangjin
	 * @CreateDate：2016-11-14
	 * @ModifyRmk：
	 * @param:   
	 * @version 1.0List<InpatientNumber> list=
	 */
	@Override
	public String getInpatientNumber() {
		String mindate="1973";
		String maxdate=new SimpleDateFormat("yyyy").format(DateUtils.addToDate(new Date(), 1, 0, 0));
		String sql=" truncate table T_INPATIENT_NUMBER ";
		int query= (int) this.getSession().createSQLQuery(sql).executeUpdate();
		if(query==0){
			String sql2=" call  pro_T_INPATIENT_NUMBER(to_date('"+mindate+"','yyyy'),to_date('"+maxdate+"','yyyy'),'y')";
			int number=this.getSession().createSQLQuery(sql2).executeUpdate();
			if(number>0){
				return "ok";
			}
		}
		return "no";
	}


	@Override
	public List<InpatientNumber> getNumberList(String medicalrecordId,
			String beganTime, String endTime,String page,String rows) {
		String sql=" select t.NUMBER_ID as id, t.NAME as name,t.IDCARD_NO as idcardNo,t.MEDICALRECORD_ID as medicalrecordId,"
				+ " t.CASE_NO as caseNo,t.INPATIENT_NO as inpatientNo,t.INPATIENT_ID as inpatientId,"
				+ " t.IN_DATE as inDate, t.OUT_DATE as outDate,t.RECALL as recall,t.DEPT_CODE as deptCode,"
				+ "t.BEDINFO_ID as bedinfoId,t.PAYKIND_CODE as paykindCode"
				+ " from T_INPATIENT_NUMBER t where t.stop_flg=0 and t.del_flg=0";
		if(StringUtils.isNotBlank(medicalrecordId)){
			sql+=" and  t.MEDICALRECORD_ID='"+medicalrecordId+"'";
		}
		if(StringUtils.isNotBlank(beganTime)){
			sql+=" and to_char(t.IN_DATE,'yyyy-MM-dd')>='"+beganTime+"'";
		}
		if(StringUtils.isNotBlank(endTime)){
			sql+=" and  to_char(t.OUT_DATE,'yyyy-MM-dd')<='"+endTime+"'";
		}
		int start=Integer.parseInt(page==null?"1":page);
		int count=Integer.parseInt(rows==null?"1":rows);
		List<InpatientNumber> list=this.getSession().createSQLQuery(sql).addScalar("id").addScalar("name").addScalar("idcardNo")
				.addScalar("medicalrecordId").addScalar("caseNo").addScalar("inpatientNo").addScalar("inpatientId")
				.addScalar("inDate",Hibernate.TIMESTAMP).addScalar("outDate",Hibernate.TIMESTAMP)
				.addScalar("recall",Hibernate.INTEGER).addScalar("deptCode").addScalar("bedinfoId").addScalar("paykindCode")
				.setFirstResult((start-1)*count).setMaxResults(count).setResultTransformer(Transformers.aliasToBean(InpatientNumber.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<InpatientNumber>();
	}

	/**  
	 * @Description：  加载数据总条数
	 * @Author：zhangjin
	 * @CreateDate：2016-11-14
	 * @ModifyRmk：
	 * @param:   
	 * @version 1.0getNumberList
	 */
	@Override
	public int getNumberTotal(String medicalrecordId, String beganTime,
			String endTime, String page, String rows) {
		String sql="  from InpatientNumber t where t.stop_flg=0 and t.del_flg=0";
		if(StringUtils.isNotBlank(medicalrecordId)){
			sql+=" and  t.medicalrecordId='"+medicalrecordId+"'";
		}
		if(StringUtils.isNotBlank(beganTime)){
			sql+=" and to_char(t.inDate,'yyyy-MM-dd')>='"+beganTime+"'";
		}
		if(StringUtils.isNotBlank(endTime)){
			sql+=" and  to_char(t.outDate,'yyyy-MM-dd')<='"+endTime+"'";
		}
		int num=super.getTotal(sql);
		if(num>0){
			return num;
		}
		return 0;
	}

	/**  
	 * @Description：获取床号
	 * @Author：zhangjin
	 * @CreateDate：2016-11-18
	 * @ModifyRmk：
	 * @param:   
	 * @version 1.0
	 */
	@Override
	public List<BusinessHospitalbed> getBedinfoId() {
		String hql="select distinct t.bedinfo_id as id,b.bed_name as bedName from t_inpatient_bedinfo t "
				+ "left join T_BUSINESS_HOSPITALBED b on t.bed_id=b.bed_id  where "
				+ "t.del_flg=0 and t.stop_flg=0 and b.del_flg=0 and b.stop_flg=0 ";
		List<BusinessHospitalbed> list=this.getSession().createSQLQuery(hql).addScalar("id").addScalar("bedName")
				.setResultTransformer(Transformers.aliasToBean(BusinessHospitalbed.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}
}
