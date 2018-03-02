package cn.honry.statistics.bi.bistac.toListView.dao;

import java.util.Date;
import java.util.List;

import cn.honry.base.dao.EntityDao;
import cn.honry.statistics.bi.bistac.toListView.vo.ToListView;
import cn.honry.statistics.bi.bistac.toListView.vo.ToListViewVo;
import cn.honry.statistics.util.echartsVo.PieVo;

public interface ToListViewDao extends EntityDao<ToListViewVo>{
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
	public ToListViewVo queryVo(List<String> tnL,String date,String lastM,String lastY);
	/**
	 * 从mongodb中查询门急诊人数
	 * @Author zxh
	 * @time 2017年5月10日
	 * @param date
	 * @param lastM
	 * @param lastY
	 * @return
	 */
	public ToListViewVo queryVoByMongo(String date,String lastM,String lastY);
	/**
	 * 
	 * @param date
	 * @return
	 */
	public ToListViewVo queryVoByES(Date thisDay, Date nextDay, String staType);
	public List<PieVo> queryAreaByES(Date sTime,Date eTime);
	/**
	 * 计算同环比
	 * @param thisDay
	 * @param nextDay
	 * @param staType
	 * @return
	 */
	public ToListView querySixMomYoyByES(Date thisDay, Date nextDay, String staType);
	/**  
	 * 
	 * 获取门诊挂号：挂号主表的最大和最小时间
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月4日 下午4:09:43 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月4日 下午4:09:43  
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public ToListViewVo findMaxMin();
	
	
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
	
	void initPcRegisterDoctorWorkTotalMonthAndYear(String date,String dateSign);
	
}
