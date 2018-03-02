package cn.honry.statistics.doctor.wordLoadDoctorTotal.dao;

import java.util.List;
import java.util.Map;

import cn.honry.inner.statistics.wordLoadDoctorTotal.vo.HospitalWork;
import cn.honry.inner.vo.MenuListVO;
import cn.honry.statistics.doctor.wordLoadDoctorTotal.vo.WordLoadVO;

/**
 * 住院医生工作量统计
 * @author conglin
 *
 */
public interface WordLoadDoctorTotalDao {
	/**
	 * @see 查询住院医生工作量
	 * @param begin
	 * @param end
	 * @param dept
	 * @param doctorCode
	 * @return
	 */
	public List<WordLoadVO> queryInhosWordTotal(String begin,String end,String dept,String doctorCode);
	
/**********************************************************************************************************/
	/**
	 * 住院医生工作量
	 */
	public void init_ZYYSGZLTJ_Num(List<String> tnL,String begin,String end);
	/**
	 * 住院医生工作量 
	 */
	public void init_ZYYSGZLTJ_Total(List<String> tnL,String begin,String end);
	/**
	 * 住院医生工作量 医嘱表 
	 */
	public void init_ZYYSGZLTJ_Detail(List<String> tnL,String begin,String end);
/***************************月统计*************************************************************************************************************/
	/**
	 * 住院医生工作量 ——————》月
	 */
	public void init_ZYYSGZLTJ_Num_MoreDay(String begin,String end,String type);
	/**
	 * 住院医生工作量byOneDay 月
	 */
	public void init_ZYYSGZLTJ_Total_MoreDay(String begin,String end,String type);
	/**
	 * 住院医生工作量科室 医嘱表 月统计
	 * @param begin
	 * @param end
	 */
	public void init_ZYYSGZLTJ_Detail_MoreDay(String begin, String end,String type,String docOrDept);
/*************************************************************************************************************************/
/**
 * @see 住院医生工作明细
 * @return
 */
public List<WordLoadVO> queryForDB(String date,String dateSign);
/**
 * @see 住院医生工作数据展示 
 * @return
 */
public Map<String,Object> queryForDBList(String begin, String end,String depts,String doctors,String menuAlias,String row,String page);
/**
 * @see 住院医生工作数据展示 
 * @return
 */
public Map<String,Object> queryForOraList(List<String> tnL,String begin, String end,String depts,String doctors,String menuAlias,String row,String page);
/**
 * @see 住院医生工作数据展示 
 * @return
 */
public List<WordLoadVO> queryForOra(List<String> tnL,String begin,String end,String dateSign);
/**
 * @see 住院医生工作同比
 * @return
 */
public List<WordLoadVO> queryForDBSame(String date,String dateSign,String queryMongo);

/**
 * @see 住院医生工作环比
 * @return
 */
public List<WordLoadVO> queryForDBSque(String date,String dateSign,String queryMongo);
/**
 * @see 住院医生工作环比 查分区
 * @return
 */
public WordLoadVO queryForOraSque(List<String> tnL,String begin,String end,String dateSign);
/**
 * 科室或医生金额top5
 * @param date
 * @param dateSign
 * @return
 */
public List<WordLoadVO> queryDeptDocTopFive(String date,String dateSign,String collections,String deptOrDco);
/**
 * 科室或医生金额top5分区查
 * @param tnL
 * @param begin
 * @param end
 * @param dateSign
 * @return
 */
public List<WordLoadVO> queryDeptDocTopFive(List<String> tnL,String begin,String end,String deptOrDco);
/**
 * 获取医生
 * @param deptTypes
 * @param menuAlias
 * @return
 */
public List<MenuListVO> getDoctorList(String deptTypes, String menuAlias);

/**
 * 
 * 
 * <p>住院医生工作量查询 </p>
 * @Author: XCL
 * @CreateDate: 2017年7月13日 上午11:44:39 
 * @Modifier: XCL
 * @ModifyDate: 2017年7月13日 上午11:44:39 
 * @ModifyRmk:  
 * @version: V1.0
 * @param tnL 住院主表List
 * @param begin 开始时间
 * @param end 结束时间
 * @param menuAlias 栏目别名
 * @param depts 科室code
 * @param doctors 医生code
 * @return:List<HospitalWork>
 *
 */
public List<HospitalWork> queryHosWorkTotal(List<String> tnL,String begin,String end,String menuAlias,String depts,String doctors,String page,String rows);
/**
 * 
 * 
 * <p>查询记录总条数 </p>
 * @Author: XCL
 * @CreateDate: 2017年7月13日 下午7:06:50 
 * @Modifier: XCL
 * @ModifyDate: 2017年7月13日 下午7:06:50 
 * @ModifyRmk:  
 * @version: V1.0
 * @param tnL
 * @param begin
 * @param end
 * @param menuAlias
 * @param depts
 * @param doctors
 * @return:
 *
 */
public int queryHosWorkTotal(List<String> tnL,String begin,String end,String menuAlias,String depts,String doctors);
/**
 * 
 * 
 * <p>从预处理中查询记录 </p>
 * @Author: XCL
 * @CreateDate: 2017年7月14日 下午2:26:30 
 * @Modifier: XCL
 * @ModifyDate: 2017年7月14日 下午2:26:30 
 * @ModifyRmk:  
 * @version: V1.0
 * @param begin
 * @param end
 * @param menuAlias
 * @param depts
 * @param doctors
 * @return:
 *
 */
public Map<String,Object> queryHosWorkTotal(String begin,String end,String menuAlias,String depts,String doctors,String page,String rows);
}
