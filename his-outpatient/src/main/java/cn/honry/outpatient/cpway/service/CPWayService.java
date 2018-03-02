package cn.honry.outpatient.cpway.service;

import java.util.List;

import cn.honry.outpatient.cpway.vo.CPWayVo;
import cn.honry.outpatient.cpway.vo.ComboxVo;
import cn.honry.outpatient.cpway.vo.PatientVo;

public interface CPWayService {

	/**
	 * 
	 * <p>住院科室树</p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年12月25日 下午2:17:40 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年12月25日 下午2:17:40 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return
	 * @throws:
	 */
	List<ComboxVo> inpatientDeptTree(String deptName);

	/**
	 * 
	 * <p>根据科室编号查询该科室下的临床路径对应的患者列表 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年12月25日 下午2:36:20 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年12月25日 下午2:36:20 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws:
	 */
	List<ComboxVo> patientList(String id);

	/**
	 * 
	 * <p>根据科室编号或者患者的住院流水号查询 临床路径患者列表----列表</p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年12月25日 下午2:46:14 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年12月25日 下午2:46:14 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws:
	 */
	List<CPWayVo> cPWayPatientList(String page,String rows,String id);

	/**
	 * 
	 * <p>根据科室编号或者患者的住院流水号查询 临床路径患者列表----总记录数</p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年12月25日 下午2:46:14 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年12月25日 下午2:46:14 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws:
	 */
	Integer cPWayPatientCount(String id);

	/**
	 * 
	 * <p>添加患者临床路径 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年12月25日 下午3:02:39 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年12月25日 下午3:02:39 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return
	 * @throws:
	 */
	void addCPWayPatient(CPWayVo cPWayVo);

	/**
	 * 
	 * <p>校验该住院流水号对应的患者是否已经添加临床路径</p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年12月25日 下午3:02:39 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年12月25日 下午3:02:39 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return
	 * @throws:
	 */
	Integer checkIsAdd(String inpatient_no);

	/**
	 * 
	 * <p>根据住院流水号查询患者信息 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年12月25日 下午3:30:55 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年12月25日 下午3:30:55 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws:
	 */
	PatientVo findPatient(String inpatient_no);

	/**
	 * 
	 * <p>查询临床路径的列表，便于用户下拉框选择 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年12月25日 下午3:33:33 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年12月25日 下午3:33:33 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws:
	 */
	List<ComboxVo> cPWList();

	/**
	 * 
	 * <p>根据临床路径的id查询对应的版本号 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年12月25日 下午3:59:10 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年12月25日 下午3:59:10 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws:
	 */
	List<ComboxVo> findVersionList(String cPWId);

	/**
	 * 
	 * <p>根据临床路径的申请id审批临床路径申请：apply_status(同意或者不同意 )</p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年12月25日 下午3:16:19 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年12月25日 下午3:16:19 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws:
	 */
	void approveApply(String cPWAppId, String apply_status);

	/**
	 * 
	 * <p>根据临床路径的申请id删除临床路径患者列表 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年12月25日 下午3:24:55 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年12月25日 下午3:24:55 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws:
	 */
	void delCPWayPatient(String cPWAppId);

}
