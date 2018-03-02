package cn.honry.statistics.operation.operationArrange.service;
/***
 * 手术安排统计service层
 * @Description:
 * @author: tangfeishuai
 * @CreateDate: 2016年5月30日 
 * @version 1.0
 */
import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.BusinessHospitalbed;
import cn.honry.base.bean.model.OperationArrange;
import cn.honry.base.service.BaseService;
import cn.honry.inner.vo.MenuListVO;
import cn.honry.statistics.operation.operationArrange.vo.OperationArrangeToReportSlave;
import cn.honry.statistics.operation.operationArrange.vo.OperationArrangeVo;
import cn.honry.utils.FileUtil;

@SuppressWarnings({"all"})
public interface OperationArrangeService  extends BaseService<OperationArrange>{
	/**  
	 * 
	 * 手术安排统计列表查询
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:startTime 开始时间
	 * @param:endTime结束时间
	 * @param:status手术状态
	 * @param:execDept执行科室
	 * @param:identityCard身份证号
	 * @param:page当前页数
	 * @param:rows行数
	 * @throws:
	 * @return: void
	 *
	 */
	public List<OperationArrangeVo> getOperationArrangeVo(String beganTime, String endTime,String status,String execDept,String page,String rows, String identityCard);
	/**  
	 * 
	 * 手术安排统计列表查询总记录数
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:startTime 开始时间
	 * @param:endTime结束时间
	 * @param:status手术状态
	 * @param:execDept执行科室
	 * @param:identityCard身份证号
	 * @throws:
	 * @return: void
	 *
	 */
	public int getTotal(String beganTime, String endTime,String status,String execDept, String identityCard); 
	/**
	 * @Description:手术名称map
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:
	 * @return Map<String, String>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	Map<String, String> opNameMap();
	
	/**
	 * @Description:导出列表
	 * @Description:
	 * @author: tangfeishuai
	 * @CreateDate: 2016年6月24日 
	 * @version 1.0
	**/
	FileUtil export(List<OperationArrangeVo> list, FileUtil fUtil);
	
	/**  
	 * 
	 * 导出手术计费信息汇总
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:status手术状态
	 * @param:execDept执行科室
	 * @param:identityCard身份证号
	 * @throws:
	 * @return:  List<OperationArrangeVo> 
	 *
	 */
	public List<OperationArrangeVo> getAllOperationArrangeVo(String beganTime, String endTime,String status,String execDept, String identityCard);
	/**
	 *
	 * @Description：获取病床
	 * @Author：zhangjin
	 * @CreateDate：2016年10月21日  
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0：
	 *
	 */
	public List<BusinessHospitalbed> getBedno();
	/**  
	 * 
	 * 手术安排统计报表打印
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:status手术状态
	 * @param:execDept执行科室
	 * @param:identityCard身份证号
	 * @param:page当前页数
	 * @param:rows行数
	 * @throws:
	 * @return: void
	 *
	 */
	public List<OperationArrangeToReportSlave> getOperationArrangeToReport(String startTime, String endTime, String status,
			String execDept, String identityCard);
	
	/**
	 * @Description:手术安排统计-查询科室信息
	 * @Author: zxl
	 * @CreateDate: 2017年3月1日
	 * @version: 1.0
	 */
	public List<MenuListVO> querysysDeptment(); 
}
