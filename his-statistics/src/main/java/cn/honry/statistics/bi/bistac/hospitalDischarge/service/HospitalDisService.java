package cn.honry.statistics.bi.bistac.hospitalDischarge.service;

import java.util.List;
import java.util.Map;

import cn.honry.statistics.bi.bistac.hospitalDischarge.vo.HospitalDisChargeVo;
import cn.honry.statistics.bi.bistac.hospitalDischarge.vo.HospitalDisVo;

/**
 * @see 住院出院人次
 * @author conglin
 *
 */
public interface HospitalDisService {
	/**
	 *  HB 环比 TB 同比
	 * @param date
	 * @param dateSign
	 * @param type
	 * @return
	 */
	public List<HospitalDisVo> querySameOrSque(String date,String dateSign,String type);
	/**
	 * 数据展示
	 */
	public List<Map<String,HospitalDisVo>> queryDate(String date,String dateSign);
	/**
	 * 饼图
	 * @param date
	 * @param dateSign
	 * @return
	 */
	public List<HospitalDisVo> queryPiechat(String date,String dateSign);
	/**
	 * 自定义查询 住院出院人次
	 * @param begin
	 * @param end
	 * @return
	 */
	public List<HospitalDisVo> queryFeelHos(String begin,String end);
	/**
	 * 
	 * 
	 * <p>临时住出院人次统计 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年8月3日 下午3:40:06 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年8月3日 下午3:40:06 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param beginDate
	 * @param endDate
	 * @param type:
	 *
	 */
	public void init_ZCYRCTj(String beginDate, String endDate, Integer type);
	
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
	public HospitalDisChargeVo queryInOutList(String begin,String end,String dateSign);
}
