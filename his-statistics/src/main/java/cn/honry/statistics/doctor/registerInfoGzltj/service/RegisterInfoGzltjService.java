package cn.honry.statistics.doctor.registerInfoGzltj.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.inner.vo.MenuListVO;
import cn.honry.statistics.doctor.registerInfoGzltj.vo.DoctorVo;
import cn.honry.statistics.doctor.registerInfoGzltj.vo.RegisterInfoGzltjVo;

public interface RegisterInfoGzltjService{

	/**  
	 *  
	 * @Description： 查询工作量列表
	 * @param 
	 * @Author：zpty
	 * @CreateDate：2015-8-27  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws ParseException
	 */
	List<RegisterInfoGzltjVo> findInfo(String Stime,String Etime,String dept,String expxrt)throws ParseException;
	
	/**  
	 *  
	 * @Description：  修改
	 * @param id
	 * @Author：liudelin
	 * @CreateDate：2015-6-24 下午05:45:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	void registerTriageSave(String id, String expxrt);
	
	/**  
	 *  
	 * @Description： 查询
	 * @Author：liudelin
	 * @CreateDate：2015-6-25 下午01:44:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	RegisterInfo queryRegisterTiragegl(String sEncode);
	
	/**  
	 * 
	 * <p> 挂号医生工作量统计 </p>
	 * @Author: aizhonghua
	 * @CreateDate: 2016年11月29日 下午03:58:02 
	 * @Modifier: aizhonghua
	 * @ModifyDate: 2016年11月29日 下午03:58:02 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:stime开始时间etime结束时间dept科室编码expxrt医生编码
	 *
	 */
	List<RegisterInfoGzltjVo> statRegDorWorkload(String stime,String etime,String dept,String expxrt)throws Exception;
	
	/**  
	 * 
	 * <p> 挂号医生工作量统计 </p>
	 * @Author: aizhonghua
	 * @CreateDate: 2016年11月29日 下午03:58:02 
	 * @Modifier: aizhonghua
	 * @ModifyDate: 2016年11月29日 下午03:58:02 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:stime开始时间etime结束时间dept科室编码expxrt医生编码
	 *
	 */
	Map<String,Object> statRegDorWork(String stime,String etime,String dept,String expxrt,String page,String rows,String menuAlia)throws Exception;
	
	/**
	 * 从mongo中获取数据
	 * @Author zxh
	 * @time 2017年5月15日
	 * @param stime
	 * @param etime
	 * @param dept
	 * @param expxrt
	 * @param page
	 * @param rows
	 * @throws Exception
	 * @return
	 */
	Map<String,Object> statRegDorWorkByMongo(String stime,String etime,String dept,String expxrt,String page,String rows,String menuAlias)throws Exception;
	
	/**
	 * 挂号医生工作量统计 elasticsearch实现
	 * @Author: 朱振坤
	 * @param stime 查询开始时间 以“reg_date”为查询字段
	 * @param etime 查询结束时间 包括当日
	 * @param dept 科室id字符串，多个id以“,”隔开
	 * @param expxrt 医生id字符串，多个id以“,”隔开
	 * @param page easyUi分页参数
	 * @param rows easyUi分页参数
	 * @param menuAlias 权限别名
	 * @return 封装easyUi表格的json数据的集合
	 */
	Map<String, Object> statRegDorWorkByES(String stime,String etime,String dept,String expxrt,String page,String rows, String menuAlias);
	
	/**
	 * @Description 根据科室code集合获取医生
	 * @author  marongbin
	 * @createDate： 2017年2月14日 上午9:30:06 
	 * @modifier 
	 * @modifyDate：
	 * @param deptCodes
	 * @return: List<SysEmployee>
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<DoctorVo> getDoctorBydeptCodes(String deptCodes);
	
	/**
	 * @Description 查询所有医生
	 * @author  gaotiantian
	 * @createDate： 2017-4-20 上午9:29:24 
	 * @modifier 
	 * @modifyDate：
	 * @param 
	 * @return: List<DoctorVO>
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<MenuListVO> getDoctorList(String deptTypes,String menuAlias);
	
	/** 查询挂号科室工作量统计
	* @Title: getRegisterDeptInfi 
	* @Description: 
	* @param stime
	* @param etime
	* @param deptCodes
	* @param menuAlia
	* @return
	* @return List<RegisterInfoVo>    返回类型 
	* @throws Exception
	* @author mrb
	* @date 2017年6月24日
	*/
	Map<String,Object> getRegisterDeptInfo(String stime,String etime,String deptCodes,String page,String rows)throws Exception;
	
	/**
	 * 
	 * 
	 * <p>门诊挂号量统计与移动端挂号量对应</p>
	 * @Author: XCL
	 * @CreateDate: 2018年1月26日 下午7:51:30 
	 * @Modifier: XCL
	 * @ModifyDate: 2018年1月26日 下午7:51:30 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param date
	 * @param dateSign
	 * @return:
	 * @throws Exception 
	 *
	 */
	String queryRegisterCharts(String date,String dateSign) throws Exception;

}
