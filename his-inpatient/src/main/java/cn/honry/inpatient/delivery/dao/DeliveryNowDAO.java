package cn.honry.inpatient.delivery.dao;

import java.util.List;

import cn.honry.base.bean.model.DepartmentContact;
import cn.honry.base.bean.model.DrugApplyout;
import cn.honry.base.bean.model.DrugApplyoutNow;
import cn.honry.base.bean.model.DrugBillclass;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientStoMsg;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.EntityDao;
import cn.honry.inpatient.delivery.vo.DeliveryVo;
/**
 * 集中发送数据库层
 * @author  lyy
 * @createDate： 2015年12月28日 上午10:50:39 
 * @modifier lyy
 * @modifyDate：2015年12月28日 上午10:50:39  
 * @modifyRmk：  
 * @version 1.0
 */
@SuppressWarnings({"all"})
public interface DeliveryNowDAO extends EntityDao<DrugApplyoutNow> {
	/**
	 * 
	 * 
	 * <p>查询出库申请表信息 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月5日 上午10:45:46 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月5日 上午10:45:46 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param hql
	 * @param page
	 * @param rows
	 * @return:
	 *
	 */
    List<DrugApplyoutNow> getDrugPage(String hql,String page,String rows);
    /**
     * 
     * 
     * <p>查询出库申请表信息条数 </p>
     * @Author: XCL
     * @CreateDate: 2017年7月5日 上午10:45:37 
     * @Modifier: XCL
     * @ModifyDate: 2017年7月5日 上午10:45:37 
     * @ModifyRmk:  
     * @version: V1.0
     * @param hql
     * @return:
     *
     */
    int getDrugTotal(String hql);
    /**
	 * 
	 * 
	 * <p>摆药单打印 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月5日 上午10:42:32 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月5日 上午10:42:32 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param tid 患者编号
	 * @param drugedbill 摆药单号
	 * @return:
	 *
	 */
    List<DeliveryVo> iReportInvoiceBill(String tid,String drugedbill);
}
