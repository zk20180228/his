<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<style type="text/css">
	.tableCss{
		border-collapse: collapse;border-spacing: 0;border: 1px solid #95b8e7;width:100%;
	}
	.tableCss td{
		border: 1px solid #95b8e7;
		padding: 5px 15px;
		word-break: keep-all;
		white-space:nowrap;
	}
	.tableCss .TDlabel{
		text-align: right;
		width:80px;
	}
</style>
<head>
<title>结算召回</title>
<script type="text/javascript">
	var bedId = "";
	var balanceDate="";
	var diseasetypeList = "";
	var yjjsrList = "";
	var tjdlList = "";
	var sexMap=new Map();
	var payTypeMap=new Map();
	//结算操作员列表页 显示		
	function diseasetypeFamater(value,row,index){			
		if(value!=null&&value!=""){					
			return diseasetypeList[value];									
		}		
	}
	//结算人列表页 显示		
	function yjjsr(value,row,index){
		if(value!=null&&value!=""){
			for(var i=0;i<yjjsrList.length;i++){
				if(value==yjjsrList[i].id){
					return yjjsrList[i].name;					
				}
			}
		}			
	}
	//结算人列表页 显示		
	function tjdl(value,row,index){
		if(value!=null&&value!=""){
			for(var i=0;i<tjdlList.length;i++){
				if(value==tjdlList[i].encode){
					return tjdlList[i].name;					
				}
			}
		}			
	}
	//根据map对象渲染支付方式
	function funCtionpayMap(value,row,index){
		if(value!=null&&value!=''){
			return payTypeMap.get(value);
		}
	}
		var canRecallFlg = false;//是否能结算召回标记
		$(function(){
			//查询结算操作员名称
			$.ajax({
				url: "<%=basePath%>nursestation/nurse/empCombox.action",				
				type:'post',
				success: function(diseasetypedata) {					
					diseasetypeList = diseasetypedata;					
				}
			});
			//查询统计大类
			$.ajax({
				url: "<%=basePath%>inpatient/settlementRecall/queryCodeCasminfee.action",				
				type:'post',
				success: function(data) {
					tjdlList = data;
				}
			});
			//事件绑定    
			$("#queryInpatientNo").focus(function(){
				$('#queryInvoiceNo').textbox('setValue','');
				clear(false);
			});
			$("#queryInvoiceNo").focus(function(){
				$('#queryInpatientNo').textbox('setValue','');
				clear(false);
			});
			//初始化无数据列表
			$("#infolist").datagrid('loadData', { total: 0, rows: [] });
			$("#infolist2").datagrid('loadData', { total: 0, rows: [] });
			//回车事件
			bindEnterEvent('queryInpatientNo',searchFrom,'easyui');
			
			//性别渲染
			$.ajax({
				url : "<%=basePath %>/baseinfo/pubCodeMaintain/queryDictionary.action",
				data:{"type":"sex"},
				type:'post',
				success: function(data) {
					var v = data;
					for(var i=0;i<v.length;i++){
						sexMap.put(v[i].encode,v[i].name);
					}
				}
			});
		
			$.ajax({
			    url:  basePath+"/baseinfo/pubCodeMaintain/queryDictionary.action?type=payway",
				type:'post',
				success: function(data) {
					var pwtype = data;
					for(var i=0;i<pwtype.length;i++){
						payTypeMap.put(pwtype[i].encode,pwtype[i].name);
					}
				}
			});
		});
		/*******************************开始读卡***********************************************/
		//定义一个事件（读卡）
		function read_card_ic(){
			var card_value = app.read_ic();
			if(card_value=='0'||card_value==undefined||card_value==''){
				$.messager.alert('提示','此卡号['+card_value+']无效');
				return;
			}
			$.ajax({
				url: "<%=basePath%>inpatient/info/queryMedicalrecordId.action",
				data:{idcardOrRe:card_value},
				type:'post',
				async:false,
				success: function(data) { 
					if(data==null||data==''){
						$.messager.alert('提示','此卡号无效');
						return;
					}
					$('#queryInpatientNo').textbox('setValue',data);
					searchFrom();
				}
			});
		};
		/*******************************结束读卡***********************************************/
		/*******************************开始读身份证***********************************************/
			//定义一个事件（读身份证）
			function read_card_sfz(){
				var card_value = app.read_sfz();
				if(card_value=='0'||card_value==undefined||card_value==''){
					$.messager.alert('提示','此卡号['+card_value+']无效');
					return;
				}
				$.ajax({
					url: "<%=basePath%>inpatient/info/queryMedicalrecordId.action",
					data:{idcardOrRe:card_value},
					type:'post',
					async:false,
					success: function(data) {
						if(data==null||data==''){
							$.messager.alert('提示','此卡号无效');
							return;
						}
						$('#queryInpatientNo').textbox('setValue',data);
						searchFrom();
					}
				});
			};
		/*******************************结束读身份证***********************************************/
		
		/**
		 * 表单提交
		 * @author  hedong
		 * @date 2015-08-12
		 * @version 1.0
		 */
		function submit(){
			var param="";
			param=param+"invoiceNo="+payArr[0].invoiceNo;
			param=param+"&balanceNo="+payArr[0].balanceNo;
			param=param+"&inpatientNo="+payArr[0].inpatientNo;
			param=param+"&payObjId="+payArr[0].id;
			var invoiceArrIds ="";
			//结算投标发票组结算信息数组
			for(var i=0;i<invoiceArr.length;i++){
				if(i<invoiceArr.length-1){
					invoiceArrIds=invoiceArrIds+invoiceArr[i].id+"_";
				}else{
					invoiceArrIds=invoiceArrIds+invoiceArr[i].id;
				}
			}
			param=param+"&invoiceArrIds="+invoiceArrIds;
			
			var balanceListIds="";
			//结算明细arr
			for(var i=0;i<balanceListIdArr.length;i++){
				if(i<balanceListIdArr.length-1){
					balanceListIds=balanceListIds+balanceListIdArr[i].id+"_";
				}else{
					balanceListIds=balanceListIds+balanceListIdArr[i].id;
				}
			}
			param=param+"&balanceListIds="+balanceListIds;
			prePayIds="";
			for(var i=0;i<prePayIdArr.length;i++){
				if(i<prePayIdArr.length-1){
					prePayIds=prePayIds+prePayIdArr[i].id+"_";
				}else{
					prePayIds=prePayIds+prePayIdArr[i].id;
				}
			}
			param=param+"&prePayIds="+prePayIds;
			param=param+"&patientInfoId="+patientInfoArr[0].inPatientInfoId;
			$.messager.progress({text:'正在处理召回,请稍等...',modal:true});
			$.ajax({//结算召回
				url: '<%=basePath%>inpatient/settlementRecall/balanceRecall.action?'+param,
				type:'post',
				success: function(data) {
					$.messager.progress('close');
					 if(data=='success'){ 
						 $.messager.alert('提示信息','结算召回成功！','info');
						 clear(true);
					 }else{
						 $.messager.alert('提示信息',data,'info');
					 }
				},error:function(data){
					$.messager.progress('close');
					$.messager.alert('提示信息','结算失败');
				}
			});
		}
	  	
		//结算对象数组
	  	var payArr =[];
		function payObj(id,invoiceNo,returnCost,supplyCost,balanceType,inpatientNo,balanceNo){ //use factory
			var obj=new Object();
			obj.id=id;
			obj.invoiceNo=invoiceNo;
			obj.returnCost=returnCost;
			obj.supplyCost=supplyCost;
			obj.balanceType=balanceType;
			obj.inpatientNo=inpatientNo;
			obj.balanceNo=balanceNo;
			return obj;
		} 
		//患者信息
		var patientInfoArr =[];
		function patientInfoObj(nurseCellCode,inPatientInfoId,patientName,pactCode,deptCode,inDate,houseDocCode,bedId,reportBirthday){ //use factory
			var obj=new Object();
			obj.nurseCellCode=nurseCellCode;
			obj.inPatientInfoId=inPatientInfoId;
			obj.patientName=patientName;
			obj.pactCode=pactCode;
			obj.deptCode=deptCode;
			obj.inDate=inDate;
			obj.houseDocCode=houseDocCode;
			obj.bedId=bedId;
			obj.reportBirthday=reportBirthday;
			return obj;
		}
		//结算投标发票组结算信息数组
		var invoiceArr =[];
		function invoiceObj(id,invoiceNo,returnCost,supplyCost,balanceType,inpatientNo,balanceNo){ //use factory
			var obj=new Object();
			obj.id=id;
			obj.invoiceNo=invoiceNo;
			obj.returnCost=returnCost;
			obj.supplyCost=supplyCost;
			obj.balanceType=balanceType;
			obj.inpatientNo=inpatientNo;
			obj.balanceNo=balanceNo;
			return obj;
		} 
		//结算明细arr 只存放了id
		var balanceListIdArr =[];
		function balanceListObj(id){ //use factory
			var obj=new Object();
			obj.id=id;
			return obj;
		} 
		var prePayIdArr = [];
		function prePayIdObj(id){ //use factory
			var obj=new Object();
			obj.id=id;
			return obj;
		} 
		function queryByInvoiceNo(queryInvoiceNo){
			$('#queryInvoiceNo').textbox('setValue',queryInvoiceNo);//发票号
			$.ajax({//查询结算头表信息
				url: '<%=basePath%>inpatient/settlementRecall/queryHeadByInvoiceNo.action',
				data:{invoiceNoSearch:queryInvoiceNo},
				type:'post',
				success: function(dataObj) {
					if(dataObj.flg=='EXCEPTION_FLG'){
						canRecallFlg=false;
						$.messager.alert('提示信息','该患者没有已结算的发票，请通过病历号查询！','info');
					}else if(dataObj.flg=='NULL_FLG'){
						canRecallFlg=false;
						$.messager.alert('提示信息','获取发票号出错！','info');
					}else if(dataObj.flg=='RECORD_FLG'){                                               //返还金额                                                                补收金额					//结算类型						//住院流水号					//结算序号
						payArr.push(payObj(dataObj.rows[0].id,dataObj.rows[0].invoiceNo,dataObj.rows[0].returnCost,dataObj.rows[0].supplyCost,dataObj.rows[0].balanceType,dataObj.rows[0].inpatientNo,dataObj.rows[0].balanceNo));
						//根据住院号获得第一条记录作为患者信息 并放入页面区域展示
						$.ajax({
							url: '<%=basePath%>inpatient/settlementRecall/queryPatientInfoByInpatientNo.action',
							data:{inpatientNoSearch:payArr[0].inpatientNo},
							type:'post',
							success: function(dataObj) {
								if(dataObj.flg=='EXCEPTION_FLG'){
									canRecallFlg=false;
									$.messager.alert('提示信息','获取患者信息出现异常！','info');
								}else if(dataObj.flg=='NULL_FLG'){
									canRecallFlg=false;
									$.messager.alert('提示信息','没有获取到患者信息！','info');
								}else if(dataObj.flg=='RECORD_FLG'){
									//住院信息主表id						姓名					合同单位代码 				科室						入院日期					医师代码(住院)					床号  						出生日期					
									patientInfoArr.push(patientInfoObj(dataObj.rows[0].nurseCellCode,dataObj.rows[0].inPatientInfoId,dataObj.rows[0].patientName,dataObj.rows[0].pactCode,dataObj.rows[0].deptCode,dataObj.rows[0].inDate,dataObj.rows[0].houseDocCode,dataObj.rows[0].bedId,dataObj.rows[0].reportBirthday));
									//判断结算对象中的结算类型（出院及直接结算 不可召回） 程序返回
									if(payArr[0].balanceType==1){//出院结算
										canRecallFlg=false;
										$.messager.alert('提示信息','该患者已经出院，不能进行中途结算召回！','info');
									}else if(payArr[0].balanceType==3){//直接结算
										canRecallFlg=false;
										$.messager.alert('提示信息','直接结算不能进行结算召回！','info');
									}else{
										//结算头表的结算序号可以对应结算头表的多条记录（已确认）
										//通过住院号和结算序号 查询住院结算头表 取得结算头表发票组结算信息
										$.ajax({ 
												url: '<%=basePath%>inpatient/settlementRecall/queryHeadByInpatientNoAndBalanceNo.action',
												data:{inpatientNoSearch:payArr[0].inpatientNo,balanceNoSearch:payArr[0].balanceNo},
												type:'post',
												success: function(dataObj) {
													if(dataObj.flg=='0'){
														canRecallFlg=false;
														$.messager.alert('提示信息','获取发票列表出现异常！','error');
													}else if(dataObj.flg=='1'){
														canRecallFlg=false;
														$.messager.alert('提示信息','获取发票列表出错！','info');
													}else if(dataObj.flg=='3'){//1条记录
														$.ajax({
															url: '<%=basePath%>inpatient/settlementRecall/InpatientBalanceHead.action',
															data:{inpatientNoSearch:payArr[0].inpatientNo,balanceNoSearch:payArr[0].balanceNo},
															type:'post',
															success: function(payList){
															balanceDate = payList[0].balanceDate;
															var balanceDate1 = payList[0].balanceDate;
															var wan="${now1}";
															var qian="${now}";
															var qian = new Date(Date.parse(qian.replace(/-/g, "/")));
															var balanceDate = new Date(Date.parse(balanceDate.replace(/-/g, "/")));
															var wan = new Date(Date.parse(wan.replace(/-/g, "/")));
															if(qian<balanceDate){
																if(balanceDate<wan){
																invoiceArr.push(invoiceObj(dataObj.rows[0].id,dataObj.rows[0].invoiceNo,dataObj.rows[0].returnCost,dataObj.rows[0].supplyCost,dataObj.rows[0].balanceType,dataObj.rows[0].inpatientNo,dataObj.rows[0].balanceNo));
																//显示住院信息
																var bedId = patientInfoArr[0].bedId;
																$('#inPatientInfoId').val(patientInfoArr[0].inPatientInfoId);
																$('#patientName').text(patientInfoArr[0].patientName);
																$.ajax({
																		url: '<%=basePath%>inpatient/settlementRecall/BusinessContractunitList.action',
																		data:{pactCode:patientInfoArr[0].pactCode},
																		type:'post',
																		success: function(payList){
																			var name=payList[0].name;
																			$('#pactCode').text(name);
																		}
																	});
																$('#deptCode').text(patientInfoArr[0].deptCode);
																$('#inDate').text(patientInfoArr[0].inDate);
																$('#houseDocCode').text(patientInfoArr[0].houseDocCode);
																$('#bedId').text(patientInfoArr[0].bedId);
																$('#bingqu').text(patientInfoArr[0].nurseCellCode);
															    $('#reportBirthday').text(patientInfoArr[0].reportBirthday);
															    //根据住院流水号及  结算序号，查询住院结算明细表，获取结算明细
															    //根据结算序号，住院流水号，查询预交金表，获取预交金信息。
															     $('#infolist').datagrid({
																	url:'<%=basePath%>inpatient/settlementRecall/queryBalanceListByInpatientNoAndBalanceNo.action',
																	queryParams:{inpatientNoSearch:payArr[0].inpatientNo,balanceNoSearch:payArr[0].balanceNo},
																	onLoadSuccess: function(datas) {
																		for(var i=0;i<datas.rows.length;i++){
																			balanceListIdArr.push(balanceListObj(datas.rows[i].id));
																		}
																	}
																});
																var medicalrecordId = $('#queryInpatientNo').textbox('getValue');
																var medicalreId = $('#medicalrecordId').val();
																//预交信息列表
																$('#infolist2').datagrid({
																	url : '<%=basePath%>inpatient/settlementRecall/queryPrepayByInpatientNoAndBalanceNo.action',
																	queryParams:{inpatientNoSearch:payArr[0].inpatientNo,balanceNoSearch:payArr[0].balanceNo},
																	onLoadSuccess: function(datas) {
																		for(var i=0;i<datas.rows.length;i++){
																			prePayIdArr.push(prePayIdObj(datas.rows[i].id));
																		}
																	}
																});
																
																	var returnCostTemp = dataObj.rows[0].returnCost;
																	var supplyCostTemp = dataObj.rows[0].supplyCost;
																	if(returnCostTemp>0){
																		$('#xianshi').hide();
																		$('#fan').show();
																		$('#fanhuan').textbox('setValue',returnCostTemp);
																		$('#zhipiao2').textbox('setValue','0');
																		$('#huipiao2').textbox('setValue','0');
																	}else if(supplyCostTemp>0){
																		$('#xianshi').hide();
																		$('#yincang').show();
																		$('#xianjinjin').textbox('setValue',supplyCostTemp);
																		$('#zhipiao3').textbox('setValue','0');
																		$('#huipiao3').textbox('setValue','0');
																	}else{
																		$('#xianshi').hide();
																		$('#xian').textbox('setValue','0');
																		$('#zhipiao1').textbox('setValue','0');
																		$('#huipiao1').textbox('setValue','0');
																	}
																	canRecallFlg=true;
																	}
																}else{
																	$.messager.alert('提示显示',"非当日结算的票据无法召回，本发票的结算时间为："+balanceDate1);
																}
															}
														});
													}else if(dataObj.flg=='4'){//多条记录
														$.ajax({
															url: '<%=basePath%>inpatient/settlementRecall/InpatientBalanceHead.action',
															data:{inpatientNoSearch:payArr[0].inpatientNo,balanceNoSearch:payArr[0].balanceNo},
															type:'post',
															success: function(payList){
																balanceDate = payList[0].balanceDate;
																var balanceDate1 = payList[0].balanceDate;
																var wan="${now1}";
																var qian="${now}";
																var qian = new Date(Date.parse(qian.replace(/-/g, "/")));
																var balanceDate = new Date(Date.parse(balanceDate.replace(/-/g, "/")));
																var wan = new Date(Date.parse(wan.replace(/-/g, "/")));
																if(qian<balanceDate){
																	if(balanceDate<wan){
																	var returnCostTemp =0;
																	var supplyCostTemp =0;
																	for(var i=0;i<dataObj.rows.length;i++){
																		returnCostTemp=returnCostTemp+dataObj.rows[i].returnCost;
																		supplyCostTemp=supplyCostTemp+dataObj.rows[i].supplyCost;
																		invoiceArr.push(invoiceObj(dataObj.rows[i].id,dataObj.rows[i].invoiceNo,dataObj.rows[i].returnCost,dataObj.rows[i].supplyCost,dataObj.rows[i].balanceType,dataObj.rows[i].inpatientNo,dataObj.rows[i].balanceNo));
																	}
																	$.messager.confirm('提示信息', '该笔结算有'+""+invoiceArr.length+""+'张发票，召回操作会对所有这些发票进行召回，是否继续？', function(r){
																		if(r){
																			//显示住院信息
																			$('#inPatientInfoId').val(patientInfoArr[0].inPatientInfoId);
																		    $('#patientName').text(patientInfoArr[0].patientName);
																		    $.ajax({
																					url: '<%=basePath%>inpatient/settlementRecall/BusinessContractunitList.action',
																					data:{pactCode:patientInfoArr[0].pactCode},
																					type:'post',
																					success: function(payList){
																						var name=payList[0].name;
																						$('#pactCode').text(name);
																					}
																				});
																			$('#deptCode').text(patientInfoArr[0].deptCode);
																			$('#inDate').text(patientInfoArr[0].inDate);
																			$('#houseDocCode').text(patientInfoArr[0].houseDocCode);
																			$('#bedId').text(patientInfoArr[0].bedId);
																			$('#bingqu').text(patientInfoArr[0].nurseCellCode);
																			$('#reportBirthday').text(patientInfoArr[0].reportBirthday);
																			//根据住院流水号及  结算序号，查询住院结算明细表，获取结算明细
																			//根据结算序号，住院流水号，查询预交金表，获取预交金信息。
																			$('#infolist').datagrid({
																				url:'<%=basePath%>inpatient/settlementRecall/queryBalanceListByInpatientNoAndBalanceNo.action',
																				queryParams:{inpatientNoSearch:payArr[0].inpatientNo,balanceNoSearch:payArr[0].balanceNo},
																				onLoadSuccess: function(datas) {
																					for(var i=0;i<datas.rows.length;i++){
																						 balanceListIdArr.push(balanceListObj(datas.rows[i].id));
																					}
																				 }		 
																			 });
																			var medicalrecordId = $('#queryInpatientNo').textbox('getValue');
																			//预交信息列表
																			$('#infolist2').datagrid({
																				url : '<%=basePath%>inpatient/settlementRecall/queryPrepayByInpatientNoAndBalanceNo.action',
																				queryParams:{inpatientNoSearch:payArr[0].inpatientNo,balanceNoSearch:payArr[0].balanceNo},
																				onLoadSuccess: function(datas) {
																					for(var i=0;i<datas.rows.length;i++){
																						prePayIdArr.push(prePayIdObj(datas.rows[i].id));
																					}
																				 }
																			 });
																			if(returnCostTemp>0){
																				$('#xianshi').hide();
																				$('#fan').show();
																				$('#fanhuan').textbox('setValue',returnCostTemp);
																				$('#zhipiao2').textbox('setValue','0');
																				$('#huipiao2').textbox('setValue','0');
																			}else if(supplyCostTemp>0){
																				$('#xianshi').hide();
																				$('#yincang').show();
																				$('#xianjinjin').textbox('setValue',supplyCostTemp);
																				$('#zhipiao3').textbox('setValue','0');
																				$('#huipiao3').textbox('setValue','0');
																			}else{
																				$('#xianshi').hide();
																				$('#xian').textbox('setValue','0');
																				$('#zhipiao1').textbox('setValue','0');
																				$('#huipiao1').textbox('setValue','0');
																			}
																			 canRecallFlg=true;
																		}else{
																			canRecallFlg=false;
																		}
																	});
																	}
																}else{
																	 $.messager.alert('提示信息',"非当日结算的票据无法召回，本发票的结算时间为："+balanceDate1);
																}
															}
														});
													}
												}
											});
										}
								}
							}
						});
					}
				}
			});
		}
		function queryByInpatientNo(queryInpatientNo){
			var medicalrecordId = $('#queryInpatientNo').textbox('getValue');
			if(medicalrecordId == ''){
				$.messager.alert('提示','请输入病历号！');
				return false;
			}
			//根据病历号来查询住院主表中是否存在该患者
			$.ajax({
				url: '<%=basePath%>inpatient/settlementRecall/InpatientInfoMedicalrecordIdList.action',
				data:{medicalrecordId:medicalrecordId},
				type:'post',
				success:function(dataObj) {
					if(dataObj.length>1){
						$("#diaInpatient").window('open');
						$("#infoDatagrid").datagrid({
							data:dataObj,
							queryParams:{medicalrecordId:medicalrecordId},
							columns:[[
								{field:'inpatientNo',title:'病历号',width:'20%',align:'center'} ,    
								{field:'medicalrecordId',title:'病历号',width:'20%',align:'center'} ,  
								{field:'reportSex',title:'性别',width:'9%',align:'center',formatter:function(value,row,index){
									return sexMap.get(value);
								}},
								{field:'patientName',title:'姓名',width:'20%',align:'center'} ,   
								{field:'certificatesNo',title:'身份证号',width:'30%',align:'center'} 
							]] ,
							onDblClickRow:function(rowIndex, rowData){
								var InpatientNo = dataObj[0].inpatientNo;
								$('#medicalrecordId').val(dataObj[0].medicalrecordId);
								bedId = dataObj[0].bedId;
								//根据住院号查询结算投标 获取为作废的发票号  List 结算对象
								$.ajax({//查询住院信息
									url: '<%=basePath%>inpatient/settlementRecall/queryHeadByInpatientNo.action',
									data:{inpatientNoSearch:InpatientNo},
									type:'post',
									success: function(dataObj) {
										if(dataObj.flg=='1'){//异常提醒
											canRecallFlg=false;
											$.messager.alert('提示信息','该患者没有已结算的发票，请通过发票号查询！','error');
										}else if(dataObj.flg=='2'){ 
											canRecallFlg=false;
											$.messager.alert('提示信息','获取发票号出错！','info');
										}else if(dataObj.flg=='3'){//1条记录  转到发票号查询过程
											$('#queryInvoiceNo').textbox('setValue',dataObj.rows[0].invoiceNo);//发票号
											queryByInvoiceNo(dataObj.rows[0].invoiceNo);//发票号查询
										}else if(dataObj.flg=='4'){//多条记录
											//弹出窗口显示所欲的结算对象List
											var invoiceNos="";
											for(var i=0;i<dataObj.rows.length;i++){
												if(i<dataObj.rows.length-1){
													if(dataObj.rows[i].invoiceNo){//排除空的发票信息
														invoiceNos=invoiceNos+dataObj.rows[i].invoiceNo+"_";
													}
												}else{
													if(dataObj.rows[i].invoiceNo){//排除空的发票信息
														invoiceNos=invoiceNos+dataObj.rows[i].invoiceNo;
													}
												}
											}
											//添加权限
											if(invoiceNos){
												Adddilog("请双击选择发票号",'<%=basePath%>inpatient/settlementRecall/invoiceNoWin.action?invoiceNos='+invoiceNos,'40%','30%');
											}
										}else if(dataObj.flg=='5'){
											canRecallFlg=false;
											$.messager.alert('提示信息','该患者没有已结算的发票，请通过发票号查询！','error');
										}
									}
								});
								$("#diaInpatient").window('close');
							}
						});
					}else if(dataObj.length==1){
						var InpatientNo = dataObj[0].inpatientNo;
						$('#medicalrecordId').val(dataObj[0].medicalrecordId);
						bedId = dataObj[0].bedId;
						//根据住院号查询结算投标 获取为作废的发票号  List 结算对象
						$.ajax({//查询住院信息
							url: '<%=basePath%>inpatient/settlementRecall/queryHeadByInpatientNo.action',
							data:{inpatientNoSearch:InpatientNo},
							type:'post',
							success: function(dataObj) {
								if(dataObj.flg=='1'){//异常提醒
									canRecallFlg=false;
									$.messager.alert('提示信息','该患者没有已结算的发票，请通过发票号查询！','error');
								}else if(dataObj.flg=='2'){
									canRecallFlg=false;
									$.messager.alert('提示信息','获取发票号出错！','info');
								}else if(dataObj.flg=='3'){//1条记录  转到发票号查询过程
									$('#queryInvoiceNo').textbox('setValue',dataObj.rows[0].invoiceNo);//发票号
									queryByInvoiceNo(dataObj.rows[0].invoiceNo);//发票号查询
								}else if(dataObj.flg=='4'){//多条记录
									//弹出窗口显示所欲的结算对象List
									var invoiceNos="";
									for(var i=0;i<dataObj.rows.length;i++){
										if(i<dataObj.rows.length-1){
											if(dataObj.rows[i].invoiceNo){//排除空的发票信息
												invoiceNos=invoiceNos+dataObj.rows[i].invoiceNo+"_";
											}
										}else{
											if(dataObj.rows[i].invoiceNo){//排除空的发票信息
												invoiceNos=invoiceNos+dataObj.rows[i].invoiceNo;
											}
										}
									}
									//添加权限
									if(invoiceNos){
										Adddilog("请双击选择发票号",'<%=basePath%>inpatient/settlementRecall/invoiceNoWin.action?invoiceNos='+invoiceNos,'40%','30%');
									}
								}else if(dataObj.flg=='5'){
									canRecallFlg=false;
									$.messager.alert('提示信息','该患者没有已结算的发票，请通过发票号查询！','error');
								}
							}
						});
					}else{
						 $.messager.alert('提示信息',"没有该患者信息！");
					}
				}
			});
		}
		//加载模式窗口
		function Adddilog(title, url, width, height) {
			$('#menuWin').dialog({    
			    title: title,    
			    width: width,    
			    height: height,    
			    closed: false,    
			    cache: false,    
			    href: url,    
			    modal: true   
			});
		}
		/**
		 * 查询
		 * @author  hedong
		 * @date 2015-08-12
		 * @version 1.0
		 */
		function searchFrom() {
			 $.ajax({
				    url: '<%=basePath%>inpatient/settlementRecall/HospitalParameterListxiugai.action',
					type:'post',
					success: function(payList){
						if(payList[0]=='0'&&payList[1]=='0'&&payList[2]=='0'&&payList[3]=='0'&&payList[4]=='1'){
							clear(false);
							var queryInpatientNo = $('#queryInpatientNo').textbox('getValue');//住院号
							var queryInvoiceNo = $('#queryInvoiceNo').textbox('getValue');//发票号
							if(queryInvoiceNo){//searchInvoiceNo
								$('#queryInvoiceNo').textbox('setValue','');//发票号
								queryByInvoiceNo(queryInvoiceNo);
							}else if(queryInpatientNo){
								queryByInpatientNo(queryInpatientNo);
							}else if(queryInpatientNo==''&&queryInvoiceNo==''){
								$.messager.alert('提示信息','发票号和病历号至少其中一项不能为空！','error');
							}
						}
					}	
				});
		};
		/**
		*清空数据
		*/
		function clear(searchFlg){
			canRecallFlg=false;
			payArr.length=0;
			patientInfoArr.length=0;
			invoiceArr.length=0;
			balanceListIdArr.length=0;
			prePayIdArr.length=0;
			if(searchFlg){
				$('#queryInpatientNo').textbox('setValue','');//住院号
				$('#queryInvoiceNo').textbox('setValue','');//发票号
			}
			
			$('#inPatientInfoId').val('');
			$('#patientName').text('');
			$('#pactCode').text('');
			$('#deptCode').text('');
			$('#inDate').text('');
			$('#houseDocCode').text('');
			$('#bedId').text('');
			$('#reportBirthday').text('');
			$('#bingqu').text('');
			$('#xian').textbox('setValue','');
			$('#zhipiao1').textbox('setValue','');
			$('#huipiao1').textbox('setValue','');
			
			$('#fanhuan').textbox('setValue','');
			$('#zhipiao2').textbox('setValue','');
			$('#huipiao2').textbox('setValue','');
			
			$('#xianjinjin').textbox('setValue','');
			$('#zhipiao3').textbox('setValue','');
			$('#huipiao3').textbox('setValue','');
			$('#returnCost').textbox('setValue','');
			$('#supplyCost').textbox('setValue','');
			//初始化无数据列表
			var item = $('#infolist').datagrid('getRows');    
            for (var i = item.length - 1; i >= 0; i--) {    
                var index = $('#infolist').datagrid('getRowIndex', item[i]);    
                $('#infolist').datagrid('deleteRow', index);    
            }
            var item = $('#infolist2').datagrid('getRows');    
            for (var i = item.length - 1; i >= 0; i--) {    
                var index = $('#infolist2').datagrid('getRowIndex', item[i]);    
                $('#infolist2').datagrid('deleteRow', index);    
            }
		}
		/**
		 * 弹出框
		 * @author  hedong
		 * @date 2015-08-12
		 * @version 1.0
		 */
			var win;	
			function showWin (title,url, width, height) {
			   	var content = '<iframe id="myiframe" src="' + url + '" width="100%" height="100%" frameborder="0" scrolling="no"></iframe>';
			    var divContent = '<div id="treeDeparWin">';
			    win = $('<div id="treeDeparWin"><div/>').dialog({
			        content: content,
			        width: width,
			        height: height,
			        modal: true,
			        minimizable:false,
			        maximizable:true,
			        resizable:true,
			        shadow:true,
			        center:true,
			        title: title
			    });
			    win.dialog('open');
			}
		</script>
		<style type="text/css">
		.panel-header{
			border-top:0;
		}
		.panel-body{
			border-bottom:0;
		}
		</style>
</head>
<body>
	<div id="cc" class="easyui-layout" data-options="fit:true">   
	    <div data-options="region:'north',border:false" style="height: 162px;">
	    	<div id="dd" class="easyui-layout" style="width:100%;height:160px;">   
			    <div data-options="region:'north',border:false" style="height:55px;padding-top: 17px;">
					病历号：
					<input type="text" id="queryInpatientNo" name="queryInpatientNo" value=""  onkeydown="KeyDown()" class="easyui-textbox"/>&nbsp;&nbsp;
					<input type="hidden" id="medicalrecordId"/>
					发票号：
					<input type="text" id="queryInvoiceNo" name="queryInvoiceNo" onkeydown="KeyDown()" class="easyui-textbox"/>&nbsp;&nbsp;
					<shiro:hasPermission name="${menuAlias}:function:query">
						<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:readCard">
						<a href="javascript:void(0)" cardNo="" onclick="read_card_ic()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读卡</a>
					</shiro:hasPermission>
		        	<shiro:hasPermission name="${menuAlias}:function:readIdCard">
		        		<a href="javascript:void(0)" cardNo="" onclick="read_card_sfz()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读身份证</a>
					</shiro:hasPermission>
					<a href="javascript:clear(true);void(0)" data-options="iconCls:'icon-clear'" class="easyui-linkbutton">清空</a>
					<shiro:hasPermission name="${menuAlias}:function:save">
						<a href="javascript:submit();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-recall'">结算召回</a>
					</shiro:hasPermission>
			    </div>   
			    <div data-options="region:'center',border:false" style="height: 90px;">
			    	<fieldset style="border:1px solid #95b8e7;margin-left:auto;margin-right:auto;" class="changeskin">
						<legend><font style="font-weight: bold;font-size: 12px;">患者信息</font></legend>
							<table  class="tableCss" cellpadding="1" cellspacing="1" border="1px solid black" id="list" fit=true>
								<tr>					
									<td style="width:12.5%" class="TDlabel">患者姓名：<input type="hidden" name="inPatientInfoId" id="inPatientInfoId" value=""/></td>
									<td style="width:12.5%" id="patientName"/></td>
									<td style="width:12.5%" class="TDlabel">合同单位：</td>
									<td style="width:12.5%" id="pactCode"/></td>
									<td style="width:12.5%" class="TDlabel">住院科室：</td>
									<td style="width:12.5%" id="deptCode"/></td>
									<td style="width:12.5%" class="TDlabel">所属病区：</td>
									<td style="width:12.5%" id="bingqu"/></td>
								</tr>
								<tr>
									<td style="width:12.5%" class="TDlabel">入院日期：</td>
	    							<td style="width:12.5%" id="inDate"/></td>
									<td style="width:12.5%" class="TDlabel">住院医师：</td>
	    							<td style="width:12.5%" id="houseDocCode"/></td>
					                <td style="width:12.5%" class="TDlabel">病床号：</td>
	    							<td style="width:12.5%" id="bedId"/></td>
					                <td style="width:12.5%" class="TDlabel">出生日期：</td>
	    					    	<td style="width:12.5%" id="reportBirthday"/></td>					
								</tr>
						</table>
					</fieldset>
			    </div>   
			</div> 
	    </div>   
	    <div data-options="region:'south'" style="height: 150px;padding-top: 10px;">
	    	<fieldset id="xianshi" style="border:1px solid #95b8e7;margin-left:auto;margin-right:auto;" class="changeskin"><legend><font style="font-weight: bold;font-size: 12px;">收支平衡</font></legend>
			  <table  class="tableCss" cellpadding="1" cellspacing="1"	border="1px solid black" id="list" fit=true>
			     <tr >
				    <td class="TDlabel">现金：</td>
						<td><input class="easyui-numberbox" id="xian" name="xian" value="" readonly="readonly"/></td>
					<td class="TDlabel">支票：</td>
						<td><input class="easyui-numberbox" id="zhipiao1" name="zhipiao" value="" readonly="readonly"/></td>
						<td class="TDlabel">汇票：</td>
						<td><input class="easyui-numberbox" id="huipiao1" name="huipiao" value="" readonly="readonly"/></td>
			     </tr>
			  </table>
			</fieldset>
			<fieldset id="fan" style="display: none;border:1px solid #95b8e7;margin-left:auto;margin-right:auto;" class="changeskin"><legend><font style="font-weight: bold;font-size: 12px;">补收金额</font></legend>
			  <table  class="tableCss" cellpadding="1" cellspacing="1"	border="1px solid black" id="list">
			     <tr >
				    <td class="TDlabel">返还金额：</td>
						<td><input class="easyui-numberbox" id="fanhuan" name="fanhuan" value="" readonly="readonly"/></td>
					<td class="TDlabel">支票：</td>
						<td><input class="easyui-numberbox" id="zhipiao2" name="zhipiao" value="" readonly="readonly"/></td>
						<td class="TDlabel">汇票：</td>
						<td><input class="easyui-numberbox" id="huipiao2" name="huipiao" value="" readonly="readonly"/></td>
			     </tr>
			  </table>
			</fieldset>
			<fieldset id="yincang" style="display: none;border:1px solid #95b8e7;margin-left:auto;margin-right:auto;" class="changeskin"><legend><font style="font-weight: bold;font-size: 12px;">返还金额</font></legend>
			  <table  class="tableCss" cellpadding="1" cellspacing="1"	border="1px solid black" id="list">
			     <tr >
				    <td class="TDlabel">补收金额：</td>
						<td><input class="easyui-numberbox" id="xianjinjin" name="xianjinjin" value="" readonly="readonly"/></td>
					<td class="TDlabel">支票：</td>
						<td><input class="easyui-numberbox" id="zhipiao3" name="zhipiao" value="" readonly="readonly"/></td>
						<td class="TDlabel">汇票：</td>
						<td><input class="easyui-numberbox" id="huipiao3" name="huipiao" value="" readonly="readonly"/></td>
			     </tr>
			  </table>
			</fieldset>
	    </div>   
	    <div data-options="region:'center'" style="height: 40%;">
	    	<div id="ee" class="easyui-layout" style="width:100%;height:100%;">
	    		<div data-options="region:'west',split:true,title:'结算费用明细',iconCls:'icon-book',border:true"
						style="padding: 10px;width:50%;height:100%;">
					<table id="infolist" class="easyui-datagrid" data-options="method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false">
						<thead>
							<tr>
								<th data-options="field:'statCode',formatter:tjdl" style="width: 20%">
									费用科目
								</th>
								<th data-options="field:'totCost'" style="width: 24%">
									费用金额
								</th>
								<th data-options="field:'balanceOpercode',formatter:diseasetypeFamater" style="width: 28%">
									结算操作员（姓名）
								</th>
								<th data-options="field:'balanceDate'" style="width: 24%">
									结算时间
								</th>
							</tr>
						</thead>
					</table>	
				</div> 
				<div data-options="region:'center',split:false,title:'预交金明细',iconCls:'icon-book',border:true"
						style="padding: 10px;width:50%;height:100%;">
					<table id="infolist2" class="easyui-datagrid" data-options="method:'post',rownumbers:true,idField: 'id2',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false">
						<thead>
							<tr>
								<th data-options="field:'receiptNo'" style="width: 19%">
									预交金票据号
								</th>
								<th data-options="field:'payWay',formatter:funCtionpayMap" style="width: 19%">
									支付方式
								</th>
								<th data-options="field:'prepayCost'" style="width: 19%">
									预交金额
								</th>
								<th data-options="field:'createUser',formatter:diseasetypeFamater" style="width: 19%">
									结算人
								</th>
								<th data-options="field:'createTime'" style="width: 19%">
									结算时间
								</th>
							</tr>
						</thead>
					</table>
				</div>  
			</div> 
	    </div>   
	</div>
	<div id="diaInpatient" class="easyui-dialog" title="患者选择" style="width:650;height:600;padding:10" data-options="modal:true, closed:true">   
   		<table id="infoDatagrid"  data-options="fitColumns:true,singleSelect:true,fit:true,rownumbers:true">   
		</table>  
	</div>
	<div id="menuWin"></div>
</body>
</html>