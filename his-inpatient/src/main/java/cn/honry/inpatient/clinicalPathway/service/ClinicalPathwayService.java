package cn.honry.inpatient.clinicalPathway.service;

import java.util.List;

import cn.honry.base.bean.model.CpExecute;
import cn.honry.base.bean.model.CpExecuteDetail;
import cn.honry.base.bean.model.CpMasterIndex;
import cn.honry.base.bean.model.CpVcontrol;
import cn.honry.base.bean.model.CpWay;
import cn.honry.base.bean.model.CpwayPlan;
import cn.honry.base.bean.model.InoroutStandard;
import cn.honry.base.bean.model.InpAccess;
import cn.honry.utils.TreeJson;

public interface ClinicalPathwayService {
	List<TreeJson> queryTree();
	List<TreeJson> queryPatientTree(String treeId);
	List<TreeJson> queryInPatientTree(String treeId);
	List<TreeJson> treeStage(String cpId);
	void addPathway(CpWay cpWay);
	void addVersion(CpVcontrol cpVcontrol);
	boolean checkIsOnly(CpVcontrol cpVcontrol);
	List<InoroutStandard> queryStand();
	List<InoroutStandard> queryVersion(String standCode);
	List<CpWay> queryCpWay();
	List<CpVcontrol> queryCpWayVersion(String cpId);
	CpWay findCpWayById(String id);
	CpVcontrol findCpVcontrolById(String id);
	void addTimeToClinical(CpwayPlan cpwayPlan);
	void saveModelToPlan(String id, String modelId, String modelNature,String cpId,String stageId);
	void saveCmi(CpMasterIndex cmi);
	void saveCe(CpExecute ce);
	void saveCed(CpExecuteDetail ced);
	void saveCeAndCed(CpExecute ce,CpExecuteDetail ced);
	
	List<CpExecute> executeInfoByInpatientNo(String inpatientNo);
	List<CpExecuteDetail> executeDetail(String inpatientNo,String cpId,String modelCode);
	void outPath(String inpatientNo);
	void executePath(String executeId);
	
	void saveOrUpdateAssess(InpAccess ia);
	void approveAssess(InpAccess ia);
	
	List<InpAccess> queryAssess(String inpatientNo,String stage,String page,String rows);
	int queryAssessNum(String inpatientNo,String stage);
}
