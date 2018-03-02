package cn.honry.inner.system.pretreatment.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.HandingStatus;
import cn.honry.base.bean.model.MongoLog;
import cn.honry.base.bean.model.TimingRules;
import cn.honry.inner.statistics.handing.service.InnerHandingService;
import cn.honry.inner.system.pretreatment.dao.PretreatmentDao;
import cn.honry.inner.system.pretreatment.service.PretreatmentService;

@Transactional
@Service("pretreatmentService")
@SuppressWarnings({"all"})
public class PretreatmentServiceImpl implements PretreatmentService {

	@Autowired
	@Qualifier(value = "pretreatmentDao")
	private PretreatmentDao pretreatmentDao;
	
	@Autowired
	@Qualifier(value = "innerHandingService")
	private InnerHandingService innerHandingService;
	
	@Override
	public TimingRules get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(TimingRules arg0) {
		
	}

	/**
	 * 获取有效的、开启状态的预处理定时规则
	 * @return 
	 */
	public List<TimingRules> getRulesList(){
		return pretreatmentDao.getRulesList();
	}
	
	/**
	 * 根据栏目别名、类型、开始时间获取相应的处理状态数据
	 * @param menuAlias 栏目别名
	 * @param type 类型(3年，2月，1日；)
	 * @param date 开始时间
	 * @return
	 */
	public List<HandingStatus> getHandingStatus(String menuAlias,String type,String date){
		return pretreatmentDao.getHandingStatus(menuAlias, type, date);
	}
	
	/**
	 * 根据栏目别名、类型获取处理失败的日志
	 * @param menuAlias 栏目别名
	 * @param type 类型
	 * @return
	 */
	public List<MongoLog> getMongoLog(String menuAlias,String type){
		return pretreatmentDao.getMongoLog(menuAlias, type);
	}
	
	/**
	 * 删除三天前的执行成功的日志
	 */
	public void delMongoLog(){
		pretreatmentDao.delMongoLog();
	}
	
	/**
	 * 据栏目别名、类型、开始时间保存或修改处理状态
	 * @param menuAlias 栏目别名
	 * @param type 类型(1-日;2-月;3-年)
	 * @param date 开始时间
	 * @param state 状态
	 * @param handWay 执行方式(0-自动;1-手动)
	 */
	public void saveorUpdateHandingStatus(String menuAlias,String type,String date,Integer state,Integer handWay){
		List<HandingStatus> list = getHandingStatus(menuAlias, type, date);
		if(list!=null && list.size()>0){
			HandingStatus handingStatus = list.get(0);
			handingStatus.setState(state);
		}else{
			HandingStatus handingStatus= new HandingStatus();
			handingStatus.setMenuAlias(menuAlias);
			handingStatus.setType(type);
			handingStatus.setBegainTime(date);
			handingStatus.setEndTime(date);
			handingStatus.setState(state);
			handingStatus.setHandWay(handWay);
			pretreatmentDao.save(handingStatus);
		}
	}
	
	/**
	 * 根据栏目别名、类型、日期进行预处理,将结果存入mongodb中
	 * @param menuAlias 栏目别名
	 * @param type 类型(1-日;2-月; 3-年)
	 * @param date 开始日期(格式为:YYYY-MM-DD)
	 */
	public void mongoHanding(String menuAlias,String type,String date){
		//需要执行的预处理方法
		innerHandingService.handing(menuAlias, type, date);
	}

}
