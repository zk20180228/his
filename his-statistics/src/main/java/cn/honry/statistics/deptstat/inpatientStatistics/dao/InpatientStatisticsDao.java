package cn.honry.statistics.deptstat.inpatientStatistics.dao;

import java.util.List;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.statistics.deptstat.inpatientStatistics.vo.InpatientStatisticsVo;

@SuppressWarnings({"all"})
public interface InpatientStatisticsDao {
	/** 
	* @Description: 科室下拉
	* @author qh
	* @date 2017年7月15日
	*  
	*/
	List<SysDepartment> queryDeptCodeName();
	/** 
	* @Description: 从数据库查询在院人数
	* @author qh
	* @date 2017年7月15日
	*  
	*/
	List<InpatientStatisticsVo> queryDataList(List<String> tnL, String firstData,
			String endData, String code, String flag);
	/** 
	* @Description: 查询院区
	* @author qh
	* @date 2017年7月15日
	*  
	*/
	List<SysDepartment> queryAreaCodeName();
	/** 
	* @Description: 根据院区code查询所属科室
	* @author qh
	* @date 2017年7月15日
	*  
	*/
	String queryDeptByAreaCode(String code);
	/** 
	* @Description: 数据初始化（非实时更新）
	* @author qh
	* @date 2017年7月15日
	*  
	*/
	void saveOrUpdateToDB(String time, String etime, Integer type) throws Exception;
	/** 
	* @Description: 从mongondb查询在院人数数据
	* @author qh
	* @date 2017年7月15日
	*  
	*/
	List<InpatientStatisticsVo> queryDataListFromDB(String time, String collection, String code,
			String flag) throws Exception;
	/** 
	* @Description: 查询科室
	* @author qh
	* @date 2017年7月15日
	*  
	*/
	String queryDeptByAreaCodes(String code);

}
