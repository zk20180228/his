package cn.honry.oa.meeting.meetingInfo.dao;

import java.util.List;

import cn.honry.base.bean.model.OaMeetingInfo;
import cn.honry.base.dao.EntityDao;

public interface OaMeetingInfoDao extends EntityDao<OaMeetingInfo> {
	public int getTotal(OaMeetingInfo oaMeetingInfo);
	public List<OaMeetingInfo> query(OaMeetingInfo oaMeetingInfo);
	public List<String> findMeetByCode(String code);
}
