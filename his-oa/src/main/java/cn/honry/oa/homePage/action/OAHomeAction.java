package cn.honry.oa.homePage.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.map.HashedMap;
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

import cn.honry.base.bean.model.User;
import cn.honry.oa.homePage.service.OAHomeService;
import cn.honry.oa.homePage.vo.ArticleVo;
import cn.honry.oa.homePage.vo.MenuVo;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

@Controller
@Namespace("/oa/OAHome")
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value={@InterceptorRef(value="manageInterceptor")})
public class OAHomeAction extends ActionSupport {

	

	private static final long serialVersionUID = -7026830647149447565L;
	@Autowired
	@Qualifier("oAHomeService")
	private OAHomeService oAHomeService;
	public void setoAHomeService(OAHomeService oAHomeService) {
		this.oAHomeService = oAHomeService;
	}

	private String menuCode;//栏目的code
	private String infoTitle;//文章的标题
	private String isMore;//是否查询更多的标记,true:是，查询全部,带分页,false:默认显示前8条
	private String page;//当前页
	private String rows;//当前显示多少页
	
	
	private HttpServletRequest request = ServletActionContext.getRequest();
	
	//http://localhost:8080/his-portal/oa/OAHome/homeUI.action
	/**
	 * 
	 * <p>oa系统首页 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年7月25日 下午3:37:55 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年7月25日 下午3:37:55 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return
	 * @throws:
	 *
	 */
	//1.oa首页
	@Action(value="homeUI",results={@Result(name="list",location="/WEB-INF/pages/oa/homePage/homeUI.jsp")})
	public String homeUI(){
		//用户信息
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		String userName=user.getName();
		
		//栏目信息列表
		List<MenuVo> menuList = menuList();
		request.setAttribute("userName",userName);
		request.setAttribute("menuList", menuList);
		
		//首页轮播图
		List<ArticleVo> articleList=null;
		try {
			articleList = oAHomeService.articleList("OA_MENU_TPXW","home",null,null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		String url=HisParameters.FILEUPLOADURL;//文件服务器的路径
		String url=HisParameters.getfileURI(ServletActionContext.getRequest());//文件服务器的路径
		for(ArticleVo a:articleList){
			a.setImgPath(url+a.getImgPath());
			System.out.println(a.getImgPath());
		}
		
		request.setAttribute("articleList", articleList);
		
		return "list";
	}
	
	
	//2.退出，进入平台管理-->跳转到his首页,调用原有的his的接口
	
	//5.根据父栏目，查询其子栏目列表
	@Action(value="sonMenuList",results={@Result(name="json",type="json")})
	public void sonMenuList(){
		
		List<MenuVo> list=null;
		try {
			list=menuList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String json = JSONUtils.toJson(list);
		
		WebUtils.webSendJSON(json);
	}
	
	
	//6.跳转到文章信息列表http://localhost:8080/his-portal/oa/OAHome/articleUI.action
	@Action(value="articleUI",results={@Result(name="list",location="/WEB-INF/pages/oa/homePage/articleUI.jsp")})
	public String articleUI(){
		
		//把查询的条件传递到前台,在从前台统一向后台发送请求查询
		request.setAttribute("infoTitle", infoTitle);
		request.setAttribute("menuCode", menuCode);
		
		return "list";
	}
	
	
	//7.根据关键字查询文章列表
	@Action(value="infoList",results={@Result(name="json",type="json")})
	public void infoList(){
		
		List<ArticleVo> list = null;
		Map<String,Object> map = null;
		Integer total =null;
		try {
			 map=new HashMap<String,Object>();
			 list =oAHomeService.infoList(menuCode,infoTitle,page, rows);
			 total=oAHomeService.pageCount(menuCode, infoTitle);
		} catch (Exception e) {
			 e.printStackTrace();
		}
		
		map.put("total", total);
		map.put("rows", list);
		String json =JSONUtils.toJson(map);
		
		WebUtils.webSendJSON(json);
	}
	
	
	
	//8.根据栏目的code,查询栏目下的文章
	@Action(value="menu_ArticleList",results={@Result(name="json",type="json")})
	public void menu_ArticleList(){
		
		List<ArticleVo> list = null;
		Map<String,Object> map = null;
		Integer total =null;
		try {
			map=new HashMap<String,Object>();
			list =oAHomeService.articleList(menuCode,isMore,page,rows);
			if("true".equals(isMore)){
				total=oAHomeService.pageCount(menuCode,null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(list==null){
			list= new ArrayList<ArticleVo>();
		}
		
		map.put("total", total);
		map.put("rows", list);
		
		String json=null;
		if(!"true".equals(isMore)){//只显示部分，前几条
			json= JSONUtils.toJson(list);
		}else{
			json= JSONUtils.toJson(map);
		}
		
		WebUtils.webSendJSON(json);
	}
	
	//9.根据文章的id，查询文章的详情,这个调用小马哥的接口
	
	
	//10
	
	
	
	
	
	
	
	
	
	//3.加载栏目列表目列表
	public List<MenuVo> menuList(){
		
		List<MenuVo> list=null;
		try {
			list = oAHomeService.MenuList(menuCode);
		} catch (Exception e) {
			list=new ArrayList<MenuVo>();
			e.printStackTrace();
		}
		
		return list;
	}
	
	
	//4.页面初始化的时候查询动态文章列表
	/**
	 * 
	 * <p>根据栏目的code,查询文章列表，当isMore='true'是，代表查询更多，这时会分页，当isMore='false'时，默认显示前8条，也不进行分页</p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年7月26日 上午11:14:46 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年7月26日 上午11:14:46 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuCode 栏目的code
	 * @param isMore 是否是更多项
	 * @param page 当前页
	 * @param rows 每页显示的记录数
	 * @return
	 * @throws:
	 *
	 */
//	public List<ArticleVo> articleList(String menuCode,String isMore,String page,String rows){
//		
//		List<ArticleVo> list=null;
//		try {
//			list=oAHomeService.articleList(menuCode,isMore,page,rows);
//		} catch (Exception e) {
//			list=new ArrayList<ArticleVo>();
//			e.printStackTrace();
//		}
//		
//		if(list==null||list.size()==0){
//			list= new ArrayList<ArticleVo>();
//		}
//		
//		return list;
//	}


	public String getMenuCode() {
		return menuCode;
	}


	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}


	public String getIsMore() {
		return isMore;
	}


	public void setIsMore(String isMore) {
		this.isMore = isMore;
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


	public HttpServletRequest getRequest() {
		return request;
	}


	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}


	public String getInfoTitle() {
		return infoTitle;
	}


	public void setInfoTitle(String infoTitle) {
		this.infoTitle = infoTitle;
	}
	
	
	
	
	
}
