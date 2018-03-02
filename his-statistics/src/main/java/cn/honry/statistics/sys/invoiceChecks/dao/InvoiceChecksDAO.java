package cn.honry.statistics.sys.invoiceChecks.dao;

import java.util.List;

import cn.honry.base.bean.model.InpatientBalanceHead;
import cn.honry.base.bean.model.InpatientBalanceHeadNow;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.dao.EntityDao;
import cn.honry.statistics.sys.invoiceChecks.vo.VinpatirntInfoBalance;

@SuppressWarnings({"all"})
public interface InvoiceChecksDAO extends EntityDao<VinpatirntInfoBalance>{
	/***
	 * 
	 * @Description:查询患者信息
	 * @author:  donghe
	 * @CreateDate: 2016年6月22日 
	 * @version 1.0
	 * @throws Exception 
	 */
	List<VinpatirntInfoBalance> queryVinpatirntInfoBalance(String medicalrecordId,String invoiceNo) throws Exception;
	/***
	 * @Description:根据病历号或者身份证号患者信息
	 * @author:  donghe
	 * @CreateDate: 2016年6月22日 
	 * @version 1.0
	 * @throws Exception 
	 */
	List<InpatientInfoNow> queryInfolist(String medicalrecordId,String idCard) throws Exception;
	/**
	 * 按住院号查询  根据住院号查询结算投标 获取的发票号 
	 * @param inpatientNo
	 * @return
	 * @throws Exception
	 */
	List<InpatientBalanceHeadNow> queryBalanceHead(String inpatientNo) throws Exception;
	/***
	 * 
	 * @Description:查询患者结账list
	 * @author:  donghe
	 * @CreateDate: 2016年6月22日 
	 * @version 1.0
	 * @throws Exception 
	 */
	List<VinpatirntInfoBalance> queryVinpatirntInfoBalancepages(String medicalrecordId,String invoiceNo,String page,String rows) throws Exception;
	/***
	 * 
	 * @Description:查询患者结账 总条数
	 * @author:  donghe
	 * @CreateDate: 2016年6月22日 
	 * @version 1.0
	 * @throws Exception 
	 */
	int queryVinpatirntInfoBalanceTotal(String medicalrecordId,String invoiceNo) throws Exception;
}
