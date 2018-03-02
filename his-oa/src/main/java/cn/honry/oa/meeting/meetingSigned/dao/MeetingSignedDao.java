package cn.honry.oa.meeting.meetingSigned.dao;

import java.util.List;

import cn.honry.oa.meeting.meetingSigned.vo.MeetingSigned;
import cn.honry.oa.meeting.meetingSigned.vo.MeetingSignedVo;
import cn.honry.oa.meeting.meetingSigned.vo.SignedPersonInfoVo;

/**
 * 
 * <p>会议签到统计dao</p>
 * @Author: zhangkui
 * @CreateDate: 2017年8月29日 下午6:38:21 
 * @Modifier: zhangkui
 * @ModifyDate: 2017年8月29日 下午6:38:21 
 * @ModifyRmk:  
 * @version: V1.0
 * @throws:
 *
 */
public interface MeetingSignedDao {
	
	
	/**
	 * 
	 * <p>会议签到统计列表 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年8月30日 上午11:37:13 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年8月30日 上午11:37:13 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param meetingName 会议名
	 * @param meetingRoomName 会议室名
	 * @param meetingStatusFlag 会议状态
	 * @return
	 * @throws:
	 *
	 */
	public List<MeetingSignedVo> meetingSignedList(String meetingName,String meetingRoomName, String meetingStatusFlag,String page,String rows);
	
	/**
	 * 
	 * <p>会议签到统计列表记录总数 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年8月30日 上午11:38:32 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年8月30日 上午11:38:32 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param meetingName 会议名
	 * @param meetingRoomName 会议室名
	 * @param meetingStatusFlag 会议状态
	 * @return
	 * @throws:
	 *
	 */
	public Long meetingSignedCount(String meetingName, String meetingRoomName,String meetingStatusFlag);
	
	/**
	 * 
	 * <p>根据会议申请的id批量删除会议 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年8月30日 上午11:39:37 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年8月30日 上午11:39:37 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param id 会议申请id
	 * @throws:
	 */
	public void delMeetingSignedById(String id);
	
	/**
	 * 
	 * <p>根据会议签到id，查询准时签到记录列表 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年8月30日 上午11:42:13 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年8月30日 上午11:42:13 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param id 会议签到id
	 * @param searchField 搜索的用的字段：根据姓名，或员工号，或者科室名字，和签到状态搜索签到列表字段
	 * @return
	 * @throws:
	 *
	 */
	public List<SignedPersonInfoVo> onTimeList(String id,String searchField,String page,String rows);
	
	/**
	 * 
	 * <p>根据会议签到id，查询准时签到记录列表总记录数 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年8月30日 上午11:42:13 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年8月30日 上午11:42:13 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param id 会议签到id
	 * @param searchField 搜索的用的字段：根据姓名，或员工号，或者科室名字，和签到状态搜索签到列表字段
	 * @return
	 * @throws:
	 *
	 */
	public Long onTimeNum(String id, String searchField);
	
	/**
	 * 
	 * <p>根据会议签到id，查询迟到记录列表 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年8月30日 上午11:42:13 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年8月30日 上午11:42:13 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param id 会议签到id
	 * @param searchField 搜索的用的字段：根据姓名，或员工号，或者科室名字，和签到状态搜索签到列表字段
	 * @return
	 * @throws:
	 *
	 */
	public List<SignedPersonInfoVo> isLateList(String id,String searchField,String page,String rows);
	
	/**
	 * 
	 * <p>根据会议签到id，查询迟到记录列表总记录数 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年8月30日 上午11:42:13 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年8月30日 上午11:42:13 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param id 会议签到id
	 * @param searchField 搜索的用的字段：根据姓名，或员工号，或者科室名字，和签到状态搜索签到列表字段
	 * @return
	 * @throws:
	 *
	 */
	public Long isLateNum(String id, String searchField);
	
	/**
	 * 
	 * <p>根据会议签到id，查询未到记录列表 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年8月30日 上午11:42:13 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年8月30日 上午11:42:13 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param id 会议签到id
	 * @param searchField 搜索的用的字段：根据姓名，或员工号，或者科室名字，和签到状态搜索签到列表字段
	 * @return
	 * @throws:
	 *
	 */
	public List<SignedPersonInfoVo> noComeList(String id,String searchField,String page,String rows);
	
	/**
	 * 
	 * <p>根据会议签到id，查询未到记录列表总记录数 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年8月30日 上午11:42:13 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年8月30日 上午11:42:13 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param id 会议签到id
	 * @param searchField 搜索的用的字段：根据姓名，或员工号，或者科室名字，和签到状态搜索签到列表字段
	 * @return
	 * @throws:
	 *
	 */
	public Long noComeNum(String id, String searchField);
	
	/**
	 * 
	 * <p>根据会议签到id，查询临时参加记录列表 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年8月30日 上午11:42:13 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年8月30日 上午11:42:13 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param id 会议签到id
	 * @param searchField 搜索的用的字段：根据姓名，或员工号，或者科室名字，和签到状态搜索签到列表字段
	 * @return
	 * @throws:
	 *
	 */
	public List<SignedPersonInfoVo> tempComeList(String id,String searchField,String page,String rows);
	
	/**
	 * 
	 * <p>根据会议签到id，查询临时参加记录列表总记录数 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年8月30日 上午11:42:13 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年8月30日 上午11:42:13 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param id 会议签到id
	 * @param searchField 搜索的用的字段：根据姓名，或员工号，或者科室名字，和签到状态搜索签到列表字段
	 * @return
	 * @throws:
	 *
	 */
	public Long tempComeNum(String id, String searchField);

	/**
	 * 
	 * <p>新建一条会议的时候，向会议签到表中插入一条数据</p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年9月4日 上午10:17:19 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年9月4日 上午10:17:19 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param meetingSigned
	 * @throws:
	 *
	 */
	public void insertMeetingSigned(MeetingSigned meetingSigned);
	
	
	/**
	 * 
	 * <p> </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年9月4日 上午10:29:13 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年9月4日 上午10:29:13 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param meetingSigned
	 * @throws:
	 *
	 */
	public void updateMeetingSigned(MeetingSigned meetingSigned);	

}
