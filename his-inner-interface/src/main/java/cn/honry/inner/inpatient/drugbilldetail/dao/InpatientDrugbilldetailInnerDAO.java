package cn.honry.inner.inpatient.drugbilldetail.dao;

import java.util.List;
import cn.honry.base.bean.model.InpatientDrugbilldetail;
import cn.honry.base.dao.EntityDao;

public interface InpatientDrugbilldetailInnerDAO extends EntityDao<InpatientDrugbilldetail>{
	/**  
	 *  
	 * @Description： 得到医嘱执行单明细列表
	 * @Author：yeguanqun
	 * @CreateDate：2015-12-17   
	 * @version 1.0
	 *
	 */
	List<InpatientDrugbilldetail> queryInpatientDrugbilldetail(InpatientDrugbilldetail inpatientDrugbilldetail);
}
