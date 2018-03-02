package cn.honry.finance.invoiceUsageRecord.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.FinanceInvoice;
import cn.honry.base.bean.model.InvoiceUsageRecord;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.finance.invoiceUsageRecord.dao.InvoiceUsageRecordDao;
import cn.honry.finance.invoiceUsageRecord.service.InvoiceUsageRecordService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;

import com.google.common.base.Strings;
import com.google.gson.reflect.TypeToken;
@Service("invoiceUsageRecordService")
@Transactional
@SuppressWarnings({ "all" })
public class InvoiceUsageRecordServiceImpl implements InvoiceUsageRecordService {
	@Autowired
	@Qualifier(value = "invoiceUsageRecordDao")
	private InvoiceUsageRecordDao invoiceUsageRecordDao;
	@Override
	public InvoiceUsageRecord get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(InvoiceUsageRecord arg0) {
		
	}

	@Override
	public List<InvoiceUsageRecord> queryDatagrid(String page, String rows,
			String code, String type, String num) throws Exception {
		return invoiceUsageRecordDao.queryDatagrid(page,rows,code,type,num);
	}

	@Override
	public int getTotal(String code,String type, String num) throws Exception {
		return invoiceUsageRecordDao.getTotal(code,type,num);
	}

	@Override
	public String saveData(String rowss) throws Exception {
		SysDepartment dept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		//查询发票领取表的记录
		List<FinanceInvoice> fil=invoiceUsageRecordDao.queryFinInvoice();
		List<InvoiceUsageRecord> omlp =new ArrayList<InvoiceUsageRecord>();
		String state="no";//判断是否有可召回发票的标识
		if(StringUtils.isNotBlank(rowss)&&!"[]".equals(rowss)){
			try {
				rowss=rowss.replaceAll("\"[a-zA-Z0-9]+\":\"\",", "")
						.replaceAll(",\"[a-zA-Z0-9]+\":\"\"", "")
						.replaceAll(",\"[a-zA-Z0-9]+\":\"\",", "");
				omlp = JSONUtils.fromJson(rowss,new TypeToken<List<InvoiceUsageRecord>>(){},DateUtils.DATE_FORMAT);
				for(int i=0;i<omlp.size();i++){
					if(omlp.get(i).getInvoiceUsestate()==null){
						state="null";
					}
					else if(omlp.get(i).getInvoiceUsestate().equals(0)){
						state="yes";//有可召回的发票
						InvoiceUsageRecord iur=invoiceUsageRecordDao.getRecordModel(omlp.get(i).getId());
						iur.setRecoveryOpcd(user.getAccount());//召回操作员
						iur.setRecoveryDate(new Date());//召回时间
						iur.setRecoveryInvoiceNo(omlp.get(i).getInvoiceNo());//召回发票号
						String num1=DateUtils.getCurrentTime().toString();
						String num2=num1.substring(11, 19);
						String num3=num2.replaceAll(":", "")+i;
						String ino=omlp.get(i).getInvoiceNo()+num3;//生成唯一的发票号（原发票号+当前时间的时分秒+循环次数）
						iur.setInvoiceNo(ino);//发票号
						iur.setInvoiceUsestate(2);
						iur.setUpdateTime(DateUtils.getCurrentTime());
						iur.setUpdateUser(user.getAccount());
						invoiceUsageRecordDao.clear();
						invoiceUsageRecordDao.save(iur);
						//生成发票领取表记录
						String itype=null;//发票类型
						for(FinanceInvoice fi:fil){			
							String a = fi.getInvoiceStartno().substring(0,1);
							String b = fi.getInvoiceEndno().substring(0,1);
							String c = omlp.get(i).getInvoiceNo().substring(0,1);
							if((c.equals(a))&&(c.equals(b))){
								itype=fi.getInvoiceType();
								break;
							}
						}
						FinanceInvoice financeInvoice=new FinanceInvoice();
						financeInvoice.setInvoiceGetperson(omlp.get(i).getUserCode());//领取人
						financeInvoice.setInvoiceType(itype);//发票种类
						financeInvoice.setInvoiceStartno(omlp.get(i).getInvoiceNo());//发票开始号
						financeInvoice.setInvoiceEndno(omlp.get(i).getInvoiceNo());//发票终止号
						int ivno = Integer.parseInt(omlp.get(i).getInvoiceNo().substring(1))-1;
						String invono=String.format("%013d", ivno);
						String useno=omlp.get(i).getInvoiceNo().substring(0, 1)+invono;
						financeInvoice.setInvoiceUsedno(useno);//发票已用号
						financeInvoice.setInvoiceUsestate(1);//使用状态
						financeInvoice.setCreateUser(user.getAccount());
						financeInvoice.setCreateDept(dept.getDeptCode());
						financeInvoice.setCreateTime(DateUtils.getCurrentTime());
						invoiceUsageRecordDao.save(financeInvoice);
					}
					else if(omlp.get(i).getInvoiceUsestate().equals(1)){
						state="used";//有可召回的发票
					}
					else if(omlp.get(i).getInvoiceUsestate().equals(2)){
						state="zhaohui";//有可召回的发票
					}else{
						state="null";
					}
				}
				if("yes".equals(state)){
					return "success";
				}else if("used".equals(state)){
					return "used";
				}else if("zhaohui".equals(state)){
					return "zhaohui";
				}else if("null".equals(state)){
					return "null";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "error";
	}

	@Override
	public List<User> queryUserRecord() throws Exception {
		return invoiceUsageRecordDao.queryUserRecord();
	}

}
