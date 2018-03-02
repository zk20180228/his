package cn.honry.oa.activiti.delegateInfo.dao;

import java.util.List;

import cn.honry.base.bean.model.OaBpmConfNode;
import cn.honry.base.bean.model.OaBpmProcess;
import cn.honry.base.bean.model.OaDelegateInfo;
import cn.honry.base.dao.EntityDao;
import cn.honry.oa.activiti.delegateInfo.vo.DelegateInfoVo;

/**
 * 代理配置DAO接口
 * @author luyanshou
 *
 */
public interface OaDelegateInfoDao extends EntityDao<OaDelegateInfo>{

	/**
	 * 获取代理配置
	 * @param assignee
	 * @param processDefinitionId
	 * @param activityId
	 * @param tenantId
	 * @return
	 */
	List<OaDelegateInfo> getInfo(String assignee,String processDefinitionId,String activityId,String tenantId);
	/**
	 * 查询我的代理
	 * @param page
	 * @param rows
	 * @return List<OaDelegateInfo> 
	 */
	List<OaDelegateInfo> queryMyDelegate(int page,int rows);
	
	/**
	 * 查询我的代理总记录数
	 * @return int
	 */
	int queryMyDelegateTotal();
	
	/**
	 * 业务名称
	 * @param page
	 * @param rows
	 * @return List<OaDelegateInfo> 
	 */
	List<DelegateInfoVo> queryProcess();
	
	/**
	 * 根据业务名称code 查询环节
	 * @param page
	 * @param rows
	 * @return List<OaDelegateInfo> 
	 */
	List<OaBpmConfNode> queryOaBpmConfNode(String proDefId);
	
	/**
	 * 添加我的代理
	 * @param OaDelegateInfo oadeInfo
	 */
	void addOaDelegateInfo(OaDelegateInfo oadeInfo);
	
	/**
	 * 删除我的代理
	 * @param String id
	 */
	void delMydelegateInfo(String id);
	
	/**
	 * 根据id查询我的代理
	 * @param String id
	 */
	OaDelegateInfo  findMydelegateInfo(String id);
}
