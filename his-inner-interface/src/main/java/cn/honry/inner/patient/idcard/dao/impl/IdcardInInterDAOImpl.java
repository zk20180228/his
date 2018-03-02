package cn.honry.inner.patient.idcard.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.PatientIdcard;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.patient.idcard.dao.IdcardInInterDAO;
@Repository("idcardInInterDAO")
@SuppressWarnings({ "all" })
public class IdcardInInterDAOImpl extends HibernateEntityDao<PatientIdcard> implements IdcardInInterDAO{
	
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	};
	
	/**  
	 *  
	 * @Description：   通过病历号获得患者信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-11 下午02:58:41  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-11 下午02:58:41  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public PatientIdcard getIdcardByPatientNo(String patientNo) {
		String hql = "FROM PatientIdcard i WHERE i.patient = (SELECT p.id FROM Patient p WHERE p.medicalrecordId = '"+patientNo+"' AND p.stop_flg=0 AND p.del_flg=0)";
		List<PatientIdcard> idcardList = super.findByObjectProperty(hql, null);
		if(idcardList!=null&&idcardList.size()>0){
			return idcardList.get(0);
		}
		return null;
	}

	/**  
	 *  
	 * @Description：   通过就诊卡号获得患者信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-11 下午02:58:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-11 下午02:58:28  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public PatientIdcard getIdcardByIdCardNo(String idCardNo) {
		final String hql = "FROM PatientIdcard i WHERE i.idcardNo = ?";
		PatientIdcard idcard = (PatientIdcard) super.excHqlGetUniqueness(hql, idCardNo);
		if(idcard!=null&&StringUtils.isNotBlank(idcard.getId())){
			return idcard;
		}
		return null;
	}
	
}

