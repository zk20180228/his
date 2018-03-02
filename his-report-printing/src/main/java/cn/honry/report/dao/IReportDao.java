package cn.honry.report.dao;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import cn.honry.base.bean.model.BirthCertificate;
import cn.honry.base.bean.model.CriticalNotice;
import cn.honry.base.bean.model.DepositReminder;
import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.EmrRecordMain;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.EntityDao;
@SuppressWarnings({"all"})
public interface IReportDao extends EntityDao {
	void doReport(HttpServletRequest request, HttpServletResponse response,
			String fileName, HashMap parameterMap) throws Exception;
	/**
	 * 
	 * @Description  根据药房查询药品名称
	 * @author  lyy
	 * @createDate： 2016年7月4日 上午10:50:52 
	 * @modifier lyy
	 * @modifyDate：2016年7月4日 上午10:50:52
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
	void doReportToJavaBean(HttpServletRequest request, HttpServletResponse response, String reportFilePath,
			Map<String, Object> parameters, JRDataSource dataSource) throws Exception;
	/**
	 * @Description：javaBean批量报表打印，在一张pdf
	 */
	void doReportToJavaBean2(HttpServletRequest request, HttpServletResponse response, String reportFilePath,
			List<HashMap<String,Object>> o) throws Exception;
	
	/**
	 * @Description：jdbc批量报表打印，在一张pdf
	 */
	void doReport2(HttpServletRequest request, HttpServletResponse response,
			String fileName, List<HashMap<String,Object>> o) throws Exception;
	
	/**
	 * @Description：jdbc和javaBean批量报表打印，在一张pdf
	 */
	void doReport3(HttpServletRequest request, HttpServletResponse response,
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
	List<CriticalNotice> queryCriticalNotice(String inpatientNo);
	List<BirthCertificate> queryBirthCertificate(String id);
	List<DepositReminder> queryDepositReminder(String inpatientNo);
	
	//病案查询打印
	public List<EmrRecordMain> recordPrint(String deptCode, String recCode,
				String patientName, String sex, Integer ageStart, Integer ageEnd,
				String outDateS, String outDateE, String digNose,
				String birthStart, String birthEnd);
	
	
}
