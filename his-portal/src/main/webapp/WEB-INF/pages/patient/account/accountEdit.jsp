<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<style type="text/css">
	.tableCss{
			border-collapse: collapse;
			border-spacing: 0;
			border-left: 1px solid #95b8e7;
			border-top: 1px solid #95b8e7;
	}
	.tableLabel{
		text-align: right;
		width:100px;
	}
	.tableCss td{
		border-right: 1px solid #95b8e7;
		border-bottom: 1px solid #95b8e7;
		padding: 5px 15px;
		word-break: keep-all;
		white-space:nowrap;
	}
</style>
<script type="text/javascript">
var payTypeVal = null;
	$.ajax({
		  url:'<%=basePath%>inpatient/info/getdengjiInfo.action?medicalrecordId='+pid,
		  success:function(data){
			  payTypeVal=data;
			}
	 });
	var paywayMap=new Map();
	$.ajax({
	    url:  basePath+"/baseinfo/pubCodeMaintain/queryDictionary.action?type=payway",
		type:'post',
		success: function(data) {
			var waytype = data;
			for(var i=0;i<waytype.length;i++){
				paywayMap.put(waytype[i].encode,waytype[i].name);
			}
		}
	});
</script>
</head>
<body>
	<div style="width:100%;">
		<div style="padding: 5px">
			<form id="editForm" method="post">
				<!--<input type="hidden" id="id" name="account.id" value="${account.id }">
					<input type="hidden" id="createUser" name="account.createUser" value="${account.createUser }">
					<input type="hidden" id="createDept" name="account.createDept" value="${account.createDept }">
					<input type="hidden" id="createTime" name="account.createTime" value="${account.createTime }">
				<input type="hidden" id="accountPassword" name="account.accountPassword" value="${account.accountPassword }">
					-->
				<input type="hidden" id="accountId" name="account.id">
				<input type="hidden" id="detailOptype" name="repaydetail.detailOptype">
				<input type="hidden" id="detailAccounttype" name="detailAccounttype" value="${detailAccounttype}">
				<input type="hidden" id="menuAlias" name="menuAlias" value="${menuAlias}">
				<input type="hidden" id="patientMedicalrecorde" name="account.medicalrecordId">
				<div style="text-align: left; padding: 5px 5px 5px 0px;">
					<shiro:hasPermission name="${menuAlias}:function:disable">
						<a href="javascript:void(0)" cardNo="" onclick="read_card_ic()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读卡</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:disable">
						<a id="stop" href="javascript:stopFlg(1);void(0)"
							class="easyui-linkbutton"
							data-options="iconCls:'icon-database_stop',disabled:'true'">停用</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:enable">
						<a id="start" href="javascript:stopFlg(0);void(0)"
							class="easyui-linkbutton"
							data-options="iconCls:'icon-database_start',disabled:'true'">启用</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:cancellation">
						<a id="zhuxiao" href="javascript:zhuxiao();void(0)"
							class="easyui-linkbutton"
							data-options="iconCls:'icon-database_delete',disabled:'true'">注销账号</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:freeze">
						<a id="freeze" href="javascript:freezeAccount();void(0)"
							class="easyui-linkbutton"
							data-options="iconCls:'icon-save',disabled:'true'">冻结</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:unfreeze">
						<a id="unfreeze" href="javascript:unfreezeAccount();void(0)"
							class="easyui-linkbutton"
							data-options="iconCls:'icon-save',disabled:'true'">解冻</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:clean">
						<a id="jieqing" href="javascript:total();void(0)"
							class="easyui-linkbutton"
							data-options="iconCls:'icon-bin_empty',disabled:'true'">结清账户</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:receivables">
						<a id="save" href="javascript:submit(0);void(0)"
							class="easyui-linkbutton"
							data-options="iconCls:'icon-database_save',disabled:'true'">收款</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:return">
						<a id="back" href="javascript:backMoney();void(0)"
							class="easyui-linkbutton"
							data-options="iconCls:'icon-save',disabled:'true'">返还</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:make">
						<a id="buda" href="javascript:latePlay();void(0)"
							class="easyui-linkbutton"
							data-options="iconCls:'icon-save',disabled:'true'">补打</a>
					</shiro:hasPermission>
					<a id="clear" href="javascript:clear();void(0)"
						class="easyui-linkbutton"
						data-options="iconCls:'icon-clear',disabled:'true'">清屏</a>
					<shiro:hasPermission name="${menuAlias}:function:edit">
						<a id="password" href="javascript:updatePwd();void(0)"
							class="easyui-linkbutton"
							data-options="iconCls:'icon-database_key',disabled:'true'">修改密码</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:save">
						<a id="add" href="javascript:addAccount();void(0)"
							class="easyui-linkbutton"
							data-options="iconCls:'icon-database_add',disabled:'true'">添加账户</a>
					</shiro:hasPermission>
				</div>
<!--				<fieldset>-->
<!--					<legend>账户信息</legend>-->
				
					<table id="table" class="tableCss" style="width:100%;">
						<tr>
							<td class="tableLabel">
								就诊卡号：
							</td>
							<td style="width: 150px;">
								<input class="easyui-textbox" id="idcardId"
									name="account.idcard.idcardNo" data-options="required:true"	missingMessage="请输入卡号" 	/>
							</td>
							<td class="tableLabel">
								姓名：
							</td>
							<td style="width: 150px;" id="patientName"></td>
							<td class="tableLabel">
								性别：
							</td>
							<td style="width: 150px;" id="patientSex"></td>
							<td class="tableLabel">
								证件类型：
							</td>
							<td id="patientCertificatestype"></td>
						</tr>
						<tr>
							<td class="tableLabel">
								电话：
							</td>
							<td id="patientPhone"></td>
							<td class="tableLabel">
								出生年月：</td>
							<td style="width: 150px" id="patientBirthday"></td>
							<td class="tableLabel">民族：</td>
							<td id="patientNation"></td>
							<td class="tableLabel">证件号：</td>
							<td id="patientCertificatesno"></td>
						</tr>
						<tr>
							<td class="tableLabel">籍贯：</td>
							<td id="patientNativeplace"></td>
							<td class="tableLabel">医保号：</td >
							<td id="patientHandbook" colspan="5"></td>
						</tr>
						<tr>
							<td class="tableLabel">账户名称：</td>
							<td>
								<input class="easyui-textbox" id="accountNameV"
									name="account.accountName" data-options="required:true"
									disabled="disabled" missingMessage="请输入账户名称" />
							</td>
							<td class="tableLabel">账户密码：</td>
							<td>
								<input class="easyui-textbox" type="password"
									id="accountPasswordV" name="account.accountPassword"
									data-options="required:true" disabled="disabled"
									missingMessage="请输入账户密码" />
							</td>
<!-- 							<td class="tableLabel">单日消费限额：</td> -->
<!-- 							<td> -->
<!-- 								<input class="easyui-textbox" id="accountDaylimitV" -->
<!-- 									name="account.accountDaylimit"  -->
<!-- 									disabled="disabled" /> -->
<!-- 							</td> -->
							<td class="tableLabel">单日消费限额：</td>
							<td>
								<input class="easyui-numberbox" id="accountDaylimitV"
									name="account.accountDaylimit" 
									disabled="disabled" />
							</td>
							<td class="tableLabel">账户备注：</td>
							<td>
								<input class="easyui-textbox" id="accountRemarkV"
									name="account.accountRemark" data-options="required:true"
									disabled="disabled" missingMessage="请输入备注" style="width: 55%"/>
							</td>
						</tr>
					</table>
<!--				</fieldset>-->
				<br>
<!--				<fieldset>-->
				<div id="flexible">
					<form id="d2">
					<table class="tableCss" style="width:100%;">
						<tr>
							<td class="tableLabel"> 支付方式：</td>
							<td style="width:150px;">
								<input class="easyui-combobox" id="detailPaytype"
									name="repaydetail.detailPaytype"
									missingMessage="请选择支付方式" 
									editable="false"/>
							</td>
							<td class="tableLabel">金额：</td>
							<td>
								<input id="detailDebitamount"
									name="repaydetail.detailDebitamount" class="easyui-numberbox"
									data-options="required:true,precision:2"
									value="${repaydetail.detailDebitamount}"
									missingMessage="请输入金额" />
							</td>
						</tr>

						<tr id="cheque" style="display: none">
							<td class="tableLabel" >开户单位：</td>
							<td>
								<input class="easyui-textbox" id="detailBankunit"
									name="repaydetail.detailBankunit" 
									value="${account.accountBalance }" data-options="required:true"
									 missingMessage="请输入开户单位" />
							</td>
							<td class="tableLabel">开户银行：</td>
							<td>
								<input class="easyui-combobox" id="CodeBank" name="repaydetail.detailBank"
									data-options="required:true"  style="width: 150px"
									missingMessage="请输入开户银行"  onkeydown="KeyDown(0,'CodeBank')" editable="false"/>
							</td>
						</tr>
						<tr id="cheque1" style="display: none">
							<td class="tableLabel" >银行账号：</td>
							<td >
								<input class="easyui-textbox" id="detailBankaccount"
									name="repaydetail.detailBankaccount"
									value="${account.accountBalance }" data-options="required:true"
									missingMessage="请输入银行账号" />
							</td>
							<td class="tableLabel">小票：</td>
							<td>
								<input class="easyui-textbox" id="detailBankbillno"
									name="repaydetail.detailBankbillno"
									value="${account.accountFrozencapital }"
									data-options="required:true"
									missingMessage="请输入小票" />
							</td>
						</tr>
						<tr id="banck">
							<td class="tableLabel">实缴：</td>
							<td>
<!-- 								<input class="easyui-textbox" id="totalMoney" onkeyup="sumMoney()" /> -->
								<input class="easyui-numberbox" id="totalMoney" data-options="precision:2"/>
							</td>
							<td class="tableLabel">找回：</td>
							<td>
								<input class="easyui-numberbox" id="banckMoney" data-options="precision:2" readonly="readonly" />
							</td>
						</tr>
						<!--
		    			<tr>
							
		    			</tr>
		    			<tr>
							<td class="tableLabel">冻结时间：</td>
							<td class="honry-info"><input id="accountFrozentime" name="account.accountFrozentime" class="easyui-datebox" data-options="required:true" value="${account.accountFrozentime}" style="width:90%;" missingMessage="请选择冻结时间"/></td>
						</tr>
						<tr>					
							<td class="tableLabel">解冻时间:</td>
			    			<td class="honry-info"><input class="easyui-datebox" id="accountUnfrozentime" name="account.accountUnfrozentime" value="${account.accountUnfrozentime }" data-options="required:true" style="width:90%;" missingMessage="请选择解冻时间"/></td>
		    			</tr>
		    			<tr>
							<td class="tableLabel">备注:</td>
			    			<td class="honry-info"><input class="easyui-textbox" id="accountRemark" name="account.accountRemark" value="${account.accountRemark }" data-options="multiline:true,required:true" style="width:95%;height:60px;"  missingMessage="请输入备注"/></td>
						</tr>
		    	-->
					</table>
					</form>
<!--				</fieldset>-->
				<br>
					<div id="ui" class="easyui-tabs" style="width:100%;">
						<div title="预存金">
								<table id="list" style="width:100%;height:320px;"
									class="easyui-datagrid"
									data-options="method:'post',rownumbers:true,idField: 'id',border:true,singleSelect:false,pagination:true">
									<thead>
										<tr>
											<th field="ck" checkbox="true"></th>
											<th data-options="field:'detailDebitamount'" style="width: 8%">
												充值金额
											</th>
											<th data-options="field:'detailCreditamount'" style="width: 8%">
												消费金额
											</th>
											<th data-options="field:'detailPaytype',formatter:functionPayType" style="width: 10%">
												支付方式
											</th>
											<th data-options="field:'detailBankunit'" style="width: 15%">
												开户单位
											</th>
											<th data-options="field:'detailBank'" formatter="functionBank" style="width: 15%">
												开户银行
											</th>
											<th data-options="field:'detailBankaccount'" style="width: 15%">银行账号</th>
											<th data-options="field:'detailOptype',formatter:function(val,row){
														if (val == 1){
															return '付款';
														} else if (val == 2){
															return '返还';
														}else if (val == 3){
															return '补打';
														}else if (val == 4){
															return '结清账户';
														}
													}" style="width: 10%">
												类型
											</th>
											<th data-options="field:'detailAccounttype',formatter:function(val,row){
														if (val == 1){
															return '门诊';
														} else if (val == 2){
															return '住院';
														}
													}" style="width: 10%">
												消费类型
											</th>
										</tr>
									</thead>
								</table>
						</div>
						<div title="历史预存金">
							<jsp:include page="../../patient/account/historyAccountList.jsp"></jsp:include>
						</div>
						<div title="就诊卡信息">
							<jsp:include page="../../patient/account/idcardList.jsp"></jsp:include>
						</div>
					</div>
				</div>
				<!--
			    <div style="text-align:center;padding:5px">
			    	<a href="javascript:submit();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			    	<a href="javascript:clear();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
			    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
			    </div>
			    -->
			</form>
		</div>
		
	<!-- 添加框 -->
	<jsp:include page="../../patient/account/updatePassword.jsp"></jsp:include>
	</div>
	
<!--javaScript  脚本集	-->

	<script type="text/javascript">	
		var sexList = "";
		var countryList = "";	
		var certiList = "";
		var nationList = "";
		//预存金开户银行
		var payTypeMap="";
		//支票开户银行
		var bankMap="";
		var payTypeArray=null;
		var sexMap=new Map();
		/*******************************开始读卡***********************************************/
		//定义一个事件（读卡）
		function read_card_ic(){
			var card_value = app.read_ic();
			if(card_value=='0'||card_value==undefined||card_value=='')
			{
				$.messager.alert('提示','此卡号['+card_value+']无效');
				return;
			}
			$("#idcardId").textbox("setValue",card_value);
			getPatient();
		};
		/*******************************结束读卡***********************************************/
		/**
		 * 页面初始化
		 * @author  
		 * @date 
		 * @version 
		 */		
		$(function(){
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
			var winH=$("body").height();
			$('#flexible').height(winH-78-30-27-22);
// 			alert();
// 			var flexible=$('#flexible').height(editForm-$('#table').height());
// 			$('list').height(flexible-$('#d2').height());
			
			
			
			
			
			$('#modifyPwd').window('close');  // close a window
			
			//支付方式下拉框
			$('#detailPaytype').combobox({    
				data:getPayTypData(payTypeVal),
				valueField:'id',    
				textField:'value',
				multiple:false,
				required:true
			});
			bindEnterEvent('idcardId',getPatient,'easyui');
				
		/**
		 * 支付方式下拉列表弹框
		 * @author  lt
		 * @date 2015-6-17
		 * @version 1.0
		 */	
			$('#detailPaytype').combobox({    
			    onSelect:function(record){
			    	if(record.value == "支票"){
			    		$("#banck").hide();
				    	$("#cheque").show(); 
				    	$("#cheque1").show();
				    	//初始化下拉框
						idCombobox("CodeBank");
						$("#totalMoney").textbox('setValue',"");
				    	$("#banckMoney").textbox('setValue',"");
			    	}else if(record.value == "现金"){
			    		$("#banck").show();
			    		$("#cheque").hide(); 
				    	$("#cheque1").hide();
				    	$("#detailBankunit").textbox('setValue',"");
				    	$("#CodeBank").combobox("setValue","");
				    	$("#detailBankaccount").textbox('setValue',"");
				    	$("#detailBankbillno").textbox('setValue',"");
			    	}else{
			    		$("#banck").hide();
			    		$("#cheque").hide(); 
				    	$("#cheque1").hide();
				    	$("#detailBankunit").textbox('setValue',"");
				    	$("#CodeBank").combobox("setValue","");
				    	$("#detailBankaccount").textbox('setValue',"");
				    	$("#detailBankbillno").textbox('setValue',"");
				    	$("#totalMoney").textbox('setValue',"");
				    	$("#banckMoney").textbox('setValue',"");
			    	}
			    	
			    	/*if(value == "支票"){
						$('#chequePay').dialog({
						    title:"支付信息", 
							minimizable:true,
							maximizable:true,
							modal:true
						});
			    	} */
			    }   
			});
			$.ajax({
				url: "<c:url value='/comboBox.action'/>",
				data:{"str" : "CodeCertificate"},
				type:'post',
				success: function(certidata) {
					certiList = eval("("+ certidata +")");
				}
			});
			$.ajax({
				url: "<c:url value='/comboBox.action'/>",
				data:{"str" : "CodeNationality"},
				type:'post',
				success: function(nationdata) {
					nationList = eval("("+ nationdata +")");
				}
			});
			
			//计算，自动找零
			$('#totalMoney').numberbox('textbox').bind('keyup', function(event) {
				//实缴
				var totalMoneys= $('#totalMoney').numberbox('getText');
				var totalMoney= totalMoneys*100;
				//金额
				var detailDebitamounts = $('#detailDebitamount').numberbox('getText');
				var detailDebitamount = detailDebitamounts*100;
				//找回
				var banckMoneys = totalMoney-detailDebitamount;
				var banckMoney = banckMoneys/100;
				$('#banckMoney').numberbox('setText',banckMoney);
			});
			
			 
			//页面初始化禁用控件
			$('#detailPaytype').combobox({    
				   disabled:true  
			});
			$('#detailDebitamount').numberbox({    
			   disabled:true  
			});
			$('#totalMoney').numberbox({    
			   disabled:true  
			});
			$('#banckMoney').numberbox({    
			   disabled:true  
			});
			
		});
		
// 		过滤支付方式(院内账户)
		function getPayTypData(payTypeArray){
			var rev = "[";
			$.each(payTypeArray,function(i,n){
				if(n.id!="4"){
					if(rev.length>1){
						rev+= ",";
					}
					rev +="{'id':'"+n.id+"','value':'"+n.value+"'}"; 
				}
			});
			rev+="]"
			return eval("("+ rev +")");
		} 
		 
		 
//		渲染预存金支付方式
		function functionPayType(value,row,index){
			if(value!=null&&value!=''){
				return paywayMap.get(value);
			}
		}	
		
//		渲染支票开户银行
		function functionBank(value,row,index){
			if(value!=null&&value!=''){
				return bankMap[value];
			}
		}	
		
		
		//从xml文件中解析，读到下拉框
		function idCombobox(param){
			$('#'+param).combobox({
			    url:"<c:url value='/comboBox.action'/>?str="+param,   
			    valueField:'id',
			    textField:'name',
			    multiple:false
			});
		}
		
		
		//下拉框的keydown事件   调用弹出窗口
		/* var CodeBankkeydown = $('#CodeBank').combobox('textbox'); 
		CodeBankkeydown.keyup(function(){
			KeyDown(0,"CodeBank");
		}); */
		
		
		function KeyDown(flg,tag){ 	    	
	    	if(flg==1){//回车键光标移动到下一个输入框
		    	if(event.keyCode==13){	
		    		event.keyCode=9;
		    	}
		    }
		    if(flg==0){	//空格键打开弹出窗口
			    if (event.keyCode == 32)  
			    { 
			        event.returnValue=false;  
			        event.cancel = true; 
			        if(tag=="CodeMedicaltype"){
			        	showWin("银行","<c:url value='/ComboxOut.action'/>?xml="+"CodeBank,0","50%","80%");
			        }
			    }
		    }
		} 
		
		/**
		 * 弹出框
		 * @author  lt
		 * @date 2015-06-29
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
		
		/**
		 * 表单提交
		 * @author  lt
		 * @date 2015-6-1
		 * @version 1.0
		 */
		var record = new Array(); //用来记录返还过的 条款id；
		function submit(repaydetailId){
		//repaydetailId==0为收款操作  不等于0 为返还操作
			if(repaydetailId != 0){
				$('#detailPaytype').combobox({    
				   required: false 
				});  
				$('#detailDebitamount').numberbox({    
				    required: false  
				});
			}else{
				$("#detailOptype").val(1);
			}
			if($('#detailPaytype').combobox("getText")!="支票"){
				$('#detailBankunit').textbox({    
				    required: false  
				});
				$('#CodeBank').combobox({    
				    required: false  
				});
				$('#detailBankaccount').textbox({    
				    required: false  
				});
				$('#detailBankbillno').textbox({    
				    required: false  
				});
			}
			$('#editForm').form('submit',{                                                             
			        url:"<c:url value='/patient/account/editAccountDetail.action'/>?repaydetailId="+repaydetailId+"&type="+'402880a54e3e0568014e3e06b3580001', 
			        onSubmit:function(){ 
						var detailDebitamount="";//金额
			        	var detailBankunit="";//开户单位
			        	var detailBankaccount="";//银行账号
			        	var detailBankbillno="";//小票
			        	detailDebitamount=$("#detailDebitamount").textbox('getValue'); 
			        	detailBankunit=$("#detailBankunit").textbox('getValue'); 
			        	detailBankaccount=$("#detailBankaccount").textbox('getValue'); 
			        	detailBankbillno=$("#detailBankbillno").textbox('getValue'); 
			        	if($("#detailOptype").val()==1){
			            	if($('#detailPaytype').combobox("getText")==""){
			            		$.messager.alert('提示',"请选择支付方式!");
					            return false;
			            	}
				         	if($('#detailPaytype').combobox("getText")!="支票"){
				            	if(detailDebitamount=="") {
				            		$.messager.alert('提示',"请填写金额!");
					            	return false;
					            }
				            }else{
					            if(detailDebitamount=="") {
					            	$.messager.alert('提示',"请填写金额!");
					            	return false;
					            }
					            if(detailBankunit=="") {
					            	$.messager.alert('提示',"请填写开户单位!");
					            	return false;
					            }
					            if($('#CodeBank').combobox("getText")=="") {
					            	$.messager.alert('提示',"请选择开户银行!");
					            	return false;
					            }
					            if(detailBankaccount=="") {
					            	$.messager.alert('提示',"请填写银行账号!");
					            	return false;
					            }
				            	if(detailBankbillno=="") {
				            		$.messager.alert('提示',"请填写小票!");
					            	return false;
					            }
				            }
				         }
			            return true; 
			        },  
			        success:function(data){
			        	if(data=="yes"){
				        	if($("#detailOptype").val()==2){
				        		record.push(repaydetailId);
				        	}
				        	$.messager.alert('提示','保存成功！');    
				        	
				        	$.messager.confirm('确认','是否打印发票？',function(r){    
				        	    if (r){  
				        	        var timerStr = Math.random();
				        	        var hospital='郑州大学第一附属医院';
				        	        //当前功能尚未完善，门诊与住院  预交金没有分离
				        	        //门诊预交金票据
				        	  		window.open ("<c:url value='/iReport/iReportPrint/iReportForOutpatientPrepaid.action?randomId='/>"+timerStr+"&tid= "+$('#idcardId').val()+"&hospital="+ encodeURIComponent(encodeURIComponent(hospital))+"&fileName=outpatient_Prepaid",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no'); 
				        	  		
				        	        //住院预交金票据
				        	       // window.open ("<c:url value='/iReport/iReportPrint/iReportForOutpatientPrepaid.action?randomId='/>"+timerStr+"&tid= "+$('#idcardId').val()+"&hospital="+ encodeURIComponent(encodeURIComponent(hospital))+"&fileName=YJJDY",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no'); 
				        	    }    
				        	}); 
				        	
				        	
				        	$('#ui').tabs({ selected:0 });
				        	$('#list').datagrid('reload');
				        	
					    	
					    	$("#d2").form("clear");
					    	//支付方式
					    	$("#detailPaytype").combobox("clear");
					    	//金额
				        	$("#detailDebitamount").numberbox("reset");
					    	//开户单位
						    $("#detailBankunit").textbox({required: true});
						    $("#detailBankunit").textbox('setValue',"");
						    //银行帐号
					    	$("#detailBankaccount").textbox({required: true});
					    	$("#detailBankaccount").textbox('setValue',"");
							//小票
					    	$("#detailBankbillno").textbox({required: true});
					    	$("#detailBankbillno").textbox('setValue',"");
					    	//实缴
					    	$("#totalMoney").textbox('setValue',"");
					    	//找回
					    	$("#banckMoney").textbox('setValue',"");
					    	


					    	$('#detailBankunit').textbox({    
							    required: true  
							});
							$('#CodeBank').combobox({  
							    required: true  
							});
							$('#detailBankaccount').textbox({    
							    required: true  
							});
							$('#detailBankbillno').textbox({    
							    required: true  
							});
							$("#cheque").hide(); 
					    	$("#cheque1").hide();
					    	
					    	//返还完的条目高亮显示
				        	$('#list').datagrid({
								rowStyler: function(index,row){
									for(var i=0;i<record.length;i++){
										if(row.id == record[i]){
											return 'background-color:#6293BB;color:#fff;';
										}
									}
								}
							});
			        	}else{
			        		$.messager.alert('提示',"补打失败，请确认是否分配发票号段！");	
			        	}
			        },
					error : function(data) {
						$.messager.alert('提示',"保存失败！");	
					}			         
			    }); 
		}
		
		/**
		 * 清除页面填写信息
		 * @author  lt
		 * @date 2015-6-17 18:06
		 * @version 1.0    
		 */
		function clear(){
// 			window.location="<c:url value='/patient/account/addAccount.action'/>?menuAlias=${menuAlias}";
			window.location="<%=basePath%>patient/account/addAccount.action?menuAlias=${menuAlias}&detailAccounttype=${detailAccounttype}";
		}
		
		/**
		 * 获取患者基本信息
		 * @author  lt
		 * @date 2015-6-8 10:53
		 * @version 1.0
		 */
		function getPatient() {
// 			var a=encodeURI(encodeURI($('#idcardId').val()));
// 			clear();
			$.ajax({
						url: "<c:url value='/patient/idcard/getPatientByIdcard.action'/>?id="+encodeURI(encodeURI($('#idcardId').val())),//通过ajax查询出树节点id,这里因为也可以输入汉字来查询科室名称,所以需要转码
// 						url: "<c:url value='/patient/idcard/getPatientByIdcard.action'/>?id="+a,//通过ajax查询出树节点id,这里因为也可以输入汉字来查询科室名称,所以需要转码
						type:'post',
						success: function(data) {
					    	//支付方式
					    	$("#detailPaytype").combobox("clear");
					    	//金额
				        	$("#detailDebitamount").numberbox("reset");
					    	//开户单位
						    $("#detailBankunit").textbox({required: true});
						    $("#detailBankunit").textbox('setValue',"");
						   	//开户银行
						   	$("#CodeBank").combobox("clear");
						    //银行帐号
					    	$("#detailBankaccount").textbox({required: true});
					    	$("#detailBankaccount").textbox('setValue',"");
							//小票
					    	$("#detailBankbillno").textbox({required: true});
					    	$("#detailBankbillno").textbox('setValue',"");
					    	//实缴
					    	$("#totalMoney").textbox('setValue',"");
					    	//找回
					    	$("#banckMoney").textbox('setValue',"");
						
						$("#d2").form("clear");
						
						
						
							var dataObj = eval("("+data+")");
							if(dataObj != null&&dataObj != ""){
								viewPatientValue(dataObj);
								if(dataObj.stop_flg!=1){
									if(dataObj.account.id!="no"){
										viewAccountValue(dataObj);
										if(dataObj.account.stop_flg!="1"){
//										$.messager.alert(dataObj.account.stop_flg!="1");
											if(dataObj.account.accountState==4){
												$.messager.alert("操作提示", "此账户已被冻结！","warning");
												$('#unfreeze').linkbutton('enable');
											}else{
												$('#stop').linkbutton('enable');
												$('#freeze').linkbutton('enable');
												$('#jieqing').linkbutton('enable');
												$('#zhuxiao').linkbutton('enable');
												$('#save').linkbutton('enable');
												$('#back').linkbutton('enable');
												$('#buda').linkbutton('enable');
												$('#password').linkbutton('enable');
												$('#clear').linkbutton('enable');
												$('#add').linkbutton('disable');
												$('#detailDebitamount').removeAttr("disabled");
												$('#detailPaytype').combobox({    
												   disabled:false  
												});  
												$('#detailDebitamount').numberbox({    
												    required: true  
												});
												$('#accountNameV').textbox({   
													disabled:true, 
												    required: false  
												});
												$('#accountPasswordV').textbox({    
													disabled:true, 
												    required: false  
												});
												$('#accountRemarkV').textbox({   
													disabled:true,  
												    required: false  
												});
												$('#accountDaylimitV').textbox({    
													disabled:true, 
												    required: false  
												});
												
												//启用控件
												$('#detailPaytype').combobox({    
													   disabled:false
												});
												$('#detailDebitamount').numberbox({    
												   disabled:false
												});
												$('#totalMoney').numberbox({    
												   disabled:false
												});
												$('#banckMoney').numberbox({    
												   disabled:false 
												});
											}
											
										}else{
											$.messager.alert("操作提示", "此账户已被停用！","warning");
											$('#start').linkbutton('enable');
											$('#detailDebitamount').attr("disabled","disabled");
											$('#detailPaytype').combobox({    
											   disabled:true  
											});  
											$('#detailDebitamount').numberbox({    
											    required: false  
											});
										}
												
										//重新载入List列表
										reloadList(dataObj);
										
										/* //返还完的条目高亮显示
							        	$('#list').datagrid({
											rowStyler: function(index,row){
												for(var i=0;i<record.length;i++){
													if(row.id == record[i]){
														return 'background-color:#6293BB;color:#fff;';
													}
												}
											}
										}); */
										
									}else{
										$.messager.alert('提示',"此卡还没有账户，请为此卡添加账户。");
										$('#add').linkbutton('enable');
										$('#stop').linkbutton('disable');
										$('#freeze').linkbutton('disable');
										$('#jieqing').linkbutton('disable');
										$('#zhuxiao').linkbutton('disable');
										$('#save').linkbutton('disable');
										$('#back').linkbutton('disable');
										$('#buda').linkbutton('disable');
										$('#password').linkbutton('disable');
										$('#clear').linkbutton('disable');
										
										$('#accountNameV').textbox({    
										   required: true,  
										   value:'',
											disabled:false
										});
										$('#accountPasswordV').textbox({    
										   required: true,  
										   value:'',
											disabled:false
										});
										$('#accountRemarkV').textbox({    
										    required: true,  
										    value:'',
											disabled:false
										});
										$('#accountDaylimitV').textbox({    
										    required: true,  
										    value:'',
											disabled:false
										});
										$('#detailDebitamount').attr("disabled","disabled");
										
										
										$('#detailDebitamount').numberbox({    
										    required: false  
										});
										
										//控件
										$('#detailPaytype').combobox({    
										   disabled:true  
										});
										$('#detailDebitamount').numberbox({    
										   disabled:true  
										});
										$('#totalMoney').numberbox({    
										   disabled:true  
										});
										$('#banckMoney').numberbox({    
										   disabled:true  
										});
											
											
										$('#detailBankunit').textbox({    
										    required: false  
										});
										$('#CodeBank').combobox({   
										    required: false  
										});
										$('#detailBankaccount').textbox({    
										    required: false  
										});
										$('#detailBankbillno').textbox({    
										    required: false  
										});
										$('#accountDaylimitV').removeAttr("disabled");
										
										clearAccountValue();
										reloadList(dataObj);
										return;
									}
								}else{
									$.messager.alert('提示',"此就诊卡已挂失，不能操作此卡。");
								}
							}else{
								clearPatientValue();
								clearAccountValue();
								$.messager.alert('提示',"请输入正确的就诊卡号！");
								reloadList();
								return;
							}
						}
			});	
		
		} 
		
	/**
	 * 载入list列表	
	 * @author  
	 * @date 
	 * @version 
	 */	
	function reloadList(dataObj){
		//支票开户银行渲染
		$.ajax({
			url: "<c:url value='/patient/account/queryBankMapComboboxs.action'/>",
			type:'post',
			success: function(bankData) {
				bankMap = eval("("+bankData+")");
				//显示预存金
				$('#list').datagrid({    
				    url:"<c:url value='/patient/account/queryRepayDetail.action'/>?menuAlias=${menuAlias}&ishis=0&accountId=" + dataObj.account.id+"&detailAccounttype=${detailAccounttype}",
				    onBeforeLoad:function(){
						$('#list').datagrid('clearChecked');
					},
				    rowStyler: function(index,row){
				    var data = $('#list').datagrid('getData');
					record = data.str;
					    if(record!=null){
					   		for(var i=0;i<record.length;i++){
								if(row.id == record[i]){
									return 'background-color:#6293BB;color:#fff;';
								}
							}
					    }
					}
				}); 
			}
		});	
		
		$("#hislist").datagrid('options').url = "<c:url value='/patient/account/queryRepayDetail.action'/>?menuAlias=${menuAlias}&ishis=1&accountId=" + dataObj.account.id+"&detailAccounttype=${detailAccounttype}";//加载list
		//$.messager.alert(title+' is selected');
		$('#hislist').datagrid('reload');    // 重新载入当前页面数据  
		$("#idcardList").datagrid('options').url = "<c:url value='/patient/idcard/queryIdCardByAccount.action'/>?menuAlias=${menuAlias}&detailAccounttype=1&idcardNO=" + encodeURI(encodeURI($('#idcardId').val()));//加载list
		//$.messager.alert(title+' is selected');
		$('#idcardList').datagrid('reload');    // 重新载入当前页面数据  
		$('#ui').tabs({    
		    border:false,
		    onSelect:function(title){
		    	if(title == "预存金"){
		    		$('#back').linkbutton('enable');
		    		$('#buda').linkbutton('enable');
			    	$("#list").datagrid('options').url = "<c:url value='/patient/account/queryRepayDetail.action'/>?menuAlias=${menuAlias}&ishis=0&accountId=" + dataObj.account.id+"&detailAccounttype=${detailAccounttype}";//加载list
					//$.messager.alert(title+' is selected');
					$('#list').datagrid('reload');    //重新载入当前页面数据
					var data =$('#list').datagrid('getData');
		    	} 
		    	if(title == "历史预存金"){
		    		$('#back').linkbutton('disable');
		    		$('#buda').linkbutton('disable');
			    	$("#hislist").datagrid('options').url = "<c:url value='/patient/account/queryRepayDetail.action'/>?menuAlias=${menuAlias}&ishis=1&accountId=" + dataObj.account.id+"&detailAccounttype=${detailAccounttype}";//加载list
					//$.messager.alert(title+' is selected');
					$('#hislist').datagrid('reload');    // 重新载入当前页面数据  
					
		    	}  
		    	if(title == "就诊卡信息"){
		    		$('#back').linkbutton('disable');
		    		$('#buda').linkbutton('disable');
			    	$("#idcardList").datagrid('options').url = "<c:url value='/patient/idcard/queryIdCardByAccount.action'/>?menuAlias=${menuAlias}&idcardNO=" + encodeURI(encodeURI($('#idcardId').val()));//加载list
					//$.messager.alert(title+' is selected');
					$('#idcardList').datagrid('reload');    // 重新载入当前页面数据  
		    	}   
		    }  
		});
	}
	
	/**
	 * 个人信息数据填充
	 * @author  lt
	 * @date 2015-6-9 10:53
	 * @version 1.0
	 *	就诊号，姓名，性别，证件类型，证件号，电话，出生年月，民族，籍贯，医保号
	 */
	function viewPatientValue(obj){
		$('#patientName').text(obj.patient.patientName);
	    $("#patientSex").text(sexMap.get(obj.patient.patientSex));
	    $("#patientBirthday").text(obj.patient.patientBirthday);	
	    $("#patientCertificatesno").text(obj.patient.patientCertificatesno);
	    $("#patientCertificatestype").text(certiFamater(obj.patient.patientCertificatestype));	
	    $("#patientNation").text(nationFamater(obj.patient.patientNation));
	    $("#patientPhone").text(obj.patient.patientPhone);
	    $("#patientNativeplace").text( obj.patient.patientNativeplace);	
	    $("#patientHandbook").text(obj.patient.patientHandbook);
	    $("#patientMedicalrecorde").val(obj.patient.medicalrecordId);
	}
	
	/**
	 *	账户名，密码，单日消费，备注
	 */
	function viewAccountValue(obj){
		$("#accountNameV").textbox('setValue',obj.account.accountName);
	    $("#accountId").val(obj.account.id);
	    $("#accountRemarkV").textbox('setValue',obj.account.accountRemark);
	    $("#accountPasswordV").textbox('setValue',obj.account.accountPassword);
	    $("#accountDaylimitV").textbox('setValue',obj.account.accountDaylimit);
	}
	
	/**
	 * 清空个人信息数据
	 * @author  lt
	 * @date 2015-6-9 10:53
	 * @version 1.0
	 */
	function clearPatientValue(){
		$('#patientName').text("");
	    $("#patientSex").text("");
	    $("#patientBirthday").text("");	
	    $("#patientCertificatesno").text("");
	    $("#patientCertificatestype").text("");	
	    $("#patientNation").text("");
	    $("#patientPhone").text("");
	    $("#patientNativeplace").text( "");	
	    $("#patientHandbook").text("");
	}
	function clearAccountValue(){
		$("#accountNameV").textbox('setValue',"");
	    $("#accountRemarkV").textbox('setValue',"");
	    $("#accountPasswordV").textbox('setValue',"");
	    $("#accountDaylimitV").textbox('setValue',"");
	}
	
	/**
	 * 修改密码
	 * @author  lt
	 * @date 2015-6-8 10:53
	 * @version 1.0
	 */
	function updatePwd(){
		$('#modifyPwd').dialog({
		    title:"修改密码", 
			minimizable:true,
			maximizable:true,
			modal:true
		});
	}
	
	/**
	 * 添加账号
	 * @author  lt
	 * @date 2015-6-10 
	 * @version 1.0
	 */
	function addAccount(){
		$('#editForm').form('submit',{  
		        url:"<c:url value='/patient/account/editAccount.action'/>",  
		        onSubmit:function(){ 
		        	var name="";
		        	var password="";
		        	var money="";
		        	var remark="";
		        	name=$("#accountNameV").textbox('getValue'); 
		        	password=$("#accountPasswordV").textbox('getValue'); 
		        	money=$("#accountDaylimitV").textbox('getValue'); 
		        	remark=$("#accountRemarkV").textbox('getValue'); 
		            if(name=="") {
		            	$.messager.alert('提示',"请填写账户名称!");
		            	return false;
		            }
		            if(password=="") {
		            	$.messager.alert('提示',"请填写账户密码!");
		            	return false;
		            }
		            if(money=="") {
		            	$.messager.alert('提示',"请填写单日消费限额!");
		            	return false;
		            }
		            if(remark=="") {
		            	$.messager.alert('提示',"请填写账户备注!");
		            	return false;
		            }
					return true;
		        },  
		        success:function(data){ 
		        	$.messager.alert('提示',"保存成功！");
		        	//window.location="<c:url value='/patient/account/addAccount.action'/>?menuAlias="+$('#menuAlias').val()+"&detailAccounttype="+$('#detailAccounttype').val();
		        	getPatient();
		        	
		        },
				error : function(data) {
					$.messager.alert('提示',"保存失败！");	
				}			         
		    }); 
	}
	
	/**
	 * 回车键查询
	 * @author  lt
	 * @param title 标签名称
	 * @param title 跳转路径
	 * @date 2015-05-27
	 * @version 1.0
	 */
	function KeyDown()  
	{  
	    if (event.keyCode == 13)  
	    {  
	        event.returnValue=false;  
	        event.cancel = true;  
	        getPatient();   
	    }  
	} 
	
	/**
	 * 返还
	 * @author  lt
	 * @date 2015-6-9 10:53
	 * @version 1.0
	 */
	function backMoney(){
		var row = $("#list").datagrid("getSelections"); 
		var i = 0;    
		var getid = ""; 
		var getDetailOptype;
		if(row.length!=1){
		    $.messager.alert("操作提示", "请选择一条用户记录！","warning");
		    return null;
		}else{ 
			for(i;i<row.length;i++){ 
				getid = row[i].id;
				getDetailOptype = row[i].detailOptype;
			}
		}
		if(getDetailOptype != 1){
		    $.messager.alert("操作提示", "只能对收款条目进行操作！","warning");
		    return null;
		}
		$("#detailOptype").val(2);
		submit(getid);
	}
	
	/**
	 * 补打
	 * @author  lt
	 * @date 2015-6-19
	 * @version 1.0
	 */
	function latePlay(){
		var row = $("#list").datagrid("getSelections"); 
		var i = 0;    
		var getid = ""; 
		var getDetailOptype ="";
		if(row.length!=1){
		    $.messager.alert("操作提示", "请选择一条用户记录！","warning");
		    return null;
		}else{  
			for(i;i<row.length;i++){ 
				getid = row[i].id;
				getDetailOptype = row[i].detailOptype;
			}
		}
		if(getDetailOptype != 1){
		    $.messager.alert("操作提示", "只能对收款条目进行操作！","warning");
		    return null;
		}
		$("#detailOptype").val(3);
		submit(getid);
	}
	
	/**
	 * 结清账户
	 * @author  lt
	 * @date 2015-6-13
	 * @version 1.0
	 */
	function total(){
		$.ajax({
			url: "<c:url value='/patient/account/checkAccount.action'/>?accountId="+encodeURI(encodeURI($('#accountId').val())),//通过ajax查询出树节点id,这里因为也可以输入汉字来查询科室名称,所以需要转码
			type:'post',
			success: function(data) {
			var dataObj = eval("("+data+")");
				if(dataObj!="yes"){
					$.messager.confirm('确认', '本账户门诊金额为：'+dataObj.clinicBalance+'住院金额为：'+dataObj.inpatientBalance+'，确定结清本账户吗?', function(res){//提示是否结清
					if (res){
						$.ajax({
							url: "<c:url value='/patient/account/totalAccount.action'/>?accountId="+encodeURI(encodeURI($('#accountId').val())),//通过ajax查询出树节点id,这里因为也可以输入汉字来查询科室名称,所以需要转码
							type:'post',
							success: function(data) {
								$.messager.alert('提示',"账户已结清，账户金额为：" + data);
								$('#list').datagrid('reload');
							}
						});	
					}
					});
				}else{
					$.messager.alert("操作提示", "此账户已结清！","warning");
				}
			}
		});	
		
		
	}
	
	/**
	 * 注销账户
	 * @author  lt
	 * @date 2015-6-13
	 * @version 1.0
	 */
	function zhuxiao(){
		
			$.ajax({
					url: "<c:url value='/patient/account/checkAccount.action'/>?accountId="+encodeURI(encodeURI($('#accountId').val())),
					type:'post',
					success: function(data) {
					var dataObj = eval("("+data+")");
						if(dataObj!="yes"){
							$.ajax({
							url: "<c:url value='/patient/account/totalAccount.action'/>?accountId="+encodeURI(encodeURI($('#accountId').val())),
							type:'post',
							success: function(data) {
								$.messager.confirm('确认', '确定注销本账户吗?', function(res){//提示是否结清
								if (res){
									$.ajax({
											url: "<c:url value='/patient/account/zhuxiaoAccount.action'/>?accountId="+encodeURI(encodeURI($('#accountId').val())),//通过ajax查询出树节点id,这里因为也可以输入汉字来查询科室名称,所以需要转码
											type:'post',
											success: function(data) {
												var dataObj = eval("("+data+")");
												if(dataObj == "no"){
													$.messager.alert('提示',"注销失败！");
												}else if(dataObj == "yes"){
													$.messager.alert('提示',"注销成功！");
													window.location="<c:url value='/patient/account/addAccount.action'/>?menuAlias="+$('#menuAlias').val()+"&detailAccounttype="+$('#detailAccounttype').val();
												}
											}
								});
								}
							});
							}
							});
						}else{
							$.messager.confirm('确认', '确定注销本账户吗?', function(res){
								if (res){
									$.ajax({
											url: "<c:url value='/patient/account/zhuxiaoAccount.action'/>?accountId="+encodeURI(encodeURI($('#accountId').val())),//通过ajax查询出树节点id,这里因为也可以输入汉字来查询科室名称,所以需要转码
											type:'post',
											success: function(data) {
												var dataObj = eval("("+data+")");
												if(dataObj == "no"){
													$.messager.alert('提示',"注销失败！");
												}else if(dataObj == "yes"){
													$.messager.alert('提示',"注销成功！");
													window.location="<c:url value='/patient/account/addAccount.action'/>?menuAlias="+$('#menuAlias').val()+"&detailAccounttype="+$('#detailAccounttype').val();
												}
											}
								});
								}
							});
						}
					}
				});	
	}
	
	/**
	 * 启用，停用账户
	 * @author  lt
	 * @date 2015-6-13
	 * @version 1.0
	 */
	function stopFlg(stopFlg){
		var str = "";
			if(stopFlg==0){
				str = "启用";
			}else if(stopFlg==1){
				str = "停用";
			}
		
			$.ajax({
				url: "<c:url value='/patient/account/checkAccount.action'/>?accountId="+encodeURI(encodeURI($('#accountId').val())),//通过ajax查询出树节点id,这里因为也可以输入汉字来查询科室名称,所以需要转码
				type:'post',
				success: function(data) {
					var dataObj = eval("("+data+")");
					if(dataObj == "yes"){
						$.messager.confirm('确认', '确定'+str+'本账户吗?', function(res){//提示是否结清
							if (res){
								$.ajax({
										url: "<c:url value='/patient/account/stopFlgAccount.action'/>?stopFlg="+ stopFlg +"&accountId="+encodeURI(encodeURI($('#accountId').val())),//通过ajax查询出树节点id,这里因为也可以输入汉字来查询科室名称,所以需要转码
										type:'post',
										success: function(data) {
											var dataObj = eval("("+data+")");
											if(dataObj == "no"){
												$.messager.alert('提示',str + "失败！");
											}else if(dataObj == "yes"){
												$.messager.alert('提示',str + "成功！");
												window.location="<c:url value='/patient/account/addAccount.action'/>?menuAlias=${menuAlias}";
											}
										}
							});
							}
						});
					}else{
						$.ajax({
							url: "<c:url value='/patient/account/totalAccount.action'/>?accountId="+encodeURI(encodeURI($('#accountId').val())),//通过ajax查询出树节点id,这里因为也可以输入汉字来查询科室名称,所以需要转码
							type:'post',
							success: function(data) {
								$.messager.confirm('确认', '确定'+str+'本账户吗?', function(res){//提示是否结清
									if (res){
										$.ajax({
												url: "<c:url value='/patient/account/stopFlgAccount.action'/>?stopFlg="+ stopFlg +"&accountId="+encodeURI(encodeURI($('#accountId').val())),//通过ajax查询出树节点id,这里因为也可以输入汉字来查询科室名称,所以需要转码
												type:'post',
												success: function(data) {
													var dataObj = eval("("+data+")");
													if(dataObj == "no"){
														$.messager.alert(str + "失败！");
													}else if(dataObj == "yes"){
														$.messager.alert(str + "成功！");
														window.location="<c:url value='/patient/account/addAccount.action'/>?menuAlias=${menuAlias}";
													}
												}
									});
									}
								});
							}
						});	
					}
					
				}
		});	
	}
	
	/**
	 * 自动显示找零
	 * @author  lt
	 * @date 2015-7-2
	 * @version 1.0
	 */
	function sumMoney(){
// 		var totalMoney = $("#totalMoney").textbox('getValue');
		var totalMoney = $("#totalMoney").val();
		var detailDebitamount = $("#detailDebitamount").val();
		if(totalMoney==null||totalMoney==""){
			$("#banckMoney").textbox('setValue',"");
		}else if(detailDebitamount==null||detailDebitamount==""){
			$("#banckMoney").textbox('setValue',"");
		}else{
			$("#banckMoney").textbox('setValue',totalMoney-detailDebitamount);
		}
	}
	
	/**
	 * 冻结账户
	 * @author  lt
	 * @date 2015-7-2
	 * @version 1.0
	 */
	function freezeAccount(){
		$.messager.confirm('确认', '确定冻结本账户吗?', function(res){
			if (res){
				$.ajax({
					url: "<c:url value='/patient/account/freezeAccount.action'/>?accountId="+encodeURI(encodeURI($('#accountId').val())),
					type:'post',
					success: function(data) {
						var dataObj = eval("("+data+")");
						if(dataObj == "no"){
							$.messager.alert('提示',"冻结失败！");
						}else if(dataObj == "yes"){
							$.messager.alert('提示',"冻结成功！");
							window.location="<c:url value='/patient/account/addAccount.action'/>?menuAlias=${menuAlias}";
						}
					}
				});
			}
		});
	}
	
	/**
	 * 解冻账户
	 * @author  lt
	 * @date 2015-7-2
	 * @version 1.0
	 */
	function unfreezeAccount(){
		$.messager.confirm('确认', '确定解冻本账户吗?', function(res){
			if (res){
				$.ajax({
					url: "<c:url value='/patient/account/unfreezeAccount.action'/>?accountId="+encodeURI(encodeURI($('#accountId').val())),
					type:'post',
					success: function(data) {
						var dataObj = eval("("+data+")");
						if(dataObj == "no"){
							$.messager.alert('提示',"解冻失败！");
						}else if(dataObj == "yes"){
							$.messager.alert('提示',"解冻成功！");
							window.location="<c:url value='/patient/account/addAccount.action'/>?menuAlias=${menuAlias}";
						}
					}
				});
			}
		});
	}
	
	/**
	 * 证件类型列表页 显示
	 * @author  lt
	 * @param title 标签名称
	 * @param title 跳转路径
	 * @date 2015-06-29
	 * @version 1.0
	 */
	function certiFamater(value){
		if(value!=null){
			for(var i=0;i<certiList.length;i++){
				if(value==certiList[i].id){
					return certiList[i].name;
				}
			}
		}
			
	}
	
	/** 民族列表页 显示
	 * @author  lt
	 * @param title 标签名称
	 * @param title 跳转路径
	 * @date 2015-06-29
	 * @version 1.0
	 */
	function nationFamater(value){
		if(value!=null){
			for(var i=0;i<nationList.length;i++){
				if(value==nationList[i].id){
					return nationList[i].name;
				}
			}
		}	
	}
	
</script>
</body>
</html>