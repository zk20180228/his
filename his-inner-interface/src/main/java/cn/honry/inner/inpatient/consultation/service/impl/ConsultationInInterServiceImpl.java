package cn.honry.inner.inpatient.consultation.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.InpatientBedinfoNow;
import cn.honry.base.bean.model.InpatientConsultation;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.inner.inpatient.consultation.dao.ConsultationInInterDao;
import cn.honry.inner.inpatient.consultation.service.ConsultationInInterService;
import cn.honry.inner.inpatient.inpatientBedInfo.dao.InpatientBedInfoInInterDAO;

/**   
* @className：ConsultationServiceImpl
* @description：  会诊申请单serviceImpl
* @author：tuchuanjiang
* @createDate：2015-12-11 下午19：24  
* @version 1.0
 */
@Service("consultationInInterService")
@Transactional
@SuppressWarnings({ "all" })
public class ConsultationInInterServiceImpl implements ConsultationInInterService {
	@Autowired
	@Qualifier(value = "consultationInInterDAO")
	private ConsultationInInterDao consultationDao;
	@Autowired
	@Qualifier(value = "inpatientBedInfoInInterDAO")
	private InpatientBedInfoInInterDAO inpatientBedInfoInInterDAO;

	/**  
	 * @Description： 通过id查询员工对象
	 * @Author：tcj
	 * @CreateDate：2015-12-19  上午10:09
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public SysEmployee queryById(String id) {
		return consultationDao.queryById(id);
	}

	@Override
	public InpatientConsultation get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(InpatientConsultation arg0) {
		
	}
	
	@Override
	public InpatientBedinfoNow queryBedInfoByMainID(String bedInfoId) {
		return inpatientBedInfoInInterDAO.queryBedInfoByMainID(bedInfoId);
	}
	
}
