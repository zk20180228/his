package cn.honry.inner.system.pretreatment.dao;

import java.util.List;

import cn.honry.base.bean.model.HandingStatus;
import cn.honry.base.bean.model.MongoLog;
import cn.honry.base.bean.model.TimingRules;
import cn.honry.base.dao.EntityDao;

/**
 * 预处理接口DAO
 * @author user
 *
 */
public interface PretreatmentDao extends EntityDao<TimingRules>{

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
	 * 删除三天前的执行成功的日志
	 */
	void delMongoLog();
}
