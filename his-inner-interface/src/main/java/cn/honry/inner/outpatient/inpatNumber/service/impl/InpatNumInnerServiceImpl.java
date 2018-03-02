package cn.honry.inner.outpatient.inpatNumber.service.impl;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientNumber;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.inner.inpatient.info.dao.InpatientInfoInInterDAO;
import cn.honry.inner.outpatient.inpatNumber.dao.InpatNumInnerDao;
import cn.honry.inner.outpatient.inpatNumber.service.InpatNumInnerService;
import cn.honry.inner.patient.patient.dao.PatinmentInnerDao;
import cn.honry.utils.ShiroSessionUtils;

@Service("inpatNumInnerService")
@Transactional
@SuppressWarnings({ "all" })
public class InpatNumInnerServiceImpl implements InpatNumInnerService {
	
	@Autowired
	@Qualifier(value = "inpatNumInnerDao")
	private InpatNumInnerDao inpatNumInnerDao;
	
	@Autowired
	@Qualifier(value = "inpatientInfoInInterDAO")
	private InpatientInfoInInterDAO inpatientInfoInInterDAO;
	@Autowired
	@Qualifier(value="patinmentInnerDao")
	private PatinmentInnerDao patinmentInnerDao;
	
	@Override
	public InpatientNumber get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(InpatientNumber arg0) {
		
	}
	/**  
	 * @Description：  根据住院主表ID
	 * @Author：zhangjin
	 * @CreateDate：2016-11-15
	 * @ModifyRmk：
	 * @param:   
	 * @version 1.0
	 */
	@Override
	public void saveInpatNum(String id) {
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		SysDepartment dept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		if(StringUtils.isNotBlank(id)){
			InpatientInfoNow inpat=inpatientInfoInInterDAO.get(id);
			if(inpat!=null){
				Patient pat=patinmentInnerDao.queryByMedicalrecordId(inpat.getMedicalrecordId());
				
				InpatientNumber inpaNum=inpatNumInnerDao.getInpatientNo(inpat.getInpatientNo());
				if(inpaNum!=null){
					inpaNum.setBedinfoId(inpat.getBedId());
					inpaNum.setCaseNo(pat.getCaseNo());
					inpaNum.setDeptCode(inpat.getDeptCode());
					inpaNum.setIdcardNo(inpat.getIdcardNo()); 
					inpaNum.setName(inpat.getPatientName());
					inpaNum.setInDate(inpat.getInDate());
					inpaNum.setOutDate(inpat.getOutDate());
					inpaNum.setPaykindCode(inpat.getPaykindCode());
					inpaNum.setInpatientNo(inpat.getInpatientNo());
					inpaNum.setMedicalrecordId(inpat.getMedicalrecordId());
					inpaNum.setUpdateTime(new Date());
					inpaNum.setUpdateUser(user.getAccount());
					inpatNumInnerDao.update(inpaNum);
				}else{
					InpatientNumber num=new InpatientNumber();
					num.setId(null);
					num.setBedinfoId(inpat.getBedId());
					num.setCaseNo(pat.getCaseNo());
					num.setDeptCode(inpat.getDeptCode());
					num.setIdcardNo(inpat.getIdcardNo());
					num.setName(inpat.getPatientName());
					num.setInDate(inpat.getInDate());
					num.setOutDate(inpat.getOutDate());
					num.setPaykindCode(inpat.getPaykindCode());
					num.setInpatientNo(inpat.getInpatientNo());
					num.setMedicalrecordId(inpat.getMedicalrecordId());
					num.setCreateDept(dept.getDeptCode());
					num.setCreateTime(new Date());
					num.setCreateUser(user.getAccount());
					inpatNumInnerDao.save(num);
				}
			}
		}
	}

}
