package cn.honry.patinent.idcard.dao.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InpatientAccount;
import cn.honry.base.bean.model.OutpatientAccount;
import cn.honry.base.bean.model.PatientBlackList;
import cn.honry.base.bean.model.PatientIdcard;
import cn.honry.base.bean.model.RegisterPreregister;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.patient.patient.dao.PatinmentInnerDao;
import cn.honry.inner.patient.patient.service.PatinentInnerService;
import cn.honry.inner.patient.patientBlack.dao.PatientBlackInnerDAO;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.patinent.idcard.dao.IdcardDAO;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.ShiroSessionUtils;
@Repository("idcardDAO")
@SuppressWarnings({ "all" })
public class IdcardDAOImpl extends HibernateEntityDao<PatientIdcard> implements IdcardDAO{
	
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	};
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterDAO;//医院参数dao
	@Autowired
	@Qualifier(value = "patinmentInnerDao")
	private PatinmentInnerDao patinmentDao;
	
	private PatinentInnerService patinentInnerService;
	@Autowired
	@Qualifier(value = "patientBlackInnerDAO")
	private PatientBlackInnerDAO patientBlackInnerDAO;
	@Override
	public List<PatientIdcard> getPage(PatientIdcard entity, String page,String rows) {
		String hql = joint(entity);
		return super.getPage(hql, page, rows);
	}

	@Override
	public int getTotal(PatientIdcard entity) {
		String hql = joint(entity);
		return super.getTotal(hql);
	}

	public String joint(PatientIdcard entity){
		String hql="FROM PatientIdcard i WHERE i.del_flg = 0 ";
		if(entity!=null){
			if(StringUtils.isNotBlank(entity.getPatient().getPatientName())){
				hql = hql+" AND (i.patient.patientName LIKE '%"+entity.getPatient().getPatientName()+"%'" 
					+ " or i.idcardNo LIKE '%"+entity.getPatient().getPatientName()+"%'"
					+ " or p.medicalrecordId LIKE '%"+entity.getPatient().getPatientName()+"%'"
					+ " or i.patient.patientHandbook LIKE '%"+entity.getPatient().getPatientName()+"%')";
			}
			if(StringUtils.isNotBlank(entity.getIdcardType())){
				hql = hql+" AND i.idcardType = '"+entity.getIdcardType()+"'";
			}
			
			//用于账户管理里的就诊卡信息显示  （这里为了简单起见 写到了这里）
			if(StringUtils.isNotBlank(entity.getCreateUser())){
				hql = hql+" AND i.idcardNo = '"+entity.getCreateUser()+"'";
			}
		}
		hql = hql+" ORDER BY i.idcardCreatetime";
		return hql;
	}
	@Override
	public PatientIdcard queryById(String patientIdcard) {
		StringBuilder sql = new StringBuilder();
		sql.append("from PatientIdcard where id = "+patientIdcard+" ");
		return (PatientIdcard) this.getSession().get(PatientIdcard.class, patientIdcard);
	}

	@Override
	public PatientIdcard queryByIdcardNo(String idcardNo) {
		String sql = "from PatientIdcard where idcardNo = '"+ idcardNo +"' and del_flg=0 ";
		List<PatientIdcard> list=this.getSession().createQuery(sql).list();
		return list.size()==0?null:list.get(0);
		
	}
	@Override
	public PatientIdcard getkh(String idcardNo) {
		 SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");   
		String time = parameterDAO.getParameterByCode("reserveTime");
		String day = parameterDAO.getParameterByCode("blackListDays");
		String rhql="from RegisterPreregister r where medicalrecordId in " +
				"(select p.medicalrecordId from Patient p where p.id in" +
				"(select patient from PatientIdcard where idcardNo=?)) " +
						"and r.status=4 and r.del_flg = 0 ";
		List<RegisterPreregister> reList = this.find(rhql, idcardNo);
		if(reList!=null&&reList.size()>0){
			for (RegisterPreregister re : reList) {
				
				re.setMissNumber(reList.size());
				patientBlackInnerDAO.save(re);
			if(reList.get(0).getMissNumber()<Integer.parseInt(time)){
				String hql="FROM PatientIdcard i WHERE i.del_flg = 0 ";
				hql = hql+" AND i.idcardNo = '"+idcardNo+"' ";
				List<PatientIdcard> cardList = super.findByObjectProperty(hql, null);
				if (cardList.size()!=0) {
					return cardList.get(0);
				}
				return null;
			}else{
				String phql="from PatientBlackList p where p.medicalrecordId in " +
						"(select p.medicalrecordId from Patient p where p.id in" +
						"(select patient from PatientIdcard where idcardNo='"+idcardNo+"')) and p.del_flg = 0 ";
				List<PatientBlackList> blackList = patientBlackInnerDAO.findByObjectProperty(phql, null);
				if(blackList==null||blackList.size()==0){
					String ehql="FROM PatientIdcard i WHERE i.del_flg = 0 and i.idcardNo='"+idcardNo+"'";
					List<PatientIdcard> cardList = super.findByObjectProperty(ehql, null);
					PatientBlackList patientBlack=new PatientBlackList();
					patientBlack.setPatient(cardList.get(0).getPatient());
					patientBlack.setMedicalrecordId(re.getMedicalrecordId());
					patientBlack.setBlacklistType("SY");//爽约
					patientBlack.setBlacklistStarttime(new Date());
					patientBlack.setBlacklistEndtime(DateUtils.addDay(new Date(), Integer.parseInt(day)));
					patientBlack.setPatientName(re.getPreregisterName());
					patientBlack.setPatientId(cardList.get(0).getPatient().getId());
					patientBlack.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
					patientBlack.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
					patientBlack.setCreateTime(DateUtils.getCurrentTime());
					patientBlackInnerDAO.save(patientBlack);
					return null;
				}
				return null;
			}
		  }
		}else{
			String pbhql="from PatientBlackList p where p.medicalrecordId in " +
				"(select p.medicalrecordId from Patient p where p.id in" +
				"(select patient from PatientIdcard where idcardNo=?)) and p.del_flg = 0 ";
			List<PatientBlackList> blackList = super.find(pbhql,idcardNo );
			DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd"); 
			if(blackList!=null&&blackList.size()>0){
				for (PatientBlackList pList : blackList) {
					String dateBegin=fmt.format(new Date()); 
					String dateEnd=fmt.format(blackList.get(0).getBlacklistEndtime()); 
					if(java.sql.Date.valueOf(dateBegin).after(java.sql.Date.valueOf(dateEnd))){
						String hql="FROM PatientIdcard i WHERE i.del_flg = 0 ";
						hql = hql+" AND i.idcardNo = '"+idcardNo+"' ";
						List<PatientIdcard> cardList = super.findByObjectProperty(hql, null);
						return cardList.get(0);
					}else{
						return null;
					}
				}
			}else{
				String hql="FROM PatientIdcard i WHERE i.del_flg = 0 ";
				hql = hql+" AND i.idcardNo = ? ";
				List<PatientIdcard> cardList = super.find(hql, idcardNo);
				if (cardList.size()!=0) {
					return cardList.get(0);
				}
			}
		}
		return null;
	}
	@Override
	public String checkIdcardVSMedicalNO(String idcardNo,String medicalrecordId,String idcardId) {
		Map<String, Object> pMap = new HashMap<>();
		pMap.put("idcardNo", idcardNo);
		if (StringUtils.isNotBlank(idcardId)) {
			pMap.put("idcardId", idcardId);
		}
		StringBuffer sql = new StringBuffer();
		sql.append("select IDCARD_NO from t_Patient_Idcard where IDCARD_NO = :idcardNo and del_flg=0 and stop_flg=0 ");
		sql.append(StringUtils.isNotBlank(idcardId) ? " and IDCARD_ID != :idcardId " : "");
		List<String> value = namedParameterJdbcTemplate.queryForList(sql.toString(), pMap, String.class);
		if(value!=null&&value.size()>0){
			return "idcardNO";
		}
		return "ok";
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
	public String checkIdcardName(String name, String sex, String birthday, String contact, String certificate, String number, String patientCitys,String pid) {
		Map<String, Object> pMap = new HashMap<>();
		if (StringUtils.isNotBlank(certificate)) {
			pMap.put("certificate", certificate);
		}
		if (StringUtils.isNotBlank(number)) {
			pMap.put("number", number);
		}
		if (StringUtils.isNotBlank(pid)) {
			pMap.put("pid", pid);
		}
		StringBuffer sql = new StringBuffer();
		sql.append("select p.PATINENT_ID from "+HisParameters.HISPARSCHEMAHISUSER+"T_PATIENT p  WHERE p.DEL_FLG=0 ");
		if(StringUtils.isNotBlank(certificate)){//证件类型
			sql.append(" AND p.PATIENT_CERTIFICATESTYPE = :certificate");
		}
		if(StringUtils.isNotBlank(number)){//证件号
			sql.append(" AND p.PATIENT_CERTIFICATESNO = :number");
		}
		if(StringUtils.isNotEmpty(pid)){
			sql.append(" AND p.PATINENT_ID != :pid ");
		}
		List<String> inpatentNos = namedParameterJdbcTemplate.queryForList(sql.toString(), pMap, String.class);
		if(inpatentNos!=null && inpatentNos.size()>0){
			return "nameNO";
		}
		return "ok";
	}

	/**  
	 * @Description： 读卡查询信息
	 * @Author：wujiao
	 * @CreateDate：2016-3-23 上午9:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@Override
	public PatientIdcard queryIdcadAllInfo(String idcardNo) {
		String hql="from PatientIdcard p where p.idcardNo='"+idcardNo+"'and p.stop_flg=0 AND p.del_flg=0 ";
		List<PatientIdcard> idcardList = super.findByObjectProperty(hql, null);
		if(idcardList!=null&&idcardList.size()>0){
			return idcardList.get(0);
		}
		return null;
	}
	
	/**
	 * @Description:执行挂失操作
	 * @Author：  lt
	 * @CreateDate： 2015-11-18
	 * @return void  
	 * @param zpty20160324从inpitaent包中移植过来
	 * @version 1.0
	**/
	@Override
	public InpatientAccount queryByIdcardId(String idcardId){
		String hql = "from InpatientAccount a where a.idcard.id = '" + idcardId + "' and a.del_flg = 0 ";
		List<InpatientAccount> list= this.getSession().createQuery(hql).list();
		return list.size()==0 ? null :list.get(0);
	}
	
	/**
	 * 根据员工工号查询员工
	 * zpty 20160324
	 * @param jobNo
	 * @return
	 */
	@Override
	public List<SysEmployee> findEmpByjobNo(String jobNo) {
		String hql = "from SysEmployee r where r.del_flg=0 and r.stop_flg=0 and r.jobNo='"+jobNo+"'";
		List<SysEmployee> list = super.find(hql, null);
		return list.size()==0?new ArrayList<SysEmployee>():list;
	}
	
	/**
	 * @Description:通过就诊卡ID查找就诊卡
	 * @Author：  zpty
	 * @CreateDate： 2016-03-27
	 * @return void  
	 * @version 1.0
	**/
	@Override
	public PatientIdcard queryOldIdcardById(String idcardId){
		String hql = "from PatientIdcard a where a.id = '" + idcardId + "' and a.del_flg = 0 ";
		List<PatientIdcard> list= this.getSession().createQuery(hql).list();
		return list.size()==0 ? null:list.get(0);
	}
	
	/**
	 * @Description:根据病历号获取住院账户信息
	 * @Author：  lt
	 * @CreateDate： 2015-7-1
	 * @param @param string
	 * @param @return   
	 * @return InpatientAccount  
	 * @modify zpty20160327移植过来
	 * @version 1.0
	**/
	@Override
	public InpatientAccount queryByMedical(String string) {
		String hql = "from InpatientAccount a where a.medicalrecordId = '" + string + "' and a.del_flg = 0 ";
		List<InpatientAccount> list = this.getSession().createQuery(hql).list();
		return list.size()==0 ? null :list.get(0);
	}
	
	/**
	 * @Description:通过父id 物理删除子表记录
	 * @Author：  lt
	 * @CreateDate： 2015-11-16
	 * @return void  
	 * @modify zpty20160327移植过来
	 * @version 1.0
	**/
	@Override
	public void delByParentIdDetail(String id) {
		String hql = "delete InpatientAccountdetail ina where ina.inpatientAccount.id = ?";
		this.excUpdateHql(hql, id);
	}
	
	/**
	 * @Description:通过父id 物理删除子表记录
	 * @Author：  lt
	 * @CreateDate： 2015-11-16
	 * @return void  
	 * @modify zpty20160327移植过来
	 * @version 1.0
	**/
	@Override
	public void delByParentIdRepaydetail(String id) {
		String hql = "delete InpatientAccountrepaydetail ina where ina.inpatientAccount.id = ?";
		this.excUpdateHql(hql, id);
	}
	
	/**
	 * @Description:物理删除账号信息
	 * @Author：  lt
	 * @CreateDate： 2015-7-1
	 * @param @param string
	 * @param @return 
	 * @modify zpty20160327移植过来  
	 * @version 1.0
	**/
	@Override
	public void delInpatientAccount(String id) {
		String hql = "delete InpatientAccount ina where ina.id = ?";
		this.excUpdateHql(hql, id);
	}
	
	/**
	 * @Description:根据IDcard ID 取患者账户
	 * @Author：  lt
	 * @CreateDate： 2015-6-18
	 * @Modifier：lt
	 * @ModifyDate：2015-6-18
	 * @ModifyRmk：  
	 * @Modifier：zpty
	 * @ModifyDate：2016-4-07
	 * @ModifyRmk：  account包中引用过来
	 * @version 1.0
	 * @param idcardId 就诊卡ID
	 * @return PatientAccount 实体
	**/
	@Override
	public OutpatientAccount queryByIdcardIdOut(String idcardId) {
		String hql = "from OutpatientAccount where idcardId = '"+idcardId +"' and del_flg=0 and stop_flg = 0";
		List<OutpatientAccount> list = this.getSession().createQuery(hql).list();
		return  list.size()==0?null:list.get(0);
	}
}

