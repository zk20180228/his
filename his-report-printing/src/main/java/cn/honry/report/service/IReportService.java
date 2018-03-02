package cn.honry.report.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import cn.honry.base.bean.model.BirthCertificate;
import cn.honry.base.bean.model.CriticalNotice;
import cn.honry.base.bean.model.DepositReminder;
import cn.honry.base.bean.model.EmrRecordMain;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.service.BaseService;

@SuppressWarnings({"all"})
public interface IReportService extends BaseService{
	/**
	 * @Description：报表打印
	 * @Author：hedong
	 * @CreateDate：2016-03-08 上午09:16:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param request
	 * @param response
	 * @param fileName  文件名称 （不含后缀,如 test.jasper 传入 test即可）
	 * @param parameterMap 参数HashMap集合
	 * @throws Exception
	 */
	public void doReport(HttpServletRequest request,
			HttpServletResponse response, String fileName, HashMap parameterMap) throws Exception;
	
	/**
	 * @Description：jdbc批量报表打印，在一张pdf
	 */
	public void doReport2(HttpServletRequest request,
			HttpServletResponse response, String fileName, List<HashMap<String,Object>> o) throws Exception;
	
	/**
	 * @Description：jdbc和javaBean批量报表打印，在一张pdf
	 */
	public void doReport3(HttpServletRequest request,
			HttpServletResponse response, List<HashMap<String,Object>> o) throws Exception;
	/**
	 * 
	 * @Description 根据药房查询药品名称
	 * @author  lyy
	 * @createDate： 2016年7月4日 上午10:49:28 
	 * @modifier lyy
	 * @modifyDate：2016年7月4日 上午10:49:28
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	SysDepartment queryDept(String drugstore);
	/**
	 * @Description：javaBean报表打印
	 * @author hd
	 * @param request
	 * @param response
	 * @param fileName
	 * @param parameters
	 * @param dataSource 
	 * @throws Exception 
	 */
	public void doReportToJavaBean(HttpServletRequest request, HttpServletResponse response, String reportFilePath,
			Map<String, Object> parameters, JRDataSource dataSource) throws Exception;
	
	/**
	 * @Description：javaBean批量报表打印，在一张pdf
	 */
	public void doReportToJavaBean2(HttpServletRequest request, HttpServletResponse response, String reportFilePath,
			List<HashMap<String,Object>> o) throws Exception;
	/**
	 * @Description 获取发药窗口
	 * @author  marongbin
	 * @createDate： 2017年4月21日 上午10:32:25 
	 * @modifier 
	 * @modifyDate：
	 * @param invoiceNo
	 * @return: String
	 * @modifyRmk：  
	 * @version 1.0
	 */
	String getSendDrugWin(String invoiceNo);

	public List<CriticalNotice> queryCriticalNotice(String inpatientNo);

	public List<BirthCertificate> queryBirthCertificate(String iD);

	public List<DepositReminder> queryDepositReminder(String inpatientNo);
	//病案查询打印
	public List<EmrRecordMain> recordPrint(String deptCode, String recCode,
				String patientName, String sex, Integer ageStart, Integer ageEnd,
				String outDateS, String outDateE, String digNose,
				String birthStart, String birthEnd);
}
