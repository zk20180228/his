package cn.honry.inner.operation.arrange.service;
/***
 * 手术安排统计service层
 * @Description:
 * @author: tangfeishuai
 * @CreateDate: 2016年5月30日 
 * @version 1.0
 */
import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.OperationArrange;
import cn.honry.base.service.BaseService;
import cn.honry.inner.operation.arrange.vo.OperationArrangeInnerVo;
import cn.honry.utils.FileUtil;

@SuppressWarnings({"all"})
public interface OperationArrangeInnerService  extends BaseService<OperationArrange>{
	/**
	 * @Description:根据条件查询手术安排信息
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:beganTime 开始时间； endTime 结束时间； status 手术状态； execDept 执行科室  page 当前页数   rows 分页条数
	 * @return List<OperationCostVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	
	public List<OperationArrangeInnerVo> getOperationArrangeVo(String beganTime, String endTime,String status,String execDept,String page,String rows);
	/**
	 * @Description:根据条件查询手术安排信息总记录数
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:beganTime 开始时间； endTime 结束时间； status 手术状态； execDept 执行科室  page 当前页数   rows 分页条数
	 * @return List<OperationArrangeVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	public int getTotal(String beganTime, String endTime,String status,String execDept); 
	/**
	 * @Description:手术名称map
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:
	 * @return List<OperationArrangeVo>
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
	FileUtil export(List<OperationArrangeInnerVo> list, FileUtil fUtil);
	
	/**
	 * @Description:根据条件查询所有手术安排信息
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月27日
	 * @param:beganTime 开始时间； endTime 结束时间； status 手术状态； execDept 执行科室  page 当前页数   rows 分页条数
	 * @return List<OperationArrangeVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	public List<OperationArrangeInnerVo> getAllOperationArrangeVo(String beganTime, String endTime,String status,String execDept); 
}
