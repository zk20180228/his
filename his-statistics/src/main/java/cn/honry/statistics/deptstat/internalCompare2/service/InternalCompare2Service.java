package cn.honry.statistics.deptstat.internalCompare2.service;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import cn.honry.statistics.deptstat.internalCompare2.vo.FicDeptVO;
import cn.honry.statistics.deptstat.internalCompare2.vo.InternalCompare2Vo;
import cn.honry.statistics.sys.reportForms.vo.DoctorWorkloadStatistics;
import cn.honry.statistics.sys.reportForms.vo.IncomeVo;
import cn.honry.statistics.sys.reportForms.vo.PatientInfoVo;
import cn.honry.statistics.sys.reportForms.vo.StatisticsVo;

@SuppressWarnings({"all"})
public interface InternalCompare2Service {
	/**
	 * 
	 * <p>查询医院内科医学部和内二医学部对比数据</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月3日 下午4:50:36 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月3日 下午4:50:36 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<InternalCompare2Vo>
	 *
	 */
	List<InternalCompare2Vo> queryinternalCompare2list(String timed,String Stime,String deptCode1List) throws Exception;

	/**
	 * 
	 * <p>初始化数据</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月3日 下午4:43:27 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月3日 下午4:43:27 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: void
	 *
	 */
	void initCompare2list(String string, String string2) throws Exception;

	/**
	 * 
	 * <p>导出表格</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月3日 下午4:42:59 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月3日 下午4:42:59 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: void
	 *
	 */
	void exportExcel(ServletOutputStream stream, List<InternalCompare2Vo> journalVos,String date) throws Exception;
	/**
	 * 医院内科医学部和内二医学部对比表2 预处理
	 * @param begin
	 * @param end
	 * @param type
	 */
	public void init_YYNKYXBHNEYXBDBBT(String begin, String end, Integer type) throws Exception;
}
