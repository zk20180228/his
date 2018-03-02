package cn.honry.statistics.bi.bistac.toListView.service;

import java.util.List;
import java.util.Map;

import cn.honry.statistics.bi.bistac.toListView.vo.ToListView;
import cn.honry.statistics.bi.bistac.toListView.vo.ToListViewVo;
import cn.honry.statistics.util.echartsVo.PieVo;

public interface ToListViewService {
	/**  
	 * 
	 * 门急诊人次统计
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月5日 下午2:38:18 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月5日 下午2:38:18 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public ToListViewVo queryVo(String date);
	/**
	 * 从mongodb中取门急诊人数
	 * @Author zxh
	 * @time 2017年5月10日
	 * @param date
	 * @return
	 */
	public ToListViewVo queryVoByMongo(String date);
	/**
	 * elasticsearch实现
	 * @param date
	 * @return
	 */
	ToListViewVo queryVoByES(String date, String staType);
	List<PieVo> queryAreaByES(String date, String staType);
	/**
	 * 6为单位的同环比
	 * @param date
	 * @param staType
	 * @return
	 */
	List<ToListView> querySixMomYoy(String date, String staType);
	
	/**
	 * 从mongodb中获取门急诊人次统计的数据
	 * @param date 日期
	 * @param dateSign 统计类型(1-按日统计；2-按月统计；3-按年统计)
	 * @return
	 */
	Map<String, Object> queryVoByMongo(String date,String dateSign);
	
	
	/**
	 * 
	 * 
	 * <p>预处理PC端门诊医生工作量统计</p>
	 * @Author: XCL
	 * @CreateDate: 2018年1月27日 下午3:09:46 
	 * @Modifier: XCL
	 * @ModifyDate: 2018年1月27日 下午3:09:46 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param date
	 * @param dateSign:
	 *
	 */
	void initPcRegisterDoctorWorkTotal(String date,String dateSign);

}
