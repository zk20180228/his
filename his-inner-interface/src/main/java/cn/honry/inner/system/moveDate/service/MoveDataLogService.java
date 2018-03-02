package cn.honry.inner.system.moveDate.service;

import cn.honry.base.bean.model.MoveDataLog;
import cn.honry.base.service.BaseService;

/** 数据迁移日志Service 
* @ClassName: MoveDataLogService 
* @Description: 数据迁移日志Service 
* @author dtl
* @date 2016年12月9日
*  
*/
public interface MoveDataLogService  extends BaseService<MoveDataLog>{
	/** 查询失败的迁移记录(单表单天查询)
	* @Title: queryMoveDataLog 
	* @Description: 查询迁移记录(单表单天查询)
	* @param optType 1-迁移，2-删除
	* @param dateType 1-门诊，2-住院
	* @param tableName 表名
	* @param dataDate 数据所在日期
	* @author dtl 
	* @date 2016年12月9日
	*/
	MoveDataLog queryMoveDataLog(Integer optType, Integer dateType, String tableName, String dataDate);
}
