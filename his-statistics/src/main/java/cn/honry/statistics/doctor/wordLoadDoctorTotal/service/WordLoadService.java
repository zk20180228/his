package cn.honry.statistics.doctor.wordLoadDoctorTotal.service;

import java.util.List;
import java.util.Map;

import cn.honry.inner.statistics.wordLoadDoctorTotal.vo.HospitalWork;
import cn.honry.inner.vo.MenuListVO;
import cn.honry.statistics.doctor.wordLoadDoctorTotal.vo.WordLoadVO;

public interface WordLoadService {
	/**
	 * @see 查询住院医生工作量
	 * @param begin
	 * @param end
	 * @param dept
	 * @param doctorCode
	 * @return
	 */
	public List<WordLoadVO> queryInhosWordTotal(String begin,String end,String dept,String doctorCode);
/****************************************************************************************************************/
	/**
	 * @see 住院医生工作明细
	 * @return
	 */
	public List<WordLoadVO> queryForDB(String date,String dateSign);
	/**
	 * @see 住院医生工作数据展示
	 * @return
	 */
	public Map<String,Object> queryForDBList(String begin,String end,String depts,String doctors,String menuAlias,String rows,String page);
	/**
	 * @see 住院医生工作同比
	 * @return
	 */
	public List<WordLoadVO> queryForDBSame(String date,String dateSign);
	/**
	 * @see 住院医生工作环比
	 * @return
	 */
	public List<WordLoadVO> queryForDBSque(String date,String dateSign);
	/**
	 * 住院科室医生TOP 
	 * @param date
	 * @param dateSign
	 * @param docOrDept
	 * @return
	 */
	public List<WordLoadVO> queryTop(String date,String dateSign,String docOrDept);
	/**
	 * 查询医生
	 * @param deptTypes
	 * @param menuAlias
	 * @return
	 */
	public List<MenuListVO> getDoctorList(String deptTypes,String menuAlias);
	/**
	 * 住院医生工作量 初始化
	 * @param begin
	 * @param end
	 * @param type
	 */
	public void init_ZYYSGZLTJ(String begin,String end,Integer type);
	/**
	 * 住院收入情况
	 * @param begin
	 * @param end
	 * @param type
	 */
	public void init_ZYSRQK(String begin,String end,Integer type);
	
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
	public List<HospitalWork> queryHosWorkTotal(String begin,String end,String menuAlias,String depts,String doctors,String page,String rows);

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
	public int queryHosWorkTotal(String begin,String end,String menuAlias,String depts,String doctors);
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
	public Map<String,Object> queryHosWorkMap(String begin,String end,String menuAlias,String depts,String doctors,String page,String rows);
}
