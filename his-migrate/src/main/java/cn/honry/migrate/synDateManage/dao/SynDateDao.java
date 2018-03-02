package cn.honry.migrate.synDateManage.dao;

import java.util.List;

import cn.honry.base.bean.model.IDataSynch;
import cn.honry.base.bean.model.ServiceManagement;
import cn.honry.base.dao.EntityDao;

/**
 * 
 * 
 * <p>同步数据dao </p>
 * @Author: XCL
 * @CreateDate: 2017年9月20日 下午2:04:57 
 * @Modifier: XCL
 * @ModifyDate: 2017年9月20日 下午2:04:57 
 * @ModifyRmk:  
 * @version: V1.0:
 *
 */
public interface SynDateDao extends EntityDao<IDataSynch> {
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
	public List<IDataSynch> querySynDate(String queryCode,String rows,String page,String menuAlias,String dateState);
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
	public int querySynDateTotal(String queryCode,String menuAlias,String dateState);
	
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
	public void delSynDate(String id);
	
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
	public IDataSynch getOneDate(String id);
	
	/**
	 * 
	 * 
	 * <p>停用启用 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年9月23日 下午5:27:22 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年9月23日 下午5:27:22 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param id
	 * @param state:
	 * @throws Exception 
	 *
	 */
	public void updateState(String id,String state) throws Exception;
	
	public List<ServiceManagement> getServiceCode(String code);
	/**
	 * 
	 * 
	 * <p>获取最大code </p>
	 * @Author: XCL
	 * @CreateDate: 2017年9月27日 下午2:23:09 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年9月27日 下午2:23:09 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param tableName
	 * @param field
	 * @return:
	 *
	 */
	public int maxCode(String tableName,String field);
}
