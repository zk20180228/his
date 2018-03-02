package cn.honry.statistics.bi.disease.diagnose.action;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.honry.statistics.bi.disease.diagnose.service.DiagnoseCountService;
import cn.honry.statistics.bi.disease.diagnose.vo.DiagnoseVo;
import cn.honry.statistics.bi.disease.diagnose.vo.FeaturesVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * <p>病症与病种诊断</p>
 * @Author: zhangkui
 * @CreateDate: 2017年12月9日 上午10:08:26 
 * @Modifier: zhangkui
 * @ModifyDate: 2017年12月9日 上午10:08:26 
 * @ModifyRmk:  
 * @version: V1.0
 * @throws:
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/disease/diagnose")
public class DiagnoseCountAction extends  ActionSupport {


	private static final long serialVersionUID = -1131120896869505423L;
	private String page;//当前页
	private String rows;//每页显示的记录数
	
	@Resource
	private DiagnoseCountService diagnoseCountService;
	public void setDiagnoseCountService(DiagnoseCountService diagnoseCountService) {
		this.diagnoseCountService = diagnoseCountService;
	}

	private String feature;//病情特征---->前台传递过来的格式：病情特征2,病情特征1,病情特征4
	
 	 /**
 	  * 
 	  * <p>病症与病种诊断页面跳转 </p>
 	  * @Author: zhangkui
 	  * @CreateDate: 2017年12月9日 上午10:39:33 
 	  * @Modifier: zhangkui
 	  * @ModifyDate: 2017年12月9日 上午10:39:33 
 	  * @ModifyRmk:  
 	  * @version: V1.0
 	  * @return
 	  * @throws:
 	  *http://localhost:8080/his-portal/statistics/diagnoseCount/toDiagnoseResultUI.action
 	  */
	@Action(value="/toDiagnoseResultUI",results={@Result(name="list",location="/WEB-INF/pages/stat/diagnose/diagnoseUI.jsp")})
	public String toDiagnoseResultUI(){
    	
    	return "list";
    }

	/**
	 * 
	 * <p>诊断结果列表查询 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年12月9日 上午10:38:01 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年12月9日 上午10:38:01 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws:
	 *http://localhost:8080/his-portal/statistics/diagnoseCount/diagnoseList.action?page=&rows=&feature=
	 */
	@Action("/diagnoseList")
	public void diagnoseList(){
		//结果map	
		Map<String,Object> map = new HashMap<String, Object>();
		//诊断列表
		List<DiagnoseVo> list=null;
		long total=0;
		
		
		try {
			list=diagnoseCountService.diagnoseList(page,rows,feature);
			total=diagnoseCountService.diagnoseTotal(feature);
		} catch (Exception e) {
			list= new ArrayList<DiagnoseVo>();
			e.printStackTrace();
		}
		
		map.put("total", total);//总记录数
		map.put("rows", list);
		
		String json = JSONUtils.toJson(map);
		
		WebUtils.webSendJSON(json);
	}
	
	
	/**
	 * 
	 * <p>病情特征下拉框渲染 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年12月9日 上午10:38:35 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年12月9日 上午10:38:35 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws:
	 *http://localhost:8080/his-portal/statistics/diagnoseCount/featureList.action?feature=
	 *参考的部门下拉框，数据形式如下：
	 *[
		    {
		        "menus": [
		            {
		                "code": "0376",
		                "id": "0376",
		                "inputCode": "XXMZ",
		                "name": "性腺一门诊",
		                "pinyin": "XXMZ",
		                "type": "C",
		                "wb": "NEUY"
		            },
		            {
		                "code": "0378",
		                "id": "0378",
		                "inputCode": "XXEMZ",
		                "name": "性腺二门诊",
		                "pinyin": "XXEMZ",
		                "type": "C",
		                "wb": "NEFUY"
		            }
		        ],
		        "parentMenu": "门诊"
		    }
		]
		
		注意：插件区分每一个病情特征的依据是id,因此要保证id不能相同，否则多选会出问题
	 */
	@Action("/featureList")
	public void featureList(){
		
		ArrayList<FeaturesVo> arrayList = new ArrayList<FeaturesVo>();
		//模拟数据
		List<String> list = diagnoseCountService.featureMap();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				FeaturesVo vo = new FeaturesVo(list.get(i),i+"");//病情特征名字
				arrayList.add(vo);
			}
		}
		
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("menus",arrayList);
		map.put("parentMenu", "病情特征");
		
		ArrayList<Map> resList = new  ArrayList<Map>();
		resList.add(map);
		
		String json = JSONUtils.toJson(resList);
		
		WebUtils.webSendJSON(json);
	}
	
	
	
	
	
	
/***********************************************************get and set************************************************************/
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

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}
	
	
}
