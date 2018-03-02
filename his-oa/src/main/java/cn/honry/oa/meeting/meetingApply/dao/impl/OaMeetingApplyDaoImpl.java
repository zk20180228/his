package cn.honry.oa.meeting.meetingApply.dao.impl;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.business.Page;
import cn.honry.base.bean.model.OaMeetingApply;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.oa.meeting.meetingApply.dao.OaMeetingApplyDao;
import cn.honry.utils.DateUtils;
import cn.honry.utils.ShiroSessionUtils;

@Repository("oaMeetingApplyDao")
@SuppressWarnings({ "all" })
public class OaMeetingApplyDaoImpl extends HibernateEntityDao<OaMeetingApply> implements
		OaMeetingApplyDao {
	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Override
	public int getTotal(OaMeetingApply oaMeetingApply) {
		String hql = "from OaMeetingApply as oma where oma.del_flg=0 ";
		hql = this.getWhereHql(oaMeetingApply, hql);
		int t = super.getTotal(hql);
		return t;
	}

	@Override
	public List<OaMeetingApply> query(OaMeetingApply oaMeetingApply) {
		String hql = "from OaMeetingApply as oma where oma.del_flg=0 ";
		hql = this.getWhereHql(oaMeetingApply, hql);
//		List<OaMeetingApply> oaMeetingApplyList = super.getPage(hql, oaMeetingApply.getPage(), oaMeetingApply.getRows());
		List<OaMeetingApply> oaMeetingApplyList = super.find(hql);
		return oaMeetingApplyList;
	}
	
	
	private String getWhereHql(OaMeetingApply oaMeetingApply, String hql){
		if(oaMeetingApply.getMeetingStarttime()!=null&&oaMeetingApply.getMeetingEndtime()!=null){
			String sTime = DateUtils.formatDateY_M_D_H_M_S(oaMeetingApply.getMeetingStarttime());
			String eTime = DateUtils.formatDateY_M_D_H_M_S(oaMeetingApply.getMeetingEndtime());
			hql += " and meetingStarttime between to_date('"+sTime+"', 'yyyy-mm-dd hh24:mi:ss')";
			hql += " and to_date('"+eTime+"', 'yyyy-mm-dd hh24:mi:ss')";
		}else if(oaMeetingApply.getMeetingStarttime()!=null){//会议室预约情况
			String sTime = DateUtils.formatDateY_M_D_H_M_S(oaMeetingApply.getMeetingStarttime());
			hql += " and meetingStarttime > to_date('"+sTime+"', 'yyyy-mm-dd hh24:mi:ss')";
		}
		if(StringUtils.isNotBlank(oaMeetingApply.getMeetName())){
			String meetName = "'%" + oaMeetingApply.getMeetName() + "%'";
			hql += "and meetName like " + meetName;
		}
		if(StringUtils.isNotBlank(oaMeetingApply.getMeetId())){
			String meetId = "'" + oaMeetingApply.getMeetId() + "'";
			hql += "and meetId = " + meetId;
		}
		if(StringUtils.isNotBlank(oaMeetingApply.getId())){
			String meetId = "'" + oaMeetingApply.getId() + "'";
			hql += "and id != " + meetId;
		}
		if(StringUtils.isNotBlank(oaMeetingApply.getMeetingAttendance())){
			String meetingAttendance = "'%" + oaMeetingApply.getMeetingAttendance() + "%'";
			hql += "and meetingAttendance like " + meetingAttendance;
		}
		if(oaMeetingApply.getMeetingApptype()!=null){
			hql += "and meetingApptype like " + oaMeetingApply.getMeetingApptype();
		}
		if(StringUtils.isNotBlank(oaMeetingApply.getAppointmentFLag())){
			hql += "and meetingApptype not in ('2','3')";
		}
		if(StringUtils.isNotBlank(oaMeetingApply.getMeetingDept())){
			String meetingDept = "'%" + oaMeetingApply.getMeetingDept() + "%'";
			hql += "and meetingDept like " + meetingDept;
		}
		if(StringUtils.isNotBlank(oaMeetingApply.getMeetingName())){
			String meetingName = "'%" + oaMeetingApply.getMeetingName() + "%'";
			hql += "and meetingName like " + meetingName;
		}
		if(StringUtils.isNotBlank(oaMeetingApply.getMeetingApplicant())){
			String meetingApplicant = "'%" + oaMeetingApply.getMeetingApplicant() + "%'";
			hql += "and meetingApplicant like " + meetingApplicant;
		}
		//本人只能看见自己申请的会议	1--会议申请；2--会议审批；3--我的会议
		if(StringUtils.isNotBlank(oaMeetingApply.getApplyFLag())){
			String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
			if("1".equals(oaMeetingApply.getApplyFLag())){
				hql += "and meetingApplicantCode = '" + account + "'";
			}else if("2".equals(oaMeetingApply.getApplyFLag())){
				hql += "and meetingAdminCode = '" + account + "'";
			}else if("3".equals(oaMeetingApply.getApplyFLag())){
				hql += "and inSidePersonCode like '%" + account + "%'";
			}
		}
		hql += " order by meetingStarttime";
		return hql; 
	}

	@Override
	public String checkHaveUsed(OaMeetingApply oaMeetingApply) {
		String hql = "from OaMeetingApply as oma where oma.del_flg=0 and meetingApptype in ('0','1')";
		if(StringUtils.isNotBlank(oaMeetingApply.getMeetId())){
			hql += " and meetId = '"+oaMeetingApply.getMeetId()+"' " ;
		}
		Date newDateStart = oaMeetingApply.getMeetingStarttime();
		Date newDateEnd = oaMeetingApply.getMeetingEndtime();
		if(newDateStart!=null&&newDateEnd!=null){
			String sTime = DateUtils.formatDateY_M_D_H_M_S(newDateStart);
			String eTime = DateUtils.formatDateY_M_D_H_M_S(newDateEnd);
			hql += " and meetingStarttime <= to_date('"+eTime+"','yyyy-mm-dd hh24:mi:ss') ";
			hql	+= " and meetingEndtime >= to_date('"+sTime+"','yyyy-mm-dd hh24:mi:ss') "; 
		}
		if(StringUtils.isNotBlank(oaMeetingApply.getId())){
			hql += "and id != '"+oaMeetingApply.getId()+"'";
		}
		List<OaMeetingApply> oaMeetingApplyList = super.getPage(hql, "1", "999999");
		int t = oaMeetingApplyList.size();
		List<String> timeList = new ArrayList<String>();
		if(t==0){
			return "ok";
		}else{
			for(int i = 0;i<t;i++){
				OaMeetingApply oaMeetingApplyOld = oaMeetingApplyList.get(i);
				if(StringUtils.isBlank(oaMeetingApplyOld.getMeetingPeriodicity())){
					continue;
				}
				timeList.addAll(findAllTime(oaMeetingApplyOld));
			}
			if(timeList!=null&&timeList.size()!=0){
				List<String> timeListNew = new ArrayList<String>();
				timeListNew = findAllTime(oaMeetingApply);
				for(String newTime : timeListNew){
					String newTimeStart = newTime.split(",")[0];
					String newTimeEnd = newTime.split(",")[1];
					Date newDateStart1 = DateUtils.parseDateY_M_D_H_M_S(newTimeStart);
					Date newDateEnd1 = DateUtils.parseDateY_M_D_H_M_S(newTimeEnd);
					for(String oldTime : timeList){
						String oldTimeStart = oldTime.split(",")[0];
						String oldTimeEnd = oldTime.split(",")[1];
						Date oldDateStart = DateUtils.parseDateY_M_D_H_M_S(oldTimeStart);
						Date oldDateEnd = DateUtils.parseDateY_M_D_H_M_S(oldTimeEnd);
						//new 比 old 大返回：1；new 比 old 小返回：-1；new 等于 old 返回：0
						if(newDateStart1.compareTo(oldDateEnd)==-1&&newDateEnd1.compareTo(oldDateStart)==1){
							return oldTime;
						}
					}
				}
			}
			return "ok";
		}
	}

	public List<String> findAllTime(OaMeetingApply oaMeetingApplyOld){
		List<String> timeList = new ArrayList<String>();
		
		Date startDate = oaMeetingApplyOld.getMeetingStarttime();
		String startTimeString = DateUtils.formatDateY_M_D_H_M_S(startDate).substring(10, 19); 
		Date endDate = oaMeetingApplyOld.getMeetingEndtime();
		String endTimeString = DateUtils.formatDateY_M_D_H_M_S(endDate).substring(10, 19); 
		String meetingApplyweek = oaMeetingApplyOld.getMeetingApplyweek();
		if(StringUtils.isNotBlank(oaMeetingApplyOld.getMeetingPeriodicity())){
			switch (oaMeetingApplyOld.getMeetingPeriodicity()) {
				case "no"://没有按照周期
					timeList.add(DateUtils.formatDateY_M_D_H_M_S(oaMeetingApplyOld.getMeetingStarttime())+","+DateUtils.formatDateY_M_D_H_M_S(oaMeetingApplyOld.getMeetingEndtime()));
					break;
				case "week"://按照星期周期,
					while(startDate.compareTo(endDate)==-1){
						//获取星期
						String startString = DateUtils.formatDateY_M_D(startDate);
						int weekOfDay = DateUtils.getWeekOfDay(startString);//看返回的星期是星期几
						if(StringUtils.isNotBlank(meetingApplyweek)){
							if(meetingApplyweek.contains(String.valueOf(weekOfDay))){
								timeList.add(startString+startTimeString+","+startString+endTimeString);
							}
						}
						startDate = DateUtils.addDay(startDate,1);
					}
					break;
				case "month"://按照月周期
					while(startDate.compareTo(endDate)==-1){
						//获取几号,注意meetingApplyweek的格式。01还是1
						String startString = DateUtils.formatDateY_M_D(startDate);
						String startDateString = startString.substring(8, 10); 
						if(startDateString.length()==1){
							startDateString = "0"+startDateString;
						}
						if(meetingApplyweek.contains(startDateString)){
							timeList.add(startString+startTimeString+","+startString+endTimeString);
						}
						startDate = DateUtils.addDay(startDate,1);
					}
					break;
				default:
					break;
			}
		}else{
			System.out.println(oaMeetingApplyOld);
		}
		
		return timeList;
	}

	@Override
	public String findSign(String meetingId) {
		StringBuffer sb = new StringBuffer();
		sb.append("select t.signed_status from t_oa_meetsigned t where t.meeting_id = '"+meetingId+"' and t.signed_jobno = '"+ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount()+"' ");
		String signStatus = (String) this.getSession().createSQLQuery(sb.toString()).uniqueResult();
		return signStatus;
	}

	@Override
	public List<SysEmployee> getEmployeeExtendPage(SysEmployee entity,String type) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer hqlBuffer = new StringBuffer();
		if(entity != null){
			hqlBuffer.append("select  * from( select t.*,rownum as n from ( ");
		}
		hqlBuffer.append("select d.id as id,d.employee_jobno as jobNo,d.employee_name as name,d.department_code as deptCode,d.department as deptName , ");
		hqlBuffer.append("d.employee_sexcode as sex,d.employee_birthday as birthday,d.employee_type_name as type,d.duties_name as post,d.title_name as title,d.employee_mobile as mobile, ");
		hqlBuffer.append(" d.EMPLOYEE_IDENTITYCARD as idEntityCard,d.NATIONAL_NAME as family ");
		if(entity == null){
			hqlBuffer.append("from t_employee_extend d where d.del_flg = 0 and d.stop_flg ");
		}else{
			if(StringUtils.isNotBlank(entity.getDeptCode())){
				if("1".equals(type)){
					map.put("deptCode", entity.getDeptCode().substring(entity.getDeptCode().indexOf("_")+1, entity.getDeptCode().length()));
				}else {
					map.put("deptCode", entity.getDeptCode());
				}
			}
			if(StringUtils.isNotBlank(entity.getName())){
				map.put("queryName", "%" + entity.getName() + "%");
				map.put("queryNameUpp", "%" + entity.getName().toUpperCase() + "%");
			}
//			if(StringUtils.isNotBlank(entity.getType())){
//				map.put("workType", entity.getType() );
//			}
//			if(StringUtils.isNotBlank(entity.getPost())){
//				map.put("workPost", entity.getPost());
//				List<BusinessDictionary> dictionaryList = innerCodeService.getDictionary("duties");
//				for (BusinessDictionary businessDictionary : dictionaryList) {
//					String name = null;
//					if (businessDictionary.getEncode().equals(entity.getPost())) {
//						name=businessDictionary.getName();
//						if (businessDictionary.getName().equals(name)) {
//							map.put("workPostName", businessDictionary.getName());
//						}
//					}
//				}
//			}
//			if(StringUtils.isNotBlank(entity.getTitle())){
//				map.put("workTitle", entity.getTitle());
//				List<BusinessDictionary> dictionaryList = innerCodeService.getDictionary("title");
//				for (BusinessDictionary businessDictionary : dictionaryList) {
//					String name = null;
//					if (businessDictionary.getEncode().equals(entity.getTitle())) {
//						name=businessDictionary.getName();
//						if (businessDictionary.getName().equals(name)) {
//							map.put("workTitleName", businessDictionary.getName());
//						}
//					}
//				}
//			}
			hqlBuffer.append(joint(entity,type));
			Integer page = entity.getPage() == null ? 1 : Integer.valueOf(entity.getPage());
			Integer row = entity.getRows() == null ? 20 : Integer.valueOf(entity.getRows());
			map.put("page", page);
			map.put("row", row);
			hqlBuffer.append(" ) t where rownum <= :page * :row) t1 where t1.n > (:page -1) * :row ");
		}
		List<SysEmployee> list =  namedParameterJdbcTemplate.query(hqlBuffer.toString(), map, new RowMapper<SysEmployee>() {
			@Override
			public SysEmployee mapRow(ResultSet rs, int rowNum) throws SQLException {
				SysEmployee sysEmployee = new SysEmployee();
				
				sysEmployee.setId(rs.getString("id"));
				sysEmployee.setJobNo(rs.getString("jobNo"));
				sysEmployee.setName(rs.getString("name"));
				sysEmployee.setDeptCode(rs.getString("deptCode"));
				sysEmployee.setDeptName(rs.getString("deptName"));
//				sysEmployee.setInputCode(rs.getString("inputCode"));
				sysEmployee.setSex(rs.getString("sex"));
				sysEmployee.setFamily(rs.getString("family"));
				sysEmployee.setBirthday(rs.getDate("birthday"));
				sysEmployee.setIdEntityCard(rs.getString("idEntityCard"));
//				sysEmployee.setEducation(rs.getString("education"));
				sysEmployee.setType(rs.getString("type"));
				sysEmployee.setPost(rs.getString("post"));
				sysEmployee.setTitle(rs.getString("title"));
				sysEmployee.setMobile(rs.getString("mobile"));
//				sysEmployee.setIsExpert(rs.getInt("isExpert"));
//				sysEmployee.setRemark(rs.getString("remark"));
				return sysEmployee;
			}
		});
		if (list != null && list.size() > 0) {
			return list;
		}else {
			return new ArrayList<SysEmployee>();
		}
	}

	@Override
	public int getEmployeeExtendTotal(SysEmployee entity,String type) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer hqlBuffer = new StringBuffer();
		hqlBuffer.append("select count(1) ");
		if(entity == null){
			hqlBuffer.append("from t_employee_extend d where d.del_flg = 0 and d.stop_flg ");
		}else{
			if(StringUtils.isNotBlank(entity.getDeptCode())){
				if("1".equals(type)){
					map.put("deptCode", entity.getDeptCode().substring(entity.getDeptCode().indexOf("_")+1, entity.getDeptCode().length()));
				}else {
					map.put("deptCode", entity.getDeptCode());
				}
			}
			if(StringUtils.isNotBlank(entity.getName())){
				map.put("queryName", "%" + entity.getName() + "%");
				map.put("queryNameUpp", "%" + entity.getName().toUpperCase() + "%");
			}
		hqlBuffer.append(joint(entity,type));
		}
		Integer value = namedParameterJdbcTemplate.queryForObject(hqlBuffer.toString(), map, Integer.class);
		if (value != null) {
			return value;
		}
		return 0;
	}
	
	public String joint(SysEmployee entity,String type){
		StringBuffer hql=new StringBuffer("FROM t_employee_extend d WHERE d.del_flg = 0 AND d.stop_flg = 0 ");
		/**2017年2月15日11:14:02  GH 新添  type节点等级如果为1  则显示改节点下所有的员工数据**/
		if(StringUtils.isNotBlank(type)){
			if(StringUtils.isNotBlank(entity.getDeptCode())){
				if("1".equals(type)){
					String deptCode=entity.getDeptCode().substring(entity.getDeptCode().indexOf("_")+1, entity.getDeptCode().length());
					hql.append("AND d.department_code in (select t.dept_code from t_department t where t.dept_type= :deptCode and t.del_flg = 0 AND t.stop_flg = 0 )");
				}else{
					hql.append("AND d.department_code= :deptCode ");
				}
			}
		}
		if(StringUtils.isNotBlank(entity.getName())){
			String queryName = entity.getName();
			hql.append(" AND (d.employee_name LIKE :queryName"
//			  + " OR d.employee_pinyin LIKE :queryNameUpp" 
//			  + " OR d.employee_wb LIKE :queryNameUpp"
//			  + " OR d.employee_inputcode LIKE :queryName"
			  + " OR d.employee_jobno LIKE :queryName)");
		}
		if(StringUtils.isNotBlank(entity.getType())){
			hql.append(" AND d.employee_type_name =:workType");
		}
		if(StringUtils.isNotBlank(entity.getPost())){
			hql.append(" AND (d.duties_name =:workPost or d.duties_name =:workPostName)");
		}
		if(StringUtils.isNotBlank(entity.getTitle())){
			hql.append(" AND (d.title_name =:workTitle or d.title_name =:workTitleName)");
		}
		return hql.toString();
	}
}
