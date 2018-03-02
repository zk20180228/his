package cn.honry.patinent.register.patientInfoCollection.action;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
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

import cn.honry.base.bean.model.District;
import cn.honry.base.bean.model.Patient;
import cn.honry.inner.baseinfo.district.service.DistrictInnerService;
import cn.honry.utils.WebUtils;
import cn.honry.utils.HibernateCascade;
import cn.honry.utils.JSONUtils;
import cn.honry.patinent.register.patientInfoCollection.service.CollectionServcie;
import cn.honry.patinent.register.patientInfoCollection.vo.Collection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opensymphony.xwork2.ActionSupport;


/**  
 *  
 * @className：CollectionAction
 * @Description：  CollectionAction
 * @Author：wj
 * @CreateDate：2015-11-25 下午16:56:31  
 * @Modifier：wj
 * @ModifyDate：2015-11-25 下午16:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
//@Namespace(value = "/register")
@Namespace(value = "/patient/register")
public class CollectionAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	
	@Autowired
	@Qualifier(value = "collectionServcie")
	private CollectionServcie collectionServcie;
	public void setCollectionServcie(CollectionServcie collectionServcie) {
		this.collectionServcie = collectionServcie;
	}
	
	@Autowired
	@Qualifier(value = "districtInnerService")
	private DistrictInnerService districtInnerService;

	public void setDistrictInnerService(
			DistrictInnerService districtInnerService) {
		this.districtInnerService = districtInnerService;
	}
	
	private HttpServletRequest request = ServletActionContext.getRequest();
	private String menuAlias;//栏目别名,在主界面中点击栏目时传到action的参数
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	/**
	 * 患者实体
	 */
	private Patient patient;
	/**
	 * 查询患者的集合
	 */
	private List<Patient> patientList;
	/**
	 * 读卡查询的时候传过来的就诊卡号
	 */
	private String idcardNo;
	/**
	 * 读卡查询的时候传过来的身份证号
	 */
	private String cerNo;
	
	/**
	 * 姓名
	 */
	private String name;
	
	/**
	 * 证件类型
	 */
	private String certificate;
	/**
	 * 证件号码
	 */
	private String certificatesno;
	
	/**  
	 *  
	 * @Description：  跳转list页面
	 * @Author：wujiao
	 * @CreateDate：2015-11-25 下午16:56:31
	 * @Modifier：wujiao
	 * @ModifyDate：2015-11-25 下午16:56:31 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"HZXXBL:function:view","GHHZXXBL:function:view"},logical=Logical.OR)
	@Action(value = "listCollection", results = { @Result(name = "list", location = "/WEB-INF/pages/register/patientInfoCollection/patientInfoCollection.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listCollection() {
		
		return "list";
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
	@Action(value = "queryCollectionById", results = { @Result(name = "json", type = "json") })
	public void queryCollectionById() {
		String idNo = request.getParameter("idNo");
		Patient patient = new Patient();
		if(StringUtils.isNotBlank(idNo)){
			patient.setMedicalrecordId(idNo);
		}
		Collection collection = collectionServcie.getById(idNo);
		Map<String , Object> map = new HashMap<String, Object>();
		map.put("flg", 0);//没有数据默认为0
		// 省市县三级联动
		if (patient!=null) {
			
			String parId = collection.getPatientCity();
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
				collection.setOneId(districtListone.get(0).getId());
				collection.setOneCode(districtListone.get(0).getCityCode());
				collection.setOneName(districtListone.get(0).getCityName());
			} else {
				collection.setOneId("");
				collection.setOneCode("");
				collection.setOneName("");
			}
			if (districtListtwo != null && districtListtwo.size() > 0) {
				collection.setTwoId(districtListtwo.get(0).getId());
				collection.setTwoCode(districtListtwo.get(0).getCityCode());
				collection.setTwoName(districtListtwo.get(0).getCityName());
			} else {
				collection.setTwoId("");
				collection.setTwoCode("");
				collection.setTwoName("");
			}
			if (districtListthree != null && districtListthree.size() > 0) {
				collection.setThreeId(districtListthree.get(0).getId());
				collection.setThreeCode(districtListthree.get(0).getCityCode());
				collection.setThreeName(districtListthree.get(0).getCityName());
			} else {
				collection.setThreeId("");
				collection.setThreeCode("");
				collection.setThreeName("");
			}
			if (districtListfour != null && districtListfour.size() > 0) {
				collection.setFourId(districtListfour.get(0).getId());
				collection.setFourCode(districtListfour.get(0).getCityCode());
				collection.setFourName(districtListfour.get(0).getCityName());
			} else {
				collection.setFourId("");
				collection.setFourCode("");
				collection.setFourName("");
			}
			map.put("flg", 1);
			map.put("collection", collection);
		}
		String json =JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
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
//	@RequiresPermissions(value={"HZXXBL:function:query"})
	@Action(value = "queryCollectionByIdcard", results = { @Result(name = "json", type = "json") })
	public void queryCollectionByIdcard() {
		Collection collection = collectionServcie.getByIdcard(idcardNo);
		Map<String , Object> map = new HashMap<String, Object>();
		map.put("flg", 0);//没有数据默认为0
		// 省市县三级联动
		if (collection!=null) {
			
			String parId = collection.getPatientCity();
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
				collection.setOneId(districtListone.get(0).getId());
				collection.setOneCode(districtListone.get(0).getCityCode());
				collection.setOneName(districtListone.get(0).getCityName());
			} else {
				collection.setOneId("");
				collection.setOneCode("");
				collection.setOneName("");
			}
			if (districtListtwo != null && districtListtwo.size() > 0) {
				collection.setTwoId(districtListtwo.get(0).getId());
				collection.setTwoCode(districtListtwo.get(0).getCityCode());
				collection.setTwoName(districtListtwo.get(0).getCityName());
			} else {
				collection.setTwoId("");
				collection.setTwoCode("");
				collection.setTwoName("");
			}
			if (districtListthree != null && districtListthree.size() > 0) {
				collection.setThreeId(districtListthree.get(0).getId());
				collection.setThreeCode(districtListthree.get(0).getCityCode());
				collection.setThreeName(districtListthree.get(0).getCityName());
			} else {
				collection.setThreeId("");
				collection.setThreeCode("");
				collection.setThreeName("");
			}
			if (districtListfour != null && districtListfour.size() > 0) {
				collection.setFourId(districtListfour.get(0).getId());
				collection.setFourCode(districtListfour.get(0).getCityCode());
				collection.setFourName(districtListfour.get(0).getCityName());
			} else {
				collection.setFourId("");
				collection.setFourCode("");
				collection.setFourName("");
			}
			map.put("flg", 1);
			map.put("collection", collection);
		}
		String menuJson = JSONUtils.toJson(map);
		WebUtils.webSendJSON(menuJson);
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
//	@RequiresPermissions(value={"HZXXBL:function:query"})
	@Action(value = "queryCollectionByCerNo", results = { @Result(name = "json", type = "json") })
	public void queryCollectionByCerNo() {
		Collection collection = collectionServcie.getByCerNo(cerNo);
		Map<String , Object> map = new HashMap<String, Object>();
		map.put("flg", 0);//没有数据默认为0
		// 省市县三级联动
				if (collection!=null) {
					
					String parId = collection.getAddress();
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
						collection.setOneId(districtListone.get(0).getId());
						collection.setOneName(districtListone.get(0).getCityName());
					} else {
						collection.setOneId("");
						collection.setOneName("");
					}
					if (districtListtwo != null && districtListtwo.size() > 0) {
						collection.setTwoId(districtListtwo.get(0).getId());
						collection.setTwoName(districtListtwo.get(0).getCityName());
					} else {
						collection.setTwoId("");
						collection.setTwoName("");
					}
					if (districtListthree != null && districtListthree.size() > 0) {
						collection.setThreeId(districtListthree.get(0).getId());
						collection.setThreeName(districtListthree.get(0).getCityName());
					} else {
						collection.setThreeId("");
						collection.setThreeName("");
					}
					if (districtListfour != null && districtListfour.size() > 0) {
						collection.setFourId(districtListfour.get(0).getId());
						collection.setFourName(districtListfour.get(0).getCityName());
					} else {
						collection.setFourId("");
						collection.setFourName("");
					}
					map.put("flg", 1);
					map.put("collection", collection);
				}
				
		String menuJson = JSONUtils.toJson(map);
		WebUtils.webSendJSON(menuJson);
	}

	/**  
	 *  
	 * @Description：添加&修改
	 *@Author：wujiao
	 * @CreateDate：2015-6-24 上午10:56:35  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-6-24 上午10:56:35   
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "patientInfoCollectionSave", results = { @Result(name = "list", location = "/WEB-INF/pages/register/patientInfoCollection/patientInfoCollection.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String patientInfoCollectionSave() throws Exception {
		PrintWriter out = WebUtils.getResponse().getWriter();
		if(StringUtils.isNotBlank(patient.getId())){
			patient.setPatientName(patient.getPatientName());
		  	if(SecurityUtils.getSubject().isPermitted("HZXXBL:function:edit")||SecurityUtils.getSubject().isPermitted("GHHZXXBL:function:edit")){
		  		collectionServcie.saveOrUpdateCollection(patient);
		  		out.write("success");
		  	}else{
		  		out.write("error");
		  	}
		}
		return "list";
	}
	
	/***
	 * 病历号模糊查询
	 * @Description:
	 * @author  wfj
	 * @date 创建时间：2016年3月18日
	 * @version 1.0
	 * @since
	 */
	@Action(value = "isSearchFrom", results = { @Result(name = "json", type = "json") })
	public void isSearchFrom(){
		Gson gson = new GsonBuilder().registerTypeAdapterFactory(HibernateCascade.FACTORY).create();
		List<Patient> list=collectionServcie.isSearchFrom(request.getParameter("idNo"));
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("resSize", list.size());
		map.put("resData", list);
		try {
			String json=gson.toJson(map);
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(json);
			out.close();
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	@Action(value = "selectDialogURL", results = { @Result(name = "list", location = "/WEB-INF/pages/register/patientInfoCollection/selectDialog.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String selectDialogURL(){
		return "list";
	}
	
	
	/**
	 * @Description:验证患者是否已存在
	 * @Author： zpty
	 * @CreateDate： 2015-12-24
	 * @param @throws Exception
	 * @return void
	 * @version 1.0
	 **/
	@Action(value = "checkIdcardName")
	public void checkIdcardName() throws Exception {
		String result = "ok";
		String flag = collectionServcie.checkIdcardName(name,certificate, certificatesno);
		if (flag == "nameNO") {
			result = "no1";
		}
		WebUtils.webSendString(result);
	}

	
	
	/**
	 * set/get方法
	 */
	
	public List<Patient> getPatientList() {
		return patientList;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCertificate() {
		return certificate;
	}
	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}
	public String getCertificatesno() {
		return certificatesno;
	}
	public void setCertificatesno(String certificatesno) {
		this.certificatesno = certificatesno;
	}
	public CollectionServcie getCollectionServcie() {
		return collectionServcie;
	}
	public void setPatientList(List<Patient> patientList) {
		this.patientList = patientList;
	}
	public Patient getPatient() {
		return patient;
	}
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	public String getIdcardNo() {
		return idcardNo;
	}
	public void setIdcardNo(String idcardNo) {
		this.idcardNo = idcardNo;
	}
	public String getCerNo() {
		return cerNo;
	}
	public void setCerNo(String cerNo) {
		this.cerNo = cerNo;
	}
	
	
	
}
