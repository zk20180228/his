package cn.honry.statistics.deptstat.hospitalday.dao;

import java.util.List;
import java.util.Map;

import cn.honry.base.dao.EntityDao;
import cn.honry.statistics.deptstat.hospitalday.vo.HospitaldayVo;

public interface HospitaldayDao extends EntityDao<HospitaldayVo>{
	/**  
	 * 
	 * 查询医院每日的汇总
	 * @Author: donghe
	 * @CreateDate: 2017年7月25日 下午4:09:43 
	 * @Modifier: donghe
	 * @ModifyDate: 2017年7月25日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	List<HospitaldayVo> queryHospitaldayList(List<String> tnL,String parameter,String startTime,String endTime);
	/**  
	 * 
	 * 预处理在院人数
	 * @Author: donghe
	 * @CreateDate: 2017年7月25日 下午4:09:43 
	 * @Modifier: donghe
	 * @ModifyDate: 2017年7月25日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	void init_YYMRHZ(String menuAlias, Integer type, String date) ;
	/**  
	 * 
	 * 查询门诊收入
	 * @Author: donghe
	 * @CreateDate: 2017年7月25日 下午4:09:43 
	 * @Modifier: donghe
	 * @ModifyDate: 2017年7月25日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	List<HospitaldayVo> querymz(List<String> tnL,String parameter,String startTime,String endTime);
	/**  
	 * 
	 * 查询在院人次
	 * @Author: donghe
	 * @CreateDate: 2017年7月25日 下午4:09:43 
	 * @Modifier: donghe
	 * @ModifyDate: 2017年7月25日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	List<HospitaldayVo> queryHospitaldayVoin(String date);
	/**  
	 * 
	 * 查询手术人次
	 * @Author: donghe
	 * @CreateDate: 2017年7月25日 下午4:09:43 
	 * @Modifier: donghe
	 * @ModifyDate: 2017年7月25日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	List<HospitaldayVo> queryHospitaldayVoopear(String date);
	/**  
	 * 
	 * 查询出院人次
	 * @Author: donghe
	 * @CreateDate: 2017年7月25日 下午4:09:43 
	 * @Modifier: donghe
	 * @ModifyDate: 2017年7月25日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	List<HospitaldayVo> queryHospitaldayVooutpa(String date);
	/**  
	 * 
	 * 查询每日运营情况
	 * @Author: donghe
	 * @CreateDate: 2017年7月25日 下午4:09:43 
	 * @Modifier: donghe
	 * @ModifyDate: 2017年7月25日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	List<HospitaldayVo> queryList(String date);
	
	/**
	 * 
	 * 
	 * <p>查询每日运营情况 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年9月4日 下午5:04:08 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年9月4日 下午5:04:08 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param begin 开始时间
	 * @param end 结束时间
	 * @param queryMong 
	 * @return:
	 *
	 */
	public Map<String,Object> queryDate(String begin,String end,String queryMong,String rows,String page);
}
