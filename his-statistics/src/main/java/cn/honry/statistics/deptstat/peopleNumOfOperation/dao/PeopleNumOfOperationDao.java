package cn.honry.statistics.deptstat.peopleNumOfOperation.dao;

import java.util.List;
import java.util.Map;

import cn.honry.statistics.deptstat.operationDeptLevel.vo.OperationDeptLevelVo;
import cn.honry.statistics.deptstat.peopleNumOfOperation.vo.PeopleNumOfOperationVo;

public interface PeopleNumOfOperationDao {

	/**
	 * 查询手术科室手术人数统计（含心内）数据
	 * @author zhuxiaolu 
	 * @param 
	 * @param response
	 */
	public List<PeopleNumOfOperationVo> listPeopleNumOfOperation(List<String> tnL, String page,String rows, String begin, String end) throws Exception;
	
	/**
	 * 查询手术科室手术人数统计（含心内）数据总数
	 * @author zhuxiaolu 
	 * @param 
	 * @param response
	 */
	public Integer findTotalRecord(String begin, String end) throws Exception;

	/**
	 * 初始化手术科室手术人数统计（含心内）数据
	 * @author zhuxiaolu 
	 * @param 
	 * @param response
	 */
	public void initPeopleNumOfOperation(String begin, String end) throws Exception;

	/**
	 * 查询列表数据mongodb
	 * @author zhuxiaolu 
	 * @param 
	 * @param response
	 */
	public Map<String, Object> queryForDBList(String begin, String rows,
			String page) throws Exception;

	/**
	 * 查询导出打印数据mongodb
	 * @author zhuxiaolu 
	 * @param 
	 * @param response
	 */
	public List<PeopleNumOfOperationVo> queryExportForDBList(String begin) throws Exception;

	/**
	 * 查询导出打印数据数据库
	 * @author zhuxiaolu 
	 * @param 
	 * @param response
	 */
	public List<PeopleNumOfOperationVo> listPeopleNumToDB(String startDate,
			String endDate) throws Exception;

	/**
	 * 手术科室手术人数统计(含心内)
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */
	public List<PeopleNumOfOperationVo> queryPeopleNumOfOperation(String startTime,String endTime,String deptCode,String menuAlias,String page, String rows);
	/**
	 * 手术科室手术人数统计(含心内)  总条数
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */
	public int getTotalPeopleNumOfOperation(String startTime, String endTime,String deptCode, String menuAlias, String page, String rows);
	/**
	 * 手术科室手术人数统计(含心内)  mongdb
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */
	public Map<String,Object> queryPeopleNumOfOperationForDB(String startTime, String endTime, String deptCode,String menuAlias, String page, String rows);

}
