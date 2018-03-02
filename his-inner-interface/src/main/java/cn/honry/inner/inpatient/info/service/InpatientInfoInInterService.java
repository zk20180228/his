package cn.honry.inner.inpatient.info.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.BusinessFrequency;
import cn.honry.base.bean.model.BusinessIcd10;
import cn.honry.base.bean.model.DrugApplyout;
import cn.honry.base.bean.model.InpatientDrugbilldetail;
import cn.honry.base.bean.model.InpatientExecbill;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientKind;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.base.service.BaseService;
import cn.honry.inner.inpatient.info.vo.FeeInInterVo;
import cn.honry.utils.TreeJson;


/**
 * ClassName: InpatientInfoService 
 * @Description: 住院登记表业务逻辑接口
 * @author lt
 * @date 2015-6-24
 */
public interface InpatientInfoInInterService extends BaseService<InpatientInfoNow> {

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
	 * 根据id查询患者信息
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: InpatientInfoNow 住院实体
	 *
	 */
	InpatientInfoNow querNurseCharge(String no);
	
	
	/**  
	 *  
	 * @Description： 获取患者树(护士站收费)
	 * @Author：zhangjin
	 * @CreateDate：2015-1-5  
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<TreeJson> treeNurseCharge(String deptId,String id,InpatientInfoNow inpatientInfo,String a,String startTime,String endTime);

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
		 * 根据科室ID查询员工List（id可以为空，即查询全部员工）
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
		 * 查询证件类型List（code，name）
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
		
		List<TreeJson> findTree(String treeAll, String deptTypes);
		/**  
		 *  
		 * @Description： 得到患者树
		 * @Author：yeguanqun
		 * @param id：树节点id   
		 * @CreateDate：2015-12-12   
		 * @version 1.0
		 *
		 */
		public List<TreeJson> treeInpatient(String id);
		/**  
		 *  
		 * @Description： 非药物执行单树
		 * @Author：yeguanqun
		 * @CreateDate：2015-12-14   
		 * @version 1.0
		 *
		 */
		public List<InpatientDrugbilldetail> queryInpatientDrugbilldetail(InpatientDrugbilldetail inpatientDrugbilldetail);
		/**  
		 *  
		 * @Description： 药物执行单树
		 * @Author：yeguanqun
		 * @param id：树节点id  
		 * @CreateDate：2015-12-12   
		 * @version 1.0
		 *
		 */
		public List<TreeJson> treeDrugExes(String id);
		/**  
		 *  
		 * @Description： 医嘱类别表
		 * @Author：yeguanqun
		 * @CreateDate：2015-12-18   
		 * @version 1.0
		 *
		 */
		public List<InpatientKind> treeDrugExe();
		/**  
		 *  
		 * @Description： 非药物执行单树
		 * @Author：yeguanqun
		 * @param id：树节点id  
		 * @CreateDate：2015-12-14   
		 * @version 1.0
		 *
		 */
		public List<TreeJson> treeNoDrugExe(String id);
		
		/**  
		 *  
		 * @Description： 查询执行单信息
		 * @Author：yeguanqun
		 * @param  
		 * @CreateDate：2015-12-14   
		 * @version 1.0
		 *
		 */
		public List<InpatientExecbill> queryDocAdvExe(String ids,String billName);
		
		/**
		 * @Description:收费
		 * @Author： yeguanqun
		 * @CreateDate： 2016-2-19
		 * @param feeVoList：费用结算vo集合
		 * @version 1.0
		**/
		public Map<String, Object> saveInpatientFeeInfo(List<FeeInInterVo> feeVoList);
		
		/**
		 * 计算收费项目的金额(公费金额、自费金额、优惠金额等)
		 * @param feeInVo
		 * @return
		 */
		public FeeInInterVo getCosts(FeeInInterVo fee);
		
		/**
		 * @Description:查询登录密码-校验登录密码
		 * @Author：  yeguanqun
		 * @CreateDate： 2016-3-4
		 * @param user：用户信息实体类   
		 * @return void  
		 * @version 1.0
		**/
		List<User> confirmPassword(User user);
		/**
		 * @Description:获取患者信息
		 * @Author： yeguanqun
		 * @CreateDate： 2016-4-15
		 * @param inpatientNo：患者住院流水号
		 * @return List<InpatientInfo>  
		 * @version 1.0
		**/
		List<InpatientInfoNow> queryInpatientInfo(String inpatientNo);
		/**
		 *  * 判断患者是否欠费  
		 * 欠费计算公式     余额或预交金+担保金额<警戒线金额
		 * @author ljl
		 * @param inpatientInfo  患者登记信息
		 * @param totCost  本次费用总额
		 * @return
		 */
		public  boolean isArrearageByInpatientInfo(InpatientInfoNow inpatientInfo,double totCost);

		List<BusinessIcd10> queryICD();
		
		/**
		 * @Description:根据住院流水号取到住院登记信息
		 * @Author：  lt
		 * @CreateDate： 2015-7-1
		 * @param @param id
		 * @param @return   
		 * @return InpatientInfo  
		 * @version 1.0
		**/
		InpatientInfoNow queryByInpatientNot(String inpatientNo);

		List<InpatientInfoNow> queryNurseChargeByMedicalNo(String inpatientNo,
				String deptId);
		
		/**
		 * 反交易收费	
		* @Title: reverseTran
		* @Description: 
		* @param feeVoList  某一个患者的所有退费项目 
		* @return 
		* @date 2016年5月16日下午5:48:02
		 */
		Map<String,Object> reverseTran(List<FeeInInterVo> feeVoList);
		/**
		 * 根据就诊卡号或者身份证号查询
		 */
		String queryMedicalrecordId(String IdcardOrRe);

		
		/**  
		 * 
		 * 根据sequence,获取applyNumber
		 * @Author:zxl
		 * @CreateDate: 2017年7月4日 下午5:45:22 
		 * @Modifier: zxl
		 * @ModifyDate: 2017年7月4日 下午5:45:22 
		 * @ModifyRmk:  
		 * @version: V1.0
		 * @param:drugAppLyout 药品出库申请序列
		 * @throws:
		 * @return: applyNumber
		 *
		 */
		String getSequece(String drugAppLyout);

		/**  
		 * 
		 * 保存药品出库申请信息
		 * @Author:zxl
		 * @CreateDate: 2017年7月4日 下午5:45:22 
		 * @Modifier: zxl
		 * @ModifyDate: 2017年7月4日 下午5:45:22 
		 * @ModifyRmk:  
		 * @version: V1.0
		 * @param:applyout 药品出库申请实体
		 * @throws:
		 * @return: 
		 *
		 */
		void save(DrugApplyout applyout);
}
