package cn.honry.inpatient.doctorAdvice.dao;

import java.util.List;

import cn.honry.base.bean.model.InpatientDrugbilldetail;
import cn.honry.base.dao.EntityDao;

public interface InpatientDrugbilldetailDAO extends EntityDao<InpatientDrugbilldetail>{
	/**  
	 *  
	 * @Description： 得到医嘱执行单明细列表
	 * @Author：yeguanqun
	 * @CreateDate：2015-12-17   
	 * @version 1.0
	 * @param rows 
	 * @param page 
	 *
	 */
	List<InpatientDrugbilldetail> queryInpatientDrugbilldetail(InpatientDrugbilldetail inpatientDrugbilldetail);

	/**  
	 *  
	 * @Description： 得到医嘱执行单明细列表(分页)
	 * @Author：zxl
	 * @CreateDate：2015-12-17   
	 * @version 1.0
	 * @param rows 
	 * @param page 
	 *
	 */
	List<InpatientDrugbilldetail> queryDrugbilldetail(InpatientDrugbilldetail inpatientDrugbilldetail, String page,String rows);

	/**  
	 *  
	 * @Description： 得到医嘱执行单明细列表(分页总条数)
	 * @Author：zxl
	 * @CreateDate：2015-12-17   
	 * @version 1.0
	 * @param rows 
	 * @param page 
	 *
	 */
	int getTotalBilldetail(InpatientDrugbilldetail inpatientDrugbilldetail);

}
