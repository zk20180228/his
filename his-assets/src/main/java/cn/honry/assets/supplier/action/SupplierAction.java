package cn.honry.assets.supplier.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
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

import cn.honry.assets.supplier.service.SupplierService;
import cn.honry.base.bean.model.AssetsSupplier;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
/**
 * @Description 供应商信息管理action
 * @author  zpty
 * @createDate： 2017年11月14日 下午1:39:09 
 * @param：  
 * @modifyRmk：  
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/assets/supplier")
public class SupplierAction extends ActionSupport implements ModelDriven<AssetsSupplier>{

	private static final long serialVersionUID = 1L;

	@Override
	public AssetsSupplier getModel() {
		return supplier;
	}
	private String menuAlias;
	
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	private SupplierService supplierService;
	private AssetsSupplier supplier  = new AssetsSupplier();
	private HttpServletRequest request = ServletActionContext.getRequest();
	
	public SupplierService getSupplierService() {
		return supplierService;
	}
	@Autowired
	@Qualifier(value="supplierService")
	public void setSupplierService(SupplierService supplierService) {
		this.supplierService = supplierService;
	}
	
	private Logger logger=Logger.getLogger(SupplierAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;
	}
	
	/**
	 * 查询数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	@RequiresPermissions(value={"GYSXXGL:function:view"})
	@Action(value = "queryAssetsSupplierUrl", results = { @Result(name = "list", location = "/WEB-INF/pages/assets/supplier/supplierList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String queryAssetsSupplierUrl(){
		return "list";
	}
	@Action(value = "queryAssetsSupplier",results={@Result(name="json",type="json")})
	public void queryAssetsSupplier(){
		List<AssetsSupplier> list = supplierService.querySupplier(supplier);
		int total = supplierService.getSupplierCount(supplier);
		Map<Object, Object> map = new HashMap<Object,Object>();
		map.put("total", total);
		map.put("rows", list);
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}

	/**
	 *  
	 * @Description：增加数据  
	 * @author zpty
	 * @date 2017-11-14
	 * @ModifyRmk：
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"GYSXXGL:function:add"})
	@Action(value = "saveSupplierUrl", results = { @Result(name = "save", location = "/WEB-INF/pages/assets/supplier/supplierEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String saveOrupdateSupplierUrl(){
		if(StringUtils.isNotBlank(supplier.getId())){
			request.setAttribute("supplier", supplierService.get(supplier.getId()));
		}
		return "save";
	}
	/**
	 *  
	 * @Description：修改数据  
	 * @author zpty
	 * @date 2017-11-14
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"GYSXXGL:function:edit"})
	@Action(value = "updateSupplierUrl", results = { @Result(name = "update", location = "/WEB-INF/pages/assets/supplier/supplierEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String updateSupplierUrl(){
		if(StringUtils.isNotBlank(supplier.getId())){
			request.setAttribute("supplier", supplierService.get(supplier.getId()));
		}
		return "update";
	}
	@Action(value = "saveOrupdateSupplier")
	public void saveOrupdateSupplier(){
		try{
			AssetsSupplier supplierSave=new AssetsSupplier();
			if(StringUtils.isNotBlank(supplier.getId())){
				supplierSave = supplierService.get(supplier.getId());//先用ID查出该数据
			}
			supplierSave.setCode(supplier.getCode());//公司编码
			supplierSave.setName(supplier.getName());//公司名称
			supplierSave.setLegal(supplier.getLegal());//公司法人
			supplierSave.setPhone(supplier.getPhone());//公司电话
			supplierSave.setAddress(supplier.getAddress());//公司地址
			supplierSave.setTelautogram(supplier.getTelautogram());//公司传真
			supplierSave.setMail(supplier.getMail());//公司邮箱
			supplierSave.setBankName(supplier.getBankName());//开户银行
			supplierSave.setBankAcco(supplier.getBankAcco());//开户账号
			supplierSave.setLinkMan(supplier.getLinkMan());//联系人
			supplierSave.setLinkPhone(supplier.getLinkPhone());//联系电话
			supplierService.saveOrupdata(supplierSave);
			WebUtils.webSendString("success");
		}catch(Exception e){
			WebUtils.webSendJSON("error");
			logger.error("GYSXXGL_XG", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GYSXXGL_XG", "供应商信息管理_修改", "2", "0"), e);
		}
	}
	/**
	 * 删除数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	@RequiresPermissions(value={"GYSXXGL:function:delete"})
	@Action(value = "deleteSupplier")
	public void deleteSupplier(){
		Map<String, String> retMap=new HashMap<String, String>();
		try {
			supplierService.delete(supplier);
			retMap.put("resCode", "success");
			retMap.put("resMsg", "删除成功");
		} catch (Exception e) {
			retMap.put("resCode", "error");
			retMap.put("resMsg", "删除失败");
			logger.error("GYSXXGL_XG", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GYSXXGL_XG", "供应商信息管理_修改", "2", "0"), e);
		}
		WebUtils.webSendJSON(JSONUtils.toJson(retMap));
	}
	/**
	 *  
	 * @Description：  查看数据
	 * @author zpty
	 * @date 2017-11-14
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"GYSXXGL:function:query"})
	@Action(value = "viewAssetsSupplier", results = { @Result(name = "view", location = "/WEB-INF/pages/assets/supplier/supplierView.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String viewAssetsSupplier(){
		request.setAttribute("supplier", supplierService.get(supplier.getId()));
		return "view";
	}
	/**
	 * @Description:返回供应商信息下拉列表
	 * @author zpty
	 * @date 2017-11-14
	 * @param @throws Exception   
	 * @return void  
	 * @version 1.0
	**/
	@Action(value = "querySupplierList",results={@Result(name="json",type="json")})
	public void querySupplierList() throws Exception{
		List<AssetsSupplier> list = supplierService.findAll();
		String json = JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * @Description:返回供应商信息下拉列表
	 * @author zpty
	 * @date 2017-11-14
	 * @param @throws Exception   
	 * @return void  
	 * @version 1.0
	**/
	@Action(value = "querySupplierMapList",results={@Result(name="json",type="json")})
	public void querySupplierMapList() throws Exception{
		List<AssetsSupplier> list = supplierService.findAll();
		String json = JSONUtils.toExposeJson(list, false, null, "code","name");
		WebUtils.webSendJSON(json);
	}
	
	public AssetsSupplier getSupplier() {
		return supplier;
	}
	public void setSupplier(AssetsSupplier supplier) {
		supplier.setDel_flg(0);
		supplier.setStop_flg(0);
		this.supplier = supplier;
	}
	

	/**
	 * 查出所有数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	@Action(value = "findAllSupplier")
	public String findAllSupplier(){
		List<AssetsSupplier> AssetsSupplierList=supplierService.findAll();
		String json = JSONUtils.toExposeJson(AssetsSupplierList, false, null, "code","name");
		WebUtils.webSendJSON(json);
		return null;
	}

	/***
	 * 操作时进行数据唯一验证
	 * @Title: verification 
	 * @author zpty
	 * @date 2017-11-14
	 * @return void
	 * @version 1.0
	 */
	@Action(value = "verification")
	public void verification(){
		String data = supplierService.verification(supplier);
		String json = JSONUtils.toJson(data);
		WebUtils.webSendJSON(json);
	}
}
