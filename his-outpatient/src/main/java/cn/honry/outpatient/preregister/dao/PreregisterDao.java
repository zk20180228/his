package cn.honry.outpatient.preregister.dao;

import java.util.List;

import cn.honry.base.bean.model.RegisterPreregister;
import cn.honry.base.bean.model.RegisterPreregisterNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.EntityDao;
import cn.honry.outpatient.preregister.vo.EmpScheduleVo;

/**  
 *  
 * @className：PreregisterDao 
 * @Description：  预约挂号Dao
 * @Author：aizhonghua
 * @CreateDate：2015-6-18 下午01:51:57  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-18 下午01:51:57  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@SuppressWarnings({"all"})
public interface PreregisterDao extends EntityDao<RegisterPreregisterNow>{

	/**  
	 *  
	 * @Description：  预约挂号分页查询 - 获得列表
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-18 下午01:52:30  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-18 下午01:52:30  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<RegisterPreregisterNow> getPage(RegisterPreregisterNow registerPreregister,String page, String rows);

	/**  
	 *  
	 * @Description：   预约挂号分页查询 - 统计总条数
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-18 下午01:52:52  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-18 下午01:52:52  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	int getTotal(RegisterPreregisterNow registerPreregister);
	
	
	/**  
	 * @Description：  挂号科室（下拉框）
	 * @Author：wj
	 * @CreateDate：2015-11-11 上午10:40:16  
	 * @ModifyRmk： 
	 * @param q 用于下拉的即时查询（拼音，五笔，自定义）
	 * @version 1.0
	 */
	List<SysDepartment> deptCombobox(String q);
	/**  
	 * @Description：  挂号科室（下拉框）通过预约时间的
	 * @Author：wj
	 * @CreateDate：2015-11-11 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	List<SysDepartment> getDeptCom(String time,String q);
	/**  
	 * @Description：  挂号人员（下拉框）通过预约时间的
	 * @Author：wj
	 * @CreateDate：2015-11-11 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	List<EmpScheduleVo> getEmpCom(String page, String rows,String time, String gradeid, String deptid,String name);
	/**  
	 * @Description：  挂号专家下拉框(查询)
	 * @Author：wj
	 * @CreateDate：2016-1-5 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	List<SysEmployee> getPageSys(String page, String rows,String name,String deptId,String grade);
	/**  
	 * @Description：  挂号专家下拉框(查询)
	 * @Author：wj
	 * @CreateDate：2016-1-5 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	int getTotalSys(String name);
	int getTotalSys(String name,String deptId,String grade);

	int getTotalemp(String time, String gradeid, String deptid,String name);

	/**  
	 *  
	 * @Description：  医生工作量统计查询
	 * @Author：wujiao
	 * @CreateDate：2016-5-12 5:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param  dept科室
	 * @param  number身份证号
	 * @param  date预约时间
	 */
	String queryPreregisterByMid(String number, String dept, String date);
	
	/**  
	 *  
	 * @Description：  根据就诊卡ID查询患者是否在患者黑名单中
	 * @Author：zpty
	 * @CreateDate：2016-5-26  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	int getPatientCount(String idcardNo);

}
