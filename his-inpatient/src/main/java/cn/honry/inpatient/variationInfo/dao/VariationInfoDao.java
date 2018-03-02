package cn.honry.inpatient.variationInfo.dao;

import java.util.List;

import cn.honry.base.bean.model.CpVariation;
import cn.honry.base.bean.model.PathApply;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.EntityDao;
import cn.honry.inpatient.variationInfo.vo.ComboxVo;

public interface VariationInfoDao extends EntityDao<CpVariation>{

	List<SysDepartment> listTree();
	List<PathApply> findPathApply();
	Integer queryPathVsIcdNum(String inpatientNo);
	List<CpVariation> queryPathVsIcdList(String inpatientNo, String page, String rows);
	List<ComboxVo> queryDictionary(String q, String type);
	List<ComboxVo> queryStageId(String q, String cpId, String versionNo);
	PathApply getPathApplyByInNo(String inpatientNo);
	void batchUpDe(String id);
	List<SysDepartment> getDeptName(String queryName);

}
