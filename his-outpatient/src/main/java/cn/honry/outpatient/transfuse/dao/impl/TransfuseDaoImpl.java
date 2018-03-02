package cn.honry.outpatient.transfuse.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessFrequency;
import cn.honry.base.bean.model.OutpatientMixLiquid;
import cn.honry.base.bean.model.OutpatientRecipedetail;
import cn.honry.base.bean.model.OutpatientRecipedetailNow;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.Registration;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.outpatient.transfuse.dao.TransfuseDao;
/**   
* @className：TransfuseDaoImpl
* @description：  门诊配液DaoImpl
* @author：tuchuanjiang
* @createDate：2016-06-21  
* @version 1.0
 */
@Repository("transfuseDao")
@SuppressWarnings({ "all" })
public class TransfuseDaoImpl extends HibernateEntityDao<OutpatientMixLiquid> implements TransfuseDao {
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	/**
	 * 通过病历号查询患者的处方信息
	 * @author tuchuanjiang
	 * @CreateDate： 2016-06-21
	 * @version 1.0
	 */
	@Override
	public List<OutpatientRecipedetailNow> queryPatientYZInfo(String clinicCode) {
//		and injectNumber >0
		String hql=" from OutpatientRecipedetailNow where del_flg=0 and stop_flg=0 and injectNumber >0 and patientNo = '"+clinicCode+"' ";
		List<OutpatientRecipedetailNow> oprl=super.find(hql, null);
		if(oprl!=null&&oprl.size()>0){
			for(int i=0;i<oprl.size();i++){
				String hql1=" from OutpatientMixLiquid where del_flg=0 and stop_flg=0 and patientNo = '"+clinicCode+"' and itemCode='"+oprl.get(i).getItemCode()+"'";
				List<OutpatientMixLiquid> opm=super.find(hql1, null);
				//配液次数等于处方中院内次数说明注射完毕，为已院注信息
				if(oprl.get(i).getInjectNumber()==opm.size()){
					oprl.remove(i);
					i=i-1;
				}
			}
			return oprl;
		}
		return new ArrayList<OutpatientRecipedetailNow>();
	}
	@Override
	public List<Patient> queryPatientInfo(String clinicCode) {
		String hql="select p.id as id,p.patientName as patientName,p.patientSex as patientSex,"
				+ " p.medicalrecordId as medicalrecordId,p.patientBirthday as patientBirthday "
				+ " from Patient p where p.cardNo=?";
		List<Patient> list = this.createQuery(hql, clinicCode).
				setResultTransformer(Transformers.aliasToBean(Patient.class)).list();
		if(list!=null && list.size()>0){
			return list;
		}
		return new ArrayList<Patient>();
	}
	@Override
	public List<SysEmployee> queryDoctrans() {
		String hql=" from SysEmployee where del_flg=0 and stop_flg=0";
		List<SysEmployee> empl =super.find(hql, null);
		if(empl!=null&&empl.size()>0){
			return empl;
		}
		return new ArrayList<SysEmployee>();
	}
	@Override
	public List<SysDepartment> queryDeptTrans() {
		String hql=" from SysDepartment where del_flg=0 and stop_flg=0";
		List<SysDepartment> deptl=super.find(hql, null);
		if(deptl!=null&&deptl.size()>0){
			return deptl;
		}
		return new ArrayList<SysDepartment>();
	}
	@Override
	public List<BusinessFrequency> queryFrequencyTrans() {
		String hql=" from BusinessFrequency where del_flg=0 and stop_flg=0";
		List<BusinessFrequency> freql=super.find(hql, null);
		if(freql!=null&&freql.size()>0){
			return freql;
		}
		return new ArrayList<BusinessFrequency>();
	}
	@Override
	public List<OutpatientMixLiquid> queryMixliquid(String clinicCode) {
		String hql=" from OutpatientMixLiquid where del_flg=0 and stop_flg=0 and patientNo=? order by confirmDate";
		List<OutpatientMixLiquid> opm=super.find(hql, clinicCode);
		if(opm!=null&&opm.size()>0){
			return opm;
		}
		return new ArrayList<OutpatientMixLiquid>();
	}
	@Override
	public List<User> queryUsertrans() {
		String hql=" from User where del_flg=0 and stop_flg=0";
		List<User> ul=super.find(hql, null);
		if(ul!=null&&ul.size()>0){
			return ul;
		}
		return new ArrayList<User>();
	}
	@Override
	public List<SysEmployee> queryEmptrans() {
		String hql=" from SysEmployee where del_flg=0 and stop_flg=0";
		List<SysEmployee> empl=super.find(hql, null);
		if(empl!=null&&empl.size()>0){
			return empl;
		}
		return new ArrayList<SysEmployee>();
	}
	
}
