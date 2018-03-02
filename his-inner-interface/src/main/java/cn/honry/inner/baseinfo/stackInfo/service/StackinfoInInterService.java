package cn.honry.inner.baseinfo.stackInfo.service;

import java.util.List;

import cn.honry.base.bean.model.BusinessStackinfo;
import cn.honry.base.service.BaseService;

public interface StackinfoInInterService  extends BaseService<BusinessStackinfo>{
	
	/**  
	 *  
	 * @Description： 获得列表数据
	 * @Author：kjh
	 * @CreateDate：2015-5-12   
	 * @version 1.0
	 *
	 */
	public List<BusinessStackinfo> searchStackinfoList() ;
	/**  
	 *  
	 * @Description： 根据外键fk查询子表的记录
	 * @Author：kjh
	 * @CreateDate：2015-5-13  
	 * @param：fkStackId组套主表id type组套主表的类型
	 * @version 1.0
	 *
	 */
	public List<BusinessStackinfo> findStackInfoByFk(String fkStackId);
	/**  
	 *  
	 * @Description： 删除
	 * @Author：kjh
	 * @CreateDate：2015-5-13   
	 * @param：ids:删除数据id
	 * @version 1.0
	 *
	 */
	public void delete(String id);
	
	/**  
	 *  
	 * @Description：  根据组套id获得组套详情
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-7 下午02:54:49  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-7 下午02:54:49  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	public List<BusinessStackinfo> getStackInfo(String stackId);
	/**
	 * 删除组套的同时也删除组套明细表信息
	 * @author  lyy
	 * @createDate： 2016年6月3日 下午5:05:14 
	 * @modifier lyy
	 * @modifyDate：2016年6月3日 下午5:05:14
	 * @param：    stackId 组套id
	 * @modifyRmk：  
	 * @version 1.0
	 */
	public void deleteBusinessStackInfo(String stackId);
}