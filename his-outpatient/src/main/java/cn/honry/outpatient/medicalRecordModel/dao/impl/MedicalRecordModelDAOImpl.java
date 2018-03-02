package cn.honry.outpatient.medicalRecordModel.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.util.WebUtils;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessMedicalRecord;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.outpatient.medicalRecordModel.dao.MedicalRecordModelDAO;
import cn.honry.utils.ShiroSessionUtils;

/**  
 *  
 * @className：MedicalRecord
 * @Description：  电子病历模版表
 * @Author：ldl
 * @CreateDate：2015-7-13   
 * @version 1.0
 *
 */
@Repository("medicalRecordModelDAO")
@SuppressWarnings({ "all" })
public class MedicalRecordModelDAOImpl extends HibernateEntityDao<BusinessMedicalRecord> implements MedicalRecordModelDAO{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {

		super.setSessionFactory(sessionFactory);
	}
	/**  
	 *  
	 * @Description：  保存电子病历模版表
	 * @Author：liudelin
	 * @ModifyDate：2015-7-13 下午05：43
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Override
	public void saveOrUpdateRecord(BusinessMedicalRecord entity) {
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		if(StringUtils.isBlank(entity.getId())){//保存
			SysDepartment dept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
			SysEmployee emp = ShiroSessionUtils.getCurrentEmployeeFromShiroSession();
			entity.setId(null);
			entity.setDocCode(emp.getJobNo());
			entity.setDeptCode(dept.getDeptCode());
			entity.setCreateUser(user.getAccount());
			entity.setCreateDept(dept.getDeptCode());
			entity.setStop_flg(0);
			entity.setDel_flg(0);
			entity.setCreateTime(new Date());
			super.save(entity);
		}else{//修改
			entity.setUpdateUser(user.getAccount());
			entity.setUpdateTime(new Date());
			super.update(entity);
		}
	}
	/**  
	 *  
	 * @Description：  电子病历模板列表
	 * @Author：liudelin
	 * @ModifyDate：2015-7-14 上午10：13
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Override
	public List<BusinessMedicalRecord> queryMedicalRecordList(String id, String recordType) {
		String hql="from BusinessMedicalRecord where del_flg = 0";
		if(StringUtils.isNotBlank(id)){
			hql=hql+"and id = '"+id+"'";
		}
		if(StringUtils.isNotBlank(recordType)){
			hql=hql+"and recordType = '"+recordType+"'";
		}
		List<BusinessMedicalRecord> medicalRecordList = super.find(hql, null);
		if(medicalRecordList==null||medicalRecordList.size()<=0){
			return new ArrayList<BusinessMedicalRecord>();
		}
		return medicalRecordList;
	}
	/**  
	 *  
	 * @Description：  电子病历模版树(个别)
	 * @Author：liudelin
	 * @ModifyDate：2015-7-14 上午10：13
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Override
	public List<BusinessMedicalRecord> medicalRecordOtherTree(int recordType) {
		String hql="FROM BusinessMedicalRecord  WHERE del_flg = '0' AND stop_flg = '0' AND recordType = '"+recordType+"'";
		List<BusinessMedicalRecord> medicalRecordList=super.findByObjectProperty(hql, null);
		if(medicalRecordList!=null && medicalRecordList.size()>0){
			return medicalRecordList;
		}
		return null;
	}
	

}
