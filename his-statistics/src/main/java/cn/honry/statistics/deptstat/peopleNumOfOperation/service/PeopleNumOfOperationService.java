package cn.honry.statistics.deptstat.peopleNumOfOperation.service;

import java.util.List;
import java.util.Map;

import cn.honry.statistics.deptstat.peopleNumOfOperation.vo.PeopleNumOfOperationVo;
import cn.honry.utils.FileUtil;

public interface PeopleNumOfOperationService {
	/**
	 * 查询手术科室手术人数统计（含心内）数据
	 * @author zhuxiaolu 
	 * @param 
	 * @param response
	 */
	public Map<String,Object> listPeopleNumOfOperation(String page,String rows, String begin) throws Exception;

	/**
	 * 查询手术科室手术人数统计（含心内）数据总条数
	 * @author zhuxiaolu 
	 * @param 
	 * @param response
	 */
	public Integer findTotalRecord( String begin, String end) throws Exception;

	/**
	 * 导出手术科室手术人数统计（含心内）数据
	 * @author zhuxiaolu 
	 * @param peopleNumOfOperationVo 封装数据
	 * @param response
	 */
	public FileUtil export(List<PeopleNumOfOperationVo> peopleNumOfOperationVo,
			FileUtil fUtil)   throws Exception;

	/**
	 * 初始化手术科室手术人数统计（含心内）数据
	 * @author zhuxiaolu 
	 * @param 
	 * @param response
	 */
	public void initPeopleNumOfOperation( String begin, String end) throws Exception;

	/**
	 * 导出打印手术科室手术人数统计（含心内）数据
	 * @author zhuxiaolu 
	 * @param 
	 * @param response
	 */
	public List<PeopleNumOfOperationVo> exportPeopleNumOfOperation(String begin) throws Exception;
	/**
	 * 手术科室手术人数 预处理
	 * @param begin
	 * @param end
	 * @param type
	 */
	public void init_SSKSSSRSTJ(String begin,String end,Integer type) throws Exception;
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
	public Map<String,Object> queryPeopleNumOfOperation(String startTime,String endTime,String deptCode,String menuAlias,String page,String rows);
	/**  
	 * 手术科室手术人数统计(含心内) 总条数
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */
	int getTotalPeopleNumOfOperation(String startTime, String endTime,String deptCode, String menuAlias, String page, String rows);
}
