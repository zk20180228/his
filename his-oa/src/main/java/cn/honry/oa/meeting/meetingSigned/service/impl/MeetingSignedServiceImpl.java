package cn.honry.oa.meeting.meetingSigned.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.oa.meeting.meetingSigned.dao.MeetingSignedDao;
import cn.honry.oa.meeting.meetingSigned.service.MeetingSignedService;
import cn.honry.oa.meeting.meetingSigned.vo.MeetingSignedVo;
import cn.honry.oa.meeting.meetingSigned.vo.SignedPersonInfoVo;
@Transactional
@Service("meetingSignedService")
public class MeetingSignedServiceImpl implements MeetingSignedService {

	@Resource
	private MeetingSignedDao meetingSignedDao;
	public void setMeetingSignedDao(MeetingSignedDao meetingSignedDao) {
		this.meetingSignedDao = meetingSignedDao;
	}

	@Override
	public List<MeetingSignedVo> meetingSignedList(String meetingName,String meetingRoomName, String meetingStatusFlag,String page,String rows) {
		
		return meetingSignedDao.meetingSignedList(meetingName, meetingRoomName, meetingStatusFlag,page,rows);
	}

	@Override
	public Long meetingSignedCount(String meetingName,String meetingRoomName, String meetingStatusFlag) {
		
		return meetingSignedDao.meetingSignedCount(meetingName, meetingRoomName, meetingStatusFlag);
	}

	@Override
	public void delMeetingSignedById(String id) {
		
		 meetingSignedDao.delMeetingSignedById(id);
	}

	@Override
	public List<SignedPersonInfoVo> onTimeList(String id, String searchField,String page,String rows) {
		
		return meetingSignedDao.onTimeList(id, searchField,page,rows);
	}

	@Override
	public Long onTimeNum(String id, String searchField) {
		
		return meetingSignedDao.onTimeNum(id, searchField);
	}

	@Override
	public List<SignedPersonInfoVo> isLateList(String id, String searchField,String page,String rows) {
		
		return meetingSignedDao.isLateList(id, searchField,page,rows);
	}

	@Override
	public Long isLateNum(String id, String searchField) {
		
		return meetingSignedDao.isLateNum(id, searchField);
	}

	@Override
	public List<SignedPersonInfoVo> noComeList(String id, String searchField,String page,String rows) {
		
		return meetingSignedDao.noComeList(id, searchField,page,rows);
	}

	@Override
	public Long noComeNum(String id, String searchField) {
		
		return meetingSignedDao.noComeNum(id, searchField);
	}

	@Override
	public List<SignedPersonInfoVo> tempComeList(String id, String searchField,String page,String rows) {
		
		return meetingSignedDao.tempComeList(id, searchField,page,rows);
	}

	@Override
	public Long tempComeNum(String id, String searchField) {
		
		return meetingSignedDao.tempComeNum(id, searchField);
	}

}
