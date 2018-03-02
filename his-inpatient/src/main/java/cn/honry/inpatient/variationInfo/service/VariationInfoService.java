package cn.honry.inpatient.variationInfo.service;

import java.util.List;

import cn.honry.base.bean.model.CpVariation;
import cn.honry.base.bean.model.PathApply;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.inpatient.variationInfo.vo.ComboxVo;
import cn.honry.utils.TreeJson;

public interface VariationInfoService {

	List<TreeJson> listTree(String id);

	Integer queryPathVsIcdNum(String inpatientNo);

	List<CpVariation> queryPathVsIcdList(String inpatientNo, String page, String rows);

	CpVariation findVariationInfoById(String id);

	List<ComboxVo> queryDictionary(String q, String type);

	List<ComboxVo> queryStageId(String q, String cpId, String versionNo);

	void saveOrUpdate(CpVariation cpVariation);

	PathApply getPathApplyByInNo(String inpatientNo);

	void batchUpDe(String id);

	List<SysDepartment> getDeptName(String queryName);
}
