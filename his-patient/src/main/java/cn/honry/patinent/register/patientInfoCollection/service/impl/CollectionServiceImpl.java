package cn.honry.patinent.register.patientInfoCollection.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.Patient;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.patinent.register.patientInfoCollection.dao.CollectionDAO;
import cn.honry.patinent.register.patientInfoCollection.service.CollectionServcie;
import cn.honry.patinent.register.patientInfoCollection.vo.Collection;
import cn.honry.utils.ShiroSessionUtils;


@Service("collectionServcie")
@Transactional
@SuppressWarnings({ "all" })
public class CollectionServiceImpl implements CollectionServcie{

	@Autowired
	@Qualifier(value = "collectionDAO")
	private CollectionDAO collectionDAO;
	
	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public Patient get(String id) {
		return null;
	}

	@Override
	public void saveOrUpdate(Patient entity) {
		
	}
	/**  
	 *  
	 * @Description： 根据病历号查询
	 * @Author：wujiao
	 * @CreateDate：2015-11-25 下午16:56:31
	 * @Modifier：wujiao
	 * @ModifyDate：2015-11-25 下午16:56:31 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public Collection getById(String idNo) {
		return collectionDAO.getById(idNo);
	}
	/**  
	 *  
	 * @Description： 修改患者表
	 * @Author：wujiao
	 * @CreateDate：2015-11-25 下午16:56:31
	 * @Modifier：wujiao
	 * @ModifyDate：2015-11-25 下午16:56:31 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public void saveOrUpdateCollection(Patient patient) {
		Patient patientOld=collectionDAO.findPatientOldbyid(patient.getId());
		patientOld.setPatientName(patient.getPatientName());
		patientOld.setPatientSex(patient.getPatientSex());
		patientOld.setPatientBirthday(patient.getPatientBirthday());
		patientOld.setPatientAddress(patient.getPatientAddress());
		patientOld.setPatientPhone(patient.getPatientPhone());
		patientOld.setPatientCertificatesno(patient.getPatientCertificatesno());
		patientOld.setPatientCertificatestype(patient.getPatientCertificatestype());
		patientOld.setPatientCity(patient.getPatientCity());
		patientOld.setPatientNationality(patient.getPatientNationality());
		patientOld.setPatientNation(patient.getPatientNation());
		patientOld.setPatientWorkunit(patient.getPatientWorkunit());
		patientOld.setPatientWorkphone(patient.getPatientWorkphone());
		patientOld.setPatientWarriage(patient.getPatientWarriage());
		patientOld.setPatientOccupation(patient.getPatientOccupation());
		patientOld.setPatientLinkphone(patient.getPatientLinkphone());
		patientOld.setPatientLinkman(patient.getPatientLinkman());
		patient.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		patient.setUpdateTime(new Date());
		collectionDAO.save(patientOld);
		OperationUtils.getInstance().conserve(patient.getId(),"患者信息补录","UPDATE","T_PATIENT",OperationUtils.LOGACTIONUPDATE);
	
		
	}

	@Override
	public List<Patient> isSearchFrom(String idNo) {
		return collectionDAO.isSearchFrom(idNo);
	}

	/**  
	 *  
	 * @Description： 根据就诊卡号查询
	 * @Author：zpty
	 * @CreateDate：2016-6-2 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public Collection getByIdcard(String idcard) {
		return collectionDAO.getByIdcard(idcard);
	}

	/**  
	 *  
	 * @Description： 根据身份证号查询
	 * @Author：zpty
	 * @CreateDate：2016-6-2 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public Collection getByCerNo(String cerNo) {
		return collectionDAO.getByCerNo(cerNo);
	}

	
	@Override
	public String checkIdcardName(String name,  String certificate, String certificatesno) {
		return collectionDAO.checkIdcardName(name,certificate,certificatesno);
	}
}
