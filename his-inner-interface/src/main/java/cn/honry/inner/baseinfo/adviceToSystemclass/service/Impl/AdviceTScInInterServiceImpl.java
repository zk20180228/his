package cn.honry.inner.baseinfo.adviceToSystemclass.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessAdvicetoSystemclass;
import cn.honry.inner.baseinfo.adviceToSystemclass.dao.AdviceTScInInterDao;
import cn.honry.inner.baseinfo.adviceToSystemclass.service.AdviceTScInInterService;

/**  
 *  
 * 接口：医嘱项目及系统类别
 * @Author：aizhonghua
 * @CreateDate：2016-2-24 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-2-24 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Service("adviceTScInInterService")
@Transactional
@SuppressWarnings({ "all" })
public class AdviceTScInInterServiceImpl implements AdviceTScInInterService{

	@Autowired
	@Qualifier(value = "adviceTScInInterDao")
	private AdviceTScInInterDao adviceTScInInterDao;

	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public BusinessAdvicetoSystemclass get(String id) {
		return null;
	}

	@Override
	public void saveOrUpdate(BusinessAdvicetoSystemclass entity) {
		
	}

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
	@Override
	public BusinessAdvicetoSystemclass getAdvtoSysByAtSt(String aType,String encode) {
		return adviceTScInInterDao.getAdvtoSysByAtSt(aType,encode);
	}

	
}
