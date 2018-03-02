package cn.honry.patinent.idcard.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.honry.base.bean.model.District;
import cn.honry.base.bean.model.OutpatientAccount;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.PatientIdcard;
import cn.honry.base.bean.model.PatientIdcardChange;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.inner.baseinfo.district.service.DistrictInnerService;
import cn.honry.inner.patient.patient.service.PatinentInnerService;
import cn.honry.inner.system.parameter.service.ParameterInnerService;
import cn.honry.patinent.idcard.service.IdcardService;
import cn.honry.utils.Constants;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * @className：IdcardAction
 * @Description： 就诊卡管理action
 * @Author：lt
 * @CreateDate：2015-6-1 上午09:17:07
 * @version 1.0
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/patient/idcard")
public class IdcardAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private Logger logger=Logger.getLogger(IdcardAction.class);

	@Autowired

	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;
	}
	@Autowired
	@Qualifier(value = "idcardService")
	private IdcardService idcardService;

	public void setIdcardService(IdcardService idcardService) {
		this.idcardService = idcardService;
	}
	
	@Autowired
	@Qualifier(value = "patinentInnerService")
	private PatinentInnerService patinentInnerService;

	public void setPatinentInnerService(
			PatinentInnerService patinentInnerService) {
		this.patinentInnerService = patinentInnerService;
	}

	@Autowired
	@Qualifier(value = "districtInnerService")
	private DistrictInnerService districtInnerService;

	public void setDistrictInnerService(
			DistrictInnerService districtInnerService) {
		this.districtInnerService = districtInnerService;
	}

	@Autowired
	@Qualifier(value = "parameterInnerService")
	private ParameterInnerService parameterInnerService;

	public void setParameterInnerService(
			ParameterInnerService parameterInnerService) {
		this.parameterInnerService = parameterInnerService;
	}

	@Autowired
	@Qualifier(value = "innerCodeService")
	private CodeInInterService innerCodeService;

	public void setInnerCodeService(CodeInInterService innerCodeService) {
		this.innerCodeService = innerCodeService;
	}

	/**
	 * 起始页数
	 */
	private String page;
	/**
	 * 数据列数
	 */
	private String rows;
	/**
	 * 就诊卡实体 就诊卡的患者实体
	 */
	private PatientIdcard idcard;
	private Patient patient;
	/**
	 * 就诊卡ID
	 */
	private String id;
	/**
	 * 栏目别名,在主界面中点击栏目时传到action的参数
	 */
	private String menuAlias;
	/**
	 * 患者ID
	 */
	private String pid;
	/**
	 * 就诊卡类型
	 */
	private String idcardType;
	/**
	 * 患者姓名
	 */
	private String patientName;
	/**
	 * 就诊卡号
	 */
	private String idcardNO;
	/**
	 * 另一个方法中的就诊卡号
	 */
	private String idcardNo;
	/**
	 * 医保手册号
	 */
	private String handBook;
	/**
	 * 患者ID
	 */
	private String patientId;
	/**
	 * 就诊卡ID
	 */
	private String idcardId;
	/**
	 * 省市县三级联动
	 */
	/**
	 * 第一层ID
	 */
	private String oneId;
	/**
	 * 第一层名字(省名)
	 */
	private String oneName;
	/**
	 * 第一层CODE
	 */
	private String oneCode;
	/**
	 * 第二层ID
	 */
	private String twoId;
	/**
	 * 第二层名字(市名)
	 */
	private String twoName;
	/**CODE
	 */
	private String twoCode;
	/**
	 * 第三层ID
	 */
	private String threeId;
	/**
	 * 第三层名字(县名)
	 */
	private String threeName;
	/**
	 * 第三层CODE
	 */
	private String threeCode;
	/**
	 * 第四层ID
	 */
	private String fourId;
	/**
	 * 第四层名字(区名)
	 */
	private String fourName;
	/**
	 * 第四层CODE
	 */
	private String fourCode;
	/**
	 * 页面回显的时候用到
	 */
	/**
	 * 性别
	 */
	private String sex;
	/**
	 * 证件类型
	 */
	private String certificatestype;
	/**
	 * 职业
	 */
	private String occupation;
	/**
	 * 民族
	 */
	private String nation;
	/**
	 * 联系人
	 */
	private String relation;
	/**
	 * 婚姻状况
	 */
	private String warriage;
	/**
	 * 国籍
	 */
	private String nationality;
	/**
	 * 验证是否存在用到的字段
	 */
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 出生日期
	 */
	private String birthday;
	/**
	 * 联系方式
	 */
	private String cost;
	/**
	 * 证件类型
	 */
	private String CodeCertificate;
	/**
	 * 证件号码
	 */
	private String certificatesno;
	/**
	 * 城市
	 */
	private String patientCity;
	/**
	 * 病历号
	 */
	private String medicalId;
	/**
	 * 就诊卡List
	 */
	private List<PatientIdcard> idcardList;
	/**
	 * gson的方法改变
	 */
	private Map<String, Object> pager = new HashMap<String, Object>();

	/**
	 * @Description: 就诊卡信息列表
	 * @Author： lt
	 * @CreateDate： 2015-6-1
	 * @version 1.0
	 **/
	@RequiresPermissions(value = { "JZKGL:function:view" })
	@Action(value = "listIdcard", results = { @Result(name = "list", location = "/WEB-INF/pages/patient/idcard/idcardList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listIdcard() {
		return "list";
	}

	/**
	 * @Description: 就诊卡信息,新版,进入是信息展示界面
	 * @Author： zpty
	 * @CreateDate： 2015-12-23
	 * @version 1.0
	 **/
	@RequiresPermissions(value = { "JZKGL:function:view" })
	@Action(value = "listIdcardNext", results = { @Result(name = "listNext", location = "/WEB-INF/pages/patient/idcard/idcardListNext.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listIdcardNext() {
		return "listNext";
	}

	/**
	 * @Description:查询就诊卡信息
	 * @Author： lt
	 * @CreateDate：2015-6-1
	 * @Modifier：lt
	 * @ModifyDate：2015-6-1
	 * @ModifyRmk：
	 * @version 1.0
	 **/
	@RequiresPermissions(value = { "JZKGL:function:query" })
	@Action(value = "queryIdcard", results = { @Result(name = "json", type = "json") })
	public void queryIdcard() {
		PatientIdcard idcardSearch = new PatientIdcard();
		Patient patient = new Patient();
		if (StringUtils.isNotBlank(idcardType)) {
			idcardSearch.setIdcardType(idcardType);
		}
		if (StringUtils.isNotBlank(patientName)) {
			patient.setPatientName(patientName);
		}
		idcardSearch.setPatient(patient);
		idcardList = idcardService.getPage(page, rows, idcardSearch);
		int total = idcardService.getTotal(idcardSearch);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("total", total);
		map.put("rows", idcardList);
		String menuJson = JSONUtils.toJson(map);
		WebUtils.webSendJSON(menuJson);
	}

	/**
	 * @Description: 就诊卡信息添加
	 * @Author： lt
	 * @CreateDate： 2015-6-1
	 * @Modifier：lt
	 * @ModifyDate：2015-6-1
	 * @ModifyRmk：
	 * @version 1.0
	 **/
	@RequiresPermissions(value = { "JZKGL:function:add" })
	@Action(value = "addIdcard", results = { @Result(name = "add", location = "/WEB-INF/pages/patient/idcard/idcardEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String addIdcard(){
		idcard = new PatientIdcard();
		return "add";
	}

	/**
	 * @Description: 就诊卡信息修改
	 * @Author： lt
	 * @CreateDate： 2015-6-1
	 * @Modifier：lt
	 * @ModifyDate：2015-6-1
	 * @ModifyRmk：
	 * @version 1.0
	 **/
	@RequiresPermissions(value = { "JZKGL:function:edit" })
	@Action(value = "editInfoIdcard", results = { @Result(name = "edit", location = "/WEB-INF/pages/patient/idcard/idcardEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String editInfoIdcard() {
		if (StringUtils.isNotEmpty(id)) {
			idcard = idcardService.get(id);
		} else {
			idcard = new PatientIdcard();
		}
		// 省市县三级联动
		if (StringUtils.isNotEmpty(pid)) {
			patient = patinentInnerService.get(pid);
			String parId = patient.getPatientCity();
			List<District> districtList = districtInnerService.queryDistricttreeSJLDCX(parId);
			String one = "";
			String two = "";
			String three = "";
			String four = "";
			if (districtList != null && districtList.size() > 0) {
				String path = districtList.get(0).getUpperPath();
				if (StringUtils.isNotBlank(path)) {
					String[] pathss = path.split(",");
					if (pathss.length < 3) {
						one = pathss[0];
						two = pathss[1];
						three = parId;
					} else {
						one = pathss[0];
						two = pathss[1];
						three = pathss[2];
						four = parId;
					}
				}
			}
			List<District> districtListone = districtInnerService.queryDistricttreeSJLDCX(one);
			List<District> districtListtwo = districtInnerService.queryDistricttreeSJLDCX(two);
			List<District> districtListthree = districtInnerService.queryDistricttreeSJLDCX(three);
			List<District> districtListfour = districtInnerService.queryDistricttreeSJLDCX(four);

			if (districtListone != null && districtListone.size() > 0) {
				setOneId(districtListone.get(0).getId());
				setOneCode(districtListone.get(0).getCityCode());
				setOneName(districtListone.get(0).getCityName());
			} else {
				setOneId("");
				setOneCode("");
				setOneName("");
			}
			if (districtListtwo != null && districtListtwo.size() > 0) {
				setTwoId(districtListtwo.get(0).getId());
				setTwoCode(districtListtwo.get(0).getCityCode());
				setTwoName(districtListtwo.get(0).getCityName());
			} else {
				setTwoId("");
				setTwoCode("");
				setTwoName("");
			}
			if (districtListthree != null && districtListthree.size() > 0) {
				setThreeId(districtListthree.get(0).getId());
				setThreeCode(districtListthree.get(0).getCityCode());
				setThreeName(districtListthree.get(0).getCityName());
			} else {
				setThreeId("");
				setThreeCode("");
				setThreeName("");
			}
			if (districtListfour != null && districtListfour.size() > 0) {
				setFourId(districtListfour.get(0).getId());
				setFourCode(districtListfour.get(0).getCityCode());
				setFourName(districtListfour.get(0).getCityName());
			} else {
				setFourId("");
				setFourCode("");
				setFourName("");
			}
		} else {
			patient = new Patient();
		}

		String remark = "";
		if (StringUtils.isNotEmpty(idcard.getIdcardRemark())) {
			remark = idcard.getIdcardRemark().replaceAll("</br>", "\r\n");
		}
		idcard.setIdcardRemark(remark);
		return "edit";
	}

	/**
	 * @Description: 就诊卡信息修改,第二版,用next标识
	 * @Author： zpty
	 * @CreateDate： 2015-12-23
	 * @version 1.0
	 **/
	@RequiresPermissions(value = { "JZKGL:function:edit" })
	@Action(value = "editInfoIdcardNext", results = { @Result(name = "edit", location = "/WEB-INF/pages/patient/idcard/idcardEditNext.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String editInfoIdcardNext() {
		if (StringUtils.isNotEmpty(id)) {
			idcard = idcardService.get(id);
		} else {
			idcard = new PatientIdcard();
		}
		// 省市县三级联动
		if (StringUtils.isNotEmpty(pid)) {
			patient = patinentInnerService.get(pid);
			String parId = patient.getPatientCity();
			List<District> districtList = districtInnerService.queryDistricttreeSJLDCX(parId);
			String one = "";
			String two = "";
			String three = "";
			String four = "";
			if (districtList != null && districtList.size() > 0) {
				String path = districtList.get(0).getUpperPath();
				if (StringUtils.isNotBlank(path)) {
					String[] pathss = path.split(",");
					if (pathss.length < 3) {
						one = pathss[0];
						two = pathss[1];
						three = parId;
					} else {
						one = pathss[0];
						two = pathss[1];
						three = pathss[2];
						four = parId;
					}
				}
			}
			List<District> districtListone = districtInnerService.queryDistricttreeSJLDCX(one);
			List<District> districtListtwo = districtInnerService.queryDistricttreeSJLDCX(two);
			List<District> districtListthree = districtInnerService.queryDistricttreeSJLDCX(three);
			List<District> districtListfour = districtInnerService.queryDistricttreeSJLDCX(four);

			if (districtListone != null && districtListone.size() > 0) {
				setOneId(districtListone.get(0).getId());
				setOneCode(districtListone.get(0).getCityCode());
				setOneName(districtListone.get(0).getCityName());
			} else {
				setOneId("");
				setOneCode("");
				setOneName("");
			}
			if (districtListtwo != null && districtListtwo.size() > 0) {
				setTwoId(districtListtwo.get(0).getId());
				setTwoCode(districtListtwo.get(0).getCityCode());
				setTwoName(districtListtwo.get(0).getCityName());
			} else {
				setTwoId("");
				setTwoCode("");
				setTwoName("");
			}
			if (districtListthree != null && districtListthree.size() > 0) {
				setThreeId(districtListthree.get(0).getId());
				setThreeCode(districtListthree.get(0).getCityCode());
				setThreeName(districtListthree.get(0).getCityName());
			} else {
				setThreeId("");
				setThreeCode("");
				setThreeName("");
			}
			if (districtListfour != null && districtListfour.size() > 0) {
				setFourId(districtListfour.get(0).getId());
				setFourCode(districtListfour.get(0).getCityCode());
				setFourName(districtListfour.get(0).getCityName());
			} else {
				setFourId("");
				setFourCode("");
				setFourName("");
			}
			if(patient.getPatientBirthday()!=null){
				String dateView = patient.getPatientBirthday().toString();
				patient.setPatientBirthdayView(dateView.split(" ")[0]);
			}
		} else {
			patient = new Patient();
		}

		String remark = "";
		if (StringUtils.isNotEmpty(idcard.getIdcardRemark())) {
			remark = idcard.getIdcardRemark().replaceAll("</br>", "\r\n");
		}
		idcard.setIdcardRemark(remark);
		return "edit";
	}

	/**
	 * @Description:就诊卡信息浏览
	 * @Author： lt
	 * @CreateDate： 2015-6-1
	 * @Modifier：lt
	 * @ModifyDate：2015-6-1
	 * @ModifyRmk：
	 * @version 1.0
	 **/

	@Action(value = "viewIdcard", results = { @Result(name = "view", location = "/WEB-INF/pages/patient/idcard/idcardView.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String viewIdcard() {
		if (StringUtils.isNotEmpty(id)) {
			idcard = idcardService.get(id);
		} else {
			idcard = new PatientIdcard();
		}
		if (StringUtils.isNotEmpty(pid)) {
			patient = patinentInnerService.get(pid);
		} else {
			patient = new Patient();
		}
		Map<String, String> mapCertificate = innerCodeService.getBusDictionaryMap("certificate");
		Map<String, String> mapOccupation = innerCodeService.getBusDictionaryMap("occupation");
		Map<String, String> mapNationality = innerCodeService.getBusDictionaryMap("nationality");
		Map<String, String> mapRelation = innerCodeService.getBusDictionaryMap("relation");
		Map<String, String> mapMarry = innerCodeService.getBusDictionaryMap("marry");
		Map<String, String> mapCountry = innerCodeService.getBusDictionaryMap("country");
		Map<String, String> mapIdcardType = innerCodeService.getBusDictionaryMap("idcardType");
		String patientName = "";
		// 省市县三级联动
		if (StringUtils.isNotEmpty(pid)) {
			patient = patinentInnerService.get(pid);
			String parId = patient.getPatientCity();
			List<District> districtList = districtInnerService.queryDistricttreeSJLDCX(parId);
			String one = "";
			String two = "";
			String three = "";
			String four = "";
			if (districtList != null && districtList.size() > 0) {
				String path = districtList.get(0).getUpperPath();
				if (StringUtils.isNotBlank(path)) {
					String[] pathss = path.split(",");
					if (pathss.length < 3) {
						one = pathss[0];
						two = pathss[1];
						three = parId;
					} else {
						one = pathss[0];
						two = pathss[1];
						three = pathss[2];
						four = parId;
					}
				}
			}
			List<District> districtListone = districtInnerService.queryDistricttreeSJLDCX(one);
			List<District> districtListtwo = districtInnerService.queryDistricttreeSJLDCX(two);
			List<District> districtListthree = districtInnerService.queryDistricttreeSJLDCX(three);
			List<District> districtListfour = districtInnerService.queryDistricttreeSJLDCX(four);

			if (districtListone != null && districtListone.size() > 0) {
				patientName += districtListone.get(0).getCityName();
			}
			if (districtListtwo != null && districtListtwo.size() > 0) {
				patientName += "-" + districtListtwo.get(0).getCityName();
			}
			if (districtListthree != null && districtListthree.size() > 0) {
				patientName += "-" + districtListthree.get(0).getCityName();
			}
			if (districtListfour != null && districtListfour.size() > 0) {
				patientName += "-" + districtListfour.get(0).getCityName();
			}
		}
		patient.setPatientCity(patientName);
		// 地址为null出现undifine的问题
		if (patient.getPatientAddress() == null) {
			patient.setPatientAddress("");
		}
		// 性别
		if (patient.getPatientSex() == 1) {
			setSex("男");
		}
		if (patient.getPatientSex() == 2) {
			setSex("女");
		}
		if (patient.getPatientSex() == 3) {
			setSex("未知");
		}
		// 证件类型
		setCertificatestype(mapCertificate.get(patient.getPatientCertificatestype()));
		// 职业
		setOccupation(mapOccupation.get(patient.getPatientOccupation()));
		// 民族
		setNation(mapNationality.get(patient.getPatientNation()));
		// 联系人关系
		setRelation(mapRelation.get(patient.getPatientLinkrelation()));
		// 婚姻状况
		setWarriage(mapMarry.get(patient.getPatientWarriage()));
		// 国籍
		setNationality(mapCountry.get(patient.getPatientNationality()));
		// 就诊卡类型
		setIdcardType(mapIdcardType.get(idcard.getIdcardType()));
		return "view";
	}

	/**
	 * @Description: 就诊卡信息添加&修改
	 * @Author： lt
	 * @CreateDate：2015-6-1
	 * @Modifier：lt
	 * @ModifyDate：2015-6-1
	 * @ModifyRmk：
	 * @version 1.0
	 **/
	@Action(value = "editIdcard")
	public void editIdcard(){
		String flag="ok";
		//验证医保号
		flag=checkHandbookNext(patient.getPatientHandbook(),patient.getId());
		if(!"ok".equals(flag)){
			WebUtils.webSendString(flag);
			return;
		}
		//验证此患者是否已存在
		flag=checkIdcardNameNext(patient.getPatientCertificatestype(),patient.getPatientCertificatesno(),patient.getId());//证件类型,证件号,id
		if(!"ok".equals(flag)){
			WebUtils.webSendString(flag);
			return;
		}
		//验证卡号是否已存在
		flag=checkIdcardNoNext(idcard.getIdcardNo(),idcard.getId());
		if(!"ok".equals(flag)){
			WebUtils.webSendString(flag);
			return;
		}
		//验证通过后才进行保存or修改操作
		PatientIdcard idcardSave=new PatientIdcard();
		if(StringUtils.isNotBlank(idcard.getId())){
			idcardSave=idcardService.get(idcard.getId());
		}
		String remark = "";
		if (StringUtils.isNotEmpty(idcard.getIdcardRemark())) {
			remark = idcard.getIdcardRemark().replaceAll("\\r\\n", "</br>");
			remark = remark.replaceAll("\\n", "</br>");
		}
		idcardSave.setIdcardRemark(remark);
		idcardSave.setIdcardType(idcard.getIdcardType());//卡类型
		idcardSave.setIdcardNo(idcard.getIdcardNo());//卡号
		idcardSave.setHospitalId(HisParameters.CURRENTHOSPITALID.toString());//所属医院
		idcardSave.setAreaCode(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getAreaCode());//所属院区
		//先取到patient的实体
		Patient patientSave=new Patient();
		if(StringUtils.isNotBlank(patient.getId())){
			patientSave=patinentInnerService.get(patient.getId());
		}else{
			//如果是新增则将所有的字段都存进去,共同的部分除外
			patientSave.setPatientInputcode(patient.getPatientInputcode());//自定义码
			patientSave.setPatientDoorno(patient.getPatientDoorno());//门牌号
			patientSave.setPatientNativeplace(patient.getPatientNativeplace());//籍贯
			patientSave.setPatientNationality(patient.getPatientNationality());//国籍
			patientSave.setPatientNation(patient.getPatientNation());//民族
			patientSave.setPatientWorkunit(patient.getPatientWorkunit());//工作单位
			patientSave.setPatientWorkphone(patient.getPatientWorkphone());//单位电话
			patientSave.setPatientWarriage(patient.getPatientWarriage());//婚姻状况
			patientSave.setPatientOccupation(patient.getPatientOccupation());//职业
			patientSave.setPatientHandbook(patient.getPatientHandbook());//医保号
			patientSave.setPatientEmail(patient.getPatientEmail());//电子邮箱
			patientSave.setPatientMother(patient.getPatientMother());//母亲姓名
			patientSave.setPatientLinkman(patient.getPatientLinkman());//联系人
			patientSave.setPatientLinkrelation(patient.getPatientLinkrelation());//联系人关系
			patientSave.setPatientLinkaddress(patient.getPatientLinkaddress());//联系人地址
			patientSave.setPatientLinkdoorno(patient.getPatientLinkdoorno());//联系人门牌号
			patientSave.setPatientLinkphone(patient.getPatientLinkphone());//联系人电话
			patientSave.setCaseNo(patient.getCaseNo());//病案号
		}
		// patinentInnerService.saveOrUpdate(patient);
		// 20160901将出生地与家庭地址设置相同
		patientSave.setPatientBirthplace(patient.getPatientCity());
		// 20160927 患者表保存就诊卡号
		patientSave.setCardNo(idcard.getIdcardNo());
		// 根据出生日期计算年龄
		String ageString=getAge(patient.getPatientBirthday());
		patientSave.setPatientAge(Double.valueOf(ageString.substring(0,ageString.length()-1)));
		patientSave.setPatientAgeunit(ageString.substring(ageString.length()-1,ageString.length()));
		patientSave.setPatientName(patient.getPatientName());//患者姓名
		patientSave.setPatientSex(patient.getPatientSex());//患者性别
		patientSave.setPatientBirthday(patient.getPatientBirthday());//出生日期
		patientSave.setPatientPhone(patient.getPatientPhone());//联系方式
		patientSave.setPatientCertificatestype(patient.getPatientCertificatestype());//证件类型
		patientSave.setPatientCertificatesno(patient.getPatientCertificatesno());//证件号码
		patientSave.setPatientAddress(patient.getPatientAddress());//家庭地址
		patientSave.setPatientCity(patient.getPatientCity());//省市县
		patientSave.setUnit(patient.getUnit());//合同单位
		patientSave.setUnitCode(patient.getUnit());//合同单位code
		patientSave.setPatientPaykind(patient.getPatientPaykind());//结算类别
		patientSave.setHospitalId(HisParameters.CURRENTHOSPITALID.toString());//所属医院
		patientSave.setAreaCode(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getAreaCode());//所属院区
		idcardSave.setPatient(patientSave);
		idcardService.saveOrUpdate(idcardSave, patientSave);// 此处将患者保存和就诊卡保存放入一个事物
	}
	/**
	 * @author GH 
	 * @param birthDate
	 *  自己写了一个计算年龄和年龄类型的方法
	 *  2016年9月28日10:45:39
	 * @return
	 */
	public static String getAge(Date birthDate) {
		String result="";
		int year;
		int m;
		int day;
		Date now = new Date();

		SimpleDateFormat format_y = new SimpleDateFormat("yyyy");
		SimpleDateFormat format_M = new SimpleDateFormat("MM");
		SimpleDateFormat format_D = new SimpleDateFormat("dd");

		String birth_year = format_y.format(birthDate);
		String this_year = format_y.format(now);

		String birth_month = format_M.format(birthDate);
		String this_month = format_M.format(now);
		
		String birth_day = format_D.format(birthDate);
		String this_day = format_D.format(now);

		// 初步，估算
		year = Integer.parseInt(this_year) - Integer.parseInt(birth_year);
		m = Integer.parseInt(this_month) - Integer.parseInt(birth_month);
		day = Integer.parseInt(this_day) - Integer.parseInt(birth_day);

		if(year==0){
			if(m==0){
				result=day+"天";
			}else if(m>0){
				result=m+"月";
			}
		}else if(year>0){
			result=year+"岁";
		}
		return result;
	}

	/**
	 * @Description: 就诊卡信息删除
	 * @Author： lt
	 * @CreateDate： 2015-6-1
	 * @Modifier：lt
	 * @ModifyDate：2015-6-1
	 * @ModifyRmk：
	 * @version 1.0
	 **/
	@RequiresPermissions(value = { "JZKGL:function:delete" })
	@Action(value = "delIdcard", results = { @Result(name = "list", location = "/WEB-INF/pages/patient/idcard/idcardList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String delIdcard() throws Exception {
		if (id.contains(",")) {
			for (String onlyId : id.split(",")) {
				idcard = idcardService.get(onlyId);
				if (idcard.getPatient() != null) {
					patinentInnerService.removeUnused(idcard.getPatient().getId());
				}

			}
		} else {
			idcard = idcardService.get(id);
			patinentInnerService.delete(idcard.getPatient());
		}

		idcardService.del(id);
		return "list";
	}

	/**
	 * @Description:查询就诊卡列表（做下拉使用）
	 * @Author： lt
	 * @CreateDate： 2015-6-1
	 * @Modifier：lt
	 * @ModifyDate：2015-6-1
	 * @ModifyRmk：
	 * @version 1.0
	 **/
	@Action(value = "findAllIdcard", results = { @Result(name = "json", type = "json") })
	public void findAllIdcard() {

		idcardList = idcardService.findAll();
		String menuJson = JSONUtils.toJson(idcardList);
		WebUtils.webSendJSON(menuJson);
	}

	/**
	 * @Description: 根据idcard获取某个患者信息返回json
	 * @Author： lt
	 * @CreateDate： 2015-6-15
	 * @Modifier：lt
	 * @ModifyDate：2015-6-15
	 * @ModifyRmk：
	 * @version 1.0
	 **/
	@Action(value = "getPatientByIdcard", results = { @Result(name = "json", type = "json") })
	public void getPatientByIdcard() {
		idcard = idcardService.queryByIdcardNo(id);
		if (idcard != null) {
			OutpatientAccount account = idcardService.queryByIdcardIdOut(idcard
					.getId());
			if (account != null) {
				idcard.setAccount(account);
			} else {
				account = new OutpatientAccount();
				account.setId("no");
				idcard.setAccount(account);
			}

		}
		String menuJson = JSONUtils.toJson(idcard);
		WebUtils.webSendJSON(menuJson);
	}

	/**
	 * @Description: 根据accountId获取idcard信息返回json
	 * @Author： lt
	 * @CreateDate： 2015-6-15
	 * @Modifier：lt
	 * @ModifyDate：2015-6-15
	 * @ModifyRmk：
	 * @version 1.0
	 **/
	@Action(value = "queryIdCardByAccount", results = { @Result(name = "json", type = "json") })
	public void queryIdCardByAccount(){
		idcard = new PatientIdcard();
		// 借用一下createUser字段 其实传的卡号idcardNO，为了方便使用这个getpage方法；
		idcard.setCreateUser(idcardNO);
		idcard.setPatient(new Patient());
		idcardList = idcardService.getPage(page, rows, idcard);
		Map<String, String> mapIdcardType = null;
		mapIdcardType = innerCodeService.getBusDictionaryMap("idcardType");
		for (PatientIdcard idcard : idcardList) {
			idcard.setIdcardType(mapIdcardType.get(idcard.getIdcardType()));
		}
		int total = idcardService.getTotal(idcard);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("total", total);
		map.put("rows", idcardList);
		String menuJson = JSONUtils.toJson(map);
		WebUtils.webSendJSON(menuJson);
	}

	/**
	 * 
	 * @Description： 就诊卡id信息
	 * @Author：wujiao
	 * @CreateDate：2015-6-3 下午05:12:01
	 * @Modifier：wujiao
	 * @ModifyDate：2015-6-3 下午05:12:01
	 * @ModifyRmk：
	 * @version 1.0
	 *
	 */
	@Action(value = "queryCcardId")
	public void queryCcardId() {
		PatientIdcard idcardSearch = new PatientIdcard();
		if (StringUtils.isNotBlank(idcardNo)) {
			idcardSearch.setIdcardNo(idcardNo);
		}
		idcardSearch = idcardService.getkh(idcardNo);
		String menuJson = JSONUtils.toExposeJson(idcardSearch, false,"yyyy-MM-dd", null);
		WebUtils.webSendJSON(menuJson);
	}

	/**
	 * @Description:验证医保手册号是否已存在
	 * @Author： lt
	 * @CreateDate： 2015-10-15
	 * @return void
	 * @version 1.0
	 **/
	@Action(value = "checkHandbook")
	public void checkHandbook(){
		String result = "no";
		boolean flag = patinentInnerService.checkHandBook(handBook, patientId);

		if (!flag) {
			result = "ok";
		}
		WebUtils.webSendString(result);
	}

	/**
	 * @Description:验证医保手册号是否已存在(后台验证)
	 * @Author： zpty
	 * @CreateDate： 2017-4-8
	 * @return void
	 * @version 1.0
	 **/
	public String checkHandbookNext(String handBook,String patientId){
		String result = "checkHandbookNo";
		boolean flag = patinentInnerService.checkHandBook(handBook, patientId);
		if (!flag) {
			result = "ok";
		}
		return result;
	}
	
	/**
	 * @Description:验证卡号和病历号是否已存在
	 * @Author： lt
	 * @CreateDate： 2015-10-15
	 * @return void
	 * @version 1.0
	 **/
	@Action(value = "checkIdcardNoVSMedicalNO")
	public void checkIdcardNoVSMedicalNO(){
		String result = "ok";
		String flag = idcardService.checkIdcardNoVSMedicalNO(idcardNo, null,idcardId);
		if (flag == "idcardNO") {
			result = "no1";
		}
		WebUtils.webSendString(result);
	}

	/**
	 * @Description:验证卡号是否已存在
	 * @Author：zpty
	 * @CreateDate： 2017-4-8
	 * @return void
	 * @version 1.0
	 **/
	public String checkIdcardNoNext(String idcardNo,String idcardId){
		String result = "ok";
		result = idcardService.checkIdcardNoVSMedicalNO(idcardNo, null,idcardId);
		return result;
	}
	
	/**
	 * @Description:验证患者是否已存在
	 * @Author： zpty
	 * @CreateDate： 2015-12-24
	 * @return void
	 * @version 1.0
	 **/
	@Action(value = "checkIdcardName")
	public void checkIdcardName(){
		String result = "ok";
		String flag = idcardService.checkIdcardName(name, sex, birthday, cost,
				CodeCertificate, certificatesno, patientCity, pid);
		if (flag == "nameNO") {
			result = "no1";
		}
		WebUtils.webSendString(result);
	}

	/**
	 * @Description:验证患者是否已存在(改为后台验证)
	 * @Author： zpty
	 * @CreateDate： 2017-4-8
	 * @return void
	 * @version 1.0
	 **/
	public String checkIdcardNameNext(String CodeCertificate,String certificatesno,String pid){
		String result = "ok";
		result = idcardService.checkIdcardName(null, null, null, null,
				CodeCertificate, certificatesno, null, pid);//只要ID,证件类型和证件号
		return result;
	}
	
	/**
	 * @Description:执行销户 、退卡操作
	 * @Author： lt
	 * @CreateDate： 2015-10-15
	 * @return void
	 * @version 1.0
	 **/
	@RequiresPermissions(value = { "JZKGL:function:backCard" })
	@Action(value = "backIdcard")
	public void backIdcard(){
		String result = "no";
		if (StringUtils.isNotEmpty(idcardId)) {
			// 用就诊卡ID在门诊账户表中查出账户
			OutpatientAccount account = idcardService.queryByIdcardIdOut(idcardId);
			PatientIdcard idcard = idcardService.get(idcardId);
			// 这里创建一个就诊卡变更的对象,用来记录变更数据
			if (idcard != null) {
				PatientIdcardChange changeIdcard = new PatientIdcardChange();
				// 就诊卡编号
				changeIdcard.setIdcard_Id(idcard.getId());
				// 患者编号
				changeIdcard.setPatient(idcard.getPatient().getId());
				// 变更类型2退卡
				changeIdcard.setChangeStatus(2);
				// 旧卡卡号
				changeIdcard.setOldIdcardNo(idcard.getIdcardNo());
				// 旧卡类型
				changeIdcard.setOldIdcardType(idcard.getIdcardType());
				// 存入就诊卡变更表
				idcardService.changeIdcardsaveOrUpdate(changeIdcard);
			}
			if (account != null) {
				// 将门诊账户中状态改为注销
				account.setAccountState(2);
				// 将门诊账户中的就诊卡ID设为空,去除之间的关联关系
				account.setIdcardId(" ");
				// 直接改变门诊账户中就诊卡信息,其他信息在账户注销的时候就修改了,这里不用管,只针对就诊卡
				idcardService.saveOrUpdateOut(account);
			}
			idcardService.removeUnused(idcardId);
			result = "yes";
			/** 修改患者数据里的卡号信息*/
			//将患者信息里的卡号置空
			patinentInnerService.updCradNoByIdOrCard(null,idcardNO,""); 
		}
		WebUtils.webSendString(result);
	}

	/**
	 * @Description:根据keyValue表中设置查询是否在未结清账户情况下退卡
	 * @Author： lt
	 * @CreateDate： 2015-10-15
	 * @return void
	 * @version 1.0
	 **/
	@Action(value = "checkIsBackCard")
	public void checkIsBackCard(){
		String value = parameterInnerService.getparameter(Constants.BACK_CARD_FLAG);
		WebUtils.webSendString(value);
	}

	/**
	 * @Description:执行挂失操作
	 * @Author： lt
	 * @CreateDate： 2015-11-18
	 * @return void
	 * @modify zpty
	 * @modifyDate 20160325
	 * @version 1.0
	 **/
	@RequiresPermissions(value = { "JZKGL:function:lossCard" })
	@Action(value = "lossIdcard")
	public void lossIdcard(){
		String result = "no";
		if (StringUtils.isNotEmpty(idcardId)) {
			idcard = idcardService.get(idcardId);
			// 门诊账户(新表)
			OutpatientAccount account = idcardService.queryByIdcardIdOut(idcardId);
			if (account == null) {// 挂失就诊卡,卡状态改为2停用
				idcard.setIdcardStatus(2);
				idcardService.saveOrUpdate(idcard);
				result = "yes";
			} else {
				if (account != null) {// 账号不为空执行冻结账号操作
					account.setAccountState(0);// 新表中0表示停用
					idcardService.saveOrUpdateOut(account);
				}
				// 挂失就诊卡,卡状态改为2停用
				idcard.setIdcardStatus(2);
				idcardService.saveOrUpdate(idcard);
				result = "yes";
			}
		}
		WebUtils.webSendString(result);
	}

	/**
	 * @Description:跳到补办就诊卡页面
	 * @Author： lt
	 * @CreateDate： 2015-11-18
	 * @return void
	 * @version 1.0
	 **/
	@RequiresPermissions(value = { "JZKGL:function:fillCard" })
	@Action(value = "fillInfoIdcard", results = { @Result(name = "fill", location = "/WEB-INF/pages/patient/idcard/fillIdcardNext.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String fillInfoIdcard(){
		if (StringUtils.isNotEmpty(id)) {
			idcard = idcardService.get(id);
		} else {
			idcard = new PatientIdcard();
		}
		if (StringUtils.isNotEmpty(pid)) {
			patient = patinentInnerService.get(pid);
		} else {
			patient = new Patient();
		}
		Map<String, String> mapCertificate = innerCodeService.getBusDictionaryMap("certificate");
		Map<String, String> mapOccupation = innerCodeService.getBusDictionaryMap("occupation");
		Map<String, String> mapNationality = innerCodeService.getBusDictionaryMap("nationality");
		Map<String, String> mapRelation = innerCodeService.getBusDictionaryMap("relation");
		Map<String, String> mapMarry = innerCodeService.getBusDictionaryMap("marry");
		Map<String, String> mapCountry = innerCodeService.getBusDictionaryMap("country");
		Map<String, String> mapIdcardType = innerCodeService.getBusDictionaryMap("idcardType");

		String patientName = "";
		// 省市县三级联动
		if (StringUtils.isNotEmpty(pid)) {
			patient = patinentInnerService.get(pid);
			String parId = patient.getPatientCity();
			List<District> districtList = districtInnerService.queryDistricttreeSJLDCX(parId);
			String one = "";
			String two = "";
			String three = "";
			String four = "";
			if (districtList != null && districtList.size() > 0) {
				String path = districtList.get(0).getUpperPath();
				if (StringUtils.isNotBlank(path)) {
					String[] pathss = path.split(",");
					if (pathss.length < 3) {
						one = pathss[0];
						two = pathss[1];
						three = parId;
					} else {
						one = pathss[0];
						two = pathss[1];
						three = pathss[2];
						four = parId;
					}
				}
			}
			List<District> districtListone = districtInnerService.queryDistricttreeSJLDCX(one);
			List<District> districtListtwo = districtInnerService.queryDistricttreeSJLDCX(two);
			List<District> districtListthree = districtInnerService	.queryDistricttreeSJLDCX(three);
			List<District> districtListfour = districtInnerService.queryDistricttreeSJLDCX(four);

			if (districtListone != null && districtListone.size() > 0) {
				patientName += districtListone.get(0).getCityName();
			}
			if (districtListtwo != null && districtListtwo.size() > 0) {
				patientName += "-" + districtListtwo.get(0).getCityName();
			}
			if (districtListthree != null && districtListthree.size() > 0) {
				patientName += "-" + districtListthree.get(0).getCityName();
			}
			if (districtListfour != null && districtListfour.size() > 0) {
				patientName += "-" + districtListfour.get(0).getCityName();
			}
		}
		patient.setPatientCity(patientName);
		// 地址为null出现undifine的问题
		if (patient.getPatientAddress() == null) {
			patient.setPatientAddress("");
		}
		// 性别
		if (patient.getPatientSex() == 1) {
			setSex("男");
		}
		if (patient.getPatientSex() == 2) {
			setSex("女");
		}
		if (patient.getPatientSex() == 3) {
			setSex("未知");
		}
		// 证件类型
		setCertificatestype(mapCertificate.get(patient.getPatientCertificatestype()));
		// 职业
		setOccupation(mapOccupation.get(patient.getPatientOccupation()));
		// 民族
		setNation(mapNationality.get(patient.getPatientNation()));
		// 联系人关系
		setRelation(mapRelation.get(patient.getPatientLinkrelation()));
		// 婚姻状况
		setWarriage(mapMarry.get(patient.getPatientWarriage()));
		// 国籍
		setNationality(mapCountry.get(patient.getPatientNationality()));
		// 就诊卡类型
		setIdcardType(mapIdcardType.get(idcard.getIdcardType()));
		return "fill";
	}

	/**
	 * @Description: 补办就诊卡
	 * @Author： lt
	 * @CreateDate：2015-11-18
	 * @version 1.0
	 **/
	@RequiresPermissions(value = { "JZKGL:function:fillCard" })
	@Action(value = "fillIdcard", results = { @Result(name = "list", location = "/WEB-INF/pages/patient/idcard/idcardList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String fillIdcard(){
		String remark = "";
		if (StringUtils.isNotEmpty(idcard.getIdcardRemark())) {
			remark = idcard.getIdcardRemark().replaceAll("\\r\\n", "</br>");
			remark = remark.replaceAll("\\n", "</br>");
		}
		// 这里创建一个就诊卡变更的对象,用来记录变更数据
		PatientIdcardChange changeIdcard = new PatientIdcardChange();
		// 就诊卡编号
		changeIdcard.setIdcard_Id(idcard.getId());
		// 患者编号
		changeIdcard.setPatient(patient.getId());
		// 新卡卡号
		changeIdcard.setIdcardNo(idcard.getIdcardNo());
		// 新卡类型
		changeIdcard.setIdcardType(idcard.getIdcardType());
		// 变更类型1补卡
		changeIdcard.setChangeStatus(1);
		// 创建一个就诊卡对象,来存旧的就诊卡数据
		PatientIdcard oldIdcard = idcardService.queryOldIdcardById(idcard.getId());
		if (oldIdcard != null) {
			// 旧卡卡号
			changeIdcard.setOldIdcardNo(oldIdcard.getIdcardNo());
			// 旧卡类型
			changeIdcard.setOldIdcardType(oldIdcard.getIdcardType());
		}
		// 存入就诊卡变更表
		idcardService.changeIdcardsaveOrUpdate(changeIdcard);
		// 判断患者账户和住院账户
		if (oldIdcard != null) {
			if (oldIdcard.getIdcardStatus() == 2) {
				OutpatientAccount account = idcardService.queryByIdcardIdOut(idcard.getId());
				if (account != null) {
					if (account.getAccountState() == 0) {// 账户解冻操作
						account.setAccountState(1);
						idcardService.saveOrUpdateOut(account);
					}
				}
				oldIdcard.setIdcardStatus(1);
			}
			oldIdcard.setUpdateTime(new Date());
			oldIdcard.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		}else{
			oldIdcard=new PatientIdcard();
			oldIdcard.setHospitalId(HisParameters.CURRENTHOSPITALID.toString());//所属医院
			oldIdcard.setAreaCode(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getAreaCode());//所属院区
			oldIdcard.setCreateTime(new Date());
			oldIdcard.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			oldIdcard.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
		}
		oldIdcard.setIdcardNo(idcard.getIdcardNo());//新卡号
		oldIdcard.setIdcardType(idcard.getIdcardType());//新卡类型
		oldIdcard.setIdcardRemark(remark);//备注
		oldIdcard.setPatient(patient);
		oldIdcard.setIdcardCreatetime(new Date());// 操作时间
		oldIdcard.setIdcardOperator(ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo());// 操作人员员工号
		//保存就诊卡数据
		idcardService.saveOrUpdate(oldIdcard);
		
		/** 修改患者数据里的卡号信息*/
		//保存新的卡号
		patinentInnerService.updCradNoByIdOrCard(patient.getId(),null,idcard.getIdcardNo());
		return "list";
	}

	/**
	 * @Description:判断是否可以执行激活操作
	 * @Author： lt
	 * @CreateDate： 2015-12-3
	 * @modify zpty 2016-03-26
	 * @return void
	 * @version 1.0
	 **/
	@Action(value = "checkIdcard")
	public void checkIdcard(){
		String result = "no";
		if (StringUtils.isNotEmpty(idcardId)) {
			idcard = idcardService.get(idcardId);
			if (idcard != null) {
				if (idcard.getIdcardStatus() == 2) {
					result = "yes";
				} else {
					result = "no";
				}
			}
		}
		WebUtils.webSendString(result);
	}

	/**
	 * @Description:执行激活操作
	 * @Author： lt
	 * @CreateDate： 2015-12-3
	 * @return void
	 * @version 1.0
	 **/
	@RequiresPermissions(value = { "JZKGL:function:activateCard" })
	@Action(value = "activateIdcard")
	public void activateIdcard(){
		String result = "no";
		if (StringUtils.isNotEmpty(idcardId)) {
			boolean flag = idcardService.activateIdcard(idcardId);// 将卡的标识改成正常
			if (flag) {
				// 这里除了要激活卡以外,还要解冻账户zpty20151205
				OutpatientAccount account = idcardService.queryByIdcardIdOut(idcardId);
				if (account != null) {
					if (account.getAccountState() == 0) {// 账户解冻操作
						account.setAccountState(1);
						idcardService.saveOrUpdateOut(account);
					}
				}
				result = "yes";
			} else {
				result = "no";
			}
		}
		WebUtils.webSendString(result);
	}

	/**
	 * 
	 * @Description： 获得就诊卡号的Map
	 * @Author：lt
	 * @CreateDate：2015-12-19 上午11:22:31
	 * @Modifier：lt
	 * @ModifyDate：2015-12-19 上午11:22:31
	 * @ModifyRmk：
	 * @version 1.0
	 *
	 */
	@Action(value = "getCardNoMap", results = { @Result(name = "json", type = "json") })
	public void getCardNoMap() {
		Map<String, String> cardNoMap = idcardService.getCardNoMap();
		String menuJson = JSONUtils.toJson(cardNoMap);
		WebUtils.webSendJSON(menuJson);
	}

	/**
	 * @Description： 读卡查询信息
	 * @Author：wujiao
	 * @CreateDate：2016-3-23 上午9:40:16
	 * @ModifyRmk：
	 * @version 1.0
	 */
	@Action(value = "queryIdcadAllInfo", results = { @Result(name = "json", type = "json") })
	public void queryRegisterInfo() {
		PatientIdcard patient = idcardService.queryIdcadAllInfo(idcardNo);
		if (patient != null) {
			Patient p = patient.getPatient();
			String patientId = p.getPatientCity();
			String patientName = "";
			// 省市县三级联动
			if (StringUtils.isNotEmpty(patientId)) {
				List<District> districtList = districtInnerService.queryDistricttreeSJLDCX(patientId);
				String one = "";
				String two = "";
				String three = "";
				String four = "";
				if (districtList != null && districtList.size() > 0) {
					String path = districtList.get(0).getUpperPath();
					if (StringUtils.isNotBlank(path)) {
						String[] pathss = path.split(",");
						if (pathss.length < 3) {
							one = pathss[0];
							two = pathss[1];
							three = patientId;
						} else {
							one = pathss[0];
							two = pathss[1];
							three = pathss[2];
							four = patientId;
						}
					}
				}
				List<District> districtListone = districtInnerService.queryDistricttreeSJLDCX(one);
				List<District> districtListtwo = districtInnerService.queryDistricttreeSJLDCX(two);
				List<District> districtListthree = districtInnerService.queryDistricttreeSJLDCX(three);
				List<District> districtListfour = districtInnerService.queryDistricttreeSJLDCX(four);

				if (districtListone != null && districtListone.size() > 0) {
					patientName += districtListone.get(0).getCityName();
				}
				if (districtListtwo != null && districtListtwo.size() > 0) {
					patientName += "-" + districtListtwo.get(0).getCityName();
				}
				if (districtListthree != null && districtListthree.size() > 0) {
					patientName += "-" + districtListthree.get(0).getCityName();
				}
				if (districtListfour != null && districtListfour.size() > 0) {
					patientName += "-" + districtListfour.get(0).getCityName();
				}
			}
			p.setPatientCity(patientName);
			// 地址为null出现undifine的问题
			if (p.getPatientAddress() == null) {
				p.setPatientAddress("");
			}
			patient.setPatient(p);
			// zpty20160324将员工号转成员工姓名显示出来
			String operator = patient.getIdcardOperator();
			SysEmployee operatorEmployee = idcardService.findEmpByjobNo(operator);
			String operatorname = "";
			operatorname = operatorEmployee.getName();
			patient.setIdcardOperator(operatorname);
			String menuJson = JSONUtils.toJson(patient, false,DateUtils.DATE_FORMAT, false);
			WebUtils.webSendJSON(menuJson);
		}
	}

	/***
	 * 根据就诊卡ID，查询患者账户实体,验证患者账户是否可退卡
	 * 
	 * @Description:
	 * @author zpty
	 * @date 创建时间：2016年4月8日
	 * @version 1.0
	 * @param
	 * @since
	 */
	@Action(value = "getAccountForcardId")
	public void getAccountForcardId() {
		// 根据就诊卡号查就诊卡信息，根据就诊卡信息查账户信息
		OutpatientAccount account = idcardService.queryByIdcardIdOut(id);
		if (account == null) {// 这里防止没有账户退卡的时候金额出现undifine所以做了一个假的结清
			account = new OutpatientAccount();
			account.setAccountBalance(0.0);
		}
		String jsonString = JSONUtils.toJson(account);
		WebUtils.webSendJSON(jsonString);
	}

	
	/**
	 * @Description: 刷身份证时解析省市区县四联动
	 * @Author： zhangkui
	 * @CreateDate： 2017-04-14
	 **/
	@Action(value = "readInfoIdcard", results = { @Result(name = "json", type = "json") })
	public void readInfoIdcard() {
		// 省市县三级联动
		if (StringUtils.isNotEmpty(patientCity)) {
			String parId = patientCity;
			List<District> districtList = districtInnerService.queryDistricttreeSJLDCX(parId);
			String one = "";
			String two = "";
			String three = "";
			String four = "";
			if (districtList != null && districtList.size() > 0) {
				String path = districtList.get(0).getUpperPath();
				if (StringUtils.isNotBlank(path)) {
					String[] pathss = path.split(",");
					if (pathss.length < 3) {
						one = pathss[0];
						two = pathss[1];
						three = parId;
					} else {
						one = pathss[0];
						two = pathss[1];
						three = pathss[2];
						four = parId;
					}
				}
			}
			List<District> districtListone = districtInnerService.queryDistricttreeSJLDCX(one);
			List<District> districtListtwo = districtInnerService.queryDistricttreeSJLDCX(two);
			List<District> districtListthree = districtInnerService.queryDistricttreeSJLDCX(three);
			List<District> districtListfour = districtInnerService.queryDistricttreeSJLDCX(four);
     
			if (districtListone != null && districtListone.size() > 0) {
			 oneCode=districtListone.get(0).getCityCode();
			} else {
				oneCode="";
			}
			if (districtListtwo != null && districtListtwo.size() > 0) {
				twoCode=districtListtwo.get(0).getCityCode();
			} else {
				twoCode="";
			}
			
			if (districtListthree != null && districtListthree.size() > 0) {
				threeCode=districtListthree.get(0).getCityCode();
			} else {
				threeCode="";
			}
			if (districtListfour != null && districtListfour.size() > 0) {
				fourCode=districtListfour.get(0).getCityCode();
			} else {
				fourCode="";
			}
		} 
		
		String rs =oneCode+","+twoCode+","+threeCode+","+fourCode;
		WebUtils.webSendString(rs);
	}

	
	public String getMenuAlias() {
		return menuAlias;
	}

	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}

	public IdcardService getIdcardService() {
		return idcardService;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getRows() {
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}

	public PatientIdcard getIdcard() {
		return idcard;
	}

	public void setIdcard(PatientIdcard idcard) {
		this.idcard = idcard;
	}

	public List<PatientIdcard> getIdcardList() {
		return idcardList;
	}

	public void setIdcardList(List<PatientIdcard> idcardList) {
		this.idcardList = idcardList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getIdcardType() {
		return idcardType;
	}

	public void setIdcardType(String idcardType) {
		this.idcardType = idcardType;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getOneId() {
		return oneId;
	}

	public void setOneId(String oneId) {
		this.oneId = oneId;
	}

	public String getOneName() {
		return oneName;
	}

	public void setOneName(String oneName) {
		this.oneName = oneName;
	}

	public String getTwoId() {
		return twoId;
	}

	public void setTwoId(String twoId) {
		this.twoId = twoId;
	}

	public String getTwoName() {
		return twoName;
	}

	public void setTwoName(String twoName) {
		this.twoName = twoName;
	}

	public String getThreeId() {
		return threeId;
	}

	public void setThreeId(String threeId) {
		this.threeId = threeId;
	}

	public String getThreeName() {
		return threeName;
	}

	public void setThreeName(String threeName) {
		this.threeName = threeName;
	}

	public String getFourId() {
		return fourId;
	}

	public void setFourId(String fourId) {
		this.fourId = fourId;
	}

	public String getFourName() {
		return fourName;
	}

	public void setFourName(String fourName) {
		this.fourName = fourName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getCertificatestype() {
		return certificatestype;
	}

	public void setCertificatestype(String certificatestype) {
		this.certificatestype = certificatestype;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public String getWarriage() {
		return warriage;
	}

	public void setWarriage(String warriage) {
		this.warriage = warriage;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public Map<String, Object> getPager() {
		return pager;
	}

	public void setPager(Map<String, Object> pager) {
		this.pager = pager;
	}


	public String getIdcardNO() {
		return idcardNO;
	}

	public void setIdcardNO(String idcardNO) {
		this.idcardNO = idcardNO;
	}

	public String getIdcardNo() {
		return idcardNo;
	}

	public void setIdcardNo(String idcardNo) {
		this.idcardNo = idcardNo;
	}

	public String getHandBook() {
		return handBook;
	}

	public void setHandBook(String handBook) {
		this.handBook = handBook;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getIdcardId() {
		return idcardId;
	}

	public void setIdcardId(String idcardId) {
		this.idcardId = idcardId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public String getCodeCertificate() {
		return CodeCertificate;
	}

	public void setCodeCertificate(String codeCertificate) {
		CodeCertificate = codeCertificate;
	}

	public String getCertificatesno() {
		return certificatesno;
	}

	public void setCertificatesno(String certificatesno) {
		this.certificatesno = certificatesno;
	}

	public String getPatientCity() {
		return patientCity;
	}

	public void setPatientCity(String patientCity) {
		this.patientCity = patientCity;
	}

	public String getMedicalId() {
		return medicalId;
	}

	public void setMedicalId(String medicalId) {
		this.medicalId = medicalId;
	}

	public String getOneCode() {
		return oneCode;
	}

	public void setOneCode(String oneCode) {
		this.oneCode = oneCode;
	}

	public String getTwoCode() {
		return twoCode;
	}

	public void setTwoCode(String twoCode) {
		this.twoCode = twoCode;
	}

	public String getThreeCode() {
		return threeCode;
	}

	public void setThreeCode(String threeCode) {
		this.threeCode = threeCode;
	}

	public String getFourCode() {
		return fourCode;
	}

	public void setFourCode(String fourCode) {
		this.fourCode = fourCode;
	}

}
