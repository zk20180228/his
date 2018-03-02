package cn.honry.outpatient.observation.service;

import java.util.List;

import cn.honry.outpatient.observation.vo.ComboxVo;
import cn.honry.outpatient.observation.vo.ObservationVo;
import cn.honry.outpatient.observation.vo.PatientRegisterVo;
import cn.honry.outpatient.observation.vo.PatientVo;

public interface ObservationService {
	
	/**
	 * 
	 * <p>保存留观信息 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年11月16日 下午1:42:08 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年11月16日 下午1:42:08 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return
	 * @throws:
	 *
	 */
	public int saveObservation(ObservationVo observation)throws Exception;

	/**
	 * 
	 * <p>根据就诊卡号查询胡患者基本信息 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年11月16日 下午5:34:37 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年11月16日 下午5:34:37 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws:
	 *
	 */
	public List<PatientVo> findPatientInfoByCardNo(String cardNo);

	/**
	 * 
	 * <p>根据就诊卡号查询患者挂号信息 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年11月16日 下午5:36:20 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年11月16日 下午5:36:20 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws:
	 *
	 */
	public List<PatientRegisterVo> findRegisterInfoByCardNo(String cardNo);
	
	/**
	 * 
	 * <p>责任医师拉框 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年11月17日 下午1:55:08 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年11月17日 下午1:55:08 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws:
	 *
	 */
	public List<ComboxVo> findNurseCode();
	
	/**
	 * 
	 * <p>责任护士下拉框 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年11月17日 下午1:55:12 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年11月17日 下午1:55:12 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws:
	 *
	 */
	public List<ComboxVo> findDoctorCode();

}
