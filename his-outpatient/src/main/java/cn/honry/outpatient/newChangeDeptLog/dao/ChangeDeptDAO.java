package cn.honry.outpatient.newChangeDeptLog.dao;

import java.util.List;

import cn.honry.base.bean.model.FinanceInvoiceInfoNow;
import cn.honry.base.bean.model.FinanceInvoicedetailNow;
import cn.honry.base.bean.model.RegisterChangeDeptLog;
import cn.honry.base.bean.model.Registration;
import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.EntityDao;
import cn.honry.outpatient.newInfo.vo.InfoVo;

@SuppressWarnings({"all"})
public interface ChangeDeptDAO extends EntityDao<RegistrationNow>{
	/**  
	 * @Description：  挂号记录
	 * @Author：liudelin
	 * @CreateDate：2016-07-14
	 * @ModifyRmk：  
	 * @param： cardNo 就诊卡号
	 * @param： clinicCode 门诊号 
	 * @version 1.0
	 * 
	 */
	List<RegistrationNow> queryRegisterMain(String cardNo, String clinicCode);
	/**  
	 * @Description：  挂号换科记录
	 * @Author：liudelin
	 * @CreateDate：2016-07-14
	 * @ModifyRmk：  
	 * @param： ids 挂号主表ＩＤ集合
	 * @version 1.0
	 */
	List<RegisterChangeDeptLog> queryChangeDeptList(String ids);
	/**  
	 * @Description：  根据ID查询挂号记录
	 * @Author：liudelin
	 * @CreateDate：2016-07-14
	 * @ModifyRmk：  
	 * @param： id 挂号主表id
	 * @version 1.0
	 */
	RegistrationNow getById(String id);
	/**  
	 * @Description：  查询门诊挂号科室
	 * @Author：liudelin
	 * @CreateDate：2016-07-14
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<SysDepartment> changeDepartmentCombobox();
	/**  
	 * @Description：换科医生
	 * @Author：liudelin
	 * @CreateDate：2016-07-14
	 * @ModifyRmk：  
	 * @param： gradeX 挂号级别
	 * @param： newDept 挂号科室
	 * @version 1.0
	 */
	List<InfoVo> changeEmployeeCombobox(String gradeX, String newDept);
	/**  
	 * @Description：根据发票号修改状态
	 * @Author：liudelin
	 * @CreateDate：2016-07-14
	 * @ModifyRmk：  
	 * @param： invoiceNo 发票号
	 * @version 1.0
	 */
	List<FinanceInvoicedetailNow> findInvoicedetailList(String invoiceNo);
	/**  
	 * @Description：根据发票号修改状态
	 * @Author：liudelin
	 * @CreateDate：2016-07-14
	 * @ModifyRmk：  
	 * @param： invoiceNo 发票号
	 * @version 1.0
	 */
	FinanceInvoiceInfoNow findInvoiceInfo(String invoiceNo);
	/**  
	 * @Description：渲染科室
	 * @Author：liudelin
	 * @CreateDate：2016-07-21
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	List<SysDepartment> querydeptComboboxs();
	/**  
	 * @Description：渲染人员
	 * @Author：liudelin
	 * @CreateDate：2016-07-21
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	List<SysEmployee> queryempComboboxs();
	/**  
	 * @Description：查询挂号主表中换科记录
	 * @Author：liudelin
	 * @CreateDate：2016-07-25
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	List<RegistrationNow> queryChangeDept(String cardNo, String clinicCode);

}
