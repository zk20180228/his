package cn.honry.outpatient.itemlist.service;

import java.util.List;

import cn.honry.base.bean.model.BusinessStackinfo;
import cn.honry.base.bean.model.OutpatientItemlist;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.PatientAccount;
import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.service.BaseService;
import cn.honry.outpatient.itemlist.vo.UndrugVo;


public interface ItemlistService extends BaseService<OutpatientItemlist>{
	
	/**  
	 *  
	 * @Description：  查询最小费用下的非药品tree
	 * @Author：aizhonghua
	 * @CreateDate：2015-8-18 下午05:37:12  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-8-18 下午05:37:12  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	String queryUndrugByMinimum(String id);
	
	/**  
	 *  
	 * @Description： 根据病历号 患者信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-8-19 下午01:09:49  
	 * @Modifier：ldl
	 * @ModifyDate：2015-12-11
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	Patient queryRegisterInfoByCaseNo(String midicalrecordId);

	/**  
	 *  
	 * @Description：  查询非药品组套
	 * @Author：aizhonghua
	 * @CreateDate：2015-11-3 上午09:35:09  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-11-3 上午09:35:09  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	String queryUndrugStackTree(String id);

	/**  
	 *  
	 * @Description：  查询组套详情
	 * @Author：aizhonghua
	 * @CreateDate：2015-11-3 下午03:59:29  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-11-3 下午03:59:29  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<BusinessStackinfo> queryUndrugStackInfo(String id);

	/**  
	 *  
	 * @Description：  保存门诊收费信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-11-3 下午06:07:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-11-3 下午06:07:28  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	void saveItemlist(List<UndrugVo> infoList, PatientAccount account,OutpatientItemlist itemlist,Double sumMoney);
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

	/**  
	 *  
	 * @Description：  
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-19 上午09:38:38  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-19 上午09:38:38  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	RegisterInfo queryRegisterByCaseNo(String id);
}
