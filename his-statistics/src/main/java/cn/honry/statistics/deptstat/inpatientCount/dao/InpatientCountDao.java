package cn.honry.statistics.deptstat.inpatientCount.dao;

import java.util.List;

import cn.honry.base.bean.model.InpatientInfoNow;

@SuppressWarnings({"all"})
public interface InpatientCountDao {
	/** 
	* @Description: 分页查询住院患者信息
	* @author qh
	* @date 2017年7月10日
	*  
	*/
	List<InpatientInfoNow> queryInpatientCountList(String dept, String page,
			String rows);
	/** 
	* @Description: 条件查询住院患者总数量
	* @author qh
	* @date 2017年7月10日
	*  
	*/
	int queryTotal(String dept);

}
