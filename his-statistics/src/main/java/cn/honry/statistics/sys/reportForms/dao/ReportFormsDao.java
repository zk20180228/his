package cn.honry.statistics.sys.reportForms.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import cn.honry.base.bean.model.FinanceInvoicedetail;
import cn.honry.base.bean.model.FinanceInvoicedetailNow;
import cn.honry.base.bean.model.InpatientMedicineList;
import cn.honry.base.bean.model.OutpatientFeedetail;
import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.base.bean.model.RegisterPreregister;
import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.base.dao.EntityDao;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.sys.reportForms.vo.DoctorWorkloadStatistics;
import cn.honry.statistics.sys.reportForms.vo.IncomeVo;
import cn.honry.statistics.sys.reportForms.vo.PatientInfoVo;
import cn.honry.statistics.sys.reportForms.vo.StatisticsVo;

@SuppressWarnings({"all"})
public interface ReportFormsDao extends EntityDao<DoctorWorkloadStatistics>{

	
	
	/**  
	 *  
	 * @Description：  医生工作量统计查询   挂号科室下的医生
	 * @Author：wujiao
	 * @CreateDate：2016-5-3 10:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param  deptw科室
	 * @param  stimew开始时间
	 * @param  etimew结束时间
	 */
	List<DoctorWorkloadStatistics> queryReservation(String deptw, String stimew, String etimew);
	
	/**  
	 *  
	 * @Description：  医生工作量统计查询   看诊信息
	 * @Author：wujiao
	 * @CreateDate：2016-5-3 10:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param  deptId科室id
	 * @param  stimew开始时间
	 * @param  etimew结束时间
	 * @param  nameId员工id
	 */
	Integer queryInfo(List<String> tnL,String nameId, String deptId, String stimew, String etimew);
	
	/**  
	 *  
	 * @Description：  医生工作量统计查询   预约号信息
	 * @Author：wujiao
	 * @CreateDate：2016-5-3 10:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param  deptId科室id
	 * @param  stimew开始时间
	 * @param  etimew结束时间
	 * @param  nameId员工id
	 */
	List<RegisterPreregister> querRegister(String nameId, String deptId, String stimew, String etimew);

	/**  
	 *  
	 * @Description：  医生工作量统计查询   看诊信息就诊数
	 * @Author：wujiao
	 * @CreateDate：2016-5-3 10:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param  deptId科室id
	 * @param  stimew开始时间
	 * @param  etimew结束时间
	 * @param  nameId员工id
	 */
	Integer queryInfonum(List<String> tnL,String nameId, String deptId, String stimew, String etimew);
	
	/**  
	 *  
	 * @Description：  医生工作量统计查询   电话预约
	 * @Author：wujiao
	 * @CreateDate：2016-5-3 10:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param  deptId科室id
	 * @param  stimew开始时间
	 * @param  etimew结束时间
	 * @param  nameId员工id
	 */
	Integer queryPhonenum(String nameId, String deptId, String stimew, String etimew);
	
	/**  
	 *  
	 * @Description：  医生工作量统计查询   网络预约
	 * @Author：wujiao
	 * @CreateDate：2016-5-3 10:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param  deptId科室id
	 * @param  stimew开始时间
	 * @param  etimew结束时间
	 * @param  nameId员工id
	 */
	Integer querynetnum(String nameId, String deptId, String stimew, String etimew);
	
	/**  
	 *  
	 * @Description：  医生工作量统计查询   现场预约
	 * @Author：wujiao
	 * @CreateDate：2016-5-3 10:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param  deptId科室id
	 * @param  stimew开始时间
	 * @param  etimew结束时间
	 * @param  nameId员工id
	 */
	Integer querynownum(String nameId, String deptId, String stimew, String etimew);
	
	/**
	 * @Description 医生工作量统计
	 * @author  marongbin
	 * @createDate： 2016年12月26日 下午2:41:04 
	 * @modifier 
	 * @modifyDate：
	 * @param tnL 挂号主表分区
	 * @param pretnl 预约表分区
	 * @param deptw 科室
	 * @param stimew 开始时间
	 * @param etimew 结束时间
	 * @return: List<DoctorWorkloadStatistics>
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<DoctorWorkloadStatistics> queryReservation(List<String> tnL,List<String> pretnl,String deptw, String stimew, String etimew,String menuType);
	
	/**
	 * @Description 医生工作量统计
	 * @author  marongbin
	 * @createDate： 2016年12月26日 下午2:41:04 
	 * @modifier 
	 * @modifyDate：
	 * @param tnL 挂号主表分区
	 * @param pretnl 预约表分区
	 * @param deptw 科室
	 * @param stimew 开始时间
	 * @param etimew 结束时间
	 * @return: List<DoctorWorkloadStatistics>
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<DoctorWorkloadStatistics> queryReservationMongDB(String deptw,String stimew,String etimew);
	/**  
	 *  
	 * @Description：  门诊住院情况统计统计查询
	 * @Author：wujiao
	 * @CreateDate：2016-5-3 下午10:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param  dept科室
	 * @param  stime开始时间
	 * @param  etime结束时间
	 *
	 */
	List<PatientInfoVo> queryPatientInfo(List<String> infotnl,List<String> feedetialtnl, List<String> notmednl,List<String> mednl, String dept, String stime, String etime,String menuType);
	
	/**  
	 *  
	 * @Description：  医院各项收入统计
	 * @Author：wujiao
	 * @CreateDate：2016-5-12 下午3:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param  dept科室
	 * @param  stime开始时间
	 * @param  etime结束时间
	 *
	 */
	List<StatisticsVo> queryStatisticsInfo(String dept, String stime, String etime);
	
	/**  
	 *  
	 * @Description：  医院各项收入统计
	 * @Author：wujiao
	 * @CreateDate：2016-5-13 10:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param  deptId科室id
	 * @param  userid用户id
	 *
	 */
	List<OutpatientFeedetail> queryFeedetailInfo(List<String> feedetialPartitionName,String deptId, String userid,String stime,String etime);
	
	/**  
	 *  
	 * @Description：  医院各项收入统计
	 * @Author：wujiao
	 * @CreateDate：2016-5-13 10:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param  invoiceNo发票号
	 * 
	 *
	 */
	List<FinanceInvoicedetailNow> queryinvo(String invoiceDetialTabName,String invoiceNo);
	
	/**
	 * @Description 根据分区和发票号进行
	 * @author  marongbin
	 * @createDate： 2016年12月3日 下午5:21:39 
	 * @modifier 
	 * @modifyDate：
	 * @param invoicePartitionName
	 * @param invoiceNo
	 * @return: List<BusinessInvoicedetail>
	 * @modifyRmk：  
	 * @version 1.0
	 */
	StatisticsVo queryinvoNew(List<String> invoicePartitionName,String invoiceNo,String doctCode,String stime,String etime);
	
	/**
	 * @Description 
	 * @author  marongbin
	 * @createDate： 2016年12月5日 下午2:15:44 
	 * @modifier 
	 * @modifyDate：
	 * @param invoicePartitionName
	 * @param invoiceNo
	 * @param doctCode
	 * @return: StatisticsVO
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<FinanceInvoicedetailNow> queryinvoNow(List<String> invoicePartitionName,String invoiceNo);
	
	/**  
	 *  
	 * @Description：  医院各项收入统计
	 * @Author：wujiao
	 * @CreateDate：2016-5-16 11:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param  emp员工id
	 * @param  stime开始时间
	 * @param  etime结束时间
	 *
	 */
	List<IncomeVo> queryIncomeInfo(String tableName,String emp, String stime);
	
	/**
	 * @Description 获取表中某个字段的最大值和最小值
	 * @author  marongbin
	 * @createDate： 2016年12月3日 上午10:51:30 
	 * @modifier 
	 * @modifyDate：
	 * @param tabName 表明
	 * @param fieldName 字段名
	 * @return: StatVo
	 * @modifyRmk：  
	 * @version 1.0
	 */
	StatVo findMaxMinByTabNameAndField(String tabName,String fieldName);
	
	/**  
	 *  
	 * @Description：  通过员工id得到用户id
	 * @Author：wujiao
	 * @CreateDate：2016-5-16 11:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param  emp员工id
	 *
	 */
	String queryUserByemp(String emp);
	
	/**  
	 *  
	 * @Description：  医院各项收入统计
	 * @Author：wujiao
	 * @CreateDate：2016-5-17 5:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param  deptId科室id
	 * @param  userId用户id
	 * @param  dates时间
	 *
	 */
	List<OutpatientFeedetail> queryFeedetailInfo(String invoiceDetialTabName,String deptId, String userId, Date dates);
	
	
	List<OutpatientFeedetail> queryFeelist(String medicalrecordId);
	
	List<InpatientMedicineList> querymedilist(String medicalrecordId);
	

}
