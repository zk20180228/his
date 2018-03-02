package cn.honry.mobile.mobileBlackList.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.MBlackList;
import cn.honry.base.bean.model.MMobileBlackList;
import cn.honry.base.bean.model.MenuIcon;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.mobile.mobileBlackList.dao.MobileBlackListDao;

@Repository("mobileBlackListDao")
@SuppressWarnings({ "all" })
public class MobileBlackListDaoImpl  extends HibernateEntityDao<MMobileBlackList> implements MobileBlackListDao{

	
	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<MMobileBlackList> getCellPhoneBlack(String rows, String page,
			String queryName, String type) throws Exception  {
		StringBuffer sb= new StringBuffer();
		sb.append(" from MMobileBlackList where stop_flg=0 and del_flg=0 ");
		if(StringUtils.isNotBlank(queryName)){
			sb.append(" and mobileNum like '%").append(queryName).append("%'");
		}
		if(StringUtils.isNotBlank(type)){
			sb.append(" and type = '").append(type).append("'");
		}
		sb.append(" order by createTime desc ");
		List<MMobileBlackList> list = super.getPage(sb.toString(), page, rows);
		if(list != null && list.size() != 0){
			return list;
		}
		return new ArrayList<MMobileBlackList>();
	}

	@Override
	public Integer getCellPhoneBlackCount(String queryName, String type) throws Exception {
		StringBuffer sb= new StringBuffer();
		sb.append(" from MMobileBlackList where stop_flg=0 and del_flg=0 ");
		if(StringUtils.isNotBlank(queryName)){
			sb.append(" and mobileNum like '%").append(queryName).append("%'");
		}
		if(StringUtils.isNotBlank(type)){
			sb.append(" and type = '").append(type).append("'");
		}
		Integer total = super.getTotal(sb.toString());
		if(total == null){
			return 0;
		}
		return total;
	}

	@Override
	public List<MMobileBlackList> checkExist(String mobileNum, String type) throws Exception{
		StringBuffer sb= new StringBuffer();
		sb.append(" from MMobileBlackList where stop_flg=0 and del_flg=0  and mobileNum=? and type=? ");
		List<MMobileBlackList> list = super.find(sb.toString(), mobileNum, type);
		if(list != null && list.size() != 0){
			return list;
		}
		return null;
	}

	@Override
	public List<String> synCach(String str) throws Exception {
		StringBuffer sb= new StringBuffer();
		String type="";
		if("YWSP".equals(str)){
			type="1";
		}else if("RCAP".equals(str)){
			type="2";
		}else{
			type="3";
		}
		sb.append("select mobileNum from MMobileBlackList where stop_flg=0 and del_flg=0  and type=? ");
		List<String> list = super.find(sb.toString(), type);
		if(list != null && list.size() != 0){
			return list;
		}
		return new ArrayList<String>();
	}
}
