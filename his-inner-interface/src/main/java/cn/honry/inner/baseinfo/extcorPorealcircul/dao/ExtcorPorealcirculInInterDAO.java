package cn.honry.inner.baseinfo.extcorPorealcircul.dao;

import java.util.List;

import cn.honry.base.bean.model.BusinessExtcorPorealcircul;
import cn.honry.base.dao.EntityDao;

/**  
 *  
 * @Author：aizhonghua
 * @CreateDate：2016-2-24 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-2-24 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@SuppressWarnings({"all"})
public interface ExtcorPorealcirculInInterDAO extends EntityDao<BusinessExtcorPorealcircul>{

	/**  
	 *  
	 * @Description：  
	 * @param:id(患者id)
	 * @Author：liudelin
	 * @ModifyDate：2015-7-2上午09：11
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	List<BusinessExtcorPorealcircul> findExtcorPorealcircul(String patMedicalrecordId);
}
