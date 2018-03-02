package cn.honry.inner.operation.arrange.dao;
/***
 * 手术安排统计DAO层
 * @Description:
 * @author: tangfeishuai
 * @CreateDate: 2016年5月30日 
 * @version 1.0
 */
import java.util.List;

import cn.honry.base.bean.model.DrugUndrug;
import cn.honry.base.bean.model.OperationArrange;
import cn.honry.base.dao.EntityDao;
import cn.honry.inner.operation.arrange.vo.OperationArrangeInnerVo;

@SuppressWarnings({"all"})
public interface OperationArrangeInnerDAO extends EntityDao<OperationArrange>{
	
	/**
	 * @Description:根据条件查询手术安排信息
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:beganTime 开始时间； endTime 结束时间； status 手术状态； execDept 执行科室  page 当前页数   rows 分页条数
	 * @return List<OperationArrangeVo>
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
	 *  
	 * @Description：拟手术名称下拉框的方法
	 * @Author：zhangjin
	 * @CreateDate：2016-4-8 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<DrugUndrug> getCombobox();
	
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
