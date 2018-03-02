package cn.honry.report.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BirthCertificate;
import cn.honry.base.bean.model.CriticalNotice;
import cn.honry.base.bean.model.DepositReminder;
import cn.honry.base.bean.model.EmrRecordMain;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.report.dao.IReportDao;
import cn.honry.report.service.IReportService;
import net.sf.jasperreports.engine.JRDataSource;
@Service("iReportService")
@Transactional
@SuppressWarnings({ "all" })
public class IReportServiceImpl implements IReportService{
    @Autowired 
    @Qualifier(value = "iReportDao")
    private IReportDao iReportDao;
	@Override
	public void doReport(HttpServletRequest request,
			HttpServletResponse response, String fileName, HashMap parameterMap) throws Exception {
		iReportDao.doReport(request,response,fileName,parameterMap);
	}
	@Override
	public void removeUnused(String id) {
	}
	@Override
	public Object get(String id) {
		return null;
	}
	@Override
	public void saveOrUpdate(Object entity) {
	}
	@Override
	public SysDepartment queryDept(String drugstore) {
		return iReportDao.queryDept(drugstore);
	}
	@Override
	public void doReportToJavaBean(HttpServletRequest request, HttpServletResponse response, String reportFilePath,
			Map<String, Object> parameters, JRDataSource dataSource) throws Exception {
		iReportDao.doReportToJavaBean(request,response,reportFilePath,parameters,dataSource);
	}
	@Override
	public void doReport2(HttpServletRequest request,
			HttpServletResponse response, String fileName, List<HashMap<String,Object>> o)
			throws Exception {
		iReportDao.doReport2(request,response,fileName,o);
		
	}
	@Override
	public void doReportToJavaBean2(HttpServletRequest request,
			HttpServletResponse response, String reportFilePath,
			List<HashMap<String, Object>> o)
			throws Exception {
		iReportDao.doReportToJavaBean2(request,response,reportFilePath,o);
		
	}
	@Override
	public void doReport3(HttpServletRequest request,
			HttpServletResponse response, List<HashMap<String, Object>> o)
			throws Exception {
		iReportDao.doReport3(request,response,o);
		
	}
	@Override
	public String getSendDrugWin(String invoiceNo) {
		return iReportDao.getSendDrugWin(invoiceNo);
	}
	@Override
	public List<CriticalNotice> queryCriticalNotice(String inpatientNo) {
		return iReportDao.queryCriticalNotice(inpatientNo);
	}
	@Override
	public List<BirthCertificate> queryBirthCertificate(String id) {
		return iReportDao.queryBirthCertificate(id);
	}
	@Override
	public List<DepositReminder> queryDepositReminder(String inpatientNo) {
		return iReportDao.queryDepositReminder(inpatientNo);
	}
	@Override
	public List<EmrRecordMain> recordPrint(String deptCode, String recCode,
			String patientName, String sex, Integer ageStart, Integer ageEnd,
			String outDateS, String outDateE, String digNose,
			String birthStart, String birthEnd) {
		return iReportDao.recordPrint(deptCode, recCode, patientName, sex, ageStart, ageEnd, outDateS, outDateE, digNose, birthStart, birthEnd);
	}

}
