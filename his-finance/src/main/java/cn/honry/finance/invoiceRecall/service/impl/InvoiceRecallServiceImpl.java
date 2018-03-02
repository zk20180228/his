package cn.honry.finance.invoiceRecall.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.FinanceInvoice;
import cn.honry.base.bean.model.FinanceInvoiceStorage;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.finance.invoiceRecall.dao.InvoiceRecallDao;
import cn.honry.finance.invoiceRecall.service.InvoiceReacllService;
import cn.honry.inner.baseinfo.code.dao.CodeInInterDAO;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;

import com.google.gson.reflect.TypeToken;
@Service("invoiceReacllService")
@Transactional
@SuppressWarnings({ "all" })
public class InvoiceRecallServiceImpl implements InvoiceReacllService {
	
	@Autowired
	@Qualifier(value = "invoiceRecallDao")
	private InvoiceRecallDao invoiceRecallDao;
	
	@Autowired
	@Qualifier(value="innerCodeDao")
	private CodeInInterDAO codeInInterDAO;
	
	@Override
	public FinanceInvoice get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(FinanceInvoice arg0) {
		
	}
	@Override
	public List<FinanceInvoice> queryInvoiceRecall(String page,String rows,String name) throws Exception {
		return invoiceRecallDao.queryInvoiceRecall(page,rows,name);
	}

	@Override
	public int getTotal(String name) {
		return invoiceRecallDao.getTotal(name);
	}

	@Override
	public List<SysEmployee> queryEmpMap() throws Exception {
		return invoiceRecallDao.queryEmpMap();
	}

	@Override
	public String recallInvoice(String date,String num) {
		SysDepartment dept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		List<FinanceInvoice> fil =new ArrayList<FinanceInvoice>();
		if(StringUtils.isNotBlank(date)&&!"[]".equals(date)){
			try {
				date=date.replaceAll("\"[a-zA-Z0-9]+\":\"\",", "")
						.replaceAll(",\"[a-zA-Z0-9]+\":\"\"", "")
						.replaceAll(",\"[a-zA-Z0-9]+\":\"\",", "");
				fil = JSONUtils.fromJson(date,new TypeToken<List<FinanceInvoice>>(){},DateUtils.DATE_FORMAT);
				//发票领取对象
				FinanceInvoice fi=fil.get(0);
				//发票入库记录对象
				FinanceInvoiceStorage fis=new FinanceInvoiceStorage();
				//发票类型对象
				BusinessDictionary cit=codeInInterDAO.getDictionaryByCode("invoiceType",fi.getInvoiceType());
				fis.setInvoiceType(fi.getInvoiceType());//发票类型
				fis.setInvoiceCode(cit.getEncode());//发票类型编码
				fis.setInvoiceEndno(fi.getInvoiceEndno());//发票结束号
				fis.setInvoiceUseState(0);//使用状态
				fis.setInvoicePinyin(cit.getPinyin());//拼音码
				fis.setInvoiceWb(cit.getWb());//五笔码
				fis.setInvoiceInputcode(cit.getInputCode());//自定义码
				int endNumNo=Integer.valueOf(fi.getInvoiceEndno().substring(1));//去掉首字母的发票结束号
				/**发票重新入库的开始号*/
				int startNum=endNumNo-Integer.valueOf(num)+1;//结束号-召回数量=开始号
				/**发票重新入库的已使用号*/
				int startNum1=startNum-1;//结束号-召回数量-1=已使用号
				
				String invono=fi.getInvoiceEndno().substring(0, 1)+String.format("%013d", startNum);
				String invono1=fi.getInvoiceEndno().substring(0, 1)+String.format("%013d", startNum1);
				fis.setInvoiceStartno(invono);//发票开始号
				fis.setInvoiceUsedno(invono1);//发票已用号
				fis.setInvoiceRemark("召回发票");
				fis.setCreateDept(dept.getDeptCode());
				fis.setCreateTime(DateUtils.getCurrentTime());
				fis.setCreateUser(user.getAccount());
				invoiceRecallDao.save(fis);
				/**更新发票领取人的信息*/
				fi.setInvoiceUsestate(-1);
				fi.setUpdateTime(DateUtils.getCurrentTime());
				fi.setUpdateUser(user.getAccount());
				invoiceRecallDao.save(fi);//将原来的发票号全部召回
				int start=Integer.valueOf(fi.getInvoiceStartno().substring(1));
				int startNumNo=Integer.valueOf(fi.getInvoiceStartno().substring(1));//去掉首字母的发票开始号
				/**插入发票领取人信息的发票结束号*/
				int endNum= startNum-1;
				String inEnd=fi.getInvoiceEndno().substring(0, 1)+String.format("%013d", endNum);
				int number = endNum-startNumNo;
				if(number>=0){//如果召回后剩余的发票数量不为0；将剩余的发票号重新生成发票领取记录
					FinanceInvoice fin=new FinanceInvoice();
					fin.setInvoiceEndno(inEnd);
					fin.setUpdateTime(DateUtils.getCurrentTime());
					fin.setUpdateUser(user.getAccount());
					fin.setInvoiceUsestate(0);
					fin.setInvoiceGettime(fi.getInvoiceGettime());
					fin.setInvoiceGetperson(fi.getInvoiceGetperson());
					fin.setInvoiceType(fi.getInvoiceType());
					fin.setInvoiceStartno(fi.getInvoiceStartno());
					fin.setInvoiceUsedno(fi.getInvoiceUsedno());
					fin.setInvoiceIspub(fi.getInvoiceIspub());
					fin.setCreateTime(fi.getCreateTime());
					fin.setCreateDept(fi.getCreateDept());
					fin.setCreateUser(fi.getCreateUser());
					invoiceRecallDao.save(fin);
				}
				return "success";
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "error";
	}
	
}
