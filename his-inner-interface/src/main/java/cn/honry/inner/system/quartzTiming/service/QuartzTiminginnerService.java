package cn.honry.inner.system.quartzTiming.service;

import java.util.List;

import cn.honry.base.bean.model.RegisterScheduleNow;
import cn.honry.base.bean.model.TimingRules;
import cn.honry.base.service.BaseService;

public interface QuartzTiminginnerService extends BaseService<RegisterScheduleNow>{
	
	void quartzTiming();
	
	/**
	 * 执行预处理
	 * @param menuAlias 栏目别名
	 * @param type 类型
	 * @param date 开始时间
	 */
	void pretreatmentExecute(String menuAlias,Integer type,String date);
	
	/**
	 * 获取有效的、开启状态的预处理定时规则
	 * @return 
	 */
	List<TimingRules> getRulesList();
	
	/**
	 * 删除三天前的执行成功的日志
	 */
	void delMongoLog();
}
