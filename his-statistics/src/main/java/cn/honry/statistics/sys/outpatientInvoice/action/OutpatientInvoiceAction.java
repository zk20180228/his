package cn.honry.statistics.sys.outpatientInvoice.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
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
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.statistics.sys.outpatientInvoice.service.OutpatientInvoiceService;
import cn.honry.statistics.sys.outpatientInvoice.vo.InvoiceInfoVo;
import cn.honry.statistics.sys.outpatientInvoice.vo.OutpatientStaVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
/**
 * 
 * @Description 门诊发票清单
 * @author  lyy
 * @createDate： 2016年7月13日 下午8:31:44 
 * @modifier lyy
 * @modifyDate：2016年7月13日 下午8:31:44
 * @param：  
 * @modifyRmk：  
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value={@InterceptorRef(value = "manageInterceptor")})
@Namespace(value = "/statistics/outpatientInvoice")
@SuppressWarnings({"all"})
public class OutpatientInvoiceAction extends ActionSupport{
	
	private String menuAlias;//栏目别名,在主界面中点击栏目时传到action的参数
	
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}

	private String invoiceNo;
	
	// 记录异常日志
	private Logger logger = Logger.getLogger(OutpatientInvoiceAction.class);
	
	// 存储异常
	@Resource
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	
	@Autowired
	@Qualifier(value = "invoiceService")
	private OutpatientInvoiceService invoiceService;
	public void setInvoiceService(OutpatientInvoiceService invoiceService) {
		this.invoiceService = invoiceService;
	}
	

	/**
	 * 
	 * <p>跳转到门诊收费(领取发票号) </p>
	 * @Author: ldl
	 * @CreateDate: 2016-06-21 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年7月4日 下午2:28:08 
	 * @ModifyRmk: 修改注释模板 
	 * @version: V1.0
	 * @return
	 * @throws Exception
	 * @throws:
	 *
	 */
	@Action(value="toView",results={@Result(name="list",location = "/WEB-INF/pages/stat/outpatientInvoice/outpatientInvoice.jsp")}, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toViewFeeStatList(){
		
		return "list";
	}
	
	

	/**
	 * 
	 * <p>查询患者信息根据发票号 </p>
	 * @Author: ldl
	 * @CreateDate: 2016-06-21 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年7月4日 下午2:28:08 
	 * @ModifyRmk: 修改注释模板,添加异常处理机制
	 * @version: V1.0
	 * @return
	 * @throws Exception
	 * @throws:
	 *
	 */
	@Action(value = "findInvoiceNo")
	public void findInvoiceNo(){
		
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			InvoiceInfoVo invoiceInfoVo = invoiceService.queryInvoiceInfoVo(invoiceNo);
			if(invoiceInfoVo!=null){
				map.put("resMsg", "success");
				map.put("resCode", invoiceInfoVo);
			}else{
				map.put("resMsg", "error");
				map.put("resCode", "对不起，该发票无效");
			}
		} catch (Exception e) {
			
			//发生异常，默认提示
			map.put("resMsg", "error");
			map.put("resCode", "对不起，该发票无效");
			
			e.printStackTrace();
			logger.error("MZCX_MZFPQD", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZCX_MZFPQD", "门诊查询_门诊发票清单", "2","0"), e);
		}
		
		String mapJosn=mapJosn = JSONUtils.toJson(map,DateUtils.DATE_FORMAT);
		WebUtils.webSendJSON(mapJosn);
	}
	

	/**
	 * 
	 * <p>查询收费记录根据发票号</p>
	 * @Author: ldl
	 * @CreateDate: 2016-06-21 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年7月4日 下午2:28:08 
	 * @ModifyRmk: 修改注释模板,添加异常处理机制
	 * @version: V1.0
	 * @return
	 * @throws Exception
	 * @throws:
	 *
	 */
	@Action(value = "findOutpatient")
	public void findOutpatient(){
		
		Double money=0.0;
		List<OutpatientStaVo> listmodls = new ArrayList<OutpatientStaVo>();
		try {
			List<OutpatientStaVo> list = invoiceService.findOutpatient(invoiceNo);
			String code = null;
			if(list!=null && list.size()>0){
				for(OutpatientStaVo modl : list){
					if(code==null){
						code = modl.getFeeCode();
						money = modl.getMoney();
						listmodls.add(modl);
					}else{
						if(code.equals(modl.getFeeCode())){
							listmodls.add(modl);
							code = modl.getFeeCode();
							money = money + modl.getMoney();
						}else{
							OutpatientStaVo vo = new OutpatientStaVo();
							vo.setItemName("小计");
							vo.setMoney(money);
							listmodls.add(vo);
							listmodls.add(modl);
							code = modl.getFeeCode();
							money = modl.getMoney();
						}
					}
				}
			}
		} catch (Exception e) {
			
			e.printStackTrace();
			logger.error("MZCX_MZFPQD", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZCX_MZFPQD", "门诊查询_门诊发票清单", "2","0"), e);
		}
		
		OutpatientStaVo vo = new OutpatientStaVo();
		vo.setItemName("小计");
		vo.setMoney(money);
		listmodls.add(vo);
		String mapJosn = JSONUtils.toJson(listmodls);
		
		WebUtils.webSendJSON(mapJosn);
	}
	
	
	
	
	
	
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}	
	
	
	
	
}
