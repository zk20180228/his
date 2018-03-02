package cn.honry.oa.publicAddressBook.action;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
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

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.PublicAddressBook;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.inner.baseinfo.department.dao.DeptInInterDAO;
import cn.honry.oa.publicAddressBook.service.PublicAddressBookService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.TreeJson;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/oa/publicAddressBook")
public class PublicAddressBookAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private PublicAddressBook publicAddressBook = new PublicAddressBook();
	private String id;//树节点
	private String nodeType;//节点类型
	private String areaCode;//院区
	private String noString;//楼号
	private String floor;//楼层
	private String typeName;//科室类型
	private String deptCode;//科室名
	private String deptType;//科室编码
	private String type;//树类型
	private String menuAlias;
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	/**
	 * 当前页数，用于分页查询
	 */
	private String page;
	
	/**
	 * 分页条数，用于分页查询
	 */
	private String rows;
	private Logger logger=Logger.getLogger(PublicAddressBookAction.class);
	@Autowired
	@Qualifier(value = "publicAddressBookService")
	private PublicAddressBookService publicAddressBookService;
	public PublicAddressBookService getPublicAddressBookService() {
		return publicAddressBookService;
	}
	public void setPublicAddressBookService(
			PublicAddressBookService publicAddressBookService) {
		this.publicAddressBookService = publicAddressBookService;
	}
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	@Autowired
	@Qualifier(value = "deptInInterDAO")
	private DeptInInterDAO deptInInterDAO;
	public void setDeptInInterDAO(DeptInInterDAO deptInInterDAO) {
		this.deptInInterDAO = deptInInterDAO;
	}
	@Autowired
	@Qualifier(value="innerCodeService")
	private CodeInInterService  innerCodeService;
	public void setInnerCodeService(CodeInInterService innerCodeService) {
		this.innerCodeService = innerCodeService;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDeptType() {
		return deptType;
	}
	public void setDeptType(String deptType) {
		this.deptType = deptType;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getFloor() {
		return floor;
	}
	public void setFloor(String floor) {
		this.floor = floor;
	}
	public String getNoString() {
		return noString;
	}
	public void setNoString(String noString) {
		this.noString = noString;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getNodeType() {
		return nodeType;
	}
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public PublicAddressBook getPublicAddressBook() {
		return publicAddressBook;
	}
	public void setPublicAddressBook(PublicAddressBook publicAddressBook) {
		this.publicAddressBook = publicAddressBook;
	}


	/**  
	 * 
	 * OA_公共通讯录
	 * @Author: huzhenguo
	 * @CreateDate: 2017年7月17日 下午1:46:12 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年7月17日 下午1:46:12 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@RequiresPermissions(value={"GGTXL:function:view"})
	@Action(value="publicAddressBookList",results={@Result(name="list",location="/WEB-INF/pages/oa/publicAddressBook/publicAddressBookList.jsp")},interceptorRefs={@InterceptorRef("manageInterceptor")})
	public String publicAddressBookList(){
		return "list";
	}
	//添加
	@RequiresPermissions(value={"GGTXL:function:add"})
	@Action(value = "addPublicAddressBook", results = { @Result(name = "add", location = "/WEB-INF/pages/oa/publicAddressBook/publicAddressBookEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String addPublicAddressBook() throws IOException {
		if (StringUtils.isNotBlank(id)) {
			PublicAddressBook addressBook = publicAddressBookService.get(id);
			if (addressBook==null) {
				nodeType="00";
				publicAddressBook.setNodeType("00");
				publicAddressBook.setParentCode("root");
			}else{
				if ("33".equals(addressBook.getNodeType())) {
					deptType=addressBook.getName();
				}
				publicAddressBook.setParentCode(addressBook.getId());
				int parseInt = Integer.parseInt(addressBook.getNodeType());
				parseInt+=11;
				publicAddressBook.setNodeType(parseInt+"");
				nodeType=parseInt+"";
			}
		}
		return "add";
	}
	//修改
	@RequiresPermissions(value={"GGTXL:function:edit"})
	@Action(value = "editPublicAddressBook", results = { @Result(name = "edit", location = "/WEB-INF/pages/oa/publicAddressBook/publicAddressBookEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String editPublicAddressBook() throws IOException {
		if (StringUtils.isNotBlank(id)) {
			publicAddressBook = publicAddressBookService.get(id);
			nodeType=publicAddressBook.getNodeType();
		}
		return "edit";
	}
	/**  
	 * 
	 * 树
	 * @Author: huzhenguo
	 * @CreateDate: 2017年7月17日 下午1:46:57 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年7月17日 下午1:46:57 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Action(value = "publicBookTree", results = { @Result(name = "json", type = "json") })
	public void publicBookTree(){
		try {
			List<TreeJson> findTree = publicAddressBookService.findTree(type);
			String json = JSONUtils.toJson(findTree);			
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("OA_GGTXL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("OA_GGTXL", "OA_公共通讯录", "2", "0"), e);
		}
	}
	/**  
	 * 
	 * 修改、添加
	 * @Author: huzhenguo
	 * @CreateDate: 2017年7月17日 下午5:11:41 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年7月17日 下午5:11:41 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Action(value = "savePublicAddressBook", results = { @Result(name = "json", type = "json") })
	public void savePublicAddressBook() throws Exception {
		try {
			if ("root".equals(publicAddressBook.getParentCode())) {
				publicAddressBook.setNodeType("00");
			}else{
				//计算科室类型
				PublicAddressBook book = publicAddressBookService.get(publicAddressBook.getParentCode());
				int parseInt = Integer.parseInt(book.getNodeType());
				parseInt+=11;
				publicAddressBook.setNodeType(parseInt+"");
			}
			//修改
			if(StringUtils.isNotBlank(publicAddressBook.getId())){
				if(SecurityUtils.getSubject().isPermitted("GGTXL:function:edit")){
					publicAddressBookService.saveOrupdataBook(publicAddressBook);
					WebUtils.webSendJSON("success");
				}else{
					WebUtils.webSendJSON("error");
				}
			}else{//添加
				if(SecurityUtils.getSubject().isPermitted("GGTXL:function:add")){
					publicAddressBookService.saveOrupdataBook(publicAddressBook);
					WebUtils.webSendJSON("success");
				}else{
					WebUtils.webSendJSON("error");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			WebUtils.webSendJSON("error");
			logger.error("OA_GGTXL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("OA_GGTXL", "OA_公共通讯录", "2", "0"), e);
		}
	}
	/**  
	 * 
	 * 获得栏目的全部工作站
	 * @Author: huzhenguo
	 * @CreateDate: 2017年7月18日 下午2:27:23 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年7月18日 下午2:27:23 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Action(value = "queryVoList", results = { @Result(name = "json", type = "json") })
	public void queryVoList(){
		if ("root".equals(id)) {
			id="";
		}
		List<PublicAddressBook> publicBookVoList = publicAddressBookService.getPublicBookVoList(page,rows,id,nodeType ,areaCode, noString, floor, typeName, deptCode);
		int total = publicAddressBookService.getPublicBookTotal(page, rows, id, nodeType, areaCode, noString, floor, typeName, deptCode);
		Map<String,Object> retMap = new HashMap<String,Object>();
		retMap.put("total", total);
		retMap.put("rows", publicBookVoList);
		String json = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}
	/**  
	 * 
	 * 院区
	 * @Author: huzhenguo
	 * @CreateDate: 2017年7月19日 上午9:09:07 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年7月19日 上午9:09:07 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Action(value = "queryVoAreaList", results = { @Result(name = "json", type = "json") })
	public void queryVoAreaList(){
		List<PublicAddressBook> publicBookVoList = publicAddressBookService.getVoAreaList();
		String json=JSONUtils.toJson(publicBookVoList);
		WebUtils.webSendJSON(json);
	}
	/**  
	 * 
	 * 获得栏目的楼号下拉框
	 * @Author: huzhenguo
	 * @CreateDate: 2017年7月18日 下午2:27:23 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年7月18日 下午2:27:23  
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Action(value = "queryVoNoStringList", results = { @Result(name = "json", type = "json") })
	public void queryVoNoStringList(){
		List<PublicAddressBook> publicBookVoList = publicAddressBookService.getVoNoStringList();
		String json=JSONUtils.toJson(publicBookVoList);
		WebUtils.webSendJSON(json);
	}
	/**  
	 * 
	 * 获得栏目的楼层下拉框
	 * @Author: huzhenguo
	 * @CreateDate: 2017年7月18日 下午2:27:23 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年7月18日 下午2:27:23  
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Action(value = "queryVoFloorList", results = { @Result(name = "json", type = "json") })
	public void queryVoFloorList(){
		List<PublicAddressBook> publicBookVoList = publicAddressBookService.getVoFloorList();
		String json=JSONUtils.toJson(publicBookVoList);
		WebUtils.webSendJSON(json);
	}
	/**  
	 * 
	 * 科室下拉框
	 * @Author: huzhenguo
	 * @CreateDate: 2017年7月19日 上午9:09:07 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年7月19日 上午9:09:07 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Action(value = "queryVoDeptList", results = { @Result(name = "json", type = "json") })
	public void queryVoDeptList(){
		List<PublicAddressBook> publicBookVoList = publicAddressBookService.getVoDeptList();
		String json=JSONUtils.toJson(publicBookVoList);
		WebUtils.webSendJSON(json);
	}
	/**  
	 * 
	 * 类别名称下拉框
	 * @Author: huzhenguo
	 * @CreateDate: 2017年7月19日 上午9:09:07 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年7月19日 上午9:09:07 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Action(value = "queryVoTypeList", results = { @Result(name = "json", type = "json") })
	public void queryVoTypeList(){
		List<PublicAddressBook> publicBookVoList = publicAddressBookService.getVoTypeList();
		String json=JSONUtils.toJson(publicBookVoList);
		WebUtils.webSendJSON(json);
	}
	/**  
	 * 
	 * 删除
	 * @Author: huzhenguo
	 * @CreateDate: 2017年7月19日 上午9:09:07 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年7月19日 上午9:09:07 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@RequiresPermissions(value={"GGTXL:function:delete"})
	@Action(value = "delMenu")
	public void delMenu() throws Exception {
		Map<String,String> retMap = new HashMap<String, String>();
		try{
			publicAddressBookService.delMenu(id);
			retMap.put("resCode", "success");
			retMap.put("resMsg", "删除成功！");
		}catch(Exception e){
			retMap.put("resCode", "error");
			retMap.put("resMsg", "删除失败，请刷新后重试！");
			WebUtils.webSendJSON("error");
			logger.error("OA_GGTXL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("OA_GGTXL", "OA_公共通讯录", "2", "0"), e);
		}
		String json = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}
	/**  
	 * 
	 * 删除工作站
	 * @Author: huzhenguo
	 * @CreateDate: 2017年7月19日 上午9:09:07 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年7月19日 上午9:09:07 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@RequiresPermissions(value={"GGTXL:function:delete"})
	@Action(value = "delWork")
	public void delWork() throws Exception {
		Map<String,String> retMap = new HashMap<String, String>();
		try{
			publicAddressBookService.delWork(id);
			retMap.put("resCode", "success");
			retMap.put("resMsg", "删除成功！");
		}catch(Exception e){
			retMap.put("resCode", "error");
			retMap.put("resMsg", "删除失败，请刷新后重试！");
			WebUtils.webSendJSON("error");
			logger.error("OA_GGTXL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("OA_GGTXL", "OA_公共通讯录", "2", "0"), e);
		}
		String json = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}
	/**  
	 * 
	 * 根据科室类别查所有科室
	 * @Author: huzhenguo
	 * @CreateDate: 2017年7月19日 下午5:15:48 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年7月19日 下午5:15:48 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Action(value = "queryDepartments", results = { @Result(name = "json", type = "json") })
	public void queryDepartments() {
		try {
			deptType=URLDecoder.decode(deptType,"utf-8");
			List<BusinessDictionary> dictionaryList = innerCodeService.getDictionary("depttype");
			for (BusinessDictionary businessDictionary : dictionaryList) {
				if (businessDictionary.getName().equals(deptType)) {
					deptType=businessDictionary.getEncode();
				}
			}
			List<SysDepartment> list = publicAddressBookService.getDept(deptType);
			String json = JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("OA_GGTXL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("OA_GGTXL", "OA_公共通讯录", "2", "0"), e);
		}
	}
	
	/**  
	 * 
	 * 通过id查对象
	 * @Author: huzhenguo
	 * @CreateDate: 2017年7月19日 上午9:09:07 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年7月19日 上午9:09:07 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Action(value = "queryPublicAddressBook", results = { @Result(name = "json", type = "json") })
	public void queryPublicAddressBook(){
		PublicAddressBook addressBook = publicAddressBookService.get(id);
		String json=JSONUtils.toJson(addressBook);
		WebUtils.webSendJSON(json);
	}
}
