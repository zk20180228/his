package cn.honry.statistics.drug.admissionStatistics.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.statistics.drug.admissionStatistics.dao.AdmissionStatisticsDAO;
import cn.honry.statistics.drug.admissionStatistics.vo.AdmissionStatisticsVo;

/***
 * 用药统计DAO实现层
 * @Description:
 * @author: tangfeishuai
 * @CreateDate: 2016年6月22日 
 * @version 1.0
 */
@Repository("admissionStatisticsDAO")
@SuppressWarnings({ "all" })
public class AdmissionStatisticsDaoImpl extends HibernateEntityDao<AdmissionStatisticsVo> implements AdmissionStatisticsDAO{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Override
	public String getHql(String beginTime, String endTime, String deptCode, String storageCode, String drugType,
			String outType) {
		StringBuffer sb = new StringBuffer();
		sb.append("select i.drug_basicinputcode as drugBasicCode,i.drug_biddingcode as drugBiddingCode,i.drug_code AS drugId,i.drug_commonname AS drugName,decode(o.out_state,'0','申请','1','审批','2','核准') AS outState, ");
		sb.append("  decode(o.op_type,'1','门诊摆药','2','内部入库','3','门诊退药','4','住院摆药','5','住院退药')  AS optype,i.drug_spec AS drugSpec,round((sum(o.out_num)/i.drug_packagingnum),2) AS num,");
		sb.append(" t.code_name AS drugPackgingUnit,round(o.retail_price, 2) * round((sum(o.out_num)/i.drug_packagingnum),2) AS sum,round(o.retail_price,2) as retailPrice ");
		sb.append(" from t_drug_info i left join t_drug_outstore o on o.drug_code=i.drug_code   left join t_business_dictionary t on t.code_encode=i.drug_packagingunit and t.code_type='drugPackagingunit'");
		if(StringUtils.isNotBlank(beginTime)){
			sb.append(" and to_char(o.DRUGED_DATE,'yyyy-MM-dd') >= '"+beginTime+"' ");
		}
		if(StringUtils.isNotBlank(endTime)){
			sb.append(" and to_char(o.DRUGED_DATE,'yyyy-MM-dd') <= '"+endTime+"' ");
		}
		if(StringUtils.isNotBlank(deptCode)){
			sb.append(" and o.DRUG_DEPT_CODE = '"+deptCode+"' ");
		}
		if(StringUtils.isNotBlank(storageCode)){
			sb.append(" and o.DRUG_STORAGE_CODE = '"+storageCode+"' ");
		} 
		if(StringUtils.isNotBlank(drugType)){
			sb.append(" and o.DRUG_TYPE = '"+drugType+"' ");
		}
		if(StringUtils.isNotBlank(outType)){
			sb.append(" and o.OUT_TYPE = '"+outType+"' ");
		}
		sb.append(" group by i.drug_basicinputcode,o.retail_price,o.op_type,o.out_state,i.drug_biddingcode,i.drug_code,i.drug_commonname,i.drug_spec,i.drug_packagingnum,t.code_name,i.drug_packagingnum");
		return sb.toString();
	}

	@Override
	public List<AdmissionStatisticsVo> getAdmissionStatisticsVo(String beginTime, String endTime, String deptCode,
			String storageCode, String drugType, String outType, String page, String rows) throws Exception {
		String sb=this.getHql(beginTime, endTime, deptCode, storageCode, drugType, outType);
		SQLQuery queryObject = this.getSession().createSQLQuery(sb.toString())
				.addScalar("drugBasicCode").addScalar("drugBiddingCode").addScalar("drugId")
				.addScalar("drugName").addScalar("outState").addScalar("optype").addScalar("drugSpec")
				.addScalar("num",Hibernate.DOUBLE).addScalar("drugPackgingUnit").addScalar("sum",Hibernate.DOUBLE)
				.addScalar("retailPrice",Hibernate.DOUBLE);
		int start = Integer.parseInt(page == null ? "1" : page);
		int count = Integer.parseInt(rows == null ? "20" : rows);
		List<AdmissionStatisticsVo> operaArragVoList = queryObject.setFirstResult((start - 1) * count).setMaxResults(count).setResultTransformer(Transformers.aliasToBean(AdmissionStatisticsVo.class)).list();
		if(operaArragVoList!=null&&operaArragVoList.size()>0){
			return operaArragVoList;
		}
		return new ArrayList<AdmissionStatisticsVo>();
	}

	
	@Override
	public int getTotal(String beginTime, String endTime, String deptCode, String storageCode, String drugType,
			String outType)  throws Exception{
		String sb=this.getHql(beginTime, endTime, deptCode, storageCode, drugType, outType);
		SQLQuery queryObject = this.getSession().createSQLQuery(sb.toString())
				.addScalar("drugBasicCode").addScalar("drugBiddingCode").addScalar("drugId")
				.addScalar("drugName").addScalar("outState").addScalar("optype").addScalar("drugSpec")
				.addScalar("num",Hibernate.DOUBLE).addScalar("drugPackgingUnit").addScalar("sum",Hibernate.DOUBLE)
				.addScalar("retailPrice",Hibernate.DOUBLE);
		List<AdmissionStatisticsVo> list = queryObject.setResultTransformer(Transformers.aliasToBean(AdmissionStatisticsVo.class)).list();
		return list.size();
	}


	/**
	 * @Description:查询所有的药房
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月23日
	 * @param:
	 * @return 
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public List<SysDepartment> getSysDepartment()  throws Exception{
		String hql="from SysDepartment where stop_flg=0 and del_flg=0 and deptType = 'P' ";
		List<SysDepartment> list=super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<SysDepartment>();
	}
	
	/**
	 * @Description:得到全部的记录数用于导出
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月22日
	 * @param:beganTime 开始时间； endTime 结束时间； status 手术状态； deptCode 取药药房；
	 * storageCode 领药科室；drugType 药品类别；outType 出库类别
	 * @return List<AdmissionStatisticsVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public List<AdmissionStatisticsVo> getAllAdmissionStatisticsVo(String beginTime, String endTime, String deptCode,
			String storageCode, String drugType, String outType)  throws Exception{
		String sb=this.getHql(beginTime, endTime, deptCode, storageCode, drugType, outType);
		SQLQuery queryObject = this.getSession().createSQLQuery(sb.toString())
				.addScalar("drugBasicCode").addScalar("drugBiddingCode").addScalar("drugId")
				.addScalar("drugName").addScalar("outState").addScalar("optype").addScalar("drugSpec")
				.addScalar("num",Hibernate.DOUBLE).addScalar("drugPackgingUnit").addScalar("sum",Hibernate.DOUBLE)
				.addScalar("retailPrice",Hibernate.DOUBLE);
		List<AdmissionStatisticsVo> list = queryObject.setResultTransformer(Transformers.aliasToBean(AdmissionStatisticsVo.class)).list();
		if(list.size()>0&&list!=null){
			return list;
		}
		return new ArrayList<AdmissionStatisticsVo>();
	}
	
}
