package cn.honry.outpatient.changeDeptLog.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.PatientIdcard;
import cn.honry.base.bean.model.RegisterChangeDeptLog;
import cn.honry.base.bean.model.RegisterGrade;
import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.outpatient.changeDeptLog.dao.RegisterChangeDeptLogDAO;
import cn.honry.outpatient.changeDeptLog.service.RegisterChangeDeptLogService;
import cn.honry.outpatient.info.vo.InfoVo;

@Service("registerChangeDeptLogService")
@Transactional
@SuppressWarnings({ "all" })
public class RegisterChangeDeptLogServiceImpl implements RegisterChangeDeptLogService{

	/** 换科数据库操作类 **/
	@Autowired
	@Qualifier(value = "registerChangeDeptLogDAO")
	private RegisterChangeDeptLogDAO registerChangeDeptLogDAO;
	
	
	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public RegisterChangeDeptLog get(String id) {
		return null;
	}

	@Override
	public void saveOrUpdate(RegisterChangeDeptLog entity) {
		
	}
	/**  
	 *  
	 * @Description：  卡号查询挂号记录
	 * @Author：liudelin
	 * @ModifyDate：2015-6-26 下午09:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public RegisterInfo findInfo(String idcardNo) {
		RegisterInfo lst = registerChangeDeptLogDAO.findInfo(idcardNo);
		return lst;
	}
	/**  
	 *  
	 * @Description：  根据医生Id查询到医生姓名
	 * @param:expxrtId(医生的ID)
	 * @Author：liudelin
	 * @ModifyDate：2015-6-26 下午09:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public SysEmployee findSysEmployee(String expxrtId) {
		SysEmployee lst = registerChangeDeptLogDAO.findSysEmployee(expxrtId);
		return lst;
	}
	/**  
	 *  
	 * @Description：  根据科室Id查询到科室名
	 * @param:deptId(科室的ID)
	 * @Author：liudelin
	 * @ModifyDate：2015-6-26 下午09:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public SysDepartment findSysDepartment(String deptId) {
		SysDepartment lst = registerChangeDeptLogDAO.findSysDepartment(deptId);
		return lst;
	}
	/**  
	 *  
	 * @Description：  根据级别Id查询到级别名
	 * @param:gradeId(级别的ID)
	 * @Author：liudelin
	 * @ModifyDate：2015-6-26 下午09:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */

	@Override
	public RegisterGrade findRegisterGrade(String gradeId) {
		RegisterGrade  lst = registerChangeDeptLogDAO.findfindRegisterGrade(gradeId);
		return lst;
	}
	/**  
	 *  
	 * @Description：  根据合同单位Id查询到合同单位名
	 * @param:contractunitId(合同单位的ID)
	 * @Author：liudelin
	 * @ModifyDate：2015-6-26 下午09:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */

	@Override
	public BusinessContractunit findBusinessContractunit(String contractunitId) {
		BusinessContractunit  lst = registerChangeDeptLogDAO.findBusinessContractunit(contractunitId);
		return lst;
	}
	/**  
	 *  
	 * @Description：  根据挂号Id查询到医生和科室
	 * @param:id（挂号表id）
	 * @Author：liudelin
	 * @ModifyDate：2015-6-26 下午09:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */

	@Override
	public RegisterInfo findInfoId(String id) {
		RegisterInfo lst = registerChangeDeptLogDAO.findInfoId(id);
		return lst;
	}
	/**  
	 *  
	 * @Description：  下拉框科室
	 * @Author：liudelin
	 * @ModifyDate：2015-6-26 下午09:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */

	@Override
	public List<SysDepartment> getCombobox() {
		return registerChangeDeptLogDAO.getCombobox();
	}
	/**  
	 *  
	 * @Description：  下拉框医生
	 * @param:departmentId(科室Id)，grade（级别id）
	 * @Author：liudelin
	 * @ModifyDate：2015-6-26 下午09:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */

	@Override
	public List<InfoVo> EgetCombobox(String departmentId, String grade) {
		return registerChangeDeptLogDAO.EgetCombobox(departmentId,grade);
	}
	/**
	 * 添加&修改
	 * @author  liudelin
	 * @date 2015-06-26
	 * @version 1.0
	 * @return
	 * @throws Exception
	 */
	@Override
	public void saveChange(RegisterChangeDeptLog entity) {
		registerChangeDeptLogDAO.saveChange(entity);
		OperationUtils.getInstance().conserve(entity.getId(),"挂号换科","UPDATE","T_REGISTER_INFO",OperationUtils.LOGACTIONUPDATE);
	}
	/**
	 * 根据就诊卡号查询该患者所挂的号
	 * @author  liudelin
	 * @date 2015-11-5
	 * @version 1.0
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<RegisterInfo> findInfoList(String idcardNo,String no, String state) {
		List<RegisterInfo> lst = registerChangeDeptLogDAO.findInfoList(idcardNo,no,state);
		return lst;
	}
	/**  
	 *  
	 * @Description：  患者信息
	 * @param:
	 * @Author：liudelin
	 * @ModifyDate：2015-11-5 下午09:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public RegisterInfo findPatientList(String idcardNo,String no) {
		RegisterInfo lst = registerChangeDeptLogDAO.findPatientList(idcardNo,no);
		return lst;
	}
	/**  
	 *  
	 * @Description：  渲染部门
	 * @param:
	 * @Author：liudelin
	 * @ModifyDate：2015-11-10 下午09:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<SysDepartment> querydeptCombobox() {
		List<SysDepartment> lst = registerChangeDeptLogDAO.querydeptCombobox();
		return lst;
	}
	/**  
	 *  
	 * @Description：  渲染级别
	 * @param:
	 * @Author：liudelin
	 * @ModifyDate：2015-11-10 下午09:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<RegisterGrade> querygradeCombobox() {
		List<RegisterGrade> lst = registerChangeDeptLogDAO.querygradeCombobox();
		return lst;
	}
	/**  
	 *  
	 * @Description：  渲染人员
	 * @param:
	 * @Author：liudelin
	 * @ModifyDate：2015-11-10 下午09:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<SysEmployee> queryempCombobox() {
		List<SysEmployee> lst = registerChangeDeptLogDAO.queryempCombobox();
		return lst;
	}
	/**  
	 *  
	 * @Description：  渲染合同单文
	 * @param:
	 * @Author：liudelin
	 * @ModifyDate：2015-11-10 下午09:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<BusinessContractunit> querycontCombobox() {
		List<BusinessContractunit> lst = registerChangeDeptLogDAO.querycontCombobox();
		return lst;
	}
	/**  
	 *  
	 * @Description：  卡号查询换科记录
	 * @Author：liudelin
	 * @ModifyDate：2015-11-11上午9:31:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<RegisterChangeDeptLog> queryChangeDeptLogList(String ids) {
		List<RegisterChangeDeptLog> lst = registerChangeDeptLogDAO.queryChangeDeptLogList(ids);
		return lst;
	}
	/**  
	 * @Description：  卡号查询换科记录
	 * @Author：liudelin
	 * @ModifyDate：2015-11-11上午9:31:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public PatientIdcard queryPatientIdcardByidcardNo(String idcardNo) {
		PatientIdcard patientIdcard = registerChangeDeptLogDAO.queryPatientIdcardByidcardNo(idcardNo);
		return patientIdcard;
	}

	@Override
	public List<RegisterInfo> findInfos(String id) {
		List<RegisterInfo> registerInfo = registerChangeDeptLogDAO.findInfos(id);
		return registerInfo;
	}

	
}
