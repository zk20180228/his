package cn.honry.inpatient.variationInfo.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessBedward;
import cn.honry.base.bean.model.BusinessHospitalbed;
import cn.honry.base.bean.model.CpVariation;
import cn.honry.base.bean.model.InpatientPrepayin;
import cn.honry.base.bean.model.ModelDict;
import cn.honry.base.bean.model.ModelVsItem;
import cn.honry.base.bean.model.PathApply;
import cn.honry.base.bean.model.PathVsIcd;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.inpatient.clinicalPathVsICD.dao.ClinicalPathVsICDDao;
import cn.honry.inpatient.clinicalPathVsICD.service.ClinicalPathVsICDService;
import cn.honry.inpatient.variationInfo.dao.VariationInfoDao;
import cn.honry.inpatient.variationInfo.service.VariationInfoService;
import cn.honry.inpatient.variationInfo.vo.ComboxVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.TreeJson;

@Service("variationInfoService")
@Transactional
@SuppressWarnings({"all"})
public class VariationInfoServiceImpl implements VariationInfoService {

	@Autowired
	@Qualifier("variationInfoDao")
	private VariationInfoDao variationInfoDao;

	@Override
	public List<TreeJson> listTree(String id) {
		List<SysDepartment> sysDepartmentList = new ArrayList<SysDepartment>();
		List<TreeJson> treeJsonList = new ArrayList<TreeJson>();
		List<PathApply> pathApplyList = variationInfoDao.findPathApply();
		Map<String, List<PathApply>> map = new HashMap();
		List<PathApply> wardList = null;
		if(pathApplyList.size()>0){
			for (PathApply PathApply : pathApplyList) {
				String nursestation = PathApply.getApplyCode();
				wardList=map.get(nursestation);
				if(wardList!=null){
					wardList.add(PathApply);
				}else{
					wardList=new ArrayList<PathApply>();
					wardList.add(PathApply);
				}
				map.put(nursestation,wardList);
			}
		}
		if(StringUtils.isBlank(id)){//根节点
			//加入转入科室的根节点
			TreeJson pTreeJson = new TreeJson();
			pTreeJson.setId("root");
			pTreeJson.setText("标准");
			pTreeJson.setIconCls("icon-branch");
			Map<String,String> attributes = new HashMap<String, String>();
			attributes.put("isdept", "1");
			pTreeJson.setAttributes(attributes);
			sysDepartmentList = variationInfoDao.listTree();
			if(sysDepartmentList!=null&&sysDepartmentList.size()>0){
				List<TreeJson> treeJsonListD = new ArrayList<TreeJson>();
				for(SysDepartment sysDepartment : sysDepartmentList){
					List<PathApply> PathList = map.get(sysDepartment.getDeptCode());
					TreeJson treeJson = new TreeJson();
					treeJson.setId(sysDepartment.getDeptCode());
					treeJson.setText(sysDepartment.getDeptName());
					treeJson.setIconCls("icon-bullet_home");
					if(PathList!=null){
						treeJson.setState("closed");
					}
					Map<String,String> attributes1 = new HashMap<String, String>();
					attributes1.put("pid",StringUtils.isBlank(sysDepartment.getDeptParent())?"1":sysDepartment.getDeptParent());
					attributes1.put("deptCode",sysDepartment.getDeptCode());
					attributes1.put("isdept","Y");
					treeJson.setAttributes(attributes1);
					//病房
					if(PathList!=null){
						List<TreeJson> treeJsonListp = new ArrayList<TreeJson>();
						for(PathApply model:PathList){
							TreeJson treeJson2 = new TreeJson();
							treeJson2.setId(model.getId());
							treeJson2.setText(model.getPatientName());
							treeJson2.setText("【"+model.getInpatientNo()+"】"+model.getPatientName());
							treeJson2.setIconCls("icon-user_brown");
							Map<String,String> mapAttributes = new HashMap<String, String>();
							mapAttributes.put("pid",sysDepartment.getDeptCode());
							mapAttributes.put("isdept","N");
							mapAttributes.put("deptCode",model.getApplyCode());
							mapAttributes.put("cpId",model.getCpId());
							mapAttributes.put("versionNo",model.getVersionNo());
							mapAttributes.put("inpatientNo",model.getInpatientNo());
							mapAttributes.put("medicalrecordId",model.getMedicalrecordId());
							treeJson2.setAttributes(mapAttributes);
							treeJsonListp.add(treeJson2);
						}
						treeJson.setChildren(treeJsonListp);
					}
					treeJsonListD.add(treeJson);
				}
				pTreeJson.setChildren(treeJsonListD);
			}
			treeJsonList.add(pTreeJson);
		}
		return treeJsonList;
	}

	@Override
	public Integer queryPathVsIcdNum(String inpatientNo) {
		return variationInfoDao.queryPathVsIcdNum(inpatientNo);
	}

	@Override
	public List<CpVariation> queryPathVsIcdList(String inpatientNo, String page, String rows) {
		return variationInfoDao.queryPathVsIcdList(inpatientNo,page,rows);
	}

	@Override
	public CpVariation findVariationInfoById(String id) {
		return variationInfoDao.get(id);
	}

	@Override
	public List<ComboxVo> queryDictionary(String q, String type) {
		return variationInfoDao.queryDictionary(q,type);
	}

	@Override
	public List<ComboxVo> queryStageId(String q, String cpId, String versionNo) {
		return variationInfoDao.queryStageId(q,cpId,versionNo);
	}

	@Override
	public void saveOrUpdate(CpVariation cpVariation) {
		String longinUserAccountCode = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		if(StringUtils.isNotBlank(cpVariation.getId())){//有id,更新
			cpVariation.setUpdateUser(longinUserAccountCode);
			cpVariation.setUpdateTime(DateUtils.getCurrentTime());
			cpVariation.setHospitalId(HisParameters.CURRENTHOSPITALID.toString());
			variationInfoDao.save(cpVariation);
		}else{//没有id，添加
			cpVariation.setId(null);//穿过来的ID是“”，将他设置为空
			cpVariation.setCreateUser(longinUserAccountCode);
			cpVariation.setCreateTime(DateUtils.getCurrentTime());
			cpVariation.setHospitalId(HisParameters.CURRENTHOSPITALID.toString());
			variationInfoDao.save(cpVariation);//自动生成ID
		}
		
	}

	@Override
	public PathApply getPathApplyByInNo(String inpatientNo) {
		return variationInfoDao.getPathApplyByInNo(inpatientNo);
	}

	@Override
	public void batchUpDe(String id) {
		variationInfoDao.batchUpDe(id);
	}

	@Override
	public List<SysDepartment> getDeptName(String queryName) {
		return variationInfoDao.getDeptName(queryName);
	}

}
