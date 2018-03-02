package cn.honry.statistics.bi.inpatient.clinicalPathway.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

import cn.honry.statistics.bi.inpatient.clinicalPathway.service.StaClinicalPathwayService;
import cn.honry.statistics.bi.inpatient.clinicalPathway.vo.ClinicalPathVo;
import cn.honry.statistics.bi.inpatient.clinicalPathway.vo.InOutDetail;
import cn.honry.statistics.bi.inpatient.clinicalPathway.vo.analysisClinicalVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 临床路径统计分析
 * 
 * <p> </p>
 * @Author: zouxianhao
 * @CreateDate: 2017年11月28日 上午10:23:57 
 * @Modifier: zouxianhao
 * @ModifyDate: 2017年11月28日 上午10:23:57 
 * @ModifyRmk:  
 * @version: V1.0:
 * @throws:
 * @return: 
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/bi/staClinicalPathwayAction")
public class StaClinicalPathwayAction extends ActionSupport {

	/**  
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月28日 上午10:23:33 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月28日 上午10:23:33 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired
	@Qualifier(value = "staClinicalPathwayService")
	private StaClinicalPathwayService staClinicalPathwayService;

	public void setStaClinicalPathwayService(
			StaClinicalPathwayService staClinicalPathwayService) {
		this.staClinicalPathwayService = staClinicalPathwayService;
	}
	
	private String sTime;
	private String eTime;
	private String page;
	private String rows;
	
	public String getsTime() {
		return sTime;
	}

	public void setsTime(String sTime) {
		this.sTime = sTime;
	}

	public String geteTime() {
		return eTime;
	}

	public void seteTime(String eTime) {
		this.eTime = eTime;
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

	@Action(value = "test", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/staClinicalPathway/staClinicalPathway.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String test(){
		System.out.println("action");
		staClinicalPathwayService.test();
		return "list";
	}
	/**
	 * 临床路径分析
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月29日 下午7:59:36 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月29日 下午7:59:36 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return:
	 * @throws:
	 * @return: String
	 *
	 */
	@Action(value = "analysisClinical", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/staClinicalPathway/analysisClinicalPathway.jsp") })
	public String analysisClinical(){
		Date date = new Date();
		eTime = DateUtils.formatDateY_M_D(date);
		sTime=DateUtils.formatDateY_M_D(DateUtils.addDay(date, -3));
		return "list";
	}
	/**
	 * 跳转临床路径统计分析的四合一页面
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月28日 下午3:39:04 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月28日 下午3:39:04 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return:
	 * @throws:
	 * @return: String
	 *
	 */
	@Action(value = "inOutPatient", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/staClinicalPathway/staClinicalPathway.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String inOutPatient(){
		Date date = new Date();
		eTime = DateUtils.formatDateY_M_D(date);
		sTime=DateUtils.formatDateY_M_D(DateUtils.addDay(date, -3));
		return "list";
	}

	/**
	 * 在出院患者统计
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月28日 下午3:51:25 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月28日 下午3:51:25 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value = "inOutList", results = { @Result(name = "json", type = "json") })
	public void inOutList(){
		Map<String, Object> map = new HashMap<String, Object>();
		List<ClinicalPathVo> list = new ArrayList<ClinicalPathVo>();
		try{
			HttpServletRequest request = ServletActionContext.getRequest();
			String deptCodeTopL = request.getParameter("deptCodeTopL");
			String inOrOutTopL = request.getParameter("inOrOutTopL");
			list = staClinicalPathwayService.inOutList(page,rows,sTime,eTime,deptCodeTopL,inOrOutTopL);
			Integer total = staClinicalPathwayService.inOutNum(sTime,eTime,deptCodeTopL,inOrOutTopL);
			if(total > 0){
				int sumPerson = 0;
				for(ClinicalPathVo vpv : list){
					sumPerson += vpv.getNum().intValue();
				}
				ClinicalPathVo cp = new ClinicalPathVo();
				cp.setDeptCode("总人数");
				cp.setNameCode(String.valueOf(sumPerson));
				list.add(cp);
			}
			map.put("total",total);
		}catch(Exception e){
			e.printStackTrace();
			map.put("total",0);
			map.put("footer","");
		}
		map.put("rows", list);
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}
	
	/**
	 * 未入径统计查询
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月28日 下午8:46:08 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月28日 下午8:46:08 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value = "notEntryList", results = { @Result(name = "json", type = "json") })
	public void notEntryList(){
		Map<String, Object> map = new HashMap<String, Object>();
		List<ClinicalPathVo> list = new ArrayList<ClinicalPathVo>();
		try{
			HttpServletRequest request = ServletActionContext.getRequest();
			String deptCodeBL = request.getParameter("deptCodeBL");
			list = staClinicalPathwayService.notEntryList(page,rows,sTime,eTime,deptCodeBL);
			Integer total = staClinicalPathwayService.notEntryNum(sTime,eTime,deptCodeBL);
			if(total > 0){
				int sumPerson = 0;
				for(ClinicalPathVo vpv : list){
					sumPerson += vpv.getNum().intValue();
				}
				ClinicalPathVo cp = new ClinicalPathVo();
				cp.setDeptCode("未入径总人数");
				cp.setNameCode(String.valueOf(sumPerson));
				list.add(cp);
			}
			map.put("total",total);
		}catch(Exception e){
			e.printStackTrace();
			map.put("total",0);
			map.put("footer","");
		}
		map.put("rows", list);
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}
	/**
	 * 变异出径统计分析
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月29日 上午10:28:53 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月29日 上午10:28:53 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value = "variationOutList", results = { @Result(name = "json", type = "json") })
	public void variationOutList(){
		Map<String, Object> map = new HashMap<String, Object>();
		List<ClinicalPathVo> list = new ArrayList<ClinicalPathVo>();
		try{
			HttpServletRequest request = ServletActionContext.getRequest();
			String deptCodeTR = request.getParameter("deptCodeTR");
			String variationTR = request.getParameter("variationTR");
			list = staClinicalPathwayService.variationOutList(page,rows,sTime,eTime,deptCodeTR,variationTR);
			Integer total = staClinicalPathwayService.variationOutNum(sTime,eTime,deptCodeTR,variationTR);
			if(total > 0){
				int sumPerson = 0;
				for(ClinicalPathVo vpv : list){
					sumPerson += vpv.getNum().intValue();
				}
				ClinicalPathVo cp = new ClinicalPathVo();
				cp.setDeptCode("变异出径总人数");
				cp.setNameCode(String.valueOf(sumPerson));
				list.add(cp);
			}
			map.put("total",total);
		}catch(Exception e){
			e.printStackTrace();
			map.put("total",0);
			map.put("footer","");
		}
		map.put("rows", list);
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}
	/**
	 * 出入径明细查询
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月29日 下午2:58:51 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月29日 下午2:58:51 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value = "inOutDetailList", results = { @Result(name = "json", type = "json") })
	public void inOutDetailList(){
		Map<String, Object> map = new HashMap<String, Object>();
		List<InOutDetail> list = new ArrayList<InOutDetail>();
		try{
			HttpServletRequest request = ServletActionContext.getRequest();
			String deptCodeBR = request.getParameter("deptCodeBR");
			String inOrOutBR = request.getParameter("inOrOutBR");
			String sexCode = request.getParameter("sexCode");
			list = staClinicalPathwayService.inOutDetailList(page,rows,sTime,eTime,deptCodeBR,inOrOutBR,sexCode);
			Integer total = staClinicalPathwayService.inOutDetailNum(sTime,eTime,deptCodeBR,inOrOutBR,sexCode);
			map.put("total",total);
		}catch(Exception e){
			e.printStackTrace();
			map.put("total",0);
			map.put("footer","");
		}
		map.put("rows", list);
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}
	/**
	 * 临床路径分析
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月30日 上午11:01:54 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月30日 上午11:01:54 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value = "analysisClinicalList", results = { @Result(name = "json", type = "json") })
	public void analysisClinicalList(){
		Map<String, Object> map = new HashMap<String, Object>();
		List<analysisClinicalVo> list = new ArrayList<analysisClinicalVo>();
		try{
			HttpServletRequest request = ServletActionContext.getRequest();
			String deptCode = request.getParameter("deptCodeTopL");
			list = staClinicalPathwayService.analysisClinicalList(page,rows,sTime,eTime,deptCode);
			Integer total = staClinicalPathwayService.analysisClinicalNum(sTime,eTime,deptCode);
			map.put("total",total);
		}catch(Exception e){
			e.printStackTrace();
			map.put("total",0);
			map.put("footer","");
		}
		map.put("rows", list);
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}
}
