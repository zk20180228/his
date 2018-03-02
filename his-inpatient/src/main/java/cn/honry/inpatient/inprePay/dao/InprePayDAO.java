package cn.honry.inpatient.inprePay.dao;

import java.util.List;

import cn.honry.base.bean.model.InpatientInPrepay;
import cn.honry.base.bean.model.InpatientInPrepayNow;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.dao.EntityDao;
import cn.honry.inpatient.inprePay.vo.AcceptingGold;
import cn.honry.inpatient.inprePay.vo.PatientVo;

/**  
 *  
 * @className：InprePayDAO 
 * @Description：  住院预交金DAO
 * @Author：aizhonghua
 * @CreateDate：2016-3-09 下午18:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-3-09 下午18:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@SuppressWarnings({"all"})
public interface InprePayDAO extends EntityDao<InpatientInPrepayNow>{

	/**
	 *
	 * @Description：通过就诊卡号获得患者信息
	 * @Author：aizhonghua
	 * @CreateDate：2016年4月11日 下午4:05:36 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0
	 * @param idcardNo 就诊卡号
	 * @throws Exception 
	 * @return：患者对象（PatientVo）
	 *
	 */
	PatientVo findPatientByIdcardNo(String idcardNo,String inState) throws Exception;

	/**
	 *
	 * @Description：通过就诊卡号查询就诊卡是否存在
	 * @Author：aizhonghua
	 * @CreateDate：2016年4月11日 下午4:05:36 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0
	 * @param idcardNo 就诊卡号
	 * @throws Exception 
	 * @return：true存在；false不存在
	 *
	 */
	boolean checkIdcardNo(String idcardNo) throws Exception;

	/**
	 *
	 * @Description：查询预交金信息 - 分页查询 - 获得总条数
	 * @Author：aizhonghua
	 * @CreateDate：2016年4月12日 上午9:47:41 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0
	 * @param：inpatientNo住院号
	 * @return 总条数
	 * @throws Exception 
	 */
	int getInprePayTotal(String inpatientNo) throws Exception;

	/**
	 *
	 * @throws Exception 
	 * @Description：查询预交金信息 - 分页查询 - 获得信息
	 * @Author：aizhonghua
	 * @CreateDate：2016年4月12日 上午9:47:41 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0
	 * @param1：inpatientNo住院号
	 * @param2：page当前页数
	 * @param3：rows分页数量
	 * @return：分页信息
	 * 
	 */
	List<InpatientInPrepayNow> getInprePayPage(String inpatientNo, String page,String rows) throws Exception;

	/**
	 *
	 * @Description：通过病历号获得患者信息
	 * @Author：aizhonghua
	 * @CreateDate：2016年4月11日 下午3:53:37 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	List<PatientVo> findPatientByInpatientNo(String medicale) throws Exception;
	
	/**
	 *
	 * @Description：通过住院号获得患者信息
	 * @Author：tangfeishuai
	 * @CreateDate：2016年4月11日 下午3:53:37 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	InpatientInfoNow findPatientByInpNo(String inpatientNo) throws Exception;
	/**
	 * 通过患者的病历号和住院状态查询患者的住院主表中的记录
	 * @param inpatientNo 患者的住院流水号
	 * @author tuchuanjiang
	 * @return
	 * @throws Exception 
	 */
	InpatientInfoNow queryInpatientInfoByInNo(String medicalrecordId) throws Exception;

	InpatientInfoNow isStopAcountNow(String inpatientNo) throws Exception;
	/**
	 * 更新患者预交金数据
	 * @param inp 患者预交金对象
	 * @author tuchuanjiang
	 * @return
	 * @throws Exception 
	 */
	void updateInpatientInPrepayNow(InpatientInPrepayNow inp) throws Exception;
	/**住院预交金
	 * @throws Exception **/
	List<AcceptingGold> iReportzyyjj(String id) throws Exception;
	/**@see 根据科室code获得所在院区
	 * @author conglin
	 * @param deptCode 科室code
	 * @return String 院区Code
	 * @throws Exception 
	 */
	String getDeptArea(String deptCode) throws Exception;

}
