package cn.honry.inpatient.info.service;

import java.io.IOException;
import java.util.List;

import cn.honry.base.bean.model.BusinessHospitalbed;
import cn.honry.base.bean.model.DrugApplyoutNow;
import cn.honry.base.bean.model.InpatientBedinfoNow;
import cn.honry.base.bean.model.InpatientCancelitemNow;
import cn.honry.base.bean.model.InpatientInPrepayNow;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientOrderNow;
import cn.honry.base.bean.model.InpatientPrepayin;
import cn.honry.base.bean.model.InpatientProof;
import cn.honry.base.bean.model.InpatientSurety;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.service.BaseService;
import cn.honry.inpatient.info.vo.InfoVo;
import cn.honry.inpatient.warnLine.vo.WarnLineVo;
import cn.honry.utils.TreeJson;

/**
 * ClassName: InpatientInfoService 
 * @Description: 住院登记表业务逻辑接口
 * @author lt
 * @date 2015-6-24
 */
public interface InpatientInfoService extends BaseService<InpatientInfoNow> {

	/**
	 * @Description: 获取分页列表
	 * @Author：  lt
	 * @CreateDate： 2015-6-24
	 * @param  page 当前页
	 * @param  rows 当前页条数
	 * @param  inpatientInfoSerch
	 * @return List<InpatientInfo>  
	 * @version 1.0
	 * @throws Exception 
	**/
	List<InpatientInfoNow> getPage(String page, String rows,InpatientInfoNow inpatientInfoSerch) throws Exception;

	/**
	 * @Description: 获取总条数
	 * @Author：  lt
	 * @CreateDate： 2015-6-24
	 * @param inpatientInfoSerch
	 * @return int  
	 * @version 1.0
	 * @throws Exception 
	**/
	int getTotal(InpatientInfoNow inpatientInfoSerch) throws Exception;

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
	 * @Description： 查询住院患者信息
	 * @Author：wujiao
	 * @CreateDate：2015-7-3 下午11:12:01  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-7-3 下午11:12:01  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	List<InpatientInfoNow> getpatientList() throws Exception;
	
	/**  
	 *  
	 * @Description：  查询患者
	 * @Author：aizhonghua
	 * @CreateDate：2015-8-18 下午05:37:12  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-8-18 下午05:37:12  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	String queryPatientTree() throws Exception;

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
	 * @Description：  保存出院登记信息
	 * @throws Exception 
	 * @Author：zj
	 * @CreateDate：2015-12-7
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	void inpatientIdGet(InpatientInfoNow inpatientInfo) throws Exception;
	
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
	 * @Description：  查看退费申请
	 * @throws Exception 
	 * @Author：zj
	 * @CreateDate：2015-12-7
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<InpatientCancelitemNow> ajaxCanceliem(String no) throws Exception;

	/**
	 * 获取患者未摆药记录
	 * @param DrugApplyoutNow
	 * @throws Exception 
	 */
	List<DrugApplyoutNow> getDrugApplyoutNowList(String inpatientNo) throws Exception;
	/**
	 * @Description：保存  设置警戒线
	 * @Author：lyy
	 * @CreateDate：2015-12-3 下午03:06:02    
	 * @Modifier：lyy
	 * @ModifyDate：2015-12-3 下午03:06:02  
	 * @param warnLineVo 护士站患者警戒线
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	void saveWarnLineVo(WarnLineVo warnLineVo) throws Exception;

	/**
	 * 获取患者未执行未作废的医嘱
	 * @param InpatientOrderNow
	 * @throws Exception 
	 */
	List<InpatientOrderNow> getInpatientOrderNowList(String inpatientNo) throws Exception;
	
	/**  
	 *  
	 * @Description：  根据患者id查询RegisterInfo信息
	 * @Author：zhenglin
	 * @CreateDate：2015-12-10 下午14:17:01  
	 * @Modifier：zhenglin
	 * @ModifyDate：2015-12-10 下午14:17:01 
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	RegisterInfo getInfoByePatientNO(String patientNo) throws Exception;
	
	
	/**  
	 *  
	 * @Description：  根据门诊号查询开院证明表
	 * @Author：zhenglin
	 * @CreateDate：2015-12-10 下午14:17:01  
	 * @Modifier：zhenglin
	 * @ModifyDate：2015-12-10 下午14:17:01 
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	InfoVo  getProof(String id,String idcardNo) throws Exception;
	
	
	/**  
	 *  
	 * @Description：  根据患者的住院状态查询.
	 * @Author：zhenglin
	 * @CreateDate：2015-12-15 下午05:37:12  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-15 下午05:37:12  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	List<InpatientInfoNow> queryPatientByInState(String state) throws Exception;
	/**  
	 *  
	 * @Description： (根据病历号获取信息) 住院诊断
	 * @Author：zhangjin
	 * @CreateDate：2015-12-22下午05:37:12  
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	InpatientInfoNow queryInpatientinfo( String no) throws Exception;
	
	/**
	 * @author zhenglin 2015-12-21
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
	 *  
	 * @Description： 获取患者树(护士站收费)
	 * @throws Exception 
	 * @Author：zhangjin
	 * @CreateDate：2015-1-5  
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<TreeJson> treeNurseCharge(String deptId,String id,InpatientInfoNow inpatientInfo,String a,String startTime,String endTime) throws Exception;
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
	List<TreeJson> queryPatientRoom(String id) throws Exception;
	/**  
	 * @Description：  点击病房树查询病床
	 * @Author：tcj
	 * @CreateDate：2015-12-30  上午18:06
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param deptId 
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
	 * @Description:根据Id获取患者信息
	 * @Author：  zhangjin
	 * @CreateDate： 2016-1-6 1
	 * @param @return   
	 * @return  
	 * @version 1.0
	 * @throws Exception 
	**/
	InpatientInfoNow querNurseCharge(String no) throws Exception;
	/**  
	 * @Description：  查询患者的住院信息（登记、接诊，以及相应的时间）
	 * @Author：tcj
	 * @CreateDate：2016-1-5  下午16:40
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	String queryMedicalrecordIdDate(String medicalrecordId) throws Exception;
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
	 * @Description：  查询是否有 有效的住院证明
	 * @Author：tcj
	 * @CreateDate：2016-1-5  下午18:40
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	String queryProofInfo(String certificatesNo) throws Exception;
	/**
	 * 根据病历号查出多条记录
	 * @author  lyy
	 * @createDate： 2016年3月21日 下午1:05:32 
	 * @modifier lyy
	 * @modifyDate：2016年3月21日 下午1:05:32  
	 * @modifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	List<InpatientInfoNow> getQueryInfo(String medicalrecordId) throws Exception;
	/**
	 * 根据住院主表的病床号bedInfo查询病床使用表中的病床号bedId
	 * @author  lyy
	 * @createDate： 2016年3月21日 下午7:21:45 
	 * @modifier lyy
	 * @modifyDate：2016年3月21日 下午7:21:45  
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
	List<InpatientProof> getdengjiInfo(String medicalrecordId) throws Exception;
	/**
	 * 查询住院科室下拉框
	 * @author  tcj
	 * @version 1.0
	 * @throws Exception 
	 */
	List<SysDepartment> zyDeptCombobox() throws Exception;
	/**  
	 *  
	 * @Description：保存住院登记.担保金、预交金
	 * @Author：tuchuanjiang
	 * @CreateDate：2016-4-18
	 * @Modifier：
	 * @ModifyDate：  
	 * @param inpatientInfo：住院登记表实体类
	 * @param inpatientInPrepay：住院预交金实体类
	 * @param inpatientSurety：担保金实体类
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	void editInpatientInfo(InpatientInfoNow inpatientInfo,InpatientInPrepayNow inpatientInPrepay,InpatientSurety inpatientSurety) throws Exception;
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
	 * @throws Exception 
	 *
	 */
	String queryMedicalrecordIdDateByMid(String medicalrecordId) throws Exception;

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
	 * @throws Exception 
	 *
	 */
	List<Patient> getPatientInfo(String medicalrecordId) throws Exception;

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
	 * @throws Exception 
	 *
	 */
	String getPaykind(String unit) throws Exception;
}
