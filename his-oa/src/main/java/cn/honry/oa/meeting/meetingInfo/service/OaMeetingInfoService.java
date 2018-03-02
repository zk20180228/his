package cn.honry.oa.meeting.meetingInfo.service;

import java.util.List;

import cn.honry.base.bean.model.OaMeetingInfo;
import cn.honry.base.service.BaseService;

public interface OaMeetingInfoService extends BaseService<OaMeetingInfo> {
	public int getTotal(OaMeetingInfo oaMeetingInfo);
	public List<OaMeetingInfo> query(OaMeetingInfo oaMeetingInfo);
	public void del(String ids);
	String checkMeetCode(String code, String id);
}
