package cn.honry.inner.inpatient.consultation.dao;

import cn.honry.base.bean.model.InpatientConsultation;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.EntityDao;

/**   
* @classname ： ConsultationDao
* @description：  会诊申请单Dao
* @author：tuchuanjiang
* @createDate：2015-12-11 下午19：24  
* @version 1.0
 */
@SuppressWarnings({"all"})
public interface ConsultationInInterDao extends EntityDao<InpatientConsultation>{

	/**  
	 * @Description： 通过id查询员工对象
	 * @Author：tcj
	 * @CreateDate：2015-12-19  上午10:09
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	SysEmployee queryById(String id);
	
}
