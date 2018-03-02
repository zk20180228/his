package cn.honry.statistics.operation.operationBillingInfo.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.inner.vo.MenuListVO;
import cn.honry.statistics.operation.operationBillingInfo.dao.OperationBillingInfoDao;
import cn.honry.statistics.operation.operationBillingInfo.service.OperationBillingInfoService;
import cn.honry.statistics.operation.operationBillingInfo.vo.OperationBillingInfoVo;
import cn.honry.utils.CommonStringUtils;
import cn.honry.utils.DateUtils;
import cn.honry.utils.FileUtil;

/**   
*  手术计费信息汇总service实现类
* @className：OperationBillingInfoService
* @description：  手术计费信息Service实现层
* @author：tangfeishuai
* @createDate：2016-5-30 上午10:52:19  
* @modifier：tangfeishuai
* @modifyDate：2015-5-30 上午10:52:19  
* @modifyRmk：  
* @version 1.0
 */
@Service("operationBillingInfoService")
@Transactional
@SuppressWarnings({ "all" })
public class OperationBillingInfoServiceImpl implements OperationBillingInfoService{
	
	/** 
	 * 手术计费信息dao
	 */
	@Autowired
	@Qualifier(value = "operationBillingInfoDao")
	private OperationBillingInfoDao operationBillingInfoDao;
	@Override
	public OperationBillingInfoVo get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(OperationBillingInfoVo arg0) {
		
	}
	/**
	 * 根据条件查询手术计费信息
	 * @Description:根据条件查询手术计费信息
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:beginTime 开始时间   endTime 结束时间   opStatus 手术状态   execDept 执行科室  feeBegTime 批费开始时间  feeEndTime 批费结束时间
	 * feeState 批费状态  inState 在院状态  opDoctor 手术医生  opDoctordept 手术以医生科室  page 当前页数   rows 分页条数  identityCard身份证号
	 * @return List<OperationBillingInfoVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public List<OperationBillingInfoVo> getOperationBillingInfoVo(String beginTime, String endTime, String opStatus,
			String execDept, String feeBegTime, String feeEndTime, String feeStates, String inState, String opDoctor,
			String opDoctordept, String page, String rows, String identityCard) {
		return operationBillingInfoDao.getOperationBillingInfoVo(beginTime, endTime, opStatus, execDept, feeBegTime, feeEndTime, feeStates, inState, opDoctor, opDoctordept, page, rows,identityCard);
	}

	/**
	 * 根据条件查询手术计费信息得到总记录数
	 * @Description:根据条件查询手术计费信息得到总记录数
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:beginTime 开始时间   endTime 结束时间   opStatus 手术状态   execDept 执行科室  feeBegTime 批费开始时间  feeEndTime 批费结束时间
	 * feeStates 批费状态  inState 在院状态  opDoctor 手术医生  opDoctordept 手术以医生科室  identityCard身份证号
	 * @return List<OperationBillingInfoVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public int getTotal(String beginTime, String endTime, String opStatus, String execDept, String feeBegTime,
			String feeEndTime, String feeStates, String inState, String opDoctor, String opDoctordept, String identityCard) {
		return operationBillingInfoDao.getTotal(beginTime, endTime, opStatus, execDept, feeBegTime, feeEndTime, feeStates, inState, opDoctor, opDoctordept, identityCard);
	}

	/**
	 * 根据条件查询所有手术计费信息
	 * @Description:根据条件查询所有手术计费信息
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:beginTime 开始时间   endTime 结束时间   opStatus 手术状态   execDept 执行科室  feeBegTime 批费开始时间  feeEndTime 批费结束时间
	 * feeStates 批费状态  inState 在院状态  opDoctor 手术医生  opDoctordept 手术以医生科室  identityCard身份证号
	 * @return List<OperationBillingInfoVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public List<OperationBillingInfoVo> getAllOperationBillingInfoVo(String beginTime, String endTime, String opStatus,
			String execDept, String feeBegTime, String feeEndTime, String feeStates, String inState, String opDoctor,
			String opDoctordept, String identityCard) {
		return operationBillingInfoDao.getAllOperationBillingInfoVo(beginTime, endTime, opStatus, execDept, feeBegTime, feeEndTime, feeStates, inState, opDoctor, opDoctordept, identityCard);
	}

	/**
	 * @Description:导出列表
	 * @Description:
	 * @author: tangfeishuai
	 * @CreateDate: 2016年6月24日 
	 * @version 1.0
	**/
	@Override
	public FileUtil export(List<OperationBillingInfoVo> list, FileUtil fUtil) {
		for (OperationBillingInfoVo model : list) {
			String record="";
			record = CommonStringUtils.trimToEmpty(model.getPatientNo()) + ",";
			record += model.getDiagName() + ",";
			record += CommonStringUtils.trimToEmpty(model.getName()) + ",";
			record += DateUtils.formatDateY_M_D_H_M(model.getPreDate()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getOpDoctor()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getOpDoctordept()) + ",";
			//在院状态
			String inState="";
				if("R".equals(model.getInState())){
					inState="住院登记";
				}
				else if("I".equals(model.getInState())){
					inState="病房接诊";
				}
				else if("B".equals(model.getInState())){
					inState="出院登记";
				}
				else if("O".equals(model.getInState())){
					inState="出院结算";
				}
				else if("P".equals(model.getInState())){
					inState="预约出院";
				}
				else if("N".equals(model.getInState())){
					inState="无费退院";
				}else{
					inState="未知";
				}
			//手术状态
				String opStatus="";
				if(model.getOpStatus()==1){
					opStatus="手术申请";
				}
				else if(model.getOpStatus()==2){
					opStatus="手术审批";
				}
				else if(model.getOpStatus()==3){
					opStatus="手术安排";
				}
				else if(model.getOpStatus()==4){
					opStatus="手术完成";
				}
				else if(model.getOpStatus()==5){
					opStatus="手术取消";
				}else{
					opStatus="未知";
				}
			//计费状态
				String feeState="";
				if("1".equals(model.getFeeState())){
					feeState= "未计费";
				}
				if("2".equals(model.getFeeState())){
					feeState= "存在计费";
				}
			record += CommonStringUtils.trimToEmpty(inState) + ",";
			record += opStatus + ",";
			record += CommonStringUtils.trimToEmpty(feeState) + ",";
			record+= DateUtils.formatDateY_M_D_H_M_S(model.getFeeDate())+ ",";
			try {
				fUtil.write(record);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fUtil;
	}
	
	/**
	 * @Description:员工
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年7月20日
	 * @return List<SysEmployee>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public List<SysEmployee> queryemployeeCombobox() {
		return operationBillingInfoDao.queryemployeeCombobox();
	}
	
	/**
	 * @Description:部门
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年7月20日
	 * @return List<SysDepartment>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public List<SysDepartment> querydeptmentCombobox() {
		return operationBillingInfoDao.querydeptmentCombobox();
	}
	
	/**
	 * @Description:查询员工得到员工id和name map 
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月3日
	 * @param:
	 * @returnMap<String, String> 
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	/**部门map 在operationCost下 **/
	@Override
	public Map<String, String> getEmpMap() {
		List<SysEmployee> list=operationBillingInfoDao.getEmp();
		HashMap<String, String> map=new HashMap<String,String>();
		if(list.size()>0&&list!=null){
			for (SysEmployee s : list) {
				map.put(s.getCode(), s.getName());
			}
		}
		return map;
	}

	/**  
	 * 
	 * 获取科室
	 * @Author: zxl
	 * @CreateDate: 2017年7月5日 下午4:26:13 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年7月5日 下午4:26:13 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<MenuListVO>
	 *
	 */
	@Override
	public List<MenuListVO> getDeptList() {
		return operationBillingInfoDao.getDeptList();
	}

	/**  
	 * 
	 * 获取医生
	 * @Author: zxl
	 * @CreateDate: 2017年7月5日 下午4:26:13 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年7月5日 下午4:26:13 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<MenuListVO>
	 *
	 */
	@Override
	public List<MenuListVO> getDoctorList() {
		return operationBillingInfoDao.getDoctorList();
	}

}
