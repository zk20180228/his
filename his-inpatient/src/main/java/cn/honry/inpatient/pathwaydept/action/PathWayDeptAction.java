package cn.honry.inpatient.pathwaydept.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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

import com.opensymphony.xwork2.ActionSupport;

import cn.honry.base.bean.model.CpDept;
import cn.honry.base.bean.model.CpWay;
import cn.honry.base.bean.model.Hospital;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inpatient.clinicalPathway.service.ClinicalPathwayService;
import cn.honry.inpatient.pathwaydept.service.PathWayService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.TreeJson;
import cn.honry.utils.WebUtils;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/inpatient/pathWay")
public class PathWayDeptAction extends ActionSupport  {
	
	@Autowired
	@Qualifier("pathWayService")
	private PathWayService pathWayService;
	@Autowired
	@Qualifier("deptInInterService")
	private DeptInInterService deptInInterService;
	@Autowired
	@Qualifier(value = "clinicalPathwayService")
	private ClinicalPathwayService clinicalPathwayService;
	private String cpID;
	private CpDept info;
	private String page;
	private String rows;
	private String name;
	private String deptCode;
	
	
	
	
	@Action(value = "tolist", results = { @Result(name = "list", location = "/WEB-INF/pages/inpatient/pathwaydept/pathWayDeptList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String tolist(){
		return "list";
	}
	@Action(value = "toAddOrEdit", results = { @Result(name = "add", location = "/WEB-INF/pages/inpatient/pathwaydept/pathWayDeptEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toAddOrEdit(){
		if(StringUtils.isNotBlank(cpID)){
			info = pathWayService.getByid(cpID);
			SysDepartment department = deptInInterService.getDeptCode(info.getDeptCode());
			Hospital hospitalId = department.getHospitalId();
			if(hospitalId!=null){
				info.setHospitalidName(hospitalId.getName());
			}
			info.setAreaCodeName(department.getAreaName());
		}else{
			info = new CpDept();
			info.setDeptCode(deptCode);
			SysDepartment department = deptInInterService.getDeptCode(deptCode);
			info.setDeptName(department.getDeptName());
			info.setInputCode(department.getDeptPinyin());
			info.setInputCodeWB(department.getDeptWb());
			info.setCustomCode(department.getDeptInputcode());
		}
		return "add";
	}
	@Action("getList")
	public void getList(){
		Map<String,Object> map = new HashMap<String,Object>();
		List<CpDept> list = pathWayService.getcpDept(page, rows, deptCode, name);
//		for (CpDept cpDept : list) {
//			CpWay cpWayChoose = clinicalPathwayService.findCpWayById(cpDept.getCpID());
//			cpDept.setCpWayName(cpWayChoose.getCpName());
//		}
		int total = pathWayService.getcpDeptTotal(deptCode, name);
		map.put("rows", list);
		map.put("total", total);
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}
	@Action("save")
	public void save(){
		Map<String,String> map = new HashMap<String, String>();
		try {
			pathWayService.saveOrUpdateCpDept(info);
			map.put("resCode", "success");
			map.put("resMsg", "操作成功!");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("resCode", "error");
			map.put("resMsg", "系统异常："+e.getLocalizedMessage());
		}
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}
	@Action("getCpWay")
	public void getCpWay(){
		List<CpWay> cpWay = pathWayService.getCpWay();
		WebUtils.webSendJSON(JSONUtils.toJson(cpWay));
	}
	@Action("getInpatientDeptTree")
	public void getInpatientDeptTree(){
		String json;
		try {
			List<TreeJson> list = deptInInterService.QueryTreeDepartmen(false, "I",null,null);
			json = JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
		  	e.printStackTrace();
		}
	}
	
	
	public String getCpID() {
		return cpID;
	}
	public void setCpID(String cpID) {
		this.cpID = cpID;
	}
	public CpDept getInfo() {
		return info;
	}
	public void setInfo(CpDept info) {
		this.info = info;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	
}
