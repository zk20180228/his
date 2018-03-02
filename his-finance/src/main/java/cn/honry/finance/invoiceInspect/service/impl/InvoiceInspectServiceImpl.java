package cn.honry.finance.invoiceInspect.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.finance.invoiceInspect.dao.InvoiceInspectDao;
import cn.honry.finance.invoiceInspect.service.InvoiceInspectService;
import cn.honry.finance.invoiceInspect.vo.InvoiceInspectVo;
import cn.honry.inner.baseinfo.code.dao.CodeInInterDAO;
import cn.honry.inner.baseinfo.employee.dao.EmployeeInInterDAO;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.TreeJson;


@Service("invoiceInspectService")
@Transactional
@SuppressWarnings({ "all" })
public class InvoiceInspectServiceImpl implements  InvoiceInspectService{
	@Autowired
	@Qualifier(value = "invoiceInspectDao")
	private InvoiceInspectDao invoiceInspectDao;

	public void setInvoiceInspectDao(InvoiceInspectDao invoiceInspectDao) {
		this.invoiceInspectDao = invoiceInspectDao;
	}
	@Autowired
	@Qualifier(value = "employeeInInterDAO")
	private EmployeeInInterDAO employeeDAO;
	
	public void setEmployeeDAO(EmployeeInInterDAO employeeDAO) {
		this.employeeDAO = employeeDAO;
	}

	@Autowired
	@Qualifier(value="innerCodeDao")
	private CodeInInterDAO codeInInterDAO;
	
	@Override
	public List<TreeJson> queryInvoiceType(String id) {
		List<TreeJson>  treeJsonList = new ArrayList<TreeJson>();
		List<BusinessDictionary> cit = codeInInterDAO.getDictionary("invoiceType");
		if(StringUtils.isBlank(id)){
			TreeJson Json1=new TreeJson();
			Json1.setId("1");
			Json1.setText("发票类型信息");
			treeJsonList.add(Json1);
		}
		if(cit!=null&&cit.size()>0){
			for(BusinessDictionary codeInvoicetype :cit){
				TreeJson Json2=new TreeJson();
				Json2.setId(codeInvoicetype.getId());
				Json2.setText(codeInvoicetype.getName());
				Map<String,String> map1=new HashMap<String,String>();
				map1.put("pid", "1");
				map1.put("encode", codeInvoicetype.getEncode());
				Json2.setAttributes(map1);
				treeJsonList.add(Json2);
			}
		}
		return TreeJson.formatTree(treeJsonList);
	}

	@Override
	public List<InvoiceInspectVo> queryInvoiceInfoList(Date beginTime,
			Date endTime, String balanceOpcd, String encode,String page,String rows) throws Exception {
		return invoiceInspectDao.queryInvoiceInfoList(beginTime,endTime,balanceOpcd,encode,page,rows);
	}

	@Override	
	public void saveDatagrid(String rows,String intype) throws Exception {	
		String[] array=new String[30];
		array=rows.split(",");
		ArrayList arrlist=new ArrayList();
		for(int i=0;i<array.length;i++){
			arrlist.add(i, array[i]);
		}
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		List<SysEmployee> sysemp = employeeDAO.findEmpByUserId(user.getId());
		invoiceInspectDao.saveDatagrid(arrlist,intype,sysemp.get(0).getId());
		OperationUtils.getInstance().conserve(sysemp.get(0).getId(),"发票核查","INSERT INTO","t_business_invoiceinfo",OperationUtils.LOGACTIONINSERT);
	}

	@Override
	public List<SysEmployee> queryBalanceOpcd(String q)  throws Exception{
		return invoiceInspectDao.queryBalanceOpcd(q);
	}

	@Override
	public int getTotal(Date beginTime, Date endTime, String balanceOpcd, String encode)  throws Exception{
		return invoiceInspectDao.getTotal(beginTime,endTime,balanceOpcd,encode);
	}


}
