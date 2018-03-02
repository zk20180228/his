package cn.honry.statistics.bi.outpatientcost.dao;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.BiOptFeedetail;
import cn.honry.base.dao.EntityDao;
import cn.honry.statistics.bi.outpatient.outpatientWorkload.vo.OutpatientWorkloadVo;
import cn.honry.statistics.bi.outpatientcost.vo.OutpatientcostVo;
import cn.honry.statistics.util.dateVo.DateVo;

@SuppressWarnings({"all"})
public interface OutpatientcostDao extends EntityDao<BiOptFeedetail>{

	/**
	 * @Description:页面加载时加载table
	 * @Author: zhangjin
	 * @CreateDate: 2016年7月15日
	 * @param:date：当前时间
	 * @return 
	 * @Modifier:zhangjin
	 * @ModifyDate:2016-8-22
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	List<OutpatientcostVo> getOutpatientcostlist(String[] dimStringArray,
			List<Map<String, List<String>>> list, int dateType, DateVo datevo);
/**
 * @Description:页面加载时加载table
 * @Author: zhangjin
 * @CreateDate: 2016年8月4日
 * @param:date：当前时间
 * @return 
 * @Modifier:zhangjin
 * @ModifyDate:2016-8-22
 * @ModifyRmk:
 * @version: 1.0
 */
	List<OutpatientcostVo> getOutpatientfeelist(String[] dimStringArray,
			List<Map<String, List<String>>> list, int dateType, DateVo datevo);


}
