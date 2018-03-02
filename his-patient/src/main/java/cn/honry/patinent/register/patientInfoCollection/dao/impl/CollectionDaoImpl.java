package cn.honry.patinent.register.patientInfoCollection.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.OutpatientRecipedetail;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.PatientAccountrepaydetail;
import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.patient.patient.vo.PatientIdcardVO;
import cn.honry.patinent.account.dao.AccountrepayDetailDAO;
import cn.honry.patinent.register.patientInfoCollection.dao.CollectionDAO;
import cn.honry.patinent.register.patientInfoCollection.vo.Collection;
import cn.honry.utils.HisParameters;


@Repository("collectionDAO")
@SuppressWarnings({ "all" })
public class CollectionDaoImpl extends HibernateEntityDao<Patient> implements CollectionDAO{
	
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	/** 账户数据库操作类 **/
	@Autowired
	@Qualifier(value = "repayDetailDAO")
	private AccountrepayDetailDAO repayDetailDAO;
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
		/**
		 * 试过了一切查询方法 各种报错  最后选择了笨方法   
		 * findByObjectProperty find 等  报querytimeoutexception异常
		 */
		List<Collection> voList= new ArrayList<Collection>();
		Collection vo=new Collection();
		String sql= "select p.PATINENT_ID as id ,p.PATIENT_NAME as patientName,p.PATIENT_SEX as patientSex,"
				+ "p.PATIENT_BIRTHDAY as patientBirthday ,p.PATIENT_ADDRESS as patientAddress,p.PATIENT_CITY as patientCity, "
				+ "p.PATIENT_PHONE as patientPhone ,p.PATIENT_CERTIFICATESTYPE as patientCertificatestype,"
				+ "p.PATIENT_CERTIFICATESNO as patientCertificatesno , p.PATIENT_OCCUPATION as patientOccupation,"
				+ "p.PATIENT_BIRTHPLACE as patientBirthplace, p.PATIENT_NATIONALITY as patientNationality,"
				+ "p.PATIENT_NATION as patientNation ,p.PATIENT_WORKUNIT as patientWorkunit,"
				+ "p.PATIENT_WORKPHONE as patientWorkphone,p.PATIENT_WARRIAGE as patientWarriage,"
				+ "p.PATIENT_LINKMAN as patientLinkman,p.PATIENT_WORKPHONE as patientWorkphone,p.PATIENT_LINKMAN as patientLinkman,"
				+ "p.PATIENT_LINKPHONE as patientLinkphone "
				+ "FROM t_Patient p WHERE p.del_Flg = 0 AND p.medicalrecord_Id = '"+idNo+"'";
		SQLQuery info = this.getSessionFactory().openSession().createSQLQuery(sql);
		info.addScalar("id");
		info.addScalar("patientName");
		info.addScalar("patientSex",Hibernate.INTEGER);
		info.addScalar("patientBirthday");
		info.addScalar("patientAddress");
		info.addScalar("patientCity");
		info.addScalar("patientPhone");
		info.addScalar("patientCertificatestype");
		info.addScalar("patientCertificatesno");
		info.addScalar("patientOccupation");
		info.addScalar("patientBirthplace");
		info.addScalar("patientNationality");
		info.addScalar("patientNation");
		info.addScalar("patientWorkunit");
		info.addScalar("patientWorkphone");
		info.addScalar("patientWarriage");
		info.addScalar("patientLinkman");
		info.addScalar("patientWorkphone");
		info.addScalar("patientLinkphone");
		List<Patient> list = info.setResultTransformer(Transformers.aliasToBean(Patient.class)).list();
		if (list.size()!=0) { 
			 vo.setId(list.get(0).getId());
			 vo.setName(list.get(0).getPatientName());
			 vo.setSex((Integer)list.get(0).getPatientSex());
			 vo.setBirthday(list.get(0).getPatientBirthday());
			 vo.setAddress(list.get(0).getPatientAddress());
			 vo.setPatientCity(list.get(0).getPatientCity());
			 vo.setPhone(list.get(0).getPatientPhone());
			 vo.setCertificatestype(list.get(0).getPatientCertificatestype());
			 vo.setCertificatesno(list.get(0).getPatientCertificatesno());
			 vo.setOccupation(list.get(0).getPatientOccupation());
			 vo.setBirthplace(list.get(0).getPatientBirthplace());
			 vo.setNationality(list.get(0).getPatientNationality());
			 vo.setNation(list.get(0).getPatientNation());
			 vo.setWorkunit(list.get(0).getPatientWorkunit());
			 vo.setWorkphone(list.get(0).getPatientWorkphone());
			 vo.setWarriage(list.get(0).getPatientWarriage());
			 vo.setLinkman(list.get(0).getPatientLinkman());
			 vo.setLinkphone(list.get(0).getPatientLinkphone());
			 voList.add(vo);
			return voList.get(0);
		}
		return null;
   }
	/**  
	 *  
	 * @Description： 根据id查询
	 * @Author：wujiao
	 * @CreateDate：2015-11-25 下午16:56:31
	 * @Modifier：wujiao
	 * @ModifyDate：2015-11-25 下午16:56:31 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public Patient findPatientOldbyid(String id) {
		String hql="FROM Patient p WHERE p.del_flg = 0 AND p.id = '"+id+"' ";
		List<Patient> list = super.findByObjectProperty(hql, null);
		return list.get(0);
	}
	@Override
	public List<Patient> isSearchFrom(String idNo) {
		String hql=" from Patient t where t.del_flg=0 and t.stop_flg=0 and t.cardNo = '"+idNo+"'";
		List<Patient> list=super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<Patient>();
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
		Collection vo=new Collection();
		String hql="FROM Patient p WHERE p.del_flg = 0 AND p.id in (select i.patient from PatientIdcard i where i.del_flg = 0 and i.stop_flg = 0 and i.idcardNo='"+idcard+"')";
		List<Patient> list = super.findByObjectProperty(hql, null);
		//这里没有去验证是否收费,是否开医嘱的问题,直接用来查询了
		if (list.size()!=0) { 
			 vo.setId(list.get(0).getId());
			 vo.setName(list.get(0).getPatientName());
			 vo.setSex(list.get(0).getPatientSex());
			 vo.setBirthday(list.get(0).getPatientBirthday());
			 vo.setPatientCity(list.get(0).getPatientCity());//省市县
			 vo.setAddress(list.get(0).getPatientAddress());
			 vo.setPhone(list.get(0).getPatientPhone());
			 vo.setCertificatestype(list.get(0).getPatientCertificatestype());
			 vo.setCertificatesno(list.get(0).getPatientCertificatesno());
			 vo.setOccupation(list.get(0).getPatientOccupation());
			 vo.setBirthplace(list.get(0).getPatientBirthplace());
			 vo.setNationality(list.get(0).getPatientNationality());
			 vo.setNation(list.get(0).getPatientNation());
			 vo.setWorkunit(list.get(0).getPatientWorkunit());
			 vo.setWorkphone(list.get(0).getPatientWorkphone());
			 vo.setWarriage(list.get(0).getPatientWarriage());
			 vo.setLinkman(list.get(0).getPatientLinkman());
			 vo.setLinkphone(list.get(0).getPatientLinkphone());
			return vo;
			}
		return null;
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
		Collection vo=new Collection();
		String hql="FROM Patient p WHERE p.del_flg = 0 AND p.patientCertificatesno = '"+cerNo+"'";
		List<Patient> list = super.findByObjectProperty(hql, null);
		//这里没有去验证是否收费,是否开医嘱的问题,直接用来查询了
		if (list.size()!=0) { 
			 vo.setId(list.get(0).getId());
			 vo.setName(list.get(0).getPatientName());
			 vo.setSex(list.get(0).getPatientSex());
			 vo.setBirthday(list.get(0).getPatientBirthday());
			 vo.setAddress(list.get(0).getPatientCity());
			 vo.setPhone(list.get(0).getPatientPhone());
			 vo.setCertificatestype(list.get(0).getPatientCertificatestype());
			 vo.setCertificatesno(list.get(0).getPatientCertificatesno());
			 vo.setOccupation(list.get(0).getPatientOccupation());
			 vo.setBirthplace(list.get(0).getPatientBirthplace());
			 vo.setNationality(list.get(0).getPatientNationality());
			 vo.setNation(list.get(0).getPatientNation());
			 vo.setWorkunit(list.get(0).getPatientWorkunit());
			 vo.setWorkphone(list.get(0).getPatientWorkphone());
			 vo.setWarriage(list.get(0).getPatientWarriage());
			 vo.setLinkman(list.get(0).getPatientLinkman());
			 vo.setLinkphone(list.get(0).getPatientLinkphone());
			return vo;
			}
		return null;
	}
	
	/**
	 * @Description:验证患者是否已存在
	 * @Author：  zpty
	 * @CreateDate： 2015-12-24
	 * @param 
	 * @middle zpty
	 * @middleDAte 2016-3-23 
	 * @return   
	 * @return boolean  
	 * @version 1.0
	**/	
	@Override
	public String checkIdcardName(String name, String certificate, String certificatesno) {
		StringBuilder sql = new StringBuilder();
		sql.append("select p.PATINENT_ID as id,p.PATIENT_NAME as patientName  from T_PATIENT p  WHERE p.DEL_FLG=0 ");
		if(StringUtils.isNotBlank(name)){//姓名
			sql.append(" AND p.PATIENT_NAME != :name");
		}
		
		if(StringUtils.isNotBlank(certificate)){//证件类型
			sql.append(" AND p.PATIENT_CERTIFICATESTYPE = :certificate");
		}
		if(StringUtils.isNotBlank(certificatesno)){//证件号
			sql.append(" AND p.PATIENT_CERTIFICATESNO = :certificatesno");
		}
		
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		query.addScalar("id").addScalar("patientName");
		if(StringUtils.isNotBlank(certificate)){//证件类型
			query.setParameter("certificate", certificate);
		}
		if(StringUtils.isNotBlank(certificatesno)){//证件号
			query.setParameter("certificatesno", certificatesno);
		}
		if(StringUtils.isNotBlank(name)){//姓名
			query.setParameter("name", name);
		}
		query.setResultTransformer(Transformers.aliasToBean(PatientIdcardVO.class));
		List<PatientIdcardVO> list1 = query.list();
		if(!list1.isEmpty()&&list1.size()>0){
			return "nameNO";
		}
		return "ok";
	}
}
