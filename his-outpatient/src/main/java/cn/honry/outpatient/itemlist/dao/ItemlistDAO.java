package cn.honry.outpatient.itemlist.dao;

import java.util.List;

import cn.honry.base.bean.model.OutpatientItemlist;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.EntityDao;

@SuppressWarnings({"all"})
public interface ItemlistDAO extends EntityDao<OutpatientItemlist>{
	/**  
	 * @Description： 根据病历号 患者信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-8-19 下午01:09:49  
	 * @Modifier：ldl
	 * @ModifyDate：2015-12-11
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	Patient queryRegisterInfoByCaseNo(String midicalrecordId);
	/**  
	 * @Description： 开立科室下拉框
	 * @Modifier：ldl
	 * @ModifyDate：2015-12-11
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<SysDepartment> quertComboboxDept();
	/**  
	 * @Description： 开立医生下拉框
	 * @Modifier：ldl
	 * @ModifyDate：2015-12-11
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param dept 
	 */
	List<SysEmployee> quertComboboxEmp(String dept);
	/**  
	 * @Description： 合同单位下拉
	 * @Modifier：ldl
	 * @ModifyDate：2015-12-11
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<BusinessContractunit> quertComboboxCont();
}
