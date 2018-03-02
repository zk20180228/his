package cn.honry.statistics.finance.pharmacyRefund.service.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.finance.pharmacyRefund.dao.RefundStatDao;
import cn.honry.statistics.finance.pharmacyRefund.service.RefundSataService;
import cn.honry.statistics.finance.pharmacyRefund.vo.RefundVo;
import cn.honry.statistics.sys.reportForms.dao.ReportFormsDao;
import cn.honry.utils.CommonStringUtils;
import cn.honry.utils.DateUtils;
import cn.honry.utils.FileUtil;
import cn.honry.utils.HisParameters;
import freemarker.template.SimpleDate;

@Service("refundSataService")
@Transactional(rollbackFor = {Throwable.class})
@SuppressWarnings("all")
public class RefundSataServiceImpl implements RefundSataService {
	
	/***
	 * 注入本类service
	 */
	@Autowired
	@Qualifier(value="refundStatDao")
	private RefundStatDao refundStatDao;
	
	@Override
	public List<RefundVo> query(List<String> invoiceInfoPartName,List<String> cancelPartName,String feeStatCode, String beginDate, String endDate,String page,String rows) {
		
		return refundStatDao.query(invoiceInfoPartName, cancelPartName, feeStatCode, beginDate, endDate,page,rows);
	}


	@Override
	public int queryTotal(List<String> invoiceInfoPartName,List<String> cancelPartName,String feeStatCode, String beginDate, String endDate) {
		
		return refundStatDao.queryTotal(invoiceInfoPartName, cancelPartName, feeStatCode, beginDate, endDate);
	}


	@Override
	public FileUtil export(List<RefundVo> list, FileUtil fUtil)throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		for (RefundVo model : list) {
			String record="";
				record = CommonStringUtils.trimToEmpty(model.getInvoiceNo()) + ",";
				record += CommonStringUtils.trimToEmpty(model.getPatientName()) + ",";
				record += CommonStringUtils.trimToEmpty(model.getFeeStatCode()) + ",";
				record += dateFormat.format(model.getConfirmDate())+ ",";
				record += model.getRefundMoney() + ",";
				record += CommonStringUtils.trimToEmpty(model.getSendWin());
				
				fUtil.write(record);

			}
			
		return fUtil;
	}


	@Override
	public List<RefundVo> queryReport(List<String> invoiceInfoPartName,List<String> cancelPartName,
	String feeStatCode, String beginDate,String endDate) {
		
		return refundStatDao.queryReport(invoiceInfoPartName, cancelPartName, feeStatCode, beginDate, endDate);
	}

	/**
	 * 
	 * @Description:将门诊药房退费统计数据从mongodb中读出
	 * @param feeStatCode 药品类别(发票科目类型)
	 * @param beginDate 开始时间
	 * @param endDate 结束时间
	 * @return
	 * List<RefundVO>
	 * @exception:
	 * @author:zhangkui
	 * @time:2017年5月13日 上午10:38:04
	 */
	public List<RefundVo> queryByMongo(String feeStatCode, String beginDate, String endDate,String page,String rows) throws Exception{
		
		return refundStatDao.queryByMongo(feeStatCode, beginDate, endDate, page, rows);
	}


	/**
	 * 
	 * @Description:根据条件,在mongodb中查询总数量
	 * @param feeStatCode 药品种类
	 * @param beginDate 开始时间
	 * @param endDate 结束时间
	 * @return
	 * int 总记录数
	 * @exception:
	 * @author: zhangkui
	 * @time:2017年5月13日 下午3:01:42
	 */
	public int queryByMongoCount(String feeStatCode, String beginDate,String endDate) {
		
		return refundStatDao.queryByMongoCount(feeStatCode,beginDate,endDate);
	}
	
}
