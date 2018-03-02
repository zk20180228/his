package cn.honry.oa.activiti.bpm.category.service;

import java.util.List;

import cn.honry.base.bean.model.OaBpmCategory;
import cn.honry.base.service.BaseService;

/**
 * 流程分类Service接口
 * @author luyanshou
 *
 */
public interface OaBpmCategoryService extends BaseService<OaBpmCategory> {

	/**
	 * 根据租户id分页查询流程分类
	 * @param tenantId 租户id
	 * @param firstResult 起始位置
	 * @param maxResults 每页显示条数
	 * @return
	 */
	List<OaBpmCategory> getListByPage(String tenantId,String name,int firstResult,int maxResults);
	
	/**
	 * 根据租户id获取流程分类总条数
	 * @param tenantId
	 * @return
	 */
	int getTotal(String tenantId,String name);
}
