package cn.honry.statistics.finance.outprepayDetail.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.OutpatientAccountrecord;
import cn.honry.base.bean.model.OutpatientOutprepay;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.statistics.finance.outprepayDetail.dao.OutDetailDao;

@Repository("outDetailDao")
@SuppressWarnings({"all"})
public class OutDetailDaoImpl extends HibernateEntityDao<OutpatientOutprepay> implements OutDetailDao {
	// 为父类HibernateDaoSupport注入sessionFactory的值
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<OutpatientOutprepay> queryOutprepay(String page, String rows, String medicalrecordId, String beginDate,
			String endDate) {
		String hql = jointOutprepay(page, rows, medicalrecordId, beginDate, endDate);
		List<OutpatientOutprepay> list = super.getPage(hql, page, rows);
		if(list != null && list.size() > 0){
			return list;
		}
		return new ArrayList<OutpatientOutprepay>();
	}
	
	@Override
	public int queryOutprepayTotal(String page, String rows, String medicalrecordId, String beginDate, String endDate) {
		String hql = jointOutprepay(page, rows, medicalrecordId, beginDate, endDate);
		int total = super.getTotal(hql);
		return total;
	}

	@Override
	public List<OutpatientAccountrecord> queryRecord(String page, String rows, String medicalrecordId, String beginDate,
			String endDate) {
		String hql = jointRecord(page, rows, medicalrecordId, beginDate, endDate);
		Query query = this.getSession().createQuery(hql);
		int start = Integer.parseInt(page==null?"1":page);
		int count = Integer.parseInt(rows==null?"20":rows);
		
		List<OutpatientAccountrecord> list = query.setFirstResult((start-1)*count).setMaxResults(count).list();
		if(list != null && list.size() > 0){
			return list;
		}
		return new ArrayList<OutpatientAccountrecord>();
	}

	@Override
	public int queryRecordTotal(String page, String rows, String medicalrecordId, String beginDate, String endDate) {
		String hql = jointRecord(page, rows, medicalrecordId, beginDate, endDate);
		int total = super.getTotal(hql);
		return total;
	}

	/***
	 * 充值统计条件拼接
	 * @Title: jointStr 
	 * @author  WFJ
	 * @createDate ：2016年6月22日
	 * @param page
	 * @param rows
	 * @param medicalrecordId
	 * @param beginDate
	 * @param endDate
	 * @return String
	 * @version 1.0
	 */
	public String jointOutprepay(String page, String rows, String medicalrecordId, String beginDate,
			String endDate){
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("from OutpatientOutprepay t");
		sBuffer.append(" where t.stop_flg = 0 and t.del_flg = 0");
		sBuffer.append(" and t.medicalrecordId = '"+medicalrecordId+"'");
		if(StringUtils.isNotBlank(beginDate)){
			sBuffer.append(" and t.createTime >= to_date('"+beginDate+"','yyyy-mm-dd hh24:mi:ss')");
		}
		if(StringUtils.isNotBlank(endDate)){
			sBuffer.append(" and t.createTime <= to_date('"+endDate+"','yyyy-mm-dd hh24:mi:ss')");
		}
		sBuffer.append(" order by t.createTime desc");
		return sBuffer.toString();
	}

	/***
	 * 消费明细条件拼接
	 * @Title: jointRecord 
	 * @author  WFJ
	 * @createDate ：2016年6月22日
	 * @param page
	 * @param rows
	 * @param medicalrecordId
	 * @param beginDate
	 * @param endDate
	 * @return String
	 * @version 1.0
	 */
	public String jointRecord(String page, String rows, String medicalrecordId, String beginDate, String endDate){
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("from OutpatientAccountrecord t");
		sBuffer.append(" where t.stop_flg = 0 and t.del_flg = 0");
		sBuffer.append(" and t.medicalrecordId = '"+medicalrecordId+"'");
		sBuffer.append(" and t.opertype in ('4','8')");
		if(StringUtils.isNotBlank(beginDate)){
			sBuffer.append(" and t.createTime >= to_date('"+beginDate+"','yyyy-mm-dd hh24:mi:ss')");
		}
		if(StringUtils.isNotBlank(endDate)){
			sBuffer.append(" and t.createTime <= to_date('"+endDate+"','yyyy-mm-dd hh24:mi:ss')");
		}
		sBuffer.append(" order by t.createTime desc");
		return sBuffer.toString();
	}

	@Override
	public List<Patient> queryPatient() {
		String hql = "from Patient t where t.stop_flg = 0 and t.del_flg = 0";
		List<Patient> list = find(hql,null);
		if(list != null && list.size() > 0)
			return list;
		return new ArrayList<Patient>();
	}

	@Override
	public List<Patient> querymedicalrecord(String medicalrecord) {
		String hql=" from Patient t where t.medicalrecordId ='"+medicalrecord+"' and stop_flg=0 and del_flg=0 ";
		List<Patient> registerList = super.find(hql, null);
		if(registerList==null||registerList.size()<=0){
			return new ArrayList<Patient>();
		}
		return registerList;
	}

	@Override
	public List<Patient> cardQueryMedicalrecord(String ic, String idCard,String codeCertificate) {
		String hql;
		if(StringUtils.isNotBlank(idCard)){
			hql="FROM Patient WHERE patientCertificatesno = '"+idCard+"'";
			if(StringUtils.isNotBlank(codeCertificate)){
				hql+=" and patientCertificatestype = '"+codeCertificate+"'";
			}
			hql+=" AND stop_flg = 0 AND del_flg = 0 ORDER BY createTime DESC";
		}else{
			hql="FROM Patient WHERE cardNo='"+ic+"' AND stop_flg = 0 AND del_flg = 0 ORDER BY createTime DESC";
		}
		List<Patient> list = super.find(hql);
		if(list.size()>0){
			return list;
		}
		return new ArrayList<Patient>();
	}

	@Override
	public List<OutpatientOutprepay> queryOutprepayList(String medicalrecordId,
			String beginDate, String endDate) {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("from OutpatientOutprepay t");
		sBuffer.append(" where t.stop_flg = 0 and t.del_flg = 0");
		sBuffer.append(" and t.medicalrecordId = '");
		sBuffer.append(medicalrecordId);
		sBuffer.append("'");
		if(StringUtils.isNotBlank(beginDate)){
			sBuffer.append(" and t.createTime >= to_date('");
			sBuffer.append(beginDate);
			sBuffer.append("','yyyy-mm-dd hh24:mi:ss')");
		}
		if(StringUtils.isNotBlank(endDate)){
			sBuffer.append(" and t.createTime <= to_date('");
			sBuffer.append(endDate);
			sBuffer.append("','yyyy-mm-dd hh24:mi:ss')");
		}
		sBuffer.append(" order by t.createTime desc");
		List<OutpatientOutprepay> list = super.find(sBuffer.toString());
		if (list == null || list.size() == 0) {
			return new ArrayList<>();
		}
		return list;
	}

	@Override
	public List<OutpatientAccountrecord> queryRecordList(
			String medicalrecordId, String beginDate, String endDate) {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("from OutpatientAccountrecord t");
		sBuffer.append(" where t.stop_flg = 0 and t.del_flg = 0");
		sBuffer.append(" and t.medicalrecordId = '");
		sBuffer.append(medicalrecordId);
		sBuffer.append("'");
		sBuffer.append(" and t.opertype in ('4','8')");
		if(StringUtils.isNotBlank(beginDate)){
			sBuffer.append(" and t.createTime >= to_date('");
			sBuffer.append(beginDate);
			sBuffer.append("','yyyy-mm-dd hh24:mi:ss')");
		}
		if(StringUtils.isNotBlank(endDate)){
			sBuffer.append(" and t.createTime <= to_date('");
			sBuffer.append(endDate);
			sBuffer.append("','yyyy-mm-dd hh24:mi:ss')");
		}
		sBuffer.append(" order by t.createTime desc");
		List<OutpatientAccountrecord> list = super.find(sBuffer.toString());
		if (list == null || list.size() == 0) {
			return new ArrayList<>();
		}
		return list;
	}
	
	
}
