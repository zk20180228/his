package cn.honry.inner.outpatient.scheduleModle.dao;

import java.util.List;

import cn.honry.base.bean.model.RegisterSchedulemodel;
import cn.honry.base.dao.EntityDao;

public interface ScheduleModleInInterDAO extends
		EntityDao<RegisterSchedulemodel> {

	/**  
	 * @Description：  查询
	 * @Author：zhangjin
	 * @CreateDate：2016-11-21
	 * @ModifyRmk：
	 * @param:   
	 * @version 1.0
	 */
	List<RegisterSchedulemodel> getmodelList(int scheduleModelhql);

}
