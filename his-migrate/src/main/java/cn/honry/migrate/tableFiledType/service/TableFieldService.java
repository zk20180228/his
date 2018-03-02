package cn.honry.migrate.tableFiledType.service;

import java.util.List;

import cn.honry.base.bean.model.TableFiledType;

/**
 * 
 * 
 * <p>同步数据service </p>
 * @Author: XCL
 * @CreateDate: 2017年9月20日 下午2:13:57 
 * @Modifier: XCL
 * @ModifyDate: 2017年9月20日 下午2:13:57 
 * @ModifyRmk:  
 * @version: V1.0:
 *
 */
public interface TableFieldService {
	/**
	 * 
	 * 
	 * <p>查询同步记录 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年9月20日 下午2:15:20 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年9月20日 下午2:15:20 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param queryCode
	 * @param rows
	 * @param page
	 * @param menuAlias
	 * @return:
	 *
	 */
	public List<TableFiledType> queryTableField(String queryCode,String rows,String page,String menuAlias);
	/**
	 * 
	 * 
	 * <p>查询同步记录总条数</p>
	 * @Author: XCL
	 * @CreateDate: 2017年9月20日 下午2:16:16 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年9月20日 下午2:16:16 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param queryCode
	 * @param menuAlias
	 * @return:
	 *
	 */
	public int queryTableFieldTotal(String queryCode,String menuAlias);
	/**
	 * 
	 * 
	 * <p>保存和更新同步数据记录 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年9月20日 下午2:17:21 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年9月20日 下午2:17:21 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param entity:
	 *
	 */
	public void saveOrUpdateTableField(TableFiledType entity);
	/**
	 * 
	 * 
	 * <p>删除同步数据 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年9月20日 下午2:18:16 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年9月20日 下午2:18:16 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param 
	 *
	 */
	public void delTableField(String id);
	/**
	 * 
	 * 
	 * <p>查询修改记录 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年9月20日 下午4:41:14 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年9月20日 下午4:41:14 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param id
	 * @return:
	 *
	 */
	public TableFiledType getOneDate(String id);
	
}
