package cn.honry.statistics.sys.reservationStatistics.dao;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.RegisterGrade;
import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.base.bean.model.RegisterPreregister;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.EntityDao;
import cn.honry.statistics.sys.reservationStatistics.vo.RegGradeVo;
import cn.honry.statistics.sys.reservationStatistics.vo.ReservationStatistics;

@SuppressWarnings({"all"})
public interface ReservationStatisticsDao extends EntityDao<RegisterPreregister>{

	
	
	
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
	List<RegisterInfo> findinfo(String dept, String stime,String etime);
	
	/**  
	 *  
	 * @Description：  查询科室部门
	 * @Author：wujiao
	 * @CreateDate：2015-12-24 下午10:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<SysDepartment> finddept();
	
	/**  
	 *  
	 * @Description：  查询预约挂号
	 * @Author：wujiao
	 * @CreateDate：2015-12-24 下午10:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param etime  开始时间
	 * @param stime 结束时间
	 * @param dept 部门编号
	 *
	 */
	List<RegisterPreregister> findregister(String dept, String stime,String etime);
	
	/**  
	 *  
	 * @Description：  查询挂号级别
	 * @Author：wujiao
	 * @CreateDate：2015-12-24 下午10:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param string 
	 *
	 */
	List<RegisterGrade> findgrade(String string);
	
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
	 * @Description 查询所有挂号级别
	 * @author  marongbin
	 * @createDate： 2016年12月24日 下午2:01:19 
	 * @modifier 
	 * @modifyDate：
	 * @return: List<RegGradeVO>
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<RegGradeVo> getRegGrade();
	
	/**
	 * @Description 预约统计查询
	 * @author  marongbin
	 * @createDate： 2016年12月24日 下午2:03:43 
	 * @modifier zhangkui
	 * @modifyDate：2017-07-03 19:06:43 
	 * @param regGradeVO 所有挂号级别的集合
	 * @param tnl T_REGISTER_PREREGISTER 预约挂号表分区表集合
	 * @param T_REGISTER_MAIN 挂号主表分区表集合
	 * @param dept 部门编号
	 * @param stime 开始时间
	 * @param etime 结束时间
	 * @param page 当前页
	 * @param rows 每页显示的行
	 * @param jobNo 医院员工工作号
	 * @return: List<ReservationStatistics> vo
	 * @modifyRmk：  修改注释内容
	 * @version 1.0
	 */
	List<ReservationStatistics> getInfoNow(List<RegGradeVo> regGradeVO,List<String> pretnl,List<String> T_REGISTER_MAIN_NOW,String dept, String stime,String etime,String page,String rows,String jobNo,String menuAlias);

	/**
	 * 
	 * <p>预约统计查询获取总记录数</p>
	 * @author  marongbin
	 * @createDate： 2016年12月24日 下午2:03:43 
	 * @modifier zhangkui
	 * @modifyDate：2017-07-03 19:06:43 
	 * @param regGradeVO 所有挂号级别的集合
	 * @param tnl T_REGISTER_PREREGISTER 预约挂号表分区表集合
	 * @param T_REGISTER_MAIN 挂号主表分区表集合
	 * @param dept 部门编号
	 * @param stime 开始时间
	 * @param etime 结束时间
	 * @param page 当前页
	 * @param rows 每页显示的行
	 * @param jobNo 医院员工工作号
	 * @return: List<ReservationStatistics> vo
	 * @modifyRmk：  修改注释内容
	 * @version 1.0
	 *
	 */
	int getTotal(List<RegGradeVo> regGradeVO,List<String> pretnl,List<String> maintnl,String dept, String stime,String etime,String jobNo,String menuAlias);

	/**
	 * 
	 * 
	 * <p>从预处理中查询预约统计 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年8月12日 上午11:38:19 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年8月12日 上午11:38:19 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param dept 科室
	 * @param time 
	 * @param etime
	 * @param page
	 * @param rows
	 * @param menuAlias
	 * @return
	 * @throws Exception:
	 *
	 */
	Map<String,Object> getInfoNow(String dept,String time,String etime,String page,String rows,String menuAlias) throws Exception;

}
