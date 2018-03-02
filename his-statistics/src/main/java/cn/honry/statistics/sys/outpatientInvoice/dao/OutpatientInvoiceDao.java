package cn.honry.statistics.sys.outpatientInvoice.dao;

import java.util.List;

import cn.honry.base.bean.model.OutpatientFeedetail;
import cn.honry.base.dao.EntityDao;
import cn.honry.statistics.sys.outpatientInvoice.vo.InvoiceInfoVo;
import cn.honry.statistics.sys.outpatientInvoice.vo.OutpatientStaVo;

@SuppressWarnings({"all"})
public interface OutpatientInvoiceDao extends EntityDao<OutpatientFeedetail>{
	
	/**  
	 * @Description：  根据发票号查询患者挂号信息
	 * @Author：ldl
	 * @CreateDate：2016-06-21
	 * @ModifyRmk：  
	 * @param：invoiceNo 发票号
	 * @version 1.0
	 */
	InvoiceInfoVo queryInvoiceInfoVo(String invoiceNo);
	
	/**
	 * @Description  根据发票号查询患者挂号信息(老表)
	 * @author  marongbin
	 * @createDate： 2016年12月27日 下午8:46:37 
	 * @modifier 
	 * @modifyDate：
	 * @param invoiceNo
	 * @return: InvoiceInfoVo
	 * @modifyRmk：  
	 * @version 1.0
	 */
	InvoiceInfoVo queryInvoiceInfoVoOld(String invoiceNo);
	
	/**
	 * @Description  根据发票号查询患者挂号信息(新表)
	 * @author  marongbin
	 * @createDate： 2016年12月27日 下午8:46:37 
	 * @modifier 
	 * @modifyDate：
	 * @param invoiceNo
	 * @return: InvoiceInfoVo
	 * @modifyRmk：  
	 * @version 1.0
	 */
	InvoiceInfoVo queryInvoiceInfoVoNew(String invoiceNo);
	
	/**  
	 * @Description：  根据发票号查询患者收费信息
	 * @Author：ldl
	 * @CreateDate：2016-06-21
	 * @ModifyRmk：  
	 * @param：invoiceNo 发票号
	 * @version 1.0
	 */
	List<OutpatientStaVo> findOutpatient(String invoiceNo);
	
	/**
	 * @Description 根据发票号查询收费信息（老表）
	 * @author  marongbin
	 * @createDate： 2016年12月28日 上午9:35:56 
	 * @modifier 
	 * @modifyDate：
	 * @param invoiceNo
	 * @return: List<OutpatientStaVo>
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<OutpatientStaVo> findOutpatientOld(String invoiceNo);
	
	/**
	 * @Description 根据发票号查询收费信息（新表）
	 * @author  marongbin
	 * @createDate： 2016年12月28日 上午9:35:56 
	 * @modifier 
	 * @modifyDate：
	 * @param invoiceNo
	 * @return: List<OutpatientStaVo>
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<OutpatientStaVo> findOutpatientNew(String invoiceNo);
	
}
