package cn.honry.inner.system.moveDate.dao;

import cn.honry.base.bean.model.MoveDataLog;
import cn.honry.base.dao.EntityDao;

/**  
 *  
 * 内部接口：用户
 * @Author：aizhonghua
 * @CreateDate：2016-7-05 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-7-05 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@SuppressWarnings({"all"})
public interface MoveDataLogDAO extends EntityDao<MoveDataLog>{

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
