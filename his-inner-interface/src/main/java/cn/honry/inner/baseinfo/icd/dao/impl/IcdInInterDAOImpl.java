package cn.honry.inner.baseinfo.icd.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessIcd;
import cn.honry.base.bean.model.BusinessIcd10;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.baseinfo.icd.dao.IcdInInterDAO;

/**  
 *  
 * @className：AddrateDAOImpl 
 * @Description：  物资加价率维护
 * @Author：aizhonghua
 * @CreateDate：2016-2-24 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-2-24 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Repository("icdInInterDAO")
@SuppressWarnings({"all"})
public class IcdInInterDAOImpl extends HibernateEntityDao<BusinessIcd> implements IcdInInterDAO{
	
	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	/**  
	 *  
	 * @Description：icd下拉框的方法
	 *@Author：wujiao
	 * @CreateDate：2015-7-1 上午10:56:35  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-7-1 上午10:56:35   
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<BusinessIcd> getCombobox(String ids) {
		String hql="FROM BusinessIcd i WHERE i.del_flg = 0 AND type = 1";
		if(StringUtils.isNotBlank(ids)){
			String id[] = ids.split(","); 
			String str = "";
			for (int i = 0; i < id.length; i++) {
				if(StringUtils.isNotBlank(id[i])){
					str = str + "'"+id[i]+"'"+",";
				}
			}
			if(str!=null&&str.contains(",")){
				str = str.substring(0, str.length()-1);
			}
			if(StringUtils.isNotBlank(str)){
				hql = hql + "AND i.id NOT IN ("+str+")";
			}
		}
		List<BusinessIcd> icdList=super.findByObjectProperty(hql, null);
		if(icdList!=null && icdList.size()>0){
			return icdList;
		}
		return new ArrayList<BusinessIcd>();
	}
	
	/**
	 * 根据code获取icd10
	 * @param code
	 * @return
	 */
	public BusinessIcd10 getIcd10ByCode(String code){
		String hql= "select t.id as id,t.code as code,t.name as name,t.pinyin as pinyin,t.wb as wb,"
			+ "t.inputcode as inputcode from BusinessIcd10 t where t.stop_flg = 0 and t.del_flg = 0 and t.code= ? ";
		List<BusinessIcd10> list = this.createQuery(hql, code)
				.setResultTransformer(Transformers.aliasToBean(BusinessIcd10.class)).list();
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 获取Icd10数据
	 * @return
	 */
	public List<BusinessIcd10> getICD10Info(String q){
		Query query=null;
		String hql="select t.id as id,t.code as code,t.name as name,t.pinyin as pinyin,t.wb as wb,"
				+ "t.inputcode as inputcode from BusinessIcd10 t where t.stop_flg = 0 and t.del_flg = 0 ";
		if(StringUtils.isNotBlank(q)){
			hql+="and (t.code like ? or t.name like ? or t.pinyin like ? or t.wb like ? )";
			query = this.createQuery(hql,"%"+q+"%","%"+q+"%","%"+q+"%","%"+q+"%");
		}else{
			query = this.createQuery(hql);
		}
		List<BusinessIcd10> list = query
				.setResultTransformer(Transformers.aliasToBean(BusinessIcd10.class)).list();
		return list;
	}
}
