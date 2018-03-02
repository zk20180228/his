package cn.honry.inner.patient.idcard.dao;

import cn.honry.base.bean.model.PatientIdcard;
import cn.honry.base.dao.EntityDao;
@SuppressWarnings({"all"})
public interface IdcardInInterDAO extends EntityDao<PatientIdcard>{
	
	/**  
	 *  
	 * @Description：   通过病历号获得患者信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-11 下午02:58:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-11 下午02:58:28  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	PatientIdcard getIdcardByPatientNo(String patientNo);

	/**  
	 *  
	 * @Description：   通过就诊卡号获得患者信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-11 下午02:58:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-11 下午02:58:28  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	PatientIdcard getIdcardByIdCardNo(String idCardNo);
	
}

