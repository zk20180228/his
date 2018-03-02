package cn.honry.inner.baseinfo.stack.dao;

import java.util.List;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.BusinessFrequency;
import cn.honry.base.bean.model.BusinessStack;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.EntityDao;
import cn.honry.inner.baseinfo.stack.vo.StackAndStockInfoInInterVo;

@SuppressWarnings({"all"})
public interface StackInInterDAO  extends EntityDao<BusinessStack>{
	
	/**  
	 *  
	 * @Description： 分页
	 * @Author：kjh
	 * @CreateDate：2015-5-10   
	 * @version 1.0
	 *
	 */
	List<BusinessStack> getPage(String page, String rows,BusinessStack businessStack);
	/**  
	 *  
	 * @Description： 得到总页数
	 * @Author：kjh
	 * @CreateDate：2015-5-10   
	 * @version 1.0
	 *
	 */
	int getTotal(BusinessStack businessStack);
	
	/**  
	 *  
	 * @Description：批量删除
	 * @Author：kjh
	 * @CreateDate：2015-5-15   
	 * @param：tablName：表名  ids：实体ids idName：表中主键id
	 * @version 1.0
	 *
	 */
	public void batchDel(String tablName,String ids,String idName);
	
	/**  
	 *  
	 * @Description： 添加组套主表，和组套子表的信息
	 * @Author：kjh
	 * @CreateDate：2015-5-10 
	 * @param：  entity:主表实体  stackInfosJson：子表json
	 * @version 1.0
	 *
	 */
	public boolean saveOrUpdate(BusinessStack entity, String stackInfosJson) throws Exception ;
	
	/**  
	 *  
	 * @Description：获取药品和非药品数据list
	 * @Author：sgt
	 * @CreateDate：2015-06-12   
	 * @version 1.0
	 * @param flag 
	 *
	 */
	List stackInfoName(String page, String rows);
	/**  
	 *  
	 * @Description：获取药品和非药品数据记录条数
	 * @Author：kjh
	 * @CreateDate：2015-06-12   
	 * @version 1.0
	 * @param flag 
	 *
	 */
	int countDrugAndUndrug(String page, String rows);
	/**  
	 *  
	 * @Description：  增加数据/修改数据
	 * @Author：sgt
	 * @CreateDate：2015-06-13  
	 * @version 1.0
	 *
	 */
	void saveOrupdataBusinessStack(BusinessStack businessStack);
	
	/**  
	 *  
	 * @Description： 删除数据
	 * @Author：sgt
	 * @CreateDate：2015-06-13   
	 * @version 1.0
	 *
	 */
	void deleteBusinessStack(BusinessStack businessStack);
	
	/**  
	 *  
	 * @Description： 根据ID查询数据
	 * @Author：sgt
	 * @CreateDate：2015-06-13   
	 * @version 1.0
	 *
	 */
	BusinessStack getBusinessStackById(BusinessStack businessStack);
	
	/**  
	 *  
	 * @Description： 根据ID查询数据
	 * @Author：kjh
	 * @CreateDate：2015-7-3   
	 * @version 1.0
	 *
	 */
	List<BusinessStack> getSysDepartmentById(String id);
	
	
	/**  
	 *  根据组套类型获得组套信息
	 * @Description：  
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-7 上午11:12:09  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-7 上午11:12:09  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<BusinessStack> getStackByType(int type,String id);
	/**  
	 *  
	 * @Description：  根据组套来源获得组套信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-8 下午06:53:32  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-8 下午06:53:32  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<BusinessStack> getStackBySource(String id);
	/**
	 * 执行科室查询
	 * @author  lyy
	 * @createDate： 2016年4月13日 下午4:26:02 
	 * @modifier lyy
	 * @modifyDate：2016年4月13日 下午4:26:02
	 * @param：    name 部门名称
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<SysDepartment> queryDept(String name);
	/**
	 *根据最小单位和包装单位
	 * @author  lyy
	 * @createDate： 2016年4月13日 下午9:52:33 
	 * @modifier lyy
	 * @modifyDate：2016年4月13日 下午9:52:33
	 * @param：      pid 包装单位id  uid 最小单位id stackId 项目名称
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<BusinessDictionary> packagingUnitcomboBox(String pid, String uid,String name,String stackId);
	/**
	 * 根据查询条件查询科室树
	 * @author  lyy
	 * @createDate： 2016年4月27日 上午9:44:12 
	 * @modifier lyy
	 * @modifyDate：2016年4月27日 上午9:44:12
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<SysDepartment> getDeptName(String queryName);
	/**
	 * 查询科室下的组套
	 * @author  donghe
	 * @createDate： 2016年4月13日 下午9:52:33 
	 * @param：     
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<BusinessStack> getDepartmentById(String deptId);
	
	/**
	 * 查询组套主表
	 * @author  zhenglin
	 * @createDate： 
	 * @modifier lyy
	 * @modifyDate：2016年4月12日 上午9:42:29
	 * @param：   stack 组套实体 id 组套来源  deptId 登录科室 userId 登录人     stackObject(组套对象: 1是财务用，2是医嘱用)  remark(门诊还是住院，收费不区分，医嘱区分) 
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<BusinessStack> getStackInfo(String id,String deptId,String userId,String stackObject,String remark,String root);
	/**
	 * 渲染频次
	 * @author  zhenglin
	 * @createDate： 
	 * @modifier lyy
	 * @modifyDate：2016年4月12日 下午12:00:30
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<BusinessFrequency> getFreq();
	
	/**
	 * 查看药品组套详情
	 * @author  zhenglin
	 * @createDate： 
	 * @modifier lyy
	 * @modifyDate：2016年4月11日 下午2:59:13
	 * @param： id 组套的编号   drugstoreId 诊断的选择的药房 feelType 是否是收费类型
	 * @modifyRmk：  
	 */
	List<StackAndStockInfoInInterVo> queryStackInfoById(String id,String drugstoreId,String feelType);
	/**
	 * 根据父级查询子级数据
	 * @author  donghe
	 * @createDate： 
	 * @modifyRmk：  
	 */
	List<BusinessStack> queryStackByIdparent(String parent);
	/**
	 * @Description:根据组套名模糊查询对应的组套，用于展开树
	 * @Author: donghe
	 * @CreateDate: 2016年11月30日
	 * @return:list
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	List<BusinessStack> getstackNameParam(String stackName,String stackObject,String remark);
}
