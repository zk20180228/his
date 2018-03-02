package cn.honry.inpatient.info.service.impl;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessBedward;
import cn.honry.base.bean.model.BusinessHospitalbed;
import cn.honry.base.bean.model.DrugApplyoutNow;
import cn.honry.base.bean.model.InpatientBedinfoNow;
import cn.honry.base.bean.model.InpatientCancelitemNow;
import cn.honry.base.bean.model.InpatientInPrepayNow;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientOrderNow;
import cn.honry.base.bean.model.InpatientPrepayin;
import cn.honry.base.bean.model.InpatientProof;
import cn.honry.base.bean.model.InpatientShiftData;
import cn.honry.base.bean.model.InpatientSurety;
import cn.honry.base.bean.model.InvoiceUsageRecord;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.VidBedInfo;
import cn.honry.inner.baseinfo.department.dao.DeptInInterDAO;
import cn.honry.inner.baseinfo.hospitalbed.dao.HospitalbedInInterDAO;
import cn.honry.inner.outpatient.inpatNumber.service.InpatNumInnerService;
import cn.honry.inner.outpatient.medicineList.dao.MedicineListInInterDAO;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.inpatient.info.dao.InpatientInfoDAO;
import cn.honry.inpatient.info.dao.InpatientInfoNowDAO;
import cn.honry.inpatient.info.service.InpatientInfoService;
import cn.honry.inpatient.info.vo.InfoVo;
import cn.honry.inpatient.inprePay.dao.InprePayDAO;
import cn.honry.inpatient.inprePay.service.InprePayService;
import cn.honry.inpatient.surety.dao.SuretyDAO;
import cn.honry.inpatient.surety.service.SuretyService;
import cn.honry.inpatient.warnLine.vo.WarnLineVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HibernateCascade;
import cn.honry.utils.HisParameters;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.TreeJson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Service("inpatientInfoService")
@Transactional
@SuppressWarnings({"all"})
public class InpatientInfoServiceImpl implements InpatientInfoService {
	@Autowired
	private InpatientInfoDAO inpatientDAO;
	@Autowired
	private InpatientInfoNowDAO inpatientInfoNowDAO;
	@Autowired
	private HospitalbedInInterDAO hospitalbedDAO;
	@Autowired
	@Qualifier(value = "medicineListInInterDAO")
	private MedicineListInInterDAO medicineListInInterDAO;
	@Autowired
	@Qualifier(value = "inprePayDAO")
	private InprePayDAO inprePayDAO;
	@Autowired
	@Qualifier(value = "suretyDAO")
	private SuretyDAO suretyDAO;
	@Autowired
	private DeptInInterDAO departmentDAO;
	@Autowired
	private InpatientInfoDAO inpatientInfoDAO;
	@Autowired
	private HospitalbedInInterDAO hospitalbed;
	/**
	 * 注入住院预交金Service
	 */
	@Autowired
	@Qualifier(value = "inprePayService")
	private InprePayService inprePayService;

	public void setInprePayService(InprePayService inprePayService) {
		this.inprePayService = inprePayService;
	}

	/**
	 * 注入住院担保金Service
	 */
	@Autowired
	@Qualifier(value = "suretyService")
	private SuretyService suretyService;

	public void setSuretyService(SuretyService suretyService) {
		this.suretyService = suretyService;
	}

	/**
	 * 注入住院次数Service
	 */
	@Autowired
	@Qualifier(value = "inpatNumInnerService")
	private InpatNumInnerService inpatNumInnerService;

	public void setInpatNumInnerService(InpatNumInnerService inpatNumInnerService) {
		this.inpatNumInnerService = inpatNumInnerService;
	}

	// 删除
	public void removeUnused(String id) {

		inpatientDAO.del(id, ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		OperationUtils.getInstance().conserve(id, "住院登记信息管理", "UPDATE", "T_INPATIENT_INFO",
				OperationUtils.LOGACTIONDELETE);
	}

	// 获取id
	public InpatientInfoNow get(String id)  {
		InpatientInfoNow info = inpatientDAO.get(id);
		return info;
	}

	// 添加、修改方法
	public void saveOrUpdate(InpatientInfoNow entity) {
		if (StringUtils.isBlank(entity.getId())) {
			entity.setId(null);
			entity.setCreateUser("");
			entity.setCreateDept("");
			entity.setCreateTime(new Date());
			entity.setInState("R");
		} else {
			entity.setUpdateUser("");
			entity.setUpdateTime(new Date());
		}
		inpatientDAO.save(entity);
		if (entity.getId() == null) {
			OperationUtils.getInstance().conserve(null, "住院登记信息管理", "INSERT INTO", "T_INPATIENT_INFO",
					OperationUtils.LOGACTIONINSERT);
		} else {
			OperationUtils.getInstance().conserve(entity.getId(), "住院登记信息管理", "UPDATE", "T_INPATIENT_INFO",
					OperationUtils.LOGACTIONUPDATE);
		}
	}

	// 分页显示住院登记信息
	public List<InpatientInfoNow> getPage(String page, String rows, InpatientInfoNow entity) throws Exception {
		List<InpatientInfoNow> list = inpatientDAO.getPage(page, rows, entity);
		return list;
	}

	// 分页显示 得到总数
	public int getTotal(InpatientInfoNow entity) throws Exception {
		return inpatientDAO.getTotal(entity);
	}

	@Override
	public InpatientInfoNow queryByMedical(String medicalNo) throws Exception {
		return inpatientDAO.queryByMedical(medicalNo);
	}

	/**
	 * 
	 * @Description： 查询住院患者信息
	 * 
	 * @Author：wujiao
	 * @CreateDate：2015-7-3 下午11:12:01
	 * @Modifier：wujiao
	 * @ModifyDate：2015-7-3 下午11:12:01 @ModifyRmk：
	 * @version 1.0
	 *
	 */
	@Override
	public List<InpatientInfoNow> getpatientList() throws Exception {
		String hql = "from InpatientInfo i where i.del_flg=0 ";
		List<InpatientInfoNow> patientList = inpatientDAO.findByObjectProperty(hql, null);
		if (patientList != null && patientList.size() > 0) {
			return patientList;
		}
		return null;
	}

	/**
	 * 
	 * @Description： 查询患者
	 * 
	 * @Author：aizhonghua @CreateDate：2015-8-18 下午05:37:12
	 * @Modifier：aizhonghua @ModifyDate：2015-8-18 下午05:37:12 @ModifyRmk：
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	@Override
	public String queryPatientTree() throws Exception {
		List<TreeJson> treeJsonList = new ArrayList<TreeJson>();
		List<TreeJson> treeJson = new ArrayList<TreeJson>();
		List<InpatientInfoNow> infoList = inpatientDAO.queryPatient();
		// 根节点
		TreeJson gTreeJson = new TreeJson();
		gTreeJson.setId("1");
		gTreeJson.setText("患者信息");
		Map<String, String> gAttMap = new HashMap<String, String>();
		gTreeJson.setAttributes(gAttMap);
		treeJsonList.add(gTreeJson);
		TreeJson fTreeJson = null;
		Map<String, String> fAttMap = null;
		if (infoList != null && infoList.size() > 0) {
			for (InpatientInfoNow infonfo : infoList) {
				// 二级节点(患者)
				fTreeJson = new TreeJson();
				fTreeJson.setId(infonfo.getBedId());
				fTreeJson.setText(infonfo.getPatientName());
				fTreeJson.setNodeType(infonfo.getInpatientNo());
				fTreeJson.setState("open");
				fAttMap = new HashMap<String, String>();
				fAttMap.put("pid", "1");
				fTreeJson.setAttributes(fAttMap);
				treeJsonList.add(fTreeJson);
			}
		}
		treeJson = TreeJson.formatTree(treeJsonList);
		Gson gson = new GsonBuilder().registerTypeAdapterFactory(HibernateCascade.FACTORY).create();
		return gson.toJson(treeJson);
	}

	/**
	 * 
	 * @Description： 查询出院登记
	 * 
	 * @Author：aizhonghua @CreateDate：2015-8-26 下午02:06:02
	 * @Modifier：zhangjin
	 * @ModifyDate：2015-12-3 下午03:08 @ModifyRmk：
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	@Override
	public List<InpatientInfoNow> queryInpinfo(String type, String no) throws Exception {
		return inpatientDAO.queryInpinfo(type, no);
	}

	/**
	 * 
	 * @Description： 查询出院登记
	 * 
	 * @Author：aizhonghua @CreateDate：2015-8-26 下午02:06:02
	 * @Modifier：zhangjin
	 * @ModifyDate：2015-12-3 下午03:08 @ModifyRmk：
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	@Override
	public List<InpatientInfoNow> queryInpinfoList(String type, String no) throws Exception {
		return inpatientDAO.queryInpinfoList(type, no);
	}

	/**
	 * 
	 * @Description： 保存出院登记信息
	 * @throws Exception 
	 * 
	 * @Author：zj
	 * @CreateDate：2015-12-7 @ModifyRmk： @version 1.0
	 *
	 */
	@Override
	public void inpatientIdGet(InpatientInfoNow inpatientInfo) throws Exception {
		String userId = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String deptId = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
		InpatientInfoNow p = inpatientDAO.inpatientIdGet(inpatientInfo.getInpatientNo());
		p.setOutDate(inpatientInfo.getOutDate());
		p.setOutState(inpatientInfo.getOutState());
		p.setInState("B");
		p.setUpdateUser(userId);
		p.setUpdateTime(new Date());
		if (StringUtils.isNotBlank(inpatientInfo.getId())) {
			OperationUtils.getInstance().conserve(inpatientInfo.getId(), "出院登记", "UPDATE", "T_INPATIENT_INFO_NOW",
					OperationUtils.LOGACTIONUPDATE);
			inpatientDAO.updeteOut(p);
			BusinessHospitalbed bed = hospitalbed.get(p.getBedNo());
			if (bed != null) {
				bed.setBedState("7");
				bed.setPatientId(null);
				bed.setPatientName(null);
				bed.setUpdateTime(new Date());
				bed.setUpdateUser(userId);
				hospitalbed.saveOrUpdate(bed);
			}

		}
		// 住院次数
		inpatNumInnerService.saveInpatNum(p.getId());
	}

	/**
	 * @Description：保存 设置警戒线
	 * @Author：lyy
	 * @CreateDate：2015-12-3 下午03:06:02
	 * @Modifier：lyy
	 * @ModifyDate：2015-12-3 下午03:06:02
	 * @param warnLineVo
	 *            护士站患者警戒线 @ModifyRmk：
	 * @version 1.0
	 */
	@Override
	public void saveWarnLineVo(WarnLineVo warnLineVo) throws Exception {
		InpatientInfoNow inpatientInfo = inpatientInfoNowDAO.get(warnLineVo.getId());
		inpatientInfo.setAlterType(warnLineVo.getAlterType());
		if ("M".equals(warnLineVo.getAlterType())) {
			inpatientInfo.setMoneyAlert(warnLineVo.getMoneyAlert());
			inpatientInfo.setAlterBegin(null);
			inpatientInfo.setAlterEnd(null);
		} else {
			inpatientInfo.setMoneyAlert(null);
			inpatientInfo.setAlterBegin(warnLineVo.getAlterBegin());
			inpatientInfo.setAlterEnd(warnLineVo.getAlterEnd());
		}
		inpatientDAO.save(inpatientInfo);
	}

	/**
	 * @Description： 根据患者id查询RegisterInfo
	 * 
	 * @Author：zhenglin @CreateDate：2015-12-10 @ModifyRmk：
	 * @version 1.0
	 * @throws Exception 
	 * @throws IOException
	 * @throws ParseException
	 */
	public RegisterInfo getInfoByePatientNO(String patientNo) throws Exception {
		RegisterInfo patientinfo = inpatientDAO.getInfoByPatientNO(patientNo);
		return patientinfo;
	}

	public InfoVo getProof(String id, String idcardNo) throws Exception {
		return inpatientDAO.getProof(id, idcardNo);
	}

	/**
	 * 
	 * @Description： 查询床号
	 * @throws Exception 
	 * 
	 * @Author：zj
	 * @CreateDate：2015-12-7 @ModifyRmk： @version 1.0
	 *
	 */
	@Override
	public InpatientInfoNow ajaxBedId(String id) throws Exception {
		return inpatientDAO.ajaxBedId(id);
	}

	/**
	 * 
	 * @Description： 查询患者退费申请
	 * @throws Exception 
	 * 
	 * @Author：zj
	 * @CreateDate：2015-12-7 @ModifyRmk： @version 1.0
	 *
	 */
	@Override
	public List<InpatientCancelitemNow> ajaxCanceliem(String no) throws Exception {
		return inpatientDAO.ajaxCanceliem(no);
	}

	/**
	 * 
	 * @Description： 根据患者的住院状态查询.
	 * 
	 * @Author：zhenglin @CreateDate：2015-12-15 下午05:37:12
	 * @Modifier：aizhonghua @ModifyDate：2015-12-15 下午05:37:12 @ModifyRmk：
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	public List<InpatientInfoNow> queryPatientByInState(String state) throws Exception {

		return inpatientDAO.queryPatientByInState(state);
	}

	/**
	 * 
	 * @Description： （根据病历号查询）住院诊断.
	 * 
	 * @Author：zhenglin @CreateDate：2015-12-15 下午05:37:12
	 * @Modifier：zhangjin @ModifyDate：2015-12-15 下午05:37:12 @ModifyRmk：
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	@Override
	public InpatientInfoNow queryInpatientinfo(String no) throws Exception {
		return inpatientDAO.query(no);
	}

	/**
	 * 
	 * @Description： 转入病人查询
	 * 
	 * @Author：zhenglin @CreateDate：2015-12-19 下午05:37:12
	 * @Modifier：zhenglin @ModifyDate：2015-12-19 下午05:37:12 @ModifyRmk：
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	public List<InpatientInfoNow> getInfoByApply() throws Exception {
		return inpatientDAO.getInfoByApply();
	}

	/**
	 * 
	 * @Description：查询患者
	 * @Author：zhenglin @CreateDate：2015-12-15 下午05:37:12
	 * @Modifier：aizhonghua @ModifyDate：2015-12-15 下午05:37:12 @ModifyRmk：
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	public List<VidBedInfo> queryPatientList() throws Exception {

		return inpatientDAO.queryPatientList();
	}

	/**
	 * 
	 * @Description： 根据患者的住院状态查询.
	 * 
	 * @Author：zhenglin @CreateDate：2015-12-15 下午05:37:12
	 * @Modifier：zhenglin @ModifyDate：2015-12-15 下午05:37:12 @ModifyRmk：
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	public List<VidBedInfo> queryPatientByInStateList(String state) throws Exception {

		return inpatientDAO.queryPatientByInStateList(state);
	}

	/**
	 * 
	 * @Description： 患者警戒线根据余额来判断
	 * 
	 * @Author：zhenglin @CreateDate：2015-12-21 下午05:37:12
	 * @Modifier：zhenglin @ModifyDate：2015-12-21 下午05:37:12 @ModifyRmk：
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	public List<InpatientInfoNow> getInfoByMoney(InpatientInfoNow info, String rows, String page) throws Exception {
		List<InpatientInfoNow> list = inpatientDAO.getInfoByMoney(info, rows, page);
		return list;
	}

	/**
	 * 
	 * @Description： 患者警戒线页总数.
	 * 
	 * @Author：zhenglin @CreateDate：2015-12-21 下午05:37:12
	 * @Modifier：zhenglin @ModifyDate：2015-12-21 下午05:37:12 @ModifyRmk：
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	public int getTotals(InpatientInfoNow entity) throws Exception {

		return inpatientDAO.getTotals(entity);
	}

	/**
	 * 
	 * @Description： (根据病历号获取信息) 住院收费
	 * 
	 * @Author：zhangjin @CreateDate：2016-1-
	 *                  20 @Modifier： @ModifyDate： @ModifyRmk：
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	@Override
	public List<TreeJson> treeNurseCharge(String deptId, String id, InpatientInfoNow inpatientInfo, String a,
			String startTime, String endTime) throws Exception {
		List<InfoVo> listDept = new ArrayList<InfoVo>();
		List<TreeJson> treeJsonList = new ArrayList<TreeJson>();
		if (StringUtils.isBlank(id)) {// 根节点
			//
			TreeJson pTreeJson = new TreeJson();
			pTreeJson.setId("1");
			if ("1".equals(a)) {
				pTreeJson.setText("在院患者");
			} else if ("2".equals(a)) {
				pTreeJson.setText("出院未结算患者");
			} else if ("3".equals(a) || "4".equals(a)) {
				pTreeJson.setText("全部患者");
			} else {
				pTreeJson.setText("本区患者");
			}
			treeJsonList.add(pTreeJson);
		}
		listDept = inpatientDAO.treeNurseCharge(deptId, inpatientInfo, a, startTime, endTime);
		if (listDept != null && listDept.size() > 0) {
			for (InfoVo sysDept : listDept) {
				TreeJson treeJson = new TreeJson();
				treeJson.setId(sysDept.getId());
				treeJson.setText(sysDept.getPatientName());
				Map<String, String> attributes = new HashMap<String, String>();
				attributes.put("pid", "1");
				attributes.put("inpatientNo", sysDept.getInpatientNo());
				attributes.put("medicalrecordId", sysDept.getMedicalrecordId());
				treeJson.setAttributes(attributes);
				treeJsonList.add(treeJson);
			}
		}
		return TreeJson.formatTree(treeJsonList);
	}

	/**
	 * @Description： 病区（下拉框）
	 * 
	 * @Author：tcj @CreateDate：2015-12-30 上午9:40:16 @ModifyRmk：
	 * @version 1.0
	 * @throws Exception 
	 */
	@Override
	public List<SysDepartment> querydeptCombobox(String id) throws Exception {
		return inpatientInfoDAO.querydeptCombobox(id);
	}

	/**
	 * @Description： 查询病房树
	 * 
	 * @Author：tcj @CreateDate：2015-12-30 上午15:06 @ModifyRmk：
	 * @version 1.0
	 * @throws Exception 
	 */
	@Override
	public List<TreeJson> queryPatientRoom(String id) throws Exception {
		List<BusinessBedward> businessBedwardList = new ArrayList<BusinessBedward>();
		List<TreeJson> treeJsonList = new ArrayList<TreeJson>();
		// 加病房树的根节点
		TreeJson pTreeJson = new TreeJson();
		pTreeJson.setId("1");
		pTreeJson.setText("病房信息");
		treeJsonList.add(pTreeJson);
		// }
		businessBedwardList = inpatientInfoDAO.queryPatientRoom(id);
		if (businessBedwardList != null && businessBedwardList.size() > 0) {
			for (BusinessBedward im : businessBedwardList) {
				TreeJson treeJson = new TreeJson();
				treeJson.setId(im.getId());
				treeJson.setText(im.getBedwardName());
				Map<String, String> attributes = new HashMap<String, String>();
				attributes.put("pid", "1");
				treeJson.setAttributes(attributes);
				treeJsonList.add(treeJson);
			}
		}
		return TreeJson.formatTree(treeJsonList);

	}

	/**
	 * @Description： 点击病房树查询病床
	 * 
	 * @Author：tcj @CreateDate：2015-12-30 上午18:06 @ModifyRmk：
	 * @version 1.0
	 * @throws Exception 
	 */
	@Override
	public List<BusinessHospitalbed> queryPatientRoomBed(String noId, String page, String rows,
			BusinessHospitalbed bedEntity) throws Exception {
		return inpatientInfoDAO.queryPatientRoomBed(noId, page, rows, bedEntity);
	}

	/**
	 * @Description:查询列表总条数 @Author： tcj @CreateDate： 2016-1-4 11:38
	 * @param @return
	 * @return int
	 * @version 1.0
	 * @throws Exception 
	 **/
	@Override
	public int getTotal(BusinessHospitalbed bedEntity, String noId) throws Exception {
		return inpatientInfoDAO.getTotal(bedEntity, noId);
	}

	/**
	 * @Description:根据id查询患者信息 @Author： zhangjin @CreateDate： 2016-1-6
	 * @param @return
	 * @return
	 * @version 1.0
	 * @throws Exception 
	 **/
	@Override
	public InpatientInfoNow querNurseCharge(String no) throws Exception {
		return inpatientInfoDAO.querNurseCharge(no);
	}

	/**
	 * @Description：查询患者的住院信息（登记、接诊，以及相应的时间） @Author：tcj
	 * @CreateDate：2016-1-5 下午16:40 @ModifyRmk：
	 * @version 1.0
	 * @throws Exception 
	 */
	@Override
	public String queryMedicalrecordIdDate(String medicalrecordId) throws Exception {
		String d = DateUtils.formatDateY_M_D(new Date());
		Date now = DateUtils.parseDate(d, DateUtils.DATE_FORMAT);
		return inpatientInfoDAO.queryMedicalrecordIdDate(medicalrecordId, now);
	}

	/**
	 * @Description： 查询住院总次数
	 * 
	 * @Author：tcj
	 * @CreateDate：2016-1-5 下午18:40 @ModifyRmk：
	 * @version 1.0
	 * @throws Exception 
	 */
	@Override
	public int queryCount(String medicalrecordId) throws Exception {
		return inpatientInfoDAO.queryCount(medicalrecordId) + 1;
	}

	/**
	 * 
	 * @Description：根据住院流水号查询患者信息
	 * @Author：zhangjin @CreateDate：2016-1-
	 *                  18 @Modifier： @ModifyDate： @ModifyRmk：
	 * @version 1.0
	 * @throws Exception 
	 * @throws IOException
	 *
	 */
	@Override
	public List<InpatientInfoNow> queryNurseChargeInpinfo(String no, String dept) throws Exception {
		return inpatientInfoDAO.queryNurseChargeInpinfo(no, dept);
	}

	/**
	 * @Description： 查询是否有 有效的住院证明
	 * 
	 * @Author：tcj
	 * @CreateDate：2016-1-5 下午18:40 @ModifyRmk：
	 * @version 1.0
	 * @throws Exception 
	 */
	@Override
	public String queryProofInfo(String certificatesNo) throws Exception {
		return inpatientInfoDAO.queryProofInfo(certificatesNo);
	}

	@Override
	public List<InpatientInfoNow> getQueryInfo(String medicalrecordId) throws Exception {
		return inpatientInfoDAO.getQueryInfo(medicalrecordId);
	}

	@Override
	public InpatientBedinfoNow queryBedId(String bedInfoId) throws Exception {
		return inpatientInfoDAO.queryBedId(bedInfoId);
	}

	@Override
	public List<InpatientProof> getdengjiInfo(String medicalrecordId) throws Exception {
		return inpatientInfoDAO.getdengjiInfo(medicalrecordId);
	}

	@Override
	public List<SysDepartment> zyDeptCombobox() throws Exception {
		return inpatientInfoDAO.zyDeptCombobox();
	}

	@Override
	public void editInpatientInfo(InpatientInfoNow inpatientInfo, InpatientInPrepayNow inpatientInPrepay,
			InpatientSurety inpatientSurety) throws Exception {
		String createUser = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		SysDepartment dept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		Map<String, String> queryFinanceInvoiceNoByNum = medicineListInInterDAO.queryFinanceInvoiceNoByNum(createUser,"04", 1);
		if("error".equals(queryFinanceInvoiceNoByNum.get("resMsg"))){
			 queryFinanceInvoiceNoByNum.get("resCode");
		}
		// 保存预交金
		if (inpatientInPrepay.getPrepayCost() != null) {
			String invoiceNum=queryFinanceInvoiceNoByNum.get("resCode");
			// 住院流水号
			inpatientInPrepay.setTransType(1);//交易类型,1正交易，2反交易
			inpatientInPrepay.setInpatientNo(inpatientInfo.getInpatientNo());
			inpatientInPrepay.setName(inpatientInfo.getPatientName());
			inpatientInPrepay.setPayWay("CA");
			inpatientInPrepay.setHappenNo(Integer.parseInt(inprePayDAO.getSeqByNameorNum("SEQ_INPREPAY_HAPPENNO", 5)));// 发生序号
			inpatientInPrepay.setReceiptNo(invoiceNum);
			inpatientInPrepay.setDeptCode(dept.getDeptCode());// 科室代码 dept_code VARCHAR2(50),
			inpatientInPrepay.setDeptName(dept.getDeptName());
			inpatientInPrepay.setBalanceState(0);// 结算标志 0:未结算；1:已结算 2:已结转
			inpatientInPrepay.setPrepayState(0);// 预交金状态0:收取；1:作废;2:补打,3结算召回作废
			inpatientInPrepay.setTransFlag(0);// 0非转押金，1转押金，2转押金已打印 trans_flag
			inpatientInPrepay.setPrintFlag(0);// 打印标志 print_flag NUMBER(1),
			inpatientInPrepay.setExtFlag(1);// 正常收取 1 结算召回 2 ext_flag NUMBER(1),x
			inpatientInPrepay.setCreateUser(createUser);
			inpatientInPrepay.setCreateDept(inpatientInfo.getDeptCode());
			inpatientInPrepay.setCreateTime(new Date());
			inpatientInPrepay.setHospitalId(HisParameters.CURRENTHOSPITALID);
			inpatientInPrepay.setAreaCode(inprePayService.getDeptArea(dept.getDeptCode()));
			inpatientInfoDAO.save(inpatientInPrepay);
			//修改发票和添加发票使用记录
			SysEmployee employee = medicineListInInterDAO.queryEmployee(createUser);
			this.updateAndSaveInvoice(employee, invoiceNum, "04", dept,queryFinanceInvoiceNoByNum.get(invoiceNum));
		}
		// 保存担保金
		if (inpatientSurety.getSuretyCost() != null) {
			// 支付方式
			inpatientSurety.setPayWay("CA");
			// 住院流水号
			inpatientSurety.setInpatientNo(inpatientInfo.getInpatientNo());
			inpatientSurety.setHappenNo(Integer.parseInt(suretyDAO.getSeqByNameorNum("SEQ_INPREPAY_HAPPENNO", 5)));// 发生序号
			/** 担保金金状态1:收取；0:作废;2:补打 **/
			inpatientSurety.setState(1);
			inpatientSurety.setDeptCode(dept.getDeptCode());
			inpatientSurety.setCreateUser(createUser);
			inpatientSurety.setCreateDept(dept.getDeptCode());
			inpatientSurety.setCreateTime(new Date());
			inpatientInfoDAO.save(inpatientSurety);
		}
		Patient patient = inpatientInfoDAO.queryPatientInfomation(inpatientInfo.getMedicalrecordId());
		if (StringUtils.isNotBlank(inpatientInfo.getPatientName())
				&& !inpatientInfo.getPatientName().equals(patient.getPatientName())) {
			InpatientShiftData inpatientShiftData = new InpatientShiftData();
			inpatientShiftData.setId(null);
			inpatientShiftData.setClinicNo(inpatientInfo.getInpatientNo());
			List<InpatientShiftData> inpatientShift = inpatientInfoDAO.queryMaxHappenNo();
			inpatientShiftData.setHappenNo(inpatientShift.get(0).getHappenNo() + 1);
			inpatientShiftData.setShiftType("B");
			inpatientShiftData.setOldDataName(patient.getPatientName());
			inpatientShiftData.setNewDataName(inpatientInfo.getPatientName());
			inpatientShiftData.setShiftCause("住院登记");
			inpatientShiftData.setTableName("T_INPATIENT_INFO_NOW");
			inpatientShiftData.setChangePropertyName("patientName");
			inpatientShiftData.setCreateTime(DateUtils.getCurrentTime());
			inpatientShiftData.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			inpatientInfoDAO.save(inpatientShiftData);
			OperationUtils.getInstance().conserve(null, "资料变更", "INSERT INTO", "T_INPATIENT_SHIFTDATA",
					OperationUtils.LOGACTIONINSERT);
		}
		if (StringUtils.isNotBlank(inpatientInfo.getCertificatesNo())
				&& !inpatientInfo.getCertificatesNo().equals(patient.getPatientCertificatesno())) {
			InpatientShiftData inpatientShiftData1 = new InpatientShiftData();
			inpatientShiftData1.setId(null);
			inpatientShiftData1.setClinicNo(inpatientInfo.getInpatientNo());
			List<InpatientShiftData> inpatientShift = inpatientInfoDAO.queryMaxHappenNo();
			if (inpatientShift.size() > 0) {
				inpatientShiftData1.setHappenNo(inpatientShift.get(0).getHappenNo() + 1);
			} else {
				inpatientShiftData1.setHappenNo(1);
			}
			inpatientShiftData1.setShiftType("B");
			inpatientShiftData1.setOldDataName(patient.getPatientCertificatesno());
			inpatientShiftData1.setNewDataName(inpatientInfo.getCertificatesNo());
			inpatientShiftData1.setShiftCause("住院登记");
			inpatientShiftData1.setTableName("T_INPATIENT_INFO_NOW");
			inpatientShiftData1.setChangePropertyName("certificatesNo");
			inpatientShiftData1.setCreateTime(DateUtils.getCurrentTime());
			inpatientShiftData1.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			inpatientShiftData1.setHospitalId(HisParameters.CURRENTHOSPITALID);
			inpatientShiftData1.setAreaCode(inprePayService.getDeptArea(dept.getDeptCode()));
			inpatientInfoDAO.save(inpatientShiftData1);
			OperationUtils.getInstance().conserve(null, "资料变更", "INSERT INTO", "T_INPATIENT_SHIFTDATA",
					OperationUtils.LOGACTIONINSERT);
		}
		Integer a = queryCount(inpatientInfo.getMedicalrecordId());
		inpatientInfo.setInTimes(a);// 住院次数
		if (inpatientInPrepay.getPrepayCost() != null) {
			inpatientInfo.setPrepayCost(inpatientInPrepay.getPrepayCost());// 预交金（未结）
			inpatientInfo.setFreeCost(inpatientInPrepay.getPrepayCost());// 余额（未结）
		} else {
			inpatientInfo.setPrepayCost(0.0);// 预交金（未结）
			inpatientInfo.setFreeCost(0.0);// 余额（未结）
		}
		inpatientInfo.setChangePrepaycost(0.0);// 转入预交金（未结）
		inpatientInfo.setTotCost(0.0);// 费用金额
		inpatientInfo.setOwnCost(0.0);// 自费金额
		inpatientInfo.setPayCost(0.0);// 自付金额
		inpatientInfo.setPubCost(0.0);// 公费金额
		inpatientInfo.setEcoCost(0.0);// 优惠金额
		inpatientInfo.setChangeTotcost(0.0);// 转入费用金额（金额）
		inpatientInfo.setBalanceCost(0.0);// 费用金额（已结）
		inpatientInfo.setBalancePrepay(0.0);// 预交金额
		inpatientInfo.setBoardCost(0.0);// 膳食话费总额
		inpatientInfo.setBoardPrepay(0.0);// 膳食预交金额
		inpatientInfo.setBoardState(0);// 膳食结算状态
		inpatientInfo.setBalanceNo(0);// 结算序号
		inpatientInfo.setStopAcount(0);// 封账状态 （开账）
		inpatientInfo.setInIcu(0);// 是否在ICU 0 NO 1 YES
		inpatientInfo.setCaseFlag(0);// 病案状态
		inpatientInfo.setAnaphyFlag(patient.getPatientIsanaphy());// 过敏标志
		inpatientInfo.setHospitalId(HisParameters.CURRENTHOSPITALID);
		inpatientInfo.setAreaCode(inprePayService.getDeptArea(dept.getDeptCode()));
		// 新加字段赋值
		// 性别名称
		if (StringUtils.isBlank(inpatientInfo.getReportSex())) {
		} else {
			if ("1".equals(inpatientInfo.getReportSex())) {
				inpatientInfo.setReportSexName("男");
			} else if ("2".equals(inpatientInfo.getReportSex())) {
				inpatientInfo.setReportSexName("女");
			} else {
				inpatientInfo.setReportSexName("未知");
			}
		}
		inpatientInfoDAO.editInpatientInfo(inpatientInfo, patient);
	}

	public void updateAndSaveInvoice(SysEmployee employee,String invoiceNo,String invoiceType,SysDepartment department,String invoiceNoId){
		//修改发票
		medicineListInInterDAO.saveInvoiceFinance(employee. getJobNo(),invoiceNo,invoiceType,invoiceNoId);
		//添加发票使用记录
		InvoiceUsageRecord usageRecord = new InvoiceUsageRecord();
		usageRecord.setId(null);
		usageRecord.setUserId(employee.getJobNo());
		usageRecord.setUserCode(employee.getCode());
		usageRecord.setUserType(2);
		usageRecord.setInvoiceNo(invoiceNo);
		usageRecord.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		usageRecord.setCreateDept(department.getDeptCode());
		usageRecord.setCreateTime(DateUtils.getCurrentTime());
		usageRecord.setUserName(employee.getName());
		usageRecord.setInvoiceUsestate(1);
		medicineListInInterDAO.save(usageRecord);
	}

	@Override
	public List<SysEmployee> querykaijudocDj() throws Exception {
		return inpatientInfoDAO.querykaijudocDj();
	}

	@Override
	public List<Patient> getIdcardInfo(String medinfoId) throws Exception {
		return inpatientInfoDAO.getIdcardInfo(medinfoId);
	}

	@Override
	public List<Patient> getPatientInfoByCerNo(String cerno) throws Exception {
		return inpatientInfoDAO.getPatientInfoByCerNo(cerno);
	}

	@Override
	public List<InpatientPrepayin> queryPrepayinInfo(String medicalrecordId) throws Exception {
		return inpatientInfoDAO.queryPrepayinInfo(medicalrecordId);
	}

	@Override
	public String queryMedicalrecordIdDateByMid(String medicalrecordId) throws Exception {
		return inpatientInfoDAO.queryMedicalrecordIdDateByMid(medicalrecordId);
	}

	@Override
	public List<Patient> getPatientInfo(String medicalrecordId) throws Exception {
		return inpatientInfoDAO.getPatientInfo(medicalrecordId);
	}

	@Override
	public String getPaykind(String unit) throws Exception {
		return inpatientInfoDAO.getPaykind(unit);
	}

	@Override
	public List<DrugApplyoutNow> getDrugApplyoutNowList(String inpatientNo) throws Exception {
		return inpatientInfoDAO.getDrugApplyoutNowList(inpatientNo);
	}

	@Override
	public List<InpatientOrderNow> getInpatientOrderNowList(String inpatientNo) throws Exception {
		return inpatientInfoDAO.getInpatientOrderNowList(inpatientNo);
	}
}
