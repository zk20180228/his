package cn.honry.statistics.bi.bistac.outpatientStac.service;

import java.util.List;

import cn.honry.statistics.bi.bistac.outpatientStac.vo.BusinessContractunitVo;
import cn.honry.statistics.bi.bistac.outpatientStac.vo.OutpatientStacVo;

public interface OutpatientStacVoService {
	/**  
	 * 
	 * 获取合同单位信息
	 * @Author: huzhenguo
	 * @CreateDate: 2017年3月30日 下午3:25:52 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年3月30日 下午3:25:52 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public List<BusinessContractunitVo> queryBusinessContractunit();
	/**
	 * 获取当日住院出院量
	 * @Author zxh
	 * @time 2017年5月9日
	 * @return
	 */
	public List<BusinessContractunitVo> queryInOutNum();
	/**  
	 * 
	 * 获取每个合同单位在门诊的数量
	 * @Author: huzhenguo
	 * @CreateDate: 2017年3月30日 下午3:25:52 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年3月30日 下午3:25:52 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public int queryBusinessContractunitTotal(String encode);
	/**  
	 * 
	 * 获取每个合同单位在门诊急诊的数量
	 * @Author: huzhenguo
	 * @CreateDate: 2017年3月30日 下午3:25:52 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年3月30日 下午3:25:52 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public int queryBusinessContractunitTotalJi(String encode);
	/**  
	 * 
	 * 查询本日门诊量\本日门诊急诊量
	 * @Author: huzhenguo
	 * @CreateDate: 2017年3月24日 下午8:17:05 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年3月24日 下午8:17:05 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */	
	public OutpatientStacVo queryRegistrationVo();
	/**  
	 * 
	 * 查询当日门诊实收
	 * @Author: huzhenguo
	 * @CreateDate: 2017年3月24日 下午8:17:05 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年3月24日 下午8:17:05 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */	
	public OutpatientStacVo queryOutpatientFeedetailNowCostVoD();
	/**  
	 * 
	 * 查询当月门诊实收
	 * @Author: huzhenguo
	 * @CreateDate: 2017年3月24日 下午8:17:05 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年3月24日 下午8:17:05 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */	
	public OutpatientStacVo queryOutpatientFeedetailNowCostVoM();
	/**  
	 * 
	 * 查询当年门诊实收
	 * @Author: huzhenguo
	 * @CreateDate: 2017年3月24日 下午8:17:05 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年3月24日 下午8:17:05 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */	
	public OutpatientStacVo queryOutpatientFeedetailNowCostVoY();
	/**  
	 * 
	 * 查询手术例数
	 * @Author: huzhenguo
	 * @CreateDate: 2017年3月24日 下午8:17:05 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年3月24日 下午8:17:05 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */	
	public OutpatientStacVo queryOperationApplyVo();
	/**  
	 * 
	 * 获取每个合同单位出院的数量
	 * @Author: huzhenguo
	 * @CreateDate: 2017年3月30日 下午3:25:52 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年3月30日 下午3:25:52 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public int queryBusinessContractunitTotalGo(String encode);
	/**  
	 * 
	 * 获取每个合同单新增住院的数量
	 * @Author: huzhenguo
	 * @CreateDate: 2017年3月30日 下午3:25:52 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年3月30日 下午3:25:52 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public int queryBusinessContractunitTotalNew(String encode);
	/**  
	 * 
	 * 查询当前在院人数\当前出院人数\新增住院人数
	 * @Author: huzhenguo
	 * @CreateDate: 2017年3月24日 下午8:17:05 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年3月24日 下午8:17:05 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */	
	public OutpatientStacVo queryInpatientInfoNowVo();
	/**  
	 * 
	 * 查询当日住院实收
	 * @Author: huzhenguo
	 * @CreateDate: 2017年3月24日 下午8:17:05 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年3月24日 下午8:17:05 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */	
	public OutpatientStacVo queryInpatientFeeInfoNowCostD();
	/**  
	 * 
	 * 查询当月住院实收
	 * @Author: huzhenguo
	 * @CreateDate: 2017年3月24日 下午8:17:05 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年3月24日 下午8:17:05 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */	
	public OutpatientStacVo queryInpatientFeeInfoNowCostM();
	/**  
	 * 
	 * 查询当年住院实收
	 * @Author: huzhenguo
	 * @CreateDate: 2017年3月24日 下午8:17:05 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年3月24日 下午8:17:05 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */	
	public OutpatientStacVo queryInpatientFeeInfoNowCostY();
	/**  
	 * 
	 * 查询住院核定床位\住院床位
	 * @Author: huzhenguo
	 * @CreateDate: 2017年3月24日 下午8:17:05 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年3月24日 下午8:17:05 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */	
	public OutpatientStacVo queryBusinessHospitalbedVo();	
}
