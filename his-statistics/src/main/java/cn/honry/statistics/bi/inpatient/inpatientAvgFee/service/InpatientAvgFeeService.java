package cn.honry.statistics.bi.inpatient.inpatientAvgFee.service;

import java.util.List;

import cn.honry.base.bean.model.BiBaseOrganization;
import cn.honry.base.bean.model.BiInpatientInfo;
import cn.honry.base.service.BaseService;
import cn.honry.statistics.util.dateVo.DateVo;

public interface InpatientAvgFeeService extends BaseService<BiInpatientInfo>{
	
	String queryInpatientAvgFee(DateVo datevo, String[] dimStringArray,int dateType,String dimensionValue);
	/**
	 * 查询科室code、name
	 * @author yeguanqun
	 * @return
	 */
	List<BiBaseOrganization> queryDeptForBiPublic(String deptType);
}
