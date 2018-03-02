	package cn.honry.statistics.bi.bistac.mongoDataInit.action;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.system.threadPool.MyThreadPool;
import cn.honry.statistics.bi.bistac.mongoDataInit.service.MongoDataInitService;
import cn.honry.statistics.bi.bistac.operationNum.service.OperationNumsService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/mongoDataInit")
@SuppressWarnings({ "all" })
public class MongoDataInitAction extends ActionSupport {
	private Logger logger=Logger.getLogger(MongoDataInitAction.class);
	//线程池
	private ExecutorService thread = MyThreadPool.getThreadPool();
	private String startTime;//开始时间
	private String endTime;//结束时间
	private Integer type;//统计类型(1-按日统计；2-按月统计；3-按年统计)
	private String menuName;//栏目别名
	
	@Autowired
	@Qualifier(value = "deptInInterService")
	private DeptInInterService deptInInterService;
	
	
	@Autowired
	@Qualifier(value = "operationNumsService")
	private OperationNumsService operationNumsService;
	
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
	
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	@Autowired
	@Qualifier(value = "mongoDataInitService")
	private MongoDataInitService mongoDataInitService;
	
	
	
	
	/**
	 * mongodb数据初始化方法
	 */
	//http://localhost:8080/his-portal/mongoDataInit/mongoDataInit.action?menuName=GHYSGZLTJ&startTime=2005-01-01&endTime=2005-12-31&type=1
	@Action(value="mongoDataInit")
	public void mongoDataInit(){
		try{
			
			if(StringUtils.isBlank(startTime)||StringUtils.isBlank(endTime)||type==null){
				WebUtils.webSendString("条件不满足!");
			}else{
				if(type!=null&&type==1){//当按日处理时
					Date sdate = DateUtils.parseDateY_M_D(startTime);
					Date edate = DateUtils.parseDateY_M_D(endTime);
					int i=0;
					final Semaphore semp = new Semaphore(4,true);
					while(true){
						Date addDay = DateUtils.addDay(sdate, i);
						Date addDay1 = DateUtils.addDay(sdate, i+1);
						final String y_M_D = DateUtils.formatDateY_M_D(addDay);
						final String ymd = DateUtils.formatDateY_M_D(addDay1);
						thread.execute(new Runnable() {
							@Override
							public void run() {
								try{
									semp.acquire();
									mongoDataInitService.mongoDataInit(menuName, y_M_D, ymd, type);
								}catch(Exception e){
									logger.error("mongoDataInit()",e);
									throw new RuntimeException(e);
								}finally{
									semp.release();
								}
							}
						});
						if(addDay.compareTo(edate)>=0){
							break;
						}
						i++;
					}
					WebUtils.webSendString("后台执行中...");
				}else{
					mongoDataInitService.mongoDataInit(menuName, startTime, endTime, type);
				}
				WebUtils.webSendString("初始化成功！");
			}
		}catch(Exception e){
			logger.error("mongoDataInit()",e);
			e.printStackTrace();
			WebUtils.webSendString("初始化失败！");
		}
	}
	@Action(value="reCalculate")
	public void reCalculate(){
		try{
			String[] split = startTime.split(",");
			for (int i=0;i<split.length;i++) {
				final String string = split[i];
				thread.execute(new Runnable() {
					@Override
					public void run() {
						try{
							mongoDataInitService.mongoDataInit(menuName, string, string, type);
						}catch(Exception e){
							logger.error("reCalculate()",e);
						}
					}
				});
			}
			WebUtils.webSendString("重新计算成功！");
		}catch(Exception e){
			logger.error("reCalculate()",e);
			WebUtils.webSendString("初始化失败！");
		}
	}
}
