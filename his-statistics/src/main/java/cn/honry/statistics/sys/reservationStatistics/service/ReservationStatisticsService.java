package cn.honry.statistics.sys.reservationStatistics.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.RegisterPreregister;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.service.BaseService;
import cn.honry.statistics.sys.reservationStatistics.vo.ReservationStatistics;

@SuppressWarnings({"all"})
public interface ReservationStatisticsService extends BaseService<RegisterPreregister>{

	/**
	 * 
	 * <p>根据时间部门查询挂号全部 </p>
	 * @Author: wujiao
	 * @CreateDate: 2015-12-24 下午10:12:16 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017-07-03 下午6:54:16
	 * @ModifyRmk:  修改注释内容
	 * @version: V1.0
	 * @param dept 科室编号
	 * @param stime 开始时间
	 * @param etime 结束时间
	 * @return 挂号信息实体
	 * @throws:
	 *
	 */
	List<ReservationStatistics> getInfo(String dept, String stime,String etime);
	
	/**
	 * @Description 预约统计查询
	 * @author  marongbin
	 * @createDate： 2016年12月24日 上午11:41:43 
	 * @modifier zhangkui
	 * @modifyDate： 2017-07-03 下午6:54:16
	 * @param dept 科室编号
	 * @param stime 开始时间
	 * @param etime 结束时间
	 * @param page 当前页
	 * @param rows 每页显示的行
	 * @param jobNo 医院员工工作号
	 * @param menuAlias 栏目别名，和权限有关
	 * @return: List<ReservationStatistics>
	 * @modifyRmk：  修改注释
	 * @version 1.0
	 * @throws Exception 
	 */
	Map<String,Object> getInfoNow(String dept, String stime,String etime,String page,String rows,String jobNo,String menuAlias) throws Exception;
	
	
	/**  
	 *  
	 * @Description：  科室下拉框(挂号科室)
	 * @Author：wujiao
	 * @CreateDate：2015-12-25 下午05:11:49  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-25 下午05:11:49  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<SysDepartment> getComboboxdept();
	
	/**
	 * 
	 * 
	 * <p>预约统计预处理 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年8月12日 上午11:17:58 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年8月12日 上午11:17:58 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param begin
	 * @param end
	 * @param type:
	 *
	 */
	public void initReservation(String begin, String end, Integer type);

}
