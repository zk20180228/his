package cn.honry.statistics.bi.bistac.mongoDataInit.dao;

import java.util.List;
import java.util.Map;


import cn.honry.base.bean.model.MongoCount;
import cn.honry.base.bean.model.MongoLog;
import cn.honry.inner.statistics.outpatientIndicatorsPrestreatment.vo.OutpatientIndicatorsVO;
import cn.honry.statistics.bi.bistac.mongoDataInit.vo.DoctorWorkCountVo;
import cn.honry.statistics.sys.reportForms.vo.StatisticsVo;

public interface MongoDataInitDao {

	/**
	 * 初始化门诊各项收入统计中的数据
	 * @param list 初始化数据
	 * @param date 日期
	 * @param type 统计类型(1-按日统计；2-按月统计；3-按年统计)
	 * 
	 */
	void init_MZGXSRTJ(List<StatisticsVo> list,String date,Integer type);
	
	/**
	 * 初始化总收入情况统计中的数据
	 * @param json 数据
	 * @param date 日期
	 * @param type 统计类型(1-按日统计；2-按月统计；3-按年统计)
	 * 
	 */
	void init_ZSRQKTJ(String json,String date,Integer type);
	/**
	 * 初始化门急诊人次统计中的数据
	 * @param json 数据
	 * @param date 日期
	 * @param type 统计类型(1-按日统计；2-按月统计；3-按年统计)
	 */
	void init_MJZRCTJ(String json,String date,Integer type);
	/**
	 * 初始化门诊收入统计中的数据
	 * 栏目别名：SRTJB（原名为：收入统计表）
	 * @param json 数据
	 * @param date 日期
	 * @param type 统计类型(1-按日统计；2-按月统计；3-按年统计)
	 */
	void init_SRTJB(String json,String date,Integer type);
	/**
	 * 初始化住院收入统计中的数据
	 * 栏目别名：ZYSRTJ
	 * @param json 数据
	 * @param date 日期
	 * @param type 统计类型(1-按日统计；2-按月统计；3-按年统计)
	 */
	void init_ZYSRTJ(String deptAndFeeData,String tonghuanbiData,String pcIncome,String date,Integer type);
	/** 根据栏目别名获取栏目的预处理设置
	* @Title: getMongoCount 
	* @Description: 
	* @param menuType
	* @return
	* @return MongoCount    返回类型 
	* @throws 
	* @author mrb
	* @date 2017年6月20日
	*/
	MongoCount getMongoCount(String menuType);
	/** 挂号科室工作量统计（天-大数据）
	* @Title: init_GHKSGZLTJ_D_D 
	* @Description: 
	* @param stime
	* @param etime
	* @return void    返回类型 
	* @throws 
	* @author mrb
	* @date 2017年6月21日
	*/
	void init_GHKSGZLTJ_D_D(String stime,String etime,String type,Map<String, String> map,String date);
	/** 挂号科室工作量统计（天-SQL）
	* @Title: init_GHKSGZLTJ_D_D 
	* @Description: 
	* @param stime
	* @param etime
	* @param type
	* @return
	* @return Integer    返回类型 
	* @throws 
	* @author mrb
	* @date 2017年6月21日
	*/
	void init_GHKSGZLTJ_D_S(String stime,String etime,String type,Map<String, String> map);
	/** 保存预处理日志
	* @Title: saveMongoLog 
	* @Description: 
	* @param log
	* @return void    返回类型 
	* @throws 
	* @author mrb
	* @date 2017年6月21日
	*/
	void saveMongoLog(MongoLog log);
	/** 获取执行失败的日志
	* @Title: findMongoLog 
	* @Description: 
	* @param menuType
	* @return
	* @return List<MongoLog>    返回类型 
	* @throws 
	* @author mrb
	* @date 2017年6月22日
	*/
	List<MongoLog> findMongoLog(String menuType);
	/** 
	* @Title: inserDoc 
	* @Description: 
	* @param sTime 开始时间
	* @param eTime 结束时间
	* @param date 作为字段的日期
	* @return void    返回类型 
	* @throws 
	* @author mrb
	* @date 2017年6月23日
	*/
	void inserDoc(String sTime,String eTime,String date,String collectionName,String inserCollName,Map<String,String> deptmap);
	/**  
	 * 
	 * <p>门诊各项指标统计--mongoDB文档插入 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月10日 上午10:09:10 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月10日 上午10:09:10 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param date
	 * @param insertCollectionName
	 * @param list
	 * @throws:
	 * @return: void
	 *
	 */
	void insertDocMZGXZBTJ(String date,String insertCollectionName,List<OutpatientIndicatorsVO> list);
	/**  
	 * 
	 * <p>从mongoDB中取数据 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月10日 下午2:58:11 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月10日 下午2:58:11 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param sDate
	 * @param eDate
	 * @param collectionName
	 * @throws:
	 * @return: void
	 *
	 */
	List<OutpatientIndicatorsVO> queryFromMongo(String sDate,String eDate,String collectionName);
	
	
	/**
	 * 
	 * <p>医生工作量统计 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年7月17日 下午3:55:10 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年7月17日 下午3:55:10 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param type 类型，根据他分别将数据存放入对应的集合中，集合分年月日集合
	 * @throws:
	 *
	 */
	public void init_DocterWorkCount(DoctorWorkCountVo vo, Integer type);
	/**
	 * 门诊医生、科室工作量(移动端)初始化预处理
	 */
	void init_MZGZL(String beginDate,String endDate);
}
