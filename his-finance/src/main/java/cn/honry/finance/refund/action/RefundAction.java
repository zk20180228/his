package cn.honry.finance.refund.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
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

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.FinanceInvoiceInfoNow;
import cn.honry.base.bean.model.HospitalParameter;
import cn.honry.base.bean.model.InpatientCancelitemNow;
import cn.honry.base.bean.model.OutpatientFeedetail;
import cn.honry.base.bean.model.OutpatientFeedetailNow;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.finance.medicinelist.service.MedicinelistService;
import cn.honry.finance.refund.service.RefundService;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.inner.system.parameter.service.ParameterInnerService;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/finance/refund")
//@Namespace(value = "/outpatient/refund")
@SuppressWarnings("all")
public class RefundAction extends ActionSupport{
	Logger logger = Logger.getLogger(RefundAction.class);
	
	@Autowired
	@Qualifier(value = "innerCodeService")
	private CodeInInterService innerCodeService;
	@Autowired
	@Qualifier(value = "parameterInnerService")
	private ParameterInnerService parameterInnerService;
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;
	}

	public void setParameterInnerService(ParameterInnerService parameterInnerService) {
		this.parameterInnerService = parameterInnerService;
	}

	/**
     * 栏目别名,在主界面中点击栏目时传到action的参数
     */
	private String menuAlias;
	
	public void setInnerCodeService(CodeInInterService innerCodeService) {
		this.innerCodeService = innerCodeService;
	}

	/**
     * 收费明细表实体
     */
	private OutpatientFeedetail feedetail;
	
	/**
     * 发票明细表实体
     */
	private FinanceInvoiceInfoNow invoiceInfo;
	
	/**
     * 系统参数表实体
     */
	private HospitalParameter parameter;
	
	/**
     * 系统参数是否按照收费员退费
     */
	private String isFeeUser;
	
	/**
     * 系统参数退费路径
     */
	private String payType;
	
	/**
     * 收费药品
     */
	private String drugList;
	
	/**
     * 收费非药品
     */
	private String undDrugList;
	/**
     * 退费药品
     */
	private String refundDrugList;
	/**
     * 退费非药品
     */
	private String refundUnDrugList;
	/**
     * 病历号
     */
	private String patientNo;
	
	/**
     * 退费金额
     */
	private Double maney;
	
	/**
     * 剩余金额
     */
	private Double price;
	
	/**
     * 发票号
     */
	private String invoiceNo;
	/**
	 * 发票号集合
	 */
	private String invoiceNos;
	private String clinicCode;
	
	/**
     * 退费service
     */
	@Autowired
	@Qualifier(value = "refundService")
	private RefundService refundService;
	
	public void setRefundService(RefundService refundService) {
		this.refundService = refundService;
	}
	
	/**
     * 门诊收费service
     */
    @Autowired
    @Qualifier(value = "medicinelistService")
    private MedicinelistService medicinelistService;
	public void setMedicinelistService(MedicinelistService medicinelistService) {
		this.medicinelistService = medicinelistService;
	}
	/**  
	 * @Description：访问门诊退费
	 * @Author：ldl
	 * @CreateDate：2016-04-08
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"MZTF:function:view"})
	@Action(value = "refundList", results = { @Result(name = "list", location = "/WEB-INF/pages/outpatient/refund/refundList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String refundList() {
		try{
			feedetail = new OutpatientFeedetail();
			//获得系统参数是否根据收费员退费
			HospitalParameter parameter = refundService.queryParameterByRj();
			//获得当前登陆用户
			feedetail.setFeeCpcd(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			//系统参数
			isFeeUser = parameter.getParameterValue();
			//系统参数退费支付方式
			HospitalParameter parameterPayType = refundService.queryParameterByPayType();
			payType = parameterPayType.getParameterValue();
			return "list";
		}catch(Exception e){
			logger.error("MZTF_MZTF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZTF_MZTF", "门诊退费_门诊退费", "1", "0"), e);
			return "list";
		}
	}
	
	/**  
	 * @Description： 根据发票号查询患者退费信息，和患者退费明细
	 * @Author：ldl
	 * @CreateDate：2016-04-06
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "getInvoiceInfoByInvoiceNo")
	public void getInvoiceInfoByInvoiceNo(){
		try{
			//初始化药品列表
			List<OutpatientFeedetailNow> drugInfoList = new ArrayList<OutpatientFeedetailNow>();
			//初始化非药品列表
			List<OutpatientFeedetailNow> unDrugInfoList = new ArrayList<OutpatientFeedetailNow>();
			//初始化总金额
			Double sumMoney = 0.0;
			//初始化map
			Map<String,Object> outMap = new HashMap<String, Object>();
			//初始化发票序号（多）
			String invoiceSeqs = "";
			//初始化发票号（多）
			String invoiceNos = "";
			//初始化发票数量
			int n = 0;
			//根据发票号查询到所有的发票序号
			List<FinanceInvoiceInfoNow> invoiceInfoList = refundService.findInvoiceInfoByInvoiceNo(invoiceInfo.getInvoiceNo());
			if(invoiceInfoList.size()>0){
				for(FinanceInvoiceInfoNow info : invoiceInfoList){//遍历得到所有的发票序号
					if(invoiceSeqs!=""){
						invoiceSeqs = invoiceSeqs + "','";
					}
					invoiceSeqs = invoiceSeqs + info.getInvoiceSeq();
				}
				//根据所有的发票序号所有的发票号
				List<FinanceInvoiceInfoNow> invoiceInfoLists = refundService.findInvoiceInfoByInvoiceSeqs(invoiceSeqs);
				if(invoiceInfoLists.size()>0){
					for(FinanceInvoiceInfoNow infos : invoiceInfoLists){//遍历得到所有的发票号
						if(invoiceNos!=""){
							invoiceNos = invoiceNos + "','";
						}
						n = n + 1;
						invoiceNos = invoiceNos + infos.getInvoiceNo();
					}
					//查询系统参数退费有效期限
					HospitalParameter parameter = refundService.queryParameter();
					//根据所有的发票号查询收费明细表
					List<OutpatientFeedetailNow> feedetailList = refundService.findFeedetailByInvoiceNos(invoiceNos,parameter.getParameterValue(),null);
					if(feedetailList.size()>0){
						for(OutpatientFeedetailNow modl : feedetailList){//遍历讲所有项目的收费金额汇总
							sumMoney = sumMoney + modl.getTotCost();
							if("1".equals(modl.getDrugFlag())){//如果是药品，放到药品列表中
								drugInfoList.add(modl);
							}else{//如果是非药品，就放到非药品列表中
								unDrugInfoList.add(modl);
							}
						}
					}else{
						outMap.put("resMsg", "error");
						outMap.put("resCode", "对不起，该药品已过退费有效期限或者已经退费，请您见谅");
						String mapJosn = JSONUtils.toJson(outMap);
						WebUtils.webSendJSON(mapJosn);
					}
				}
				//患者姓名
				invoiceInfo.setName(invoiceInfoList.get(0).getName());
				//患者病历号
				invoiceInfo.setCardNo(invoiceInfoList.get(0).getCardNo());
				//合同单位代码
				invoiceInfo.setPactCode(invoiceInfoList.get(0).getPactCode());
				//门诊号
				invoiceInfo.setClinicCode(invoiceInfoList.get(0).getClinicCode());
				//查询合同单位名称
				BusinessContractunit contractunit = refundService.queryContractunit(invoiceInfoList.get(0).getPactCode());
				//合同单位名称
				invoiceInfo.setPactName(contractunit.getName());
				//总钱数
				invoiceInfo.setTotCost(sumMoney);
				outMap.put("resMsg", "success");
				outMap.put("drugInfoList", drugInfoList);
				outMap.put("unDrugInfoList", unDrugInfoList);
				outMap.put("invoiceInfo", invoiceInfo);
				outMap.put("no", n);
			}else{
				outMap.put("resMsg", "error");
				outMap.put("resCode", "发票号错误，请重新录入");
			}
			String mapJosn = JSONUtils.toJson(outMap);
			WebUtils.webSendJSON(mapJosn);
		}catch(Exception e){
			logger.error("MZTF_MZTF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZTF_MZTF", "门诊退费_门诊退费", "1", "0"), e);
		}
	}
	
	
	/**  
	 * @Description：  渲染单位
	 * @Author：ldl
	 * @CreateDate：2016-04-06
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "packFunction")
	public void packFunction(){
		try{
			//药品编码
			List<BusinessDictionary> drugpackagingunitList = innerCodeService.getDictionary(HisParameters.DRUGPACKUNIT);
			//非药品编码
			List<BusinessDictionary> drugminUinitList = innerCodeService.getDictionary(HisParameters.DRUGMINUNIT);
			Map<String,Map<String,String>> map = new HashMap<String, Map<String,String>>();
			Map<String,String> drugPackUintMap = new HashMap<String,String>();
			Map<String,String> drugMinUintMap = new HashMap<String,String>();
			for(BusinessDictionary pack : drugpackagingunitList){
				drugPackUintMap.put(pack.getEncode(), pack.getName());
			}
			for(BusinessDictionary packNot : drugminUinitList){
				drugMinUintMap.put(packNot.getEncode(), packNot.getName());
			}
			map.put("drugPackUint", drugPackUintMap);
			map.put("drugMinUint", drugMinUintMap);
			String mapJosn = JSONUtils.toJson(map);
			WebUtils.webSendJSON(mapJosn);
		}catch(Exception e){
			logger.error("MZTF_MZTF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZTF_MZTF", "门诊退费_门诊退费", "1", "0"), e);
		}
	}
	
	/**  
	 * @Description：  渲染员工
	 * @Author：ldl
	 * @CreateDate：2016-04-09
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "empFunction")
	public void empFunction(){
		try{
			//药品编码
			List<SysEmployee> employeeList =  refundService.queryEmpFunction();
			Map<String,String> empMap = new HashMap<String, String>();
			for(SysEmployee emp : employeeList){
				if(emp.getUserId()!=null){
					empMap.put(emp.getJobNo(), emp.getName());
				}
			}
			String mapJosn = JSONUtils.toJson(empMap);
			WebUtils.webSendJSON(mapJosn);
		}catch(Exception e){
			logger.error("MZTF_MZTF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZTF_MZTF", "门诊退费_门诊退费", "1", "0"), e);
		}
	}
	
	
	/**  
	 * @Description：  退费保存
	 * @Author：ldl
	 * @CreateDate：2016-04-11
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "saveRefund")
	public void saveRefund(){
		try{
			Map<String,String> maps = new HashMap<String, String>();
			Map<String,String> map = new HashMap<String, String>();
			Boolean flag = false;
			try{
				map = refundService.saveRefundNow(payType,drugList,undDrugList,refundDrugList,refundUnDrugList,patientNo,maney,price,invoiceNo);
				flag = true;
			}catch(Exception e){
				map.put("resMsg", "error");
				String message = e.getLocalizedMessage();
				message = message.substring(message.indexOf(":")+1,message.length());
				if(" INVOICE IS NOT ENOUGTH".equals(message)){
					map.put("resCode", "发票不足、请领取发票！");
				}else{
					map.put("resCode", "系统繁忙，请稍后重试...");
				}
				flag = true;
				throw new RuntimeException(e);
			}finally{
				if(!flag){
					map.put("resMsg", "error");
					map.put("resCode", "系统繁忙，请稍后重试...");
				}
				String mapJosn = JSONUtils.toJson(map);
				WebUtils.webSendJSON(mapJosn);
			}
		}catch(Exception e){
			logger.error("MZTF_MZTF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZTF_MZTF", "门诊退费_门诊退费", "1", "0"), e);
		}
	}
	/**
	 * @Description 根据一个发票号查询发票序号，再根据发票序号查询所有的发票号
	 * @author  marongbin
	 * @createDate： 2016年12月15日 上午11:03:22 
	 * @modifier 
	 * @modifyDate：: void
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "getInvoiceNosByInvoiceNo")
	public void getInvoiceNosByInvoiceNo(){
		try{
			//初始化药品列表
			List<OutpatientFeedetailNow> drugInfoList = new ArrayList<OutpatientFeedetailNow>();
			//初始化非药品列表
			List<OutpatientFeedetailNow> unDrugInfoList = new ArrayList<OutpatientFeedetailNow>();
			String invoiceSeqs = "";
			String invoiceNos = "";
			Double sumMoney = 0.00;
			Map<String,Object> map = new HashMap<String,Object>();
			List<FinanceInvoiceInfoNow> invoiceInfoLists = new ArrayList<FinanceInvoiceInfoNow>();
			List<FinanceInvoiceInfoNow> invoiceseqs = refundService.findInvoiceInfoByInvoiceNo(invoiceInfo.getInvoiceNo());
			boolean flag = true;//查询发票号查询不到处方信息时使用
			if(invoiceseqs.size()>0){
				Date date = invoiceseqs.get(0).getCreateTime();
				Long day = (new Date().getTime() -date.getTime())/(24*60*60*1000);
				String validDays = parameterInnerService.getparameter("backValidDay");//获取退费时的有效天数
				if(day<=Long.valueOf(validDays)){
					if(invoiceseqs.size()>0){
						for(FinanceInvoiceInfoNow info : invoiceseqs){//遍历得到所有的发票序号
							if(invoiceSeqs!=""){
								invoiceSeqs = invoiceSeqs + "','";
							}
							invoiceSeqs = invoiceSeqs + info.getInvoiceSeq();
						}
						invoiceInfoLists = refundService.findInvoiceInfoByInvoiceSeqs(invoiceSeqs);
						String invoiceNo = "";
						for (FinanceInvoiceInfoNow f : invoiceInfoLists) {
							if(invoiceNo!=""){
								invoiceNo += "','";
							}
							invoiceNo += f.getInvoiceNo();
						}
						//查询是否存在申请项目
						List<InpatientCancelitemNow> list = refundService.getCancelItemByInvoiceNo(invoiceNo);
						//如果存在申请项目则走申请流程
						String cancelinvoice = "";
						if(list!=null&&list.size()>0){
							double applyCost = 0D;
							for(InpatientCancelitemNow modls:list){
								applyCost  = applyCost + (modls.getSalePrice()*modls.getQuantity());
								if(cancelinvoice!=""){
									cancelinvoice += "','";
								}
								cancelinvoice += modls.getBillNo();
							}
//							List<OutpatientFeedetailNow> list2 = refundService.findFeedetailByInvoiceNos(cancelinvoice, validDays, list.get(0).getInpatientNo());
							BusinessContractunit contractunit = refundService.queryContractunit(invoiceInfoLists.get(0).getPactCode());
//							//合同单位名称
//							invoiceInfo.setPactName(contractunit.getName());
							map.put("packName", contractunit.getName());
							map.put("name", list.get(0).getName());
							map.put("medicalRecord", list.get(0).getCardNo());
							map.put("applyCost", applyCost);
							map.put("payType", 1);
							map.put("resMsg", "apply");
							map.put("resCode", list);
							map.put("invoice", cancelinvoice);
						}else{//不存在申请项目
							if(invoiceInfoLists.size()==1){
//							//查询系统参数退费有效期限
//							HospitalParameter parameter = refundService.queryParameter();
								//根据所有的发票号查询收费明细表
								List<OutpatientFeedetailNow> feedetailList = refundService.findFeedetailByInvoiceNos(invoiceInfoLists.get(0).getInvoiceNo(),validDays,null);
								String invoice = "";
								if(feedetailList.size()>0){
									//辅材是否可退标识  1是0否
									String subjobflag = parameterInnerService.getparameter("SUBJOBFLAG");
									for(OutpatientFeedetailNow modl : feedetailList){//遍历讲所有项目的收费金额汇总
										sumMoney = sumMoney + modl.getTotCost();
										if("1".equals(modl.getDrugFlag())){//如果是药品，放到药品列表中
											drugInfoList.add(modl);
										}else{//如果是非药品，就放到非药品列表中
											if(StringUtils.isNotBlank(subjobflag)&&modl.getSubjobFlag()!=null&&0!=modl.getSubjobFlag()){
												if("0".equals(subjobflag)){//辅材不可退   当辅材不可退时，flay给其任意一个不为空的值即可
													modl.setFlay(modl.getExtendOne());
												}else{//辅材可退
													String checkISsend = refundService.checkISsend(modl.getExtendOne());
													if(StringUtils.isNotBlank(checkISsend)){
														if("3".equals(checkISsend)||"4".equals(checkISsend)){
															modl.setFlay(modl.getExtendOne());
														}
													}
												}
											}
											unDrugInfoList.add(modl);
										}
										if(invoice!=""){
											invoice +="','";
										}
										invoice += modl.getInvoiceNo();
									}
									//患者姓名
									invoiceInfo.setName(invoiceInfoLists.get(0).getName());
									//患者病历号
									invoiceInfo.setCardNo(invoiceInfoLists.get(0).getCardNo());
									//合同单位代码
									invoiceInfo.setPactCode(invoiceInfoLists.get(0).getPactCode());
									//门诊号
									invoiceInfo.setClinicCode(invoiceInfoLists.get(0).getClinicCode());
									//查询合同单位名称
									BusinessContractunit contractunit = refundService.queryContractunit(invoiceInfoLists.get(0).getPactCode());
									//合同单位名称
									invoiceInfo.setPactName(contractunit.getName());
									//总钱数
									invoiceInfo.setTotCost(sumMoney);
									invoiceInfo.setInvoiceNo(invoice);
									//对集合进行排序
									ComparatorChain chain = new ComparatorChain();
									chain.addComparator(new BeanComparator("combNo"), true);
									Collections.sort(drugInfoList,chain);
									Collections.sort(unDrugInfoList,chain);
									map.put("drugInfoList", drugInfoList);
									map.put("unDrugInfoList", unDrugInfoList);
									map.put("invoice", feedetailList.get(0).getInvoiceNo());
									map.put("invoiceInfo", invoiceInfo);
									map.put("resMsg", "success");
									map.put("resCode", invoiceInfoLists);
									map.put("no", invoiceInfoLists.size());
								}
							}else if(invoiceInfoLists.size()<=0){
								flag = false;
								map.put("resMsg", "error");
								map.put("resCode", "对不起！该发票号下无查询结果，请核对发票号！");
							}else if(invoiceInfoLists.size()>1){
								for (FinanceInvoiceInfoNow finance : invoiceInfoLists) {
									//查询合同单位名称
									BusinessContractunit contractunit = refundService.queryContractunit(finance.getPactCode());
									//合同单位名称
									finance.setPactName(contractunit.getName());
								}
								map.put("invoiceInfo", invoiceInfo);
								map.put("resMsg", "success");
								map.put("resCode", invoiceInfoLists);
								map.put("no", invoiceInfoLists.size());
							}
						}
						
					}else{
						map.put("resMsg", "error");
						map.put("resCode", "对不起！该发票号下无查询结果，请核对发票号！");
					}
				}else{
					map.put("resMsg", "error");
					map.put("resCode", "对不起！该发票号已超过可退天数，不允许退费！");
				}
			}else{
				map.put("resMsg", "error");
				map.put("resCode", "对不起！该发票号不存在！");
			}
			String mapJosn = JSONUtils.toJson(map);
			WebUtils.webSendJSON(mapJosn);
		}catch(Exception e){
			logger.error("MZTF_MZTF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZTF_MZTF", "门诊退费_门诊退费", "1", "0"), e);
		}
	}
	
	/**
	 * @Description  根据发票号查询明细
	 * @author  marongbin
	 * @createDate： 2016年12月15日 下午3:44:14 
	 * @modifier 
	 * @modifyDate：: void
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "getDetailByInvoiceNos")
	public void getDetailByInvoiceNos(){
		try{
			Map<String,Object> outMap = new HashMap<String,Object>();
			//初始化药品列表
			List<OutpatientFeedetailNow> drugInfoList = new ArrayList<OutpatientFeedetailNow>();
			//初始化非药品列表
			List<OutpatientFeedetailNow> unDrugInfoList = new ArrayList<OutpatientFeedetailNow>();
			//查询系统参数退费有效期限
			HospitalParameter parameter = refundService.queryParameter();
			List<OutpatientFeedetailNow> feedetailList = null;
			//根据所有的发票号查询收费明细表
			if(StringUtils.isNotBlank(invoiceNos)){
				feedetailList = refundService.findFeedetailByInvoiceNos(invoiceNos,parameter.getParameterValue(),clinicCode);
			}
			if(feedetailList.size()>0){
				//辅材是否可退标识  1是0否
				String subjobflag = parameterInnerService.getparameter("SUBJOBFLAG");
				for(OutpatientFeedetailNow modl : feedetailList){//遍历讲所有项目的收费金额汇总
					if("1".equals(modl.getDrugFlag())){//如果是药品，放到药品列表中
						drugInfoList.add(modl);
					}else{//如果是非药品，就放到非药品列表中
						if(StringUtils.isNotBlank(subjobflag)&&modl.getSubjobFlag()!=null&&0!=modl.getSubjobFlag()){
							if("0".equals(subjobflag)){//辅材不可退   当辅材不可退时，flay给其任意一个不为空的值即可
								modl.setFlay(modl.getExtendOne());
							}else{//辅材可退
								String checkISsend = refundService.checkISsend(modl.getExtendOne());
								if(StringUtils.isNotBlank(checkISsend)){
									if("3".equals(checkISsend)||"4".equals(checkISsend)){
										modl.setFlay(modl.getExtendOne());
									}else{
										modl.setFlay(null);
									}
								}
							}
						}
						unDrugInfoList.add(modl);
					}
				}
				ComparatorChain chain = new ComparatorChain();
				chain.addComparator(new BeanComparator("combNo"), true);
				Collections.sort(drugInfoList, chain);
				Collections.sort(unDrugInfoList, chain);
				outMap.put("resMsg", "success");
				outMap.put("drugInfoList", drugInfoList);
				outMap.put("unDrugInfoList", unDrugInfoList);
			}else{
				outMap.put("resMsg", "error");
				outMap.put("resCode", "对不起！该发票号下无查询结果，请核对发票号！");
			}
			String mapJosn = JSONUtils.toJson(outMap);
			WebUtils.webSendJSON(mapJosn);
		}catch(Exception e){
			logger.error("MZTF_MZTF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZTF_MZTF", "门诊退费_门诊退费", "1", "0"), e);
		}
	}
	
	
	
	
	
	
	
	
	public String getInvoiceNos() {
		return invoiceNos;
	}
	public void setInvoiceNos(String invoiceNos) {
		this.invoiceNos = invoiceNos;
	}
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	public OutpatientFeedetail getFeedetail() {
		return feedetail;
	}
	public void setFeedetail(OutpatientFeedetail feedetail) {
		this.feedetail = feedetail;
	}
	
	public FinanceInvoiceInfoNow getInvoiceInfo() {
		return invoiceInfo;
	}

	public void setInvoiceInfo(FinanceInvoiceInfoNow invoiceInfo) {
		this.invoiceInfo = invoiceInfo;
	}

	public HospitalParameter getParameter() {
		return parameter;
	}
	public void setParameter(HospitalParameter parameter) {
		this.parameter = parameter;
	}
	public String getIsFeeUser() {
		return isFeeUser;
	}
	public void setIsFeeUser(String isFeeUser) {
		this.isFeeUser = isFeeUser;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getDrugList() {
		return drugList;
	}
	public void setDrugList(String drugList) {
		this.drugList = drugList;
	}
	public String getUndDrugList() {
		return undDrugList;
	}
	public void setUndDrugList(String undDrugList) {
		this.undDrugList = undDrugList;
	}
	public String getRefundDrugList() {
		return refundDrugList;
	}
	public void setRefundDrugList(String refundDrugList) {
		this.refundDrugList = refundDrugList;
	}
	public String getRefundUnDrugList() {
		return refundUnDrugList;
	}
	public void setRefundUnDrugList(String refundUnDrugList) {
		this.refundUnDrugList = refundUnDrugList;
	}
	public String getPatientNo() {
		return patientNo;
	}
	public void setPatientNo(String patientNo) {
		this.patientNo = patientNo;
	}
	public Double getManey() {
		return maney;
	}
	public void setManey(Double maney) {
		this.maney = maney;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getClinicCode() {
		return clinicCode;
	}
	public void setClinicCode(String clinicCode) {
		this.clinicCode = clinicCode;
	}
	
}
