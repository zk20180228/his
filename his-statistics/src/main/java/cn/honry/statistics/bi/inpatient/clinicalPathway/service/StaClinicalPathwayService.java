package cn.honry.statistics.bi.inpatient.clinicalPathway.service;

import java.util.List;

import cn.honry.base.bean.model.CpMasterIndex;
import cn.honry.base.service.BaseService;
import cn.honry.statistics.bi.inpatient.clinicalPathway.vo.ClinicalPathVo;
import cn.honry.statistics.bi.inpatient.clinicalPathway.vo.InOutDetail;
import cn.honry.statistics.bi.inpatient.clinicalPathway.vo.analysisClinicalVo;

public interface StaClinicalPathwayService extends BaseService<CpMasterIndex>{
	void test();
	List<ClinicalPathVo> inOutList(String page,String rows,String sTime,String eTime,String deptCodeTopL,String inOrOutTopL);
	Integer inOutNum(String sTime,String eTime,String deptCodeTopL,String inOrOutTopL);
	List<ClinicalPathVo> notEntryList(String page,String rows,String sTime,String eTime,String deptCodeBL);
	Integer notEntryNum(String sTime,String eTime,String deptCodeBL);
	List<ClinicalPathVo> variationOutList(String page,String rows,String sTime,String eTime,String deptCodeTR,String variationTR);
	Integer variationOutNum(String sTime,String eTime,String deptCodeTR,String variationTR);
	List<InOutDetail> inOutDetailList(String page,String rows,String sTime,String eTime,String deptCodeBR,String inOrOutBR,String sexCode);
	Integer inOutDetailNum(String sTime,String eTime,String deptCodeBR,String inOrOutBR,String sexCode);
	List<analysisClinicalVo> analysisClinicalList(String page,String rows,String sTime,String eTime,String deptCode);
	Integer analysisClinicalNum(String sTime,String eTime,String deptCode);
}
