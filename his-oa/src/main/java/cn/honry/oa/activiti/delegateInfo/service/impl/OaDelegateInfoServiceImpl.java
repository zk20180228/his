package cn.honry.oa.activiti.delegateInfo.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.OaBpmConfNode;
import cn.honry.base.bean.model.OaDelegateInfo;
import cn.honry.oa.activiti.bpm.utils.BeanMapper;
import cn.honry.oa.activiti.delegateInfo.dao.OaDelegateInfoDao;
import cn.honry.oa.activiti.delegateInfo.service.OaDelegateInfoService;
import cn.honry.oa.activiti.delegateInfo.vo.DelegateInfoVo;
import cn.honry.utils.ShiroSessionUtils;

/**
 * 代理配置service实现类
 * @author luyanshou
 *
 */

@Service("oaDelegateInfoService")
@Transactional
@SuppressWarnings({ "all" })
public class OaDelegateInfoServiceImpl implements OaDelegateInfoService {

	@Autowired
	@Qualifier(value = "oaDelegateInfoDao")
	private OaDelegateInfoDao oaDelegateInfoDao;
	
	public void setOaDelegateInfoDao(OaDelegateInfoDao oaDelegateInfoDao) {
		this.oaDelegateInfoDao = oaDelegateInfoDao;
	}

	private BeanMapper mapper= new BeanMapper();
	
	@Override
	public OaDelegateInfo get(String arg0) {
		return oaDelegateInfoDao.get(arg0);
	}

	@Override
	public void removeUnused(String arg0) {

	}

	@Override
	public void saveOrUpdate(OaDelegateInfo arg0) {
		oaDelegateInfoDao.save(arg0);
	}

	/**
	 * 获取代理人
	 * @param assignee 负责人
	 * @param processDefinitionId 流程定义id
	 * @param activityId 节点id
	 * @param tenantId 租户id
	 * @return
	 */
	public Map getAttorney(String assignee,String processDefinitionId,String activityId,String tenantId){
		Map<String,String> map = new HashMap<>();
		List<OaDelegateInfo> list = oaDelegateInfoDao.getInfo(assignee, processDefinitionId, activityId, tenantId);
		if(list==null || list.size()==0){
			return map;
		}
		Date now = new Date();
		for (OaDelegateInfo info : list) {
			Date startTime = info.getStartTime();
			Date endTime = info.getEndTime();
			String assignee2 = info.getAssignee();
			String attorney2 = info.getAttorney();
			if(startTime==null){
				startTime=now;
			}
			if(endTime==null){
				endTime=now;
			}
			if(now.after(startTime) && now.before(endTime)){
				String att = map.get(assignee2);
				if(att!=null){
					att+=","+attorney2;
				}else{
					map.put(assignee2, attorney2);
				}
			}
			
		}
		return map;
	}

	@Override
	public List<OaDelegateInfo> queryMyDelegate(int page, int rows) {
		return oaDelegateInfoDao.queryMyDelegate(page, rows);
	}

	@Override
	public int queryMyDelegateRotal() {
		return oaDelegateInfoDao.queryMyDelegateTotal();
	}

	@Override
	public List<DelegateInfoVo> queryProcess() {
		return oaDelegateInfoDao.queryProcess();
	}

	@Override
	public List<OaBpmConfNode> queryOaBpmConfNode(String proDefId) {
		return oaDelegateInfoDao.queryOaBpmConfNode(proDefId);
	}

	@Override
	public void addOaDelegateInfo(OaDelegateInfo oadeInfo) {
		OaDelegateInfo info = new OaDelegateInfo();
		String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String asName = ShiroSessionUtils.getCurrentUserFromShiroSession().getName();
		if(StringUtils.isBlank(oadeInfo.getId())){
			oadeInfo.setAssignee(account);
			oadeInfo.setAssingeName(asName);
			oadeInfo.setStatus(1);
			oadeInfo.setCreateTime(new Date());
			oadeInfo.setCreateUser(account);
			oadeInfo.setCreateDept(ShiroSessionUtils.getCurrentUserDepartmentFromShiroSession().getDeptCode());
			mapper.copy(oadeInfo, info);
			saveOrUpdate(info);
			//oaDelegateInfoDao.addOaDelegateInfo(info);
		}else{
			oadeInfo.setAssignee(account);
			oadeInfo.setAssingeName(asName);
			oadeInfo.setStatus(1);
			oadeInfo.setUpdateTime(new Date());
			oadeInfo.setUpdateUser(account);
			oaDelegateInfoDao.addOaDelegateInfo(oadeInfo);
		}
		
	}

	@Override
	public void delMydelegateInfo(String id) {
		oaDelegateInfoDao.delMydelegateInfo(id);
		
	}

	@Override
	public OaDelegateInfo findMydelegateInfo(String id) {
		return oaDelegateInfoDao.findMydelegateInfo(id);
	}
}
