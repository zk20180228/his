package cn.honry.statistics.operation.operationArrange.dao;
/***
 * 手术安排统计DAO层
 * @Description:
 * @author: tangfeishuai
 * @CreateDate: 2016年5月30日 
 * @version 1.0
 */
import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.BusinessHospitalbed;
import cn.honry.base.bean.model.DrugUndrug;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.EntityDao;
import cn.honry.inner.operation.record.vo.OpNameVo;
import cn.honry.inner.vo.MenuListVO;
import cn.honry.statistics.operation.operationArrange.vo.OperationArrangeVo;

@SuppressWarnings({"all"})
public interface OperationArrangeDao extends EntityDao<OperationArrangeVo>{
	
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
	public List<OperationArrangeVo> getOperationArrangeVo(String beganTime, String endTime,String status,String execDept,String page,String rows,String identityCard); 
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
	 * @param:page当前页数
	 * @param:rows行数
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
	public List<OperationArrangeVo> getAllOperationArrangeVo(String beganTime, String endTime,String status,String execDept);
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
	 * @Description：获取手术名称
	 * @Author：zhangjin
	 * @CreateDate：2017年1月4日  
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0：
	 *
	 */
	public List<OpNameVo> getOperationItem(String opId);
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
	public List<OperationArrangeVo> getOperationArrangeVo2(String beganTime,
			String endTime, String status, String execDept, String identityCard);
	/**
	 *
	 * @Description：获取手术室
	 * @Author：zhangjin
	 * @CreateDate：2017年1月4日  
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0：
	 *
	 */
	public Map<String, String> getroomdept();
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
	public List<OperationArrangeVo> getOperationArrangeToReport(String beganTime, String endTime, String status,
			String execDept, String identityCard);
	/**
     * @Description:手术室查询 用于手术安排统计-报表数据获取
	 * @Author: hedong
	 * @CreateDate: 2017年3月1日
	 * @version: 1.0
     */
	public List<SysDepartment> likeSearchOpRoom();
	/**
	 * @Description:手术安排统计-查询科室信息
	 * @Author: zxl
	 * @CreateDate: 2017年3月1日
	 * @version: 1.0
	 */
	public List<MenuListVO> querysysDeptment(); 
	
}
