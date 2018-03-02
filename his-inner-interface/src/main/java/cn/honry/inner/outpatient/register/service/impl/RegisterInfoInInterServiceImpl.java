package cn.honry.inner.outpatient.register.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.inner.outpatient.register.dao.RegisterInfoInInterDAO;
import cn.honry.inner.outpatient.register.service.RegisterInfoInInterService;
import cn.honry.inner.outpatient.register.vo.InfoInInterPatient;

@Service("registerInfoInInterService")
@Transactional(rollbackFor={Throwable.class})
@SuppressWarnings({ "all" })
public class RegisterInfoInInterServiceImpl implements RegisterInfoInInterService{

	/** 挂号数据库操作类 **/
	@Autowired
	@Qualifier(value = "registerInfoInInterDAO")
	private RegisterInfoInInterDAO registerInfoInInterDAO;
	
	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public RegisterInfo get(String id) {
		return registerInfoInInterDAO.get(id);
	}

	@Override
	public void saveOrUpdate(RegisterInfo entity) {
		
	}

	/**  
	 * @Description：  查询信息
	 * @Author：ldl
	 * @CreateDate：2015-11-2 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@Override
	public InfoInInterPatient queryRegisterInfo(String idcardNo) {
		InfoInInterPatient infoPatient = registerInfoInInterDAO.queryRegisterInfo(idcardNo);
		return infoPatient;
	}

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
	@Override
	public List<RegisterInfo> getInfoByEmployeeId(String id,String type) {
		return registerInfoInInterDAO.getInfoByEmployeeId(id,type);
	}
	
	@Override
	public List<RegisterInfo> getInfo(String no) {
		return registerInfoInInterDAO.getInfo(no);
	}

}	