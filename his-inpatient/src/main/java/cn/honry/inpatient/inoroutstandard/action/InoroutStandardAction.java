package cn.honry.inpatient.inoroutstandard.action;

import java.util.ArrayList;
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

import cn.honry.base.bean.model.InoroutStandard;
import cn.honry.base.bean.model.InoroutStandardDetail;
import cn.honry.inpatient.inoroutstandard.service.InorOutStandardService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.TreeJson;
import cn.honry.utils.WebUtils;

/**  
 * <p>出入径标准 </p>
 * @Author: mrb
 * @CreateDate: 2017年12月23日 上午11:29:19 
 * @Modifier: mrb
 * @ModifyDate: 2017年12月23日 上午11:29:19 
 * @ModifyRmk:  
 * @version: V1.0 
 *InoroutStandardAction
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value={@InterceptorRef(value = "manageInterceptor")})
@Namespace(value = "/inpatient/inoroutStandard")
@SuppressWarnings({"all"})
public class InoroutStandardAction {
	
	@Autowired
	@Qualifier("inorOutStandardService")
	private InorOutStandardService inorOutStandardService;
	private String standardID;//出入径标准id
	private String detialID;//出入径标准id
	private String standardCode;//标准代码
	private String versionNO;//标准版本号
	private InoroutStandard standard;
	private InoroutStandardDetail detial;
	private String standardIDs;
	private String detialIDs;
	/**  
	 * <p>转到出入径标准列表 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年12月23日 上午11:33:10 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年12月23日 上午11:33:10 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return
	 * String
	 */
	@Action(value="tolist",results={@Result(name="list",location = "/WEB-INF/pages/inpatient/standard/satndardList.jsp")}, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String tolist(){
		return "list";
	}
	/**  
	 * <p>转到出入径标准添加修改页面 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年12月23日 上午11:33:40 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年12月23日 上午11:33:40 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return
	 * String
	 */
	@Action(value="toStandardADD",results={@Result(name="add",location = "/WEB-INF/pages/inpatient/standard/standardEdit.jsp")}, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toStandardADD(){
		if(StringUtils.isBlank(standardID)){
			standard = new InoroutStandard();
		}else{
			standard = inorOutStandardService.getStandard(standardID);
		}
		return "add";
	}
	/**  
	 * <p>转到出入径标准明细添加修改页面 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年12月23日 上午11:33:57 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年12月23日 上午11:33:57 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return
	 * String
	 */
	@Action(value="toStandardDetialADD",results={@Result(name="add",location = "/WEB-INF/pages/inpatient/standard/standardDetialEdit.jsp")}, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toStandardDetialADD(){
		if(StringUtils.isNotBlank(detialID)){
			detial = inorOutStandardService.getStandardDetial(detialID);
			List<InoroutStandard> list = inorOutStandardService.getStandardList(detial.getStandCode());
			String version = "";
			if(StringUtils.isNotBlank(detial.getStandVersionNo())){
				version = detial.getStandVersionNo();
			}
			for (InoroutStandard in : list) {
				if (version.equals(in.getStandVersionNo())) {
					detial.setStandName(in.getStandName());
				}
			}
		}else{
			detial = new InoroutStandardDetail();
			standard = inorOutStandardService.getStandard(standardID);
			detial.setStandName(standard.getStandName());
			detial.setStandCode(standard.getStandCode());
			detial.setStandVersionNo(standard.getStandVersionNo());
		}
		return "add";
	}
	/**  
	 * <p>获取标准列表 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年12月23日 上午11:35:10 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年12月23日 上午11:35:10 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * void
	 */
	@Action("getStandardList")
	public void getStandardList(){
		List<InoroutStandard> list = inorOutStandardService.getStandardList(standardCode);
		WebUtils.webSendJSON(JSONUtils.toJson(list));
	}
	/**  
	 * <p>获取标准明细列表 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年12月23日 上午11:35:10 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年12月23日 上午11:35:10 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * void
	 */
	@Action("getStandardDetialList")
	public void getStandardDetialList(){
		List<InoroutStandardDetail> list = new ArrayList<InoroutStandardDetail>();
		if(StringUtils.isNotBlank(standardCode) && StringUtils.isNotBlank(versionNO)){
			list = inorOutStandardService.getStandardDetialList(standardCode, versionNO);
		}
		WebUtils.webSendJSON(JSONUtils.toJson(list));
	}
	/**  
	 * <p>获取出入径标准树 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年12月23日 上午11:34:25 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年12月23日 上午11:34:25 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * void
	 */
	@Action("getSandardTree")
	public void getSandardTree(){
		TreeJson tree = inorOutStandardService.getSandardTree();
		List<TreeJson> trees = new ArrayList<TreeJson>();
		trees.add(tree);
		WebUtils.webSendJSON(JSONUtils.toJson(trees));
	}
	@Action("saveStandart")
	public void saveStandart(){
		Map<String,String> map = new HashMap<String, String>();
		try {
			inorOutStandardService.saveStandard(standard);
			map.put("resCode", "success");
			map.put("resMsg", "操作成功!");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("resCode", "error");
			map.put("resMsg", "系统异常："+e.getLocalizedMessage());
		}
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}
	@Action("saveStandartDetail")
	public void saveStandartDetail(){
		Map<String,String> map = new HashMap<String, String>();
		try {
			inorOutStandardService.saveStandardDetial(detial);
			map.put("resCode", "success");
			map.put("resMsg", "操作成功!");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("resCode", "error");
			map.put("resMsg", "系统异常："+e.getLocalizedMessage());
		}
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}
	@Action("delStandard")
	public void delStandard(){
		Map<String,String> map = new HashMap<String, String>();
		try {
			inorOutStandardService.delStandard(standardIDs);
			map.put("resCode", "success");
			map.put("resMsg", "操作成功!");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("resCode", "error");
			map.put("resMsg", "系统异常："+e.getLocalizedMessage());
		}
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}
	@Action("delStandardDetail")
	public void delStandardDetail(){
		Map<String,String> map = new HashMap<String, String>();
		try {
			inorOutStandardService.delStandardDetail(detialIDs);
			map.put("resCode", "success");
			map.put("resMsg", "操作成功!");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("resCode", "error");
			map.put("resMsg", "系统异常："+e.getLocalizedMessage());
		}
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}
	
	
	public String getStandardID() {
		return standardID;
	}
	public void setStandardID(String standardID) {
		this.standardID = standardID;
	}
	public InoroutStandard getStandard() {
		return standard;
	}
	public void setStandard(InoroutStandard standard) {
		this.standard = standard;
	}
	public InoroutStandardDetail getDetial() {
		return detial;
	}
	public void setDetial(InoroutStandardDetail detial) {
		this.detial = detial;
	}
	public String getDetialID() {
		return detialID;
	}
	public void setDetialID(String detialID) {
		this.detialID = detialID;
	}
	public String getStandardCode() {
		return standardCode;
	}
	public void setStandardCode(String standardCode) {
		this.standardCode = standardCode;
	}
	public String getVersionNO() {
		return versionNO;
	}
	public void setVersionNO(String versionNO) {
		this.versionNO = versionNO;
	}
	public String getStandardIDs() {
		return standardIDs;
	}
	public void setStandardIDs(String standardIDs) {
		this.standardIDs = standardIDs;
	}
	public String getDetialIDs() {
		return detialIDs;
	}
	public void setDetialIDs(String detialIDs) {
		this.detialIDs = detialIDs;
	}
	
}
