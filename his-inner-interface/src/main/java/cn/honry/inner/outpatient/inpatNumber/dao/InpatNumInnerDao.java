package cn.honry.inner.outpatient.inpatNumber.dao;

import cn.honry.base.bean.model.InpatientNumber;
import cn.honry.base.dao.EntityDao;
@SuppressWarnings({"all"})
public interface InpatNumInnerDao extends EntityDao<InpatientNumber>{

	/**  
	 * @Description：  根据住院流水号查询I住院次数表
	 * @Author：zhangjin
	 * @CreateDate：2016-11-15
	 * @ModifyRmk：
	 * @param:   
	 * @version 1.0
	 */
	InpatientNumber getInpatientNo(String inpatientNo);

}
