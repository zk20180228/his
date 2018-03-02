package cn.honry.inpatient.clinicalPathwayModel.dao;

import java.util.List;

import cn.honry.base.bean.model.CpwayPlan;
import cn.honry.base.bean.model.ModelDict;
import cn.honry.base.bean.model.ModelVsItem;
import cn.honry.base.dao.EntityDao;

public interface ClinicalPathwayModelDao extends EntityDao<ModelDict> {
	List<ModelDict> queryTree();
	ModelDict findPathwayModelById(String id);
	List<ModelDict> searchClinicalModelByNature(String modelNature);
	List<CpwayPlan> searchClinicalModelByStage(String planId,String modelNature,String stageId);
	ModelVsItem findPathwayModelDetailById(String id);
	List<ModelVsItem> queryClinicalPathModelDetail(String modelId,String page, String rows);
	Integer queryClinicalPathModelDetailNum(String modelId);
	void delPathwayDetail(String id);
}
