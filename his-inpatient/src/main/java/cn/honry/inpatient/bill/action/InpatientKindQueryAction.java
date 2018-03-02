package cn.honry.inpatient.bill.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import cn.honry.base.bean.model.DrugBillclass;
import cn.honry.base.bean.model.DrugBilllist;
import cn.honry.base.bean.model.InpatientKind;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.inpatient.bill.service.InpatientKindQueryService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/inpatient/billInpatientKind")
public class InpatientKindQueryAction extends ActionSupport implements ModelDriven<InpatientKind>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 医嘱类别
	 */
	private InpatientKind inpatientKind=new InpatientKind();
	
	public InpatientKind getInpatientKind() {
		return inpatientKind;
	}
	public void setInpatientKind(InpatientKind inpatientKind) {
		this.inpatientKind = inpatientKind;
	}
	@Override
	public InpatientKind getModel() {
		return inpatientKind;
	}
	@Autowired
	private InpatientKindQueryService inpatientKindQueryService;	
	
	/**
	 * 医嘱类别编辑列表
	 */
		private List<InpatientKind> inpatientKindList;
		String inpatientkind="inpatientkind";
		/**
		 * 医嘱类别 Json字符串
		 */
		private String billJsonYzsx;
		/**
		 * 药品类别id Json字符串
		 */
		private String billJsonYplb;
		/**
		 * 药品性质id Json字符串
		 */
		private String billJsonYpxz;
		/**
		 * 药品药剂id Json字符串
		 */
		private String billJsonYpyj;
		/**
		 * 药品用法id Json字符串
		 */
		private String billJsonYpyf;
		
		public String getBillJsonYzsx() {
			return billJsonYzsx;
		}
		public void setBillJsonYzsx(String billJsonYzsx) {
			this.billJsonYzsx = billJsonYzsx;
		}
		public String getBillJsonYplb() {
			return billJsonYplb;
		}
		public void setBillJsonYplb(String billJsonYplb) {
			this.billJsonYplb = billJsonYplb;
		}
		public String getBillJsonYpxz() {
			return billJsonYpxz;
		}
		public void setBillJsonYpxz(String billJsonYpxz) {
			this.billJsonYpxz = billJsonYpxz;
		}
		public String getBillJsonYpyj() {
			return billJsonYpyj;
		}
		public void setBillJsonYpyj(String billJsonYpyj) {
			this.billJsonYpyj = billJsonYpyj;
		}
		public String getBillJsonYpyf() {
			return billJsonYpyf;
		}
		public void setBillJsonYpyf(String billJsonYpyf) {
			this.billJsonYpyf = billJsonYpyf;
		}
		
		/***
		 * 公共编码资料service实现层
		 */
		@Autowired
		@Qualifier(value = "innerCodeService")
		private CodeInInterService innerCodeService;
	    public void setInnerCodeService(CodeInInterService innerCodeService) {
			this.innerCodeService = innerCodeService;
		}
		
		/**
		 * 查询医嘱类别编辑信息
		 * @Description：
		 * @author  dh
		 * @createDate： 2016年4月13日 上午8:48:30 
		 * @modifier dh
		 * @modifyDate：2016年4月13日 上午8:48:30
		 * @param：  
		 * @return：
		 * @modifyRmk：  
		 * @version 1.0
		 */
		@Action(value="queryInpatientKind",results={@Result(name="json",type="json")})
		public void queryInpatientKind() {
			int total=inpatientKindQueryService.getTotal(inpatientKind);
			inpatientKindList=inpatientKindQueryService.searchOrdercategoryList(inpatientKind);
			java.util.Map<String, Object> map=new HashMap<String, Object>();
			map.put("total", total);
			map.put("rows", inpatientKindList);
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);

		}
		
		/**
		 * 医嘱类别信息查看
		 * @Description：
		 * @author  dh
		 * @createDate： 2016年4月13日 上午8:48:30 
		 * @modifier dh
		 * @modifyDate：2016年4月13日 上午8:48:30
		 * @param：  
		 * @return：
		 * @modifyRmk：  
		 * @version 1.0
		 */
		@Action(value = "viewInpatientKind", results = { @Result(name = "view", location = "/WEB-INF/pages/code/codeOrdercategory/codeOrdercategoryView.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
		public String viewInpatientKind() throws Exception {
			inpatientKind=inpatientKindQueryService.get(inpatientKind.getId());
			ServletActionContext.getRequest().setAttribute("ordercategory",inpatientKind);
			return "view";
		}
		/**
		 * 医嘱类别查询(combobox下拉可以搜索)
		 * @Description：
		 * @author  dh
		 * @createDate： 2016年4月13日 上午8:48:30 
		 * @modifier dh
		 * @modifyDate：2016年4月13日 上午8:48:30
		 * @param：  
		 * @return：
		 * @modifyRmk：  
		 * @version 1.0
		 */
		@Action(value="likeInpatientKind")
		public void likeInpatientKind(){
			List<InpatientKind> codeinpatientKindList = inpatientKindQueryService.likeSearch(inpatientKind.getId());
			String json=JSONUtils.toJson(codeinpatientKindList);
			WebUtils.webSendJSON(json);
		}
		/**
		 * 药品用法信息查看
		 * @Description：
		 * @author  dh
		 * @createDate： 2016年4月13日 上午8:48:30 
		 * @modifier dh
		 * @modifyDate：2016年4月13日 上午8:48:30
		 * @param：  
		 * @return：
		 * @modifyRmk：  
		 * @version 1.0
		 */
		@Action(value = "viewUseage", interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
		public void viewUseage() throws Exception {
			List<BusinessDictionary> UseageList = inpatientKindQueryService.likeSearch("useage", codeyf);
			String json = JSONUtils.toJson(UseageList);
			WebUtils.webSendJSON(json);
		}
		/**
		 * 药品类别信息查看
		 * @Description：
		 * @author  dh
		 * @createDate： 2016年4月13日 上午8:48:30 
		 * @modifier dh
		 * @modifyDate：2016年4月13日 上午8:48:30
		 * @param：  
		 * @return：
		 * @modifyRmk：  
		 * @version 1.0
		 */
		@Action(value = "viewDrugtype", interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
		public void viewDrugtype() throws Exception {
			List<BusinessDictionary> DrugtypeList = inpatientKindQueryService.likeSearch("drugType", codeyplb);
			String json = JSONUtils.toJson(DrugtypeList);
			WebUtils.webSendJSON(json);
		}
		/**
		 * 药品性质信息查看
		 * @Description：
		 * @author  dh
		 * @createDate： 2016年4月13日 上午8:48:30 
		 * @modifier dh
		 * @modifyDate：2016年4月13日 上午8:48:30
		 * @param：  
		 * @return：
		 * @modifyRmk：  
		 * @version 1.0
		 */
		@Action(value = "viewDrugproperties", interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
		public void viewDrugproperties() throws Exception {
			List<BusinessDictionary> DrugpropertiesList = inpatientKindQueryService.likeSearch("drugProperties", codeypsz);
			String json = JSONUtils.toJson(DrugpropertiesList);
			WebUtils.webSendJSON(json);
		}
		/**
		 * 药品药剂信息查看
		 * @Description：
		 * @author  dh
		 * @createDate： 2016年4月13日 上午8:48:30 
		 * @modifier dh
		 * @modifyDate：2016年4月13日 上午8:48:30
		 * @param：  
		 * @return：
		 * @modifyRmk：  
		 * @version 1.0
		 */
		@Action(value = "viewDosageform", interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
		public void viewDosageform() throws Exception {
			List<BusinessDictionary> DrugpropertiesList1 = inpatientKindQueryService.likeSearch("dosageForm", codeypyj);
			String json = JSONUtils.toJson(DrugpropertiesList1);
			WebUtils.webSendJSON(json);
		}
		/**
		 * 医嘱类别信息查看
		 * @Description：
		 * @author  dh
		 * @createDate： 2016年4月13日 上午8:48:30 
		 * @modifier dh
		 * @modifyDate：2016年4月13日 上午8:48:30
		 * @param：  
		 * @return：
		 * @modifyRmk：  
		 * @version 1.0
		 */
		@Action(value = "viewKind", interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
		public void viewKind() throws Exception {
			List<InpatientKind> KindList = new ArrayList<InpatientKind>();
			KindList=inpatientKindQueryService.getKind(codeyzlb);
			String json = JSONUtils.toJson(KindList);
			WebUtils.webSendJSON(json);
		}
		/**
		 * 根据摆药分类id查询摆药分类明细信息
		 * @Description：
		 * @author  dh
		 * @createDate： 2016年4月13日 上午8:48:30 
		 * @modifier dh
		 * @modifyDate：2016年4月13日 上午8:48:30
		 * @param：  
		 * @return：
		 * @modifyRmk：  
		 * @version 1.0
		 */
		@Action(value = "viewDrugBilllist", interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
		public void viewDrugBilllist() throws Exception {
			List<DrugBilllist> DrugBilllist = new ArrayList<DrugBilllist>();
			DrugBilllist=inpatientKindQueryService.queryBilllist(BillclassId);
			String json = JSONUtils.toJson(DrugBilllist);
			WebUtils.webSendJSON(json);
		}
		/**
		 * 根据摆药分类id查询摆药分类明细信息的每个类型的数据
		 * @Description：
		 * @author  dh
		 * @createDate： 2016年4月13日 上午8:48:30 
		 * @modifier dh
		 * @modifyDate：2016年4月13日 上午8:48:30
		 * @param：  
		 * @return：
		 * @modifyRmk：  
		 * @version 1.0
		 */
		@Action(value = "drugBilllistByclassId", interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
		public void drugBilllistByclassId() throws Exception {
			List<DrugBilllist> DrugBilllist1 = new ArrayList<DrugBilllist>();
			List<DrugBilllist> DrugBilllist2 = new ArrayList<DrugBilllist>();
			List<DrugBilllist> DrugBilllist3 = new ArrayList<DrugBilllist>();
			List<DrugBilllist> DrugBilllist4 = new ArrayList<DrugBilllist>();
			List<DrugBilllist> DrugBilllist5 = new ArrayList<DrugBilllist>();
			
			DrugBilllist1=inpatientKindQueryService.queryTypeCode(BillclassId);//医嘱类型  
			DrugBilllist2=inpatientKindQueryService.queryDrugType(BillclassId);//药品类型
			DrugBilllist3=inpatientKindQueryService.queryUsageCode(BillclassId);//药品用法
			DrugBilllist4=inpatientKindQueryService.queryDoseModelCode(BillclassId);//药品药剂
			DrugBilllist5=inpatientKindQueryService.queryDrugQuality(BillclassId);//药品性质
			Map<String, Object> outmap=new HashMap<String, Object>();
			outmap.put("typeCode", DrugBilllist1);
			outmap.put("drugType", DrugBilllist2);
			outmap.put("usageCode", DrugBilllist3);
			outmap.put("doseModelCode", DrugBilllist4);
			outmap.put("drugQuality", DrugBilllist5);
			String json=JSONUtils.toJson(outmap);
			WebUtils.webSendJSON(json);
		}
		/**
		 * 根据摆药分类id查询摆药信息
		 * @Description：
		 * @author  dh
		 * @createDate： 2016年4月13日 上午8:48:30 
		 * @modifier dh
		 * @modifyDate：2016年4月13日 上午8:48:30
		 * @param：  
		 * @return：
		 * @modifyRmk：  
		 * @version 1.0
		 */
		@Action(value = "viewBilllist", interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
		public void viewBilllist() throws Exception {
			List<DrugBillclass> drugBillclasslist = new ArrayList<DrugBillclass>();
			if("".equals(BillclassId)){
				String json = JSONUtils.toJson(drugBillclasslist);
				WebUtils.webSendJSON(json);
			}else{
				drugBillclasslist=inpatientKindQueryService.queryBillclass(BillclassId);
				String json = JSONUtils.toJson(drugBillclasslist);
				WebUtils.webSendJSON(json);
			}
		}
		/**
		 * 修改摆药
		 * @Description：
		 * @author  dh
		 * @createDate： 2016年4月13日 上午8:48:30 
		 * @modifier dh
		 * @modifyDate：2016年4月13日 上午8:48:30
		 * @param：  
		 * @return：
		 * @modifyRmk：  
		 * @version 1.0
		 */
		@Action(value="updateBillClassInfo")
		public void updateBillClassInfo () throws Exception{
			Map<String,String> parameterMap=new HashMap<String,String>();
			parameterMap.put("yzlbid", billJsonYzsx);//医嘱类别id
			parameterMap.put("yplbid", billJsonYplb);//药品类别id 
			parameterMap.put("ypxzid", billJsonYpxz);//药品性质id
			parameterMap.put("ypyjid", billJsonYpyj);//药品药剂id
			parameterMap.put("ypyfid", billJsonYpyf);//药品用法id
			String result="";
			try{
				inpatientKindQueryService.saveOrUpdate(parameterMap,billJson);
				result="success";
			}catch(Exception e){
				result="error";
			}
			WebUtils.webSendString(result);
		}
		/**
		 * 摆药分类字符串
		 */
		private String billJson;
		public String getBillJson() {
			return billJson;
		}
		public void setBillJson(String billJson) {
			this.billJson = billJson;
		}
		/**
		 * 摆药分类id
		 */
		private String BillclassId;
		public String getBillclassId() {
			return BillclassId;
		}
		public void setBillclassId(String billclassId) {
			BillclassId = billclassId;
		}
		/**
		 * 药品用法code
		 */
		private String codeyf;
		public String getCodeyf() {
			return codeyf;
		}
		public void setCodeyf(String codeyf) {
			this.codeyf = codeyf;
		}
		/**
		 * 医嘱类别code
		 */
		private String codeyzlb;
		public String getCodeyzlb() {
			return codeyzlb;
		}
		public void setCodeyzlb(String codeyzlb) {
			this.codeyzlb = codeyzlb;
		}
		/**
		 * 药品用法code
		 */
		private String codeypyj;
		public String getCodeypyj() {
			return codeypyj;
		}
		public void setCodeypyj(String codeypyj) {
			this.codeypyj = codeypyj;
		}
		/**
		 * 药品类别code
		 */
		private String codeyplb;
		public String getCodeyplb() {
			return codeyplb;
		}
		public void setCodeyplb(String codeyplb) {
			this.codeyplb = codeyplb;
		}
		/**
		 * 药品性质code
		 */
		private String codeypsz;
		public String getCodeypsz() {
			return codeypsz;
		}
		public void setCodeypsz(String codeypsz) {
			this.codeypsz = codeypsz;
		}
		/**
		 * 医嘱类别id
		 */
		private String yzlbid;
		/**
		 * 药品类别id
		 */
		private String yplbid;
		/**
		 * 药品性质id
		 */
		private String ypxzid;
		/**
		 * 药品药剂id
		 */
		private String ypyjid;
		/**
		 * 药品用法id
		 */
		private String yzyfid;

		public String getYzlbid() {
			return yzlbid;
		}
		public void setYzlbid(String yzlbid) {
			this.yzlbid = yzlbid;
		}
		public String getYplbid() {
			return yplbid;
		}
		public void setYplbid(String yplbid) {
			this.yplbid = yplbid;
		}
		public String getYpxzid() {
			return ypxzid;
		}
		public void setYpxzid(String ypxzid) {
			this.ypxzid = ypxzid;
		}
		public String getYpyjid() {
			return ypyjid;
		}
		public void setYpyjid(String ypyjid) {
			this.ypyjid = ypyjid;
		}
		public String getYzyfid() {
			return yzyfid;
		}
		public void setYzyfid(String yzyfid) {
			this.yzyfid = yzyfid;
		}
		
}

