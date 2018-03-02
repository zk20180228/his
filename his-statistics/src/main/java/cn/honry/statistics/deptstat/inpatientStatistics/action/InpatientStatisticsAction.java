package cn.honry.statistics.deptstat.inpatientStatistics.action;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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

import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.statistics.deptstat.inpatientStatistics.service.InpatientStatisticsService;
import cn.honry.statistics.deptstat.inpatientStatistics.vo.InpatientStatisticsVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;
/** 
* @ClassName: InpatientCountAction 
* @Description: 在院患者情况
* @author qh
* @date 2017年7月10日
*  
*/
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/statistics/deptstat/inpatientStatisticsAction")
public class InpatientStatisticsAction {
	private static final long serialVersionUID = 1L;
	private Logger logger=Logger.getLogger(InpatientStatisticsAction.class);
	
	@Autowired
	@Qualifier(value = "inpatientStatisticsService")
	private InpatientStatisticsService inpatientStatisticsService;
	public void setInpatientStatisticsService(
			InpatientStatisticsService inpatientStatisticsService) {
		this.inpatientStatisticsService = inpatientStatisticsService;
	}
//	private dataJurisInInterService dataJurisInInterService;
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	
	private String time;
    private String ktype;
    private String ytype;
    private String menuAlias;
    
    
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getKtype() {
		return ktype;
	}
	public void setKtype(String ktype) {
		this.ktype = ktype;
	}
	public String getYtype() {
		return ytype;
	}
	public void setYtype(String ytype) {
		this.ytype = ytype;
	}

	@RequiresPermissions(value={"BQSHZBTJ:function:view"}) 
	@Action(value = "inpatientStatisticsListToView", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/deptstat/inpatientStatistics/inpatientStatisticsList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String inpatientStatisticsListToView() {
		return "list";
	}

	/** 
	* @Description:查询科室住院人数
	* @author qh
	 * @throws Exception 
	* @date 2017年7月12日
	*  
	*/
	@Action(value = "queryInpatientStatisticsList")
	public void queryInpatientStatisticsList() {
		try{
			//封装返回值
			List<Map<String,Object>> plist=new ArrayList<>();
			Map<String,Object> pmap=null;
			
			Map<String,String> dmap=inpatientStatisticsService.queryDeptCodeName();
			Map<String,String> ymap=inpatientStatisticsService.queryAreaCodeName();
			
			//获取需要查询的环比日期
			List<String>  htimeList=inpatientStatisticsService.getHTimeList(time);
			
			//获取需要查询的同比日期
			List<String>  ttimeList=inpatientStatisticsService.getTTimeList(time);
			
			List<String> hlist =new ArrayList<>();
			hlist.addAll(htimeList);
			List<String> tlist =new ArrayList<>();
			if(ttimeList!=null&&ttimeList.size()>0){
				tlist.addAll(ttimeList);
			}
			List<List<InpatientStatisticsVo>> hAllList=new ArrayList<List<InpatientStatisticsVo>>();
			List<List<InpatientStatisticsVo>> tAllList=new ArrayList<List<InpatientStatisticsVo>>();
			
			List<InpatientStatisticsVo> hList=null;
			List<InpatientStatisticsVo> tList=null;
			List<InpatientStatisticsVo> tempList=new ArrayList<InpatientStatisticsVo>();
			//获取日期集合(x轴数据)
			List<String> htimelist = getTimeList(hlist);
			
			List<String> ttimelist = getTimeList(tlist);
			//按科室查询
			if(StringUtils.isNotBlank(ktype)){
				hAllList=inpatientStatisticsService.queryDataList(ktype,htimeList,"K");
					
				tAllList=inpatientStatisticsService.queryDataList(ktype,ttimeList,"K");
				tempList=hAllList.get(0);//获取指定的code集合
				for(int i=0,len=tempList.size();i<len;i++){
					hList=new ArrayList<InpatientStatisticsVo>();
					tList=new ArrayList<InpatientStatisticsVo>();
						for(List<InpatientStatisticsVo> temp2List:hAllList){
								hList.add(temp2List.get(i));
						}
						for(List<InpatientStatisticsVo> temp2List:tAllList){
								tList.add(temp2List.get(i));
						}
						//计算获取增长率
						List<String> hGender = getGender(hList);
						List<String> tGender = getGender(tList);
						//计算获取人数
						List<Integer> hTotalList=getTotal(hList);
						List<Integer> tTotalList=getTotal(tList);
						//封装
						pmap=new HashMap<>();
						pmap.put("title", dmap.get(tempList.get(i).getCode()));
						if(hList.get(4).getTotal()==null){
							hList.get(4).setTotal(0);
						}
						if(hList.get(4).getGender()==null){
							hList.get(4).setGender("--");
						}else{
							if(!"--".equals(hList.get(4).getGender())){
								hList.get(4).setGender((Double.parseDouble(hList.get(4).getGender())+""));
							}
						}
						pmap.put("dataTable",hList.get(4));
						
						Map<String,Object> cmap1=new HashMap<>();
						Map<String,Object> cmap2=new HashMap<>();
						cmap1.put("xAxis",htimelist);
						cmap1.put("data",hGender);
						cmap1.put("data1",hTotalList);
						cmap2.put("xAxis",ttimelist);
						cmap2.put("data",tGender);
						cmap2.put("data1",tTotalList);
						pmap.put("huanData", cmap1);
						pmap.put("tonData", cmap2);
						plist.add(pmap);
				}
			}
			//按院区查询
			if(StringUtils.isNotBlank(ytype)){
				String[] arr = ytype.split(",");
				for(int i=0,len=arr.length;i<len;i++){
					hList=new ArrayList<InpatientStatisticsVo>();
					tList=new ArrayList<InpatientStatisticsVo>();
					String code=inpatientStatisticsService.queryDeptByAreaCodes(arr[i]);
					hAllList=inpatientStatisticsService.queryDataList(code,htimeList,"Y"+arr[i]);
					tAllList=inpatientStatisticsService.queryDataList(code,ttimeList,"Y"+arr[i]);
					for(List<InpatientStatisticsVo> temp2List:hAllList){
						for(InpatientStatisticsVo tem:temp2List){
							hList.add(tem);
						}
					}
					for(List<InpatientStatisticsVo> temp2List:tAllList){
						for(InpatientStatisticsVo tem:temp2List){
							tList.add(tem);
						}
					}
					//获取增长率
					List<String> hGender = getGender(hList);
					List<String> tGender = getGender(tList);
					//获取人数
					List<Integer> hTotalList=getTotal(hList);
					List<Integer> tTotalList=getTotal(tList);
					//封装
					pmap=new HashMap<>();
					pmap.put("title", ymap.get(arr[i]));
					if(hList.get(4).getTotal()==null){
						hList.get(4).setTotal(0);
					}
					if(hList.get(4).getGender()==null){
						hList.get(4).setGender("--");
					}else{
						if(!"--".equals(hList.get(4).getGender())){
							hList.get(4).setGender((Double.parseDouble(hList.get(4).getGender())+""));
						}
					}
					pmap.put("dataTable",hList.get(4));
					Map<String,Object> cmap1=new HashMap<>();
					Map<String,Object> cmap2=new HashMap<>();
					cmap1.put("xAxis",htimelist);
					cmap1.put("data",hGender);
					cmap1.put("data1",hTotalList);
					cmap2.put("xAxis",ttimelist);
					cmap2.put("data",tGender);
					cmap2.put("data1",tTotalList);
					pmap.put("huanData", cmap1);
					pmap.put("tonData", cmap2);
					plist.add(pmap);
				}
			}
			Map<String,Object> map=new HashMap<>();
			map.put("data", plist);
			map.put("success", "1");
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("KSTJ_ZYRSTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_ZYRSTJ", "科室统计_住院人数统计", "2", "0"), e); 
		}
	}
	/** 
	* @Description:获取y轴数据
	* @author qh
	* @date 2017年7月26日
	*  
	*/
	private List<Integer> getTotal(List<InpatientStatisticsVo> hList) {
		List<Integer> list=new ArrayList<Integer>();
		if(hList!=null&&hList.size()>0){
				for(int i=0;i<hList.size();i++){
					if(hList.get(i).getTotal()==null){
						list.add(0);
					}else{
						list.add(hList.get(i).getTotal());
					}
				}
				list.remove(0);
			}else{
				list.add(0);
				list.add(0);
				list.add(0);
				list.add(0);
			}
		return list;
	}
	/** 
	* @Description:得到x轴时间数据
	* @author qh
	* @date 2017年7月12日
	*  
	*/
	private List<String> getTimeList(List<String> tlist) {
		try{
			if(tlist!=null&&tlist.size()>0){
				tlist.remove(0);
			}
			return tlist;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("KSTJ_ZYRSTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_ZYRSTJ", "科室统计_住院人数统计", "2", "0"), e); 
			return null;
		}
	}
	/** 
	* @Description:计算增长率
	* @author qh
	* @date 2017年7月12日
	*  
	*/
	private List<String> getGender(List<InpatientStatisticsVo> hList) {
		try{
			List<String> list=new ArrayList<String>();
			DecimalFormat df = new DecimalFormat("######00.00");
			if(hList!=null&&hList.size()>0){
				for(int i=1;i<5;i++){
					String gender=(hList.get(i).getTotal()==null||hList.get(i).getTotal()==0)?"":""+(df.format((double)hList.get(i).getTotal()*100/hList.get(i-1).getTotal()-100));
					list.add(gender);
				}
			}		
			return list;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("KSTJ_ZYRSTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_ZYRSTJ", "科室统计_住院人数统计", "2", "0"), e); 
			return null;
		}
	}
	/** 
	* @Description:院区下拉
	* @author qh
	* @date 2017年7月12日
	*  
	*/
	@Action(value = "queryAreaCode")
	public void queryAreaCode(){
		try{
			List<SysDepartment> list = inpatientStatisticsService.queryArea();
			String json=JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("KSTJ_ZYRSTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_ZYRSTJ", "科室统计_住院人数统计", "2", "0"), e); 
		}
	}

}
