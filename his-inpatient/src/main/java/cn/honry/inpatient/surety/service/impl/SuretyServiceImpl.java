package cn.honry.inpatient.surety.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.InpatientInPrepayNow;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientSurety;
import cn.honry.base.bean.model.InvoiceUsageRecord;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.inner.outpatient.medicineList.dao.MedicineListInInterDAO;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.inpatient.surety.dao.SuretyDAO;
import cn.honry.inpatient.surety.service.SuretyService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.ShiroSessionUtils;

/**  
 *  
 * @className：SuretyServiceImpl
 * @Description：  担保金ServiceImpl
 * @Author：aizhonghua
 * @CreateDate：2016-2-24 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-2-24 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Service("suretyService")
@Transactional
@SuppressWarnings({ "all" })
public class SuretyServiceImpl implements SuretyService{

	@Autowired
	@Qualifier(value = "suretyDAO")
	private SuretyDAO suretyDAO;

	@Autowired
	@Qualifier(value = "medicineListInInterDAO")
	private MedicineListInInterDAO medicineListInInterDAO;
	
	/**  
	 *  
	 * @Description：  根据id获得信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-2-24 上午11:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-2-24 上午11:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public InpatientSurety get(String id) {
		return suretyDAO.get(id);
	}

	@Override
	public void removeUnused(String arg0) {
		
	}
	

	@Override
	public void saveOrUpdate(InpatientSurety surety) {
		
	}

	/**
	 *
	 * @Description：保存担保金
	 * @Author：aizhonghua
	 * @CreateDate：2016年4月11日 下午4:05:36 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0
	 * @param surety 担保金对象
	 *
	 */
	@Override
	public String save(InpatientSurety surety) {
		User createUser = ShiroSessionUtils.getCurrentUserFromShiroSession();
		SysDepartment createDept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		Map<String, String> queryFinanceInvoiceNoByNum = medicineListInInterDAO.queryFinanceInvoiceNoByNum(createUser.getAccount(),"04", 1);
		if("error".equals(queryFinanceInvoiceNoByNum.get("resMsg"))){
			return queryFinanceInvoiceNoByNum.get("resCode");
		}
		if(createDept!=null){
			String invoiceNum=queryFinanceInvoiceNoByNum.get("resCode");
			surety.setHappenNo(Integer.parseInt(suretyDAO.getSeqByNameorNum("SEQ_INPREPAY_HAPPENNO",5)));//发生序号
			surety.setState(1);
			surety.setDeptCode(createDept.getDeptCode());
			surety.setCreateUser(createUser.getAccount());
			surety.setCreateDept(createDept.getDeptCode());
			surety.setInvoiceNo(invoiceNum);
			surety.setCreateTime(new Date());
			suretyDAO.save(surety);
			OperationUtils.getInstance().conserve(null, "住院担保金", "INSERT_INTO", "T_INPATIENT_SURETY", OperationUtils.LOGACTIONINSERT);
		
			//修改发票和添加发票使用记录
			SysEmployee employee = medicineListInInterDAO.queryEmployee(createUser.getAccount());
			this.updateAndSaveInvoice(employee, invoiceNum, "04", createDept,queryFinanceInvoiceNoByNum.get(invoiceNum));
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
	 * @Description：查询担保金信息 - 分页查询 - 获得总条数
	 * @Author：aizhonghua
	 * @CreateDate：2016年4月12日 上午9:47:41 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0
	 * @param：inpatientNo住院号
	 * @return 总条数
	 */
	@Override
	public int getSuretyTotal(String inpatientNo) {
		return suretyDAO.getSuretyTotal(inpatientNo);
	}

	/**
	 *
	 * @Description：查询担保金信息 - 分页查询 - 获得信息
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
	public List<InpatientSurety> getSuretyPage(String inpatientNo, String page,String rows) {
		return suretyDAO.getSuretyPage(inpatientNo,page,rows);
	}

	@Override
	public void removeSuretyByids(String[] ids) {
		Double a = 0.0;
		String inpatientNo="";
		for(String id:ids){
			InpatientSurety ins= suretyDAO.get(id);
			inpatientNo=ins.getInpatientNo();
			//更新担保金状态为返还
		//	ins.setSuretyCost(a);
			ins.setState(0);
		   	suretyDAO.updateInpatientSurety(ins);
//			suretyDAO.save(ins);
			OperationUtils.getInstance().conserve(ins.getId(), "担保金返回", "UPDATE", "T_INPATIENT_SURETY", OperationUtils.LOGACTIONUPDATE);
		}
	}

	
}
