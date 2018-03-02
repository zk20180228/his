package cn.honry.outpatient.preregister.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.RegisterGrade;
import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.base.bean.model.RegisterPreregisterNow;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.service.BaseService;
import cn.honry.inner.outpatient.register.vo.InfoInInterPatient;
import cn.honry.outpatient.preregister.vo.EmpScheduleVo;
import cn.honry.outpatient.preregister.vo.IdCardPreVo;
import cn.honry.outpatient.preregister.vo.RegInfoInInterVo;

public interface BusinessPreregisterService extends BaseService<RegisterInfo>{
	/**  
	 * @Description： 数据源
	 * @Author：liudelin
	 * @CreateDate：2015-12-02
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	SysEmployee queryInfoPeret(String userId);
	/**  
	 * @Description：查询排班信息（剩余号数）
	 * @Author：liudelin
	 * @CreateDate：2015-12-02
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<RegInfoInInterVo> findInfoList(String id);
	/**  
	 * @Description：  预约挂号（医生站）
	 * @Author：liudelin
	 * @CreateDate：2015-12-02
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	IdCardPreVo searchIdcard(String idcardNo);
	/**  
	 * @Description：添加&修改
	 * @Author：ldl
	 * @CreateDate：2015-12-4
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	void editPreregisterVo(RegisterPreregisterNow preregister);
	/**  
	 * @Description：级别转换
	 * @Author：ldl
	 * @CreateDate：2015-12-4
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	RegisterGrade findGradeEdit(String gradeId);
	/**  
	 * @Description：添加&修改
	 * @Author：ldl
	 * @CreateDate：2015-12-4
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	InfoInInterPatient queryRegisterInfo(String idcardNo);
	/**  
	 * @Description：  通过预约午别带入开始结束时间
	 * @Author：wujiao
	 * @CreateDate：2016-1-29
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	Map<String, String> queryAll(String time, String gradeid, String deptid,String middy);
	/**  
	 * @Description：  验证该午别下的该医生是否已经有这患者的预约
	 * @Author：ldl
	 * @CreateDate：2016-06-06
	 * @ModifyRmk：  
	 * @param：dates 日期
	 * @param：idCardno 就诊卡号
	 * @param：empId 医生ID
	 * @param：midday 午别
	 * @version 1.0
	 */
	Map<String, String> queryPreInfo(String dates, String idCardno,String empId, String midday);
	/**
	 * 查询挂号级别
	 * @param empId
	 * @return
	 */
	EmpScheduleVo getEmpee(String empId);

	
}
