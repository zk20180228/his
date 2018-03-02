package cn.honry.inpatient.diagnose.dao;

import cn.honry.base.bean.model.BusinessDiagnoseMedicare;
import cn.honry.base.dao.EntityDao;

@SuppressWarnings({"all"})
public interface DiagnoseMedicareDAO extends EntityDao<BusinessDiagnoseMedicare>{

	/**
	 * 
	 * 
	 * <p>获取医保信息(进行修改) </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月4日 下午3:53:02 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月4日 下午3:53:02 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param id
	 * @return:
	 *
	 */
	BusinessDiagnoseMedicare getval(String id);
   

}
