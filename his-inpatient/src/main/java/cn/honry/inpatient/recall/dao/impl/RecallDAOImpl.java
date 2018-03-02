package cn.honry.inpatient.recall.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessBedward;
import cn.honry.base.bean.model.BusinessHospitalbed;
import cn.honry.base.bean.model.InpatientBabyInfo;
import cn.honry.base.bean.model.InpatientBabyInfoNow;
import cn.honry.base.bean.model.InpatientBedinfo;
import cn.honry.base.bean.model.InpatientBedinfoNow;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientShiftData;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.VidBedInfo;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.baseinfo.hospitalbed.dao.HospitalbedInInterDAO;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.inpatient.recall.dao.RecallDAO;
import cn.honry.inpatient.recall.vo.Vhinfo;
import cn.honry.utils.ShiroSessionUtils;

@Repository("recallDAO")
@SuppressWarnings({ "all" })
public class RecallDAOImpl extends HibernateEntityDao<InpatientInfoNow> implements
		RecallDAO {
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {

		super.setSessionFactory(sessionFactory);
	}

	private HospitalbedInInterDAO hospitalbedDAO;

	/**
	 * 
	 * @Description： 查询（住院号&&医保号）
	 * @Author：liudelin
	 * @CreateDate：2015-8-31上午9:12
	 * @ModifyRmk：
	 * @version 1.0
	 */
	@Override
	public VidBedInfo queryRecallList(String mcardNo, String inpatientNo,
			String bedId, String patientName) throws Exception {
		String hql = "from VidBedInfo where del_flg=0 and stop_flg=0 and inState='B' ";
		if (StringUtils.isNotBlank(mcardNo)) {
			hql = hql + " and mcardNo='" + mcardNo + "'";
		}
		if (StringUtils.isNotBlank(inpatientNo)) {
			hql = hql + " and inpatientNo='" + inpatientNo + "'";
		}
		if (StringUtils.isNotBlank(bedId)) {
			hql = hql + " and bedId='" + bedId + "'";
		}
		if (StringUtils.isNotBlank(patientName)) {
			hql = hql + " and patientName='" + patientName + "'";
		}
		List<VidBedInfo> vidBedInfoList = super.find(hql, null);
		if (vidBedInfoList == null || vidBedInfoList.size() <= 0) {
			return new VidBedInfo();
		}
		return vidBedInfoList.get(0);
	}

	/**
	 * 
	 * @Description： 业务判断查询（住院号）
	 * @Author：liudelin
	 * @CreateDate：2015-8-31上午9:12
	 * @ModifyRmk：
	 * @version 1.0
	 */
	@Override
	public VidBedInfo RecallByInpatientNo(String inpatientNos) throws Exception{
		String hql = "from VidBedInfo where del_flg=0 and stop_flg=0  and inpatientNo='"
				+ inpatientNos + "'";
		List<VidBedInfo> vidBedInfoList = super.find(hql, null);
		if (vidBedInfoList == null || vidBedInfoList.size() <= 0) {
			return new VidBedInfo();
		}
		return vidBedInfoList.get(0);
	}

	/**
	 * 
	 * @Description： 业务判断查询（床号）
	 * @Author：liudelin
	 * @CreateDate：2015-8-31上午9:12
	 * @ModifyRmk：
	 * @version 1.0
	 */
	@Override
	public VidBedInfo RecallBedByName(String bedIds) throws Exception{
		String hql = "from VidBedInfo where del_flg=0 and stop_flg=0  and bedIds='"
				+ bedIds + "'";
		List<VidBedInfo> vidBedInfoList = super.find(hql, null);
		if (vidBedInfoList == null || vidBedInfoList.size() <= 0) {
			return new VidBedInfo();
		}
		return vidBedInfoList.get(0);
	}

	/**
	 * 
	 * @Description： 更新病床表信息
	 * @Author：liudelin
	 * @CreateDate：2015-8-31上午9:12
	 * @ModifyRmk：
	 * @version 1.0
	 */
	@Override
	public void updateBedid(String bedIds, String inpatientNos,
			String dutyNurseCode, String chiefDocCode, String chargeDocCode,
			String houseDocCode) throws Exception{
		getSession()
				.createQuery(
						"UPDATE BusinessHospitalbed SET patientId='"
								+ inpatientNos
								+ "',bedState='402880984dd22fe5014dd230d2fb0001'  WHERE  id = '"
								+ bedIds + "'").executeUpdate();
		getSession().createQuery(
				"UPDATE InpatientInfoNow SET bedId='" + bedIds
						+ "',houseDocCode='" + houseDocCode
						+ "',chargeDocCode='" + chargeDocCode
						+ "',dutyNurseCode='" + dutyNurseCode
						+ "',chiefDocCode='" + chiefDocCode
						+ "',inState='I'  WHERE  inpatientNo = '"
						+ inpatientNos + "'").executeUpdate();
		InpatientShiftData inpatientShiftData = new InpatientShiftData();
		inpatientShiftData.setId(null);
		inpatientShiftData.setClinicNo(inpatientNos);
		inpatientShiftData.setShiftCause("召回");
		super.save(inpatientShiftData);
		OperationUtils.getInstance().conserve(null, "出院召回", "INSERT INTO",
				"T_INPATIENT_SHIFTDATA", OperationUtils.LOGACTIONUPDATE);
	}

	/**
	 * 根据病历号查询患者信息
	 */
	public List<InpatientInfoNow> queryInfoList(String mid) throws Exception{
		String hql = "from InpatientInfoNow v where (v.medicalrecordId = :mid  )";
		Query query=this.getSession().createQuery(hql);
		query.setParameter("mid", mid);
		List<InpatientInfoNow> list =query.list();
		if (list != null && list.size() > 0) {
			return list;
		}
		return new ArrayList<InpatientInfoNow>();
	}

	/**
	 * 根据患者姓名来查询患者信息
	 */
	public Patient getInfoBYName(String name) throws Exception{
		String hql = " from Patient p where p.del_flg =0 and p.stop_flg=0 and p.medicalrecordId='"
				+ name + "'";
		Query query = getSession().createQuery(hql);
		List<Patient> list = query.list();
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return new Patient();
	}

	/**
	 * 根据母亲姓名查询母亲住院状态
	 */
	public VidBedInfo queryInfoListBYmotherName(String motherName) throws Exception{
		String hql = "from VidBedInfo v where v.del_flg =0 and v.stop_flg =0 and v.medicalrecordId='"
				+ motherName + "'";
		List<VidBedInfo> list = super.find(hql, null);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return new VidBedInfo();
	}

	/**
	 * 病床下拉框！
	 */
	public List<BusinessHospitalbed> getcombobox(String deptId) throws Exception{
		String hql="from BusinessHospitalbed d where d.stop_flg=0 and d.del_flg=0 and  (d.bedState='7' or d.bedState='1')  and d.nurseCellCode = ? ";
		List<BusinessHospitalbed> list = super.find(hql, deptId);
		if (list != null && list.size() > 0) {
			return list;
		}
		return new ArrayList<BusinessHospitalbed>();
	}

	/**
	 * 根据床号查询病床状态信息
	 */
	public BusinessHospitalbed getBedByName(String bedId) throws Exception{
		String hql = " from BusinessHospitalbed d where d.id=?";
		List<BusinessHospitalbed> list = super.find(hql, bedId);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return new BusinessHospitalbed();
	}

	/**
	 * 根据病床号查询病床信息表
	 */
	public InpatientBedinfoNow getInfoByBedId(String bedID) throws Exception{
		String hql = " from InpatientBedinfoNow t where t.stop_flg=0 and t.del_flg=0 and t.bedId='"
				+ bedID + "'";
		List<InpatientBedinfoNow> list = super.find(hql, null);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return new InpatientBedinfoNow();
	}

	/**
	 * 病房下拉框
	 */
	public List<BusinessBedward> getBEdWardCombobox() throws Exception{
		String deptIds = ShiroSessionUtils
				.getCurrentUserDepartmentFromShiroSession().getId();
		return null;
	}

	/**
	 * 根据科室和员工类型查询护士和医生
	 */
	public List<SysEmployee> getEmpCombobox(String empType) throws Exception{
		String hql = " from SysEmployee e where e.stop_flg=0 and e.del_flg=0 and e.deptId in "
				+ "(select d.id from SysDepartment d where d.stop_flg=0 and d.del_flg=0)";
		if (StringUtils.isNotBlank(empType)) {
			hql += " and e.type='" + empType + "'";
		}
		List<SysEmployee> list = super.find(hql, null);
		if (list != null && list.size() > 0) {
			return list;
		}
		return new ArrayList<SysEmployee>();
	}

	/**
	 * 根据住院流水号查询婴儿表f.stop_flg=0 and del_flg=0
	 */
	public InpatientBabyInfoNow babyInfoByInpatientNo(String inpatientNo) throws Exception {
		String hql = " from InpatientBabyInfoNow f where "
				+ " f.inpatientNo=?";
		List<InpatientBabyInfoNow> list = super.find(hql,inpatientNo);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return new InpatientBabyInfoNow();
	}

	/**
	 * 根据婴儿妈妈住院流水号查询住院主表
	 */
	public InpatientInfoNow getInfoByMotherid(String inpatientNO) throws Exception{
		String hql=" from InpatientInfoNow o where o.inpatientNo= ?";
		List<InpatientInfoNow> list = super.find(hql, inpatientNO);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return new InpatientInfoNow();
	}

	/**
	 * 根据病人病历号查询病人信息
	 */
	public Patient getIdByMedId(String mid) throws Exception{
		String hql=" from Patient t where t.stop_flg=0 and t.del_flg=0 and t.medicalrecordId='"+mid+"'";
		List<Patient> list = super.find(hql, null);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return new Patient();
	}

	@Override
	public List<Vhinfo> getBedidByBusinessHospitalbed(String bedId) throws Exception{
		StringBuilder sql = new StringBuilder();
		sql.append("select s.bed_name as bedName,s.house_doc_code as houseDocCode,"
				+ "s.charge_doc_code as chargeDocCode,s.chief_doc_code as chiefDocCode,"
				+ "s.duty_nurse_code as dutyNurseCode  from "
				+ " T_BUSINESS_HOSPITALBED s  "
				+ "where s.BED_ID =:bedId");
		SQLQuery queryObject = this.getSession().createSQLQuery(sql.toString()).addScalar("bedName").addScalar("houseDocCode")
		.addScalar("chargeDocCode").addScalar("chiefDocCode").addScalar("dutyNurseCode");
		queryObject.setParameter("bedId", bedId);
		List<Vhinfo> list = queryObject.setResultTransformer(Transformers.aliasToBean(Vhinfo.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<Vhinfo>();
	}

	@Override
	public List<InpatientInfo> queryOldInfoList(String mid) throws Exception{
		String hql = "from InpatientInfo v where (v.medicalrecordId like :mid  or v.inpatientNo like :mids )";
		
		Query query=this.getSession().createQuery(hql);
		query.setParameter("mid", "%"+mid);
		query.setParameter("mids", "%"+mid);
		List<InpatientInfo> list = query.list();
		if (list != null && list.size() > 0) {
			return list;
		}
		return new ArrayList<InpatientInfo>();
	}

	@Override
	public InpatientInfo queryOldbyId(String id) throws Exception{
		String hql = "from InpatientInfo v where v.id='"+id+"'";
		InpatientInfo list = (InpatientInfo) super.find(hql, null).get(0);
		if(list!=null){
			return list;
		}
		return new InpatientInfo();
	}

	@Override
	public void updateOld(String oid) throws Exception{
		String hql = "update InpatientInfo t set t.stop_flg=1, t.del_flg=1 where t.id=?";
		this.excUpdateHql(hql, oid);
	}

}
