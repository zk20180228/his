package cn.honry.inpatient.inprePay.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.InpatientInPrepay;
import cn.honry.base.bean.model.InpatientInPrepayNow;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InvoiceUsageRecord;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.inner.baseinfo.code.dao.CodeInInterDAO;
import cn.honry.inner.outpatient.medicineList.dao.MedicineListInInterDAO;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.inpatient.info.dao.InpatientInfoDAO;
import cn.honry.inpatient.inprePay.dao.InprePayDAO;
import cn.honry.inpatient.inprePay.service.InprePayService;
import cn.honry.inpatient.inprePay.vo.AcceptingGold;
import cn.honry.inpatient.inprePay.vo.PatientVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.ShiroSessionUtils;

/**  
 *  
 * @className：InprePayService 
 * @Description：  住院预交金ServiceImpl
 * @Author：aizhonghua
 * @CreateDate：2016-3-09 下午18:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-3-09 下午18:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Service("inprePayService")
@Transactional
@SuppressWarnings({ "all" })
public class InprePayServiceImpl implements InprePayService{

	@Autowired
	@Qualifier(value = "inprePayDAO")
	private InprePayDAO inprePayDAO;
	@Autowired
	@Qualifier(value = "inpatientInfoDAO")
	private InpatientInfoDAO inpatientInfoDAO;
	@Autowired
	@Qualifier(value="innerCodeDao")
	private CodeInInterDAO innerCodeDao;
	@Autowired
	@Qualifier(value = "medicineListInInterDAO")
	private MedicineListInInterDAO medicineListInInterDAO;

	@Override
	public InpatientInPrepayNow get(String id) {
		return inprePayDAO.get(id);
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	/**
	 *
	 * @Description：保存预存金
	 * @Author：aizhonghua
	 * @CreateDate：2016年4月11日 下午4:05:36 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0
	 * @param inPrepay 预存金对象
	 * @throws Exception 
	 *
	 */
	@Override
	public String save(InpatientInPrepayNow inPrepay) throws Exception {
		User createUser = ShiroSessionUtils.getCurrentUserFromShiroSession();
		SysDepartment dept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		Map<String, String> queryFinanceInvoiceNoByNum = medicineListInInterDAO.queryFinanceInvoiceNoByNum(createUser.getAccount(),"04", 1);
		if("error".equals(queryFinanceInvoiceNoByNum.get("resMsg"))){
			return queryFinanceInvoiceNoByNum.get("resCode");
		}
		if(dept.getDeptCode()!=null){
			String invoiceNum=queryFinanceInvoiceNoByNum.get("resCode");
			inPrepay.setTransType(1);//交易类型,1正交易，2反交易
			inPrepay.setHappenNo(Integer.parseInt(inprePayDAO.getSeqByNameorNum("SEQ_INPREPAY_HAPPENNO",5)));//发生序号 happen_no         NUMBER(5),
			inPrepay.setDeptCode(dept.getDeptCode());//科室代码 dept_code         VARCHAR2(50),
			inPrepay.setDeptName(dept.getDeptName());
			inPrepay.setReceiptNo(invoiceNum);
			inPrepay.setBalanceState(0);//结算标志 0:未结算；1:已结算 2:已结转 balance_state     NUMBER(1),
			inPrepay.setPrepayState(0);//预交金状态0:收取；1:作废;2:补打,3结算召回作废 prepay_state      NUMBER(1),
			inPrepay.setTransFlag(0);//0非转押金，1转押金，2转押金已打印 trans_flag       NUMBER(1),
			inPrepay.setPrintFlag(0);//打印标志 print_flag        NUMBER(1),
			inPrepay.setExtFlag(1);//正常收取 1 结算召回 2  ext_flag        NUMBER(1),
			inPrepay.setCreateUser(createUser.getAccount());
			inPrepay.setCreateDept(dept.getDeptCode());
			inPrepay.setCreateTime(new Date());
			inPrepay.setAreaCode(this.getDeptArea(dept.getDeptCode()));
			inPrepay.setHospitalId(HisParameters.CURRENTHOSPITALID);
			inprePayDAO.save(inPrepay);
			OperationUtils.getInstance().conserve(null, "住院预存金", "INSERT_INTO", "T_INPATIENT_INPREPAY", OperationUtils.LOGACTIONINSERT);
			InpatientInfoNow inpatientInfo=inprePayDAO.queryInpatientInfoByInNo(inPrepay.getInpatientNo());
			if(inpatientInfo!=null){
				inpatientInfo.setPrepayCost(inpatientInfo.getPrepayCost()+inPrepay.getPrepayCost());//更新住院主表中的预交金额(未结)
				inpatientInfo.setFreeCost(inpatientInfo.getFreeCost()+inPrepay.getPrepayCost());//更新住院主表中的余额(未结)
				inpatientInfo.setUpdateUser(createUser.getAccount());
				inpatientInfo.setUpdateTime(new Date());
				inprePayDAO.update(inpatientInfo);
			}

		
			
			//修改发票和添加发票使用记录
			SysEmployee employee = medicineListInInterDAO.queryEmployee(createUser.getAccount());
			this.updateAndSaveInvoice(employee, invoiceNum, "04", dept,queryFinanceInvoiceNoByNum.get(invoiceNum));
			return "success";
		}else{
			return "error";
		}
		
	}
	
	/**
	 * @Description  修改发票和添加发票使用记录
	 * @author  zxl
	 * @createDate： 2016年11月11日 上午9:26:36 
	 * @modifier 
	 * @modifyDate：
	 * @param：  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	public void updateAndSaveInvoice(SysEmployee employee,String invoiceNo,String invoiceType,SysDepartment department,String invoiceNoId){
		//修改发票
		medicineListInInterDAO.saveInvoiceFinance(employee. getJobNo(),invoiceNo,invoiceType,invoiceNoId);
		//添加发票使用记录
		InvoiceUsageRecord usageRecord = new InvoiceUsageRecord();
		usageRecord.setId(null);
		usageRecord.setUserId(employee.getJobNo());
		usageRecord.setUserCode(employee.getCode());
		usageRecord.setUserType(2);
		usageRecord.setInvoiceNo(invoiceNo);
		usageRecord.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		usageRecord.setCreateDept(department.getDeptCode());
		usageRecord.setCreateTime(DateUtils.getCurrentTime());
		usageRecord.setUserName(employee.getName());
		usageRecord.setInvoiceUsestate(1);
		medicineListInInterDAO.save(usageRecord);
	}
	
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
	@Override
	public PatientVo findPatientByIdcardNo(String idcardNo,String inState) throws Exception {
		return inprePayDAO.findPatientByIdcardNo(idcardNo,inState);
	}

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
	@Override
	public boolean checkIdcardNo(String idcardNo) throws Exception {
		return inprePayDAO.checkIdcardNo(idcardNo);
	}

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
	@Override
	public int getInprePayTotal(String inpatientNo) throws Exception {
		return inprePayDAO.getInprePayTotal(inpatientNo);
	}

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
	@Override
	public List<InpatientInPrepayNow> getInprePayPage(String inpatientNo,String page, String rows) throws Exception {
		return inprePayDAO.getInprePayPage(inpatientNo,page,rows);
	}

	/**
	 *
	 * @Description：获得银行Map
	 * @Author：aizhonghua
	 * @CreateDate：2016年4月12日 上午9:47:41 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0：
	 * @return:银行Map
	 *
	 */
	@Override
	public Map<String, String> getCodeBankMap() {
		Map<String, String> retMap = new HashMap<String, String>();
		List<BusinessDictionary> list = innerCodeDao.getDictionary("bank");
		if(list!=null&&list.size()>0){
			for(BusinessDictionary bank : list){
				retMap.put(bank.getEncode(), bank.getName());
			}
		}
		return retMap;
	}

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
	@Override
	public List<PatientVo> findPatientByInpatientNo(String medicale) throws Exception {
		return inprePayDAO.findPatientByInpatientNo(medicale);
	}
	/**
	 *
	 * @Description：返还预交金并且更新住院主表中的余额等
	 * @Author：tangfeishuai
	 * @CreateDate：2016年5月27日 下午3:53:37 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public void removeInprePayByids(String[] ids) throws Exception {
		Double a = 0.0;
		String inpatientNo="";
		for(String id:ids){
			InpatientInPrepayNow inp= inprePayDAO.get(id);
			inpatientNo=inp.getInpatientNo();
			//得到总的要返还的预交金
			a=a+inp.getPrepayCost();
			//更新预交金状态为返还
			inp.setPrepayState(1);
			inprePayDAO.updateInpatientInPrepayNow(inp);
			OperationUtils.getInstance().conserve(inp.getId(), "预交金返回", "UPDATE", "T_INPATIENT_INPREPAY_NOW", OperationUtils.LOGACTIONUPDATE);
		}
		InpatientInfoNow i=inprePayDAO.findPatientByInpNo(inpatientNo);
		//根据返还的预交金更新住院主表的余额
		Double b=i.getFreeCost()-a;
		//根据返还的预交金更新住院主表的预交金额
		Double c=i.getPrepayCost()-a;
		i.setFreeCost(b);
		i.setPrepayCost(c);
		inpatientInfoDAO.updateInpatientInfoNow(i);
		OperationUtils.getInstance().conserve(i.getId(), "患者住院信息", "UPDATE", "T_INPATIENT_INFO_NOW", OperationUtils.LOGACTIONUPDATE);
	}

	/**
	 *
	 * @Description：通过住院号获得患者信息
	 * @Author：tangfeishuai
	 * @CreateDate：2016年5月27日 下午3:53:37 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	@Override
	public InpatientInfoNow findPatientByInpNo(String inpatientNo) throws Exception {
		InpatientInfoNow inp=inprePayDAO.findPatientByInpNo(inpatientNo);
		return inp;
	}

	/**
	 *
	 * @Description：修改住院主表中的预交金余额
	 * @Author：tangfeishuai
	 * @CreateDate：2016年5月27日 下午3:53:37 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	@Override
	public void updatePatientByInpNo(String inpatientNo, Double prepayCost) throws Exception {
		InpatientInfoNow i=inprePayDAO.findPatientByInpNo(inpatientNo);
		if(i.getPrepayCost()==null){
			i.setPrepayCost(prepayCost);
		}else{
			i.setPrepayCost(i.getPrepayCost()+prepayCost);
		}
		if(i.getFreeCost()==null){
			i.setFreeCost(prepayCost);
		}else{
			i.setFreeCost(i.getFreeCost()+prepayCost);
		}
		inpatientInfoDAO.save(i);
		OperationUtils.getInstance().conserve(i.getId(), "患者住院信息", "UPDATE", "T_INPATIENT_INFO_NOW", OperationUtils.LOGACTIONUPDATE);
	}

	@Override
	public void saveOrUpdate(InpatientInPrepayNow arg0) {
		
	}

	@Override
	public InpatientInfoNow isStopAcountNow(String inpatientNo) throws Exception {
		return inprePayDAO.isStopAcountNow(inpatientNo);
	}

	@Override
	public Map<String, String> queryFinanceInvoiceNo(String account,String invoiceType) {
		return medicineListInInterDAO.queryFinanceInvoiceNo(account,invoiceType);
	}

	@Override
	public List<AcceptingGold> iReportzyyjj(String id) throws Exception {
		return inprePayDAO.iReportzyyjj(id);
	}

	@Override
	public String getDeptArea(String deptCode) throws Exception {
		return inprePayDAO.getDeptArea(deptCode);
	}
}
