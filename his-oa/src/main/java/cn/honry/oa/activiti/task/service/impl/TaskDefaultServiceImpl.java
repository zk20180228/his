package cn.honry.oa.activiti.task.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.OaTaskDefBase;
import cn.honry.oa.activiti.bpm.form.vo.ConfFormVo;
import cn.honry.oa.activiti.task.dao.TaskDefaultDao;
import cn.honry.oa.activiti.task.service.TaskDefaultService;

@Service("taskDefaultService")
@Transactional
@SuppressWarnings({ "all" })
public class TaskDefaultServiceImpl implements TaskDefaultService {

	@Autowired
	@Qualifier(value = "taskDefaultDao")
	private TaskDefaultDao taskDefaultDao;

	public void setTaskDefaultDao(TaskDefaultDao taskDefaultDao) {
		this.taskDefaultDao = taskDefaultDao;
	}
	

	public <T> T findUnique(String hql,T t,Object... values){
		return taskDefaultDao.findUnique(hql, t, values);
	}
	
	public <T> void saveOrUpdate(T t){
		taskDefaultDao.saveOrUpdate(t);
	}
	
	
	public <T> T get(String id,T t){
		return taskDefaultDao.get(id, t);
	}
	
	
	public <T> List<T> getList(String hql,T t,Object... values){
		return taskDefaultDao.getList(hql, t, values);
	}
	
	public <T> List<T> getListByPage(String hql,int firstResult,int maxResults,T t,Object... values){
		return taskDefaultDao.getList(hql, firstResult, maxResults, t, values);
	}
	
	/**
	 * 查询总条数
	 * @param hql 查询语句
	 * @param values 查询参数
	 * @return
	 */
	public int getCount(String hql,Object... values){
		return taskDefaultDao.getCount(hql, values);
	}
}
