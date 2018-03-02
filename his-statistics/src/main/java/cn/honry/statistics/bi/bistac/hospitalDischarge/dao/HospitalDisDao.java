package cn.honry.statistics.bi.bistac.hospitalDischarge.dao;

import java.util.List;
import java.util.Map;

import cn.honry.statistics.bi.bistac.hospitalDischarge.vo.HospitalDisChargeVo;
import cn.honry.statistics.bi.bistac.hospitalDischarge.vo.HospitalDisVo;

/**
 * @see 住院出院人次统计
 * @author conglin
 *
 */
public interface HospitalDisDao {
	/**
	 * 同环比数据查询
	 * @param date
	 * @param dateSign
	 * @param searchTime
	 * @return
	 */
	public HospitalDisVo querySameOrSque(String date, String dateSign,String searchTime);
	/**
	 * 同环比数据查询走sql
	 * @param date
	 * @param dateSign
	 * @param searchTime
	 * @return
	 */
	public HospitalDisVo querySameOrSque(List<String> tnL, String dateSign,String begin,String end);
	/**
	 * 住院出院人次饼状图
	 * @param date
	 * @param dateSign
	 * @return
	 */
	public List<HospitalDisVo> queryHospitalDis(String date,String dateSign);
	/**
	 * 自定义查询 
	 * @param begin 开始时间
	 * @param end 结束时间
	 * @return List<HospitalDisVo>
	 */
	public List<HospitalDisVo> queryFeelHos(String begin,String end);
	/**
	 * 自定义查询 
	 * @param begin 开始时间
	 * @param end 结束时间
	 * @param tnL 分区表
	 * @return List<HospitalDisVo>
	 */
	public List<HospitalDisVo> queryFeelHos(List<String> tnL,String begin,String end);
	/**
	 * 年月日 数据展示
	 * @param date
	 * @param dateSign
	 * @return
	 */
	public Map<String,HospitalDisVo> queryHospitalDisList(String date,String dateSign);
	/**
	 * 列表数据展示
	 * @param date
	 * @param dateSign
	 * @return
	 */
	public List<HospitalDisVo> queryHospitalDisList(List<String> tnL,String begin,String end);
	/**
	 * 
	 * 
	 * <p>住出院人次统计 院长查询综合日报 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年11月1日 下午5:30:13 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年11月1日 下午5:30:13 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param begin
	 * @param end
	 * @return:
	 *
	 */
	public HospitalDisChargeVo queryInOutList(String begin,String end);
}
