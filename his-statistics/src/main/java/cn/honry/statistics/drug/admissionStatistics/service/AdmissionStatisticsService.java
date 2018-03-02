package cn.honry.statistics.drug.admissionStatistics.service;

import java.util.List;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.service.BaseService;
import cn.honry.statistics.drug.admissionStatistics.vo.AdmissionStatisticsVo;
import cn.honry.utils.FileUtil;
/***
 * 用药统计service层
 * @Description:
 * @author: tangfeishuai
 * @CreateDate: 2016年6月22日 
 * @version 1.0
 */
@SuppressWarnings({"all"})
public interface AdmissionStatisticsService extends BaseService<AdmissionStatisticsVo>{
	/**
	 * @Description:根据条件查询用药信息
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月22日
	 * @param:beganTime 开始时间； endTime 结束时间； status 手术状态； deptCode 取药药房；
	 * storageCode 领药科室；drugType 药品类别；outType 出库类别；  page 当前页数 ；  rows 分页条数
	 * @return List<AdmissionStatisticsVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	List<AdmissionStatisticsVo> getAdmissionStatisticsVo(String beginTime,String endTime,String deptCode,String storageCode,
			String drugType,String outType,String page,String rows) throws Exception;
	
	/**
	 * @Description:根据条件查询用药信息
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月22日
	 * @param:beganTime 开始时间； endTime 结束时间； status 手术状态； deptCode 取药药房；
	 * storageCode 领药科室；drugType 药品类别；outType 出库类别；  page 当前页数 ；  rows 分页条数
	 * @return int
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	int getTotal(String beginTime,String endTime,String deptCode,String storageCode,
			String drugType,String outType) throws Exception;

		
	/**
	 * @Description:查询所有的药房
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月23日
	 * @param:
	 * @return 
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	List<SysDepartment> getSysDepartment() throws Exception;
	
	/**
	 * @Description:导出列表
	 * @Description:
	 * @author: tangfeishuai
	 * @CreateDate: 2016年6月24日 
	 * @version 1.0
	**/
	FileUtil export(List<AdmissionStatisticsVo> list, FileUtil fUtil) throws Exception;
	
	/**
	 * @Description:得到全部的记录数用于导出
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月22日
	 * @param:beganTime 开始时间； endTime 结束时间； status 手术状态； deptCode 取药药房；
	 * storageCode 领药科室；drugType 药品类别；outType 出库类别
	 * @return List<AdmissionStatisticsVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	List<AdmissionStatisticsVo> getAllAdmissionStatisticsVo(String beginTime,String endTime,String deptCode,String storageCode,
			String drugType,String outType) throws Exception;
	
	
}
