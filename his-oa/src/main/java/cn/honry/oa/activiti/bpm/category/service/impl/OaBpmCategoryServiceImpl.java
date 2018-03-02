package cn.honry.oa.activiti.bpm.category.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.OaBpmCategory;
import cn.honry.oa.activiti.bpm.category.dao.OaBpmCategoryDao;
import cn.honry.oa.activiti.bpm.category.service.OaBpmCategoryService;

/**
 * 流程分类Service实现类
 * @author luyanshou
 *
 */
@Service("oaBpmCategoryService")
@Transactional
@SuppressWarnings({ "all" })
public class OaBpmCategoryServiceImpl implements OaBpmCategoryService {


	@Autowired
	@Qualifier(value="oaBpmCategoryDao")
	private OaBpmCategoryDao oaBpmCategoryDao;
	
	public void setOaBpmCategoryDao(OaBpmCategoryDao oaBpmCategoryDao) {
		this.oaBpmCategoryDao = oaBpmCategoryDao;
	}

	@Override
	public OaBpmCategory get(String arg0) {
		
		return oaBpmCategoryDao.get(arg0);//根据id获取对象
	}

	@Override
	public void removeUnused(String arg0) {

	}

	@Override
	public void saveOrUpdate(OaBpmCategory arg0) {

		oaBpmCategoryDao.save(arg0);
	}

	/**
	 * 根据租户id分页查询流程分类
	 * @param tenantId 租户id
	 * @param firstResult 起始位置
	 * @param maxResults 每页显示条数
	 * @return
	 */
	public List<OaBpmCategory> getListByPage(String tenantId,String name,int firstResult,int maxResults){
		return oaBpmCategoryDao.getListByPage(tenantId, name, firstResult, maxResults);
	}
	
	/**
	 * 根据租户id获取流程分类总条数
	 * @param tenantId
	 * @return
	 */
	public int getTotal(String tenantId,String name){
		return oaBpmCategoryDao.getTotal(tenantId, name);
	}
	
	
}
