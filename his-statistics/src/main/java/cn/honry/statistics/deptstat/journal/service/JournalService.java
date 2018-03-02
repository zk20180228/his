package cn.honry.statistics.deptstat.journal.service;

import java.io.IOException;
import java.util.List;

import cn.honry.base.bean.model.DrugChecklogs;
import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.service.BaseService;
import cn.honry.statistics.deptstat.journal.vo.JournalVo;
import cn.honry.statistics.drug.inventoryLog.vo.InventoryLogVo;
import cn.honry.utils.FileUtil;

@SuppressWarnings({"all"})
public interface JournalService {

	/** 查询住院日报
	* @Title: queryJournals 查询住院日报
	* @Description: 查询住院日报
	* @param time 时间
	* @param dept 科室
	* @return List<JournalVo>
	* @author dtl 
	* @date 2017年6月5日
	*/
	List<JournalVo> queryJournals(String time, String dept) throws Exception;
	
	/** 查询所有住院科室
	* @Title: getAllInpateintDeptCode 查询所有住院科室
	* @Description: 查询所有住院科室
	* @return String  住院科室code（以逗号区分） 
	* @author dtl 
	* @date 2017年6月5日
	*/
	String getAllInpateintDeptCode() throws Exception;
	
	/** <p>根据开始、结束时间向MongoDB初始化数据</p>
	*   <p>此方法也可作为数据同步使用</p>
	* @Title: toMongoDB 根据开始、结束时间向MongoDB初始化数据
	* @Description: 根据开始、结束时间向MongoDB初始化数据
	* @param sTime 开始时间
	* @param eTime 结束时间
	* @return boolean 是否成功
	* @author dtl 
	* @date 2017年6月5日
	*/
	boolean toMongoDB(String sTime, String eTime) throws Exception;
	/** 根据时间从MongoDB取数据
	* @Title: fromMongoDB 根据时间从MongoDB取数据
	* @Description: fromMongoDB 根据时间从MongoDB取数据
	* @param time 时间
	* @param dept 科室
	* @return List<JournalVo>
	* @author dtl 
	* @date 2017年6月5日
	*/
	List<JournalVo> fromMongoDB(String time, String dept) throws Exception;

	/** 通过日志集合得到合计记录
	* @Title: sumVo 通过日志集合得到合计记录
	* @Description: 通过日志集合得到合计记录
	* @param list 日志集合
	* @return JournalVo  到合计记录
	* @author dtl 
	* @date 2017年6月6日
	*/
	JournalVo sumVo(List<JournalVo> list) throws Exception;

	/** 导出记录
	* @Title: export 导出记录
	* @Description: 导出记录 
	* @param journalVos 日报明细集合
	* @param fUtil 文件工具类
	* @return 文件工具类
	* @return FileUtil    文件工具类
	* @throws  IOException
	* @author dtl 
	* @date 2017年6月6日
	*/
	FileUtil export(List<JournalVo> journalVos, FileUtil fUtil) throws IOException;
	
	/**
	 * 
	 * 
	 * <p>从住院日报维护表中查询住院日报数据 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月12日 下午4:13:23 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月12日 下午4:13:23 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param begin 开始时间
	 * @param end 结束时间
	 * @param depts 科室
	 * @param menuAlias 栏目别名
	 * @param campus 院区
	 * @return: List<JournalVo>
	 *
	 */
	List<JournalVo> queryDayReport(String begin,String depts,String menuAlias,String campus) throws Exception;
	
}
