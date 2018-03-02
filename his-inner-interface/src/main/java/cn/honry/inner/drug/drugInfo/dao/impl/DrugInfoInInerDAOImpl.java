package cn.honry.inner.drug.drugInfo.dao.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.DrugApplyout;
import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.drug.drugInfo.dao.DrugInfoInInerDAO;

/**
 * 
 * @Description 药品接口DAO实现层
 * @author  lyy
 * @createDate： 2016年7月7日 下午4:28:05 
 * @modifier lyy
 * @modifyDate：2016年7月7日 下午4:28:05
 * @param：  
 * @modifyRmk：  
 * @version 1.0
 */
@Repository("drugInfoInInerDAO")
@SuppressWarnings({"all"})
public class DrugInfoInInerDAOImpl extends HibernateEntityDao<DrugInfo> implements DrugInfoInInerDAO {
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {

		super.setSessionFactory(sessionFactory);
	}
	/**
	 * 
	 * @Description： 分页查询－获得总条数
	 * @Author：wujiao
	 * @CreateDate：2015-10-29 上午11:59:48
	 * @Modifier：wujiao
	 * @ModifyDate：2015-10-29上午11:59:48
	 * @ModifyRmk：
	 * @version 1.0
	 *
	 */
	@Override
	public int getTotal(DrugInfo drug) {
		String hql = jointview(drug);
		return super.getTotal(hql);
	}

	/**
	 * 
	 * @Description： 分页查询－获得列表数据
	 * @Author：wujiao
	 * @CreateDate：2015-12-2 上午11:59:48
	 * @Modifier：wujiao
	 * @ModifyDate：2015-12-2上午11:59:48
	 * @ModifyRmk：
	 * @version 1.0
	 *
	 */
	@Override
	public List<DrugInfo> getPage(String page, String rows, DrugInfo drug) {
		String hql = jointview(drug);
		return super.getPage(hql, page, rows);
	}
	private String jointview(DrugInfo drug) {
		String hql = "FROM DrugInfo d WHERE d.del_flg = 0 AND d.stop_flg = 0";
		if (drug != null) {
			if (StringUtils.isNotEmpty(drug.getName())) {
				String queryName = drug.getName();
				hql = hql + " AND (d.name LIKE '%" + queryName + "%'"
						+ " OR d.drugNamepinyin LIKE '%"+queryName.toUpperCase()+"%'"
						+ " OR d.drugNamewb LIKE '%" + queryName.toUpperCase()+"%'"
						+ " OR d.drugNameinputcode LIKE '%" + queryName+ "%'"
						+ " OR d.drugCommonname LIKE '%" + queryName+ "%')";
			}
			if (StringUtils.isNotBlank(drug.getDrugType())) {
				hql = hql + " AND d.drugType = '" + drug.getDrugType() + "'";
			}
		}
		return hql;
	}
	@Override
	public String companyCode(String drugid) {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append(" select t.DRUG_PACKAGINGUNIT from t_drug_info t");
		sBuffer.append(" where t.drug_code = ? ");
		
		String packagingunit = (String) this.getSession().createSQLQuery(sBuffer.toString())
				.setParameter(0,drugid).uniqueResult();
		return packagingunit;
	}

	/**
	 * 根据药品编码获取药品信息
	 * @param code 药品编码
	 * @return
	 */
	@Override
	public DrugInfo getByCode(String code){
		DrugInfo drugInfo = findUniqueBy("code", code);
		if(drugInfo!=null&&StringUtils.isNotBlank(drugInfo.getId())){
			return drugInfo;
		}
		return null;
	}
	@Override
	public DrugInfo getDrugName(String drugCode) {
		String hql="from DrugInfo d where d.del_flg = 0 and d.stop_flg = 0 and d.code =?";
		List<DrugInfo> drugInfo = super.find(hql, drugCode);
		if(drugInfo!=null && drugInfo.size() > 0){
			return drugInfo.get(0);
		}
		return null;
	}
	public List<DrugInfo> getDrugCodeAndName(){
		String hql="from DrugInfo d";
		List<DrugInfo> drug=super.find(hql);
		if(null!=drug){
			return drug;
		}
		return drug;
		
	}
}
