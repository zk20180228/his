package cn.honry.inner.statistics.hospitalday.action;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.honry.base.bean.model.BusinessYzcxzhcx;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.statistics.hospitalday.service.HospitaldayinnerService;
import cn.honry.inner.statistics.hospitalday.vo.HospitaldayVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("all")
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value={@InterceptorRef("manageInterceptor")})
@Namespace(value="/inner/hospitalday")
public class HospitaldayinnerAction extends ActionSupport{
	private String menuAlias;//栏目
	private String startTime;//开始时间
	private String endTime;//结束时间
	private String upDateRow;//更新
	
	public String getUpDateRow() {
		return upDateRow;
	}
	public void setUpDateRow(String upDateRow) {
		this.upDateRow = upDateRow;
	}
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	@Autowired
	@Qualifier(value = "hospitaldayinnerService")
	private HospitaldayinnerService hospitaldayinnerService;

	public void setHospitaldayinnerService(
			HospitaldayinnerService hospitaldayinnerService) {
		this.hospitaldayinnerService = hospitaldayinnerService;
	}
	
	private Logger logger=Logger.getLogger(HospitaldayinnerAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	/**  
	 * 
	 * 从郑东库导每日运营数据导28库
	 * @Author: donghe
	 * @CreateDate: 2017年7月25日 下午4:09:43 
	 * @Modifier: donghe
	 * @ModifyDate: 2017年7月25日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Action(value = "saveBusinessYzcxzhcx")
	public void saveBusinessYzcxzhcx(){
		try {
			List<BusinessYzcxzhcx> list = hospitaldayinnerService.queryBusinessYzcxzhcxMaxdate();
			String startTime= "";
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, -1); //得到前一天
			Date date = calendar.getTime();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String yestoday=df.format(date);
			if(list.size()==0){
				startTime = "2010-12-11";
			}else{
				startTime=df.format(list.get(0).getOperDate());
			}
			
			Date sdate = DateUtils.parseDateY_M_D(startTime);
			Date edate = DateUtils.parseDateY_M_D(yestoday);
			if(sdate.compareTo(edate)!=0){
				int i=0;
				while(true){
					Date addDay = DateUtils.addDay(sdate, i);
					Date addDay1 = DateUtils.addDay(sdate, i+1);
					final String ymd = DateUtils.formatDateY_M_D(addDay1);
					if(addDay.compareTo(edate)>=0){
						break;
					}
					logger.info("开始查询"+ymd+"的数据");
					hospitaldayinnerService.saveBusinessYzcxzhcx(ymd);
					i++;
				}
			}
		} catch (Exception e) {
			logger.error("JYRB", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("JYRB", "经营日报_数据导入", "2", "0"), e);
		}
	}
	/**  
	 * 
	 * 从郑东库导每日运营数据导28库
	 * @Author: donghe
	 * @CreateDate: 2017年7月25日 下午4:09:43 
	 * @Modifier: donghe
	 * @ModifyDate: 2017年7月25日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Action(value = "saveBusiness")
	public void saveBusiness(){
		Map<String,String> map=new HashMap<String,String>();
		try {
			Date sdate = DateUtils.parseDateY_M_D(startTime);
			Date edate = DateUtils.parseDateY_M_D(endTime);
			if(sdate.compareTo(edate)!=0){
				int i=0;
				while(true){
					Date addDay = DateUtils.addDay(sdate, i);
					Date addDay1 = DateUtils.addDay(sdate, i+1);
					final String ymd = DateUtils.formatDateY_M_D(addDay1);
					if(addDay.compareTo(edate)>=0){
						break;
					}
					logger.info("开始查询"+ymd+"的数据");
					map=hospitaldayinnerService.saveBusinessYzcxzhcx(ymd);
					i++;
				}
			}else{
				map.put("resCode", "info");
				map.put("resMsg", "初始化成功");
			}
		} catch (Exception e) {
			map.put("resCode", "error");
			map.put("resMsg", "初始化失败");
			logger.error("YDPT_YWSJCSH_JYRBCSH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YDPT_YWSJCSH_JYRBCSH", "移动平台_业务数据初始化_业务数据初始化", "2", "0"), e);
		}
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	/**  
	 * 
	 * 从郑东库导每日运营数据导28库
	 * @Author: donghe
	 * @CreateDate: 2017年7月25日 下午4:09:43 
	 * @Modifier: donghe
	 * @ModifyDate: 2017年7月25日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Action(value = "businessYzcxzhcx")
	public void businessYzcxzhcx(){
		try {
			hospitaldayinnerService.init_DRJYSJinner("2017-04-15",null,null);
			String json=JSONUtils.toJson(null);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("YDPT_YWSJCSH_JYRBCSH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YDPT_YWSJCSH_JYRBCSH", "移动平台_业务数据初始化_业务数据导入失败", "2", "0"), e);
		}
	}

	/**  
	 * 
	 * 对T_BUSINESS_YZCX_ZHCX的增加和修改
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月12日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月12日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Action(value = "JdbcUtils")
	static void update() throws SQLException{
		Connection conn=null;  
		Statement st=null;  
		ResultSet resultset=null;  
		try {  
		    //2.建立连接  
		   conn=JdbcUtils.getConnection();  
		   //3.创建语句  
		   st=conn.createStatement();  
		        //4.执行语句  
		   String sql="UPDATE T_BUSINESS_YZCX_ZHCX set PZ_NUM=?, YMZ_NUM=?, QTPT_NUM=?, ZMZJ_NUM=?, JS_NUM=?, FJS_NUM=?, JZ_NUM=?, SFHJ_NUM=?,	SFZF_NUM=?,	CFHJ_NUM=?,	CFZF_NUM=?,	ZDCF_COST=?, ZXCF_COST=?, CFHJ_COST=?, RY_NUM=?, ZK_NUM=?, CYWJ_NUM=?,	CYYJ_NUM=?,	ZYHJ_NUM=?,	ZYZF_NUM=?,	ZYNH_NUM=?,	MZYP_COST=?, MZYL_COST=?, ZYYP_COST=?, ZYYL_COST=?, MZSS_NUM=?,	MZSS_COST=?, ZYSS_NUM=?,	ZYSS_COST=?, OPER_DATE=?, BED_BZ=?, SJRY_NUM=?, GHSR_COST=?, TSH_NUM=?, GHHJ_RS=?, YQ=?, GHHJ_NUM=?, SSTS=?";  
		   int i=st.executeUpdate(sql);  
		       
		    System.out.println("i="+i);  
		    }
			finally{  
		         JdbcUtils.free(resultset, st, conn);  
		    }  
		}  
		static void create() throws SQLException{  
		    Connection conn=null;  
		    Statement st=null;  
		    ResultSet resultset=null;  
		      
		   try {  
		   //2.建立连接  
		    conn=JdbcUtils.getConnection();  
		  //3.创建语句  
		   st=conn.createStatement();  
		   //4.执行语句  
		   String sql="INSERT INTO T_BUSINESS_YZCX_ZHCX(PZ_NUM, JYMZ_NUM, QTPT_NUM, ZMZJ_NUM, JS_NUM, FJS_NUM, JZ_NUM, SFHJ_NUM, SFZF_NUM, CFHJ_NUM, CFZF_NUM, ZDCF_COST, ZXCF_COST, CFHJ_COST, RY_NUM, ZK_NUM, CYWJ_NUM, CYYJ_NUM, ZYHJ_NUM, ZYZF_NUM, ZYNH_NUM, MZYP_COST,	MZYL_COST, ZYYP_COST, ZYYL_COST, MZSS_NUM, MZSS_COST, ZYSS_NUM, ZYSS_COST, OPER_DATE, BED_BZ, SJRY_NUM, GHSR_COST, TSH_NUM, GHHJ_RS, YQ, GHHJ_NUM, SSTS) values('','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','')";  
		   int i=st.executeUpdate(sql);  
		         System.out.println("i="+i);  
		    }
		   finally{  
		        JdbcUtils.free(resultset, st, conn);  
		   }  
		}
		@Action(value = "delHospital")
		public void delHospital(){
			Map<String,String> map=new HashMap<String,String>();
			try {
				if(StringUtils.isNotBlank(startTime)){
					BasicDBObject document=new BasicDBObject();
					String delMongo="DRJYSJ_DAY";
					if(new MongoBasicDao().isCollection(delMongo))
					document.append("timeValue", startTime);
					new MongoBasicDao().remove(delMongo, document);
					
				}
				map.put("resCode", "success");
				map.put("resMsg", "删除成功,时间:"+startTime);
			} catch (Exception e) {
				logger.error("YDPT_YWSJCSH_JYRBCSH", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YDPT_YWSJCSH_JYRBCSH", "移动平台_业务数据初始化_业务数据删除失败", "2", "0"), e);
				map.put("resCode", "error");
				map.put("resMsg", "删除失败,时间:"+startTime);
			}
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		}
		@Action(value = "updateHospital")
		public void updateHospital(){
			Map<String,String> map=new HashMap<String,String>();
			try {
				if(StringUtils.isNotBlank(upDateRow)){
					List<HospitaldayVo> voList = JSONUtils.fromJson(upDateRow, new TypeToken<List<HospitaldayVo>>(){});
					if(voList!=null&&voList.size()>0){
						for(HospitaldayVo vo:voList){
							if(StringUtils.isNotBlank(vo.getTimeValue())){
								Document doucment1=new Document();
								doucment1.append("timeValue", vo.getTimeValue());
								if(StringUtils.isBlank(startTime)){
									startTime=vo.getTimeValue()+"号,";
								}else{
									startTime+=vo.getTimeValue()+"号,";
								}
								Document document = new Document();
								document.append("drugProportion", vo.getDrugProportion());
								document.append("drugProportionHy", vo.getDrugProportionHy());
								document.append("drugProportionZd", vo.getDrugProportionZd());
								document.append("drugProportionHj", vo.getDrugProportionHj());
								
								document.append("incomeCost", vo.getIncomeCost());
								document.append("incomeCostHy", vo.getIncomeCostHy());
								document.append("incomeCostZd", vo.getIncomeCostZd());
								document.append("incomeCostHj", vo.getIncomeCostHj());
								
								document.append("inHospitalNum", vo.getInHospitalNum());
								document.append("inHospitalNumHy", vo.getInHospitalNumHy());
								document.append("inHospitalNumZd", vo.getInHospitalNumZd());
								document.append("inHospitalNumHj", vo.getInHospitalNumHj());
								
								document.append("leaveHospitalNum", vo.getLeaveHospitalNum());
								document.append("leaveHospitalNumHy", vo.getLeaveHospitalNumHy());
								document.append("leaveHospitalNumZd", vo.getLeaveHospitalNumZd());
								document.append("leaveHospitalNumHj", vo.getLeaveHospitalNumHj());
								
								document.append("operationNum", vo.getOperationNum());
								document.append("operationNumHy",vo.getOperationNumHy());
								document.append("operationNumZd", vo.getOperationNumZd());
								document.append("operationNumHj", vo.getOperationNumHj());
								
								document.append("inpatientNum", vo.getInpatientNum());
								document.append("inpatientNumHy", vo.getInpatientNumHy());
								document.append("inpatientNumZd", vo.getInpatientNumZd());
								document.append("inpatientNumHj", vo.getInpatientNumHj());
								
								document.append("outpatientNum", vo.getOutpatientNum());
								document.append("outpatientNumHy", vo.getOutpatientNumHy());
								document.append("outpatientNumZd", vo.getOutpatientNumZd());
								document.append("outpatientNumHj", vo.getOutpatientNumHj());
								new MongoBasicDao().update("DRJYSJ_DAY", doucment1, document, true);
							
							}
						}
					}
				}
				if(StringUtils.isNotBlank(startTime)){
					startTime=startTime.substring(0, startTime.length()-1);
				}
				map.put("resCode", "success");
				map.put("resMsg", "修改成功,时间:"+startTime);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("YDPT_YWSJCSH_JYRBCSH", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YDPT_YWSJCSH_JYRBCSH", "移动平台_业务数据初始化_业务数据修改失败", "2", "0"), e);
				map.put("resCode", "error");
				map.put("resMsg", "修改失败,时间:"+startTime);
			}
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		}



}
