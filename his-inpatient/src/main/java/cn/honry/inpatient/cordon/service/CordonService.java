package cn.honry.inpatient.cordon.service;

import java.util.List;

import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.service.BaseService;
import cn.honry.inpatient.cordon.vo.CordonVo;
import cn.honry.utils.TreeJson;
/**
 * @Description： 住院（护士站）患者警戒线 service
 * @Author：lyy
 * @CreateDate：2015-12-9 下午06:36:13  
 * @Modifier：lyy
 * @ModifyDate：2015-12-9 下午06:36:13  
 * @ModifyRmk：  
 * @version 1.0
 */
public interface CordonService extends BaseService<InpatientInfoNow> {
	/**
	 * @Description：  获取护士站 (封装成josn)
	 * @Author：lyy
	 * @CreateDate：2015-12-9 下午06:36:07  
	 * @Modifier：lyy
	 * @ModifyDate：2015-12-9 下午06:36:07 
	 * @param： id  病区护士站Id 
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	List<TreeJson> TreeDeptCordon(String id) throws Exception;
	/**
	 * 连表查询数据(总条数)
	 * @author  lyy
	 * @createDate： 2015年12月15日 下午5:28:01 
	 * @modifier lyy
	 * @modifyDate：2015年12月15日 下午5:28:01  
	 * @param entity 患者警戒线虚拟实体 deptId 登录科室
	 * @modifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	int getTotal(CordonVo entity,String deptId) throws Exception;
	/**
	 * 连表查询数据
	 * @author  lyy
	 * @createDate： 2015年12月15日 下午5:28:24 
	 * @modifier lyy
	 * @modifyDate：2015年12月15日 下午5:28:24  
	 * @param entity 患者警戒线虚拟实体  page 当前页   rows 当前页条数  deptId 登录科室
	 * @modifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	List<CordonVo> getPage(CordonVo entity, String page, String rows,String deptId) throws Exception;
	/**
	 * 设置警戒线（保存）
	 * @author  lyy
	 * @createDate： 2015年12月21日 上午11:15:15 
	 * @modifier lyy
	 * @modifyDate：2015年12月21日 上午11:15:15  
	 * @param cordonVo  患者警戒线虚拟实体
	 * @modifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	void saveCordonVo(CordonVo cordonVo) throws Exception;
}

