package cn.honry.patinent.patinent.action;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.struts2.ServletActionContext;
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

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.District;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.inner.baseinfo.district.service.DistrictInnerService;
import cn.honry.inner.patient.patient.vo.CheckAccountVO;
import cn.honry.inner.patient.patient.vo.PatientIdcardVO;
import cn.honry.patinent.patinent.service.PatinentService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HibernateCascade;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("serial")
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
//@Namespace(value = "/patinent")
@Namespace(value = "/patient/patinent")
public class PatinentAction extends ActionSupport implements
		ModelDriven<Patient> {
	@Override
	public Patient getModel() {
		return patient;
	}
	
	private HttpServletRequest request = ServletActionContext.getRequest();
	@Autowired
	@Qualifier(value = "patinentService")
	private PatinentService patinentService;
	public void setPatinentService(PatinentService patinentService) {
		this.patinentService = patinentService;
	}
	@Autowired
	@Qualifier(value = "innerCodeService")
	private CodeInInterService codeInInterService;
	
	private DistrictInnerService districtInnerService;
	public void setDistrictInnerService(DistrictInnerService districtInnerService) {
		this.districtInnerService = districtInnerService;
	}

	public void setCodeInInterService(CodeInInterService codeInInterService) {
		this.codeInInterService = codeInInterService;
	}

	/**
	 *  栏目别名,在主界面中点击栏目时传到action的参数
	 */
	private String menuAlias;
	/**
	 * 创建患者对象
	 */
	private Patient patient;

	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 性别
	 */
	private String sex;
	/**
	 * 出生日期
	 */
	private String birthday;
	/**
	 * 联系方式
	 */
	private String contact;
	/**
	 * 证件类型
	 */
	private String certificate;
	/**
	 * 证件号码
	 */
	private String number;
	/**
	 * 城市
	 */
	private String patientCitys;
	/**
	 * 具体地址
	 */
	private String address;
	
	/**
	 * 2016年9月23日16:30:49  by GH
	 * 就诊卡号条件
	 */
	private String idcardId;
	
	/**
	 * 2016年9月28日17:40:09  by GH
	 * 病历号
	 */
	private String medicalrecordId;
	/**
	 * 返回JspList页面
	 * 
	 * @date 2015-06-02
	 * @author sgt
	 * @version 1.0
	 * @return
	 */
	@Action(value = "listPatientUrl", results = { @Result(name = "list", location = "/WEB-INF/pages/patient/patient/patientList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listPatientUrl() {
		return "list";
	}

	/**
	 * 查询数据
	 * 
	 * @date 2015-11-17
	 * @author lt
	 * @version 1.0
	 * @return
	 */
	@RequiresPermissions(value = { "JZKGL:function:query" })
	@Action(value = "listPatient")
	public void listPatient() {
		String idcardType = request.getParameter("idcardType");
		String patientName = request.getParameter("patientName");
		PatientIdcardVO vo = new PatientIdcardVO();
		if (StringUtils.isNotBlank(idcardType)) {
			vo.setIdcardType(idcardType);
		}
		if (StringUtils.isNotBlank(patientName)) {
			vo.setPatientName(patientName);
		}
		List<PatientIdcardVO> list = patinentService.listPatient(vo,
				request.getParameter("page"), request.getParameter("rows"));
		int total = patinentService.getPatientCount(vo);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("total", total);
		map.put("rows", list);
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}

	/**
	 * 查询数据,第二版,用NEXT标识
	 * 
	 * @date 2015-12-23
	 * @author zpty
	 * @version 1.0
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@Action(value = "listPatientNext")
	public void listPatientNext() throws UnsupportedEncodingException {
			int flg=0;//判断一个条件都没有的标记
		PatientIdcardVO vo = new PatientIdcardVO();
		if (StringUtils.isNotBlank(idcardId)) {//就诊卡号
			flg=1;
		}
		if (StringUtils.isNotBlank(name)) {//姓名
			vo.setPatientName(name.replaceAll(" ", ""));
			flg=1;
		}
		if (StringUtils.isNotBlank(sex)) {//性别
			vo.setPatientSex(Integer.parseInt(sex.replaceAll(" ", "")));
			flg=1;
		}
		if (StringUtils.isNotBlank(birthday)) {//生日
			vo.setBrithDayCondition(birthday.replaceAll(" ", ""));
			flg=1;
		}
		if (StringUtils.isNotBlank(contact)) {//联系方式
			vo.setPatientPhone(contact.replaceAll(" ", ""));
			flg=1;
		}
		if (StringUtils.isNotBlank(certificate)) {//证件类型
			vo.setPatientCertificatestype(certificate.replaceAll(" ", ""));
			flg=1;
		}
		if (StringUtils.isNotBlank(number)) {//证件号码
			vo.setPatientCertificatesno(number.replaceAll(" ", ""));
			flg=1;
		}
		if (StringUtils.isNotBlank(patientCitys)) {//省市县ID
			vo.setPatientCity(patientCitys);
			flg=1;
		}
		if (StringUtils.isNotBlank(address)) {//具体地址
			vo.setPatientAddress(address);
			flg=1;
		}
		if(flg==1){//如果有查询条件的话
			Gson gson = new GsonBuilder().registerTypeAdapterFactory(HibernateCascade.FACTORY).create();
			List<PatientIdcardVO> listNext = patinentService.listPatientNext(vo,idcardId.replaceAll(" ", ""));
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("resSize", listNext.size());
			
			if (listNext.size() == 1) {
				PatientIdcardVO voNext = listNext.get(0);
				if(voNext.getIdcardCreatetime()!=null){//判空
					voNext.setCreateTimeStr(DateUtils.formatDateY_M_D(voNext.getIdcardCreatetime()));
				}
				if(voNext.getPatientBirthday()!=null){//判空
					voNext.setBirthdayStr(DateUtils.formatDateY_M_D(voNext.getPatientBirthday()));
				}
				String patientId = voNext.getPatientCity();
				String patientName = "";
				if (StringUtils.isNotEmpty(patientId)) {
					// 省市县三级联动
					List<District> districtList = districtInnerService.queryDistricttreeSJLDCX(patientId);
					String one = "";
					String two = "";
					String three = "";
					String four = "";
					if (districtList != null && districtList.size() > 0) {
						String path = districtList.get(0).getUpperPath();
						if(StringUtils.isNotBlank(path)){	
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
					List<District> districtListone = districtInnerService
							.queryDistricttreeSJLDCX(one);
					List<District> districtListtwo = districtInnerService
							.queryDistricttreeSJLDCX(two);
					List<District> districtListthree = districtInnerService
							.queryDistricttreeSJLDCX(three);
					List<District> districtListfour = districtInnerService
							.queryDistricttreeSJLDCX(four);
	
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
				voNext.setPatientCity(patientName);
	
				// zpty20160324将员工号转成员工姓名显示出来
				String operator = voNext.getIdcardOperator();
				SysEmployee operatorEmployee = patinentService
						.findEmpByjobNo(operator);
				String operatorname = "";
				operatorname = operatorEmployee.getName();
				voNext.setIdcardOperator(operatorname);
				//这里判断如果是退卡了,只能显示出患者,卡信息没有,判空操作
				if(voNext.getIdcardId()==null){
					voNext.setIdcardId("");
					voNext.setIdcardStatus(0);
				}
				if(voNext.getIdcardNo()==null){
					voNext.setIdcardNo("");
				}
				if(voNext.getIdcardOperator()==null){
					voNext.setIdcardOperator("");
				}
				if(voNext.getIdcardRemark()==null){
					voNext.setIdcardRemark("");
				}
				
				map.put("resData", voNext);
				
				
//				// 变成json串输出
//				String json = JSONUtils.toJson(voNext,false, DateUtils.DATE_FORMAT, false);
//				WebUtils.webSendJSON(json);
			}else{
				map.put("resData", listNext);
			}
			try {
				String json=gson.toJson(map);
				PrintWriter out = WebUtils.getResponse().getWriter();
				out.write(json);
				out.close();
			}
			catch (Exception ex) {
				System.out.println(ex.getMessage());
			}
		}else{
			WebUtils.webSendString("noSearch");
		}
	}
	
	/**
	 * 多条数据情况下 使用病历号查询
	 * @date 2016年9月28日17:38:43
	 * @author GH
	 * @version 1.0
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@Action(value = "listPatientByMedicalrecord")
	public void listPatientByMedicalrecord() throws UnsupportedEncodingException {
			List<PatientIdcardVO> listNext = patinentService.listPatientByMedicalrecord(medicalrecordId);
			
			PatientIdcardVO voNext = listNext.get(0);
			if(voNext.getIdcardCreatetime()!=null){//判空
				voNext.setCreateTimeStr(DateUtils.formatDateY_M_D(voNext.getIdcardCreatetime()));
			}
			if(voNext.getPatientBirthday()!=null){//判空
				voNext.setBirthdayStr(DateUtils.formatDateY_M_D(voNext.getPatientBirthday()));
			}
			String patientId = voNext.getPatientCity();
			String patientName = "";
			if (StringUtils.isNotEmpty(patientId)) {
				// 省市县三级联动
				List<District> districtList = districtInnerService.queryDistricttreeSJLDCX(patientId);
				String one = "";
				String two = "";
				String three = "";
				String four = "";
				if (districtList != null && districtList.size() > 0) {
					String path = districtList.get(0).getUpperPath();
					if(StringUtils.isNotBlank(path)){	
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
				List<District> districtListone = districtInnerService
						.queryDistricttreeSJLDCX(one);
				List<District> districtListtwo = districtInnerService
						.queryDistricttreeSJLDCX(two);
				List<District> districtListthree = districtInnerService
						.queryDistricttreeSJLDCX(three);
				List<District> districtListfour = districtInnerService
						.queryDistricttreeSJLDCX(four);
				
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
			voNext.setPatientCity(patientName);
			
			// zpty20160324将员工号转成员工姓名显示出来
			String operator = voNext.getIdcardOperator();
			SysEmployee operatorEmployee = patinentService
					.findEmpByjobNo(operator);
			String operatorname = "";
			operatorname = operatorEmployee.getName();
			voNext.setIdcardOperator(operatorname);
			//这里判断如果是退卡了,只能显示出患者,卡信息没有,判空操作
			if(voNext.getIdcardId()==null){
				voNext.setIdcardId("");
				voNext.setIdcardStatus(0);
			}
			if(voNext.getIdcardNo()==null){
				voNext.setIdcardNo("");
			}
			if(voNext.getIdcardOperator()==null){
				voNext.setIdcardOperator("");
			}
			if(voNext.getIdcardRemark()==null){
				voNext.setIdcardRemark("");
			}
		try {
			String json = JSONUtils.toJson(voNext,false, DateUtils.DATE_FORMAT, false);
			WebUtils.webSendJSON(json);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	//弹窗
	@Action(value = "selectDialogURL", results = { @Result(name = "selectDialogURL", location = "/WEB-INF/pages/patient/idcard/selectDialog.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String selectDialogURL(){
		return "selectDialogURL";
	}
	/**
	 * 保存数据
	 * 
	 * @date 2015-06-02
	 * @author sgt
	 * @version 1.0
	 * @return
	 */
	@Action(value = "savePatientURL", results = { @Result(name = "saveUrl", location = "/WEB-INF/pages/patient/patient/patientEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String saveUrl() {
		return "saveUrl";
	}
	/**
	 * 保存患者信息
	 * @date 2015-12-4
	 * @author Tcj
	 */
	@Action(value = "savePatient", results = { @Result(name = "json", type = "json") })
	public void save() {
			RegisterInfo p = patinentService.savePatient(patient);
			String menuJson = JSONUtils.toJson(p);
			WebUtils.webSendJSON(menuJson);
	}

	/**
	 * 修改数据
	 * 
	 * @date 2015-06-02
	 * @author sgt
	 * @version 1.0
	 * @return
	 */
	@Action(value = "editPatientUrl", results = { @Result(name = "listedit", location = "/WEB-INF/pages/patient/patient/patientEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String editUrl() {
		Patient p = patinentService.queryById(patient.getId());
		request.setAttribute("patient", p);
		return "listedit";
	}

	@Action(value = "editPatient")
	public void edit() {
		patinentService.edit(patient);
	}

	/**
	 * 查看数据
	 * 
	 * @date 2015-06-02
	 * @author sgt
	 * @version 1.0
	 * @return
	 */
	@Action(value = "viewPatient", results = { @Result(name = "listview", location = "/WEB-INF/pages/patient/patient/patientView.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String viewPatient() {
		Patient p = patinentService.queryById(patient.getId());
		request.setAttribute("patient", p);
		return "listview";
	}

	/**
	 * 删除数据
	 * 
	 * @date 2015-06-02
	 * @author sgt
	 * @version 1.0
	 * @return
	 */
	@Action(value = "deletePatient")
	public void delete() {
		patinentService.delete(patient);
	}

	/**
	 * @desc 根据id获取某个患者信息返回json
	 * @author lt
	 * @param 参数
	 * @date 下午05:30:46
	 * @version 1.0
	 * @return String
	 * @throws Exception
	 */
	@Action(value = "getPatientOnly", results = { @Result(name = "json", type = "json") })
	public String getPatientOnly(){
		String id = ServletActionContext.getRequest().getParameter("id");
		Patient patient = patinentService.queryById(id);
		BusinessDictionary certificatesType = codeInInterService.getDictionaryByCode("certificate", patient.getPatientCertificatestype());
		BusinessDictionary nationCode = codeInInterService.getDictionaryByCode("nationality", patient.getPatientNationality());
		if (patient != null) {
			patient.setPatientCertificatestype(certificatesType.getName());
			patient.setPatientNationality(nationCode.getName());
		}
		String menuJson = JSONUtils.toJson(patient);
		WebUtils.webSendJSON(menuJson);
		return null;
	}

	
	/**@desc 根据身份证号d获取某个患者信息返回json
	 * @author zhenglin
	 * @param 参数
	 * @date 2015-12-10 13:18pm
	 * @version 1.0
	 * @throws Exception
	 * */
	@Action(value = "getIdcard", results = { @Result(name = "json", type = "json") })
	public void getIdcard() throws Exception {
		String id = ServletActionContext.getRequest().getParameter("id");
		Patient patient = patinentService.queryById(id);
		if(patient.getUnit()==null){
			patient.setUnit("");
		}
		if(patient.getPatientPaykind()==null){
			patient.setPatientPaykind("");
		}
		String menuJson = JSONUtils.toJson(patient);
		WebUtils.webSendJSON(menuJson);
	}

	/**
	 * @Description: 患者信息删除
	 * @Author： lt
	 * @CreateDate： 2015-6-1
	 * @Modifier：lt
	 * @ModifyDate：2015-6-1
	 * @ModifyRmk：
	 * @version 1.0
	 **/
	@RequiresPermissions(value = { "JZKGL:function:delete" })
	@Action(value = "delPatient", results = { @Result(name = "list", location = "/WEB-INF/pages/patient/idcard/idcardList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String delPatient() throws Exception {
		String ids = request.getParameter("id");
		patinentService.removeUnused(ids);
		return "list";
	}

	/**
	 * @Description: 验证患者，住院账户是否已结清及患者是否办理出院
	 * @Author： lt
	 * @CreateDate： 2015-11-12
	 * @version 1.0
	 **/
	@Action(value = "checkAllAccount", results = { @Result(name = "json", type = "json") })
	public void checkAllAccount() throws Exception {
		String retVal = "no";
		String medicalId = ServletActionContext.getRequest().getParameter(
				"medicalId");
		CheckAccountVO vo = patinentService.checkAllAccount(medicalId);
		if (StringUtils.isBlank(vo.getState())) {
			if (vo.getBalance() != 0) {
				retVal = "患者账户余额为：" + vo.getBalance() + "，此患者没有住院记录！";
			} else {
				retVal = "yes";
			}
		} else if (!"B".equals(vo.getState())) {
			retVal = "state";
		} else {
			if (vo.getBalance() != 0 && vo.getIbalance() != 0) {
				retVal = "患者账户余额为：" + vo.getBalance() + "，住院账户余额为："
						+ vo.getIbalance();
			} else if (vo.getBalance() != 0) {
				retVal = "患者账户余额为：" + vo.getBalance();
			} else if (vo.getIbalance() != 0) {
				retVal = "住院账户余额为：" + vo.getIbalance();
			} else {
				retVal = "yes";
			}
		}
		String menuJson = JSONUtils.toJson(retVal);
		WebUtils.webSendJSON(menuJson);
	}
	
	/**  
	 *  
	 * @Description：  合同单位下拉框
	 * @Author：zpty
	 * @CreateDate：2016-4-26 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "queryUnitCombobox", results = { @Result(name = "json", type = "json") })
	public void queryUnitCombobox() {
		List<BusinessContractunit> contractunitList = patinentService.queryUnitCombobox();
		String json=JSONUtils.toJson(contractunitList);
		WebUtils.webSendJSON(json);
	}
	
	
	
	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getPatientCitys() {
		return patientCitys;
	}
	public void setPatientCitys(String patientCitys) {
		this.patientCitys = patientCitys;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getCertificate() {
		return certificate;
	}
	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}
	public String getMenuAlias() {
		return menuAlias;
	}

	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	
	//gh 就诊卡
	public String getIdcardId() {
		return idcardId;
	}

	public void setIdcardId(String idcardId) {
		this.idcardId = idcardId;
	}

	public String getMedicalrecordId() {
		return medicalrecordId;
	}

	public void setMedicalrecordId(String medicalrecordId) {
		this.medicalrecordId = medicalrecordId;
	}

}
