package cn.honry.statistics.doctor.registerInfoGzltj.dao;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.base.dao.EntityDao;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.doctor.registerInfoGzltj.vo.RegisterInfoGzltjVo;

@SuppressWarnings({"all"})
public interface RegisterInfoGzltjDao  extends EntityDao<RegisterInfoGzltjVo>{

	/**  
	 *  
	 * @Description： 查询工作量列表
	 * @param 
	 * @Author：zpty
	 * @CreateDate：2015-8-27  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<RegisterInfoGzltjVo> findInfo(String Stime,String Etime,String dept,String expxrt);
	
	/**  
	 *  
	 * @Description：  跳转修改页面(查询同科室同级别医生)
	 * @param id
	 * @Author：liudelin
	 * @CreateDate：2015-6-24 下午05:45:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	void registerTriageSave(String id, String expxrt);
	
	/**  
	 *  
	 * @Description： 查询
	 * @Author：liudelin
	 * @CreateDate：2015-6-25 下午01:44:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	RegisterInfo queryRegisterTiragegl(String sEncode);
	
	/**  
	 *  
	 * @Description： 查询工作量列表 查询挂号科室表
	 * @param time时间
	 * @param deptId科室id
	 * @param expxrtId专家id
	 * @Author：wujiao
	 * @CreateDate：2016-4-26 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<RegisterInfo> findPrereInfo(String time, String deptId, String expxrtId);
	
	/**  
	 *  
	 * @Description： 查询工作量列表 查询挂号科室表
	 * @param time时间
	 * @param deptId科室id
	 * @param expxrtId专家id
	 * @Author：wujiao
	 * @CreateDate：2016-4-26 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<RegisterInfoGzltjVo> findPrereSum(Map<Integer, String> map, String deptId, String expxrtId);
	
	/**  
	 * 
	 * <p> 获取需要统计的表或分区表中的科室及医生信息  </p>
	 * @Author: aizhonghua
	 * @CreateDate: 2016年11月29日 下午03:58:02 
	 * @Modifier: aizhonghua
	 * @ModifyDate: 2016年11月29日 下午03:58:02 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param：isZone是否连接分区tnL查询的表或分期集合stime开始时间etime结束时间dept科室编码expxrt医生编码
	 *
	 */
	List<RegisterInfoGzltjVo> statRegDorWorkloadFindDept(List<String> tnL,String stime,String etime, String dept, String expxrt);
	
	/**  
	 * 
	 * <p> 根据科室及医生获取此医生一周的工作量  </p>
	 * @Author: aizhonghua
	 * @CreateDate: 2016年11月29日 下午03:58:02 
	 * @Modifier: aizhonghua
	 * @ModifyDate: 2016年11月29日 下午03:58:02 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param：isZone是否连接分区tnL查询的表或分期集合stime开始时间etime结束时间vo统计对象
	 *
	 */
	RegisterInfoGzltjVo statRegDorWorkloadFindInfo(List<String> tnL,String stime, String etime,RegisterInfoGzltjVo vo);
	
	/**  
	 * 
	 * <p> 获取业务表中最大及最小时间 </p>
	 * @Author: aizhonghua
	 * @CreateDate: 2016年11月29日 下午03:58:02 
	 * @Modifier: aizhonghua
	 * @ModifyDate: 2016年11月29日 下午03:58:02 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	StatVo findMaxMin();
	
}
