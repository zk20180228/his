package cn.honry.inner.outpatient.register.service;

import java.util.List;

import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.base.service.BaseService;
import cn.honry.inner.outpatient.register.vo.InfoInInterPatient;


public interface RegisterInfoInInterService extends BaseService<RegisterInfo>{
	
	/**  
	 * @Description：  查询信息
	 * @Author：ldl
	 * @CreateDate：2015-11-2 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	InfoInInterPatient queryRegisterInfo(String idcardNo);
	
	/**  
	 *  
	 * @Description：  查询患者树
	 *@Author：wujiao
	 * @CreateDate：2015-6-25 上午3:56:35  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-6-25 上午3:56:35   
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	
	List<RegisterInfo> getInfoByEmployeeId(String id,String type);
	
	/**  
	 *  
	 * @Description： 根据就诊卡号查询挂号表
	 * @Author：kjh
	 * @CreateDate：2016-1-11上午09:07:48  
	 *
	 */
	List<RegisterInfo> getInfo(String no);
}
