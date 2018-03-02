package cn.honry.statistics.bi.bistac.mongoDataInit.service;

import java.util.List;

import cn.honry.base.bean.model.MongoCount;
import cn.honry.base.bean.model.MongoLog;

public interface MongoDataInitService {

	/**
	 * mongodb数据初始化方法
	 * @param menuName 栏目名
	 * @param beginDate 开始时间
	 * @param endDate 结束时间
	 * @param type 统计类型(1-按日统计；2-按月统计；3-按年统计)
	 * @throws Exception 
	 */
	void mongoDataInit(String menuName,String beginDate,String endDate,Integer type) throws Exception;
	/** 查询失败的日志信息
	* @Title: findMongoLog 
	* @Description: 
	* @param menuType
	* @return
	* @return List<MongoLog>    返回类型 
	* @throws 
	* @author mrb
	* @date 2017年6月23日
	*/
	List<MongoLog> findMongoLog(String menuType);
	/** 根据栏目别名获取栏目的预处理设置
	* @Title: getMongoCount 
	* @Description: 
	* @param menuType
	* @return
	* @return MongoCount    返回类型 
	* @throws 
	* @author mrb
	* @date 2017年6月20日
	*/
	MongoCount getMongoCount(String menuType);
	

}
