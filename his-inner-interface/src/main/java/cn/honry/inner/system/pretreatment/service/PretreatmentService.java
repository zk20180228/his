package cn.honry.inner.system.pretreatment.service;

import java.util.List;

import cn.honry.base.bean.model.HandingStatus;
import cn.honry.base.bean.model.MongoLog;
import cn.honry.base.bean.model.TimingRules;
import cn.honry.base.service.BaseService;

public interface PretreatmentService extends BaseService<TimingRules>{

	/**
	 * 获取有效的、开启状态的预处理定时规则
	 * @return 
	 */
	List<TimingRules> getRulesList();
	/**
	 * 根据栏目别名、类型、开始时间获取相应的处理状态数据
	 * @param menuAlias 栏目别名
	 * @param type 类型(3年，2月，1日；)
	 * @param date 开始时间
	 * @return
	 */
	List<HandingStatus> getHandingStatus(String menuAlias,String type,String date);
	
	/**
	 * 根据栏目别名、类型获取处理失败的日志
	 * @param menuAlias 栏目别名
	 * @param type 类型
	 * @return
	 */
	List<MongoLog> getMongoLog(String menuAlias,String type);
	
	/**
	 * 根据栏目别名、类型、日期进行预处理,将结果存入mongodb中
	 * @param menuAlias 栏目别名
	 * @param type 类型(1-日;2-月; 3-年)
	 * @param date 开始日期(按日处理时,格式为:YYYY-MM-DD;按月处理时,格式为:YYYY-MM;按年处理时,YYYY)
	 */
	void mongoHanding(String menuAlias,String type,String date);
	
	/**
	 * 据栏目别名、类型、开始时间保存或修改处理状态
	 * @param menuAlias 栏目别名
	 * @param type 类型(1-日;2-月;3-年)
	 * @param date 开始时间
	 * @param state 状态
	 * @param handWay 执行方式(0-自动;1-手动)
	 */
	void saveorUpdateHandingStatus(String menuAlias,String type,String date,Integer state,Integer handWay);
	
	/**
	 * 删除三天前的执行成功的日志
	 */
	void delMongoLog();
}
