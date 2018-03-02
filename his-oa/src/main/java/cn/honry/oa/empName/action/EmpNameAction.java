package cn.honry.oa.empName.action;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

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

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.baseinfo.employee.service.EmployeeInInterService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/oa/empName")
@SuppressWarnings({"all"})
public class EmpNameAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private String code;
	private String str;
	public String getStr() {
		return str;
	}
	public void setStr(String str) {
		this.str = str;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	@Autowired
	@Qualifier(value="innerCodeService")
	private CodeInInterService  innerCodeService;
	@Autowired
	@Qualifier(value = "employeeInInterService")
	private EmployeeInInterService employeeInInterService;
	@Autowired
	@Qualifier(value = "deptInInterService")
	private DeptInInterService deptInInterService;
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}
	public void setEmployeeInInterService(
			EmployeeInInterService employeeInInterService) {
		this.employeeInInterService = employeeInInterService;
	}
	public void setInnerCodeService(CodeInInterService innerCodeService) {
		this.innerCodeService = innerCodeService;
	}
	/**  
	 * 
	 * 通过员工code查询name
	 * @Author: huzhenguo
	 * @CreateDate: 2018年2月6日 下午7:06:35 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2018年2月6日 下午7:06:35 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Action(value = "empNameByCode", results = { @Result(name = "json", type = "json") })
	public void empName() {
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		HashMap<String, Object> retMap = new HashMap<String, Object>();
		Map<String, String> map = null;
		if(StringUtils.isNoneBlank(code)){
			String[] codes = code.split("-");
			Map<String, String> codeAndNameMap = employeeInInterService.queryEmpCodeAndNameMap();
			for (String str : codes) {
				map = new HashMap<String, String>();
				String name = codeAndNameMap.get(str);
				if (StringUtils.isNoneBlank(name)) {
					map.put("name",name);
					map.put("code",str);
					list.add(map);
				}
			}
			retMap.put("resCode", "success");
			retMap.put("data", list);
		}else{
			retMap.put("resCode", "error");
		}
		String json = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}
	/**  
	 * 
	 * 通过科室code查询name
	 * @Author: huzhenguo
	 * @CreateDate: 2018年2月6日 下午7:06:35 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2018年2月6日 下午7:06:35 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Action(value = "deptNameByCode", results = { @Result(name = "json", type = "json") })
	public void deptNameByCode() {
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		HashMap<String, Object> retMap = new HashMap<String, Object>();
		Map<String, String> map = null;
		if(StringUtils.isNoneBlank(code)){
			String[] codes = code.split("-");
			Map<String, String> codeAndNameMap = deptInInterService.querydeptCodeAndNameMap();
			for (String str : codes) {
				map = new HashMap<String, String>();
				String name = codeAndNameMap.get(str);
				if (StringUtils.isNoneBlank(name)) {
					map.put("name",name);
					map.put("code",str);
					list.add(map);
				}
			}
			retMap.put("resCode", "success");
			retMap.put("data", list);
		}else{
			retMap.put("resCode", "error");
		}
		String json = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}
	/**  
	 * 
	 * 查询科室（10个）
	 * @Author: huzhenguo
	 * @CreateDate: 2018年2月7日 上午10:56:17 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2018年2月7日 上午10:56:17 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Action(value = "queryDept", results = { @Result(name = "json", type = "json") })
	public void queryDept() {
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		HashMap<String, Object> retMap = new HashMap<String, Object>();
		Map<String, String> map = null;
		try {
			Map<String, String> codeAndNameMap = deptInInterService.querydeptCodeAndNameMap();
			Set<String> keySet = codeAndNameMap.keySet();
			Iterator<String> keyIterator = keySet.iterator();
			while (keyIterator.hasNext() ){
				map = new HashMap<String, String>();
				String code = keyIterator.next();
				String name = codeAndNameMap.get(code);
				if(StringUtils.isNoneBlank(str)){
					Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
					boolean type = pattern.matcher(str).matches();//判断输入的是否是数字
					if (type==true?code.startsWith(str):name.contains(str)) {
						map.put("code",code);
						map.put("name",name);
						list.add(map);
					}
				}else {
					map.put("code",code);
					map.put("name",name);
					list.add(map);
				}
				if (list.size()>9) {
					break;
				}
			}
			retMap.put("resCode", "success");
			retMap.put("data", list);
		} catch (Exception e) {
			retMap.put("resCode", "error");
			e.printStackTrace();
		}
		String json = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}
	/**  
	 * 
	 * 通过决策组名称代码查询name
	 * @Author: huzhenguo
	 * @CreateDate: 2018年2月7日 上午10:56:17 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2018年2月7日 上午10:56:17 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Action(value = "queryJobByCode", results = { @Result(name = "json", type = "json") })
	public void queryJobByCode() {
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		HashMap<String, Object> retMap = new HashMap<String, Object>();
		Map<String, String> map = null;
		if(StringUtils.isNoneBlank(code)){
			String[] codes = code.split("-");
			List<BusinessDictionary> dictionaryList = innerCodeService.getDictionary("manageGroup");
			for (String code : codes) {
				for (BusinessDictionary dict : dictionaryList) {
					if (code.equals(dict.getEncode())) {
						map = new HashMap<String, String>();
						map.put("code",dict.getEncode());
						map.put("name",dict.getName());
						list.add(map);
					}
				}
			}
			retMap.put("resCode", "success");
			retMap.put("data", list);
		}else{
			retMap.put("resCode", "error");
		}
		String json = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 * 
	 * 查询决策组（10个）
	 * @Author: huzhenguo
	 * @CreateDate: 2018年2月8日 上午11:40:18 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2018年2月8日 上午11:40:18 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Action(value = "queryJob", results = { @Result(name = "json", type = "json") })
	public void queryJob() {
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		HashMap<String, Object> retMap = new HashMap<String, Object>();
		Map<String, String> map = null;
		try {
			List<BusinessDictionary> dictionaryList = innerCodeService.getDictionary("manageGroup");
			for (BusinessDictionary dict : dictionaryList) {
				String encode = dict.getEncode();
				String name = dict.getName();
				if(StringUtils.isNoneBlank(str)){
					Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
					boolean type = pattern.matcher(str).matches();//判断输入的是否是数字
					if (type==true?encode.startsWith(str):name.contains(str)) {
						map = new HashMap<String, String>();
						map.put("code",encode);
						map.put("name",name);
						list.add(map);
					}
				}else{
					map = new HashMap<String, String>();
					map.put("code",encode);
					map.put("name",name);
					list.add(map);
				}
				if (list.size()>9) {
					break;
				}
			}
			retMap.put("resCode", "success");
			retMap.put("data", list);
		} catch (Exception e) {
			retMap.put("resCode", "error");
			e.printStackTrace();
		}
		String json = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}
	/**  
	 * 
	 * 通过院区code查询name
	 * @Author: huzhenguo
	 * @CreateDate: 2018年2月9日 下午2:27:19 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2018年2月9日 下午2:27:19 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Action(value = "queryHospitalArea", results = { @Result(name = "json", type = "json") })
	public void queryHospitalArea() {
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		HashMap<String, Object> retMap = new HashMap<String, Object>();
		Map<String, String> map = null;
		List<BusinessDictionary> dictionaryList = innerCodeService.getDictionary("hospitalArea");
		for (BusinessDictionary busDict : dictionaryList) {
			map = new HashMap<String, String>();
			map.put("code",busDict.getEncode());
			map.put("name",busDict.getName());
			list.add(map);
		}
		retMap.put("data", list);
		String json = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}
	/**  
	 * 
	 * 院区和人员类型
	 * @Author: huzhenguo
	 * @CreateDate: 2018年2月27日 上午10:13:00 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2018年2月27日 上午10:13:00 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Action(value = "hisType", results = { @Result(name = "json", type = "json") })
	public void hisType(){
		HashMap<String, String> areaMap = new HashMap<String, String>();
		HashMap<String, String> userMap = new HashMap<String, String>();
		Map<String, Object> retMap = new HashMap<String, Object>();
		List<BusinessDictionary> areaList = innerCodeService.getDictionary("hospitalArea");//院区
		List<BusinessDictionary> userList = innerCodeService.getDictionary("employeeType");//人员类型
		for (BusinessDictionary busDict : areaList) {
			areaMap.put(busDict.getEncode(),busDict.getName());
		}
		for (BusinessDictionary busDict : userList) {
			userMap.put(busDict.getEncode(),busDict.getName());
		}
		retMap.put("hisType", areaMap);
		retMap.put("userType", userMap);
		String json = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}
}
