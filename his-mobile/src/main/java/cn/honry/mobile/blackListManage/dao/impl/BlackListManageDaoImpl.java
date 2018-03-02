package cn.honry.mobile.blackListManage.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.MMobileTypeManage;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.mobile.blackListManage.dao.BlackListManageDao;

@Repository("blackListManageDao")
@SuppressWarnings({ "all" })
public class BlackListManageDaoImpl extends HibernateEntityDao<MMobileTypeManage> implements BlackListManageDao {

	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Override
	public List<MMobileTypeManage> getBlackManageList(String rows, String page,
			String mobileCategory) throws Exception {
		StringBuffer sb= new StringBuffer();
		sb.append(" from MMobileTypeManage where stop_flg=0 and del_flg=0 ");
		if(StringUtils.isNotBlank(mobileCategory)){
			sb.append(" and upper(mobileCategory) like '%").append(mobileCategory.toUpperCase()).append("%'");
		}
		sb.append(" and type ='2' ");
		sb.append(" order by createTime desc ");
		List<MMobileTypeManage> list = super.getPage(sb.toString(), page, rows);
		if(list != null && list.size() != 0){
			return list;
		}
		return new ArrayList<MMobileTypeManage>();
	}

	@Override
	public Integer getBlackManageCount(String mobileCategory) throws Exception {
		StringBuffer sb= new StringBuffer();
		sb.append(" from MMobileTypeManage where stop_flg=0 and del_flg=0 ");
		if(StringUtils.isNotBlank(mobileCategory)){
			sb.append(" and upper(mobileCategory) like '%").append(mobileCategory.toUpperCase()).append("%'");
		}
		sb.append(" and type = '2'");
		Integer total = super.getTotal(sb.toString());
		if(total == null){
			return 0;
		}
		return total;
	}

	@Override
	public List<String> getBlackList() throws Exception{
		StringBuffer sb= new StringBuffer();
		sb.append(" select mobileCategory from MMobileTypeManage where stop_flg=0 and del_flg=0 ");
		sb.append(" and type ='2' ");
		sb.append(" order by createTime desc ");
		List<String> list = super.find(sb.toString());
		if(list != null && list.size() != 0){
			return list;
		}
		return new ArrayList<String>();
	}
}
