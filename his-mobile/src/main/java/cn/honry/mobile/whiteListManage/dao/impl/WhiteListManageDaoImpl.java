package cn.honry.mobile.whiteListManage.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.MMobileBlackList;
import cn.honry.base.bean.model.MMobileTypeManage;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.mobile.whiteListManage.dao.WhiteListManageDao;

@Repository("whiteListManageDao")
@SuppressWarnings({ "all" })
public class WhiteListManageDaoImpl  extends HibernateEntityDao<MMobileTypeManage> implements WhiteListManageDao{

	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<MMobileTypeManage> getWhiteManageList(String rows, String page,
			String mobileCategory) throws Exception {
		StringBuffer sb= new StringBuffer();
		sb.append(" from MMobileTypeManage where stop_flg=0 and del_flg=0 ");
		if(StringUtils.isNotBlank(mobileCategory)){
			sb.append(" and upper(mobileCategory) like '%").append(mobileCategory.toUpperCase()).append("%'");
		}
		sb.append(" and type ='1' ");
		sb.append(" order by createTime desc ");
		List<MMobileTypeManage> list = super.getPage(sb.toString(), page, rows);
		if(list != null && list.size() != 0){
			return list;
		}
		return new ArrayList<MMobileTypeManage>();
	}

	@Override
	public Integer getWhiteManageCount(String mobileCategory) throws Exception {
		StringBuffer sb= new StringBuffer();
		sb.append(" from MMobileTypeManage where stop_flg=0 and del_flg=0 ");
		if(StringUtils.isNotBlank(mobileCategory)){
			sb.append(" and upper(mobileCategory) like '%").append(mobileCategory.toUpperCase()).append("%'");
		}
		sb.append(" and type = '1'");
		Integer total = super.getTotal(sb.toString());
		if(total == null){
			return 0;
		}
		return total;
	}

	@Override
	public List<MMobileTypeManage> checkExist(String mobileCategory,String type)
			throws Exception {
		StringBuffer sb= new StringBuffer();
		sb.append(" from MMobileTypeManage where stop_flg=0 and del_flg=0  and upper(mobileCategory)=? and type=? ");
		List<MMobileTypeManage> list = super.find(sb.toString(), mobileCategory.toUpperCase(),type);
		if(list != null && list.size() != 0){
			return list;
		}
		return null;
	}

	@Override
	public List<String> getInitData()  throws Exception {
		StringBuffer sb= new StringBuffer();
		sb.append("select distinct t.PHONE_TYPE from t_sys_user t where t.phone_type is not null and t.del_flg = 0 and t.stop_flg = 0 ");
		sb.append("and t.phone_type not in ( select  t.mobile_category from m_mobiltypemanage t where   t.stop_flg=0 and t.del_flg=0 and t.type='2' ) ");
		List<String> list = this.getSession().createSQLQuery(sb.toString()).list();
		if(list != null && list.size() != 0){
			return list;
		}
		return new ArrayList<String>();
	}

	@Override
	public void clearData() throws Exception {
		StringBuffer sb= new StringBuffer();
		sb.append("delete from  m_mobiltypemanage t where t.type='1'  ");
		this.getSession().createSQLQuery(sb.toString()).executeUpdate();
		
	}

}
