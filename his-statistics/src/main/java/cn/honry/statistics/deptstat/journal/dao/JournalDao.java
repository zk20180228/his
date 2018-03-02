package cn.honry.statistics.deptstat.journal.dao;

import java.util.ArrayList;
import java.util.List;

import cn.honry.base.bean.model.DrugChecklogs;
import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.EntityDao;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.deptstat.journal.vo.JournalVo;
import cn.honry.statistics.drug.inventoryLog.vo.InventoryLogVo;

@SuppressWarnings({"all"})
public interface JournalDao {

	/** 查询科室名称list
	* @Title: queryDepts 查询科室名称list
	* @Description: 查询科室名称list
	* @param dept 科室code
	* @return List<String>
	* @author dtl 
	* @date 2017年6月5日
	*/
	List<String> queryDepts(String dept) throws Exception;

	/** <p>查询&nbsp;入院&nbsp;/&nbsp;转入&nbsp;/&nbsp;转出&nbsp;人数</p>
	*  <p>标记分别为&nbsp;1&nbsp;/&nbsp;2&nbsp;/&nbsp;3</p>
	* @Title: queryInOutNum 查询入院/转入/转出人数
	* @Description: 查询入院/转入/转出人数
	* @param sTime 开始时间
	* @param eTime 结束时间
	* @param dept 科室
	* @param i 1：入院；2：转入；3：转出
	* @return List<Integer>
	* @author dtl 
	* @date 2017年6月5日
	*/
	List<Integer> queryInOutNum(String sTime, String eTime, String dept, int i) throws Exception;


	/** <p>查询现有人数 </p>
	* @Title: queryNowNum  查询现有人数
	* @Description: 查询现有人数
	* @param time 时间参数
	* @param dept 科室参数
	* @return List<Integer>
	* @author dtl 
	* @date 2017年6月5日
	*/
	List<Integer> queryNowNum(String time, String dept) throws Exception;
	/** <p>查询&nbsp;实占床位&nbsp;/&nbsp;开放床位&nbsp;/&nbsp;加床/&nbsp;空床&nbsp;数量</p>
	*  <p>标记分别为&nbsp;1&nbsp;/&nbsp;2&nbsp;/&nbsp;3/&nbsp;4&nbsp;</p>
	* @Title: queryBedNum 查询实占床位/开放床位/加床/空床数量
	* @Description: 查询实占床位/开放床位/加床/空床数量
	* @param dept 科室参数
	* @param i 1：实占床位；2：开放床位；3：加床；4：空床
	* @return List<Integer>
	* @author dtl 
	* @date 2017年6月5日
	*/
	List<Integer> queryBedNum(String dept, int i) throws Exception;

	/** <p>查询&nbsp;危重病人&nbsp;/&nbsp;一级护理&nbsp人数</p>
	*  <p>标记分别为&nbsp;1&nbsp;/&nbsp;2</p>
	* @Title: queryCriticallyOrGrateOneNum查询危重病人/一级护理人数 
	* @Description: 查询危重病人/一级护理人数
	* @param eTime 结束时间
	* @param dept 科室参数
	* @param i 1：危重病人；2：一级护理
	* @return List<Integer>
	* @author dtl 
	* @date 2017年6月5日
	*/
	List<Integer> queryCriticallyOrGrateOneNum(String eTime, String dept, int i) throws Exception;
	/** 查询所有住院科室
	* @Title: getAllInpateintDeptCode 查询所有住院科室
	* @Description: 查询所有住院科室
	* @return List<String>  住院科室code（以逗号区分） 
	* @author dtl 
	* @date 2017年6月5日
	*/
	List<String> getAllInpateintDeptCode() throws Exception;
	/** 获得住院主表在线表中的最大出院时间
	* @Title: getMaxOutDateFromOnLineInpateint 获得住院主表在线表中的最大出院时间
	* @Description: 获得住院主表在线表中的最大出院时间
	* @return String 住院主表在线表中的最大出院时间
	* @author dtl 
	* @date 2017年6月5日
	*/
	String getMaxOutDateFromOnLineInpateint() throws Exception;

	/** 根据开始、结束时间、科室以及表名查询时间段内的出院人数
	* @Title: queryOutNum 根据开始、结束时间、科室以及表名查询时间段内的出院人数
	* @Description: 根据开始、结束时间、科室以及表名查询时间段内的出院人数
	* @param sTime 开始时间
	* @param eTime 结束时间
	* @param dept 科室
	* @param tableName 表名
	* @return List<Integer> 出院人数
	* @author dtl 
	* @date 2017年6月5日
	*/
	List<Integer> queryOutNum(String sTime, String eTime, String dept,
			String tableName) throws Exception;
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
