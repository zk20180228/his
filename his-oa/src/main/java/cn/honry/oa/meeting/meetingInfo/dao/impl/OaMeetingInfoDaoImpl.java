package cn.honry.oa.meeting.meetingInfo.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.OaMeetingInfo;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.oa.meeting.meetingInfo.dao.OaMeetingInfoDao;

@Repository("oaMeetingInfoDao")
@SuppressWarnings({ "all" })
public class OaMeetingInfoDaoImpl extends HibernateEntityDao<OaMeetingInfo> implements
		OaMeetingInfoDao {
	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public int getTotal(OaMeetingInfo oaMeetingInfo) {
		String hql = " from OaMeetingInfo as omi where omi.del_flg=0 ";
		hql = this.getWhereHql(oaMeetingInfo, hql);
		int t = super.getTotal(hql);
		return t;
	}

	@Override
	public List<OaMeetingInfo> query(OaMeetingInfo oaMeetingInfo) {
		String hql = " from OaMeetingInfo as omi where omi.del_flg=0 ";
		if("findY".equals(oaMeetingInfo.getMeetState())){
			hql += "and meetState = 'Y'";
		}
		hql = this.getWhereHql(oaMeetingInfo, hql);
		List<OaMeetingInfo> oaMeetingInfoList = super.getPage(hql, oaMeetingInfo.getPage(), oaMeetingInfo.getRows());
		return oaMeetingInfoList;
	}
	
	private String getWhereHql(OaMeetingInfo oaMeetingInfo, String hql){
		if(StringUtils.isNotBlank(oaMeetingInfo.getMeetName())){
			String meetName = "'%" + oaMeetingInfo.getMeetName() + "%'";
			hql += "and meetName like " + meetName;
		}
		if(StringUtils.isNotBlank(oaMeetingInfo.getMeetPlace())){
			String meetPlace = "'%" + oaMeetingInfo.getMeetPlace() + "%'";
			hql += "and meetPlace like " + meetPlace;
		}
		hql += " order by createTime desc";
		return hql;
	}

	@Override
	public List<String> findMeetByCode(String code) {
		String sql = "select id from T_OA_MEETINFO where meet_code = '" + code +"'";
		List<String> list = this.getSession().createSQLQuery(sql).list();
		return list;
	}
}
