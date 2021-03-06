package cn.honry.mobile.updateVersion.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
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

import cn.honry.base.bean.model.MApkVersion;
import cn.honry.base.bean.model.RecordToMobileException;
import cn.honry.inner.system.upload.service.UploadFileService;
import cn.honry.mobile.exceptionLog.service.ExceptionLogService;
import cn.honry.mobile.updateVersion.service.UpdateVersionService;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace("/mosys/updateVersion")
public class UpdateVersionAction extends ActionSupport{

	
	private Logger logger = Logger.getLogger(UpdateVersionAction.class);
	
	@Autowired
	@Qualifier(value = "exceptionLogService")
	private ExceptionLogService exceptionLogService;
	public void setExceptionLogService(ExceptionLogService exceptionLogService) {
		this.exceptionLogService = exceptionLogService;
	}
	
	@Autowired
	@Qualifier(value = "uploadFileService")
	private UploadFileService uploadFileService;
	public void setUploadFileService(UploadFileService uploadFileService) {
		this.uploadFileService = uploadFileService;
	}

	@Autowired
	@Qualifier(value = "updateVersionService")
	private UpdateVersionService updateVersionService;
	public void setUpdateVersionService(UpdateVersionService updateVersionService) {
		this.updateVersionService = updateVersionService;
	}

	private File mFile;
	private MApkVersion mApkVersion;
	private String page;
	private String rows;
	private String queryNum;
	private String queryName;
	private String queryFlg;
	private String addr;
	private String id;
	private String ids;
	@RequiresPermissions(value={"BBSJ:function:view"})
	@Action(value = "updateVersionList", results = { @Result(name = "list", location = "/WEB-INF/pages/mobile/versions/versionList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String updateVersionList(){
		return "list";
	}
	
	
	/** 查询版本信息列表
	* @Title: findVersionList 查询版本信息列表
	* @author zxl 
	* @date 2017年6月15日
	*/
	@RequiresPermissions(value={"BBSJ:function:query"})
	@Action(value = "findVersionList")
	public void findVersionList( ){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			MApkVersion mApkVersion = new MApkVersion();
			if(StringUtils.isNotBlank(queryName)){
				mApkVersion.setApkVersionName(queryName);
			}
			if(StringUtils.isNotBlank(queryNum)){
				mApkVersion.setApkMinimumNum(Integer.parseInt(queryNum));
			}
			if(StringUtils.isNotBlank(queryFlg)&&!"7".equals(queryFlg)){
				mApkVersion.setForceUpdateFlg(Integer.parseInt(queryFlg));;
			}
			List<MApkVersion> list = updateVersionService.getPagedVersionList(mApkVersion, rows, page);
			int count = updateVersionService.getTotal(mApkVersion);
			map.put("total", count);
			map.put("rows", list);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("BBSJ", e);
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("BBSJ","版本升级","2","1"), e);
		}
		String json = JSONUtils.toJson(map, "yyyy-MM-dd");
		WebUtils.webSendJSON(json);
	}
	
	/** 跳转查看版本信息界面
	* @author zxl 
	 * @return 
	* @date 2017年6月20日
	*/
	@RequiresPermissions(value={"BBSJ:function:edit"})
	@Action(value = "toViewVersion", results = { @Result(name = "list", location = "/WEB-INF/pages/mobile/versions/versionView.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toViewVersion(){
		mApkVersion = updateVersionService.get(id);
		if(mApkVersion != null){
			String addr = mApkVersion.getApkDownloadAddr();
			mApkVersion.setApkDownloadAddr(addr);
		}
		return "list";
	}
	
	/** 删除版本信息
	* @param request
	* @param response
	* @param ids 要删除的id字符串，以逗号分割
	* @author zxl 
	* @date 2017年6月20日
	*/
	@RequiresPermissions(value={"BBSJ:function:del"})
	@Action(value = "delVersions")
	public void delVersions(){
		Map<String, String> map = new HashMap<String, String>();
		try {
			updateVersionService.delVersions(ids);
			map.put("resCode", "0");
			map.put("resMsg", "删除成功！");
		} catch (Exception e) {
			map.put("resCode", "1");
			map.put("resMsg", "删除失败！");
			e.printStackTrace();
			logger.error("BBSJ", e);
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("BBSJ","版本升级","2","1"), e);
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/** 跳转添加版本管理界面
	* @author zxl
	 * @return 
	* @date 2017年6月20日
	*/
	@RequiresPermissions(value={"BBSJ:function:add"})
	@Action(value = "toAddVersion", results = { @Result(name = "list", location = "/WEB-INF/pages/mobile/versions/versionEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toAddVersion() {
		mApkVersion = new MApkVersion();
		return "list";
	}
	
	/** 跳转修改版本信息管理界面
	* @author zxl
	 * @return 
	* @date 2017年6月20日
	*/
	@RequiresPermissions(value={"BBSJ:function:edit"})
	@Action(value = "toEditVersion", results = { @Result(name = "list", location = "/WEB-INF/pages/mobile/versions/versionEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toEditVersion() {
		try{
			mApkVersion = updateVersionService.get(id);
			if(mApkVersion != null){
				String addr = mApkVersion.getApkDownloadAddr();
				mApkVersion.setApkDownloadAddr(addr);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("BBSJ", e);
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("BBSJ","版本升级","2","1"), e);
		}
		return "list";
	}
	
	/** 保存版本信息
	 * @param request
	 * @param response
	 * @param user 保存实体
	 * @author zxl
	 * @date 2017年6月20日
	 */
	@Action(value = "saveVersion")
	public void saveVersion(){
		Map<String, String> map = new HashMap<String, String>();
		FileOutputStream fos = null;
		FileInputStream fis = null;
		try {
			if(mFile == null || mFile.length() == 0){
				map = updateVersionService.save(mApkVersion);
			}else{
				String fileAddress = null;
				//APK下载地址（固定）
				String fileAddressFixed=null;
				if(mFile!=null){
					fileAddress = uploadFileService.fileUpload(mFile,"aa.apk","2","apkFile");
					fileAddressFixed = uploadFileService.fileUploadFixed(mFile,"aa.apk","2","apkFile","qrCode");
					
					//手机扫码必须用外网
					String addressOut = HisParameters.OUTFILEUPLOADURL;
					//将上面生成的文件路径拼入下载地址
					String fileurl = addressOut+fileAddressFixed;
					/**二维码内容**/
					String content = fileurl ;
					/**内容的格式，如果有中文的话必须转码，**/
					content = new String(content.getBytes("UTF-8"), "ISO-8859-1") ;
					/**二维码图片的保存路径(放在临时文件夹的临时文件里)**/
					File tempFile = new File(ServletActionContext.getRequest().getSession().getServletContext().getRealPath("/")+"WEB-INF/temp");
					if(!tempFile.exists()){
						tempFile.mkdirs();
					}
					File file = new File(ServletActionContext.getRequest().getSession().getServletContext().getRealPath("/")+"WEB-INF/temp/downLoadApk.jpg");
					/**构造二维码，宽高分别为400、400**/
					QRCodeWriter writer = new QRCodeWriter() ;
					BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, 400, 400) ;
					MatrixToImageWriter.writeToFile(matrix, "jpg", file);
					//保存二维码图片到文件服务器
					String fileAddressapk=uploadFileService.fileUpload(file,"downLoadApk.jpg","2","apkFile");
					
					mApkVersion.setApkDownloadAddr(fileAddress);
					mApkVersion.setApkDownloadQRAddr(fileAddressapk);//二维码地址
					mApkVersion.setApkDownloadFixedAddr(fileAddressFixed);//APK下载地址（固定）
					map=updateVersionService.save(mApkVersion);
					map.put("resCode", "0");
					map.put("resMsg", "保存成功！");
				}
			}
		} catch (Exception e) {
			map.put("resCode", "1");
			map.put("resMsg", "保存失败！");
			e.printStackTrace();
			logger.error("BBSJ", e);
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("BBSJ","版本升级","2","1"), e);
		} finally{
			try {
				if(fos != null){
					fos.close();
				}
				if(fis != null){
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("BBSJ", e);
				exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("BBSJ","版本升级","2","1"), e);
			}
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		}
	}

	
	public MApkVersion getmApkVersion() {
		return mApkVersion;
	}
	public void setmApkVersion(MApkVersion mApkVersion) {
		this.mApkVersion = mApkVersion;
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
	public String getQueryNum() {
		return queryNum;
	}
	public void setQueryNum(String queryNum) {
		this.queryNum = queryNum;
	}
	public String getQueryName() {
		return queryName;
	}
	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}
	public String getQueryFlg() {
		return queryFlg;
	}
	public void setQueryFlg(String queryFlg) {
		this.queryFlg = queryFlg;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public UpdateVersionService getUpdateVersionService() {
		return updateVersionService;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}


	public File getmFile() {
		return mFile;
	}


	public void setmFile(File mFile) {
		this.mFile = mFile;
	}


	public String getIds() {
		return ids;
	}


	public void setIds(String ids) {
		this.ids = ids;
	}
	
}
