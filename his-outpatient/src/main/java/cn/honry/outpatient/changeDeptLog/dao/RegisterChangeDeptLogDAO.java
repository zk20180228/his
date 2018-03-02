package cn.honry.outpatient.changeDeptLog.dao;

import java.util.List;

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.PatientIdcard;
import cn.honry.base.bean.model.RegisterChangeDeptLog;
import cn.honry.base.bean.model.RegisterGrade;
import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.EntityDao;
import cn.honry.outpatient.info.vo.InfoVo;

@SuppressWarnings({"all"})
public interface RegisterChangeDeptLogDAO  extends EntityDao<RegisterChangeDeptLog>{
	/**  
	 *  
	 * @Description：  卡号查询挂号记录
	 * @Author：liudelin
	 * @ModifyDate：2015-6-26 下午09:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	RegisterInfo findInfo(String idcardNo);
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
	SysEmployee findSysEmployee(String expxrtId);
	/**  
	 *  
	 * @Description：  根据科室Id查询到科室名
	 * @param:expxrtId(科室的ID)
	 * @Author：liudelin
	 * @ModifyDate：2015-6-26 下午09:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	SysDepartment findSysDepartment(String deptId);
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
	RegisterGrade findfindRegisterGrade(String gradeId);
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
	BusinessContractunit findBusinessContractunit(String contractunitId);
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
	RegisterInfo findInfoId(String id);
	/**  
	 *  
	 * @Description：  下拉框科室
	 * @Author：liudelin
	 * @ModifyDate：2015-6-26 下午09:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<SysDepartment> getCombobox();
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
	List<InfoVo> EgetCombobox(String departmentId, String grade);
	/**
	 * 添加&修改
	 * @author  liudelin
	 * @date 2015-06-26
	 * @version 1.0
	 * @return
	 * @throws Exception
	 */
	void saveChange(RegisterChangeDeptLog entity);
	/**
	 * 根据就诊卡号查询该患者所挂的号
	 * @author  liudelin
	 * @date 2015-11-5
	 * @version 1.0
	 * @param no 
	 * @param state 
	 * @return
	 * @throws Exception
	 */
	List<RegisterInfo> findInfoList(String idcardNo, String no, String state);
	/**  
	 *  
	 * @Description：  患者信息
	 * @param:
	 * @Author：liudelin
	 * @ModifyDate：2015-11-5 下午09:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param no 
	 *
	 */
	RegisterInfo findPatientList(String idcardNo, String no);
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
	List<SysDepartment> querydeptCombobox();
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
	List<RegisterGrade> querygradeCombobox();
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
	List<SysEmployee> queryempCombobox();
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
	List<BusinessContractunit> querycontCombobox();
	/**  
	 *  
	 * @Description：  卡号查询换科记录
	 * @Author：liudelin
	 * @ModifyDate：2015-11-11上午9:31:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<RegisterChangeDeptLog> queryChangeDeptLogList(String ids);
	/**  
	 * @Description：  卡号查询换科记录
	 * @Author：liudelin
	 * @ModifyDate：2015-11-11上午9:31:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	PatientIdcard queryPatientIdcardByidcardNo(String idcardNo);
	/**  
	 * @Description：  根据就诊卡ID查询挂号记录
	 * @Author：liudelin
	 * @ModifyDate：2015-11-11上午9:31:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<RegisterInfo> findInfos(String id);
	
}
