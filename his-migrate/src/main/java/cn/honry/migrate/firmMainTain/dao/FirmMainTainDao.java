package cn.honry.migrate.firmMainTain.dao;

import java.util.List;

import cn.honry.base.bean.model.FirmMainTain;
import cn.honry.base.dao.EntityDao;

public interface FirmMainTainDao extends EntityDao<FirmMainTain>{
	/**  
	 * 厂商维护列表查询
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	List<FirmMainTain> queryFirmMainTain(String code, String page, String rows,String menuAlias);
	/**  
	 * 厂商维护列表查询(总条数)
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	int queryTotal(String code);
	/**  
	 * 厂商维护 删除
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	void delFirmMainTain(String id);
	/**  
	 * 厂商维护 得到要修改的数据
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	public FirmMainTain getOnedata(String id);
	/**  
	 * 厂商维护 查询编号最大的
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	public FirmMainTain getMaxCode(String id);
	/**
	 * 
	 * 
	 * <p>修改厂商接口密码 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年9月27日 下午7:51:30 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年9月27日 下午7:51:30 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param id
	 * @param pass:
	 *
	 */
	public void updatePasswor(String id,String pass);
	/**  
	 * 厂商维护列表查询
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	List<FirmMainTain> queryFirm();
	
}
