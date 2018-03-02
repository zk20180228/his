package cn.honry.oa.userPortal.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.Information;
import cn.honry.base.bean.model.InpatientNurass;
import cn.honry.base.bean.model.MApkVersion;
import cn.honry.base.bean.model.OaPortalWidget;
import cn.honry.base.bean.model.OaTaskInfo;
import cn.honry.base.bean.model.OaUserPortal;
import cn.honry.base.bean.model.Schedule;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.SysInfo;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.system.utli.DataRightUtils;
import cn.honry.oa.userPortal.dao.UserPortalDAO;
import cn.honry.utils.DateUtils;
import cn.honry.utils.SessionUtils;

@Repository("userPortalDAO")
@SuppressWarnings({ "all" })
public class UserPortalDAOImpl extends HibernateEntityDao<OaUserPortal> implements UserPortalDAO {

	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Override
	public OaUserPortal getById(String id) {
			String hql=" from OaUserPortal  where id='"+id+"'";
		return (OaUserPortal) super.find(hql).get(0);
	}
	@Override
	public List<OaPortalWidget> queryPortalWidgetAll() {
		StringBuilder hql = new StringBuilder();
		hql.append(" from OaPortalWidget b where "+DataRightUtils.connectHQLSentence("b")+"");
		List<OaPortalWidget> list=find(hql.toString(), null);
		if(list!=null && list.size()>0){
			return list;
		}
		return new ArrayList<OaPortalWidget>();
	}
	@Override
	public List<OaPortalWidget> queryPortalWidget() {
		StringBuilder hql = new StringBuilder();
		hql.append(" from OaPortalWidget b where b.status=0 and "+DataRightUtils.connectHQLSentence("b")+"");
		List<OaPortalWidget> list=find(hql.toString(), null);
		if(list!=null && list.size()>0){
			return list;
		}
		return new ArrayList<OaPortalWidget>();
	}
	@Override
	public OaPortalWidget queryPortalWidgetById(String id) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from OaPortalWidget b where b.id='"+id+"' and "+DataRightUtils.connectHQLSentence("b")+"");
		List<OaPortalWidget> list=find(hql.toString(), null);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	@Override
	public List<OaUserPortal> queryUserPortal(String longinUserAccount) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from OaUserPortal b where b.local=0 and b.global=0 and b.account='"+longinUserAccount+"' and "+DataRightUtils.connectHQLSentence("b")+"");
		List<OaUserPortal> list=find(hql.toString(), null);
		if(list!=null && list.size()>0){
			return list;
		}
		return new ArrayList<OaUserPortal>();
	}
	@Override
	public List<OaUserPortal> queryUserPortalAll(String longinUserAccount) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from OaUserPortal b where b.account='"+longinUserAccount+"' and "+DataRightUtils.connectHQLSentence("b")+"");
		List<OaUserPortal> list=find(hql.toString(), null);
		if(list!=null && list.size()>0){
			return list;
		}
		return new ArrayList<OaUserPortal>();
	}
	@Override
	public void updateWidget(final OaUserPortal userPortal) {
		final String sql="update T_OA_USER_PORTAL set PORTAL_ORDER=:order, UPDATEUSER=:account,UPDATETIME=sysdate where "
				+ "USER_ACCOUNT =:account and WIDGET_ID = :widget and "
				+ "LOCAL_STATUS = 0 and GLOBAL_STATUS = 0 and "+DataRightUtils.connectHQLSentence(null)+"";
		this.getHibernateTemplate().execute(new HibernateCallback<Integer>() {

			@Override
			public Integer doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sql);
				query.setParameter("account", userPortal.getAccount())
				.setParameter("widget", userPortal.getWidget())
				.setParameter("order", Integer.valueOf(userPortal.getOrder()));
				return query.executeUpdate();
			}
		});
	}

	@Override
	public List<SysInfo> getList(SysInfo info) {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("from SysInfo t");
		sBuffer.append(" where t.stop_flg=0 and t.del_flg=0");
		sBuffer.append(" and t.infoPubflag = '"+ info.getInfoPubflag() +"'");
		if(info.getInfoPubflag() == 1){
			if(info.getInfoType() != null){
				sBuffer.append(" and t.infoType = '"+ info.getInfoType() +"'");
			}else{
				sBuffer.append(" and t.infoType in(1,2)");
			}
		}
		if(StringUtils.isNotBlank(info.getInfoBrev())){
			String infoBrev  = info.getInfoBrev().trim().toUpperCase();
			sBuffer.append(" and (upper(t.infoTitle) like '%"+ infoBrev +"%'");
				sBuffer.append(" or upper(t.infoKeyword) like '%"+ infoBrev +"%')");
		}
		if(info.getInfoPubflag() == 1)
			sBuffer.append(" order by t.infoOrder desc");
		else
			sBuffer.append(" order by t.updateTime desc");
		List<SysInfo> list = find(sBuffer.toString());
		if(list != null && list.size() > 0)
			return list;
		return new ArrayList<SysInfo>();
	}

	@Override
	public List<User> queryAllUser() {
		StringBuilder hql = new StringBuilder();
		hql.append(" from User t where t.stop_flg=0 and t.del_flg=0");
		List<User> list=find(hql.toString(), null);
		if(list!=null && list.size()>0){
			return list;
		}
		return new ArrayList<User>();
	}

	@Override
	public List<OaUserPortal> queryPortalUser(String widgetId) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from OaUserPortal t where t.widget='"+widgetId+"'");
		List<OaUserPortal> list=find(hql.toString(), null);
		if(list!=null && list.size()>0){
			return list;
		}
		return new ArrayList<OaUserPortal>();
	}

	@Override
	public void enableUserWidget(OaUserPortal oaUserPortal) {
		String sql="update T_OA_USER_PORTAL set PORTAL_ORDER="+oaUserPortal.getOrder()+",PORTAL_NAME='"+oaUserPortal.getName()+"',LOCAL_STATUS=0, UPDATEUSER='"+oaUserPortal.getAccount()+"',UPDATETIME=sysdate where "
				+ "ID = '"+oaUserPortal.getId()+"' and "+DataRightUtils.connectHQLSentence(null)+"";
		this.getSession().createSQLQuery(sql).executeUpdate();
	}

	@Override
	public List<Information> getInformationList(String type,String longinUserAccount) {
		List<Information> information=null;
		//查询当前登录人的且未读的,或者是所有人可读的信息
		String sql = "select t.INFO_ID as id, '['||m.menu_Name||']'||t.INFO_TITLE as infoTitle, t.INFO_PUBTIME as infoPubtime "
			+ "from T_OA_INFORMATION t, T_OA_INFORMATION_SUBSCRIPE sub , T_OA_MENU m where m.menu_code = t.info_menuid ";
		if(StringUtils.isNotBlank(type)){
			sql+= "and t.INFO_MENUID = '"+type+"' ";
		}
		//必须是已审核已发布的数据
		sql+= "and t.INFO_ID = sub.INFORMATIONID and t.INFO_PUBFLAG=1 and t.INFO_CHECK_FLAG=1 "
			+ "and t.del_flg=0 and t.stop_flg=0 "
//			+ "and (sub.TYPE='0' or (sub.SUBSCRIPEPERSION='"+longinUserAccount+"' and sub.ISREAD=0)) "
			+ "and sub.SUBSCRIPEPERSION='"+longinUserAccount+"' and sub.ISREAD=0 "//由于修改规则了,所有人也按个人来查询所以这里修改不要所有人
			+ "order by t.INFO_PUBTIME desc";
		SQLQuery query = this.getSession().createSQLQuery(sql.toString()).addScalar("id").addScalar("infoTitle").addScalar("infoPubtime",Hibernate.DATE);
		information = query.setResultTransformer(Transformers.aliasToBean(Information.class)).list();
		if(information!=null && information.size()>0){
			return information;
		}
		return new ArrayList<Information>();
	}
	//改JDBC
//	@Override
//	public List<Information> getInformationList(String type,String longinUserAccount) {
//		List<Information> information=null;
//		Map<String, Object> pMap = new HashMap<>();
//		pMap.put("longinUserAccount", longinUserAccount);
//		if (StringUtils.isNotBlank(type)) {
//			pMap.put("type", type);
//		}
//		//查询当前登录人的且未读的,或者是所有人可读的信息
//		String sql = "select t.INFO_ID as id, '['||m.menu_Name||']'||t.INFO_TITLE as infoTitle, t.INFO_PUBTIME as infoPubtime "
//			+ "from T_OA_INFORMATION t, T_OA_INFORMATION_SUBSCRIPE sub , T_OA_MENU m where m.menu_code = t.info_menuid ";
//		if(StringUtils.isNotBlank(type)){
//			sql+= "and t.INFO_MENUID = :type ";
//		}
//		//必须是已审核已发布的数据
//		sql+= "and t.INFO_ID = sub.INFORMATIONID and t.INFO_PUBFLAG=1 and t.INFO_CHECK_FLAG=1 "
//			+ "and t.del_flg=0 and t.stop_flg=0 "
//			+ "and sub.SUBSCRIPEPERSION=:longinUserAccount and sub.ISREAD=0 "//由于修改规则了,所有人也按个人来查询所以这里修改不要所有人
//			+ "order by t.INFO_PUBTIME desc";
//		List<Information> value = namedParameterJdbcTemplate.queryForList(sql.toString(), pMap, Information.class);
//		if(information!=null && information.size()>0){
//			return information;
//		}
//		return new ArrayList<Information>();
//	}
	
	@Override
	public List<Information> getInformationCheck(SysEmployee employee,User user) {
		List<Information> information=null;
		//查询当前登录人的且未读的,或者是所有人可读的信息//未审核且已发布的才可以审核
		String sql = "select t.INFO_ID as id, '['||m.menu_Name||']'||t.INFO_TITLE as infoTitle, t.INFO_PUBTIME as infoPubtime,t.INFO_MENUID as infoMenuid "
			+ "from T_OA_INFORMATION t, T_OA_MENURIGHT menu , T_OA_MENU m "
			+ "where t.INFO_CHECK_FLAG=0 and t.INFO_PUBFLAG=1 "
			+ "and m.menu_code = t.info_menuid "
			+ "and t.INFO_MENUID=menu.MENU_ID and menu.RIGHT_RIGHTTYPE='2' "
			+ "and t.del_flg=0 and t.stop_flg=0 "
			+ "and (menu.RIGHT_TYPE='0' or (menu.RIGHT_TYPE='1' and menu.RIGHT_CODE='"+user.getAccount()+"') ";
			if(StringUtils.isNotBlank(employee.getTitle())){
				sql+= "or (menu.RIGHT_TYPE='2' and menu.RIGHT_CODE='"+employee.getTitle()+"') ";
			}
			if(StringUtils.isNotBlank(employee.getDeptCode())){
				sql+= "or (menu.RIGHT_TYPE='3' and menu.RIGHT_CODE='"+employee.getDeptCode()+"') ";
			}
			if(StringUtils.isNotBlank(employee.getPost())){
				sql+= "or (menu.RIGHT_TYPE='4' and menu.RIGHT_CODE='"+employee.getPost()+"')";
			}
			sql+= ") order by t.INFO_PUBTIME desc";
		SQLQuery query = this.getSession().createSQLQuery(sql.toString()).addScalar("id").addScalar("infoTitle").addScalar("infoPubtime",Hibernate.DATE).addScalar("infoMenuid");
		information = query.setResultTransformer(Transformers.aliasToBean(Information.class)).list();
		if(information!=null && information.size()>0){
			return information;
		}
		return new ArrayList<Information>();
	}

	@Override
	public SysDepartment getDeptForId(String deptId) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from SysDepartment b where b.id='"+deptId+"'");
		List<SysDepartment> list=find(hql.toString(), null);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public String queryWidgetForUser(String longinUserAccount, String moudelId) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from OaUserPortal b where b.account='"+longinUserAccount+"' and b.widget='"+moudelId+"'");
		List<OaUserPortal> list=find(hql.toString(), null);
		if(list!=null && list.size()>0){
			return "yes";
		}
		return "no";
	}

//	@Override
//	public List<Schedule> qeryScheduleList(String userAccount) {
//		String hql="from Schedule s where s.del_flg = 0 and s.stop_flg=0 and s.userId=:userId and to_char(s.time,'yyyy-MM-dd')=to_char(sysdate,'yyyy-MM-dd') order by s.time desc";
//	    List<Schedule> list = this.getSession().createQuery(hql).setParameter("userId",userAccount).list();
//	    if(list!=null && list.size()>0){
//	    	return list;
//	    }		
//		return new ArrayList<Schedule>();
//	}
//改JDBC
	@Override
	public List<Schedule> qeryScheduleList(String userAccount) {
		Map<String, Object> pMap = new HashMap<>();
		pMap.put("userId", userAccount);
		String sql="select s.SCHEDULE_CODES as id,s.SCHEDULE_TITILE as title,s.ALL_DAY_FLG as dayFlg,"
				+ "s.SCHEDULE_START_TIME as startTime,s.SCHEDULE_END_TIME as end,s.SCHEDULE_TIME_MINUS as timeMinus,"
				+ "s.SCHEDULE_TIME_UNIT as timeUnit,s.SCHEDULE_TIME as time,s.SCHEDULE_REMARK as remark,"
				+ "s.USER_ACCOUNT as userId,s.CREATEUSER as createUser,s.CREATEDEPT as createDept,"
				+ "s.CREATETIME as createTime,s.DEL_FLG as del_flg,s.STOP_FLG as stop_flg,s.TIME_MINUS_HH_MM as hhmm,"
				+ "s.IS_ZDY as isZDY,s.SCHEDULE_FLG as scheduleFlg,s.IS_FINISH as isFinish from M_SCHEDULE s where s.del_flg = 0 and s.stop_flg=0 and s.USER_ACCOUNT=:userId and to_char(s.SCHEDULE_TIME,'yyyy-MM-dd')=to_char(sysdate,'yyyy-MM-dd') order by s.SCHEDULE_TIME desc";
		List<Schedule> list=namedParameterJdbcTemplate.query(sql,pMap,new RowMapper<Schedule>() {
			@Override
			public Schedule mapRow(ResultSet rs, int rownum)
					throws SQLException {
				Schedule vo=new Schedule();
				vo.setId(rs.getString("id"));
				vo.setTitle(rs.getString("title"));
				vo.setStart(rs.getTimestamp("startTime"));
				vo.setEnd(rs.getTimestamp("end"));
				vo.setTimeMinus(rs.getInt("timeMinus"));
				vo.setTimeUnit(rs.getString("timeUnit"));
				vo.setTime(rs.getTimestamp("time"));
				vo.setRemark(rs.getString("remark"));
				vo.setUserId(rs.getString("userId"));
				vo.setCreateUser(rs.getString("createUser"));
				vo.setCreateDept(rs.getString("createDept"));
				vo.setCreateTime(rs.getTimestamp("createTime"));
				vo.setDel_flg(rs.getInt("del_flg"));
				vo.setStop_flg(rs.getInt("stop_flg"));
				vo.setHhmm(rs.getString("hhmm"));
				vo.setIsZDY(rs.getString("isZDY"));
				vo.setScheduleFlg(rs.getString("scheduleFlg"));
				vo.setIsFinish(rs.getInt("isFinish"));
				return vo;
			 }
			});
	    if(list!=null && list.size()>0){
	    	return list;
	    }		
		return new ArrayList<Schedule>();
	}
	
	@Override
	public MApkVersion getVersion() {
		StringBuffer hql = new StringBuffer("from MApkVersion t where t.stop_flg = 0 and t.del_flg = 0 order by t.apkNewestVnum desc");
		List<MApkVersion> list = list=find(hql.toString(), null);
		if(list != null && list.size() != 0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<OaTaskInfo> getListForTask(String account, String tenantId) {
		StringBuilder hql = new StringBuilder();
		hql.append("from OaTaskInfo where assignee like :account and tenantId=:tenantId and status='active' and stop_flg=0 and del_flg=0 order by createTime desc");
		List<OaTaskInfo> list=this.getSession().createQuery(hql.toString()).setParameter("account","%"+account+"%").setParameter("tenantId",tenantId).list();
		if(list!=null && list.size()>0){
			return list;
		}
		return new ArrayList<OaTaskInfo>();
	}

}
