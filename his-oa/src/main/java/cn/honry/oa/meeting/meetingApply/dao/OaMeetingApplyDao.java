package cn.honry.oa.meeting.meetingApply.dao;

import java.util.List;

import cn.honry.base.bean.model.OaMeetingApply;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.EntityDao;

public interface OaMeetingApplyDao extends EntityDao<OaMeetingApply> {
	public int getTotal(OaMeetingApply oaMeetingApply);
	public List<OaMeetingApply> query(OaMeetingApply oaMeetingApply);
	public String checkHaveUsed(OaMeetingApply oaMeetingApply);
	public String findSign(String meetingId);
	public List<SysEmployee> getEmployeeExtendPage(SysEmployee se, String dtype);
	public int getEmployeeExtendTotal(SysEmployee se, String dtype);
}
