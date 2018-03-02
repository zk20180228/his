package cn.honry.oa.activiti.bpm.process.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.OaBpmCategory;
import cn.honry.base.bean.model.OaBpmConfBase;
import cn.honry.base.bean.model.OaBpmProcess;
import cn.honry.oa.activiti.bpm.process.dao.OaBpmProcessDao;
import cn.honry.oa.activiti.bpm.process.service.OaBpmProcessService;
import cn.honry.oa.activiti.bpm.process.vo.OaProcessVo;

/**
 * 流程配置Service实现类
 * @author luyanshou
 *
 */
@Service("oaBpmProcessService")
@Transactional
@SuppressWarnings({ "all" })
public class OaBpmProcessServiceImpl implements OaBpmProcessService {

	@Autowired
	@Qualifier(value = "oaBpmProcessDao")
	private OaBpmProcessDao oaBpmProcessDao;
	
	public void setOaBpmProcessDao(OaBpmProcessDao oaBpmProcessDao) {
		this.oaBpmProcessDao = oaBpmProcessDao;
	}

	@Override
	public OaBpmProcess get(String arg0) {
		return oaBpmProcessDao.get(arg0);//根据id获取对象
	}

	@Override
	public void removeUnused(String arg0) {

	}

	@Override
	public void saveOrUpdate(OaBpmProcess arg0) {

		oaBpmProcessDao.save(arg0);
	}

	/**
	 * 根据租户id获取流程配置分页数据
	 * @param tenantId 租户id
	 * @param firstResult
	 * @param maxResults
	 * @return
	 */
	public List<OaBpmProcess> getListByPage(String name,String tenantId,int firstResult,int maxResults){
		return oaBpmProcessDao.getListByPage(name,tenantId, firstResult, maxResults);
	}
	
	/**
	 * 根据租户id获取流程配置总数
	 * @param tenantId 租户id
	 */
	public int getTotal(String name,String tenantId){
		return oaBpmProcessDao.getTotal(name,tenantId);
	}
	
	/**
	 * 获取流程定义信息列表及所属分类
	 * @return
	 */
	public List<OaBpmCategory> getCategoryList(){
		List<OaProcessVo> voList = oaBpmProcessDao.getCategoryInfo();
		Map<String,OaBpmCategory> map = new HashMap<>();
		for (OaProcessVo vo : voList) {
			OaBpmProcess process= new OaBpmProcess();
			process.setId(vo.getId());//流程定义id
			process.setName(vo.getName());//流程定义名称
			process.setDescn(vo.getDescn());//流程定义描述
			String code = vo.getCategoryCode();//所属分类id
			OaBpmCategory category = map.get(code);
			if(category!=null){
				category.getBpmProcesses().add(process);
			}else{
				category=new OaBpmCategory();
				category.setCode(code);
				category.setName(vo.getCategoryName());
				Set<OaBpmProcess> processSet=new HashSet<>();
				processSet.add(process);
				category.setBpmProcesses(processSet);
				map.put(code, category);
			}
		}
		List<OaBpmCategory> list = new ArrayList<>();
		list.addAll(map.values());
		return list;
	}
	/**
	 * 获取流程定义信息列表及所属分类
	 * @return
	 */
	public List<OaProcessVo> getCategoryList1(int page, int rows,String param,String category,String treeId){
		List<OaProcessVo> voList = oaBpmProcessDao.getCategoryInfo(page,rows,param,category,treeId);
		return voList;
	}
	public Integer getCategoryList1Size(String param,String category,String treeId){
		Integer total = oaBpmProcessDao.getCategoryInfo(param,category,treeId);
		return total;
	}
	
	/**
	 * 根据流程定义信息id获取配置信息
	 * @param processId
	 * @return
	 */
	public OaBpmConfBase getConfBase(String processId){
		return oaBpmProcessDao.getConfBase(processId);
	}
	
	/**
	 * 根据流程定义id获取流程定义信息
	 * @param processDefinitionId
	 * @return
	 */
	public OaBpmProcess getProcessInfo(String processDefinitionId){
		return oaBpmProcessDao.getProcessInfo(processDefinitionId);
	}
	
}
