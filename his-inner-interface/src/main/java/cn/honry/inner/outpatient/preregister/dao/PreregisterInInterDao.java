package cn.honry.inner.outpatient.preregister.dao;

import java.util.List;

import cn.honry.base.bean.model.HospitalParameter;
import cn.honry.base.bean.model.PatientBlackList;
import cn.honry.base.bean.model.RegisterPreregister;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.EntityDao;

/**  
 *  
 * @className：PreregisterInInterDao
 * @Description：  预约挂号接口Dao
 * @Author：aizhonghua
 * @CreateDate：2015-6-18 下午01:51:57  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-18 下午01:51:57  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@SuppressWarnings({"all"})
public interface PreregisterInInterDao extends EntityDao<RegisterPreregister>{

	/**  
	 *  
	 * @Description：  查询当天预约患者，预约状态为有效
	 * @Author：zhuxiaolu
	 * @CreateDate：2016-10-25 上午3:56:35  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<RegisterPreregister> findRegisterPreregister();
	
	/**  
	 *  
	 * @Description：  将当天预约患者，预约状态为有效更改为爽约
	 * @Author：zhuxiaolu
	 * @CreateDate：2016-10-25 上午3:56:35  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	void saveRegisterPreregister(RegisterPreregister registerPreregister);

	/**  
	 *  
	 * @Description：  判断该患者爽约次数
	 * @Author：zhuxiaolu
	 * @CreateDate：2016-10-25 上午3:56:35  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<RegisterPreregister> queryMissTime(String idcardId);
	
	/**  
	 *  
	 * @Description：  获取患者黑名单有效时间
	 * @Author：zhuxiaolu
	 * @CreateDate：2016-10-25 上午3:56:35  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	HospitalParameter  queryTime();

	
}
