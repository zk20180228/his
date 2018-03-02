package cn.honry.oa.meeting.meetingApply.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.OaMeetingApply;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.crazycake.shiro.SerializeUtils;
import cn.honry.mq.MessageSend;
import cn.honry.oa.meeting.meetingApply.dao.OaMeetingApplyDao;
import cn.honry.oa.meeting.meetingApply.service.OaMeetingApplyService;
import cn.honry.oa.meeting.meetingSigned.dao.MeetingSignedDao;
import cn.honry.oa.meeting.meetingSigned.vo.MeetingSigned;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.RedisUtil;
import cn.honry.utils.ShiroSessionUtils;

@Service("oaMeetingApplyService")
@Transactional
@SuppressWarnings({ "all" })
public class OaMeetingApplyServiceImpl implements OaMeetingApplyService {
	@Autowired
	@Qualifier(value = "oaMeetingApplyDao")
	private OaMeetingApplyDao oaMeetingApplyDao;
	@Resource
	private RedisUtil redis;
	@Resource
	private MeetingSignedDao meetingSignedDao;
	public void setMeetingSignedDao(MeetingSignedDao meetingSignedDao) {
		this.meetingSignedDao = meetingSignedDao;
	}
	@Resource
	private MessageSend messageSend;
	
	@Override
	public void removeUnused(String arg0) {

	}

	@Override
	public void saveOrUpdate(OaMeetingApply oaMeetingApply, String type,String flag) {
		Map<String , Object> mapRemind = new HashMap<String, Object>();
		if(StringUtils.isBlank(oaMeetingApply.getSignBeforeTime())){
			//提前签到时间默认30min
			oaMeetingApply.setSignBeforeTime("30");
		}
		
		if(StringUtils.isNotBlank(oaMeetingApply.getMeetingDescribeString())){
			byte[] bs = SerializeUtils.serialize(oaMeetingApply.getMeetingDescribeString());
			String string =  (String) SerializeUtils.deserialize(bs);
			Clob clob = Hibernate.createClob(string);
			oaMeetingApply.setMeetingDescribe(clob);
		}
		
		String longinUserAccount = ShiroSessionUtils.getCurrentUserFromShiroSession().getName();
		String longinUserAccountCode = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		if(StringUtils.isBlank(oaMeetingApply.getId())){
			String uuid = UUID.randomUUID().toString().replace("-", "");//主键自己维护因为我要在插入之后获取主键
			oaMeetingApply.setId(uuid);
			//申请人
			oaMeetingApply.setMeetingApplicant(longinUserAccount);
			//申请人Code
			oaMeetingApply.setMeetingApplicantCode(longinUserAccountCode);
			//申请时间
			oaMeetingApply.setMeetingApplicanttime(DateUtils.getCurrentTime());
			/**申请状态0:申请状态,1:审核成功状态,2:申请拒绝状态,3:撤销状态**/
			if("YZBGS".equals(flag)){//院长办公室模块可以直接发布，不用审批
				oaMeetingApply.setMeetingApptype(1);
				if(oaMeetingApply.getMeetingNoticehour()!=null){
					boolean isToday = false;
//					判断会议是不是当天。当天就往redies里面放
					switch (oaMeetingApply.getMeetingPeriodicity()) {
					case "no":
						String date = DateUtils.getDate();
						String d2 = DateUtils.formatDateY_M_D(oaMeetingApply.getMeetingStarttime());
						if(date.equals(d2)){//当天数据
							isToday = true;
						}
						break;
					case "week":
						int week = DateUtils.getWeekOfDay(DateUtils.formatDateY_M_D_H_M(DateUtils.getCurrentTime()));
						if(oaMeetingApply.getMeetingApplyweek().contains(String.valueOf(week))){
							isToday = true;
						}
						
						break;
					case "month":
						String day = DateUtils.getStringDay();
						if(oaMeetingApply.getMeetingApplyweek().contains(day)){
							isToday = true;
						}
						break;

					default:
						break;
					}
					if(isToday){
						String field=DateUtils.formatDateY_M_D_H_M(DateUtils.addHour(oaMeetingApply.getMeetingStarttime(), -oaMeetingApply.getMeetingNoticehour())).split(" ")[1];
						mapRemind.put("insidePerson", oaMeetingApply.getInSidePersonCode());
						mapRemind.put("meetingName", oaMeetingApply.getMeetingName());
						redis.hset("meeting", field, mapRemind);
					}
				}
			}else{
				oaMeetingApply.setMeetingApptype(0);
			}
			
			
			if(StringUtils.isNotBlank(String.valueOf(oaMeetingApply.getMeetingNoticehour()))){
				if(oaMeetingApply.getMeetingNoticehour()!=null){
					String remindTime = DateUtils.formatDateY_M_D_H_M(DateUtils.addHour(oaMeetingApply.getMeetingStarttime(),-oaMeetingApply.getMeetingNoticehour())).split(" ")[1]; 
					oaMeetingApply.setMeetingRemindTime(remindTime);
				}
			}
			oaMeetingApply.setCreateUser(longinUserAccountCode);
			oaMeetingApply.setCreateTime(DateUtils.getCurrentTime());
			try {
				oaMeetingApplyDao.save(oaMeetingApply);
				//给会议室管理员发出消息提醒
				Map<String, String> map = new HashMap<String, String>();
				map.put("jid", oaMeetingApply.getMeetingAdminCode());
				map.put("msg_type", "msg_type_meetingApprove_message");
				map.put("msg_title", oaMeetingApply.getMeetingName());
				map.put("msg_time", DateUtils.formatDateY_M_D_H_M_S(DateUtils.getCurrentTime()));
				String json = JSONUtils.toJson(map);
				System.out.println(json);
				messageSend.sendMessage(json);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			//以上完毕后，要在签到表中插入一条记录(用于院长办公室)
			if("YZBGS".equals(flag)){
				updateMeetingSigned(oaMeetingApply, "save");
			}
		}else{
			if("redo".equals(type)){
				OaMeetingApply oaMeetingApplyRedo = oaMeetingApplyDao.get(oaMeetingApply.getId());
				oaMeetingApplyRedo = changeClob(oaMeetingApplyRedo);
				oaMeetingApplyRedo.setMeetingApptype(3);
				oaMeetingApplyRedo.setMeetingApprover(longinUserAccount);
				oaMeetingApplyRedo.setMeetingApproverCode(longinUserAccountCode);
				oaMeetingApplyRedo.setMeetingApprovertime(DateUtils.getCurrentTime());
				oaMeetingApplyDao.save(oaMeetingApplyRedo);
				
				//以上完毕后，要在签到表中删除该条记录
				if("YZBGS".equals(flag)){
					meetingSignedDao.delMeetingSignedById(oaMeetingApply.getId());
				}
			}else if("approveOk".equals(type)){
				OaMeetingApply oaMeetingApplyOk = oaMeetingApplyDao.get(oaMeetingApply.getId());
				oaMeetingApplyOk = changeClob(oaMeetingApplyOk);
				oaMeetingApplyOk.setMeetingApptype(1);
				oaMeetingApplyOk.setMeetingApprover(longinUserAccount);
				oaMeetingApplyOk.setMeetingApproverCode(longinUserAccountCode);
				oaMeetingApplyOk.setMeetingApprovertime(DateUtils.getCurrentTime());
				
				if(oaMeetingApplyOk.getMeetingNoticehour()!=null){
					boolean isToday = false;
//					判断会议是不是当天。当天就往redies里面放
					switch (oaMeetingApplyOk.getMeetingPeriodicity()) {
					case "no":
						String date = DateUtils.getDate();
						String d2 = DateUtils.formatDateY_M_D(oaMeetingApplyOk.getMeetingStarttime());
						if(date.equals(d2)){//当天数据
							isToday = true;
						}
						break;
					case "week":
						int week = DateUtils.getWeekOfDay(DateUtils.formatDateY_M_D_H_M(DateUtils.getCurrentTime()));
						if(oaMeetingApplyOk.getMeetingApplyweek().contains(String.valueOf(week))){
							isToday = true;
						}
						
						break;
					case "month":
						String day = DateUtils.getStringDay();
						if(oaMeetingApplyOk.getMeetingApplyweek().contains(day)){
							isToday = true;
						}
						break;

					default:
						break;
					}
					if(isToday){
						String field=DateUtils.formatDateY_M_D_H_M(DateUtils.addHour(oaMeetingApplyOk.getMeetingStarttime(), -oaMeetingApplyOk.getMeetingNoticehour())).split(" ")[1];
						mapRemind.put("insidePerson", oaMeetingApplyOk.getInSidePersonCode());
						mapRemind.put("meetingName", oaMeetingApplyOk.getMeetingName());
						redis.hset("meeting", field, mapRemind);
					}
				}
				
				oaMeetingApplyDao.save(oaMeetingApplyOk);
			}else if("approveNo".equals(type)){
				OaMeetingApply oaMeetingApplyNo = oaMeetingApplyDao.get(oaMeetingApply.getId());
				oaMeetingApplyNo = changeClob(oaMeetingApplyNo);
				oaMeetingApplyNo.setMeetingApptype(2);
				oaMeetingApplyNo.setMeetingApprover(longinUserAccount);
				oaMeetingApplyNo.setMeetingApproverCode(longinUserAccountCode);
				oaMeetingApplyNo.setMeetingApprovertime(DateUtils.getCurrentTime());
				oaMeetingApplyDao.save(oaMeetingApplyNo);				
			}else{
				OaMeetingApply oaMeetingApplyNew = oaMeetingApplyDao.get(oaMeetingApply.getId());
				if(StringUtils.isNotBlank(String.valueOf(oaMeetingApply.getMeetingNoticehour()))){
//					DateUtils.formatDateY_M_D_H_M_S(DateUtils.addHour(oaMeetingApply.getMeetingStarttime(),-oaMeetingApply.getMeetingNoticehour())).split(" ")[1];
					if(oaMeetingApply.getMeetingNoticehour()!=null){
						String remindTime = DateUtils.formatDateY_M_D_H_M(DateUtils.addHour(oaMeetingApply.getMeetingStarttime(),-oaMeetingApply.getMeetingNoticehour())).split(" ")[1]; 
						oaMeetingApplyNew.setMeetingRemindTime(remindTime);
					}
				}
				oaMeetingApplyNew.setId(oaMeetingApply.getId());
				oaMeetingApplyNew.setMeetId(oaMeetingApply.getMeetId());
				oaMeetingApplyNew.setMeetingName(oaMeetingApply.getMeetingName());
				oaMeetingApplyNew.setMeetingTheme(oaMeetingApply.getMeetingTheme());
				oaMeetingApplyNew.setMeetingNature(oaMeetingApply.getMeetingNature());
				oaMeetingApplyNew.setMeetingAdmin(oaMeetingApply.getMeetingAdmin());
				oaMeetingApplyNew.setMeetingAdminCode(oaMeetingApply.getMeetingAdminCode());
				oaMeetingApplyNew.setMeetingMinutes(oaMeetingApply.getMeetingMinutes());
				oaMeetingApplyNew.setMeetingMinutesCode(oaMeetingApply.getMeetingMinutesCode());
				oaMeetingApplyNew.setMeetingDescribe(oaMeetingApply.getMeetingDescribe());
				oaMeetingApplyNew.setMeetingAttendance(oaMeetingApply.getMeetingAttendance());
				oaMeetingApplyNew.setInSidePersonCode(oaMeetingApply.getInSidePersonCode());
				oaMeetingApplyNew.setMeetingDept(oaMeetingApply.getMeetingDept());
				oaMeetingApplyNew.setMeetingDeptCode(oaMeetingApply.getMeetingDeptCode());
				oaMeetingApplyNew.setMeetingPeriodicity(oaMeetingApply.getMeetingPeriodicity());
				oaMeetingApplyNew.setMeetingStarttime(oaMeetingApply.getMeetingStarttime());
				oaMeetingApplyNew.setMeetingEndtime(oaMeetingApply.getMeetingEndtime());
				oaMeetingApplyNew.setMeetingEmail(oaMeetingApply.getMeetingEmail());
				oaMeetingApplyNew.setMeetingSchedule(oaMeetingApply.getMeetingSchedule());
				oaMeetingApplyNew.setMeetingRemind(oaMeetingApply.getMeetingRemind());
				oaMeetingApplyNew.setMeetingNotice(oaMeetingApply.getMeetingNotice());
				oaMeetingApplyNew.setMeetingNoticedept(oaMeetingApply.getMeetingNoticedept());
				oaMeetingApplyNew.setMeetingNoticehour(oaMeetingApply.getMeetingNoticehour());
				oaMeetingApplyNew.setMeetingNoticeminute(oaMeetingApply.getMeetingNoticeminute());
				oaMeetingApplyNew.setMeetingNoticefrequency(oaMeetingApply.getMeetingNoticefrequency());
				oaMeetingApplyNew.setMeetingApplicant(oaMeetingApplyNew.getMeetingApplicant());
				oaMeetingApplyNew.setMeetingApplicantCode(oaMeetingApplyNew.getMeetingApplicantCode());
				oaMeetingApplyNew.setMeetingApplicanttime(DateUtils.getCurrentTime());
				oaMeetingApplyNew.setMeetingApprover(oaMeetingApply.getMeetingApprover());
				oaMeetingApplyNew.setMeetingApproverCode(oaMeetingApply.getMeetingApproverCode());
				oaMeetingApplyNew.setMeetingApprovertime(oaMeetingApply.getMeetingApprovertime());
				oaMeetingApplyNew.setMeetingApptype(0);
				
				oaMeetingApplyNew.setMeetingFile(oaMeetingApply.getMeetingFile());
				oaMeetingApplyNew.setMeetingFileName(oaMeetingApply.getMeetingFileName());
				//提前签到时间默认30min
				String signBeforeTime = "30";
				if(StringUtils.isNotBlank(oaMeetingApply.getSignBeforeTime())){
					signBeforeTime = oaMeetingApply.getSignBeforeTime();
				}
				oaMeetingApplyNew.setSignBeforeTime(signBeforeTime);
				
				oaMeetingApplyNew.setUpdateTime(DateUtils.getCurrentTime());
				oaMeetingApplyNew.setUpdateUser(longinUserAccountCode);
				try {
					oaMeetingApplyDao.save(oaMeetingApplyNew);
					
					//给会议室管理员发出消息提醒
					Map<String, String> map = new HashMap<String, String>();
					map.put("jid", oaMeetingApply.getMeetingAdminCode());
					map.put("msg_type", "msg_type_meetingApprove_message");
					map.put("msg_title", oaMeetingApply.getMeetingName());
					map.put("msg_time", DateUtils.formatDateY_M_D_H_M_S(DateUtils.getCurrentTime()));
					String json = JSONUtils.toJson(map);
					System.out.println(json);
					messageSend.sendMessage(json);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				//以上完毕后，要在签到表中更新对应的一条记录(用于院长办公室)
				if("YZBGS".equals(flag)){
					updateMeetingSigned(oaMeetingApplyNew, "update");
					if(oaMeetingApplyNew.getMeetingNoticehour()!=null){
						boolean isToday = false;
//						判断会议是不是当天。当天就往redies里面放
						switch (oaMeetingApplyNew.getMeetingPeriodicity()) {
						case "no":
							String date = DateUtils.getDate();
							String d2 = DateUtils.formatDateY_M_D(oaMeetingApplyNew.getMeetingStarttime());
							if(date.equals(d2)){//当天数据
								isToday = true;
							}
							break;
						case "week":
							int week = DateUtils.getWeekOfDay(DateUtils.formatDateY_M_D_H_M(DateUtils.getCurrentTime()));
							if(oaMeetingApplyNew.getMeetingApplyweek().contains(String.valueOf(week))){
								isToday = true;
							}
							
							break;
						case "month":
							String day = DateUtils.getStringDay();
							if(oaMeetingApplyNew.getMeetingApplyweek().contains(day)){
								isToday = true;
							}
							break;

						default:
							break;
						}
						if(isToday){
							String field=DateUtils.formatDateY_M_D_H_M(DateUtils.addHour(oaMeetingApplyNew.getMeetingStarttime(), -oaMeetingApplyNew.getMeetingNoticehour())).split(" ")[1];
							mapRemind.put("insidePerson", oaMeetingApplyNew.getInSidePersonCode());
							mapRemind.put("meetingName", oaMeetingApplyNew.getMeetingName());
							redis.hset("meeting", field, mapRemind);
						}
					}
				}
			}
		}
	}

	@Override
	public int getTotal(OaMeetingApply oaMeetingApply) {
		return oaMeetingApplyDao.getTotal(oaMeetingApply);
	}

	@Override
	public List<OaMeetingApply> query(OaMeetingApply oaMeetingApply) {
		return oaMeetingApplyDao.query(oaMeetingApply);
	}
	
	@Override
	public List<OaMeetingApply> queryMyMeeting(OaMeetingApply oaMeetingApply) {
		/**  一次性查出该用户的所有会议  **/
		Date currentTime = DateUtils.getCurrentTime();
		List<OaMeetingApply> list = oaMeetingApplyDao.query(oaMeetingApply);
		List<OaMeetingApply> allList = new ArrayList<OaMeetingApply>();//所有会议
		List<OaMeetingApply> finishList = new ArrayList<OaMeetingApply>();//整理后的会议
		for(OaMeetingApply oma : list){
			allList.addAll(changeToNoCycle(oma));
		}
		Map<String,OaMeetingApply> map = new HashMap<String,OaMeetingApply>();
		String[] stringStartDate = new String[allList.size()];
		int i = 0;
		for(OaMeetingApply oma : allList){
			Date starttime = oma.getMeetingStarttime();
			String startDate = DateUtils.formatDateY_M_D_H_M_S(starttime);
			Date endtime = oma.getMeetingEndtime();
			if(currentTime.compareTo(starttime)==-1){//当前时间小于开始时间---未开始
				oma.setIsEnd("1");
			}else if(endtime.compareTo(currentTime)==-1){//结束时间小于当前时间---已结束
				oma.setIsEnd("2");
				String sign = oaMeetingApplyDao.findSign(oma.getId());//签到标记：0-成功，1-迟到，8-未到
				oma.setSignStatus(sign==null?"8":sign);
			}
			stringStartDate[i++]=startDate;
			map.put(startDate, oma);
		}
		//按时间排序
		Comparator comparator = Collator.getInstance(java.util.Locale.CHINA);
		Arrays.sort(stringStartDate, comparator);
		for(int j = 0;j<stringStartDate.length;j++){
			if(StringUtils.isNotBlank(stringStartDate[j])){
				finishList.add(map.get(stringStartDate[j]));
			}
		}
		return finishList;
	}

	/**
	 * 按时间排序
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年9月19日 下午3:08:10 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年9月19日 下午3:08:10 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param list
	 * @return:
	 * @throws:
	 * @return: List<OaMeetingApply>
	 *
	 */
	private List<OaMeetingApply> listSortByTime(List<OaMeetingApply> list){
		List<OaMeetingApply> finishList = new ArrayList<OaMeetingApply>();
		return list;
	}
	/**
	 * 将周期会议拆成单个会议
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年9月18日 下午5:34:42 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年9月18日 下午5:34:42 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param oma
	 * @return:
	 * @throws:
	 * @return: List<OaMeetingApply>
	 *
	 */
	private List<OaMeetingApply> changeToNoCycle(OaMeetingApply oma){
		List<OaMeetingApply> list = new ArrayList<OaMeetingApply>();
		Date meetingStartDate = null;
		Date meetingEndDate = null;
		String stringMeetingStarttime = DateUtils.formatDateY_M_D_H_M_S(oma.getMeetingStarttime()).split(" ")[1];
		String stringMeetingendtime = DateUtils.formatDateY_M_D_H_M_S(oma.getMeetingEndtime()).split(" ")[1];
		String nowMeetingStart = "";
		String nowMeetingEnd = "";
		if(StringUtils.isNotBlank(oma.getMeetingPeriodicity())){
			if("no".equals(oma.getMeetingPeriodicity())){
				list.add(oma);
			}else{
				meetingStartDate = DateUtils.parseDateY_M_D(DateUtils.formatDateY_M_D(oma.getMeetingStarttime()));
				meetingEndDate = DateUtils.parseDateY_M_D(DateUtils.formatDateY_M_D(oma.getMeetingEndtime()));
				String stringMeetingEndDate = DateUtils.formatDateY_M_D(meetingEndDate);
				while(meetingStartDate.compareTo(meetingEndDate)==-1)  {//开始时间小于结束时间
					String stringMeetingStartDate = DateUtils.formatDateY_M_D(meetingStartDate);
					String day = "";
					if("week".equals(oma.getMeetingPeriodicity())){
						day = String.valueOf(DateUtils.getWeekOfDay(stringMeetingStartDate));
					}else if("month".equals(oma.getMeetingPeriodicity())){
						day = stringMeetingStartDate.split("-")[2]; 
						if(day.length()==1){
							day = "0"+day;
						}
					}
					if(oma.getMeetingApplyweek().contains(String.valueOf(day))){
						OaMeetingApply newOma = copyOaMeetingApply(oma);
						nowMeetingStart = stringMeetingStartDate + " " + stringMeetingStarttime;
						nowMeetingEnd = stringMeetingEndDate + " " + stringMeetingendtime;
						newOma.setMeetingStarttime(DateUtils.parseDateY_M_D_H_M_S(nowMeetingStart));
						newOma.setMeetingEndtime(DateUtils.parseDateY_M_D_H_M_S(nowMeetingEnd));
						list.add(newOma);
					}
					meetingStartDate = DateUtils.addDay(meetingStartDate, 1);
				}
			}
		}
		return list;
	}
	/**
	 * 复制对象
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年9月19日 上午10:24:52 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年9月19日 上午10:24:52 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param oma
	 * @return:
	 * @throws:
	 * @return: OaMeetingApply
	 *
	 */
	private OaMeetingApply copyOaMeetingApply(OaMeetingApply oma){
		OaMeetingApply newOma = new OaMeetingApply();
		newOma.setId(oma.getId());
		newOma.setSignBeforeTime(oma.getSignBeforeTime());
		newOma.setMeetingName(oma.getMeetingName());
		newOma.setMeetName(oma.getMeetName());
		newOma.setMeetingApplicant(oma.getMeetingApplicant());
		newOma.setMeetingAttendance(oma.getMeetingAttendance());
		newOma.setMeetingStarttime(oma.getMeetingStarttime());
		newOma.setMeetingEndtime(oma.getMeetingEnd());
		newOma.setMeetingPeriodicity(oma.getMeetingPeriodicity());
		newOma.setMeetingApplyweek(oma.getMeetingApplyweek());
		return newOma;
	}
	@Override
	public void del(String ids) {
		oaMeetingApplyDao.del(ids, ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
	}
	
	@Override
	public OaMeetingApply get(String id){
		OaMeetingApply oma = oaMeetingApplyDao.get(id);
		Clob clob = oma.getMeetingDescribe();
		String string = this.ClobToString(clob);
		//处理上传文件路径
		string = string.replaceAll("/upload/", HisParameters.getfileURI(ServletActionContext.getRequest())+"/upload/");
		oma.setMeetingDescribeString(string);
		return oma;
	}
	
	private String ClobToString(Clob clob) {
		if(clob == null){
			return "";
		}
		String clobStr = "";
		Reader is = null;
		if(clob != null){
			try {
				is = clob.getCharacterStream();
				// 得到流
				BufferedReader br = new BufferedReader(is);
				String s = null;
				s = br.readLine();
				StringBuffer sb = new StringBuffer();
				// 执行循环将字符串全部取出赋值给StringBuffer，由StringBuffer转成String
				while (s != null) {
					sb.append(s);
					s = br.readLine();
				}
				clobStr = sb.toString();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return clobStr;
	}

	private OaMeetingApply changeClob(OaMeetingApply oaMeetingApply) {
		Clob clob = oaMeetingApply.getMeetingDescribe();
		oaMeetingApply.setMeetingDescribeString(this.ClobToString(clob));
		if(StringUtils.isNotBlank(oaMeetingApply.getMeetingDescribeString())){
			byte[] bs = SerializeUtils.serialize(oaMeetingApply.getMeetingDescribeString());
			String string =  (String) SerializeUtils.deserialize(bs);
			Clob clob1 = Hibernate.createClob(string);
			oaMeetingApply.setMeetingDescribe(clob1);
		}
		return oaMeetingApply;
	}
	@Override
	public void saveOrUpdate(OaMeetingApply arg0) {
		
	}

	@Override
	public String checkHaveUsed(OaMeetingApply oaMeetingApply) {
		return oaMeetingApplyDao.checkHaveUsed(oaMeetingApply);
	}
	
	/**
	 * 
	 * <p>新增或者更新签到表中的记录</p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年9月4日 上午11:48:45 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年9月4日 上午11:48:45 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param flag 标记是新增(add)，还是更新(update)
	 * @param oaMeetingApply 会议申请实体
	 * @throws:
	 *
	 */
	public void updateMeetingSigned(OaMeetingApply oaMeetingApply,String flag){
		MeetingSigned signed = new MeetingSigned();
		if("save".equals(flag)){
			String signedUuid = UUID.randomUUID().toString().replace("-", "");
			signed.setId(signedUuid);
		}
		
		signed.setMeeting_id(oaMeetingApply.getId());
		signed.setMeet_name(oaMeetingApply.getMeetName());
		signed.setMeeting_name(oaMeetingApply.getMeetingName());
		signed.setMeeting_tartTime(oaMeetingApply.getMeetingStarttime());
		signed.setMeeting_endTime(oaMeetingApply.getMeetingEndtime());
		if("save".equals(flag)){
			meetingSignedDao.insertMeetingSigned(signed);
		}
		
		if("update".equals(flag)){
			meetingSignedDao.updateMeetingSigned(signed);
		}
	}

	@Override
	public List<SysEmployee> getEmployeeExtendPage(SysEmployee se, String dtype) {
		return oaMeetingApplyDao.getEmployeeExtendPage(se,dtype);
	}

	@Override
	public int getEmployeeExtendTotal(SysEmployee se, String dtype) {
		return oaMeetingApplyDao.getEmployeeExtendTotal(se,dtype);
	}
}
