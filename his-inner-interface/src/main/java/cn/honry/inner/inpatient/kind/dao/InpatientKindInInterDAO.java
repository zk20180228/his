package cn.honry.inner.inpatient.kind.dao;

import java.util.List;

import cn.honry.base.bean.model.InpatientKind;
import cn.honry.base.dao.EntityDao;

@SuppressWarnings({"all"})
public interface InpatientKindInInterDAO extends EntityDao<InpatientKind>{
	
	/**  
	 *  
	 * 查询所有符合条件的数据
	 * @Author liguikang
	 * @date 2016-03-23 
	 * @version 1.0
	 *
	 */
	List<InpatientKind> queryKindInfo();

	int queryKindInfoEncode(String encode,String typeCode );
	
	/**  
	 *  
	 * 查询医嘱资料
	 * @Author：liguikang
	 * @date 2016-03-23   
	 * @version 1.0
	 *
	 */
	List<InpatientKind> getPage(String page, String rows, InpatientKind kindInfo);

	/**  
	 *  
	 * 查询医嘱总条数
	 * @Author：liguikang
	 * @date 2016-03-23   
	 * @version 1.0
	 *
	 */
	int getTotal(InpatientKind entity);
	/**  
	 *  
	 * 生成编号
	 * @Author：liguikang
	 * @date 2016-03-28   
	 * @version 1.0
	 *
	 */
	int getSequece(InpatientKind entity );

	/**
	 * 根据医嘱名称获得医嘱id
	 * @Description：计算年龄
	 * @Author：aizhonghua
	 * @CreateDate：2016年4月19日 上午10:09:44 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0
	 * @param birthday
	 * @return：年龄
	 *
	 */
	String queryKindInfoByName(String name);

	InpatientKind getByCode(String typeCode);
}
	
	
	 



