package cn.honry.statistics.deptstat.kidneyDiseaseWithDept.action;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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

import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.department.dao.DeptInInterDAO;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.system.userMenuDataJuris.service.DataJurisInInterService;
import cn.honry.report.service.IReportService;
import cn.honry.statistics.deptstat.kidneyDiseaseWithDept.service.ItemVoService;
import cn.honry.statistics.deptstat.kidneyDiseaseWithDept.vo.DeptVo;
import cn.honry.statistics.deptstat.kidneyDiseaseWithDept.vo.DeptVoToIReport;
import cn.honry.statistics.deptstat.kidneyDiseaseWithDept.vo.ItemVo;
import cn.honry.statistics.deptstat.kidneyDiseaseWithDept.vo.KidneyDiseaseWithDeptVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.DownloadUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.NumberUtil;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.opensymphony.xwork2.ActionSupport;


@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/statistics/kidneyDisease")
public class KidneyDiseaseWithDeptAction extends ActionSupport{
	private static final long serialVersionUID = 8535596438329082479L;
	private HttpServletRequest request = ServletActionContext.getRequest();
	private HttpServletResponse response = ServletActionContext.getResponse();
	private String webPath="WEB-INF"+File.separator+"reportFormat"+File.separator+"jasper"+File.separator;
	private Logger logger=Logger.getLogger(KidneyDiseaseWithDeptAction.class);
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;
	}
	@Autowired
	@Qualifier(value="iReportService")
	private IReportService iReportService;
	public void setiReportService(IReportService iReportService) {
		this.iReportService = iReportService;
	}
	//把数据存入mongodb中
	@Autowired
	@Qualifier(value = "itemVoService")
	private ItemVoService itemVoService;
	@Autowired
	@Qualifier(value = "deptInInterService")
	private DeptInInterService deptInInterService;
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}
	@Autowired
	@Qualifier(value = "dataJurisInInterService")
	private DataJurisInInterService dataJurisInInterService;
	public void setDataJurisInInterService(DataJurisInInterService dataJurisInInterService) {
		this.dataJurisInInterService = dataJurisInInterService;
	}
	/**
	 * 栏目别名,在主界面中点击栏目时传到action的参数
	 */ 
	private String menuAlias;
	private String ETime;
	private String deptCode;
	private String date;
	private String acount;//当前登录科室code
	private String acountName;//当前登录科室name
	public String getAcountName() {
		return acountName;
	}
	public void setAcountName(String acountName) {
		this.acountName = acountName;
	}
	public String getAcount() {
		return acount;
	}
	public void setAcount(String acount) {
		this.acount = acount;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public ItemVoService getItemVoService() {
		return itemVoService;
	}
	public void setItemVoService(ItemVoService itemVoService) {
		this.itemVoService = itemVoService;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getETime() {
		return ETime;
	}
	public void setETime(String eTime) {
		ETime = eTime;
	}
	
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	/**
	 * 肾病科室对比表
	 * @return
	 */
	@RequiresPermissions(value={"SBKSDBB:function:view"})
	@Action(value = "listkidneyDisease", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/deptstat/kidneyDiseaseWithDept/kidneyDiseaseWithDeptList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listIllMedical() {
		SysDepartment longinDept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		acount = longinDept == null ? "" : longinDept.getDeptCode();
		acountName = longinDept == null ? "" : longinDept.getDeptName();
		Date date = new Date();
		ETime = DateUtils.formatDateY_M(date);
		return "list";
	}
	/**  
	 * 
	 * 通过科室日期查询内容
	 * @Author: huzhenguo
	 * @CreateDate: 2017年6月2日 下午8:21:57 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年6月2日 下午8:21:57 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Action(value="quertItemVo")
	public void quertItemVo(){
		boolean collection = new MongoBasicDao().isCollection("T_KSDBB");//判断mongon中是否存在该表
//		String acount = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
//		List<SysDepartment> deptByauthority = dataJurisInInterService.getJurisDeptList(menuAlias, acount);
		KidneyDiseaseWithDeptVo vo=null;//入院人次
		KidneyDiseaseWithDeptVo vo2=null;//出院人次
		KidneyDiseaseWithDeptVo vo3=null;//病床周转次数
		KidneyDiseaseWithDeptVo vo4=null;//病床使用率（%）
		KidneyDiseaseWithDeptVo vo5=null;//平均住院天数
		KidneyDiseaseWithDeptVo vo6=null;//住院抢救成功率（%）
		KidneyDiseaseWithDeptVo vo7=null;//门诊工作量
		KidneyDiseaseWithDeptVo vo8=null;//总收入（万元）
		KidneyDiseaseWithDeptVo vo9=null;//其中：门诊收入
		KidneyDiseaseWithDeptVo vo10=null;//住院收入
		ItemVo LastItemVo = null;
		ItemVo itemVo = null;
		List<KidneyDiseaseWithDeptVo> list=new ArrayList<KidneyDiseaseWithDeptVo>();
		if (StringUtils.isNotBlank(deptCode)) {
			String[] codes = deptCode.split(",");
			for (String  code : codes) {
				SysDepartment department = deptInInterService.getDeptCode(code);
				vo=new KidneyDiseaseWithDeptVo();
				vo2=new KidneyDiseaseWithDeptVo();
				vo3=new KidneyDiseaseWithDeptVo();
				vo4=new KidneyDiseaseWithDeptVo();
				vo5=new KidneyDiseaseWithDeptVo();
				vo6=new KidneyDiseaseWithDeptVo();
				vo7=new KidneyDiseaseWithDeptVo();
				vo8=new KidneyDiseaseWithDeptVo();
				vo9=new KidneyDiseaseWithDeptVo();
				vo10=new KidneyDiseaseWithDeptVo();
				if (collection==false) {
					itemVo = itemVoService.quertItemVo(date, code);
					LastItemVo = itemVoService.quertItemVo(this.getLastYear(date), code);
				}else{
					itemVo = itemVoService.itemVos(date,  code);
					LastItemVo = itemVoService.itemVos(this.getLastYear(date), code);
				}
				if (itemVo!=null || LastItemVo!=null) {
					vo.setDeptName(itemVo.getDept_name()==null?department.getDeptName():itemVo.getDept_name());
					vo.setXiangmu("入院人次");
					vo.setLastDate(String.valueOf(LastItemVo.getRuYuNum()));
					vo.setDate(String.valueOf(itemVo.getRuYuNum()));
					vo.setDiffer(String.valueOf(itemVo.getRuYuNum()-LastItemVo.getRuYuNum()));
					if (LastItemVo.getRuYuNum()==0) {
						vo.setDifferPer("0.0");
					}else{
						vo.setDifferPer(NumberUtil.init().format(((itemVo.getRuYuNum()-LastItemVo.getRuYuNum())/LastItemVo.getRuYuNum())*100, 2));
					}
					
					vo2.setDeptName(itemVo.getDept_name()==null?department.getDeptName():itemVo.getDept_name());
					vo2.setXiangmu("出院人次");
					vo2.setLastDate(String.valueOf(LastItemVo.getChuYUNum()));
					vo2.setDate(String.valueOf(itemVo.getChuYUNum()));
					vo2.setDiffer(String.valueOf(itemVo.getChuYUNum()-LastItemVo.getChuYUNum()));
					if (LastItemVo.getChuYUNum()==0) {
						vo2.setDifferPer("0.0");
					}else{
						vo2.setDifferPer(NumberUtil.init().format(((itemVo.getChuYUNum()-LastItemVo.getChuYUNum())/LastItemVo.getChuYUNum())*100, 2));
					}
					
					vo3.setDeptName(itemVo.getDept_name()==null?department.getDeptName():itemVo.getDept_name());
					vo3.setXiangmu("病床周转次数");
					vo3.setLastDate("-");
					vo3.setDate("-");
					vo3.setDiffer("-");
					vo3.setDifferPer("-");
					
					vo4.setDeptName(itemVo.getDept_name()==null?department.getDeptName():itemVo.getDept_name());
					vo4.setXiangmu("病床使用率（%）");
					if (LastItemVo.getBeds()==0) {
						vo4.setLastDate("0.0");
					}else{
						vo4.setLastDate(NumberUtil.init().format((LastItemVo.getBedUsed()/LastItemVo.getBeds())*100,2));
					}
					if (itemVo.getBeds()==0) {
						vo4.setDate("0.0");
					}else{
						vo4.setDate(NumberUtil.init().format((itemVo.getBedUsed()/itemVo.getBeds())*100,2));
					}
					if (LastItemVo.getBeds()==0 || itemVo.getBeds()==0) {
						vo4.setDiffer("0.0");
					}else{
						vo4.setDiffer(NumberUtil.init().format((itemVo.getBedUsed()/itemVo.getBeds())-(LastItemVo.getBedUsed()/LastItemVo.getBeds())*100,2));
					}
					if (LastItemVo.getBeds()==0 || itemVo.getBeds()==0 || (LastItemVo.getBedUsed()/LastItemVo.getBeds())==0) {
						vo4.setDifferPer("0.0");
					}else{
						vo4.setDifferPer(NumberUtil.init().format(((itemVo.getBedUsed()/itemVo.getBeds())-(LastItemVo.getBedUsed()/LastItemVo.getBeds()))/(LastItemVo.getBedUsed()/LastItemVo.getBeds())*100, 2));
					}
					
					vo5.setDeptName(itemVo.getDept_name()==null?department.getDeptName():itemVo.getDept_name());
					vo5.setXiangmu("平均住院天数");
					vo5.setLastDate(String.valueOf(NumberUtil.init().format(LastItemVo.getAvgInYuDays(),1)));
					vo5.setDate(String.valueOf(NumberUtil.init().format(itemVo.getAvgInYuDays(),1)));
					vo5.setDiffer(String.valueOf(NumberUtil.init().format(itemVo.getAvgInYuDays()-LastItemVo.getAvgInYuDays(),1)));
					if (LastItemVo.getAvgInYuDays()==0) {
						vo5.setDifferPer("0.0");
					}else{
						vo5.setDifferPer(NumberUtil.init().format(((itemVo.getAvgInYuDays()-LastItemVo.getAvgInYuDays())/LastItemVo.getAvgInYuDays())*100, 2));
					}
					
					vo6.setDeptName(itemVo.getDept_name()==null?department.getDeptName():itemVo.getDept_name());
					vo6.setXiangmu("住院抢救成功率（%）");
					vo6.setLastDate("-");
					vo6.setDate("-");
					vo6.setDiffer("-");
					vo6.setDifferPer("-");
					
					vo7.setDeptName(itemVo.getDept_name()==null?department.getDeptName():itemVo.getDept_name());
					vo7.setXiangmu("门诊工作量");
					vo7.setLastDate(String.valueOf(LastItemVo.getWorkNum()));
					vo7.setDate(String.valueOf(itemVo.getWorkNum()));
					vo7.setDiffer(String.valueOf(itemVo.getWorkNum()-LastItemVo.getWorkNum()));
					if (LastItemVo.getWorkNum()==0) {
						vo7.setDifferPer("0.0");
					}else{
						vo7.setDifferPer(NumberUtil.init().format(((itemVo.getWorkNum()-LastItemVo.getWorkNum())/LastItemVo.getWorkNum())*100, 2));
					}
					
					vo8.setDeptName(itemVo.getDept_name()==null?department.getDeptName():itemVo.getDept_name());
					vo8.setXiangmu("总收入（万元）");
					vo8.setLastDate(NumberUtil.init().format((LastItemVo.getMenCost()+LastItemVo.getZhuCost()),4));
					vo8.setDate(NumberUtil.init().format((itemVo.getMenCost()+itemVo.getZhuCost()),4));
					vo8.setDiffer(NumberUtil.init().format((itemVo.getMenCost()+itemVo.getZhuCost()-LastItemVo.getMenCost()-LastItemVo.getZhuCost()),4));
					if ((LastItemVo.getMenCost()+LastItemVo.getZhuCost())==0) {
						vo8.setDifferPer("0");
					}else{
						vo8.setDifferPer(NumberUtil.init().format(((itemVo.getMenCost()+itemVo.getZhuCost()-LastItemVo.getMenCost()-LastItemVo.getZhuCost())/(LastItemVo.getMenCost()+LastItemVo.getZhuCost()))*100, 2));
					}
					
					vo9.setDeptName(itemVo.getDept_name()==null?department.getDeptName():itemVo.getDept_name());
					vo9.setXiangmu("其中：门诊收入");
					vo9.setLastDate(NumberUtil.init().format((LastItemVo.getMenCost()),4));
					vo9.setDate(NumberUtil.init().format((itemVo.getMenCost()),4));
					vo9.setDiffer(NumberUtil.init().format((itemVo.getMenCost()-LastItemVo.getMenCost()),4));
					if (LastItemVo.getMenCost()==0) {
						vo9.setDifferPer("0.0");
					}else{
						vo9.setDifferPer(NumberUtil.init().format(((itemVo.getMenCost()-LastItemVo.getMenCost())/LastItemVo.getMenCost())*100, 2));
					}
					
					vo10.setDeptName(itemVo.getDept_name()==null?department.getDeptName():itemVo.getDept_name());
					vo10.setXiangmu("&nbsp;&nbsp;&nbsp;住院收入");
					vo10.setLastDate(NumberUtil.init().format((LastItemVo.getZhuCost()),4));
					vo10.setDate(NumberUtil.init().format((itemVo.getZhuCost()),4));
					vo10.setDiffer(NumberUtil.init().format((itemVo.getZhuCost()-LastItemVo.getZhuCost()),4));
					if (LastItemVo.getZhuCost()==0) {
						vo10.setDifferPer("0.0");
					}else{
						vo10.setDifferPer(NumberUtil.init().format(((itemVo.getZhuCost()-LastItemVo.getZhuCost())/LastItemVo.getZhuCost())*100, 2));
					}
					
					list.add(vo);
					list.add(vo2);
					list.add(vo3);
					list.add(vo4);
					list.add(vo5);
					list.add(vo6);
					list.add(vo7);
					list.add(vo8);
					list.add(vo9);
					list.add(vo10);
				}
			}
		}
		String json = JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}
	/**  
	 * 
	 * 封装求上一年同月
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月5日 上午9:40:13 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月5日 上午9:40:13 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	private String getLastYear(String date){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		Calendar calendar = Calendar.getInstance();
		Date stime = null;
		try {
			stime = format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        calendar.setTime(stime);
        calendar.add(Calendar.YEAR, -1);
        Date d = calendar.getTime();
        //上年
        String lastDate = format.format(d);
		return lastDate;
	}
	/**  
	 * 
	 * 导出
	 * @Author: huzhenguo
	 * @CreateDate: 2017年6月6日 下午7:06:25 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年6月6日 下午7:06:25 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Action(value="expKidneyDiseaseWithDept")
	public void expKidneyDiseaseWithDept() throws IOException{
		try {
			String rows=request.getParameter("rows");
			String exlToLastTime=request.getParameter("exlToLastTime");
			String exlToTime=request.getParameter("exlToTime");
			String viewTitle=request.getParameter("viewTitle");
			Gson gson = new Gson();
			List<KidneyDiseaseWithDeptVo> list = gson.fromJson(rows, new TypeToken<List<KidneyDiseaseWithDeptVo>>(){}.getType());
			if (list == null || list.isEmpty()) {
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write("根据您选择的下载条件，不存在具备您要求的记录！");
			}
			String name = "科室对比表";
			String fileName = name + DateUtils.formatDateY_M_D_H_M(new Date())+ ".csv";
			String filePath = ServletActionContext.getServletContext().getRealPath("/WEB-INF") + "/modelExcel/"+fileName;
			//创建工作文档对象     
			HSSFWorkbook workbook = new HSSFWorkbook();
			// 生成一个表格
			HSSFSheet sheet = workbook.createSheet("sheet");
			// 设置表格默认列宽度为15个字节
			sheet.setDefaultColumnWidth((short) 17);
			// 生成一个样式
			HSSFCellStyle style = workbook.createCellStyle();
			// 设置这些样式
			style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
			style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			String[] headers = { "科室", "项目", exlToLastTime, exlToTime,"增减数", "增减率(%)"};
			// 表单名称
			HSSFRow tableName = sheet.createRow(0);
			HSSFCell cellTableName = tableName.createCell(0);
			cellTableName.setCellValue(viewTitle);// 第一行题目内容
			cellTableName.setCellStyle(style);// 给第一行加样式
			Region region = new Region(0, (short) 0, 0, (short) (headers.length - 1));
			sheet.addMergedRegion(region);
			// 产生表格标题行
			HSSFRow row = sheet.createRow(1);
			for (short ii = 0; ii < headers.length; ii++){
				HSSFCell cell = row.createCell(ii);
				cellTableName.setCellStyle(style);
				HSSFRichTextString text = new HSSFRichTextString(headers[ii]);
				cell.setCellValue(text);
			}
			int index = 1;
			int rowbased=2;
			//数据
			Map<DeptVo, List<KidneyDiseaseWithDeptVo>> temp = new HashMap<DeptVo, List<KidneyDiseaseWithDeptVo>>();  
			List<KidneyDiseaseWithDeptVo> devices =new ArrayList<KidneyDiseaseWithDeptVo>();
			DeptVo deptVo=null;
			for (KidneyDiseaseWithDeptVo kvo : list) {
				if(kvo.getXiangmu().contains("&nbsp;")){
				   	String replace = kvo.getXiangmu().replace("&nbsp;", "  ");//替换空格,加2个空格
				   	kvo.setXiangmu(replace);
				}
				devices.add(kvo);
				deptVo=new DeptVo();
				deptVo.setDeptNameString(kvo.getDeptName());
				temp.put(deptVo, devices);
			}
			if (!temp.isEmpty()) {
				Field[] accountFields =deptVo.getClass().getDeclaredFields();
				for (int d = 0; d < devices.size(); d++) {
					int columnNumber=accountFields.length;
					index++;
					row = sheet.createRow(index);
					KidneyDiseaseWithDeptVo order = devices.get(d);
					Field[] orderFields = order.getClass().getDeclaredFields();
					for (int s = 1; s < orderFields.length; s++) {
						HSSFCell cell = row.createCell(columnNumber);
						Field field = orderFields[s];
						String fieldName = field.getName();
						String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
						@SuppressWarnings("rawtypes")
						Class tCls = order.getClass();
						@SuppressWarnings("unchecked")
						Method getMethod = tCls.getMethod(getMethodName, new Class[] {});
						Object value = getMethod.invoke(order, new Object[] {});
						String textValue = "";
						if (value != null || "".equals(value)){
							textValue = value.toString();
						}
						cell.setCellValue(textValue);
						columnNumber++;
					}
					for(int i=0;i<accountFields.length;i++){
						HSSFCell cell = row.createCell(i);
						Field field = accountFields[i];
						String fieldName = field.getName();
						String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
						@SuppressWarnings("rawtypes")
						Class tCls = deptVo.getClass();
						@SuppressWarnings("unchecked")
						Method getMethod = tCls.getMethod(getMethodName, new Class[] {});
						Object value = getMethod.invoke(deptVo, new Object[] {});
						String textValue = "";
						if (value != null || "".equals(value)){
							textValue = value.toString();
						}
						cell.setCellValue(textValue);
					}
				}
				for(int i=0;i<accountFields.length;i++){
					if(devices.size()>1){
						sheet.addMergedRegion(new CellRangeAddress(rowbased, rowbased+devices.size()-1, 0, 0));
					}else{
						sheet.addMergedRegion(new CellRangeAddress(rowbased, rowbased, 0, 0));
					}
				}
				rowbased=rowbased+devices.size();
			}
			 FileOutputStream fout = new FileOutputStream(filePath);  
			 workbook.write(fout);  
			 fout.close();  
			 DownloadUtils.download(request, response, filePath, HisParameters.PREFIXFILENAME + fileName);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("KSTG_KSDBB", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTG_KSDBB", "科室统计_科室对比表", "2", "0"), e);
		}
	}
	/**  
	 * 
	 * 打印
	 * @Author: huzhenguo
	 * @CreateDate: 2017年6月6日 下午7:06:25 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年6月6日 下午7:06:25 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Action(value="iReportKidneyDiseaseWithDept")
	public void iReportKidneyDiseaseWithDept(){
		try{
		   //jasper文件名称 不含后缀
		   String rows = request.getParameter("reportToRows"); 
		   String fileName = request.getParameter("fileName");
		   String exlToLastTime=request.getParameter("exlToLastTime");
		   String exlToTime=request.getParameter("exlToTime");
		   String viewCaption = request.getParameter("viewCaption");
		   String root_path = request.getSession().getServletContext().getRealPath("/");
		   root_path = root_path.replace('\\', '/');
		   String reportFilePath = root_path + webPath +fileName+".jasper";
		   Gson gson = new Gson();
		   List<KidneyDiseaseWithDeptVo> list = gson.fromJson(rows, new TypeToken<List<KidneyDiseaseWithDeptVo>>(){}.getType());
		   //javaBean数据封装
		   ArrayList<DeptVoToIReport> voList = new ArrayList<DeptVoToIReport>();
		   DeptVoToIReport vo = null;
		   int i=0;
		   Map<String, List<KidneyDiseaseWithDeptVo>> temp = new HashMap<String, List<KidneyDiseaseWithDeptVo>>();  
		   List<KidneyDiseaseWithDeptVo> devices =new ArrayList<KidneyDiseaseWithDeptVo>();
		   for (KidneyDiseaseWithDeptVo kvo : list) {
			   if(kvo.getXiangmu().contains("&nbsp;")){
				   	String replace = kvo.getXiangmu().replace("&nbsp;", "     ");//替换空格,加2个空格
				   	kvo.setXiangmu(replace);
				}		
			   boolean flag = temp.containsKey(kvo.getDeptName());
			   if (flag==true) {
				   i++;
				   devices.add(kvo);
				   temp.put(kvo.getDeptName(), devices);
				   if (i==9) {
					   i=0;
					   vo = new DeptVoToIReport();
					   vo.setDeptString(kvo.getDeptName());
					   vo.setList(devices);
					   voList.add(vo);
				   }
			   }else{
				   devices=new ArrayList<KidneyDiseaseWithDeptVo>();
				   devices.add(kvo);
				   temp.put(kvo.getDeptName(), devices);
			   }
		   }
		  
		   Map<String, Object> parameters = new HashMap<String, Object>();
		   JRDataSource jrd=new JRBeanCollectionDataSource(voList);
		   parameters.put("hName", viewCaption);
		   parameters.put("exlToLastTime", exlToLastTime);
		   parameters.put("exlToTime", exlToTime);
		   parameters.put("SUBREPORT_DIR", root_path + webPath);
		   iReportService.doReportToJavaBean(request,WebUtils.getResponse(),reportFilePath,parameters,jrd);
		  }catch(Exception e){
		     e.printStackTrace();
		     logger.error("KSTG_KSDBB", e);
			 hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTG_KSDBB", "科室统计_科室对比表", "2", "0"), e);
		  }
	}
}
