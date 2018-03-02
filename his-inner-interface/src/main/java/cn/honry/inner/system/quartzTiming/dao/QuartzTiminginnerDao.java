package cn.honry.inner.system.quartzTiming.dao;


import java.util.List;

import cn.honry.base.bean.model.RegisterDocSource;
import cn.honry.base.dao.EntityDao;

public interface QuartzTiminginnerDao extends EntityDao<RegisterDocSource> {

	/**
	 * @Description:创建添加SQl
	 * @Author：  zhangjin
	 * @CreateDate： 2016-1-6
	 * @param @return   
	 * @return  
	 * @version 1.0
	**/
//	void saveSql(List<RegisterDocSourceVo> reglist);

	RegisterDocSource haveDoc(String SCHEDULE_DOCTOR, String SCHEDULE_MIDDAY, String SCHEDULE_DATE);
}
