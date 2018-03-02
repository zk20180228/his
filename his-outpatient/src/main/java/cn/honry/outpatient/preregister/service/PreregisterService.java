package cn.honry.outpatient.preregister.service;

import java.util.List;

import cn.honry.base.bean.model.RegisterPreregisterNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.service.BaseService;
import cn.honry.outpatient.preregister.vo.EmpScheduleVo;

/**  
 *  
 * @className：PreregisterService 
 * @Description：   预约挂号信息Service 
 * @Author：aizhonghua
 * @CreateDate：2015-6-18 下午01:42:46  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-18 下午01:42:46  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
public interface PreregisterService extends BaseService<RegisterPreregisterNow> {

	/**  
	 *  
	 * @Description：  预约挂号信息分页查询 - 获得列表
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-18 下午01:43:04  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-18 下午01:43:04  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<RegisterPreregisterNow> getPage(String page, String rows,RegisterPreregisterNow registerPreregister);

	
	/**  
	 *  
	 * @Description：  预约挂号信息分页查询 - 统计总数
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-18 下午01:43:37  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-18 下午01:43:37  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	int getTotal(RegisterPreregisterNow registerPreregister);

	
	/**  
	 *  
	 * @Description：  预约挂号信息添加&修改
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-18 下午01:43:56  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-18 下午01:43:56  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	void saveOrUpdatePreregister(RegisterPreregisterNow registerPreregister);

	
	/**  
	 *  
	 * @Description：    预约挂号信息删除
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-18 下午01:44:12  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-18 下午01:44:12  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	void del(String id);

	

	
	

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
	 * @param rows 
	 * @param page 
	 */
	List<EmpScheduleVo> getEmpCom(String page, String rows,String time, String gradeid, String deptid,String name);

	/**  
	 * @Description： 获取参数预约时间
	 * @Author：wj
	 * @CreateDate：2015-11-11 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	String getParameterByCode(String string);

	/**  
	 * @Description：  挂号专家下拉框(查询)
	 * @Author：wj
	 * @CreateDate：2016-1-5 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 * @param name 
	 */
	List<SysEmployee> getPageSys(String page, String rows, String name,String deptId,String grade);

	/**  
	 * @Description：  挂号专家下拉框(查询)
	 * @Author：wj
	 * @CreateDate：2016-1-5 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	int getTotalSys(String name);
	int getTotalSys(String name,String deptId,String grade);
	/**  
	 * @Description：  挂号人员（下拉框）通过预约时间的-总条数
	 * @Author：wj
	 * @CreateDate：2015-11-11 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 * @param rows 
	 * @param page 
	 */

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
