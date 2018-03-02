package cn.honry.inner.inpatient.nurseApply.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientShiftApply;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.inner.inpatient.nurseApply.dao.NurseApplyInInterDao;
import cn.honry.inner.inpatient.nurseApply.service.NurseApplyInInterService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.SessionUtils;
import cn.honry.utils.TreeJson;


/**
 * @author Administrators
 *
 */
@Service("nurseApplyInInterService")
@Transactional
@SuppressWarnings({ "all" })
public class NurseApplyInInterServiceImpl implements NurseApplyInInterService {
	
	@Autowired
	@Qualifier(value="nurseApplyInInterDao")
	private NurseApplyInInterDao nurseApplyInInterDao;
	
	public String treeInpatient(String id,String deptId){
		List<TreeJson> treeJsonList = new ArrayList<TreeJson>();
		if(StringUtils.isBlank(id)){//根节点
			Map<Integer, String> weekMap = new HashMap<Integer, String>();
			weekMap.put(0, "本区患者");
			weekMap.put(1, "转入患者");
			weekMap.put(2, "转出患者");
			weekMap.put(3, "出院登记患者");
			TreeJson gTreeJson = null;
			Map<String,String> gAttMap = null;
			for (int i = 0; i < 4; i++) {
				gTreeJson = new TreeJson();
				gAttMap = new HashMap<String, String>();
				gTreeJson.setId((i+1)+"");
				gTreeJson.setText(weekMap.get(i));
				gTreeJson.setState("closed");
				if(i==0){
					gTreeJson.setIconCls("icon-bullet_home");
				}if(i==1){
					gTreeJson.setIconCls("icon-bullet_right");
				}if(i==2){
					gTreeJson.setIconCls("icon-bullet_left");
				}if(i==3){
					gTreeJson.setIconCls("icon-bullet_edit");
				}
				gAttMap.put("pid", "root");
				gTreeJson.setAttributes(gAttMap);
				treeJsonList.add(gTreeJson);
			}
		}else{
			TreeJson cTreeJson = null;
			Map<String,String> cAttMap = null;
			SysDepartment dept=nurseApplyInInterDao.queryState(deptId);
			String type=dept.getDeptType();
			List<InpatientInfo> inpatientInfoList = nurseApplyInInterDao.infoo(id, deptId,type);
			for(InpatientInfo inpatientInfo:inpatientInfoList){
				cTreeJson = new TreeJson();
				cAttMap = new HashMap<String, String>();
				cTreeJson.setId(inpatientInfo.getInpatientNo());
				cTreeJson.setText(inpatientInfo.getPatientName());
				cTreeJson.setState("open");
				if("1".equals(inpatientInfo.getReportSex())||"3".equals(inpatientInfo.getReportSex())){ //男或未知
					cTreeJson.setIconCls("icon-user_b");
				}else{
					cTreeJson.setIconCls("icon-user_female");
				}
				
				cAttMap.put("pid", id);
				cAttMap.put("inpatientNo", inpatientInfo.getInpatientNo());
				cAttMap.put("extFlag2", inpatientInfo.getExtFlag2());
				cAttMap.put("medicalrecordId", inpatientInfo.getMedicalrecordId());
				cAttMap.put("extFlag1", inpatientInfo.getExtFlag1());
				cAttMap.put("haveBabyFlag", inpatientInfo.getHaveBabyFlag().toString());
				cAttMap.put("dept", inpatientInfo.getDeptCode());
				cAttMap.put("name", inpatientInfo.getPatientName());
				cAttMap.put("bedId", inpatientInfo.getBedId());
				cAttMap.put("inDate", inpatientInfo.getInDate().toString());
				User user = (User) SessionUtils.getCurrentUserFromShiroSession();
				cAttMap.put("operCode", user.getId());
				cTreeJson.setAttributes(cAttMap);
				treeJsonList.add(cTreeJson);
			}
		}
		 return JSONUtils.toJson(treeJsonList);
	}

	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public InpatientShiftApply get(String id) {
		return nurseApplyInInterDao.get(id);
	}

	@Override
	public void saveOrUpdate(InpatientShiftApply entity) {
		
	}
}
