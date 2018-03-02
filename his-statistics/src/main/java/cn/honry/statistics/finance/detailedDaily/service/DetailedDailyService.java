package cn.honry.statistics.finance.detailedDaily.service;

import java.util.List;

import cn.honry.base.service.BaseService;
import cn.honry.statistics.finance.detailedDaily.vo.VdetailedDaily;
import cn.honry.utils.FileUtil;

public interface DetailedDailyService extends BaseService<VdetailedDaily>{
	/***
	 * 
	 * @Description:查询结账明细日报  当天的
	 * @author:  donghe
	 * @CreateDate: 2016年6月22日 
	 * @version 1.0
	 * @throws Exception 
	 */
	List<VdetailedDaily> queryVdetailedDaily(String beginDate,String endDate,String page,String rows) throws Exception;
	/***
	 * 
	 * @Description:查询结账明细日报  当天的
	 * @author:  donghe
	 * @CreateDate: 2016年6月22日 
	 * @version 1.0
	 * @throws Exception 
	 */
	int queryVdetailedDailyTotal(String beginDate,String endDate) throws Exception;
	/**
	 * @Description:导出列表
	 * @Description:
	 * @author: dh
	 * @CreateDate: 2016年6月22日 
	 * @version 1.0
	**/
	FileUtil export(List<VdetailedDaily> list, FileUtil fUtil);
}
