package cn.honry.statistics.bi.bistac.outpatientStac.dao;

import java.util.List;

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.dao.EntityDao;
import cn.honry.statistics.bi.bistac.outpatientStac.vo.BusinessContractunitVo;
import cn.honry.statistics.bi.bistac.outpatientStac.vo.OutpatientStacVo;

@SuppressWarnings({"all"})
public interface OutpatientStacVoDao extends EntityDao<OutpatientStacVo>{
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
	 * 获取住院出院量
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
	public OutpatientStacVo queryOutpatientFeedetailNowCostVoM(List<String> tnL,String stime,String etime);
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
	public OutpatientStacVo queryOutpatientFeedetailNowCostVoY(List<String> tnL,String stime,String etime);
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
	public OutpatientStacVo queryInpatientFeeInfoNowCostM(List<String> tnL,String stime,String etime);
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
	public OutpatientStacVo queryInpatientFeeInfoNowCostY(List<String> tnL,String stime,String etime);
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
	/**  
	 * 
	 * 获取门诊处方明细表的最大和最小时间
	 * @Author: huzhenguo
	 * @CreateDate: 2017年3月29日 上午10:51:26 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年3月29日 上午10:51:26 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public OutpatientStacVo findMaxMin();
	/**  
	 * 
	 * 获取住院表的最大和最小时间
	 * @Author: huzhenguo
	 * @CreateDate: 2017年3月29日 上午10:51:26 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年3月29日 上午10:51:26 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	/**  
	 * 
	 * <p> </p>
	 * @Author: huzhenguo
	 * @CreateDate: 2017年3月30日 下午3:25:42 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年3月30日 下午3:25:42 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public OutpatientStacVo findMaxMinZ();
}
