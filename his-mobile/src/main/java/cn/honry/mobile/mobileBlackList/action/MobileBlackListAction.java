package cn.honry.mobile.mobileBlackList.action;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.honry.base.bean.model.MMobileBlackList;
import cn.honry.base.bean.model.RecordToMobileException;
import cn.honry.mobile.exceptionLog.service.ExceptionLogService;
import cn.honry.mobile.mobileBlackList.service.MobileBlackListService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.RedisUtil;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;


@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@RequestMapping("/mosys/cellPhoneBlackList")
public class MobileBlackListAction extends ActionSupport{

private Logger logger=Logger.getLogger(MobileBlackListAction.class);
	
	@Autowired
	@Qualifier(value = "exceptionLogService")
	private ExceptionLogService exceptionLogService;
	public void setExceptionLogService(ExceptionLogService exceptionLogService) {
		this.exceptionLogService = exceptionLogService;
	}
	
	@Autowired
	@Qualifier(value = "mobileBlackListService")
	private MobileBlackListService mobileBlackListService;
	public void setMobileBlackListService(
			MobileBlackListService mobileBlackListService) {
		this.mobileBlackListService = mobileBlackListService;
	}
	
	@Autowired
	@Qualifier(value = "redisUtil")
	private RedisUtil redisUtil;
	public void setRedisUtil(RedisUtil redisUtil) {
		this.redisUtil = redisUtil;
	}
	
	/**
	 * 列表查询参数
	 */
	private  String mobileNum;
	/**
	 * 分页页数
	 */
	private String page;
	/**
	 * 分页行数
	 */
	private String rows;
	
	/**
	 * 类型查询
	 */
	private  String type;
	
	/**
	 * id，多个用逗号分隔
	 */
	private String ids;
	
	private String id;

	private MMobileBlackList mobileBlackList;
	
	private String menuAlias;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getMenuAlias() {
		return menuAlias;
	}

	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}

	public String getMobileNum() {
		return mobileNum;
	}

	public void setMobileNum(String mobileNum) {
		this.mobileNum = mobileNum;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public MMobileBlackList getMobileBlackList() {
		return mobileBlackList;
	}

	public void setMobileBlackList(MMobileBlackList mobileBlackList) {
		this.mobileBlackList = mobileBlackList;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	/**  
	 * 
	 * 跳转至移动手机黑名单页面
	 * @Author: zxl
	 * @CreateDate: 2017年7月17日 下午7:44:03 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年7月17日 下午7:44:03 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return: String 返回值类型
	 *
	 */
	@RequiresPermissions(value={"SJHHMDGL:function:view"})
	@Action(value = "cellPhoneBlackList", results = { @Result(name = "list", location = "/WEB-INF/pages/mobile/mobileBlackList/mobileBlackList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String  cellPhoneBlackList(){
		return "list";
	}
	
	
	/**  
	 * 
	 * 列表数据
	 * @Author: zxl
	 * @CreateDate: 2017年9月21日 上午10:43:56 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年9月21日 上午10:43:56 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@RequiresPermissions(value={"SJHHMDGL:function:query"})
	@Action(value = "findCellPhoneBlack")
	public void findCellPhoneBlack(){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			List<MMobileBlackList> list = mobileBlackListService.getCellPhoneBlack(rows, page, mobileNum,type);
			Integer total = mobileBlackListService.getCellPhoneBlackCount(mobileNum,type);
			map.put("total", total);
			map.put("rows", list);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("SJHHMDGL", e);
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("SJHHMDGL","手机号黑名单管理","2","0"), e);
		}
		String json = JSONUtils.toJson(map,"yyyy-MM-dd");
		WebUtils.webSendJSON(json);
	}
	
	
	/**  
	 * 
	 * 跳转添加页面
	 * @Author: zxl
	 * @CreateDate: 2017年7月17日 下午7:44:03 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年7月17日 下午7:44:03 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return: String 返回值类型
	 *
	 */
	@RequiresPermissions(value={"SJHHMDGL:function:add"})
	@Action(value = "cellPhoneBlackAdd", results = { @Result(name = "list", location = "/WEB-INF/pages/mobile/mobileBlackList/mobileBlackEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String  cellPhoneBlackAdd(){
		return "list";
	}
	
	
	/** 保存
	 * @param request
	 * @param response
	 * @param menuIcon 保存实体
	 * @author zxl
	 * @date 2017年6月20日
	 */
	@Action(value = "saveCellPhoneBlack")
	public void saveCellPhoneBlack(){
		Map<String, String> map = new HashMap<String, String>();
		try {
			mobileBlackListService.save(mobileBlackList);
			map.put("resCode", "0");
			map.put("resMsg", "保存成功！");
		} catch (Exception e) {
			map.put("resCode", "1");
			map.put("resMsg", "保存失败！");
			logger.error(e);
			e.printStackTrace();
		}
	
		String json= JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
		
	}
	
	/** 
	 * 根据手机号和类型校验该信息是否存在
	 * @param request
	 * @param response
	 * @author zxl
	 * @date 2017年6月20日
	 */
	@RequiresPermissions(value={"SJHHMDGL:function:edit"})
	@Action(value = "checkExist")
	public void checkExist(){
		Map<String, String> map = new HashMap<String, String>();
		List<MMobileBlackList> list;
		try {
			list = mobileBlackListService.checkExist(mobileNum,type);
			if(list!=null&&list.size()>0){
				map.put("resCode", "1");
				map.put("resMsg", "校验未通过！");
			}else{
				map.put("resCode", "0");
				map.put("resMsg", "校验通过！");
			}
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	
	/** 删除
	* @param request
	* @param response
	* @param ids 要删除的id字符串，以逗号分割
	* @author zxl 
	* @date 2017年6月20日
	*/
	@RequiresPermissions(value={"SJHHMDGL:function:del"})
	@Action(value = "delCellPhoneBlack")
	public void delCellPhoneBlack(){
		Map<String, String> map = new HashMap<String, String>();
		try {
			mobileBlackListService.delCellPhoneBlack(ids);
			map.put("resCode", "0");
			map.put("resMsg", "删除成功！");
		} catch (Exception e) {
			map.put("resCode", "1");
			map.put("resMsg", "删除失败！");
			e.printStackTrace();
			logger.error("SJHHMDGL", e);
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("SJHHMDGL","手机号黑名单管理","2","1"), e);
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/** 
	* 跳转修改界面
	* @param request
	* @param response
	* @param id
	* @author zxl 
	* @date 2017年6月20日
	*/
	@RequiresPermissions(value={"SJHHMDGL:function:edit"})
	@Action(value = "toEditCellPhoneBlack", results = { @Result(name = "list", location = "/WEB-INF/pages/mobile/mobileBlackList/mobileBlackEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toEditCellPhoneBlack() {
		try{
			mobileBlackList = mobileBlackListService.get(id);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("SJHHMDGL", e);
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("SJHHMDGL","手机号黑名单管理","2","1"), e);
		}
		return "list";
	}
	
	/** 
	* 跳转查看界面
	* @author zxl 
	* @return 
	* @date 2017年6月20日
	*/
	@RequiresPermissions(value={"SJHHMDGL:function:view"})
	@Action(value = "toViewCellPhoneBlack", results = { @Result(name = "list", location = "/WEB-INF/pages/mobile/mobileBlackList/mobileBlackView.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toViewCellPhoneBlack(){
		mobileBlackList = mobileBlackListService.get(id);
		return "list";
	}
	
	/** 同步至缓存
	 * @param request
	 * @param response
	 * @param mBlackList 保存实体
	 * @author zxl
	 * @date 2017年6月20日
	 */
	@RequiresPermissions(value={"SJHHMDGL:function:edit"})
	@Action(value = "synCach")
	public void synCach(){
		Map<String, String> map = new HashMap<String, String>();
		try {
			
			List<String> strList=Arrays.asList(id.split(","));
			for(int i=0;i<strList.size();i++){
				List<String> list=mobileBlackListService.synCach(strList.get(i));
				String cacheKey="phoneNumberBlackList";
				if(redisUtil.exists(cacheKey)){
					redisUtil.hset("phoneNumberBlackList",strList.get(i), list);
				}else{
					redisUtil.hset(cacheKey,strList.get(i), list);
				}
			}
			map.put("resCode", "0");
			map.put("resMsg", "同步成功！");
		} catch (Exception e) {
			map.put("resCode", "1");
			map.put("resMsg", "同步失败！");
			logger.error(e);
			e.printStackTrace();
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}

}
