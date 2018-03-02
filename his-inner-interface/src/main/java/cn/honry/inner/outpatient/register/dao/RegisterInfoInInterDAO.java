package cn.honry.inner.outpatient.register.dao;

import java.util.Date;
import java.util.List;

import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.base.bean.model.Registration;
import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.base.dao.EntityDao;
import cn.honry.inner.outpatient.register.vo.InfoInInterPatient;

@SuppressWarnings({"all"})
public interface RegisterInfoInInterDAO extends EntityDao<RegisterInfo>{
	
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
	 * @Description：  根据病历号和看诊序号查询当天的挂号信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-11 下午03:53:52  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-11 下午03:53:52  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	RegisterInfo getRegisterInfoByPatientNoAndNo(String patientNo, String no);

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
	
	/**  
	 *  
	 * @Description：  根据条件获得挂号信息 
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-6 上午09:07:48  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-6 上午09:07:48  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param juri 是不是查询部门  id （user/dept）  no 就诊/病例 号  sTime开始时间  eTime 结束时间
	 *
	 */
	List<RegisterInfo> getInfo(String juri,String id, String no, String sTime,String eTime);
	
	/**  
	 *  
	 * @Description：  根据证件类型和证件号获得患者id
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-6 上午09:25:26  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-6 上午09:25:26  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	String getIdByNo(String certificatesType,String certificatesNo);
	
	/**  
	 *  
	 * @Description：  根据时间及支付类型查询挂号信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-1 上午09:27:19  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-1 上午09:27:19  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<RegistrationNow> getInfoByTime(Date startTime, Date endTime, String payTypeId,String registrarId);
}
