package cn.honry.statistics.bi.outpatientDrugRatio.service;

import java.util.List;

import cn.honry.base.bean.model.BiBaseOrganization;
import cn.honry.base.bean.model.BiInpatientInfo;
import cn.honry.base.service.BaseService;
import cn.honry.statistics.util.dateVo.DateVo;

public interface OutpatientDrugRatioService  extends BaseService<BiInpatientInfo>{

	/**
	 * @Description:获取门诊药品比例分析列表数据
	 * @Author： zhuxiaolu
	 * @CreateDate： 2016-7-29
	 * @return List<DrugRatioVo>  
	 * @version 1.0
	**/
	String queryOutpatientDrugRatio(DateVo datevo, String[] dimStringArray,
			int dateType, String dimensionValue);

	/**
	 * 查询科室code、name
	 * @author tcj
	 * @return
	 */
	List<BiBaseOrganization> queryDeptForBiPublic(String deptType);
}
