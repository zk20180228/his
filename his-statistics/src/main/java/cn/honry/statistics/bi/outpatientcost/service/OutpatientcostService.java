package cn.honry.statistics.bi.outpatientcost.service;

import java.util.List;

import cn.honry.base.bean.model.BiOptFeedetail;
import cn.honry.base.service.BaseService;
import cn.honry.statistics.bi.outpatientcost.vo.OutpatientcostVo;
import cn.honry.statistics.util.dateVo.DateVo;

@SuppressWarnings({"all"})
public interface OutpatientcostService extends BaseService<BiOptFeedetail>{

	/**
	 * @Description:页面加载时加载table
	 * @Author: zhangjin
	 * @CreateDate: 2016年7月15日
	 * @param:date：当前时间
	 * @return 
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	String getOutpatientcostlist(DateVo datevo, String[] dimStringArray,
			int dateType, String dimensionValue);
	/**
	 * @Description:页面加载时加载table
	 * @Author: zhangjin
	 * @CreateDate: 2016年8月4日
	 * @param:date：当前时间
	 * @return 
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	String getOutpatientfeelist(DateVo datevo, String[] dimStringArray,
			int dateType, String dimensionValue);
	
	

}
