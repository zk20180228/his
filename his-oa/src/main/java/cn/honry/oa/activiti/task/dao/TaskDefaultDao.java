package cn.honry.oa.activiti.task.dao;

import java.util.List;


public interface TaskDefaultDao {

	/**
	 * 执行HQL返回唯一结果
	 * @param hql 查询语句
	 * @param t 返回结果类型
	 * @param values 参数
	 * @return
	 */
	public <T> T findUnique(String hql,T t,Object... values);
	
	/**
	 * 保存方法
	 * @param t
	 */
	public <T> void saveOrUpdate(T t);
	
	/**
	 * 根据主键查询实体
	 * @param id 
	 * @param t
	 * @return
	 */
	public <T> T get(String id,T t);
	
	/**
	 * 执行HQL返回list集合
	 * @param hql 查询语句
	 * @param t 返回泛型
	 * @param values 查询参数
	 * @return
	 */
	public <T> List<T> getList(String hql,T t,Object... values);
	
	/**
	 * 执行HQL返回分页数据
	 * @param hql 查询语句
	 * @param firstResult 起始位置
	 * @param maxResults 每页显示条数
	 * @param t 泛型
	 * @param values 查询参数
	 * @return
	 */
	public <T> List<T> getList(String hql,int firstResult,int maxResults,T t,Object... values);
	
	/**
	 * 查询总条数
	 * @param hql 查询语句
	 * @param values 查询参数
	 * @return
	 */
	public int getCount(String hql,Object... values);
}
