package cn.honry.oa.meeting.meetingSigned.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.convert.Bucket;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.oa.meeting.meetingSigned.dao.MeetingSignedDao;
import cn.honry.oa.meeting.meetingSigned.vo.MeetingSigned;
import cn.honry.oa.meeting.meetingSigned.vo.MeetingSignedVo;
import cn.honry.oa.meeting.meetingSigned.vo.SignedPersonInfoVo;
import cn.honry.utils.DateUtils;

@Repository("meetingSignedDao")
@SuppressWarnings({"all"})
public class MeetingSignedDaoImpl implements MeetingSignedDao{

	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Override
	public List<MeetingSignedVo> meetingSignedList(String meetingName,String meetingRoomName, String meetingStatusFlag, String page,String rows) {
		
		StringBuffer sb = new StringBuffer();
		Integer p = (page==null?1:Integer.parseInt(page));
		Integer r = (rows==null?20:Integer.parseInt(rows));
		
		sb.append(" SELECT ");
		sb.append(" G.*, M.MEETING_APPLICANT AS APPLYER, ");
		sb.append(" M.INSIDEPERSONCODE AS ATTENDPERSONS ");
		sb.append(" FROM ( ");
		
		sb.append(" SELECT ");
		sb.append(" R.ID AS ID, ");
		sb.append(" R.MEETINGNAME AS MEETINGNAME," );
		sb.append(" R.MEETINGSTARTTIME AS MEETINGSTARTTIME, ");
		sb.append(" R.MEETINGENDTIME AS MEETINGENDTIME, ");
		sb.append(" R.MEETINGROOMNAME AS MEETINGROOMNAME ");
		sb.append(" FROM ( ");
		sb.append(" SELECT ");
		sb.append(" ROWNUM RID, ");
		sb.append(" D.* ");
		sb.append(" FROM ( ");
		sb.append(" SELECT ");
		sb.append(" T.MEETING_ID AS ID, ");
		sb.append(" T.MEETING_NAME AS MEETINGNAME,");
		sb.append(" T.MEETING_STARTTIME AS MEETINGSTARTTIME, ");
		sb.append(" T.MEETING_ENDTIME AS MEETINGENDTIME, ");
		sb.append(" T.MEET_NAME AS MEETINGROOMNAME ");
		sb.append(" FROM ");
		sb.append(" T_OA_MEETSIGNED T ");
		sb.append(" WHERE ");
		sb.append(" 1 = 1 ");
		if(StringUtils.isNotBlank(meetingName)){
			sb.append(" AND T.MEETING_NAME LIKE '%"+meetingName+"%'");
		}
		if(StringUtils.isNotBlank(meetingRoomName)){
			sb.append(" AND T.MEET_NAME LIKE '%"+meetingRoomName+"%'");
		}
		if(StringUtils.isNotBlank(meetingStatusFlag)){
			if("0".equals(meetingStatusFlag)){//已结束，当前时间大于会议的结束时间
				sb.append(" AND SYSDATE>T.MEETING_ENDTIME ");
			} 
			if("1".equals(meetingStatusFlag)){//进行中：当前时间大于会议的开始时间，且小于会议的结束时间
				sb.append(" AND SYSDATE>=T.MEETING_STARTTIME ");
				sb.append(" AND SYSDATE<=T.MEETING_ENDTIME ");
			}
			if("2".equals(meetingStatusFlag)){//未开始：当前时间小于会议的开始时间
				sb.append(" AND SYSDATE<T.MEETING_STARTTIME ");
			}
		}
		
		
		sb.append(" GROUP BY ");
		sb.append(" T.MEETING_ID, ");
		sb.append(" T.MEETING_NAME, ");
		sb.append(" T.MEETING_STARTTIME, ");
		sb.append(" T.MEETING_ENDTIME, ");
		sb.append(" T.MEET_NAME ");
		sb.append(" ORDER BY T.MEETING_ENDTIME DESC ");
		sb.append(" ) D ");
		sb.append(" WHERE ");
		sb.append(" ROWNUM <="+p*r);
		sb.append(" ) R ");
		sb.append(" WHERE ");
		sb.append(" RID >"+(p-1)*r);
		sb.append(" ) G,T_OA_MEETAPPLY M ");
		sb.append(" WHERE ");
		sb.append(" M.DEL_FLG = 0 ");
		sb.append(" AND M.ID = G.ID ");
		
		//得到列表
		List<MeetingSignedVo> list = namedParameterJdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper(MeetingSignedVo.class));
		
		for(MeetingSignedVo m:list){			
			
			StringBuffer ps = new StringBuffer();
			//处理出席人员
			String persons = m.getAttendPersons();//数据形式：12:"张三",23:"李四",56:"王五"
			if(StringUtils.isNotBlank(persons)){
				String[] ac =persons.split(",");
				for( int i=0;i<ac.length;i++){
					String[] c =ac[i].split(":");
					if(i==ac.length-1){
						ps.append(c[1]);
					}else{
						ps.append(c[1]+",");
					}
					
				}
			}
			m.setAttendPersons(ps.toString());
			
			
			String start = m.getMeetingStartTime();
			String end = m.getMeetingEndTime();
			Date startTime=null;
			Date endTime=null;
			if(StringUtils.isNotBlank(start)){
				startTime=DateUtils.parseDateY_M_D_H_M_S(start);
			}
			if(StringUtils.isNotBlank(end)){
				endTime=DateUtils.parseDateY_M_D_H_M_S(end);
			}
			Date nowDate = new Date();
			if(DateUtils.compDate(nowDate, endTime)){//已结束，当前时间大于会议的结束时间
				m.setMeetingStatusFlag("0");
			}else if(DateUtils.compDate(startTime, nowDate)){//未开始：当前时间小于会议的开始时间
				m.setMeetingStatusFlag("2");
			}else{
				m.setMeetingStatusFlag("1");
			}
		}
	
		return list;
	}

	@Override
	public Long meetingSignedCount(String meetingName,String meetingRoomName, String meetingStatusFlag) {
		
		StringBuffer sb = new StringBuffer();
		
		sb.append(" SELECT COUNT(1) FROM( ");
		sb.append(" SELECT ");
		sb.append(" T.MEETING_ID as ID ");
		sb.append(" FROM T_OA_MEETSIGNED T,T_OA_MEETAPPLY M ");
		sb.append(" WHERE M.ID = T.MEETING_ID ");
		sb.append(" AND M .DEL_FLG = 0 ");
		if(StringUtils.isNotBlank(meetingName)){
			sb.append(" AND T.MEETING_NAME LIKE '%"+meetingName+"%'");
		}
		if(StringUtils.isNotBlank(meetingRoomName)){
			sb.append(" AND T.MEET_NAME LIKE '%"+meetingRoomName+"%'");
		}
		if(StringUtils.isNotBlank(meetingStatusFlag)){
			if("0".equals(meetingStatusFlag)){//已结束，当前时间大于会议的结束时间
				sb.append(" AND SYSDATE>T.MEETING_ENDTIME ");
			} 
			if("1".equals(meetingStatusFlag)){//进行中：当前时间大于会议的开始时间，且小于会议的结束时间
				sb.append(" AND SYSDATE>=T.MEETING_STARTTIME ");
				sb.append(" AND SYSDATE<=T.MEETING_ENDTIME ");
			}
			if("2".equals(meetingStatusFlag)){//未开始：当前时间小于会议的开始时间
				sb.append(" AND SYSDATE<T.MEETING_STARTTIME ");
			}
		}
		sb.append(" GROUP BY ");
		sb.append(" MEETING_ID )");
		
		long l = namedParameterJdbcTemplate.queryForLong(sb.toString(), new HashMap());
		return l;
	}

	
	@Override
	public void delMeetingSignedById(String id) {
		
		if(StringUtils.isBlank(id)){
			return ;
		}
		
		String[] ids=id.split(",");
		List<String> asList = Arrays.asList(ids);
		String sql = " DELETE FROM T_OA_MEETSIGNED WHERE MEETING_ID IN (:IDS) ";
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("IDS", asList);
		namedParameterJdbcTemplate.update(sql, map);
	}

	
	@Override
	public List<SignedPersonInfoVo> onTimeList(String id, String searchField,String page, String rows) {
		
		if(StringUtils.isBlank(id)){
			return new ArrayList<SignedPersonInfoVo>();
		}
		StringBuffer sb = new StringBuffer();
		Integer p = (page==null?1:Integer.parseInt(page));
		Integer r = (rows==null?20:Integer.parseInt(rows));
		
		sb.append(" SELECT T.* FROM ( ");
		sb.append(" SELECT ");
		sb.append(" ROWNUM AS RID, ");
		sb.append(" SIGNED_NAME AS PNAME, ");
		sb.append(" SIGNED_JOBNO AS PACCOUNT,");
		sb.append(" SIGNED_DEPTNAME AS DEPTNAME,");
		sb.append(" SIGNED_TIME AS SIGNEDTIME ");
		sb.append(" FROM ");
		sb.append(" T_OA_MEETSIGNED ");
		sb.append(" WHERE ");
		sb.append(" SIGNED_STATUS='0' ");
		sb.append(" AND ROWNUM<="+p*r);
		sb.append(" AND MEETING_ID='"+id+"'");
		if(StringUtils.isNotBlank(searchField)){
			sb.append(" AND( SIGNED_NAME LIKE '%"+searchField+"%'");
			sb.append(" OR SIGNED_JOBNO LIKE '%"+searchField+"%'");
			sb.append(" OR SIGNED_DEPTNAME LIKE '%"+searchField+"%' )");
		}
		sb.append(" ) T ");
		sb.append(" WHERE ");
		sb.append(" RID>"+(p-1)*r);
		
		List<SignedPersonInfoVo> list = namedParameterJdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper(SignedPersonInfoVo.class));
		return list;
	}

	@Override
	public Long onTimeNum(String id, String searchField) {
		
		if(StringUtils.isBlank(id)){
			return 0l;
		}
		StringBuffer sb = new StringBuffer();
		
		sb.append(" SELECT COUNT(1) FROM  ");
		sb.append(" T_OA_MEETSIGNED ");
		sb.append(" WHERE ");
		sb.append(" SIGNED_STATUS='0' ");
		sb.append(" AND MEETING_ID='"+id+"'");
		if(StringUtils.isNotBlank(searchField)){
			sb.append(" AND( SIGNED_NAME LIKE '%"+searchField+"%'");
			sb.append(" OR SIGNED_JOBNO LIKE '%"+searchField+"%'");
			sb.append(" OR SIGNED_DEPTNAME LIKE '%"+searchField+"%' )");
		}
		
		return  namedParameterJdbcTemplate.queryForLong(sb.toString(), new HashMap());
	}

	@Override
	public List<SignedPersonInfoVo> isLateList(String id, String searchField,String page, String rows) {
		
		if(StringUtils.isBlank(id)){
			return new ArrayList<SignedPersonInfoVo>();
		}
		StringBuffer sb = new StringBuffer();
		Integer p = (page==null?1:Integer.parseInt(page));
		Integer r = (rows==null?20:Integer.parseInt(rows));
		
		sb.append(" SELECT T.* FROM ( ");
		sb.append(" SELECT ");
		sb.append(" ROWNUM AS RID, ");
		sb.append(" B.SIGNED_NAME AS PNAME, ");
		sb.append(" B.SIGNED_JOBNO AS PACCOUNT,");
		sb.append(" B.SIGNED_DEPTNAME AS DEPTNAME,");
		sb.append(" B.SIGNED_TIME AS SIGNEDTIME, ");
		sb.append(" M.ISLATENUM AS ISLATENUM ");
		sb.append(" FROM ");
		sb.append(" T_OA_MEETSIGNED B,");
		sb.append(" (SELECT COUNT(1) AS ISLATENUM ,SIGNED_JOBNO AS PACCOUNT  FROM T_OA_MEETSIGNED WHERE SIGNED_STATUS='1' GROUP BY SIGNED_JOBNO) M ");
		sb.append(" WHERE ");
		sb.append(" B.SIGNED_STATUS='1' ");
		sb.append(" AND ROWNUM<="+p*r);
		sb.append(" AND B.MEETING_ID='"+id+"'");
		sb.append(" AND M.PACCOUNT=B.SIGNED_JOBNO ");
		if(StringUtils.isNotBlank(searchField)){
			sb.append(" AND( B.SIGNED_NAME LIKE '%"+searchField+"%'");
			sb.append(" OR B.SIGNED_JOBNO LIKE '%"+searchField+"%'");
			sb.append(" OR B.SIGNED_DEPTNAME LIKE '%"+searchField+"%' )");
		}
		sb.append(" ) T ");
		sb.append(" WHERE ");
		sb.append(" RID>"+(p-1)*r);
		
		List<SignedPersonInfoVo> list = namedParameterJdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper(SignedPersonInfoVo.class));
		return list;
	}

	@Override
	public Long isLateNum(String id, String searchField) {
		
		if(StringUtils.isBlank(id)){
			return 0l;
		}
		StringBuffer sb = new StringBuffer();
		
		sb.append(" SELECT COUNT(1) FROM ");
		sb.append(" T_OA_MEETSIGNED ");
		sb.append(" WHERE ");
		sb.append(" SIGNED_STATUS='1' ");
		sb.append(" AND MEETING_ID='"+id+"'");
		if(StringUtils.isNotBlank(searchField)){
			sb.append(" AND( SIGNED_NAME LIKE '%"+searchField+"%'");
			sb.append(" OR SIGNED_JOBNO LIKE '%"+searchField+"%'");
			sb.append(" OR SIGNED_DEPTNAME LIKE '%"+searchField+"%' )");
		}
		return  namedParameterJdbcTemplate.queryForLong(sb.toString(), new HashMap());
	}

	@Override
	public List<SignedPersonInfoVo> noComeList(String id, String searchField,String page, String rows) {
		
		
		Map<String, Object> map = mapData(id,searchField,"noCome");
		List<SignedPersonInfoVo> list=(List<SignedPersonInfoVo>) map.get("noComeList");
		Integer p = (page==null?1:Integer.parseInt(page));
		Integer r = (rows==null?20:Integer.parseInt(rows));
		//模拟分页：
		//查询的起始行：>=(p-1)*r
		//查询的结束：<(p*r>list.size()?list.size():p*r)
		int start = (p-1)*r;//查询的起始行
		int totalCount = list.size();//总记录数
		int mid = p*r;
		int end = (mid>totalCount?totalCount:mid);//查询的结束行
		
		ArrayList<SignedPersonInfoVo> voList = new ArrayList<SignedPersonInfoVo>();
		if(list!=null&&list.size()>0){
			for(int i=start;i<end;i++){
				voList.add(list.get(i));
			}
		}
		
		return voList;
	}

	@Override
	public Long noComeNum(String id, String searchField) {
		
		Map<String, Object> map = mapData(id,searchField,"noCome");
		Integer p=(Integer)map.get("noComeNum");
		long noComeNum=p.longValue();
		return noComeNum;
	}

	@Override
	public List<SignedPersonInfoVo> tempComeList(String id, String searchField,String page, String rows) {
		
		Map<String, Object> map = mapData(id,searchField,"tempCome");
		List<SignedPersonInfoVo> list=(List<SignedPersonInfoVo>) map.get("tempComeList");
		Integer p = (page==null?1:Integer.parseInt(page));
		Integer r = (rows==null?20:Integer.parseInt(rows));
		//模拟分页：
		//查询的起始行：>=(p-1)*r
		//查询的结束：<(p*r>list.size()?list.size():p*r)
		int start = (p-1)*r;//查询的起始行
		int totalCount = list.size();//总记录数
		int mid = p*r;
		int end = (mid>totalCount?totalCount:mid);//查询的结束行
		
		ArrayList<SignedPersonInfoVo> voList = new ArrayList<SignedPersonInfoVo>();
		if(list!=null&&list.size()>0){
			for(int i=start;i<end;i++){
				voList.add(list.get(i));
			}
		}
		
		return voList;
	}

	@Override
	public Long tempComeNum(String id, String searchField) {
		
		Map<String, Object> map = mapData(id,searchField,"tempCome");
		Integer p=(Integer) map.get("tempComeNum");
		long tempComeNum=p.longValue();
		return tempComeNum;
	}
	
	/**
	 * 
	 * <p>查询未到，临时参加列表 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年9月1日 上午11:21:15 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年9月1日 上午11:21:15 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param id 会议id
	 * @param searchField 搜索关键词：员工号，员工姓名，所在科室
	 * @param flag 调用标记：查询未来：noCome，查询临时参加：tempCome,用于优化查询，减少情况是未到时的查询，因为我爸这两个功能1.未来2.临时参加写到一个方法了
	 * @return
	 * @throws:
	 *
	 */
	public Map<String,Object> mapData(String id, String searchField,String flag){
		
				Map<String,Object> map= new HashMap<String,Object>();
				//未到： 签到成功表中没有但是会议室使用申请表中有的数据。
				//1.查询会议使用申请表中的出席人员
				StringBuffer sql_apply = new StringBuffer();
				sql_apply.append(" SELECT INSIDEPERSONCODE  AS PACCOUNT FROM T_OA_MEETAPPLY WHERE ID ='"+id+"'"+"AND DEL_FLG=0 ");//查询结果为 员工号:姓名 如12:"张三",23:"李四",56:"王五"
				if(StringUtils.isNotBlank(searchField)){
					sql_apply.append(" AND( INSIDEPERSONCODE LIKE '%"+searchField+"%'");
					sql_apply.append(" OR MEET_NAME LIKE '%"+searchField+"%' )");
				}
				
				List<SignedPersonInfoVo> applyList = namedParameterJdbcTemplate.query(sql_apply.toString(), new BeanPropertyRowMapper(SignedPersonInfoVo.class));
				//存放会议申请的员工号的list
				List<String> list1 = new ArrayList<String>();
				if(applyList!=null&&applyList.size()>0){
					String accounts = applyList.get(0).getpAccount();//数据形式：12:"张三",23:"李四",56:"王五"
					if(StringUtils.isNotBlank(accounts)){
						String[] ac =accounts.split(",");
						for(String str:ac){
							if(StringUtils.isNotBlank(searchField)){
								if(str.contains(searchField)){
									String[] c =str.split(":");
									list1.add(c[0]);
								}
							}else{
								String[] c =str.split(":");
								list1.add(c[0]);
							}

							
						}
					}
				}
				
				//2.查询签到表出席人员员工号
				StringBuffer sql_signed = new StringBuffer();
				sql_signed.append(" SELECT ");
				sql_signed.append(" B.SIGNED_NAME AS PNAME, ");//姓名
				sql_signed.append(" B.SIGNED_JOBNO AS PACCOUNT,");//员工号
				sql_signed.append(" B.SIGNED_DEPTNAME AS DEPTNAME,");//科室名
				sql_signed.append(" B.SIGNED_TIME AS SIGNEDTIME ");//签到时间
				sql_signed.append(" FROM ");
				sql_signed.append(" T_OA_MEETSIGNED B ");
				sql_signed.append(" WHERE MEETING_ID='"+id+"'");
				if(StringUtils.isNotBlank(searchField)){
					sql_signed.append(" AND( B.SIGNED_NAME LIKE '%"+searchField+"%'");
					sql_signed.append(" OR B.SIGNED_JOBNO LIKE '%"+searchField+"%'");
					sql_signed.append(" OR B.SIGNED_DEPTNAME LIKE '%"+searchField+"%' )");
				}
				sql_signed.append(" AND SIGNED_WAY IS NOT NULL ");//这个条件是为了区别：是发布的会议所占用的一条记录，还是签到人员会议签到记录。因为这个表中包含了会议签到记录和院办发布的会议申请记录
				
				List<SignedPersonInfoVo> signedList = namedParameterJdbcTemplate.query(sql_signed.toString(), new BeanPropertyRowMapper(SignedPersonInfoVo.class));
				//存放签到表出席人员员工号
				List<String> list2 = new ArrayList<String>();
				//key:员工号，value:签到员工信息vo
				HashMap<String,SignedPersonInfoVo> map1 = new HashMap<String, SignedPersonInfoVo>();
				for(SignedPersonInfoVo v:signedList){
					String account = v.getpAccount();
					list2.add(account);
					map1.put(account, v);
				}
				
				//未到： 签到成功表中没有但是会议室使用申请表中有的数据。
				//未来，人员vo的集合
				List<SignedPersonInfoVo> noComeList = new ArrayList<SignedPersonInfoVo>();
				//copy一份list1用于操作list1中的元素，copy一份list2用于操作list2中的元素
				if("noCome".equals(flag)){
					List<String> list3= new ArrayList<String>();//会议申请
					list3.addAll(list1);
					List<String> list4 = new ArrayList<String>();//签到
					list4.addAll(list2);
					//未到的员工号
					list3.removeAll(list4);
					for(String str:list3){
						//1.得到未到的员工号
						//2.去T_employee表中根据员工号查询员工信息
						String sql = " SELECT T .EMPLOYEE_NAME AS PNAME,M .DEPT_NAME AS DEPTNAME FROM "
								      + " T_EMPLOYEE T,T_DEPARTMENT M WHERE T .EMPLOYEE_JOBNO ='"+str+
								      "'AND T.STOP_FLG = 0 AND T.DEL_FLG = 0 AND T.DEPT_ID = M.DEPT_ID ";
						List<SignedPersonInfoVo> list = namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper(SignedPersonInfoVo.class));
						if(list!=null&&list.size()>0){
							SignedPersonInfoVo vo = list.get(0);
							vo.setpAccount(str);//设置员工号
							noComeList.add(vo);
						}
					}
				}
				
				//得到临时参加人员的信息列表
				List<SignedPersonInfoVo> tempComeList=new ArrayList<SignedPersonInfoVo>(); 
				if("tempCome".equals(flag)){
					//临时参加：签到成功表中有但是会议室使用申请表中没有的数据。
					//copy一份list1用于操作list1中的元素，copy一份list2用于操作list2中的元素
					List<String> list5= new ArrayList<String>();//会议申请
					list5.addAll(list1);
					List<String> list6 = new ArrayList<String>();//签到
					list6.addAll(list2);
					//临时参加员工员工号
					list6.removeAll(list5);
					
					for(String k:list6){
						tempComeList.add(map1.get(k));
					}
				}
				map.put("noComeList", noComeList);//未到列表
				map.put("noComeNum", noComeList.size());//未到总人数
				map.put("tempComeList", tempComeList);//临时参加列表
				map.put("tempComeNum", tempComeList.size());//临时参加总人数
				
				return map;
	}
	
	
	@Override
	public void insertMeetingSigned(MeetingSigned meetingSigned) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startDate="'"+dateFormat.format(meetingSigned.getMeeting_tartTime())+"'";
		String endDate="'"+dateFormat.format(meetingSigned.getMeeting_endTime())+"'";
		
		String sql ="INSERT INTO T_OA_MEETSIGNED(ID,MEET_NAME,MEETING_ID,MEETING_NAME,MEETING_STARTTIME,MEETING_ENDTIME) VALUES ( '"+
					meetingSigned.getId()+"' ,'"+meetingSigned.getMeet_name()+"' ,'"+meetingSigned.getMeeting_id()+"' ,'"+meetingSigned.getMeeting_name()+
					"' ,to_date("+startDate+",'yyyy-mm-dd hh24:mi:ss') ,to_date("+endDate+",'yyyy-mm-dd hh24:mi:ss'))";
		
		namedParameterJdbcTemplate.update(sql, new HashMap());
	}

	@Override
	public void updateMeetingSigned(MeetingSigned meetingSigned) {

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startDate="to_date('"+dateFormat.format(meetingSigned.getMeeting_tartTime())+"','yyyy-mm-dd hh24:mi:ss')";
		String endDate="to_date('"+dateFormat.format(meetingSigned.getMeeting_endTime())+"','yyyy-mm-dd hh24:mi:ss')";
		
		String sql =" UPDATE T_OA_MEETSIGNED SET MEET_NAME='"+meetingSigned.getMeet_name()
					+"', MEETING_NAME='"+meetingSigned.getMeeting_name()+"', MEETING_STARTTIME="+startDate+", MEETING_ENDTIME="+endDate
					+" WHERE MEETING_ID='"+meetingSigned.getMeeting_id()+"'"; 
	
		namedParameterJdbcTemplate.update(sql, new HashMap<String, Object>());
	}
	
}
