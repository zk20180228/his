package cn.honry.statistics.bi.inpatient.inpatientDrugRatio.service;

import java.util.List;

import cn.honry.base.bean.model.BiBaseOrganization;
import cn.honry.base.bean.model.BiInpatientInfo;
import cn.honry.base.service.BaseService;
import cn.honry.statistics.util.dateVo.DateVo;

public interface InpatientDrugRatioService extends BaseService<BiInpatientInfo> {
	
	String queryInpatientDrugRatio(DateVo datevo, String[] dimStringArray,int dateType,String dimensionValue);
	/**
	 * 查询科室code、name
	 * @author yeguanqun
	 * @return
	 */
	List<BiBaseOrganization> queryDeptForBiPublic(String deptType);
}
