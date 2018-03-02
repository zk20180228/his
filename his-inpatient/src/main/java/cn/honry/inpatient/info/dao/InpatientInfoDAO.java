package cn.honry.inpatient.info.dao;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import cn.honry.base.bean.model.BusinessBedward;
import cn.honry.base.bean.model.BusinessHospitalbed;
import cn.honry.base.bean.model.DrugApplyoutNow;
import cn.honry.base.bean.model.InpatientBedinfoNow;
import cn.honry.base.bean.model.InpatientCancelitemNow;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientOrderNow;
import cn.honry.base.bean.model.InpatientPrepayin;
import cn.honry.base.bean.model.InpatientProof;
import cn.honry.base.bean.model.InpatientShiftData;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.VidBedInfo;
import cn.honry.base.dao.EntityDao;
import cn.honry.inpatient.info.vo.InfoVo;


/**
 * ClassName: InpatientInfoDAO 
 * @Description: 住院登记表业务逻辑接口
 * @author lt
 * @date 2015-6-24
 */
public interface InpatientInfoDAO extends EntityDao<InpatientInfoNow>{

	/**
	 * @Description:获取分页列表
	 * @Author：  lt
	 * @CreateDate： 2015-6-24
	 * @param @param page
	 * @param @param rows
	 * @param @param entity
	 * @return List<InpatientInfo>  
	 * @version 1.0
	 * @throws Exception 
	**/
	List<InpatientInfoNow> getPage(String page, String rows, InpatientInfoNow entity) throws Exception;
	/**
	 * @Description:获取总条数
	 * @Author：  lt
	 * @CreateDate： 2015-6-24
	 * @param @param entity
	 * @return int  
	 * @version 1.0
	 * @throws Exception 
	**/
	int getTotal(InpatientInfoNow entity) throws Exception;
	/**
	 * @Description:根据病历号取到住院登记信息
	 * @Author：  lt
	 * @CreateDate： 2015-7-1
	 * @param @param id
	 * @param @return   
	 * @return InpatientInfo  
	 * @version 1.0
	 * @throws Exception 
	**/
	InpatientInfoNow queryByMedical(String medicalNo) throws Exception;

	/**  
	 *  
	 * @Description：  查询住院患者 .
	 * @Author：aizhonghua
	 * @CreateDate：2015-8-18 下午05:37:12  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-8-18 下午05:37:12  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	List<InpatientInfoNow> queryPatient() throws Exception;
	/**  
	 *  
	 * @Description：  根据患者的住院状态查询.
	 * @Author：zhenglin
	 * @CreateDate：2015-12-15 下午05:37:12  
	 * @Modifier：zhenglin
	 * @ModifyDate：2015-12-15 下午05:37:12  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	List<InpatientInfoNow> queryPatientByInState(String state) throws Exception;
	
	/**  
	 *  
	 * @Description：  查询住院患者 .
	 * @Author：aizhonghua
	 * @CreateDate：2015-8-18 下午05:37:12  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-8-18 下午05:37:12  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	List<VidBedInfo> queryPatientList() throws Exception;
	/**  
	 *  
	 * @Description：  根据患者的住院状态查询.
	 * @Author：zhenglin
	 * @CreateDate：2015-12-15 下午05:37:12  
	 * @Modifier：zhenglin
	 * @ModifyDate：2015-12-15 下午05:37:12  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	List<VidBedInfo> queryPatientByInStateList(String state) throws Exception;
	/**  
	 *  
	 * @Description：  查询出院登记
	 * @Author：aizhonghua
	 * @CreateDate：2015-8-26 下午02:06:02  
	 * @Modifier：lyy
	 * @ModifyDate：2016-3-21 下午02:06:02  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	List<InpatientInfoNow> queryInpinfo(String type, String no) throws Exception;
	/**  
	 *  
	 * @Description：  查询出院登记
	 * @Author：aizhonghua
	 * @CreateDate：2015-8-26 下午02:06:02  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-8-26 下午02:06:02  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	List<InpatientInfoNow> queryInpinfoList(String type, String no) throws Exception;

	/**  
	 *  
	 * @Description：  保存前查到保存信息
	 * @throws Exception 
	 * @Author：zj
	 * @CreateDate：2015-12-7
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	InpatientInfoNow inpatientIdGet(String id) throws Exception;
	/**  
	 *  
	 * @Description：  查询床号
	 * @throws Exception 
	 * @Author：zj
	 * @CreateDate：2015-12-7
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	InpatientInfoNow ajaxBedId(String id) throws Exception;
	/**  
	 *  
	 * @Description：  查询患者退费申请表
	 * @throws Exception 
	 * @Author：zj
	 * @CreateDate：2015-12-7
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<InpatientCancelitemNow> ajaxCanceliem(String no) throws Exception;
	
	/**   
	 * @Description：  根据患者id查询RegisterInfo信息
	 * @Author：zhenglin
	 * @CreateDate：2015-12-10 下午13:41:01  
	 * @Modifier：zhenglin
	 * @ModifyDate：2015-12-10 下午13:41:01  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	RegisterInfo getInfoByPatientNO(String patientNo) throws Exception;
	
	
	
	/**  
	 *  
	 * @Description：  查询病人信息
	 *@Author：zhenglin
	 * @CreateDate：2015-12-12 上午10:56:35  
	 * @Modifier：zhenglin
	 * @ModifyDate：2015-12-12 上午10:56:35   
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	InfoVo  getProof(String id,String idcardNo) throws Exception;
	/**  
	 *  
	 * @Description：  （根据病历号查询）住院诊断.
	 * @Author：zhenglin
	 * @CreateDate：2015-12-15 下午05:37:12  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-15 下午05:37:12  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	InpatientInfoNow query( String no) throws Exception;
	

	/**  
	 * @Description： 
	 *@Author：zhenglin
	 * @CreateDate：2015-12-19 上午10:56:35  
	 * @Modifier：zhenglin
	 * @ModifyDate：2015-12-19 上午10:56:35   
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	List<InpatientInfoNow> getInfoByApply() throws Exception;
	
	
	
	
	/**
	 * 根据患者警戒线来查询患者信息
	 * @return
	 * @throws Exception 
	 */
	List<InpatientInfoNow> getInfoByMoney(InpatientInfoNow info,String rows,String page) throws Exception;
	
	/**
	 * @Description:获取总条数(患者警戒线)
	 * @Author：  zhenglin
	 * @CreateDate： 2015-12-21
	 * @param @param entity
	 * @return int  
	 * @version 1.0
	 * @throws Exception 
	**/
	int getTotals(InpatientInfoNow entity) throws Exception;
	/**
	 * @Description:获取患者树(护士站收费)
	 * @Author： zhangjin
	 * @CreateDate： 2015-1—5
	 * @param @param 
	 * @return 
	 * @version 1.0
	 * @throws Exception 
	**/
	List<InfoVo> treeNurseCharge(String deptId,InpatientInfoNow inpatientInfo,String a,String startTime,String endTime) throws Exception;
	/**  
	 * @Description：  病区（下拉框）
	 * @Author：tcj
	 * @CreateDate：2015-12-30 上午9:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 * @throws Exception 
	 */
	List<SysDepartment> querydeptCombobox(String id) throws Exception;
	/**  
	 * @Description：  查询病房树
	 * @Author：tcj
	 * @CreateDate：2015-12-30  上午15:06
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	List<BusinessBedward> queryPatientRoom(String id) throws Exception;
	/**  
	 * @Description：  点击病房树查询病床
	 * @Author：tcj
	 * @CreateDate：2015-12-30  上午18:06
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	List<BusinessHospitalbed> queryPatientRoomBed(String noId,String page,String rows,BusinessHospitalbed bedEntity) throws Exception;
	/**
	 * @Description:查询列表总条数
	 * @Author：  tcj
	 * @CreateDate： 2016-1-4 11:38
	 * @param @return   
	 * @return int  
	 * @version 1.0
	 * @throws Exception 
	**/
	int getTotal(BusinessHospitalbed bedEntity,String noId) throws Exception;
	
	/**
	 * @Description:根据id查询患者信息
	 * @Author：  zhangjin
	 * @CreateDate： 2016-1-6
	 * @param @return   
	 * @return  
	 * @version 1.0
	 * @throws Exception 
	**/
	InpatientInfoNow querNurseCharge(String no) throws Exception;
	/**  
	 * @Description：  查询当日的挂号记录
	 * @Author：tcj
	 * @CreateDate：2016-1-5  下午16:40
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	String queryMedicalrecordIdDate(String medicalrecordId,Date now) throws Exception;
	/**  
	 * @Description：  查询住院总次数
	 * @Author：tcj
	 * @CreateDate：2016-1-5  下午18:40
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	int queryCount(String medicalrecordId) throws Exception;
	/**  
	 *  
	 * @Description：根据住院流水号查询患者信息
	 * @Author：zhangjin
	 * @CreateDate：2016-1-18
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 * @throws IOException 
	 *
	 */
	 List<InpatientInfoNow> queryNurseChargeInpinfo(String no,String dept) throws Exception;
	/**  
	 * @Description： 根据住院流水号查询处于接诊状态的患者
	 * @Author：tcj
	 * @CreateDate：2016-1-5  下午18:40
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	InpatientInfoNow queryByInpatientNo(String inpatientNo) throws Exception;
	/**  
	 * @Description： 根据住院流水号查询患者
	 * @Author：tcj
	 * @CreateDate：2016-1-5  下午18:40
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	InpatientInfoNow queryByInpatientNot(String inpatientNo) throws Exception;
	/**  
	 * @Description：  查询是否有 有效的住院证明
	 * @Author：tcj
	 * @CreateDate：2016-1-5  下午18:40
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	String queryProofInfo(String certificatesNo) throws Exception;
	/**  
	 *  
	 * @Description： 根据科室编号查询住院患者信息
	 * @Author：tfs
	 * @CreateDate：2016-3-5 下午14:12:01  
	 * @Modifier：
	 * @ModifyDate：2015-3-5 下午14:12:01  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	List<InpatientInfoNow> getInfoByDeptCode(String deptCode) throws Exception;
	/**
	 * 根据病历号查出多条数据
	 * @author  lyy
	 * @createDate： 2016年3月21日 下午1:07:03 
	 * @modifier lyy
	 * @modifyDate：2016年3月21日 下午1:07:03  
	 * @modifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	List<InpatientInfoNow> getQueryInfo(String medicalrecordId) throws Exception;
	/**
	 * 根据住院主表的病床号bedInfo查询病床使用表中的病床号bedId
	 * @author  lyy
	 * @createDate： 2016年3月21日 下午7:25:08 
	 * @modifier lyy
	 * @modifyDate：2016年3月21日 下午7:25:08  
	 * @modifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	InpatientBedinfoNow queryBedId(String bedInfoId) throws Exception;
	/**
	 * 根据病历号查出多条住院信息数据
	 * @author  lyy
	 * @createDate： 2016年3月21日 下午1:01:16 
	 * @modifier lyy
	 * @modifyDate：2016年3月21日 下午1:01:16  
	 * @modifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	List<InpatientProof> getdengjiInfo(String medicalrecordId);
	
	/**  
	 *  
	 * @Description： 根据科室编号和住院号查询住院患者信息
	 * @Author：tfs
	 * @CreateDate：2016-3-21 上午11:12:01  
	 * @Modifier：
	 * @ModifyDate：2015-3-21 上午11:12:01  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	List<InpatientInfoNow> getInfoByDeptCodeAndMid(String deptCode,String medicalrecordId) throws Exception;
	/**
	 * 查询住院科室下拉框
	 * @author  tcj
	 * @version 1.0
	 * @throws Exception 
	 */
	List<SysDepartment> zyDeptCombobox() throws Exception;
	/**
	 * 根据病历号查询患者信息
	 * @param medicalrecordId
	 * @return
	 * @throws Exception 
	 */
	Patient queryPatientInfomation(String medicalrecordId) throws Exception;
	/**
	 * 查询资料变更表中的最大发生序号
	 * @return
	 * @throws Exception 
	 */
	List<InpatientShiftData> queryMaxHappenNo() throws Exception;
	/**
	 * 保存住院登记
	 * @param inpatientInfo
	 * @param patient
	 * @throws Exception 
	 */
	void editInpatientInfo(InpatientInfoNow inpatientInfo, Patient patient) throws Exception;
	/**
	 * 获取患者未执行未作废的医嘱
	 * @param InpatientOrderNow
	 * @throws Exception 
	 */
	List<InpatientOrderNow> getInpatientOrderNowList(String inpatientNo) throws Exception;
	/**
	 * 获取患者未摆药记录
	 * @param DrugApplyoutNow
	 * @throws Exception 
	 */
	List<DrugApplyoutNow> getDrugApplyoutNowList(String inpatientNo) throws Exception;
	/**
	 * 查询开据医生下拉框
	 * @author  tcj
	 * @version 1.0
	 * @throws Exception 
	 */
	List<SysEmployee> querykaijudocDj() throws Exception;
	/**
	 * 通过病历号查询患者信息
	 * @author  tcj
	 * @version 1.0
	 * @throws Exception 
	 */
	List<Patient> getIdcardInfo(String medinfoId) throws Exception;
	/**
	 *  通过身份证号查询患者信息
	 * @author  tcj
	 * @version 1.0
	 * @param cerno 身份证号
	 * @return
	 * @throws Exception 
	 */
	List<Patient> getPatientInfoByCerNo(String cerno) throws Exception;
	/**
	 * 通过病历号查询患者是否有预约住院信息
	 * @author  tcj
	 * @version 1.0
	 * @throws Exception 
	 */
	List<InpatientPrepayin> queryPrepayinInfo(String medicalrecordId) throws Exception;
	/**
	 * 
	 * 
	 * <p>根据病历号查询患者在院状态</p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月4日 下午1:47:46 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月4日 下午1:47:46 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param medicalrecordId 病历号
	 * @return:
	 *
	 */
	String queryMedicalrecordIdDateByMid(String medicalrecordId);
	
	/**
	 * 
	 * 
	 * <p>根据病历号查询T_Patient表中的患者数据 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月4日 下午1:49:09 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月4日 下午1:49:09 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param medicalrecordId 病历号
	 * @return:
	 *
	 */
	List<Patient> getPatientInfo(String medicalrecordId);
	
	/**
	 * 
	 * 
	 * <p>通过合同单位代码查询支付方式 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月4日 下午1:49:39 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月4日 下午1:49:39 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param unit 合同单位代码
	 * @return:
	 *
	 */
	String getPaykind(String unit);
	
	/**
	 * 更新患者出院相关信息
	 * @author  wsj
	 * @version 1.0
	 * @throws Exception 
	 */
	void updateInpatientFeeInfoNow(InpatientInfoNow info1);
	
	/**
	 * 
	 * 
	 * <p>更新状态为出院登记状态 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月4日 下午1:53:51 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月4日 下午1:53:51 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param p
	 * @return:
	 *
	 */
	int updeteOut(InpatientInfoNow p);
	/**
	 * 
	 * 
	 * <p>更新预交金和余额 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月4日 下午1:55:58 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月4日 下午1:55:58 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param i: 住院主表ID
	 *
	 */
	void updateInpatientInfoNow(InpatientInfoNow i);

}
