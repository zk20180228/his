package cn.honry.inner.operation.arrange.service.impl;
import java.io.IOException;
/***
 * 手术安排统计service实现层
 * @Description:
 * @author: tangfeishuai
 * @CreateDate: 2016年5月30日 
 * @version 1.0
 */
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.DrugUndrug;
import cn.honry.base.bean.model.OperationArrange;
import cn.honry.inner.operation.arrange.dao.OperationArrangeInnerDAO;
import cn.honry.inner.operation.arrange.service.OperationArrangeInnerService;
import cn.honry.inner.operation.arrange.vo.OperationArrangeInnerVo;
import cn.honry.utils.CommonStringUtils;
import cn.honry.utils.FileUtil;


@Service("operationArrangeInnerService")
@Transactional
@SuppressWarnings({ "all" })
public class OperationArrangeInnerServiceImpl implements OperationArrangeInnerService{
	
	/** 手术安排汇总 **/
	@Autowired
	@Qualifier(value = "operationArrangeInnerDAO")
	private OperationArrangeInnerDAO operationArrangeDAO;
	
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
	@Override
	public List<OperationArrangeInnerVo> getOperationArrangeVo(String beganTime, String endTime, String status,
			String execDept, String page, String rows) {
		
		return operationArrangeDAO.getOperationArrangeVo(beganTime,endTime,status,execDept,page,rows);
	}

	@Override
	public OperationArrange get(String id) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(OperationArrange arg0) {
		
	}
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
	@Override
	public Map<String, String> opNameMap() {
		HashMap<String, String> map=new HashMap<String,String>();
		List<DrugUndrug> undrugList=operationArrangeDAO.getCombobox();
		if(undrugList.size()>0&&undrugList!=null){
			for (DrugUndrug d : undrugList) {
				map.put(d.getId(), d.getName());
			}
		}
		return map;
	}
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
	@Override
	public int getTotal(String beganTime, String endTime, String status, String execDept) {
		return operationArrangeDAO.getTotal(beganTime, endTime, status, execDept);
	}
	
	/**
	 * @Description:导出列表
	 * @Description:
	 * @author: tangfeishuai
	 * @CreateDate: 2016年6月24日 
	 * @version 1.0
	**/
	@Override
	public FileUtil export(List<OperationArrangeInnerVo> list, FileUtil fUtil) {
		for (OperationArrangeInnerVo model : list) {
			String record="";
			record = CommonStringUtils.trimToEmpty(model.getInDept()) + ",";
			record += model.getPreDate() + ",";
			record += CommonStringUtils.trimToEmpty(model.getPatientNo()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getBedNo()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getName()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getSex()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getAge()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getItemName()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getRoomId()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getOpDoctor()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getThelper()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getTour()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getWash()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getAneWay()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getZanesthesia()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getFanesthesia()) + ",";
			try {
				fUtil.write(record);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fUtil;
	}
	
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
	@Override
	public List<OperationArrangeInnerVo> getAllOperationArrangeVo(String beganTime, String endTime, String status,
			String execDept) {
		return operationArrangeDAO.getAllOperationArrangeVo(beganTime, endTime, status, execDept);
	}


}
