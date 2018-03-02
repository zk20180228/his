package cn.honry.inpatient.clinicalPathwayModel.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.CpwayPlan;
import cn.honry.base.bean.model.ModelDict;
import cn.honry.base.bean.model.ModelVsItem;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.inpatient.clinicalPathwayModel.dao.ClinicalPathwayModelDao;
import cn.honry.inpatient.clinicalPathwayModel.service.ClinicalPathwayModelService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.TreeJson;

@Service("clinicalPathwayModelService")
@Transactional
@SuppressWarnings({"all"})
public class ClinicalPathwayModelServiceImpl implements
		ClinicalPathwayModelService {

	@Autowired
	@Qualifier("clinicalPathwayModelDao")
	private ClinicalPathwayModelDao clinicalPathwayModelDao;
	
	@Autowired
	@Qualifier("innerCodeService")
	private CodeInInterService innerCodeService;
	public void setInnerCodeService(CodeInInterService innerCodeService) {
		this.innerCodeService = innerCodeService;
	}
	/**
	 * 模板树
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月16日 下午3:18:05 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月16日 下午3:18:05 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public List<TreeJson> queryTree() {
		List<ModelDict> modelDictList = clinicalPathwayModelDao.queryTree();
		List<TreeJson> treeJsonList = new ArrayList<TreeJson>();
		String sType="";
		
		TreeJson topTreeJson = new TreeJson();
		topTreeJson.setId("1");
		topTreeJson.setText("临床路径信息");
		Map<String,String> tAttMap = new HashMap<String, String>();
		topTreeJson.setAttributes(tAttMap);
		treeJsonList.add(topTreeJson);
		
		List<BusinessDictionary> dictionaryList = innerCodeService.getDictionary("systemType");
		int dictionarylen = dictionaryList.size();
		
		if(modelDictList!=null&&modelDictList.size()>0){
			String curType="";
			for(ModelDict md : modelDictList){
				TreeJson treeJson = new TreeJson();
				if(StringUtils.isNotBlank(md.getModelClass())&&!curType.equals(md.getModelClass())){
					TreeJson typeTreeJson = new TreeJson();
					curType = md.getModelClass();
					typeTreeJson.setId("type_"+curType);
					for(BusinessDictionary bd : dictionaryList){
						if(bd.getEncode().equals(curType)){
							typeTreeJson.setText(bd.getName());
							typeTreeJson.setState("closed");
						}
					}
					if("".equals(sType)){
						sType=curType;
					}else{
						sType+=","+curType;
					}
					Map<String,String> attributes = new HashMap<String, String>();
					attributes.put("pid","1");
					attributes.put("hasson","1");
					attributes.put("modelClass","code_"+curType);
					typeTreeJson.setAttributes(attributes);
					treeJsonList.add(typeTreeJson);	
				}
				
				treeJson.setId(md.getId());
				treeJson.setText(md.getModelName());
				
				Map<String,String> attributes = new HashMap<String, String>();
				
				attributes.put("pid", "type_"+curType);
				
				attributes.put("id", md.getId());
				attributes.put("modelCode", md.getModelCode());
				attributes.put("modelVersion", md.getModelVersion());
				attributes.put("modelName", md.getModelName());
				attributes.put("inputCode", md.getInputCode());
				attributes.put("inputCodeWb", md.getInputCodeWb());
				attributes.put("customCode", md.getCustomCode());
				attributes.put("modelClass", md.getModelClass());
				attributes.put("deptCode", md.getDeptCode());
				attributes.put("hospitalId", md.getHospitalId());
				attributes.put("areaCode", md.getAreaCode());
				
				treeJson.setAttributes(attributes);
				treeJsonList.add(treeJson);
			}
		}
		
		for(BusinessDictionary bd : dictionaryList){
			if((","+sType+",").indexOf(","+bd.getEncode()+",")==-1){
				TreeJson treeJson = new TreeJson();
				treeJson.setId("type_"+bd.getEncode());
				treeJson.setText(bd.getName());
				treeJson.setIconCls("icon-application_side_list");
				Map<String,String> attributes = new HashMap<String, String>();
				attributes.put("pid","1");
				attributes.put("hasson","0");
				attributes.put("modelClass","code_"+bd.getEncode());
				treeJson.setAttributes(attributes);
				treeJsonList.add(treeJson);
			}
		}
		
		return TreeJson.formatTree(treeJsonList);
	}

	/**
	 * 增加修改临床路径模板
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月16日 下午3:17:37 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月16日 下午3:17:37 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param modelDict:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public void saveOrUpdate(ModelDict modelDict) {
		String longinUserAccountCode = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String pyWb = clinicalPathwayModelDao.getSpellCode(modelDict.getModelName());
		if(pyWb!=null&&pyWb.contains("$")){
			String pw[] = pyWb.split("\\$");
			modelDict.setInputCode(pw[0]);
			modelDict.setInputCodeWb(pw[1]);
		}
		if(StringUtils.isNotBlank(modelDict.getId())){//有id,更新
			ModelDict modelDictNew = findPathwayModelById(modelDict.getId());
			
			modelDictNew.setModelCode(modelDict.getModelClass());
			modelDictNew.setModelVersion(modelDict.getModelVersion());
			modelDictNew.setModelName(modelDict.getModelName());
			modelDictNew.setModelNature(modelDict.getModelNature());
			modelDictNew.setInputCode(modelDict.getInputCode());
			modelDictNew.setInputCodeWb(modelDict.getInputCodeWb());
			modelDictNew.setCustomCode(modelDict.getCustomCode());
			modelDictNew.setModelClass(modelDict.getModelClass());
			modelDictNew.setDeptCode(modelDict.getDeptCode());
			modelDictNew.setHospitalId(modelDict.getHospitalId());
			modelDictNew.setAreaCode(modelDict.getAreaCode());
			modelDictNew.setUpdateUser(longinUserAccountCode);
			modelDictNew.setUpdateTime(DateUtils.getCurrentTime());
			
			clinicalPathwayModelDao.save(modelDictNew);
		}else{//没有id，添加
			modelDict.setId(null);//穿过来的ID是“”，将他设置为空
			modelDict.setDeptLevel(1);
			modelDict.setCreateUser(longinUserAccountCode);
			modelDict.setCreateTime(DateUtils.getCurrentTime());
			clinicalPathwayModelDao.save(modelDict);//自动生成ID
		}
	}
	@Override
	public ModelDict findPathwayModelById(String id) {
		return clinicalPathwayModelDao.findPathwayModelById(id);
	}
	@Override
	public void saveOrUpdatePathwayDetail(ModelVsItem modelVsItem) {
		String longinUserAccountCode = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		if(StringUtils.isNotBlank(modelVsItem.getId())){//ID不为空，是修改
			ModelVsItem modelVsItemNew = findPathwayModelDetailById(modelVsItem.getId());
			modelVsItemNew.setDrugRoom(modelVsItem.getDrugRoom());
			modelVsItemNew.setDrugRoomName(modelVsItem.getDrugRoomName());
			modelVsItemNew.setItemCode(modelVsItem.getItemCode());
			modelVsItemNew.setItemName(modelVsItem.getItemName());
			modelVsItemNew.setFlag(modelVsItem.getFlag());
			modelVsItemNew.setChooseFlag(modelVsItem.getChooseFlag());
			modelVsItemNew.setUnit(modelVsItem.getUnit());
			modelVsItemNew.setNum(modelVsItem.getNum());
			modelVsItemNew.setFrequencyCode(modelVsItem.getFrequencyCode());
			modelVsItemNew.setDirectionCode(modelVsItem.getDirectionCode());
			modelVsItemNew.setModelId(modelVsItem.getModelId());
			modelVsItemNew.setModelClass(modelVsItem.getModelClass());
			modelVsItemNew.setUpdateUser(longinUserAccountCode);
			modelVsItemNew.setUpdateTime(DateUtils.getCurrentTime());
			clinicalPathwayModelDao.save(modelVsItemNew);
		}else{//ID为空，新增
			modelVsItem.setId(null);
			modelVsItem.setCreateUser(longinUserAccountCode);
			modelVsItem.setCreateTime(DateUtils.getCurrentTime());
			clinicalPathwayModelDao.save(modelVsItem);
		}
	}
	@Override
	public List<ModelVsItem> queryClinicalPathModelDetail(String modelId,String page, String rows) {
		return clinicalPathwayModelDao.queryClinicalPathModelDetail(modelId,page,rows);
	}
	@Override
	public Integer queryClinicalPathModelDetailNum(String modelId) {
		return clinicalPathwayModelDao.queryClinicalPathModelDetailNum(modelId);
	}
	@Override
	public ModelVsItem findPathwayModelDetailById(String id) {
		return clinicalPathwayModelDao.findPathwayModelDetailById(id);
	}
	@Override
	public void delPathwayDetail(String id) {
		clinicalPathwayModelDao.delPathwayDetail(id);
	}
	@Override
	public List<ModelDict> searchClinicalModelByNature(String modelNature) {
		return clinicalPathwayModelDao.searchClinicalModelByNature(modelNature);
	}
	@Override
	public List<CpwayPlan> searchClinicalModelByStage(String planId,
			String modelNature, String stageId) {
		return clinicalPathwayModelDao.searchClinicalModelByStage(planId,modelNature,stageId);
	}
	@Override
	public List<ModelDict> searchAllClinicalModel() {
		List<ModelDict> modelDictList = clinicalPathwayModelDao.queryTree();
		return modelDictList;
	}
}
