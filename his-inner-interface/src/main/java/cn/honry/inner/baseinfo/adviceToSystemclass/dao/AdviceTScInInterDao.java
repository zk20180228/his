package cn.honry.inner.baseinfo.adviceToSystemclass.dao;

import java.util.List;

import cn.honry.base.bean.model.BusinessAdvicetoSystemclass;
import cn.honry.base.dao.EntityDao;
import cn.honry.inner.baseinfo.adviceToSystemclass.vo.CodeSystemtypeVo;
/**
 * 医嘱类型与系统类别关系维护Dao
 *
 */
@SuppressWarnings({"all"})
public interface AdviceTScInInterDao extends EntityDao<BusinessAdvicetoSystemclass>{
	
	/**  
	 * 根据医嘱类别id获得系统类别list
	 * @Author：hedong
	 * @param typeId 医嘱类别id
	 * @date2016-04-20
	 * @version 1.0
	 */
	List<CodeSystemtypeVo> querySystemTypesByTypeId(String typeId,boolean bool);

	/**  
	 * 
	 * <p> 根据医嘱类别及项目类别判断对照关系是否存在 </p>
	 * @Author: aizhonghua
	 * @CreateDate: 2017年2月21日 下午3:14:49 
	 * @Modifier: aizhonghua
	 * @ModifyDate: 2017年2月21日 下午3:14:49 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: BusinessAdvicetoSystemclass
	 *
	 */
	BusinessAdvicetoSystemclass getAdvtoSysByAtSt(String aType, String encode);
}
