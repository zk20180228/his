package cn.honry.inner.inpatient.info.dao;

import java.io.IOException;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import cn.honry.base.bean.model.BusinessBedward;
import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.BusinessFrequency;
import cn.honry.base.bean.model.BusinessIcd10;
import cn.honry.base.bean.model.BusinessMedicalGroupInfo;
import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.DrugUndrug;
import cn.honry.base.bean.model.InpatientConsultation;
import cn.honry.base.bean.model.InpatientExecbill;
import cn.honry.base.bean.model.InpatientFeeInfoNow;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientItemListNow;
import cn.honry.base.bean.model.InpatientKind;
import cn.honry.base.bean.model.InpatientMedicineListNow;
import cn.honry.base.bean.model.InpatientOrderNow;
import cn.honry.base.bean.model.InpatientSurety;
import cn.honry.base.bean.model.InsuranceSiitem;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.UndrugZtinfo;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.EntityDao;
import cn.honry.inner.inpatient.info.vo.InfoInInterVo;
import cn.honry.inner.inpatient.info.vo.InpatientInfoInInterVo;
import cn.honry.inner.vo.FixedChargeVo;

/**
 * ClassName: InpatientInfoDAO 
 * @Description: 住院登记表业务逻辑接口
 * @author lt
 * @date 2015-6-24
 */
/**
 * @author Administrator
 *
 */
public interface InpatientInfoInInterDAO extends EntityDao<InpatientInfoNow>{


	/**
	 * @Description:根据病历号取到住院登记信息
	 * @Author：  lt
	 * @CreateDate： 2015-7-1
	 * @param @param id
	 * @param @return   
	 * @return InpatientInfo  
	 * @version 1.0
	**/
	InpatientInfoNow queryByMedical(String medicalNo);
	
	/**
	 * @Description:根据id查询患者信息
	 * @Author：  zhangjin
	 * @CreateDate： 2016-1-6
	 * @param @return   
	 * @return  
	 * @version 1.0
	**/
	InpatientInfoNow querNurseCharge(String no);
	
	/**
	 * @Description:获取患者树(护士站收费)
	 * @Author： zhangjin
	 * @CreateDate： 2015-1—5
	 * @param @param 
	 * @return 
	 * @version 1.0
	**/
	List<InfoInInterVo> treeNurseCharge(String deptId,InpatientInfoNow inpatientInfo,String a,String startTime,String endTime);

	/**  
	 *  
	 * @Description：根据住院流水号查询患者信息
	 * @Author：zhangjin
	 * @CreateDate：2016-1-18
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws IOException 
	 *
	 */
	 List<InpatientInfoNow> queryNurseChargeInpinfo(String no,String dept);

	 
	 /**  
		 *  
		 * @Description： 根据科室编号和住院号查询住院患者信息
		 * @Author：tfs
		 * @CreateDate：2016-3-21 上午11:12:01  
		 * @Modifier：
		 * @ModifyDate：2015-3-21 上午11:12:01  
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		List<InpatientInfoNow> getInfoByDeptCodeAndMid(String deptCode,String medicalrecordId);
		
		/**  
		 *  
		 * @Description： 根据科室编号查询住院患者信息
		 * @Author：tfs
		 * @CreateDate：2016-3-5 下午14:12:01  
		 * @Modifier：
		 * @ModifyDate：2015-3-5 下午14:12:01  
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		List<InpatientInfoNow> getInfoByDeptCode(String deptCode);
	 
		/**  
		 * @Description： 根据住院流水号查询患者
		 * @Author：tcj
		 * @CreateDate：2016-1-5  下午18:40
		 * @ModifyRmk：  
		 * @version 1.0
		 */
		InpatientInfoNow queryByInpatientNot(String inpatientNo);

		/**  
		 *  
		 * @Description：  根据id  user/dept 获得住院信息
		 * @Author：aizhonghua
		 * @CreateDate：2015-7-6 上午09:02:25  
		 * @Modifier：aizhonghua
		 * @ModifyDate：2015-7-6 上午09:02:25  
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		List<InpatientInfoNow> getInfo(String juri,String id, String no, String sTime,String eTime);

		/**  
		 *  
		 * @Description：  根据证件类型和证件号获得患者id
		 * @Author：aizhonghua
		 * @CreateDate：2015-7-6 上午09:25:26  
		 * @Modifier：aizhonghua
		 * @ModifyDate：2015-7-6 上午09:25:26  
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		String getIdByNo(String certificatesType,String certificatesNo);
		
		/**  
		 *  
		 * @Description：  用病历号查询当前住院的患者信息(id是病历号)
		 * @param:id
		 * @Author：zpty
		 * @ModifyDate：2015-8-20
		 * @ModifyRmk： 
		 * @version 1.0
		 *
		 */
		InpatientInfoNow getPeopleById(String id);
		/**  
		 * 通过病历号查询患者的在院状态
		 * @Author：tcj
		 * @ModifyRmk：  
		 * @version 1.0
		 */
		List<SysEmployee> employeeComboboxProof(String departmentCode,String q);
		/**
		 * 查询员工map（code，name）
		 * @author tcj
		 * @version 1.0
		 */
		List<SysEmployee> queryEmpMapPublic();
		/**
		 * 查询科室map（code，name）
		 * @author tcj
		 * @version 1.0
		 */
		List<SysDepartment> queryDeptMapPublic();
		/**
		 * 查询合同单位List（code，name）
		 * @author tcj
		 * @version 1.0
		 */
		List<BusinessContractunit> queryContractunitListForcombobox();
		/**
		 * 查询合同单位List（code，name）
		 * @author tcj
		 * @version 1.0
		 */
		List<BusinessDictionary> queryDictionaryListForcomboboxPublic(String type, String encode);

		/**
		 * 查询用户List（code，name）
		 * @author tcj
		 * @version 1.0
		 */
		List<User> queryUserListPublic();
		/**
		 * 查询频次List（code，name）
		 * @author tcj
		 * @version 1.0
		 */
		List<BusinessFrequency> queryFrequencyListPublic();
		
		/**  
		 *  
		 * @Description： 得到对应护士站（病区）信息
		 * @Author：yeguanqun
		 * @CreateDate：2015-12-09   
		 * @version 1.0
		 *
		 */
		List<BusinessBedward> findNurseCellCode(String inpatientNo);
		/**  
		 *  
		 * @Description： 得到患者会诊申请信息
		 * @Author：yeguanqun
		 * @CreateDate：2016-4-6   
		 * @version 1.0
		 *
		 */
		List<InpatientConsultation> findInpatientConsultation(String inpatientNo);
		
		/**  
		 *  
		 * @Description： 得到药物执行单树医嘱分类
		 * @Author：yeguanqun
		 * @CreateDate：2015-12-09   
		 * @version 1.0
		 *
		 */
		List<InpatientKind> treeDrugExe();
		/**  
		 *  
		 * @Description： 查询医嘱执行单信息
		 * @Author：yeguanqun
		 * @CreateDate：2015-12-09   
		 * @version 1.0
		 *
		 */
		List<InpatientExecbill> queryDocAdvExe(String ids,String billName);
		/**  
		 *  
		 * @Description： 得到分管患者树
		 * @Author：yeguanqun
		 * @CreateDate：2015-12-09   
		 * @version 1.0
		 *
		 */
		List<InpatientInfoNow> findTree(String id);
		
		/**
		 * @Description:获取患者信息
		 * @Author： yeguanqun
		 * @CreateDate： 2015-12-29
		 * @param entity：患者信息实体类
		 * @return List<InpatientInfo>  
		 * @version 1.0
		**/
		List<InpatientInfoInInterVo> getInpatientInfo(InpatientInfoNow entity);
		/**  
		 *  
		 * @Description：根据住院流水号查询住院登记表信息
		 * @Author：yeguanqun
		 * @param inpatientNo：患者住院流水号
		 * @CreateDate：2016-2-20   
		 * @version 1.0
		 *
		 */
		List<InpatientInfoNow> queryInpatientInfo(String inpatientNo);
		/**  
		 *  
		 * @Description： 查询药品资料
		 * @Author：yeguanqun
		 * @param id：药品资料表id
		 * @CreateDate：2016-2-24   
		 * @version 1.0
		 *
		 */
		List<DrugInfo> queryDrugInfo(String id);
		/**  
		 *  
		 * @Description： 查询医疗组对应医生表
		 * @Author：yeguanqun
		 * @CreateDate：2016-2-24   
		 * @version 1.0
		 *
		 */
		BusinessMedicalGroupInfo queryMedicalGroupInfo(String docCode);  
		/**  
		 *  
		 * @Description： 查询合同单位维护表-合同单位比例
		 * @Author：yeguanqun
		 * @CreateDate：2016-2-24   
		 * @version 1.0
		 *
		 */
		List<BusinessContractunit> queryContractunit(String pactCode);
		/**  
		 *  
		 * @Description： 查询药品明细资料
		 * @Author：yeguanqun
		 * @CreateDate：2016-2-24   
		 * @version 1.0
		 *
		 */
		List<InpatientMedicineListNow> queryInpatientMedicineList(String recipeNo);
		/**  
		 *  
		 * @Description： 查询药品明细资料
		 * @Author：yeguanqun
		 * @CreateDate：2016-5-16   
		 * @version 1.0
		 *
		 */
		List<InpatientMedicineListNow> queryInpatientMedicineList(String recipeNo,Integer sequenceNo);
		/**  
		 *  
		 * @Description： 查询非药品明细资料
		 * @Author：yeguanqun
		 * @CreateDate：2016-2-24   
		 * @version 1.0
		 *
		 */
		List<InpatientItemListNow> queryInpatientItemList(String recipeNo);
		/**  
		 *  
		 * @Description： 查询非药品明细资料
		 * @Author：yeguanqun
		 * @CreateDate：2016-5-16   
		 * @version 1.0
		 *
		 */
		List<InpatientItemListNow> queryInpatientItemList(String recipeNo,Integer sequenceNo);
		/**  
		 *  
		 * @Description： 查询非药品明细资料
		 * @Author：yeguanqun
		 * @CreateDate：2016-2-24   
		 * @version 1.0
		 *
		 */
		List<InpatientFeeInfoNow> queryInpatientFeeInfo(String recipeNo);
		/**  
		 *  
		 * @Description： 查询社保项目信息表（自付比例）
		 * @Author：yeguanqun
		 * @CreateDate：2016-2-24   
		 * @version 1.0
		 *
		 */
		List<InsuranceSiitem> queryInsuranceSiitem(String itemCode);
		/**  
		 *  
		 * @Description：查询住院担保金表中的担保金额
		 * @Author：yeguanqun
		 * @param inpatientNo：患者住院流水号
		 * @CreateDate：2016-2-20   
		 * @version 1.0
		 *
		 */
		List<InpatientSurety> querysuretyCost(String inpatientNo);
		/**
		 * @Description:查询登录密码-校验登录密码
		 * @Author：  yeguanqun
		 * @CreateDate： 2016-3-4
		 * @param    
		 * @return void  
		 * @version 1.0
		**/
		List<User> confirmPassword(User user);
		/**  
		 *  
		 * @Description： 查询非药品资料
		 * @Author：yeguanqun
		 * @CreateDate：2015-12-22   
		 * @version 1.0
		 *
		 */
		List<DrugUndrug> queryNoDrugInfo(String id);
		List<BusinessIcd10> queryICD();
		/**  
		 *  
		 * @Description：查询当前病区
		 * @Author：zhangjin
		 * @CreateDate：2016-10-25 
		 * @version 1.0
		 *
		 */
		List<InfoInInterVo> treeNursegetDept(String deptId);
		
		/**
		 * @Deprecated 根据组套id查询明细数据
		 * @Author：GH
		 * @CreateDate：2016年11月7日10:21:40
		 * @version 1.0
		 */
		public List<UndrugZtinfo> queryUndrugById(String undrugId) ;
		
		/**
		 * @Deprecated 根据非药品id查询明细数据
		 * @Author：zhuxiaolu
		 * @CreateDate：2016年11月7日10:21:40
		 * @version 1.0
		 */
		List<DrugUndrug> queryUnDrug(String id);
		
		/** 住院患者列表(根据科室，病历号/住院号/就诊卡号，挂号时间段)
		* @Title: inpatientList 住院患者列表(根据科室，病历号/住院号/就诊卡号，挂号时间段)
		* @Description: 住院患者列表(根据科室，病历号/住院号/就诊卡号，挂号时间段)
		* @param deptCode 科室code
		* @param no 病历号/住院号
		* @param sTime 开始时间
		* @param eTime 结束时间
		* @param rows
		* @param page
		* @author dtl 
		* @date 2016年11月10日
		*/
		List<InpatientInfoNow> inpatientList(String deptCode, String no, String sTime,
				String eTime, String rows, String page);
		
		/** 住院患者列表(根据科室，病历号/住院号/就诊卡号，挂号时间段)
		* @Title: inpatientTotal 住院患者列表(根据科室，病历号/住院号/就诊卡号，挂号时间段)
		* @Description: 住院患者列表(根据科室，病历号/住院号/就诊卡号，挂号时间段)
		* @param deptCode 科室code
		* @param no 病历号/住院号
		* @param sTime 开始时间
		* @param eTime 结束时间
		* @author dtl 
		* @date 2016年11月10日
		*/
		int inpatientTotal(String deptCode, String no, String sTime, String eTime);
		/**
		 * @Deprecated 查询合同单位
		 * @Author：donghe
		 * @CreateDate：2017年1月16日
		 * @version 1.0
		 */
		List<BusinessContractunit> queryContractunit();

		InpatientInfoNow getInpatientInfoNow(DetachedCriteria detachedCriteria);

		List<FixedChargeVo> getInpatientBedInfoNow(String string);

		List<FixedChargeVo> getInpatientBedInfoTend(String string);

		List<InpatientOrderNow> getOrders(DetachedCriteria detachedCriteria);
		
		/**
		 * 根据药品code获取药品部分信息(包装单位、包装数量、价格、药品性质、规格、药品类别、名称等)
		 * @param itemCode
		 * @return
		 */
		DrugInfo getDrugInfoByCode(String itemCode);
		
		/**
		 * 获取非药品明细表(或药品明细表)中某处方号最大的处方内流水号
		 * @param recipeNo 处方号
		 * @param n(1-药品明细表,2-非药品明细表)
		 * @return
		 */
		int getMaxSequenceNo(String recipeNo,int n);
		/**
		 * 根据就诊卡号或者身份证号查询
		 */
		String queryMedicalrecordId(String IdcardOrRe);

		List<FixedChargeVo> getInpatientBedInfo();
		
		/**@see 根据科室code获得所在院区
		 * @author conglin
		 * @param deptCode 科室code
		 * @return String 院区Code
		 */
		String getDeptArea(String deptCode);
}
