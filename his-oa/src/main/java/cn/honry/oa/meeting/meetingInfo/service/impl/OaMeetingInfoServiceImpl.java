package cn.honry.oa.meeting.meetingInfo.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.OaMeetingInfo;
import cn.honry.oa.meeting.meetingInfo.dao.OaMeetingInfoDao;
import cn.honry.oa.meeting.meetingInfo.service.OaMeetingInfoService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.ShiroSessionUtils;

@Service("oaMeetingInfoService")
@Transactional
@SuppressWarnings({ "all" })
public class OaMeetingInfoServiceImpl implements OaMeetingInfoService{

	@Autowired
	@Qualifier(value = "oaMeetingInfoDao")
	private OaMeetingInfoDao oaMeetingInfoDao;
	
	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(OaMeetingInfo oaMeetingInfo) {
		String longinUserAccount = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		if(StringUtils.isBlank(oaMeetingInfo.getId())){
			oaMeetingInfo.setId(null);
			oaMeetingInfo.setCreateUser(longinUserAccount);
			oaMeetingInfo.setCreateTime(DateUtils.getCurrentTime());
			oaMeetingInfoDao.save(oaMeetingInfo);
		}else{
			OaMeetingInfo oaMeetingInfoNew = oaMeetingInfoDao.get(oaMeetingInfo.getId());
			oaMeetingInfoNew.setAreaCode(oaMeetingInfo.getAreaCode());
			oaMeetingInfoNew.setMeetCode(oaMeetingInfo.getMeetCode());
			oaMeetingInfoNew.setMeetName(oaMeetingInfo.getMeetName());
			oaMeetingInfoNew.setMeetPlace(oaMeetingInfo.getMeetPlace());
			oaMeetingInfoNew.setMeetNumber(oaMeetingInfo.getMeetNumber());
			oaMeetingInfoNew.setMeetState(oaMeetingInfo.getMeetState());
			oaMeetingInfoNew.setMeetType(oaMeetingInfo.getMeetType());
			oaMeetingInfoNew.setMeetAdmin(oaMeetingInfo.getMeetAdmin());
			oaMeetingInfoNew.setMeetAdminCode(oaMeetingInfo.getMeetAdminCode());
			oaMeetingInfoNew.setMeetEquipment(oaMeetingInfo.getMeetEquipment());
			oaMeetingInfoNew.setMeetIsapply(oaMeetingInfo.getMeetIsapply());
			oaMeetingInfoNew.setMeetPhone(oaMeetingInfo.getMeetPhone());
			oaMeetingInfoNew.setMeetDescribe(oaMeetingInfo.getMeetDescribe());
			oaMeetingInfoNew.setUpdateTime(new Date());
			oaMeetingInfoNew.setUpdateUser(longinUserAccount);
			oaMeetingInfoDao.save(oaMeetingInfoNew);
		}
	}

	@Override
	public int getTotal(OaMeetingInfo oaMeetingInfo) {
		return oaMeetingInfoDao.getTotal(oaMeetingInfo);
	}

	@Override
	public List<OaMeetingInfo> query(OaMeetingInfo oaMeetingInfo) {
		return oaMeetingInfoDao.query(oaMeetingInfo);
	}

	@Override
	public void del(String ids) {
		oaMeetingInfoDao.del(ids,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());	
	}

	@Override
	public OaMeetingInfo get(String id){
		return oaMeetingInfoDao.get(id);
	}

	@Override
	public String checkMeetCode(String code, String id) {
		List<String> list = oaMeetingInfoDao.findMeetByCode(code);
		if(list==null||list.size()==0){
			return "ok";
		}else if(list.size()>0){
			if(list.size()==1){
				if(list.get(0).equals(id)){
					return "ok";
				}else{
					return "会议室编号重复";
				}
			}
			return "会议室编号重复";
		}
		return "会议室编号异常";
	}
}
