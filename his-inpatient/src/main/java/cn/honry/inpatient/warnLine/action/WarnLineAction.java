package cn.honry.inpatient.warnLine.action;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
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

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inpatient.info.service.InpatientInfoService;
import cn.honry.inpatient.warnLine.service.WarnLineService;
import cn.honry.inpatient.warnLine.vo.WarnLineVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.TreeJson;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
 * ClassName: WarnLineAction.java 
 * @Description: 全院警戒线action
 * @author lyy
 * @date 2016-3-30
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value={@InterceptorRef(value = "manageInterceptor")})
@Namespace(value = "/inpatient/warnLine")
@SuppressWarnings({"all"})
public class WarnLineAction extends ActionSupport{
	private Logger logger=Logger.getLogger(WarnLineAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	
	@Autowired
	@Qualifier(value = "warnLineService")
	private WarnLineService warnLineService;
	public void setWarnLineService(WarnLineService warnLineService) {
		this.warnLineService = warnLineService;
	}
	
	@Autowired
	@Qualifier(value = "inpatientInfoService")
	private InpatientInfoService inpatientInfoService;
	public void setInpatientInfoService(InpatientInfoService inpatientInfoService) {
		this.inpatientInfoService = inpatientInfoService;
	}
	
	@Autowired
	@Qualifier(value="innerCodeService")
	private CodeInInterService innerCodeService;
	public void setInnerCodeService(CodeInInterService innerCodeService) {
		this.innerCodeService = innerCodeService;
	}
	private DeptInInterService deptInInterService;
	@Autowired
	@Qualifier(value="deptInInterService")
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}
	private List<WarnLineVo> warnLineVoList;
	
	/**
	 * 栏目别名,在主界面中点击栏目时传到action的参数
	 */
	private String menuAlias;
	
	/**
	 *患者警戒线虚拟实体 
	 */
	private WarnLineVo warnLineVo;
	
	/**
	 * 起始页数
	 */
	private String page;
	
	/**
	 * 数据列数
	 */
	private String rows;
	
	/**
	 * 住院主表
	 */
	private InpatientInfoNow inpatientInfo;
	
	/**
	 * 查询条件 根据就住院流水号,病历号,就诊卡号,床号查询
	 */
	private String queryName;
	
	/**
	 * 根据病区树的deptId查询患者
	 */
	private String deptId;
	
	/**
	 * 患者警戒线主键id（需要显示科室树的条件也是部门id）
	 */
	private String id;
	
	/**
	 * 患者主键patientId
	 */
	private String patientId;
	
	public WarnLineVo getWarnLineVo() {
		return warnLineVo;
	}
	public void setWarnLineVo(WarnLineVo warnLineVo) {
		this.warnLineVo = warnLineVo;
	}
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
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
	public String getQueryName() {
		return queryName;
	}
	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public InpatientInfoNow getInpatientInfo() {
		return inpatientInfo;
	}
	public void setInpatientInfo(InpatientInfoNow inpatientInfo) {
		this.inpatientInfo = inpatientInfo;
	}
	
	/**
	 * 跳转到警戒线list页面
	 * @Description:获取list页面
	 * @Author：  kjh
	 * @CreateDate： 2015-6-30
	 * @return String  
	 * @version 1.0
	**/
	@RequiresPermissions(value={"JJXSZ:function:view"})
	@Action(value="warnLineList",results={@Result(name="list",location = "/WEB-INF/pages/inpatient/warnLine/warnLineList.jsp")}, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String warnLineList()throws Exception{
		return "list";
	}
	
	/**
	 * @Description:患者警戒线查询列表
	 * @Author：  kjh
	 * @CreateDate： 2015-6-30
	 * @Modifier：lyy
	 * @ModifyDate：2015-12-2 上午10:26:39 
	 * @param    queryName 查询条件  deptId 病区Id
	 * @return void  
	 * @version 1.0
	**/
	@Action(value = "queryWarnLine")
	public void queryWarnLine(){
		try {
			warnLineVo=new WarnLineVo();
			if(StringUtils.isNotBlank(deptId)){
				deptId=deptId.trim();
			}
			if(StringUtils.isNotBlank(queryName)){
				queryName=queryName.trim();
			}
			warnLineVo.setNationCode(deptId);
			warnLineVo.setIdcardNo(queryName);
			warnLineVo.setInpatientNo(queryName);
			warnLineVo.setBedId(queryName);
			long startTime=new Date().getTime();
			int total = warnLineService.getTotalCount(warnLineVo);	
			System.out.println("total="+total+" 时间="+(new Date().getTime()-startTime)*1.0D/1000);
			startTime=new Date().getTime();
			warnLineVoList = warnLineService.getWarnLine(warnLineVo,page,rows);
			for (int i = 0; i < warnLineVoList.size(); i++) {
				if(i==0){
					warnLineVoList.get(i).setOrderId(Integer.parseInt(page)*Integer.parseInt(rows)-Integer.parseInt(rows)+1);
				}else{
					warnLineVoList.get(i).setOrderId(warnLineVoList.get(i-1).getOrderId()+1);
				}
			}
			Map<String, Object> outmap=new HashMap<String,Object>();
			outmap.put("total", total);
			outmap.put("rows", warnLineVoList);
			String json=JSONUtils.toJson(outmap);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYGL_JJXSZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_JJXSZ", "住院管理_警戒线设置", "2", "0"), e);
		}
	}
	
	/**  
	 *  
	 * @Description：住院分类下的护士站的科室树
	 * @Author：kjh
	 * @CreateDate：2015-6-30  
	 * @Modifier：lyy
	 * @ModifyDate：2015-12-1 下午04:26:39
	 * @return void 
	 * @version 1.0
	 *
	 */
	@Action(value = "treeNurseStation")
	public void treeNurseStation() {
		try {
			List<TreeJson> treeDepar =  warnLineService.queryTreeNurse(id);
			String json=JSONUtils.toJson(treeDepar);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYGL_JJXSZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_JJXSZ", "住院管理_警戒线设置", "2", "0"), e);
		}
		
	}
	
	/**  
	 *  
	 * @Description：跳转到设置警戒线的页面
	 * @Author：kjh
	 * @CreateDate：2015-7-01 
	 * @Modifier：lyy
	 * @ModifyDate：2016-3-30 下午04:26:39
	 * @return String 
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"JJXSZ:function:set"})
	@Action(value = "setWarnLineUrl", results = { @Result(name = "warnLineUrl", location = "/WEB-INF/pages/inpatient/warnLine/setWarnLine.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String setWarnLineUrl(){
		inpatientInfo=inpatientInfoService.get(patientId);
		return "warnLineUrl";
	}
	
	/**  
	 *  
	 * @Description：保存设置的警戒线
	 * @Author：kjh
	 * @CreateDate：2015-7-01 
	 * @Modifier：lyy
	 * @ModifyDate：2016-3-30 下午04:26:39
	 * @return void 
	 * @version 1.0
	 */
	@RequiresPermissions(value={"JJXSZ:function:save"})
	@Action(value = "saveWarnLine")
	public void saveWarnLine(){
		String retVal="no";
		try {
			inpatientInfoService.saveWarnLineVo(warnLineVo);
			retVal="yes";
		} catch (Exception e) {
			retVal="no";
			WebUtils.webSendJSON("error");
			logger.error("ZYGL_JJXSZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_JJXSZ", "住院管理_警戒线设置", "2", "0"), e);
		}
		WebUtils.webSendString(retVal);
	}
	
	/**
	 * 
	 * @Description 证件类型渲染
	 * @author  lyy
	 * @createDate： 2016年7月25日 上午10:29:08 
	 * @modifier lyy
	 * @modifyDate：2016年7月25日 上午10:29:08
	 * @param：  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value="likeCertificate")
	public void likeCertificate(){
		List<BusinessDictionary> dicCertificate=innerCodeService.getDictionary("certificate");
		String json=JSONUtils.toJson(dicCertificate);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * 
	 * @Description 部门选人问题
	 * @author  lyy
	 * @createDate： 2016年7月25日 上午10:29:12 
	 * @modifier lyy
	 * @modifyDate：2016年7月25日 上午10:29:12
	 * @param：  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value="likeDept")
	public void likeDept(){
		List<SysDepartment> dept=deptInInterService.queryAllDept();
		Map<String,String> map=new HashMap<String,String>();
		for (SysDepartment sysDepartment : dept) {
			map.put(sysDepartment.getDeptCode(), sysDepartment.getDeptName());
		}
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * 
	 * @Description 医疗类别渲染
	 * @author  lyy
	 * @createDate： 2016年7月25日 上午11:04:15 
	 * @modifier lyy
	 * @modifyDate：2016年7月25日 上午11:04:15
	 * @param：  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value="likeMedicalType")
	public void likeMedicalType(){
		List<BusinessDictionary> dicCertificate=innerCodeService.getDictionary("medicalType");
		String json=JSONUtils.toJson(dicCertificate);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 *  
	 * @Description：   查询信息得到科室集合
	 * @throws UnsupportedEncodingException 
	 * @Author：kjh
	 * @CreateDate：2016-1-7  
	 * @version 1.0
	 *
	 */
	@Action(value = "queryDeptLsit")
	public void queryDeptLsit() {
	    try {
			List<SysDepartment> treeDeptLsit=warnLineService.getDeptName(queryName);
			WebUtils.webSendJSON(JSONUtils.toJson(treeDeptLsit, "yyyy-MM-dd HH:mm:ss"));
		} catch (Exception e) {
			logger.error("ZYGL_JJXSZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_JJXSZ", "住院管理_警戒线设置", "2", "0"), e);
		}
	}
}
