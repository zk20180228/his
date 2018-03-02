package cn.honry.finance.invoiceStorage.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.FinanceInvoiceStorage;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.finance.invoiceStorage.dao.InvoiceStorageDao;
import cn.honry.finance.invoiceStorage.service.InvoiceStorageService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.ShiroSessionUtils;
@Service("invoiceStorageService")
@Transactional
@SuppressWarnings({ "all" })
public class InvoiceStorageServiceImpl implements InvoiceStorageService {
	@Autowired
	@Qualifier(value = "invoiceStorageDao")
	private InvoiceStorageDao invoiceStorageDao;
	@Override
	public FinanceInvoiceStorage get(String arg0) {
		return invoiceStorageDao.get(arg0);
	}

	@Override
	public void removeUnused(String arg0) {
		invoiceStorageDao.removeById(arg0);
	}

	@Override
	public void saveOrUpdate(FinanceInvoiceStorage arg0) {
		invoiceStorageDao.save(arg0);
	}

	@Override
	public List<BusinessDictionary> queryInvoiceType(String type, String encode) throws Exception{
		return invoiceStorageDao.queryInvoiceType(type, encode);
	}

	@Override
	public List<FinanceInvoiceStorage> queryInvoiceStorage(String page,
			String rows, FinanceInvoiceStorage fis)  throws Exception{
		return invoiceStorageDao.queryInvoiceStorage(page,rows,fis);
	}

	@Override
	public int getTotal(FinanceInvoiceStorage fis) throws Exception {
		return invoiceStorageDao.getTotal(fis);
	}

	@Override
	public String saveform(FinanceInvoiceStorage financeInvoiceStorage)  throws Exception{
		SysDepartment dept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		if(StringUtils.isBlank(financeInvoiceStorage.getId())){
			financeInvoiceStorage.setId(null);
			int startInt =Integer.parseInt(financeInvoiceStorage.getInvoiceStartno().substring(1, 14));
			int endInt =Integer.parseInt(financeInvoiceStorage.getInvoiceEndno().substring(1, 14));
			String type=financeInvoiceStorage.getInvoiceType();
			List<FinanceInvoiceStorage> invoiceList = invoiceStorageDao.queryFISByType(type);
			for(FinanceInvoiceStorage f:invoiceList){
				int start=Integer.parseInt(f.getInvoiceStartno().substring(1, 14));
				int end=Integer.parseInt(f.getInvoiceEndno().substring(1, 14));
				if((startInt >=start) && (startInt<=end) &&(endInt>=start) && (endInt<=end) ){
					return "used";
				}else if((startInt >=start) && (startInt<=end) && (endInt>=end) ){
					return "used";
				}else if((startInt <=start) && (endInt>start) && ( endInt<=end)){
					return "used";
				}else if((startInt <=start) && (endInt>=end)){
					return "used";
				}				
			}
			Integer lenu = financeInvoiceStorage.getInvoiceStartno().length();
			String startNoStrenu = financeInvoiceStorage.getInvoiceStartno().toString().substring(0, 1);
			String startNoStru = financeInvoiceStorage.getInvoiceStartno().toString().substring(1, financeInvoiceStorage.getInvoiceStartno().toString().length());
			int useNou = Integer.parseInt(startNoStru)-1;
			String strlen=useNou+"";
			int length = strlen.toString().length();
			for (int j = 0; j < lenu-length-1; j++) {
				strlen="0"+strlen;
			}
			String strAll=startNoStrenu+strlen;
			financeInvoiceStorage.setInvoiceUsedno(strAll);
			financeInvoiceStorage.setCreateTime(DateUtils.getCurrentTime());
			financeInvoiceStorage.setCreateUser(user.getAccount());
			financeInvoiceStorage.setCreateDept(dept.getDeptCode());
			invoiceStorageDao.save(financeInvoiceStorage);
			return "success";
		}else{
			int startInt =Integer.parseInt(financeInvoiceStorage.getInvoiceStartno().substring(1, 14));
			int endInt =Integer.parseInt(financeInvoiceStorage.getInvoiceEndno().substring(1, 14));
			String type=financeInvoiceStorage.getInvoiceType();
			List<FinanceInvoiceStorage> invoiceList = invoiceStorageDao.queryFISByType(type);
			int index = invoiceList.indexOf(financeInvoiceStorage);
			invoiceList.remove(index);
			for(FinanceInvoiceStorage f:invoiceList){
					int start=Integer.parseInt(f.getInvoiceStartno().substring(1, 14));
					int end=Integer.parseInt(f.getInvoiceEndno().substring(1, 14));
					if((startInt >=start) && (startInt<=end) &&(endInt>=start) && (endInt<=end) ){
						return "used";
					}else if((startInt >=start) && (startInt<=end) && (endInt>=end) ){
						return "used";
					}else if((startInt <=start) && (endInt>start) && ( endInt<=end)){
						return "used";
					}else if((startInt <=start) && (endInt>=end)){
						return "used";
					}					
			}
			Integer lenu = financeInvoiceStorage.getInvoiceStartno().length();
			String startNoStrenu = financeInvoiceStorage.getInvoiceStartno().toString().substring(0, 1);
			String startNoStru = financeInvoiceStorage.getInvoiceStartno().toString().substring(1, financeInvoiceStorage.getInvoiceStartno().toString().length());
			int useNou = Integer.parseInt(startNoStru)-1;
			String strlen=useNou+"";
			int length = strlen.toString().length();
			for (int j = 0; j < lenu-length-1; j++) {
				strlen="0"+strlen;
			}
			String strAll=startNoStrenu+strlen;
			FinanceInvoiceStorage fis=invoiceStorageDao.queryFISById(financeInvoiceStorage.getId());
			//发票类型
			fis.setInvoiceType(financeInvoiceStorage.getInvoiceType());
			//发票类型编码
			fis.setInvoiceCode(financeInvoiceStorage.getInvoiceCode());
			//发票开始号
			fis.setInvoiceStartno(financeInvoiceStorage.getInvoiceStartno());
			// 发票终止号
			fis.setInvoiceEndno(financeInvoiceStorage.getInvoiceEndno());
			//发票已用号
			fis.setInvoiceUsedno(strAll);
			//使用状态(0-在用,1-停用)
			fis.setInvoiceUseState(financeInvoiceStorage.getInvoiceUseState());
			//拼音码
			fis.setInvoicePinyin(financeInvoiceStorage.getInvoicePinyin());
			//五笔码
			fis.setInvoiceWb(financeInvoiceStorage.getInvoiceWb());
			//自定义码
			fis.setInvoiceInputcode(financeInvoiceStorage.getInvoiceInputcode());
			//备注
			fis.setInvoiceRemark(financeInvoiceStorage.getInvoiceRemark());
			//修改时间
			fis.setUpdateTime(DateUtils.getCurrentTime());
			//修改人
			fis.setUpdateUser(user.getAccount());					
			invoiceStorageDao.save(fis);		
			return "update";
		}
		
	}

}
