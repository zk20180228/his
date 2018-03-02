package cn.honry.statistics.bi.bistac.hospitalDischarge.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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

import cn.honry.statistics.bi.bistac.hospitalDischarge.service.HospitalDisService;
import cn.honry.statistics.bi.bistac.hospitalDischarge.vo.HospitalDisChargeVo;
import cn.honry.statistics.bi.bistac.hospitalDischarge.vo.HospitalDisVo;
import cn.honry.statistics.bi.bistac.toListView.vo.ToListView;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.NumberUtil;
import cn.honry.utils.WebUtils;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/HospitalDischarge")
public class HospitalDischargeAction {
	@Autowired
	@Qualifier("hospitalDisService")
	private HospitalDisService hospitalDisService;
	public void setHospitalDisService(HospitalDisService hospitalDisService) {
		this.hospitalDisService = hospitalDisService;
	}
	private String date;
	private String dateSign;
	private String type;
	private String Etime;
	private String Stime;
	
//	@Action(value="initDB")
//	public void initDB(){
//		hospitalDisService.initOneDay(Stime, Etime);
//	}
	@Action(value="querySameOrSque")
	public void querySameOrSque(){
		List<HospitalDisVo> list=hospitalDisService.querySameOrSque(date, dateSign, type);
		List<HospitalDisVo> list1=new ArrayList<HospitalDisVo>();
		for(int i=list.size()-1;i>=0;i--){
			list1.add(list.get(i));
		}
		String json=JSONUtils.toJson(list1);
		WebUtils.webSendJSON(json);
	}
	@Action(value="queryHospitalDischarge")
	public void queryHospitalDischarge(){
		List<Map<String,HospitalDisVo>> list=hospitalDisService.queryDate(date, dateSign);
		List<Integer> listNum=new ArrayList<Integer>();
		for(Map<String,HospitalDisVo> vo:list){//循环获取各个月份的值
			for(String key:vo.keySet()){
				listNum.add(vo.get(key).getNum());
			}
		}
		Integer outpatientD = 0;//当月入院人次
		Integer outpatientLastM = 0;//上月入院人次
		Integer outpatientLastY=0;//上年入院人次
		Integer emergencyD = 0;//出院人次
		Integer emergencyLastM =0;//上月出院人次
		Integer emergencyLastY = 0;//上年出院人次
		if("1".equals(dateSign)){
			 outpatientD = listNum.get(0);//当月入院人次
			 outpatientLastM = listNum.get(2);//上月入院人次
			 outpatientLastY=0;//上年入院人次
		     emergencyD = listNum.get(1);//出院人次
			 emergencyLastM = listNum.get(3);//上月出院人次
			 emergencyLastY = 0;//上年出院人次
		}else{
			 outpatientD = listNum.get(0);//当月入院人次
			 outpatientLastM = listNum.get(2);//上月入院人次
			 outpatientLastY=listNum.get(4);//上年入院人次
		     emergencyD = listNum.get(1);//出院人次
			 emergencyLastM = listNum.get(3);//上月出院人次
			 emergencyLastY = listNum.get(5);//上年出院人次
		}
		List<ToListView> list1=formateView(outpatientD, outpatientLastM, outpatientLastY, emergencyD, emergencyLastM, emergencyLastY);
		String json=JSONUtils.toJson(list1);
		WebUtils.webSendJSON(json);
		
	}
	/**
	 * 获取list页面(入出院人次统计)
	 */
	@RequiresPermissions(value={"ZCYRCTJ:function:view"})
	@Action(value = "toListView", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/reportStatic/hospitalDis/hospitalDis.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toListView() {
		Date date = new Date();
		Etime = DateUtils.formatDateY_M_D(date);
		Stime = DateUtils.formatDateY_M_D(DateUtils.addDay(date, -7));
		return "list";
	}
	
	
	/**
	 * 
	 */
	@Action(value="queryFeeHos")
	public void queryFeeHos(){
		List<HospitalDisVo> list=hospitalDisService.queryFeelHos(Stime, Etime);
		List<ToListView> list1=new ArrayList<ToListView>();
		Integer num=0;//合计数量
		for(HospitalDisVo vo:list){
			num+=vo.getNum();
		}
		ToListView vo1=new ToListView();//合计
		vo1.setIndex("合计");
		vo1.setPassengers(num);
		ToListView vo2=new ToListView();//合计
		vo2.setIndex("入院");
		vo2.setPassengers(list.get(0).getNum());
		ToListView vo3=new ToListView();//合计
		vo3.setIndex("出院");
		vo3.setPassengers(list.get(1).getNum());
		list1.add(vo2);
		list1.add(vo3);
		list1.add(vo1);
		String json=JSONUtils.toJson(list1);
		WebUtils.webSendJSON(json);
	}
	/**
	 * 
	 * 
	 * <p>入出院人次同环比  new </p>
	 * @Author: XCL
	 * @CreateDate: 2017年11月2日 上午10:18:26 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年11月2日 上午10:18:26 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 *
	 */
	@Action(value="queryContinue")
	public void queryContinue(){
		String[] searchTimes;
		if("HB".equals(type)){
			searchTimes=this.conYear(date, dateSign);
		}else{
			searchTimes=this.conMonth(date, dateSign);
		}
		List<HospitalDisVo> list1=new ArrayList<HospitalDisVo>();
		for(String searTime:searchTimes){
			HospitalDisChargeVo vo=hospitalDisService.queryInOutList(searTime, null, dateSign);
			Integer total=vo.getInHost()+vo.getOutHost();
			HospitalDisVo reVo=new HospitalDisVo();
			reVo.setNum(total);
			reVo.setDate(searTime);
			list1.add(reVo);
		}
		//排序
		Collections.sort(list1,new Comparator<HospitalDisVo>(){
			@Override
			public int compare(HospitalDisVo vo1, HospitalDisVo vo2) {
				return vo1.getDate().compareTo(vo2.getDate());
			}
		});
		String json=JSONUtils.toJson(list1);
		WebUtils.webSendJSON(json);
	}
	/**
	 * 
	 * 
	 * <p>入出院人次统计 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年11月1日 下午6:48:14 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年11月1日 下午6:48:14 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 *
	 */
	@Action(value="newHospDisc")
	public void newHospDisc(){
		List<String> searchTimes=lastSameq(date,dateSign);
		List<HospitalDisChargeVo> list=new ArrayList<>();
		for(String time:searchTimes){
			HospitalDisChargeVo vo=hospitalDisService.queryInOutList(time, Etime, dateSign);
			vo.setOperDate(time);
			list.add(vo);
		}
		List<ToListView> list1=null;
		if(StringUtils.isNotBlank(dateSign)){
			Integer outpatientD = 0;//当月入院人次
			Integer outpatientLastM = 0;//上月入院人次
			Integer outpatientLastY=0;//上年入院人次
			Integer emergencyD = 0;//出院人次
			Integer emergencyLastM =0;//上月出院人次
			Integer emergencyLastY = 0;//上年出院人次
			
			if("1".equals(dateSign)){
				 outpatientD = list.get(0).getInHost();//当月入院人次
				 outpatientLastM = list.get(1).getInHost();//上月入院人次
				 outpatientLastY=0;//上年入院人次
			     emergencyD = list.get(0).getOutHost();//出院人次
				 emergencyLastM = list.get(1).getOutHost();//上月出院人次
				 emergencyLastY = 0;//上年出院人次
			}else{
				 outpatientD = list.get(0).getInHost();//当月入院人次
				 outpatientLastM = list.get(1).getInHost();//上月入院人次
				 outpatientLastY=list.get(2).getInHost();//上年入院人次
			     emergencyD = list.get(0).getOutHost();//出院人次
				 emergencyLastM = list.get(1).getOutHost();//上月出院人次
				 emergencyLastY = list.get(2).getOutHost();//上年出院人次
			}
			list1=formateView(outpatientD, outpatientLastM, outpatientLastY, emergencyD, emergencyLastM, emergencyLastY);
		}else{
			Integer num=0;//合计数量
			for(HospitalDisChargeVo vo:list){
				num+=vo.getInHost();
				num+=vo.getOutHost();
			}
			list1=new ArrayList<>();
			ToListView vo1=new ToListView();//合计
			vo1.setIndex("合计");
			vo1.setPassengers(num);
			ToListView vo2=new ToListView();//合计
			vo2.setIndex("入院");
			vo2.setPassengers(list.get(0).getInHost());
			ToListView vo3=new ToListView();//合计
			vo3.setIndex("出院");
			vo3.setPassengers(list.get(0).getOutHost());
			list1.add(vo2);
			list1.add(vo3);
			list1.add(vo1);
			
		}
		String json=JSONUtils.toJson(list1);
		WebUtils.webSendJSON(json);
	}
	public static List<ToListView>  formateView(Integer outpatientD,
	Integer outpatientLastM
	,Integer outpatientLastY
	,Integer emergencyD
	,Integer emergencyLastM
	,Integer emergencyLastY){
		List<ToListView> list=new ArrayList<>();
		Integer allHosD=outpatientD+emergencyD;//合计当日
		Integer allLastM=outpatientLastM+emergencyLastM;//上月合计
		Integer allLashY=outpatientLastY+emergencyLastY;//上年出院人次
		ToListView tlv1=new ToListView();
		tlv1.setIndex("合计");
		tlv1.setPassengers(allHosD);//人数
		tlv1.setAddsLastM(allHosD-allLastM);//与上月增加量
		if (allLastM==0) {
			tlv1.setAddPerLastM("0.00");
		}else{
			tlv1.setAddPerLastM(NumberUtil.init().format((double)(allHosD-allLastM)/(double)allLastM*100,2));//与上月增加百分比
		}
		tlv1.setAddsLastY(allHosD-allLashY);
		if (allLashY==0) {
			tlv1.setAddPerLastY("0.00");
		}else{
			tlv1.setAddPerLastY(NumberUtil.init().format((double)(allHosD-allLashY)/(double)allLashY*100,2));
		}
		tlv1.setRatio("--");
		ToListView tlv2=new ToListView();
		tlv2.setIndex("入院");
		tlv2.setPassengers(outpatientD);
		tlv2.setAddsLastM((outpatientD)-(outpatientLastM));
		if ((outpatientLastM)==0) {
			tlv2.setAddPerLastM("0.00");
		}else{
			tlv2.setAddPerLastM(NumberUtil.init().format(((double)(outpatientD)-(double)(outpatientLastM))/(double)(outpatientLastM)*100,2));
		}
		tlv2.setAddsLastY((outpatientD)-(outpatientLastY));
		
		if (outpatientLastY==0) {
			tlv2.setAddPerLastY("0.00");
		}else{
			tlv2.setAddPerLastY(NumberUtil.init().format(((double)(outpatientD)-(double)(outpatientLastY))/(double)(outpatientLastY)*100,2));
		}
		
		tlv2.setRatio("--");
		ToListView tlv3=new ToListView();
		tlv3.setIndex("出院");
		tlv3.setPassengers(emergencyD);
		tlv3.setAddsLastM(emergencyD-emergencyLastM);
		if (emergencyLastM==0) {
			tlv3.setAddPerLastM("0.00");
		}else{
			tlv3.setAddPerLastM(NumberUtil.init().format((double)(emergencyD-emergencyLastM)/(double)emergencyLastM*100,2));
		}
		tlv3.setAddsLastY(emergencyD-emergencyLastY);
		if (emergencyLastY==0) {
			tlv3.setAddPerLastY("0.00");
		}else{
			tlv3.setAddPerLastY(NumberUtil.init().format((double)(emergencyD-emergencyLastY)/(double)emergencyLastY*100,2));
		}
		tlv3.setRatio("--");
		
		list.add(tlv2);
		list.add(tlv3);
		list.add(tlv1);
		return list;
		
	}
	public void sortBy(){
		
	}
	public List<String> lastSameq(String date,String dateSign){
		List<String> list=new ArrayList<String>();
		SimpleDateFormat sdf=null;
		Calendar ca=Calendar.getInstance();
		if("1".equals(dateSign)){
			sdf=new SimpleDateFormat("yyyy");
			ca.set(Calendar.YEAR, Integer.parseInt(date));
			ca.add(Calendar.YEAR, -1);//上年
			list.add(date);
			list.add(sdf.format(ca.getTime()));
		}else if("2".equals(dateSign)){
			sdf=new SimpleDateFormat("yyyy-MM");
			String[] dateArr=date.split("-");
			ca.set(Calendar.YEAR, Integer.parseInt(dateArr[0]));
			ca.set(Calendar.MONTH, Integer.parseInt(dateArr[1])-1);
			list.add(date);//当月
			ca.add(Calendar.MONTH, -1);
			list.add(sdf.format(ca.getTime()));//上月
			ca.add(Calendar.MONTH, 1);
			ca.add(Calendar.YEAR, -1);
			list.add(sdf.format(ca.getTime()));
		}else if("3".equals(dateSign)){
			sdf=new SimpleDateFormat("yyyy-MM-dd");
			String[] dateArr=date.split("-");
			ca.set(Calendar.YEAR, Integer.parseInt(dateArr[0]));
			ca.set(Calendar.MONTH, Integer.parseInt(dateArr[1])-1);
			ca.set(Calendar.DATE, Integer.parseInt(dateArr[2]));
			list.add(date);//当月
			ca.add(Calendar.MONTH, -1);
			list.add(sdf.format(ca.getTime()));//上月
			ca.add(Calendar.MONTH, 1);
			ca.add(Calendar.YEAR, -1);
			list.add(sdf.format(ca.getTime()));
		}else{
			list.add(date);
		}
		return list;
	}
	
	/**
	 * 环比
	 * @param date
	 * @param dateSing
	 * @return
	 */
	public String[] conYear(String date,String dateSing){
		 Calendar ca = Calendar .getInstance();
		 SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		 SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM");
		 SimpleDateFormat sdf2=new SimpleDateFormat("yyyy");
		 try {
			 if("1".equals(dateSing)){
				date=sdf.format(sdf2.parse(date));
			 }else if("2".equals(dateSing)){
				 date=sdf.format(sdf1.parse(date)); 
			 }
		 } catch (ParseException e) {
			} 
		 String[] dateOne =date.split("-");
		 String[] strArr=new String[6];
		 ca.set(Integer.parseInt(dateOne[0]), Integer.parseInt(dateOne[1])-1, Integer.parseInt(dateOne[2]));
		for(int i=0;i<6;i++){
			if("1".equals(dateSing)){
				strArr[i]=sdf2.format(ca.getTime());
				ca.add(Calendar.YEAR, -1);
			}else if("2".equals(dateSing)){
				strArr[i]=sdf1.format(ca.getTime());
				ca.add(Calendar.MONTH, -1);
			}else{
				strArr[i]=sdf.format(ca.getTime());
				ca.add(Calendar.DATE, -1);
			}
		}
		 return strArr;
	}
	/**
	 * 同比
	 * @param date
	 * @param dateSing
	 * @return
	 */
	public String[] conMonth(String date,String dateSing){
		String [] strArr=new String[6];
		String[] dateArr=date.split("-");
		int dateTemp=Integer.parseInt(dateArr[0]);
		for(int i=0;i<6;i++){
			if("2".equals(dateSing)){//月同比
				strArr[i]=(dateTemp-i)+"-"+dateArr[1];
			}else{
				strArr[i]=(dateTemp-i)+"-"+dateArr[1]+"-"+dateArr[2];
			}
		}
		
		return strArr;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;  
	}
	public String getDateSign() {
		return dateSign;
	}
	public void setDateSign(String dateSign) {
		this.dateSign = dateSign;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getEtime() {
		return Etime;
	}
	public void setEtime(String etime) {
		Etime = etime;
	}
	public String getStime() {
		return Stime;
	}
	public void setStime(String stime) {
		Stime = stime;
	}
	
	
	
}
