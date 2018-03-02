package cn.honry.inner.baseinfo.stackInfo.dao;

import java.util.List;

import cn.honry.base.bean.model.BusinessStack;
import cn.honry.base.bean.model.BusinessStackinfo;
import cn.honry.base.dao.EntityDao;

@SuppressWarnings({"all"})
public  interface StackinfoInInterDAO  extends EntityDao<BusinessStackinfo>{
	
	/**  
	 *  
	 * @Description： 根据外键fk查询子表的记录
	 * @Author：kjh
	 * @CreateDate：2015-5-13   
	 * @param：fkStackId组套主表id
	 * @version 1.0
	 *
	 */
	public List<BusinessStackinfo> findStackInfoByFk(String fkStackId);
	
	/**  
	 *  
	 * @Description： 批量删除
	 * @Author：kjh
	 * @CreateDate：2015-5-14 
	 * @param：ids:删除数据id  
	 * @version 1.0
	 *
	 */
	public void batchDel(String ids);
	/**
	 * 删除组套同时也删除组套明细表的数据
	 * @author  lyy
	 * @createDate： 2016年6月3日 下午5:15:42 
	 * @modifier lyy
	 * @modifyDate：2016年6月3日 下午5:15:42
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	public void deleteBusinessStackInfo(String stackId);
}
