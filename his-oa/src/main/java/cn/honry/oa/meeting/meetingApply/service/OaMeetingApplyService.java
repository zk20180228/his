package cn.honry.oa.meeting.meetingApply.service;

import java.util.List;

import cn.honry.base.bean.model.OaMeetingApply;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.service.BaseService;

public interface OaMeetingApplyService extends BaseService<OaMeetingApply> {
	public int getTotal(OaMeetingApply oaMeetingApply);
	public List<OaMeetingApply> query(OaMeetingApply oaMeetingApply);
	public List<OaMeetingApply> queryMyMeeting(OaMeetingApply oaMeetingApply);
	public void del(String ids);
	public void saveOrUpdate(OaMeetingApply oaMeetingApply, String type,String flag);
	public String checkHaveUsed(OaMeetingApply oaMeetingApply);
	public List<SysEmployee> getEmployeeExtendPage(SysEmployee se, String dtype);
	public int getEmployeeExtendTotal(SysEmployee se, String dtype);
}
