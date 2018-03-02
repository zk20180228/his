package cn.honry.inpatient.clinicalPathway.dao;

import java.util.List;

import cn.honry.base.bean.model.CpExecute;
import cn.honry.base.bean.model.CpExecuteDetail;
import cn.honry.base.bean.model.CpMasterIndex;
import cn.honry.base.bean.model.CpVcontrol;
import cn.honry.base.bean.model.CpWay;
import cn.honry.base.bean.model.CpwayPlan;
import cn.honry.base.bean.model.InoroutStandard;
import cn.honry.base.bean.model.InpAccess;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.PathApply;
import cn.honry.base.dao.EntityDao;

public interface ClinicalPathwayDao extends EntityDao<CpWay>{
	List<CpVcontrol> queryTree();
	List<CpwayPlan> queryTimeTree(String versionId);
	List<CpWay> queryDisease();
	List<CpVcontrol> queryCpWayVersion(String cpId);
	List<InoroutStandard> queryStand();
	List<InoroutStandard> queryVersion(String standCode);
	CpWay findCpWayById(String id);
	CpMasterIndex findCmiByInpatient(String inpatientNo);
	boolean checkIsOnly(CpVcontrol cpVcontrol);
	CpVcontrol findCpVcontrolById(String id);
	CpwayPlan findCpWayPlanById(String id);
	void delOldPlan(String modelNature,String cpId,String stageId);
	List<InpatientInfoNow> findPatientByDeptCode(String treeId);
	List<InpatientInfoNow> findInPatientByDeptCode(String treeId);
	List<CpwayPlan> findStageByInpatientNo(String inpatientNo);
	
	List<CpExecute> executeInfoByInpatientNo(String inpatientNo);
	List<CpExecuteDetail> executeDetail(String inpatientNo,String cpId,String modelCode);
	void outPath(String inpatientNo);
	void executePath(String executeId);
	
	List<InpAccess> queryAssess(String inpatientNo,String stage,String page,String rows);
	InpAccess findInpAccess(String id);
	int queryAssessNum(String inpatientNo,String stage);
}
