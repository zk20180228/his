package cn.honry.statistics.bi.bistac.toListView.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.honry.statistics.bi.bistac.toListView.service.ToListViewService;
import cn.honry.statistics.bi.bistac.toListView.vo.ToListView;
import cn.honry.statistics.bi.bistac.toListView.vo.ToListViewVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.NumberUtil;
import cn.honry.utils.WebUtils;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/toListView")
public class ToListViewAction {
	@Autowired
	@Qualifier(value = "toListViewService")
	private ToListViewService toListViewService;
	private String date;
	private String staType;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getStaType() {
		return staType;
	}
	public void setStaType(String staType) {
		this.staType = staType;
	}
	/**  
	 * 
	 * 门急诊人次统计
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月5日 下午2:38:18 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月5日 下午2:38:18 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Action(value="queryToListView")
	public void queryToListView(){
//		ToListViewVo queryVo = toListViewService.queryVo(date);
//		ToListViewVo queryVo = toListViewService.queryVoByMongo(date);
//		toListViewService.queryAreaByES(date, staType);  测试用es查询院区
		List<ToListView> list=new ArrayList<ToListView>();
		ToListViewVo queryVo=null;
		switch (Integer.parseInt(staType)) {
		case 2:
			date = date.substring(0, 7);
			break;
		case 3:
			date = date.substring(0, 4);
			break;
		default:
			break;
		}
		Map<String, Object> queryVoByMongo = toListViewService.queryVoByMongo(date, staType);
		queryVo = (ToListViewVo) queryVoByMongo.get("viewVo");
		/*if("1".equals(staType)||"2".equals(staType)||"3".equals(staType)){
		}else{
			queryVo = toListViewService.queryVoByES(date, staType);
		}*/
		/*Integer outpatientD = queryVo.getOutpatientD()==null?0:queryVo.getOutpatientD();//门诊人次
		Integer outpatientLastM = queryVo.getOutpatientLastM()==null?0:queryVo.getOutpatientLastM();//上月门诊人次
		Integer outpatientLastY=queryVo.getOutpatientLastY()==null?0:queryVo.getOutpatientLastY();//上年门诊人次
		Integer emergencyD = queryVo.getEmergencyD()==null?0:queryVo.getEmergencyD();//急诊人次
		Integer emergencyLastM = queryVo.getEmergencyLastM()==null?0:queryVo.getEmergencyLastM();//上月急诊人次
		Integer emergencyLastY = queryVo.getEmergencyLastY()==null?0:queryVo.getEmergencyLastY();//上年急诊人次*/
		
		Integer outpatientD = 0;//当月总人数
		Integer outpatientLastM = 0;//上月总人数
		Integer outpatientLastY=0;//上年总人数
		Integer emergencyD = 0;//当月急诊人次
		Integer emergencyLastM =0;//上月急诊人次
		Integer emergencyLastY = 0;//上年急诊人次
		if("3".equals(staType)){
			 outpatientD = queryVo.getOutpatientD()==null?0:queryVo.getOutpatientD();//当月入院人次
			 outpatientLastM = queryVo.getOutpatientLastY()==null?0:queryVo.getOutpatientLastY();//上月入院人次
			 outpatientLastY=0;//上年总人次
		     emergencyD = queryVo.getEmergencyD()==null?0:queryVo.getEmergencyD();//出院人次
			 emergencyLastM =queryVo.getEmergencyLastY()==null?0:queryVo.getEmergencyLastY();//上月出院人次
			 emergencyLastY = 0;//上年急诊人次
		}else{
			outpatientD = queryVo.getOutpatientD()==null?0:queryVo.getOutpatientD();//门诊人次
			outpatientLastM = queryVo.getOutpatientLastM()==null?0:queryVo.getOutpatientLastM();//上月门诊人次
			outpatientLastY=queryVo.getOutpatientLastY()==null?0:queryVo.getOutpatientLastY();//上年门诊人次
			emergencyD = queryVo.getEmergencyD()==null?0:queryVo.getEmergencyD();//急诊人次
			emergencyLastM = queryVo.getEmergencyLastM()==null?0:queryVo.getEmergencyLastM();//上月急诊人次
			emergencyLastY = queryVo.getEmergencyLastY()==null?0:queryVo.getEmergencyLastY();//上年急诊人次
		}
		ToListView tlv1=new ToListView();
		tlv1.setIndex("合计");
		tlv1.setPassengers(outpatientD);
		Integer allLastM=outpatientD-outpatientLastM;
		tlv1.setAddsLastM(allLastM==null?0:allLastM);
		if (outpatientLastM==0) {
			tlv1.setAddPerLastM("--");
		}else{
			tlv1.setAddPerLastM(NumberUtil.init().format(100.0*(outpatientD-outpatientLastM)/outpatientLastM,2));
		}
		tlv1.setAddsLastY(outpatientD-outpatientLastY);
		if (outpatientLastY==0) {
			tlv1.setAddPerLastY("--");
		}else{
			tlv1.setAddPerLastY(NumberUtil.init().format(100.0*(outpatientD-outpatientLastY)/outpatientLastY,2));
//			if("3".equals(staType)){
//				tlv1.setAddsLastM(outpatientD-outpatientLastY);
//				tlv1.setAddPerLastM(tlv1.getAddPerLastY());
//			}
		}
		tlv1.setRatio("--");
		ToListView tlv2=new ToListView();
		tlv2.setIndex("门诊");
		tlv2.setPassengers(outpatientD-emergencyD);
		Integer outLastM=(outpatientD-emergencyD)-(outpatientLastM-emergencyLastM);
		tlv2.setAddsLastM(outLastM==null?0:outLastM);
		if ((outpatientLastM-emergencyLastM)==0) {
			tlv2.setAddPerLastM("--");
		}else{
			tlv2.setAddPerLastM(NumberUtil.init().format(100.0*((outpatientD-emergencyD)-(outpatientLastM-emergencyLastM))/(outpatientLastM-emergencyLastM),2));
		}
		tlv2.setAddsLastY((outpatientD-emergencyD)-(outpatientLastY-emergencyLastY));
		if ((outpatientLastY-emergencyLastY)==0) {
			tlv2.setAddPerLastY("--");
		}else{
			tlv2.setAddPerLastY(NumberUtil.init().format(100.0*((outpatientD-emergencyD)-(outpatientLastY-emergencyLastY))/(outpatientLastY-emergencyLastY),2));
		}
		tlv2.setRatio("--");
		ToListView tlv3=new ToListView();
		tlv3.setIndex("急诊");
		tlv3.setPassengers(emergencyD);
		Integer emerLastM=emergencyD-emergencyLastM;
		tlv3.setAddsLastM(emerLastM==null?0:emerLastM);
		if (emergencyLastM==0) {
			tlv3.setAddPerLastM("--");
		}else{
			tlv3.setAddPerLastM(NumberUtil.init().format(100.0*(emergencyD-emergencyLastM)/emergencyLastM,2));
		}
		tlv3.setAddsLastY(emergencyD-emergencyLastY);
		if (emergencyLastY==0) {
			tlv3.setAddPerLastY("--");
		}else{
			tlv3.setAddPerLastY(NumberUtil.init().format(100.0*(emergencyD-emergencyLastY)/emergencyLastY,2));
		}
		tlv3.setRatio("--");
		
		list.add(tlv2);
		list.add(tlv3);
		list.add(tlv1);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("toView", list);
		map.put("areaJson", queryVoByMongo.get("areaString"));
		String mapString = JSONUtils.toJson(map);
		WebUtils.webSendJSON(mapString);
	}
	/**
	 * 查询以六为单位的同环比
	 */
	@Action(value="querySixMomYoy")
	public void querySixMomYoy(){
		List<ToListView> queryList = toListViewService.querySixMomYoy(date, staType);
		String json = JSONUtils.toJson(queryList);
		WebUtils.webSendJSON(json);
	}
}
