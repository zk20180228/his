package cn.honry.statistics.deptstat.inpatientCount.service;

import java.util.List;

import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.statistics.deptstat.antimicrobialDrugAccess.vo.AntimicrobialDrugAccessVo;
import cn.honry.utils.FileUtil;

@SuppressWarnings({"all"})
public interface InpatientCountService {
	/** 
	* @Description: 分页查询住院患者信息
	* @author qh
	* @date 2017年7月10日
	*  
	*/
	List<InpatientInfoNow> queryInpatientCountList(String dept, String page, String rows);
	/** 
	* @Description: 条件查询住院患者总数量
	* @author qh
	* @date 2017年7月10日
	*  
	*/
	int queryTotal(String dept);
	/** 
	* @Description:导出功能
	* @author qh
	 * @throws Exception 
	* @date 2017年7月11日
	*  
	*/
	FileUtil exportList(List<InpatientInfoNow> list, FileUtil fUtil) throws Exception;

}
