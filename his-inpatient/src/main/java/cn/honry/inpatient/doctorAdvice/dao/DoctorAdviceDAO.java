package cn.honry.inpatient.doctorAdvice.dao;

import java.util.List;

import cn.honry.base.bean.model.BusinessBedward;
import cn.honry.base.bean.model.DepartmentContact;
import cn.honry.base.bean.model.InpatientConsultation;
import cn.honry.base.bean.model.InpatientDrugbilldetail;
import cn.honry.base.bean.model.InpatientExecbill;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientKind;
import cn.honry.base.dao.EntityDao;

public interface DoctorAdviceDAO  extends EntityDao<InpatientInfo>{
	/**  
	 *  
	 * @Description： 得到对应护士站（病区）信息
	 * @Author：yeguanqun
	 * @CreateDate：2015-12-09   
	 * @version 1.0
	 *
	 */
	List<BusinessBedward> findNurseCellCode(String inpatientNo);
	/**  
	 *  
	 * @Description： 得到患者会诊申请信息
	 * @Author：yeguanqun
	 * @CreateDate：2016-4-6   
	 * @version 1.0
	 *
	 */
	List<InpatientConsultation> findInpatientConsultation(String inpatientNo);
	
	/**  
	 *  
	 * @Description： 得到药物执行单树医嘱分类
	 * @Author：yeguanqun
	 * @CreateDate：2015-12-09   
	 * @version 1.0
	 *
	 */
	List<InpatientKind> treeDrugExe();
	/**  
	 *  
	 * @Description： 根据code查询医嘱分类
	 * @Author：yeguanqun
	 * @CreateDate：2015-12-09   
	 * @version 1.0
	 *
	 */
	List<InpatientKind> treeDrugExeByCode(String code);
	/**  
	 *  
	 * @Description： 查询医嘱执行单信息
	 * @Author：yeguanqun
	 * @CreateDate：2015-12-09   
	 * @version 1.0
	 *
	 */
	List<InpatientExecbill> queryDocAdvExe(String ids,String billName);
	/**  
	 *  
	 * @Description： 根据执行单编号查询医嘱执行单信息
	 * @Author：yeguanqun
	 * @CreateDate：2015-12-09   
	 * @version 1.0
	 *
	 */
	List<InpatientExecbill> queryDocAdvExeByNo(String billNo);
	/**  
	 *  
	 * @Description：根据登录科室获得病区
	 * @Author：yeguanqun
	 * @CreateDate：2015-12-09   
	 * @version 1.0
	 *
	 */
	List<DepartmentContact> queryDepartmentContact(String deptCode);
	/**
	 * 
	 * 
	 * <p>保存医嘱执行单 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月4日 下午4:50:52 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月4日 下午4:50:52 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param inpatientDrugbilldetail:
	 *
	 */
	void saveDrugBillDetail(InpatientDrugbilldetail inpatientDrugbilldetail);

	/**
	 * 
	 * 
	 * <p>查询所有执行单 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月4日 下午4:51:07 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月4日 下午4:51:07 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param inpatientDrugbilldetail
	 * @return:
	 *
	 */
	List<InpatientDrugbilldetail> queryAllBillDetail(InpatientDrugbilldetail inpatientDrugbilldetail);

	/**
	 * 
	 * 
	 * <p>通过id查找执行单 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月4日 下午4:51:40 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月4日 下午4:51:40 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param id 医嘱执行单Id
	 * @return:
	 *
	 */
	InpatientDrugbilldetail findInpatientDrugbilldetailById(String id);

	/**
	 * 
	 * 
	 * <p>更新执行单 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月4日 下午4:53:25 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月4日 下午4:53:25 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param oInpatientDrugbilldetail:
	 *
	 */
	void updateDrugBillDetail(InpatientDrugbilldetail oInpatientDrugbilldetail);
}
