package cn.honry.oa.juris.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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

import com.opensymphony.xwork2.ActionSupport;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.OaBpmProcess;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.oa.activiti.bpm.process.service.OaBpmProcessService;
import cn.honry.oa.activiti.bpm.process.vo.OaProcessVo;
import cn.honry.oa.juris.service.JurisService;
import cn.honry.oa.juris.vo.AreaVo;
import cn.honry.oa.juris.vo.CustVo;
import cn.honry.oa.juris.vo.DutiesVo;
import cn.honry.oa.juris.vo.EchoVo;
import cn.honry.oa.juris.vo.JurisVo;
import cn.honry.oa.juris.vo.LevelVo;
import cn.honry.oa.juris.vo.PersVo;
import cn.honry.oa.juris.vo.TypeVo;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.TreeJson;
import cn.honry.utils.WebUtils;



/**  
 *  
 * @Author：aizhonghua
 * @CreateDate：2018-2-1 下午20:32:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2018-2-1 下午20:32:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/oa/juris")
public class JurisAction extends ActionSupport{
	
	private static final long serialVersionUID = 1L;
	
	@Autowired
	@Qualifier(value = "jurisService")
	private JurisService jurisService;
	public void setJurisService(JurisService jurisService) {
		this.jurisService = jurisService;
	}
	@Autowired
	@Qualifier(value = "oaBpmProcessService")
	private OaBpmProcessService oaBpmProcessService;
	public void setOaBpmProcessService(OaBpmProcessService oaBpmProcessService) {
		this.oaBpmProcessService = oaBpmProcessService;
	}
	
	@Autowired
	@Qualifier(value = "innerCodeService")
	private CodeInInterService innerCodeService;
	public void setInnerCodeService(CodeInInterService innerCodeService) {
		this.innerCodeService = innerCodeService;
	}
	
	private String menuAlias;
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	
	private String id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	private String json;
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
	
	private int page;//页码
	private int rows;//每页记录数
	private String name;//模糊查询
	private String category;//分类id
	private String deptcode;//科室code
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getDeptcode() {
		return deptcode;
	}
	public void setDeptcode(String deptcode) {
		this.deptcode = deptcode;
	}
	
	/**  
	 * 跳转至流程权限页面
	 * @Author：aizhonghua
	 * @CreateDate：2018-2-1 下午20:32:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2018-2-1 下午20:32:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"LCQXWH:function:view"})
	@Action(value = "jurisView", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/activiti/process/jurisView.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String jurisView() {
		return "list";
	}
	
	/**  
	 * 获取流程权限
	 * @Author：aizhonghua
	 * @CreateDate：2018-2-1 下午20:32:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2018-2-1 下午20:32:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "getJuris", results = { @Result(name = "json", type = "json") })
	public void getJuris(){
		OaBpmProcess process = oaBpmProcessService.get(id);//获取流程对象
		JurisVo vo = new JurisVo();
		List<BusinessDictionary> typeList = innerCodeService.getDictionary(HisParameters.EMPLOYEETYPE);//人员类型
		List<BusinessDictionary> dutiesList = innerCodeService.getDictionary(HisParameters.DUTIES);//职务
		LinkedHashMap<String,String> employeeLevelMap = HisParameters.getEmployeeLevel();//级别
		List<BusinessDictionary> areaList = innerCodeService.getDictionary(HisParameters.HOSPITALAREA);//院区
		EchoVo echoVo = jurisService.getJurisMap(process.getId());
		vo.setId(process.getId());
		vo.setName(process.getName());
		vo.setAll(echoVo!=null?echoVo.isAll():false);
		//人员类型
		ArrayList<TypeVo> type = new ArrayList<TypeVo>();
		if(typeList!=null&&typeList.size()>0){
			TypeVo typeVo = null;
			for(BusinessDictionary b : typeList){
				typeVo = new TypeVo();
				typeVo.setCode(b.getEncode());
				typeVo.setName(b.getName());
				typeVo.setSelect((echoVo!=null&&echoVo.getType().get(b.getEncode())!=null)?true:false);
				type.add(typeVo);
			}
		}
		//职务
		ArrayList<DutiesVo> duties = new ArrayList<DutiesVo>();
		if(dutiesList!=null&&dutiesList.size()>0){
			DutiesVo dutiesVo = null;
			for(BusinessDictionary b : dutiesList){
				dutiesVo = new DutiesVo();
				dutiesVo.setCode(b.getEncode());
				dutiesVo.setName(b.getName());
				dutiesVo.setSelect((echoVo!=null&&echoVo.getDuties().get(b.getEncode())!=null)?true:false);
				duties.add(dutiesVo);
			}
		}
		//级别
		ArrayList<LevelVo> level = new ArrayList<LevelVo>();
		if(employeeLevelMap!=null&&employeeLevelMap.size()>0){
			LevelVo levelVo = null;
			for(Map.Entry<String, String> map : employeeLevelMap.entrySet()){
				levelVo = new LevelVo();
				levelVo.setCode(map.getValue());
				levelVo.setName(map.getKey());
				levelVo.setSelect((echoVo!=null&&echoVo.getLevel().get(map.getValue())!=null)?true:false);
				level.add(levelVo);
			}
		}
		//院区
		ArrayList<AreaVo> area = new ArrayList<AreaVo>();
		if(areaList!=null&&areaList.size()>0){
			AreaVo areaVo = null;
			for(BusinessDictionary b : areaList){
				areaVo = new AreaVo();
				areaVo.setCode(b.getEncode());
				areaVo.setName(b.getName());
				areaVo.setSelect((echoVo!=null&&echoVo.getArea().get(b.getEncode())!=null)?true:false);
				area.add(areaVo);
			}
		}
		//个人
		ArrayList<PersVo> pers = new ArrayList<PersVo>();
		if(echoVo!=null&&echoVo.getPers().size()>0){
			PersVo persVo = null;
			for(Map.Entry<String, String> map : echoVo.getPers().entrySet()){
				persVo = new PersVo();
				persVo.setCode(map.getKey());
				persVo.setName(map.getValue());
				pers.add(persVo);
			}
		}
		//自定义
		CustVo cust = new CustVo();
		if(echoVo!=null&&StringUtils.isNotBlank(echoVo.getCust().getCode())){
			cust.setCode(echoVo.getCust().getCode());
			cust.setName(echoVo.getCust().getName());
		}
		vo.setType(type);
		vo.setDuties(duties);
		vo.setLevel(level);
		vo.setArea(area);
		vo.setPers(pers);
		vo.setCust(cust);
		String json = JSONUtils.toJson(vo);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 * 保存流程权限
	 * @Author：aizhonghua
	 * @CreateDate：2018-2-3 下午16:19:20  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2018-2-3 下午16:19:20  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "saveJuris", results = { @Result(name = "json", type = "json") })
	public void saveJuris(){
		HashMap<String, String> retMap = new HashMap<String, String>();
		try {
			JurisVo vo = JSONUtils.fromJson(json, JurisVo.class);
			jurisService.saveJuris(vo);
			retMap.put("resCode", "success");
			retMap.put("resMsg", "授权成功!");
		} catch (Exception e) {
			retMap.put("resCode", "error");
			retMap.put("resMsg", "授权失败!");
		}
		String retJson = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(retJson);
	}
	
	/**  
	 * 获取有权限的流程分类树
	 * @Author：aizhonghua
	 * @CreateDate：2018-2-3 下午16:19:20  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2018-2-3 下午16:19:20  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "getProcessTree", results = { @Result(name = "json", type = "json") })
	public void getProcessTree(){
		List<TreeJson> list = jurisService.getProcessTree();
		WebUtils.webSendJSON(JSONUtils.toJson(list));
	}
	
	/**  
	 * 获取有权限的流程列表
	 * @Author：aizhonghua
	 * @CreateDate：2018-2-3 下午16:19:20  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2018-2-3 下午16:19:20  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "getProcess", results = { @Result(name = "json", type = "json") })
	public void getProcess(){
		List<String> idList = jurisService.getJurisList();
		Map<String, Object> retMap = new HashMap<String, Object>();
		if(idList!=null){
			List<OaProcessVo> list = jurisService.getProcessListByJuris(page,rows,name,category,deptcode,idList);
			int total = jurisService.getProcessTotalByJuris(name,category,deptcode,idList);
			retMap.put("rows", list);
			retMap.put("total",total);
		}else{
			retMap.put("rows", new ArrayList<OaProcessVo>());
			retMap.put("total",0);
		}
		String json = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}
	
}
