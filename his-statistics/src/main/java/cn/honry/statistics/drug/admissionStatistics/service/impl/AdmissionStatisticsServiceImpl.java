package cn.honry.statistics.drug.admissionStatistics.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.statistics.drug.admissionStatistics.dao.AdmissionStatisticsDAO;
import cn.honry.statistics.drug.admissionStatistics.service.AdmissionStatisticsService;
import cn.honry.statistics.drug.admissionStatistics.vo.AdmissionStatisticsVo;
import cn.honry.utils.CommonStringUtils;
import cn.honry.utils.FileUtil;

@Service("admissionStatisticsService")
@Transactional
@SuppressWarnings({ "all" })
public class AdmissionStatisticsServiceImpl implements AdmissionStatisticsService{
	
	/** 汇总 **/
	@Autowired
	@Qualifier(value = "admissionStatisticsDAO")
	private AdmissionStatisticsDAO admissionStatisticsDAO;
	
	@Override
	public AdmissionStatisticsVo get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(AdmissionStatisticsVo arg0) {
		
	}

	@Override
	public List<AdmissionStatisticsVo> getAdmissionStatisticsVo(String beginTime, String endTime, String deptCode,
			String storageCode, String drugType, String outType, String page, String rows) throws Exception {
		return admissionStatisticsDAO.getAdmissionStatisticsVo(beginTime, endTime, deptCode, storageCode, drugType, outType, page, rows);
	}

	@Override
	public int getTotal(String beginTime, String endTime, String deptCode, String storageCode, String drugType,
			String outType) throws Exception {
		return admissionStatisticsDAO.getTotal(beginTime, endTime, deptCode, storageCode, drugType, outType);
	}


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
	 * @throws Exception 
	 */
	@Override
	public List<SysDepartment> getSysDepartment() throws Exception {
		return admissionStatisticsDAO.getSysDepartment();
	}

	/**
	 * @Description:导出列表
	 * @Description:
	 * @author: tangfeishuai
	 * @CreateDate: 2016年6月24日 
	 * @version 1.0
	**/
	@Override
	public FileUtil export(List<AdmissionStatisticsVo> list, FileUtil fUtil) {
		for (AdmissionStatisticsVo model : list) {
			String record="";
			record = CommonStringUtils.trimToEmpty(model.getDrugBasicCode()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getDrugBiddingCode()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getDrugId()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getDrugName()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getOutState()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getOpType()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getDrugSpec()) + ",";
			record += model.getNum() + ",";
			record += CommonStringUtils.trimToEmpty(model.getDrugPackgingUnit()) + ",";
			record += model.getSum() + ",";
			record += model.getRetailPrice() + ",";
			try {
				fUtil.write(record);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fUtil;
	}

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
	 * @throws Exception 
	 */
	@Override
	public List<AdmissionStatisticsVo> getAllAdmissionStatisticsVo(String beginTime, String endTime, String deptCode,
			String storageCode, String drugType, String outType) throws Exception {
		return admissionStatisticsDAO.getAllAdmissionStatisticsVo(beginTime, endTime, deptCode, storageCode, drugType, outType);
	}
	
	
}
