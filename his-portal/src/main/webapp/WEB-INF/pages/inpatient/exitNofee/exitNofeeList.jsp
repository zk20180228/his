<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>无费出院管理</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>easyui1.4.5/custom_css/from_base.css">
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
			
			function del(){	
				 //选中要删除的行	
		            var ids = $('#id').val();				 
		           	if (ids!="") {//选中几行的话触发事件	                        
						 	$.messager.confirm('确认', '确定要选中的信息进行无费退院操作吗?', function(e){
						 		if(e){ 	
										if($('#inState').val()=='N'){
											$.messager.alert('提示','患者已经出院！');
											setTimeout(function(){
												$(".messager-body").window('close');
											},3500);
											ids="";
											return;
										}
										if($('#prepayCost').textbox('getValue')>0){
											$.messager.alert('提示','患者预交金大于0，不可以无费退院！');
											setTimeout(function(){
												$(".messager-body").window('close');
											},3500);
											ids="";
											return;
										}		
										if($('#totCost').textbox('getValue')>0){
											$.messager.alert('提示','患者已经发生费用或结算记录，不能无费退院！');
											setTimeout(function(){
												$(".messager-body").window('close');
											},3500);
											ids="";
											return;
										}
										if($('#babyFlag').val()==1){
											$.messager.alert('提示','患者是婴儿，无需办理退院！');
											setTimeout(function(){
												$(".messager-body").window('close');
											},3500);
											ids="";
											return;
										}																												
									if(ids!=''){
										var inpatientNo = $("#inpatientNo").val();
										var deptCode = $("#deptCode").val();
										var bedId = $("#bedId").val();
										var nurseCellCode = $("#nurseCellCode").val();
										var bedName = $("#bedName").html();
										var deptName = $("#deptName").html();
										var nurseCellName = $("#nurseCellName").val();
										$.ajax({ 
										    url: '<%=basePath%>inpatient/exitNofee/exitNoFeeInpatient.action',	
										    data:'ids='+ids+'&inpatientInfo.inpatientNo='+inpatientNo+'&inpatientInfo.deptCode='+deptCode+'&inpatientInfo.deptName='+deptName+'&inpatientInfo.bedId='+bedId+'&inpatientInfo.bedName='+bedName+'&inpatientInfo.nurseCellCode='+nurseCellCode+'&inpatientInfo.bingqu='+nurseCellName,
										    type:'post',
										    success:function(data){ 									
												  if(data.resMsg=="error"){
													  $.messager.alert('提示','无费退院失败！');
												  }else if(data.resMsg=="success"){
													  $.messager.alert('提示','无费退院成功！');
													  setTimeout(function(){
															$(".messager-body").window('close');
														},3500);
													  clean();
													  $("input").val('');
												  }else{
													  $.messager.alert('提示','编码表出错了，操作失败！');
												  }
										    }
										});
									}
						 		}
                      		});
                  	}else if(ids==''){
                  		 $.messager.alert('提示','请查询患者信息！');
					 	 setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
                  		
                  	}
			}
			function clean(){
				$("#patientName").html('');
				$("#inDate").html('');
				$("#bedName").html('');
				$("#deptName").html('');
				$("#pactName").html('');
				$("#houseDocName").html('');
				$("#inpatientNoSerc").textbox('setValue','');
				$("#medicalrecordId").textbox('setValue','');
				
				$("#prepayCost").textbox('setValue','');
				$("#payCost").textbox('setValue','');
				$("#ownCost").textbox('setValue','');
				$("#totCost").textbox('setValue','');
				$("#pubCost").textbox('setValue','');
				$("#freeCost").textbox('setValue','');
			}
			function out(){	
				self.parent.$('#tabs').tabs('close',"无费出院");
			}
			function searchFromM(){
				var inpatientNoSerc = $('#medicalrecordId').textbox('getValue');
				if(inpatientNoSerc == ''){
					$.messager.alert('提示','请输入病历号！');
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return false;
				}
				else{	
					    $.messager.progress({text:'查询中，请稍后...',modal:true});
					    $.ajax({
							url: '<%=basePath%>inpatient/exitNofee/queryExitNoFeeM.action',
							data:'inpatientInfo.medicalrecordId='+inpatientNoSerc,
							type:'post',
							datatype:'json',
							success: function(data) {
								var json1=data;
								if(json1==''){
									$.messager.alert('提示','该患者不存在！');	
									setTimeout(function(){
										$(".messager-body").window('close');
									},3500);
									$.messager.progress("close");
									clean();
								}else if(json1.length==1){
									$.messager.progress("close");
									$.ajax({
											url: '<%=basePath%>inpatient/exitNofee/queryExitNoFeeByInNo.action',
											data:'inpatientInfo.inpatientNo='+data[0].inpatientNo,
											type:'post',
											datatype:'json',
											success:function(data){
												var json = data;
												if(json ==''){
													$.messager.alert('提示','患者已消费,请进行出院结算');
													setTimeout(function(){
														$(".messager-body").window('close');
													},3500);
													return ;
												}
												if(json[0].inState=='N'){
													$.messager.alert('提示','该患者已经办理无费出院');
													setTimeout(function(){
														$(".messager-body").window('close');
													},3500);
													return ;
												}
												if(json[0].inState=='O'){
													$.messager.alert('提示','该患者已经办理出院结算');
													setTimeout(function(){
														$(".messager-body").window('close');
													},3500);
													return ;
												}
												$("#patientName").html(json[0].patientName);					
												$("#inDate").html(json[0].inDate);
												$("#bedName").html(json[0].bedName);
												$("#prepayCost").textbox('setValue',String(json[0].prepayCost==null?'':json[0].prepayCost));
												$("#payCost").textbox('setValue',String(json[0].payCost==null?'':json[0].payCost));
												$("#ownCost").textbox('setValue',String(json[0].ownCost==null?'':json[0].ownCost));
												$("#totCost").textbox('setValue',String(json[0].totCost==null?'':json[0].totCost));
												$("#pubCost").textbox('setValue',String(json[0].pubCost==null?'':json[0].pubCost));
												$("#freeCost").textbox('setValue',String(json[0].freeCost==null?'':json[0].freeCost));
												$("#medicalrecordId").textbox('setValue',json[0].medicalrecordId);
												$("#inpatientNoSerc").textbox('setValue',json[0].idcardNo);
												$("#id").val(json[0].id);
												$("#inState").val(json[0].inState);
												$("#babyFlag").val(json[0].babyFlag);
												$("#deptCode").val(json[0].deptCode);
												$("#bedId").val(json[0].bedId);
												$("#nurseCellCode").val(json[0].nurseCellCode);
												$("#inpatientNo").val(json[0].inpatientNo);
												$("#nurseCellName").val(json[0].nurseCellName);
												$("#houseDocName").html(json[0].houseDocName);
												$("#deptName").html(json[0].deptName);

												$.ajax({
													url: '<%=basePath%>inpatient/info/likeRegcon.action',
													type:'post',
													success: function(regdata) {
														var regjson=regdata;	

														for(var i=0;i<regjson.length;i++){
															if(json[0].pactCode==regjson[i].encode){
																$("#pactName").html(regjson[i].name);
															}
														}
													}
												});
											}
									})
									
								}else if(json1.length>1){
									$.messager.progress("close");
									Adddilog("请双击选择患者",'<%=basePath%>inpatient/exitNofee/inpatientInfoListxuanze.action?inpatientNoMerc='+inpatientNoSerc,'25%','50%');
								}
							}
						});
					}
			}
				/**
				 * 查询
				 * @author  yeguanqun
				 * @param title 标签名称
				 * @param title 跳转路径
				 * @date 2015-12-8
				 * @version 1.0
				 */
				function searchFrom() {	
					var inpatientNoSerc = $('#inpatientNoSerc').textbox('getValue');
					if(inpatientNoSerc == ''){
						$.messager.alert('提示','请输入就诊卡号！');
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						return false;
					}
					else{	
						    $.messager.progress({text:'查询中，请稍后...',modal:true});
						    $.ajax({
								url: '<%=basePath%>inpatient/exitNofee/queryExitNoFee.action',
								data:'inpatientInfo.idcardNo='+inpatientNoSerc,
								type:'post',
								datatype:'json',
								success: function(data) {
									var json1=data;
									if(json1==''){
										$.messager.alert('提示','该患者不存在！');	
										setTimeout(function(){
											$(".messager-body").window('close');
										},3500);
										$.messager.progress("close");
										clean();
									}else if(json1.length==1){
										$.messager.progress("close");
										$.ajax({
												url: '<%=basePath%>inpatient/exitNofee/queryExitNoFeeByInNo.action',
												data:'inpatientInfo.inpatientNo='+data[0].inpatientNo,
												type:'post',
												datatype:'json',
												success:function(data){
													var json = data;
													if(json ==''){
														$.messager.alert('提示','患者已消费,请进行出院结算');
														setTimeout(function(){
															$(".messager-body").window('close');
														},3500);
														return ;
													}
													if(json[0].inState=='N'){
														$.messager.alert('提示','该患者已经办理无费出院');
														setTimeout(function(){
															$(".messager-body").window('close');
														},3500);
														return ;
													}
													if(json[0].inState=='O'){
														$.messager.alert('提示','该患者已经办理出院结算');
														setTimeout(function(){
															$(".messager-body").window('close');
														},3500);
														return ;
													}
													$("#patientName").html(json[0].patientName);					
													$("#inDate").html(json[0].inDate);
													$("#bedName").html(json[0].bedName);
													$("#prepayCost").textbox('setValue',String(json[0].prepayCost==null?'':json[0].prepayCost));
													$("#payCost").textbox('setValue',String(json[0].payCost==null?'':json[0].payCost));
													$("#ownCost").textbox('setValue',String(json[0].ownCost==null?'':json[0].ownCost));
													$("#totCost").textbox('setValue',String(json[0].totCost==null?'':json[0].totCost));
													$("#pubCost").textbox('setValue',String(json[0].pubCost==null?'':json[0].pubCost));
													$("#freeCost").textbox('setValue',String(json[0].freeCost==null?'':json[0].freeCost));
													$("#medicalrecordId").textbox('setValue',json[0].medicalrecordId);
													$("#inpatientNoSerc").textbox('setValue',json[0].idcardNo);
													$("#id").val(json[0].id);
													$("#inState").val(json[0].inState);
													$("#babyFlag").val(json[0].babyFlag);
													$("#deptCode").val(json[0].deptCode);
													$("#bedId").val(json[0].bedId);
													$("#nurseCellCode").val(json[0].nurseCellCode);
													$("#inpatientNo").val(json[0].inpatientNo);
													$("#nurseCellName").val(json[0].nurseCellName);
													$("#houseDocName").html(json[0].houseDocName);
													$("#deptName").html(json[0].deptName);

													$.ajax({
														url: '<%=basePath%>inpatient/info/likeRegcon.action',
														type:'post',
														success: function(regdata) {
															var regjson=regdata;	

															for(var i=0;i<regjson.length;i++){
																if(json[0].pactCode==regjson[i].encode){
																	$("#pactName").html(regjson[i].name);
																}
															}
														}
													});
												}
										})
										
									}else if(json1.length>1){
										$.messager.progress("close");
										Adddilog("请双击选择患者",'<%=basePath%>inpatient/exitNofee/inpatientInfoListxuanze.action?inpatientNoMerc='+inpatientNoSerc,'25%','50%');
									}
								}
							});
						}
					}
					function queryByInfo(data){
						$.ajax({
							url: '<%=basePath%>inpatient/exitNofee/queryExitNoFeeByInNo.action',
							data:'inpatientInfo.inpatientNo='+data.inpatientNo,
							type:'post',
							datatype:'json',
							success:function(data){
								var rowData = data[0];
								if(rowData ==null){
									$.messager.alert('提示','患者已消费,请进行出院结算');
									setTimeout(function(){
										$(".messager-body").window('close');
									},3500);
									return ;
								}
								if(rowData.inState=='N'){
									$.messager.alert('提示','该患者已经办理无费出院');
									setTimeout(function(){
										$(".messager-body").window('close');
									},3500);
									return ;
								}
								if(rowData.inState=='O'){
									$.messager.alert('提示','该患者已经办理出院结算');
									setTimeout(function(){
										$(".messager-body").window('close');
									},3500);
									return ;
								}
								
								$("#patientName").html(rowData.patientName);
								$("#inDate").html(rowData.inDate);
								$("#bedName").html(rowData.bedName);
								$("#prepayCost").textbox('setValue',String(rowData.prepayCost==null?'':rowData.prepayCost));
								$("#payCost").textbox('setValue',String(rowData.payCost==null?'':rowData.payCost));
								$("#ownCost").textbox('setValue',String(rowData.ownCost==null?'':rowData.ownCost));
								$("#totCost").textbox('setValue',String(rowData.totCost==null?'':rowData.totCost));
								$("#pubCost").textbox('setValue',String(rowData.pubCost==null?'':rowData.pubCost));
								$("#freeCost").textbox('setValue',String(rowData.freeCost==null?'':rowData.freeCost));
								$("#medicalrecordId").textbox('setValue',rowData.medicalrecordId);
								$("#inpatientNoSerc").textbox('setValue',rowData.idcardNo);
								$("#id").val(rowData.id);
								$("#inState").val(rowData.inState);
								$("#babyFlag").val(rowData.babyFlag);
								$("#deptCode").val(rowData.deptCode);
								$("#bedId").val(rowData.bedId);
								$("#nurseCellCode").val(rowData.nurseCellCode);
								$("#inpatientNo").val(rowData.inpatientNo);
								$("#nurseCellName").val(rowData.nurseCellName); 
								$("#houseDocName").html(rowData.houseDocName);
								$("#deptName").html(rowData.deptName);
								$.ajax({
									url: '<%=basePath%>inpatient/info/likeRegcon.action',
									type:'post',
									success: function(regdata) {
										var regjson=regdata;										
										for(var i=0;i<regjson.length;i++){
											if(rowData.pactCode==regjson[i].encode){
												$("#pactName").html(regjson[i].name);
											}
										}
									}
								});
							}
						})
					}
					
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
							$('#medicalrecordId').textbox('setValue',data);
							searchFromM();
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
								$('#medicalrecordId').textbox('setValue',data);
								searchFromM();
							}
						});
					};
				/*******************************结束读身份证***********************************************/
				/**
				 * 回车键查询
				 * @author  yeguanqun
				 * @param title 标签名称
				 * @param title 跳转路径
				 * @date 2015-12-9
				 * @version 1.0
				 */		
			    $(function(){
			     	bindEnterEvent('medicalrecordId',searchFromM,'easyui');
			    });
			 				    				  											
				//加载模式窗口
				function AdddilogModel(id,title,url,width,height) {
					$('#'+id).dialog({    
					    title: title,    
					    width: width,    
					    height: height,    
					    closed: false,    
					    cache: false,
					    href: url,    
					    modal: true   
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
		</script>
</head>
<body style="margin: 0px; padding: 0px">
	<div id="divLayout"  class="easyui-layout" fit=true>
		<div data-options="region:'center',split:false"
			style="padding: 10px; min-height: 80px; height: auto;border-top:0">
			<div id="top" style="height: 25px; line-height: 25px;padding:0px 0px 20px 0px">
			<fieldset style="width:854px;border:1px;margin-left:auto;margin-right:auto;">	
				<table style="width:100%;padding:5px 5px 1px 5px;"> 
					<tr>
						<td>
							病历号：<input style="width:180px;" ID="medicalrecordId"  class="easyui-textbox" data-options="prompt:'输入病历号回车查询'" />							
						<shiro:hasPermission name="${menuAlias}:function:query">
							&nbsp;&nbsp;<a href="javascript:void(0)" onclick="searchFromM()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="${menuAlias}:function:exit">						
							<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" iconCls="icon-return">退院</a>
						</shiro:hasPermission>
							<a href="javascript:void(0)" onclick="clean()" class="easyui-linkbutton" iconCls="icon-clear">清屏</a>
						<shiro:hasPermission name="${menuAlias}:function:readCard">
							<a href="javascript:void(0)" cardNo="" onclick="read_card_ic()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读卡</a>
						</shiro:hasPermission>
			        	<shiro:hasPermission name="${menuAlias}:function:readIdCard">
			        		<a href="javascript:void(0)" cardNo="" onclick="read_card_sfz()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读身份证</a>
						</shiro:hasPermission>
						</td>
					</tr>
				</table>
			</fieldset>
			</div>
		
		<div style="font-size:14px;">		
				<fieldset style="width:854px;border:1px solid #95b8e7;margin-top:5px;margin-left:auto;margin-right:auto;" class="changeskin">	
					<legend style="text-align:left;font-size:14px;">患者信息</legend>		
					<table id="patientinfo" class="honry-table" style="width: 100%;align:center;font-size:14px;"  data-options="method:'post',idField:'id',split:false">
						<thead> 
						<tr>
							<td class="honry-lable" width="25%">患者姓名  ：</td>
							<td id="patientName" width="25%"></td>
							<td class="honry-lable" width="25%">合同单位  ：</td>
							<td id="pactName" width="25%"></td>
						</tr>
						<tr>
							<td class="honry-lable" width="25%">住院科室  ：</td>
							<td id="deptName" width="25%"></td>
							<td class="honry-lable" width="25%">入院日期  ：</td>
							<td id="inDate" width="25%"></td>
						</tr>
						<tr>
							<td class="honry-lable" width="25%">住院医生  ：</td>
							<td id="houseDocName" width="25%"></td>
							<td class="honry-lable" width="25%">床号  ：</td>
							<td id="bedName" width="25%"></td>
						</tr>
						</thead>
					</table>					
				</fieldset>
				<fieldset style="width:854px;border:1px solid #95b8e7;margin-top:15px;margin-left:auto;margin-right:auto;" class="changeskin">	
					<legend style="text-align:left;font-size:14px;">费用信息</legend>		
					<table class="honry-table" style="width: 100%;"  data-options="method:'post',idField:'id',split:false">
						<thead> 
						<tr>
							<td class="honry-lable" width="25%">预交金总额  ：</td>
							<td id="nos" width="25%"><input name="prepayCost" id="prepayCost" class="easyui-textbox" data-options="readonly:true"/></td>
							<td class="honry-lable" width="25%">自付金额  ：</td>
							<td id="midicalrecordNo" width="25%"><input name="payCost" id="payCost" class="easyui-textbox" data-options="readonly:true" ></td>
						</tr>
						<tr>
							<td class="honry-lable" width="25%">自费金额 ：</td>
							<td id="name" width="25%"><input name="ownCost" id="ownCost" class="easyui-textbox" data-options="readonly:true" ></td>
							<td class="honry-lable" width="25%">费用金额  ：</td>
							<td id="sex" width="25%"><input name="totCost" id="totCost" class="easyui-textbox" data-options="readonly:true" ></td>
						</tr>
						<tr>
							<td class="honry-lable" width="25%">记账金额  ：</td>
							<td id="age" width="25%"><input name="pubCost" id="pubCost" class="easyui-textbox" data-options="readonly:true" ></td>
							<td class="honry-lable" width="25%">费用余额  ：</td>
							<td id="ageUnits" width="25%"><input name="freeCost" id="freeCost" class="easyui-textbox" data-options="readonly:true" ></td>
						</tr>
						</thead>
					</table>				
				</fieldset>
			</div>
				<input id="id" type="hidden"><input id="inState" type="hidden"><input id="babyFlag" type="hidden"><input id="inpatientNo" type="hidden">
				<input id="deptCode" type="hidden"><input id="bedId" type="hidden"><input id="nurseCellCode" type="hidden"><input id="nurseCellName" type="hidden">
	</div>
	<input id="arrearageId" type="hidden" value="hszsf"/> 
	<div id="arrearageInfo-window"> 
	</div> 
	<div id="menuWin"></div>
</body>
</html>