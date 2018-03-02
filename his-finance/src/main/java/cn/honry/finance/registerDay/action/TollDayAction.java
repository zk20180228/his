package cn.honry.finance.registerDay.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

import cn.honry.base.bean.model.OutpatientDaybalance;
import cn.honry.base.bean.model.User;
import cn.honry.finance.registerDay.service.TollDayService;
import cn.honry.finance.registerDay.vo.DayBalanceVO;
import cn.honry.finance.registerDay.vo.TotalVo;
import cn.honry.finance.registerDay.vo.ViewVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;


@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
//@Namespace(value = "/sys/tollDay")
@Namespace(value = "/finance/tollDay")
public class TollDayAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	
	//注入本类Service
	@Autowired
	@Qualifier(value = "tollDayService")
	private TollDayService tollDayService;
	public void setRegisterDayService(TollDayService tollDayService) {
		this.tollDayService = tollDayService;
	}
	//栏目别名,在主界面中点击栏目时传到action的参数
	private String menuAlias;
	//门诊收款日日结实体
	private OutpatientDaybalance dayBalance = new OutpatientDaybalance();
	private String subStartTime;
	private String subEndTime;
	
	/***
	 * 收费员日结
	 * @Title: registerDayList 
	 * @author  WFJ
	 * @createDate ：2016年6月12日
	 * @return
	 * @return String
	 * @version 1.0
	 */
	@Action(value="tollDayList",results={@Result(name="list",location="/WEB-INF/pages/sys/reportForms/tollDayListNew.jsp")},interceptorRefs={@InterceptorRef(value="manageInterceptor")})
	public String registerDayList(){
		Date date = tollDayService.getBeginDate();
//		String string = DateUtils.formatDateYM(new Date())+"-01";
//		Date parseDateY_M_D = DateUtils.parseDateY_M_D(string);
		dayBalance.setBeginDate(date);
		dayBalance.setEndDate(DateUtils.getCurrentTime());
		return "list";
	}
	
	/***
	 * 查询时间段内收费发票汇总
	 * @Title: queryInvoiceinfo 
	 * @author  WFJ
	 * @createDate ：2016年6月13日
	 * @return void
	 * @version 1.0
	 */
	@Action(value = "queryInvoiceinfo", results = { @Result(name = "list", location = "/WEB-INF/pages/sys/reportForms/tollDayList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String queryInvoiceinfo(){
		List<ViewVo> voList = tollDayService.queryInvoiceinfo(dayBalance);
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setAttribute("voList", voList);
		TotalVo vo = new TotalVo();
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		if(voList.size()>1){
			Double total = voList.get(voList.size()-1).getTotal();
			vo.setMoney(digitUppercase(total.toString()));
		}else{
			vo.setMoney("零元");
		}
		vo.setGathering(user.getName());
		vo.setReceiver(user.getName());
		vo.setFill(user.getName());
		vo.setCashier("");
		vo.setCountTime(DateUtils.formatDateY_M_D_H_M_S(dayBalance.getBeginDate()) +" 至 "+ DateUtils.formatDateY_M_D_H_M_S(dayBalance.getEndDate()));
		vo.setStartTimeStr(DateUtils.formatDateY_M_D_H_M_S(dayBalance.getBeginDate()));
		vo.setEndTimeStr(DateUtils.formatDateY_M_D_H_M_S(dayBalance.getEndDate()));
		request.setAttribute("vo", vo);
		return "list";
	}
	@Action(value = "queryInvoiceinfoNew")
	public void queryInvoiceinfoNew(){
		try{
			HttpServletRequest request = ServletActionContext.getRequest();
			String startTime = request.getParameter("startTime");
			String endTime = request.getParameter("endTime");
			List<DayBalanceVO> balance = tollDayService.getBalance(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount(), startTime,endTime);
			String json = JSONUtils.toJson(balance);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	/***
	 * 查询时间段内收费发票汇总
	 * @Title: queryInvoiceinfo 
	 * @author  WFJ
	 * @createDate ：2016年6月13日
	 * @return void
	 * @version 1.0
	 */
	@Action(value = "saveDaybalance")
	public void saveDaybalance(){
		Map<String,Object> map=new HashMap<String,Object>();
		try {
			map = tollDayService.saveDaybalance(dayBalance);
		} catch (Exception e) {
			map.put("resMsg", "error");
			map.put("resCode", "未知错误请联系管理员!");
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	@Action(value = "saveDaybalanceNew")
	public void saveDaybalanceNew(){
		Map<String,Object> map=new HashMap<String,Object>();
		try{
			map = tollDayService.saveDaybalanceNew(subStartTime, subEndTime);
		}catch(Exception e){
			map.put("resMsg", "error");
			map.put("resCode", "未知错误请联系管理员!");
			e.printStackTrace();
		}finally{
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		}
	}
	
	/***
	 * 转大写金额
	 * @author aizhonghua
	 * @createDate ：2016-6-15 03::06:03
	 */
	@SuppressWarnings("unused")
	public static String digitUppercase(String num){  
		String fraction[] = {"角", "分"};  
        String digit[] = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };  
        String unit1[] = {"", "拾", "佰", "仟"};//把钱数分成段,每四个一段,实际上得到的是一个二维数组  
        String unit2[] = {"元", "万", "亿","万亿"}; //把钱数分成段,每四个一段,实际上得到的是一个二维数组  
        BigDecimal bigDecimal =  new BigDecimal(num);  
        bigDecimal=bigDecimal.multiply(new BigDecimal(100));  
        String strVal = String.valueOf(bigDecimal.toBigInteger());  
        String head = strVal.substring(0,strVal.length()-2);         //整数部分  
        String end = strVal.substring(strVal.length()-2);              //小数部分  
        String endMoney="";  
        String headMoney = "";  
        if("00".equals(end)){  
            endMoney = "整";  
        }else{  
            if(!"0".equals(end.substring(0,1))){  
                endMoney+=digit[Integer.valueOf(end.substring(0,1))]+"角";  
            }else if("0".equals(end.substring(0,1)) && !"0".equals(end.substring(1,2))){  
                endMoney+= "零";  
            }  
            if(!"0".equals(end.substring(1,2))){  
                endMoney+=digit[Integer.valueOf(end.substring(1,2))]+"分";  
            }  
        }  
        char[] chars = head.toCharArray();  
        Map<String,Boolean> map = new HashMap<String,Boolean>();//段位置是否已出现zero  
        boolean zeroKeepFlag = false;//0连续出现标志  
        int vidxtemp = 0;  
        for(int i=0;i<chars.length;i++){  
            int idx = (chars.length-1-i)%4;//段内位置  unit1  
            int vidx = (chars.length-1-i)/4;//段位置 unit2  
            if(!"-".equals(chars[i]+"")){
            	String s = digit[Integer.valueOf(String.valueOf(chars[i]))];  
                if(!"零".equals(s)){  
                    headMoney += s +unit1[idx]+unit2[vidx];  
                    zeroKeepFlag = false;  
                }else if(i==chars.length-1 || map.get("zero"+vidx)!=null){  
                    headMoney += "" ;  
                }else{  
                    headMoney += s;  
                    zeroKeepFlag = true;  
                    map.put("zero"+vidx,true);//该段位已经出现0；  
                }  
                if(vidxtemp!=vidx || i==chars.length-1){  
                    headMoney = headMoney.replaceAll(unit2[vidx],"");  
                    headMoney+=unit2[vidx];  
                }  
                if(zeroKeepFlag && (chars.length-1-i)%4==0){  
                    headMoney = headMoney.replaceAll("零","");  
                }  
            }else{
            	headMoney = "负";
            }
        }  
        return headMoney+endMoney;  
    }  

	
	


/*--------------------------------------------------------------------------------Get Set--------------------------------------*/
	public String getMenuAlias() {
		return menuAlias;
	}

	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	
	public OutpatientDaybalance getDayBalance() {
		return dayBalance;
	}
	
	public void setDayBalance(OutpatientDaybalance dayBalance) {
		this.dayBalance = dayBalance;
	}

	public String getSubStartTime() {
		return subStartTime;
	}

	public void setSubStartTime(String subStartTime) {
		this.subStartTime = subStartTime;
	}

	public String getSubEndTime() {
		return subEndTime;
	}

	public void setSubEndTime(String subEndTime) {
		this.subEndTime = subEndTime;
	}
	
}
