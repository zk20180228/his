package cn.honry.finance.invoicereprint.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.InvoiceUsageRecord;
import cn.honry.base.bean.model.OutpatientFeedetailNow;
import cn.honry.base.bean.model.User;
import cn.honry.finance.invoicereprint.dao.InvocieReprintDao;
import cn.honry.finance.invoicereprint.service.InvoiceReprintService;
import cn.honry.finance.invoicereprint.vo.InvoiceReprintVO;
import cn.honry.inner.outpatient.medicineList.dao.MedicineListInInterDAO;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.ShiroSessionUtils;
@Service("invoiceReprintService")
@Transactional
@SuppressWarnings({ "all" })
public class InvoiceReprintServiceImpl implements InvoiceReprintService {
	@Autowired
	@Qualifier(value="invocieReprintDao")
	private InvocieReprintDao invocieReprintDao;
	@Autowired
	@Qualifier(value = "medicineListInInterDAO")
	private MedicineListInInterDAO medicineListInInterDAO;
	@Override
	public List<InvoiceReprintVO> getInvoiceBycode(String clinicCode,String invoiceNo) throws Exception {
		if(StringUtils.isBlank(invoiceNo)&&StringUtils.isBlank(clinicCode)){
			return new ArrayList<InvoiceReprintVO>();
		}
		return invocieReprintDao.getInvoiceVO(clinicCode, invoiceNo);
	}

	@Override
	public Map<String, String> reprint(String invoiceNo) {
		Map<String, String> map = new HashMap<String, String>();
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		try{
			String[] newInvoiceNo = {};
			String[] split = invoiceNo.split(",");
			String returnNo = "";
			//获取多张发票号
			Map<String, String> queryFinanceInvoiceNoByNum = medicineListInInterDAO.queryFinanceInvoiceNoByNum(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount(),HisParameters.OUTPATIENTINVOICETYPE,split.length);
			if("error".equals(queryFinanceInvoiceNoByNum.get("resMsg"))){
				throw new RuntimeException("INVOICE IS NOT ENOUGTH");
			}else{
				newInvoiceNo = queryFinanceInvoiceNoByNum.get("resCode").split(",");
			}
			for (int i=0;i<split.length;i++) {
				//更新相关表
				int updateInvoice = invocieReprintDao.updateInvoiceInfo(split[i], newInvoiceNo[i]);
				int updatePayMode = invocieReprintDao.updatePayMode(split[i], newInvoiceNo[i]);
				int updateInvoiceDetial = invocieReprintDao.updateInvoiceDetial(split[i], newInvoiceNo[i]);
				int updateFeeDetail = invocieReprintDao.updateFeeDetail(split[i], newInvoiceNo[i]);
				//修改发票
				medicineListInInterDAO.saveInvoiceFinance(user.getAccount(),newInvoiceNo[i],HisParameters.OUTPATIENTINVOICETYPE,queryFinanceInvoiceNoByNum.get(newInvoiceNo[i]));
				//添加发票使用记录
				InvoiceUsageRecord usageRecord = new InvoiceUsageRecord();
				usageRecord.setId(null);
				usageRecord.setUserId(user.getId());
				usageRecord.setUserCode(user.getAccount());
				usageRecord.setUserType(2);
				usageRecord.setInvoiceNo(newInvoiceNo[i]);
				usageRecord.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				usageRecord.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
				usageRecord.setCreateTime(DateUtils.getCurrentTime());
				usageRecord.setUserName(user.getName());
				usageRecord.setInvoiceUsestate(1);
				medicineListInInterDAO.save(usageRecord);
				if(StringUtils.isNotBlank(returnNo)){
					returnNo += ",";
				}
				returnNo += newInvoiceNo[i];
			}
			map.put("resMsg", "操作成功!");
			map.put("resCode", "success");
			map.put("newInvoiceNo",returnNo);
			return map;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<OutpatientFeedetailNow> getfee(String invoiceNo) throws Exception {
		return invocieReprintDao.getfee(invoiceNo);
	}

}
