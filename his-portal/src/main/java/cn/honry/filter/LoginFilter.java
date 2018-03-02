package cn.honry.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;

import cn.honry.base.bean.model.SysMobileLoginToken;
import cn.honry.utils.SessionUtils;
import cn.honry.utils.ShiroSessionUtils;

import com.google.gson.Gson;

public class LoginFilter implements Filter {
	private Logger logger = Logger.getLogger(LoginFilter.class);

	@Override
	public void destroy() {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		SessionUtils.getShiroSession().setAttribute("contextPath", req.getContextPath());
		logger.info("req.getContextPath()="+req.getContextPath()+" 当前请求URI：" + req.getRequestURI());
		if(req.getRequestURI().contains("mobile")){//移动端
			if((req.getContextPath()+"/mobile/login.action").equals(req.getRequestURI().trim())){
				chain.doFilter(request, response);
			}else{
				//这里来判断是否登录
				String userAccount = request.getParameter("userAccount");
				String loginToken = request.getParameter("loginToken");
				if(!StringUtils.isBlank(userAccount) && !StringUtils.isBlank(loginToken)){//判断用户名和令牌是否存在
					SysMobileLoginToken mobileLoginTokenSession = ShiroSessionUtils.getCurrentUserLoginTokenShiroSession();//获取当前用户登录的令牌
					if(mobileLoginTokenSession!=null){//判断session是否过期
							if(mobileLoginTokenSession.getLoginToken().equals(loginToken) && mobileLoginTokenSession.getUserAccount().equals(userAccount)){//如果用户名和令牌都相同的时候说明已经登录了
								SessionUtils.getShiroSession().setAttribute(SessionUtils.SESSIONLOGINTOKEN,mobileLoginTokenSession);
								chain.doFilter(request, response);
							}else{//session内的用户名和token无法匹配上,人为修改了登录的用户名
								SessionUtils.getShiroSession().removeAttribute(SessionUtils.SESSIONLOGINTOKEN);//清空session
								Map<String,Object> map=new HashMap<String,Object>();
								map.put("resCode", "-1");
								map.put("resMsg", "请登录后操作！");
								Gson gson =new Gson();
								res.setContentType("application/json");
								response.setCharacterEncoding("UTF-8"); 
								PrintWriter out = response.getWriter();
								out.write(gson.toJson(map));
								out.flush();
								out.close();
								return;
							}
					}else{//如果session过期了,就去数据库中判断
//							MobileLoginTokenService mobileLoginTokenService=(MobileLoginTokenService) ContextLoader.getCurrentWebApplicationContext().getBean("mobileLoginTokenService");
//							if(mobileLoginTokenService!=null){//取到用来查询后台的service
//								List<SysMobileLoginToken> mobileLoginTokenList=mobileLoginTokenService.findMobileLogin(userAccount,loginToken);//需要加入参数,过期天数,用来计算最后登录时间几天之后过期
//								SysMobileLoginToken sysMobileLoginToken=new SysMobileLoginToken();
//								if (mobileLoginTokenList!=null&&mobileLoginTokenList.size()>0)	{//如果查询出来了数据,说明是存在的
//									sysMobileLoginToken=mobileLoginTokenList.get(0);
//									Long days=sysMobileLoginToken.getDays();
//									Date lastdate=sysMobileLoginToken.getLatestLoginDate();
//									Date nowdate=new Date();
//									long diff = nowdate.getTime() - lastdate.getTime();//这样得到的差值是微秒级别
//									long minute = diff / (1000 * 60);
//									if(minute<days){
//										SessionUtils.getShiroSession().setAttribute(SessionUtils.SESSIONLOGINTOKEN,sysMobileLoginToken);
//										chain.doFilter(request, response);										
//									}else{
//										Map<String,Object> map=new HashMap<String,Object>();
//										map.put("resCode", "-1");
//										map.put("resMsg", "请登录后操作！");
//										Gson gson =new Gson();
//										res.setContentType("application/json");
//										response.setCharacterEncoding("UTF-8"); 
//										PrintWriter out = response.getWriter();
//										out.write(gson.toJson(map));
//										out.flush();
//										out.close();
//										return;
//									}
//								}else{//如果不存在记录则是未登录,返回登录页面
									Map<String,Object> map=new HashMap<String,Object>();
									map.put("resCode", "-1");
									map.put("resMsg", "请登录后操作！");
									Gson gson =new Gson();
									res.setContentType("application/json");
									response.setCharacterEncoding("UTF-8"); 
									PrintWriter out = response.getWriter();
									out.write(gson.toJson(map));
									out.flush();
									out.close();
									return;
//								}					
//							}
					}
				}else{//未登录,返回登录页面
					Map<String,Object> map=new HashMap<String,Object>();
					map.put("resCode", "-1");
					map.put("resMsg", "请登录后操作！");
					Gson gson =new Gson();
					res.setContentType("application/json");
					response.setCharacterEncoding("UTF-8"); 
					PrintWriter out = response.getWriter();
					out.write(gson.toJson(map));
					out.flush();
					out.close();
					return;
				}
			}
		}else{//非移动端
			String requestURI=req.getRequestURI();//移动端请求his方法调用
			if (requestURI.equals((req.getContextPath()+"/login.jsp"))||(requestURI.length()!=(requestURI.replaceAll("/AppNurseStation/|/outBalanceMobileAction/|/appReturnPremium/","").length()))) {
				chain.doFilter(request, response);
			} else{
				String requestType=req.getHeader("X-Requested-With");
				if (!SecurityUtils.getSubject().isAuthenticated()) {
					logger.info("过滤器：当前用户未登录，跳转到登录页面！");
					if(requestType!=null&&"XMLHttpRequest".equalsIgnoreCase(requestType)){
						PrintWriter printWriter = res.getWriter();
						printWriter.print("ajaxSessionTimeOut");
						printWriter.flush(); 
						printWriter.close();  
						return;
					}else{
						res.sendRedirect((req.getContextPath()+"/login.jsp?v="+System.currentTimeMillis()));
						return;
					}
				} 
				chain.doFilter(request, response);
			}
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

}
