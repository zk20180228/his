package cn.honry.portal.procedure.service;

import java.util.Map;
/**
 * 存储数据库冗余字段
 * @author hzr
 *
 */
public interface ProcedureService {
	/**
	 * 查询表中的最大时间和最小时间
	 * @param table 需要添加冗余字段的表名
	 * @return 最大时间和最小时间
	 */
	public Map<String,Object> gettime(String table);
	
	/**
	 * 
	 * @param table 需要添加冗余字段的表名
	 * @param maxDateStr 最大时间
	 * @param minDateStr 最小时间
	 * @throws Exception 
	 */
	void getupdate(String table,String maxDateStr,String minDateStr) throws Exception;
}
