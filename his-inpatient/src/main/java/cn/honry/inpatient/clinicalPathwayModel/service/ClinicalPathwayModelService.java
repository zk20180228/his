package cn.honry.inpatient.clinicalPathwayModel.service;

import java.util.List;

import cn.honry.base.bean.model.CpwayPlan;
import cn.honry.base.bean.model.ModelDict;
import cn.honry.base.bean.model.ModelVsItem;
import cn.honry.utils.TreeJson;

public interface ClinicalPathwayModelService {
	List<TreeJson> queryTree();
	void saveOrUpdate(ModelDict modelDict);
	void saveOrUpdatePathwayDetail(ModelVsItem modelVsItem);
	ModelDict findPathwayModelById(String id);
	List<ModelDict> searchClinicalModelByNature(String modelNature);
	List<ModelDict> searchAllClinicalModel();
	List<CpwayPlan> searchClinicalModelByStage(String planId,String modelNature,String stageId);
	ModelVsItem findPathwayModelDetailById(String id);
	List<ModelVsItem> queryClinicalPathModelDetail(String modelId,String page, String rows);
	Integer queryClinicalPathModelDetailNum(String modelId);
	void delPathwayDetail(String id);
}
