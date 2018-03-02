package cn.honry.outpatient.itemlist.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.OutpatientItemlist;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.outpatient.itemlist.dao.ItemlistDAO;

@Repository("itemlistDAO")
@SuppressWarnings({ "all" })
public class ItemlistDAOImpl extends HibernateEntityDao<OutpatientItemlist> implements ItemlistDAO {
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	/**  
	 * @Description： 根据病历号 患者信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-8-19 下午01:09:49  
	 * @Modifier：ldl
	 * @ModifyDate：2015-12-11
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public Patient queryRegisterInfoByCaseNo(String midicalrecordId) {
		String hql = " from Patient where  del_flg=0 and stop_flg=0  and medicalrecordId = '"+midicalrecordId+"' ";
		List<Patient> patientList = super.find(hql, null);
		if(patientList!=null && patientList.size()>0){
			return patientList.get(0);
		}
		return new Patient();
	}
	/**  
	 * @Description： 开立科室下拉框
	 * @Modifier：ldl
	 * @ModifyDate：2015-12-11
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<SysDepartment> quertComboboxDept() {
		String hql = " from SysDepartment where del_flg=0 and stop_flg=0 and deptType = 'C' ";
		List<SysDepartment> departmentList = super.find(hql, null);
		if(departmentList!=null&&departmentList.size()>0){
			return departmentList;
		}
		return new ArrayList<SysDepartment>();
	}
	/**  
	 * @Description： 开立医生下拉框
	 * @Modifier：ldl
	 * @ModifyDate：2015-12-11
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<SysEmployee> quertComboboxEmp(String dept) {
		String hql = " from SysEmployee where del_flg=0 and stop_flg=0 ";
		if(StringUtils.isNotBlank(dept)){
			hql = hql + "   and deptId = '"+dept+"'  ";
		}
		List<SysEmployee> employeeList = super.find(hql, null);
		if(employeeList!=null&&employeeList.size()>0){
			return employeeList;
		}
		return new ArrayList<SysEmployee>();
	}
	/**  
	 * @Description： 合同单位下拉
	 * @Modifier：ldl
	 * @ModifyDate：2015-12-11
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<BusinessContractunit> quertComboboxCont() {
		String hql = " from BusinessContractunit where del_flg=0 and stop_flg=0  ";
		List<BusinessContractunit> contractunitList = super.find(hql, null);
		if(contractunitList!=null&&contractunitList.size()>0){
			return contractunitList;
		}
		return new ArrayList<BusinessContractunit>();
	}
}
